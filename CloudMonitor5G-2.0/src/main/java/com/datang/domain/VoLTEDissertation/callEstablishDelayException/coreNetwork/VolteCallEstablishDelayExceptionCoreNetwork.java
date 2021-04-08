/**
 * 
 */
package com.datang.domain.VoLTEDissertation.callEstablishDelayException.coreNetwork;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;

/**
 * VoLTE专题----呼叫建立时延异常路段分析,核心网原因指标
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 上午11:13:24
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CEDE_CORE_NET")
public class VolteCallEstablishDelayExceptionCoreNetwork extends
		VolteCallEstablishDelayException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8202864620343306554L;
	/**
	 * 呼叫建立时延异常路段rsrp均值
	 */
	private Float rsrpAvg;
	/**
	 * 呼叫建立时延异常路段sinr均值
	 */
	private Float sinrAvg;

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

}
