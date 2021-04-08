package com.datang.bean.monitor;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * 硬件状态监控响应Bean
 * 
 * @explain
 * @name StatusReportBean
 * @author shenyanwei
 * @date 2016年7月25日上午9:36:39
 */
public class StatusReportBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3020029771371193084L;
	private String boxId;
	/** 前端的工作温度，浮点数 */
	private Float temperature;
	/** 供电模式，I表示使用内置电池供电，E表示外电供电 */
	private String powerMode;
	/** 前端剩余磁盘空间 */
	private Float spaceLeft;
	/** 前段未传的文件数 */
	private Integer filesLeft;
	/** 状态时间 */
	private Date statusReportTime;

	/**
	 * @return the boxId
	 */
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
	public Float getTemperature() {
		return temperature;
	}

	/**
	 * @param the
	 *            temperature to set
	 */

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the powerMode
	 */
	public String getPowerMode() {
		return powerMode;
	}

	/**
	 * @param the
	 *            powerMode to set
	 */

	public void setPowerMode(String powerMode) {
		this.powerMode = powerMode;
	}

	/**
	 * @return the spaceLeft
	 */
	public Float getSpaceLeft() {
		return spaceLeft;
	}

	/**
	 * @param the
	 *            spaceLeft to set
	 */

	public void setSpaceLeft(Float spaceLeft) {
		this.spaceLeft = spaceLeft;
	}

	/**
	 * @return the filesLeft
	 */
	public Integer getFilesLeft() {
		return filesLeft;
	}

	/**
	 * @param the
	 *            filesLeft to set
	 */

	public void setFilesLeft(Integer filesLeft) {
		this.filesLeft = filesLeft;
	}

	/**
	 * @return the statusReportTime
	 */
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getStatusReportTime() {
		return statusReportTime;
	}

	/**
	 * @param the
	 *            statusReportTime to set
	 */

	public void setStatusReportTime(Date statusReportTime) {
		this.statusReportTime = statusReportTime;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatusReportBean [boxId=" + boxId + ", temperature="
				+ temperature + ", powerMode=" + powerMode + ", spaceLeft="
				+ spaceLeft + ", filesLeft=" + filesLeft
				+ ", statusReportTime=" + statusReportTime + "]";
	}

}
