package com.datang.domain.stream.streamVideoQualitybad.neighbourPlot;

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
 * 流媒体视频质差邻区问题---优化建议添加邻区
 * 
 * @explain
 * @name StreamNeighbourAdvice
 * @author shenyanwei
 * @date 2017年10月20日下午3:55:23
 */
@Entity
@Table(name = "STREAM_BQ_NCELL_ADVICE")
public class StreamNeighbourAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -822565425485061373L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题(邻区)
	 */
	private StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot;
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
	 * 邻区名
	 */
	private String ncellName;
	/**
	 * 邻区CELLID
	 */
	private Long ncellId;
	/**
	 * 邻区PCI
	 */
	private Long npci;
	/**
	 * 邻区EARFCN
	 */
	private Long nearfcn;
	/**
	 * 邻区RSRP
	 */
	private Float nrsrp;
	/**
	 * 邻区和问题采样点距离(m)
	 */
	private Float ntoProblemDotDistance;
	/**
	 * 持续时间
	 */
	private Long duration;

	/**
	 * @return the duration
	 */
	@Column(name = "DURATION")
	public Long getDuration() {
		return duration;
	}

	/**
	 * @param the
	 *            duration to set
	 */

	public void setDuration(Long duration) {
		this.duration = duration;
	}

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
	public StreamQualityBadNeighbourPlot getStreamQualityBadNeighbourPlot() {
		return streamQualityBadNeighbourPlot;
	}

	/**
	 * @param volteQualityBadRoadNbDeficiency
	 *            the volteQualityBadRoadNbDeficiency to set
	 */
	public void setStreamQualityBadNeighbourPlot(
			StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot) {
		this.streamQualityBadNeighbourPlot = streamQualityBadNeighbourPlot;
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
	 * @return the ncellName
	 */
	@Column(name = "NCELL_NAME")
	public String getNcellName() {
		return ncellName;
	}

	/**
	 * @param the
	 *            ncellName to set
	 */

	public void setNcellName(String ncellName) {
		this.ncellName = ncellName;
	}

	/**
	 * @return the ncellId
	 */
	@Column(name = "NCELL_ID")
	public Long getNcellId() {
		return ncellId;
	}

	/**
	 * @param the
	 *            ncellId to set
	 */

	public void setNcellId(Long ncellId) {
		this.ncellId = ncellId;
	}

	/**
	 * @return the npci
	 */
	@Column(name = "NCELL_PCI")
	public Long getNpci() {
		return npci;
	}

	/**
	 * @param the
	 *            npci to set
	 */

	public void setNpci(Long npci) {
		this.npci = npci;
	}

	/**
	 * @return the nearfcn
	 */
	@Column(name = "NCELL_EARFCN")
	public Long getNearfcn() {
		return nearfcn;
	}

	/**
	 * @param the
	 *            nearfcn to set
	 */

	public void setNearfcn(Long nearfcn) {
		this.nearfcn = nearfcn;
	}

	/**
	 * @return the nrsrp
	 */
	@Column(name = "NCELL_RSRP")
	public Float getNrsrp() {
		return nrsrp;
	}

	/**
	 * @param the
	 *            nrsrp to set
	 */

	public void setNrsrp(Float nrsrp) {
		this.nrsrp = nrsrp;
	}

	/**
	 * @return the ntoProblemDotDistance
	 */
	@Column(name = "NCELL_DISTANCE")
	public Float getNtoProblemDotDistance() {
		return ntoProblemDotDistance;
	}

	/**
	 * @param the
	 *            ntoProblemDotDistance to set
	 */

	public void setNtoProblemDotDistance(Float ntoProblemDotDistance) {
		this.ntoProblemDotDistance = ntoProblemDotDistance;
	}

}
