/**
 * 
 */
package com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover;

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
 * 呼叫建立时延异常弱覆盖路段----天馈接反详情表
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 下午1:26:47
 * @version
 */
@Entity
@Table(name = "IADS_CEDE_WEAK_COVER_TK_REV")
public class WeakCoverTianKuiConnectReverseCEDE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8525653223085121489L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属呼叫建立时延异常路段(弱覆盖问题路段)
	 */
	private VolteCallEstablishDelayExceptionWeakCover volteCallEstablishDelayExceptionWeakCover;
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
	 * @return the
	 *         volteCallEstablishDelayExceptionWeakCovervolteCallEstablishDelayExceptionWeakCover
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteCallEstablishDelayExceptionWeakCover getVolteCallEstablishDelayExceptionWeakCover() {
		return volteCallEstablishDelayExceptionWeakCover;
	}

	/**
	 * @param volteCallEstablishDelayExceptionWeakCover
	 *            the volteCallEstablishDelayExceptionWeakCover to set
	 */
	public void setVolteCallEstablishDelayExceptionWeakCover(
			VolteCallEstablishDelayExceptionWeakCover volteCallEstablishDelayExceptionWeakCover) {
		this.volteCallEstablishDelayExceptionWeakCover = volteCallEstablishDelayExceptionWeakCover;
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
