/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.qualityBadRoad;

import java.io.Serializable;

/**
 * 弱覆盖指标呈现响应bean
 * 
 * @author yinzhipeng
 * @date:2015年11月13日 上午9:16:47
 * @version
 */
public class WeakCoverIndexResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8704470059528476914L;
	/**
	 * mos均值
	 */
	private Float mosAvg;
	/**
	 * 主被叫
	 */
	private String callType;

	/**
	 * 码字0BLER:初始BLER
	 */
	private Float codon0FirstBler;

	/**
	 * 码字0BLER:残留BLER
	 */
	private Float codon0ResidualBler;
	/**
	 * 码字1BLER:初始BLER
	 */
	private Float codon1FirstBler;

	/**
	 * 码字1BLER:残留BLER
	 */
	private Float codon1ResidualBler;
	/**
	 * rtp丢包率
	 */
	private Float rtpDropRatio;
	/**
	 * rtp抖动
	 */
	private Float rtpShake;

	/**
	 * @return the mosAvgmosAvg
	 */
	public Float getMosAvg() {
		return mosAvg;
	}

	/**
	 * @param mosAvg
	 *            the mosAvg to set
	 */
	public void setMosAvg(Float mosAvg) {
		this.mosAvg = mosAvg;
	}

	/**
	 * @return the callTypecallType
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * @param callType
	 *            the callType to set
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * @return the codon0FirstBlercodon0FirstBler
	 */
	public Float getCodon0FirstBler() {
		return codon0FirstBler;
	}

	/**
	 * @param codon0FirstBler
	 *            the codon0FirstBler to set
	 */
	public void setCodon0FirstBler(Float codon0FirstBler) {
		this.codon0FirstBler = codon0FirstBler;
	}

	/**
	 * @return the codon0ResidualBlercodon0ResidualBler
	 */
	public Float getCodon0ResidualBler() {
		return codon0ResidualBler;
	}

	/**
	 * @param codon0ResidualBler
	 *            the codon0ResidualBler to set
	 */
	public void setCodon0ResidualBler(Float codon0ResidualBler) {
		this.codon0ResidualBler = codon0ResidualBler;
	}

	/**
	 * @return the codon1FirstBlercodon1FirstBler
	 */
	public Float getCodon1FirstBler() {
		return codon1FirstBler;
	}

	/**
	 * @param codon1FirstBler
	 *            the codon1FirstBler to set
	 */
	public void setCodon1FirstBler(Float codon1FirstBler) {
		this.codon1FirstBler = codon1FirstBler;
	}

	/**
	 * @return the codon1ResidualBlercodon1ResidualBler
	 */
	public Float getCodon1ResidualBler() {
		return codon1ResidualBler;
	}

	/**
	 * @param codon1ResidualBler
	 *            the codon1ResidualBler to set
	 */
	public void setCodon1ResidualBler(Float codon1ResidualBler) {
		this.codon1ResidualBler = codon1ResidualBler;
	}

	/**
	 * @return the rtpDropRatiortpDropRatio
	 */
	public Float getRtpDropRatio() {
		return rtpDropRatio;
	}

	/**
	 * @param rtpDropRatio
	 *            the rtpDropRatio to set
	 */
	public void setRtpDropRatio(Float rtpDropRatio) {
		this.rtpDropRatio = rtpDropRatio;
	}

	/**
	 * @return the rtpShakertpShake
	 */
	public Float getRtpShake() {
		return rtpShake;
	}

	/**
	 * @param rtpShake
	 *            the rtpShake to set
	 */
	public void setRtpShake(Float rtpShake) {
		this.rtpShake = rtpShake;
	}

}
