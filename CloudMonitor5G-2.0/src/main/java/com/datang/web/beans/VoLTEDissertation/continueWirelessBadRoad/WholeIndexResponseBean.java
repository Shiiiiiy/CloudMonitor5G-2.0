/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.continueWirelessBadRoad;

import java.io.Serializable;

/**
 * 整体分析指标列表响应Bean
 * 
 * @explain
 * @name WholeIndexResponseBean
 * @author shenyanwei
 * @date 2016年5月31日下午3:37:18
 */
public class WholeIndexResponseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3312070523915902271L;

	/**
	 * volte mos均值
	 */
	private Float volteMosAvg;
	/**
	 * 连续无线差距离门限
	 */
	private Float distanceThreshold;
	/**
	 * 连续无线差里程占比
	 */
	private Float mileageProportion;

	/**
	 * rtp丢包率
	 */
	private Float rtpDropRatio;

	/**
	 * 平均rsrp
	 */
	private Float rsrpAvg;
	/**
	 * 平均sinr
	 */
	private Float sinrAvg;

	/**
	 * 
	 * @return the volteMosAvg
	 */
	public Float getVolteMosAvg() {
		return volteMosAvg;
	}

	/**
	 * 
	 * @param volteMosAvg
	 *            the volteMosAvg to set
	 */
	public void setVolteMosAvg(Float volteMosAvg) {
		this.volteMosAvg = volteMosAvg;
	}

	/**
	 * 
	 * @return the distanceThreshold
	 */
	public Float getDistanceThreshold() {
		return distanceThreshold;
	}

	/**
	 * 
	 * @param distanceThreshold
	 *            the distanceThreshold to set
	 */
	public void setDistanceThreshold(Float distanceThreshold) {
		this.distanceThreshold = distanceThreshold;
	}

	/**
	 * 
	 * @return the mileageProportion
	 */
	public Float getMileageProportion() {
		return mileageProportion;
	}

	/**
	 * 
	 * @param mileageProportion
	 *            the mileageProportion to set
	 */
	public void setMileageProportion(Float mileageProportion) {
		this.mileageProportion = mileageProportion;
	}

	/**
	 * 
	 * @return the rtpDropRatio
	 */
	public Float getRtpDropRatio() {
		return rtpDropRatio;
	}

	/**
	 * 
	 * @param rtpDropRatio
	 *            the rtpDropRatio to set
	 */
	public void setRtpDropRatio(Float rtpDropRatio) {
		this.rtpDropRatio = rtpDropRatio;
	}

	/**
	 * 
	 * @return the rsrpAvg
	 */
	public Float getRsrpAvg() {
		return rsrpAvg;
	}

	/**
	 * 
	 * @param rsrpAvg
	 *            the rsrpAvg to set
	 */
	public void setRsrpAvg(Float rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	/**
	 * 
	 * @return the sinrAvg
	 */
	public Float getSinrAvg() {
		return sinrAvg;
	}

	/**
	 * 
	 * @param sinrAvg
	 *            the sinrAvg to set
	 */
	public void setSinrAvg(Float sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

	/**
	 * toString
	 */
	@Override
	public String toString() {
		return "WholeIndexResponseBean [volteMosAvg=" + volteMosAvg
				+ ", distanceThreshold=" + distanceThreshold
				+ ", mileageProportion=" + mileageProportion
				+ ", rtpDropRatio=" + rtpDropRatio + ", rsrpAvg=" + rsrpAvg
				+ ", sinrAvg=" + sinrAvg + "]";
	}

}
