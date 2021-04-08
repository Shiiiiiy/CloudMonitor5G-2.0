/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;

/**
 * VoLTE质量专题----语音质差路段分析,干扰原因路段指标
 * 
 * @author yinzhipeng
 * @date:2015年11月4日 下午2:54:23
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_QBR_DIST_PRO")
public class VolteQualityBadRoadDisturbProblem extends VolteQualityBadRoad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2862476855891077162L;
	/**
	 * 问题路段sinr均值
	 */
	private Float sinrAvg;
	/**
	 * 问题路段sinr最小值
	 */
	private Float sinrMin;
	/**
	 * 问题路段优化建议:0三超站点，建议整改;1PCI调整;2天馈调整
	 */
	private Integer optimizeAdvise;
	/**
	 * 问题路段重叠覆盖占比:问题路段中，满足重叠覆盖的采样点/总覆盖采样点*100%
	 */
	private Float overlapCoverRatio;

	// 初始BLER =（初传次数-初传成功次数）/初传次数*100% @date 20160324 废弃
	// 残留BLER =（初传次数-多次重传后成功次数）/初传次数*100% @date 20160324 废弃
	/**
	 * 码字0BLER:初始BLER,残留BLER:初传次数<BR>
	 * @date 20160324 废弃
	 */
	private Long codon0BlerFirstRequestNum;
	/**
	 * 码字0BLER:初始BLER:初传成功次数<BR>
	 * @date 20160324 废弃
	 */
	private Long codon0BlerFirstSuccessNum;
	/**
	 * 码字0BLER:残留BLER:多次重传后成功次数<BR>
	 * @date 20160324 废弃
	 */
	private Long codon0BlerAgainSuccessNum;
	/**
	 * 码字1BLER:初始BLER,残留BLER:初传次数<BR>
	 * @date 20160324 废弃
	 */
	private Long codon1BlerFirstRequestNum;
	/**
	 * 码字1BLER:初始BLER:初传成功次数<BR>
	 * @date 20160324 废弃
	 */
	private Long codon1BlerFirstSuccessNum;
	/**
	 * 码字1BLER:残留BLER:多次重传后成功次数<BR>
	 * @date 20160324 废弃
	 */
	private Long codon1BlerAgainSuccessNum;
	
	
	/**
	 * 码字0BLER:初始BLER<br>
	 * @date 20160324 新增
	 */
	private Float code0lBler;
	/**
	 * 码字0BLER:残留BLER<br>
	 * @date 20160324 新增
	 */
	private Float code0rBler;
	/**
	 * 码字1BLER:初始BLER<br>
	 * @date 20160324 新增
	 */
	private Float code1lBler;
	/**
	 * 码字1BLER:残留BLER<br>
	 * @date 20160324 新增
	 */
	private Float code1rBler;

	/**
	 * @return the sinrAvgsinrAvg
	 */
	@Column(name = "SINR_AVG")
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
	 * @return the sinrMinsinrMin
	 */
	@Column(name = "SINR_MIN")
	public Float getSinrMin() {
		return sinrMin;
	}

	/**
	 * @param sinrMin
	 *            the sinrMin to set
	 */
	public void setSinrMin(Float sinrMin) {
		this.sinrMin = sinrMin;
	}

	/**
	 * @return the optimizeAdviseoptimizeAdvise
	 */
	@Column(name = "OPTIMIZE_ADVISE")
	public Integer getOptimizeAdvise() {
		return optimizeAdvise;
	}

	/**
	 * @param optimizeAdvise
	 *            the optimizeAdvise to set
	 */
	public void setOptimizeAdvise(Integer optimizeAdvise) {
		this.optimizeAdvise = optimizeAdvise;
	}

	/**
	 * @return the overlapCoverRatiooverlapCoverRatio
	 */
	@Column(name = "OVERLAPCOVER_RATIO")
	public Float getOverlapCoverRatio() {
		return overlapCoverRatio;
	}

	/**
	 * @param overlapCoverRatio
	 *            the overlapCoverRatio to set
	 */
	public void setOverlapCoverRatio(Float overlapCoverRatio) {
		this.overlapCoverRatio = overlapCoverRatio;
	}

	/**
	 * @return the codon0BlerFirstRequestNumcodon0BlerFirstRequestNum
	 */
	@Column(name = "CODON0_BLER_FIRST_REQ_NUM")
	public Long getCodon0BlerFirstRequestNum() {
		return codon0BlerFirstRequestNum;
	}

	/**
	 * @param codon0BlerFirstRequestNum
	 *            the codon0BlerFirstRequestNum to set
	 */
	public void setCodon0BlerFirstRequestNum(Long codon0BlerFirstRequestNum) {
		this.codon0BlerFirstRequestNum = codon0BlerFirstRequestNum;
	}

	/**
	 * @return the codon0BlerFirstSuccessNumcodon0BlerFirstSuccessNum
	 */
	@Column(name = "CODON0_BLER_FIRST_SUCC_NUM")
	public Long getCodon0BlerFirstSuccessNum() {
		return codon0BlerFirstSuccessNum;
	}

	/**
	 * @param codon0BlerFirstSuccessNum
	 *            the codon0BlerFirstSuccessNum to set
	 */
	public void setCodon0BlerFirstSuccessNum(Long codon0BlerFirstSuccessNum) {
		this.codon0BlerFirstSuccessNum = codon0BlerFirstSuccessNum;
	}

	/**
	 * @return the codon0BlerAgainSuccessNumcodon0BlerAgainSuccessNum
	 */
	@Column(name = "CODON0_BLER_AGAIN_SUCC_NUM")
	public Long getCodon0BlerAgainSuccessNum() {
		return codon0BlerAgainSuccessNum;
	}

	/**
	 * @param codon0BlerAgainSuccessNum
	 *            the codon0BlerAgainSuccessNum to set
	 */
	public void setCodon0BlerAgainSuccessNum(Long codon0BlerAgainSuccessNum) {
		this.codon0BlerAgainSuccessNum = codon0BlerAgainSuccessNum;
	}

	/**
	 * @return the codon1BlerFirstRequestNumcodon1BlerFirstRequestNum
	 */
	@Column(name = "CODON1_BLER_FIRST_REQ_NUM")
	public Long getCodon1BlerFirstRequestNum() {
		return codon1BlerFirstRequestNum;
	}

	/**
	 * @param codon1BlerFirstRequestNum
	 *            the codon1BlerFirstRequestNum to set
	 */
	public void setCodon1BlerFirstRequestNum(Long codon1BlerFirstRequestNum) {
		this.codon1BlerFirstRequestNum = codon1BlerFirstRequestNum;
	}

	/**
	 * @return the codon1BlerFirstSuccessNumcodon1BlerFirstSuccessNum
	 */
	@Column(name = "CODON1_BLER_FIRST_SUCC_NUM")
	public Long getCodon1BlerFirstSuccessNum() {
		return codon1BlerFirstSuccessNum;
	}

	/**
	 * @param codon1BlerFirstSuccessNum
	 *            the codon1BlerFirstSuccessNum to set
	 */
	public void setCodon1BlerFirstSuccessNum(Long codon1BlerFirstSuccessNum) {
		this.codon1BlerFirstSuccessNum = codon1BlerFirstSuccessNum;
	}

	/**
	 * @return the codon1BlerAgainSuccessNumcodon1BlerAgainSuccessNum
	 */
	@Column(name = "CODON1_BLER_AGAIN_SUCC_NUM")
	public Long getCodon1BlerAgainSuccessNum() {
		return codon1BlerAgainSuccessNum;
	}

	/**
	 * @param codon1BlerAgainSuccessNum
	 *            the codon1BlerAgainSuccessNum to set
	 */
	public void setCodon1BlerAgainSuccessNum(Long codon1BlerAgainSuccessNum) {
		this.codon1BlerAgainSuccessNum = codon1BlerAgainSuccessNum;
	}
	
	@Column(name = "CODE0_IBLER")
	public Float getCode0lBler() {
		return code0lBler;
	}

	public void setCode0lBler(Float code0lBler) {
		this.code0lBler = code0lBler;
	}

	@Column(name = "CODE0_RBLER")
	public Float getCode0rBler() {
		return code0rBler;
	}

	public void setCode0rBler(Float code0rBler) {
		this.code0rBler = code0rBler;
	}

	@Column(name = "CODE1_IBLER")
	public Float getCode1lBler() {
		return code1lBler;
	}

	public void setCode1lBler(Float code1lBler) {
		this.code1lBler = code1lBler;
	}

	@Column(name = "CODE1_RBLER")
	public Float getCode1rBler() {
		return code1rBler;
	}

	public void setCode1rBler(Float code1rBler) {
		this.code1rBler = code1rBler;
	}

}
