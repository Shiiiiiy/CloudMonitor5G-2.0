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
 * embb覆盖专题优化建议详表----10小区方位角不合理，调整邻区方位角<br>
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_NB_A_ADJ")
public class EmbbCoverNbCellAzimuthAdjust implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7008500542499442391L;
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
	 * 占用波束RSRP（dBm）,问题路段上，终端占用波束的SS-RSRP求平均
	 */
	private Float beamRsrpAvg;

	/**
	 * 邻区名
	 */
	private String nb_cellName;
	/**
	 * 邻区CELLID
	 */
	private Long nb_cellId;

	/**
	 * 邻区RSRP（dBm）
	 */
	private Float nb_rsrpAvg;

	/**
	 * 邻区方位角
	 */
	private Float nb_azimuth;

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

	@Column(name = "BEAM_RSRP")
	public Float getBeamRsrpAvg() {
		return beamRsrpAvg;
	}

	public void setBeamRsrpAvg(Float beamRsrpAvg) {
		this.beamRsrpAvg = beamRsrpAvg;
	}

	@Column(name = "NB_CELL_NAME")
	public String getNb_cellName() {
		return nb_cellName;
	}

	public void setNb_cellName(String nb_cellName) {
		this.nb_cellName = nb_cellName;
	}

	@Column(name = "NB_CELL_ID")
	public Long getNb_cellId() {
		return nb_cellId;
	}

	public void setNb_cellId(Long nb_cellId) {
		this.nb_cellId = nb_cellId;
	}

	@Column(name = "NB_RSRP")
	public Float getNb_rsrpAvg() {
		return nb_rsrpAvg;
	}

	public void setNb_rsrpAvg(Float nb_rsrpAvg) {
		this.nb_rsrpAvg = nb_rsrpAvg;
	}

	@Column(name = "NB_AZIMUTH")
	public Float getNb_azimuth() {
		return nb_azimuth;
	}

	public void setNb_azimuth(Float nb_azimuth) {
		this.nb_azimuth = nb_azimuth;
	}

}
