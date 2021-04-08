/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem;

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
 * 连续无线差干扰原因问题路段----PCI调整详情表
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午5:36:48
 * @version
 */
@Entity
@Table(name = "IADS_CWBR_DIST_PRO_PCI_ADJ")
public class DisturbProblemPCIAdjustCWBR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7447749014154203444L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属连续无线差路段(干扰原因问题路段)
	 */
	private VolteContinueWirelessBadRoadDisturbProblem volteContinueWirelessBadRoadDisturbProblem;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * 原PCI
	 */
	private Float originalPci;
	/**
	 * 和干扰路段的距离,该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;

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
	 *         volteContinueWirelessBadRoadDisturbProblemvolteContinueWirelessBadRoadDisturbProblem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteContinueWirelessBadRoadDisturbProblem getVolteContinueWirelessBadRoadDisturbProblem() {
		return volteContinueWirelessBadRoadDisturbProblem;
	}

	/**
	 * @param volteContinueWirelessBadRoadDisturbProblem
	 *            the volteContinueWirelessBadRoadDisturbProblem to set
	 */
	public void setVolteContinueWirelessBadRoadDisturbProblem(
			VolteContinueWirelessBadRoadDisturbProblem volteContinueWirelessBadRoadDisturbProblem) {
		this.volteContinueWirelessBadRoadDisturbProblem = volteContinueWirelessBadRoadDisturbProblem;
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
	 * @return the originalPcioriginalPci
	 */
	@Column(name = "ORIGINAL_PCI")
	public Float getOriginalPci() {
		return originalPci;
	}

	/**
	 * @param originalPci
	 *            the originalPci to set
	 */
	public void setOriginalPci(Float originalPci) {
		this.originalPci = originalPci;
	}

	/**
	 * @return the distanceAvgdistanceAvg
	 */
	@Column(name = "DISTANCE_AVG")
	public Float getDistanceAvg() {
		return distanceAvg;
	}

	/**
	 * @param distanceAvg
	 *            the distanceAvg to set
	 */
	public void setDistanceAvg(Float distanceAvg) {
		this.distanceAvg = distanceAvg;
	}

}
