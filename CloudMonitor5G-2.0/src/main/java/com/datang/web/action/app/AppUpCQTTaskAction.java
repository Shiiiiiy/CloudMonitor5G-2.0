package com.datang.web.action.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.Cell5GNbCell;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.projectParam.TdlNbCell;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.taskOrderManage.StationTaskLogNamePojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.oppositeOpen.OppositeOpen3dCompletionShowService;
import com.datang.service.platform.projectParam.IProjectParamService;
import com.datang.service.report.IReportService;
import com.datang.service.stationTest.StationCompletionShowService;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.tools.CreatCQTTaskReport;
import com.datang.tools.DrawGisLteCell;
import com.datang.util.DateUtil;
import com.datang.util.GPSUtils;
import com.datang.util.TextUtil;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.datang.web.action.report.ReportAction;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.report.StatisticeTaskRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

/**
 * APP测发出定点测试任务Action
 * @author lucheng
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class AppUpCQTTaskAction implements ModelDriven<FixedPointTaskOrderPojo> {
	private static Logger LOGGER = LoggerFactory.getLogger(AppUpCQTTaskAction.class);
	
	/**
	 * 区域组服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	/**
	 *  定点测试任务服务
	 */
	@Autowired
	private CQTTaskOrderService fixedPointTaskOrderService;
	
	@Autowired
	private CreatCQTTaskReport creatCQTTaskReport;
	
	/**
	 * Description:终端服务
	 * @author lucheng
	 * @date 下午6:52:39 
	 */
	@Autowired
	private TerminalService terminalService;
	
	@Autowired
	private StationCompletionShowService staionCompletionShowService;
	
	@Autowired
	private OppositeOpen3dCompletionShowService oppositeOpen3dCompletionShowService;
	
	@Autowired
	private IReportService iReportService;
	

	/**
	 * 保存统计任务时收集参数
	 */
	private FixedPointTaskOrderPojo fixedPointTaskOrderPojo = new FixedPointTaskOrderPojo();
	
	@Value("${appTaskReportFileLink}")
	private String fileSaveUrl;
	
	/**
	 * 市级区域ID
	 */
	private Long cityId;
	
	/**
	 * 任务发起时间范围
	 */
	private Date beginDate ;

	private Date endDate ;
	
	//测试工单记录id集合
	private String idStr;
	
	//新增工单id
	private String newWorkOrderid;
	
	/**
	 * 任务测试时间范围
	 */
	private String beginTime ;

	private String endTime ;
	
	
	//日志名称
	private String logNames;
	
	
	/**
	 * APP侧查询定点测试任务工单
	 * @author lucheng
	 * @date 2020年8月19日 下午7:49:20
	 * @return
	 */
	public String queryCQTTaskData() {
		try {		
			if(fixedPointTaskOrderPojo.getBoxId() != null){
				PageList pageList = new PageList();
				pageList.putParam("boxId", fixedPointTaskOrderPojo.getBoxId());
				List<FixedPointTaskOrderPojo> fixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
				ActionContext.getContext().getValueStack().set("data", fixedPointTaskList);
				
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,boxid为空");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "查询发生异常:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * APP发出定点任务状态变化通知
	 * @author lucheng
	 * @date 2020年8月19日 下午1:23:42
	 * @return
	 */
	public String updateCQTTaskState() {
		try {		
			if(fixedPointTaskOrderPojo.getWorkOrderId() != null){
				PageList pageList = new PageList();
				pageList.putParam("workOrderId", fixedPointTaskOrderPojo.getWorkOrderId());
				List<FixedPointTaskOrderPojo> fixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
				if(fixedPointTaskList.size()>0 ){
					FixedPointTaskOrderPojo newfixedPointTask = fixedPointTaskList.get(0);
					if(StringUtils.hasText(fixedPointTaskOrderPojo.getBoxId())){
						newfixedPointTask.setBoxId(fixedPointTaskOrderPojo.getBoxId());
					}
					if(StringUtils.hasText(fixedPointTaskOrderPojo.getTestPointLon())){
						newfixedPointTask.setTestPointLon(fixedPointTaskOrderPojo.getTestPointLon());
					}
					if(StringUtils.hasText(fixedPointTaskOrderPojo.getTestPointLat())){
						newfixedPointTask.setTestPointLat(fixedPointTaskOrderPojo.getTestPointLat());
					}
					
					if(StringUtils.hasText(fixedPointTaskOrderPojo.getOperateType())){
						if(fixedPointTaskOrderPojo.getOperateType().equals("00")){
							newfixedPointTask.setWorkOrderState("110");
						}else if(fixedPointTaskOrderPojo.getOperateType().equals("10")){
							newfixedPointTask.setWorkOrderState("101");
						}
					}else{
						ActionContext.getContext().getValueStack().set("errorMsg", "缺少操作类型参数");
					}
					 
					fixedPointTaskOrderService.update(newfixedPointTask);
				}else{
					ActionContext.getContext().getValueStack().set("errorMsg", "工单编号与表中工单不对应");
				}
				
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,工单号为空");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "保存发生异常:"+e.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 保存APP发出的定点测试工单任务
	 * @author lucheng
	 * @date 2020年8月18日 下午2:29:30
	 * @return
	 */
	public String saveCQTaskByAPP() {
		try {		
			if(fixedPointTaskOrderPojo.getBoxId() != null){
				fixedPointTaskOrderPojo.setWorkOrderState("010");
				fixedPointTaskOrderPojo.setTestPointLon(String.format("%.6f", Float.valueOf(fixedPointTaskOrderPojo.getTestPointLon())));
				fixedPointTaskOrderPojo.setTestPointLat(String.format("%.6f", Float.valueOf(fixedPointTaskOrderPojo.getTestPointLat())));
				fixedPointTaskOrderPojo.setTaskInitiator("测试人员发起");
				fixedPointTaskOrderPojo.setTaskCreatTime(fixedPointTaskOrderPojo.getTaskCreatTime());
				fixedPointTaskOrderPojo.setTaskTimeLimit("48小时");
				fixedPointTaskOrderPojo.setComment(fixedPointTaskOrderPojo.getComment());
				
				//根据boxid查询终端
				Terminal terminalDB = terminalService.getTerminal(fixedPointTaskOrderPojo.getBoxId());
				String cityname = terminalDB.getTerminalGroup().getName();
				String wordkOrderId = queryWordkOrderId(terminalDB.getTerminalGroup().getId());
				fixedPointTaskOrderPojo.setWorkOrderId(wordkOrderId);
				fixedPointTaskOrderPojo.setRegion(cityname);
			
				fixedPointTaskOrderService.update(fixedPointTaskOrderPojo);
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,boxid为空");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "保存发生异常:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 查询平台新增自动生成的工单编号
	 * @author lucheng
	 * @date 2020年8月18日 上午10:09:06
	 * @return
	 */
	public String queryWordkOrderId(Long cityid){
		String newWorkId = null;
		PageList pageList = new PageList();
		if (null != cityid) {
			TerminalGroup group = terminalGroupService.findGroupById(cityid);
			pageList.putParam("cityId", cityid);
			String regionEnHeadChar = TextUtil.getPinYinHeadCharToUpper(group.getName());
			//查找工单类型为APP侧发出，工单编号以"DD_"开头，而APP侧以"DDZ_"开头
			pageList.putParam("workOrderType", "DDZ_"+regionEnHeadChar);
			
			List<FixedPointTaskOrderPojo> findFixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			
			newWorkId = "DDZ_"+ regionEnHeadChar + "_" + time + "_";
			
			if(findFixedPointTaskList == null || findFixedPointTaskList.size() == 0){
				newWorkId = newWorkId + "00";
			}else{
				Long seqMax = null;
				for (FixedPointTaskOrderPojo fixedPointTaskOrderPojo : findFixedPointTaskList) {
					if(StringUtils.hasText(fixedPointTaskOrderPojo.getWorkOrderId())){
						String workId = fixedPointTaskOrderPojo.getWorkOrderId();
						String seqStr = workId.substring(workId.lastIndexOf("_")+1, workId.length());
						if(StringUtils.hasText(seqStr)){
							if(seqMax == null){
								seqMax = Long.valueOf(seqStr);
							}else{
								if(seqMax < Long.valueOf(seqStr)){
									seqMax = Long.valueOf(seqStr); 
								}
							}
						}
					}
				}
				//保证数字编号最起码为两位数
				seqMax++;
				if(seqMax<10){
					newWorkId = newWorkId + "0" + seqMax ;
				}else{
					newWorkId = newWorkId + seqMax;
				}
			}
			
		}
		return newWorkId;
	}
	
	/**
	 * 保存工单编号对应的日志名称
	 * @author lucheng
	 * @date 2020年8月20日 下午1:31:00
	 * @return
	 */
	public String saveCQTLogName() {
		try {		
//			fixedPointTaskOrderPojo.setWorkOrderId("DD_GY_20200824142455_04");
//			logNames = new String[]{"5000000120200620133159LZ1.l5g"};
			if(fixedPointTaskOrderPojo.getWorkOrderId() != null && logNames != null){
				List<FixedPointTaskLogNamePojo> logNamesByOrder = fixedPointTaskOrderService.getLogNamesByOrder(fixedPointTaskOrderPojo.getWorkOrderId());
				Set<String> logNameSet = new HashSet<String>();
				for (FixedPointTaskLogNamePojo taskLogNamePojo : logNamesByOrder) {
					logNameSet.add(taskLogNamePojo.getTestLogFileName());
				}
				List<String> logNameList = new ArrayList(logNameSet);
				
				if(StringUtils.hasText(logNames)){
					String[] split = logNames.replace(" ", "").split(",");
					for (String log : split) {
						if(!logNameList.contains(log)){
							logNameList.add(log);
							FixedPointTaskLogNamePojo pojo = new FixedPointTaskLogNamePojo();
							pojo.setWorkOrderId(fixedPointTaskOrderPojo.getWorkOrderId());
							pojo.setTestLogFileName(log);
							fixedPointTaskOrderService.addCQTLogName(pojo);
						}
					}
				}
				
				PageList pageList = new PageList();
				pageList.putParam("workOrderId", fixedPointTaskOrderPojo.getWorkOrderId());
				List<FixedPointTaskOrderPojo> fixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
				if(fixedPointTaskList.size()>0){
					FixedPointTaskOrderPojo newfixedPointTask = fixedPointTaskList.get(0);
					String cityid =  newfixedPointTask.getTerminal().getTerminalGroup().getId().toString();
					if(logNameList.size()>0){
						creatCQTTaskReport.setCityId(cityid);
						creatCQTTaskReport.setLogfileNames(logNameList);
						creatCQTTaskReport.setNewfixedPointTask(newfixedPointTask);
						new Thread(creatCQTTaskReport).start();
						
					}
				}
				
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,WorkOrderId为空");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "保存发生异常:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	//下载cqt统计报告
	public String getDownload() {
		PageList pageList = new PageList();
		if(fixedPointTaskOrderPojo.getWorkOrderId()!=null){
			pageList.putParam("boxid", fixedPointTaskOrderPojo.getBoxId());
			pageList.putParam("workOrderId", fixedPointTaskOrderPojo.getWorkOrderId());
			List<FixedPointTaskOrderPojo> fixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
			if(fixedPointTaskList!=null && fixedPointTaskList.size()>0){
				FixedPointTaskOrderPojo pojo = fixedPointTaskList.get(0);
				
				File file = new File(pojo.getFilePath());
				if (file.exists()) {
					return "downloadData";
				}else{
					ActionContext.getContext().getValueStack().set("errorMsg", "excel不存在,请检查工单号和路径");
				}
			}
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,工单号为空");
		}
		return ReturnType.JSON;
	}

	public InputStream getDownloadData() {
		try {
			PageList pageList = new PageList();
			if(fixedPointTaskOrderPojo.getWorkOrderId()!=null){
				pageList.putParam("boxid", fixedPointTaskOrderPojo.getBoxId());
				pageList.putParam("workOrderId", fixedPointTaskOrderPojo.getWorkOrderId());
				List<StatisticeTask> statisticeTaskList = iReportService.findStatisticeTask(pageList);
				List<FixedPointTaskOrderPojo> fixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
				if(statisticeTaskList!=null && statisticeTaskList.size()>0 && fixedPointTaskList!=null && fixedPointTaskList.size()>0){
					StatisticeTask statisticeTask = statisticeTaskList.get(0);
					FixedPointTaskOrderPojo pojo = fixedPointTaskList.get(0);
					
					File file = new File(pojo.getFilePath());
					String fileName = statisticeTask.getName() + ".xls";
					if (file.exists() && file.isFile()) {
						try {
							ActionContext.getContext().put("fileName",new String(fileName.getBytes(), "ISO8859-1"));
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						try {
							return new FileInputStream(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public FixedPointTaskOrderPojo getModel() {
		// TODO Auto-generated method stub
		return fixedPointTaskOrderPojo;
	}

	/**
	 * @return the cityId
	 */
	public Long getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the idStr
	 */
	public String getIdStr() {
		return idStr;
	}

	/**
	 * @param idStr the idStr to set
	 */
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	/**
	 * @return the newWorkOrderid
	 */
	public String getNewWorkOrderid() {
		return newWorkOrderid;
	}

	/**
	 * @param newWorkOrderid the newWorkOrderid to set
	 */
	public void setNewWorkOrderid(String newWorkOrderid) {
		this.newWorkOrderid = newWorkOrderid;
	}

	/**
	 * @return the beginTime
	 */
	public String getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the logNames
	 */
	public String getLogNames() {
		return logNames;
	}

	/**
	 * @param logNames the logNames to set
	 */
	public void setLogNames(String logNames) {
		this.logNames = logNames;
	}

}
