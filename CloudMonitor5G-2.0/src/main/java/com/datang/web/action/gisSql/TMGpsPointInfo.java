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

/**
 * TM颜色配置
 * 
 * @explain
 * @name TMGpsPointInfo
 * @author shenyanwei
 * @date 2017年6月20日下午2:21:38
 */
@Component
@Scope("singleton")
public class TMGpsPointInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408894004805093916L;
	@Value("${gis.videoGpsPoint.tm1Color}")
	private String tm1Color;
	@Value("${gis.videoGpsPoint.tm2Color}")
	private String tm2Color;
	@Value("${gis.videoGpsPoint.tm3Color}")
	private String tm3Color;
	@Value("${gis.videoGpsPoint.tm4Color}")
	private String tm4Color;
	@Value("${gis.videoGpsPoint.tm5Color}")
	private String tm5Color;
	@Value("${gis.videoGpsPoint.tm6Color}")
	private String tm6Color;
	@Value("${gis.videoGpsPoint.tm7Color}")
	private String tm7Color;
	@Value("${gis.videoGpsPoint.tm8Color}")
	private String tm8Color;
	@Value("${gis.videoGpsPoint.tmOtherColor}")
	private String tmOtherColor;

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
		Map<String, String> tm1ColorMap = new LinkedHashMap<String, String>();
		tm1ColorMap.put("tmType", "tm1");
		tm1ColorMap.put("tmTypeName", "TM1");
		tm1ColorMap.put("color", tm1Color);
		listmMaps.add(tm1ColorMap);
		Map<String, String> tm2ColorMap = new LinkedHashMap<String, String>();
		tm2ColorMap.put("tmType", "tm2");
		tm2ColorMap.put("tmTypeName", "TM2");
		tm2ColorMap.put("color", tm2Color);
		listmMaps.add(tm2ColorMap);
		Map<String, String> tm3ColorMap = new LinkedHashMap<String, String>();
		tm3ColorMap.put("tmType", "tm3");
		tm3ColorMap.put("tmTypeName", "TM3");
		tm3ColorMap.put("color", tm3Color);
		listmMaps.add(tm3ColorMap);
		Map<String, String> tm4ColorMap = new LinkedHashMap<String, String>();
		tm4ColorMap.put("tmType", "tm4");
		tm4ColorMap.put("tmTypeName", "TM4");
		tm4ColorMap.put("color", tm4Color);
		listmMaps.add(tm4ColorMap);
		Map<String, String> tm5ColorMap = new LinkedHashMap<String, String>();
		tm5ColorMap.put("tmType", "tm5");
		tm5ColorMap.put("tmTypeName", "TM5");
		tm5ColorMap.put("color", tm5Color);
		listmMaps.add(tm5ColorMap);
		Map<String, String> tm6ColorMap = new LinkedHashMap<String, String>();
		tm6ColorMap.put("tmType", "tm6");
		tm6ColorMap.put("tmTypeName", "TM6");
		tm6ColorMap.put("color", tm6Color);
		listmMaps.add(tm6ColorMap);
		Map<String, String> tm7ColorMap = new LinkedHashMap<String, String>();
		tm7ColorMap.put("tmType", "tm7");
		tm7ColorMap.put("tmTypeName", "TM7");
		tm7ColorMap.put("color", tm7Color);
		listmMaps.add(tm7ColorMap);
		Map<String, String> tm8ColorMap = new LinkedHashMap<String, String>();
		tm8ColorMap.put("tmType", "tm8");
		tm8ColorMap.put("tmTypeName", "TM8");
		tm8ColorMap.put("color", tm8Color);
		listmMaps.add(tm8ColorMap);
		Map<String, String> tmOtherColorMap = new LinkedHashMap<String, String>();
		tmOtherColorMap.put("tmType", "TMOTHER");
		tmOtherColorMap.put("tmTypeName", "其他");
		tmOtherColorMap.put("color", tmOtherColor);
		listmMaps.add(tmOtherColorMap);
		return listmMaps;
	}

	/**
	 * @return the tm1Color
	 */
	public String getTm1Color() {
		return tm1Color;
	}

	/**
	 * @param the
	 *            tm1Color to set
	 */

	public void setTm1Color(String tm1Color) {
		this.tm1Color = tm1Color;
	}

	/**
	 * @return the tm2Color
	 */
	public String getTm2Color() {
		return tm2Color;
	}

	/**
	 * @param the
	 *            tm2Color to set
	 */

	public void setTm2Color(String tm2Color) {
		this.tm2Color = tm2Color;
	}

	/**
	 * @return the tm3Color
	 */
	public String getTm3Color() {
		return tm3Color;
	}

	/**
	 * @param the
	 *            tm3Color to set
	 */

	public void setTm3Color(String tm3Color) {
		this.tm3Color = tm3Color;
	}

	/**
	 * @return the tm4Color
	 */
	public String getTm4Color() {
		return tm4Color;
	}

	/**
	 * @param the
	 *            tm4Color to set
	 */

	public void setTm4Color(String tm4Color) {
		this.tm4Color = tm4Color;
	}

	/**
	 * @return the tm5Color
	 */
	public String getTm5Color() {
		return tm5Color;
	}

	/**
	 * @param the
	 *            tm5Color to set
	 */

	public void setTm5Color(String tm5Color) {
		this.tm5Color = tm5Color;
	}

	/**
	 * @return the tm6Color
	 */
	public String getTm6Color() {
		return tm6Color;
	}

	/**
	 * @param the
	 *            tm6Color to set
	 */

	public void setTm6Color(String tm6Color) {
		this.tm6Color = tm6Color;
	}

	/**
	 * @return the tm7Color
	 */
	public String getTm7Color() {
		return tm7Color;
	}

	/**
	 * @param the
	 *            tm7Color to set
	 */

	public void setTm7Color(String tm7Color) {
		this.tm7Color = tm7Color;
	}

	/**
	 * @return the tm8Color
	 */
	public String getTm8Color() {
		return tm8Color;
	}

	/**
	 * @param the
	 *            tm8Color to set
	 */

	public void setTm8Color(String tm8Color) {
		this.tm8Color = tm8Color;
	}

	/**
	 * @return the tmOtherColor
	 */
	public String getTmOtherColor() {
		return tmOtherColor;
	}

	/**
	 * @param the
	 *            tmOtherColor to set
	 */

	public void setTmOtherColor(String tmOtherColor) {
		this.tmOtherColor = tmOtherColor;
	}

}
