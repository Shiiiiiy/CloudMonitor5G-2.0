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
 * embb覆盖专题优化建议详表----5小区无信号，核查影响无线性能的告警<br>
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_WCC")
public class EmbbCoverWaitCheckCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -695312571613148038L;
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
	 * 待核查邻区友好名
	 */
	private String nb_cellName;
	/**
	 * 待核查邻区CELLID
	 */
	private Long nb_cellId;

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

}
