/**
 * 
 */
package com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency;

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
 * 邻区缺失问题路段----GSM邻区添加建议
 * 
 * @author yinzhipeng
 * @date:2015年9月11日 下午4:03:11
 * @version
 */
@Entity
@Table(name = "IADS_QBR_NB_DEF_GSM_ADVICE")
public class NbCellGsmAddAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3926198047662970916L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题路段(邻区缺失问题路段)
	 */
	private VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 建议添加的GSM频点号
	 */
	private Long addGSMFreqNum;

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
	 *         volteQualityBadRoadNbDeficiencyvolteQualityBadRoadNbDeficiency
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteQualityBadRoadNbDeficiency getVolteQualityBadRoadNbDeficiency() {
		return volteQualityBadRoadNbDeficiency;
	}

	/**
	 * @param volteQualityBadRoadNbDeficiency
	 *            the volteQualityBadRoadNbDeficiency to set
	 */
	public void setVolteQualityBadRoadNbDeficiency(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency) {
		this.volteQualityBadRoadNbDeficiency = volteQualityBadRoadNbDeficiency;
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
	 * @return the addGSMFreqNumaddGSMFreqNum
	 */
	@Column(name = "ADD_GSM_FREQ_NUM")
	public Long getAddGSMFreqNum() {
		return addGSMFreqNum;
	}

	/**
	 * @param addGSMFreqNum
	 *            the addGSMFreqNum to set
	 */
	public void setAddGSMFreqNum(Long addGSMFreqNum) {
		this.addGSMFreqNum = addGSMFreqNum;
	}

}
