/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem;

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
 * 其他原因问题路段----小区详情列表
 * 
 * @author yinzhipeng
 * @date:2015年9月15日 上午11:02:31
 * @version
 */
@Entity
@Table(name = "IADS_QBR_OTHER_CELL")
public class OtherProblemCellInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5668543270861385773L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(其他原因问题路段)
	 */
	private VolteQualityBadRoadOther volteQualityBadRoadOther;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
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
	 * 质差区域采样点数与总采样点数占比:该小区在当前选择问题路段的采样点数/该小区在当前路段所属日志总采样点数
	 */
	private Float cellNumRatio;

	/**
	 * RSRP均值(dBm),问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * SINR均值,问题路段中，该服务小区的SINR均值
	 */
	private Float sinrAvg;

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
	 * @return the volteQualityBadRoadOthervolteQualityBadRoadOther
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadOther getVolteQualityBadRoadOther() {
		return volteQualityBadRoadOther;
	}

	/**
	 * @param volteQualityBadRoadOther
	 *            the volteQualityBadRoadOther to set
	 */
	public void setVolteQualityBadRoadOther(
			VolteQualityBadRoadOther volteQualityBadRoadOther) {
		this.volteQualityBadRoadOther = volteQualityBadRoadOther;
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
	 * @return the cellNumRationcellNumRation
	 */
	// @Column(name = "CELL_NUM_RATIO")
	@Transient
	public Float getCellNumRatio() {
		return cellNumRatio;
	}

	/**
	 * @param cellNumRation
	 *            the cellNumRation to set
	 */
	public void setCellNumRatio(Float cellNumRatio) {
		this.cellNumRatio = cellNumRatio;
	}

}
