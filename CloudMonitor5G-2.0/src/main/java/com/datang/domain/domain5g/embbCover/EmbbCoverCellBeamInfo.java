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
 * eMBB覆盖专题----弱过重叠覆盖路段覆盖小区波束详表bean
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_CELL_BEAM")
public class EmbbCoverCellBeamInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5447095318354162486L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属小区(embb覆盖问题路段小区)
	 */
	private EmbbCoverCellInfo embbCoverCellInfo;

	/**
	 * 波束编号
	 */
	private Integer beamNo;

	/**
	 * RSRP(dBm)
	 */
	private Float rsrp;
	/**
	 * RSRQ(dBm)
	 */
	private Float rsrq;

	/**
	 * SINR
	 */
	private Float sinr;

	/**
	 * 垂直半功率角
	 */
	private String v_fPowerAngle;
	/**
	 * 水平半功率角
	 */
	private String h_fPowerAngle;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CID")
	@JSON(serialize = false)
	public EmbbCoverCellInfo getEmbbCoverCellInfo() {
		return embbCoverCellInfo;
	}

	public void setEmbbCoverCellInfo(EmbbCoverCellInfo embbCoverCellInfo) {
		this.embbCoverCellInfo = embbCoverCellInfo;
	}

	@Column(name = "BEAM_NO")
	public Integer getBeamNo() {
		return beamNo;
	}

	public void setBeamNo(Integer beamNo) {
		this.beamNo = beamNo;
	}

	@Column(name = "RSRP")
	public Float getRsrp() {
		return rsrp;
	}

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

	@Column(name = "RSRQ")
	public Float getRsrq() {
		return rsrq;
	}

	public void setRsrq(Float rsrq) {
		this.rsrq = rsrq;
	}

	@Column(name = "SINR")
	public Float getSinr() {
		return sinr;
	}

	public void setSinr(Float sinr) {
		this.sinr = sinr;
	}

	@Column(name = "V_F_POWER_ANGLE")
	public String getV_fPowerAngle() {
		return v_fPowerAngle;
	}

	public void setV_fPowerAngle(String v_fPowerAngle) {
		this.v_fPowerAngle = v_fPowerAngle;
	}

	@Column(name = "H_F_POWER_ANGLE")
	public String getH_fPowerAngle() {
		return h_fPowerAngle;
	}

	public void setH_fPowerAngle(String h_fPowerAngle) {
		this.h_fPowerAngle = h_fPowerAngle;
	}

}
