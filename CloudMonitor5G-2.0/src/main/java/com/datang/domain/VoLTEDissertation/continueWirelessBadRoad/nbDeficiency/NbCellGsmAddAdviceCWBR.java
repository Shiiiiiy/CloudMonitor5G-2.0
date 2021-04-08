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
 * 连续无线差邻区缺失路段----GSM邻区添加建议
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午5:17:50
 * @version
 */
@Entity
@Table(name = "IADS_CWBR_NB_DEF_GSM_ADVICE")
public class NbCellGsmAddAdviceCWBR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1763529126185195969L;
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
