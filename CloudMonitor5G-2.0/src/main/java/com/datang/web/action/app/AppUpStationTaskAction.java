package com.datang.web.action.app;

import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.taskOrderManage.StationTaskLogNamePojo;
import com.datang.domain.taskOrderManage.StationTaskOrderPojo;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.oppositeOpen.OppositeOpen3dCompletionShowService;
import com.datang.service.oppositeOpen.OppositeOpen3dReportCreateService;
import com.datang.service.report.IReportService;
import com.datang.service.stationTest.StationCompletionShowService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.taskOrderManage.StationTaskOrderService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.tools.CreatStationTaskReport;
import com.datang.util.TextUtil;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * APP测发出单站任务Action
 * @author lucheng
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class AppUpStationTaskAction implements ModelDriven<StationTaskOrderPojo> {
	private static Logger LOGGER = LoggerFactory.getLogger(AppUpStationTaskAction.class);
	
	/**
	 * 区域组服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	/**
	 *  单验测试任务服务
	 */
	@Autowired
	private StationTaskOrderService stationTaskOrderService;
	
	@Autowired
	private StationReportCreatService stationReportCreatService;
	
	@Autowired
	private OppositeOpen3dReportCreateService oppositeOpen3dReportCreateService;
	
	@Autowired
	private CreatStationTaskReport creatStationTaskReport;
	
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
	private StationTaskOrderPojo stationTaskOrderPojo = new StationTaskOrderPojo();
	
	@Value("${appTaskReportFileLink}")
	private String fileSaveUrl;
	
	/**
	 * 市级区域ID
	 */
	private Long cityId;
	

	
	//测试工单记录id集合
	private String idStr;
	
	//新增工单id
	private String newWorkOrderid;
	

	
	//日志名称
	private String logNames;
	
	
	/**
	 * APP侧查询定点测试任务工单
	 * @author lucheng
	 * @date 2020年8月19日 下午7:49:20
	 * @return
	 */
	public String queryStationTaskData() {
		try {	
			if(stationTaskOrderPojo.getBoxId() != null){
				PageList pageList = new PageList();
				pageList.putParam("boxId", stationTaskOrderPojo.getBoxId());
				List<StationTaskOrderPojo> stationTaskList = stationTaskOrderService.findStationTaskTask(pageList);
				ActionContext.getContext().getValueStack().set("data", stationTaskList);
				
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
	 * APP发出定点任务状态变化通知
	 * @author lucheng
	 * @date 2020年8月19日 下午1:23:42
	 * @return
	 */
	public String updateStationTaskState() {
		try {
			String errorMsg ="";
//			stationTaskOrderPojo.setWorkOrderId("DY_JC_20200822205511_02");
//			stationTaskOrderPojo.setOperateType("10");
			if(stationTaskOrderPojo.getWorkOrderId() != null){
				PageList pageList = new PageList();
				pageList.putParam("workOrderId", stationTaskOrderPojo.getWorkOrderId());
				List<StationTaskOrderPojo> stationTaskList = stationTaskOrderService.findStationTaskTask(pageList);
				if(stationTaskList.size()== 1 ){
					StationTaskOrderPojo newStationTask = stationTaskList.get(0);
					if(StringUtils.hasText(stationTaskOrderPojo.getBoxId())){
						newStationTask.setBoxId(stationTaskOrderPojo.getBoxId());
					}
					if(stationTaskOrderPojo.getRecentUpdateTime()!=null){
						newStationTask.setRecentUpdateTime(stationTaskOrderPojo.getRecentUpdateTime());
					}
					if(StringUtils.hasText(stationTaskOrderPojo.getTestPointLon())){
						newStationTask.setTestPointLon(stationTaskOrderPojo.getTestPointLon());
					}
					if(StringUtils.hasText(stationTaskOrderPojo.getTestPointLat())){
						newStationTask.setTestPointLat(stationTaskOrderPojo.getTestPointLat());
					}
					
					if(StringUtils.hasText(stationTaskOrderPojo.getOperateType())){
						if(stationTaskOrderPojo.getOperateType().equals("00")){
							newStationTask.setWorkOrderState("110");
						}else if(stationTaskOrderPojo.getOperateType().equals("10")){
							newStationTask.setWorkOrderState("101");
						}
					}else{
						ActionContext.getContext().getValueStack().set("errorMsg", "缺少操作类型参数");
					}
					 
					stationTaskOrderService.update(newStationTask);
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
	public String saveStationTaskByAPP() {
		try {		
			if(stationTaskOrderPojo.getBoxId() != null){
				
				stationTaskOrderPojo.setBoxId(stationTaskOrderPojo.getBoxId());
				stationTaskOrderPojo.setWorkOrderState("010");
				stationTaskOrderPojo.setSiteName(stationTaskOrderPojo.getSiteName());
				stationTaskOrderPojo.setTaskNetworkType(stationTaskOrderPojo.getTaskNetworkType());
				stationTaskOrderPojo.setTestPointLon(String.format("%.6f", Float.valueOf(stationTaskOrderPojo.getTestPointLon())));
				stationTaskOrderPojo.setTestPointLat(String.format("%.6f", Float.valueOf(stationTaskOrderPojo.getTestPointLat())));
				stationTaskOrderPojo.setTaskInitiator("测试人员发起");
				stationTaskOrderPojo.setTaskCreatTime(stationTaskOrderPojo.getTaskCreatTime());
				stationTaskOrderPojo.setTaskTimeLimit("48小时");
				stationTaskOrderPojo.setComment(stationTaskOrderPojo.getComment());
				
				//根据boxid查询终端
				Terminal terminalDB = terminalService.getTerminal(stationTaskOrderPojo.getBoxId());
				String cityname = terminalDB.getTerminalGroup().getName();
				String wordkOrderId = queryWordkOrderId(terminalDB.getTerminalGroup().getId());
				stationTaskOrderPojo.setWorkOrderId(wordkOrderId);
				stationTaskOrderPojo.setRegion(cityname);
			
				stationTaskOrderService.update(stationTaskOrderPojo);
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
			//查找工单类型为APP侧发出，工单编号以"DY_"开头，而APP侧以"DYZ_"开头
			pageList.putParam("workOrderType", "DYZ_"+regionEnHeadChar);
			
			List<StationTaskOrderPojo> stationTaskOrderList = stationTaskOrderService.findStationTaskTask(pageList);
			
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			
			newWorkId = "DYZ_"+ regionEnHeadChar + "_" + time + "_";
			
			if(stationTaskOrderList == null || stationTaskOrderList.size() == 0){
				newWorkId = newWorkId + "00";
			}else{
				Long seqMax = null;
				for (StationTaskOrderPojo pojo : stationTaskOrderList) {
					if(StringUtils.hasText(pojo.getWorkOrderId())){
						String workId = pojo.getWorkOrderId();
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
	public String saveStationLogName() {
		try {		
			//5g
//			stationTaskOrderPojo.setWorkOrderId("DY_SMX_20200826180221_00");
//			logNames = new String[]{"20200804051622_灵宝新华书店铁塔_2_H_PING#2000.lte",
//					"20200804041622_灵宝新华书店铁塔_2_H_PING#2000.lte","20200804041622_灵宝新华书店铁塔_2_H_PING#32.lte",
//					"20200804181300_灵宝新华书店铁塔_2_H_UL.lte"};
			//4g
//			stationTaskOrderPojo.setWorkOrderId("DY_JC_20200826152950_04");
//			logNames = new String[]{"5388888220200330_A2_SQ岗头DLDS_H_2_J_VO.l5g",
//					"5388888220200330_A2_SQ岗头DLDS_H_2_J_CS.l5g","5388888220200330_A2_SQ岗头DLDS_H_3_H_DL.l5g"};
			if(stationTaskOrderPojo.getWorkOrderId() != null && logNames != null){
				List<StationTaskLogNamePojo> logNamesByOrder = stationTaskOrderService.getLogNamesByOrder(stationTaskOrderPojo.getWorkOrderId());
				Set<String> logNameSet = new HashSet<String>();
				for (StationTaskLogNamePojo taskLogNamePojo : logNamesByOrder) {
					logNameSet.add(taskLogNamePojo.getTestLogFileName());
				}
				List<String> logNameList = new ArrayList(logNameSet);
				
				if(StringUtils.hasText(logNames)){
					String[] split = logNames.replace(" ", "").split(",");
					for (String log : split) {
						if(!logNameList.contains(log)){
							logNameList.add(log);
							StationTaskLogNamePojo pojo = new StationTaskLogNamePojo();
							pojo.setWorkOrderId(stationTaskOrderPojo.getWorkOrderId());
							pojo.setTestLogFileName(log);
							stationTaskOrderService.addCQTLogName(pojo);
						}
					}
				}
				
				PageList pageList = new PageList();
				pageList.putParam("workOrderId", stationTaskOrderPojo.getWorkOrderId());
				List<StationTaskOrderPojo> stationTaskList = stationTaskOrderService.findStationTaskTask(pageList);
				if(stationTaskList.size()==1){
					StationTaskOrderPojo newStationTask =  stationTaskList.get(0);
					String cityid =  newStationTask.getTerminal().getTerminalGroup().getId().toString();
					
					creatStationTaskReport.setSitename(newStationTask.getSiteName());
					creatStationTaskReport.setNetType(newStationTask.getTaskNetworkType());
					creatStationTaskReport.setWorkOrderId(newStationTask.getWorkOrderId());
					creatStationTaskReport.setNewStationTask(newStationTask);
					new Thread(creatStationTaskReport).start();
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
	
	//下载单验任务统计报告
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
						ActionContext.getContext().getValueStack().set("errorMsg", "对应路径的任务报告不存在!");
					}
				}else{
					ActionContext.getContext().getValueStack().set("errorMsg","工单号"+stationTaskOrderPojo.getWorkOrderId()+"的报告没有生成，请先生成后再下载!");
				}
			}
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "缺少保存的必要参数,工单号为空!");
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
		// TODO Auto-generated method stub
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
