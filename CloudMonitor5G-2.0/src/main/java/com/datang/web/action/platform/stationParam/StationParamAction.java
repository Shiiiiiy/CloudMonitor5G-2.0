package com.datang.web.action.platform.stationParam;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.ClassUtil;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationReportTemplatePojo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.testLogItem.TestLogEtgTrailKpiPojo;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.stationParam.StationParamService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.platform.projectParam.ProjectParamInfoType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class StationParamAction extends PageAction implements ModelDriven<StationParamPojo>{

	private static final long serialVersionUID = 3144913319996457061L;
	
	private StationParamPojo stationParamPojo = new StationParamPojo();
	
	@Autowired
	private StationParamService stationParamService;
	
	@Autowired
	private IMenuManageService iMenuManageService;
	
	private Long sid;
	
	private Long cityId;//城市id
	
	private String modelType;//模板类型：单站还是反开3d
	
	private String modelSelect;//模板的选择
	
	public String getToStationParamJsp(){
		return ReturnType.LISTUI;
	}
	
	public String getStationParam(){
		Menu menu = iMenuManageService.getMenuById(cityId);
		ActionContext.getContext().getValueStack().set("menu", menu);
		return ReturnType.JSON;
	}
	
	public String getInitTemplate(){
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("templateType", 0);
		List<StationReportTemplatePojo> dzlist = stationParamService.findTemplateByParam(map1);
		List<HashMap<String, Object>> tlList = new ArrayList<HashMap<String, Object>>();
		for (StationReportTemplatePojo pojo : dzlist) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", pojo.getTemplateValue());
			map.put("name", pojo.getTemplateName());
			tlList.add(map);
		}
		
		Map<String,Object> map2 = new HashMap<String, Object>();
		map2.put("templateType", 1);
		List<StationReportTemplatePojo> oppo3dlist = stationParamService.findTemplateByParam(map2);
		List<HashMap<String, Object>> tlList2 = new ArrayList<HashMap<String, Object>>();
		for (StationReportTemplatePojo pojo : oppo3dlist) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", pojo.getTemplateValue());
			map.put("name", pojo.getTemplateName());
			tlList2.add(map);
		}

		ActionContext.getContext().getValueStack().set("dzList", tlList);
		ActionContext.getContext().getValueStack().set("oppo3dlist", tlList2);

		return ReturnType.JSON;
	}
	
	/**
	 * 保存修改参数
	 * @author maxuancheng
	 */
	public String saveStationParam(){
		try {
			Menu menu = iMenuManageService.getMenuById(cityId);
			if(sid == null || sid == -1){//不存在
				stationParamPojo.setMenu(menu);
				if(stationParamPojo.getTrailSamDisplay()==null){
					stationParamPojo.setTrailSamDisplay("0");
				}
				menu.setStationParamPojo(stationParamPojo);
				iMenuManageService.updateMenu(menu);
			}else{//如果已经存在
				StationParamPojo spp = stationParamService.findByMenuId(cityId);
				Menu menu2 = spp.getMenu();
				
				spp.setUpgradeBad(stationParamPojo.getUpgradeBad());
				spp.setUpgradeGood(stationParamPojo.getUpgradeGood());
				spp.setUpgradeMid(stationParamPojo.getUpgradeMid());
				
				spp.setUploadBad(stationParamPojo.getUploadBad());
				spp.setUploadGood(stationParamPojo.getUploadGood());
				spp.setUploadMid(stationParamPojo.getUploadMid());
				
				spp.setDownloadGoodApex(stationParamPojo.getDownloadGoodApex());
				spp.setUploadGoodApex(stationParamPojo.getUploadGoodApex());
				
				spp.setChangeSuccessRatio4G(stationParamPojo.getChangeSuccessRatio4G());
				spp.setChangeSuccessRatio5G(stationParamPojo.getChangeSuccessRatio5G());
				spp.setConnectNumber4g(stationParamPojo.getConnectNumber4g());
				spp.setConnectNumber5G(stationParamPojo.getConnectNumber5G());
				spp.setConnectSuccess5G(stationParamPojo.getConnectSuccess5G());
				spp.setChangeSuccessRatio5G(stationParamPojo.getChangeSuccessRatio5G());
				spp.setDisconectRatio4G(stationParamPojo.getDisconectRatio4G());
				spp.setDisconectRatio5G(stationParamPojo.getDisconectRatio5G());
				
				spp.setPing1500DelayTime(stationParamPojo.getPing1500DelayTime());
				spp.setPing1500Nember(stationParamPojo.getPing1500Nember());
				spp.setPing1500SuccessRatio(stationParamPojo.getPing1500SuccessRatio());
				spp.setPing32DelayTime(stationParamPojo.getPing32DelayTime());
				spp.setPing32Number(stationParamPojo.getPing32Number());
				spp.setPing32SuccessRatio(stationParamPojo.getPing32SuccessRatio());
				spp.setNrAccessDelayTime(stationParamPojo.getNrAccessDelayTime());
				
				spp.setRrcRatio4G(stationParamPojo.getRrcRatio4G());
				
				spp.setUpgradeVeryGood3D4G(stationParamPojo.getUpgradeVeryGood3D4G());
				spp.setUploadVeryGood3D4G(stationParamPojo.getUploadVeryGood3D4G());
				spp.setPing32DelayTime3D(stationParamPojo.getPing32DelayTime3D());
				spp.setPing32SuccessRatio3D(stationParamPojo.getPing32SuccessRatio3D());
				spp.setPing32Number3D(stationParamPojo.getPing32Number3D());
				
				spp.setVoiceAccessRatio(stationParamPojo.getVoiceAccessRatio());
				spp.setVoiceDropRatio(stationParamPojo.getVoiceDropRatio());
				spp.setVoiceAccesDelayTime(stationParamPojo.getVoiceAccesDelayTime());
				
				spp.setDtDownloadRate(stationParamPojo.getDtDownloadRate());
				spp.setDtUploadRate(stationParamPojo.getDtUploadRate());
				
				spp.setDtTestRsrp(stationParamPojo.getDtTestRsrp());
				spp.setDtTestSinr(stationParamPojo.getDtTestSinr());
				
				spp.setEpsFallbackTestNum(stationParamPojo.getEpsFallbackTestNum());
				spp.setEpsFallbackTimeDelay(stationParamPojo.getEpsFallbackTimeDelay());
				spp.setVonrTestNumber(stationParamPojo.getVonrTestNumber());
				
				spp.setEpsFallbackTestNum(stationParamPojo.getEpsFallbackTestNum());
				spp.setEpsFallbackTimeDelay(stationParamPojo.getEpsFallbackTimeDelay());
				spp.setVonrTestNumber(stationParamPojo.getVonrTestNumber());
				
				spp.setTrailSamDisplay(stationParamPojo.getTrailSamDisplay() == null? "0" : stationParamPojo.getTrailSamDisplay());
				
				spp.setSingleStationMOdelSelect(stationParamPojo.getSingleStationMOdelSelect());
				spp.setContrastOpen3DMOdelSelect(stationParamPojo.getContrastOpen3DMOdelSelect());
				
				iMenuManageService.updateMenu(menu2);
				//stationParamService.update(spp);
			}
			ActionContext.getContext().getValueStack().set("state", 200);
		} catch (Exception e) {
			ActionContext.getContext().getValueStack().set("state", 400);
			e.printStackTrace();
		}
		return ReturnType.JSON;
	}
	
	
	/**
	 * 下载模板
	 * @return
	 */
	public String downloadStationModel() {
		return "downloadTemp";
	}

	/**
	 * 下载模板
	 * 
	 * @return
	 */
	public InputStream getDownloadTemp(){
		try {
			InputStream is = null;
			if(modelType != null && modelSelect != null){
				if (modelType.equals("1")) {
					StationReportTemplatePojo findTemplateParam = stationParamService.findTemplateParam(0, Long.valueOf(modelSelect));
					ActionContext.getContext().put("fileName",
							new String(findTemplateParam.getTemplateExmpleDownload().getBytes(), "ISO8859-1"));
					is = ClassUtil.getResourceAsStream("templates/"+findTemplateParam.getTemplateExmpleName());
					
				} else if (modelType.equals("2")) {
					StationReportTemplatePojo findTemplateParam = stationParamService.findTemplateParam(1, Long.valueOf(modelSelect));
					ActionContext.getContext().put("fileName",
							new String(findTemplateParam.getTemplateExmpleDownload().getBytes(), "ISO8859-1"));
					is = ClassUtil.getResourceAsStream("templates/"+findTemplateParam.getTemplateExmpleName());
				}
			}
			return is;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StationParamPojo getModel() {
		return stationParamPojo;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		
		return null;
	}

	public StationParamPojo getStationParamPojo() {
		return stationParamPojo;
	}

	public void setStationParamPojo(StationParamPojo stationParamPojo) {
		this.stationParamPojo = stationParamPojo;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getModelType() {
		return modelType;
	}

	public String getModelSelect() {
		return modelSelect;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public void setModelSelect(String modelSelect) {
		this.modelSelect = modelSelect;
	}

}
