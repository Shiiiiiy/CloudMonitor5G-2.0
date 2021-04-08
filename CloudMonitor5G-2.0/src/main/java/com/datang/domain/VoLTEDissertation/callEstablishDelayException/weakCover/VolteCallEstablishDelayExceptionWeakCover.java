/**
 * 
 */
package com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;

/**
 * VoLTE专题----呼叫建立时延异常路段分析,弱覆盖原因指标
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 上午11:06:20
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CEDE_WEAK_CO")
public class VolteCallEstablishDelayExceptionWeakCover extends
		VolteCallEstablishDelayException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4833586491657639829L;
	/**
	 * 呼叫建立时延异常路段RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * 呼叫建立时延异常路段优化建议:0建议加站;1反向覆盖，建议调整;2天馈调整;
	 */
	private Integer optimizeAdvise;

	/**
	 * @return the rsrpAvgrsrpAvg
	 */
	@Column(name = "RSRP_AVG")
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

}
