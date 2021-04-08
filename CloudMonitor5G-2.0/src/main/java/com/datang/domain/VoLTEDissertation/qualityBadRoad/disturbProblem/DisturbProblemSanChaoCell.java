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
 * 干扰原因问题路段----三超小区详情
 * 
 * @author yinzhipeng
 * @date:2015年9月15日 上午10:25:13
 * @version
 */
@Entity
@Table(name = "IADS_QBR_DIST_PRO_SC_CELL")
public class DisturbProblemSanChaoCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5901364695175380919L;
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
	 * 和质差距离(m),该服务小区与各采样点问题路段的平均距离(采样点集合使用用户选择的当前问题路段)
	 */
	private Float distanceAvg;
	/**
	 * SINR均值,问题路段中，该服务小区的SINR均值
	 */
	private Float sinrAvg;

	/**
	 * 三超类型,“0超高”、“1超远”、“2超近”
	 */
	private Integer sanChaoType;

	/**
	 * 该服务小区的采样点(RSRP):
	 * 采样点数量占整个语音质差路段采样点比例(%),该服务小区的采样点/总的服务小区采样点*100%,采样点集合使用用户选择的当前问题路段
	 */
	// private Float rsrpNum;
	/**
	 * 采样点数量占整个语音质差路段采样点比例(%)
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
	 * @return the sanChaoTypesanChaoType
	 */
	@Column(name = "SC_TYPE")
	public Integer getSanChaoType() {
		return sanChaoType;
	}

	/**
	 * @param sanChaoType
	 *            the sanChaoType to set
	 */
	public void setSanChaoType(Integer sanChaoType) {
		this.sanChaoType = sanChaoType;
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
