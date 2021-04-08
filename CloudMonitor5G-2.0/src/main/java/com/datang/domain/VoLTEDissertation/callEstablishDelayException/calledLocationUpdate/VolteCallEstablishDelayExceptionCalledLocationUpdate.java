/**
 * 
 */
package com.datang.domain.VoLTEDissertation.callEstablishDelayException.calledLocationUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;

/**
 * VoLTE专题----呼叫建立时延异常路段分析,被叫位置更新原因指标
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 上午11:14:20
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CEDE_LOC_UPD")
public class VolteCallEstablishDelayExceptionCalledLocationUpdate extends
		VolteCallEstablishDelayException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8326082203392035693L;
	/**
	 * 源TAU
	 */
	private Integer sourceTau;
	/**
	 * 目标TAU
	 */
	private Integer targetTau;
	/**
	 * TAU发起时间
	 */
	private Long tauRequestTime;

	/**
	 * @return the sourceTausourceTau
	 */
	@Column(name = "SOURCE_TAU")
	public Integer getSourceTau() {
		return sourceTau;
	}

	/**
	 * @param sourceTau
	 *            the sourceTau to set
	 */
	public void setSourceTau(Integer sourceTau) {
		this.sourceTau = sourceTau;
	}

	/**
	 * @return the targetTautargetTau
	 */
	@Column(name = "TARGET_TAU")
	public Integer getTargetTau() {
		return targetTau;
	}

	/**
	 * @param targetTau
	 *            the targetTau to set
	 */
	public void setTargetTau(Integer targetTau) {
		this.targetTau = targetTau;
	}

	/**
	 * @return the tauRequestTimetauRequestTime
	 */
	@Column(name = "TAU_REQ_TIME")
	public Long getTauRequestTime() {
		return tauRequestTime;
	}

	/**
	 * @param tauRequestTime
	 *            the tauRequestTime to set
	 */
	public void setTauRequestTime(Long tauRequestTime) {
		this.tauRequestTime = tauRequestTime;
	}

}
