/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;

/**
 * 视频质差报表导出响应bean
 * 
 * @explain
 * @name VideoStatementeResponseBean
 * @author shenyanwei
 * @date 2017年6月28日下午1:03:24
 */
public class VideoStatementeResponseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6995011046293953584L;
	/**
	 * 地市
	 */
	private String region;
	/**
	 * 厂家
	 */
	private String vender;
	/**
	 * 场景
	 */
	private String coverScene;
	/**
	 * 网格
	 */
	private String isBelongtoNetwork;
	/**
	 * BOXID
	 */
	private String boxId;
	/**
	 * 事件名称
	 */
	private String evenName;
	/**
	 * 原因分类
	 */
	private String reason;
	/**
	 * 无线原因分类
	 */
	private String reasonWireless;
	/**
	 * 开始时间
	 */
	private String beginTime;
	/**
	 * 路段名称 
	 */
	private String roadName;
	/**
	 * 问题路段中心经度
	 */
	private String longitude;
	/**
	 * 问题路段中心纬度
	 */
	private String latitude;
	/**
	 * 持续距离(m) 
	 */
	private String distance;
	/**
	 * 问题时长(ms) 
	 */
	private String questionTime;
	/**
	 * RSRP均值(dBm) 
	 */
	private String rsrpAvg;
	/**
	 * RSRP最低值(dBm)
	 */
	private String rsrpMin;
	/**
	 * SINR均值(dB) 
	 */
	private String sinrAvg;
	/**
	 * SINR最低值(dB) 
	 */
	private String sinrMin;
	/**
	 * 测试日志名称
	 */
	private String testLogName;
	/**
	 * 优化建议 
	 */
	private String optimization;
	/**
	 * 小区详情
	 */
	private String cellInfo;
	/**
	 * 优化建议详情
	 */
	private String optimizationInfo;
	/**
	 * 视频码率（kbps）
	 */
	private String videoBitRate;
	/**
	 * 视频帧率（次）
	 */
	private String videoFrameRate;
	/**
	 * 丢包率（%）
	 */
	private String lossPageRate = "0";
	/**
	 * i_RTT（秒）
	 */
	private String irtt;
	/**
	 * 音频码率（kbps）
	 */
	private String audioBitRate;
	/**
	 * VMOS值
	 */
	private String vmos;

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param the
	 *            region to set
	 */

	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the vender
	 */
	public String getVender() {
		return vender;
	}

	/**
	 * @param the
	 *            vender to set
	 */

	public void setVender(String vender) {
		this.vender = vender;
	}

	/**
	 * @return the coverScene
	 */
	public String getCoverScene() {
		return coverScene;
	}

	/**
	 * @param the
	 *            coverScene to set
	 */

	public void setCoverScene(String coverScene) {
		this.coverScene = coverScene;
	}

	/**
	 * @return the isBelongtoNetwork
	 */
	public String getIsBelongtoNetwork() {
		return isBelongtoNetwork;
	}

	/**
	 * @param the
	 *            isBelongtoNetwork to set
	 */

	public void setIsBelongtoNetwork(String isBelongtoNetwork) {
		this.isBelongtoNetwork = isBelongtoNetwork;
	}

	/**
	 * @return the boxId
	 */
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param the
	 *            boxId to set
	 */

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the evenName
	 */
	public String getEvenName() {
		return evenName;
	}

	/**
	 * @param the
	 *            evenName to set
	 */

	public void setEvenName(String evenName) {
		this.evenName = evenName;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param the
	 *            reason to set
	 */

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the reasonWireless
	 */
	public String getReasonWireless() {
		return reasonWireless;
	}

	/**
	 * @param the
	 *            reasonWireless to set
	 */

	public void setReasonWireless(String reasonWireless) {
		this.reasonWireless = reasonWireless;
	}

	/**
	 * @return the beginTime
	 */
	public String getBeginTime() {
		return beginTime;
	}

	/**
	 * @param the
	 *            beginTime to set
	 */

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the roadName
	 */
	public String getRoadName() {
		return roadName;
	}

	/**
	 * @param the
	 *            roadName to set
	 */

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param the
	 *            distance to set
	 */

	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the questionTime
	 */
	public String getQuestionTime() {
		return questionTime;
	}

	/**
	 * @param the
	 *            questionTime to set
	 */

	public void setQuestionTime(String questionTime) {
		this.questionTime = questionTime;
	}

	/**
	 * @return the testLogName
	 */
	public String getTestLogName() {
		return testLogName;
	}

	/**
	 * @param the
	 *            testLogName to set
	 */

	public void setTestLogName(String testLogName) {
		this.testLogName = testLogName;
	}

	/**
	 * @return the optimization
	 */
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
	 * @return the cellInfo
	 */
	public String getCellInfo() {
		return cellInfo;
	}

	/**
	 * @param the
	 *            cellInfo to set
	 */

	public void setCellInfo(String cellInfo) {
		this.cellInfo = cellInfo;
	}

	/**
	 * @return the optimizationInfo
	 */
	public String getOptimizationInfo() {
		return optimizationInfo;
	}

	/**
	 * @param the
	 *            optimizationInfo to set
	 */

	public void setOptimizationInfo(String optimizationInfo) {
		this.optimizationInfo = optimizationInfo;
	}

	/**
	 * @return the lossPageRate
	 */
	public String getLossPageRate() {
		return lossPageRate;
	}

	/**
	 * @param the
	 *            lossPageRate to set
	 */

	public void setLossPageRate(String lossPageRate) {
		this.lossPageRate = lossPageRate;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param the
	 *            longitude to set
	 */

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param the
	 *            latitude to set
	 */

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the rsrpAvg
	 */
	public String getRsrpAvg() {
		return rsrpAvg;
	}

	/**
	 * @param the
	 *            rsrpAvg to set
	 */

	public void setRsrpAvg(String rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	/**
	 * @return the rsrpMin
	 */
	public String getRsrpMin() {
		return rsrpMin;
	}

	/**
	 * @param the
	 *            rsrpMin to set
	 */

	public void setRsrpMin(String rsrpMin) {
		this.rsrpMin = rsrpMin;
	}

	/**
	 * @return the sinrAvg
	 */
	public String getSinrAvg() {
		return sinrAvg;
	}

	/**
	 * @param the
	 *            sinrAvg to set
	 */

	public void setSinrAvg(String sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

	/**
	 * @return the sinrMin
	 */
	public String getSinrMin() {
		return sinrMin;
	}

	/**
	 * @param the
	 *            sinrMin to set
	 */

	public void setSinrMin(String sinrMin) {
		this.sinrMin = sinrMin;
	}

	/**
	 * @return the videoBitRate
	 */
	public String getVideoBitRate() {
		return videoBitRate;
	}

	/**
	 * @param the
	 *            videoBitRate to set
	 */

	public void setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
	}

	/**
	 * @return the videoFrameRate
	 */
	public String getVideoFrameRate() {
		return videoFrameRate;
	}

	/**
	 * @param the
	 *            videoFrameRate to set
	 */

	public void setVideoFrameRate(String videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	/**
	 * @return the irtt
	 */
	public String getIrtt() {
		return irtt;
	}

	/**
	 * @param the
	 *            irtt to set
	 */

	public void setIrtt(String irtt) {
		this.irtt = irtt;
	}

	/**
	 * @return the audioBitRate
	 */
	public String getAudioBitRate() {
		return audioBitRate;
	}

	/**
	 * @param the
	 *            audioBitRate to set
	 */

	public void setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
	}

	/**
	 * @return the vmos
	 */
	public String getVmos() {
		return vmos;
	}

	/**
	 * @param the
	 *            vmos to set
	 */

	public void setVmos(String vmos) {
		this.vmos = vmos;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoStatementeResponseBean [region=" + region + ", vender="
				+ vender + ", coverScene=" + coverScene
				+ ", isBelongtoNetwork=" + isBelongtoNetwork + ", boxId="
				+ boxId + ", evenName=" + evenName + ", reason=" + reason
				+ ", reasonWireless=" + reasonWireless + ", beginTime="
				+ beginTime + ", roadName=" + roadName + ", longitude="
				+ longitude + ", latitude=" + latitude + ", distance="
				+ distance + ", questionTime=" + questionTime + ", rsrpAvg="
				+ rsrpAvg + ", rsrpMin=" + rsrpMin + ", sinrAvg=" + sinrAvg
				+ ", sinrMin=" + sinrMin + ", testLogName=" + testLogName
				+ ", optimization=" + optimization + ", cellInfo=" + cellInfo
				+ ", optimizationInfo=" + optimizationInfo + ", videoBitRate="
				+ videoBitRate + ", videoFrameRate=" + videoFrameRate
				+ ", lossPageRate=" + lossPageRate + ", irtt=" + irtt
				+ ", audioBitRate=" + audioBitRate + ", vmos=" + vmos + "]";
	}

}
