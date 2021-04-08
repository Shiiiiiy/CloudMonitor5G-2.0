package com.datang.domain.testLogItem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 有关服务小区和邻区测量详情信息实体
 * 
 * @explain
 * @name TestLogMeasure
 * @author shenyanwei
 * @date 2016年8月3日下午3:42:50
 */
@Entity
@Table(name = "IADS_TESTLOG_MEASURE")
public class TestLogMeasure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7816304743828811198L;
	private Long id;
	private Date time;
	private Long timeLong;
	private TestLogItem testLogItem;
	private Long scellPci;
	private Long scellEarfcn;
	private Float scellRsrp;
	private Float scellSinr;
	private Long ncell1Pci;
	private Long ncell1Earfcn;
	private Float ncell1Rsrp;
	private Long ncell2Pci;
	private Long ncell2Earfcn;
	private Float ncell2Rsrp;
	private Long ncell3Pci;
	private Long ncell3Earfcn;
	private Float ncell3Rsrp;
	private Long ncell4Pci;
	private Long ncell4Earfcn;
	private Float ncell4Rsrp;
	private Long ncell5Pci;
	private Long ncell5Earfcn;
	private Float ncell5Rsrp;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param the
	 *            id to set
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the time
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getTime() {
		return time;
	}

	/**
	 * @param the
	 *            time to set
	 */

	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the timeLong
	 */
	@Column(name = "TIME")
	public Long getTimeLong() {
		return timeLong;
	}

	/**
	 * @param the
	 *            timeLong to set
	 */

	public void setTimeLong(Long timeLong) {
		this.timeLong = timeLong;
		if (null != timeLong) {
			this.time = new Date(timeLong);

		}
	}

	/**
	 * @return the testLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO")
	@JSON(serialize = false)
	public TestLogItem getTestLogItem() {
		return testLogItem;
	}

	/**
	 * @param the
	 *            testLogItem to set
	 */

	public void setTestLogItem(TestLogItem testLogItem) {
		this.testLogItem = testLogItem;
	}

	/**
	 * @return the scellPci
	 */
	@Column(name = "SCELL_PCI")
	public Long getScellPci() {
		return scellPci;
	}

	/**
	 * @param the
	 *            scellPci to set
	 */

	public void setScellPci(Long scellPci) {
		this.scellPci = scellPci;
	}

	/**
	 * @return the scellEarfcn
	 */
	@Column(name = "SCELL_EARFCN")
	public Long getScellEarfcn() {
		return scellEarfcn;
	}

	/**
	 * @param the
	 *            scellEarfcn to set
	 */

	public void setScellEarfcn(Long scellEarfcn) {
		this.scellEarfcn = scellEarfcn;
	}

	/**
	 * @return the scellRsrp
	 */
	@Column(name = "SCELL_RSRP")
	public Float getScellRsrp() {
		return scellRsrp;
	}

	/**
	 * @param the
	 *            scellRsrp to set
	 */

	public void setScellRsrp(Float scellRsrp) {
		this.scellRsrp = scellRsrp;
	}

	/**
	 * @return the scellSinr
	 */
	@Column(name = "SCELL_SINR")
	public Float getScellSinr() {
		return scellSinr;
	}

	/**
	 * @param the
	 *            scellSinr to set
	 */

	public void setScellSinr(Float scellSinr) {
		this.scellSinr = scellSinr;
	}

	/**
	 * @return the ncell1Pci
	 */
	@Column(name = "NCELL1_PCI")
	public Long getNcell1Pci() {
		return ncell1Pci;
	}

	/**
	 * @param the
	 *            ncell1Pci to set
	 */

	public void setNcell1Pci(Long ncell1Pci) {
		this.ncell1Pci = ncell1Pci;
	}

	/**
	 * @return the ncell1Earfcn
	 */
	@Column(name = "NCELL1_EARFCN")
	public Long getNcell1Earfcn() {
		return ncell1Earfcn;
	}

	/**
	 * @param the
	 *            ncell1Earfcn to set
	 */

	public void setNcell1Earfcn(Long ncell1Earfcn) {
		this.ncell1Earfcn = ncell1Earfcn;
	}

	/**
	 * @return the ncell1Rsrp
	 */
	@Column(name = "NCELL1_RSRP")
	public Float getNcell1Rsrp() {
		return ncell1Rsrp;
	}

	/**
	 * @param the
	 *            ncell1Rsrp to set
	 */

	public void setNcell1Rsrp(Float ncell1Rsrp) {
		this.ncell1Rsrp = ncell1Rsrp;
	}

	/**
	 * @return the ncell2Pci
	 */
	@Column(name = "NCELL2_PCI")
	public Long getNcell2Pci() {
		return ncell2Pci;
	}

	/**
	 * @param the
	 *            ncell2Pci to set
	 */

	public void setNcell2Pci(Long ncell2Pci) {
		this.ncell2Pci = ncell2Pci;
	}

	/**
	 * @return the ncell2Earfcn
	 */
	@Column(name = "NCELL2_EARFCN")
	public Long getNcell2Earfcn() {
		return ncell2Earfcn;
	}

	/**
	 * @param the
	 *            ncell2Earfcn to set
	 */

	public void setNcell2Earfcn(Long ncell2Earfcn) {
		this.ncell2Earfcn = ncell2Earfcn;
	}

	/**
	 * @return the ncell2Rsrp
	 */
	@Column(name = "NCELL2_RSRP")
	public Float getNcell2Rsrp() {
		return ncell2Rsrp;
	}

	/**
	 * @param the
	 *            ncell2Rsrp to set
	 */

	public void setNcell2Rsrp(Float ncell2Rsrp) {
		this.ncell2Rsrp = ncell2Rsrp;
	}

	/**
	 * @return the ncell3Pci
	 */
	@Column(name = "NCELL3_PCI")
	public Long getNcell3Pci() {
		return ncell3Pci;
	}

	/**
	 * @param the
	 *            ncell3Pci to set
	 */

	public void setNcell3Pci(Long ncell3Pci) {
		this.ncell3Pci = ncell3Pci;
	}

	/**
	 * @return the ncell3Earfcn
	 */
	@Column(name = "NCELL3_EARFCN")
	public Long getNcell3Earfcn() {
		return ncell3Earfcn;
	}

	/**
	 * @param the
	 *            ncell3Earfcn to set
	 */

	public void setNcell3Earfcn(Long ncell3Earfcn) {
		this.ncell3Earfcn = ncell3Earfcn;
	}

	/**
	 * @return the ncell3Rsrp
	 */
	@Column(name = "NCELL3_RSRP")
	public Float getNcell3Rsrp() {
		return ncell3Rsrp;
	}

	/**
	 * @param the
	 *            ncell3Rsrp to set
	 */

	public void setNcell3Rsrp(Float ncell3Rsrp) {
		this.ncell3Rsrp = ncell3Rsrp;
	}

	/**
	 * @return the ncell4Pci
	 */
	@Column(name = "NCELL4_PCI")
	public Long getNcell4Pci() {
		return ncell4Pci;
	}

	/**
	 * @param the
	 *            ncell4Pci to set
	 */

	public void setNcell4Pci(Long ncell4Pci) {
		this.ncell4Pci = ncell4Pci;
	}

	/**
	 * @return the ncell4Earfcn
	 */
	@Column(name = "NCELL4_EARFCN")
	public Long getNcell4Earfcn() {
		return ncell4Earfcn;
	}

	/**
	 * @param the
	 *            ncell4Earfcn to set
	 */

	public void setNcell4Earfcn(Long ncell4Earfcn) {
		this.ncell4Earfcn = ncell4Earfcn;
	}

	/**
	 * @return the ncell4Rsrp
	 */
	@Column(name = "NCELL4_RSRP")
	public Float getNcell4Rsrp() {
		return ncell4Rsrp;
	}

	/**
	 * @param the
	 *            ncell4Rsrp to set
	 */

	public void setNcell4Rsrp(Float ncell4Rsrp) {
		this.ncell4Rsrp = ncell4Rsrp;
	}

	/**
	 * @return the ncell5Pci
	 */
	@Column(name = "NCELL5_PCI")
	public Long getNcell5Pci() {
		return ncell5Pci;
	}

	/**
	 * @param the
	 *            ncell5Pci to set
	 */

	public void setNcell5Pci(Long ncell5Pci) {
		this.ncell5Pci = ncell5Pci;
	}

	/**
	 * @return the ncell5Earfcn
	 */
	@Column(name = "NCELL5_EARFCN")
	public Long getNcell5Earfcn() {
		return ncell5Earfcn;
	}

	/**
	 * @param the
	 *            ncell5Earfcn to set
	 */

	public void setNcell5Earfcn(Long ncell5Earfcn) {
		this.ncell5Earfcn = ncell5Earfcn;
	}

	/**
	 * @return the ncell5Rsrp
	 */
	@Column(name = "NCELL5_RSRP")
	public Float getNcell5Rsrp() {
		return ncell5Rsrp;
	}

	/**
	 * @param the
	 *            ncell5Rsrp to set
	 */

	public void setNcell5Rsrp(Float ncell5Rsrp) {
		this.ncell5Rsrp = ncell5Rsrp;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestLogMeasure [id=" + id + ", time=" + time + ", timeLong="
				+ timeLong + ", testLogItem=" + testLogItem + ", scellPci="
				+ scellPci + ", scellEarfcn=" + scellEarfcn + ", scellRsrp="
				+ scellRsrp + ", scellSinr=" + scellSinr + ", ncell1Pci="
				+ ncell1Pci + ", ncell1Earfcn=" + ncell1Earfcn
				+ ", ncell1Rsrp=" + ncell1Rsrp + ", ncell2Pci=" + ncell2Pci
				+ ", ncell2Earfcn=" + ncell2Earfcn + ", ncell2Rsrp="
				+ ncell2Rsrp + ", ncell3Pci=" + ncell3Pci + ", ncell3Earfcn="
				+ ncell3Earfcn + ", ncell3Rsrp=" + ncell3Rsrp + ", ncell4Pci="
				+ ncell4Pci + ", ncell4Earfcn=" + ncell4Earfcn
				+ ", ncell4Rsrp=" + ncell4Rsrp + ", ncell5Pci=" + ncell5Pci
				+ ", ncell5Earfcn=" + ncell5Earfcn + ", ncell5Rsrp="
				+ ncell5Rsrp + "]";
	}

}
