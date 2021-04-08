/**
 * 
 */
package com.datang.domain.VoLTEDissertation.exceptionEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VoLTE质量专题----异常事件分析,csfb失败事件指标bean
 * 
 * @author yinzhipeng
 * @date:2016年4月15日 下午1:54:55
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_EE_CSFB_FAIL")
public class VolteExceptionEventCsfbFail extends VolteExceptionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5691213968937214987L;

	/**
	 * RSRP均值
	 */
	private Float rsrpAvg;

	/**
	 * SINR均值
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VolteExceptionEventCsfbFail [rsrpAvg=" + rsrpAvg + ", sinrAvg="
				+ sinrAvg + "]";
	}

}
