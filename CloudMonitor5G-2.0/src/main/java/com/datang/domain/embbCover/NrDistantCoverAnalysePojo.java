package com.datang.domain.embbCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NR超远覆盖分析表
 * @author maxuancheng
 * @date 2020年5月7日
 */
@Entity
@Table(name = "IADS_5G_EMBB_NR_SPFAR")
public class NrDistantCoverAnalysePojo implements Serializable{

	/**
	 * @author maxuancheng
	 * date:2020年5月7日 下午5:09:46
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	/**
	 * 日志名
	 */
	private String logName;
	
	/**
	 * 区域
	 */
	private String regin;//区域
	
	/**
	 * 小区友好名
	 */
	private String cellName;
	
	/**
	 * 采样点数
	 */
	private String pointNumber;

	/**
	 * 站间距（米）
	 */
	private String distance;
	
	/**
	 * 测试时长（秒）
	 */
	private String duration;

	/**
	 * X覆盖采样点
	 */
	private String coverNumber;
	
	/**
	 * 采样点到站点距离
	 */
	private String space;
	
	/**
	 * X覆盖采样点到站点距离
	 */
	private String coverSpace;

	/**
	 * 弱覆盖采样点RSRP均值（dBm）
	 */
	private String rsrp;

	/**
	 * 弱覆盖采样点最强邻区RSRP均值（dBm）
	 */
	private String bestNcellRsrp;

	/**
	 * 弱覆盖采样点SINR均值（dBm）
	 */
	private String sinr;
	
	/**
	 * ibler平均值
	 */
	private String ibler;

	@Column(name = "ID")
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RELATELOGNAME")
	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	@Column(name = "NR_REGION")
	public String getRegin() {
		return regin;
	}

	public void setRegin(String regin) {
		this.regin = regin;
	}

	@Column(name = "NR_CELLNAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "NR_NUMBER")
	public String getPointNumber() {
		return pointNumber;
	}

	public void setPointNumber(String pointNumber) {
		this.pointNumber = pointNumber;
	}

	@Column(name = "NR_DISTANCE")
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Column(name = "NR_DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "NR_CNUMBER")
	public String getCoverNumber() {
		return coverNumber;
	}

	public void setCoverNumber(String coverNumber) {
		this.coverNumber = coverNumber;
	}

	@Column(name = "NR_SPACE")
	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	@Column(name = "NR_CSPACE")
	public String getCoverSpace() {
		return coverSpace;
	}

	public void setCoverSpace(String coverSpace) {
		this.coverSpace = coverSpace;
	}

	@Column(name = "NR_CBESTBEAMRSRP")
	public String getRsrp() {
		return rsrp;
	}

	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}

	@Column(name = "NR_CBESTNCELLRSRP")
	public String getBestNcellRsrp() {
		return bestNcellRsrp;
	}

	public void setBestNcellRsrp(String bestNcellRsrp) {
		this.bestNcellRsrp = bestNcellRsrp;
	}

	@Column(name = "NR_CSINR")
	public String getSinr() {
		return sinr;
	}

	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	@Column(name = "NR_CIBLER")
	public String getIbler() {
		return ibler;
	}

	public void setIbler(String ibler) {
		this.ibler = ibler;
	}
}
