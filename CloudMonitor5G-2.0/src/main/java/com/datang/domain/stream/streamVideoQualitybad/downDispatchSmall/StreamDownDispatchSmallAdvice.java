package com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall;

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
 * 下行调度数小---优化建议：核查小区用户数以及是否存在干扰。
 * 
 * @explain
 * @name DownDispatchSmallAdjustCutParameter
 * @author shenyanwei
 * @date 2017年6月5日下午2:44:20
 */
@Entity
@Table(name = "STREAM_BQ_PDCCHDL_ADVICE")
public class StreamDownDispatchSmallAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5754499698712280516L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题(下行调度数小)
	 */
	private StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall;
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
	 * SINR
	 */
	private Float sinr;
	/**
	 * 和问题采样点距离(m)
	 */
	private Float toProblemDotDistance;
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
	 * @return the streamQualityBadDownDispatchSmall
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public StreamQualityBadDownDispatchSmall getStreamQualityBadDownDispatchSmall() {
		return streamQualityBadDownDispatchSmall;
	}

	/**
	 * @param the
	 *            streamQualityBadDownDispatchSmall to set
	 */

	public void setStreamQualityBadDownDispatchSmall(
			StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall) {
		this.streamQualityBadDownDispatchSmall = streamQualityBadDownDispatchSmall;
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
	 * @return the sinr
	 */
	@Column(name = "SINR")
	public Float getSinr() {
		return sinr;
	}

	/**
	 * @param the
	 *            sinr to set
	 */

	public void setSinr(Float sinr) {
		this.sinr = sinr;
	}

}
