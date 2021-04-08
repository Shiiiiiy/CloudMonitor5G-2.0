/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency;

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
 * 连续无线差邻区缺失问题路段----LTE邻区添加建议
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午5:17:42
 * @version
 */
@Entity
@Table(name = "IADS_CWBR_NB_DEF_LTE_ADVICE")
public class NbCellLteAddAdviceCWBR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8557078785060122366L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属连续无线差路段(邻区缺失问题路段)
	 */
	private VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 邻区名
	 */
	private String nb_cellName;
	/**
	 * 邻区CELLID
	 */
	private Long nb_cellId;
	/**
	 * 对该连续无线差路段覆盖强度（dBm）,该邻区在该问题路段上的平均RSRP
	 */
	private Float nb_rsrpAvg;
	/**
	 * 和该连续无线差路段距离(m),该邻区与问题路段上各覆盖点的平均距离
	 */
	private Float nb_distanceAvg;

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
	 * @return the
	 *         volteContinueWirelessBadRoadNbDeficiencyvolteContinueWirelessBadRoadNbDeficiency
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteContinueWirelessBadRoadNbDeficiency getVolteContinueWirelessBadRoadNbDeficiency() {
		return volteContinueWirelessBadRoadNbDeficiency;
	}

	/**
	 * @param volteContinueWirelessBadRoadNbDeficiency
	 *            the volteContinueWirelessBadRoadNbDeficiency to set
	 */
	public void setVolteContinueWirelessBadRoadNbDeficiency(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency) {
		this.volteContinueWirelessBadRoadNbDeficiency = volteContinueWirelessBadRoadNbDeficiency;
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
	 * @return the nb_cellNamenb_cellName
	 */
	@Column(name = "NB_CELL_NAME")
	public String getNb_cellName() {
		return nb_cellName;
	}

	/**
	 * @param nb_cellName
	 *            the nb_cellName to set
	 */
	public void setNb_cellName(String nb_cellName) {
		this.nb_cellName = nb_cellName;
	}

	/**
	 * @return the nb_cellIdnb_cellId
	 */
	@Column(name = "NB_CELL_ID")
	public Long getNb_cellId() {
		return nb_cellId;
	}

	/**
	 * @param nb_cellId
	 *            the nb_cellId to set
	 */
	public void setNb_cellId(Long nb_cellId) {
		this.nb_cellId = nb_cellId;
	}

	/**
	 * @return the nb_rsrpAvgnb_rsrpAvg
	 */
	@Column(name = "NB_RSRP_AVG")
	public Float getNb_rsrpAvg() {
		return nb_rsrpAvg;
	}

	/**
	 * @param nb_rsrpAvg
	 *            the nb_rsrpAvg to set
	 */
	public void setNb_rsrpAvg(Float nb_rsrpAvg) {
		this.nb_rsrpAvg = nb_rsrpAvg;
	}

	/**
	 * @return the nb_distanceAvgnb_distanceAvg
	 */
	@Column(name = "NB_DISTANCE_AVG")
	public Float getNb_distanceAvg() {
		return nb_distanceAvg;
	}

	/**
	 * @param nb_distanceAvg
	 *            the nb_distanceAvg to set
	 */
	public void setNb_distanceAvg(Float nb_distanceAvg) {
		this.nb_distanceAvg = nb_distanceAvg;
	}

}
