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
 * 呼叫建立时延异常弱覆盖路段----建议加站问题小区覆盖详情
 * 
 * @author yinzhipeng
 * @date:2016年5月20日 下午1:21:38
 * @version
 */
@Entity
@Table(name = "IADS_CEDE_WEAK_COVER_ADD_STA")
public class WeakCoverAdviceAddStationCEDE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5266018169464580184L;
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
	 * 和质差路段距离(m),该服务小区与各采样点问题路段的平均距离
	 */
	private Float distanceAvg;
	/**
	 * 该小区本身站间距(m),该小区和TOP3近的3个邻区的距离均值
	 */
	private Float cellDistance;
	/**
	 * RSRP均值（dBm),以该小区为服务小区时的RSRP均值
	 */
	private Float rsrpAvg;

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
	 * @return the cellDistancecellDistance
	 */
	@Column(name = "CELL_DISTANCE")
	public Float getCellDistance() {
		return cellDistance;
	}

	/**
	 * @param cellDistance
	 *            the cellDistance to set
	 */
	public void setCellDistance(Float cellDistance) {
		this.cellDistance = cellDistance;
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

}
