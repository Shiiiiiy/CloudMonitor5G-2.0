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
 * 呼叫建立时延异常弱覆盖路段----天馈调整详情表
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 下午1:26:36
 * @version
 */
@Entity
@Table(name = "IADS_CEDE_WEAK_COVER_TK_ADJ")
public class WeakCoverTianKuiAdjustCEDE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2863592535890750437L;
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
	 * 调整优先级,天馈调整的优先级，从1开始（优先级最高）
	 */
	private Integer adjustRank;
	/**
	 * 和质差距离(m),该服务小区与各采样点问题路段的平均距离(采样点集合使用用户选择的当前问题路段)
	 */
	private Float distanceAvg;
	/**
	 * rsrp均值,问题路段中，该服务小区的RSRP均值
	 */
	private Float rsrpAvg;
	/**
	 * 工参下倾角
	 */
	private Float downdipAngle;

	/**
	 * 工参方位角
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
	 * @return the adjustRankadjustRank
	 */
	@Column(name = "ADJUST_RANK")
	public Integer getAdjustRank() {
		return adjustRank;
	}

	/**
	 * @param adjustRank
	 *            the adjustRank to set
	 */
	public void setAdjustRank(Integer adjustRank) {
		this.adjustRank = adjustRank;
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
	 * @return the downdipAngledowndipAngle
	 */
	@Column(name = "DOWNDIP_ANGLE")
	public Float getDowndipAngle() {
		return downdipAngle;
	}

	/**
	 * @param downdipAngle
	 *            the downdipAngle to set
	 */
	public void setDowndipAngle(Float downdipAngle) {
		this.downdipAngle = downdipAngle;
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
