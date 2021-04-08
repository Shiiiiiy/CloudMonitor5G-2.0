/**
 * 
 */
package com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;

/**
 * VoLTE专题----呼叫建立时延异常路段分析,重叠覆盖原因指标
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 上午11:11:28
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CEDE_OVER_CO")
public class VolteCallEstablishDelayExceptionOverlapCover extends
		VolteCallEstablishDelayException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7916374589116218635L;
	/**
	 * 呼叫建立时延异常路段sinr均值
	 */
	private Float sinrAvg;

	/**
	 * 呼叫建立时延异常路段重叠覆盖占比:问题路段中，满足重叠覆盖的采样点/总覆盖采样点*100%
	 */
	private Float overlapCoverRatio;

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
	 * @return the optimizeAdviseoptimizeAdvise
	 */

}
