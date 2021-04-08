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
@Table(name = "IADS_QUALITY_5G_CELL_BEAM")
public class BadRoadCellBeamPojo implements Serializable{

	/**
	 * @author maxuancheng
	 */
	private static final long serialVersionUID = -5988918072637870872L;

	/**
	 * 
	 */
	private Long id;
	
	/**
	 * 小区id
	 */
	private Long cid;
	
	/**
	 * 编号
	 */
	private Long beamNo;
	
	/**
	 * rsrp
	 */
	private Float rsrp;
	
	/**
	 * sinr
	 */
	private Float sinr;
	
	/**
	 * rsrp
	 */
	private Float rsrq;
	
	/**
	 * 垂直半功率角
	 */
	private String v_fPowerAngle;
	/**
	 * 水平半功率角
	 */
	private String h_fPowerAngle;

	@Column(name = "V_FPOWERANGLE")
	public String getV_fPowerAngle() {
		return v_fPowerAngle;
	}

	public void setV_fPowerAngle(String v_fPowerAngle) {
		this.v_fPowerAngle = v_fPowerAngle;
	}

	@Column(name = "H_FPOWERANGLE")
	public String getH_fPowerAngle() {
		return h_fPowerAngle;
	}

	public void setH_fPowerAngle(String h_fPowerAngle) {
		this.h_fPowerAngle = h_fPowerAngle;
	}

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CID")
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	@Column(name = "BEAM_NO")
	public Long getBeamNo() {
		return beamNo;
	}

	public void setBeamNo(Long beamNo) {
		this.beamNo = beamNo;
	}

	@Column(name = "RSRP")
	public Float getRsrp() {
		return rsrp;
	}

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

	@Column(name = "SINR")
	public Float getSinr() {
		return sinr;
	}

	public void setSinr(Float sinr) {
		this.sinr = sinr;
	}

	@Column(name = "RSRQ")
	public Float getRsrq() {
		return rsrq;
	}

	public void setRsrq(Float rsrq) {
		this.rsrq = rsrq;
	}
	
}
