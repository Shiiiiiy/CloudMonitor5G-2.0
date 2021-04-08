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
 * embb覆盖专题优化建议详表----1邻区缺失，建议添加邻区<br>
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_ADD_NC")
public class EmbbCoverAddNbCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4710714417309945748L;
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
	 * 邻区名
	 */
	private String nb_cellName;
	/**
	 * 邻区CELLID
	 */
	private Long nb_cellId;

	/**
	 * 站间距,服务小区与建议添加邻小区的有效距离
	 */
	private Float distance;
	/**
	 * SC-PCI
	 */
	private Long pci;
	/**
	 * NC-PCI
	 */
	private Long nb_pci;

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

	@Column(name = "DISTANCE")
	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
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

	@Column(name = "PCI")
	public Long getPci() {
		return pci;
	}

	public void setPci(Long pci) {
		this.pci = pci;
	}

	@Column(name = "NB_PCI")
	public Long getNb_pci() {
		return nb_pci;
	}

	public void setNb_pci(Long nb_pci) {
		this.nb_pci = nb_pci;
	}

}
