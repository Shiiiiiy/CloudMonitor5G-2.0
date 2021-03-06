package com.datang.domain.embbCover;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LTE锚点小区弱覆盖分析表
 * @author maxuancheng
 * @date 2020年5月7日
 */
@Entity
@Table(name = "IADS_5G_EMBB_LTE_WEAK")
public class LteWeakCoverAnalysePojo implements Serializable{

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
	 * 里程
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
	 * 备注：从表中字段区分rsrp和coverRsrp
	 * 弱覆盖采样点RRSRP均值（dBm）
	 */
	private String rsrp;

	/**
	 * 覆盖电平rsrp
	 */
	private String coverRsrp;

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

	@Column(name = "LTE_RNUMBER")
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

	@Column(name = "LTE_RSPACE")
	public String getCoverSpace() {
		return coverSpace;
	}

	public void setCoverSpace(String coverSpace) {
		this.coverSpace = coverSpace;
	}

	@Column(name = "LTE_RRSRP")
	public String getRsrp() {
		return rsrp;
	}

	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}

	@Column(name = "LTE_RSRP")
	public String getCoverRsrp() {
		return coverRsrp;
	}

	public void setCoverRsrp(String coverRsrp) {
		this.coverRsrp = coverRsrp;
	}

	@Column(name = "LTE_RBESTNCELLRSRP")
	public String getBestNcellRsrp() {
		return bestNcellRsrp;
	}

	public void setBestNcellRsrp(String bestNcellRsrp) {
		this.bestNcellRsrp = bestNcellRsrp;
	}

	@Column(name = "LTE_RSINR")
	public String getSinr() {
		return sinr;
	}

	public void setSinr(String sinr) {
		this.sinr = sinr;
	}

	@Column(name = "LTE_RIBLER")
	public String getIbler() {
		return ibler;
	}

	public void setIbler(String ibler) {
		this.ibler = ibler;
	}
	
}
