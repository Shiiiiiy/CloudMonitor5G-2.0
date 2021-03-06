/**
 * 
 */
package com.datang.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;



import org.jxls.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.datang.common.util.ClassUtil;
import com.datang.common.util.Jxls2Utils;
import com.datang.common.util.StringUtils;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dPerformanceReceivePojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dResultPojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dWirelessPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.domain.taskOrderManage.StationTaskLogNamePojo;
import com.datang.domain.taskOrderManage.StationTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.oppositeOpen.OppositeOpen3dReportCreateService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.stationParam.StationProspectParamService;
import com.datang.service.report.IReportService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.taskOrderManage.StationTaskOrderService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testLogItem.StationVerificationTestService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.util.DateUtil;
import com.datang.util.SqlCreateUtils;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.datang.web.action.oppositeOpen.OppositeOpen3dReportCreateAction;
import com.datang.web.action.stationTest.StationReportCreatAction;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.utility.StringUtil;

/**
 * ??????????????????????????????????????????????????????xls????????????????????????????????????????????????????????????APP??????????????????
 * @author lucheng
 * @date 2020???8???19??? ??????3:56:39
 */
@Controller
public class CreatStationTaskReport implements Runnable{
	
	@Autowired
	private StationReportCreatService stationReportCreatService;
	
	@Autowired
	private OppositeOpen3dReportCreateService oppositeOpen3dReportCreateService;

	@Autowired
	private TerminalMenuService terminalMenuService;
	
	@Autowired
	private StationVerificationTestService stationVerificationTestService;
	
	/**
	 *  ????????????????????????
	 */
	@Autowired
	private StationTaskOrderService stationTaskOrderService;

	/**
	 * ??????????????????
	 */
	@Autowired
	private StationProspectParamService stationProspectParamService;

	
	private String sitename;// ??????
	
	private String netType;// ??????
	
	private String workOrderId;// ????????????
	
	private String errorMsg;// ????????????
	
	private String cellNames; //?????????????????????????????????
	
	private List<String> logNameList;
	
	private StationTaskOrderPojo newStationTask;
	
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreatStationTaskReport.class);
	
	@Value("${appTaskReportFileLink}")
	private String fileSaveUrl;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Map<String, Object> map = creatStationTaskRepot();
		if(map.get("path")!=null && StringUtils.hasText((String)map.get("path"))){
			newStationTask.setFilePath((String)map.get("path"));
			stationTaskOrderService.update(newStationTask);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public Map<String,Object> creatStationTaskRepot() {
		//???????????????????????????
		List<StationTaskLogNamePojo> logNamesByOrder = stationTaskOrderService.getLogNamesByOrder(workOrderId);
		Set<String> logNameSet = new HashSet<String>();
		for (StationTaskLogNamePojo taskLogNamePojo : logNamesByOrder) {
			logNameSet.add(taskLogNamePojo.getTestLogFileName());
		}
		logNameList = new ArrayList<String>(logNameSet); 
		
		long start = new Date().getTime();
		String rltStatus = null;
		loop:while(true){
			rltStatus= "2";
			List<StationVerificationLogPojo> queryTestLogItems = stationVerificationTestService.findOfBoxidLogName(newStationTask.getBoxId(), logNameList);
			if(queryTestLogItems.size()==logNameList.size()){
				for (StationVerificationLogPojo testLogItem : queryTestLogItems) {
					//??????????????????????????????????????????????????????????????????1???
					if(testLogItem.getTestFileStatus()==null || testLogItem.getTestFileStatus()!=2){
						rltStatus = "1";
					}
				}
				if(rltStatus.equals("2")){
					System.out.println("?????????????????????????????????");
					break loop;
				}else if ((new Date().getTime() - start) > (12*60*60*1000)) {
				    break loop;
			    }
			}
			try {
				//??????5min
				System.out.println("???????????????????????????????????????5min???????????????");
				Thread.sleep(5*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break loop;
			}
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(rltStatus.equals("2")){
			if(netType.equals("0")){
				String ids = "";
				List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(sitename);
				if(planParamPojoAscList!=null && planParamPojoAscList.size()>0){
					for (int i = 0; i < planParamPojoAscList.size(); i++) {
						ids = ids + (i==(planParamPojoAscList.size()-1)
											?planParamPojoAscList.get(i).getId() : (planParamPojoAscList.get(i).getId()+","));
						
					}
					if(StringUtils.hasText(ids)){
						//????????????
						createNrReport(ids);
						if(StringUtils.hasText(cellNames)){
							System.out.println("???????????????,??????"+cellNames+"???????????????,????????????????????????!");
							map.put("cellNames", cellNames);
						}
						if(StringUtils.hasText(errorMsg)){
							System.out.println("???????????????,?????????"+errorMsg);
							map.put("errorMsg", errorMsg);
						}
						String path = getDownloadNrTestLog(ids);
						if(StringUtils.hasText(path)){
							System.out.println("??????????????????");
							map.put("path", path);
						}else{
							System.out.println("??????????????????");
						}
						return map;
						
					}
					
				}

			}else if(netType.equals("1")){
				String ids = "";
				List<Plan4GParam> oppo3dPojoList = oppositeOpen3dReportCreateService.getAllBySitename(sitename);
				if(oppo3dPojoList!=null && oppo3dPojoList .size()>0){
					for (int i = 0; i < oppo3dPojoList.size(); i++) {
						ids = ids + (i==(oppo3dPojoList.size()-1)
											?oppo3dPojoList.get(i).getId() : (oppo3dPojoList.get(i).getId()+","));
						
					}
					if(StringUtils.hasText(ids)){
						//????????????
						createLteReport(ids);
						if(StringUtils.hasText(cellNames)){
							System.out.println("???????????????,??????"+cellNames+"???????????????,????????????????????????!");
							map.put("cellNames", cellNames);
						}
						if(StringUtils.hasText(errorMsg)){
							System.out.println("???????????????,?????????"+errorMsg);
							map.put("errorMsg", errorMsg);
						}
						String path = getDownloadLteTestLog(ids);
						if(StringUtils.hasText(path)){
							System.out.println("??????????????????");
							map.put("path", path);
						}else{
							System.out.println("??????????????????");
						}
						return map;
					}
				}
			}
		}else{
			System.out.println("??????????????????");
			map.put("errorMsg", "??????????????????");
			return map;
		}
		return map;
		
	}
	
	//??????5G????????????
	public void createNrReport(String ids) {

		String[] idList = ids.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<PlanParamPojo> planList = new ArrayList<PlanParamPojo>();
		String newDate = sdf.format(new Date());
		List<Long> idLong = new ArrayList<Long>();
		cellNames = "";
		for (String id : idList) {
			PlanParamPojo plan = stationReportCreatService.find(Long.valueOf(id));
			List<EceptionCellLogPojo> cellLogList = stationReportCreatService
					.getExceptionCellLogOfCellLog(plan.getCellName(),logNameList,null,null);
			if (cellLogList == null || cellLogList.size() < 1) {
				if (!StringUtils.hasText(cellNames)) {
					cellNames = cellNames + plan.getCellName();
				} else {
					cellNames = cellNames + "," + plan.getCellName();
				}
				continue;
			}
			planList.add(plan);
			idLong.add(Long.valueOf(id));
		}

		stationReportCreatService.deleteByids(idLong);

		for (PlanParamPojo plan : planList) {
			// ??????cellName??????????????????????????????
			List<EceptionCellLogPojo> cellLogList = stationReportCreatService
					.getExceptionCellLogOfCellLog(plan.getCellName(),logNameList,null,null);
			plan.setReportCreateDate(Long.valueOf(newDate));
			stationReportCreatService.update(plan);
			if (cellLogList != null) {
				// ????????????????????????
				stationCellParamCensus(newDate, plan, cellLogList);
				// ?????????????????????????????????????????????
				netoptReceiveTest(newDate, plan, cellLogList);
				// ???????????????????????????????????????
				stationBaseParamSave(newDate, plan, cellLogList);
			}
			
		}
		
		//????????????localCellid??????????????????????????????????????????????????????????????????????????????
		List<PlanParamPojo> newPlanList = new ArrayList<PlanParamPojo>();
		for (Long id : idLong) {
			PlanParamPojo plan = stationReportCreatService.find1(Long.valueOf(id));
			newPlanList.add(plan);
		}
		for (PlanParamPojo plan : newPlanList) {
			// ???????????????????????????????????????????????????????????????????????????
			addContrastParam(plan);
			

			StationNetoptReceiveTestPojo netoptReceiveTest = null;
			StationPerformanceReceivePojo performanceReceive = null;
			StationBasicParamPojo stationBasicParamPojo = null;
			//?????????????????????
			if(plan.getStationNetoptReceiveTestList()!=null && plan.getStationNetoptReceiveTestList().size()>0 ){
				netoptReceiveTest = plan.getStationNetoptReceiveTestList().get(0);
			}
			//?????????????????????
			if(plan.getStationPerformanceReceiveList()!=null && plan.getStationPerformanceReceiveList().size()>0 ){
				performanceReceive = plan.getStationPerformanceReceiveList().get(0);
			}
			//??????????????????
			if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
				stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
			}
			
			StationProspectParamPojo stationProspectParamPojo = stationProspectParamService
					.findBySiteName(plan.getSiteName());
			
			Menu menu = terminalMenuService.get2(plan.getCity());	
			// ?????????????????? ????????????????????????????????????????????????
			if (menu == null || menu.getStationParamPojo() == null) {
				plan.setStationTemplateSelect(1);
				if (performanceReceive.getFtpDownload().equals("???") || performanceReceive.getFtpUpload().equals("???")) {
					plan.setNoPassTestEvent("FTP???????????????");
				} else if (performanceReceive.getNrEdsaddSuccessRation().equals("???")
						|| performanceReceive.getNrSwitchSuccessRation38().equals("???")) {
					plan.setNoPassTestEvent("???????????????????????????");
				} else if (performanceReceive.getPingDelay().equals("???")){
					plan.setNoPassTestEvent("Ping???????????????");
				} else {
					plan.setNoPassTestEvent("????????????");
				}

				if (stationProspectParamPojo != null) {
					if(stationBasicParamPojo.getAdjustLonContrast() == null
							|| stationBasicParamPojo.getAdjustLatContrast() == null
							|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
							|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
						plan.setStationProspectRlt("???????????????");
					} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
							|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
						plan.setStationProspectRlt("??????????????????");
					} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
							|| stationBasicParamPojo.getAdjustTiltMContrast() == null
							||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
							|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
						plan.setStationProspectRlt("??????????????????");
					} else{
						plan.setStationProspectRlt("????????????");
					}
				}
				
			} else if (menu.getStationParamPojo() != null) {
				StationParamPojo spp = menu.getStationParamPojo();
				if (spp.getSingleStationMOdelSelect()==null || "1".equals(spp.getSingleStationMOdelSelect())) { // ????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if (performanceReceive.getFtpDownload().equals("???") || performanceReceive.getFtpUpload().equals("???")) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (performanceReceive.getNrEdsaddSuccessRation().equals("???")
							|| performanceReceive.getNrSwitchSuccessRation38().equals("???")) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if (performanceReceive.getPingDelay().equals("???")){
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
					
				} else if ("2".equals(spp.getSingleStationMOdelSelect())) { // ????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if (performanceReceive.getFtpDownload().equals("???") 
							|| performanceReceive.getFtpUpload().equals("???")
							|| performanceReceive.getLteEarfcnSuccessRation().equals("???") 
							|| performanceReceive.getNrEarfcnSuccessRation().equals("???")) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (performanceReceive.getLteEpsattachSuccessRation().equals("???")
							|| performanceReceive.getNrEdsaddSuccessRation().equals("???")
							|| performanceReceive.getLteInterfreqHandoverSuccessRation().equals("???")
							|| performanceReceive.getNrEdschangeSuccessRation().equals("???")) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if (performanceReceive.getPing32SuccessRation().equals("???")
							|| performanceReceive.getPing1500SuccessRation().equals("???")) {
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
					
				} else if ("3".equals(spp.getSingleStationMOdelSelect())) { // ????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if (netoptReceiveTest.getFtpDownThrputGoog() == null
							|| Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) < spp.getUpgradeGood()
							|| netoptReceiveTest.getFtpUpThrputGoog() == null
							|| Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) < spp.getUploadGood()) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (netoptReceiveTest.getLteEpsattachSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteEpsattachSum()) < 10
							|| netoptReceiveTest.getNrEdsaddSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEdsaddSum()) < 10) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if (netoptReceiveTest.getPing32SuccessRation() == null
							|| Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) > spp.getPing32DelayTime()) {
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("?????????")){
							plan.setStationProspectRlt("AAU????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
					
					
				} else if ("4".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if (performanceReceive.getFtpDownload().equals("???") || performanceReceive.getFtpUpload().equals("???")
							|| performanceReceive.getNrEarfcnSuccessRation42().equals("???")) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (performanceReceive.getNrEdsaddSuccessRation().equals("???")
							|| performanceReceive.getNrSwitchSuccessRation38().equals("???")) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if (performanceReceive.getPingDelay().equals("???")){
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("?????????")){
							plan.setStationProspectRlt("AAU????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
					
				} else if ("5".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if (netoptReceiveTest.getLteEarfcnSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteEarfcnSum()) < 1
							|| netoptReceiveTest.getNrEarfcnSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEarfcnSum()) < 1) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (netoptReceiveTest.getLteEpsattachSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteEpsattachSum()) < 10
							|| netoptReceiveTest.getNrEdsaddSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEdsaddSum()) < 10
							|| netoptReceiveTest.getLteInterfreqHandoverSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteInterfreqHandoverSum()) < 1
							|| netoptReceiveTest.getNrEdschangeSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEdschangeSum()) < 1
							) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if(netoptReceiveTest.getPing32SuccessRation() == null
							|| Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) > spp.getPing32DelayTime()
							|| netoptReceiveTest.getPing32Success() == null
							|| Float.valueOf(netoptReceiveTest.getPing32Success()) < spp.getPing32Number()
							|| netoptReceiveTest.getPing1500SuccessRation() == null
							|| Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) > spp.getPing1500DelayTime()
							|| netoptReceiveTest.getPing1500Success() == null
							|| Float.valueOf(netoptReceiveTest.getPing1500Success()) < spp.getPing1500Nember()){
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						plan.setStationProspectRlt("????????????");
					}
					
				}  else if ("6".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if (performanceReceive.getFtpDownload().equals("???") || performanceReceive.getFtpUpload().equals("???")) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (performanceReceive.getNrSwitchSuccessAvg50().equals("???")) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if (performanceReceive.getPing32SuccessRation().equals("???")
							|| performanceReceive.getPing1500SuccessRation().equals("???")) {
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
					
				} else if ("7".equals(spp.getSingleStationMOdelSelect()) || "8".equals(spp.getSingleStationMOdelSelect())) { // ???????????????????????????????????????????????????????????????
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
					if ((netoptReceiveTest.getPdcpnrThrputDL() != null && Float.valueOf(netoptReceiveTest.getPdcpnrThrputDL()) <= 400) 
								|| (netoptReceiveTest.getPdcpnrThrputUL() != null && Float.valueOf(netoptReceiveTest.getPdcpnrThrputUL()) <= 100)) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if ((netoptReceiveTest.getNrConnectSuccessSum() != null && Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) <= 10 )
								|| (netoptReceiveTest.getNrConnectTimeDelay() != null && Float.valueOf(netoptReceiveTest.getNrConnectTimeDelay()) >= 120)) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if ((netoptReceiveTest.getPing32SuccessRation() != null && Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) >= 15)
								|| (netoptReceiveTest.getPing32Sum() != null && Float.valueOf(netoptReceiveTest.getPing32Sum()) < 10)
								|| (netoptReceiveTest.getPing1500SuccessRation() != null && Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) >= 17)
								|| (netoptReceiveTest.getPing1500Sum() != null && Float.valueOf(netoptReceiveTest.getPing1500Sum()) < 10)) {
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
					
					
				} else { //?????????????????????
					plan.setStationTemplateSelect(1);
					if (performanceReceive.getFtpDownload().equals("???") || performanceReceive.getFtpUpload().equals("???")) {
						plan.setNoPassTestEvent("FTP???????????????");
					} else if (performanceReceive.getNrEdsaddSuccessRation().equals("???")
							|| performanceReceive.getNrSwitchSuccessRation38().equals("???")) {
						plan.setNoPassTestEvent("???????????????????????????");
					} else if (performanceReceive.getPingDelay().equals("???")){
						plan.setNoPassTestEvent("Ping???????????????");
					} else {
						plan.setNoPassTestEvent("????????????");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
				}
			}
			
			stationReportCreatService.update(plan);
		
		}
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @author lucheng date:2020???6???24??? ??????3:02:31
	 * @param newDate ??????????????????
	 * @param plan ????????????
	 * @param cellLogList ????????????
	 */
	private void stationBaseParamSave(String newDate, PlanParamPojo plan, List<EceptionCellLogPojo> cellLogList) {

		// ????????????????????????localcellid???????????????????????????
		List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(plan.getSiteName());

		StationBasicParamPojo stationBasicParamPojo = new StationBasicParamPojo();
		Menu menu = terminalMenuService.get2(plan.getCity());

		stationBasicParamPojo.setRegion(plan.getCity());
		stationBasicParamPojo.setCellName(plan.getCellName());
		stationBasicParamPojo.setSiteName(plan.getSiteName());
		stationBasicParamPojo.setCellParamId(plan.getId());
		stationBasicParamPojo.setLocalCellId(plan.getLocalCellID().toString());

		// ????????????????????????
		stationBasicParamPojo.setLon(plan.getLon());
		stationBasicParamPojo.setLat(plan.getLat());
		stationBasicParamPojo.setGnbId(plan.getGnbId().toString());
		stationBasicParamPojo.setHeight(plan.getHeight());
		stationBasicParamPojo.setAzimuth(plan.getAzimuth());
		stationBasicParamPojo.setTiltE(plan.getTiltE());
		stationBasicParamPojo.setTiltM(plan.getTiltM());
		if (plan.getTiltE() != null && plan.getTiltM() != null) {
			stationBasicParamPojo.setTiltTotal(plan.getTiltE() + plan.getTiltM());
		} else if (plan.getTiltE() != null) {
			stationBasicParamPojo.setTiltTotal(plan.getTiltE());
		} else if (plan.getTiltM() != null) {
			stationBasicParamPojo.setTiltTotal(plan.getTiltM());
		}
		stationBasicParamPojo.setAauModel(plan.getAauModel());
		stationBasicParamPojo.setTac(plan.getTac());
		stationBasicParamPojo.setAntennaManufacturer(plan.getAntennaManufacturer());
		
		stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
		
		stationBasicParamPojo.setAdjustGnbId(String.valueOf(planParamPojoAscList.get(0).getGnbId()));
		
		if (stationBasicParamPojo.getGnbId() != null && stationBasicParamPojo.getAdjustGnbId() != null
				&& stationBasicParamPojo.getGnbId().equals(stationBasicParamPojo.getAdjustGnbId())) {
			stationBasicParamPojo.setAdjustGnBIdContrast("??????");
		} else {
			stationBasicParamPojo.setAdjustGnBIdContrast("?????????");
		}

		// ????????????????????????
		StationProspectParamPojo stationProspectParamPojo = stationProspectParamService
				.findBySiteName(plan.getSiteName());
		if (stationProspectParamPojo != null) {
			stationBasicParamPojo.setAdjustHeight(stationProspectParamPojo.getHeight());
			stationBasicParamPojo.setAdjustAddress(stationProspectParamPojo.getAddress());
			stationBasicParamPojo.setAdjustAauModel(stationProspectParamPojo.getAauModel());
			;
			stationBasicParamPojo.setAdjustAntennaType(stationProspectParamPojo.getAntennaType());
			stationBasicParamPojo.setAdjustLon(Float.valueOf(stationProspectParamPojo.getLongitude()));
			stationBasicParamPojo.setAdjustLat(Float.valueOf(stationProspectParamPojo.getLatitude()));

			for (int i = 0; i < planParamPojoAscList.size(); i++) {
				if (plan.getLocalCellID() == planParamPojoAscList.get(i).getLocalCellID()) {
					if (i == 0) {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell1Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell1TiltM());
						stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
						if (stationBasicParamPojo.getAdjustTiltM() != null
								&& stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(
									stationBasicParamPojo.getAdjustTiltM() + stationBasicParamPojo.getAdjustTiltE());
						} else if (stationBasicParamPojo.getAdjustTiltM() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltM());
						} else if (stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltE());
						}
					} else if (i == 1) {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell2Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell2TiltM());
						stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
						if (stationBasicParamPojo.getAdjustTiltM() != null
								&& stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(
									stationBasicParamPojo.getAdjustTiltM() + stationBasicParamPojo.getAdjustTiltE());
						} else if (stationBasicParamPojo.getAdjustTiltM() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltM());
						} else if (stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltE());
						}
					} else {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell3Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell3TiltM());
						stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
						if (stationBasicParamPojo.getAdjustTiltM() != null
								&& stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(
									stationBasicParamPojo.getAdjustTiltM() + stationBasicParamPojo.getAdjustTiltE());
						} else if (stationBasicParamPojo.getAdjustTiltM() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltM());
						} else if (stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltE());
						}
					}
				}
			}

			if (stationBasicParamPojo.getHeight() != null && stationBasicParamPojo.getAdjustHeight() != null
					&& Math.abs(stationBasicParamPojo.getHeight() - stationBasicParamPojo.getAdjustHeight()) < 3) {
				stationBasicParamPojo.setAdjustHeightContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustHeightContrast("?????????");
			}

			if (stationBasicParamPojo.getAzimuth() != null && stationBasicParamPojo.getAdjustAzimuth() != null
					&& Math.abs(stationBasicParamPojo.getAzimuth() - stationBasicParamPojo.getAdjustAzimuth()) < 10) {
				stationBasicParamPojo.setAdjustAzimuthContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustAzimuthContrast("?????????");
			}

			if (stationBasicParamPojo.getTiltTotal() != null && stationBasicParamPojo.getAdjustTiltToatal() != null
					&& Math.abs(
							stationBasicParamPojo.getTiltTotal() - stationBasicParamPojo.getAdjustTiltToatal()) < 2) {
				stationBasicParamPojo.setAdjustTiltToatalContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltToatalContrast("?????????");
			}

			if (stationBasicParamPojo.getTiltE() != null && stationBasicParamPojo.getAdjustTiltE() != null
					&& Math.abs(stationBasicParamPojo.getTiltE() - stationBasicParamPojo.getAdjustTiltE()) < 2) {
				stationBasicParamPojo.setAdjustTiltEContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltEContrast("?????????");
			}

			if (stationBasicParamPojo.getTiltM() != null && stationBasicParamPojo.getAdjustTiltM() != null
					&& Math.abs(stationBasicParamPojo.getTiltM() - stationBasicParamPojo.getAdjustTiltM()) < 2) {
				stationBasicParamPojo.setAdjustTiltMContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltMContrast("?????????");
			}

			if (stationBasicParamPojo.getAauModel() != null && stationBasicParamPojo.getAdjustAauModel() != null
					&& stationBasicParamPojo.getAauModel().equals(stationBasicParamPojo.getAdjustAauModel())) {
				stationBasicParamPojo.setAdjustAauModelContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustAauModelContrast("?????????");
			}

			if (stationBasicParamPojo.getLon() != null && stationBasicParamPojo.getAdjustLon() != null
					&& String.format("%.4f", stationBasicParamPojo.getLon())
							.equals(String.format("%.4f", stationBasicParamPojo.getAdjustLon()))) {
				stationBasicParamPojo.setAdjustLonContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustLonContrast("?????????");
			}

			if (stationBasicParamPojo.getLat() != null && stationBasicParamPojo.getAdjustLat() != null
					&& String.format("%.4f", stationBasicParamPojo.getLat())
							.equals(String.format("%.4f", stationBasicParamPojo.getAdjustLat()))) {
				stationBasicParamPojo.setAdjustLatContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustLatContrast("?????????");
			}

			if (stationBasicParamPojo.getLon() != null && stationBasicParamPojo.getAdjustLon() != null
					&& stationBasicParamPojo.getLat() != null && stationBasicParamPojo.getAdjustLat() != null) {
				Double value = Math
						.sqrt(Math.pow(((Math.abs(stationBasicParamPojo.getAdjustLon() - stationBasicParamPojo.getLon()) * 110000)
								+ (Math.abs(stationBasicParamPojo.getAdjustLat() - stationBasicParamPojo.getLat())* 110000*Math.cos(stationBasicParamPojo.getAdjustLat()))),2));
				stationBasicParamPojo.setLonLatCompute(String.valueOf(value));
			} else {
				stationBasicParamPojo.setLonLatCompute(null);
			}

		} else {
			errorMsg = errorMsg+" ??????'"+plan.getSiteName()+"'?????????????????????????????????! ";
		}

		stationBasicParamPojo.setPlanParamPojo(plan);
		stationReportCreatService.createStationBasicParamTest(stationBasicParamPojo);
	}

	/**
	 * ?????????????????????/?????????????????????
	 * 
	 * @author maxuancheng date:2020???2???22??? ??????5:02:31
	 * @author lucheng date:2020???6???24?????????
	 * @param newDate
	 *            ??????????????????
	 * @param plan
	 *            ????????????
	 * @param cellLogList
	 *            ????????????
	 */
	private void netoptReceiveTest(String newDate, PlanParamPojo plan, List<EceptionCellLogPojo> cellLogList) {
		StationNetoptReceiveTestPojo netoptReceiveTest = new StationNetoptReceiveTestPojo();
		StationPerformanceReceivePojo performanceReceive = new StationPerformanceReceivePojo();
		Menu menu = terminalMenuService.get(plan.getCity());

		netoptReceiveTest.setCellName(plan.getCellName());
		netoptReceiveTest.setSiteName(plan.getSiteName());
		netoptReceiveTest.setCellParamId(plan.getId());
		netoptReceiveTest.setLocalCellId(plan.getLocalCellID().toString());

		performanceReceive.setCellName(plan.getCellName());
		performanceReceive.setSiteName(plan.getSiteName());
		performanceReceive.setCellParamId(plan.getId());
		performanceReceive.setLocalCellId(plan.getLocalCellID().toString());

		Long testTimeEndc = null;
		List<EceptionCellLogPojo> endcMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeRao = null;
		List<EceptionCellLogPojo> raoMap = new ArrayList<EceptionCellLogPojo>();
		
		Long testTimeRaoDl = null;
		List<EceptionCellLogPojo> raoDLMap = new ArrayList<EceptionCellLogPojo>();

		// Long testTimeFtpUpOrDown = null;
		// List<FixedPointTaskOrderPojo> ftpUpOrDownMap = new
		// ArrayList<FixedPointTaskOrderPojo>();

		Long testTimeDtUlDl = null;
		List<EceptionCellLogPojo> dtUlDlMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimePing1500 = null;
		List<EceptionCellLogPojo> ping1500Map = new ArrayList<EceptionCellLogPojo>();

		Long testTimePing32 = null;
		List<EceptionCellLogPojo> ping32Map = new ArrayList<EceptionCellLogPojo>();
		
		Long testTimePingSum = null;
		List<EceptionCellLogPojo> pingSumMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeFtpUpGood = null;
		List<EceptionCellLogPojo> ftpUpGoodMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeFtpUpMid = null;
		List<EceptionCellLogPojo> ftpUpMidMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeFtpUpBad = null;
		List<EceptionCellLogPojo> ftpUpBadMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeFtpDownGood = null;
		List<EceptionCellLogPojo> ftpDownGoodMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeFtpDownMid = null;
		List<EceptionCellLogPojo> ftpDownMidMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeFtpDownBad = null;
		List<EceptionCellLogPojo> ftpDownBadMap = new ArrayList<EceptionCellLogPojo>();

		Long testTimeDtDLGood = null;
		List<EceptionCellLogPojo> dtDLGoodMap = new ArrayList<EceptionCellLogPojo>();
		
		Long testTimeDtULGood = null;
		List<EceptionCellLogPojo> dtULGoodMap = new ArrayList<EceptionCellLogPojo>();
		
		Long testTimeftpDLGoodBest = null;
		List<EceptionCellLogPojo> ftpDLGoodBestMap = new ArrayList<EceptionCellLogPojo>();
		
		Long testTimeftpULGoodBest = null;
		List<EceptionCellLogPojo> ftpULGoodBestMap = new ArrayList<EceptionCellLogPojo>();
		
		Long testTimeDialVolte = null;
		List<EceptionCellLogPojo> dialVolteMap = new ArrayList<EceptionCellLogPojo>();

		// ????????????????????????????????????????????????????????????????????????????????????????????????XXXX????????????
		for (EceptionCellLogPojo log : cellLogList) {
			if (log == null || !StringUtils.hasText(log.getNrTestevent())) {
				continue;
			}
			if (log.getNrTestevent().equals("ENDC???????????????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeEndc == null) {
					testTimeEndc = Long.valueOf(log.getNrTestdate());
					endcMap.add(log);
				} else if (Math.abs(testTimeEndc - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeEndc = Long.valueOf(log.getNrTestdate());
					endcMap.clear();
					endcMap.add(log);
				} else if (Math.abs(testTimeEndc - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					endcMap.add(log);
				}
			}

			if (log.getNrTestevent().equals("??????_??????")||log.getNrTestevent().equals("??????_??????")) {
				if (testTimeRao == null) {
					testTimeRao = Long.valueOf(log.getNrTestdate());
					raoMap.add(log);
				} else if (Math.abs(testTimeRao - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeRao = Long.valueOf(log.getNrTestdate());
					raoMap.clear();
					raoMap.add(log);
				} else if (Math.abs(testTimeRao - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					raoMap.add(log);
				}
			}
			
			if (log.getNrTestevent().equals("??????_??????")) {
				if (testTimeRaoDl == null) {
					testTimeRaoDl = Long.valueOf(log.getNrTestdate());
					raoDLMap.add(log);
				} else if (Math.abs(testTimeRaoDl - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeRaoDl = Long.valueOf(log.getNrTestdate());
					raoDLMap.clear();
					raoDLMap.add(log);
				} else if (Math.abs(testTimeRaoDl - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					raoDLMap.add(log);
				}
			}

			if (log.getNrTestevent().equals("FTP??????") || equals("FTP??????")) {
				if (testTimeDtUlDl == null) {
					testTimeDtUlDl = Long.valueOf(log.getNrTestdate());
					dtUlDlMap.add(log);
				} else if (Math.abs(testTimeDtUlDl - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeDtUlDl = Long.valueOf(log.getNrTestdate());
					dtUlDlMap.clear();
					dtUlDlMap.add(log);
				} else if (Math.abs(testTimeDtUlDl - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					dtUlDlMap.add(log);
				}
			}

			if (log.getNrTestevent().contains("PING") && log.getNrTestevent().contains("???") && log.getNrTestevent().contains("???")  && log.getNrWirelessstation().equals("??????")) {
				String pingName = log.getNrTestevent().substring(log.getNrTestevent().indexOf("???")+1, log.getNrTestevent().indexOf("???")) ;
				if(StringUtils.hasText(pingName)){
					if (Float.valueOf(pingName)>=200) { //ping1500??????
						if (testTimePing1500 == null) {
							testTimePing1500 = Long.valueOf(log.getNrTestdate());
							ping1500Map.add(log);
						} else if (Math.abs(testTimePing1500 - Long.valueOf(newDate)) > Math
								.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
							testTimePing1500 = Long.valueOf(log.getNrTestdate());
							ping1500Map.clear();
							ping1500Map.add(log);
						} else if (Math.abs(testTimePing1500 - Long.valueOf(newDate)) == Math
								.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
							ping1500Map.add(log);
						}
					}else if (Float.valueOf(pingName)<200) { //ping32??????
						if (testTimePing32 == null) {
							testTimePing32 = Long.valueOf(log.getNrTestdate());
							ping32Map.add(log);
						} else if (Math.abs(testTimePing32 - Long.valueOf(newDate)) > Math
								.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
							testTimePing32 = Long.valueOf(log.getNrTestdate());
							ping32Map.clear();
							ping32Map.add(log);
						} else if (Math.abs(testTimePing32 - Long.valueOf(newDate)) == Math
								.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
							ping32Map.add(log);
						}
					}
				}
			}
			
			if (log.getNrTestevent().contains("PING") && log.getNrTestevent().contains("???") && log.getNrTestevent().contains("???")  && log.getNrWirelessstation().equals("??????")) {
					if (testTimePingSum == null) {
						testTimePingSum = Long.valueOf(log.getNrTestdate());
						pingSumMap.add(log);
					} else if (Math.abs(testTimePingSum - Long.valueOf(newDate)) > Math
							.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
						testTimePingSum = Long.valueOf(log.getNrTestdate());
						pingSumMap.clear();
						pingSumMap.add(log);
					} else if (Math.abs(testTimePingSum - Long.valueOf(newDate)) == Math
							.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
						pingSumMap.add(log);
					}
			}


			if (log.getNrTestevent().equals("FTP??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeFtpDownGood == null) {
					testTimeFtpDownGood = Long.valueOf(log.getNrTestdate());
					ftpDownGoodMap.add(log);
				} else if (Math.abs(testTimeFtpDownGood - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeFtpDownGood = Long.valueOf(log.getNrTestdate());
					ftpDownGoodMap.clear();
					ftpDownGoodMap.add(log);
				} else if (Math.abs(testTimeFtpDownGood - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpDownGoodMap.add(log);
				}
			}

			if (log.getNrTestevent().equals("FTP??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeFtpDownMid == null) {
					testTimeFtpDownMid = Long.valueOf(log.getNrTestdate());
					ftpDownMidMap.add(log);
				} else if (Math.abs(testTimeFtpDownMid - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeFtpDownMid = Long.valueOf(log.getNrTestdate());
					ftpDownMidMap.clear();
					ftpDownMidMap.add(log);
				} else if (Math.abs(testTimeFtpDownMid - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpDownMidMap.add(log);
				}
			}

			if (log.getNrTestevent().equals("FTP??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeFtpDownBad == null) {
					testTimeFtpDownBad = Long.valueOf(log.getNrTestdate());
					ftpDownBadMap.add(log);
				} else if (Math.abs(testTimeFtpDownBad - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeFtpDownBad = Long.valueOf(log.getNrTestdate());
					ftpDownBadMap.clear();
					ftpDownBadMap.add(log);
				} else if (Math.abs(testTimeFtpDownBad - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpDownBadMap.add(log);
				}
			}

			if (log.getNrTestevent().equals("FTP??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeFtpUpGood == null) {
					testTimeFtpUpGood = Long.valueOf(log.getNrTestdate());
					ftpUpGoodMap.add(log);
				} else if (Math.abs(testTimeFtpUpGood - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeFtpUpGood = Long.valueOf(log.getNrTestdate());
					ftpUpGoodMap.clear();
					ftpUpGoodMap.add(log);
				} else if (Math.abs(testTimeFtpUpGood - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpUpGoodMap.add(log);
				}
			}
			if (log.getNrTestevent().equals("FTP??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeFtpUpMid == null) {
					testTimeFtpUpMid = Long.valueOf(log.getNrTestdate());
					ftpUpMidMap.add(log);
				} else if (Math.abs(testTimeFtpUpMid - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeFtpUpMid = Long.valueOf(log.getNrTestdate());
					ftpUpMidMap.clear();
					ftpUpMidMap.add(log);
				} else if (Math.abs(testTimeFtpUpMid - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpUpMidMap.add(log);
				}
			}
			if (log.getNrTestevent().equals("FTP??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeFtpUpBad == null) {
					testTimeFtpUpBad = Long.valueOf(log.getNrTestdate());
					ftpUpBadMap.add(log);
				} else if (Math.abs(testTimeFtpUpBad - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeFtpUpBad = Long.valueOf(log.getNrTestdate());
					ftpUpBadMap.clear();
					ftpUpBadMap.add(log);
				} else if (Math.abs(testTimeFtpUpBad - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpUpBadMap.add(log);
				}
			}
			
			if (log.getNrTestevent().equals("??????_??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeDtDLGood == null) {
					testTimeDtDLGood = Long.valueOf(log.getNrTestdate());
					dtDLGoodMap.add(log);
				} else if (Math.abs(testTimeDtDLGood - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeDtDLGood = Long.valueOf(log.getNrTestdate());
					dtDLGoodMap.clear();
					dtDLGoodMap.add(log);
				} else if (Math.abs(testTimeDtDLGood - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					dtDLGoodMap.add(log);
				}
			}
			
			if (log.getNrTestevent().equals("??????_??????") && log.getNrWirelessstation().equals("??????")) {
				if (testTimeDtULGood == null) {
					testTimeDtULGood = Long.valueOf(log.getNrTestdate());
					dtULGoodMap.add(log);
				} else if (Math.abs(testTimeDtULGood - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeDtULGood = Long.valueOf(log.getNrTestdate());
					dtULGoodMap.clear();
					dtULGoodMap.add(log);
				} else if (Math.abs(testTimeDtULGood - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					dtULGoodMap.add(log);
				}
			}
			
			if (log.getNrTestevent().equals("FTP??????") && (log.getNrWirelessstation().equals("??????") || log.getNrWirelessstation().equals("?????????"))) {
				if (testTimeftpDLGoodBest == null) {
					testTimeftpDLGoodBest = Long.valueOf(log.getNrTestdate());
					ftpDLGoodBestMap.add(log);
				} else if (Math.abs(testTimeftpDLGoodBest - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeftpDLGoodBest = Long.valueOf(log.getNrTestdate());
					ftpDLGoodBestMap.clear();
					ftpDLGoodBestMap.add(log);
				} else if (Math.abs(testTimeftpDLGoodBest - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpDLGoodBestMap.add(log);
				}
			}
			
			if (log.getNrTestevent().equals("FTP??????") && (log.getNrWirelessstation().equals("??????") || log.getNrWirelessstation().equals("?????????"))) {
				if (testTimeftpULGoodBest == null) {
					testTimeftpULGoodBest = Long.valueOf(log.getNrTestdate());
					ftpULGoodBestMap.add(log);
				} else if (Math.abs(testTimeftpULGoodBest - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeftpULGoodBest = Long.valueOf(log.getNrTestdate());
					ftpULGoodBestMap.clear();
					ftpULGoodBestMap.add(log);
				} else if (Math.abs(testTimeftpULGoodBest - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					ftpULGoodBestMap.add(log);
				}
			}
			
			if (log.getNrTestevent().equals("volte??????") && (log.getNrWirelessstation().equals("??????") || log.getNrWirelessstation().equals("?????????"))) {
				if (testTimeDialVolte == null) {
					testTimeDialVolte = Long.valueOf(log.getNrTestdate());
					dialVolteMap.add(log);
				} else if (Math.abs(testTimeDialVolte - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTimeDialVolte = Long.valueOf(log.getNrTestdate());
					dialVolteMap.clear();
					dialVolteMap.add(log);
				} else if (Math.abs(testTimeDialVolte - Long.valueOf(newDate)) == Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					dialVolteMap.add(log);
				}
			}
			
		}
		
		Long lteEpsattachSuccess = 0L;// 4G??????????????????????????????
		Long nrEdsaddSuccess = 0L;// 5G??????????????????????????????
		Long lteInterfreqSuccess = 0L;// 4G????????????????????????
		Long nrEdschangeSuccess = 0L;// 5G????????????????????????
		Long rrcConnectionSuccess = 0L;// 4G??????RRC??????????????????
		Long lteEarfcnSuccess = 0L;// dt???dl???ul???4G??????????????????
		Long nrEarfcnSuccess = 0L;// dt???dl???ul???5G????????????
		Long dtLteEarfcnSuccessSum = 0L; // dt???4G??????????????????
		Long dtNREarfcnSuccessSum = 0L; // dt???5G????????????
		Long dtLteEarfcnFailSum = 0L; // dt???4G??????????????????
		Long dtNREarfcnFailSum = 0L; // dt???5G????????????
		Long dialVolteSuccessSum = 0L; // ??????volte????????????
		Long dialVonrSuccessSum = 0L; // ??????VONR?????????
		Long nrConectSuccess2Sum = 0L; // NR????????????????????????2
		
		Long nrSwitchSuccessSum = 0L; // NR??????????????????
		Long nrSwitchSum = 0L; // NR??????????????????+NR??????????????????

		Long ping1500 = 0L;// ping1500????????????
		int ping1500Number = 0;// ping1500?????????????????????
		Float nrSamplenumber1500 = 0F;// ping1500?????????????????????
		Long ping32 = 0L;// ping32????????????
		int ping32Number = 0;// ping1500?????????????????????
		Float nrSamplenumber32 = 0F;// ping1500?????????????????????

		// ???????????????
		for (EceptionCellLogPojo log : endcMap) {
			if (testTimeEndc != null && log.getNrTestdate().equals(testTimeEndc.toString())) {
				if (log.getLteEpsattachfailure() != null && Long.valueOf(log.getLteEpsattachfailure()) == 0) {
					if(log.getLteEpsattachsuccess()!=null){
						lteEpsattachSuccess = lteEpsattachSuccess + Long.valueOf(log.getLteEpsattachsuccess());
					}
				}
				if (log.getNrEdsaddfailure() != null && Long.valueOf(log.getNrEdsaddfailure()) == 0) {
					if(log.getNrEdsaddsuccess()!=null){
						nrEdsaddSuccess = nrEdsaddSuccess + Long.valueOf(log.getNrEdsaddsuccess());
					}
				}
			}
		}

		for (EceptionCellLogPojo log : raoMap) {
			if (testTimeRao != null && log.getNrTestdate().equals(testTimeRao.toString())) {
				if (log.getLteInterfreqhandoverfailure() != null
						&& Long.valueOf(log.getLteInterfreqhandoverfailure()) == 0) {
					if(log.getLteInterfreqhandoversuccess()!=null){
						lteInterfreqSuccess = lteInterfreqSuccess + Long.valueOf(log.getLteInterfreqhandoversuccess());
					}
				}
				if (log.getNrEdschangefailure() != null && Long.valueOf(log.getNrEdschangefailure()) == 0) {
					if(log.getNrEdschangesuccess()!=null){
						nrEdschangeSuccess = nrEdschangeSuccess + Long.valueOf(log.getNrEdschangesuccess());
					}
				}
				if (log.getRrcConnectionfailure() != null && Long.valueOf(log.getRrcConnectionfailure()) == 0) {
					if(log.getRrcConnectionsuccess()!=null){
						rrcConnectionSuccess = rrcConnectionSuccess + Long.valueOf(log.getRrcConnectionsuccess());
					}
				}

				if(log.getLteEarfcnsuccess()!=null){
					dtLteEarfcnSuccessSum = dtLteEarfcnSuccessSum + Long.valueOf(log.getLteEarfcnsuccess());
				}
				if(log.getNrEarfcnsuccess()!=null){
					dtNREarfcnSuccessSum = dtNREarfcnSuccessSum + Long.valueOf(log.getNrEarfcnsuccess());
				}
				if(log.getLteEarfcndrop()!=null){
					dtLteEarfcnFailSum = dtLteEarfcnFailSum + Long.valueOf(log.getLteEarfcndrop());
				}
				if(log.getNrEarfcndrop()!=null){
					dtNREarfcnFailSum = dtNREarfcnFailSum + Long.valueOf(log.getNrEarfcndrop());
				}
				
				if(log.getNrSwitchSuccess()!=null){
					nrSwitchSuccessSum = nrSwitchSuccessSum + Long.valueOf(log.getNrSwitchSuccess());
					nrSwitchSum = nrSwitchSum + Long.valueOf(log.getNrSwitchSuccess());
				}
				if(log.getNrSwitchDrop()!=null){
					nrSwitchSum = nrSwitchSum + Long.valueOf(log.getNrSwitchDrop());
				}
			}
		}

		for (EceptionCellLogPojo log : dtUlDlMap) {
			if (log.getLteEarfcndrop() != null && Long.valueOf(log.getLteEarfcndrop()) == 0) {
				if(log.getLteEarfcnsuccess()!=null){
					lteEarfcnSuccess = lteEarfcnSuccess + Long.valueOf(log.getLteEarfcnsuccess());
				}
			}
			if (log.getNrEarfcndrop() != null && Long.valueOf(log.getNrEarfcndrop()) == 0) {
				if(log.getNrEarfcnsuccess()!=null){
					nrEarfcnSuccess = nrEarfcnSuccess + Long.valueOf(log.getNrEarfcnsuccess());
				}
			}
		}
		
		for(EceptionCellLogPojo log : dialVolteMap){
			if(log.getVolteSuccess()!=null && log.getVolteDrop() != null && log.getVolteDrop().equals("0")){
				dialVolteSuccessSum = dialVolteSuccessSum + Long.valueOf(log.getVolteSuccess());
			}
			
			if(log.getDialVonrSuccess()!=null && log.getDialVonrDrop() != null && log.getDialVonrDrop().equals("0")){
				dialVonrSuccessSum = dialVonrSuccessSum + Long.valueOf(log.getDialVonrSuccess());
			}
			
		}

		for (EceptionCellLogPojo log : ping1500Map) {
			if (testTimePing1500 != null && log.getNrTestdate().equals(testTimePing1500.toString())) {
				if (log.getNrPingresqtime() != null && Integer.valueOf(log.getNrPingresqtime()) <= 100) {
					ping1500 = ping1500 + Long.valueOf(log.getNrPingrespose());
					ping1500Number = ping1500Number + Integer.valueOf(log.getNrPingrespose());
					// nrSamplenumber1500 = nrSamplenumber1500 +
					// Float.valueOf(log.getNrSamplenumber());
					nrSamplenumber1500 = nrSamplenumber1500
							+ Integer.valueOf(log.getNrPingrespose()) * Float.valueOf(log.getNrPingresqtime());
				}
			}
		}
		
		for (EceptionCellLogPojo log : ping32Map) {
			if (testTimePing32 != null && log.getNrTestdate().equals(testTimePing32.toString())) {
				if (log.getNrPingresqtime() != null && Integer.valueOf(log.getNrPingresqtime()) <= 100) {
					ping32 = ping32 + Long.valueOf(log.getNrPingrespose());
					ping32Number = ping32Number + Integer.valueOf(log.getNrPingrespose());
					// nrSamplenumber32 = nrSamplenumber32 +
					// Float.valueOf(log.getNrSamplenumber());
					nrSamplenumber32 = nrSamplenumber32
							+ Integer.valueOf(log.getNrPingrespose()) * Float.valueOf(log.getNrPingresqtime());
				}
			}
		}
		
		for (EceptionCellLogPojo log : pingSumMap) {
			if (testTimePingSum != null && log.getNrTestdate().equals(testTimePingSum.toString())) {
				if(log.getNrEdsFailureSecond() != null && Long.valueOf(log.getNrEdsFailureSecond()) == 0){
					if(log.getNrEdsSuccessSecond()!=null){
						nrConectSuccess2Sum = nrConectSuccess2Sum + Long.valueOf(log.getNrEdsSuccessSecond());
					}
				}
			}
		}

		// 4G???????????????????????????
		netoptReceiveTest.setLteEpsattachSuccess(lteEpsattachSuccess.toString());
		netoptReceiveTest.setLteEpsattachFailure("0");
		netoptReceiveTest.setLteEpsattachSum(lteEpsattachSuccess.toString());
		netoptReceiveTest.setLteEpsattachSuccessRation("100");
		// 5G????????????????????????gNB??????????????????
		netoptReceiveTest.setNrEdsaddSuccess(nrEdsaddSuccess.toString());
		netoptReceiveTest.setNrEdsaddFailure("0");
		netoptReceiveTest.setNrEdsaddSum(nrEdsaddSuccess.toString());
		netoptReceiveTest.setNrEdsaddSuccessRation("100");
		// 4G????????????????????????Long lteInterfreqSuccess = 0L;
		netoptReceiveTest.setLteInterfreqHandoverSuccess(lteInterfreqSuccess.toString());
		netoptReceiveTest.setLteInterfreqHandoverFailure("0");
		netoptReceiveTest.setLteInterfreqHandoverSum(lteInterfreqSuccess.toString());
		netoptReceiveTest.setLteInterfreqHandoverSuccessRation("100");
		// 5G????????????????????????Long nrEdschangeSuccess = 0L;
		netoptReceiveTest.setNrEdschangeSuccess(nrEdschangeSuccess.toString());
		netoptReceiveTest.setNrEdschangeFailure("0");
		netoptReceiveTest.setNrEdschangeSum(nrEdschangeSuccess.toString());
		netoptReceiveTest.setNrEdschangeSuccessRation("100");
		// 4G??????RRC??????????????????Long rrcConnectionSuccess = 0L;
		netoptReceiveTest.setRrcConnectionSuccess(rrcConnectionSuccess.toString());
		netoptReceiveTest.setRrcConnectionFailure("0");
		netoptReceiveTest.setRrcConnectionSum(rrcConnectionSuccess.toString());
		netoptReceiveTest.setRrcConnectionSuccessRation("100");
		// 4G??????????????????Long lteEarfcnSuccess = 0L;
		netoptReceiveTest.setLteEarfcnSuccess(lteEarfcnSuccess.toString());
		netoptReceiveTest.setLteEarfcnFailure("0");
		netoptReceiveTest.setLteEarfcnSum(lteEarfcnSuccess.toString());
		netoptReceiveTest.setLteEarfcnSuccessRation("100");
		// 5G????????????Long nrEarfcnSuccess = 0L;
		netoptReceiveTest.setNrEarfcnSuccess(nrEarfcnSuccess.toString());
		netoptReceiveTest.setNrEarfcnFailure("0");
		netoptReceiveTest.setNrEarfcnSum(nrEarfcnSuccess.toString());
		netoptReceiveTest.setNrEarfcnSuccessRation("100");

		// ???????????????????????????????????????????????????4G????????????????????? Long dtLteEarfcnSuccessSum = 0L;
		netoptReceiveTest.setDtLteEarfcnSuccessSum(dtLteEarfcnSuccessSum.toString());
		// ???????????????????????????????????????????????????4G??????????????? Long dtLteEarfcnFailSum = 0L;
		netoptReceiveTest.setDtLteFailSumDegree(dtLteEarfcnFailSum.toString());
		netoptReceiveTest.setDtLteSumDegree(String.valueOf(dtLteEarfcnSuccessSum + dtLteEarfcnFailSum));
		netoptReceiveTest.setDtLteEarfcnFailRation(
				(dtLteEarfcnSuccessSum + dtLteEarfcnFailSum) == 0 ? null : String.valueOf(dtLteEarfcnFailSum / (dtLteEarfcnSuccessSum + dtLteEarfcnFailSum)*100));

		// ???????????????????????????????????????????????????5G??????????????? Long dtNREarfcnSuccessSum = 0L;
		netoptReceiveTest.setDtNRSuccessSum(dtNREarfcnSuccessSum.toString());
		// ???????????????????????????????????????????????????4G??????????????? Long dtNREarfcnFailSum = 0L;
		netoptReceiveTest.setDtNrFailSumDegree(dtNREarfcnFailSum.toString());
		netoptReceiveTest.setDtNRSumDegree(String.valueOf(dtNREarfcnSuccessSum + dtNREarfcnFailSum));
		netoptReceiveTest.setDtNREarfcnFailRation(
				(dtNREarfcnSuccessSum + dtNREarfcnFailSum) == 0 ? null : String.valueOf(dtNREarfcnFailSum / (dtNREarfcnSuccessSum + dtNREarfcnFailSum)*100));

		// ping1500????????????Long ping1500 = 0L;
		netoptReceiveTest.setPing1500Success(ping1500.toString());
		netoptReceiveTest.setPing1500Failure("0");
		netoptReceiveTest.setPing1500Sum(ping1500.toString());
		netoptReceiveTest.setPing1500SuccessRation(
				ping1500Number == 0 ? null : String.format("%.2f", (nrSamplenumber1500 / ping1500Number)));
		// ping32????????????Long ping32 = 0L;
		netoptReceiveTest.setPing32Success(ping32.toString());
		netoptReceiveTest.setPing32Failure("0");
		netoptReceiveTest.setPing32Sum(ping32.toString());
		netoptReceiveTest.setPing32SuccessRation(
				ping32Number == 0 ? null : String.format("%.2f", (nrSamplenumber32 / ping32Number)));

		// ??????56 1-???30??????4G???????????????????????? /???????????? ?????????1-100%
		netoptReceiveTest.setLteInterfreqHandoverfailRation("0");
		
		// ??????76 ??????volte????????????
		netoptReceiveTest.setDialVolteSuccessSum(dialVolteSuccessSum.toString());
		
		// ??????77 ??????vonr????????????
		netoptReceiveTest.setDialVonrSuccessSum(dialVonrSuccessSum.toString());

		// ??????78 ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????NR?????????????????????????????????NR?????????????????????+???NR??????????????????????????????????????????????????????????????????????????????????????????????????????
		netoptReceiveTest.setNrSwitchSuccessRate(
				nrSwitchSum == 0 ? null : String.format("%.2f",(nrSwitchSuccessSum/nrSwitchSum)));
		
		//??????81 nr??????????????????
		netoptReceiveTest.setNrConnectSuccessSum(nrConectSuccess2Sum.toString());

		
		//??????75 ????????????????????????????????????_???????????????????????????????????????
		for (EceptionCellLogPojo log : raoDLMap) {
			if (netoptReceiveTest.getDtWeakCoverRateMin() == null) {
				if(log.getWeakCoverRate()!=null && StringUtils.hasText(log.getWeakCoverRate())){
					netoptReceiveTest.setDtWeakCoverRateMin(log.getWeakCoverRate());
				}
			} else if (log.getWeakCoverRate() != null && StringUtils.hasText(log.getWeakCoverRate())
					&& Float.valueOf(netoptReceiveTest.getDtWeakCoverRateMin()) > Float.valueOf(log.getWeakCoverRate())) {
				netoptReceiveTest.setDtWeakCoverRateMin(log.getWeakCoverRate());
			}
		}
	
		
		for (EceptionCellLogPojo log : ftpDLGoodBestMap) {
			//??????73 ?????????????????????????????????????????????????????????????????????????????????FTP????????????????????????NR???????????????max???
			if (netoptReceiveTest.getFtpNrThrouDLRateMax() == null && log.getNrThrputDLMax()!=null) {
				netoptReceiveTest.setFtpNrThrouDLRateMax(log.getNrThrputDLMax().toString());
			} else if (log.getNrThrputDLMax() != null
					&& Float.valueOf(netoptReceiveTest.getFtpNrThrouDLRateMax()) < Float.valueOf(log.getNrThrputDLMax())) {
				netoptReceiveTest.setFtpNrThrouDLRateMax(log.getNrThrputDLMax().toString());
			}
			
			//??????83 ??????????????????????????????????????????????????????????????????????????????FTP?????????????????????NR PDSCH DMRS RSRP??????????????????????????????????????????????????????
			if (netoptReceiveTest.getNrPdschRsrpFtpDL() == null && log.getNrPdschDmrsRsrpAvg()!=null) {
				netoptReceiveTest.setNrPdschRsrpFtpDL(log.getNrPdschDmrsRsrpAvg().toString());
			} else if (log.getNrPdschDmrsRsrpAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschRsrpFtpDL()) < log.getNrPdschDmrsRsrpAvg()) {
				netoptReceiveTest.setNrPdschRsrpFtpDL(log.getNrPdschDmrsRsrpAvg().toString());
			}
			
			//??????84 ??????????????????????????????????????????????????????????????????????????????FTP????????????????????????NR PDCCH DMRS SINR??????????????????????????????????????????????????????
			if (netoptReceiveTest.getNrPdschSinrFtpDL() == null && log.getNrPdschDmrsSinrAvg()!=null) {
				netoptReceiveTest.setNrPdschSinrFtpDL(log.getNrPdschDmrsSinrAvg().toString());
			} else if (log.getNrPdschDmrsSinrAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschSinrFtpDL()) > log.getNrPdschDmrsSinrAvg()) {
				netoptReceiveTest.setNrPdschSinrFtpDL(log.getNrPdschDmrsSinrAvg().toString());
			}
			
		}
		
		for (EceptionCellLogPojo log : ftpULGoodBestMap) {
			//??????74 ?????????????????????????????????????????????????????????????????????????????????FTP????????????????????????NR???????????????max???
			if (netoptReceiveTest.getFtpNrThrouULRateMax() == null && log.getNrThrputULMax()!=null) {
				netoptReceiveTest.setFtpNrThrouULRateMax(log.getNrThrputULMax().toString());
			} else if (log.getNrThrputULMax() != null
					&& Float.valueOf(netoptReceiveTest.getFtpNrThrouULRateMax()) < Float.valueOf(log.getNrThrputULMax())) {
				netoptReceiveTest.setFtpNrThrouULRateMax(log.getNrThrputULMax().toString());
			}
			
			//??????85 ??????????????????????????????????????????????????????????????????????????????FTP?????????????????????NR PDSCH DMRS RSRP??????????????????????????????????????????????????????
			if (netoptReceiveTest.getNrPdschRsrpFtpUL() == null && log.getNrPdschDmrsRsrpAvg()!=null) {
				netoptReceiveTest.setNrPdschRsrpFtpUL(log.getNrPdschDmrsRsrpAvg().toString());
			} else if (log.getNrPdschDmrsRsrpAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschRsrpFtpUL()) < log.getNrPdschDmrsRsrpAvg()) {
				netoptReceiveTest.setNrPdschRsrpFtpUL(log.getNrPdschDmrsRsrpAvg().toString());
			}
			
			//??????86 ??????????????????????????????????????????????????????????????????????????????FTP????????????????????????NR PDCCH DMRS SINR??????????????????????????????????????????????????????
			if (netoptReceiveTest.getNrPdschSinrFtpUL() == null && log.getNrPdschDmrsSinrAvg()!=null) {
				netoptReceiveTest.setNrPdschSinrFtpUL(log.getNrPdschDmrsSinrAvg().toString());
			} else if (log.getNrPdschDmrsSinrAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschSinrFtpUL()) > log.getNrPdschDmrsSinrAvg()) {
				netoptReceiveTest.setNrPdschSinrFtpUL(log.getNrPdschDmrsSinrAvg().toString());
			}
		}
		
		//??????82 ??????????????????????????????ping???32?????????????????????ping???1500????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????NR???????????????????????????????????????????????????????????????????????????
		for (EceptionCellLogPojo log : pingSumMap) {
			if (netoptReceiveTest.getNrConnectTimeDelay() == null && log.getNrConnectTimeDelay()!=null) {
				netoptReceiveTest.setNrConnectTimeDelay(log.getNrConnectTimeDelay());
			} else if (log.getNrConnectTimeDelay() != null
					&& Float.valueOf(netoptReceiveTest.getNrConnectTimeDelay()) > Float.valueOf(log.getNrConnectTimeDelay())) {
				netoptReceiveTest.setNrConnectTimeDelay(log.getNrConnectTimeDelay());
			}
		}
		
		// ?????????????????????????????????????????????????????????????????????_?????????
		for (EceptionCellLogPojo log : dtDLGoodMap) {
			// 65:???????????????????????????????????????????????????????????????????????????????????????NR rsrp??????????????????????????????????????????????????????
			if (netoptReceiveTest.getDtNrSsbRsrp() == null) {
				netoptReceiveTest.setDtNrSsbRsrp(log.getNrRsrpavg());
			} else if (log.getNrRsrpavg() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbRsrp()) < Float.valueOf(log.getNrRsrpavg())) {
				netoptReceiveTest.setDtNrSsbRsrp(log.getNrRsrpavg());
			}
			// 66:??????????????????????????????????????????????????????????????????????????????????????????NR sinr?????????????????????????????????????????????????????????
			if (netoptReceiveTest.getDtNrSsbSinr() == null) {
				netoptReceiveTest.setDtNrSsbSinr(log.getNrSinr());
			} else if (log.getNrSinr() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbSinr()) > Float.valueOf(log.getNrSinr())) {
				netoptReceiveTest.setDtNrSsbSinr(log.getNrSinr());
			}
			// 67:??????????????????????????????????????????????????????????????????????????????????????????NR Throughput
			// DL??????????????????????????????????????????????????????
			if (netoptReceiveTest.getDtNrSsbThrouDL() == null) {
				netoptReceiveTest.setDtNrSsbThrouDL(log.getNrThrputdl());
			} else if (log.getNrThrputdl() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbThrouDL()) < Float.valueOf(log.getNrThrputdl())) {
				netoptReceiveTest.setDtNrSsbThrouDL(log.getNrThrputdl());
			}
			// 69:??????????????????????????????????????????????????????????????????????????????????????????LTE rsrp??????????????????????????????????????????????????????
			if (netoptReceiveTest.getDtLteEarfcnRsrp() == null) {
				netoptReceiveTest.setDtLteEarfcnRsrp(log.getLteRsrp());
			} else if (log.getLteRsrp() != null
					&& Float.valueOf(netoptReceiveTest.getDtLteEarfcnRsrp()) < Float.valueOf(log.getLteRsrp())) {
				netoptReceiveTest.setDtLteEarfcnRsrp(log.getLteRsrp());
			}
			// 70:??????????????????????????????????????????????????????????????????????????????????????????LTE sinr??????????????????????????????????????????????????????
			if (netoptReceiveTest.getDtLteEarfcnSinr() == null) {
				netoptReceiveTest.setDtLteEarfcnSinr(log.getLteSinr());
			} else if (log.getLteSinr() != null
					&& Float.valueOf(netoptReceiveTest.getDtLteEarfcnSinr()) > Float.valueOf(log.getLteSinr())) {
				netoptReceiveTest.setDtLteEarfcnSinr(log.getLteSinr());
			}
			// 87:??????????????????????????????????????????????????????????????????DT??????????????????PDCPNR?????????????????????????????????????????????????????????????????????
			if (netoptReceiveTest.getPdcpnrThrputDL() == null && log.getNrPdcpThrputDLAvg()!= null) {
				netoptReceiveTest.setPdcpnrThrputDL(log.getNrPdcpThrputDLAvg().toString());
			} else if (log.getNrPdcpThrputDLAvg() != null
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputDL()) < log.getNrPdcpThrputDLAvg()) {
				netoptReceiveTest.setPdcpnrThrputDL(log.getNrPdcpThrputDLAvg().toString());
			}
		}
		
		// ?????????????????????????????????????????????????????????????????????_?????????
		for (EceptionCellLogPojo log : dtULGoodMap){
			// 68:??????????????????????????????????????????????????????????????????????????????????????????NR Throughput
			// UL??????????????????????????????????????????????????????
			if (netoptReceiveTest.getDtNrSsbThrouUL() == null) {
				netoptReceiveTest.setDtNrSsbThrouUL(log.getNrThrputul());
			} else if (log.getNrThrputul() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbThrouUL()) < Float.valueOf(log.getNrThrputul())) {
				netoptReceiveTest.setDtNrSsbThrouUL(log.getNrThrputul());
			}
			// 88:??????????????????????????????????????????????????????????????????DT??????????????????PDCPNR?????????????????????????????????????????????????????????????????????
			if (netoptReceiveTest.getPdcpnrThrputUL() == null && log.getNrPdcpThrputULAvg()!= null) {
				netoptReceiveTest.setPdcpnrThrputUL(log.getNrPdcpThrputULAvg().toString());
			} else if (log.getNrPdcpThrputULAvg() != null
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputUL()) < log.getNrPdcpThrputULAvg()) {
				netoptReceiveTest.setPdcpnrThrputUL(log.getNrPdcpThrputULAvg().toString());
			}
		}

		for (EceptionCellLogPojo log : ftpDownGoodMap) {
			// ftp?????? rsrp sinr throughput ?????????
			// ??????ftp??????
			if (testTimeFtpDownGood != null && log.getNrTestdate().equals(testTimeFtpDownGood.toString())) {
				// ??????
				// 37:???????????????????????????FTP????????????????????????NR rsrp??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownRsrpGoog() == null) {
					netoptReceiveTest.setFtpDownRsrpGoog(log.getNrRsrpavg());
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpGoog()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpDownRsrpGoog(log.getNrRsrpavg());
				}
				// 38:??????????????????????????????FTP????????????????????????NR sinr?????????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownSinrGoog() == null) {
					netoptReceiveTest.setFtpDownSinrGoog(log.getNrSinr());
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownSinrGoog()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpDownSinrGoog(log.getNrSinr());
				}
				// 39:??????????????????????????????FTP????????????????????????NR Throughput DL??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownThrputGoog() == null) {
					netoptReceiveTest.setFtpDownThrputGoog(log.getNrThrputdl());
				} else if (log.getNrThrputdl() != null && Float
						.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) < Float.valueOf(log.getNrThrputdl())) {
					netoptReceiveTest.setFtpDownThrputGoog(log.getNrThrputdl());
				}
			}
		}

		for (EceptionCellLogPojo log : ftpDownMidMap) {
			// ??????ftp??????
			if (testTimeFtpDownMid != null && log.getNrTestdate().equals(testTimeFtpDownMid.toString())) {
				// ??????
				// 37:???????????????????????????FTP????????????????????????NR rsrp??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownRsrpMid() == null) {
					netoptReceiveTest.setFtpDownRsrpMid(log.getNrRsrpavg());
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpMid()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpDownRsrpMid(log.getNrRsrpavg());
				}
				// 38:??????????????????????????????FTP????????????????????????NR sinr?????????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownSinrMid() == null) {
					netoptReceiveTest.setFtpDownSinrMid(log.getNrSinr());
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownSinrMid()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpDownSinrMid(log.getNrSinr());
				}
				// 39:??????????????????????????????FTP????????????????????????NR Throughput DL??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownThrputMid() == null) {
					netoptReceiveTest.setFtpDownThrputMid(log.getNrThrputdl());
				} else if (log.getNrThrputdl() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) < Float
						.valueOf(log.getNrThrputdl())) {
					netoptReceiveTest.setFtpDownThrputMid(log.getNrThrputdl());
				}
			}
		}
		for (EceptionCellLogPojo log : ftpDownBadMap) {
			// ??????ftp??????
			if (testTimeFtpDownBad != null && log.getNrTestdate().equals(testTimeFtpDownBad.toString())) {
				// ??????
				// 37:???????????????????????????FTP????????????????????????NR rsrp??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownRsrpBad() == null) {
					netoptReceiveTest.setFtpDownRsrpBad(log.getNrRsrpavg());
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpBad()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpDownRsrpBad(log.getNrRsrpavg());
				}
				// 38:??????????????????????????????FTP????????????????????????NR sinr?????????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownSinrBad() == null) {
					netoptReceiveTest.setFtpDownSinrBad(log.getNrSinr());
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownSinrBad()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpDownSinrBad(log.getNrSinr());
				}
				// 39:??????????????????????????????FTP????????????????????????NR Throughput DL??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpDownThrputBad() == null) {
					netoptReceiveTest.setFtpDownThrputBad(log.getNrThrputdl());
				} else if (log.getNrThrputdl() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) < Float
						.valueOf(log.getNrThrputdl())) {
					netoptReceiveTest.setFtpDownThrputBad(log.getNrThrputdl());
				}
			}
		}
		for (EceptionCellLogPojo log : ftpUpGoodMap) {
			// ??????ftp??????
			if (testTimeFtpUpGood != null && log.getNrTestdate().equals(testTimeFtpUpGood.toString())) {
				// ??????
				// 37:???????????????????????????FTP????????????????????????NR rsrp??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpRsrpGoog() == null) {
					netoptReceiveTest.setFtpUpRsrpGoog(log.getNrRsrpavg());
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpRsrpGoog()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpUpRsrpGoog(log.getNrRsrpavg());
				}
				// 38:??????????????????????????????FTP????????????????????????NR sinr?????????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpSinrGoog() == null) {
					netoptReceiveTest.setFtpUpSinrGoog(log.getNrSinr());
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpSinrGoog()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpUpSinrGoog(log.getNrSinr());
				}
				// 39:??????????????????????????????FTP????????????????????????NR Throughput uL??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpThrputGoog() == null) {
					netoptReceiveTest.setFtpUpThrputGoog(log.getNrThrputul());
				} else if (log.getNrThrputul() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setFtpUpThrputGoog(log.getNrThrputul());
				}
			}
		}

		for (EceptionCellLogPojo log : ftpUpMidMap) {
			// ??????ftp??????
			if (testTimeFtpUpMid != null && log.getNrTestdate().equals(testTimeFtpUpMid.toString())) {
				// ??????
				// 37:???????????????????????????FTP????????????????????????NR rsrp??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpRsrpMid() == null) {
					netoptReceiveTest.setFtpUpRsrpMid(log.getNrRsrpavg());
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpRsrpMid()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpUpRsrpMid(log.getNrRsrpavg());
				}
				// 38:??????????????????????????????????????????????????????????????????FTP????????????????????????NR sinr??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpSinrMid() == null) {
					netoptReceiveTest.setFtpUpSinrMid(log.getNrSinr());
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpSinrMid()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpUpSinrMid(log.getNrSinr());
				}
				// 39:??????????????????????????????FTP????????????????????????NR Throughput uL??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpThrputMid() == null) {
					netoptReceiveTest.setFtpUpThrputMid(log.getNrThrputul());
				} else if (log.getNrThrputul() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setFtpUpThrputMid(log.getNrThrputul());
				}
			}
		}
		for (EceptionCellLogPojo log : ftpUpBadMap) {
			// ??????ftp??????
			if (testTimeFtpUpBad != null && log.getNrTestdate().equals(testTimeFtpUpBad.toString())) {
				// ??????
				// 37:???????????????????????????FTP????????????????????????NR rsrp??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpRsrpBad() == null) {
					netoptReceiveTest.setFtpUpRsrpBad(log.getNrRsrpavg());
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpRsrpBad()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpUpRsrpBad(log.getNrRsrpavg());
				}
				// 38:??????????????????????????????????????????????????????????????????FTP????????????????????????NR sinr??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpSinrBad() == null) {
					netoptReceiveTest.setFtpUpSinrBad(log.getNrSinr());
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpSinrBad()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpUpSinrBad(log.getNrSinr());
				}
				// 39:??????????????????????????????FTP????????????????????????NR Throughput uL??????????????????????????????????????????????????????
				if (netoptReceiveTest.getFtpUpThrputBad() == null) {
					netoptReceiveTest.setFtpUpThrputBad(log.getNrThrputul());
				} else if (log.getNrThrputul() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setFtpUpThrputBad(log.getNrThrputul());
				}
			}
		}

		//??????????????????
		if (menu == null || menu.getStationParamPojo() == null) {
			// ????????????ftp??????
			if (netoptReceiveTest.getFtpDownThrputGoog() != null
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) >= 300
					&& netoptReceiveTest.getFtpDownThrputMid() != null
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) >= 120
					&& netoptReceiveTest.getFtpDownThrputBad() != null
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) >= 40) {
				performanceReceive.setFtpDownload("???");
			} else {
				performanceReceive.setFtpDownload("???");
			}
			// ????????????ftp??????
			if (netoptReceiveTest.getFtpUpThrputGoog() != null
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) >= 30
					&& netoptReceiveTest.getFtpUpThrputMid() != null
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) >= 20
					&& netoptReceiveTest.getFtpUpThrputBad() != null
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) >= 10) {
				performanceReceive.setFtpUpload("???");
			} else {
				performanceReceive.setFtpUpload("???");
			}
			// 4G???????????????????????????
			if ("0".equals(netoptReceiveTest.getLteEpsattachFailure())
					&& netoptReceiveTest.getLteEpsattachSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteEpsattachSuccess()) >= 10) {
				performanceReceive.setLteEpsattachSuccessRation("???");
			} else {
				performanceReceive.setLteEpsattachSuccessRation("???");
			}
			// 5G????????????????????????gNB??????????????????
			if ("0".equals(netoptReceiveTest.getNrEdsaddFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= 10) {
				performanceReceive.setNrEdsaddSuccessRation("???");
			} else {
				performanceReceive.setNrEdsaddSuccessRation("???");
			}
			// 4G?????????????????????
			if ("0".equals(netoptReceiveTest.getLteInterfreqHandoverFailure())
					&& netoptReceiveTest.getLteInterfreqHandoverSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteInterfreqHandoverSuccess()) >= 1) {
				performanceReceive.setLteInterfreqHandoverSuccessRation("???");
			} else {
				performanceReceive.setLteInterfreqHandoverSuccessRation("???");
			}
			// 5G????????????????????????gNB??????????????????
			if ("0".equals(netoptReceiveTest.getNrEdschangeFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= 1) {
				performanceReceive.setNrEdschangeSuccessRation("???");
			} else {
				performanceReceive.setNrEdschangeSuccessRation("???");
			}
			// 4G??????RRC????????????
			if ("0".equals(netoptReceiveTest.getRrcConnectionFailure())
					&& netoptReceiveTest.getRrcConnectionSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getRrcConnectionSuccess()) >= 10) {
				performanceReceive.setRrcConnectionSuccessRation("???");
			} else {
				performanceReceive.setRrcConnectionSuccessRation("???");
			}
			// //4G???????????????
			// if("0".equals(netoptReceiveTest.getLteEarfcnFailure())
			// && netoptReceiveTest.getLteEarfcnSuccess() != null
			// && Integer.valueOf(netoptReceiveTest.getLteEarfcnSuccess()) >=
			// 1){
			// performanceReceive.setLteEarfcnSuccessRation("???");
			// }else{
			// performanceReceive.setLteEarfcnSuccessRation("???");
			// }
			// //5G?????????
			// if("0".equals(netoptReceiveTest.getNrEarfcnFailure())
			// && netoptReceiveTest.getNrEarfcnSuccess() != null
			// && Integer.valueOf(netoptReceiveTest.getNrEarfcnSuccess()) >= 1){
			// performanceReceive.setNrEarfcnSuccessRation("???");
			// }else{
			// performanceReceive.setNrEarfcnSuccessRation("???");
			// }
			// ??????ping32
			if (StringUtils.hasText(netoptReceiveTest.getPing32SuccessRation())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Sum())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Success())
					&& Float.valueOf(netoptReceiveTest.getPing32Success()) >= 20
					&& Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) <= 15
					&& (Float.valueOf(netoptReceiveTest.getPing32Success())
							/ Float.valueOf(netoptReceiveTest.getPing32Sum())) >= 1) {
				performanceReceive.setPing32SuccessRation("???");
			} else {
				performanceReceive.setPing32SuccessRation("???");
			}
			// ??????ping1500
			if (StringUtils.hasText(netoptReceiveTest.getPing1500SuccessRation())// 36
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Sum())// 18
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Success())// 9
					&& Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) <= 17
					&& Float.valueOf(netoptReceiveTest.getPing1500Success()) >= 20
					&& (Float.valueOf(netoptReceiveTest.getPing1500Success())
							/ Float.valueOf(netoptReceiveTest.getPing1500Sum())) >= 1) {
				performanceReceive.setPing1500SuccessRation("???");
			} else {
				performanceReceive.setPing1500SuccessRation("???");
			}


		} else if (menu.getStationParamPojo() != null) {
			StationParamPojo spp = menu.getStationParamPojo();
			// ????????????ftp??????
			if (netoptReceiveTest.getFtpDownThrputGoog() != null
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) > spp.getUpgradeGood()
					&& netoptReceiveTest.getFtpDownThrputMid() != null
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) > spp.getUpgradeMid()
					&& netoptReceiveTest.getFtpDownThrputBad() != null
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) > spp.getUpgradeBad()) {
				performanceReceive.setFtpDownload("???");
			} else {
				performanceReceive.setFtpDownload("???");
			}
			// ????????????ftp??????
			if (netoptReceiveTest.getFtpUpThrputGoog() != null
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) > spp.getUploadGood()
					&& netoptReceiveTest.getFtpUpThrputMid() != null
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) > spp.getUploadMid()
					&& netoptReceiveTest.getFtpUpThrputBad() != null
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) > spp.getUploadBad()) {
				performanceReceive.setFtpUpload("???");
			} else {
				performanceReceive.setFtpUpload("???");
			}
			// 4G???????????????????????????
			if ("0".equals(netoptReceiveTest.getLteEpsattachFailure())
					&& netoptReceiveTest.getLteEpsattachSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteEpsattachSuccess()) >= spp.getConnectNumber4g()) {
				performanceReceive.setLteEpsattachSuccessRation("???");
			} else {
				performanceReceive.setLteEpsattachSuccessRation("???");
			}
			// 5G????????????????????????gNB??????????????????
			if ("0".equals(netoptReceiveTest.getNrEdsaddFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= spp.getConnectNumber5G()) {
				performanceReceive.setNrEdsaddSuccessRation("???");
			} else {
				performanceReceive.setNrEdsaddSuccessRation("???");
			}
			// 4G?????????????????????
			if ("0".equals(netoptReceiveTest.getLteInterfreqHandoverFailure())
					&& netoptReceiveTest.getLteInterfreqHandoverSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteInterfreqHandoverSuccess()) >= 1) {
				performanceReceive.setLteInterfreqHandoverSuccessRation("???");
			} else {
				performanceReceive.setLteInterfreqHandoverSuccessRation("???");
			}
			// 5G????????????????????????gNB??????????????????
			if ("0".equals(netoptReceiveTest.getNrEdschangeFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= 1) {
				performanceReceive.setNrEdschangeSuccessRation("???");
			} else {
				performanceReceive.setNrEdschangeSuccessRation("???");
			}
			// 4G??????RRC????????????
			if ("0".equals(netoptReceiveTest.getRrcConnectionFailure())
					&& netoptReceiveTest.getRrcConnectionSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getRrcConnectionSuccess()) >= spp.getConnectNumber4g()) {
				performanceReceive.setRrcConnectionSuccessRation("???");
			} else {
				performanceReceive.setRrcConnectionSuccessRation("???");
			}
			// //4G???????????????
			// if("0".equals(netoptReceiveTest.getLteEarfcnFailure())
			// && netoptReceiveTest.getLteEarfcnSuccess() != null
			// && Integer.valueOf(netoptReceiveTest.getLteEarfcnSuccess()) >=
			// 1){
			// performanceReceive.setLteEarfcnSuccessRation("???");
			// }else{
			// performanceReceive.setLteEarfcnSuccessRation("???");
			// }
			// //5G?????????
			// if("0".equals(netoptReceiveTest.getNrEarfcnFailure())
			// && netoptReceiveTest.getNrEarfcnSuccess() != null
			// && Integer.valueOf(netoptReceiveTest.getNrEarfcnSuccess()) >= 1){
			// performanceReceive.setNrEarfcnSuccessRation("???");
			// }else{
			// performanceReceive.setNrEarfcnSuccessRation("???");
			// }
			// ??????ping32
			if (StringUtils.hasText(netoptReceiveTest.getPing32SuccessRation())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Sum())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Success())
					&& Float.valueOf(netoptReceiveTest.getPing32Success()) >= spp.getPing32Number()
					&& Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) < spp.getPing32DelayTime()
					&& (Float.valueOf(netoptReceiveTest.getPing32Success())
							/ Float.valueOf(netoptReceiveTest.getPing32Sum())*100) >= spp.getPing32SuccessRatio()) {
				performanceReceive.setPing32SuccessRation("???");
			} else {
				performanceReceive.setPing32SuccessRation("???");
			}
			// ??????ping1500
			if (StringUtils.hasText(netoptReceiveTest.getPing1500SuccessRation())// 36
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Sum())// 18
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Success())// 9
					&& Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) < spp.getPing1500DelayTime()
					&& Float.valueOf(netoptReceiveTest.getPing1500Success()) >= spp.getPing1500Nember()
					&& (Float.valueOf(netoptReceiveTest.getPing1500Success())
							/ Float.valueOf(netoptReceiveTest.getPing1500Sum())*100) >= spp.getPing1500SuccessRatio()) {
				performanceReceive.setPing1500SuccessRation("???");
			} else {
				performanceReceive.setPing1500SuccessRation("???");
			}
		}
		
		// ping????????????
		if (performanceReceive.getPing32SuccessRation().equals("???")
				&& performanceReceive.getPing1500SuccessRation().equals("???")) {
			performanceReceive.setPingDelay("???");
		} else {
			performanceReceive.setPingDelay("???");
		}

		// 4G???????????????
		if ("0".equals(netoptReceiveTest.getDtLteFailSumDegree())) {
			performanceReceive.setLteEarfcnSuccessRation("???");
		} else {
			performanceReceive.setLteEarfcnSuccessRation("???");
		}
		// 5G?????????
		if ("0".equals(netoptReceiveTest.getDtNrFailSumDegree())) {
			performanceReceive.setNrEarfcnSuccessRation("???");
		} else {
			performanceReceive.setNrEarfcnSuccessRation("???");
		}
		
		//?????????????????????????????????
		if (netoptReceiveTest.getDialVolteSuccessSum() != null && netoptReceiveTest.getDialVonrSuccessSum() != null 
				&&(Float.valueOf(netoptReceiveTest.getDialVolteSuccessSum()) + Float.valueOf(netoptReceiveTest.getDialVonrSuccessSum())) > 10) {
			performanceReceive.setDialVoSunccess51("???");
		} else {
			performanceReceive.setDialVoSunccess51("???");
		}
		
		//nr??????????????????????????????/???81???????????????10????????????????????????????????????????????????????????????
		if (netoptReceiveTest.getNrConnectSuccessSum() != null && Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) >10 ) {
			performanceReceive.setNrConnectSuccess55("???");
		} else {
			performanceReceive.setNrConnectSuccess55("???");
		}
		
		//nr??????????????????/???82???????????????120????????????????????????????????????????????????????????????
		if (netoptReceiveTest.getNrConnectTimeDelay() != null && Float.valueOf(netoptReceiveTest.getNrConnectTimeDelay()) >120 ) {
			performanceReceive.setNrConnectTimeDelay58("???");
		} else {
			performanceReceive.setNrConnectTimeDelay58("???");
		}
		
		netoptReceiveTest.setPlanParamPojo(plan);
		performanceReceive.setPlanParamPojo(plan);
		stationReportCreatService.createNetoptReceiveTest(netoptReceiveTest);
		stationReportCreatService.createPerformanceReceive(performanceReceive);
	}

	/**
	 * ????????????????????????
	 * 
	 * @author maxuancheng date:2020???2???22??? ??????1:21:53
	 * @author lucheng date:2020???6???23?????????
	 * @param newDate
	 *            ??????????????????
	 * @param plan
	 *            ????????????
	 * @param cellLogList
	 *            ??????????????????
	 */
	private void stationCellParamCensus(String newDate, PlanParamPojo plan, List<EceptionCellLogPojo> cellLogList) {
		StationCellParamCensusPojo CellParamCensus = new StationCellParamCensusPojo();
		CellParamCensus.setCellParamId(plan.getId());
		CellParamCensus.setSiteName(plan.getSiteName());
		CellParamCensus.setLocalCellId(plan.getLocalCellID().toString());
		CellParamCensus.setCellName(plan.getCellName());
		CellParamCensus.setCellId1(plan.getCellId().toString());
		CellParamCensus.setPci2(plan.getPci());
		CellParamCensus.setNrFrequency3(plan.getNrFrequency());
		CellParamCensus.setFrequency14(plan.getFrequency1());
		CellParamCensus.setCellBroadband5(plan.getCellBroadband());
		CellParamCensus.setSpecialRatio6(plan.getSpecialRatio());
		CellParamCensus.setUpAndDownRatio7(plan.getUpAndDownRatio());
		CellParamCensus.setSsbWaveInterval8(plan.getSsbWaveInterval());
		CellParamCensus.setTac9(plan.getTac());
		CellParamCensus.setSsbSending10(plan.getSsbSending());
		CellParamCensus.setP_a_p(plan.getP_a());
		CellParamCensus.setP_b_p(plan.getP_b());
		CellParamCensus.setRootSeq_p(plan.getRootSeq());
		CellParamCensus.setPdcchSymbol_p(plan.getPdcchSymbol());
		CellParamCensus.setCellTRx_p(plan.getCellTRx());
		CellParamCensus.setFrameStructure_p(plan.getFrameStructure());
		List<EceptionCellLogPojo> cellLog = new ArrayList<EceptionCellLogPojo>();
		Long testTime = null;
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		for (EceptionCellLogPojo log : cellLogList) {
			if (log != null && StringUtils.hasText(log.getNrTestevent()) && !log.getNrTestevent().equals("??????")) {
				if (testTime == null) {
					testTime = Long.valueOf(log.getNrTestdate());
					cellLog.add(log);
					// ????????????????????????
				} else if (Math.abs(testTime - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTime = Long.valueOf(log.getNrTestdate());
					cellLog.clear();
					cellLog.add(log);
					// ??????????????????
				} else if (testTime.equals(Long.valueOf(log.getNrTestdate()))) {
					cellLog.add(log);
				}
			}
		}
		// 11~20?????????
		if (cellLog != null) {
			Map<String, Integer> cellIdMap = new HashMap<String, Integer>();
			Map<String, Integer> pciMap = new HashMap<String, Integer>();
			Map<String, Integer> freMap = new HashMap<String, Integer>();
			Map<String, Integer> poiMap = new HashMap<String, Integer>();
			Map<String, Integer> broadMap = new HashMap<String, Integer>();
			Map<String, Integer> sspMap = new HashMap<String, Integer>();
			Map<String, Integer> sarMap = new HashMap<String, Integer>();
			Map<String, Integer> ssbwMap = new HashMap<String, Integer>();
			Map<String, Integer> tacMap = new HashMap<String, Integer>();
			Map<String, Integer> ssbsMap = new HashMap<String, Integer>();
			Map<String, Integer> paMap = new HashMap<String, Integer>();
			Map<String, Integer> pbMap = new HashMap<String, Integer>();
			Map<String, Integer> rootSeqMap = new HashMap<String, Integer>();
			Map<String, Integer> pdcchSmMap = new HashMap<String, Integer>();
			Map<String, Integer> cellTrxMap = new HashMap<String, Integer>();
			Map<String, Integer> frameStrcMap = new HashMap<String, Integer>();
			for (EceptionCellLogPojo ecp : cellLog) {
				if (CellParamCensus.getCellId11() == null) {
					if (plan.getCellId() == null) {
						if (ecp.getNrCellid1() != null && !ecp.getNrCellid1().equals("0")) {
							Integer c1 = cellIdMap.get(ecp.getNrCellid1());
							if (c1 == null) {
								cellIdMap.put(ecp.getNrCellid1(), 1);
							} else {
								cellIdMap.put(ecp.getNrCellid1(), c1 + 1);
							}
						}
					} else if (ecp.getNrCellid1() != null && ecp.getNrCellid1().equals(plan.getCellId())) {
						CellParamCensus.setCellId11(ecp.getNrCellid1());
					} else if (ecp.getNrCellid2() != null && ecp.getNrCellid2().equals(plan.getCellId())) {
						CellParamCensus.setCellId11(ecp.getNrCellid2());
					} else if (ecp.getNrCellid3() != null && ecp.getNrCellid3().equals(plan.getCellId())) {
						CellParamCensus.setCellId11(ecp.getNrCellid3());
					} else {
						if (ecp.getNrCellid1() != null && !ecp.getNrCellid1().equals("0")) {
							Integer c1 = cellIdMap.get(ecp.getNrCellid1());
							if (c1 == null) {
								cellIdMap.put(ecp.getNrCellid1(), 1);
							} else {
								cellIdMap.put(ecp.getNrCellid1(), c1 + 1);
							}
						}
					}
				}
				if (CellParamCensus.getPci12() == null) {
					if (plan.getPci() == null) {
						if (ecp.getNrPci1() != null && !ecp.getNrPci1().equals("0")) {
							Integer p1 = pciMap.get(ecp.getNrPci1());
							if (p1 == null) {
								pciMap.put(ecp.getNrPci1(), 1);
							} else {
								pciMap.put(ecp.getNrPci1(), p1 + 1);
							}
						}
						if (ecp.getNrPci2() != null && !ecp.getNrPci2().equals("0")) {
							Integer p2 = pciMap.get(ecp.getNrPci2());
							if (p2 == null) {
								pciMap.put(ecp.getNrPci2(), 1);
							} else {
								pciMap.put(ecp.getNrPci2(), p2 + 1);
							}
						}
						if (ecp.getNrPci3() != null && !ecp.getNrPci3().equals("0")) {
							Integer p3 = pciMap.get(ecp.getNrPci3());
							if (p3 == null) {
								pciMap.put(ecp.getNrPci3(), 1);
							} else {
								pciMap.put(ecp.getNrPci3(), p3 + 1);
							}
						}
					} else if (ecp.getNrPci1() != null && ecp.getNrPci1().equals(plan.getPci().toString())) {
						CellParamCensus.setPci12(Integer.valueOf(ecp.getNrPci1()));
					} else if (ecp.getNrPci2() != null && ecp.getNrPci2().equals(plan.getPci().toString())) {
						CellParamCensus.setPci12(Integer.valueOf(ecp.getNrPci2()));
					} else if (ecp.getNrPci3() != null && ecp.getNrPci3().equals(plan.getPci().toString())) {
						CellParamCensus.setPci12(Integer.valueOf(ecp.getNrPci3()));
					} else {
						if (ecp.getNrPci1() != null && !ecp.getNrPci1().equals("0")) {
							Integer p1 = pciMap.get(ecp.getNrPci1());
							if (p1 == null) {
								pciMap.put(ecp.getNrPci1(), 1);
							} else {
								pciMap.put(ecp.getNrPci1(), p1 + 1);
							}
						}
						if (ecp.getNrPci2() != null && !ecp.getNrPci2().equals("0")) {
							Integer p2 = pciMap.get(ecp.getNrPci2());
							if (p2 == null) {
								pciMap.put(ecp.getNrPci2(), 1);
							} else {
								pciMap.put(ecp.getNrPci2(), p2 + 1);
							}
						}
						if (ecp.getNrPci3() != null && !ecp.getNrPci3().equals("0")) {
							Integer p3 = pciMap.get(ecp.getNrPci3());
							if (p3 == null) {
								pciMap.put(ecp.getNrPci3(), 1);
							} else {
								pciMap.put(ecp.getNrPci3(), p3 + 1);
							}
						}
					}
				}
				// ??????
				if (CellParamCensus.getNrFrequency13() == null) {
					if (ecp.getNrFrequery() == null || ecp.getNrFrequery().equals("0")) {

					} else if (plan.getNrFrequency() == null
							|| !plan.getNrFrequency().equals(Float.valueOf(ecp.getNrFrequery()))) {
						if (ecp.getNrFrequery() != null) {
							Integer f1 = freMap.get(ecp.getNrFrequery());
							if (f1 == null) {
								freMap.put(ecp.getNrFrequery(), 1);
							} else {
								freMap.put(ecp.getNrFrequery(), f1 + 1);
							}
						}
					} else if (plan.getNrFrequency().equals(Float.valueOf(ecp.getNrFrequery()))) {
						CellParamCensus.setNrFrequency13(ecp.getNrFrequery());
					}
				}
				// ????????????
				//SA??????NR??????
				if (CellParamCensus.getFrequency114() == null) {
					if (ecp.getNrArfcnssbSA() == null || ecp.getNrArfcnssbSA().equals("0")) {

					} else if (plan.getFrequency1() == null
							|| !plan.getFrequency1().equals(ecp.getNrArfcnssbSA())) {
						if (ecp.getNrArfcnssbSA() != null) {
							Integer f1 = poiMap.get(ecp.getNrArfcnssbSA());
							if (f1 == null) {
								poiMap.put(ecp.getNrArfcnssbSA().toString(), 1);
							} else {
								poiMap.put(ecp.getNrArfcnssbSA().toString(), f1 + 1);
							}
						}
					} else if (plan.getFrequency1().equals(ecp.getNrArfcnssbSA())) {
						CellParamCensus.setFrequency114(ecp.getNrArfcnssbSA());
					}
				}
				// SA??????NR??????
				if (CellParamCensus.getCellBroadband15() == null) {
					if (ecp.getNrBandwidthSA() == null || ecp.getNrBandwidthSA().equals("0")) {

					} else if (plan.getCellBroadband() == null
							|| !plan.getCellBroadband().equals(ecp.getNrBandwidthSA())) {
						if (ecp.getNrBandwidthSA() != null) {
							Integer f1 = broadMap.get(ecp.getNrBandwidthSA());
							if (f1 == null) {
								broadMap.put(ecp.getNrBandwidthSA().toString(), 1);
							} else {
								broadMap.put(ecp.getNrBandwidthSA().toString(), f1 + 1);
							}
						}
					} else if (plan.getCellBroadband().equals(ecp.getNrBandwidthSA())) {
						CellParamCensus.setCellBroadband15(ecp.getNrBandwidthSA().toString());
					}
				}

//				Menu menu = terminalMenuService.get2(plan.getCity());
//				if (menu == null || menu.getStationParamPojo() == null) {
//					// ??????
//					if (CellParamCensus.getFrequency114() == null) {
//						if (ecp.getNrPoint() == null || ecp.getNrPoint().equals("0")) {
//
//						} else if (plan.getFrequency1() == null
//								|| !plan.getFrequency1().equals(Integer.valueOf(ecp.getNrPoint()))) {
//							if (ecp.getNrPoint() != null) {
//								Integer f1 = poiMap.get(ecp.getNrPoint());
//								if (f1 == null) {
//									poiMap.put(ecp.getNrPoint(), 1);
//								} else {
//									poiMap.put(ecp.getNrPoint(), f1 + 1);
//								}
//							}
//						} else if (plan.getFrequency1().equals(Integer.valueOf(ecp.getNrPoint()))) {
//							CellParamCensus.setFrequency114(Integer.valueOf(ecp.getNrPoint()));
//						}
//					}
//					// ??????
//					if (CellParamCensus.getCellBroadband15() == null) {
//						if (ecp.getNrBandwidth() == null || ecp.getNrBandwidth().equals("0")) {
//
//						} else if (plan.getCellBroadband() == null
//								|| !plan.getCellBroadband().equals(ecp.getNrBandwidth())) {
//							if (ecp.getNrBandwidth() != null) {
//								Integer f1 = broadMap.get(ecp.getNrBandwidth());
//								if (f1 == null) {
//									broadMap.put(ecp.getNrBandwidth(), 1);
//								} else {
//									broadMap.put(ecp.getNrBandwidth(), f1 + 1);
//								}
//							}
//						} else if (plan.getCellBroadband().equals(ecp.getNrBandwidth())) {
//							CellParamCensus.setCellBroadband15(ecp.getNrBandwidth());
//						}
//					}
//
//				} else if (menu.getStationParamPojo() != null) {
//					StationParamPojo spp = menu.getStationParamPojo();
//					if ("6".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
//						//SA??????NR??????
//						if (CellParamCensus.getFrequency114() == null) {
//							if (ecp.getNrArfcnssbSA() == null || ecp.getNrArfcnssbSA().equals("0")) {
//
//							} else if (plan.getFrequency1() == null
//									|| !plan.getFrequency1().equals(ecp.getNrArfcnssbSA())) {
//								if (ecp.getNrArfcnssbSA() != null) {
//									Integer f1 = poiMap.get(ecp.getNrArfcnssbSA());
//									if (f1 == null) {
//										poiMap.put(ecp.getNrArfcnssbSA().toString(), 1);
//									} else {
//										poiMap.put(ecp.getNrArfcnssbSA().toString(), f1 + 1);
//									}
//								}
//							} else if (plan.getFrequency1().equals(ecp.getNrArfcnssbSA())) {
//								CellParamCensus.setFrequency114(ecp.getNrArfcnssbSA());
//							}
//						}
//						// SA??????NR??????
//						if (CellParamCensus.getCellBroadband15() == null) {
//							if (ecp.getNrBandwidthSA() == null || ecp.getNrBandwidthSA().equals("0")) {
//
//							} else if (plan.getCellBroadband() == null
//									|| !plan.getCellBroadband().equals(ecp.getNrBandwidthSA())) {
//								if (ecp.getNrBandwidthSA() != null) {
//									Integer f1 = broadMap.get(ecp.getNrBandwidthSA());
//									if (f1 == null) {
//										broadMap.put(ecp.getNrBandwidthSA().toString(), 1);
//									} else {
//										broadMap.put(ecp.getNrBandwidthSA().toString(), f1 + 1);
//									}
//								}
//							} else if (plan.getCellBroadband().equals(ecp.getNrBandwidthSA())) {
//								CellParamCensus.setCellBroadband15(ecp.getNrBandwidthSA().toString());
//							}
//						}
//
//					} else { 
//						// ??????
//						if (CellParamCensus.getFrequency114() == null) {
//							if (ecp.getNrPoint() == null || ecp.getNrPoint().equals("0")) {
//
//							} else if (plan.getFrequency1() == null
//									|| !plan.getFrequency1().equals(Integer.valueOf(ecp.getNrPoint()))) {
//								if (ecp.getNrPoint() != null) {
//									Integer f1 = poiMap.get(ecp.getNrPoint());
//									if (f1 == null) {
//										poiMap.put(ecp.getNrPoint(), 1);
//									} else {
//										poiMap.put(ecp.getNrPoint(), f1 + 1);
//									}
//								}
//							} else if (plan.getFrequency1().equals(Integer.valueOf(ecp.getNrPoint()))) {
//								CellParamCensus.setFrequency114(Integer.valueOf(ecp.getNrPoint()));
//							}
//						}
//						// ??????
//						if (CellParamCensus.getCellBroadband15() == null) {
//							if (ecp.getNrBandwidth() == null || ecp.getNrBandwidth().equals("0")) {
//
//							} else if (plan.getCellBroadband() == null
//									|| !plan.getCellBroadband().equals(ecp.getNrBandwidth())) {
//								if (ecp.getNrBandwidth() != null) {
//									Integer f1 = broadMap.get(ecp.getNrBandwidth());
//									if (f1 == null) {
//										broadMap.put(ecp.getNrBandwidth(), 1);
//									} else {
//										broadMap.put(ecp.getNrBandwidth(), f1 + 1);
//									}
//								}
//							} else if (plan.getCellBroadband().equals(ecp.getNrBandwidth())) {
//								CellParamCensus.setCellBroadband15(ecp.getNrBandwidth());
//							}
//						}
//
//					}
//				}
				//
				if (CellParamCensus.getSpecialRatio16() == null) {
					if (ecp.getNrSspratio() == null || ecp.getNrSspratio().equals("0")) {

					} else if (plan.getSpecialRatio() == null || !plan.getSpecialRatio().equals(ecp.getNrSspratio())) {
						if (ecp.getNrSspratio() != null) {
							Integer f1 = sspMap.get(ecp.getNrSspratio());
							if (f1 == null) {
								sspMap.put(ecp.getNrSspratio(), 1);
							} else {
								sspMap.put(ecp.getNrSspratio(), f1 + 1);
							}
						}
					} else if (plan.getSpecialRatio().equals(ecp.getNrSspratio())) {
						CellParamCensus.setSpecialRatio16(ecp.getNrSspratio());
					}
				}
				//
				if (CellParamCensus.getUpAndDownRatio17() == null) {
					if (ecp.getNrSaratio() == null || ecp.getNrSaratio().equals("0")) {

					} else if (plan.getUpAndDownRatio() == null
							|| !plan.getUpAndDownRatio().equals(ecp.getNrSaratio())) {
						if (ecp.getNrSaratio() != null) {
							Integer f1 = sarMap.get(ecp.getNrSaratio());
							if (f1 == null) {
								sarMap.put(ecp.getNrSaratio(), 1);
							} else {
								sarMap.put(ecp.getNrSaratio(), f1 + 1);
							}
						}
					} else if (plan.getUpAndDownRatio().equals(ecp.getNrSaratio())) {
						CellParamCensus.setUpAndDownRatio17(ecp.getNrSaratio());
					}
				}
				//
				if (CellParamCensus.getSsbWaveInterval18() == null) {
					if (ecp.getNrSubcarrierspacing() == null || ecp.getNrSubcarrierspacing().equals("0")) {

					} else if (plan.getSsbWaveInterval() == null
							|| !plan.getSsbWaveInterval().equals(ecp.getNrSubcarrierspacing())) {
						if (ecp.getNrSubcarrierspacing() != null) {
							Integer f1 = ssbwMap.get(ecp.getNrSubcarrierspacing());
							if (f1 == null) {
								ssbwMap.put(ecp.getNrSubcarrierspacing(), 1);
							} else {
								ssbwMap.put(ecp.getNrSubcarrierspacing(), f1 + 1);
							}
						}
					} else if (plan.getSsbWaveInterval().equals(ecp.getNrSubcarrierspacing())) {
						CellParamCensus.setSsbWaveInterval18(ecp.getNrSubcarrierspacing());
					}
				}
				//
				if (CellParamCensus.getTac19() == null) {
					if (ecp.getNrTac() == null || ecp.getNrTac().equals("0")) {

					} else if (plan.getTac() == null || !plan.getTac().equals(Integer.valueOf(ecp.getNrTac()))) {
						if (ecp.getNrTac() != null) {
							Integer f1 = tacMap.get(ecp.getNrTac());
							if (f1 == null) {
								tacMap.put(ecp.getNrTac(), 1);
							} else {
								tacMap.put(ecp.getNrTac(), f1 + 1);
							}
						}
					} else if (plan.getTac().equals(Integer.valueOf(ecp.getNrTac()))) {
						CellParamCensus.setTac19(Integer.valueOf(ecp.getNrTac()));
					}
				}
				//
				if (CellParamCensus.getSsbSending20() == null) {
					if (ecp.getNrPuschtxpower() == null || ecp.getNrPuschtxpower().equals("0")) {

					} else if (plan.getSsbSending() == null || !plan.getSsbSending().equals(ecp.getNrPuschtxpower())) {
						if (ecp.getNrPuschtxpower() != null) {
							Integer f1 = ssbsMap.get(ecp.getNrPuschtxpower());
							if (f1 == null) {
								ssbsMap.put(ecp.getNrPuschtxpower(), 1);
							} else {
								ssbsMap.put(ecp.getNrPuschtxpower(), f1 + 1);
							}
						}
					} else if (plan.getSsbSending().equals(ecp.getNrPuschtxpower())) {
						CellParamCensus.setSsbSending20(ecp.getNrPuschtxpower());
					}
				}
				//
				if (CellParamCensus.getP_a_r() == null) {
					if (ecp.getpA() == null || ecp.getpA().equals("0")) {

					} else if (plan.getP_a() == null || !plan.getP_a().equals(ecp.getpA())) {
						if (ecp.getpA() != null) {
							Integer f1 = paMap.get(ecp.getpA());
							if (f1 == null) {
								paMap.put(ecp.getpA(), 1);
							} else {
								paMap.put(ecp.getpA(), f1 + 1);
							}
						}
					} else if (plan.getP_a().equals(ecp.getpA())) {
						CellParamCensus.setP_a_r(ecp.getpA());
					}
				}
				//
				if (CellParamCensus.getP_b_r() == null) {
					if (ecp.getpB() == null || ecp.getpB().equals("0")) {

					} else if (plan.getP_b() == null || !plan.getP_b().equals(ecp.getpB())) {
						if (ecp.getpB() != null) {
							Integer f1 = pbMap.get(ecp.getpB());
							if (f1 == null) {
								pbMap.put(ecp.getpB(), 1);
							} else {
								pbMap.put(ecp.getpB(), f1 + 1);
							}
						}
					} else if (plan.getP_b().equals(ecp.getpB())) {
						CellParamCensus.setP_b_r(ecp.getpB());
					}
				}
				//
				if (CellParamCensus.getRootSeq_r() == null) {
					if (ecp.getNrPreambleFormat() == null || ecp.getNrPreambleFormat().equals("0")) {

					} else if (plan.getRootSeq() == null || !plan.getRootSeq().equals(ecp.getNrPreambleFormat())) {
						if (ecp.getNrPreambleFormat() != null) {
							Integer f1 = rootSeqMap.get(ecp.getNrPreambleFormat());
							if (f1 == null) {
								rootSeqMap.put(ecp.getNrPreambleFormat(), 1);
							} else {
								rootSeqMap.put(ecp.getNrPreambleFormat(), f1 + 1);
							}
						}
					} else if (plan.getRootSeq().equals(ecp.getNrPreambleFormat())) {
						CellParamCensus.setRootSeq_r(ecp.getNrPreambleFormat());
					}
				}
				//
				if (CellParamCensus.getPdcchSymbol_r() == null) {
					if (ecp.getPdcch() == null || ecp.getPdcch().equals("0")) {

					} else if (plan.getPdcchSymbol() == null || !plan.getPdcchSymbol().equals(ecp.getPdcch())) {
						if (ecp.getPdcch() != null) {
							Integer f1 = pdcchSmMap.get(ecp.getPdcch());
							if (f1 == null) {
								pdcchSmMap.put(ecp.getPdcch(), 1);
							} else {
								pdcchSmMap.put(ecp.getPdcch(), f1 + 1);
							}
						}
					} else if (plan.getPdcchSymbol().equals(ecp.getPdcch())) {
						CellParamCensus.setPdcchSymbol_r(ecp.getPdcch());
					}
				}
				//
				if (CellParamCensus.getCellTRx_r() == null) {
					if (ecp.getNrTrx() == null || ecp.getNrTrx().equals("0")) {

					} else if (plan.getCellTRx() == null || !plan.getCellTRx().equals(ecp.getNrTrx())) {
						if (ecp.getNrTrx() != null) {
							Integer f1 = cellTrxMap.get(ecp.getNrTrx());
							if (f1 == null) {
								cellTrxMap.put(ecp.getNrTrx(), 1);
							} else {
								cellTrxMap.put(ecp.getNrTrx(), f1 + 1);
							}
						}
					} else if (plan.getCellTRx().equals(ecp.getNrTrx())) {
						CellParamCensus.setCellTRx_r(ecp.getNrTrx());
					}
				}
				//
				if (CellParamCensus.getFrameStructure_r() == null) {
					if (ecp.getNrFrameStructure() == null || ecp.getNrFrameStructure().equals("0")) {

					} else if (plan.getFrameStructure() == null
							|| !plan.getFrameStructure().equals(ecp.getNrFrameStructure())) {
						if (ecp.getNrFrameStructure() != null) {
							Integer f1 = frameStrcMap.get(ecp.getNrFrameStructure());
							if (f1 == null) {
								frameStrcMap.put(ecp.getNrFrameStructure(), 1);
							} else {
								frameStrcMap.put(ecp.getNrFrameStructure(), f1 + 1);
							}
						}
					} else if (plan.getFrameStructure().equals(ecp.getNrFrameStructure())) {
						CellParamCensus.setFrameStructure_r(ecp.getNrFrameStructure());
					}
				}
			}
			// ?????????????????? ?????????????????? ??????????????????????????????????????????
			if (CellParamCensus.getCellId11() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : cellIdMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setCellId11(key);
			}
			//
			if (CellParamCensus.getPci12() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : pciMap.entrySet()) {
					if (val == null && entry.getKey() != null && Integer.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null
							&& Integer.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setPci12(key == null ? null : Integer.valueOf(key));
			}
			//
			if (CellParamCensus.getNrFrequency13() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : freMap.entrySet()) {
					if (val == null && entry.getKey() != null && Float.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null
							&& Float.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setNrFrequency13(key == null ? null : key);
			}
			//
			if (CellParamCensus.getFrequency114() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : poiMap.entrySet()) {
					if (val == null && entry.getKey() != null && Integer.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null
							&& Integer.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setFrequency114(key == null ? null : Integer.valueOf(key));
			}
			//
			if (CellParamCensus.getCellBroadband15() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : broadMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setCellBroadband15(key);
			}
			//
			if (CellParamCensus.getSpecialRatio16() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : sspMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setSpecialRatio16(key);
			}
			//
			if (CellParamCensus.getUpAndDownRatio17() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : sarMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setUpAndDownRatio17(key);
			}
			//
			if (CellParamCensus.getSsbWaveInterval18() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : ssbwMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setSsbWaveInterval18(key);
			}
			//
			if (CellParamCensus.getTac19() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : tacMap.entrySet()) {
					if (val == null && entry.getKey() != null && Integer.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null
							&& Integer.valueOf(entry.getKey()) != 0) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setTac19(key == null ? null : Integer.valueOf(key));
			}
			//
			if (CellParamCensus.getSsbSending20() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : ssbsMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setSsbSending20(key);
			}
			//
			if (CellParamCensus.getP_a_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : paMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setP_a_r(key);
			}
			//
			if (CellParamCensus.getP_b_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : pbMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setP_b_r(key);
			}
			//
			if (CellParamCensus.getRootSeq_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : rootSeqMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setRootSeq_r(key);
			}
			//
			if (CellParamCensus.getPdcchSymbol_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : pdcchSmMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setPdcchSymbol_r(key);
			}
			//
			if (CellParamCensus.getCellTRx_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : cellTrxMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setCellTRx_r(key);
			}
			//
			if (CellParamCensus.getFrameStructure_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : frameStrcMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				CellParamCensus.setFrameStructure_r(key);
			}
		}

		// 20~30?????????
		if ((CellParamCensus.getCellId11() == null && CellParamCensus.getCellId1() == null)
				|| (CellParamCensus.getCellId11() != null && CellParamCensus.getCellId1() != null
						&& CellParamCensus.getCellId1().equals(CellParamCensus.getCellId11()))) {
			CellParamCensus.setCellIdStatus21("??????");
		} else {
			CellParamCensus.setCellIdStatus21("?????????");
		}

		if ((CellParamCensus.getPci2() == null && CellParamCensus.getPci12() == null)
				|| (CellParamCensus.getPci2() != null && CellParamCensus.getPci12() != null
						&& CellParamCensus.getPci2().equals(CellParamCensus.getPci12()))) {
			CellParamCensus.setPciStatus22("??????");
		} else {
			CellParamCensus.setPciStatus22("?????????");
		}

		if ((CellParamCensus.getNrFrequency13() == null && CellParamCensus.getNrFrequency3() == null)
				|| (CellParamCensus.getNrFrequency13() != null && CellParamCensus.getNrFrequency3() != null
						&& CellParamCensus.getNrFrequency13().equals(CellParamCensus.getNrFrequency3()))) {
			CellParamCensus.setNrFrequencyStatus23("??????");
		} else {
			CellParamCensus.setNrFrequencyStatus23("?????????");
		}

		if ((CellParamCensus.getFrequency14() == null && CellParamCensus.getFrequency114() == null)
				|| (CellParamCensus.getFrequency14() != null && CellParamCensus.getFrequency114() != null
						&& CellParamCensus.getFrequency14().equals(CellParamCensus.getFrequency114()))) {
			CellParamCensus.setFrequencyStatus24("??????");
		} else {
			CellParamCensus.setFrequencyStatus24("?????????");
		}

		if ((CellParamCensus.getCellBroadband15() == null && CellParamCensus.getCellBroadband5() == null)
				|| (CellParamCensus.getCellBroadband15() != null && CellParamCensus.getCellBroadband5() != null
						&& CellParamCensus.getCellBroadband15().equals(CellParamCensus.getCellBroadband5()))) {
			CellParamCensus.setCellBroadbandStatus25("??????");
		} else {
			CellParamCensus.setCellBroadbandStatus25("?????????");
		}

		if ("FLEXIBLE".equals(CellParamCensus.getSpecialRatio16())
				|| (CellParamCensus.getSpecialRatio16() == null && CellParamCensus.getSpecialRatio6() == null)
				|| (CellParamCensus.getSpecialRatio16() != null && CellParamCensus.getSpecialRatio6() != null
						&& CellParamCensus.getSpecialRatio16().equals(CellParamCensus.getSpecialRatio6()))) {
			CellParamCensus.setSpecialRatioStatus26("??????");
		} else {
			CellParamCensus.setSpecialRatioStatus26("?????????");
		}

		if ("FLEXIBLE".equals(CellParamCensus.getUpAndDownRatio17())
				|| (CellParamCensus.getUpAndDownRatio17() == null && CellParamCensus.getUpAndDownRatio7() == null)
				|| (CellParamCensus.getUpAndDownRatio17() != null && CellParamCensus.getUpAndDownRatio7() != null
						&& CellParamCensus.getUpAndDownRatio17().equals(CellParamCensus.getUpAndDownRatio7()))) {
			CellParamCensus.setUpAndDownRatioStatus27("??????");
		} else {
			CellParamCensus.setUpAndDownRatioStatus27("?????????");
		}

		if ((CellParamCensus.getSsbWaveInterval18() == null && CellParamCensus.getSsbWaveInterval8() == null)
				|| (CellParamCensus.getSsbWaveInterval18() != null && CellParamCensus.getSsbWaveInterval8() != null
						&& CellParamCensus.getSsbWaveInterval18().equals(CellParamCensus.getSsbWaveInterval8()))) {
			CellParamCensus.setSsbWaveInterval28("??????");
		} else {
			CellParamCensus.setSsbWaveInterval28("?????????");
		}

		if ((CellParamCensus.getTac9() == null && CellParamCensus.getTac19() == null)
				|| (CellParamCensus.getTac9() != null && CellParamCensus.getTac19() != null
						&& CellParamCensus.getTac9().equals(CellParamCensus.getTac19()))) {
			CellParamCensus.setTacStatus29("??????");
		} else {
			CellParamCensus.setTacStatus29("?????????");
		}

		if ((CellParamCensus.getSsbSending20() == null && CellParamCensus.getSsbSending10() == null)
				|| (CellParamCensus.getSsbSending20() != null && CellParamCensus.getSsbSending10() != null
						&& CellParamCensus.getSsbSending20().equals(CellParamCensus.getSsbSending10()))) {
			CellParamCensus.setSsbSendingStatus30("??????");
		} else {
			CellParamCensus.setSsbSendingStatus30("?????????");
		}

		if ((CellParamCensus.getP_a_r() == null && CellParamCensus.getP_a_p() == null)
				|| (CellParamCensus.getP_a_r() != null && CellParamCensus.getP_a_p() != null
						&& CellParamCensus.getP_a_r().equals(CellParamCensus.getP_a_p()))) {
			CellParamCensus.setP_a_rlt("??????");
		} else {
			CellParamCensus.setP_a_rlt("?????????");
		}

		if ((CellParamCensus.getP_b_r() == null && CellParamCensus.getP_b_p() == null)
				|| (CellParamCensus.getP_b_r() != null && CellParamCensus.getP_b_p() != null
						&& CellParamCensus.getP_b_r().equals(CellParamCensus.getP_b_p()))) {
			CellParamCensus.setP_b_rlt("??????");
		} else {
			CellParamCensus.setP_b_rlt("?????????");
		}

		if ((CellParamCensus.getRootSeq_r() == null && CellParamCensus.getRootSeq_p() == null)
				|| (CellParamCensus.getRootSeq_r() != null && CellParamCensus.getRootSeq_p() != null
						&& CellParamCensus.getRootSeq_r().equals(CellParamCensus.getRootSeq_p()))) {
			CellParamCensus.setRootSeq_rlt("??????");
		} else {
			CellParamCensus.setRootSeq_rlt("?????????");
		}

		if ((CellParamCensus.getPdcchSymbol_r() == null && CellParamCensus.getPdcchSymbol_p() == null)
				|| (CellParamCensus.getPdcchSymbol_r() != null && CellParamCensus.getPdcchSymbol_p() != null
						&& CellParamCensus.getPdcchSymbol_r().equals(CellParamCensus.getPdcchSymbol_p()))) {
			CellParamCensus.setPdcchSymbol_rlt("??????");
		} else {
			CellParamCensus.setPdcchSymbol_rlt("?????????");
		}

		if ((CellParamCensus.getCellTRx_r() == null && CellParamCensus.getCellTRx_p() == null)
				|| (CellParamCensus.getCellTRx_r() != null && CellParamCensus.getCellTRx_p() != null
						&& CellParamCensus.getCellTRx_r().equals(CellParamCensus.getCellTRx_p()))) {
			CellParamCensus.setCellTRx_rlt("??????");
		} else {
			CellParamCensus.setCellTRx_rlt("?????????");
		}

		if ((CellParamCensus.getFrameStructure_r() == null && CellParamCensus.getFrameStructure_p() == null)
				|| (CellParamCensus.getFrameStructure_r() != null && CellParamCensus.getFrameStructure_p() != null
						&& CellParamCensus.getFrameStructure_r().equals(CellParamCensus.getFrameStructure_p()))) {
			CellParamCensus.setFrameStructure_rlt("??????");
		} else {
			CellParamCensus.setFrameStructure_rlt("?????????");
		}

		// ???????????????????????? ?????????????????????
		Menu menu = terminalMenuService.get2(plan.getCity());
		if (menu == null || menu.getStationParamPojo() == null) {
			if (CellParamCensus.getPciStatus22().equals("?????????")) {
				plan.setWirelessParamStatus("PCI?????????");
			} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
					|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
				plan.setWirelessParamStatus("?????????????????????");
			} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
				plan.setWirelessParamStatus("TAC?????????");
			} else {
				plan.setWirelessParamStatus("????????????");
			}
		} else if (menu.getStationParamPojo() != null) {
			StationParamPojo spp = menu.getStationParamPojo();
			if (spp.getSingleStationMOdelSelect()==null || "1".equals(spp.getSingleStationMOdelSelect())) { // ????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else if ("2".equals(spp.getSingleStationMOdelSelect())) { // ????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")
						|| CellParamCensus.getSsbWaveInterval28().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else if ("3".equals(spp.getSingleStationMOdelSelect())) { // ????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("?????????")
						|| CellParamCensus.getP_a_rlt().equals("?????????") || CellParamCensus.getP_b_rlt().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else if ("4".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else if ("5".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")
						|| CellParamCensus.getSsbWaveInterval28().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else if ("6".equals(spp.getSingleStationMOdelSelect())) { // ??????????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else if ("7".equals(spp.getSingleStationMOdelSelect()) || "8".equals(spp.getSingleStationMOdelSelect())) { // ???????????????????????????????????????????????????????????????
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("?????????")
						|| CellParamCensus.getP_a_rlt().equals("?????????") || CellParamCensus.getP_b_rlt().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			} else {
				if (CellParamCensus.getPciStatus22().equals("?????????")) {
					plan.setWirelessParamStatus("PCI?????????");
				} else if (CellParamCensus.getFrequencyStatus24().equals("?????????")
						|| CellParamCensus.getNrFrequencyStatus23().equals("?????????")) {
					plan.setWirelessParamStatus("?????????????????????");
				} else if (CellParamCensus.getTacStatus29().equals("?????????")) {
					plan.setWirelessParamStatus("TAC?????????");
				} else {
					plan.setWirelessParamStatus("????????????");
				}
			}
		}

		CellParamCensus.setPlanParamPojo(plan);
		stationReportCreatService.createExceptionCellParam(CellParamCensus);
	}
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @param plan
	 * @param cellLogList
	 */
	private void addContrastParam(PlanParamPojo plan) {
		
		//?????????????????????localcellid????????????????????????????????????????????????
		List<StationNetoptReceiveTestPojo> netoptReceiveTestList = stationReportCreatService.getNetoptReceiveTest(plan.getSiteName());
		List<StationPerformanceReceivePojo> performanceReceiveList = stationReportCreatService.getPerformanceReceiveList(plan.getSiteName());
		List<StationBasicParamPojo> stationBasicParamList = stationReportCreatService.getStationBasicParamList(plan.getSiteName());
		
		StationNetoptReceiveTestPojo netoptReceiveTest = null; 
		StationPerformanceReceivePojo performanceReceive = null;
		StationBasicParamPojo stationBasicParamPojo = null;
		
		//???????????????
		if(plan.getStationNetoptReceiveTestList()!=null && plan.getStationNetoptReceiveTestList().size()>0 ){
			netoptReceiveTest = plan.getStationNetoptReceiveTestList().get(0);
			
			//???????????????
			Float wireCoverRate = null;
			for(StationNetoptReceiveTestPojo stationNetoptReceiveTestPojo : netoptReceiveTestList) {
				if (netoptReceiveTest.getDtWeakCoverRateMin() == null) {
					if(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin()!=null && StringUtils.hasText(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin())){
						wireCoverRate = 1- Float.valueOf(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin());
						netoptReceiveTest.setDtWeakCoverRateMin(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin());
					}
				} else if (stationNetoptReceiveTestPojo.getDtWeakCoverRateMin() != null && StringUtils.hasText(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin())
						&& Float.valueOf(netoptReceiveTest.getDtWeakCoverRateMin()) >= Float.valueOf(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin())) {
					wireCoverRate = 1- Float.valueOf(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin());
					netoptReceiveTest.setDtWeakCoverRateMin(stationNetoptReceiveTestPojo.getDtWeakCoverRateMin());
				}
			}
			netoptReceiveTest.setDtWireCoverRate(wireCoverRate == null ? null : String.format("%.2f",wireCoverRate*100));
			
			//????????????
			Float nrSwitchSuccessAvg  = 0F;
			Integer n= 0;
			for(StationNetoptReceiveTestPojo stationNetoptReceiveTestPojo : netoptReceiveTestList) {
				if(stationNetoptReceiveTestPojo.getNrSwitchSuccessRate() != null){
					n++;
					nrSwitchSuccessAvg = nrSwitchSuccessAvg + Float.valueOf(stationNetoptReceiveTestPojo.getNrSwitchSuccessRate());
				}
			}
			if(n != 0) {
				nrSwitchSuccessAvg = nrSwitchSuccessAvg / n;
			}
			netoptReceiveTest.setNrSwitchSuccessAvg(String.format("%.2f",nrSwitchSuccessAvg*100));
		}
		
		//?????????????????????
		if(plan.getStationPerformanceReceiveList()!=null && plan.getStationPerformanceReceiveList().size()>0 ){
		    performanceReceive = plan.getStationPerformanceReceiveList().get(0);
			performanceReceive.setLteSwitchSuccessRation37("???");
			performanceReceive.setNrSwitchSuccessRation38("???");
			performanceReceive.setNrEarfcnSuccessRation42("???");
			performanceReceive.setLteEarfcnSuccessRation46("???");
			performanceReceive.setNrSwitchSuccessAvg50("???");
			//???????????????
			if (netoptReceiveTest.getDtWireCoverRate()!=null && Float.valueOf(netoptReceiveTest.getDtWireCoverRate()) > 0.92) {
				performanceReceive.setWireCoverRate54("???");
			} else {
				performanceReceive.setWireCoverRate54("???");
			}
			for (StationPerformanceReceivePojo stationPerformanceReceivePojo : performanceReceiveList) {
				if(stationPerformanceReceivePojo.getLteInterfreqHandoverSuccessRation()==null 
						|| stationPerformanceReceivePojo.getLteInterfreqHandoverSuccessRation().equals("???")){
					performanceReceive.setLteSwitchSuccessRation37("???");
				}
				if(stationPerformanceReceivePojo.getNrEdschangeSuccessRation()==null 
						|| stationPerformanceReceivePojo.getNrEdschangeSuccessRation().equals("???")){
					performanceReceive.setNrSwitchSuccessRation38("???");
				}
				if(stationPerformanceReceivePojo.getNrEarfcnSuccessRation()==null 
						|| stationPerformanceReceivePojo.getNrEarfcnSuccessRation().equals("???")){
					performanceReceive.setNrEarfcnSuccessRation42("???");
				}
				if(stationPerformanceReceivePojo.getLteEarfcnSuccessRation() == null 
						|| stationPerformanceReceivePojo.getLteEarfcnSuccessRation().equals("???")){
					performanceReceive.setLteEarfcnSuccessRation46("???");
				}
				if(netoptReceiveTest.getNrSwitchSuccessAvg() != null && Float.valueOf(netoptReceiveTest.getNrSwitchSuccessAvg()) > 0.98){
					performanceReceive.setNrSwitchSuccessAvg50("???");;
				}
			}
		}
		
		//?????????????????????
		if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
			stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
			stationBasicParamPojo.setHeightContrastBySitname81("??????");
			stationBasicParamPojo.setAauModelContrastBySitname("??????");
			stationBasicParamPojo.setAzimuthContrastBySitname("??????");
			stationBasicParamPojo.setTiltEContrastBySitname("??????");
			stationBasicParamPojo.setTiltMContrastBySitname("??????");
			stationBasicParamPojo.setHeightContrastByParam77("??????");
			for (StationBasicParamPojo basicParamPojo : stationBasicParamList) {
				if(basicParamPojo.getAdjustHeightContrast() == null 
						|| basicParamPojo.getAdjustHeightContrast().equals("?????????")){
					stationBasicParamPojo.setHeightContrastBySitname81("?????????");
				}
				if(basicParamPojo.getAdjustAauModelContrast() == null 
						|| basicParamPojo.getAdjustAauModelContrast().equals("?????????")){
					stationBasicParamPojo.setAauModelContrastBySitname("?????????");
				}
				if(basicParamPojo.getAdjustAzimuthContrast() == null 
						|| basicParamPojo.getAdjustAzimuthContrast().equals("?????????")){
					stationBasicParamPojo.setAzimuthContrastBySitname("?????????");
				}
				if(basicParamPojo.getAdjustTiltEContrast() == null 
						|| basicParamPojo.getAdjustTiltEContrast().equals("?????????")){
					stationBasicParamPojo.setTiltEContrastBySitname("?????????");
				}
				if(basicParamPojo.getAdjustTiltMContrast() == null 
						|| basicParamPojo.getAdjustTiltMContrast().equals("?????????")){
					stationBasicParamPojo.setTiltMContrastBySitname("?????????");
				}
				if(stationBasicParamPojo.getAdjustHeight() == null 
						||Math.abs(plan.getHeight()-stationBasicParamPojo.getAdjustHeight())>3){
					stationBasicParamPojo.setHeightContrastByParam77("?????????");
				}
			}
		}
		
		List<StationPerformanceReceivePojo> perList = new ArrayList<StationPerformanceReceivePojo>();
		perList.add(performanceReceive);
		plan.setStationPerformanceReceiveList(perList);
		performanceReceive.setPlanParamPojo(plan);
		stationReportCreatService.updatePerformanceReceive(performanceReceive);
		
		List<StationBasicParamPojo> basiceParamList = new ArrayList<StationBasicParamPojo>();
		basiceParamList.add(stationBasicParamPojo);
		plan.setStationBasicParamPojoList(basiceParamList);
		stationBasicParamPojo.setPlanParamPojo(plan);
		stationReportCreatService.updateStationBasicParamTest(stationBasicParamPojo);
	
	}
	
	/**
	 * ????????????3d??????
	 * @return
	 */
	public void createLteReport(String ids){
		cellNames = "";
		String[] idList = ids.split(",");
		List<Plan4GParam> planList = new ArrayList<Plan4GParam>();
		//??????????????????
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String newDate = sdf.format(new Date());
		
		HashSet<String> siteNames = new HashSet<String>();
		List<Plan4GParam> p4pList = new ArrayList<Plan4GParam>();
		List<Long> idLong = new ArrayList<Long>();
		//?????????:????????????????????????????????????
		for(String id : idList){
			Plan4GParam plan = oppositeOpen3dReportCreateService.find(Long.valueOf(id));
			List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellLog(plan.getCellName(),logNameList,null,null);
			if(cellLogList == null || cellLogList.size() < 1){
				if(!StringUtils.hasText(cellNames)){
					cellNames = plan.getCellName();
				}else{
					cellNames = cellNames + "," +plan.getCellName();
				}
				continue;
			}
			plan.setCreateReportDate(Long.valueOf(newDate));
			planList.add(plan);
			oppositeOpen3dReportCreateService.update(plan);
			idLong.add(Long.valueOf(id));
			siteNames.add(plan.getSiteName());
			p4pList.add(plan);
		}
		
		Map<String,String> mapModel = new HashMap<String, String>();
		
		//?????????????????????
		oppositeOpen3dReportCreateService.deleteReportByids(idLong);
		
		for (Plan4GParam plan : p4pList) {
			plan.setOppositeOpen3dPerformanceReceivePojo(null);
			plan.setOppositeOpen3dResultPojo(null);
			plan.setOppositeOpen3dWirelessPojo(null);
			plan.setStationBasicParamPojoList(null);
			Menu menu = terminalMenuService.get2(plan.getRegion());	
			if (menu == null || menu.getStationParamPojo() == null) {
				mapModel.put(plan.getRegion(), "????????????3d??????");
				plan.setOpposOpenTemplateSelect(1);
			}else{
				StationParamPojo spp = menu.getStationParamPojo();
				if("2".equals(spp.getContrastOpen3DMOdelSelect())) { // ????????????3d??????
					mapModel.put(plan.getRegion(), "????????????3d??????");
					plan.setOpposOpenTemplateSelect(2);
				}else { // ????????????3d??????
					mapModel.put(plan.getRegion(), "????????????3d??????");
					plan.setOpposOpenTemplateSelect(1);
				}
			}
			
			if(mapModel.get(plan.getRegion()).equals("????????????3d??????") || mapModel.get(plan.getRegion()).equals("????????????3d??????")){
				//???????????????????????????
				createWirelessCellReport(Long.valueOf(newDate),siteNames, plan,mapModel.get(plan.getRegion()));
				
				//?????????:????????????????????????
				creatPerformanceReport(plan,newDate,mapModel.get(plan.getRegion()));

				//?????????
				creatResultReport(plan,newDate);
				
				// ???????????????????????????????????????
				stationBaseParamSave(newDate, plan);
			}
		}
		
		//????????????localCellid??????????????????????????????????????????????????????????????????????????????
		List<Plan4GParam> newPlanList = new ArrayList<Plan4GParam>();
		for (Long id : idLong) {
			Plan4GParam plan = oppositeOpen3dReportCreateService.find2(Long.valueOf(id));
			newPlanList.add(plan);
		}
		for (Plan4GParam plan : newPlanList) {
			StationBasicParamPojo stationBasicParamPojo = null;
			StationProspectParamPojo stationProspectParamPojo = stationProspectParamService
					.findBySiteName(plan.getSiteName());

			if(mapModel.get(plan.getRegion()).equals("????????????3d??????") || mapModel.get(plan.getRegion()).equals("????????????3d??????")){
				//????????????????????????????????????
				addStationBasicParam(plan);
				//??????????????????
				if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
					stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
				}
				if (stationProspectParamPojo != null) {
					if (mapModel.get(plan.getRegion()).equals("????????????3d??????")) { // ????????????
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getHeightContrastBySitname81() == null 
								|| stationBasicParamPojo.getHeightContrastBySitname81().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAauModelContrastBySitname() == null
								|| stationBasicParamPojo.getTiltEContrastBySitname() == null
								|| stationBasicParamPojo.getAzimuthContrastBySitname() == null
								||stationBasicParamPojo.getAauModelContrastBySitname().equals("?????????")
								|| stationBasicParamPojo.getTiltEContrastBySitname().equals("?????????")
								|| stationBasicParamPojo.getAzimuthContrastBySitname().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}else{
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("?????????") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("?????????") ){
							plan.setStationProspectRlt("???????????????");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								|| stationBasicParamPojo.getAdjustTiltEContrast() == null
								|| stationBasicParamPojo.getAdjustAzimuthContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustTiltEContrast().equals("?????????")
								|| stationBasicParamPojo.getAdjustAzimuthContrast().equals("?????????")){
							plan.setStationProspectRlt("??????????????????");
						} else{
							plan.setStationProspectRlt("????????????");
						}
					}
				}
				
			}	
			oppositeOpen3dReportCreateService.update(plan);
		}
	}
	
	/**
	 * ???????????????????????????
	 * 
	 * @param plan
	 */
	private void addStationBasicParam(Plan4GParam plan) {
		//?????????????????????localcellid????????????????????????
		List<StationBasicParamPojo> stationBasicParamList = stationReportCreatService.getStationBasicParamList(plan.getSiteName());
		
		StationBasicParamPojo stationBasicParamPojo = null;
		
		//?????????????????????
		if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
			stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
			stationBasicParamPojo.setHeightContrastBySitname81("??????");
			stationBasicParamPojo.setAauModelContrastBySitname("??????");
			stationBasicParamPojo.setAzimuthContrastBySitname("??????");
			stationBasicParamPojo.setTiltEContrastBySitname("??????");
			stationBasicParamPojo.setTiltMContrastBySitname("??????");
			stationBasicParamPojo.setHeightContrastByParam77("??????");
			for (StationBasicParamPojo basicParamPojo : stationBasicParamList) {
				if(basicParamPojo.getAdjustHeightContrast() == null 
						|| basicParamPojo.getAdjustHeightContrast().equals("?????????")){
					stationBasicParamPojo.setHeightContrastBySitname81("?????????");
				}
				if(basicParamPojo.getAdjustAauModelContrast() == null 
						|| basicParamPojo.getAdjustAauModelContrast().equals("?????????")){
					stationBasicParamPojo.setAauModelContrastBySitname("?????????");
				}
				if(basicParamPojo.getAdjustAzimuthContrast() == null 
						|| basicParamPojo.getAdjustAzimuthContrast().equals("?????????")){
					stationBasicParamPojo.setAzimuthContrastBySitname("?????????");
				}
				if(basicParamPojo.getAdjustTiltEContrast() == null 
						|| basicParamPojo.getAdjustTiltEContrast().equals("?????????")){
					stationBasicParamPojo.setTiltEContrastBySitname("?????????");
				}
				if(basicParamPojo.getAdjustTiltMContrast() == null 
						|| basicParamPojo.getAdjustTiltMContrast().equals("?????????")){
					stationBasicParamPojo.setTiltMContrastBySitname("?????????");
				}
				if(stationBasicParamPojo.getAdjustHeight() == null 
						||Math.abs(plan.getHigh()-stationBasicParamPojo.getAdjustHeight())>3){
					stationBasicParamPojo.setHeightContrastByParam77("?????????");
				}
			}
		}
		
		List<StationBasicParamPojo> basiceParamList = new ArrayList<StationBasicParamPojo>();
		basiceParamList.add(stationBasicParamPojo);
		plan.setStationBasicParamPojoList(basiceParamList);
		stationBasicParamPojo.setPlan4GParam(plan);
		stationReportCreatService.updateStationBasicParamTest(stationBasicParamPojo);
	
	}
	
	
	/**
	 * ???????????????????????????????????????
	 * 
	 * @author lucheng date:2020???6???24??? ??????3:02:31
	 * @param newDate ??????????????????
	 * @param plan ????????????
	 * @param cellLogList ????????????
	 */
	private void stationBaseParamSave(String newDate, Plan4GParam plan) {

		// ????????????????????????localcellid???????????????????????????
//		List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(plan.getSiteName());
		List<Plan4GParam> planParamPojoAscList = oppositeOpen3dReportCreateService.getAllLocalCellId(plan.getEnbId());

		StationBasicParamPojo stationBasicParamPojo = new StationBasicParamPojo();

		stationBasicParamPojo.setRegion(plan.getRegion());
		stationBasicParamPojo.setCellName(plan.getCellName());
		stationBasicParamPojo.setSiteName(plan.getSiteName());
		stationBasicParamPojo.setCellParamId(plan.getId());
		stationBasicParamPojo.setLocalCellId(plan.getLocalCellId().toString());

		// ????????????????????????
		stationBasicParamPojo.setLon(plan.getLongitude());
		stationBasicParamPojo.setLat(plan.getLatitude());
		if(plan.getEnbId() != null){
			stationBasicParamPojo.setGnbId(plan.getEnbId().toString());
		}
		if(plan.getHigh() != null){
			stationBasicParamPojo.setHeight(plan.getHigh().intValue());
		}
		if(plan.getAzimuth() != null){
			stationBasicParamPojo.setAzimuth(plan.getAzimuth().intValue());
		}
		if (plan.getTilt_e() != null && plan.getTilt_m() != null) {
			stationBasicParamPojo.setTiltE(plan.getTilt_e().intValue());
			stationBasicParamPojo.setTiltM(plan.getTilt_m().intValue());
			stationBasicParamPojo.setTiltTotal(plan.getTilt_e().intValue() + plan.getTilt_m().intValue());
		} else if (plan.getTilt_e() != null) {
			stationBasicParamPojo.setTiltE(plan.getTilt_e().intValue());
			stationBasicParamPojo.setTiltTotal(plan.getTilt_e().intValue());
		} else if (plan.getTilt_m() != null) {
			stationBasicParamPojo.setTiltM(plan.getTilt_m().intValue());
			stationBasicParamPojo.setTiltTotal(plan.getTilt_m().intValue());
		}
		stationBasicParamPojo.setAauModel(plan.getAauModel());
		stationBasicParamPojo.setTac(plan.getTac());
		stationBasicParamPojo.setAntennaManufacturer(plan.getAntennaManufacturer());
		
		if(plan.getTilt_e() != null){
			stationBasicParamPojo.setAdjustTiltE(plan.getTilt_e().intValue());
		}
		
		// ????????????????????????
		StationProspectParamPojo stationProspectParamPojo = stationProspectParamService
				.findBySiteName(plan.getSiteName());
		if (stationProspectParamPojo != null) {
			stationBasicParamPojo.setAdjustHeight(stationProspectParamPojo.getHeight());
			stationBasicParamPojo.setAdjustAddress(stationProspectParamPojo.getAddress());
			stationBasicParamPojo.setAdjustAauModel(stationProspectParamPojo.getAauModel());
			;
			stationBasicParamPojo.setAdjustAntennaType(stationProspectParamPojo.getAntennaType());
			stationBasicParamPojo.setAdjustLon(Float.valueOf(stationProspectParamPojo.getLongitude()));
			stationBasicParamPojo.setAdjustLat(Float.valueOf(stationProspectParamPojo.getLatitude()));

			for (int i = 0; i < planParamPojoAscList.size(); i++) {
				if (plan.getLocalCellId() == planParamPojoAscList.get(i).getLocalCellId()) {
					if (i == 0) {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell1Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell1TiltM());
						if(plan.getTilt_e() != null){
							stationBasicParamPojo.setAdjustTiltE(plan.getTilt_e().intValue());
						}
						if (stationBasicParamPojo.getAdjustTiltM() != null
								&& stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(
									stationBasicParamPojo.getAdjustTiltM() + stationBasicParamPojo.getAdjustTiltE());
						} else if (stationBasicParamPojo.getAdjustTiltM() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltM());
						} else if (stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltE());
						}
					} else if (i == 1) {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell2Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell2TiltM());
						if(plan.getTilt_e() != null){
							stationBasicParamPojo.setAdjustTiltE(plan.getTilt_e().intValue());
						}
						if (stationBasicParamPojo.getAdjustTiltM() != null
								&& stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(
									stationBasicParamPojo.getAdjustTiltM() + stationBasicParamPojo.getAdjustTiltE());
						} else if (stationBasicParamPojo.getAdjustTiltM() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltM());
						} else if (stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltE());
						}
					} else {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell3Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell3TiltM());
						if(plan.getTilt_e() != null){
							stationBasicParamPojo.setAdjustTiltE(plan.getTilt_e().intValue());
						}
						if (stationBasicParamPojo.getAdjustTiltM() != null
								&& stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(
									stationBasicParamPojo.getAdjustTiltM() + stationBasicParamPojo.getAdjustTiltE());
						} else if (stationBasicParamPojo.getAdjustTiltM() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltM());
						} else if (stationBasicParamPojo.getAdjustTiltE() != null) {
							stationBasicParamPojo.setAdjustTiltToatal(stationBasicParamPojo.getAdjustTiltE());
						}
					}
				}
			}

			stationBasicParamPojo.setAdjustGnbId(String.valueOf(planParamPojoAscList.get(0).getEnbId()));

			if (stationBasicParamPojo.getHeight() != null && stationBasicParamPojo.getAdjustHeight() != null
					&& Math.abs(stationBasicParamPojo.getHeight() - stationBasicParamPojo.getAdjustHeight()) < 3) {
				stationBasicParamPojo.setAdjustHeightContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustHeightContrast("?????????");
			}

			if (stationBasicParamPojo.getAzimuth() != null && stationBasicParamPojo.getAdjustAzimuth() != null
					&& Math.abs(stationBasicParamPojo.getAzimuth() - stationBasicParamPojo.getAdjustAzimuth()) < 10) {
				stationBasicParamPojo.setAdjustAzimuthContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustAzimuthContrast("?????????");
			}

			if (stationBasicParamPojo.getTiltTotal() != null && stationBasicParamPojo.getAdjustTiltToatal() != null
					&& Math.abs(
							stationBasicParamPojo.getTiltTotal() - stationBasicParamPojo.getAdjustTiltToatal()) < 2) {
				stationBasicParamPojo.setAdjustTiltToatalContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltToatalContrast("?????????");
			}

			if (stationBasicParamPojo.getTiltE() != null && stationBasicParamPojo.getAdjustTiltE() != null
					&& Math.abs(stationBasicParamPojo.getTiltE() - stationBasicParamPojo.getAdjustTiltE()) < 2) {
				stationBasicParamPojo.setAdjustTiltEContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltEContrast("?????????");
			}

			if (stationBasicParamPojo.getTiltM() != null && stationBasicParamPojo.getAdjustTiltM() != null
					&& Math.abs(stationBasicParamPojo.getTiltM() - stationBasicParamPojo.getAdjustTiltM()) < 2) {
				stationBasicParamPojo.setAdjustTiltMContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltMContrast("?????????");
			}

			if (stationBasicParamPojo.getAauModel() != null && stationBasicParamPojo.getAdjustAauModel() != null
					&& stationBasicParamPojo.getAauModel().equals(stationBasicParamPojo.getAdjustAauModel())) {
				stationBasicParamPojo.setAdjustTiltMContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustTiltMContrast("?????????");
			}

			if (stationBasicParamPojo.getLon() != null && stationBasicParamPojo.getAdjustLon() != null
					&& String.format("%.4f", stationBasicParamPojo.getLon())
							.equals(String.format("%.4f", stationBasicParamPojo.getAdjustLon()))) {
				stationBasicParamPojo.setAdjustLonContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustLonContrast("?????????");
			}

			if (stationBasicParamPojo.getLat() != null && stationBasicParamPojo.getAdjustLat() != null
					&& String.format("%.4f", stationBasicParamPojo.getLat())
							.equals(String.format("%.4f", stationBasicParamPojo.getAdjustLat()))) {
				stationBasicParamPojo.setAdjustLatContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustLatContrast("?????????");
			}

			if (stationBasicParamPojo.getGnbId() != null && stationBasicParamPojo.getAdjustGnbId() != null
					&& stationBasicParamPojo.getGnbId().equals(stationBasicParamPojo.getAdjustGnbId())) {
				stationBasicParamPojo.setAdjustGnBIdContrast("??????");
			} else {
				stationBasicParamPojo.setAdjustGnBIdContrast("?????????");
			}

			if (stationBasicParamPojo.getLon() != null && stationBasicParamPojo.getAdjustLon() != null
					&& stationBasicParamPojo.getLat() != null && stationBasicParamPojo.getAdjustLat() != null) {
				Double value = Math
						.sqrt(Math.pow(((Math.abs(stationBasicParamPojo.getAdjustLon() - stationBasicParamPojo.getLon()) * 110000)
								+ (Math.abs(stationBasicParamPojo.getAdjustLat() - stationBasicParamPojo.getLat())* 110000*Math.cos(stationBasicParamPojo.getAdjustLat()))),2));
				stationBasicParamPojo.setLonLatCompute(String.valueOf(value));
			} else {
				stationBasicParamPojo.setLonLatCompute(null);
			}

		} else {
			errorMsg = errorMsg+" ??????'"+plan.getSiteName()+"'?????????????????????????????????! ";
		}
		List<StationBasicParamPojo> list  = new ArrayList<StationBasicParamPojo>();
		list.add(stationBasicParamPojo);
		plan.setStationBasicParamPojoList(list);
		stationBasicParamPojo.setPlan4GParam(plan);
		stationReportCreatService.createStationBasicParamTest(stationBasicParamPojo);
	}
	
	/**
	 * ??????3d???????????????
	 * @param p4pList
	 * @param newDate
	 */
	public void creatResultReport(Plan4GParam p4p , String newDate){
		OppositeOpen3dWirelessPojo oow = p4p.getOppositeOpen3dWirelessPojo();
		OppositeOpen3dPerformanceReceivePojo oop = p4p.getOppositeOpen3dPerformanceReceivePojo();
		OppositeOpen3dResultPojo oor = new OppositeOpen3dResultPojo();
		if(oow != null){
			if(oow.getPciContrast() != null && oow.getPciContrast() == 1
					&& oow.getFrequencyDlContrast() != null && oow.getFrequencyDlContrast() == 1
					&& oow.getEarfcnContrast() != null && oow.getEarfcnContrast() == 1 
					&& oow.getBroadBandContrast() != null && oow.getBroadBandContrast() == 1
					&& oow.getCellIdContrast() != null && oow.getCellIdContrast() == 1 
					&& oow.getEnbIdContrast() != null && oow.getEnbIdContrast() == 1 
					&& oow.getP_aContrast() != null && oow.getP_aContrast() == 1 
					&& oow.getP_bContrast() != null && oow.getP_bContrast() == 1 
					&& oow.getRs_epreContrast() != null && oow.getRs_epreContrast() == 1 
					&& oow.getSpecialSubFrameConfigContrast() != null && oow.getSpecialSubFrameConfigContrast() == 1
					&& oow.getTacContrast() != null && oow.getTacContrast() == 1){
				oor.setParamCensor(1);
			}else{
				oor.setParamCensor(0);
			}
		}
		if(oop != null){
			oor.setCellMeanUl(oop.getTcpUpload());
			oor.setCellMeanDl(oop.getTcpDownload());
			if(oop.getRrcSetup() != null && oop.getRrcSetup() == 1
				&& oop.getErabSetup() != null && oop.getErabSetup() == 1
				&& oop.getAccess() != null && oop.getAccess() == 1){
				oor.setCellInsert("??????");
			}else{
				oor.setCellInsert("?????????");
			}
			
			oor.setCellMeanPing(oop.getPing());
			if(oop.getChangeTest() != null && oop.getChangeTest() == 1){
				oor.setStationChange("??????");
			}else{
				oor.setStationChange("?????????");
			}
			
			oor.setMutual("??????");
			if(oop.getVolte() != null && oop.getVolte() == 1){
				oor.setVolte("??????");
			}else{
				oor.setVolte("?????????");					
			}
			
			if(oop.getCsfbFunction() != null && oop.getCsfbFunction() == 1){
				oor.setCsfb("??????");
			}else{					
				oor.setCsfb("?????????");
			}
		}
		p4p.setOppositeOpen3dResultPojo(oor);
		oor.setPlan4GParam(p4p);
		if(oor.getId() == null){
			oppositeOpen3dReportCreateService.creatOOR(oor);			
		}else{
			oppositeOpen3dReportCreateService.updateOOR(oor);
		}
	}
	
	public void creatPerformanceReport(Plan4GParam p4p , String newDate, String cityTemplate){
		//?????????:????????????????????????
		OppositeOpen3dPerformanceReceivePojo oop = new OppositeOpen3dPerformanceReceivePojo();
		
		oop.setCellId(p4p.getCellId());
		oop.setLocalCellId(p4p.getLocalCellId());
		oop.setCellName(p4p.getCellName());
		oop.setSiteName(p4p.getSiteName());
		//??????????????????
		List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellLog(p4p.getCellName(),logNameList,null,null);
		
		List<Plan4GParam> plan4GParamAscList = oppositeOpen3dReportCreateService.getAllLocalCellId(p4p.getEnbId());
		List<Integer> localCellidList = new ArrayList<Integer>();
		for (Plan4GParam plan4gParam : plan4GParamAscList) {
			localCellidList.add(plan4gParam.getLocalCellId());
		}
		Collections.sort(localCellidList);
		//?????????????????????????????????????????????,?????????????????????????????????????????????????????????????????????FTP?????????????????????????????????
		//ftp??????
		Long timeFtpDownload = null;
		List<EceptionCellLogPojo> ecpFtpDownload = new ArrayList<EceptionCellLogPojo>();
		//ftp??????
		Long timeFtpUpload = null;
		List<EceptionCellLogPojo> ecpFtpUpload = new ArrayList<EceptionCellLogPojo>();
		//ENDC???????????????
		Long timeEndc = null;
		List<EceptionCellLogPojo> ecpEndc = new ArrayList<EceptionCellLogPojo>();
		//ping???32?????????
		Long timePing = null;
		List<EceptionCellLogPojo> ecpPing = new ArrayList<EceptionCellLogPojo>();
		//volte??????
		Long timeVolte = null;
		List<EceptionCellLogPojo> ecpVolte = new ArrayList<EceptionCellLogPojo>();
		//CSFB??????
		Long timeCsfb = null;
		List<EceptionCellLogPojo> ecpCsfb = new ArrayList<EceptionCellLogPojo>();
		//??????
		Long timeRaodian = null;
		List<EceptionCellLogPojo> ecpRaodian = new ArrayList<EceptionCellLogPojo>();
		long currentDate = Long.valueOf(newDate);
		for (EceptionCellLogPojo ecp : cellLogList) {
			Long testDate = ecp.getNrTestdate() == null ? null : Long.valueOf(ecp.getNrTestdate());
			if(testDate==null){
				continue;
			}
			//ftp??????
			if("FTP??????".equals(ecp.getNrTestevent())&& "?????????".equals(ecp.getNrWirelessstation())){
				if(timeFtpDownload == null || Math.abs(currentDate - timeFtpDownload) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeFtpDownload = testDate;
					ecpFtpDownload.add(ecp);
				}else if(Math.abs(currentDate - timeFtpDownload) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpFtpDownload.clear();
					timeFtpDownload = testDate;
					ecpFtpDownload.add(ecp);
				}
			}
			//ftp??????
			if("FTP??????".equals(ecp.getNrTestevent())&& "?????????".equals(ecp.getNrWirelessstation())){
				if(timeFtpUpload == null || Math.abs(currentDate - timeFtpUpload) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeFtpUpload = testDate;
					ecpFtpUpload.add(ecp);
				}else if(Math.abs(currentDate - timeFtpUpload) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpFtpUpload.clear();
					timeFtpUpload = testDate;
					ecpFtpUpload.add(ecp);
				}
			}
			//ENDC???????????????
			if("ENDC???????????????".equals(ecp.getNrTestevent())&& "?????????".equals(ecp.getNrWirelessstation())){
				if(timeEndc == null || Math.abs(currentDate - timeEndc) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeEndc = testDate;
					ecpEndc.add(ecp);
				}else if(Math.abs(currentDate - timeEndc) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpEndc.clear();
					timeEndc = testDate;
					ecpEndc.add(ecp);
				}
			}
			//ping???32?????????
			if (ecp.getNrTestevent().contains("ping")){
				String pingName = ecp.getNrTestevent().substring(ecp.getNrTestevent().indexOf("???")+1, ecp.getNrTestevent().indexOf("???")) ;
				if(StringUtils.hasText(pingName)){
					if (Float.valueOf(pingName)<200) { //ping32??????
						if(timePing == null || Math.abs(currentDate - timePing) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
							timePing = testDate;
							ecpPing.add(ecp);
						}else if(Math.abs(currentDate - timePing) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
							ecpPing.clear();
							timePing = testDate;
							ecpPing.add(ecp);
						}
					}
				}
			}
			//volte??????
			if("volte??????".equals(ecp.getNrTestevent())&& "?????????".equals(ecp.getNrWirelessstation())){
				if(timeVolte == null || Math.abs(currentDate - timeVolte) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeVolte = testDate;
					ecpVolte.add(ecp);
				}else if(Math.abs(currentDate - timeVolte) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpVolte.clear();
					timeVolte = testDate;
					ecpVolte.add(ecp);
				}
			}
			//CSFB??????
			if("CSFB??????".equals(ecp.getNrTestevent())&& "?????????".equals(ecp.getNrWirelessstation())){
				if(timeCsfb == null || Math.abs(currentDate - timeCsfb) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeCsfb = testDate;
					ecpCsfb.add(ecp);
				}else if(Math.abs(currentDate - timeCsfb) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpCsfb.clear();
					timeCsfb = testDate;
					ecpCsfb.add(ecp);
				}
			}
			//??????
			if("??????".equals(ecp.getNrTestevent())){
				if(timeRaodian == null || Math.abs(currentDate - timeRaodian) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeRaodian = testDate;
					ecpRaodian.add(ecp);
				}else if(Math.abs(currentDate - timeRaodian) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpRaodian.clear();
					timeRaodian = testDate;
					ecpRaodian.add(ecp);
				}
			}
		
		}
		//???????????????????????????:rsrp
		String lteRsrp = null; 
		//???????????????????????????:sinr
		String lteSinr = null; 
		//TCP???????????????(60s??????)
		String tcpDownload = null; 
		//TCP???????????????(60s??????)
		String tcpUpload = null; 
		//??????????????????(Mbps)
		String maxUpload = null;
		//??????????????????(Mbps)
		String maxDownload = null;
		//RRC Setup   0:????????? ???1?????????
		Integer rrcSetup = null; 
		//ERAB Setup
		Integer erabSetup = null; 
		//Access
		Integer LteAccessTest = null; 
		//ping??????
		String pingTimeDelay = null; 
		//VOLTE????????????(??????volte?????????????????????volte????????????)
		Integer volteTest = null; 
		//CSFB????????????
		Integer csfbFunctionTest = null; 
		//CSFB????????????????????????
		String csfbConnect = null; 
		//??????????????????????????? 0???"????????????"; 1???"??????2????????????";  2???"??????3????????????";  3???"????????????";
		Integer changeTest = null; 
		//??????volte????????????
		Integer dialingVolteNetTest = 0; 
		//??????volte????????????
		Integer calledVolteNetTest = null; 
		//??????volte????????????????????????
		Integer dialingVolteVideoTest = null; 
		//??????voltee????????????????????????
		Integer calledVolteVideoTest = null; 
		
//					DecimalFormat df = new DecimalFormat("#.000");
		//????????????????????????????????????FTP????????????????????????LTE rsrp?????????LTE sinr?????????(LTE Throughput DL)??? ????????????TCP???????????????(60s??????)/TCP???????????????(60s??????)??????????????????1024.?????????????????????Mbps
		if(ecpFtpDownload != null && ecpFtpDownload.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpFtpDownload) {
				if(eceptionCellLogPojo.getLteRsrp() != null && !eceptionCellLogPojo.getLteRsrp().equals("")){
					if(lteRsrp == null || Double.valueOf(eceptionCellLogPojo.getLteRsrp()) > Double.valueOf(lteRsrp)){
						lteRsrp = eceptionCellLogPojo.getLteRsrp();
					}
				}
				if(eceptionCellLogPojo.getLteSinr() != null && !eceptionCellLogPojo.getLteSinr().equals("")){
					if(lteSinr == null || Double.valueOf(eceptionCellLogPojo.getLteSinr()) > Double.valueOf(lteSinr)){
						lteSinr = eceptionCellLogPojo.getLteSinr();
					}
				}
				if(eceptionCellLogPojo.getLteThrputdl() != null && !eceptionCellLogPojo.getLteThrputdl().equals("")){
					if(tcpDownload == null || Double.valueOf(eceptionCellLogPojo.getLteThrputdl())> Double.valueOf(tcpDownload)){
						tcpDownload = (Double.valueOf(eceptionCellLogPojo.getLteThrputdl())) + "";
					}
				}
				if(eceptionCellLogPojo.getLteThrputDLMax() != null){
					if(maxDownload == null || eceptionCellLogPojo.getLteThrputDLMax()> Float.valueOf(maxDownload)){
						maxDownload = (eceptionCellLogPojo.getLteThrputDLMax()) + "";
					}
				}
			}
		}
		
		//????????????????????????????????????FTP????????????????????????(LTE Throughput UL)???
		if(ecpFtpUpload != null && ecpFtpUpload.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpFtpUpload) {
				if(eceptionCellLogPojo.getLteThrputul() != null && !eceptionCellLogPojo.getLteThrputul().equals("")){
					if(tcpUpload == null || Float.valueOf(eceptionCellLogPojo.getLteThrputul()) > Double.valueOf(tcpUpload)){
						tcpUpload = (Double.valueOf(eceptionCellLogPojo.getLteThrputul())) + "";
					}
				}
				if(eceptionCellLogPojo.getLteThrputULMax() != null){
					if(maxUpload == null || eceptionCellLogPojo.getLteThrputULMax()> Float.valueOf(maxUpload)){
						maxUpload = (eceptionCellLogPojo.getLteThrputULMax()) + "";
					}
				}
			}
		}
		
		//??????????????????????????????ENDC???????????????????????????????????????????????????????????????????????????RRC???????????????????????????ERAB???????????????????????????4G?????????????????????????????????????????????????????????????????????0???????????????????????????????????????0???????????????????????????
		if(ecpEndc != null && ecpEndc.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpEndc) {
				if(eceptionCellLogPojo.getRrcConnectionSetupComplete() != null && !eceptionCellLogPojo.getRrcConnectionSetupComplete().equals("")){
					if(rrcSetup == null){
						rrcSetup = 0;
					}
					if(!eceptionCellLogPojo.getRrcConnectionSetupComplete().equals("0")){
						rrcSetup = 1;
					}
				}
				if(eceptionCellLogPojo.getErabConnectionSetupSuccess() != null && !eceptionCellLogPojo.getErabConnectionSetupSuccess().equals("")){
					if(erabSetup == null){
						erabSetup = 0;
					}
					if(!eceptionCellLogPojo.getErabConnectionSetupSuccess().equals("0")){
						erabSetup = 1;
					}
				}
				if(eceptionCellLogPojo.getLteEpsattachsuccess() != null && !eceptionCellLogPojo.getLteEpsattachsuccess().equals("")){
					if(LteAccessTest == null){
						LteAccessTest = 0;
					}
					if(!eceptionCellLogPojo.getLteEpsattachsuccess().equals("0")){
						LteAccessTest = 1;
					}
				}
			}
		}
		//??????????????????????????????ping???32?????????????????????????????????ping?????????????????????????????????????????????
		if(ecpPing != null && ecpPing.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpPing) {
				if(eceptionCellLogPojo.getNrPingresqtime() != null && !eceptionCellLogPojo.getNrPingresqtime().equals("")){
					if(pingTimeDelay == null || Float.valueOf(eceptionCellLogPojo.getNrPingresqtime()) < Float.valueOf(pingTimeDelay)){
						pingTimeDelay = (Float.valueOf(eceptionCellLogPojo.getNrPingresqtime())) + "";
					}
				}
			}
		}
		//??????????????????????????????volte?????????,????????????????????????????????????,?????????
		if(ecpVolte != null && ecpVolte.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpVolte) {
				//??????????????????????????????volte?????????,????????????????????????????????????,???????????????volte?????????????????????????????????????????????0???????????????????????????????????????0???????????????????????????
				if(eceptionCellLogPojo.getVolteSuccess() != null && !eceptionCellLogPojo.getVolteSuccess().equals("")){
					if(volteTest == null){
						volteTest = 0;
					}
					if(!eceptionCellLogPojo.getVolteSuccess().equals("0")){
						volteTest = 1;
					}
				}
				//??????????????????volte???????????????????????????????????????????????????volte?????????????????????0???????????????????????????volte??????????????????????????????????????????????????????????????????volte?????????????????????
				if(eceptionCellLogPojo.getVolteSuccess() != null && !eceptionCellLogPojo.getVolteSuccess().equals("") 
						&& eceptionCellLogPojo.getVolteDrop() != null && eceptionCellLogPojo.getVolteDrop().equals("0")){
					dialingVolteNetTest = dialingVolteNetTest + Integer.valueOf(eceptionCellLogPojo.getVolteSuccess());
				}
			}
		}
		//??????????????????????????????CSFB?????????,??????????????????????????????????????????????????????csfb?????????????????????????????????????????????0???????????????????????????????????????0?????????????????????,???CSFB??????????????????????????????????????????????????????
		if(ecpCsfb != null && ecpCsfb.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpCsfb) {
				if(eceptionCellLogPojo.getCsfbSuccess() != null && !eceptionCellLogPojo.getCsfbSuccess().equals("")){
					if(csfbFunctionTest == null){
						csfbFunctionTest = 0;
					}
					if(!eceptionCellLogPojo.getCsfbSuccess().equals("0")){
						csfbFunctionTest = 1;
					}
				}
				if(eceptionCellLogPojo.getCsfbRequesttime() != null && !eceptionCellLogPojo.getCsfbRequesttime().equals("")){
					if(csfbConnect == null || Float.valueOf(eceptionCellLogPojo.getCsfbRequesttime()) < Float.valueOf(csfbConnect)){
						csfbConnect = (Float.valueOf(eceptionCellLogPojo.getCsfbRequesttime())) + "";
					}
				}
			}
		}
		
		//???????????????????????????????????????????????????
		for(int i=0;i<localCellidList.size();i++){
			if(p4p.getLocalCellId() == localCellidList.get(i)){
				if(i == 0){
					oop.setLocalCellIdTag(0);
					if(ecpRaodian != null && ecpRaodian.size() > 0){
						boolean station2Change=false;
						boolean station3Change=false;
						for (EceptionCellLogPojo eceptionCellLogPojo : ecpRaodian) {
							if(eceptionCellLogPojo.getToStation2Change() != null && !eceptionCellLogPojo.getToStation2Change().equals("")&&
									eceptionCellLogPojo.getToStation3Change() != null && !eceptionCellLogPojo.getToStation3Change().equals("")){
								if(changeTest == null){
									changeTest = 3;
								}
								if(!eceptionCellLogPojo.getToStation2Change().equals("0")){
									station2Change = true;
								}
								if(!eceptionCellLogPojo.getToStation3Change().equals("0")){
									station3Change = true;
								}
							}
						}
						//??????????????????????????? 1???"????????????"; 2???"??????2????????????";  3???"??????3????????????";  4???"????????????";
						if(changeTest!=null){
							if(station2Change == true && station3Change == true){
								changeTest = 1;
							}else if(station2Change != true && station3Change == true){
								changeTest = 2;
							}else if(station2Change == true && station3Change != true){
								changeTest = 3;
							}else{
								changeTest = 4;
							}
						}
					}
				}else if(i == 1){
					oop.setLocalCellIdTag(1);
					if(ecpRaodian != null && ecpRaodian.size() > 0){
						boolean station1Change=false;
						boolean station3Change=false;
						for (EceptionCellLogPojo eceptionCellLogPojo : ecpRaodian) {
							if(eceptionCellLogPojo.getToStation1Change() != null && !eceptionCellLogPojo.getToStation1Change().equals("")&&
									eceptionCellLogPojo.getToStation3Change() != null && !eceptionCellLogPojo.getToStation3Change().equals("")){
								if(changeTest == null){
									changeTest = 3;
								}
								if(!eceptionCellLogPojo.getToStation1Change().equals("0")){
									station1Change = true;
								}
								if(!eceptionCellLogPojo.getToStation3Change().equals("0")){
									station3Change = true;
								}
							}
						}
						//??????????????????????????? 1???"????????????"; 2???"??????1????????????";  3???"??????3????????????";  4???"????????????";
						if(changeTest!=null){
							if(station1Change == true && station3Change == true){
								changeTest = 1;
							}else if(station1Change != true && station3Change == true){
								changeTest = 2;
							}else if(station1Change == true && station3Change != true){
								changeTest = 3;
							}else{
								changeTest = 4;
							}
						}
					}
				}else{
					oop.setLocalCellIdTag(2);
					if(ecpRaodian != null && ecpRaodian.size() > 0){
						boolean station1Change=false;
						boolean station2Change=false;
						for (EceptionCellLogPojo eceptionCellLogPojo : ecpRaodian) {
							if(eceptionCellLogPojo.getToStation1Change() != null && !eceptionCellLogPojo.getToStation1Change().equals("")&&
									eceptionCellLogPojo.getToStation2Change() != null && !eceptionCellLogPojo.getToStation2Change().equals("")){
								if(changeTest == null){
									changeTest = 3;
								}
								if(!eceptionCellLogPojo.getToStation1Change().equals("0")){
									station1Change = true;
								}
								if(!eceptionCellLogPojo.getToStation2Change().equals("0")){
									station2Change = true;
								}
							}
						}
						//??????????????????????????? 1???"????????????"; 2???"??????1????????????";  3???"??????2????????????";  4???"????????????";
						if(changeTest!=null){
							if(station1Change == true && station2Change == true){
								changeTest = 1;
							}else if(station1Change != true && station2Change == true){
								changeTest = 2;
							}else if(station1Change == true && station2Change != true){
								changeTest = 3;
							}else{
								changeTest = 4;
							}
						}
					}
				
				}
			}
		}
		
		oop.setRsrp(lteRsrp);
		oop.setSinr(lteSinr);
		oop.setTcpDownload(tcpDownload);
		oop.setTcpUpload(tcpUpload);
		oop.setRrcSetup(rrcSetup);
		oop.setErabSetup(erabSetup);
		oop.setAccess(LteAccessTest);
		oop.setPing(pingTimeDelay);
		oop.setVolte(volteTest);
		oop.setCsfbFunction(csfbFunctionTest);
		oop.setCsfbConnect(csfbConnect);
		oop.setChangeTest(changeTest);
		oop.setMaxDownload(maxDownload);
		oop.setMaxUpload(maxUpload);
		//??????5???????????????????????????????????????????????????null
		oop.setVolte(volteTest);
		oop.setDialingVolteNetQuality(dialingVolteNetTest);
		oop.setCalledVolteNetQuality(calledVolteNetTest);
		oop.setDialingVolteVideoQuality(dialingVolteVideoTest);
		oop.setCalledVolteVideoQuality(calledVolteVideoTest); 
		
		//??????4G??????????????????	?????????????????????
		String noPassTestEvent = null;
		
		Menu menu = (Menu)terminalMenuService.get2(p4p.getRegion());
		StationParamPojo spp = menu.getStationParamPojo();
		if (spp != null && spp.getId() != null) {
			Float upgradeVeryGood3D4G = spp.getUpgradeVeryGood3D4G();
			Float uploadVeryGood3D4G = spp.getUploadVeryGood3D4G();
			Float ping32DelayTime3D = spp.getPing32DelayTime3D();
			if(cityTemplate.equals("????????????3d??????")){
				if (tcpDownload == null || tcpUpload == null || Float.valueOf(tcpDownload) < Float.valueOf(uploadVeryGood3D4G)
						 || Float.valueOf(tcpUpload) < Float.valueOf(upgradeVeryGood3D4G)) {
					noPassTestEvent = "FTP???????????????";
				} else if (rrcSetup == null || rrcSetup == 0 
							|| erabSetup == null || erabSetup == 0
							|| LteAccessTest == null || LteAccessTest == 0
							|| changeTest == null || changeTest!=1) {
					noPassTestEvent = "???????????????????????????";
				} else if (pingTimeDelay == null
						|| Float.valueOf(pingTimeDelay) > Float.valueOf(ping32DelayTime3D)) {
					noPassTestEvent = "ping???????????????";
				} else if (volteTest == null || volteTest == 0) {
					noPassTestEvent = "volte???????????????";
				} else {
					noPassTestEvent = "????????????";
				}
			} else if (cityTemplate.equals("????????????3d??????")) { // ????????????
				if (tcpDownload == null || tcpUpload == null || Float.valueOf(tcpDownload) < Float.valueOf(uploadVeryGood3D4G)
						 || Float.valueOf(tcpUpload) < Float.valueOf(upgradeVeryGood3D4G)) {
					noPassTestEvent = "FTP???????????????";
				} else if (changeTest == null || changeTest!=1) {
					noPassTestEvent = "???????????????????????????";
				} else if (dialingVolteNetTest == null || dialingVolteNetTest < 1) {
					noPassTestEvent = "volte???????????????";
				}  else {
					noPassTestEvent = "????????????";
				}
			}
			
		} else {
			errorMsg = errorMsg+" ????????????????????????????????????3d?????????????????????????????????!";
		}
		p4p.setNoPassTestEvent(noPassTestEvent);
		
		oop.setPlan4GParam(p4p);
		p4p.setOppositeOpen3dPerformanceReceivePojo(oop);
		
		if(p4p.getOppositeOpen3dPerformanceReceivePojo()!=null){
			oppositeOpen3dReportCreateService.updatePerformanceReceivePojo(oop);
		}else{
			oppositeOpen3dReportCreateService.savePerformanceReceivePojo(oop);
		}
				
	}

	/**
	 * ???????????????????????????
	 * @author maxuancheng
	 * date:2020???3???16??? ??????2:53:10
	 * @param siteNames
	 * @param p4pList
	 */
	private void createWirelessCellReport(Long newDate,HashSet<String> siteNames, Plan4GParam p4p, String cityTemplate) {
		//?????????:????????????
		OppositeOpen3dWirelessPojo oow = new OppositeOpen3dWirelessPojo();;
		
		//??????cellName??????????????????????????????
		List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellLog(p4p.getCellName(),logNameList,null,null);
		//???????????????????????????????????????????????????????????????????????????????????????????????????
		Long time = null;
		List<EceptionCellLogPojo> eceptionCellLogPojoList = new ArrayList<EceptionCellLogPojo>();
		for (EceptionCellLogPojo ecp : cellLogList) {
			if(ecp.getNrTestdate() != null 
					&& time == null 
					&& ecp.getNrTestevent() != null
					&& !"??????".equals(ecp.getNrTestevent())){
				time = Long.valueOf(ecp.getNrTestdate());
				eceptionCellLogPojoList.add(ecp);
			}else if(ecp.getNrTestdate() != null 
					&& ecp.getNrTestevent() != null 
					&& time != null 
					&& Math.abs(newDate - time) > Math.abs(newDate - Long.valueOf(ecp.getNrTestdate()))
					&& !"??????".equals(ecp.getNrTestevent())){
				time = Long.valueOf(ecp.getNrTestdate());
				eceptionCellLogPojoList.clear();
				eceptionCellLogPojoList.add(ecp);
			}else if(ecp.getNrTestdate() != null 
					&& time != null 
					&& time.equals(Long.valueOf(ecp.getNrTestdate()))
					&& ecp.getNrTestevent() != null 
					&& !"??????".equals(ecp.getNrTestevent())){
				eceptionCellLogPojoList.add(ecp);
			}
		}
		
		//??????1-30
		oow.setCellName1(p4p.getCellName());
		oow.setPci1(p4p.getPci());
		oow.setFrequencyDl1(p4p.getFrequencyDl());
		oow.setEarfcn1(p4p.getEarfcn());
		oow.setBroadBand1(p4p.getBroadBand());
		oow.setSubFrameConfig1(p4p.getSubFrameConfig());
		oow.setSpecialSubFrameConfig1(p4p.getSpecialSubFrameConfig());
		oow.setRs_epre1(p4p.getRs_epre());
		oow.setP_a1(p4p.getP_a());
		oow.setP_b1(p4p.getP_b());
		oow.setCellId1(p4p.getCellId());
		
		oow.setSiteName(p4p.getSiteName());
		//??????31-60 PCI ?????? ?????? ?????? ??????????????? ?????????????????? RS EPRE??? p-a p-b CELLID
		oow.setTac(p4p.getTac());
		//64:???10???????????????1?????????65536
		if(oow.getCellId1() != null){
			oow.setEnbId(Math.round((oow.getCellId1() / 256)));
		}
		oow.setPdcchSymbol(p4p.getPdcchSymbol());
		oow.setRootSeq(p4p.getRootSeq());
		
		Map<String, Integer> tacMap = new HashMap<String, Integer>();
		Map<String, Integer> pciMap = new HashMap<String, Integer>();
		Map<String, Integer> freMap = new HashMap<String, Integer>();
		Map<String, Integer> earMap = new HashMap<String, Integer>();
		Map<String, Integer> broMap = new HashMap<String, Integer>();
		Map<String, Integer> subMap = new HashMap<String, Integer>();
		Map<String, Integer> ssubMap = new HashMap<String, Integer>();
		Map<String, Integer> rsMap = new HashMap<String, Integer>();
		Map<String, Integer> paMap = new HashMap<String, Integer>();
		Map<String, Integer> pbMap = new HashMap<String, Integer>();
		Map<String, Integer> cellMap = new HashMap<String, Integer>();
		Map<String, Integer> pdcchSymbolMap = new HashMap<String, Integer>();
		Map<String, Integer> rootSeqMap = new HashMap<String, Integer>();
		
		oow.setTacReality(null);
		oow.setPciReality1(null);
		oow.setFrequencyDlReality1(null);
		oow.setEarfcnReality1(null);
		oow.setBroadBandReality1(null);
		oow.setSubFrameConfigReality1(null);
		oow.setSpecialSubFrameConfigReality1(null);
		oow.setRs_epreReality1(null);
		oow.setP_aReality1(null);
		oow.setP_bReality1(null);
		oow.setCellIdReality1(null);
		oow.setEnbIdReality(null);
		oow.setPdcchSymbolReality1(null);
		oow.setRootSeqReality1(null);
		for (EceptionCellLogPojo ecp : eceptionCellLogPojoList) {
			//64:?????????????????????????????????????????????????????????LTE TAC??????????????????????????????????????????????????????63?????????????????????????????????63????????????????????????????????????????????????????????????0???????????????
			if(oow.getTacReality() == null && oow.getTac() != null
					&& ecp.getLteTac() != null
					&& oow.getTac() == Integer.valueOf(ecp.getLteTac())){
				oow.setTacReality(Integer.valueOf(ecp.getLteTac()));							
			}else if(ecp.getLteTac() != null && !ecp.getLteTac().equals("0")){
				if(tacMap.get(ecp.getLteTac()) == null){
					tacMap.put(ecp.getLteTac(), 1);
				}else{
					tacMap.put(ecp.getLteTac(), tacMap.get(ecp.getLteTac()) + 1);
				}
			}
			
			//31:???LTE PCI1?????????LTE PCI2?????????LTE PCI3??????????????????????????????????????????????????????1?????????????????????????????????1????????????????????????????????????????????????????????????0???????????????
			if(oow.getPciReality1() == null){
				if(ecp.getLtePci1() != null && ecp.getLtePci1().equals(oow.getPci1().toString())){
					oow.setPciReality1(Long.valueOf(ecp.getLtePci1()));
				}else if(ecp.getLtePci2() != null && ecp.getLtePci2().equals(oow.getPci1().toString())){
					oow.setPciReality1(Long.valueOf(ecp.getLtePci2()));
				}else if(ecp.getLtePci3() != null && ecp.getLtePci3().equals(oow.getPci1().toString())){
					oow.setPciReality1(Long.valueOf(ecp.getLtePci3()));
				}else{
					if(ecp.getLtePci1() != null && !ecp.getLtePci1().equals("0")){
						if(pciMap.get(ecp.getLtePci1()) == null){
							pciMap.put(ecp.getLtePci1(), 1);
						}else{
							pciMap.put(ecp.getLtePci1(), pciMap.get(ecp.getLtePci1()) + 1);
						}
					}
					if(ecp.getLtePci2() != null && !ecp.getLtePci2().equals("0")){
						if(pciMap.get(ecp.getLtePci2()) == null){
							pciMap.put(ecp.getLtePci2(), 1);
						}else{
							pciMap.put(ecp.getLtePci2(), pciMap.get(ecp.getLtePci2()) + 1);
						}
					}
					if(ecp.getLtePci3() != null && !ecp.getLtePci3().equals("0")){
						if(pciMap.get(ecp.getLtePci3()) == null){
							pciMap.put(ecp.getLtePci3(), 1);
						}else{
							pciMap.put(ecp.getLtePci3(), pciMap.get(ecp.getLtePci3()) + 1);
						}
					}
				}
			}
			
			//32:???LTE???????????????????????????????????????????????????????????????2?????????????????????????????????2????????????????????????????????????????????????????????????0???????????????
			if(oow.getFrequencyDlReality1() == null 
					&& ecp.getLteFrequery() != null 
					&& ecp.getLteFrequery().equals(oow.getFrequencyDl1())){
				oow.setFrequencyDl1(ecp.getLteFrequery());
			}else if(ecp.getLteFrequery() != null && !ecp.getLteFrequery().equals("0")){
				if(freMap.get(ecp.getLteFrequery()) == null){
					freMap.put(ecp.getLteFrequery(), 1);
				}else{
					freMap.put(ecp.getLteFrequery(), freMap.get(ecp.getLteFrequery()) + 1);
				}
			}
			//33:???LTE ????????????????????????????????????????????????????????????3?????????????????????????????????3????????????????????????????????????????????????????????????0???????????????
			if(oow.getEarfcnReality1() == null
					&& ecp.getLtePoint() != null
					&& ecp.getLtePoint().equals(oow.getEarfcn1())){
				oow.setEarfcnReality1(ecp.getLtePoint());
			}else if(ecp.getLtePoint() != null  && !ecp.getLtePoint().equals("0")){
				if(earMap.get(ecp.getLtePoint()) == null){
					earMap.put(ecp.getLtePoint(), 1);
				}else{
					earMap.put(ecp.getLtePoint(), earMap.get(ecp.getLtePoint()) + 1);
				}
			}
			//34:???LTE??????????????????????????????????????????????????????????????????4?????????????????????????????????4????????????????????????????????????????????????????????????0???????????????
			if(oow.getBroadBandReality1() == null
					&& ecp.getLteBandwidth() != null
					&& Float.valueOf(ecp.getLteBandwidth()).equals(oow.getBroadBand1())){
				oow.setBroadBandReality1(Float.valueOf(ecp.getLteBandwidth()));
			}else if(ecp.getLteBandwidth() != null && !ecp.getLteBandwidth().equals("0")){
				if(broMap.get(ecp.getLteBandwidth()) == null){
					broMap.put(ecp.getLteBandwidth(), 1);
				}else{
					broMap.put(ecp.getLteBandwidth(), broMap.get(ecp.getLteBandwidth()) + 1);
				}
			}
			//35:???LTE?????????????????????????????????????????????????????????????????????5?????????????????????????????????5????????????????????????????????????????????????????????????0???????????????
			if(oow.getSubFrameConfigReality1() == null
					&& ecp.getLteSpratio() != null
					&& ecp.getLteSpratio().equals(oow.getSubFrameConfig1())){
				oow.setSubFrameConfigReality1(ecp.getLteSpratio());
			}else if(ecp.getLteSpratio() != null && !ecp.getLteSpratio().equals("0")){
				if(subMap.get(ecp.getLteSpratio()) == null){
					subMap.put(ecp.getLteSpratio(), 1);
				}else{									
					subMap.put(ecp.getLteSpratio(), subMap.get(ecp.getLteSpratio()) + 1);
				}
			}
			//36:???LTE????????????????????????????????????????????????????????????????????????6?????????????????????????????????6????????????????????????????????????????????????????????????0???????????????
			if(oow.getSpecialSubFrameConfigReality1() == null
					&& ecp.getLteSspratio() != null
					&& ecp.getLteSspratio().equals(oow.getSpecialSubFrameConfig1())){
				oow.setSpecialSubFrameConfigReality1(ecp.getLteSspratio());
			}else if(ecp.getLteSspratio() != null && !ecp.getLteSspratio().equals("0") ){
				if(ssubMap.get(ecp.getLteSspratio()) == null){
					ssubMap.put(ecp.getLteSspratio(), 1);									
				}else{
					ssubMap.put(ecp.getLteSspratio(), ssubMap.get(ecp.getLteSspratio()) + 1);									
				}
			}
			//37:???RS-ERPE??????????????????????????????????????????????????????7?????????????????????????????????7????????????????????????????????????????????????????????????0???????????????
			if(oow.getRs_epreReality1() == null
					&& ecp.getRsErpe() != null
					&& ecp.getRsErpe().equals(oow.getRs_epre1())){
				oow.setRs_epreReality1(ecp.getRsErpe());
			}else if(ecp.getRsErpe() != null && !ecp.getRsErpe().equals("0")){
				if(rsMap.get(ecp.getRsErpe()) == null){
					rsMap.put(ecp.getRsErpe(), 1);
				}else{
					rsMap.put(ecp.getRsErpe(), rsMap.get(ecp.getRsErpe()) + 1);									
				}
			}
			//38:???p-a?????????????????????????????????????????????????????????8?????????????????????????????????8????????????????????????????????????????????????????????????0???????????????
			if(oow.getP_aReality1() == null
					&& ecp.getpA() != null
					&& ecp.getpA().equals(oow.getP_a1())){
				oow.setP_aReality1(ecp.getpA());
			}else if(ecp.getpA() != null && !ecp.getpA().equals("0")){
				if(paMap.get(ecp.getpA()) == null){
					paMap.put(ecp.getpA(), 1);
				}else{
					paMap.put(ecp.getpA(), paMap.get(ecp.getpA()) + 1);									
				}
			}
			//39:???p-b??????????????????????????????????????????????????????9?????????????????????????????????9????????????????????????????????????????????????????????????0???????????????
			if(oow.getP_bReality1() == null
					&& ecp.getpB() != null
					&& ecp.getpB().equals(oow.getP_b1())){
				oow.setP_aReality1(ecp.getpB());
			}else if(ecp.getpB() != null && !ecp.getpB().equals("0")){
				if(pbMap.get(ecp.getpB()) == null){
					pbMap.put(ecp.getpB(), 1);
				}else{
					pbMap.put(ecp.getpB(), pbMap.get(ecp.getpB()) + 1);									
				}
			}
			//40:???LTE CELLID1?????????LTE CELLID2?????????LTE CELLID3??????????????????????????????????????????????????????10?????????????????????????????????10????????????????????????????????????????????????????????????0???????????????
			if(oow.getCellIdReality1() == null){
				if(ecp.getLteCellid1() != null && ecp.getLteCellid1().equals(oow.getCellId1())){
					oow.setCellIdReality1(Long.valueOf(ecp.getLteCellid1()));
				}else if(ecp.getLteCellid2() != null && ecp.getLteCellid2().equals(oow.getCellId1())){
					oow.setCellIdReality1(Long.valueOf(ecp.getLteCellid2()));
				}else if(ecp.getLteCellid3() != null && ecp.getLteCellid3().equals(oow.getCellId1())){
					oow.setCellIdReality1(Long.valueOf(ecp.getLteCellid3()));
				}else{
					if(ecp.getLteCellid1() != null && !ecp.getLteCellid1().equals("0")){
						if(cellMap.get(ecp.getLteCellid1()) == null){
							cellMap.put(ecp.getLteCellid1(), 1);										
						}else{
							cellMap.put(ecp.getLteCellid1(), cellMap.get(ecp.getLteCellid1()) + 1);																				
						}
					}
					if(ecp.getLteCellid2() != null && !ecp.getLteCellid2().equals("0")){
						if(cellMap.get(ecp.getLteCellid2()) == null){
							cellMap.put(ecp.getLteCellid2(), 1);										
						}else{
							cellMap.put(ecp.getLteCellid2(), cellMap.get(ecp.getLteCellid2()) + 1);																				
						}
					}
					if(ecp.getLteCellid3() != null && !ecp.getLteCellid3().equals("0")){
						if(cellMap.get(ecp.getLteCellid3()) == null){
							cellMap.put(ecp.getLteCellid3(), 1);										
						}else{
							cellMap.put(ecp.getLteCellid3(), cellMap.get(ecp.getLteCellid3()) + 1);																				
						}
					}
				}
			}
			
			//102:???LocalCellId??????1???2???3??? ???pdcch?????????????????????????????????????????????????????????????????????99?????????????????????????????????99????????????????????????????????????????????????????????????0???????????????
			if(oow.getPdcchSymbolReality1() == null
					&& ecp.getPdcch() != null
					&& ecp.getPdcch().equals(oow.getPdcchSymbol())){
				oow.setPdcchSymbolReality1(ecp.getPdcch());
			}else if(ecp.getPdcch() != null && !ecp.getPdcch().equals("0")){
				if(pdcchSymbolMap.get(ecp.getPdcch()) == null){
					pdcchSymbolMap.put(ecp.getPdcch(), 1);
				}else{
					pdcchSymbolMap.put(ecp.getPdcch(), pdcchSymbolMap.get(ecp.getPdcch()) + 1);									
				}
			}
			
			//111:???LocalCellId??????1???2???3??? ???????????????????????????????????????????????????????????????????????????108?????????????????????????????????108????????????????????????????????????????????????????????????0???????????????
			if(oow.getRootSeqReality1() == null
					&& ecp.getNrPreambleFormat() != null
					&& ecp.getNrPreambleFormat().equals(oow.getRootSeq())){
				oow.setRootSeqReality1(ecp.getNrPreambleFormat());
			}else if(ecp.getNrPreambleFormat() != null && !ecp.getNrPreambleFormat().equals("0")){
				if(rootSeqMap.get(ecp.getNrPreambleFormat()) == null){
					rootSeqMap.put(ecp.getNrPreambleFormat(), 1);
				}else{
					rootSeqMap.put(ecp.getNrPreambleFormat(), rootSeqMap.get(ecp.getNrPreambleFormat()) + 1);									
				}
			}
		}
		
		//?????????????????????    PCI ?????? ?????? ?????? ??????????????? ?????????????????? RS EPRE??? p-a p-b CELLID pdcch?????????  ????????????
		//??????????????????XX?????????????????????????????????XX???????????????????????????????????????????????????
		if(oow.getTacReality() == null){
			int val = 0;
			for (Entry<String, Integer> entry : tacMap.entrySet()) {
				if(oow.getTacReality() == null && entry.getKey() != null && Integer.valueOf(entry.getKey()) != 0){
					oow.setTacReality(Integer.valueOf(entry.getKey()));
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null && Integer.valueOf(entry.getKey()) != 0){
					oow.setTacReality(Integer.valueOf(entry.getKey()));
					val = entry.getValue();									
				}
			}
		}
		if(oow.getPciReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : pciMap.entrySet()) {
				if(oow.getPciReality1() == null && entry.getKey() != null && Long.valueOf(entry.getKey()) != 0){
					oow.setPciReality1(Long.valueOf(entry.getKey()));
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null && Long.valueOf(entry.getKey()) != 0){
					oow.setPciReality1(Long.valueOf(entry.getKey()));
					val = entry.getValue();									
				}
			}
		}
		if(oow.getFrequencyDlReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : freMap.entrySet()) {
				if(oow.getFrequencyDlReality1() == null && entry.getKey() != null){
					oow.setFrequencyDlReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setFrequencyDlReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getEarfcnReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : earMap.entrySet()) {
				if(oow.getEarfcnReality1() == null && entry.getKey() != null){
					oow.setEarfcnReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setEarfcnReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getBroadBandReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : broMap.entrySet()) {
				if(oow.getBroadBandReality1() == null && entry.getKey() != null && Float.valueOf(entry.getKey()) != 0){
					oow.setBroadBandReality1(Float.valueOf(entry.getKey()));
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null && Float.valueOf(entry.getKey()) != 0){
					oow.setBroadBandReality1(Float.valueOf(entry.getKey()));
					val = entry.getValue();
				}
			}
		}
		if(oow.getSubFrameConfigReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : subMap.entrySet()) {
				if(oow.getSubFrameConfigReality1() == null && entry.getKey() != null){
					oow.setSubFrameConfigReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setSubFrameConfigReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getSpecialSubFrameConfigReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : ssubMap.entrySet()) {
				if(oow.getSpecialSubFrameConfigReality1() == null && entry.getKey() != null){
					oow.setSpecialSubFrameConfigReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setSpecialSubFrameConfigReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getRs_epreReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : rsMap.entrySet()) {
				if(oow.getRs_epreReality1() == null && entry.getKey() != null){
					oow.setRs_epreReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setRs_epreReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getP_aReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : paMap.entrySet()) {
				if(oow.getP_aReality1() == null && entry.getKey() != null){
					oow.setP_aReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setP_aReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getP_bReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : pbMap.entrySet()) {
				if(oow.getP_bReality1() == null && entry.getKey() != null){
					oow.setP_bReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setP_bReality1(entry.getKey());
					val = entry.getValue();
				}
			}
		}
		if(oow.getCellIdReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : cellMap.entrySet()) {
				if(oow.getCellIdReality1() == null && entry.getKey() != null && Long.valueOf(entry.getKey()) != 0){
					oow.setCellIdReality1(Long.valueOf(entry.getKey()));
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null && Long.valueOf(entry.getKey()) != 0){
					oow.setCellIdReality1(Long.valueOf(entry.getKey()));
					val = entry.getValue();								
				}
			}
		}
		if(oow.getPdcchSymbolReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : pdcchSymbolMap.entrySet()) {
				if(oow.getPdcchSymbolReality1() == null && entry.getKey() != null){
					oow.setPdcchSymbolReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setPdcchSymbolReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		if(oow.getRootSeqReality1() == null){
			int val = 0;
			for (Entry<String, Integer> entry : rootSeqMap.entrySet()) {
				if(oow.getRootSeqReality1() == null && entry.getKey() != null){
					oow.setRootSeqReality1(entry.getKey());
					val = entry.getValue();
				}else if(entry.getValue() > val && entry.getKey() != null){
					oow.setRootSeqReality1(entry.getKey());
					val = entry.getValue();								
				}
			}
		}
		
		//enbid
		if(oow.getCellId1() != null){
			if(oow.getCellId1() != null){
				oow.setEnbId(Math.round((oow.getCellId1() / 256)));					
			}
			if(oow.getCellIdContrast() != null){
				oow.setEnbIdReality(Math.round((oow.getCellIdContrast() / 256)));
			}
		}
		
		//??????  PCI ?????? ?????? ?????? ??????????????? ?????????????????? RS EPRE??? p-a p-b CELLID  pdcch?????????  ????????????
		if(oow.getPci1() != null && oow.getPciReality1() != null && oow.getPci1().equals(oow.getPciReality1())){
			oow.setPciContrast(1);
		}else if(oow.getPci1() == null && oow.getPciReality1() == null){
			oow.setPciContrast(1);
		}else{
			oow.setPciContrast(0);
		}
		
		if(oow.getFrequencyDl1() != null && oow.getFrequencyDlReality1() != null && oow.getFrequencyDl1().equals(oow.getFrequencyDlReality1())){
			oow.setFrequencyDlContrast(1);
		}else if(oow.getFrequencyDl1() == null && oow.getFrequencyDlReality1() == null){
			oow.setFrequencyDlContrast(1);
		}else{
			oow.setFrequencyDlContrast(0);
		}
		
		if(oow.getEarfcn1() != null && oow.getEarfcnReality1() != null && oow.getEarfcn1().equals(oow.getEarfcnReality1())){
			oow.setEarfcnContrast(1);
		}else if(oow.getEarfcn1() == null && oow.getEarfcnReality1() == null){
			oow.setEarfcnContrast(1);
		}else{
			oow.setEarfcnContrast(0);
		}
		
		if(oow.getBroadBand1() != null && oow.getBroadBandReality1() != null && oow.getBroadBand1().equals(oow.getBroadBandReality1())){
			oow.setBroadBandContrast(1);
		}else if(oow.getBroadBandReality1() == null && oow.getBroadBand1() == null ){
			oow.setBroadBandContrast(1);
		}else{
			oow.setBroadBandContrast(0);
		}
		
		if(oow.getSubFrameConfig1() != null && oow.getSubFrameConfigReality1() != null 
				&& oow.getSubFrameConfig1().equals(oow.getSubFrameConfigReality1())){
			oow.setSubFrameConfigContrast(1);
		}else if(oow.getSubFrameConfig1() == null && oow.getSubFrameConfigReality1() == null){
			oow.setSubFrameConfigContrast(1);
		}else{
			oow.setSubFrameConfigContrast(0);				
		}
		
		if(oow.getSpecialSubFrameConfig1() != null && oow.getSpecialSubFrameConfigReality1() != null
				&& oow.getSpecialSubFrameConfig1().equals(oow.getSpecialSubFrameConfigReality1())){
			oow.setSpecialSubFrameConfigContrast(1);
		}else if(oow.getSpecialSubFrameConfig1() == null && oow.getSpecialSubFrameConfigReality1() == null){
			oow.setSpecialSubFrameConfigContrast(1);
		}else{
			oow.setSpecialSubFrameConfigContrast(0);				
		}
		
		if(oow.getRs_epre1() != null && oow.getRs_epreReality1() != null && oow.getRs_epre1().equals(oow.getRs_epreReality1())){
			oow.setRs_epreContrast(1);
		}else if(oow.getRs_epre1() == null && oow.getRs_epreReality1() == null){
			oow.setRs_epreContrast(1);
		}else{
			oow.setRs_epreContrast(0);
		}
		
		if(oow.getP_a1() != null && oow.getP_aReality1() != null && oow.getP_aReality1().equals(oow.getP_a1())){
			oow.setP_aContrast(1);
		}else if(oow.getP_a1() == null && oow.getP_aReality1() == null){
			oow.setP_aContrast(1);
		}else{
			oow.setP_aContrast(0);
		}
		
		if(oow.getP_b1() != null && oow.getP_bReality1() != null && oow.getP_bReality1().equals(oow.getP_b1())){
			oow.setP_bContrast(1);
		}else if(oow.getP_b1() == null && oow.getP_bReality1() == null){
			oow.setP_bContrast(1);
		}else{
			oow.setP_bContrast(0);
		}
		
		if(oow.getCellId1() != null && oow.getCellIdReality1() != null && oow.getCellId1().equals(oow.getCellIdReality1())){
			oow.setCellIdContrast(1);
		}else if(oow.getCellId1() == null && oow.getCellIdReality1() == null){
			oow.setCellIdContrast(1);
		}else{
			oow.setCellIdContrast(0);
		}
		
		if(oow.getTac() != null && oow.getTacReality() != null && oow.getTac().equals(oow.getTacReality())){
			oow.setTacContrast(1);
		}else if(oow.getTac() == null && oow.getTacReality() == null ){
			oow.setTacContrast(1);
		}else{
			oow.setTacContrast(0);
		}
		
		if(oow.getEnbId() != null && oow.getEnbIdReality() != null && oow.getEnbIdReality().equals(oow.getEnbId())){
			oow.setEnbIdContrast(1);
		}else if(oow.getEnbId() == null && oow.getEnbIdReality() == null){
			oow.setEnbIdContrast(1);
		}else{
			oow.setEnbIdContrast(0);
		}
		
		if(oow.getPdcchSymbol() != null && oow.getPdcchSymbolReality1() != null && oow.getPdcchSymbol().equals(oow.getPdcchSymbolReality1())){
			oow.setPdcchSymContrast(1);
		}else if(oow.getPdcchSymbol() == null && oow.getPdcchSymbolReality1() == null ){
			oow.setPdcchSymContrast(1);
		}else{
			oow.setPdcchSymContrast(0);
		}
		
		if(oow.getRootSeq() != null && oow.getRootSeqReality1() != null && oow.getRootSeq().equals(oow.getRootSeqReality1())){
			oow.setRootSeqContrast(1);
		}else if(oow.getRootSeq() == null && oow.getRootSeqReality1() == null ){
			oow.setRootSeqContrast(1);
		}else{
			oow.setRootSeqContrast(0);
		}
		
		// ???????????????????????? ?????????????????????
		if(cityTemplate.equals("????????????3d??????")){
			if (oow.getPciContrast()==0) {
				p4p.setWirelessParamStatus("PCI?????????");
			} else if (oow.getFrequencyDlContrast()==0 || oow.getEarfcnContrast()==0 || oow.getBroadBandContrast()==0) {
				p4p.setWirelessParamStatus("?????????????????????");
			} else if (oow.getTacContrast()==0) {
				p4p.setWirelessParamStatus("TAC?????????");
			} else if (oow.getP_aContrast()==0 || oow.getP_bContrast()==0) {
				p4p.setWirelessParamStatus("?????????????????????");
			} else {
				p4p.setWirelessParamStatus("????????????");
			}
		} else if (cityTemplate.equals("????????????3d??????")) { // ????????????
			if (oow.getPciContrast()==0) {
				p4p.setWirelessParamStatus("PCI?????????");
			} else if (oow.getFrequencyDlContrast()==0 || oow.getEarfcnContrast()==0 || oow.getBroadBandContrast()==0) {
				p4p.setWirelessParamStatus("?????????????????????");
			} else if (oow.getTacContrast()==0) {
				p4p.setWirelessParamStatus("TAC?????????");
			} else if (oow.getP_aContrast()==0 || oow.getP_bContrast()==0 || oow.getRs_epreContrast()==0) {
				p4p.setWirelessParamStatus("?????????????????????");
			} else {
				p4p.setWirelessParamStatus("????????????");
			}
		}
		
		oow.setPlan4GParam(p4p);
		p4p.setOppositeOpen3dWirelessPojo(oow);
		if(oow.getId() == null){
			oppositeOpen3dReportCreateService.creteOOW(oow);				
		}else{
			oppositeOpen3dReportCreateService.updateOOW(oow);
		}
	}
	
	
	/**
	 * ??????5g??????????????????
	 * 
	 * @return
	 */
	public String getDownloadNrTestLog(String idsStr) {
		String[] ids = idsStr.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (String id : ids) {
			idList.add(Long.valueOf(id));
		}
		
		List<PlanParamPojo> pppList = stationReportCreatService.findByIds2(idList);
		Map<String, Map<String, Object>> pppMap = new HashMap<String, Map<String,Object>>();
		try {
			for (PlanParamPojo ppp : pppList) {
				if(ppp.getReportCreateDate()!=null && ppp.getStationTemplateSelect()!=null){
					// ????????????????????????localcellid???????????????????????????
					List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(ppp.getSiteName());
					Integer localCellId = null;
					for(int i=0; i<planParamPojoAscList.size();i++){
						if(planParamPojoAscList.get(i).getLocalCellID() == ppp.getLocalCellID()){
							localCellId = i+1;
							break;
						}
					}
					
					Map<String, Object> map = pppMap.get(ppp.getSiteName());
					String recentDate = String.valueOf(ppp.getReportCreateDate()).substring(0, 4)
											+"/"+String.valueOf(ppp.getReportCreateDate()).substring(4, 6)
											+"/"+String.valueOf(ppp.getReportCreateDate()).substring(6, 8);
					if(map == null){
						map = new HashMap<String, Object>();
						map.put("reportCreateDate", recentDate);
					}else if(map.get("reportCreateDate") == null){
						map.put("reportCreateDate", recentDate);
					}else {
						String oldDate =  String.valueOf(map.get("reportCreateDate")).replace("/", "");
						if(ppp.getReportCreateDate() > Long.valueOf(oldDate)){
							map.put("reportCreateDate", recentDate);
						}
					}
					
					if(ppp.getStationCellParamCensusList() == null || ppp.getStationCellParamCensusList().size() < 1){
						map.put("cellId" + localCellId + "cell", ppp.getStationCellParamCensusList());
						map.put("tac",null);
						map.put("tacStatus",null);
					}else{
						map.put("cellId" + localCellId + "cell", ppp.getStationCellParamCensusList().get(0));
						map.put("tac",ppp.getStationCellParamCensusList().get(0).getTac19());
						map.put("tacStatus",ppp.getStationCellParamCensusList().get(0).getTacStatus29());
					}
					
					if(ppp.getStationNetoptReceiveTestList() == null || ppp.getStationNetoptReceiveTestList().size() < 1){
						map.put("cellId" + localCellId + "net", ppp.getStationNetoptReceiveTestList());
						map.put("dtWireCoverRate",null);
						map.put("nrSwitchSuccessAvg",null);
					}else{
						map.put("cellId" + localCellId + "net", ppp.getStationNetoptReceiveTestList().get(0));
						map.put("dtWireCoverRate",ppp.getStationNetoptReceiveTestList().get(0).getDtWireCoverRate());
						map.put("nrSwitchSuccessAvg",ppp.getStationNetoptReceiveTestList().get(0).getNrSwitchSuccessAvg());
					}
					
					if(ppp.getStationPerformanceReceiveList() == null || ppp.getStationPerformanceReceiveList().size() < 1){
						map.put("cellId" + localCellId + "per", ppp.getStationPerformanceReceiveList());
						map.put("lteSwitchSuccessRation37",null);
						map.put("nrSwitchSuccessRation38",null);
						map.put("nrEarfcnSuccessRation42",null);
						map.put("lteEarfcnSuccessRation46",null);
						map.put("nrSwitchSuccessAvg50",null);
						map.put("wireCoverRate54",null);
					}else{
						map.put("cellId" + localCellId + "per", ppp.getStationPerformanceReceiveList().get(0));
						map.put("lteSwitchSuccessRation37",ppp.getStationPerformanceReceiveList().get(0).getLteSwitchSuccessRation37());
						map.put("nrSwitchSuccessRation38",ppp.getStationPerformanceReceiveList().get(0).getNrSwitchSuccessRation38());
						map.put("nrEarfcnSuccessRation42",ppp.getStationPerformanceReceiveList().get(0).getNrEarfcnSuccessRation42());
						map.put("lteEarfcnSuccessRation46",ppp.getStationPerformanceReceiveList().get(0).getLteEarfcnSuccessRation46());
						map.put("nrSwitchSuccessAvg50",ppp.getStationPerformanceReceiveList().get(0).getNrSwitchSuccessAvg50());
						map.put("wireCoverRate54",ppp.getStationPerformanceReceiveList().get(0).getWireCoverRate54());
					}
					
					if(ppp.getStationBasicParamPojoList() == null || ppp.getStationBasicParamPojoList().size() < 1){
						map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList());
						map.put("cellIdParam", ppp.getStationBasicParamPojoList());
					}else{
						map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList().get(0));
						map.put("cellIdParam", ppp.getStationBasicParamPojoList().get(0));
					}
			
					String templateName = "";
					String imgPath = fileSaveUrl + ppp.getSiteName() +"/";
					Map<String,String> imgNameMap = null;
					Map<String, Object> byteArryMap = null;
					if (ppp.getStationTemplateSelect()==1) { // ????????????
						templateName = "????????????????????????.xlsx";
						imgNameMap = new HashMap<String, String>();
						imgNameMap.put("?????????????????????", "roofFullScene");
						imgNameMap.put(localCellId+"?????????????????????2", "cell"+localCellId+"CoverDirect2");
						byteArryMap = getFile(imgPath,imgNameMap);
						
					} else if (ppp.getStationTemplateSelect()==2) { // ????????????
						templateName = "????????????????????????.xlsx";
						imgNameMap = new HashMap<String, String>();
						imgNameMap.put("?????????????????????", "roofFullScene");
						imgNameMap.put(localCellId+"?????????????????????2", "cell"+localCellId+"CoverDirect2");
						byteArryMap = getFile(imgPath,imgNameMap);
						
					} else if (ppp.getStationTemplateSelect()==3) { // ????????????
						templateName = "????????????????????????.xlsx";
						imgNameMap = new HashMap<String, String>();
						imgNameMap.put("??????????????????", "buildingFullScene");
						imgNameMap.put("???????????????", "statioEntry");
						imgNameMap.put("?????????????????????", "roofFullScene");
						imgNameMap.put(localCellId+"?????????????????????1", "cell"+localCellId+"CoverDirect1");
						imgNameMap.put(localCellId+"?????????????????????2", "cell"+localCellId+"CoverDirect2");
						imgNameMap.put(localCellId+"?????????????????????3", "cell"+localCellId+"CoverDirect3");
						imgNameMap.put(localCellId+"???????????????", "cell"+localCellId+"Surface");
						byteArryMap = getFile(imgPath,imgNameMap);
						
					} else if (ppp.getStationTemplateSelect()==4) { // ??????????????????
						byteArryMap = new HashMap<String,Object>();
						templateName = "??????????????????????????????.xlsx";
					} else if (ppp.getStationTemplateSelect()==5) { // ??????????????????
						byteArryMap = new HashMap<String,Object>();
						templateName = "??????????????????????????????.xlsx";
					} else if (ppp.getStationTemplateSelect()==6) { // ??????????????????
						byteArryMap = new HashMap<String,Object>();
						templateName = "??????????????????????????????.xlsx";
					} else if (ppp.getStationTemplateSelect()==7 || ppp.getStationTemplateSelect()==8 ) { // ???????????????????????????????????????
						byteArryMap = new HashMap<String,Object>();
						templateName = "??????????????????????????????.xlsx";
						imgNameMap = new HashMap<String, String>();
						imgNameMap.put("??????????????????", "buildingFullScene");
						imgNameMap.put("???????????????", "statioEntry");
						imgNameMap.put("?????????????????????", "roofFullScene");
						imgNameMap.put(localCellId+"?????????????????????1", "cell"+localCellId+"CoverDirect1");
						imgNameMap.put(localCellId+"?????????????????????2", "cell"+localCellId+"CoverDirect2");
						imgNameMap.put(localCellId+"?????????????????????3", "cell"+localCellId+"CoverDirect3");
						imgNameMap.put(localCellId+"???????????????", "cell"+localCellId+"Surface");
						byteArryMap = getFile(imgPath,imgNameMap);
					} else { //?????????????????????
						templateName = "????????????????????????.xlsx";
						imgNameMap = new HashMap<String, String>();
						imgNameMap.put("?????????????????????", "roofFullScene");
						imgNameMap.put(localCellId+"?????????????????????2", "cell"+localCellId+"CoverDirect2");
						byteArryMap = getFile(imgPath,imgNameMap);
					}
					map.put("templateName", templateName);
					for (Map.Entry<String, Object> byteArry : byteArryMap.entrySet()) {
						map.put(byteArry.getKey(), byteArry.getValue());
					}
					
					pppMap.put(ppp.getSiteName(), map);
				}
			}
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			File file1 = new File(fileSaveUrl+ "/NR????????????/");
			if (!file1.exists()) {
				file1.mkdirs();
			}
			
			deleteFile(file1);
			
			for (Map.Entry<String, Map<String, Object>> siteNamePpp : pppMap.entrySet()) {
				File file = new File(fileSaveUrl + "/NR????????????/" + workOrderId+"_"+sitename +".xls");
				try {
					OutputStream fileOutputStream = new FileOutputStream(file);
					Jxls2Utils.exportExcel("templates/"+siteNamePpp.getValue().get("templateName"), fileOutputStream, siteNamePpp.getValue());
					String path = fileSaveUrl + "/NR????????????/" + workOrderId+"_"+sitename +".xls";
					return path;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ????????????3d????????????
	 * 
	 * @return
	 */
	public String getDownloadLteTestLog(String idsStr) {
		try {
			String[] ids = idsStr.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String id : ids) {
				idList.add(Long.valueOf(id));
			}
			
			List<Plan4GParam> pppList = oppositeOpen3dReportCreateService.findByIds2(idList);
			Map<String, Map<String, Object>> pppMap = new HashMap<String, Map<String,Object>>();
			for (Plan4GParam ppp : pppList) {
				if(ppp.getCreateReportDate()!=null && ppp.getOpposOpenTemplateSelect()!=null){
					List<Plan4GParam> plan4GParamAscList = oppositeOpen3dReportCreateService.getAllLocalCellId(ppp.getEnbId());
					Integer localCellId = null;
					for(int i=0; i<plan4GParamAscList.size();i++){
						if(plan4GParamAscList.get(i).getLocalCellId() == ppp.getLocalCellId()){
							localCellId = i+1;
							break;
						}
					}
					if(localCellId != null){
						Map<String, Object> map = pppMap.get(ppp.getSiteName());
						String recentDate = String.valueOf(ppp.getCreateReportDate()).substring(0, 4)
								+"/"+String.valueOf(ppp.getCreateReportDate()).substring(4, 6)
								+"/"+String.valueOf(ppp.getCreateReportDate()).substring(6, 8);
						if(map == null){
							map = new HashMap<String, Object>();
							map.put("longitude",ppp.getLongitude());
							map.put("latitude",ppp.getLatitude());
							map.put("enbId",ppp.getOppositeOpen3dWirelessPojo().getEnbId());
							map.put("tac",ppp.getOppositeOpen3dWirelessPojo().getTac());
							map.put("enbIdReality",ppp.getOppositeOpen3dWirelessPojo().getEnbIdReality());
							map.put("enbIdContrast",ppp.getOppositeOpen3dWirelessPojo().getEnbIdContrastStr());
							map.put("tacContrast",ppp.getOppositeOpen3dWirelessPojo().getTacContrastStr());
							map.put("reportCreateDate", recentDate);
						}else if(map.get("reportCreateDate") == null){
							map.put("reportCreateDate", recentDate);
						}else {
							String oldDate =  String.valueOf(map.get("reportCreateDate")).replace("/", "");
							if(ppp.getCreateReportDate() > Long.valueOf(oldDate)){
								map.put("reportCreateDate", recentDate);
							}
						}
						
		//				map.put("stationId" + localCellId + "Info",ppp);
						map.put("cellId" + localCellId + "result", ppp.getOppositeOpen3dResultPojo());
						map.put("cellId" + localCellId + "wire", ppp.getOppositeOpen3dWirelessPojo());
						map.put("cellId" + localCellId + "per", ppp.getOppositeOpen3dPerformanceReceivePojo());
						
						if(ppp.getStationBasicParamPojoList() == null || ppp.getStationBasicParamPojoList().size() < 1){
							map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList());
							map.put("cellIdParam", ppp.getStationBasicParamPojoList());
						}else{
							map.put("cellId" + localCellId + "basic", ppp.getStationBasicParamPojoList().get(0));
							map.put("cellIdParam", ppp.getStationBasicParamPojoList().get(0));
						}
						
						String templateName = "";
						String imgPath = fileSaveUrl + ppp.getSiteName() +"/";
						Map<String,String> imgNameMap = null;
						Map<String, Object> byteArryMap = null;
						if (ppp.getOpposOpenTemplateSelect()==1) { // ????????????
							templateName = "????????????3d????????????.xlsx";
							imgNameMap = new HashMap<String, String>();
							imgNameMap.put("??????????????????", "buildingFullScene");
							imgNameMap.put("?????????????????????", "roofFullScene");
							imgNameMap.put(localCellId+"???????????????", "cell"+localCellId+"Surface");
							imgNameMap.put(localCellId+"?????????????????????", "cell"+localCellId+"CoverDirect");
							byteArryMap = getFile(imgPath,imgNameMap);
							
						} else if (ppp.getOpposOpenTemplateSelect()==2) { // ????????????
							templateName = "????????????3d????????????.xlsx";
							imgNameMap = new HashMap<String, String>();
							imgNameMap.put(localCellId+"???????????????", "cell"+localCellId+"Surface");
							imgNameMap.put(localCellId+"?????????????????????", "cell"+localCellId+"CoverDirect");
							byteArryMap = getFile(imgPath,imgNameMap);
							
						} else { //?????????????????????
							templateName = "????????????3d????????????.xlsx";
							imgNameMap = new HashMap<String, String>();
							imgNameMap.put("??????????????????", "buildingFullScene");
							imgNameMap.put("?????????????????????", "roofFullScene");
							imgNameMap.put(localCellId+"???????????????", "cell"+localCellId+"Surface");
							imgNameMap.put(localCellId+"?????????????????????", "cell"+localCellId+"CoverDirect");
							byteArryMap = getFile(imgPath,imgNameMap);
						}
						map.put("templateName", templateName);
						for (Map.Entry<String, Object> byteArry : byteArryMap.entrySet()) {
							map.put(byteArry.getKey(), byteArry.getValue());
						}
						
						pppMap.put(ppp.getSiteName(), map);
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			File file1 = new File(fileSaveUrl+ "/??????3d????????????/");
			if (!file1.exists()) {
				file1.mkdirs();
			}
			
			deleteFile(file1);
			
			for (Map.Entry<String, Map<String, Object>> siteNamePpp : pppMap.entrySet()) {
				File file = new File(fileSaveUrl + "/??????3d????????????/" + workOrderId+"_"+sitename +".xls");
				try {
					OutputStream fileOutputStream = new FileOutputStream(file);
					Jxls2Utils.exportExcel("templates/"+siteNamePpp.getValue().get("templateName"), fileOutputStream, siteNamePpp.getValue());
					String path = fileSaveUrl + "/??????3d????????????/" + workOrderId+"_"+sitename +".xls";
					return path;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * ?????????????????????????????????????????????????????????????????????????????????
	 * @param filePath ???????????????
	 * @param imgNameMap key???????????????????????????value?????????excel???????????????
	 */
	public Map<String,Object> getFile(String filePath, Map<String,String> imgNameMap) throws Exception{
		File filePackage = new File(filePath);
		Map<String,Object> byteArryMap = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate = sdf.format(new Date());
		try {
			if (filePackage != null) {
				if (filePackage.isDirectory()) {
					File[] fileArray = filePackage.listFiles();
					if (fileArray != null && fileArray.length > 0) {
						// ????????????
						for (Map.Entry<String, String> imgName : imgNameMap.entrySet()) {
							Long testTime = null; 
							File defile = null; 
							// ?????????????????????????????????
							for (File f : fileArray) {
								String fileName = f.getName();
								if (fileName.indexOf(imgName.getKey()) != -1 && f.isFile()) {
									if(fileName.indexOf("#") != -1 
											&& fileName.indexOf("#", fileName.indexOf("#")+1) != -1 
											&& fileName.lastIndexOf("#") > fileName.indexOf("#", fileName.indexOf("#")+1)){
										Long imgTime = Long.valueOf(fileName.substring(fileName.indexOf("#", fileName.indexOf("#")+1)+1, fileName.lastIndexOf("#")));
										if (testTime == null 
												|| Math.abs(testTime - Long.valueOf(newDate)) > Math.abs(imgTime - Long.valueOf(newDate))) {
											testTime = imgTime;
											defile = f;
										}
									}
								}
							}
							if(defile != null){
								// ????????????????????????
								InputStream imageInputStream = ClassUtil.getResourceAsStream(defile.getPath());
//						        InputStream imageInputStream = new FileInputStream(defile.getPath());
						        byte[] imageBytes = Util.toByteArray(imageInputStream);
						        byteArryMap.put(imgName.getValue(), imageBytes);
							}
						}
					}
				}
			}
			return byteArryMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
			throw new Exception("?????????????????????????????????");
		} 
	}
	
	static int flag = 1;//????????????????????????????????????
	
	public static void deleteFile(File file){
        //??????????????????null?????????????????????
        if (file == null || !file.exists()){
            flag = 0;
            System.out.println("??????????????????,?????????????????????????????????");
            return;
        }
        //?????????????????????????????????????????????
        File[] files = file.listFiles();
        //?????????????????????????????????
        for (File f: files){
            f.delete();
        }
    }
	



	public CreatStationTaskReport() {
		super();
	}


	/**
	 * @return the sitename
	 */
	public String getSitename() {
		return sitename;
	}

	/**
	 * @param sitename the sitename to set
	 */
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	/**
	 * @return the netType
	 */
	public String getNetType() {
		return netType;
	}

	/**
	 * @param netType the netType to set
	 */
	public void setNetType(String netType) {
		this.netType = netType;
	}
	
	/**
	 * @return the workOrderId
	 */
	public String getWorkOrderId() {
		return workOrderId;
	}

	/**
	 * @param workOrderId the workOrderId to set
	 */
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	/**
	 * @return the newStationTask
	 */
	public StationTaskOrderPojo getNewStationTask() {
		return newStationTask;
	}

	/**
	 * @param newStationTask the newStationTask to set
	 */
	public void setNewStationTask(StationTaskOrderPojo newStationTask) {
		this.newStationTask = newStationTask;
	}

	
}
