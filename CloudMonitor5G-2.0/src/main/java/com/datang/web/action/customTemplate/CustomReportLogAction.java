package com.datang.web.action.customTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zeromq.ZMQ.Socket;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;
import com.datang.domain.customTemplate.MappingIeToKpiPojo;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.customTemplate.CustomLogReportService;
import com.datang.service.customTemplate.CustomTemplateService;
import com.datang.service.report.IReportService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.util.DateUtil;
import com.datang.util.ReadExcelToHtml;
import com.datang.util.ZMQUtils;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.report.BoxInforRequestBean;
import com.datang.web.beans.report.CityInfoRequestBean;
import com.datang.web.beans.report.ReportRequertBean;
import com.datang.web.beans.report.StatisticeTaskRequest;
import com.datang.web.beans.report.TestInfoRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

/**
 * 5G统计任务Action
 * 
 * @author _YZP
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CustomReportLogAction extends PageAction implements
		ModelDriven<CustomLogReportTask> {
	private static Logger LOGGER = LoggerFactory.getLogger(CustomReportLogAction.class);
	/**
	 * 自定义统计任务服务
	 */
	@Autowired
	private CustomLogReportService customLogReportService;
	
	/**
	 * 自定义报表模板服务
	 */
	@Autowired
	private CustomTemplateService customTemplateService;

	/** 多条件查询请求参数 */
	private ReportRequertBean reportRequertBean = new ReportRequertBean();
	/**
	 * 保存统计任务时收集参数
	 */
	private CustomLogReportTask customLogReportTask = new CustomLogReportTask();
	/**
	 * 删除统计任务时收集参数
	 */
	private String ids;
	/**
	 * 查看详情收集参数
	 */
	private Long idLong;
	
	private String templateName;
	
	/**
	 * 要导出的sheet名
	 */
	private String sheetName;

	/**
	 * 跳转到 list界面
	 * 
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到统计结果查看界面
	 */
	public String goSee() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (null != idLong) {
			session.setAttribute("idLong", idLong);
			CustomLogReportTask task = customLogReportService.queryOneByID(idLong);
			List<CustomReportTemplatePojo> queryTemplateByParam = customTemplateService.queryTemplateByParam(new PageList());
			Set<String> unionTeplateSet = new HashSet<String>();
			for (CustomReportTemplatePojo customReportTemplatePojo : queryTemplateByParam) {
				unionTeplateSet.add(customReportTemplatePojo.getTemplateName());
			}
			List<Map<String,Object>> teplateList = new ArrayList<Map<String,Object>>();
			if (unionTeplateSet!=null && unionTeplateSet.size()>0) {
				for (String name : unionTeplateSet) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("valueField",name);
					map.put("textField",name);
					teplateList.add(map);
				}
			}
			ActionContext.getContext().getValueStack().set("reportTemplate", teplateList);
		}
		return "seeReport";
	}

	
	/**
	 * 跳转到自定义报表报表统计界面
	 * @return
	 */
	public String goTemplateExcel() {
		HttpSession session = ServletActionContext.getRequest()
				.getSession();
			session.setAttribute("templateName", templateName);
		return "templateExcel";
	}
	

	/**
	 * 多条件查询任务
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {

		reportRequertBean
				.setCreaterName(customLogReportTask.getCreaterName());
		reportRequertBean.setName(customLogReportTask.getName());
		pageList.putParam("pageQueryBean", reportRequertBean);
		return customLogReportService.pageList(pageList);

	}

	@Override
	public CustomLogReportTask getModel() {
		return customLogReportTask;
	}


	/**
	 * 删除多个统计任务
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delReport() {
		if (null != ids) {
			String[] logIds = ids.trim().split(",");
			List<Long> idss = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				if (StringUtils.hasText(logIds[i])) {
					try {
						idss.add(Long.parseLong(logIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			customLogReportService.delete(idss);
		}

		return ReturnType.JSON;
	}

	
	/**
	 * 输出自定义模板
	 * @author lucheng
	 * @date 2020年12月30日 下午1:13:10
	 * @return
	 */
	public String exportKpiExcel() {
		try {
			Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
			CustomLogReportTask task = customLogReportService.queryOneByID(id);
			if(task!=null && !task.getTaskStatus().equals("2") && !task.getTaskStatus().equals("3")){
				throw new ApplicationException("任务未解析，请等待解析完成");
			}
			
			//查询模板
			PageList selectConditions = new PageList();
			selectConditions.putParam("templateName", templateName);
			List<CustomReportTemplatePojo> queryTemplateByParam = customTemplateService.queryTemplateByParam(selectConditions);
			
			File file = new File(queryTemplateByParam.get(0).getSaveFilePath());
			if (file == null || !file.exists() || file.isDirectory()) {
				throw new ApplicationException("模板文件不存在");
			}
			String newExcelname = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_"+task.getId()
					+ file.getName().substring(file.getName().lastIndexOf("."));
			String newPath = file.getParentFile().getAbsolutePath() + "/" + newExcelname;
			
			String exportFilePath = null;
			Map<String, Object> map = new HashMap<>();
			String idsString = task.getLogIds();
			
			PageList page = new PageList();
			page.putParam("taskName", task.getName());
			page.putParam("logids", idsString);
			page.putParam("templateName", templateName);
			page.putParam("excelPath",queryTemplateByParam.get(0).getSaveFilePath());
			page.putParam("exportFilePath", newPath);
			//根据指标公式修改获取值保存到excel
			exportFilePath = customTemplateService.modifyExcelValue(page);
			
			List<Map<String, String>> readExcelToMap = ReadExcelToHtml.readExcelToMap(exportFilePath, true);

			//修改统计状态
			task.setTaskStatus("3");
			customLogReportService.update(task);
			
			ActionContext.getContext().getValueStack().set("dataList", readExcelToMap);
		} catch (ApplicationException e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "错误:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 自定义任务报表模板导出
	 * 
	 * @return
	 */
	public String downloadTemplateExcelTotal() {
		return "downloadTemplateExcelTotal";
	}

	/**
	 * 自定义任务报表模板导出
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public InputStream getDownloadTemplateExcel() {
		Long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("idLong");
		CustomLogReportTask statisticeTask = customLogReportService.queryOneByID(id);
		PageList selectConditions = new PageList();
		selectConditions.putParam("templateName", templateName);
		List<CustomReportTemplatePojo> queryTemplateByParam = customTemplateService.queryTemplateByParam(selectConditions);
		
		File file = new File(queryTemplateByParam.get(0).getSaveFilePath());
		String newExcelname = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_"+statisticeTask.getId()
				+ file.getName().substring(file.getName().lastIndexOf("."));
		String newPath = file.getParentFile().getAbsolutePath() + "/" + newExcelname;
		File exportfile = new File(newPath);
		
		if(exportfile!=null && exportfile.exists()){
			FileInputStream inputStream = null ;
			try {
				ActionContext.getContext().put("fileName",new String(exportfile.getName().getBytes(),"ISO8859-1"));
				inputStream = new FileInputStream(exportfile);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return inputStream;
		}
		return null;
	}
	
	/**
	 * 获取终端类型
	 * 
	 * 测试目标或者终端类型:0自动LTE ,1单模块商务终端 ,2LTE-FI
	 */
	private String getString(Integer i) {
		if (null != i) {
			if (0 == i) {
				return "自动LTE";
			} else if (1 == i) {
				return "单模块商务终端";
			} else if (2 == i) {
				return "LTE-FI";
			}
		}
		return null;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param the
	 *            ids to set
	 */

	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the id
	 */
	public Long getIdLong() {
		return idLong;
	}

	/**
	 * @param the
	 *            id to set
	 */

	public void setIdLong(Long id) {
		this.idLong = id;
	}

	public CustomLogReportTask getCustomLogReportTask() {
		return customLogReportTask;
	}

	public void setCustomLogReportTask(CustomLogReportTask customLogReportTask) {
		this.customLogReportTask = customLogReportTask;
	}


	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param the
	 *            sheetName to set
	 */

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	
}
