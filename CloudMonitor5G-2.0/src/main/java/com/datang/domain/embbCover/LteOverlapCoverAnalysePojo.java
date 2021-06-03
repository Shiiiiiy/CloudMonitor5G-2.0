package com.datang.domain.embbCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * LTE重叠覆盖分析表
 * @author maxuancheng
 * @date 2020年5月7日
 */
@Entity
@Table(name = "IADS_5G_EMBB_LTE_OVERLAY")
public class LteOverlapCoverAnalysePojo  implements Serializable{

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

	private String bestncellrsrp;

	private String gnumber;
	private String gspace;
	private String gibler;
	private String grsrp;
	private String gsinr;

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

	@Column(name = "LTE_REGION")
	public String getRegin() {
		return regin;
	}

	public void setRegin(String regin) {
		this.regin = regin;
	}

	@Column(name = "LTE_CELLNAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(name = "LTE_NUMBER")
	public String getPointNumber() {
		return pointNumber;
	}

	public void setPointNumber(String pointNumber) {
		this.pointNumber = pointNumber;
	}

	@Column(name = "LTE_DISTANCE")
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Column(name = "LTE_DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "LTE_DNUMBER")
	public String getCoverNumber() {
		return coverNumber;
	}

	public void setCoverNumber(String coverNumber) {
		this.coverNumber = coverNumber;
	}

	@Column(name = "LTE_SPACE")
	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	@Column(name = "LTE_DSPACE")
	public String getCoverSpace() {
		return coverSpace;
	}

	public void setCoverSpace(String coverSpace) {
		this.coverSpace = coverSpace;
	}

	@Column(name = "LTE_DRSRP")
	public String getRsrp() {
		return rsrp;
	}

	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}

	@Transient
	public String getBestNcellRsrp() {
		return bestNcellRsrp;
	}

	public void setBestNcellRsrp(String bestNcellRsrp) {
		this.bestNcellRsrp = bestNcellRsrp;
	}

	@Column(name = "LTE_DSINR")
	public String getSinr() {
		return sinr;
	}

	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	@Column(name = "LTE_GBESTNCELLRSRP")
	public String getBestncellrsrp() {
		return bestncellrsrp;
	}

	public void setBestncellrsrp(String bestncellrsrp) {
		this.bestncellrsrp = bestncellrsrp;
	}

	@Column(name = "LTE_GNUMBER")
	public String getGnumber() { return gnumber;}

	public void setGnumber(String gnumber) { this.gnumber = gnumber; }

	@Column(name = "LTE_GSPACE")
	public String getGspace() {	return gspace;}

	public void setGspace(String gspace) {	this.gspace = gspace;}

	@Column(name = "LTE_GIBLER")
	public String getGibler() {	return gibler;	}

	public void setGibler(String gibler) { this.gibler = gibler;	}

	@Column(name = "LTE_GRSRP")
	public String getGrsrp() {return grsrp;	}

	public void setGrsrp(String grsrp) {this.grsrp = grsrp;	}

	@Column(name = "LTE_GSINR")
	public String getGsinr() { return gsinr;}

	public void setGsinr(String gsinr) {this.gsinr = gsinr;	}

	@Column(name = "LTE_DIBLER")
	public String getIbler() {
		return ibler;
	}

	public void setIbler(String ibler) {
		this.ibler = ibler;
	}
}
