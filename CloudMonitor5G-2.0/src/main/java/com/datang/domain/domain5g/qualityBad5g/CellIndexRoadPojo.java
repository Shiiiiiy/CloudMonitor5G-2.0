package com.datang.domain.domain5g.qualityBad5g;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 小区指标列表实体类
 * @author maxuancheng
 * @date 2019年3月15日
 */
@Entity
@Table(name = "IADS_QUALITY_5G_CELL_INDEX")
public class CellIndexRoadPojo  implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	/**
	 * 路段id
	 */
	private Long rid;

	/**
	 * 小区友好名
	 */
	private String cellName;

	/**
	 * 小区ID
	 */
	private String cellId;

	/**
	 * RSRP(DBM)
	 */
	private Double rsrpAvg;

	/**
	 * rsrq
	 */
	private Double rsrqAvg;

	/**
	 * sinr
	 */
	private Double sinrAvg;

	/**
	 * 波束配置
	 */
	private String beamConfig;

	/**
	 * 过覆盖占比
	 */
	private Double highCoverRatio;

	/**
	 * 重叠覆盖占比
	 */
	private Double overlayCoverRatio;

	/**
	 * 持续距离
	 */
	private Double continuedDistance;

	/**
	 * 测试时长
	 */
	private Double testDuration;
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RID")
	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "CELL_ID")
	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	@Column(name = "RSRP_AVG")
	public Double getRsrpAvg() {
		return rsrpAvg;
	}

	public void setRsrpAvg(Double rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	@Column(name = "RSRQ_AVG")
	public Double getRsrqAvg() {
		return rsrqAvg;
	}

	public void setRsrqAvg(Double rsrqAvg) {
		this.rsrqAvg = rsrqAvg;
	}

	@Column(name = "SINR_AVG")
	public Double getSinrAvg() {
		return sinrAvg;
	}

	public void setSinrAvg(Double sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

	@Column(name = "BEAM_CONFIG")
	public String getBeamConfig() {
		return beamConfig;
	}

	public void setBeamConfig(String beamConfig) {
		this.beamConfig = beamConfig;
	}

	@Column(name = "HIGH_COVER_RATIO")
	public Double getHighCoverRatio() {
		return highCoverRatio;
	}

	public void setHighCoverRatio(Double highCoverRatio) {
		this.highCoverRatio = highCoverRatio;
	}

	@Column(name = "OVERLAY_COVER_RATIO")
	public Double getOverlayCoverRatio() {
		return overlayCoverRatio;
	}

	public void setOverlayCoverRatio(Double overlayCoverRatio) {
		this.overlayCoverRatio = overlayCoverRatio;
	}

	@Column(name = "CONTINUED_DISTANCE")
	public Double getContinuedDistance() {
		return continuedDistance;
	}

	public void setContinuedDistance(Double continuedDistance) {
		this.continuedDistance = continuedDistance;
	}

	@Column(name = "TEST_DURATION")
	public Double getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(Double testDuration) {
		this.testDuration = testDuration;
	}
	
}
