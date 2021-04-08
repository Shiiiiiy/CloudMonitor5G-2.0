package com.datang.domain.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 终端信息监控
 * 
 * @explain
 * @name TerminalInfo
 * @author shenyanwei
 * @date 2016年8月15日上午11:25:29
 */
@Entity
@Table(name = "IADS_REALTIME_DEVICEINFO")
public class TerminalInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3020029771371193084L;
	private Long id;
	private String boxId;
	private Long sessionId;
	/** CPU使用率 */
	private Float cpu;
	/** 内存使用率 */
	private Float memory;
	/** 前端剩余磁盘空间 */
	private Float spaceLeft;
	/** 前端IMEI */
	private String imei;
	/** 前端系统版本 */
	private String systemVersion;
	/** 前端基带版本 */
	private String basebandVersion;
	/** 时间 */
	private Date deviceInfoTime;
	private Long deviceInfoTimeLong;

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
	 * @return the sessionId
	 */
	@Column(name = "SESSION_ID")
	public Long getSessionId() {
		return sessionId;
	}

	/**
	 * @param the
	 *            sessionId to set
	 */

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the cpu
	 */
	@Column(name = "CPU")
	public Float getCpu() {
		return cpu;
	}

	/**
	 * @param the
	 *            cpu to set
	 */

	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the memory
	 */
	@Column(name = "MEMORY")
	public Float getMemory() {
		return memory;
	}

	/**
	 * @param the
	 *            memory to set
	 */

	public void setMemory(Float memory) {
		this.memory = memory;
	}

	/**
	 * @return the imei
	 */
	@Column(name = "IMEI")
	public String getImei() {
		return imei;
	}

	/**
	 * @param the
	 *            imei to set
	 */

	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the systemVersion
	 */
	@Column(name = "SYSTEM_VERSION")
	public String getSystemVersion() {
		return systemVersion;
	}

	/**
	 * @param the
	 *            systemVersion to set
	 */

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	/**
	 * @return the basebandVersion
	 */
	@Column(name = "BASEBAND_VERSION")
	public String getBasebandVersion() {
		return basebandVersion;
	}

	/**
	 * @param the
	 *            basebandVersion to set
	 */

	public void setBasebandVersion(String basebandVersion) {
		this.basebandVersion = basebandVersion;
	}

	/**
	 * @return the deviceInfoTime
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getDeviceInfoTime() {
		return deviceInfoTime;
	}

	/**
	 * @param the
	 *            deviceInfoTime to set
	 */

	public void setDeviceInfoTime(Date deviceInfoTime) {
		this.deviceInfoTime = deviceInfoTime;
	}

	/**
	 * @return the deviceInfoTimeLong
	 */
	@Column(name = "DEVICEINFO_TIME")
	public Long getDeviceInfoTimeLong() {
		return deviceInfoTimeLong;
	}

	/**
	 * @param the
	 *            deviceInfoTimeLong to set
	 */

	public void setDeviceInfoTimeLong(Long deviceInfoTimeLong) {
		this.deviceInfoTimeLong = deviceInfoTimeLong;
		if (null != deviceInfoTimeLong) {
			this.deviceInfoTime = new Date(deviceInfoTimeLong);
		}
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TerminalInfo [id=" + id + ", boxId=" + boxId + ", sessionId="
				+ sessionId + ", cpu=" + cpu + ", memory=" + memory
				+ ", spaceLeft=" + spaceLeft + ", imei=" + imei
				+ ", systemVersion=" + systemVersion + ", basebandVersion="
				+ basebandVersion + ", deviceInfoTime=" + deviceInfoTime
				+ ", deviceInfoTimeLong=" + deviceInfoTimeLong + "]";
	}

}
