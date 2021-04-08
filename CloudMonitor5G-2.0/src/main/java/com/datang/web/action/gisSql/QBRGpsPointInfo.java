/**
 * 
 */
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
 * 各种质差路段的颜色配置
 * 
 * @author yinzhipeng
 * @date:2015年12月18日 下午1:33:05
 * @version
 */
@Component
@Scope("singleton")
public class QBRGpsPointInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408894004805093916L;
	@Value("${gis.QBRGpsPoint.weakCoverRoadColor}")
	private String weakCoverColor;
	@Value("${gis.QBRGpsPoint.disturbRoadColor}")
	private String disturbColor;
	@Value("${gis.QBRGpsPoint.nbCellRoadColor}")
	private String nbCellColor;
	@Value("${gis.QBRGpsPoint.paramErrorRoadColor}")
	private String paramErrorColor;
	@Value("${gis.QBRGpsPoint.coreNetworkRoadColor}")
	private String coreNetWorkColor;
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
		weakCoverColorMap.put("qbrType", QBRType.WeakCover.toString());
		weakCoverColorMap.put("qbrTypeName", "弱覆盖");
		weakCoverColorMap.put("color", weakCoverColor);
		listmMaps.add(weakCoverColorMap);
		Map<String, String> disturbColorMap = new LinkedHashMap<String, String>();
		disturbColorMap.put("qbrType", QBRType.Disturb.toString());
		disturbColorMap.put("qbrTypeName", "干扰");
		disturbColorMap.put("color", disturbColor);
		listmMaps.add(disturbColorMap);
		Map<String, String> nbCellColorMap = new LinkedHashMap<String, String>();
		nbCellColorMap.put("qbrType", QBRType.NbCell.toString());
		nbCellColorMap.put("qbrTypeName", "邻区配置");
		nbCellColorMap.put("color", nbCellColor);
		listmMaps.add(nbCellColorMap);
		Map<String, String> paramErrorColorMap = new LinkedHashMap<String, String>();
		paramErrorColorMap.put("qbrType", QBRType.ParamError.toString());
		paramErrorColorMap.put("qbrTypeName", "参数配置");
		paramErrorColorMap.put("color", paramErrorColor);
		listmMaps.add(paramErrorColorMap);
		Map<String, String> coreNetworkColorMap = new LinkedHashMap<String, String>();
		coreNetworkColorMap.put("qbrType", QBRType.CoreNetwork.toString());
		coreNetworkColorMap.put("qbrTypeName", "核心网");
		coreNetworkColorMap.put("color", coreNetWorkColor);
		listmMaps.add(coreNetworkColorMap);
		Map<String, String> otherColorMap = new LinkedHashMap<String, String>();
		otherColorMap.put("qbrType", QBRType.Other.toString());
		otherColorMap.put("qbrTypeName", "其他");
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
	public String getParamErrorColor() {
		return paramErrorColor;
	}

	/**
	 * @param paramErrorColor
	 *            the paramErrorColor to set
	 */
	public void setParamErrorColor(String paramErrorColor) {
		this.paramErrorColor = paramErrorColor;
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
