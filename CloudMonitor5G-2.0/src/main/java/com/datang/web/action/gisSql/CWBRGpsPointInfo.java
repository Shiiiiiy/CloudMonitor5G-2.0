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
 * 各种连续无线差颜色配置
 * 
 * @name CWBRGpsPointInfo
 * @author shenyanwei
 * @date 2016年6月25日下午8:30:17
 */
@Component
@Scope("singleton")
public class CWBRGpsPointInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1441351276714257542L;
	@Value("${gis.QBRGpsPoint.weakCoverRoadColor}")
	private String weakCoverColor;
	@Value("${gis.QBRGpsPoint.disturbRoadColor}")
	private String disturbColor;
	@Value("${gis.QBRGpsPoint.nbCellRoadColor}")
	private String nbCellColor;
	@Value("${gis.QBRGpsPoint.otherRoadColor}")
	private String otherColor;

	/**
	 * 获取所有qbr路段的颜色配置的[{"qbrType":"","color":"#ffffff"}...]形式
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
		weakCoverColorMap.put("cwbrType", QBRType.WeakCover.toString());
		weakCoverColorMap.put("cwbrTypeName", "弱覆盖");
		weakCoverColorMap.put("color", weakCoverColor);
		listmMaps.add(weakCoverColorMap);
		Map<String, String> disturbColorMap = new LinkedHashMap<String, String>();
		disturbColorMap.put("cwbrType", QBRType.Disturb.toString());
		disturbColorMap.put("cwbrTypeName", "干扰");
		disturbColorMap.put("color", disturbColor);
		listmMaps.add(disturbColorMap);
		Map<String, String> nbCellColorMap = new LinkedHashMap<String, String>();
		nbCellColorMap.put("cwbrType", QBRType.NbCell.toString());
		nbCellColorMap.put("cwbrTypeName", "邻区配置");
		nbCellColorMap.put("color", nbCellColor);
		listmMaps.add(nbCellColorMap);
		Map<String, String> otherColorMap = new LinkedHashMap<String, String>();
		otherColorMap.put("cwbrType", QBRType.Other.toString());
		otherColorMap.put("cwbrTypeName", "其他");
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

	/**
	 * @return the disturbColordisturbColor
	 */
	public String getDisturbColor() {
		return disturbColor;
	}

	/**
	 * @param disturbColor
	 *            the disturbColor to set
	 */
	public void setDisturbColor(String disturbColor) {
		this.disturbColor = disturbColor;
	}

	/**
	 * @return the nbCellColornbCellColor
	 */
	public String getNbCellColor() {
		return nbCellColor;
	}

	/**
	 * @param nbCellColor
	 *            the nbCellColor to set
	 */
	public void setNbCellColor(String nbCellColor) {
		this.nbCellColor = nbCellColor;
	}

	/**
	 * @return the paramErrorColorparamErrorColor
	 */

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
