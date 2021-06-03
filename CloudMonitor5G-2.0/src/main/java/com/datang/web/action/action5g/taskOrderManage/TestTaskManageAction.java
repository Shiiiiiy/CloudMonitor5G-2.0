package com.datang.web.action.action5g.taskOrderManage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import com.datang.domain.security.User;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.domain.testPlan.TestPlan;
import com.datang.exception.ApplicationException;
import com.datang.service.oppositeOpen.OppositeOpen3dCompletionShowService;
import com.datang.service.platform.projectParam.IProjectParamService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.report.IReportService;
import com.datang.service.service5g.embbCover.EmbbCoverAnalyseService;
import com.datang.service.stationTest.StationCompletionShowService;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.service.testPlan.TestPlanService;
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
 * 平台侧定点测试任务Action
 * @author lucheng
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TestTaskManageAction extends PageAction implements
		ModelDriven<FixedPointTaskOrderPojo> {
	private static Logger LOGGER = LoggerFactory.getLogger(TestTaskManageAction.class);
	
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
	private EmbbCoverAnalyseService embbCoverAnalyseService;
	
	@Autowired
	private IUserService userService;
	
	/**
	 * 测试计划服务
	 */
	@Autowired
	private TestPlanService testPlanService;
	

	/**
	 * 保存统计任务时收集参数
	 */
	private FixedPointTaskOrderPojo fixedPointTaskOrderPojo = new FixedPointTaskOrderPojo();
	
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
	 * 位置偏离
	 */
	private Integer loctionDeparture;
	
	/**
	 * 测试计划的id
	 */
	private Integer testPlanId;
	
	
	/**
	 * 跳转到设备树LIST页面list界面
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
	
	public String newFixPointTestTaskUI() {
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			Map<String, String> map = new HashMap<String, String>();
			map.put("cityId", group.getId().toString());
			map.put("cityName", group.getName());
			map.put("newWorkOrderid", newWorkOrderid);
			
			ActionContext.getContext().getValueStack().push(map);
		}
		return "newCQTTaskUI";
	}
	
	public String deleteTestTask(){
		try {
			if (StringUtils.hasText(idStr)) {
				fixedPointTaskOrderService.delete(idStr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "删除失败:"+e);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 多条件查询任务
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
		pageList.putParam("loctionDeparture", loctionDeparture);
		
		pageList.putParam("occupyNrCellName", fixedPointTaskOrderPojo.getOccupyNrCellName());
		pageList.putParam("occupyLteCellName", fixedPointTaskOrderPojo.getOccupyLteCellName());
		pageList.putParam("boxId", fixedPointTaskOrderPojo.getBoxId());
		pageList.putParam("workOrderState", fixedPointTaskOrderPojo.getWorkOrderState());
		pageList.putParam("taskInitiator", fixedPointTaskOrderPojo.getTaskInitiator());
		pageList.putParam("taskTimeLimit", fixedPointTaskOrderPojo.getTaskTimeLimit());
		
		return fixedPointTaskOrderService.doPageQuery(pageList);
	}
	
	/**
	 * 查询平台新增自动生成的工单编号
	 * @author lucheng
	 * @date 2020年8月18日 上午10:09:06
	 * @return
	 */
	public String queryWordkOrderId(){
		PageList pageList = new PageList();
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			pageList.putParam("cityId", cityId);
			String regionEnHeadChar = TextUtil.getPinYinHeadCharToUpper(group.getName());
			//查找工单类型为平台发出，工单编号以"DD_"开头，而APP侧以"DDZ_"开头
			pageList.putParam("workOrderType", "DD_"+regionEnHeadChar);
			
			List<FixedPointTaskOrderPojo> findFixedPointTaskList = fixedPointTaskOrderService.findFixedPointTask(pageList);
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			
			String newWorkId = "DD_"+ regionEnHeadChar + "_" + time + "_";
			
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
			
			ActionContext.getContext().getValueStack().set("workid", newWorkId);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 获取45G小区信息
	 * @author lucheng
	 * date:2020年8月15日 下午3:16:25
	 * @return
	 */
	public String getInitCellInfo(){
		
		List<PlanParamPojo> nrcellList = fixedPointTaskOrderService.getNrCell(cityId);
		List<HashMap<String, Object>> nrList = new ArrayList<HashMap<String,Object>>();
		for (PlanParamPojo planParamPojo : nrcellList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", planParamPojo.getId());
			map.put("name", planParamPojo.getCellName());
			nrList.add(map);
		}
		
		List<Plan4GParam> ltecellList = fixedPointTaskOrderService.getLteCell(cityId);
		List<HashMap<String, Object>> lteList = new ArrayList<HashMap<String,Object>>();
		for (Plan4GParam plan4GParam : ltecellList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", plan4GParam.getId());
			map.put("name", plan4GParam.getCellName());
			lteList.add(map);
		}

		ActionContext.getContext().getValueStack().set("nrList", nrList);
		ActionContext.getContext().getValueStack().set("lteList", lteList);
		return ReturnType.JSON;
	}
	
	/**
	 * 获取区域下的终端
	 * @author lucheng
	 * @date 下午6:58:20 
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
	 * 查询所有用户
	 * @author lucheng
	 * @date 2020年8月21日 下午4:14:44
	 * @return
	 */
	public String getInitUsers(){
		List<User> findAllUsers = userService.findAllUsers();
		
		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String,Object>>();
		for (User user : findAllUsers) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("name", user.getUsername());
			tlList.add(map);
		}
		ActionContext.getContext().getValueStack().set("tlList", tlList);
		return ReturnType.JSON;

	}
	
	/**
	 * 根据boxid查找对应的测试计划
	 * @author lucheng
	 * @date 2020年8月27日 下午5:14:45
	 * @return
	 */
	public String findTestPlan(){
		PageList pageList = new PageList();
		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String,Object>>();
		try {
			if(fixedPointTaskOrderPojo.getBoxId() != null){
				Terminal terminal = terminalService.getTerminal(fixedPointTaskOrderPojo.getBoxId());
				pageList.putParam("terminalIds", terminal.getId().toString());
				List<TestPlan> queryTestPlanByBoxid = testPlanService.queryTestPlanByBoxid(pageList);
				for (TestPlan testPlan : queryTestPlanByBoxid) {
					if (!testPlan.isSended()) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", testPlan.getId());
						map.put("name", testPlan.getName());
						tlList.add(map);
					}
				}
				ActionContext.getContext().getValueStack().set("tlList", tlList);
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,boxid为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "查询测试计划发生异常:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 根据区域获取下面5g工参shpName和4g工参shpName
	 * @author lucheng
	 * @date 2020年8月17日 下午3:38:24
	 * @return
	 */
	public String queryShpname() {
		HashMap<String, Object> dataMap = new HashMap<>();
		TerminalGroup group = terminalGroupService.findGroupById(cityId);
		
		List<PlanParamPojo> planParamPojoList = staionCompletionShowService.getDataOfCityName(group.getName());
		for (PlanParamPojo planParamPojo : planParamPojoList) {
			if(planParamPojo.getCellInfo() != null){
				if(StringUtils.hasText(planParamPojo.getCellInfo().getLteCellGisFileName())){
					dataMap.put("shpName_nr", planParamPojo.getCellInfo().getLteCellGisFileName()+".shp");
				}else{
					dataMap.put("shpName_nr", null);
				}
				dataMap.put("lon_nr", planParamPojo.getLon());
				dataMap.put("lat_nr", planParamPojo.getLat());
			}
		}
		
		List<Plan4GParam> plan4GParamList = oppositeOpen3dCompletionShowService.getDataOfCityName(group.getName());
		for (Plan4GParam planParamPojo : plan4GParamList) {
			if(planParamPojo.getCellInfo() != null){
				if(StringUtils.hasText(planParamPojo.getCellInfo().getLteCellGisFileName())){
					dataMap.put("shpName_lte", planParamPojo.getCellInfo().getLteCellGisFileName()+".shp");
				}else{
					dataMap.put("shpName_lte", null);
				}
				dataMap.put("lon_lte", planParamPojo.getLongitude());
				dataMap.put("lat_lte", planParamPojo.getLatitude());
			}
		}
		ActionContext.getContext().getValueStack().push(dataMap);
		return ReturnType.JSON;
	}
	
	/**
	 * 保存平台发出的定点测试工单任务
	 * @author lucheng
	 * @date 2020年8月18日 下午2:29:30
	 * @return
	 */
	public String saveByETG() {
		try {
			if(fixedPointTaskOrderPojo.getBoxId() != null){
				fixedPointTaskOrderPojo.setTaskCreatTime(new Date());
				fixedPointTaskOrderPojo.setWorkOrderState("010");
				Object user = SecurityUtils.getSubject().getPrincipal();
				fixedPointTaskOrderPojo.setTaskInitiator((String) user);
				
				if(testPlanId!=null){
					TestPlan testPlan = testPlanService.getTestPlan(testPlanId);
					if(testPlan.getTestSuit().size()>0 && testPlan.getTestSuit().get(0).getTimes()!=null && testPlan.getTestSuit().get(0).getTimes().size()>0){
						String beginTime = testPlan.getTestSuit().get(0).getTimes().get(0).getBeginTime();
						String endTime = testPlan.getTestSuit().get(0).getTimes().get(0).getEndTime();
						if(beginTime!=null && endTime!=null){
							String testDate = beginTime.replace(":", "") + "-" + endTime.replace(":", "");
							fixedPointTaskOrderPojo.setTestDate(testDate);
						}
					}
					
					String gpsCondition = testPlan.getAutoTestUnit().getGeneralItem().getGpsCondition();
					String leftTopLon = testPlan.getAutoTestUnit().getGeneralItem().getLeftTopLon();
					String leftTopLat = testPlan.getAutoTestUnit().getGeneralItem().getLeftTopLat();
					String rightBottomLon = testPlan.getAutoTestUnit().getGeneralItem().getRightBottomLon();
					String rightBottomLat = testPlan.getAutoTestUnit().getGeneralItem().getRightBottomLat();
					if(gpsCondition!=null && gpsCondition.equals("1")){
						if(leftTopLon!=null && leftTopLat!=null && rightBottomLon!=null && rightBottomLat!=null){
							Float lonAvg =  (Float.valueOf(leftTopLon)+Float.valueOf(rightBottomLon))/2;
							Float latAvg =  (Float.valueOf(leftTopLat)+Float.valueOf(rightBottomLat))/2;
							fixedPointTaskOrderPojo.setTestPointLon(String.format("%.6f", lonAvg));
							fixedPointTaskOrderPojo.setTestPointLat(String.format("%.6f", latAvg));
						}
					}
				}
				
				TerminalGroup group = terminalGroupService.findGroupById(cityId);
				fixedPointTaskOrderPojo.setRegion(group.getName());
				
				//根据boxid查询终端
				Terminal terminalDB = terminalService.getTerminal(fixedPointTaskOrderPojo.getBoxId());
				fixedPointTaskOrderPojo.setTerminal(terminalDB);
				fixedPointTaskOrderService.update(fixedPointTaskOrderPojo);
				ActionContext.getContext().getValueStack().set("cityName", group.getName());
			}else{
				ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,boxid为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "保存发生异常:"+e.getMessage());
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 闭环任务
	 * @author lucheng
	 * @date 2020年8月19日 上午11:11:18
	 * @return
	 */
	public String closeTestTask(){
		try {
			if (StringUtils.hasText(idStr)) {
				String[] StrArry = idStr.split(",");
				for (int i = 0; i < StrArry.length; i++) {
					FixedPointTaskOrderPojo fixedPointTaskOrderPojo = fixedPointTaskOrderService.find(Long.valueOf(StrArry[i]));
					fixedPointTaskOrderPojo.setWorkOrderState("100");
					
					List<FixedPointTaskLogNamePojo> logNamesByOrder = fixedPointTaskOrderService.getLogNamesByOrder(fixedPointTaskOrderPojo.getWorkOrderId());
					List<String> logNameList = new ArrayList<String>();
					
					for (FixedPointTaskLogNamePojo taskLogNamePojo : logNamesByOrder) {
						logNameList.add(taskLogNamePojo.getTestLogFileName());
					}
					if(logNameList.size()>0){
						List<StationEtgTralPojo> stationEtgTralPojoList = embbCoverAnalyseService.getGpsPointData(logNameList,null,null,-1);
						Double lonSum = 0D;
						Double latSum = 0D;
						Integer n=0;
						for (StationEtgTralPojo stationEtgTralPojo : stationEtgTralPojoList) {
							if(StringUtils.hasText(stationEtgTralPojo.getLongtitude()) && StringUtils.hasText(stationEtgTralPojo.getLatitude())){
								n++;
								lonSum = lonSum + Double.valueOf(stationEtgTralPojo.getLongtitude());
								latSum = latSum + Double.valueOf(stationEtgTralPojo.getLatitude());
							}
						}
						if(n!=0){
							double distance = GPSUtils.distance(latSum/n, lonSum/n, Double.valueOf(fixedPointTaskOrderPojo.getTestPointLat()), 
									Double.valueOf(fixedPointTaskOrderPojo.getTestPointLon()));
							fixedPointTaskOrderPojo.setTestLocationSkewing(Math.round(distance));
						}
					}
					fixedPointTaskOrderService.update(fixedPointTaskOrderPojo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getValueStack().set("errorMsg", "闭环失败"+e);
		}
		return ReturnType.JSON;
	}

	@Override
	public FixedPointTaskOrderPojo getModel() {
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
	 * @return the loctionDeparture
	 */
	public Integer getLoctionDeparture() {
		return loctionDeparture;
	}

	/**
	 * @param loctionDeparture the loctionDeparture to set
	 */
	public void setLoctionDeparture(Integer loctionDeparture) {
		this.loctionDeparture = loctionDeparture;
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
	 * @return the testPlanId
	 */
	public Integer getTestPlanId() {
		return testPlanId;
	}

	/**
	 * @param testPlanId the testPlanId to set
	 */
	public void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}
	
}
