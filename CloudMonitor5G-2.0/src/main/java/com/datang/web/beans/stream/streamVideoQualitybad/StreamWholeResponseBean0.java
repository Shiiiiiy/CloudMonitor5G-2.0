/**
 * 
 */
package com.datang.web.beans.stream.streamVideoQualitybad;

import java.io.Serializable;

/**
 * 流媒体视频质差整体分析指标概览响应bean
 * 
 * @explain
 * @name StreamWholeResponseBean0
 * @author shenyanwei
 * @date 2017年10月23日下午1:40:52
 */
public class StreamWholeResponseBean0 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5840875542060366728L;

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
	 * 卡顿比例均值
	 */
	private Float stallingratioAvg;
	/**
	 * 初始缓冲时延均值
	 */
	private Float initialbuffertimeAvg;
	/**
	 * 视频全程感知速率均值
	 */
	private Float videoresolutionAvg;

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
	 * @return the stallingratioAvg
	 */
	public Float getStallingratioAvg() {
		return stallingratioAvg;
	}

	/**
	 * @param the
	 *            stallingratioAvg to set
	 */

	public void setStallingratioAvg(Float stallingratioAvg) {
		this.stallingratioAvg = stallingratioAvg;
	}

	/**
	 * @return the initialbuffertimeAvg
	 */
	public Float getInitialbuffertimeAvg() {
		return initialbuffertimeAvg;
	}

	/**
	 * @param the
	 *            initialbuffertimeAvg to set
	 */

	public void setInitialbuffertimeAvg(Float initialbuffertimeAvg) {
		this.initialbuffertimeAvg = initialbuffertimeAvg;
	}

	/**
	 * @return the videoresolutionAvg
	 */
	public Float getVideoresolutionAvg() {
		return videoresolutionAvg;
	}

	/**
	 * @param the
	 *            videoresolutionAvg to set
	 */

	public void setVideoresolutionAvg(Float videoresolutionAvg) {
		this.videoresolutionAvg = videoresolutionAvg;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StreamWholeResponseBean0 [vmos=" + vmos + ", rsrpValueAvg="
				+ rsrpValueAvg + ", sinrValueAvg=" + sinrValueAvg
				+ ", stallingratioAvg=" + stallingratioAvg
				+ ", initialbuffertimeAvg=" + initialbuffertimeAvg
				+ ", videoresolutionAvg=" + videoresolutionAvg + "]";
	}

}
