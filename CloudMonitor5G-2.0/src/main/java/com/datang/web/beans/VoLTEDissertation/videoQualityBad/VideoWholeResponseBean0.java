/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.videoQualityBad;

import java.io.Serializable;

/**
 * 视频质差整体分析指标概览响应bean
 * 
 * @explain
 * @name WholeIndexResponseBean
 * @author shenyanwei
 * @date 2017年5月15日上午9:21:48
 */
public class VideoWholeResponseBean0 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5840875542060366728L;
	/**
	 * 问题路段视频码率
	 */
	private Float videoBitRate = 0.0f;
	/**
	 * 问题路段视频帧率
	 */
	private Float videoFrameRate = 0.0f;
	/**
	 * 问题路段语音丢包率
	 */
	private Float packLossRateForVoice = 0.0f;
	/**
	 * 问题路段视频丢包率
	 */
	private Float packLossRateForVideo = 0.0f;
	/**
	 * 问题路段丢包率
	 */
	private Float packLossRate = 0.0f;
	/**
	 * 问题路段i_RTT
	 */
	private Float irtt = 0.0f;
	/**
	 * 问题路段音频码率
	 */
	private Float audioBitRate = 0.0f;
	/**
	 * VMOS
	 */
	private Float vmos = 0.0f;
	/**
	 * 问题路段rsrp均值
	 */
	private Float rsrpValueAvg = 0.0f;
	/**
	 * 问题路段sinr均值
	 */
	private Float sinrValueAvg = 0.0f;

	/**
	 * @return the videoBitRate
	 */
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
	 * @return the vmos
	 */
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
	 * @return the rsrpValueAvg
	 */
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
	 * @return the sinrValueAvg
	 */
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
	 * @return the packLossRateForVoice
	 */
	public Float getPackLossRateForVoice() {
		return packLossRateForVoice;
	}

	/**
	 * @param the
	 *            packLossRateForVoice to set
	 */

	public void setPackLossRateForVoice(Float packLossRateForVoice) {
		this.packLossRateForVoice = packLossRateForVoice;
	}

	/**
	 * @return the packLossRateForVideo
	 */
	public Float getPackLossRateForVideo() {
		return packLossRateForVideo;
	}

	/**
	 * @param the
	 *            packLossRateForVideo to set
	 */

	public void setPackLossRateForVideo(Float packLossRateForVideo) {
		this.packLossRateForVideo = packLossRateForVideo;
	}

	/**
	 * @return the packLossRate
	 */
	public Float getPackLossRate() {
		return packLossRate;
	}

	/**
	 * @param the
	 *            packLossRate to set
	 */

	public void setPackLossRate(Float packLossRate) {
		this.packLossRate = packLossRate;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoWholeResponseBean0 [videoBitRate=" + videoBitRate
				+ ", videoFrameRate=" + videoFrameRate
				+ ", packLossRateForVoice=" + packLossRateForVoice
				+ ", packLossRateForVideo=" + packLossRateForVideo
				+ ", packLossRate=" + packLossRate + ", irtt=" + irtt
				+ ", audioBitRate=" + audioBitRate + ", vmos=" + vmos
				+ ", rsrpValueAvg=" + rsrpValueAvg + ", sinrValueAvg="
				+ sinrValueAvg + "]";
	}

}
