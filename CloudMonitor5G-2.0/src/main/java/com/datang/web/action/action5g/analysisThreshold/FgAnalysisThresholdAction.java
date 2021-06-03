/**
 * 
 */
package com.datang.web.action.action5g.analysisThreshold;

import com.datang.common.util.StringUtils;
import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 5G专题分析门限(参数设置)
 * 
 * @author _YZP
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class FgAnalysisThresholdAction extends ActionSupport {
	@Autowired
	private IVolteAnalysisThresholdService analysisThresholdService;

	private Map<String, VolteAnalysisThreshold> thresholdMap;

	/**
	 * 进入volte专题分析门限页面
	 * 
	 * @return
	 */
	public String fgAnalysisThresholdListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType("fg");

		List<VolteAnalysisThreshold> disturbAnalysis = new ArrayList<>();
		List<VolteAnalysisThreshold> embbCoverAnalysis = new ArrayList<>();
		List<List<Map<String, String>>> mapParam = new ArrayList<>();

		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			String thresholdType = volteAnalysisThreshold.getThresholdType();
			if (null == thresholdType || 0 == thresholdType.trim().length()) {
				continue;
			}
			switch (thresholdType.trim()) {
			case VolteAnalysisThresholdTypeConstant.DISTURB_ANALYSIS:
				disturbAnalysis.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.EMBB_COVER_ANALYSIS:
				embbCoverAnalysis.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.MAP_PARAM:
				String nameEn = volteAnalysisThreshold.getNameEn();
				String currentThreshold = volteAnalysisThreshold
						.getCurrentThreshold();
				List<Map<String, String>> mapParamMap = new ArrayList<>();
				if (null != nameEn
						&& (nameEn.equals("rsrpIndex")
								|| nameEn.equals("rsrqIndex") || nameEn
									.equals("sinrIndex"))) {
					mapParam.add(mapParamMap);
					// rsrp默认值:(∞,-105]@(-105,-95]@(-95,-85]@(-85,-75]@(-75,∞)
					// rsrq默认值:(∞,0]@(0,5]@(5,10]@(10,15]@(15,∞)
					// sinr默认值:(∞,3]@(3,10]@(10,15]@(15,25]@(25,∞)
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
							String[] split2 = split[i].substring(1,
									split[i].length() - 1).split(",");
							String begin = split2[0];
							String end = split2[1];
							indexMap.put("begin", begin);
							indexMap.put("end", end);
							indexMap.put("nameCh", nameEn.replace("Index", ""));
						}
					}
				}
				if (null != nameEn
						&& (nameEn.equals("rsrpColor")
								|| nameEn.equals("rsrqColor") || nameEn
									.equals("sinrColor"))) {
					// mapParam.add(mapParamMap);
					// rsrp默认值:#ff0000,#ffc000,#ffff00,#00b0f0,#00b050
					// rsrq默认值:#ff0000,#ffc000,#ffff00,#00b0f0,#00b050
					// sinr默认值:#ff0000,#ffc000,#ffff00,#00b0f0,#00b050
					if (StringUtils.hasText(currentThreshold)) {
						String[] split = currentThreshold.split(",");
						for (int i = 0; i < split.length; i++) {
							for (List<Map<String, String>> mapParamMap_ : mapParam) {
								if (mapParamMap_.get(0).get("nameCh")
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
							colorMap.put("nameCh", nameEn.replace("Color", ""));
						}
					}
				}
				break;
			default:
				break;
			}
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.DISTURB_ANALYSIS,
				disturbAnalysis);
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_COVER_ANALYSIS,
				embbCoverAnalysis);
		valueStack.set(VolteAnalysisThresholdTypeConstant.MAP_PARAM, mapParam);

		// org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
		// configurer = new PropertyPlaceholderConfigurer();
		// configurer.setProperties(properties);

		return ReturnType.LISTUI;
	}

	/**
	 * 保存Volte分析门限修改
	 * 
	 * @return
	 */
	public String saveVolteDissThreshold() {
		if (null != thresholdMap) {
			for (Entry<String, VolteAnalysisThreshold> entry : thresholdMap
					.entrySet()) {
				VolteAnalysisThreshold value = entry.getValue();
				if (null != value && null != value.getId()) {
					analysisThresholdService
							.updateVolteAnalysisThreshold(value);
				}
			}
		}
		ActionContext.getContext().getValueStack().set("result", "SUCCESS");
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

}
