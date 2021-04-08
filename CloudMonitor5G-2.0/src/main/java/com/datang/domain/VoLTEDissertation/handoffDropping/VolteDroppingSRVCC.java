package com.datang.domain.VoLTEDissertation.handoffDropping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SRVCC切换失败事件
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午2:39:40
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_DROPPING_SRVCC")
public class VolteDroppingSRVCC extends VolteDropping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2561863643215883411L;
	/**
	 * GSM小区频点
	 */
	private Long gsmFreq;

	/**
	 * @return the gsmFreqgsmFreq
	 */
	@Column(name = "GSM_FREQ")
	public Long getGsmFreq() {
		return gsmFreq;
	}

	/**
	 * @param gsmFreq
	 *            the gsmFreq to set
	 */
	public void setGsmFreq(Long gsmFreq) {
		this.gsmFreq = gsmFreq;
	}

}
