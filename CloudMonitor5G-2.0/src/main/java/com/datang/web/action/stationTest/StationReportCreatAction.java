package com.datang.web.action.stationTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.stationParam.StationProspectParamService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 单站报告生成Controller
 * 
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class StationReportCreatAction extends PageAction implements ModelDriven<PlanParamPojo> {

	private static final long serialVersionUID = 1284972158355134539L;

	private PlanParamPojo planParamPojo = new PlanParamPojo();

	private Long startTime;// 测试开始时间

	private Long endTime;// 测试结束时间

	private Integer testStatus;// 测试状态

	private Integer testService;// 测试业务

	private String ids;

	private String siteNames;

	private String cityNamesStr;
	
	private String errorMsg = "";

	@Autowired
	private StationReportCreatService stationReportCreatService;

	@Autowired
	private TerminalMenuService terminalMenuService;

	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;

	/**
	 * 设备组服务
	 */
	@Autowired
	private TerminalGroupService groupService;

	/**
	 * 基站勘察服务
	 */
	@Autowired
	private StationProspectParamService stationProspectParamService;

	public String goToStationReportCreatJsp() {

		return ReturnType.LISTUI;
	}

	@Override
	public PlanParamPojo getModel() {
		return planParamPojo;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// 初始化查询时，需要判断用户的区域权限
		if (!StringUtils.hasText(planParamPojo.getCity()) || "全部".equals(planParamPojo.getCity())) {
			if (cityNamesStr == null || !StringUtils.hasText(cityNamesStr)) {
				cityNamesStr = "";
				// 获取用户权限范围内的二级域menu
				List<TerminalMenu> cities = menuManageService.getCities();
				// 将二级域menu转化成terminalGroup
				List<TerminalGroup> groupsByMenus = groupService.getGroupsByMenus(cities);
				for (int i = 0; i < groupsByMenus.size(); i++) {
					cityNamesStr = cityNamesStr + groupsByMenus.get(i).getName();
					if (i != groupsByMenus.size() - 1) {
						cityNamesStr = cityNamesStr + ",";
					}
				}
			}
		}

		pageList.putParam("cityStr", cityNamesStr);
		pageList.putParam("startTime", startTime);
		pageList.putParam("endTime", endTime);
		pageList.putParam("testStatus", testStatus);
		pageList.putParam("testService", testService);
		pageList.putParam("planParamPojo", planParamPojo);
		return stationReportCreatService.doPageQuery(pageList);
	}

	public String createReport() {

		String[] idList = ids.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<PlanParamPojo> planList = new ArrayList<PlanParamPojo>();
		String newDate = sdf.format(new Date());
		List<Long> idLong = new ArrayList<Long>();
		String cellNames = "";
		for (String id : idList) {
			PlanParamPojo plan = stationReportCreatService.find(Long.valueOf(id));
			List<EceptionCellLogPojo> cellLogList = stationReportCreatService
					.getExceptionCellLogOfCellName(plan.getCellName());
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
			// 根据cellName获取单验日志详表数据
			List<EceptionCellLogPojo> cellLogList = stationReportCreatService
					.getExceptionCellLogOfCellName(plan.getCellName());
			plan.setReportCreateDate(Long.valueOf(newDate));
			stationReportCreatService.update(plan);
			if (cellLogList != null) {
				// 计算小区参数规划
				stationCellParamCensus(newDate, plan, cellLogList);
				// 网优验收测试表和性能验收测试表
				netoptReceiveTest(newDate, plan, cellLogList);
				// 站点规划参数和基站工程参数
				stationBaseParamSave(newDate, plan, cellLogList);
			}
			
		}
		
		//对比不同localCellid的结果，补充性能验收表、基站工程参数表、规划工参的值
		List<PlanParamPojo> newPlanList = new ArrayList<PlanParamPojo>();
		for (Long id : idLong) {
			PlanParamPojo plan = stationReportCreatService.find(Long.valueOf(id));
			newPlanList.add(plan);
		}
		for (PlanParamPojo plan : newPlanList) {
			// 网优验收表、性能验收测试表和基站工程参数表补充参数
			addContrastParam(plan);
			

			StationNetoptReceiveTestPojo netoptReceiveTest = null;
			StationPerformanceReceivePojo performanceReceive = null;
			StationBasicParamPojo stationBasicParamPojo = null;
			//网优验收测试表
			if(plan.getStationNetoptReceiveTestList()!=null && plan.getStationNetoptReceiveTestList().size()>0 ){
				netoptReceiveTest = plan.getStationNetoptReceiveTestList().get(0);
			}
			//性能验收测试表
			if(plan.getStationPerformanceReceiveList()!=null && plan.getStationPerformanceReceiveList().size()>0 ){
				performanceReceive = plan.getStationPerformanceReceiveList().get(0);
			}
			//基站工程参数
			if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
				stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
			}
			
			StationProspectParamPojo stationProspectParamPojo = stationProspectParamService
					.findBySiteName(plan.getSiteName());
			
			Menu menu = terminalMenuService.get(plan.getCity());	
			// 设置工参表中 不通过的测试项和基站勘察结果判定
			if (menu == null || menu.getStationParamPojo() == null) {
				plan.setStationTemplateSelect(1);
				if (performanceReceive.getFtpDownload().equals("否") || performanceReceive.getFtpUpload().equals("否")) {
					plan.setNoPassTestEvent("FTP测试未通过");
				} else if (performanceReceive.getNrEdsaddSuccessRation().equals("否")
						|| performanceReceive.getNrSwitchSuccessRation38().equals("否")) {
					plan.setNoPassTestEvent("接入切换测试未通过");
				} else if (performanceReceive.getPingDelay().equals("否")){
					plan.setNoPassTestEvent("Ping测试未通过");
				} else {
					plan.setNoPassTestEvent("全部通过");
				}

				if (stationProspectParamPojo != null) {
					if(stationBasicParamPojo.getAdjustLonContrast() == null
							|| stationBasicParamPojo.getAdjustLatContrast() == null
							|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
							|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
						plan.setStationProspectRlt("经纬度不符");
					} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
							|| stationBasicParamPojo.getHeightContrastByParam77().equals("不一致")){
						plan.setStationProspectRlt("天线挂高不符");
					} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
							|| stationBasicParamPojo.getAdjustTiltMContrast() == null
							||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
							|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
						plan.setStationProspectRlt("天馈参数不符");
					} else{
						plan.setStationProspectRlt("全部一致");
					}
				}
				
			} else if (menu.getStationParamPojo() != null) {
				StationParamPojo spp = menu.getStationParamPojo();
				if(StringUtils.hasText(spp.getSingleStationMOdelSelect())){
					plan.setStationTemplateSelect(Integer.valueOf(spp.getSingleStationMOdelSelect()));
				}else{
					plan.setStationTemplateSelect(1);
				}
				if ("1".equals(spp.getSingleStationMOdelSelect())) { // 山西模板
					if (performanceReceive.getFtpDownload().equals("否") || performanceReceive.getFtpUpload().equals("否")) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (performanceReceive.getNrEdsaddSuccessRation().equals("否")
							|| performanceReceive.getNrSwitchSuccessRation38().equals("否")) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPingDelay().equals("否")){
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				} else if ("2".equals(spp.getSingleStationMOdelSelect())) { // 兰州模板
					if (performanceReceive.getFtpDownload().equals("否") 
							|| performanceReceive.getFtpUpload().equals("否")
							|| performanceReceive.getLteEarfcnSuccessRation().equals("否") 
							|| performanceReceive.getNrEarfcnSuccessRation().equals("否")) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (performanceReceive.getLteEpsattachSuccessRation().equals("否")
							|| performanceReceive.getNrEdsaddSuccessRation().equals("否")
							|| performanceReceive.getLteInterfreqHandoverSuccessRation().equals("否")
							|| performanceReceive.getNrEdschangeSuccessRation().equals("否")) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPing32SuccessRation().equals("否")
							|| performanceReceive.getPing1500SuccessRation().equals("否")) {
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				} else if ("3".equals(spp.getSingleStationMOdelSelect())) { // 贵阳模板
					if (netoptReceiveTest.getFtpDownThrputGoog() == null
							|| Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) < spp.getUpgradeGood()
							|| netoptReceiveTest.getFtpUpThrputGoog() == null
							|| Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) < spp.getUploadGood()) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (netoptReceiveTest.getLteEpsattachSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteEpsattachSum()) < 10
							|| netoptReceiveTest.getNrEdsaddSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEdsaddSum()) < 10) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (netoptReceiveTest.getPing32SuccessRation() == null
							|| Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) > spp.getPing32DelayTime()) {
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("不一致")){
							plan.setStationProspectRlt("AAU型号不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
					
				} else if ("4".equals(spp.getSingleStationMOdelSelect())) { // 云南室外模板
					if (performanceReceive.getFtpDownload().equals("否") || performanceReceive.getFtpUpload().equals("否")
							|| performanceReceive.getNrEarfcnSuccessRation42().equals("否")) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (performanceReceive.getNrEdsaddSuccessRation().equals("否")
							|| performanceReceive.getNrSwitchSuccessRation38().equals("否")) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPingDelay().equals("否")){
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("不一致")){
							plan.setStationProspectRlt("AAU型号不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				} else if ("5".equals(spp.getSingleStationMOdelSelect())) { // 云南室分模板
					if (netoptReceiveTest.getLteEarfcnSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteEarfcnSum()) < 1
							|| netoptReceiveTest.getNrEarfcnSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEarfcnSum()) < 1) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (netoptReceiveTest.getLteEpsattachSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteEpsattachSum()) < 10
							|| netoptReceiveTest.getNrEdsaddSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEdsaddSum()) < 10
							|| netoptReceiveTest.getLteInterfreqHandoverSum() == null
							|| Float.valueOf(netoptReceiveTest.getLteInterfreqHandoverSum()) < 1
							|| netoptReceiveTest.getNrEdschangeSum() == null
							|| Float.valueOf(netoptReceiveTest.getNrEdschangeSum()) < 1
							) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if(netoptReceiveTest.getPing32SuccessRation() == null
							|| Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) > spp.getPing32DelayTime()
							|| netoptReceiveTest.getPing32Success() == null
							|| Float.valueOf(netoptReceiveTest.getPing32Success()) < spp.getPing32Number()
							|| netoptReceiveTest.getPing1500SuccessRation() == null
							|| Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) > spp.getPing1500DelayTime()
							|| netoptReceiveTest.getPing1500Success() == null
							|| Float.valueOf(netoptReceiveTest.getPing1500Success()) < spp.getPing1500Nember()){
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						plan.setStationProspectRlt("全部一致");
					}
					
				}  else if ("6".equals(spp.getSingleStationMOdelSelect())) { // 河南联通模板
					if (netoptReceiveTest.getFtpGoodPdcpnrThrputDL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputDL()) < spp.getUpgradeGood()
							|| netoptReceiveTest.getFtpGoodPdcpnrThrputUL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputUL()) < spp.getUploadGood()) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (netoptReceiveTest.getNrConnectSuccessSum()== null
							|| Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) < 10
							|| netoptReceiveTest.getNrSwitchSuccessRate() == null
							|| Float.valueOf(netoptReceiveTest.getNrSwitchSuccessRate()) < spp.getChangeSuccessRatio5G()) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPing32SuccessRation().equals("否")
							|| performanceReceive.getPing1500SuccessRation().equals("否")) {
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				} else if ("7".equals(spp.getSingleStationMOdelSelect()) || "8".equals(spp.getSingleStationMOdelSelect())) { // 贵州电信单验报告模板或贵州联通单验报告模板
					if (netoptReceiveTest.getFtpGoodPdcpnrThrputDL() == null 
								|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputDL()) < spp.getUpgradeGood() 
								|| netoptReceiveTest.getFtpGoodPdcpnrThrputUL() != null 
								|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputUL()) < spp.getUploadGood()) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (netoptReceiveTest.getNrConnectSuccessSum() == null || Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) < 10) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if ((netoptReceiveTest.getPing32SuccessRation() != null && Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) > 15)
								|| (netoptReceiveTest.getPing32Success() != null && Float.valueOf(netoptReceiveTest.getPing32Success()) < 20)
								|| (netoptReceiveTest.getPing1500SuccessRation() != null && Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) >= 17)
								|| (netoptReceiveTest.getPing1500Success() != null && Float.valueOf(netoptReceiveTest.getPing1500Success()) < 20)) {
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("不一致")){
							plan.setStationProspectRlt("AAU型号不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				}  else if ("9".equals(spp.getSingleStationMOdelSelect()) || "10".equals(spp.getSingleStationMOdelSelect())) { // 辽宁电信单验报告模板或辽宁联通单验报告模板
					if (netoptReceiveTest.getFtpGoodPdcpnrThrputDL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputDL()) < spp.getUpgradeGood()
							|| netoptReceiveTest.getFtpGoodPdcpnrThrputUL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputUL()) < spp.getUploadGood()) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (netoptReceiveTest.getNrConnectSuccessSum()== null
							|| Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) < 10
							|| netoptReceiveTest.getNrSwitchSuccessRate() == null
							|| Float.valueOf(netoptReceiveTest.getNrSwitchSuccessRate()) < spp.getChangeSuccessRatio5G()) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if ((netoptReceiveTest.getPing32SuccessRation() != null && Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) > 15)
							|| (netoptReceiveTest.getPing32Success() != null && Float.valueOf(netoptReceiveTest.getPing32Success()) < 20)
							|| (netoptReceiveTest.getPing1500SuccessRation() != null && Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) >= 17)
							|| (netoptReceiveTest.getPing1500Success() != null && Float.valueOf(netoptReceiveTest.getPing1500Success()) < 20)) {
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				}  else if ("11".equals(spp.getSingleStationMOdelSelect()) || "12".equals(spp.getSingleStationMOdelSelect())) { // 内蒙电信单验报告模板或内蒙联通单验报告模板
					if (netoptReceiveTest.getFtpGoodPdcpnrThrputDL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputDL()) < spp.getUpgradeGood()
							|| netoptReceiveTest.getFtpGoodPdcpnrThrputUL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputUL()) < spp.getUploadGood()) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (netoptReceiveTest.getNrConnectSuccessSum()== null
							|| Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) < 10
							|| netoptReceiveTest.getNrSwitchSuccessRate() == null
							|| Float.valueOf(netoptReceiveTest.getNrSwitchSuccessRate()) < spp.getChangeSuccessRatio5G()) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPingDelay().equals("否")){
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("不一致")){
							plan.setStationProspectRlt("AAU型号不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				}  else if ("13".equals(spp.getSingleStationMOdelSelect()) 
								|| "14".equals(spp.getSingleStationMOdelSelect())
								|| "15".equals(spp.getSingleStationMOdelSelect())
								|| "16".equals(spp.getSingleStationMOdelSelect())) { // 陕西电信单验报告模板或陕西联通单验报告模板或湖北移动或湖北电联
					if (netoptReceiveTest.getFtpGoodPdcpnrThrputDL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputDL()) < spp.getUpgradeGood()
							|| netoptReceiveTest.getFtpGoodPdcpnrThrputUL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputUL()) < spp.getUploadGood()
							|| netoptReceiveTest.getFtpBadPdcpnrThrputDL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpBadPdcpnrThrputDL()) < spp.getUpgradeBad()
							|| netoptReceiveTest.getFtpBadPdcpnrThrputUL() == null
							|| Float.valueOf(netoptReceiveTest.getFtpBadPdcpnrThrputUL()) < spp.getUploadBad()) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (performanceReceive.getNrEdsaddSuccessRation()== null
							|| performanceReceive.getNrEdsaddSuccessRation().equals("否")
							|| performanceReceive.getNrEdschangeGt6Rlt94() == null
							|| performanceReceive.getNrEdschangeGt6Rlt94().equals("否")) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPingDelay().equals("否")){
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						}else{
							plan.setStationProspectRlt("全部一致");
						}
					}
					
				} else { //默认是山西模板
					if (performanceReceive.getFtpDownload().equals("否") || performanceReceive.getFtpUpload().equals("否")) {
						plan.setNoPassTestEvent("FTP测试未通过");
					} else if (performanceReceive.getNrEdsaddSuccessRation().equals("否")
							|| performanceReceive.getNrSwitchSuccessRation38().equals("否")) {
						plan.setNoPassTestEvent("接入切换测试未通过");
					} else if (performanceReceive.getPingDelay().equals("否")){
						plan.setNoPassTestEvent("Ping测试未通过");
					} else {
						plan.setNoPassTestEvent("全部通过");
					}
					
					if (stationProspectParamPojo != null) {
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getHeightContrastByParam77() == null 
								|| stationBasicParamPojo.getHeightContrastByParam77().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAzimuthContrast() == null
								|| stationBasicParamPojo.getAdjustTiltMContrast() == null
								||stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltMContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
				}
			}
			
			stationReportCreatService.update(plan);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.hasText(cellNames)) {
			map.put("cellNames", cellNames);
		} else {
			map.put("cellNames", null);
		}
		
		if(StringUtils.hasText(errorMsg)){
			map.put("errorMsg", errorMsg);
		}else{
			map.put("errorMsg", null);
		}

		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}

	/**
	 * 站点规划参数和基站工程参数
	 * 
	 * @author lucheng date:2020年6月24日 下午3:02:31
	 * @param newDate 报告生成时间
	 * @param plan 日志数据
	 * @param cellLogList 日志详表
	 */
	private void stationBaseParamSave(String newDate, PlanParamPojo plan, List<EceptionCellLogPojo> cellLogList) {

		// 获取对应的所有的localcellid从而判断属于几小区
		List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(plan.getSiteName());

		StationBasicParamPojo stationBasicParamPojo = new StationBasicParamPojo();
		Menu menu = terminalMenuService.get(plan.getCity());

		stationBasicParamPojo.setRegion(plan.getCity());
		stationBasicParamPojo.setCellName(plan.getCellName());
		stationBasicParamPojo.setSiteName(plan.getSiteName());
		stationBasicParamPojo.setCellParamId(plan.getId());
		stationBasicParamPojo.setLocalCellId(plan.getLocalCellID().toString());

		// 保存站点规划参数
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
		
//		stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
		
		stationBasicParamPojo.setAdjustGnbId(String.valueOf(planParamPojoAscList.get(0).getGnbId()));
		
		if (stationBasicParamPojo.getGnbId() != null && stationBasicParamPojo.getAdjustGnbId() != null
				&& stationBasicParamPojo.getGnbId().equals(stationBasicParamPojo.getAdjustGnbId())) {
			stationBasicParamPojo.setAdjustGnBIdContrast("一致");
		} else {
			stationBasicParamPojo.setAdjustGnBIdContrast("不一致");
		}

		// 保存基站工程参数
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
			
			stationBasicParamPojo.setBuildingFunction86(stationProspectParamPojo.getBuildingFunction());

			for (int i = 0; i < planParamPojoAscList.size(); i++) { 
				if (plan.getLocalCellID() == planParamPojoAscList.get(i).getLocalCellID()) {
					if (i == 0) {
						stationBasicParamPojo.setAdjustAzimuth(stationProspectParamPojo.getCell1Azimuth());
						stationBasicParamPojo.setAdjustTiltM(stationProspectParamPojo.getCell1TiltM());
//						stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
						stationBasicParamPojo.setAdjustTiltE(stationProspectParamPojo.getCell1TilE());
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
//						stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
						stationBasicParamPojo.setAdjustTiltE(stationProspectParamPojo.getCell2TiltE());
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
//						stationBasicParamPojo.setAdjustTiltE(plan.getTiltE());
						stationBasicParamPojo.setAdjustTiltE(stationProspectParamPojo.getCell3TiltE());
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
				stationBasicParamPojo.setAdjustHeightContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustHeightContrast("不一致");
			}

			if (stationBasicParamPojo.getAzimuth() != null && stationBasicParamPojo.getAdjustAzimuth() != null
					&& Math.abs(stationBasicParamPojo.getAzimuth() - stationBasicParamPojo.getAdjustAzimuth()) < 10) {
				stationBasicParamPojo.setAdjustAzimuthContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustAzimuthContrast("不一致");
			}

			if (stationBasicParamPojo.getTiltTotal() != null && stationBasicParamPojo.getAdjustTiltToatal() != null
					&& Math.abs(
							stationBasicParamPojo.getTiltTotal() - stationBasicParamPojo.getAdjustTiltToatal()) < 2) {
				stationBasicParamPojo.setAdjustTiltToatalContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustTiltToatalContrast("不一致");
			}

			if (stationBasicParamPojo.getTiltE() != null && stationBasicParamPojo.getAdjustTiltE() != null
					&& Math.abs(stationBasicParamPojo.getTiltE() - stationBasicParamPojo.getAdjustTiltE()) < 2) {
				stationBasicParamPojo.setAdjustTiltEContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustTiltEContrast("不一致");
			}

			if (stationBasicParamPojo.getTiltM() != null && stationBasicParamPojo.getAdjustTiltM() != null
					&& Math.abs(stationBasicParamPojo.getTiltM() - stationBasicParamPojo.getAdjustTiltM()) < 2) {
				stationBasicParamPojo.setAdjustTiltMContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustTiltMContrast("不一致");
			}

			if (stationBasicParamPojo.getAauModel() != null && stationBasicParamPojo.getAdjustAauModel() != null
					&& stationBasicParamPojo.getAauModel().equals(stationBasicParamPojo.getAdjustAauModel())) {
				stationBasicParamPojo.setAdjustAauModelContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustAauModelContrast("不一致");
			}

			if (stationBasicParamPojo.getLon() != null && stationBasicParamPojo.getAdjustLon() != null
					&& String.format("%.4f", stationBasicParamPojo.getLon())
							.equals(String.format("%.4f", stationBasicParamPojo.getAdjustLon()))) {
				stationBasicParamPojo.setAdjustLonContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustLonContrast("不一致");
			}

			if (stationBasicParamPojo.getLat() != null && stationBasicParamPojo.getAdjustLat() != null
					&& String.format("%.4f", stationBasicParamPojo.getLat())
							.equals(String.format("%.4f", stationBasicParamPojo.getAdjustLat()))) {
				stationBasicParamPojo.setAdjustLatContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustLatContrast("不一致");
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
			errorMsg = errorMsg+" 站名'"+plan.getSiteName()+"'对应的基站勘察数据为空! ";
		}

		stationBasicParamPojo.setPlanParamPojo(plan);
		stationReportCreatService.createStationBasicParamTest(stationBasicParamPojo);
	}

	/**
	 * 网优验收测试表/性能验收测试表
	 * 
	 * @author maxuancheng date:2020年2月22日 下午5:02:31
	 * @author lucheng date:2020年6月24日修改
	 * @param newDate
	 *            报告生成时间
	 * @param plan
	 *            日志数据
	 * @param cellLogList
	 *            日志详表
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

		// 找出“测试日期”距离生成单验报告日期最近，且“测试业务属性“是”XXXX”的记录
		for (EceptionCellLogPojo log : cellLogList) {
			if (log == null || !StringUtils.hasText(log.getNrTestevent())) {
				continue;
			}
			if (log.getNrTestevent().equals("ENDC成功率测试") && (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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

			if (log.getNrTestevent().equals("绕点_下载")||log.getNrTestevent().equals("绕点_上传")) {
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
			
			if (log.getNrTestevent().equals("绕点_下载")) {
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

			if (log.getNrTestevent().equals("FTP下载") || equals("FTP上传")) {
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

			if (log.getNrTestevent().contains("PING") && log.getNrTestevent().contains("（") 
					&& log.getNrTestevent().contains("）")  
					&& (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
				String pingName = log.getNrTestevent().substring(log.getNrTestevent().indexOf("（")+1, log.getNrTestevent().indexOf("）")) ;
				if(StringUtils.hasText(pingName)){
					if (Float.valueOf(pingName)>=200) { //ping1500业务
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
					}else if (Float.valueOf(pingName)<200) { //ping32业务
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
			
			if (log.getNrTestevent().contains("PING") 
					&& log.getNrTestevent().contains("（") 
					&& log.getNrTestevent().contains("）")  
					&& (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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


			if (log.getNrTestevent().equals("FTP下载") && (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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

			if (log.getNrTestevent().equals("FTP下载") && log.getNrWirelessstation().equals("中点")) {
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

			if (log.getNrTestevent().equals("FTP下载") && log.getNrWirelessstation().equals("差点")) {
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

			if (log.getNrTestevent().equals("FTP上传") && (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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
			if (log.getNrTestevent().equals("FTP上传") && log.getNrWirelessstation().equals("中点")) {
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
			if (log.getNrTestevent().equals("FTP上传") && log.getNrWirelessstation().equals("差点")) {
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
			
			if (log.getNrTestevent().equals("绕点_下载") && log.getNrWirelessstation().equals("好点")) {
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
			
			if (log.getNrTestevent().equals("绕点_上传") && log.getNrWirelessstation().equals("好点")) {
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
			
			if (log.getNrTestevent().equals("FTP下载") && (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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
			
			if (log.getNrTestevent().equals("FTP上传") && (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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
			
			if (log.getNrTestevent().equals("volte测试") && (log.getNrWirelessstation().equals("好点") || log.getNrWirelessstation().equals("极好点"))) {
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
		
		Long lteEpsattachSuccess = 0L;// 4G锚点连接建立成功次数
		Long nrEdsaddSuccess = 0L;// ENDC成功率的5G锚点连接建立成功次数
		Long lteInterfreqSuccess = 0L;// 4G锚点切换成功次数
		Long nrEdschangeSuccess = 0L;// 5G连接切换成功次数
		Long rrcConnectionSuccess = 0L;// 4G锚点RRC重建成功次数
		Long lteEarfcnSuccess = 0L;// dt、dl和ul的4G锚点成功次数
		Long nrEarfcnSuccess = 0L;// dt、dl和ul的5G成功次数
		Long dtLteEarfcnSuccessSum = 0L; // dt的4G锚点成功次数
		Long dtNREarfcnSuccessSum = 0L; // dt的5G成功次数
		Long dtLteEarfcnFailSum = 0L; // dt的4G锚点掉线次数
		Long dtNREarfcnFailSum = 0L; // dt的5G掉线次数
		Long dialVolteSuccessSum = 0L; // 主叫volte成功次数
		Long dialVonrSuccessSum = 0L; // 主叫VONR功次数
		Long voCsfbSuccessSum = 0L; // CSFB成功次数
		Long voCsfbCallbackSucSum = 0L; // CSFB成功回落次数
		Long nrConectSuccess2Sum = 0L; // NR连接建立成功次数2
		Long endcGoodNrConnectSum = 0L; // 序号：105
		
		int cqi_n=0; //CQI的个数
		Float nrCwoWbCqiSum = 0F;// cqi总和
		
		Long nrSwitchSuccessSum = 0L; // NR切换成功次数
		Long nrSwitchDropSum = 0L; // NR切换失败次数
		Long nrSwitchSum = 0L; // NR切换成功次数+NR切换失败次数
		
		Long nrConnectBuildSuccessSum = 0L; //绕点上传或下载的5G连接建立成功次数
		Long nrStationSwitchSum =0L; // “测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G连接切换成功次数-往本站1小区切换成功次数-往本站2小区切换成功次数-往本站3小区切换成功次数”的总和
		Long raoNREarfcnSuccessSum = 0L;  //绕点上传或下载的5G成功次数总和
		Long raoLteEarfcnSuccessSum = 0L; //绕点上传或下载的4G成功次数总和
		
		Long ping1500 = 0L;// ping1500成功次数
		int ping1500Number = 0;// ping1500采样点个数次数
		Float nrSamplenumber1500 = 0F;// ping1500采样点个数综合
		Long ping32 = 0L;// ping32成功次数
		int ping32Number = 0;// ping1500采样点个数次数
		Float nrSamplenumber32 = 0F;// ping1500采样点个数总和

		// 计算总次数
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
				// 序号：105
				if (log.getNrConnectDrop() != null && Long.valueOf(log.getNrConnectDrop()) == 0) {
					if(log.getNrConnectSuccess()!=null){
						endcGoodNrConnectSum = endcGoodNrConnectSum + Long.valueOf(log.getNrConnectSuccess());
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
					nrSwitchDropSum = nrSwitchDropSum + Long.valueOf(log.getNrSwitchDrop());
					nrSwitchSum = nrSwitchSum + Long.valueOf(log.getNrSwitchDrop());
				}
				
				if (log.getNrEdsaddfailure() != null && Long.valueOf(log.getNrEdsaddfailure()) == 0) {
					if(log.getNrEdsaddsuccess()!=null){
						nrConnectBuildSuccessSum = nrConnectBuildSuccessSum + Long.valueOf(log.getNrEdsaddsuccess());
					}
				}
				//103
				if (log.getNrEdschangefailure() != null && Long.valueOf(log.getNrEdschangefailure()) == 0) {
					if(log.getNrEdschangesuccess()!=null){
						nrStationSwitchSum = nrStationSwitchSum + Long.valueOf(log.getNrEdschangesuccess());
						if(log.getToStation1Change() != null){
							nrStationSwitchSum = nrStationSwitchSum - Long.valueOf(log.getToStation1Change());
						}
						if(log.getToStation2Change() != null){
							nrStationSwitchSum = nrStationSwitchSum - Long.valueOf(log.getToStation2Change());
						}
						if(log.getToStation3Change() != null){
							nrStationSwitchSum = nrStationSwitchSum - Long.valueOf(log.getToStation3Change());
						}
					}
				}
				
				//106 ： 测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“5G成功次数”，若有多个记录符合条件，那么取总和。
				if(log.getNrEarfcnsuccess()!=null){
					raoNREarfcnSuccessSum = raoNREarfcnSuccessSum + Long.valueOf(log.getNrEarfcnsuccess());
				}
				//107 ： 测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“4G成功次数”，若有多个记录符合条件，那么取总和。
				if(log.getLteEarfcnsuccess()!=null){
					raoLteEarfcnSuccessSum = raoLteEarfcnSuccessSum + Long.valueOf(log.getNrEarfcnsuccess());
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
			
			if(log.getCsfbSuccess()!=null && log.getCsfbNotRequest() != null && log.getCsfbNotRequest().equals("0")){
				voCsfbSuccessSum = voCsfbSuccessSum + Long.valueOf(log.getCsfbSuccess());
			}
			
			if(log.getCsfbCallbackSuccess()!=null && log.getCsfbCallbackDrop() != null && log.getCsfbCallbackDrop().equals("0")){
				voCsfbCallbackSucSum = voCsfbCallbackSucSum + Long.valueOf(log.getCsfbCallbackSuccess());
			}
		}

		for (EceptionCellLogPojo log : ping1500Map) {
			if (testTimePing1500 != null && log.getNrTestdate().equals(testTimePing1500.toString())) {
				if (log.getNrPingfailure() == null || (log.getNrPingfailure() != null && Integer.valueOf(log.getNrPingfailure()) ==0)) {
					if(log.getNrPingrespose()!=null && log.getNrPingresqtime()!=null && !log.getNrPingresqtime().equals("0")  && log.getNrEdsSuccessSecond()<=2){
						ping1500 = ping1500 + Long.valueOf(log.getNrPingrespose());
						ping1500Number = ping1500Number + Integer.valueOf(log.getNrPingrespose());
						// nrSamplenumber1500 = nrSamplenumber1500 +
						// Float.valueOf(log.getNrSamplenumber());
						nrSamplenumber1500 = nrSamplenumber1500
								+ Integer.valueOf(log.getNrPingrespose()) * Float.valueOf(log.getNrPingresqtime());
					}
				}
			}
		}
		
		for (EceptionCellLogPojo log : ping32Map) {
			if (testTimePing32 != null && log.getNrTestdate().equals(testTimePing32.toString())) {
				if (log.getNrPingfailure() == null || (log.getNrPingfailure() != null && Integer.valueOf(log.getNrPingfailure()) ==0)) {
					if(log.getNrPingrespose()!=null && log.getNrPingresqtime()!=null && !log.getNrPingresqtime().equals("0") && log.getNrEdsSuccessSecond()<=2){
						ping32 = ping32 + Long.valueOf(log.getNrPingrespose());
						ping32Number = ping32Number + Integer.valueOf(log.getNrPingrespose());
						// nrSamplenumber32 = nrSamplenumber32 +
						// Float.valueOf(log.getNrSamplenumber());
						nrSamplenumber32 = nrSamplenumber32
								+ Integer.valueOf(log.getNrPingrespose()) * Float.valueOf(log.getNrPingresqtime());
					}
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
		
		// 91:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“CQI”，若有多个记录符合条件，则取平均值
		for (EceptionCellLogPojo log : dtDLGoodMap) {
			if(log.getNrCwoWbCqiAbg()!=null){
				cqi_n++;
				nrCwoWbCqiSum = nrCwoWbCqiSum + log.getNrCwoWbCqiAbg();
			}
		}
		
		// 4G锚点连接建立成功率
		netoptReceiveTest.setLteEpsattachSuccess(lteEpsattachSuccess.toString());
		netoptReceiveTest.setLteEpsattachFailure("0");
		netoptReceiveTest.setLteEpsattachSum(lteEpsattachSuccess.toString());
		netoptReceiveTest.setLteEpsattachSuccessRation("100");
		// 5G连接建立成功率（gNB添加成功率）
		netoptReceiveTest.setNrEdsaddSuccess(nrEdsaddSuccess.toString());
		netoptReceiveTest.setNrEdsaddFailure("0");
		netoptReceiveTest.setNrEdsaddSum(nrEdsaddSuccess.toString());
		netoptReceiveTest.setNrEdsaddSuccessRation("100");
		// 4G锚点切换成功次数Long lteInterfreqSuccess = 0L;
		netoptReceiveTest.setLteInterfreqHandoverSuccess(lteInterfreqSuccess.toString());
		netoptReceiveTest.setLteInterfreqHandoverFailure("0");
		netoptReceiveTest.setLteInterfreqHandoverSum(lteInterfreqSuccess.toString());
		netoptReceiveTest.setLteInterfreqHandoverSuccessRation("100");
		// 5G连接切换成功次数Long nrEdschangeSuccess = 0L;
		netoptReceiveTest.setNrEdschangeSuccess(nrEdschangeSuccess.toString());
		netoptReceiveTest.setNrEdschangeFailure("0");
		netoptReceiveTest.setNrEdschangeSum(nrEdschangeSuccess.toString());
		netoptReceiveTest.setNrEdschangeSuccessRation("100");
		// 4G锚点RRC重建成功次数Long rrcConnectionSuccess = 0L;
		netoptReceiveTest.setRrcConnectionSuccess(rrcConnectionSuccess.toString());
		netoptReceiveTest.setRrcConnectionFailure("0");
		netoptReceiveTest.setRrcConnectionSum(rrcConnectionSuccess.toString());
		netoptReceiveTest.setRrcConnectionSuccessRation("100");
		// 4G锚点成功次数Long lteEarfcnSuccess = 0L;
		netoptReceiveTest.setLteEarfcnSuccess(lteEarfcnSuccess.toString());
		netoptReceiveTest.setLteEarfcnFailure("0");
		netoptReceiveTest.setLteEarfcnSum(lteEarfcnSuccess.toString());
		netoptReceiveTest.setLteEarfcnSuccessRation("100");
		// 5G成功次数Long nrEarfcnSuccess = 0L;
		netoptReceiveTest.setNrEarfcnSuccess(nrEarfcnSuccess.toString());
		netoptReceiveTest.setNrEarfcnFailure("0");
		netoptReceiveTest.setNrEarfcnSum(nrEarfcnSuccess.toString());
		netoptReceiveTest.setNrEarfcnSuccessRation("100");

		// 测试业务”属性为“绕点”的记录的“4G锚点成功次数” Long dtLteEarfcnSuccessSum = 0L;
		netoptReceiveTest.setDtLteEarfcnSuccessSum(dtLteEarfcnSuccessSum.toString());
		// 测试业务”属性为“绕点”的记录的“4G掉线次数” Long dtLteEarfcnFailSum = 0L;
		netoptReceiveTest.setDtLteFailSumDegree(dtLteEarfcnFailSum.toString());
		netoptReceiveTest.setDtLteSumDegree(String.valueOf(dtLteEarfcnSuccessSum + dtLteEarfcnFailSum));
		netoptReceiveTest.setDtLteEarfcnFailRation(
				(dtLteEarfcnSuccessSum + dtLteEarfcnFailSum) == 0 ? null : String.format("%.2f",(dtLteEarfcnFailSum / (dtLteEarfcnSuccessSum + dtLteEarfcnFailSum)*100f)));

		// 测试业务”属性为“绕点”的记录的“5G成功次数” Long dtNREarfcnSuccessSum = 0L;
		netoptReceiveTest.setDtNRSuccessSum(dtNREarfcnSuccessSum.toString());
		// 测试业务”属性为“绕点”的记录的“4G掉线次数” Long dtNREarfcnFailSum = 0L;
		netoptReceiveTest.setDtNrFailSumDegree(dtNREarfcnFailSum.toString());
		netoptReceiveTest.setDtNRSumDegree(String.valueOf(dtNREarfcnSuccessSum + dtNREarfcnFailSum));
		netoptReceiveTest.setDtNREarfcnFailRation(
				(dtNREarfcnSuccessSum + dtNREarfcnFailSum) == 0 ? null : String.format("%.2f",(dtNREarfcnFailSum / (dtNREarfcnSuccessSum + dtNREarfcnFailSum)*100f)));

		// ping1500成功次数Long ping1500 = 0L;
		netoptReceiveTest.setPing1500Success(ping1500.toString());
		netoptReceiveTest.setPing1500Failure("0");
		netoptReceiveTest.setPing1500Sum(ping1500.toString());
		netoptReceiveTest.setPing1500SuccessRation(
				ping1500Number == 0 ? null : String.format("%.2f", (nrSamplenumber1500 / ping1500Number)));
		// ping32成功次数Long ping32 = 0L;
		netoptReceiveTest.setPing32Success(ping32.toString());
		netoptReceiveTest.setPing32Failure("0");
		netoptReceiveTest.setPing32Sum(ping32.toString());
		netoptReceiveTest.setPing32SuccessRation(
				ping32Number == 0 ? null : String.format("%.2f", (nrSamplenumber32 / ping32Number)));

		// 序号56 1-“30”（4G锚点切换成功次数 /总次数） 值为：1-100%
		netoptReceiveTest.setLteInterfreqHandoverfailRation("0");
		
		// 序号76 主叫volte成功次数
		netoptReceiveTest.setDialVolteSuccessSum(dialVolteSuccessSum.toString());
		
		// 序号77 主叫vonr成功次数
		netoptReceiveTest.setDialVonrSuccessSum(dialVonrSuccessSum.toString());

		// 序号78 “测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点测试”的记录的“NR切换成功次数”除以（“NR切换成功次数”+“NR切换失败次数”），若有多个记录符合条件，那么分子分母分别求和以后汇总
		netoptReceiveTest.setNrSwitchSuccessRate(
				nrSwitchSum == 0 ? null : String.format("%.2f",(nrSwitchSuccessSum/nrSwitchSum*100F)));
		
		//序号81 nr连接建立次数
		netoptReceiveTest.setNrConnectSuccessSum(nrConectSuccess2Sum.toString());

		// 序号 91:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“CQI”，若有多个记录符合条件，则取平均值
		netoptReceiveTest.setNrCwoWbCqiAvg(cqi_n == 0 ? null : String.valueOf(Math.round(nrCwoWbCqiSum/cqi_n)));
		
		// 序号 93:“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“NR切换成功次数”,若有多个符合条件，那么取总和
		netoptReceiveTest.setNrDlUlSwitchSuccess(String.valueOf(nrSwitchSuccessSum));
		
		// 序号 94:“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“NR切换失败次数”,若有多个符合条件，那么取总和
		netoptReceiveTest.setNrDlUlSwitchDrop(String.valueOf(nrSwitchDropSum));
		
		// 序号 95: “测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G连接建立成功次数”，若有多个记录符合条件，那么取总和，但是如果某条记录的“5G连接建立失败次数”不等于0，则该条记录的“5G连接建立成功次数”不计入统计，
		netoptReceiveTest.setNrConnectBuildSucess(String.valueOf(nrConnectBuildSuccessSum));
		
		// 序号 98：“测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“CSFB成功次数”，若有多个记录，则取所有“CSFB未接通次数”为0的记录的总和
		netoptReceiveTest.setVolteCsfbSuccess(String.valueOf(voCsfbSuccessSum));
		
		// 序号 99：“测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“CSFB成功回落次数”，若有多个记录，则取所有“CSFB回落失败次数”为0的记录的总和
		netoptReceiveTest.setVolteCsfbCallbackSuccess(String.valueOf(voCsfbCallbackSucSum));
		
		// 序号 103：“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G连接切换成功次数-往本站1小区切换成功次数-往本站2小区切换成功次数-往本站3小区切换成功次数”，
		//若有多个记录符合，那么取总和。但是如果某条记录的“5G连接切换失败次数”不等于0，则该条记录的““5G连接切换成功次数-往本站1小区切换成功次数-往本站2小区切换成功次数-往本站3小区切换成功次数”不计入统计
		netoptReceiveTest.setNrStationSwitchSum(String.valueOf(nrStationSwitchSum));
		
		//序号：105:测试业务”属性为“ENDC成功率测试”。“无线情况”属性为好点或者极好点，的记录的“NR连接建立成功次数”，若有多个记录符合条件，那么取总和,但是如果某条记录的“NR连接建立失败次数”不等于0，则该条记录的“NR连接建立成功次数”不计入统计，
		netoptReceiveTest.setEndcGoodNrConnectSum(String.valueOf(endcGoodNrConnectSum));
		
		//106 ： 测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“5G成功次数”，若有多个记录符合条件，那么取总和。
		netoptReceiveTest.setRaoNREarfcnSuccessSum(String.valueOf(raoNREarfcnSuccessSum));
		
		//107 ： 测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“4G成功次数”，若有多个记录符合条件，那么取总和。
		netoptReceiveTest.setRaoLteEarfcnSuccessSum(String.valueOf(raoLteEarfcnSuccessSum));
		
		for (EceptionCellLogPojo log : raoDLMap) {
			//序号75 “测试业务“属性为”绕点_下载“的记录的“弱覆盖率”,若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getDtWeakCoverRateMin() == null) {
				if(log.getWeakCoverRate()!=null && StringUtils.hasText(log.getWeakCoverRate())){
					netoptReceiveTest.setDtWeakCoverRateMin(String.format("%.4f",Float.valueOf(log.getWeakCoverRate())));
				}
			} else if (log.getWeakCoverRate() != null && StringUtils.hasText(log.getWeakCoverRate())
					&& Float.valueOf(netoptReceiveTest.getDtWeakCoverRateMin()) > Float.valueOf(log.getWeakCoverRate())) {
				netoptReceiveTest.setDtWeakCoverRateMin(String.format("%.4f",Float.valueOf(log.getWeakCoverRate())));
			}
			//序号119 “测试业务“属性为”绕点_下载“的记录的“弱覆盖率2”,若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getRaoDLSSRsrpSinr100min() == null) {
				if(log.getNrSSRsrpSinr100()!=null){
					netoptReceiveTest.setRaoDLSSRsrpSinr100min(String.format("%.2f",Float.valueOf(log.getNrSSRsrpSinr100())));
				}
			} else if (log.getNrSSRsrpSinr100() != null
					&& Float.valueOf(netoptReceiveTest.getRaoDLSSRsrpSinr100min()) > Float.valueOf(log.getNrSSRsrpSinr100())) {
				netoptReceiveTest.setRaoDLSSRsrpSinr100min(String.format("%.2f",Float.valueOf(log.getNrSSRsrpSinr100())));
			}
			//序号120 “测试业务“属性为”绕点_下载“的记录的“弱覆盖率3”,若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getRaoDLSSRsrpSinr105min() == null) {
				if(log.getNrSSRsrpSinr105()!=null){
					netoptReceiveTest.setRaoDLSSRsrpSinr105min(String.format("%.2f",Float.valueOf(log.getNrSSRsrpSinr105())));
				}
			} else if (log.getNrSSRsrpSinr105() != null
					&& Float.valueOf(netoptReceiveTest.getRaoDLSSRsrpSinr105min()) > Float.valueOf(log.getNrSSRsrpSinr105())) {
				netoptReceiveTest.setRaoDLSSRsrpSinr105min(String.format("%.2f",Float.valueOf(log.getNrSSRsrpSinr105())));
			}
			//序号121 “测试业务“属性为”绕点_下载“的记录的“弱覆盖率4”,若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getRaoDLSSRsrpSinr110min() == null) {
				if(log.getNrSSRsrpSinr110()!=null){
					netoptReceiveTest.setRaoDLSSRsrpSinr110min(String.format("%.2f",Float.valueOf(log.getNrSSRsrpSinr110())));
				}
			} else if (log.getNrSSRsrpSinr110() != null
					&& Float.valueOf(netoptReceiveTest.getRaoDLSSRsrpSinr110min()) > Float.valueOf(log.getNrSSRsrpSinr110())) {
				netoptReceiveTest.setRaoDLSSRsrpSinr110min(String.format("%.2f",Float.valueOf(log.getNrSSRsrpSinr110())));
			}
		}
	
		
		for (EceptionCellLogPojo log : ftpDLGoodBestMap) {
			//序号73 “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP下载“的记录的“NR下载吞吐率max”
			if (netoptReceiveTest.getFtpNrThrouDLRateMax() == null && log.getNrThrputDLMax()!=null) {
				netoptReceiveTest.setFtpNrThrouDLRateMax(String.format("%.2f",Float.valueOf(log.getNrThrputDLMax())));
			} else if (log.getNrThrputDLMax() != null
					&& Float.valueOf(netoptReceiveTest.getFtpNrThrouDLRateMax()) < Float.valueOf(log.getNrThrputDLMax())) {
				netoptReceiveTest.setFtpNrThrouDLRateMax(String.format("%.2f",Float.valueOf(log.getNrThrputDLMax())));
			}
			
			//序号83 “无线情况”属性为好点或极好点，“测试业务“属性为”FTP下载“记录的“NR PDSCH DMRS RSRP”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getNrPdschRsrpFtpDL() == null && log.getNrPdschDmrsRsrpAvg()!=null) {
				netoptReceiveTest.setNrPdschRsrpFtpDL(String.format("%.2f",log.getNrPdschDmrsRsrpAvg()));
			} else if (log.getNrPdschDmrsRsrpAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschRsrpFtpDL()) < log.getNrPdschDmrsRsrpAvg()) {
				netoptReceiveTest.setNrPdschRsrpFtpDL(String.format("%.2f",log.getNrPdschDmrsRsrpAvg()));
			}
			
			//序号84 “无线情况”属性为好点或极好点，“测试业务“属性为”FTP下载“的记录的“NR PDCCH DMRS SINR”，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getNrPdschSinrFtpDL() == null && log.getNrPdschDmrsSinrAvg()!=null) {
				netoptReceiveTest.setNrPdschSinrFtpDL(String.format("%.2f",log.getNrPdschDmrsSinrAvg()));
			} else if (log.getNrPdschDmrsSinrAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschSinrFtpDL()) > log.getNrPdschDmrsSinrAvg()) {
				netoptReceiveTest.setNrPdschSinrFtpDL(String.format("%.2f",log.getNrPdschDmrsSinrAvg()));
			}
			
			//序号96 “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getFtpGoodPdcpnrThrputDL() == null && log.getNrPdcpThrputDLAvg()!=null) {
				netoptReceiveTest.setFtpGoodPdcpnrThrputDL(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
			} else if (log.getNrPdcpThrputDLAvg() != null
					&& Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputDL()) < log.getNrPdcpThrputDLAvg()) {
				netoptReceiveTest.setFtpGoodPdcpnrThrputDL(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
			}
			
		}
		
		for (EceptionCellLogPojo log : ftpULGoodBestMap) {
			//序号74 “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP上传“的记录的“NR上传吞吐率max”
			if (netoptReceiveTest.getFtpNrThrouULRateMax() == null && log.getNrThrputULMax()!=null) {
				netoptReceiveTest.setFtpNrThrouULRateMax(String.format("%.2f",Float.valueOf(log.getNrThrputULMax())));
			} else if (log.getNrThrputULMax() != null
					&& Float.valueOf(netoptReceiveTest.getFtpNrThrouULRateMax()) < Float.valueOf(log.getNrThrputULMax())) {
				netoptReceiveTest.setFtpNrThrouULRateMax(String.format("%.2f",Float.valueOf(log.getNrThrputULMax())));
			}
			
			//序号85 “无线情况”属性为好点或极好点，“测试业务“属性为”FTP上传“记录的“NR PDSCH DMRS RSRP”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getNrPdschRsrpFtpUL() == null && log.getNrPdschDmrsRsrpAvg()!=null) {
				netoptReceiveTest.setNrPdschRsrpFtpUL(String.format("%.2f",log.getNrPdschDmrsRsrpAvg()));
			} else if (log.getNrPdschDmrsRsrpAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschRsrpFtpUL()) < log.getNrPdschDmrsRsrpAvg()) {
				netoptReceiveTest.setNrPdschRsrpFtpUL(String.format("%.2f",log.getNrPdschDmrsRsrpAvg()));
			}
			
			//序号86 “无线情况”属性为好点或极好点，“测试业务“属性为”FTP上传“的记录的“NR PDCCH DMRS SINR”，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getNrPdschSinrFtpUL() == null && log.getNrPdschDmrsSinrAvg()!=null) {
				netoptReceiveTest.setNrPdschSinrFtpUL(String.format("%.2f",log.getNrPdschDmrsSinrAvg()));
			} else if (log.getNrPdschDmrsSinrAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrPdschSinrFtpUL()) > log.getNrPdschDmrsSinrAvg()) {
				netoptReceiveTest.setNrPdschSinrFtpUL(String.format("%.2f",log.getNrPdschDmrsSinrAvg()));
			}
			
			//序号97 “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getFtpGoodPdcpnrThrputUL() == null && log.getNrPdcpThrputULAvg()!=null) {
				netoptReceiveTest.setFtpGoodPdcpnrThrputUL(String.format("%.2f",log.getNrPdcpThrputULAvg()));
			} else if (log.getNrPdcpThrputULAvg() != null
					&& Float.valueOf(netoptReceiveTest.getFtpGoodPdcpnrThrputUL()) < log.getNrPdcpThrputULAvg()) {
				netoptReceiveTest.setFtpGoodPdcpnrThrputUL(String.format("%.2f",log.getNrPdcpThrputULAvg()));
			}
		}
		
		//序号82 “测试业务”属性为“ping（32）测试”或者“ping（1500）测试”（注意，这里的数字是根据门限判定以后的结果）。“无线情况”属性为好点，的记录的“NR连接建立时延”，若有多个记录符合条件，那么取最小值
		for (EceptionCellLogPojo log : pingSumMap) {
			if (netoptReceiveTest.getNrConnectTimeDelay() == null && log.getNrConnectTimeDelay()!=null && !log.getNrConnectTimeDelay().equals("0")) {
				netoptReceiveTest.setNrConnectTimeDelay(String.format("%.2f",Float.valueOf(log.getNrConnectTimeDelay())));
			} else if (log.getNrConnectTimeDelay() != null  && !log.getNrConnectTimeDelay().equals("0")
					&& Float.valueOf(netoptReceiveTest.getNrConnectTimeDelay()) > Float.valueOf(log.getNrConnectTimeDelay())) {
				netoptReceiveTest.setNrConnectTimeDelay(String.format("%.2f",Float.valueOf(log.getNrConnectTimeDelay())));
			}
		}
		
		// “无线情况属性为“好点”，测试业务属性为”绕点_下载“
		for (EceptionCellLogPojo log : dtDLGoodMap) {
			// 65:“无线情况属性为“好点”，测试业务属性为”绕点_下载“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getDtNrSsbRsrp() == null && log.getNrRsrpavg() != null) {
				netoptReceiveTest.setDtNrSsbRsrp(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
			} else if (log.getNrRsrpavg() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbRsrp()) < Float.valueOf(log.getNrRsrpavg())) {
				netoptReceiveTest.setDtNrSsbRsrp(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
			}
			// 66:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“NR sinr”，，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getDtNrSsbSinr() == null && log.getNrSinr() != null) {
				netoptReceiveTest.setDtNrSsbSinr(String.format("%.2f",Float.valueOf(log.getNrSinr())));
			} else if (log.getNrSinr() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbSinr()) > Float.valueOf(log.getNrSinr())) {
				netoptReceiveTest.setDtNrSsbSinr(String.format("%.2f",Float.valueOf(log.getNrSinr())));
			}
			// 67:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“NR Throughput
			// DL”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getDtNrSsbThrouDL() == null && log.getNrThrputdl() != null) {
				netoptReceiveTest.setDtNrSsbThrouDL(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
			} else if (log.getNrThrputdl() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbThrouDL()) < Float.valueOf(log.getNrThrputdl())) {
				netoptReceiveTest.setDtNrSsbThrouDL(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
			}
			// 69:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“LTE rsrp”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getDtLteEarfcnRsrp() == null && log.getLteRsrp() != null) {
				netoptReceiveTest.setDtLteEarfcnRsrp(String.format("%.2f",Float.valueOf(log.getLteRsrp())));
			} else if (log.getLteRsrp() != null
					&& Float.valueOf(netoptReceiveTest.getDtLteEarfcnRsrp()) < Float.valueOf(log.getLteRsrp())) {
				netoptReceiveTest.setDtLteEarfcnRsrp(String.format("%.2f",Float.valueOf(log.getLteRsrp())));
			}
			// 70:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“LTE sinr”，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getDtLteEarfcnSinr() == null && log.getLteSinr() != null) {
				netoptReceiveTest.setDtLteEarfcnSinr(String.format("%.2f",Float.valueOf(log.getLteSinr())));
			} else if (log.getLteSinr() != null
					&& Float.valueOf(netoptReceiveTest.getDtLteEarfcnSinr()) > Float.valueOf(log.getLteSinr())) {
				netoptReceiveTest.setDtLteEarfcnSinr(String.format("%.2f",Float.valueOf(log.getLteSinr())));
			}
			// 87:“无线情况”属性为好点，“测试业务“属性为”绕点_下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getPdcpnrThrputDL() == null && log.getNrPdcpThrputDLAvg()!= null) {
				netoptReceiveTest.setPdcpnrThrputDL(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
			} else if (log.getNrPdcpThrputDLAvg() != null
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputDL()) < log.getNrPdcpThrputDLAvg()) {
				netoptReceiveTest.setPdcpnrThrputDL(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
			}
			// 89:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“下行低速率占比”，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getNrMacThrputDLMIN() == null) {
				if(log.getNrMacThrputDLvg()!=null){
					netoptReceiveTest.setNrMacThrputDLMIN(String.format("%.2f",log.getNrMacThrputDLvg()));
				}
			} else if (log.getNrMacThrputDLvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrMacThrputDLMIN()) > Float.valueOf(log.getNrMacThrputDLvg())) {
				netoptReceiveTest.setNrMacThrputDLMIN(String.format("%.2f",log.getNrMacThrputDLvg()));
			}
			// 92:“无线情况属性为“好点”，测试业务“属性为”绕点_下载“的记录的“RANK”，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getNrRankIndicatMIn() == null) {
				if(log.getNrRankIndicatAvg()!=null){
					netoptReceiveTest.setNrRankIndicatMIn(String.valueOf(Math.round(log.getNrRankIndicatAvg())));
				}
			} else if (log.getNrRankIndicatAvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrRankIndicatMIn()) > Float.valueOf(log.getNrRankIndicatAvg())) {
				netoptReceiveTest.setNrRankIndicatMIn(String.valueOf(Math.round(log.getNrRankIndicatAvg())));
			}
			
		}
		
		// “无线情况属性为“好点”，测试业务属性为”绕点_上传“
		for (EceptionCellLogPojo log : dtULGoodMap){
			// 68:“无线情况属性为“好点”，测试业务“属性为”绕点_上传“的记录的“NR ThroughputUL”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getDtNrSsbThrouUL() == null && log.getNrThrputul() != null ) {
				netoptReceiveTest.setDtNrSsbThrouUL(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
			} else if (log.getNrThrputul() != null
					&& Float.valueOf(netoptReceiveTest.getDtNrSsbThrouUL()) < Float.valueOf(log.getNrThrputul())) {
				netoptReceiveTest.setDtNrSsbThrouUL(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
			}
			// 88:“无线情况”属性为好点，“测试业务“属性为”绕点_上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
			if (netoptReceiveTest.getPdcpnrThrputUL() == null && log.getNrPdcpThrputULAvg()!= null) {
				netoptReceiveTest.setPdcpnrThrputUL(String.format("%.2f",log.getNrPdcpThrputULAvg()));
			} else if (log.getNrPdcpThrputULAvg() != null
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputUL()) < log.getNrPdcpThrputULAvg()) {
				netoptReceiveTest.setPdcpnrThrputUL(String.format("%.2f",log.getNrPdcpThrputULAvg()));
			}
			// 90:“无线情况”属性为好点，“测试业务“属性为”绕点_上传“的记录的“上行低速率占比”，若有多个记录符合条件，则取最低值
			if (netoptReceiveTest.getNrMacThrputULMin() == null && log.getNrMacThrputULvg()!= null) {
				netoptReceiveTest.setNrMacThrputULMin(String.format("%.2f",log.getNrMacThrputULvg()));
			} else if (log.getNrMacThrputULvg() != null
					&& Float.valueOf(netoptReceiveTest.getNrMacThrputULMin()) > log.getNrMacThrputULvg()) {
				netoptReceiveTest.setNrMacThrputULMin(String.format("%.2f",log.getNrMacThrputULvg()));
			}
		}

		for (EceptionCellLogPojo log : ftpDownGoodMap) {
			// ftp下载 rsrp sinr throughput 好中差
			// 好点或极好点ftp下载
			if (testTimeFtpDownGood != null && log.getNrTestdate().equals(testTimeFtpDownGood.toString())) {
				// 好点或极好点
				// 37:“测试业务属性为”FTP下载“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownRsrpGoog() == null && log.getNrRsrpavg() != null) {
					netoptReceiveTest.setFtpDownRsrpGoog(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpGoog()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpDownRsrpGoog(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				}
				// 38:“测试业务“属性为”FTP下载“的记录的“NR sinr”，，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownSinrGoog() == null  && log.getNrSinr() != null) {
					netoptReceiveTest.setFtpDownSinrGoog(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownSinrGoog()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpDownSinrGoog(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				}
				// 39:“测试业务“属性为”FTP下载“的记录的“NR Throughput DL”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownThrputGoog() == null && log.getNrThrputdl() != null) {
					netoptReceiveTest.setFtpDownThrputGoog(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
				} else if (log.getNrThrputdl() != null && Float
						.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) < Float.valueOf(log.getNrThrputdl())) {
					netoptReceiveTest.setFtpDownThrputGoog(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
				}
				// 117:“无线情况”属性为好点或者极好点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率max”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getGoodFtpDlPdcpnrDLThrmax() == null && log.getNrPdcpThrDLMaxval() != null) {
					netoptReceiveTest.setGoodFtpDlPdcpnrDLThrmax(String.format("%.2f",Float.valueOf(log.getNrPdcpThrDLMaxval())));
				} else if (log.getNrPdcpThrDLMaxval() != null && Float
						.valueOf(netoptReceiveTest.getGoodFtpDlPdcpnrDLThrmax()) < Float.valueOf(log.getNrPdcpThrDLMaxval())) {
					netoptReceiveTest.setGoodFtpDlPdcpnrDLThrmax(String.format("%.2f",Float.valueOf(log.getNrPdcpThrDLMaxval())));
				}
			}
		}

		for (EceptionCellLogPojo log : ftpDownMidMap) {
			// 中点ftp下载
			if (testTimeFtpDownMid != null && log.getNrTestdate().equals(testTimeFtpDownMid.toString())) {
				// 中点
				// 40:“测试业务属性为”FTP下载“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownRsrpMid() == null && log.getNrRsrpavg() != null) {
					netoptReceiveTest.setFtpDownRsrpMid(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpMid()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpDownRsrpMid(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				}
				// 41:“测试业务“属性为”FTP下载“的记录的“NR sinr”，，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownSinrMid() == null && log.getNrSinr() != null) {
					netoptReceiveTest.setFtpDownSinrMid(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownSinrMid()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpDownSinrMid(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				}
				// 42“测试业务“属性为”FTP下载“的记录的“NR Throughput DL”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownThrputMid() == null && log.getNrThrputdl() != null) {
					netoptReceiveTest.setFtpDownThrputMid(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
				} else if (log.getNrThrputdl() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) < Float
						.valueOf(log.getNrThrputdl())) {
					netoptReceiveTest.setFtpDownThrputMid(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
				}
				// 108 无线情况”属性为中点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getMidFtpDlPdcpnrDlThr() == null && log.getNrPdcpThrputDLAvg() != null) {
					netoptReceiveTest.setMidFtpDlPdcpnrDlThr(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
				} else if (log.getNrPdcpThrputDLAvg() != null 
						&& Float.valueOf(netoptReceiveTest.getMidFtpDlPdcpnrDlThr()) < Float.valueOf(log.getNrPdcpThrputDLAvg())) {
					netoptReceiveTest.setMidFtpDlPdcpnrDlThr(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
				}
				// 113 “无线情况”属性为中点，“测试业务“属性为”FTP下载“的记录的“NR下载吞吐率max”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getMidFtpDlNrDlThrMax() == null && log.getNrThrputDLMax() != null) {
					netoptReceiveTest.setMidFtpDlNrDlThrMax(String.format("%.2f",log.getNrThrputDLMax()));
				} else if (log.getNrThrputDLMax() != null 
						&& Float.valueOf(netoptReceiveTest.getMidFtpDlNrDlThrMax()) < Float.valueOf(log.getNrThrputDLMax())) {
					netoptReceiveTest.setMidFtpDlNrDlThrMax(String.format("%.2f",log.getNrThrputDLMax()));
				}
			}
		}
		for (EceptionCellLogPojo log : ftpDownBadMap) {
			// 差点ftp下载
			if (testTimeFtpDownBad != null && log.getNrTestdate().equals(testTimeFtpDownBad.toString())) {
				// 差点
				// 43:“测试业务属性为”FTP下载“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownRsrpBad() == null  && log.getNrRsrpavg() != null) {
					netoptReceiveTest.setFtpDownRsrpBad(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpBad()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpDownRsrpBad(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				}
				// 44:“测试业务“属性为”FTP下载“的记录的“NR sinr”，，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownSinrBad() == null && log.getNrSinr() != null) {
					netoptReceiveTest.setFtpDownSinrBad(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpDownSinrBad()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpDownSinrBad(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				}
				// 45:“测试业务“属性为”FTP下载“的记录的“NR Throughput DL”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpDownThrputBad() == null && log.getNrThrputdl() != null) {
					netoptReceiveTest.setFtpDownThrputBad(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
				} else if (log.getNrThrputdl() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) < Float
						.valueOf(log.getNrThrputdl())) {
					netoptReceiveTest.setFtpDownThrputBad(String.format("%.2f",Float.valueOf(log.getNrThrputdl())));
				}
				// 101:无线情况”属性为差点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpBadPdcpnrThrputDL() == null && log.getNrPdcpThrputDLAvg()!=null) {
					netoptReceiveTest.setFtpBadPdcpnrThrputDL(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
				} else if (log.getNrPdcpThrputDLAvg() != null && Float.valueOf(netoptReceiveTest.getFtpBadPdcpnrThrputDL()) < Float
						.valueOf(log.getNrPdcpThrputDLAvg())) {
					netoptReceiveTest.setFtpBadPdcpnrThrputDL(String.format("%.2f",log.getNrPdcpThrputDLAvg()));
				}
				// 115:“无线情况”属性为差点，“测试业务“属性为”FTP下载“的记录的“NR下载吞吐率max”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getBadFtpDlNrDlThrMax() == null && log.getNrThrputDLMax()!=null) {
					netoptReceiveTest.setBadFtpDlNrDlThrMax(String.format("%.2f",log.getNrThrputDLMax()));
				} else if (log.getNrThrputDLMax() != null && Float.valueOf(netoptReceiveTest.getBadFtpDlNrDlThrMax()) < Float
						.valueOf(log.getNrThrputDLMax())) {
					netoptReceiveTest.setBadFtpDlNrDlThrMax(String.format("%.2f",log.getNrThrputDLMax()));
				}
			}
		}
		for (EceptionCellLogPojo log : ftpUpGoodMap) {
			// 好点或极好点ftp上传
			if (testTimeFtpUpGood != null && log.getNrTestdate().equals(testTimeFtpUpGood.toString())) {
				// 好点或极好点
				// 46:“测试业务属性为”FTP下载“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpRsrpGoog() == null && log.getNrRsrpavg() != null) {
					netoptReceiveTest.setFtpUpRsrpGoog(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpRsrpGoog()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpUpRsrpGoog(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				}
				// 47:“测试业务“属性为”FTP下载“的记录的“NR sinr”，，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpSinrGoog() == null && log.getNrSinr() != null) {
					netoptReceiveTest.setFtpUpSinrGoog(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpSinrGoog()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpUpSinrGoog(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				}
				// 48:“测试业务“属性为”FTP下载“的记录的“NR Throughput uL”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpThrputGoog() == null && log.getNrThrputul() != null) {
					netoptReceiveTest.setFtpUpThrputGoog(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				} else if (log.getNrThrputul() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setFtpUpThrputGoog(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				}
				// 118:“无线情况”属性为好点或者极好点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率max”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getGoodFtpUlPdcpnrULThrmax() == null && log.getNrPdcpThrULMaxval() != null) {
					netoptReceiveTest.setGoodFtpUlPdcpnrULThrmax(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				} else if (log.getNrPdcpThrULMaxval() != null
						&& Float.valueOf(netoptReceiveTest.getGoodFtpUlPdcpnrULThrmax()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setGoodFtpUlPdcpnrULThrmax(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				}
			}
		}

		for (EceptionCellLogPojo log : ftpUpMidMap) {
			// 中点ftp上传
			if (testTimeFtpUpMid != null && log.getNrTestdate().equals(testTimeFtpUpMid.toString())) {
				// 中点
				// 49:“测试业务属性为”FTP下载“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpRsrpMid() == null && log.getNrRsrpavg() != null) {
					netoptReceiveTest.setFtpUpRsrpMid(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpRsrpMid()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpUpRsrpMid(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				}
				// 50:“无线情况”属性为中点，“测试业务“属性为”FTP上传“的记录的“NR sinr”，若有多个记录符合条件，则取最低值
				if (netoptReceiveTest.getFtpUpSinrMid() == null && log.getNrSinr() != null) {
					netoptReceiveTest.setFtpUpSinrMid(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpSinrMid()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpUpSinrMid(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				}
				// 51:“测试业务“属性为”FTP下载“的记录的“NR Throughput uL”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpThrputMid() == null && log.getNrThrputul() != null) {
					netoptReceiveTest.setFtpUpThrputMid(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				} else if (log.getNrThrputul() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setFtpUpThrputMid(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				}
				// 109 无线情况”属性为中点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getMidFtpUlPdcpnrUlThr() == null && log.getNrPdcpThrputULAvg() != null) {
					netoptReceiveTest.setMidFtpUlPdcpnrUlThr(String.format("%.2f",Float.valueOf(log.getNrPdcpThrputULAvg())));
				} else if (log.getNrPdcpThrputULAvg() != null 
						&& Float.valueOf(netoptReceiveTest.getMidFtpUlPdcpnrUlThr()) < Float.valueOf(log.getNrPdcpThrputULAvg())) {
					netoptReceiveTest.setMidFtpUlPdcpnrUlThr(String.format("%.2f",Float.valueOf(log.getNrPdcpThrputULAvg())));
				}
				// 114 “无线情况”属性为中点，“测试业务“属性为”FTP上传“的记录的“NR上传吞吐率max”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getMidFtpUlNrUlThrMax() == null && log.getNrThrputULMax() != null) {
					netoptReceiveTest.setMidFtpUlNrUlThrMax(String.format("%.2f",Float.valueOf(log.getNrThrputULMax())));
				} else if (log.getNrThrputULMax() != null 
						&& Float.valueOf(netoptReceiveTest.getMidFtpUlNrUlThrMax()) < Float.valueOf(log.getNrThrputULMax())) {
					netoptReceiveTest.setMidFtpUlNrUlThrMax(String.format("%.2f",Float.valueOf(log.getNrThrputULMax())));
				}
				
			}
		}
		for (EceptionCellLogPojo log : ftpUpBadMap) {
			// 差点ftp上传
			if (testTimeFtpUpBad != null && log.getNrTestdate().equals(testTimeFtpUpBad.toString())) {
				// 差点
				// 52:“测试业务属性为”FTP上传“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpRsrpBad() == null && log.getNrRsrpavg() != null) {
					netoptReceiveTest.setFtpUpRsrpBad(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				} else if (log.getNrRsrpavg() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpRsrpBad()) < Float.valueOf(log.getNrRsrpavg())) {
					netoptReceiveTest.setFtpUpRsrpBad(String.format("%.2f",Float.valueOf(log.getNrRsrpavg())));
				}
				// 53:“无线情况”属性为好点，“测试业务“属性为”FTP上传“的记录的“NR sinr”，若有多个记录符合条件，则取最低值
				if (netoptReceiveTest.getFtpUpSinrBad() == null && log.getNrSinr() != null) {
					netoptReceiveTest.setFtpUpSinrBad(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				} else if (log.getNrSinr() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpSinrBad()) > Float.valueOf(log.getNrSinr())) {
					netoptReceiveTest.setFtpUpSinrBad(String.format("%.2f",Float.valueOf(log.getNrSinr())));
				}
				// 54:“测试业务“属性为”FTP上传“的记录的“NR Throughput uL”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpUpThrputBad() == null && log.getNrThrputul() != null) {
					netoptReceiveTest.setFtpUpThrputBad(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				} else if (log.getNrThrputul() != null
						&& Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) < Float.valueOf(log.getNrThrputul())) {
					netoptReceiveTest.setFtpUpThrputBad(String.format("%.2f",Float.valueOf(log.getNrThrputul())));
				}
				
				// 102:无线情况”属性为差点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getFtpBadPdcpnrThrputUL() == null && log.getNrPdcpThrputULAvg()!=null) {
					netoptReceiveTest.setFtpBadPdcpnrThrputUL(String.format("%.2f",log.getNrPdcpThrputULAvg()));
				} else if (log.getNrPdcpThrputULAvg() != null && Float.valueOf(netoptReceiveTest.getFtpBadPdcpnrThrputUL()) < Float
						.valueOf(log.getNrPdcpThrputULAvg())) {
					netoptReceiveTest.setFtpBadPdcpnrThrputUL(String.format("%.2f",log.getNrPdcpThrputULAvg()));
				}
				// 116:“无线情况”属性为差点，“测试业务“属性为”FTP上传“的记录的“NR上传吞吐率max”，若有多个记录符合条件，则取最高值
				if (netoptReceiveTest.getBadFtpUlNrUlThrMax() == null && log.getNrThrputULMax()!=null) {
					netoptReceiveTest.setBadFtpUlNrUlThrMax(String.format("%.2f",log.getNrThrputULMax()));
				} else if (log.getNrThrputULMax() != null && Float.valueOf(netoptReceiveTest.getBadFtpUlNrUlThrMax()) < Float
						.valueOf(log.getNrThrputULMax())) {
					netoptReceiveTest.setBadFtpUlNrUlThrMax(String.format("%.2f",log.getNrThrputULMax()));
				}
			}
		}
		
		Integer csfbCallbackNum = 0;
		Float csfbCallbackSum = 0F;
		for(EceptionCellLogPojo log : dialVolteMap){
			if (testTimeDialVolte != null && log.getNrTestdate().equals(testTimeDialVolte.toString())) {
				// 100:“测试业务”属性为“volte测试”，“无线情况”属性为极好点或者好点，的记录的“CSFB回落时延”，若有多个记录，则取所有“CSFB回落失败次数”为0的记录的平均值(忽略0和空值)
				if (StringUtils.hasText(log.getCsfbCallbackTime()) && !log.getCsfbCallbackTime().equals("0") 
						&& (log.getCsfbCallbackDrop() != null && log.getCsfbCallbackDrop().equals("0"))) {
					csfbCallbackNum++;
					csfbCallbackSum = csfbCallbackSum + Float.valueOf(log.getCsfbCallbackTime());
				}
				
			}
		}
		netoptReceiveTest.setVolteCsfbCallbackTime(csfbCallbackNum == 0? null : String.format("%.2f", csfbCallbackSum/csfbCallbackNum));

		//性能验收报表
		if (menu == null || menu.getStationParamPojo() == null) {
			// 网优验收ftp下载
			if((netoptReceiveTest.getFtpDownThrputGoog() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) != 0 )
					|| (netoptReceiveTest.getFtpDownThrputMid() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) != 0)
					|| (netoptReceiveTest.getFtpDownThrputBad() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) != 0)){
				if ((netoptReceiveTest.getFtpDownThrputGoog() == null
						|| Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) == 0
						|| (netoptReceiveTest.getFtpDownThrputGoog() != null
								&& Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) > 300))
						&& (netoptReceiveTest.getFtpDownThrputMid() == null
								|| Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) == 0
								|| (netoptReceiveTest.getFtpDownThrputMid() != null
									&& Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) > 120))
						&& (netoptReceiveTest.getFtpDownThrputBad() == null
								|| Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) == 0
								|| (netoptReceiveTest.getFtpDownThrputBad() != null 
									&& Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) > 40))
					) {
					performanceReceive.setFtpDownload("是");
				} else {
					performanceReceive.setFtpDownload("否");
				}
			}else {
				performanceReceive.setFtpDownload("否");
			}
			// 网优验收ftp上传
			if((netoptReceiveTest.getFtpUpThrputGoog() != null && Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) != 0 )
					|| (netoptReceiveTest.getFtpUpThrputMid() != null && Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) != 0 )
					|| (netoptReceiveTest.getFtpUpThrputBad() != null && Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) != 0 )){
				if ((netoptReceiveTest.getFtpUpThrputGoog() == null
						|| Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) == 0
						|| (netoptReceiveTest.getFtpUpThrputGoog() != null
								&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) > 30))
						&& (netoptReceiveTest.getFtpUpThrputMid() == null
								|| Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) == 0
								|| (netoptReceiveTest.getFtpUpThrputMid() != null
									&& Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) > 20))
						&& (netoptReceiveTest.getFtpUpThrputBad() == null
								|| Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) == 0
								|| (netoptReceiveTest.getFtpUpThrputBad() != null 
									&& Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) > 10))
					) {
					performanceReceive.setFtpUpload("是");
				} else {
					performanceReceive.setFtpUpload("否");
				}
			}else {
				performanceReceive.setFtpUpload("否");
			}
			// 4G锚点连接建立成功率
			if ("0".equals(netoptReceiveTest.getLteEpsattachFailure())
					&& netoptReceiveTest.getLteEpsattachSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteEpsattachSuccess()) >= 10) {
				performanceReceive.setLteEpsattachSuccessRation("是");
			} else {
				performanceReceive.setLteEpsattachSuccessRation("否");
			}
			// 5G连接建立成功率（gNB添加成功率）
			if ("0".equals(netoptReceiveTest.getNrEdsaddFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= 10) {
				performanceReceive.setNrEdsaddSuccessRation("是");
			} else {
				performanceReceive.setNrEdsaddSuccessRation("否");
			}
			// 4G锚点切换成功率
			if ("0".equals(netoptReceiveTest.getLteInterfreqHandoverFailure())
					&& netoptReceiveTest.getLteInterfreqHandoverSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteInterfreqHandoverSuccess()) >= 1) {
				performanceReceive.setLteInterfreqHandoverSuccessRation("是");
			} else {
				performanceReceive.setLteInterfreqHandoverSuccessRation("否");
			}
			// 5G连接切换成功率（gNB变更成功率）
			if ("0".equals(netoptReceiveTest.getNrEdschangeFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= 1) {
				performanceReceive.setNrEdschangeSuccessRation("是");
			} else {
				performanceReceive.setNrEdschangeSuccessRation("否");
			}
			// 4G锚点RRC重建比例
			if ("0".equals(netoptReceiveTest.getRrcConnectionFailure())
					&& netoptReceiveTest.getRrcConnectionSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getRrcConnectionSuccess()) >= 10) {
				performanceReceive.setRrcConnectionSuccessRation("是");
			} else {
				performanceReceive.setRrcConnectionSuccessRation("否");
			}

			// 网优ping32
			if (StringUtils.hasText(netoptReceiveTest.getPing32SuccessRation())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Sum())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Success())
					&& Float.valueOf(netoptReceiveTest.getPing32Success()) >= 20
					&& Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) <= 15
					&& (Float.valueOf(netoptReceiveTest.getPing32Success())
							/ Float.valueOf(netoptReceiveTest.getPing32Sum())) >= 1) {
				performanceReceive.setPing32SuccessRation("是");
			} else {
				performanceReceive.setPing32SuccessRation("否");
			}
			// 网优ping1500
			if (StringUtils.hasText(netoptReceiveTest.getPing1500SuccessRation())// 36
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Sum())// 18
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Success())// 9
					&& Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) <= 17
					&& Float.valueOf(netoptReceiveTest.getPing1500Success()) >= 20
					&& (Float.valueOf(netoptReceiveTest.getPing1500Success())
							/ Float.valueOf(netoptReceiveTest.getPing1500Sum())) >= 1) {
				performanceReceive.setPing1500SuccessRation("是");
			} else {
				performanceReceive.setPing1500SuccessRation("否");
			}
			
			//序号：67/ping32时延是否通过/“35”小于15（ping32时延门限）该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPing32SuccessRation())
					&& Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) < 15){
				performanceReceive.setPing32TimeRlt67("是");
			}else{
				performanceReceive.setPing32TimeRlt67("否");
			}
			
			//序号：68/ping32成功率是否通过/“17”除以“8”大于等于1（ping32成功率门限）.且，“17”大于等于20（ping32次数门限），该指标为“是”，不然，该指标为“否”
			if(StringUtils.hasText(netoptReceiveTest.getPing32Success())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Sum())
					&& Float.valueOf(netoptReceiveTest.getPing32Sum())!=0
					&& (Float.valueOf(netoptReceiveTest.getPing32Success())
							/ Float.valueOf(netoptReceiveTest.getPing32Sum())*100) >= 100
					&& Float.valueOf(netoptReceiveTest.getPing32Success()) >= 20){
				performanceReceive.setPing32SucessRlt68("是");
			}else{
				performanceReceive.setPing32SucessRlt68("否");
			}
			
			//序号：69/ping1500时延是否通过/“36”小于17（ping1500时延门限），该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPing1500SuccessRation())
					&& Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) < 17){
				performanceReceive.setPing1500TimeRlt69("是");
			}else{
				performanceReceive.setPing1500TimeRlt69("否");
			}
			
			//序号：70/ping1500成功率是否通过/“18”除以“9”大于等于1（ping1500成功率门限）.，且，“18”大于等于20（ping1500次数门限），该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPing1500Success())
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Sum())
					&& Float.valueOf(netoptReceiveTest.getPing1500Sum())!=0
					&& (Float.valueOf(netoptReceiveTest.getPing1500Success())
							/ Float.valueOf(netoptReceiveTest.getPing1500Sum())*100) >= 100
					&& Float.valueOf(netoptReceiveTest.getPing1500Success()) >= 20){
				performanceReceive.setPing1500SucessRlt70("是");
			}else{
				performanceReceive.setPing1500SucessRlt70("否");
			}
			
			//序号：79/下行峰值速率是否通过/“39”大于300(下载好点门限)该指标为“是”，不然，该指标为“否”。
			if (StringUtils.hasText(netoptReceiveTest.getFtpDownThrputGoog())
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) > 300) {
				performanceReceive.setDownLoadRateRlt79("是");
			} else {
				performanceReceive.setDownLoadRateRlt79("否");
			}
			//序号：80/上行峰值速率是否通过/“48”大于30(上传好点门限，该指标为“是”，不然，该指标为“否”。
			if (StringUtils.hasText(netoptReceiveTest.getFtpUpThrputGoog())
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) > 30) {
				performanceReceive.setUpLoadRateRlt80("是");
			} else {
				performanceReceive.setUpLoadRateRlt80("否");
			}
			
			//序号：85/切换功能是否通过/“78”，大于“0.98”（5G切换成功率”门限），该指标为“是”，不然，该指标为“否”。
			if (StringUtils.hasText(netoptReceiveTest.getNrSwitchSuccessRate())
					&& Float.valueOf(netoptReceiveTest.getNrSwitchSuccessRate()) > 0.98) {
				performanceReceive.setNrSwitchSuccessRlt85("是");
			} else {
				performanceReceive.setNrSwitchSuccessRlt85("否");
			}
			
			//序号：88/“99”大于等于10，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getVolteCsfbCallbackSuccess())
					&& Float.valueOf(netoptReceiveTest.getVolteCsfbCallbackSuccess()) >= 10){
				performanceReceive.setCsfbFallbackSuccessRlt88("是");
			}else{
				performanceReceive.setCsfbFallbackSuccessRlt88("否");
			}
			
			//序号：91/“100”小于等于200，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getVolteCsfbCallbackTime())
					&& Float.valueOf(netoptReceiveTest.getVolteCsfbCallbackTime()) <= 200){
				performanceReceive.setCsfbFallbackTimeRlt91("是");
			}else{
				performanceReceive.setCsfbFallbackTimeRlt91("否");
			}
			
			//序号：97/“77”或“98”大于等于10，该指标为“是”，不然，该指标为“否
			if((netoptReceiveTest.getDialVonrSuccessSum() != null && Float.valueOf(netoptReceiveTest.getDialVonrSuccessSum()) >= 10)
					|| (netoptReceiveTest.getVolteCsfbSuccess() != null && Float.valueOf(netoptReceiveTest.getVolteCsfbSuccess()) >= 10)){
				performanceReceive.setVoiceBuildSuccessRlt97("是");
			}else{
				performanceReceive.setVoiceBuildSuccessRlt97("否");
			}
			
			// 序号：100/“105”大于等于10(attach尝试次数门限)，该指标为“是”，不然，该指标为“否
			if((netoptReceiveTest.getEndcGoodNrConnectSum() != null && Float.valueOf(netoptReceiveTest.getEndcGoodNrConnectSum()) >= 10)){
				performanceReceive.setEndcGoodNrConnectRlt100("是");
			}else{
				performanceReceive.setEndcGoodNrConnectRlt100("否");
			}
			
			// 序号：103/“118”大于220(上传好点峰值门限)，该指标为“是”，不然，该指标为“否”。
			if((netoptReceiveTest.getGoodFtpUlPdcpnrULThrmax() != null && Float.valueOf(netoptReceiveTest.getGoodFtpUlPdcpnrULThrmax()) > 220)){
				performanceReceive.setGoodFtpPdcpnrULRlt103("是");
			}else{
				performanceReceive.setGoodFtpPdcpnrULRlt103("否");
			}
			
			// 序号：104/“117”大于880(下载好点峰值门限)该指标为“是”，不然，该指标为“否”。
			if((netoptReceiveTest.getGoodFtpDlPdcpnrDLThrmax() != null && Float.valueOf(netoptReceiveTest.getGoodFtpDlPdcpnrDLThrmax()) > 880)){
				performanceReceive.setGoodFtpPdcpnrDLRlt104("是");
			}else{
				performanceReceive.setGoodFtpPdcpnrDLRlt104("否");
			}
			
			//序号：124 / “87”大于“(下载好点门限)-500”，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPdcpnrThrputDL())
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputDL()) > -500){
				performanceReceive.setRaoGoodPdcpnrDL124("是");
			}else{
				performanceReceive.setRaoGoodPdcpnrDL124("否");
			}
			
			//序号：127 / “88”大于“(上传好点门限)-55”，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPdcpnrThrputUL())
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputUL()) > -55){
				performanceReceive.setRaoGoodPdcpnrUL127("是");
			}else{
				performanceReceive.setRaoGoodPdcpnrUL127("否");
			}


		} else if (menu.getStationParamPojo() != null) {
			StationParamPojo spp = menu.getStationParamPojo();
			// 网优验收ftp下载
			if((netoptReceiveTest.getFtpDownThrputGoog() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) != 0 )
					|| (netoptReceiveTest.getFtpDownThrputMid() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) != 0)
					|| (netoptReceiveTest.getFtpDownThrputBad() != null && Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) != 0)){
				if ((netoptReceiveTest.getFtpDownThrputGoog() == null
						|| Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) == 0
						|| (netoptReceiveTest.getFtpDownThrputGoog() != null
								&& Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) > spp.getUpgradeGood()))
						&& (netoptReceiveTest.getFtpDownThrputMid() == null
								|| Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) == 0
								|| (netoptReceiveTest.getFtpDownThrputMid() != null
									&& Float.valueOf(netoptReceiveTest.getFtpDownThrputMid()) > spp.getUpgradeMid()))
						&& (netoptReceiveTest.getFtpDownThrputBad() == null
								|| Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) == 0
								|| (netoptReceiveTest.getFtpDownThrputBad() != null 
									&& Float.valueOf(netoptReceiveTest.getFtpDownThrputBad()) > spp.getUpgradeBad()))
					) {
					performanceReceive.setFtpDownload("是");
				}else{
					performanceReceive.setFtpDownload("否");
				}
			}else {
				performanceReceive.setFtpDownload("否");
			}
			// 网优验收ftp上传
			if((netoptReceiveTest.getFtpUpThrputGoog() != null && Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) != 0 )
					|| (netoptReceiveTest.getFtpUpThrputMid() != null && Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) != 0 )
					|| (netoptReceiveTest.getFtpUpThrputBad() != null && Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) != 0 )){
				if ((netoptReceiveTest.getFtpUpThrputGoog() == null
						|| Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) == 0
						|| (netoptReceiveTest.getFtpUpThrputGoog() != null
								&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) > spp.getUploadGood()))
						&& (netoptReceiveTest.getFtpUpThrputMid() == null
								|| Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) == 0
								|| (netoptReceiveTest.getFtpUpThrputMid() != null
									&& Float.valueOf(netoptReceiveTest.getFtpUpThrputMid()) > spp.getUploadMid()))
						&& (netoptReceiveTest.getFtpUpThrputBad() == null
								|| Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) == 0
								|| (netoptReceiveTest.getFtpUpThrputBad() != null 
									&& Float.valueOf(netoptReceiveTest.getFtpUpThrputBad()) > spp.getUploadBad()))
					) {
					performanceReceive.setFtpUpload("是");
				} else{
					performanceReceive.setFtpUpload("否");
				}
			}else {
				performanceReceive.setFtpUpload("否");
			}
			
			// 4G锚点连接建立成功率
			if ("0".equals(netoptReceiveTest.getLteEpsattachFailure())
					&& netoptReceiveTest.getLteEpsattachSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteEpsattachSuccess()) >= spp.getConnectNumber4g()) {
				performanceReceive.setLteEpsattachSuccessRation("是");
			} else {
				performanceReceive.setLteEpsattachSuccessRation("否");
			}
			// 5G连接建立成功率（gNB添加成功率）
			if ("0".equals(netoptReceiveTest.getNrEdsaddFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= spp.getConnectNumber5G()) {
				performanceReceive.setNrEdsaddSuccessRation("是");
			} else {
				performanceReceive.setNrEdsaddSuccessRation("否");
			}
			// 4G锚点切换成功率
			if ("0".equals(netoptReceiveTest.getLteInterfreqHandoverFailure())
					&& netoptReceiveTest.getLteInterfreqHandoverSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getLteInterfreqHandoverSuccess()) >= 1) {
				performanceReceive.setLteInterfreqHandoverSuccessRation("是");
			} else {
				performanceReceive.setLteInterfreqHandoverSuccessRation("否");
			}
			// 5G连接切换成功率（gNB变更成功率）
			if ("0".equals(netoptReceiveTest.getNrEdschangeFailure()) && netoptReceiveTest.getNrEdsaddSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getNrEdsaddSuccess()) >= 1) {
				performanceReceive.setNrEdschangeSuccessRation("是");
			} else {
				performanceReceive.setNrEdschangeSuccessRation("否");
			}
			// 4G锚点RRC重建比例
			if ("0".equals(netoptReceiveTest.getRrcConnectionFailure())
					&& netoptReceiveTest.getRrcConnectionSuccess() != null
					&& Integer.valueOf(netoptReceiveTest.getRrcConnectionSuccess()) >= spp.getConnectNumber4g()) {
				performanceReceive.setRrcConnectionSuccessRation("是");
			} else {
				performanceReceive.setRrcConnectionSuccessRation("否");
			}
			// 网优ping32
			if (StringUtils.hasText(netoptReceiveTest.getPing32SuccessRation())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Sum())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Success())
					&& Float.valueOf(netoptReceiveTest.getPing32Success()) >= spp.getPing32Number()
					&& Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) < spp.getPing32DelayTime()
					&& (Float.valueOf(netoptReceiveTest.getPing32Success())
							/ Float.valueOf(netoptReceiveTest.getPing32Sum())*100) >= spp.getPing32SuccessRatio()) {
				performanceReceive.setPing32SuccessRation("是");
			} else {
				performanceReceive.setPing32SuccessRation("否");
			}
			// 网优ping1500
			if (StringUtils.hasText(netoptReceiveTest.getPing1500SuccessRation())// 36
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Sum())// 18
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Success())// 9
					&& Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) < spp.getPing1500DelayTime()
					&& Float.valueOf(netoptReceiveTest.getPing1500Success()) >= spp.getPing1500Nember()
					&& (Float.valueOf(netoptReceiveTest.getPing1500Success())
							/ Float.valueOf(netoptReceiveTest.getPing1500Sum())*100) >= spp.getPing1500SuccessRatio()) {
				performanceReceive.setPing1500SuccessRation("是");
			} else {
				performanceReceive.setPing1500SuccessRation("否");
			}
			
			//序号：67/ping32时延是否通过/“35”小于15（ping32时延门限）该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPing32SuccessRation())
					&& Float.valueOf(netoptReceiveTest.getPing32SuccessRation()) < spp.getPing32DelayTime()){
				performanceReceive.setPing32TimeRlt67("是");
			}else{
				performanceReceive.setPing32TimeRlt67("否");
			}
			
			//序号：68/ping32成功率是否通过/“17”除以“8”大于等于1（ping32成功率门限）.且，“17”大于等于20（ping32次数门限），该指标为“是”，不然，该指标为“否”
			if(StringUtils.hasText(netoptReceiveTest.getPing32Success())
					&& StringUtils.hasText(netoptReceiveTest.getPing32Sum())
					&& Float.valueOf(netoptReceiveTest.getPing32Sum())!=0
					&& (Float.valueOf(netoptReceiveTest.getPing32Success())
							/ Float.valueOf(netoptReceiveTest.getPing32Sum())*100) >= spp.getPing32SuccessRatio()
					&& Float.valueOf(netoptReceiveTest.getPing32Success()) >= spp.getPing32Number()){
				performanceReceive.setPing32SucessRlt68("是");
			}else{
				performanceReceive.setPing32SucessRlt68("否");
			}
			
			//序号：69/ping1500时延是否通过/“36”小于17（ping1500时延门限），该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPing1500SuccessRation())
					&& Float.valueOf(netoptReceiveTest.getPing1500SuccessRation()) < spp.getPing1500DelayTime()){
				performanceReceive.setPing1500TimeRlt69("是");
			}else{
				performanceReceive.setPing1500TimeRlt69("否");
			}
			
			//序号：70/ping1500成功率是否通过/“18”除以“9”大于等于1（ping1500成功率门限）.，且，“18”大于等于20（ping1500次数门限），该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPing1500Success())
					&& StringUtils.hasText(netoptReceiveTest.getPing1500Sum())
					&& Float.valueOf(netoptReceiveTest.getPing1500Sum())!=0
					&& (Float.valueOf(netoptReceiveTest.getPing1500Success())
							/ Float.valueOf(netoptReceiveTest.getPing1500Sum())*100) >= spp.getPing1500SuccessRatio()
					&& Float.valueOf(netoptReceiveTest.getPing1500Success()) >= spp.getPing1500Nember()){
				performanceReceive.setPing1500SucessRlt70("是");
			}else{
				performanceReceive.setPing1500SucessRlt70("否");
			}
			
			//序号：79/下行峰值速率是否通过/“39”大于300(下载好点门限)该指标为“是”，不然，该指标为“否”。
			if (StringUtils.hasText(netoptReceiveTest.getFtpDownThrputGoog())
					&& Float.valueOf(netoptReceiveTest.getFtpDownThrputGoog()) > spp.getUpgradeGood()) {
				performanceReceive.setDownLoadRateRlt79("是");
			} else {
				performanceReceive.setDownLoadRateRlt79("否");
			}
			//序号：80/上行峰值速率是否通过/“48”大于30(上传好点门限，该指标为“是”，不然，该指标为“否”。
			if (StringUtils.hasText(netoptReceiveTest.getFtpUpThrputGoog())
					&& Float.valueOf(netoptReceiveTest.getFtpUpThrputGoog()) > spp.getUploadGood()) {
				performanceReceive.setUpLoadRateRlt80("是");
			} else {
				performanceReceive.setUpLoadRateRlt80("否");
			}
			
			//序号：85/切换功能是否通过/“78”，大于“0.98”（5G切换成功率”门限），该指标为“是”，不然，该指标为“否”。
			if (StringUtils.hasText(netoptReceiveTest.getNrSwitchSuccessRate())
					&& Float.valueOf(netoptReceiveTest.getNrSwitchSuccessRate()) >= spp.getChangeSuccessRatio5G()) {
				performanceReceive.setNrSwitchSuccessRlt85("是");
			} else {
				performanceReceive.setNrSwitchSuccessRlt85("否");
			}
			
			//序号：88/“99”大于等于10，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getVolteCsfbCallbackSuccess())
					&& Float.valueOf(netoptReceiveTest.getVolteCsfbCallbackSuccess()) >= spp.getEpsFallbackTestNum()){
				performanceReceive.setCsfbFallbackSuccessRlt88("是");
			}else{
				performanceReceive.setCsfbFallbackSuccessRlt88("否");
			}
			
			//序号：91/“100”小于等于200，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getVolteCsfbCallbackTime())
					&& Float.valueOf(netoptReceiveTest.getVolteCsfbCallbackTime()) <= spp.getEpsFallbackTimeDelay()){
				performanceReceive.setCsfbFallbackTimeRlt91("是");
			}else{
				performanceReceive.setCsfbFallbackTimeRlt91("否");
			}
			
			//序号：97/“77”或“98”大于等于10，该指标为“是”，不然，该指标为“否
			if((netoptReceiveTest.getDialVonrSuccessSum() != null && Float.valueOf(netoptReceiveTest.getDialVonrSuccessSum()) >= spp.getVonrTestNumber())
					|| (netoptReceiveTest.getVolteCsfbSuccess() != null && Float.valueOf(netoptReceiveTest.getVolteCsfbSuccess()) >= spp.getVonrTestNumber())){
				performanceReceive.setVoiceBuildSuccessRlt97("是");
			}else{
				performanceReceive.setVoiceBuildSuccessRlt97("否");
			}
			
			// 序号：100/“105”大于等于10(attach尝试次数门限)，该指标为“是”，不然，该指标为“否
			if((netoptReceiveTest.getEndcGoodNrConnectSum() != null && Float.valueOf(netoptReceiveTest.getEndcGoodNrConnectSum()) >= spp.getConnectNumber4g())){
				performanceReceive.setEndcGoodNrConnectRlt100("是");
			}else{
				performanceReceive.setEndcGoodNrConnectRlt100("否");
			}
			
			// 序号：103/“118”大于220(上传好点峰值门限)，该指标为“是”，不然，该指标为“否”。
			if((netoptReceiveTest.getGoodFtpUlPdcpnrULThrmax() != null && Float.valueOf(netoptReceiveTest.getGoodFtpUlPdcpnrULThrmax()) > spp.getUploadGoodApex())){
				performanceReceive.setGoodFtpPdcpnrULRlt103("是");
			}else{
				performanceReceive.setGoodFtpPdcpnrULRlt103("否");
			}
			
			// 序号：104/“117”大于880(下载好点峰值门限)该指标为“是”，不然，该指标为“否”。
			if((netoptReceiveTest.getGoodFtpDlPdcpnrDLThrmax() != null && Float.valueOf(netoptReceiveTest.getGoodFtpDlPdcpnrDLThrmax()) > spp.getDownloadGoodApex())){
				performanceReceive.setGoodFtpPdcpnrDLRlt104("是");
			}else{
				performanceReceive.setGoodFtpPdcpnrDLRlt104("否");
			}
			
			//序号：124 / “87”大于“(下载好点门限)-500”，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPdcpnrThrputDL())
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputDL()) > spp.getUpgradeGood()){
				performanceReceive.setRaoGoodPdcpnrDL124("是");
			}else{
				performanceReceive.setRaoGoodPdcpnrDL124("否");
			}
			
			//序号：127 / “88”大于“(上传好点门限)-55”，该指标为“是”，不然，该指标为“否”。
			if(StringUtils.hasText(netoptReceiveTest.getPdcpnrThrputUL())
					&& Float.valueOf(netoptReceiveTest.getPdcpnrThrputUL()) > spp.getUploadGood()){
				performanceReceive.setRaoGoodPdcpnrUL127("是");
			}else{
				performanceReceive.setRaoGoodPdcpnrUL127("否");
			}
			
		}
		
		// ping延迟汇总
		if (performanceReceive.getPing32SuccessRation().equals("是")
				&& performanceReceive.getPing1500SuccessRation().equals("是")) {
			performanceReceive.setPingDelay("是");
		} else {
			performanceReceive.setPingDelay("否");
		}

		// 4G锚点掉线率
		if ("0".equals(netoptReceiveTest.getDtLteFailSumDegree())) {
			performanceReceive.setLteEarfcnSuccessRation("是");
		} else {
			performanceReceive.setLteEarfcnSuccessRation("否");
		}
		// 5G掉线率
		if ("0".equals(netoptReceiveTest.getDtNrFailSumDegree())) {
			performanceReceive.setNrEarfcnSuccessRation("是");
		} else {
			performanceReceive.setNrEarfcnSuccessRation("否");
		}
		
		//语音业务呼叫建立成功率
		if (netoptReceiveTest.getDialVolteSuccessSum() != null && netoptReceiveTest.getDialVonrSuccessSum() != null 
				&&(Float.valueOf(netoptReceiveTest.getDialVolteSuccessSum()) + Float.valueOf(netoptReceiveTest.getDialVonrSuccessSum())) > 10) {
			performanceReceive.setDialVoSunccess51("是");
		} else {
			performanceReceive.setDialVoSunccess51("否");
		}
		
		//nr连接建立成功次数结果/“81”大于等于10。该指标为“是”，不然，该指标为“否”。
		if (netoptReceiveTest.getNrConnectSuccessSum() != null && Float.valueOf(netoptReceiveTest.getNrConnectSuccessSum()) >10 ) {
			performanceReceive.setNrConnectSuccess55("是");
		} else {
			performanceReceive.setNrConnectSuccess55("否");
		}
		
		//nr连接建立时延/“82”小于等于120。该指标为“是”，不然，该指标为“否”。
		if (netoptReceiveTest.getNrConnectTimeDelay() != null && Float.valueOf(netoptReceiveTest.getNrConnectTimeDelay()) >120 ) {
			performanceReceive.setNrConnectTimeDelay58("是");
		} else {
			performanceReceive.setNrConnectTimeDelay58("否");
		}
		
		//序号：61/RSRP好点是否通过/“37”大于-85，该指标为“是”，不然，该指标为“否”
		if(StringUtils.hasText(netoptReceiveTest.getFtpDownRsrpGoog())
				&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpGoog()) > -85){
			performanceReceive.setRsrpGoodPointRlt61("是");
		}else{
			performanceReceive.setRsrpGoodPointRlt61("否");
		}
		
		//序号：62/SINR好点是否通过/“38”大于15，该指标为“是”，不然，该指标为“否”
		if(StringUtils.hasText(netoptReceiveTest.getFtpDownSinrGoog())
				&& Float.valueOf(netoptReceiveTest.getFtpDownSinrGoog()) > 15){
			performanceReceive.setSinrGoodPointRlt62("是");
		}else{
			performanceReceive.setSinrGoodPointRlt62("否");
		}
		
		//序号：94/“22”等于0，且，“13”大于等于6。该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getNrEdschangeSuccess())
				&& Float.valueOf(netoptReceiveTest.getNrEdschangeSuccess()) >= 6
				&& Float.valueOf(netoptReceiveTest.getNrEdschangeFailure()) == 0){
			performanceReceive.setNrEdschangeGt6Rlt94("是");
		}else{
			performanceReceive.setNrEdschangeGt6Rlt94("否");
		}
		
		//序号：109/1，减去，网优性能验收表的,LocalCellid=1,“75”的结果，大于“0.92”，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getDtWeakCoverRateMin())
				&& (1- Float.valueOf(netoptReceiveTest.getDtWeakCoverRateMin())) > 0.92){
			performanceReceive.setCellWireCoverRate109("是");
		}else{
			performanceReceive.setCellWireCoverRate109("否");
		}
		
		//序号：112/1，减去，网优性能验收表的,LocalCellid=1,“119”的结果，大于“0.9”，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getRaoDLSSRsrpSinr100min())
				&& (1- Float.valueOf(netoptReceiveTest.getRaoDLSSRsrpSinr100min())) > 0.9){
			performanceReceive.setSsRsrpSinr100Rlt112("是");
		}else{
			performanceReceive.setSsRsrpSinr100Rlt112("否");
		}
		
		//序号：115/1，减去，网优性能验收表的,LocalCellid=1,“120”的结果，大于“0.9”，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getRaoDLSSRsrpSinr105min())
				&& (1- Float.valueOf(netoptReceiveTest.getRaoDLSSRsrpSinr105min())) > 0.9){
			performanceReceive.setSsRsrpSinr105Rlt115("是");
		}else{
			performanceReceive.setSsRsrpSinr105Rlt115("否");
		}
		
		//序号：118/1，减去，网优性能验收表的,LocalCellid=1,“121”的结果，大于“0.95”，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getRaoDLSSRsrpSinr110min())
				&& (1- Float.valueOf(netoptReceiveTest.getRaoDLSSRsrpSinr110min())) > 0.95){
			performanceReceive.setSsRsrpSinr110Rlt118("是");
		}else{
			performanceReceive.setSsRsrpSinr110Rlt118("否");
		}
		
		//序号：8 / “24”等于0，且，“15”大于等于1，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getLteEarfcnSuccess())
				&& Float.valueOf(netoptReceiveTest.getLteEarfcnSuccess()) >= 1
				&& Float.valueOf(netoptReceiveTest.getLteEarfcnFailure()) == 0){
			performanceReceive.setLteEarfcnSuccessRlt8("是");
		}else{
			performanceReceive.setLteEarfcnSuccessRlt8("否");
		}
		
		//序号：9 / “25”等于0，且，“16”大于等于5，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getNrEarfcnSuccess())
				&& Float.valueOf(netoptReceiveTest.getNrEarfcnSuccess()) >= 5
				&& Float.valueOf(netoptReceiveTest.getNrEarfcnFailure()) == 0){
			performanceReceive.setNrEarfcnSuccessRlt9("是");
		}else{
			performanceReceive.setNrEarfcnSuccessRlt9("否");
		}
		
		//序号：121 / “37”大于-80，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getFtpDownRsrpGoog())
				&& Float.valueOf(netoptReceiveTest.getFtpDownRsrpGoog()) > -80){
			performanceReceive.setRsrpGoodPointRlt121("是");
		}else{
			performanceReceive.setRsrpGoodPointRlt121("否");
		}
		
		//序号：130/ “122”（等于“95”，取网优“95”的值）大于等于1，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getNrConnectBuildSucess())
				&& Float.valueOf(netoptReceiveTest.getNrConnectBuildSucess()) >= 1){
			performanceReceive.setRaoNrConnectBuilRlt130("是");
		}else{
			performanceReceive.setRaoNrConnectBuilRlt130("否");
		}
		
		//序号：133/ “69”大于-80，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getDtLteEarfcnRsrp())
				&& Float.valueOf(netoptReceiveTest.getDtLteEarfcnRsrp()) > -80){
			performanceReceive.setRaoGoodLteRsrpRlt133("是");
		}else{
			performanceReceive.setRaoGoodLteRsrpRlt133("否");
		}
		
		//序号：134/ “70”大于15，该指标为“是”，不然，该指标为“否”。
		if(StringUtils.hasText(netoptReceiveTest.getDtLteEarfcnSinr())
				&& Float.valueOf(netoptReceiveTest.getDtLteEarfcnSinr()) > 15){
			performanceReceive.setRaoGoodLteSinrRlt134("是"); 
		}else{
			performanceReceive.setRaoGoodLteSinrRlt134("否");
		}
		
		netoptReceiveTest.setPlanParamPojo(plan);
		performanceReceive.setPlanParamPojo(plan);
		stationReportCreatService.createNetoptReceiveTest(netoptReceiveTest);
		stationReportCreatService.createPerformanceReceive(performanceReceive);
	}

	/**
	 * 计算小区参数规划
	 * 
	 * @author maxuancheng date:2020年2月22日 下午1:21:53
	 * @author lucheng date:2020年6月23日修改
	 * @param newDate
	 *            生成报告时间
	 * @param plan
	 *            日志数据
	 * @param cellLogList
	 *            详细指标数据
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
		CellParamCensus.setGnbid_p(plan.getGnbId());
		List<EceptionCellLogPojo> cellLog = new ArrayList<EceptionCellLogPojo>();
		Long testTime = null;
		// 找出“测试日期”距离生成单验报告日期最近，且“测试业务属性“不是”绕点”的记录
		for (EceptionCellLogPojo log : cellLogList) {
			if (log != null && StringUtils.hasText(log.getNrTestevent()) && !log.getNrTestevent().equals("绕点")) {
				if (testTime == null) {
					testTime = Long.valueOf(log.getNrTestdate());
					cellLog.add(log);
					// 如果测试时间更近
				} else if (Math.abs(testTime - Long.valueOf(newDate)) > Math
						.abs(Long.valueOf(log.getNrTestdate()) - Long.valueOf(newDate))) {
					testTime = Long.valueOf(log.getNrTestdate());
					cellLog.clear();
					cellLog.add(log);
					// 如果时间相等
				} else if (testTime.equals(Long.valueOf(log.getNrTestdate()))) {
					cellLog.add(log);
				}
			}
		}
		// 11~20的数据
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
			Map<String, Integer> gnobidMap = new HashMap<String, Integer>();
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
				// 频率
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
				// 设置频点
				//SA制式NR频点
				if (CellParamCensus.getFrequency114() == null) {
					if (ecp.getNrArfcnssbSA() == null || ecp.getNrArfcnssbSA().equals(0)) {

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
				// SA制式NR带宽
				if (CellParamCensus.getCellBroadband15() == null) {
					if (ecp.getNrBandwidthSA() == null || ecp.getNrBandwidthSA().equals(0)) {

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

//				Menu menu = terminalMenuService.get(plan.getCity());
//				if (menu == null || menu.getStationParamPojo() == null) {
//					// 频点
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
//					// 带宽
//					if (CellParamCensus.getCellBroadband15() == null) {
//						if (ecp.getNrBandwidth() == null || ecp.getNrBandwidth().equals(0)) {
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
//					if ("6".equals(spp.getSingleStationMOdelSelect())) { // 河南联通模板
//						//SA制式NR频点
//						if (CellParamCensus.getFrequency114() == null) {
//							if (ecp.getNrArfcnssbSA() == null || ecp.getNrArfcnssbSA().equals(0)) {
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
//						// SA制式NR带宽
//						if (CellParamCensus.getCellBroadband15() == null) {
//							if (ecp.getNrBandwidthSA() == null || ecp.getNrBandwidthSA().equals(0)) {
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
//						// 频点
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
//						// 带宽
//						if (CellParamCensus.getCellBroadband15() == null) {
//							if (ecp.getNrBandwidth() == null || ecp.getNrBandwidth().equals(0)) {
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
				//
				if (CellParamCensus.getGnbid_r() == null) {
					if (ecp.getNrGnbid() == null || ecp.getNrGnbid().equals("0")) {

					} else if (plan.getGnbId() == null
							|| !String.valueOf(plan.getGnbId()).equals(ecp.getNrGnbid())) {
						if (ecp.getNrGnbid() != null) {
							Integer f1 = gnobidMap.get(ecp.getNrGnbid());
							if (f1 == null) {
								gnobidMap.put(ecp.getNrGnbid(), 1);
							} else {
								gnobidMap.put(ecp.getNrGnbid(), f1 + 1);
							}
						}
					} else if (plan.getGnbId().equals(ecp.getNrGnbid())) {
						CellParamCensus.setGnbid_r(plan.getGnbId());
					}
				}
			}
			// 如果为空说明 没有相同数据 需要找到出现最多次的数据填充
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
			//
			if (CellParamCensus.getGnbid_r() == null) {
				Integer val = null;
				String key = null;
				for (Entry<String, Integer> entry : gnobidMap.entrySet()) {
					if (val == null && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					} else if (val != null && entry.getValue() > val && entry.getKey() != null) {
						val = entry.getValue();
						key = entry.getKey();
					}
				}
				if(StringUtils.hasText(key)){
					CellParamCensus.setGnbid_r(Long.valueOf(key));
				}
			}
		}

		// 20~30的数据
		if ((CellParamCensus.getCellId11() == null && CellParamCensus.getCellId1() == null)
				|| (CellParamCensus.getCellId11() != null && CellParamCensus.getCellId1() != null
						&& CellParamCensus.getCellId1().equals(CellParamCensus.getCellId11()))) {
			CellParamCensus.setCellIdStatus21("一致");
		} else {
			CellParamCensus.setCellIdStatus21("不一致");
		}

		if ((CellParamCensus.getPci2() == null && CellParamCensus.getPci12() == null)
				|| (CellParamCensus.getPci2() != null && CellParamCensus.getPci12() != null
						&& CellParamCensus.getPci2().equals(CellParamCensus.getPci12()))) {
			CellParamCensus.setPciStatus22("一致");
		} else {
			CellParamCensus.setPciStatus22("不一致");
		}

		if ((CellParamCensus.getNrFrequency13() == null && CellParamCensus.getNrFrequency3() == null)
				|| (CellParamCensus.getNrFrequency13() != null && CellParamCensus.getNrFrequency3() != null
						&& CellParamCensus.getNrFrequency13().equals(CellParamCensus.getNrFrequency3()))) {
			CellParamCensus.setNrFrequencyStatus23("一致");
		} else {
			CellParamCensus.setNrFrequencyStatus23("不一致");
		}

		if ((CellParamCensus.getFrequency14() == null && CellParamCensus.getFrequency114() == null)
				|| (CellParamCensus.getFrequency14() != null && CellParamCensus.getFrequency114() != null
						&& CellParamCensus.getFrequency14().equals(CellParamCensus.getFrequency114()))) {
			CellParamCensus.setFrequencyStatus24("一致");
		} else {
			CellParamCensus.setFrequencyStatus24("不一致");
		}

		if ((CellParamCensus.getCellBroadband15() == null && CellParamCensus.getCellBroadband5() == null)
				|| (CellParamCensus.getCellBroadband15() != null && CellParamCensus.getCellBroadband5() != null
						&& CellParamCensus.getCellBroadband15().equals(CellParamCensus.getCellBroadband5().replace("M", "")))) {
			CellParamCensus.setCellBroadbandStatus25("一致");
		} else {
			CellParamCensus.setCellBroadbandStatus25("不一致");
		}

		if ("FLEXIBLE".equals(CellParamCensus.getSpecialRatio16())
				|| (CellParamCensus.getSpecialRatio16() == null && CellParamCensus.getSpecialRatio6() == null)
				|| (CellParamCensus.getSpecialRatio16() != null && CellParamCensus.getSpecialRatio6() != null
						&& CellParamCensus.getSpecialRatio16().equals(CellParamCensus.getSpecialRatio6()))) {
			CellParamCensus.setSpecialRatioStatus26("一致");
		} else {
			CellParamCensus.setSpecialRatioStatus26("不一致");
		}

		if ("FLEXIBLE".equals(CellParamCensus.getUpAndDownRatio17())
				|| (CellParamCensus.getUpAndDownRatio17() == null && CellParamCensus.getUpAndDownRatio7() == null)
				|| (CellParamCensus.getUpAndDownRatio17() != null && CellParamCensus.getUpAndDownRatio7() != null
						&& CellParamCensus.getUpAndDownRatio17().equals(CellParamCensus.getUpAndDownRatio7()))) {
			CellParamCensus.setUpAndDownRatioStatus27("一致");
		} else {
			CellParamCensus.setUpAndDownRatioStatus27("不一致");
		}

		if ((CellParamCensus.getSsbWaveInterval18() == null && CellParamCensus.getSsbWaveInterval8() == null)
				|| (CellParamCensus.getSsbWaveInterval18() != null && CellParamCensus.getSsbWaveInterval8() != null
						&& CellParamCensus.getSsbWaveInterval18().equals(CellParamCensus.getSsbWaveInterval8()))) {
			CellParamCensus.setSsbWaveInterval28("一致");
		} else {
			CellParamCensus.setSsbWaveInterval28("不一致");
		}

		if ((CellParamCensus.getTac9() == null && CellParamCensus.getTac19() == null)
				|| (CellParamCensus.getTac9() != null && CellParamCensus.getTac19() != null
						&& CellParamCensus.getTac9().equals(CellParamCensus.getTac19()))) {
			CellParamCensus.setTacStatus29("一致");
		} else {
			CellParamCensus.setTacStatus29("不一致");
		}

		if ((CellParamCensus.getSsbSending20() == null && CellParamCensus.getSsbSending10() == null)
				|| (CellParamCensus.getSsbSending20() != null && CellParamCensus.getSsbSending10() != null
						&& CellParamCensus.getSsbSending20().equals(CellParamCensus.getSsbSending10()))) {
			CellParamCensus.setSsbSendingStatus30("一致");
		} else {
			CellParamCensus.setSsbSendingStatus30("不一致");
		}

		if ((CellParamCensus.getP_a_r() == null && CellParamCensus.getP_a_p() == null)
				|| (CellParamCensus.getP_a_r() != null && CellParamCensus.getP_a_p() != null
						&& CellParamCensus.getP_a_r().equals(CellParamCensus.getP_a_p()))) {
			CellParamCensus.setP_a_rlt("一致");
		} else {
			CellParamCensus.setP_a_rlt("不一致");
		}

		if ((CellParamCensus.getP_b_r() == null && CellParamCensus.getP_b_p() == null)
				|| (CellParamCensus.getP_b_r() != null && CellParamCensus.getP_b_p() != null
						&& CellParamCensus.getP_b_r().equals(CellParamCensus.getP_b_p()))) {
			CellParamCensus.setP_b_rlt("一致");
		} else {
			CellParamCensus.setP_b_rlt("不一致");
		}

		if ((CellParamCensus.getRootSeq_r() == null && CellParamCensus.getRootSeq_p() == null)
				|| (CellParamCensus.getRootSeq_r() != null && CellParamCensus.getRootSeq_p() != null
						&& CellParamCensus.getRootSeq_r().equals(CellParamCensus.getRootSeq_p()))) {
			CellParamCensus.setRootSeq_rlt("一致");
		} else {
			CellParamCensus.setRootSeq_rlt("不一致");
		}

		if ((CellParamCensus.getPdcchSymbol_r() == null && CellParamCensus.getPdcchSymbol_p() == null)
				|| (CellParamCensus.getPdcchSymbol_r() != null && CellParamCensus.getPdcchSymbol_p() != null
						&& CellParamCensus.getPdcchSymbol_r().equals(CellParamCensus.getPdcchSymbol_p()))) {
			CellParamCensus.setPdcchSymbol_rlt("一致");
		} else {
			CellParamCensus.setPdcchSymbol_rlt("不一致");
		}

		if ((CellParamCensus.getCellTRx_r() == null && CellParamCensus.getCellTRx_p() == null)
				|| (CellParamCensus.getCellTRx_r() != null && CellParamCensus.getCellTRx_p() != null
						&& CellParamCensus.getCellTRx_r().equals(CellParamCensus.getCellTRx_p()))) {
			CellParamCensus.setCellTRx_rlt("一致");
		} else {
			CellParamCensus.setCellTRx_rlt("不一致");
		}

		if ((CellParamCensus.getFrameStructure_r() == null && CellParamCensus.getFrameStructure_p() == null)
				|| (CellParamCensus.getFrameStructure_r() != null && CellParamCensus.getFrameStructure_p() != null
						&& CellParamCensus.getFrameStructure_r().equals(CellParamCensus.getFrameStructure_p()))) {
			CellParamCensus.setFrameStructure_rlt("一致");
		} else {
			CellParamCensus.setFrameStructure_rlt("不一致");
		}
		
		if ((CellParamCensus.getGnbid_r() == null && CellParamCensus.getGnbid_p() == null)
				|| (CellParamCensus.getGnbid_r() != null && CellParamCensus.getGnbid_p() != null
						&& CellParamCensus.getGnbid_r().equals(CellParamCensus.getGnbid_p()))) {
			CellParamCensus.setGnbid_rlt("一致");
		} else {
			CellParamCensus.setGnbid_rlt("不一致");
		}

		// 设置规划工参表中 无线参数一致性
		Menu menu = terminalMenuService.get(plan.getCity());
		if (menu == null || menu.getStationParamPojo() == null) {
			if (CellParamCensus.getPciStatus22().equals("不一致")) {
				plan.setWirelessParamStatus("PCI不一致");
			} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
					|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
				plan.setWirelessParamStatus("频率参数不一致");
			} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
				plan.setWirelessParamStatus("TAC不一致");
			} else {
				plan.setWirelessParamStatus("全部一致");
			}
		} else if (menu.getStationParamPojo() != null) {
			StationParamPojo spp = menu.getStationParamPojo();
			if ("1".equals(spp.getSingleStationMOdelSelect())) { // 山西模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("2".equals(spp.getSingleStationMOdelSelect())) { // 兰州模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")
						|| CellParamCensus.getSsbWaveInterval28().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("不一致")) {
					plan.setWirelessParamStatus("功率参数不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("3".equals(spp.getSingleStationMOdelSelect())) { // 贵阳模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("不一致")
						|| CellParamCensus.getP_a_rlt().equals("不一致") || CellParamCensus.getP_b_rlt().equals("不一致")) {
					plan.setWirelessParamStatus("功率参数不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("4".equals(spp.getSingleStationMOdelSelect())) { // 云南室外模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("5".equals(spp.getSingleStationMOdelSelect())) { // 云南室内模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")
						|| CellParamCensus.getSsbWaveInterval28().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("不一致")) {
					plan.setWirelessParamStatus("功率参数不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("6".equals(spp.getSingleStationMOdelSelect())) { // 河南联通模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("7".equals(spp.getSingleStationMOdelSelect()) || "8".equals(spp.getSingleStationMOdelSelect())) { // 贵州电信单验报告模板或贵州联通单验报告模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("不一致")) {
					plan.setWirelessParamStatus("功率参数不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("9".equals(spp.getSingleStationMOdelSelect()) || "10".equals(spp.getSingleStationMOdelSelect())) { // 辽宁电信单验报告模板或辽宁联通单验报告模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else if ("11".equals(spp.getSingleStationMOdelSelect()) || "12".equals(spp.getSingleStationMOdelSelect())
							|| "13".equals(spp.getSingleStationMOdelSelect()) 
							|| "14".equals(spp.getSingleStationMOdelSelect())
							|| "15".equals(spp.getSingleStationMOdelSelect())
							|| "16".equals(spp.getSingleStationMOdelSelect())) { // 内蒙电信、内蒙联通、陕西单验、湖北移动SA报告模板
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else if (CellParamCensus.getSsbSendingStatus30().equals("不一致")) {
					plan.setWirelessParamStatus("功率参数不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			} else {
				if (CellParamCensus.getPciStatus22().equals("不一致")) {
					plan.setWirelessParamStatus("PCI不一致");
				} else if (CellParamCensus.getFrequencyStatus24().equals("不一致")
						|| CellParamCensus.getNrFrequencyStatus23().equals("不一致")) {
					plan.setWirelessParamStatus("频率参数不一致");
				} else if (CellParamCensus.getTacStatus29().equals("不一致")) {
					plan.setWirelessParamStatus("TAC不一致");
				} else {
					plan.setWirelessParamStatus("全部一致");
				}
			}
		}

		CellParamCensus.setPlanParamPojo(plan);
		stationReportCreatService.createExceptionCellParam(CellParamCensus);
	}
	
	/**
	 * 补充性能验收测试表参数和基站工程表参数
	 * 
	 * @param plan
	 * @param cellLogList
	 */
	private void addContrastParam(PlanParamPojo plan) {
		
		//获取同站下其余localcellid的性能验收测试表和基站工程表参数
		List<StationNetoptReceiveTestPojo> netoptReceiveTestList = stationReportCreatService.getNetoptReceiveTest(plan.getSiteName());
		List<StationPerformanceReceivePojo> performanceReceiveList = stationReportCreatService.getPerformanceReceiveList(plan.getSiteName());
		List<StationBasicParamPojo> stationBasicParamList = stationReportCreatService.getStationBasicParamList(plan.getSiteName());
		
		StationNetoptReceiveTestPojo netoptReceiveTest = null; 
		StationPerformanceReceivePojo performanceReceive = null;
		StationBasicParamPojo stationBasicParamPojo = null;
		
		//网优验收表
		if(plan.getStationNetoptReceiveTestList()!=null && plan.getStationNetoptReceiveTestList().size()>0 ){
			netoptReceiveTest = plan.getStationNetoptReceiveTestList().get(0);
			
			//无线覆盖率
			Float wireCoverRate = null;
			Long nrStationSwitchSum = 0L;
			Long stationDtNrFailSumDegree = 0L;
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
				
				if(netoptReceiveTest.getNrStationSwitchSum()!=null){
					nrStationSwitchSum = nrStationSwitchSum + Long.valueOf(netoptReceiveTest.getNrStationSwitchSum());
				}
				
				if(netoptReceiveTest.getDtNrFailSumDegree()!=null){
					stationDtNrFailSumDegree = stationDtNrFailSumDegree + Long.valueOf(netoptReceiveTest.getDtNrFailSumDegree());
				}
			}
			netoptReceiveTest.setDtWireCoverRate(wireCoverRate == null ? null : String.format("%.4f",wireCoverRate));
			netoptReceiveTest.setNrStationSwitchSum(String.valueOf(nrStationSwitchSum));
			netoptReceiveTest.setStationDtNrFailSumDegree(String.valueOf(stationDtNrFailSumDegree));
			
			//切换成功
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
			netoptReceiveTest.setNrSwitchSuccessAvg(String.format("%.2f",nrSwitchSuccessAvg));
		}
		
		//性能验收测试表
		if(plan.getStationPerformanceReceiveList()!=null && plan.getStationPerformanceReceiveList().size()>0 ){
		    performanceReceive = plan.getStationPerformanceReceiveList().get(0);
			performanceReceive.setLteSwitchSuccessRation37("是");
			performanceReceive.setNrSwitchSuccessRation38("是");
			performanceReceive.setNrEarfcnSuccessRation42("是");
			performanceReceive.setLteEarfcnSuccessRation46("是");
			performanceReceive.setNrSwitchSuccessAvg50("否");
			//无线覆盖率
			if (netoptReceiveTest.getDtWireCoverRate()!=null && Float.valueOf(netoptReceiveTest.getDtWireCoverRate()) > 0.92) {
				performanceReceive.setWireCoverRate54("是");
			} else {
				performanceReceive.setWireCoverRate54("否");
			}
			for (StationPerformanceReceivePojo stationPerformanceReceivePojo : performanceReceiveList) {
				if(stationPerformanceReceivePojo.getLteInterfreqHandoverSuccessRation()==null 
						|| stationPerformanceReceivePojo.getLteInterfreqHandoverSuccessRation().equals("否")){
					performanceReceive.setLteSwitchSuccessRation37("否");
				}
				if(stationPerformanceReceivePojo.getNrEdschangeSuccessRation()==null 
						|| stationPerformanceReceivePojo.getNrEdschangeSuccessRation().equals("否")){
					performanceReceive.setNrSwitchSuccessRation38("否");
				}
				if(stationPerformanceReceivePojo.getNrEarfcnSuccessRation()==null 
						|| stationPerformanceReceivePojo.getNrEarfcnSuccessRation().equals("否")){
					performanceReceive.setNrEarfcnSuccessRation42("否");
				}
				if(stationPerformanceReceivePojo.getLteEarfcnSuccessRation() == null 
						|| stationPerformanceReceivePojo.getLteEarfcnSuccessRation().equals("否")){
					performanceReceive.setLteEarfcnSuccessRation46("否");
				}
				if(netoptReceiveTest.getNrSwitchSuccessAvg() != null && Float.valueOf(netoptReceiveTest.getNrSwitchSuccessAvg()) > 0.98){
					performanceReceive.setNrSwitchSuccessAvg50("是");;
				}
			}
		}
		
		//基站工程表参数
		if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
			stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
			stationBasicParamPojo.setHeightContrastBySitname81("一致");
			stationBasicParamPojo.setAauModelContrastBySitname("一致");
			stationBasicParamPojo.setAzimuthContrastBySitname("一致");
			stationBasicParamPojo.setTiltEContrastBySitname("一致");
			stationBasicParamPojo.setTiltMContrastBySitname("一致");
			stationBasicParamPojo.setHeightContrastByParam77("一致");
			for (StationBasicParamPojo basicParamPojo : stationBasicParamList) {
				if(basicParamPojo.getAdjustHeightContrast() == null 
						|| basicParamPojo.getAdjustHeightContrast().equals("不一致")){
					stationBasicParamPojo.setHeightContrastBySitname81("不一致");
				}
				if(basicParamPojo.getAdjustAauModelContrast() == null 
						|| basicParamPojo.getAdjustAauModelContrast().equals("不一致")){
					stationBasicParamPojo.setAauModelContrastBySitname("不一致");
				}
				if(basicParamPojo.getAdjustAzimuthContrast() == null 
						|| basicParamPojo.getAdjustAzimuthContrast().equals("不一致")){
					stationBasicParamPojo.setAzimuthContrastBySitname("不一致");
				}
				if(basicParamPojo.getAdjustTiltEContrast() == null 
						|| basicParamPojo.getAdjustTiltEContrast().equals("不一致")){
					stationBasicParamPojo.setTiltEContrastBySitname("不一致");
				}
				if(basicParamPojo.getAdjustTiltMContrast() == null 
						|| basicParamPojo.getAdjustTiltMContrast().equals("不一致")){
					stationBasicParamPojo.setTiltMContrastBySitname("不一致");
				}
				if(stationBasicParamPojo.getAdjustHeight() == null 
						||Math.abs(plan.getHeight()-stationBasicParamPojo.getAdjustHeight())>3){
					stationBasicParamPojo.setHeightContrastByParam77("不一致");
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

	public PlanParamPojo getPlanParamPojo() {
		return planParamPojo;
	}

	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(Integer testStatus) {
		this.testStatus = testStatus;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getTestService() {
		return testService;
	}

	public void setTestService(Integer testService) {
		this.testService = testService;
	}

	public String getSiteNames() {
		return siteNames;
	}

	public void setSiteNames(String siteNames) {
		this.siteNames = siteNames;
	}

	public String getCityNamesStr() {
		return cityNamesStr;
	}

	public void setCityNamesStr(String cityNamesStr) {
		this.cityNamesStr = cityNamesStr;
	}

}
