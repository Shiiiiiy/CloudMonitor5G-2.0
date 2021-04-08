/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;

/**
 * VoLTE质量专题----连续无线差路段分析,弱覆盖路段指标
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午4:12:26
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_CWBR_WEAK_CO")
public class VolteContinueWirelessBadRoadWeakCover extends
		VolteContinueWirelessBadRoad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5393665202465355681L;
	/**
	 * 连续无线差路段RSRP最小值
	 */
	private Float rsrpMin;
	/**
	 * 连续无线差路段优化建议:0建议加站;1反向覆盖，建议调整;2天馈调整;
	 */
	private Integer optimizeAdvise;

	/**
	 * @return the rsrpMinrsrpMin
	 */
	@Column(name = "RSRP_MIN")
	public Float getRsrpMin() {
		return rsrpMin;
	}

	/**
	 * @param rsrpMin
	 *            the rsrpMin to set
	 */
	public void setRsrpMin(Float rsrpMin) {
		this.rsrpMin = rsrpMin;
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
