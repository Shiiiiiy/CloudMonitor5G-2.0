/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;

/**
 * VoLTE质量专题----连续无线差路段分析,干扰原因路段指标
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午4:23:57
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CWBR_DIST_PRO")
public class VolteContinueWirelessBadRoadDisturbProblem extends
		VolteContinueWirelessBadRoad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8128104550187039011L;
	/**
	 * 连续无线差路段sinr最小值
	 */
	private Float sinrMin;
	/**
	 * 连续无线差路段优化建议:0三超站点，建议整改;1PCI调整;2天馈调整
	 */
	private Integer optimizeAdvise;
	/**
	 * 连续无线差路段重叠覆盖占比:问题路段中，满足重叠覆盖的采样点/总覆盖采样点*100%
	 */
	private Float overlapCoverRatio;

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

}
