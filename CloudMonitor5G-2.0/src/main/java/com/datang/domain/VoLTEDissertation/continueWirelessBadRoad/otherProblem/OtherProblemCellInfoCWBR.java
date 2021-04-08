/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.otherProblem;

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
 * 连续无线差其他原因问题路段----小区详情列表
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午5:32:40
 * @version
 */
@Entity
@Table(name = "IADS_CWBR_OTHER_CELL")
public class OtherProblemCellInfoCWBR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -768067788922904584L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属连续无线差路段(其他原因问题路段)
	 */
	private VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;

	/**
	 * RSRP均值(dBm),问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * SINR均值,问题路段中，该服务小区的SINR均值
	 */
	private Float sinrAvg;

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
	 *         volteContinueWirelessBadRoadOtherProblemvolteContinueWirelessBadRoadOtherProblem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteContinueWirelessBadRoadOtherProblem getVolteContinueWirelessBadRoadOtherProblem() {
		return volteContinueWirelessBadRoadOtherProblem;
	}

	/**
	 * @param volteContinueWirelessBadRoadOtherProblem
	 *            the volteContinueWirelessBadRoadOtherProblem to set
	 */
	public void setVolteContinueWirelessBadRoadOtherProblem(
			VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem) {
		this.volteContinueWirelessBadRoadOtherProblem = volteContinueWirelessBadRoadOtherProblem;
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
	 * @return the rsrpAvgrsrpAvg
	 */
	@Column(name = "RSRP_AVG")
	public Float getRsrpAvg() {
		return rsrpAvg;
	}

	/**
	 * @param rsrpAvg
	 *            the rsrpAvg to set
	 */
	public void setRsrpAvg(Float rsrpAvg) {
		this.rsrpAvg = rsrpAvg;
	}

	/**
	 * @return the sinrAvgsinrAvg
	 */
	@Column(name = "SINR_AVG")
	public Float getSinrAvg() {
		return sinrAvg;
	}

	/**
	 * @param sinrAvg
	 *            the sinrAvg to set
	 */
	public void setSinrAvg(Float sinrAvg) {
		this.sinrAvg = sinrAvg;
	}

}
