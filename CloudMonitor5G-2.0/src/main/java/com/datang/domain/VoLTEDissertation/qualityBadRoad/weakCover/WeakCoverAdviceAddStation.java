/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover;

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
 * 弱覆盖问题路段----建议加站问题小区覆盖详情
 * 
 * @author yinzhipeng
 * @date:2015年9月11日 下午4:54:59
 * @version
 */
@Entity
@Table(name = "IADS_QBR_WEAK_COVER_ADD_STA")
public class WeakCoverAdviceAddStation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5456374443501879000L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(弱覆盖问题路段)
	 */
	private VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 和质差路段距离(m),该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;
	/**
	 * 该小区本身站间距(m),该小区和TOP3近的3个邻区的距离均值
	 */
	private Float cellDistance;
	/**
	 * RSRP均值（dBm),以该小区为服务小区时的RSRP均值
	 */
	private Float rsrpAvg;

	/**
	 * 该服务小区的采样点(RSRP):
	 * 采样点数量占整个语音质差路段采样点比例(%),该服务小区的采样点/总的服务小区采样点*100%,采样点集合使用用户选择的当前问题路段
	 */
	// private Long rsrpNum;

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
	 * @return the volteQualityBadRoadWeakCovervolteQualityBadRoadWeakCover
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadWeakCover getVolteQualityBadRoadWeakCover() {
		return volteQualityBadRoadWeakCover;
	}

	/**
	 * @param volteQualityBadRoadWeakCover
	 *            the volteQualityBadRoadWeakCover to set
	 */
	public void setVolteQualityBadRoadWeakCover(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		this.volteQualityBadRoadWeakCover = volteQualityBadRoadWeakCover;
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
	 * @return the cellDistancecellDistance
	 */
	@Column(name = "CELL_DISTANCE")
	public Float getCellDistance() {
		return cellDistance;
	}

	/**
	 * @param cellDistance
	 *            the cellDistance to set
	 */
	public void setCellDistance(Float cellDistance) {
		this.cellDistance = cellDistance;
	}

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
	// @Column(name = "RSRP_NUM")
	// public Long getRsrpNum() {
	// return rsrpNum;
	// }

	/**
	 * @param rsrpNum
	 *            the rsrpNum to set
	 */
	// public void setRsrpNum(Long rsrpNum) {
	// this.rsrpNum = rsrpNum;
	// }

}
