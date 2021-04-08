/**
 * 
 */
package com.datang.domain.VoLTEDissertation.exceptionEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VoLTE质量专题----异常事件分析,IMS注册失败事件指标bean
 * 
 * @author yinzhipeng
 * @date:2016年4月16日 上午9:25:59
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_EE_IMS_FAIL")
public class VolteExceptionEventImsRegistFail extends VolteExceptionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5691213968937214987L;

	/**
	 * RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * RSRP最低值
	 */
	private Float rsrpMin;
	/**
	 * SINR均值
	 */
	private Float sinrAvg;
	/**
	 * SINR最低值
	 */
	private Float sinrMin;
	/**
	 * 重叠覆盖率
	 */
	private Float overlappingCoverageRate;

	/**
	 * 失败类型:0Attach失败,1默认承载建立失败,2IMS注册失败
	 */
	private Integer failType;

	/**
	 * 失败信令节点:0'随机接入',1'RRC建立',2'鉴权',3'QCI=9的专载建立'<br>
	 * 4'QCI=5的专载建立',5'IMS注册'
	 */
	private Integer failSignallingNode;

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
	 * @return the overlappingCoverageRateoverlappingCoverageRate
	 */
	@Column(name = "OVER_COVER_RATE")
	public Float getOverlappingCoverageRate() {
		return overlappingCoverageRate;
	}

	/**
	 * @param overlappingCoverageRate
	 *            the overlappingCoverageRate to set
	 */
	public void setOverlappingCoverageRate(Float overlappingCoverageRate) {
		this.overlappingCoverageRate = overlappingCoverageRate;
	}

	/**
	 * @return the failTypefailType
	 */
	@Column(name = "FAIL_TYPE")
	public Integer getFailType() {
		return failType;
	}

	/**
	 * @param failType
	 *            the failType to set
	 */
	public void setFailType(Integer failType) {
		this.failType = failType;
	}

	/**
	 * @return the failSignallingNodefailSignallingNode
	 */
	@Column(name = "FAIL_SIGNALLING_NODE")
	public Integer getFailSignallingNode() {
		return failSignallingNode;
	}

	/**
	 * @param failSignallingNode
	 *            the failSignallingNode to set
	 */
	public void setFailSignallingNode(Integer failSignallingNode) {
		this.failSignallingNode = failSignallingNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VolteExceptionEventImsRegistFail [rsrpAvg=" + rsrpAvg
				+ ", rsrpMin=" + rsrpMin + ", sinrAvg=" + sinrAvg
				+ ", sinrMin=" + sinrMin + ", overlappingCoverageRate="
				+ overlappingCoverageRate + ", failType=" + failType
				+ ", failSignallingNode=" + failSignallingNode + "]";
	}

}
