/**
 * 
 */
package com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover;

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
 * 连续无线差弱覆盖路段----问题路段服务小区信息
 * 
 * @author yinzhipeng
 * @date:2016年5月19日 下午5:21:34
 * @version
 */
@Entity
@Table(name = "IADS_CWBR_WEAK_COVER_CELL")
public class WeakCoverCellInfoCWBR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3453955203432550961L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属连续无线差路段(弱覆盖问题路段)
	 */
	private VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * rsrp均值,问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * rsrp最小值,问题路段中，该服务小区的RSRP最低值
	 */
	private Float rsrpMin;
	/**
	 * 持续距离(m),该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;
	/**
	 * 测试时间(s),以该小区为服务小区的时长
	 */
	private Float testTime;

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
	 *         volteContinueWirelessBadRoadWeakCovervolteContinueWirelessBadRoadWeakCover
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public VolteContinueWirelessBadRoadWeakCover getVolteContinueWirelessBadRoadWeakCover() {
		return volteContinueWirelessBadRoadWeakCover;
	}

	/**
	 * @param volteContinueWirelessBadRoadWeakCover
	 *            the volteContinueWirelessBadRoadWeakCover to set
	 */
	public void setVolteContinueWirelessBadRoadWeakCover(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover) {
		this.volteContinueWirelessBadRoadWeakCover = volteContinueWirelessBadRoadWeakCover;
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
	 * @return the rsrpMinrsrpMin
	 */
	@Column(name = "RSRP_MIN")
	public Float getRsrpMin() {
		return rsrpMin;
	}

	/**
	 * @param rsrpMin
	 *            the rsrpMin to set
	 */
	public void setRsrpMin(Float rsrpMin) {
		this.rsrpMin = rsrpMin;
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

	/**
	 * @return the testTimetestTime
	 */
	@Column(name = "TEST_TIME")
	public Float getTestTime() {
		return testTime;
	}

	/**
	 * @param testTime
	 *            the testTime to set
	 */
	public void setTestTime(Float testTime) {
		this.testTime = testTime;
	}

}
