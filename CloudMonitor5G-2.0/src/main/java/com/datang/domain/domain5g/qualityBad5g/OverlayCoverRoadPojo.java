package com.datang.domain.domain5g.qualityBad5g;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 建议天馈调整_重叠覆盖表实体类
 * @author maxuancheng
 * @date 2019年3月15日
 */
@Entity
@Table(name = "IADS_QUALITY_5G_OVERLAY_COVER")
public class OverlayCoverRoadPojo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long rid;
	
	private String adjustPriority;
	
	private String cellName;
	
	private String cellId;
	
	private Double distanceAvg;
	
	private Double overlayCoverSampleRatio;
	
	private Double sampleRsrpMeat;
	
	private Double sampleSinrMeat;
	
	private String paramDowntilt;
	
	private String paramAzimuth;

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

	@Column(name = "ADJUST_PRIORITY")
	public String getAdjustPriority() {
		return adjustPriority;
	}

	public void setAdjustPriority(String adjustPriority) {
		this.adjustPriority = adjustPriority;
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

	@Column(name = "DISTANCE_AVG")
	public Double getDistanceAvg() {
		return distanceAvg;
	}

	public void setDistanceAvg(Double distanceAvg) {
		this.distanceAvg = distanceAvg;
	}

	@Column(name = "OVERLAY_COVER_SAMPLE_RATIO")
	public Double getOverlayCoverSampleRatio() {
		return overlayCoverSampleRatio;
	}

	public void setOverlayCoverSampleRatio(Double overlayCoverSampleRatio) {
		this.overlayCoverSampleRatio = overlayCoverSampleRatio;
	}

	@Column(name = "SAMPLE_RSRP_MEAT")
	public Double getSampleRsrpMeat() {
		return sampleRsrpMeat;
	}

	public void setSampleRsrpMeat(Double sampleRsrpMeat) {
		this.sampleRsrpMeat = sampleRsrpMeat;
	}

	@Column(name = "SAMPLE_SINR_MEAT")
	public Double getSampleSinrMeat() {
		return sampleSinrMeat;
	}

	public void setSampleSinrMeat(Double sampleSinrMeat) {
		this.sampleSinrMeat = sampleSinrMeat;
	}

	@Column(name = "PARAM_DOWNTILT")
	public String getParamDowntilt() {
		return paramDowntilt;
	}

	public void setParamDowntilt(String paramDowntilt) {
		this.paramDowntilt = paramDowntilt;
	}

	@Column(name = "PARAM_AZIMUTH")
	public String getParamAzimuth() {
		return paramAzimuth;
	}

	public void setParamAzimuth(String paramAzimuth) {
		this.paramAzimuth = paramAzimuth;
	}
}
