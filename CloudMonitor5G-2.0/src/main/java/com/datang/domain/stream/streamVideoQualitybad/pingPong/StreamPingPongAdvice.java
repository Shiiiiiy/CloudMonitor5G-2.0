package com.datang.domain.stream.streamVideoQualitybad.pingPong;

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
 * 流媒体视频质差乒乓切换---优化建议调整切换参数
 * 
 * @explain
 * @name StreamPingPongAdvice
 * @author shenyanwei
 * @date 2017年10月20日下午4:30:54
 */
@Entity
@Table(name = "STREAM_BQ_PP_ADVICE")
public class StreamPingPongAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5754499698712280516L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属问题(乒乓切换)
	 */
	private StreamQualityBadPingPong streamQualityBadPingPong;
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
	private Long cellPci;
	/**
	 * EARFCN
	 */
	private Long cellEarfcn;
	/**
	 * RSRP
	 */
	private Float cellRsrp;
	/**
	 * 和问题采样点距离(m)
	 */
	private Float cellToProblemDotDistance;
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
	private Long ncellPci;
	/**
	 * 邻区EARFCN
	 */
	private Long ncellEarfcn;
	/**
	 * 邻区RSRP
	 */
	private Float ncellRsrp;
	/**
	 * 邻区和问题采样点距离(m)
	 */
	private Float ncellToProblemDotDistance;
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
	public StreamQualityBadPingPong getStreamQualityBadPingPong() {
		return streamQualityBadPingPong;
	}

	/**
	 * @param volteQualityBadRoadNbDeficiency
	 *            the volteQualityBadRoadNbDeficiency to set
	 */
	public void setStreamQualityBadPingPong(
			StreamQualityBadPingPong streamQualityBadPingPong) {
		this.streamQualityBadPingPong = streamQualityBadPingPong;
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
	 * @return the cellPci
	 */
	@Column(name = "CELL_PCI")
	public Long getCellPci() {
		return cellPci;
	}

	/**
	 * @param the
	 *            cellPci to set
	 */

	public void setCellPci(Long cellPci) {
		this.cellPci = cellPci;
	}

	/**
	 * @return the cellEarfcn
	 */
	@Column(name = "CELL_EARFCN")
	public Long getCellEarfcn() {
		return cellEarfcn;
	}

	/**
	 * @param the
	 *            cellEarfcn to set
	 */

	public void setCellEarfcn(Long cellEarfcn) {
		this.cellEarfcn = cellEarfcn;
	}

	/**
	 * @return the cellRsrp
	 */
	@Column(name = "CELL_RSRP")
	public Float getCellRsrp() {
		return cellRsrp;
	}

	/**
	 * @param the
	 *            cellRsrp to set
	 */

	public void setCellRsrp(Float cellRsrp) {
		this.cellRsrp = cellRsrp;
	}

	/**
	 * @return the cellToProblemDotDistance
	 */
	@Column(name = "CELL_DISTANCE")
	public Float getCellToProblemDotDistance() {
		return cellToProblemDotDistance;
	}

	/**
	 * @param the
	 *            cellToProblemDotDistance to set
	 */

	public void setCellToProblemDotDistance(Float cellToProblemDotDistance) {
		this.cellToProblemDotDistance = cellToProblemDotDistance;
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
	 * @return the ncellPci
	 */
	@Column(name = "NCELL_PCI")
	public Long getNcellPci() {
		return ncellPci;
	}

	/**
	 * @param the
	 *            ncellPci to set
	 */

	public void setNcellPci(Long ncellPci) {
		this.ncellPci = ncellPci;
	}

	/**
	 * @return the ncellRarfcn
	 */
	@Column(name = "NCELL_EARFCN")
	public Long getNcellEarfcn() {
		return ncellEarfcn;
	}

	/**
	 * @param the
	 *            ncellRarfcn to set
	 */

	public void setNcellEarfcn(Long ncellEarfcn) {
		this.ncellEarfcn = ncellEarfcn;
	}

	/**
	 * @return the ncellRsrp
	 */
	@Column(name = "NCELL_RSRP")
	public Float getNcellRsrp() {
		return ncellRsrp;
	}

	/**
	 * @param the
	 *            ncellRsrp to set
	 */

	public void setNcellRsrp(Float ncellRsrp) {
		this.ncellRsrp = ncellRsrp;
	}

	/**
	 * @return the ncellToProblemDotDistance
	 */
	@Column(name = "NCELL_DISTANCE")
	public Float getNcellToProblemDotDistance() {
		return ncellToProblemDotDistance;
	}

	/**
	 * @param the
	 *            ncellToProblemDotDistance to set
	 */

	public void setNcellToProblemDotDistance(Float ncellToProblemDotDistance) {
		this.ncellToProblemDotDistance = ncellToProblemDotDistance;
	}

}
