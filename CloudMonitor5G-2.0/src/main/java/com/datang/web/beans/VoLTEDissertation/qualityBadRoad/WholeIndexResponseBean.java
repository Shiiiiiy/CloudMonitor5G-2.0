/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.qualityBadRoad;

import java.io.Serializable;

/**
 * 整体分析指标呈现响应bean
 * 
 * @author yinzhipeng
 * @date:2015年12月14日 上午9:54:41
 * @version
 */
public class WholeIndexResponseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3312070523915902271L;
	/**
	 * 日志类型,0原始日志,1对比日志,2汇总
	 */
	private Integer testLogType;
	/**
	 * 主被叫
	 */
	private String callType;
	/**
	 * volte mos均值
	 */
	private Float volteMosAvg;
	/**
	 * 初始BLER
	 */
	private Float firstBler;

	/**
	 * 残留BLER
	 */
	private Float residualBler;
	/**
	 * rtp丢包率
	 */
	private Float rtpDropRatio;
	/**
	 * rtp抖动
	 */
	private Float rtpShake;
	/**
	 * TDL驻网时长
	 */
	private Float tdlTestTime;
	/**
	 * TDS驻网时长
	 */
	private Float tdsTestTime;
	/**
	 * GSM驻网时长
	 */
	private Float gsmTestTime;
	/**
	 * MOS>=3.0占比
	 */
	private Float psMosOver30Rate;
	/**
	 * MOS>=3.5占比
	 */
	private Float psMosOver35Rate;

	/**
	 * LTE HO成功率
	 */
	private Float lteHoSuccRate;
	/**
	 * 平均rsrp
	 */
	private Float rsrpAvg;
	/**
	 * 平均sinr
	 */
	private Float sinrAvg;
	/**
	 * LTE覆盖率
	 */
	private Float lteCoverRate;

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
	 * @return the volteMosAvgvolteMosAvg
	 */
	public Float getVolteMosAvg() {
		return volteMosAvg;
	}

	/**
	 * @param volteMosAvg
	 *            the volteMosAvg to set
	 */
	public void setVolteMosAvg(Float volteMosAvg) {
		this.volteMosAvg = volteMosAvg;
	}

	/**
	 * @return the firstBlerfirstBler
	 */
	public Float getFirstBler() {
		return firstBler;
	}

	/**
	 * @param firstBler
	 *            the firstBler to set
	 */
	public void setFirstBler(Float firstBler) {
		this.firstBler = firstBler;
	}

	/**
	 * @return the residualBlerresidualBler
	 */
	public Float getResidualBler() {
		return residualBler;
	}

	/**
	 * @param residualBler
	 *            the residualBler to set
	 */
	public void setResidualBler(Float residualBler) {
		this.residualBler = residualBler;
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

	/**
	 * @return the tdlTestTimetdlTestTime
	 */
	public Float getTdlTestTime() {
		return tdlTestTime;
	}

	/**
	 * @param tdlTestTime
	 *            the tdlTestTime to set
	 */
	public void setTdlTestTime(Float tdlTestTime) {
		this.tdlTestTime = tdlTestTime;
	}

	/**
	 * @return the tdsTestTimetdsTestTime
	 */
	public Float getTdsTestTime() {
		return tdsTestTime;
	}

	/**
	 * @param tdsTestTime
	 *            the tdsTestTime to set
	 */
	public void setTdsTestTime(Float tdsTestTime) {
		this.tdsTestTime = tdsTestTime;
	}

	/**
	 * @return the gsmTestTimegsmTestTime
	 */
	public Float getGsmTestTime() {
		return gsmTestTime;
	}

	/**
	 * @param gsmTestTime
	 *            the gsmTestTime to set
	 */
	public void setGsmTestTime(Float gsmTestTime) {
		this.gsmTestTime = gsmTestTime;
	}

	/**
	 * @return the psMosOver30RatepsMosOver30Rate
	 */
	public Float getPsMosOver30Rate() {
		return psMosOver30Rate;
	}

	/**
	 * @param psMosOver30Rate
	 *            the psMosOver30Rate to set
	 */
	public void setPsMosOver30Rate(Float psMosOver30Rate) {
		this.psMosOver30Rate = psMosOver30Rate;
	}

	/**
	 * @return the psMosOver35RatepsMosOver35Rate
	 */
	public Float getPsMosOver35Rate() {
		return psMosOver35Rate;
	}

	/**
	 * @param psMosOver35Rate
	 *            the psMosOver35Rate to set
	 */
	public void setPsMosOver35Rate(Float psMosOver35Rate) {
		this.psMosOver35Rate = psMosOver35Rate;
	}

	/**
	 * @return the lteHoSuccRatelteHoSuccRate
	 */
	public Float getLteHoSuccRate() {
		return lteHoSuccRate;
	}

	/**
	 * @param lteHoSuccRate
	 *            the lteHoSuccRate to set
	 */
	public void setLteHoSuccRate(Float lteHoSuccRate) {
		this.lteHoSuccRate = lteHoSuccRate;
	}

	/**
	 * @return the rsrpAvgrsrpAvg
	 */
	public Float getRsrpAvg() {
		return rsrpAvg;
	}

	/**
	 * @param rsrpAvg
	 *            the rsrpAvg to set
	 */
	public void setRsrpAvg(Float rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	/**
	 * @return the sinrAvgsinrAvg
	 */
	public Float getSinrAvg() {
		return sinrAvg;
	}

	/**
	 * @param sinrAvg
	 *            the sinrAvg to set
	 */
	public void setSinrAvg(Float sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

	/**
	 * @return the lteCoverRatelteCoverRate
	 */
	public Float getLteCoverRate() {
		return lteCoverRate;
	}

	/**
	 * @param lteCoverRate
	 *            the lteCoverRate to set
	 */
	public void setLteCoverRate(Float lteCoverRate) {
		this.lteCoverRate = lteCoverRate;
	}

	/**
	 * @return the testLogTypetestLogType
	 */
	public Integer getTestLogType() {
		return testLogType;
	}

	/**
	 * @param testLogType
	 *            the testLogType to set
	 */
	public void setTestLogType(Integer testLogType) {
		this.testLogType = testLogType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WholeIndexResponseBean [testLogType=" + testLogType
				+ ", callType=" + callType + ", volteMosAvg=" + volteMosAvg
				+ ", firstBler=" + firstBler + ", residualBler=" + residualBler
				+ ", rtpDropRatio=" + rtpDropRatio + ", rtpShake=" + rtpShake
				+ ", tdlTestTime=" + tdlTestTime + ", tdsTestTime="
				+ tdsTestTime + ", gsmTestTime=" + gsmTestTime
				+ ", psMosOver30Rate=" + psMosOver30Rate + ", psMosOver35Rate="
				+ psMosOver35Rate + ", lteHoSuccRate=" + lteHoSuccRate
				+ ", rsrpAvg=" + rsrpAvg + ", sinrAvg=" + sinrAvg
				+ ", lteCoverRate=" + lteCoverRate + "]";
	}

}
