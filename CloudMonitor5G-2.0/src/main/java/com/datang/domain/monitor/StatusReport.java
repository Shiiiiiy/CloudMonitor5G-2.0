package com.datang.domain.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 硬件状态监控
 * 
 * @explain
 * @name StatusReport
 * @author shenyanwei
 * @date 2016年7月11日下午1:57:31
 */
@Entity
@Table(name = "IADS_REALTIME_STATUS_REPORT")
public class StatusReport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3020029771371193084L;
	private Long id;
	private String boxId;
	/** 前端的工作温度，浮点数 */
	private Float temperature;
	/** 供电模式，I表示使用内置电池供电，E表示外电供电 */
	private Integer powerMode;
	/** 前端剩余磁盘空间 */
	private Float spaceLeft;
	/** 前段未传的文件数 */
	private Integer filesLeft;
	/** 状态时间 */
	private Date statusReportTime;
	private Long statusReportTimeLong;

	/**
	 * @return the id
	 */
	@Id
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
	 * @return the boxId
	 */
	@Column(name = "BOX_ID")
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param the
	 *            boxId to set
	 */

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the temperature
	 */
	@Column(name = "TEMPERATURE")
	public Float getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the powerMode
	 */
	@Column(name = "POWER_MODE")
	public Integer getPowerMode() {
		return powerMode;
	}

	/**
	 * @param powerMode
	 *            the powerMode to set
	 */
	public void setPowerMode(Integer powerMode) {
		this.powerMode = powerMode;
	}

	/**
	 * @return the spaceLeft
	 */
	@Column(name = "SPACE_LEFT")
	public Float getSpaceLeft() {
		return spaceLeft;
	}

	/**
	 * @param spaceLeft
	 *            the spaceLeft to set
	 */
	public void setSpaceLeft(Float spaceLeft) {
		this.spaceLeft = spaceLeft;
	}

	/**
	 * @return the filesLeft
	 */
	@Column(name = "FILES_LEFT")
	public Integer getFilesLeft() {
		return filesLeft;
	}

	/**
	 * @param filesLeft
	 *            the filesLeft to set
	 */
	public void setFilesLeft(Integer filesLeft) {
		this.filesLeft = filesLeft;
	}

	/**
	 * @return the stateReportTime
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getStatusReportTime() {
		return statusReportTime;
	}

	/**
	 * @param the
	 *            stateReportTime to set
	 */

	public void setStatusReportTime(Date statusReportTime) {
		this.statusReportTime = statusReportTime;
	}

	/**
	 * @return the statusReportTimeLong
	 */
	@Column(name = "STATUS_REPORT_TIME")
	public Long getStatusReportTimeLong() {
		return statusReportTimeLong;
	}

	/**
	 * @param the
	 *            statusReportTimeLong to set
	 */

	public void setStatusReportTimeLong(Long statusReportTimeLong) {
		this.statusReportTimeLong = statusReportTimeLong;
		if (null != statusReportTimeLong) {
			this.statusReportTime = new Date(statusReportTimeLong);
		}
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatusReport [id=" + id + ", boxId=" + boxId + ", temperature="
				+ temperature + ", powerMode=" + powerMode + ", spaceLeft="
				+ spaceLeft + ", filesLeft=" + filesLeft
				+ ", statusReportTime=" + statusReportTime
				+ ", statusReportTimeLong=" + statusReportTimeLong + "]";
	}

}
