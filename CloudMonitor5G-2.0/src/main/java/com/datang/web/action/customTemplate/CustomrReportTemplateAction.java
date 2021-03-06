/**
 * 
 */
package com.datang.web.action.customTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.datang.common.util.ClassUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.security.User;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.stationTest.StationReportExcelPojo;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.customTemplate.CustomTemplateService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testLogItem.INetworkTestLogItemService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.service.testPlan.TestPlanService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

import net.sf.json.JSONObject;

/**
 * ???????????????????????????
 * @author lucheng
 * @date 2020???12???22??? ??????2:20:44
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CustomrReportTemplateAction extends PageAction implements ModelDriven<CustomReportTemplatePojo> {
	
	@Value("${stationReportFileLink}")
	private String reportUrl;
	
	@Autowired
	private CustomTemplateService customTemplateService;
	
	/**
	 * ??????????????????
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * ??????????????????
	 */
	@Autowired
	private IUserService userService;
	
	/**
	 * ???????????????
	 */
	@Autowired
	private TerminalGroupService groupService;

	private CustomReportTemplatePojo customReportTemplatePojo = new CustomReportTemplatePojo();
	
	/**
	 * ????????????
	 */
	private File importFile;
	
	private String importFileFileName;
	
	private String importFileContentType;
	
	private Date beginImportDate;// ??????????????????
	private Date endImportDate;// ??????????????????
	private String userName;// ??????????????????
	private String citNames;// ????????????????????????
	
	private Long cityId;// ??????????????????
	
	private String idsStr; //????????????id
	

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}
	
	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	public String goImport() {
		return "importUI";
	}
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		//??????????????????????????????????????????????????????
//		if(citNames == null || !StringUtils.hasText(citNames)){
//			citNames="";
//			// ???????????????????????????????????????menu
//			List<TerminalMenu> cities = menuManageService.getCities();
//			// ????????????menu?????????terminalGroup
//			List<TerminalGroup> groupsByMenus = groupService
//					.getGroupsByMenus(cities);
//			for(int i=0;i<groupsByMenus.size();i++){
//				citNames = citNames+groupsByMenus.get(i).getName();
//				if(i!=groupsByMenus.size()-1){
//					citNames = citNames+",";
//				}
//			}
//		}
		pageList.putParam("beginImportDate", beginImportDate);
		pageList.putParam("endImportDate", endImportDate);
//		pageList.putParam("citNames", citNames);
		AbstractPageList list = customTemplateService.doPageQuery(pageList);
		
		return list;
	}
	
	/**
	 * ?????????????????????
	 * @author lucheng
	 * @date 2020???12???22??? ??????3:40:32
	 * @return
	 */
	public String importReportTemplate() {
		try {
			ValueStack valueStack = ActionContext.getContext().getValueStack();
			customTemplateService.importReportTemplate(cityId,importFile,importFileFileName);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	public String downloadExcel() {
		return "downloadData";
	}
	
	public InputStream getDownloadData() {
		List<File> fileList = new ArrayList<File>();
		String[] ids = idsStr.split(",");


		List<String> fileName = new ArrayList<>();
		fileName.add("????????????????????????????????????_DT??????V1.0.xlsx");
		fileName.add("???????????????????????????????????????_DT??????V1.0.xlsx");


		for (String id : ids) {
			CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(id));
			if(pojo.getSaveFilePath() != null){
				// ????????????????????? ???????????????,??????????????????????????????
				if(fileName.contains(pojo.getTemplateName())){
					InputStream is = ClassUtil.getResourceAsStream("/templates/" + pojo.getTemplateName());
					File file = new File(reportUrl + "/uploadReportTemplate/" + pojo.getTemplateName() );
					fileList.add(file);
					OutputStream os = null;
					try {
						 os = new FileOutputStream(file);
						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = is.read(buffer))!=-1){
							os.write(buffer,0,len);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}finally {
						try{
							os.close();
						}catch (IOException ose){
							ose.printStackTrace();
						}
						try{
							is.close();
						}catch (IOException ise){
							ise.printStackTrace();
						}
					}
				}else{
					File file = new File(pojo.getSaveFilePath());
					if(file.exists()){
						fileList.add(file);
					}

				}




			}
		}





		File file1 = new File(reportUrl+ "/downloadTemplate/");
		if (!file1.exists()) {
			file1.mkdirs();
		}


		
		File zipFile = new File(reportUrl + "/downloadTemplate/" + "?????????????????????.zip");
		FileInputStream zipIn = null ;
		try {
			ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
			ZipMultiFile.zipFiles(fileList, zipFile);
			zipIn = new FileInputStream(zipFile);
			zipFile.delete();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return zipIn;
		
	}
	
	/**
	 * ?????????????????????
	 * @author lucheng
	 * @date 2020???12???22??? ??????3:11:32
	 * @return
	 */
	public String delectParams() {
		try {
			String[] ids = idsStr.split(","); 
			List<Long> list = new ArrayList<Long>();
			for (String id : ids) {
				CustomReportTemplatePojo pojo = customTemplateService.find(Long.valueOf(id));
				if(pojo.getSaveFilePath() != null){
					File file = new File(pojo.getSaveFilePath());
					if(file != null && file.exists() ){
						file.delete();
					}
				}
				PageList selectConditions = new PageList();
				selectConditions.putParam("templateName", pojo.getTemplateName());
				List<CustomReportTemplatePojo> queryTemplateByParam = customTemplateService.queryTemplateByParam(selectConditions);
				for (CustomReportTemplatePojo customReportTemplatePojo : queryTemplateByParam) {
					list.add(customReportTemplatePojo.getId());
				}
			}
			customTemplateService.deleteCustomTemplate(list);
			
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "??????????????????,"+e);
		}
		return ReturnType.JSON;
	}
	
	
	
	@Override
	public CustomReportTemplatePojo getModel() {
		return customReportTemplatePojo;
	}

	public CustomReportTemplatePojo getCustomReportTemplatePojo() {
		return customReportTemplatePojo;
	}

	public void setCustomReportTemplatePojo(CustomReportTemplatePojo customReportTemplatePojo) {
		this.customReportTemplatePojo = customReportTemplatePojo;
	}

	public Date getBeginImportDate() {
		return beginImportDate;
	}

	public void setBeginImportDate(Date beginImportDate) {
		this.beginImportDate = beginImportDate;
	}

	public Date getEndImportDate() {
		return endImportDate;
	}

	public void setEndImportDate(Date endImportDate) {
		this.endImportDate = endImportDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCitNames() {
		return citNames;
	}

	public void setCitNames(String citNames) {
		this.citNames = citNames;
	}

	public String getIdsStr() {
		return idsStr;
	}

	public void setIdsStr(String idsStr) {
		this.idsStr = idsStr;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public String getImportFileFileName() {
		return importFileFileName;
	}

	public void setImportFileFileName(String importFileFileName) {
		this.importFileFileName = importFileFileName;
	}

	public String getImportFileContentType() {
		return importFileContentType;
	}

	public void setImportFileContentType(String importFileContentType) {
		this.importFileContentType = importFileContentType;
	}


}
