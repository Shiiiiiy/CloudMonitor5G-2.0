/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;

/**
 * VoLTE质量专题----语音质差路段分析,其他问题路段指标
 * 
 * @author yinzhipeng
 * @date:2015年11月4日 下午3:36:23
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_QBR_OTHER")
public class VolteQualityBadRoadOther extends VolteQualityBadRoad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1863289372896032007L;
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
	 * @return the upRBNumupRBNum
	 */
	@Column(name = "UP_RB_NUM")
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
	@Column(name = "DOWN_RB_NUM")
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
	@Column(name = "UP_MSC_NUM")
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
	@Column(name = "DOWN_MSC_NUM")
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
	@Column(name = "UP_PHY_SPEED")
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
	@Column(name = "DOWN_PHY_SPEED")
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
	@Column(name = "UP_PDCP_SPEED")
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
	@Column(name = "DOWN_PDCP_SPEED")
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

}
