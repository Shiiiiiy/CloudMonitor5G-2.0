/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 干扰原因问题路段----PCI调整详情表
 * 
 * @author yinzhipeng
 * @date:2015年9月15日 上午10:33:23
 * @version
 */
@Entity
@Table(name = "IADS_QBR_DIST_PRO_PCI_ADJ")
public class DisturbProblemPCIAdjust implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8009752608307156387L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(干扰原因问题路段)
	 */
	private VolteQualityBadRoadDisturbProblem volteQualityBadRoadDisturbProblem;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 原PCI
	 */
	private Float originalPci;
	/**
	 * 和干扰路段的距离,该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;

	/**
	 * 该服务小区的采样点(RSRP):
	 * 采样点数量占整个语音质差路段采样点比例(%),该服务小区的采样点/总的服务小区采样点*100%,采样点集合使用用户选择的当前问题路段
	 */
	// private Float rsrpNum;

	/**
	 * 采样点数量占整个语音质差路段采样点比例
	 */
	private Float sampleRatio;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the
	 *         volteQualityBadRoadDisturbProblemvolteQualityBadRoadDisturbProblem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadDisturbProblem getVolteQualityBadRoadDisturbProblem() {
		return volteQualityBadRoadDisturbProblem;
	}

	/**
	 * @param volteQualityBadRoadDisturbProblem
	 *            the volteQualityBadRoadDisturbProblem to set
	 */
	public void setVolteQualityBadRoadDisturbProblem(
			VolteQualityBadRoadDisturbProblem volteQualityBadRoadDisturbProblem) {
		this.volteQualityBadRoadDisturbProblem = volteQualityBadRoadDisturbProblem;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the originalPcioriginalPci
	 */
	@Column(name = "ORIGINAL_PCI")
	public Float getOriginalPci() {
		return originalPci;
	}

	/**
	 * @param originalPci
	 *            the originalPci to set
	 */
	public void setOriginalPci(Float originalPci) {
		this.originalPci = originalPci;
	}

	/**
	 * @return the distanceAvgdistanceAvg
	 */
	@Column(name = "DISTANCE_AVG")
	public Float getDistanceAvg() {
		return distanceAvg;
	}

	/**
	 * @param distanceAvg
	 *            the distanceAvg to set
	 */
	public void setDistanceAvg(Float distanceAvg) {
		this.distanceAvg = distanceAvg;
	}

	/**
	 * @return the sampleRatiosampleRatio
	 */
	// @Column(name = "SAMPLE_RATIO")
	@Transient
	public Float getSampleRatio() {
		return sampleRatio;
	}

	/**
	 * @param sampleRatio
	 *            the sampleRatio to set
	 */
	public void setSampleRatio(Float sampleRatio) {
		this.sampleRatio = sampleRatio;
	}

	/**
	 * @return the rsrpNumrsrpNum
	 */
	// public Float getRsrpNum() {
	// return rsrpNum;
	// }

	/**
	 * @param rsrpNum
	 *            the rsrpNum to set
	 */
	// public void setRsrpNum(Float rsrpNum) {
	// this.rsrpNum = rsrpNum;
	// }

}
