package com.datang.web.action.gisSql;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.datang.constant.QBRType;

/**
 * 各种呼叫建立时延异常颜色配置
 * 
 * @name CEDEGpsPointInfo
 * @author shenyanwei
 * @date 2016年6月25日下午8:31:15
 */
@Component
@Scope("singleton")
public class CEDEGpsPointInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9016844871872354024L;
	@Value("${gis.QBRGpsPoint.weakCoverRoadColor}")
	private String weakCoverColor;
	@Value("${gis.QBRGpsPoint.disturbRoadColor}")
	private String overlapCoverColor;
	@Value("${gis.QBRGpsPoint.nbCellRoadColor}")
	private String locationUpdateColor;
	@Value("${gis.QBRGpsPoint.coreNetworkRoadColor}")
	private String coreNetWorkColor;
	@Value("${gis.QBRGpsPoint.otherRoadColor}")
	private String otherColor;

	/**
	 * 获取所有cede路段的颜色配置的[{"qbrType":"","color":"#ffffff"}...]形式
	 * 
	 * @param indexType
	 * @return
	 */
	public List<Map<String, String>> getColorListMap() {
		List<Map<String, String>> listmMaps = new LinkedList<>();
		// {
		// "qbrType" : "",//路段类型
		// "color" : "#ffffff"//该路段的颜色
		// }
		Map<String, String> weakCoverColorMap = new LinkedHashMap<String, String>();
		weakCoverColorMap.put("cedeType", QBRType.WeakCover.toString());
		weakCoverColorMap.put("cedeTypeName", "弱覆盖");
		weakCoverColorMap.put("color", weakCoverColor);
		listmMaps.add(weakCoverColorMap);
		Map<String, String> disturbColorMap = new LinkedHashMap<String, String>();
		disturbColorMap.put("cedeType", QBRType.OverlapCover.toString());
		disturbColorMap.put("cedeTypeName", "重叠覆盖");
		disturbColorMap.put("color", overlapCoverColor);
		listmMaps.add(disturbColorMap);
		Map<String, String> nbCellColorMap = new LinkedHashMap<String, String>();
		nbCellColorMap.put("cedeType", QBRType.LocationUpdate.toString());
		nbCellColorMap.put("cedeTypeName", "被叫位置更新");
		nbCellColorMap.put("color", locationUpdateColor);
		listmMaps.add(nbCellColorMap);
		Map<String, String> coreNetworkColorMap = new LinkedHashMap<String, String>();
		coreNetworkColorMap.put("cedeType", QBRType.CoreNetwork.toString());
		coreNetworkColorMap.put("cedeTypeName", "核心网");
		coreNetworkColorMap.put("color", coreNetWorkColor);
		listmMaps.add(coreNetworkColorMap);
		Map<String, String> otherColorMap = new LinkedHashMap<String, String>();
		otherColorMap.put("cedeType", QBRType.Other.toString());
		otherColorMap.put("cedeTypeName", "其他");
		otherColorMap.put("color", otherColor);
		listmMaps.add(otherColorMap);
		return listmMaps;
	}

	/**
	 * @return the weakCoverColorweakCoverColor
	 */
	public String getWeakCoverColor() {
		return weakCoverColor;
	}

	/**
	 * @param weakCoverColor
	 *            the weakCoverColor to set
	 */
	public void setWeakCoverColor(String weakCoverColor) {
		this.weakCoverColor = weakCoverColor;
	}

	public String getOverlapCoverColor() {
		return overlapCoverColor;
	}

	public void setOverlapCoverColor(String overlapCoverColor) {
		this.overlapCoverColor = overlapCoverColor;
	}

	public String getLocationUpdateColor() {
		return locationUpdateColor;
	}

	public void setLocationUpdateColor(String locationUpdateColor) {
		this.locationUpdateColor = locationUpdateColor;
	}

	/**
	 * @return the coreNetWorkColorcoreNetWorkColor
	 */
	public String getCoreNetWorkColor() {
		return coreNetWorkColor;
	}

	/**
	 * @param coreNetWorkColor
	 *            the coreNetWorkColor to set
	 */
	public void setCoreNetWorkColor(String coreNetWorkColor) {
		this.coreNetWorkColor = coreNetWorkColor;
	}

	/**
	 * @return the otherColorotherColor
	 */
	public String getOtherColor() {
		return otherColor;
	}

	/**
	 * @param otherColor
	 *            the otherColor to set
	 */
	public void setOtherColor(String otherColor) {
		this.otherColor = otherColor;
	}

}
