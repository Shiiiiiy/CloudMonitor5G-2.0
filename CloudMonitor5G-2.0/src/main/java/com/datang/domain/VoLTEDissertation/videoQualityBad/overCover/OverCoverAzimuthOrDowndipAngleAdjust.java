package com.datang.domain.VoLTEDissertation.videoQualityBad.overCover;

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
 * @explain 重叠覆盖问题 ----优化建议方位角下倾角调整
 * @name OverCoverAzimuthOrDowndipAngleAdjust
 * @author shenyanwei
 * @date 2017年5月11日上午9:25:21
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_QB_OC_AODAA")
public class OverCoverAzimuthOrDowndipAngleAdjust implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7706411237033491439L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题(弱覆盖)
	 */
	private VideoQualityBadOverCover videoQualityBadOverCover;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;
	/**
	 * PCI
	 */
	private Long pci;
	/**
	 * EARFCN
	 */
	private Long earfcn;
	/**
	 * RSRP
	 */
	private Float rsrp;
	/**
	 * 和问题采样点距离(m)
	 */
	private Float toProblemDotDistance;
	/**
	 * 工参方位角）
	 */
	private Float projectAzimuth;
	/**
	 * 工参下倾角
	 */
	private Float projectDowndipAngle;

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
	public VideoQualityBadOverCover getVideoQualityBadOverCover() {
		return videoQualityBadOverCover;
	}

	/**
	 * @param volteQualityBadRoadNbDeficiency
	 *            the volteQualityBadRoadNbDeficiency to set
	 */
	public void setVideoQualityBadOverCover(
			VideoQualityBadOverCover videoQualityBadOverCover) {
		this.videoQualityBadOverCover = videoQualityBadOverCover;
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
	 * @return the pci
	 */
	@Column(name = "PCI")
	public Long getPci() {
		return pci;
	}

	/**
	 * @param the
	 *            pci to set
	 */

	public void setPci(Long pci) {
		this.pci = pci;
	}

	/**
	 * @return the earfcn
	 */
	@Column(name = "EARFCN")
	public Long getEarfcn() {
		return earfcn;
	}

	/**
	 * @param the
	 *            earfcn to set
	 */

	public void setEarfcn(Long earfcn) {
		this.earfcn = earfcn;
	}

	/**
	 * @return the rsrp
	 */
	@Column(name = "RSRP")
	public Float getRsrp() {
		return rsrp;
	}

	/**
	 * @param the
	 *            rsrp to set
	 */

	public void setRsrp(Float rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * @return the toProblemDotDistance
	 */
	@Column(name = "DISTANCE")
	public Float getToProblemDotDistance() {
		return toProblemDotDistance;
	}

	/**
	 * @param the
	 *            toProblemDotDistance to set
	 */

	public void setToProblemDotDistance(Float toProblemDotDistance) {
		this.toProblemDotDistance = toProblemDotDistance;
	}

	/**
	 * @return the projectDowndipAngle
	 */
	@Column(name = "DOWNDIP_ANGLE")
	public Float getProjectDowndipAngle() {
		return projectDowndipAngle;
	}

	/**
	 * @param the
	 *            projectDowndipAngle to set
	 */

	public void setProjectDowndipAngle(Float projectDowndipAngle) {
		this.projectDowndipAngle = projectDowndipAngle;
	}

	/**
	 * @return the projectAzimuth
	 */
	@Column(name = "AZIMUTH")
	public Float getProjectAzimuth() {
		return projectAzimuth;
	}

	/**
	 * @param the
	 *            projectAzimuth to set
	 */

	public void setProjectAzimuth(Float projectAzimuth) {
		this.projectAzimuth = projectAzimuth;
	}

}
