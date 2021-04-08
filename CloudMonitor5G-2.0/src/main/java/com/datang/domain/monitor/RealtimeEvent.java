package com.datang.domain.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 事件监控
 * 
 * @explain
 * @name RealtimeEvent
 * @author shenyanwei
 * @date 2016年7月11日下午1:56:42
 */
@Entity
@Table(name = "IADS_REALTIME_EVENT")
public class RealtimeEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6175330231514106938L;
	private Long id;
	private String boxId;
	/** 通道号 */
	private Integer channelNo;
	/** 通道类型 */
	private Integer channelType;
	/** 事件信息代码 */
	private Integer eventCode;
	/** 事件时间 */
	private Date eventTime;
	private Long eventTimeLong;
	/** 事件关联GPS */
	private GPS gps;

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
	 * @return the channelNo
	 */
	@Column(name = "CHANNEL_NO")
	public Integer getChannelNo() {
		return channelNo;
	}

	/**
	 * @param the
	 *            channelNo to set
	 */

	public void setChannelNo(Integer channelNo) {
		this.channelNo = channelNo;
	}

	/**
	 * @return the channelType
	 */
	@Column(name = "CHANNEL_TYPE")
	public Integer getChannelType() {
		return channelType;
	}

	/**
	 * @param the
	 *            channelType to set
	 */

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

	/**
	 * @return the eventCode
	 */
	@Column(name = "EVENT_CODE")
	public Integer getEventCode() {
		return eventCode;
	}

	/**
	 * @param eventCode
	 *            the eventCode to set
	 */
	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}

	/**
	 * @return the gps
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GPS_ID")
	public GPS getGps() {
		return gps;
	}

	/**
	 * @param gps
	 *            the gps to set
	 */
	public void setGps(GPS gps) {
		this.gps = gps;
	}

	/**
	 * @return the eventTime
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getEventTime() {
		return eventTime;
	}

	/**
	 * @param the
	 *            eventTime to set
	 */

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	/**
	 * @return the eventTimeLong
	 */
	@Column(name = "EVENT_TIME")
	public Long getEventTimeLong() {
		return eventTimeLong;
	}

	/**
	 * @param the
	 *            eventTimeLong to set
	 */

	public void setEventTimeLong(Long eventTimeLong) {
		this.eventTimeLong = eventTimeLong;
		if (null != eventTimeLong) {
			this.eventTime = new Date(eventTimeLong);
		}
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RealtimeEvent [id=" + id + ", boxId=" + boxId + ", channelNo="
				+ channelNo + ", channelType=" + channelType + ", eventCode="
				+ eventCode + ", gps=" + gps + "]";
	}

}
