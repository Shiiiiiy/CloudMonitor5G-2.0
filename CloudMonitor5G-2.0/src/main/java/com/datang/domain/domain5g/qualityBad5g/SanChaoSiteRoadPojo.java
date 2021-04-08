package com.datang.domain.domain5g.qualityBad5g;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 三超站点整改表实体类
 * @author maxuancheng
 * @date 2019年3月15日
 */
@Entity
@Table(name = "IADS_QUALITY_5G_SANCHAO_SITE")
public class SanChaoSiteRoadPojo implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	
	private Long rid;
	
	private String cellName;
	
	private String cellId;
	
	private Double distanceAvg;
	
	private Double sampleRsrpRatio;
	
	private Double sampleSinrRatio;
	
	private String sanchaoType;

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

	@Column(name = "DISTANCE_AVG")
	public Double getDistanceAvg() {
		return distanceAvg;
	}

	public void setDistanceAvg(Double distanceAvg) {
		this.distanceAvg = distanceAvg;
	}

	@Column(name = "SAMPLE_RSRP_RATIO")
	public Double getSampleRsrpRatio() {
		return sampleRsrpRatio;
	}

	public void setSampleRsrpRatio(Double sampleRsrpRatio) {
		this.sampleRsrpRatio = sampleRsrpRatio;
	}

	@Column(name = "SAMPLE_SINR_RATIO")
	public Double getSampleSinrRatio() {
		return sampleSinrRatio;
	}

	public void setSampleSinrRatio(Double sampleSinrRatio) {
		this.sampleSinrRatio = sampleSinrRatio;
	}

	@Column(name = "SANCHAO_TYPE")
	public String getSanchaoType() {
		return sanchaoType;
	}

	public void setSanchaoType(String sanchaoType) {
		this.sanchaoType = sanchaoType;
	}
	
}
