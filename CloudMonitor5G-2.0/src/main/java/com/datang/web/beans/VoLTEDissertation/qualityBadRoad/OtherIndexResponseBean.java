/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.qualityBadRoad;

import java.io.Serializable;

/**
 * 其他问题路段指标呈现响应bean
 * 
 * @author yinzhipeng
 * @date:2015年12月4日 上午11:18:45
 * @version
 */
public class OtherIndexResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2941222414219228027L;
	/**
	 * mos均值
	 */
	private Float mosAvg;
	/**
	 * 上行RB数
	 */
	private Float upRBNum;
	/**
	 * 下行RB数
	 */
	private Float downRBNum;
	/**
	 * 上行MCS均值
	 */
	private Float upMSCAvg;
	/**
	 * 下行MCS均值
	 */
	private Float downMSCAvg;
	/**
	 * 上行PHY速率(kbps)
	 */
	private Float upPHYSpeed;
	/**
	 * 下行PHY速率(kbps)
	 */
	private Float downPHYSpeed;
	/**
	 * 上行PDCP速率(kbps)
	 */
	private Float upPDCPSpeed;
	/**
	 * 下行PDCP速率(kbps)
	 */
	private Float downPDCPSpeed;

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
	 * @return the upRBNumupRBNum
	 */
	public Float getUpRBNum() {
		return upRBNum;
	}

	/**
	 * @param upRBNum
	 *            the upRBNum to set
	 */
	public void setUpRBNum(Float upRBNum) {
		this.upRBNum = upRBNum;
	}

	/**
	 * @return the downRBNumdownRBNum
	 */
	public Float getDownRBNum() {
		return downRBNum;
	}

	/**
	 * @param downRBNum
	 *            the downRBNum to set
	 */
	public void setDownRBNum(Float downRBNum) {
		this.downRBNum = downRBNum;
	}

	/**
	 * @return the upMSCAvgupMSCAvg
	 */
	public Float getUpMSCAvg() {
		return upMSCAvg;
	}

	/**
	 * @param upMSCAvg
	 *            the upMSCAvg to set
	 */
	public void setUpMSCAvg(Float upMSCAvg) {
		this.upMSCAvg = upMSCAvg;
	}

	/**
	 * @return the downMSCAvgdownMSCAvg
	 */
	public Float getDownMSCAvg() {
		return downMSCAvg;
	}

	/**
	 * @param downMSCAvg
	 *            the downMSCAvg to set
	 */
	public void setDownMSCAvg(Float downMSCAvg) {
		this.downMSCAvg = downMSCAvg;
	}

	/**
	 * @return the upPHYSpeedupPHYSpeed
	 */
	public Float getUpPHYSpeed() {
		return upPHYSpeed;
	}

	/**
	 * @param upPHYSpeed
	 *            the upPHYSpeed to set
	 */
	public void setUpPHYSpeed(Float upPHYSpeed) {
		this.upPHYSpeed = upPHYSpeed;
	}

	/**
	 * @return the downPHYSpeeddownPHYSpeed
	 */
	public Float getDownPHYSpeed() {
		return downPHYSpeed;
	}

	/**
	 * @param downPHYSpeed
	 *            the downPHYSpeed to set
	 */
	public void setDownPHYSpeed(Float downPHYSpeed) {
		this.downPHYSpeed = downPHYSpeed;
	}

	/**
	 * @return the upPDCPSpeedupPDCPSpeed
	 */
	public Float getUpPDCPSpeed() {
		return upPDCPSpeed;
	}

	/**
	 * @param upPDCPSpeed
	 *            the upPDCPSpeed to set
	 */
	public void setUpPDCPSpeed(Float upPDCPSpeed) {
		this.upPDCPSpeed = upPDCPSpeed;
	}

	/**
	 * @return the downPDCPSpeeddownPDCPSpeed
	 */
	public Float getDownPDCPSpeed() {
		return downPDCPSpeed;
	}

	/**
	 * @param downPDCPSpeed
	 *            the downPDCPSpeed to set
	 */
	public void setDownPDCPSpeed(Float downPDCPSpeed) {
		this.downPDCPSpeed = downPDCPSpeed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OtherIndexResponseBean [mosAvg=" + mosAvg + ", upRBNum="
				+ upRBNum + ", downRBNum=" + downRBNum + ", upMSCAvg="
				+ upMSCAvg + ", downMSCAvg=" + downMSCAvg + ", upPHYSpeed="
				+ upPHYSpeed + ", downPHYSpeed=" + downPHYSpeed
				+ ", upPDCPSpeed=" + upPDCPSpeed + ", downPDCPSpeed="
				+ downPDCPSpeed + "]";
	}

}
