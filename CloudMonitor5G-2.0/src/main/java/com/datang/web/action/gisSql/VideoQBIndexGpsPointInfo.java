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
 * 视频质差路段各指标颜色配置
 * 
 * @explain
 * @name VideoQBIndexGpsPointInfo
 * @author shenyanwei
 * @date 2017年6月20日下午1:08:43
 */
@Component
@Scope("singleton")
public class VideoQBIndexGpsPointInfo implements Serializable {
	/**
	 * gis.QBRGpsPoint.upprbNumIndex=[0,3]@(3,9]@(9,15]@(15,\u221E)
	 * gis.QBRGpsPoint.upprbNumIndexColor=\#00b050,\#ffff00,\#ffc000,\#ff0000
	 * gis.QBRGpsPoint.downprbNumIndex=[0,5]@(5,12]@(12,18]@(18,\u221E)
	 * gis.QBRGpsPoint.downprbNumIndexColor=\#00b050,\#ffff00,\#ffc000,\#ff0000
	 * 
	 * gis.QBRGpsPoint.rtpAudioPacketLostRatio=(\\u221E,5]@(5,10]@(10,50]@(50,\
	 * \u221E)
	 * gis.QBRGpsPoint.rtpAudioPacketLostRatioColor=\\#00b050,\\#ffff00,\
	 * \#ffc000,\\#ff0000
	 * gis.QBRGpsPoint.rtpVideoPacketLostRatio=(\\u221E,5]@(5,
	 * 10]@(10,50]@(50,\\u221E)
	 * gis.QBRGpsPoint.rtpVideoPacketLostRatioColor=\\#00
	 * b050,\\#ffff00,\\#ffc000,\\#ff0000
	 * gis.QBRGpsPoint.rtpAudioJitter=(\\u221E,5]@(5,10]@(10,20]@(20,\\u221E)
	 * gis .QBRGpsPoint.rtpAudioJitterColor=\\#00b050,\\#ffff00,\\#ffc000,\\#
	 * ff0000
	 * gis.QBRGpsPoint.rtpVideoJitter=(\\u221E,5]@(5,10]@(10,20]@(20,\\u221E)
	 * gis .QBRGpsPoint.rtpVideoJitterColor=\\#00b050,\\#ffff00,\\#ffc000,\\#
	 * ff0000
	 */
	private static final long serialVersionUID = 7977872375482320312L;

	@Value("${gis.QBRGpsPoint.vmosIndex}")
	private String vmosIndex;
	@Value("${gis.QBRGpsPoint.vmosIndexColor}")
	private String vmosIndexColor;
	@Value("${gis.QBRGpsPoint.rsrpIndex}")
	private String rsrpIndex;
	@Value("${gis.QBRGpsPoint.rsrpIndexColor}")
	private String rsrpIndexColor;
	@Value("${gis.QBRGpsPoint.sinrIndex}")
	private String sinrIndex;
	@Value("${gis.QBRGpsPoint.sinrIndexColor}")
	private String sinrIndexColor;
	@Value("${gis.QBRGpsPoint.PDCCHDLGrantNumIndex}")
	private String PDCCHDLGrantNumIndex;
	@Value("${gis.QBRGpsPoint.PDCCHDLGrantNumIndexColor}")
	private String PDCCHDLGrantNumIndexColor;
	@Value("${gis.QBRGpsPoint.upprbNumIndex}")
	private String upprbNumIndex;
	@Value("${gis.QBRGpsPoint.upprbNumIndexColor}")
	private String upprbNumIndexColor;
	@Value("${gis.QBRGpsPoint.downprbNumIndex}")
	private String downprbNumIndex;
	@Value("${gis.QBRGpsPoint.downprbNumIndexColor}")
	private String downprbNumIndexColor;
	@Value("${gis.QBRGpsPoint.rtpAudioPacketLostRatio}")
	private String rtpAudioPacketLostRatio;
	@Value("${gis.QBRGpsPoint.rtpAudioPacketLostRatioColor}")
	private String rtpAudioPacketLostRatioColor;
	@Value("${gis.QBRGpsPoint.rtpVideoPacketLostRatio}")
	private String rtpVideoPacketLostRatio;
	@Value("${gis.QBRGpsPoint.rtpVideoPacketLostRatioColor}")
	private String rtpVideoPacketLostRatioColor;
	@Value("${gis.QBRGpsPoint.rtpAudioJitter}")
	private String rtpAudioJitter;
	@Value("${gis.QBRGpsPoint.rtpAudioJitterColor}")
	private String rtpAudioJitterColor;
	@Value("${gis.QBRGpsPoint.rtpVideoJitter}")
	private String rtpVideoJitter;
	@Value("${gis.QBRGpsPoint.rtpVideoJitterColor}")
	private String rtpVideoJitterColor;

	@Value("${gis.QBRGpsPoint.streamStallingRatio}")
	private String streamStallingRatio;
	@Value("${gis.QBRGpsPoint.streamStallingRatioColor}")
	private String streamStallingRatioColor;
	@Value("${gis.QBRGpsPoint.streamInitialBufferTime}")
	private String streamInitialBufferTime;
	@Value("${gis.QBRGpsPoint.streamInitialBufferTimeColor}")
	private String streamInitialBufferTimeColor;
	@Value("${gis.QBRGpsPoint.streamVideoBitrate}")
	private String streamVideoBitrate;
	@Value("${gis.QBRGpsPoint.streamVideoBitrateColor}")
	private String streamVideoBitrateColor;

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
				index = vmosIndex;
				indexColor = vmosIndexColor;
				break;
			case 1:
				index = rsrpIndex;
				indexColor = rsrpIndexColor;
				break;
			case 2:
				index = sinrIndex;
				indexColor = sinrIndexColor;
				break;
			case 7:
				index = PDCCHDLGrantNumIndex;
				indexColor = PDCCHDLGrantNumIndexColor;
				break;
			case 8:
				index = rtpAudioPacketLostRatio;
				indexColor = rtpAudioPacketLostRatioColor;
				break;
			case 9:
				index = rtpAudioJitter;
				indexColor = rtpAudioJitterColor;
				break;
			case 10:
				index = upprbNumIndex;
				indexColor = upprbNumIndexColor;
				break;
			case 11:
				index = downprbNumIndex;
				indexColor = downprbNumIndexColor;
				break;
			case 12:
				index = rtpVideoJitter;
				indexColor = rtpVideoJitterColor;
				break;
			case 13:
				index = rtpVideoPacketLostRatio;
				indexColor = rtpVideoPacketLostRatioColor;
				break;
			case 15:// Stream vmos
				index = vmosIndex;
				indexColor = vmosIndexColor;
				break;
			case 16:// Stream 卡顿比列
				index = streamStallingRatio;
				indexColor = streamStallingRatioColor;
				break;
			case 17:// Stream 初始缓冲时延
				index = streamInitialBufferTime;
				indexColor = streamInitialBufferTimeColor;
				break;
			case 18:// Stream 视频全程感知速率
				index = streamVideoBitrate;
				indexColor = streamVideoBitrateColor;
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

	/**
	 * @return the rsrpIndex
	 */
	public String getRsrpIndex() {
		return rsrpIndex;
	}

	/**
	 * @param the
	 *            rsrpIndex to set
	 */

	public void setRsrpIndex(String rsrpIndex) {
		this.rsrpIndex = rsrpIndex;
	}

	/**
	 * @return the rsrpIndexColor
	 */
	public String getRsrpIndexColor() {
		return rsrpIndexColor;
	}

	/**
	 * @param the
	 *            rsrpIndexColor to set
	 */

	public void setRsrpIndexColor(String rsrpIndexColor) {
		this.rsrpIndexColor = rsrpIndexColor;
	}

	/**
	 * @return the sinrIndex
	 */
	public String getSinrIndex() {
		return sinrIndex;
	}

	/**
	 * @param the
	 *            sinrIndex to set
	 */

	public void setSinrIndex(String sinrIndex) {
		this.sinrIndex = sinrIndex;
	}

	/**
	 * @return the sinrIndexColor
	 */
	public String getSinrIndexColor() {
		return sinrIndexColor;
	}

	/**
	 * @param the
	 *            sinrIndexColor to set
	 */

	public void setSinrIndexColor(String sinrIndexColor) {
		this.sinrIndexColor = sinrIndexColor;
	}

	/**
	 * @return the PDCCHDLGrantNumIndex
	 */
	public String getPDCCHDLGrantNumIndex() {
		return PDCCHDLGrantNumIndex;
	}

	/**
	 * @param the
	 *            PDCCHDLGrantNumIndex to set
	 */

	public void setPDCCHDLGrantNumIndex(String pDCCHDLGrantNumIndex) {
		PDCCHDLGrantNumIndex = pDCCHDLGrantNumIndex;
	}

	/**
	 * @return the PDCCHDLGrantNumIndexColor
	 */
	public String getPDCCHDLGrantNumIndexColor() {
		return PDCCHDLGrantNumIndexColor;
	}

	/**
	 * @param the
	 *            PDCCHDLGrantNumIndexColor to set
	 */

	public void setPDCCHDLGrantNumIndexColor(String pDCCHDLGrantNumIndexColor) {
		PDCCHDLGrantNumIndexColor = pDCCHDLGrantNumIndexColor;
	}

	/**
	 * @return the upprbNumIndex
	 */
	public String getUpprbNumIndex() {
		return upprbNumIndex;
	}

	/**
	 * @param the
	 *            upprbNumIndex to set
	 */

	public void setUpprbNumIndex(String upprbNumIndex) {
		this.upprbNumIndex = upprbNumIndex;
	}

	/**
	 * @return the upprbNumIndexColor
	 */
	public String getUpprbNumIndexColor() {
		return upprbNumIndexColor;
	}

	/**
	 * @param the
	 *            upprbNumIndexColor to set
	 */

	public void setUpprbNumIndexColor(String upprbNumIndexColor) {
		this.upprbNumIndexColor = upprbNumIndexColor;
	}

	/**
	 * @return the downprbNumIndex
	 */
	public String getDownprbNumIndex() {
		return downprbNumIndex;
	}

	/**
	 * @param the
	 *            downprbNumIndex to set
	 */

	public void setDownprbNumIndex(String downprbNumIndex) {
		this.downprbNumIndex = downprbNumIndex;
	}

	/**
	 * @return the downprbNumIndexColor
	 */
	public String getDownprbNumIndexColor() {
		return downprbNumIndexColor;
	}

	/**
	 * @param the
	 *            downprbNumIndexColor to set
	 */

	public void setDownprbNumIndexColor(String downprbNumIndexColor) {
		this.downprbNumIndexColor = downprbNumIndexColor;
	}

	/**
	 * @return the rtpAudioPacketLostRatio
	 */
	public String getRtpAudioPacketLostRatio() {
		return rtpAudioPacketLostRatio;
	}

	/**
	 * @param the
	 *            rtpAudioPacketLostRatio to set
	 */

	public void setRtpAudioPacketLostRatio(String rtpAudioPacketLostRatio) {
		this.rtpAudioPacketLostRatio = rtpAudioPacketLostRatio;
	}

	/**
	 * @return the rtpAudioPacketLostRatioColor
	 */
	public String getRtpAudioPacketLostRatioColor() {
		return rtpAudioPacketLostRatioColor;
	}

	/**
	 * @param the
	 *            rtpAudioPacketLostRatioColor to set
	 */

	public void setRtpAudioPacketLostRatioColor(
			String rtpAudioPacketLostRatioColor) {
		this.rtpAudioPacketLostRatioColor = rtpAudioPacketLostRatioColor;
	}

	/**
	 * @return the rtpVideoPacketLostRatio
	 */
	public String getRtpVideoPacketLostRatio() {
		return rtpVideoPacketLostRatio;
	}

	/**
	 * @param the
	 *            rtpVideoPacketLostRatio to set
	 */

	public void setRtpVideoPacketLostRatio(String rtpVideoPacketLostRatio) {
		this.rtpVideoPacketLostRatio = rtpVideoPacketLostRatio;
	}

	/**
	 * @return the rtpVideoPacketLostRatioColor
	 */
	public String getRtpVideoPacketLostRatioColor() {
		return rtpVideoPacketLostRatioColor;
	}

	/**
	 * @param the
	 *            rtpVideoPacketLostRatioColor to set
	 */

	public void setRtpVideoPacketLostRatioColor(
			String rtpVideoPacketLostRatioColor) {
		this.rtpVideoPacketLostRatioColor = rtpVideoPacketLostRatioColor;
	}

	/**
	 * @return the rtpAudioJitter
	 */
	public String getRtpAudioJitter() {
		return rtpAudioJitter;
	}

	/**
	 * @param the
	 *            rtpAudioJitter to set
	 */

	public void setRtpAudioJitter(String rtpAudioJitter) {
		this.rtpAudioJitter = rtpAudioJitter;
	}

	/**
	 * @return the rtpAudioJitterColor
	 */
	public String getRtpAudioJitterColor() {
		return rtpAudioJitterColor;
	}

	/**
	 * @param the
	 *            rtpAudioJitterColor to set
	 */

	public void setRtpAudioJitterColor(String rtpAudioJitterColor) {
		this.rtpAudioJitterColor = rtpAudioJitterColor;
	}

	/**
	 * @return the rtpVideoJitter
	 */
	public String getRtpVideoJitter() {
		return rtpVideoJitter;
	}

	/**
	 * @param the
	 *            rtpVideoJitter to set
	 */

	public void setRtpVideoJitter(String rtpVideoJitter) {
		this.rtpVideoJitter = rtpVideoJitter;
	}

	/**
	 * @return the rtpVideoJitterColor
	 */
	public String getRtpVideoJitterColor() {
		return rtpVideoJitterColor;
	}

	/**
	 * @param the
	 *            rtpVideoJitterColor to set
	 */

	public void setRtpVideoJitterColor(String rtpVideoJitterColor) {
		this.rtpVideoJitterColor = rtpVideoJitterColor;
	}

	/**
	 * @return the streamStallingRatiostreamStallingRatio
	 */
	public String getStreamStallingRatio() {
		return streamStallingRatio;
	}

	/**
	 * @param streamStallingRatio
	 *            the streamStallingRatio to set
	 */
	public void setStreamStallingRatio(String streamStallingRatio) {
		this.streamStallingRatio = streamStallingRatio;
	}

	/**
	 * @return the streamInitialBufferTimestreamInitialBufferTime
	 */
	public String getStreamInitialBufferTime() {
		return streamInitialBufferTime;
	}

	/**
	 * @param streamInitialBufferTime
	 *            the streamInitialBufferTime to set
	 */
	public void setStreamInitialBufferTime(String streamInitialBufferTime) {
		this.streamInitialBufferTime = streamInitialBufferTime;
	}

	/**
	 * @return the streamVideoBitratestreamVideoBitrate
	 */
	public String getStreamVideoBitrate() {
		return streamVideoBitrate;
	}

	/**
	 * @param streamVideoBitrate
	 *            the streamVideoBitrate to set
	 */
	public void setStreamVideoBitrate(String streamVideoBitrate) {
		this.streamVideoBitrate = streamVideoBitrate;
	}

	/**
	 * @return the streamStallingRatioColorstreamStallingRatioColor
	 */
	public String getStreamStallingRatioColor() {
		return streamStallingRatioColor;
	}

	/**
	 * @param streamStallingRatioColor
	 *            the streamStallingRatioColor to set
	 */
	public void setStreamStallingRatioColor(String streamStallingRatioColor) {
		this.streamStallingRatioColor = streamStallingRatioColor;
	}

	/**
	 * @return the streamInitialBufferTimeColorstreamInitialBufferTimeColor
	 */
	public String getStreamInitialBufferTimeColor() {
		return streamInitialBufferTimeColor;
	}

	/**
	 * @param streamInitialBufferTimeColor
	 *            the streamInitialBufferTimeColor to set
	 */
	public void setStreamInitialBufferTimeColor(
			String streamInitialBufferTimeColor) {
		this.streamInitialBufferTimeColor = streamInitialBufferTimeColor;
	}

	/**
	 * @return the streamVideoBitrateColorstreamVideoBitrateColor
	 */
	public String getStreamVideoBitrateColor() {
		return streamVideoBitrateColor;
	}

	/**
	 * @param streamVideoBitrateColor
	 *            the streamVideoBitrateColor to set
	 */
	public void setStreamVideoBitrateColor(String streamVideoBitrateColor) {
		this.streamVideoBitrateColor = streamVideoBitrateColor;
	}

}
