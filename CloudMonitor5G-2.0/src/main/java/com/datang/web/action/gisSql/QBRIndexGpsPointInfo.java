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

import com.datang.common.util.StringUtils;

/**
 * 质差路段各指标颜色配置
 * 
 * @author yinzhipeng
 * @date:2015年12月1日 下午5:02:19
 * @version
 */
@Component
@Scope("singleton")
public class QBRIndexGpsPointInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7977872375482320312L;
	// gis.QBRGpsPoint.mosIndex=[0,2]@(2,3]@(3,-4]@(4,5]
	// gis.QBRGpsPoint.mosIndexColor=\#ff0000,\#ffc000,\#ffff00,\#00b050
	// gis.QBRGpsPoint.rsrpIndex=(\u221E,-110]@(-110,-100]@(-100,-85]@(-85,\u221E)
	// gis.QBRGpsPoint.rsrpIndexColor=\#ff0000,\#ffc000,\#ffff00,\#00b050
	// gis.QBRGpsPoint.sinrIndex=(\u221E,-10]@(-10,-5]@(-5,3]@(3,\u221E)
	// gis.QBRGpsPoint.sinrIndexColor=\#ff0000,\#ffc000,\#ffff00,\#00b050
	// gis.QBRGpsPoint.upmcsIndex=[0,10]@(10,15]@(15,20]@(20,29]
	// gis.QBRGpsPoint.upmcsIndexColor=\#ff0000,\#ffc000,\#ffff00,\#00b050
	// gis.QBRGpsPoint.downmcsIndex=[0,10]@(10,15]@(15,20]@(20,29]
	// gis.QBRGpsPoint.downmcsIndexColor=\#ff0000,\#ffc000,\#ffff00,\#00b050
	// gis.QBRGpsPoint.rtpTimeDelayIndex=[0,0.1]@(0.1,0.2]@(0.2,0.5]@(0.5,1]
	// gis.QBRGpsPoint.rtpTimeDelayIndexColor=\#00b050,\#ffff00,\#ffc000,\#ff0000
	// gis.QBRGpsPoint.rtpDropRatioIndex=[0,0.1]@(0.1,0.2]@(0.2,0.3]@(0.3,1]
	// gis.QBRGpsPoint.rtpDropRatioIndexColor=\#00b050,\#ffff00,\#ffc000,\#ff0000
	// gis.QBRGpsPoint.upprbNumIndex=[0,3]@(3,9]@(9,15]@(15,\u221E)
	// gis.QBRGpsPoint.upprbNumIndexColor=\#00b050,\#ffff00,\#ffc000,\#ff0000
	// gis.QBRGpsPoint.downprbNumIndex=[0,5]@(5,12]@(12,18]@(18,\u221E)
	// gis.QBRGpsPoint.downprbNumIndexColor=\#00b050,\#ffff00,\#ffc000,\#ff0000
	@Value("${gis.QBRGpsPoint.mosIndex}")
	private String mosIndex;
	@Value("${gis.QBRGpsPoint.mosIndexColor}")
	private String mosIndexColor;
	@Value("${gis.QBRGpsPoint.rsrpIndex}")
	private String rsrpIndex;
	@Value("${gis.QBRGpsPoint.rsrpIndexColor}")
	private String rsrpIndexColor;
	@Value("${gis.QBRGpsPoint.sinrIndex}")
	private String sinrIndex;
	@Value("${gis.QBRGpsPoint.sinrIndexColor}")
	private String sinrIndexColor;
	@Value("${gis.QBRGpsPoint.upmcsIndex}")
	private String upmcsIndex;
	@Value("${gis.QBRGpsPoint.upmcsIndexColor}")
	private String upmcsIndexColor;
	@Value("${gis.QBRGpsPoint.downmcsIndex}")
	private String downmcsIndex;
	@Value("${gis.QBRGpsPoint.downmcsIndexColor}")
	private String downmcsIndexColor;
	@Value("${gis.QBRGpsPoint.rtpTimeDelayIndex}")
	private String rtpTimeDelayIndex;
	@Value("${gis.QBRGpsPoint.rtpTimeDelayIndexColor}")
	private String rtpTimeDelayIndexColor;
	@Value("${gis.QBRGpsPoint.rtpDropRatioIndex}")
	private String rtpDropRatioIndex;
	@Value("${gis.QBRGpsPoint.rtpDropRatioIndexColor}")
	private String rtpDropRatioIndexColor;
	@Value("${gis.QBRGpsPoint.upprbNumIndex}")
	private String upprbNumIndex;
	@Value("${gis.QBRGpsPoint.upprbNumIndexColor}")
	private String upprbNumIndexColor;
	@Value("${gis.QBRGpsPoint.downprbNumIndex}")
	private String downprbNumIndex;
	@Value("${gis.QBRGpsPoint.downprbNumIndexColor}")
	private String downprbNumIndexColor;
	@Value("${gis.QBRGpsPoint.vmosIndex}")
	private String vmosIndex;
	@Value("${gis.QBRGpsPoint.vmosIndexColor}")
	private String vmosIndexColor;
	@Value("${gis.QBRGpsPoint.sinr5gIndex}")
	private String sinr5gIndex;
	@Value("${gis.QBRGpsPoint.sinr5gIndexColor}")
	private String sinr5gIndexColor;
	@Value("${gis.QBRGpsPoint.rsrp5gIndex}")
	private String rsrp5gIndex;
	@Value("${gis.QBRGpsPoint.rsrp5gIndexColor}")
	private String rsrp5gIndexColor;
	@Value("${gis.QBRGpsPoint.rsrqIndex}")
	private String rsrqIndex;
	@Value("${gis.QBRGpsPoint.rsrqIndexColor}")
	private String rsrqIndexColor;

	/**
	 * 获取某个指标的[{"beginValue":108,"color":"#ffffff","endValue":110}...]形式
	 * 
	 * @param indexType
	 * @return
	 */
	public List<Map<String, Object>> getIndexColorListMap(Integer indexType) {
		List<Map<String, Object>> listmMaps = new LinkedList<>();
		if (null != indexType) {
			String index = null;
			String indexColor = null;
			switch (indexType) {
			case 0:
				index = mosIndex;
				indexColor = mosIndexColor;
				break;
			case 1:
				index = rsrpIndex;
				indexColor = rsrpIndexColor;
				break;
			case 2:
				index = sinrIndex;
				indexColor = sinrIndexColor;
				break;
			case 3:
				index = upmcsIndex;
				indexColor = upmcsIndexColor;
				break;
			case 4:
				index = downmcsIndex;
				indexColor = downmcsIndexColor;
				break;
			case 5:
				index = rtpTimeDelayIndex;
				indexColor = rtpTimeDelayIndexColor;
				break;
			case 6:
				index = rtpDropRatioIndex;
				indexColor = rtpDropRatioIndexColor;
				break;
			case 7:
				index = upprbNumIndex;
				indexColor = upprbNumIndexColor;
				break;
			case 8:
				index = downprbNumIndex;
				indexColor = downprbNumIndexColor;
				break;
			case 9:
				index = vmosIndex;
				indexColor = vmosIndexColor;
				break;
			case 31:
				index = sinr5gIndex;
				indexColor = sinr5gIndexColor;
				break;
			case 32:
				index = rsrp5gIndex;
				indexColor = rsrp5gIndexColor;
				break;
			case 33:
				index = rsrqIndex;
				indexColor = rsrqIndexColor;
				break;
			default:
				break;
			}
			// {
			// "beginValue" : 108,//指标区间开始值
			// "color" : "#ffffff",//指标位于该区间的颜色
			// "endValue" : 110//指标区间结束值
			// }
			if (null != index && null != indexColor
					&& StringUtils.hasText(index)
					&& StringUtils.hasText(indexColor)) {
				String[] indexs = index.split("@");
				String[] indexColors = indexColor.split(",");
				if ((0 != indexs.length && 0 != indexColors.length)
						&& (indexs.length == indexColors.length)) {
					for (int i = 0; i < indexs.length; i++) {
						if (StringUtils.hasText(indexs[i])) {
							// [0,2]@(2,3]@(3,-4]@(4,5]
							String[] indexSplit = indexs[i].trim()
									.substring(1, indexs[i].length() - 1)
									.split(",");
							Map<String, Object> map = new LinkedHashMap<String, Object>();
							map.put("beginValue", indexSplit[0]);
							map.put("color", indexColors[i]);
							map.put("endValue", indexSplit[1]);
							listmMaps.add(map);
						}
					}
				}
			}
		}
		return listmMaps;
	}

	/**
	 * 获取某个指标的[{"beginValue":108,"color":"#ffffff","endValue":110}...]形式
	 * 
	 * @param indexType
	 * @return
	 */
	public List<Map<String, Object>> getIndexColorListMap5g(Integer indexType) {
		List<Map<String, Object>> listmMaps = new LinkedList<>();
		if (null != indexType) {
			String index = null;
			String indexColor = null;
			switch (indexType) {
			case 0:
				index = mosIndex;
				indexColor = mosIndexColor;
				break;
			case 1:
				index = rsrp5gIndex;
				indexColor = rsrp5gIndexColor;
				break;
			case 2:
				index = sinr5gIndex;
				indexColor = sinr5gIndexColor;
				break;
			case 3:
				index = upmcsIndex;
				indexColor = upmcsIndexColor;
				break;
			case 4:
				index = downmcsIndex;
				indexColor = downmcsIndexColor;
				break;
			case 5:
				index = rtpTimeDelayIndex;
				indexColor = rtpTimeDelayIndexColor;
				break;
			case 6:
				index = rtpDropRatioIndex;
				indexColor = rtpDropRatioIndexColor;
				break;
			case 7:
				index = upprbNumIndex;
				indexColor = upprbNumIndexColor;
				break;
			case 8:
				index = downprbNumIndex;
				indexColor = downprbNumIndexColor;
				break;
			case 9:
				index = rsrqIndex;
				indexColor = rsrqIndexColor;
				break;
			case 33:
				index = rsrqIndex;
				indexColor = rsrqIndexColor;
				break;
			default:
				break;
			}
			// {
			// "beginValue" : 108,//指标区间开始值
			// "color" : "#ffffff",//指标位于该区间的颜色
			// "endValue" : 110//指标区间结束值
			// }
			if (null != index && null != indexColor
					&& StringUtils.hasText(index)
					&& StringUtils.hasText(indexColor)) {
				String[] indexs = index.split("@");
				String[] indexColors = indexColor.split(",");
				if ((0 != indexs.length && 0 != indexColors.length)
						&& (indexs.length == indexColors.length)) {
					for (int i = 0; i < indexs.length; i++) {
						if (StringUtils.hasText(indexs[i])) {
							// [0,2]@(2,3]@(3,-4]@(4,5]
							String[] indexSplit = indexs[i].trim()
									.substring(1, indexs[i].length() - 1)
									.split(",");
							Map<String, Object> map = new LinkedHashMap<String, Object>();
							map.put("beginValue", indexSplit[0]);
							map.put("color", indexColors[i]);
							map.put("endValue", indexSplit[1]);
							listmMaps.add(map);
						}
					}
				}
			}
		}
		return listmMaps;
	}

	/**
	 * @return the mosIndexmosIndex
	 */
	public String getMosIndex() {
		return mosIndex;
	}

	/**
	 * @param mosIndex
	 *            the mosIndex to set
	 */
	public void setMosIndex(String mosIndex) {
		this.mosIndex = mosIndex;
	}

	/**
	 * @return the mosIndexColormosIndexColor
	 */
	public String getMosIndexColor() {
		return mosIndexColor;
	}

	/**
	 * @param mosIndexColor
	 *            the mosIndexColor to set
	 */
	public void setMosIndexColor(String mosIndexColor) {
		this.mosIndexColor = mosIndexColor;
	}

	/**
	 * @return the rsrpIndexrsrpIndex
	 */
	public String getRsrpIndex() {
		return rsrpIndex;
	}

	/**
	 * @param rsrpIndex
	 *            the rsrpIndex to set
	 */
	public void setRsrpIndex(String rsrpIndex) {
		this.rsrpIndex = rsrpIndex;
	}

	/**
	 * @return the rsrpIndexColorrsrpIndexColor
	 */
	public String getRsrpIndexColor() {
		return rsrpIndexColor;
	}

	/**
	 * @param rsrpIndexColor
	 *            the rsrpIndexColor to set
	 */
	public void setRsrpIndexColor(String rsrpIndexColor) {
		this.rsrpIndexColor = rsrpIndexColor;
	}

	/**
	 * @return the sinrIndexsinrIndex
	 */
	public String getSinrIndex() {
		return sinrIndex;
	}

	/**
	 * @param sinrIndex
	 *            the sinrIndex to set
	 */
	public void setSinrIndex(String sinrIndex) {
		this.sinrIndex = sinrIndex;
	}

	/**
	 * @return the sinrIndexColorsinrIndexColor
	 */
	public String getSinrIndexColor() {
		return sinrIndexColor;
	}

	/**
	 * @param sinrIndexColor
	 *            the sinrIndexColor to set
	 */
	public void setSinrIndexColor(String sinrIndexColor) {
		this.sinrIndexColor = sinrIndexColor;
	}

	/**
	 * @return the upmcsIndexupmcsIndex
	 */
	public String getUpmcsIndex() {
		return upmcsIndex;
	}

	/**
	 * @param upmcsIndex
	 *            the upmcsIndex to set
	 */
	public void setUpmcsIndex(String upmcsIndex) {
		this.upmcsIndex = upmcsIndex;
	}

	/**
	 * @return the upmcsIndexColorupmcsIndexColor
	 */
	public String getUpmcsIndexColor() {
		return upmcsIndexColor;
	}

	/**
	 * @param upmcsIndexColor
	 *            the upmcsIndexColor to set
	 */
	public void setUpmcsIndexColor(String upmcsIndexColor) {
		this.upmcsIndexColor = upmcsIndexColor;
	}

	/**
	 * @return the downmcsIndexdownmcsIndex
	 */
	public String getDownmcsIndex() {
		return downmcsIndex;
	}

	/**
	 * @param downmcsIndex
	 *            the downmcsIndex to set
	 */
	public void setDownmcsIndex(String downmcsIndex) {
		this.downmcsIndex = downmcsIndex;
	}

	/**
	 * @return the downmcsIndexColordownmcsIndexColor
	 */
	public String getDownmcsIndexColor() {
		return downmcsIndexColor;
	}

	/**
	 * @param downmcsIndexColor
	 *            the downmcsIndexColor to set
	 */
	public void setDownmcsIndexColor(String downmcsIndexColor) {
		this.downmcsIndexColor = downmcsIndexColor;
	}

	/**
	 * @return the rtpTimeDelayIndexrtpTimeDelayIndex
	 */
	public String getRtpTimeDelayIndex() {
		return rtpTimeDelayIndex;
	}

	/**
	 * @param rtpTimeDelayIndex
	 *            the rtpTimeDelayIndex to set
	 */
	public void setRtpTimeDelayIndex(String rtpTimeDelayIndex) {
		this.rtpTimeDelayIndex = rtpTimeDelayIndex;
	}

	/**
	 * @return the rtpTimeDelayIndexColorrtpTimeDelayIndexColor
	 */
	public String getRtpTimeDelayIndexColor() {
		return rtpTimeDelayIndexColor;
	}

	/**
	 * @param rtpTimeDelayIndexColor
	 *            the rtpTimeDelayIndexColor to set
	 */
	public void setRtpTimeDelayIndexColor(String rtpTimeDelayIndexColor) {
		this.rtpTimeDelayIndexColor = rtpTimeDelayIndexColor;
	}

	/**
	 * @return the rtpDropRatioIndexrtpDropRatioIndex
	 */
	public String getRtpDropRatioIndex() {
		return rtpDropRatioIndex;
	}

	/**
	 * @param rtpDropRatioIndex
	 *            the rtpDropRatioIndex to set
	 */
	public void setRtpDropRatioIndex(String rtpDropRatioIndex) {
		this.rtpDropRatioIndex = rtpDropRatioIndex;
	}

	/**
	 * @return the rtpDropRatioIndexColorrtpDropRatioIndexColor
	 */
	public String getRtpDropRatioIndexColor() {
		return rtpDropRatioIndexColor;
	}

	/**
	 * @param rtpDropRatioIndexColor
	 *            the rtpDropRatioIndexColor to set
	 */
	public void setRtpDropRatioIndexColor(String rtpDropRatioIndexColor) {
		this.rtpDropRatioIndexColor = rtpDropRatioIndexColor;
	}

	/**
	 * @return the upprbNumIndexupprbNumIndex
	 */
	public String getUpprbNumIndex() {
		return upprbNumIndex;
	}

	/**
	 * @param upprbNumIndex
	 *            the upprbNumIndex to set
	 */
	public void setUpprbNumIndex(String upprbNumIndex) {
		this.upprbNumIndex = upprbNumIndex;
	}

	/**
	 * @return the upprbNumIndexColorupprbNumIndexColor
	 */
	public String getUpprbNumIndexColor() {
		return upprbNumIndexColor;
	}

	/**
	 * @param upprbNumIndexColor
	 *            the upprbNumIndexColor to set
	 */
	public void setUpprbNumIndexColor(String upprbNumIndexColor) {
		this.upprbNumIndexColor = upprbNumIndexColor;
	}

	/**
	 * @return the downprbNumIndexdownprbNumIndex
	 */
	public String getDownprbNumIndex() {
		return downprbNumIndex;
	}

	/**
	 * @param downprbNumIndex
	 *            the downprbNumIndex to set
	 */
	public void setDownprbNumIndex(String downprbNumIndex) {
		this.downprbNumIndex = downprbNumIndex;
	}

	/**
	 * @return the downprbNumIndexColordownprbNumIndexColor
	 */
	public String getDownprbNumIndexColor() {
		return downprbNumIndexColor;
	}

	/**
	 * @param downprbNumIndexColor
	 *            the downprbNumIndexColor to set
	 */
	public void setDownprbNumIndexColor(String downprbNumIndexColor) {
		this.downprbNumIndexColor = downprbNumIndexColor;
	}

	/**
	 * @return the vmosIndex
	 */
	public String getVmosIndex() {
		return vmosIndex;
	}

	/**
	 * @param the
	 *            vmosIndex to set
	 */

	public void setVmosIndex(String vmosIndex) {
		this.vmosIndex = vmosIndex;
	}

	/**
	 * @return the vmosIndexColor
	 */
	public String getVmosIndexColor() {
		return vmosIndexColor;
	}

	/**
	 * @param the
	 *            vmosIndexColor to set
	 */

	public void setVmosIndexColor(String vmosIndexColor) {
		this.vmosIndexColor = vmosIndexColor;
	}

}
