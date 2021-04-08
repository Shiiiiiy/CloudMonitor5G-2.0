package com.datang.domain.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datang.domain.testLogItem.TestLogItem;

/**
 * VoLTE质量专题----视频质差分析,视频质差公共指标
 * 
 * @explain
 * @name VideoQualityBad
 * @author shenyanwei
 * @date 2017年5月10日下午3:55:08
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_QB")
@Inheritance(strategy = InheritanceType.JOINED)
public class VideoQualityBad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5479171449571318877L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 测试日志
	 */
	private TestLogItem testLogItem;
	private String cellTypeString;
	/**
	 * 时间戳
	 */
	private Long time;
	/**
	 * VMOS
	 */
	private Float vmos;
	/**
	 * 问题路段视频码率
	 */
	private Float videoBitRate;
	/**
	 * 问题路段视频帧率
	 */
	private Float videoFrameRate;
	/**
	 * 丢失的音频数据包数量 丢包率=(丢失的数据包数量)/发送的数据包数量
	 */
	private Long audioPacketlossNum;
	/**
	 * 发送的音频数据包数量
	 */
	private Long audioPacketNum;
	/**
	 * 丢失的视频数据包数量
	 */
	private Long videoPacketlossNum;
	/**
	 * 发送的视频数据包数量
	 */
	private Long videoPacketNum;
	/**
	 * 问题路段i_RTT
	 */
	private Float irtt;
	/**
	 * 问题路段音频码率
	 */
	private Float audioBitRate;
	/**
	 * 问题路段sinr值
	 */
	private Float sinrValue;

	/**
	 * 问题路段rsrp值
	 */
	private Float rsrpValue;
	/**
	 * 关键参数原因 0 丢包率原因 1 视频码率 2 视频帧率 3 音频码率
	 */
	private Integer keyParameterCause;
	private String keyParameterCauseString;

	/**
	 * 服务小区ID
	 */
	private Long cellId;
	/**
	 * 服务小区
	 */
	private String cellName;
	/**
	 * 优化建议
	 */
	private String optimization;
	/**
	 * 服务小区PCI
	 */
	private Long cellPci;
	/**
	 * 经度
	 */
	private Float longitude;
	/**
	 * 纬度
	 */
	private Float latitude;
	/**
	 * 路段名称
	 */
	private String m_stRoadName;
	/**
	 * RSRP均值
	 */
	private Float rsrpValueAvg;
	/**
	 * RSRP最小值
	 */
	private Float rsrpValueMin;
	/**
	 * SINR均值
	 */
	private Float sinrValueAvg;
	/**
	 * SINR最小值
	 */
	private Float sinrValueMin;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the callPci
	 */
	@Column(name = "CELL_PCI")
	public Long getCellPci() {
		return cellPci;
	}

	/**
	 * @param the
	 *            callPci to set
	 */

	public void setCellPci(Long cellPci) {
		this.cellPci = cellPci;
	}

	/**
	 * @return the optimization
	 */
	@Column(name = "OPTIMIZATION")
	public String getOptimization() {
		return optimization;
	}

	/**
	 * @param the
	 *            optimization to set
	 */

	public void setOptimization(String optimization) {
		this.optimization = optimization;
	}

	/**
	 * @return the testLogItemtestLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO", nullable = false, referencedColumnName = "RECSEQNO")
	public TestLogItem getTestLogItem() {
		return testLogItem;
	}

	/**
	 * @param testLogItem
	 *            the testLogItem to set
	 */
	public void setTestLogItem(TestLogItem testLogItem) {
		this.testLogItem = testLogItem;
	}

	/**
	 * @return the vmos
	 */
	@Column(name = "VMOS")
	public Float getVmos() {
		return vmos;
	}

	/**
	 * @param the
	 *            vmos to set
	 */

	public void setVmos(Float vmos) {
		this.vmos = vmos;
	}

	/**
	 * @return the keyParameterCause
	 */
	@Column(name = "KEY_PARAMETER_CAUSE")
	public Integer getKeyParameterCause() {
		return keyParameterCause;
	}

	/**
	 * @param the
	 *            keyParameterCause to set
	 */

	public void setKeyParameterCause(Integer keyParameterCause) {
		this.keyParameterCause = keyParameterCause;
	}

	/**
	 * @return the videoBitRate
	 */
	@Column(name = "VIDEO_BITRATE")
	public Float getVideoBitRate() {
		return videoBitRate;
	}

	/**
	 * @param the
	 *            videoBitRate to set
	 */

	public void setVideoBitRate(Float videoBitRate) {
		this.videoBitRate = videoBitRate;
	}

	/**
	 * @return the videoFrameRate
	 */
	@Column(name = "VIDEO_FRAMERATE")
	public Float getVideoFrameRate() {
		return videoFrameRate;
	}

	/**
	 * @param the
	 *            videoFrameRate to set
	 */

	public void setVideoFrameRate(Float videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	/**
	 * @return the irtt
	 */
	@Column(name = "IRTT")
	public Float getIrtt() {
		return irtt;
	}

	/**
	 * @param the
	 *            irtt to set
	 */

	public void setIrtt(Float irtt) {
		this.irtt = irtt;
	}

	/**
	 * @return the audioBitRate
	 */
	@Column(name = "AUDIO_BITRATE")
	public Float getAudioBitRate() {
		return audioBitRate;
	}

	/**
	 * @param the
	 *            audioBitRate to set
	 */

	public void setAudioBitRate(Float audioBitRate) {
		this.audioBitRate = audioBitRate;
	}

	/**
	 * @return the time
	 */
	@Column(name = "TIME")
	public Long getTime() {
		return time;
	}

	/**
	 * @param the
	 *            time to set
	 */

	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * @return the audioPacketNum
	 */
	@Column(name = "AUDIO_PACKET_NUM")
	public Long getAudioPacketNum() {
		return audioPacketNum;
	}

	/**
	 * @param the
	 *            audioPacketNum to set
	 */

	public void setAudioPacketNum(Long audioPacketNum) {
		this.audioPacketNum = audioPacketNum;
	}

	/**
	 * @return the audioPacketlossNum
	 */
	@Column(name = "AUDIO_PACKETLOSS_NUM")
	public Long getAudioPacketlossNum() {
		return audioPacketlossNum;
	}

	/**
	 * @param the
	 *            audioPacketlossNum to set
	 */

	public void setAudioPacketlossNum(Long audioPacketlossNum) {
		this.audioPacketlossNum = audioPacketlossNum;
	}

	/**
	 * @return the videoPacketNum
	 */
	@Column(name = "VIDEO_PACKET_NUM")
	public Long getVideoPacketNum() {
		return videoPacketNum;
	}

	/**
	 * @param the
	 *            videoPacketNum to set
	 */

	public void setVideoPacketNum(Long videoPacketNum) {
		this.videoPacketNum = videoPacketNum;
	}

	/**
	 * @return the videoPacketlossNum
	 */
	@Column(name = "VIDEO_PACKETLOSS_NUM")
	public Long getVideoPacketlossNum() {
		return videoPacketlossNum;
	}

	/**
	 * @param the
	 *            videoPacketlossNum to set
	 */

	public void setVideoPacketlossNum(Long videoPacketlossNum) {
		this.videoPacketlossNum = videoPacketlossNum;
	}

	/**
	 * @return the sinrValue
	 */
	@Column(name = "SINR_VALUE")
	public Float getSinrValue() {
		return sinrValue;
	}

	/**
	 * @param the
	 *            sinrValue to set
	 */

	public void setSinrValue(Float sinrValue) {
		this.sinrValue = sinrValue;
	}

	/**
	 * @return the rsrpValue
	 */
	@Column(name = "RSRP_VALUE")
	public Float getRsrpValue() {
		return rsrpValue;
	}

	/**
	 * @param the
	 *            rsrpValue to set
	 */

	public void setRsrpValue(Float rsrpValue) {
		this.rsrpValue = rsrpValue;
	}

	@Transient
	public String getTimeValue() {
		if (null != time) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(time));
		}
		return null;
	}

	/**
	 * @return the cellTypeString
	 */
	@Transient
	public String getCellTypeString() {
		return cellTypeString;
	}

	/**
	 * @param the
	 *            cellTypeString to set
	 */

	public void setCellTypeString(String cellTypeString) {
		this.cellTypeString = cellTypeString;
	}

	/**
	 * @return the keyParameterCausesString
	 */
	@Transient
	public String getKeyParameterCauseString() {
		return keyParameterCauseString;
	}

	/**
	 * @param the
	 *            keyParameterCausesString to set
	 */

	public void setKeyParameterCauseString(String keyParameterCauseString) {
		this.keyParameterCauseString = keyParameterCauseString;
	}

	/**
	 * @return the cellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param the
	 *            cellId to set
	 */

	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the callName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param the
	 *            callName to set
	 */

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "LONGITUDE")
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param the
	 *            longitude to set
	 */

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	@Column(name = "LATITUDE")
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param the
	 *            latitude to set
	 */

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the rsrpValueAvg
	 */
	@Column(name = "RSRP_VALUE_AVG")
	public Float getRsrpValueAvg() {
		return rsrpValueAvg;
	}

	/**
	 * @param the
	 *            rsrpValueAvg to set
	 */

	public void setRsrpValueAvg(Float rsrpValueAvg) {
		this.rsrpValueAvg = rsrpValueAvg;
	}

	/**
	 * @return the rsrpValueMin
	 */
	@Column(name = "RSRP_VALUE_MIN")
	public Float getRsrpValueMin() {
		return rsrpValueMin;
	}

	/**
	 * @param the
	 *            rsrpValueMin to set
	 */

	public void setRsrpValueMin(Float rsrpValueMin) {
		this.rsrpValueMin = rsrpValueMin;
	}

	/**
	 * @return the sinrValueAvg
	 */
	@Column(name = "SINR_VALUE_AVG")
	public Float getSinrValueAvg() {
		return sinrValueAvg;
	}

	/**
	 * @param the
	 *            sinrValueAvg to set
	 */

	public void setSinrValueAvg(Float sinrValueAvg) {
		this.sinrValueAvg = sinrValueAvg;
	}

	/**
	 * @return the sinrValueMin
	 */
	@Column(name = "SINR_VALUE_MIN")
	public Float getSinrValueMin() {
		return sinrValueMin;
	}

	/**
	 * @param the
	 *            sinrValueMin to set
	 */

	public void setSinrValueMin(Float sinrValueMin) {
		this.sinrValueMin = sinrValueMin;
	}

	/**
	 * @return the m_stRoadName
	 */
	@Column(name = "ROAD_NAME")
	public String getM_stRoadName() {
		return m_stRoadName;
	}

	/**
	 * @param the
	 *            m_stRoadName to set
	 */

	public void setM_stRoadName(String m_stRoadName) {
		this.m_stRoadName = m_stRoadName;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoQualityBad [id=" + id + ", testLogItem=" + testLogItem
				+ ", cellTypeString=" + cellTypeString + ", time=" + time
				+ ", vmos=" + vmos + ", videoBitRate=" + videoBitRate
				+ ", videoFrameRate=" + videoFrameRate
				+ ", audioPacketlossNum=" + audioPacketlossNum
				+ ", audioPacketNum=" + audioPacketNum
				+ ", videoPacketlossNum=" + videoPacketlossNum
				+ ", videoPacketNum=" + videoPacketNum + ", irtt=" + irtt
				+ ", audioBitRate=" + audioBitRate + ", sinrValue=" + sinrValue
				+ ", rsrpValue=" + rsrpValue + ", keyParameterCause="
				+ keyParameterCause + ", keyParameterCauseString="
				+ keyParameterCauseString + ", cellId=" + cellId
				+ ", cellName=" + cellName + ", optimization=" + optimization
				+ ", cellPci=" + cellPci + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", m_stRoadName=" + m_stRoadName
				+ ", rsrpValueAvg=" + rsrpValueAvg + ", rsrpValueMin="
				+ rsrpValueMin + ", sinrValueAvg=" + sinrValueAvg
				+ ", sinrValueMin=" + sinrValueMin + "]";
	}

}
