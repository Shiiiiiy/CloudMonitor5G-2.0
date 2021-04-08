package com.datang.web.action.oppositeOpen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
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
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.oppositeOpen.OppositeOpen3dReportCreateService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.stationParam.StationProspectParamService;
import com.datang.service.stationTest.StationReportCreatService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 反开3d报告生成Controller
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class OppositeOpen3dReportCreateAction extends PageAction implements ModelDriven<Plan4GParam> {

	private static final long serialVersionUID = 1L;
	
	private Plan4GParam plan4GParam = new Plan4GParam();
	
	private Long startTime;//测试开始时间
	
	private Long endTime;//测试结束时间
	
	private Integer testStatus;//测试状态
	
	private Integer testService;//测试业务
	
	private String ids;//多个小区id
	
	/**
	 * 基站名
	 */
	private String siteNames;
	
	private String cityNamesStr;
	
	@Autowired
	private OppositeOpen3dReportCreateService oppositeOpen3dReportCreateService;
	
	@Autowired
	private StationReportCreatService stationReportCreatService;
	
	@Autowired
	private TerminalMenuService terminalMenuService;
	
	/**
	 * 基站勘察服务
	 */
	@Autowired
	private StationProspectParamService stationProspectParamService;
	
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
	
	private String cellNames = "";
	
	private String errorMsg = "";
	
	public String getOppositeOpen3dReportCreateJsp(){
		
		return ReturnType.LISTUI;
	}

	@Override
	public Plan4GParam getModel() {
		
		return plan4GParam;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		//初始化查询时，需要判断用户的区域权限
		if(!StringUtils.hasText(plan4GParam.getRegion()) || "全部".equals(plan4GParam.getRegion())){
			if(cityNamesStr == null || !StringUtils.hasText(cityNamesStr)){
				cityNamesStr="";
				// 获取用户权限范围内的二级域menu
				List<TerminalMenu> cities = menuManageService.getCities();
				// 将二级域menu转化成terminalGroup
				List<TerminalGroup> groupsByMenus = groupService
						.getGroupsByMenus(cities);
				for(int i=0;i<groupsByMenus.size();i++){
					cityNamesStr = cityNamesStr+groupsByMenus.get(i).getName();
					if(i!=groupsByMenus.size()-1){
						cityNamesStr = cityNamesStr+",";
					}
				}
			}
		}
		pageList.putParam("cityStr", cityNamesStr);
		pageList.putParam("startTime", startTime);
		pageList.putParam("endTime", endTime);
		pageList.putParam("testStatus", testStatus);
		pageList.putParam("testService", testService);
		pageList.putParam("plan4GParam", plan4GParam);
		return oppositeOpen3dReportCreateService.doPageQuery(pageList);
	}
	
	/**
	 * 生成报告
	 * @author maxuancheng
	 * date:2020年3月13日 下午4:20:06
	 * @return
	 */
	public String createReport(){
		
		String[] idList = ids.split(",");
		List<Plan4GParam> planList = new ArrayList<Plan4GParam>();
		//生成报告日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String newDate = sdf.format(new Date());
		
		HashSet<String> siteNames = new HashSet<String>();
		List<Plan4GParam> p4pList = new ArrayList<Plan4GParam>();
		List<Long> idLong = new ArrayList<Long>();
		//第一步:修改工参生成报告日期数据
		for(String id : idList){
			Plan4GParam plan = oppositeOpen3dReportCreateService.find(Long.valueOf(id));
			List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellName(plan.getCellName());
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
		
		//删除已生成报告
		oppositeOpen3dReportCreateService.deleteReportByids(idLong);
		
		for (Plan4GParam plan : p4pList) {
			plan.setOppositeOpen3dPerformanceReceivePojo(null);
			plan.setOppositeOpen3dResultPojo(null);
			plan.setOppositeOpen3dWirelessPojo(null);
			plan.setStationBasicParamPojoList(null);
			Menu menu = terminalMenuService.get(plan.getRegion());	
			if (menu == null || menu.getStationParamPojo() == null) {
				mapModel.put(plan.getRegion(), "山西反开3d模板");
				plan.setOpposOpenTemplateSelect(1);
			}else{
				StationParamPojo spp = menu.getStationParamPojo();
				if("2".equals(spp.getContrastOpen3DMOdelSelect())) { // 兰州反开3d模板
					mapModel.put(plan.getRegion(), "兰州反开3d模板");
					plan.setOpposOpenTemplateSelect(2);
				}else { // 山西反开3d模板
					mapModel.put(plan.getRegion(), "山西反开3d模板");
					plan.setOpposOpenTemplateSelect(1);
				}
			}
			
			if(mapModel.get(plan.getRegion()).equals("山西反开3d模板") || mapModel.get(plan.getRegion()).equals("兰州反开3d模板")){
				//生成小区无线参数表
				createWirelessCellReport(Long.valueOf(newDate),siteNames, plan,mapModel.get(plan.getRegion()));
				
				//第三步:生成性能验收报告
				creatPerformanceReport(plan,newDate,mapModel.get(plan.getRegion()));

				//结果表
				creatResultReport(plan,newDate);
				
				// 站点规划参数和基站工程参数
				stationBaseParamSave(newDate, plan);
			}
		}
		
		//对比不同localCellid的结果，补充性能验收表、基站工程参数表、规划工参的值
		List<Plan4GParam> newPlanList = new ArrayList<Plan4GParam>();
		for (Long id : idLong) {
			Plan4GParam plan = oppositeOpen3dReportCreateService.find(Long.valueOf(id));
			newPlanList.add(plan);
		}
		for (Plan4GParam plan : newPlanList) {
			StationBasicParamPojo stationBasicParamPojo = null;
			StationProspectParamPojo stationProspectParamPojo = stationProspectParamService
					.findBySiteName(plan.getSiteName());

			if(mapModel.get(plan.getRegion()).equals("山西反开3d模板") || mapModel.get(plan.getRegion()).equals("兰州反开3d模板")){
				//补充基站工程表的部分参数
				addStationBasicParam(plan);
				//基站工程参数
				if(plan.getStationBasicParamPojoList()!=null && plan.getStationBasicParamPojoList().size()>0 ){
					stationBasicParamPojo = plan.getStationBasicParamPojoList().get(0);
				}
				if (stationProspectParamPojo != null) {
					if (mapModel.get(plan.getRegion()).equals("山西反开3d模板")) { // 贵阳模板
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getHeightContrastBySitname81() == null 
								|| stationBasicParamPojo.getHeightContrastBySitname81().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAauModelContrastBySitname() == null
								|| stationBasicParamPojo.getTiltEContrastBySitname() == null
								|| stationBasicParamPojo.getAzimuthContrastBySitname() == null
								||stationBasicParamPojo.getAauModelContrastBySitname().equals("不一致")
								|| stationBasicParamPojo.getTiltEContrastBySitname().equals("不一致")
								|| stationBasicParamPojo.getAzimuthContrastBySitname().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}else{
						if(stationBasicParamPojo.getAdjustLonContrast() == null
								|| stationBasicParamPojo.getAdjustLatContrast() == null
								|| stationBasicParamPojo.getAdjustLonContrast().equals("不一致") 
								|| stationBasicParamPojo.getAdjustLatContrast().equals("不一致") ){
							plan.setStationProspectRlt("经纬度不符");
						} else if(stationBasicParamPojo.getAdjustHeightContrast() == null 
								|| stationBasicParamPojo.getAdjustHeightContrast().equals("不一致")){
							plan.setStationProspectRlt("天线挂高不符");
						} else if(stationBasicParamPojo.getAdjustAauModelContrast() == null
								|| stationBasicParamPojo.getAdjustTiltEContrast() == null
								|| stationBasicParamPojo.getAdjustAzimuthContrast() == null
								||stationBasicParamPojo.getAdjustAauModelContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustTiltEContrast().equals("不一致")
								|| stationBasicParamPojo.getAdjustAzimuthContrast().equals("不一致")){
							plan.setStationProspectRlt("天馈参数不符");
						} else{
							plan.setStationProspectRlt("全部一致");
						}
					}
				}
				
			}	
			oppositeOpen3dReportCreateService.update(plan);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.hasText(cellNames)){
			map.put("cellNames", cellNames);
		}else{
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
	 * 补充基站工程表参数
	 * 
	 * @param plan
	 */
	private void addStationBasicParam(Plan4GParam plan) {
		//获取同站下其余localcellid的基站工程表参数
		List<StationBasicParamPojo> stationBasicParamList = stationReportCreatService.getStationBasicParamList(plan.getSiteName());
		
		StationBasicParamPojo stationBasicParamPojo = null;
		
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
						||Math.abs(plan.getHigh()-stationBasicParamPojo.getAdjustHeight())>3){
					stationBasicParamPojo.setHeightContrastByParam77("不一致");
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
	 * 站点规划参数和基站工程参数
	 * 
	 * @author lucheng date:2020年6月24日 下午3:02:31
	 * @param newDate 报告生成时间
	 * @param plan 日志数据
	 * @param cellLogList 日志详表
	 */
	private void stationBaseParamSave(String newDate, Plan4GParam plan) {

		// 获取对应的所有的localcellid从而判断属于几小区
//		List<PlanParamPojo> planParamPojoAscList = stationReportCreatService.getAllLocalCellId(plan.getSiteName());
		List<Plan4GParam> planParamPojoAscList = oppositeOpen3dReportCreateService.getAllLocalCellId(plan.getEnbId());

		StationBasicParamPojo stationBasicParamPojo = new StationBasicParamPojo();

		stationBasicParamPojo.setRegion(plan.getRegion());
		stationBasicParamPojo.setCellName(plan.getCellName());
		stationBasicParamPojo.setSiteName(plan.getSiteName());
		stationBasicParamPojo.setCellParamId(plan.getId());
		stationBasicParamPojo.setLocalCellId(plan.getLocalCellId().toString());

		// 保存站点规划参数
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
				stationBasicParamPojo.setAdjustTiltMContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustTiltMContrast("不一致");
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

			if (stationBasicParamPojo.getGnbId() != null && stationBasicParamPojo.getAdjustGnbId() != null
					&& stationBasicParamPojo.getGnbId().equals(stationBasicParamPojo.getAdjustGnbId())) {
				stationBasicParamPojo.setAdjustGnBIdContrast("一致");
			} else {
				stationBasicParamPojo.setAdjustGnBIdContrast("不一致");
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

		List<StationBasicParamPojo> list  = new ArrayList<StationBasicParamPojo>();
		list.add(stationBasicParamPojo);
		plan.setStationBasicParamPojoList(list);
		stationBasicParamPojo.setPlan4GParam(plan);
		stationReportCreatService.createStationBasicParamTest(stationBasicParamPojo);
	}
	
	/**
	 * 反开3d生成结果表
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
				oor.setCellInsert("成功");
			}else{
				oor.setCellInsert("不成功");
			}
			
			oor.setCellMeanPing(oop.getPing());
			if(oop.getChangeTest() != null && oop.getChangeTest() == 1){
				oor.setStationChange("成功");
			}else{
				oor.setStationChange("不成功");
			}
			
			oor.setMutual("通过");
			if(oop.getVolte() != null && oop.getVolte() == 1){
				oor.setVolte("通过");
			}else{
				oor.setVolte("不通过");					
			}
			
			if(oop.getCsfbFunction() != null && oop.getCsfbFunction() == 1){
				oor.setCsfb("通过");
			}else{					
				oor.setCsfb("不通过");
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
		//第三步:生成性能验收报告
		OppositeOpen3dPerformanceReceivePojo oop = new OppositeOpen3dPerformanceReceivePojo();
		
		oop.setCellId(p4p.getCellId());
		oop.setLocalCellId(p4p.getLocalCellId());
		oop.setCellName(p4p.getCellName());
		oop.setSiteName(p4p.getSiteName());
		//获取指标详表
		List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellName(p4p.getCellName());
		
		List<Plan4GParam> plan4GParamAscList = oppositeOpen3dReportCreateService.getAllLocalCellId(p4p.getEnbId());
		List<Integer> localCellidList = new ArrayList<Integer>();
		for (Plan4GParam plan4gParam : plan4GParamAscList) {
			localCellidList.add(plan4gParam.getLocalCellId());
		}
		Collections.sort(localCellidList);
		//寻找测试日期和生成报告时间最近,“无线情况”属性为极好点，“测试业务“属性为”FTP下载“的记录等类似数据
		//ftp下载
		Long timeFtpDownload = null;
		List<EceptionCellLogPojo> ecpFtpDownload = new ArrayList<EceptionCellLogPojo>();
		//ftp上传
		Long timeFtpUpload = null;
		List<EceptionCellLogPojo> ecpFtpUpload = new ArrayList<EceptionCellLogPojo>();
		//ENDC成功率测试
		Long timeEndc = null;
		List<EceptionCellLogPojo> ecpEndc = new ArrayList<EceptionCellLogPojo>();
		//ping（32）测试
		Long timePing = null;
		List<EceptionCellLogPojo> ecpPing = new ArrayList<EceptionCellLogPojo>();
		//volte测试
		Long timeVolte = null;
		List<EceptionCellLogPojo> ecpVolte = new ArrayList<EceptionCellLogPojo>();
		//CSFB测试
		Long timeCsfb = null;
		List<EceptionCellLogPojo> ecpCsfb = new ArrayList<EceptionCellLogPojo>();
		//绕点
		Long timeRaodian = null;
		List<EceptionCellLogPojo> ecpRaodian = new ArrayList<EceptionCellLogPojo>();
		long currentDate = Long.valueOf(newDate);
		for (EceptionCellLogPojo ecp : cellLogList) {
			Long testDate = ecp.getNrTestdate() == null ? null : Long.valueOf(ecp.getNrTestdate());
			if(testDate==null){
				continue;
			}
			//ftp下载
			if("FTP下载".equals(ecp.getNrTestevent())&& "极好点".equals(ecp.getNrWirelessstation())){
				if(timeFtpDownload == null || Math.abs(currentDate - timeFtpDownload) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeFtpDownload = testDate;
					ecpFtpDownload.add(ecp);
				}else if(Math.abs(currentDate - timeFtpDownload) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpFtpDownload.clear();
					timeFtpDownload = testDate;
					ecpFtpDownload.add(ecp);
				}
			}
			//ftp上传
			if("FTP上传".equals(ecp.getNrTestevent())&& "极好点".equals(ecp.getNrWirelessstation())){
				if(timeFtpUpload == null || Math.abs(currentDate - timeFtpUpload) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeFtpUpload = testDate;
					ecpFtpUpload.add(ecp);
				}else if(Math.abs(currentDate - timeFtpUpload) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpFtpUpload.clear();
					timeFtpUpload = testDate;
					ecpFtpUpload.add(ecp);
				}
			}
			//ENDC成功率测试
			if("ENDC成功率测试".equals(ecp.getNrTestevent())&& "极好点".equals(ecp.getNrWirelessstation())){
				if(timeEndc == null || Math.abs(currentDate - timeEndc) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeEndc = testDate;
					ecpEndc.add(ecp);
				}else if(Math.abs(currentDate - timeEndc) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpEndc.clear();
					timeEndc = testDate;
					ecpEndc.add(ecp);
				}
			}
			//ping（32）测试
			if (ecp.getNrTestevent().contains("ping")){
				String pingName = ecp.getNrTestevent().substring(ecp.getNrTestevent().indexOf("（")+1, ecp.getNrTestevent().indexOf("）")) ;
				if(StringUtils.hasText(pingName)){
					if (Float.valueOf(pingName)<200) { //ping32业务
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
			//volte测试
			if("volte测试".equals(ecp.getNrTestevent())&& "极好点".equals(ecp.getNrWirelessstation())){
				if(timeVolte == null || Math.abs(currentDate - timeVolte) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeVolte = testDate;
					ecpVolte.add(ecp);
				}else if(Math.abs(currentDate - timeVolte) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpVolte.clear();
					timeVolte = testDate;
					ecpVolte.add(ecp);
				}
			}
			//CSFB测试
			if("CSFB测试".equals(ecp.getNrTestevent())&& "极好点".equals(ecp.getNrWirelessstation())){
				if(timeCsfb == null || Math.abs(currentDate - timeCsfb) == Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					timeCsfb = testDate;
					ecpCsfb.add(ecp);
				}else if(Math.abs(currentDate - timeCsfb) > Math.abs(currentDate - Long.valueOf(ecp.getNrTestdate()))){
					ecpCsfb.clear();
					timeCsfb = testDate;
					ecpCsfb.add(ecp);
				}
			}
			//绕点
			if("绕点".equals(ecp.getNrTestevent())){
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
		//测试点导频信号质量:rsrp
		String lteRsrp = null; 
		//测试点导频信号质量:sinr
		String lteSinr = null; 
		//TCP下载吞吐量(60s均值)
		String tcpDownload = null; 
		//TCP上传吞吐量(60s均值)
		String tcpUpload = null; 
		//峰值上传速率(Mbps)
		String maxUpload = null;
		//峰值下载速率(Mbps)
		String maxDownload = null;
		//RRC Setup   0:不成功 ，1：成功
		Integer rrcSetup = null; 
		//ERAB Setup
		Integer erabSetup = null; 
		//Access
		Integer LteAccessTest = null; 
		//ping时延
		String pingTimeDelay = null; 
		//VOLTE功能测试(主叫volte成功次数和被叫volte成功次数)
		Integer volteTest = null; 
		//CSFB功能测试
		Integer csfbFunctionTest = null; 
		//CSFB接通时延（单端）
		String csfbConnect = null; 
		//切换测试（基站内） 0："切换成功"; 1："切换2小区失败";  2："切换3小区失败";  3："无法切换";
		Integer changeTest = null; 
		//主叫volte成功次数
		Integer dialingVolteNetTest = 0; 
		//被叫volte成功次数
		Integer calledVolteNetTest = null; 
		//主叫volte视频业务成功次数
		Integer dialingVolteVideoTest = null; 
		//被叫voltee视频业务成功次数
		Integer calledVolteVideoTest = null; 
		
//					DecimalFormat df = new DecimalFormat("#.000");
		//存储“测试业务“属性为”FTP下载“的记录的“LTE rsrp”、“LTE sinr”、“(LTE Throughput DL)” 已修改：TCP下载吞吐量(60s均值)/TCP上传吞吐量(60s均值)不需要再除以1024.后台单位已经是Mbps
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
		
		//存储“测试业务“属性为”FTP上传“的记录的“(LTE Throughput UL)”
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
		
		//“测试业务”属性为“ENDC成功率测试”。“无线情况”属性为极好点，的记录的“RRC建立成功次数”、“ERAB建立成功次数”、“4G锚点连接建立成功次数”，如果各自其中有值不等于0，则输出“成功”，如果等于0，则输出”不成功“
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
		//“测试业务”属性为“ping（32）测试”。相应记录的“ping时延“，若有多个值，就取最低值
		if(ecpPing != null && ecpPing.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpPing) {
				if(eceptionCellLogPojo.getNrPingresqtime() != null && !eceptionCellLogPojo.getNrPingresqtime().equals("")){
					if(pingTimeDelay == null || Float.valueOf(eceptionCellLogPojo.getNrPingresqtime()) < Float.valueOf(pingTimeDelay)){
						pingTimeDelay = (Float.valueOf(eceptionCellLogPojo.getNrPingresqtime())) + "";
					}
				}
			}
		}
		//“测试业务”属性为“volte测试”,“无线情况”属性为极好点,的记录
		if(ecpVolte != null && ecpVolte.size() > 0){
			for (EceptionCellLogPojo eceptionCellLogPojo : ecpVolte) {
				//“测试业务”属性为“volte测试”,“无线情况”属性为极好点,的记录的“volte成功次数”，如果其中有值不等于0，则输出“成功”，如果等于0，则输出”不成功“
				if(eceptionCellLogPojo.getVolteSuccess() != null && !eceptionCellLogPojo.getVolteSuccess().equals("")){
					if(volteTest == null){
						volteTest = 0;
					}
					if(!eceptionCellLogPojo.getVolteSuccess().equals("0")){
						volteTest = 1;
					}
				}
				//记录为“主叫volte成功次数”，注意，若该日志的“主叫volte掉线次数“不为0则该条记录的“主叫volte成功次数”不计入统计，然后对计入统计的“主叫volte成功次数”求和
				if(eceptionCellLogPojo.getVolteSuccess() != null && !eceptionCellLogPojo.getVolteSuccess().equals("") 
						&& eceptionCellLogPojo.getVolteDrop() != null && eceptionCellLogPojo.getVolteDrop().equals("0")){
					dialingVolteNetTest = dialingVolteNetTest + Integer.valueOf(eceptionCellLogPojo.getVolteSuccess());
				}
			}
		}
		//“测试业务”属性为“CSFB测试”,“无线情况”属性为极好点，的记录的“csfb成功次数”，如果其中有值不等于0，则输出“成功”，如果等于0，则输出不成功,“CSFB接通时延”，如果有多个值，就取最低值
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
		
		//“测试业务”属性为“绕点”的记录”
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
						//切换测试（基站内） 1："切换成功"; 2："切换2小区失败";  3："切换3小区失败";  4："无法切换";
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
						//切换测试（基站内） 1："切换成功"; 2："切换1小区失败";  3："切换3小区失败";  4："无法切换";
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
						//切换测试（基站内） 1："切换成功"; 2："切换1小区失败";  3："切换2小区失败";  4："无法切换";
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
		//以下5项因日志表里无对应字段，暂时默认为null
		oop.setVolte(volteTest);
		oop.setDialingVolteNetQuality(dialingVolteNetTest);
		oop.setCalledVolteNetQuality(calledVolteNetTest);
		oop.setDialingVolteVideoQuality(dialingVolteVideoTest);
		oop.setCalledVolteVideoQuality(calledVolteVideoTest); 
		
		//设置4G规划工参表中	不通过的测试项
		String noPassTestEvent = null;
		
		Menu menu = (Menu)terminalMenuService.get(p4p.getRegion());
		StationParamPojo spp = menu.getStationParamPojo();
		if (spp != null && spp.getId() != null) {
			Float upgradeVeryGood3D4G = spp.getUpgradeVeryGood3D4G();
			Float uploadVeryGood3D4G = spp.getUploadVeryGood3D4G();
			Float ping32DelayTime3D = spp.getPing32DelayTime3D();
			if(cityTemplate.equals("山西反开3d模板")){
				if (tcpDownload == null || tcpUpload == null || Float.valueOf(tcpDownload) < Float.valueOf(uploadVeryGood3D4G)
						 || Float.valueOf(tcpUpload) < Float.valueOf(upgradeVeryGood3D4G)) {
					noPassTestEvent = "FTP测试未通过";
				} else if (rrcSetup == null || rrcSetup == 0 
							|| erabSetup == null || erabSetup == 0
							|| LteAccessTest == null || LteAccessTest == 0
							|| changeTest == null || changeTest!=1) {
					noPassTestEvent = "接入切换测试未通过";
				} else if (pingTimeDelay == null
						|| Float.valueOf(pingTimeDelay) > Float.valueOf(ping32DelayTime3D)) {
					noPassTestEvent = "ping测试未通过";
				} else if (volteTest == null || volteTest == 0) {
					noPassTestEvent = "volte测试未通过";
				} else {
					noPassTestEvent = "全部通过";
				}
			} else if (cityTemplate.equals("兰州反开3d模板")) { // 兰州模板
				if (tcpDownload == null || tcpUpload == null || Float.valueOf(tcpDownload) < Float.valueOf(uploadVeryGood3D4G)
						 || Float.valueOf(tcpUpload) < Float.valueOf(upgradeVeryGood3D4G)) {
					noPassTestEvent = "FTP测试未通过";
				} else if (changeTest == null || changeTest!=1) {
					noPassTestEvent = "接入切换测试未通过";
				} else if (dialingVolteNetTest == null || dialingVolteNetTest < 1) {
					noPassTestEvent = "volte测试未通过";
				}  else {
					noPassTestEvent = "全部通过";
				}
			}
			
		} else {
			errorMsg = errorMsg+" 请检查区域是否设置好反开3d测试参数，并且都不为空!";
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
	 * 生成小区无线参数表
	 * @author maxuancheng
	 * date:2020年3月16日 下午2:53:10
	 * @param siteNames
	 * @param p4pList
	 */
	private void createWirelessCellReport(Long newDate,HashSet<String> siteNames, Plan4GParam p4p, String cityTemplate) {
		//第二步:生成报告
		OppositeOpen3dWirelessPojo oow = new OppositeOpen3dWirelessPojo();;
		
		//根据cellName获取单验日志详表数据
		List<EceptionCellLogPojo> cellLogList = stationReportCreatService.getExceptionCellLogOfCellName(p4p.getCellName());
		//寻找测试日期和生成报告时间最近且“测试业务属性“不是”绕点”的记录
		Long time = null;
		List<EceptionCellLogPojo> eceptionCellLogPojoList = new ArrayList<EceptionCellLogPojo>();
		for (EceptionCellLogPojo ecp : cellLogList) {
			if(ecp.getNrTestdate() != null 
					&& time == null 
					&& ecp.getNrTestevent() != null
					&& !"绕点".equals(ecp.getNrTestevent())){
				time = Long.valueOf(ecp.getNrTestdate());
				eceptionCellLogPojoList.add(ecp);
			}else if(ecp.getNrTestdate() != null 
					&& ecp.getNrTestevent() != null 
					&& time != null 
					&& Math.abs(newDate - time) > Math.abs(newDate - Long.valueOf(ecp.getNrTestdate()))
					&& !"绕点".equals(ecp.getNrTestevent())){
				time = Long.valueOf(ecp.getNrTestdate());
				eceptionCellLogPojoList.clear();
				eceptionCellLogPojoList.add(ecp);
			}else if(ecp.getNrTestdate() != null 
					&& time != null 
					&& time.equals(Long.valueOf(ecp.getNrTestdate()))
					&& ecp.getNrTestevent() != null 
					&& !"绕点".equals(ecp.getNrTestevent())){
				eceptionCellLogPojoList.add(ecp);
			}
		}
		
		//设置1-30
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
		//设置31-60 PCI 频段 频点 带宽 子帧配置　 特殊子帧配置 RS EPRE　 p-a p-b CELLID
		oow.setTac(p4p.getTac());
		//64:“10”的值，减1，除以65536
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
			//64:“测试业务属性“不是”绕点”的记录的“LTE TAC”，若有多个取值，那么，其中有值和“63”相同，则直接填充为“63”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			
			//31:“LTE PCI1”、“LTE PCI2”、“LTE PCI3”，若有多个取值，那么，其中有值和“1”相同，则直接填充为“1”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			
			//32:“LTE频率”，，若有多个取值，那么，其中有值和“2”相同，则直接填充为“2”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//33:“LTE 频点”，若有多个取值，那么，其中有值和“3”相同，则直接填充为“3”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//34:“LTE小区带宽”，若有多个取值，那么，其中有值和“4”相同，则直接填充为“4”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//35:“LTE子帧配置”，，若有多个取值，那么，其中有值和“5”相同，则直接填充为“5”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//36:“LTE特殊子帧配置”，若有多个取值，那么，其中有值和“6”相同，则直接填充为“6”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//37:“RS-ERPE”，若有多个取值，那么，其中有值和“7”相同，则直接填充为“7”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//38:“p-a”，，若有多个取值，那么，其中有值和“8”相同，则直接填充为“8”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//39:“p-b”，若有多个取值，那么，其中有值和“9”相同，则直接填充为“9”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			//40:“LTE CELLID1”、“LTE CELLID2”、“LTE CELLID3”，若有多个取值，那么，其中有值和“10”相同，则直接填充为“10”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			
			//102:“LocalCellId”为1、2、3的 “pdcch符号数”字段，若有多个取值，那么，其中有值和“99”相同，则直接填充为“99”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
			
			//111:“LocalCellId”为1、2、3的 “跟序列码”字段，若有多个取值，那么，其中有值和“108”相同，则直接填充为“108”的值，否则，输出出现次数最多的值（忽略0值和空值）
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
		
		//如果有没赋值的    PCI 频段 频点 带宽 子帧配置　 特殊子帧配置 RS EPRE　 p-a p-b CELLID pdcch符号数  跟序列码
		//其中有值和“XX”相同，则直接填充为“XX”的值，否则，输出出现次数最多的值
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
		
		//结论  PCI 频段 频点 带宽 子帧配置　 特殊子帧配置 RS EPRE　 p-a p-b CELLID  pdcch符号数  跟序列码
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
		
		// 设置规划工参表中 无线参数一致性
		if(cityTemplate.equals("山西反开3d模板")){
			if (oow.getPciContrast()==0) {
				p4p.setWirelessParamStatus("PCI不一致");
			} else if (oow.getFrequencyDlContrast()==0 || oow.getEarfcnContrast()==0 || oow.getBroadBandContrast()==0) {
				p4p.setWirelessParamStatus("频率参数不一致");
			} else if (oow.getTacContrast()==0) {
				p4p.setWirelessParamStatus("TAC不一致");
			} else if (oow.getP_aContrast()==0 || oow.getP_bContrast()==0) {
				p4p.setWirelessParamStatus("功率参数不一致");
			} else {
				p4p.setWirelessParamStatus("全部一致");
			}
		} else if (cityTemplate.equals("兰州反开3d模板")) { // 兰州模板
			if (oow.getPciContrast()==0) {
				p4p.setWirelessParamStatus("PCI不一致");
			} else if (oow.getFrequencyDlContrast()==0 || oow.getEarfcnContrast()==0 || oow.getBroadBandContrast()==0) {
				p4p.setWirelessParamStatus("频率参数不一致");
			} else if (oow.getTacContrast()==0) {
				p4p.setWirelessParamStatus("TAC不一致");
			} else if (oow.getP_aContrast()==0 || oow.getP_bContrast()==0 || oow.getRs_epreContrast()==0) {
				p4p.setWirelessParamStatus("功率参数不一致");
			} else {
				p4p.setWirelessParamStatus("全部一致");
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



	public Plan4GParam getPlan4GParam() {
		return plan4GParam;
	}

	public void setPlan4GParam(Plan4GParam plan4gParam) {
		plan4GParam = plan4gParam;
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

	public OppositeOpen3dReportCreateService getOppositeOpen3dReportCreateService() {
		return oppositeOpen3dReportCreateService;
	}

	public void setOppositeOpen3dReportCreateService(OppositeOpen3dReportCreateService oppositeOpen3dReportCreateService) {
		this.oppositeOpen3dReportCreateService = oppositeOpen3dReportCreateService;
	}

	public String getCityNamesStr() {
		return cityNamesStr;
	}

	public void setCityNamesStr(String cityNamesStr) {
		this.cityNamesStr = cityNamesStr;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
