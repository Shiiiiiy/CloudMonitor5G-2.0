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
 * embb覆盖专题优化建议详表----6超高站，降低天线的高度<br>
 * 
 * @author _YZP
 * 
 */
@Entity
@Table(name = "IADS_5G_EMBB_HEC")
public class EmbbCoverHeightErrorCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8840910740054852084L;
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
	 * 天线高度（m）
	 */
	private Float height;

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

	@Column(name = "HEIGHT")
	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

}
