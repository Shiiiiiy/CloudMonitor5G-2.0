/**
 * 
 */
package com.datang.domain.domain5g.embbCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * embb覆盖专题优化建议详表----9其他原因，现场复测分析<br>
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_SCE_RT")
public class EmbbCoverSceneRetest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8618961515045006869L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(embb覆盖问题路段)
	 */
	private EmbbCoverBadRoad embbCoverBadRoad;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 调整优先级,天馈调整的优先级，从1开始（优先级最高）
	 */
	private Integer adjustRank;
	/**
	 * 和弱覆盖路段距离(m),该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;
	/**
	 * rsrp均值,问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * sinr均值,问题路段中，该服务小区的SINR均值
	 */
	private Float sinrAvg;
	/**
	 * 天线高度（m）
	 */
	private Float height;
	/**
	 * 工参下倾角
	 */
	private Float downdipAngle;
	/**
	 * 工参方位角
	 */
	private Float azimuth;

	/**
	 * 工参功率（dB）
	 */
	private Float power;

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
	public EmbbCoverBadRoad getEmbbCoverBadRoad() {
		return embbCoverBadRoad;
	}

	public void setEmbbCoverBadRoad(EmbbCoverBadRoad embbCoverBadRoad) {
		this.embbCoverBadRoad = embbCoverBadRoad;
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

	@Column(name = "SINR_AVG")
	public Float getSinrAvg() {
		return sinrAvg;
	}

	public void setSinrAvg(Float sinrAvg) {
		this.sinrAvg = sinrAvg;
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
	 * @return the adjustRankadjustRank
	 */
	@Column(name = "ADJUST_RANK")
	public Integer getAdjustRank() {
		return adjustRank;
	}

	/**
	 * @param adjustRank
	 *            the adjustRank to set
	 */
	public void setAdjustRank(Integer adjustRank) {
		this.adjustRank = adjustRank;
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
	 * @return the downdipAngledowndipAngle
	 */
	@Column(name = "DOWNDIP_ANGLE")
	public Float getDowndipAngle() {
		return downdipAngle;
	}

	/**
	 * @param downdipAngle
	 *            the downdipAngle to set
	 */
	public void setDowndipAngle(Float downdipAngle) {
		this.downdipAngle = downdipAngle;
	}

	@Column(name = "HEIGHT")
	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	@Column(name = "AZIMUTH")
	public Float getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

	@Column(name = "POWER")
	public Float getPower() {
		return power;
	}

	public void setPower(Float power) {
		this.power = power;
	}

}
