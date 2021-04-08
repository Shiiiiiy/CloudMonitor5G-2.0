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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 分析门限(弱覆盖参数设置)
 * 
 * @author _YZP
 * 
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CoverageParamAction extends ActionSupport {
	@Autowired
	private IVolteAnalysisThresholdService analysisThresholdService;

	private Map<String, VolteAnalysisThreshold> thresholdMap;
	
	private Map<String, String> legendMap = new HashMap<String, String>();
	
	private String fieldNameEn;
	
	private String fieldValue;
	
	private String fieldColor;
	
	/**
	 * 地图的图例对应类型
	 */
	private String colorMapType;

	/**
	 * 进入弱覆盖参数设置页面
	 * 
	 * @return
	 */
	public String weakCoverageParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType(VolteAnalysisThresholdTypeConstant.EMBB_WEAK_COVER_ANALYSIS);
		
		Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_WEAK_COVER_ANALYSIS,
				weakCoveragParamMap);

		return "weakCoveragelistUI";
	}
	
	/**
	 * 进入过覆盖参数设置页面
	 * 
	 * @return
	 */
	public String overCoverageParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType(VolteAnalysisThresholdTypeConstant.EMBB_OVER_COVER_ANALYSIS);
		
		Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_OVER_COVER_ANALYSIS,
				weakCoveragParamMap);

		return "overCoveragelistUI";
	}
	
	
	/**
	 * 进入重叠覆盖参数设置页面
	 * 
	 * @return
	 */
	public String overlayCoverageParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType(VolteAnalysisThresholdTypeConstant.EMBB_OVERLAY_COVER_ANALYSIS);
		
		Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_OVERLAY_COVER_ANALYSIS,
				weakCoveragParamMap);

		return "overlayCoveragelistUI";
	}
	
	/**
	 * 进入超远覆盖参数设置页面
	 * 
	 * @return
	 */
	public String spfarCoverageParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType(VolteAnalysisThresholdTypeConstant.EMBB_SPFAR_COVER_ANALYSIS);
		
		Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_SPFAR_COVER_ANALYSIS,
				weakCoveragParamMap);

		return "spfarCoveragelistUI";
	}
	
	/**
	 * 进入反向覆盖参数设置页面
	 * 
	 * @return
	 */
	public String reverseCoverageParamListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType(VolteAnalysisThresholdTypeConstant.EMBB_REVERSE_COVER_ANALYSIS);
		
		Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_REVERSE_COVER_ANALYSIS,
				weakCoveragParamMap);

		return "reverseCoveragelistUI";
	}
	
	/**
	 * 进入质差参数设置页面
	 * @return
	 */
	public String lteQualityBadListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();

		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType(VolteAnalysisThresholdTypeConstant.EMBB_QUALITY_BAD_ANALYSIS);
		
		Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
			weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.EMBB_QUALITY_BAD_ANALYSIS,
				weakCoveragParamMap);

		return "lteQualityBadListUI";
	}
	

	/**
	 * 保存覆盖参数设置修改
	 * 
	 * @return
	 */
	public String saveWeakCoverThreshold() {
		String[] nameEnArry = null;
		String[] currentThresholdArry = null;
		if(fieldNameEn!=null && !fieldNameEn.equals("")){
			nameEnArry = fieldNameEn.split(",");
		}
		if(fieldValue!=null && !fieldValue.equals("")){
			currentThresholdArry = fieldValue.split(",");
		}
		if(nameEnArry.length == currentThresholdArry.length){
			for (int i = 0; i < nameEnArry.length; i++) {
				VolteAnalysisThreshold analysisThreshold = analysisThresholdService.getVolteAnalysisThresholdByNameEn(nameEnArry[i]);
				analysisThreshold.setCurrentThreshold(currentThresholdArry[i]);
				analysisThresholdService.updateVolteAnalysisThreshold(analysisThreshold);
			}
			ActionContext.getContext().getValueStack().set("result", "SUCCESS");
		}else{
			ActionContext.getContext().getValueStack().set("errorMsg", "英文名称和修改值不对应");
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

	public String getFieldNameEn() {
		return fieldNameEn;
	}

	public void setFieldNameEn(String fieldNameEn) {
		this.fieldNameEn = fieldNameEn;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldColor() {
		return fieldColor;
	}

	public void setFieldColor(String fieldColor) {
		this.fieldColor = fieldColor;
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
	
	

	

}
