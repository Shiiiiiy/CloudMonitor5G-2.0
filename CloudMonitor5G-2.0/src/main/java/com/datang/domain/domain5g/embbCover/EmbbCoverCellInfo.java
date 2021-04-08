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
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * eMBB覆盖专题----弱过重叠覆盖路段覆盖小区详表bean
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_CELL")
public class EmbbCoverCellInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5447095318354162486L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(embb覆盖问题路段)
	 */
	private EmbbCoverBadRoad embbCoverBadRoad;
	/**
	 * 小区友好名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * RSRP均值(dBm),问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * RSRP最低值(dBm),问题路段中，该服务小区的RSRP最低值
	 */
	private Float rsrpMin;

	/**
	 * SINR均值(dBm),问题路段中，该服务小区的SINR均值
	 */
	private Float sinrAvg;
	/**
	 * SINR最低值(dBm),问题路段中，该服务小区的SINR最低值
	 */
	private Float sinrMin;

	/**
	 * 波束指标
	 */
	private String beamIndex = "波束详情";
	/**
	 * 弱覆盖过覆盖重叠覆盖占比（%）
	 */
	private Float coverRate;

	/**
	 * 持续距离(m),该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;
	/**
	 * 弱覆盖过覆盖重叠覆盖路段持续距离占比（%）
	 */
	private Float distanceRate;
	/**
	 * 测试时长(s),以该小区为服务小区的时长
	 */
	private Float testTime;

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
	 * @return the testTimetestTime
	 */
	@Column(name = "TEST_TIME")
	public Float getTestTime() {
		return testTime;
	}

	/**
	 * @param testTime
	 *            the testTime to set
	 */
	public void setTestTime(Float testTime) {
		this.testTime = testTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public EmbbCoverBadRoad getEmbbCoverBadRoad() {
		return embbCoverBadRoad;
	}

	public void setEmbbCoverBadRoad(EmbbCoverBadRoad embbCoverBadRoad) {
		this.embbCoverBadRoad = embbCoverBadRoad;
	}

	@Column(name = "SINR_AVG")
	public Float getSinrAvg() {
		return sinrAvg;
	}

	public void setSinrAvg(Float sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

	@Column(name = "SINR_MIN")
	public Float getSinrMin() {
		return sinrMin;
	}

	public void setSinrMin(Float sinrMin) {
		this.sinrMin = sinrMin;
	}

	@Column(name = "COVER_RATE")
	public Float getCoverRate() {
		return coverRate;
	}

	public void setCoverRate(Float coverRate) {
		this.coverRate = coverRate;
	}

	@Column(name = "DISTANCE_RATE")
	public Float getDistanceRate() {
		return distanceRate;
	}

	public void setDistanceRate(Float distanceRate) {
		this.distanceRate = distanceRate;
	}

	@Transient
	public String getBeamIndex() {
		return beamIndex;
	}

	public void setBeamIndex(String beamIndex) {
		this.beamIndex = beamIndex;
	}

}
