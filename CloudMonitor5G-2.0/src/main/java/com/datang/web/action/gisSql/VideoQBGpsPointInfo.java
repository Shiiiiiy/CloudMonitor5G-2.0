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

import com.datang.constant.VideoQBType;

/**
 * 各种视频质差的颜色配置
 * 
 * @explain
 * @name VideoQBGpsPointInfo
 * @author shenyanwei
 * @date 2017年5月22日下午5:40:11
 */
@Component
@Scope("singleton")
public class VideoQBGpsPointInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408894004805093916L;
	@Value("${gis.videoGpsPoint.pingPongColor}")
	private String pingPongColor;
	@Value("${gis.videoGpsPoint.disturbColor}")
	private String disturbColor;
	@Value("${gis.videoGpsPoint.neighbourColor}")
	private String neighbourColor;
	@Value("${gis.videoGpsPoint.weakCoverColor}")
	private String weakCoverColor;
	@Value("${gis.videoGpsPoint.overCoverColor}")
	private String overCoverColor;
	@Value("${gis.videoGpsPoint.otherColor}")
	private String otherColor;
	@Value("${gis.videoGpsPoint.patternSwitchColor}")
	private String patternSwitchColor;
	@Value("${gis.videoGpsPoint.downDispatchSmallColor}")
	private String downDispatchSmallColor;

	/**
	 * 获取所有视频质差的颜色配置的[{"qbrType":"","color":"#ffffff"}...]形式
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
		weakCoverColorMap.put("videoQBType", VideoQBType.WeakCover.toString());
		weakCoverColorMap.put("videoQBTypeName", "弱覆盖");
		weakCoverColorMap.put("color", weakCoverColor);
		listmMaps.add(weakCoverColorMap);
		Map<String, String> disturbColorMap = new LinkedHashMap<String, String>();
		disturbColorMap.put("videoQBType", VideoQBType.Disturb.toString());
		disturbColorMap.put("videoQBTypeName", "干扰");
		disturbColorMap.put("color", disturbColor);
		listmMaps.add(disturbColorMap);
		Map<String, String> neighbourColorMap = new LinkedHashMap<String, String>();
		neighbourColorMap.put("videoQBType", VideoQBType.Neighbour.toString());
		neighbourColorMap.put("videoQBTypeName", "邻区配置");
		neighbourColorMap.put("color", neighbourColor);
		listmMaps.add(neighbourColorMap);
		Map<String, String> pingPongColorMap = new LinkedHashMap<String, String>();
		pingPongColorMap.put("videoQBType", VideoQBType.PingPong.toString());
		pingPongColorMap.put("videoQBTypeName", "乒乓切换");
		pingPongColorMap.put("color", pingPongColor);
		listmMaps.add(pingPongColorMap);
		Map<String, String> overCoverColorMap = new LinkedHashMap<String, String>();
		overCoverColorMap.put("videoQBType", VideoQBType.OverCover.toString());
		overCoverColorMap.put("videoQBTypeName", "重叠覆盖");
		overCoverColorMap.put("color", overCoverColor);
		listmMaps.add(overCoverColorMap);
		Map<String, String> otherColorMap = new LinkedHashMap<String, String>();
		otherColorMap.put("videoQBType", VideoQBType.Other.toString());
		otherColorMap.put("videoQBTypeName", "其他");
		otherColorMap.put("color", otherColor);
		listmMaps.add(otherColorMap);
		Map<String, String> patternSwitchColorMap = new LinkedHashMap<String, String>();
		patternSwitchColorMap.put("videoQBType",
				VideoQBType.PatternSwitch.toString());
		patternSwitchColorMap.put("videoQBTypeName", "模式转换");
		patternSwitchColorMap.put("color", patternSwitchColor);
		listmMaps.add(patternSwitchColorMap);
		Map<String, String> downDispatchSmallColorMap = new LinkedHashMap<String, String>();
		downDispatchSmallColorMap.put("videoQBType",
				VideoQBType.DownDispatchSmall.toString());
		downDispatchSmallColorMap.put("videoQBTypeName", "下行调度数小");
		downDispatchSmallColorMap.put("color", downDispatchSmallColor);
		listmMaps.add(downDispatchSmallColorMap);
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

	/**
	 * @return the pingPongColor
	 */
	public String getPingPongColor() {
		return pingPongColor;
	}

	/**
	 * @param the
	 *            pingPongColor to set
	 */

	public void setPingPongColor(String pingPongColor) {
		this.pingPongColor = pingPongColor;
	}

	/**
	 * @return the neighbourColor
	 */
	public String getNeighbourColor() {
		return neighbourColor;
	}

	/**
	 * @param the
	 *            neighbourColor to set
	 */

	public void setNeighbourColor(String neighbourColor) {
		this.neighbourColor = neighbourColor;
	}

	/**
	 * @return the overCoverColor
	 */
	public String getOverCoverColor() {
		return overCoverColor;
	}

	/**
	 * @param the
	 *            overCoverColor to set
	 */

	public void setOverCoverColor(String overCoverColor) {
		this.overCoverColor = overCoverColor;
	}

	/**
	 * @return the patternSwitchColor
	 */
	public String getPatternSwitchColor() {
		return patternSwitchColor;
	}

	/**
	 * @param the
	 *            patternSwitchColor to set
	 */

	public void setPatternSwitchColor(String patternSwitchColor) {
		this.patternSwitchColor = patternSwitchColor;
	}

	/**
	 * @return the downDispatchSmallColor
	 */
	public String getDownDispatchSmallColor() {
		return downDispatchSmallColor;
	}

	/**
	 * @param the
	 *            downDispatchSmallColor to set
	 */

	public void setDownDispatchSmallColor(String downDispatchSmallColor) {
		this.downDispatchSmallColor = downDispatchSmallColor;
	}

}
