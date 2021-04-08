/**
 * 
 */
package com.datang.web.action.action5g.analysisThreshold;

import com.datang.common.util.StringUtils;
import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.domain.platform.analysisThreshold.MapTrailThreshold;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.web.action.ReturnType;
import com.mchange.v2.collection.MapEntry;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 轨迹图参数设置
 * 
 * @author lucheng
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class MapTrailLegendAction extends ActionSupport {
	@Autowired
	private IVolteAnalysisThresholdService analysisThresholdService;
	
	/**
	 * 区域组服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;

	@Autowired
	private TerminalMenuService terminalMenuService;

	private Map<String, VolteAnalysisThreshold> thresholdMap;
	
	private Map<String, String> legendMap = new HashMap<String, String>();
	
	/**
	 * 市级区域ID
	 */
	private Long cityId;
	
	private String cityName;
	
	/**
	 * 地图的图例对应类型
	 */
	private String colorMapType;
	
	public String listUI(){
		return ReturnType.LISTUI;
	}

	
	/**
	 * 进入覆盖指标地图显示区间设置页面
	 * 
	 * @return
	 */
	public String displayMapRangeParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		if (null != cityId) {
			Map<String,Object> paramSelect = new HashMap<String, Object>();
			paramSelect.put("subjectType", colorMapType);
			//查询非默认参数
			paramSelect.put("isDefault", "0");
			paramSelect.put("cityId", cityId);
			List<MapTrailThreshold> all = analysisThresholdService.queryTrailByMapParam(paramSelect);
			
			//区域没有绑定参数情况下，查询默认参数
			if(all == null || all.size()==0){
				Map<String,Object> param2 = new HashMap<String, Object>();
				param2.put("subjectType", colorMapType);
				//查询默认参数,isDefault为1
				param2.put("isDefault", "1");
				all = analysisThresholdService.queryTrailByMapParam(param2);
			}

			List<List<Map<String, String>>> mapParam = new ArrayList<>();

			for (MapTrailThreshold mapTrailThreshold : all) {
				String thresholdType = mapTrailThreshold.getThresholdType();
				if (null == thresholdType || 0 == thresholdType.trim().length()) {
					continue;
				}
				switch (thresholdType.trim()) {
				case VolteAnalysisThresholdTypeConstant.COLOR_MAP_ETG_TRAL:
					String nameEn = mapTrailThreshold.getNameEn();
					String nameCn = mapTrailThreshold.getNameCh();
					String currentThreshold = mapTrailThreshold
							.getCurrentThreshold();
					List<Map<String, String>> mapParamMap = new ArrayList<>();
					if (null != nameEn
							&& (nameEn.equals("rsrpIndex")
									|| nameEn.equals("sinrIndex")
									|| nameEn.equals("beamIndex")
									|| nameEn.equals("macthrputdlIndex")
									|| nameEn.equals("macthrputulIndex"))) {
						mapParam.add(mapParamMap);
						if (StringUtils.hasText(currentThreshold)) {
							String[] split = currentThreshold.split("@");
							for (int i = 0; i < split.length; i++) {
								Map<String, String> indexMap;
								try {
									indexMap = mapParamMap.get(i);
								} catch (Exception e) {
									indexMap = new HashMap<>();
									mapParamMap.add(i, indexMap);
								}
								String range1 = split[i].substring(0, 1);
								String range2 = split[i].substring(split[i].length() - 1, split[i].length());
								List<String> split2 = Arrays.asList(split[i].substring(1,
										split[i].length() - 1).split(",",-1));
								String begin = split2.get(0);
								String end = split2.get(1);
								indexMap.put("begin", begin);
								indexMap.put("end", end);
								indexMap.put("nameEh", nameEn.replace("Index", ""));
								indexMap.put("nameCh", nameCn);
								indexMap.put("range1", range1);
								indexMap.put("range2", range2);
							}
						}
					}
					if (null != nameEn
							&& (nameEn.equals("rsrpColor")
									|| nameEn.equals("sinrColor") 
									|| nameEn.equals("beamColor")
									|| nameEn.equals("macthrputdlColor")
									|| nameEn.equals("macthrputulColor"))) {
						if (StringUtils.hasText(currentThreshold)) {
							String[] split = currentThreshold.split(",");
							for (int i = 0; i < split.length; i++) {
								for (List<Map<String, String>> mapParamMap_ : mapParam) {
									if (mapParamMap_.get(0).get("nameEh")
											.equals(nameEn.replace("Color", ""))) {
										mapParamMap = mapParamMap_;
									}
								}
								Map<String, String> colorMap;
								try {
									colorMap = mapParamMap.get(i);
								} catch (Exception e) {
									colorMap = new HashMap<>();
									mapParamMap.add(i, colorMap);
								}
								String color = split[i];
								colorMap.put("color", color);
								colorMap.put("nameEh", nameEn.replace("Color", ""));
							}
						}
					}
					break;
				case VolteAnalysisThresholdTypeConstant.PCI_MAP_COLOR:
					String nameCnPci = mapTrailThreshold.getNameCh();
					String nameEnPci = mapTrailThreshold.getNameEn();
					String currentThresholdPci = mapTrailThreshold
							.getCurrentThreshold();
					
					if( null != nameCnPci) {
						if (StringUtils.hasText(currentThresholdPci)) {
							String[] split = currentThresholdPci.split(",");
							List<Map<String, String>> pciMapParam = new ArrayList<>();
							
							for (int i = 0; i < split.length; i++) {
								Map<String, String> pciMap = new HashMap<>();
								pciMap.put("color", split[i]);
								pciMap.put("index", String.valueOf(i));
								pciMap.put("scope", "第"+(i+1)+"个pci");
								pciMap.put("nameEh", nameEnPci.replace("Color", ""));
								pciMap.put("nameCh", nameCnPci);
								pciMapParam.add(pciMap);
							}
							//取nameEn去除Color作为名称
							mapParam.add(pciMapParam);
						}
					}
					break;
				default:
					break;
				}
			}
			valueStack.set("cityId", cityId);
			valueStack.set("colorMapType", colorMapType);
			valueStack.set(VolteAnalysisThresholdTypeConstant.MAP_PARAM, mapParam);


			return "displayMapRangeParamListUI";
		}
		return null;
		
	}
	
	/**
	 * 获取地图颜色图例
	 * @author lucheng
	 * @date 2020年11月13日 下午1:20:01
	 * @return
	 */
	public String displayLegnd(){
		List<Object> returnList = new ArrayList<>();
		List<MapTrailThreshold> all = null;
		if(StringUtils.hasText(cityName)){
			Menu menu = terminalMenuService.get(cityName);
			Set<MapTrailThreshold> mapTrailThreshold = menu.getMapTrailThreshold();
			if(mapTrailThreshold.size()>0){
				all = new ArrayList<MapTrailThreshold>(mapTrailThreshold);
			}
		}
		
		if(all == null || all.size()==0){
			Map<String,Object> param2 = new HashMap<String, Object>();
			param2.put("subjectType", colorMapType);
			//查询默认参数,isDefault为1
			param2.put("isDefault", "1");
			all = analysisThresholdService.queryTrailByMapParam(param2);
		}else{
			//补充地图图例的参数名称和easyui-combobox的value对应的关系
			Map<String,Object> param3 = new HashMap<String, Object>();
			param3.put("subjectType", colorMapType);
			//查询默认参数,isDefault为1
			param3.put("isDefault", "1");
			param3.put("thresholdType", VolteAnalysisThresholdTypeConstant.TARGET_CHOOSE_DATA);
			List<MapTrailThreshold> list = analysisThresholdService.queryTrailByMapParam(param3);
			if(list!=null && list.size()>0){
				all.addAll(list);
			}else{
				System.out.println("TargetChooseData类型的参数不全,请补充参数");
				return null;
			}
		}
		
		//保存渲染地图轨迹图例时的初始值
		Map<String,List<Map<String, String>>> mapParam = new HashMap<String,List<Map<String, String>>>();
		//保存界面打开地图图层管理的图例选择样式类型
		List<Map<String, Object>> targetChooseType =  new ArrayList<Map<String,Object>>();

		//查询阈值颜色参数
		for (MapTrailThreshold mapTrailThreshold : all) {
			String thresholdType = mapTrailThreshold.getThresholdType();
			if (null == thresholdType || 0 == thresholdType.trim().length()) {
				continue;
			}
			switch (thresholdType.trim()) {
			case VolteAnalysisThresholdTypeConstant.COLOR_MAP_ETG_TRAL:
				String nameEn = mapTrailThreshold.getNameEn();
				String nameCn = mapTrailThreshold.getNameCh();
				String currentThreshold = mapTrailThreshold
						.getCurrentThreshold();
				
				List<Map<String, String>> mapParamMap = new ArrayList<>();
				
				if (null != nameEn
						&& (nameEn.equals("rsrpIndex")
								|| nameEn.equals("sinrIndex") 
								|| nameEn.equals("beamIndex")
								|| nameEn.equals("macthrputdlIndex")
								|| nameEn.equals("macthrputulIndex"))) {
					if (StringUtils.hasText(currentThreshold)) {
						String[] split = currentThreshold.split("@");
						
						if(mapParam.get(nameEn.replace("Index", "")) != null){
							mapParamMap = mapParam.get(nameEn.replace("Index", ""));
						}
						
						for (int i = 0; i < split.length; i++) {
							String range1 = split[i].substring(0, 1);
							String range2 = split[i].substring(split[i].length() - 1, split[i].length());
							List<String> split2 = Arrays.asList(split[i].substring(1,
									split[i].length() - 1).split(",",-1));
							String begin = split2.get(0);
							String end = split2.get(1);
							
							//保存渲染轨迹时的图例
							Map<String, String> indexMap;
							try {
								indexMap = mapParamMap.get(i);
							} catch (Exception e) {
								indexMap = new HashMap<>();
								mapParamMap.add(i, indexMap);
							}
							indexMap.put("beginValue", begin);
							indexMap.put("endValue", end);
							indexMap.put("range1", range1);
							indexMap.put("range2", range2);
							
							//保存图层管理的图例
							String scope = "";
							if(range1.equals("(")){
								range1 = "<";
							}else if(range1.equals("[")){
								range1 = "<=";
							}
							if(range2.equals(")")){
								range2 = "<";
							}else if(range2.equals("]")){
								range2 = "<=";
							}
							
							if (begin != null) {
								if(!begin.equals("-∞")){
									scope = scope + begin + range1;
								}
							}
							if (end != null) {
								scope = scope + "X" + range2 + end;
							}
							indexMap.put("scope", scope);
							//此数值暂时用不到，所以就默认了
							indexMap.put("data", "(1184)3%");
							
						}
						//取nameEn去除Index作为名称
						mapParam.put(nameEn.replace("Index", ""), mapParamMap);
					}
				}
				if (null != nameEn
						&& (nameEn.equals("rsrpColor")
								|| nameEn.equals("sinrColor") 
								|| nameEn.equals("beamColor")
								|| nameEn.equals("macthrputdlColor")
								|| nameEn.equals("macthrputulColor"))) {
					if (StringUtils.hasText(currentThreshold)) {
						String[] split = currentThreshold.split(",");
						
						if(mapParam.get(nameEn.replace("Color", "")) != null){
							mapParamMap = mapParam.get(nameEn.replace("Color", ""));
						}
						
						for (int i = 0; i < split.length; i++) {
							//保存渲染轨迹时的图例
							Map<String, String> colorMap;
							try {
								colorMap = mapParamMap.get(i);
							} catch (Exception e) {
								colorMap = new HashMap<>();
								mapParamMap.add(i, colorMap);
							}
							String color = split[i];
							colorMap.put("color", color);
							
						}
						//取nameEn去除Color作为名称
						mapParam.put(nameEn.replace("Color", ""), mapParamMap);
					}
				}
				break;
			case VolteAnalysisThresholdTypeConstant.TARGET_CHOOSE_DATA:
				String nameCnTarget = mapTrailThreshold.getNameCh();
				String currentThresholdTarget = mapTrailThreshold
						.getCurrentThreshold();
				
				if( null != nameCnTarget) {
					if (StringUtils.hasText(currentThresholdTarget)) {
						Map<String, Object> targetMap = new HashMap<>();
						//取nameCn作为名称
						targetMap.put("label", nameCnTarget);
						targetMap.put("value", currentThresholdTarget);
						if(targetChooseType.size()==0){
							targetMap.put("selected", true);
						}else{
							targetMap.put("selected", false);
						}
						targetChooseType.add(targetMap);
					}
				}
				break;
			case VolteAnalysisThresholdTypeConstant.PCI_MAP_COLOR:
				String nameCnPci = mapTrailThreshold.getNameCh();
				String nameEnPci = mapTrailThreshold.getNameEn();
				String currentThresholdPci = mapTrailThreshold
						.getCurrentThreshold();
				
				if( null != nameCnPci) {
					if (StringUtils.hasText(currentThresholdPci)) {
						String[] split = currentThresholdPci.split(",");
						List<Map<String, String>> pciMapParam = new ArrayList<>();
						
						for (int i = 0; i < split.length; i++) {
							Map<String, String> pciMap = new HashMap<>();
							pciMap.put("color", split[i]);
							pciMap.put("index", String.valueOf(i));
							pciMap.put("scope", "第"+(i+1)+"个pci");
							//此数值暂时用不到，所以就默认了
							pciMap.put("data", "(1184)3%");
							pciMapParam.add(pciMap);
						}
						//取nameEn去除Color作为名称
						mapParam.put(nameEnPci.replace("Color", ""), pciMapParam);
					}
				}
				break;
			default:
				break;
			}
		}
		returnList.add(0,mapParam);
		returnList.add(1,targetChooseType);
		ActionContext.getContext().getValueStack().push(returnList);
		
		return ReturnType.JSON;
		
	}
	
	/**
	 * 保存覆盖指标地图显示区间设置修改
	 * @return
	 */
	public String saveMapRangeParamThreshold() {
		TerminalGroup oldGroup = terminalGroupService.findGroupById(cityId);
		Menu menu = terminalMenuService.get(oldGroup.getName());
		
		Map<String,Object> paramSelect = new HashMap<String, Object>();
		paramSelect.put("subjectType", colorMapType);
		//查询非默认参数
		paramSelect.put("isDefault", "0");
		paramSelect.put("cityId", cityId);
		List<MapTrailThreshold> mapThsoldList = analysisThresholdService.queryTrailByMapParam(paramSelect);
		//区域没有绑定参数情况下，查询默认参数
		if(mapThsoldList == null || mapThsoldList.size()==0){
			Map<String,Object> param2 = new HashMap<String, Object>();
			param2.put("subjectType", colorMapType);
			//查询默认参数,isDefault为1
			param2.put("isDefault", "1");
			mapThsoldList = analysisThresholdService.queryTrailByMapParam(param2);
		}
		
		for (MapTrailThreshold mapTrailThreshold : mapThsoldList) {
			if(legendMap.containsKey(mapTrailThreshold.getNameEn())){
				mapTrailThreshold.setCurrentThreshold(legendMap.get(mapTrailThreshold.getNameEn()));
				if(mapTrailThreshold.getIsDefault()==0){
					//查询的是非默认参数，更新参数
					analysisThresholdService.updateMapTrailThreshold(mapTrailThreshold);
				}else{
					//查询的是默认参数，保存参数
					MapTrailThreshold cloneParam = mapTrailThreshold.clone();
					cloneParam.setId(null);
					cloneParam.setMenu(menu);
					cloneParam.setIsDefault(0);
					cloneParam.setCurrentThreshold(legendMap.get(mapTrailThreshold.getNameEn()));
					analysisThresholdService.saveMapTrailThreshold(cloneParam);
				}
			}
		}

		return ReturnType.JSON;
	}
	
	

	/**
	 * @return the thresholdMapthresholdMap
	 */
	public Map<String, VolteAnalysisThreshold> getThresholdMap() {
		return thresholdMap;
	}

	/**
	 * @param thresholdMap
	 *            the thresholdMap to set
	 */
	public void setThresholdMap(Map<String, VolteAnalysisThreshold> thresholdMap) {
		this.thresholdMap = thresholdMap;
	}

	/**
	 * @return the legendMap
	 */
	public Map<String, String> getLegendMap() {
		return legendMap;
	}

	/**
	 * @param legendMap the legendMap to set
	 */
	public void setLegendMap(Map<String, String> legendMap) {
		this.legendMap = legendMap;
	}

	/**
	 * @return the colorMapType
	 */
	public String getColorMapType() {
		return colorMapType;
	}

	/**
	 * @param colorMapType the colorMapType to set
	 */
	public void setColorMapType(String colorMapType) {
		this.colorMapType = colorMapType;
	}


	public Long getCityId() {
		return cityId;
	}


	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	

	

}
