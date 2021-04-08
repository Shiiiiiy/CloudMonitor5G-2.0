/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover;

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
 * 弱覆盖问题路段----天馈接反详情表
 * 
 * @author yinzhipeng
 * @date:2015年9月15日 上午9:39:44
 * @version
 */
@Entity
@Table(name = "IADS_QBR_WEAK_COVER_TK_REV")
public class WeakCoverTianKuiConnectReverse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1426921312947633660L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(弱覆盖问题路段)
	 */
	private VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;

	/**
	 * 工参显示方位角
	 */
	private Float azimuth;

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
	 * @return the volteQualityBadRoadWeakCovervolteQualityBadRoadWeakCover
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadWeakCover getVolteQualityBadRoadWeakCover() {
		return volteQualityBadRoadWeakCover;
	}

	/**
	 * @param volteQualityBadRoadWeakCover
	 *            the volteQualityBadRoadWeakCover to set
	 */
	public void setVolteQualityBadRoadWeakCover(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover) {
		this.volteQualityBadRoadWeakCover = volteQualityBadRoadWeakCover;
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
	 * @return the azimuthazimuth
	 */
	@Column(name = "AZIMUTH")
	public Float getAzimuth() {
		return azimuth;
	}

	/**
	 * @param azimuth
	 *            the azimuth to set
	 */
	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

}
