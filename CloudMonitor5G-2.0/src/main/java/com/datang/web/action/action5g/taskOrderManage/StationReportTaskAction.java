package com.datang.web.action.action5g.taskOrderManage;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dWirelessPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.taskOrderManage.StationTaskLogNamePojo;
import com.datang.domain.taskOrderManage.StationTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.oppositeOpen.OppositeOpen3dCompletionShowService;
import com.datang.service.oppositeOpen.OppositeOpen3dReportCreateService;
import com.datang.service.report.IReportService;
import com.datang.service.service5g.embbCover.EmbbCoverAnalyseService;
import com.datang.service.stationTest.StationCompletionShowService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import com.datang.service.taskOrderManage.StationTaskOrderService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testLogItem.StationVerificationTestService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.util.DateUtil;
import com.datang.util.GPSUtils;
import com.datang.util.TextUtil;
import com.datang.web.action.ReturnType;
import com.datang.web.action.report.ReportAction;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.datang.web.beans.report.BoxInforRequestBean;
import com.datang.web.beans.report.CityInfoRequestBean;
import com.datang.web.beans.report.ReportRequertBean;
import com.datang.web.beans.report.StatisticeTaskRequest;
import com.datang.web.beans.report.TestInfoRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


/**
 * ??????????????????Action
 * @author lucheng
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class StationReportTaskAction extends PageAction implements
		ModelDriven<StationTaskOrderPojo> {
	private static Logger LOGGER = LoggerFactory.getLogger(StationReportTaskAction.class);

	/**
	 * ?????????????????????????????????
	 */
	private StationTaskOrderPojo stationTaskOrderPojo = new StationTaskOrderPojo();
	
	/**
	 * ???????????????
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	/**
	 *  ????????????????????????
	 */
	@Autowired
	private StationTaskOrderService stationTaskOrderService;
	
	/**
	 * Description:????????????
	 * @author lucheng
	 * @date ??????6:52:39 
	 */
	@Autowired
	private TerminalService terminalService;
	
	@Autowired
	private StationCompletionShowService staionCompletionShowService;
	
	@Autowired
	private OppositeOpen3dCompletionShowService oppositeOpen3dCompletionShowService;
	
	@Autowired
	private EmbbCoverAnalyseService embbCoverAnalyseService;
	
	@Autowired
	private StationReportCreatService stationReportCreatService;
	
	@Autowired
	private OppositeOpen3dReportCreateService oppositeOpen3dReportCreateService;
	
	@Autowired
	private StationVerificationTestService stationVerificationTestService;
	
	/**
	 * ????????????ID
	 */
	private Long cityId;
	
	//??????????????????id??????
	private String idStr;
	
	//????????????id
	private String newWorkOrderid;
	
	/**
	 * ????????????????????????
	 */
	private Date beginDate ;

	private Date endDate ;
	
	private String siteNames;
	
	/**
	 * ??????????????????LIST??????list??????
	 * 
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}

	public String testTaskListUI() {
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			ActionContext.getContext().getValueStack().push(group);
		}
		return "testTaskUI";
	}
	
//	public String newFixPointTestTaskUI() {
//		if (null != cityId) {
//			TerminalGroup group = terminalGroupService.findGroupById(cityId);
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("cityId", group.getId().toString());
//			map.put("cityName", group.getName());
//			map.put("newWorkOrderid", newWorkOrderid);
//			
//			ActionContext.getContext().getValueStack().push(map);
//		}
//		return "newCQTTaskUI";
//	}
	
	/**
	 * ??????????????????
	 * @author lucheng
	 * @date 2020???8???20??? ??????6:57:53
	 * @return
	 */
	public String deleteTestTask(){
		try {
			if (StringUtils.hasText(idStr)) {
				stationTaskOrderService.delete(idStr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "????????????:"+e);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ?????????????????????
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
//		if (null != cityId) {
//			pageList.putParam("cityId", cityId);
//		}else{
//			EasyuiPageList easyuiPageList = new EasyuiPageList();
//			List<FixedPointTaskOrderPojo> list = new ArrayList<FixedPointTaskOrderPojo>();
//			easyuiPageList.setRows(list);
//			easyuiPageList.setTotal("0");
//			return easyuiPageList;
//		}
		pageList.putParam("cityId", cityId);
		pageList.putParam("beginDate", beginDate);
		pageList.putParam("endDate", endDate);
		
		pageList.putParam("siteName", stationTaskOrderPojo.getSiteName());
		pageList.putParam("networkType", stationTaskOrderPojo.getTaskNetworkType());
		pageList.putParam("boxId", stationTaskOrderPojo.getBoxId());
		pageList.putParam("workOrderState", stationTaskOrderPojo.getWorkOrderState());
		pageList.putParam("taskInitiator", stationTaskOrderPojo.getTaskInitiator());
		pageList.putParam("taskTimeLimit", stationTaskOrderPojo.getTaskTimeLimit());
		
		return stationTaskOrderService.doPageQuery(pageList);
	}
	
	/**
	 * ?????????????????????????????????????????????
	 * @author lucheng
	 * @date 2020???8???18??? ??????10:09:06
	 * @return
	 */
	public String queryWordkOrderId(){
		PageList pageList = new PageList();
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			pageList.putParam("cityId", cityId);
			String regionEnHeadChar = TextUtil.getPinYinHeadCharToUpper(group.getName());
			//???????????????????????????????????????????????????"DY_"????????????APP??????"DYZ_"??????
			pageList.putParam("workOrderType", "DY_"+regionEnHeadChar);
			
			List<StationTaskOrderPojo> stationTaskOrderList = stationTaskOrderService.findStationTaskTask(pageList);
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			
			String newWorkId = "DY_"+ regionEnHeadChar + "_" + time + "_";
			
			if(stationTaskOrderList == null || stationTaskOrderList.size() == 0){
				newWorkId = newWorkId + "00";
			}else{
				Long seqMax = null;
				for (StationTaskOrderPojo stationTaskOrderPojo : stationTaskOrderList) {
					if(StringUtils.hasText(stationTaskOrderPojo.getWorkOrderId())){
						String workId = stationTaskOrderPojo.getWorkOrderId();
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
				//???????????????????????????????????????
				seqMax++;
				if(seqMax<10){
					newWorkId = newWorkId + "0" + seqMax ;
				}else{
					newWorkId = newWorkId + seqMax;
				}
			}
			
			ActionContext.getContext().getValueStack().set("workid", newWorkId);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ??????45G????????????
	 * @author lucheng
	 * date:2020???8???15??? ??????3:16:25
	 * @return
	 */
	public String getInitCellInfo(){
		HashMap<String, Object> nrSitNameMap = new HashMap<String, Object>();
		HashMap<String, Object> lteSitNameMap = new HashMap<String, Object>();
		
		List<PlanParamPojo> nrcellList = stationTaskOrderService.getNrCell(cityId);
		List<HashMap<String, Object>> nrList = new ArrayList<HashMap<String,Object>>();
		for (PlanParamPojo planParamPojo : nrcellList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(nrSitNameMap.get(planParamPojo.getSiteName()) == null){
				map.put("id", planParamPojo.getId());
				map.put("name", planParamPojo.getSiteName());
				nrList.add(map);
				
				HashMap<String, Object> lonLatMap = new HashMap<String, Object>();
				lonLatMap.put("lon", planParamPojo.getLon());
				lonLatMap.put("lat", planParamPojo.getLat());
				nrSitNameMap.put(planParamPojo.getSiteName(), lonLatMap);
			}
			
		}
		
		List<Plan4GParam> ltecellList = stationTaskOrderService.getLteCell(cityId);
		List<HashMap<String, Object>> lteList = new ArrayList<HashMap<String,Object>>();
		for (Plan4GParam plan4GParam : ltecellList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(lteSitNameMap.get(plan4GParam.getSiteName()) == null){
				map.put("id", plan4GParam.getId());
				map.put("name", plan4GParam.getSiteName());
				lteList.add(map);
				
				HashMap<String, Object> lonLatMap = new HashMap<String, Object>();
				lonLatMap.put("lon", plan4GParam.getLongitude());
				lonLatMap.put("lat", plan4GParam.getLatitude());
				lteSitNameMap.put(plan4GParam.getSiteName(), lonLatMap);
			}
		}

		ActionContext.getContext().getValueStack().set("nrList", nrList);
		ActionContext.getContext().getValueStack().set("lteList", lteList);
		
		return ReturnType.JSON;
	}
	
	/**
	 * ????????????????????????
	 * @author lucheng
	 * @date ??????6:58:20 
	 * @return
	 */
	public String getInitTerminal(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("cityId", cityId);
		
		List<Terminal> tl = terminalService.findByParam(param);
		
		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String,Object>>();
		for (Terminal terminal : tl) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", terminal.getBoxId());
			map.put("name", terminal.getBoxId());
			tlList.add(map);
		}
		ActionContext.getContext().getValueStack().set("tlList", tlList);
		return ReturnType.JSON;

	}
	
	/**
	 * ????????????????????????5g??????shpName???4g??????shpName
	 * @author lucheng
	 * @date 2020???8???17??? ??????3:38:24
	 * @return
	 */
//	public String queryShpname() {
//		HashMap<String, Object> dataMap = new HashMap<>();
//		TerminalGroup group = terminalGroupService.findGroupById(cityId);
//		
//		List<PlanParamPojo> planParamPojoList = staionCompletionShowService.getDataOfCityName(group.getName());
//		for (PlanParamPojo planParamPojo : planParamPojoList) {
//			if(planParamPojo.getCellInfo() != null){
//				if(StringUtils.hasText(planParamPojo.getCellInfo().getLteCellGisFileName())){
//					dataMap.put("shpName_nr", planParamPojo.getCellInfo().getLteCellGisFileName()+".shp");
//				}else{
//					dataMap.put("shpName_nr", null);
//				}
//				dataMap.put("lon_nr", planParamPojo.getLon());
//				dataMap.put("lat_nr", planParamPojo.getLat());
//			}
//		}
//		
//		List<Plan4GParam> plan4GParamList = oppositeOpen3dCompletionShowService.getDataOfCityName(group.getName());
//		for (Plan4GParam planParamPojo : plan4GParamList) {
//			if(planParamPojo.getCellInfo() != null){
//				if(StringUtils.hasText(planParamPojo.getCellInfo().getLteCellGisFileName())){
//					dataMap.put("shpName_lte", planParamPojo.getCellInfo().getLteCellGisFileName()+".shp");
//				}else{
//					dataMap.put("shpName_lte", null);
//				}
//				dataMap.put("lon_lte", planParamPojo.getLongitude());
//				dataMap.put("lat_lte", planParamPojo.getLatitude());
//			}
//		}
//		ActionContext.getContext().getValueStack().push(dataMap);
//		return ReturnType.JSON;
//	}
	
	/**
	 * ?????????????????????????????????????????????
	 * @author lucheng
	 * @date 2020???8???18??? ??????2:29:30
	 * @return
	 */
	public String saveByETG() {
		try {
			if(stationTaskOrderPojo.getBoxId() != null){
				stationTaskOrderPojo.setTaskCreatTime(new Date());
				stationTaskOrderPojo.setWorkOrderState("010");
				String queryWordkOrderId = queryWordkOrderId();
				
				Object user = SecurityUtils.getSubject().getPrincipal();
				stationTaskOrderPojo.setTaskInitiator((String) user);
				
				if(stationTaskOrderPojo.getTaskNetworkType().equals("0")){
					List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(stationTaskOrderPojo.getSiteName());
					if(planParamPojoAscList!=null && planParamPojoAscList.size()>0){
						stationTaskOrderPojo.setTestPointLon(planParamPojoAscList.get(0).getLon().toString());
						stationTaskOrderPojo.setTestPointLat(planParamPojoAscList.get(0).getLat().toString());
					}

				}else if(stationTaskOrderPojo.getTaskNetworkType().equals("1")){
					List<Plan4GParam> oppo3dPojoList = oppositeOpen3dReportCreateService.getAllBySitename(stationTaskOrderPojo.getSiteName());
					if(oppo3dPojoList!=null && oppo3dPojoList .size()>0){
						stationTaskOrderPojo.setTestPointLon(oppo3dPojoList.get(0).getLongitude().toString());
						stationTaskOrderPojo.setTestPointLat(oppo3dPojoList.get(0).getLatitude().toString());
					}
				}
				
				TerminalGroup group = terminalGroupService.findGroupById(cityId);
				stationTaskOrderPojo.setRegion(group.getName());
				
				//??????boxid????????????
				Terminal terminalDB = terminalService.getTerminal(stationTaskOrderPojo.getBoxId());
				stationTaskOrderPojo.setTerminal(terminalDB);
				stationTaskOrderService.update(stationTaskOrderPojo);
				ActionContext.getContext().getValueStack().set("cityName", group.getName());
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "???????????????????????????,boxid??????");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "??????????????????:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ????????????
	 * @author lucheng
	 * @date 2020???8???19??? ??????11:11:18
	 * @return
	 */
	public String closeTestTask(){
		try {
			if (StringUtils.hasText(idStr)) {
				String[] StrArry = idStr.split(",");
				for (int i = 0; i < StrArry.length; i++) {
					StationTaskOrderPojo stationTaskOrde = stationTaskOrderService.find(Long.valueOf(StrArry[i]));
					stationTaskOrde.setWorkOrderState("100");
					
					stationTaskOrderService.update(stationTaskOrde);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "????????????"+e);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @author lucheng
	 * @date 2020???8???21??? ??????9:50:07
	 * @return
	 */
	public String getCellInfoBySiteName(){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		if(siteNames!=null){
			if(stationTaskOrderPojo.getTaskNetworkType()!=null){
				String[] siteNameArry = siteNames.split(",");
				for (String sitName : siteNameArry) {
					if(stationTaskOrderPojo.getTaskNetworkType().equals("0")){
						List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(sitName);
						if(planParamPojoAscList!=null && planParamPojoAscList.size()>0){
							for (PlanParamPojo planParamPojo : planParamPojoAscList) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("id", planParamPojo.getId());
								map.put("name", planParamPojo.getCellName());
								list.add(map);
							}
								
						}
					}else if(stationTaskOrderPojo.getTaskNetworkType().equals("1")){
						List<Plan4GParam> oppo3dPojoList = oppositeOpen3dReportCreateService.getAllBySitename(sitName);
						if(oppo3dPojoList!=null && oppo3dPojoList .size()>0){
							for (Plan4GParam plan4GParam : oppo3dPojoList) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("id", plan4GParam.getId());
								map.put("name", plan4GParam.getSiteName());
								list.add(map);
							}
						}
					}
				}
			}
			
		}

		ActionContext.getContext().getValueStack().set("tlList", list);
		return ReturnType.JSON;
		
	}
	
	/**
	 * ?????????????????????????????????
	 * @author lucheng
	 * @date 2020???8???22??? ??????7:55:12
	 * @return
	 */
	public String getNrPoint(){
		PlanParamPojo find = stationReportCreatService.find(stationTaskOrderPojo.getId());
		//1.??????cellName ?????????????????????????????????,????????????????????? ???????????????
		String event="DT";
		List<StationVerificationLogPojo> svflList = stationVerificationTestService.findByCorrelativeCell(find.getCellName(),event,null);
		//2.???????????????????????????
		List<StationTaskLogNamePojo> logNamesByOrder = stationTaskOrderService.getLogNamesByOrder(stationTaskOrderPojo.getWorkOrderId());
		Set<String> logNameSet = new HashSet<String>();
		for (StationTaskLogNamePojo taskLogNamePojo : logNamesByOrder) {
			logNameSet.add(taskLogNamePojo.getTestLogFileName());
		}
		List<String> logNameList = new ArrayList(logNameSet); 
		//3.????????????id???????????????????????????
		List<StationEtgTralPojo> setList = null;
		if(logNameList != null && logNameList.size()>0){
			setList = stationTaskOrderService.findTralById(logNameList,find.getPci().toString());
		}
		ActionContext.getContext().getValueStack().set("data", setList);
		
		return ReturnType.JSON;
	}
	
	/**
	 * ????????????3d?????????????????????
	 * @author lucheng
	 * @date 2020???8???22??? ??????7:55:12
	 * @return
	 */
	public String getLtePoint(){
		Plan4GParam find = oppositeOpen3dReportCreateService.find(stationTaskOrderPojo.getId());
		//1.??????cellName ?????????????????????????????????,????????????????????? ???????????????
		String event="DT";
		List<StationVerificationLogPojo> svflList = stationVerificationTestService.findByCorrelativeCell(find.getCellName(),event,null);
		//2.???????????????????????????
		List<StationTaskLogNamePojo> logNamesByOrder = stationTaskOrderService.getLogNamesByOrder(stationTaskOrderPojo.getWorkOrderId());
		Set<String> logNameSet = new HashSet<String>();
		for (StationTaskLogNamePojo taskLogNamePojo : logNamesByOrder) {
			logNameSet.add(taskLogNamePojo.getTestLogFileName());
		}
		List<String> logNameList = new ArrayList(logNameSet); 
		//3.????????????id???????????????????????????
		List<StationEtgTralPojo> setList = null;
		if(logNameList != null && logNameList.size()>0){
			setList = stationTaskOrderService.findTralById(logNameList,find.getPci().toString());
		}
		ActionContext.getContext().getValueStack().set("data", setList);
		
		return ReturnType.JSON;
	}

	
	//??????????????????????????????
	public String getDownload() {
		PageList pageList = new PageList();
		if(stationTaskOrderPojo.getWorkOrderId()!=null){
			pageList.putParam("boxid", stationTaskOrderPojo.getBoxId());
			pageList.putParam("workOrderId", stationTaskOrderPojo.getWorkOrderId());
			List<StationTaskOrderPojo> stationTaskList = stationTaskOrderService.findStationTaskTask(pageList);
			if(stationTaskList!=null && stationTaskList.size()>0){
				StationTaskOrderPojo statisticeTask = stationTaskList.get(0); 
				if(StringUtils.hasText(statisticeTask.getFilePath())){
					File file = new File(statisticeTask.getFilePath());
					if (file.exists()) {
						return "downloadData";
					}else{
						ActionContext.getContext().getValueStack().set("errorMsg", "????????????????????????????????????!");
					}
				}else{
					ActionContext.getContext().getValueStack().set("errorMsg","?????????"+stationTaskOrderPojo.getWorkOrderId()+"????????????????????????????????????????????????!");
				}
			}
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "???????????????????????????,???????????????!");
		}
		return ReturnType.JSON;
	}

	public InputStream getDownloadData() {
		try {
			PageList pageList = new PageList();
			if(stationTaskOrderPojo.getWorkOrderId()!=null){
				pageList.putParam("boxid", stationTaskOrderPojo.getBoxId());
				pageList.putParam("workOrderId", stationTaskOrderPojo.getWorkOrderId());
				List<StationTaskOrderPojo> stationTaskList = stationTaskOrderService.findStationTaskTask(pageList);
				
				if(stationTaskList!=null && stationTaskList.size()>0){
					StationTaskOrderPojo statisticeTask = stationTaskList.get(0);
					
					File file = new File(statisticeTask.getFilePath());
					if (file.exists() && file.isFile()) {
						String fileName = statisticeTask.getWorkOrderId() + "_" + statisticeTask.getSiteName()+".jpg";
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
	public StationTaskOrderPojo getModel() {
		return stationTaskOrderPojo;
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
	 * @return the siteNames
	 */
	public String getSiteNames() {
		return siteNames;
	}

	/**
	 * @param siteNames the siteNames to set
	 */
	public void setSiteNames(String siteNames) {
		this.siteNames = siteNames;
	}
	
}
