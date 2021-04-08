package com.datang.bean.monitor;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * 事件监控响应Bean
 * 
 * @explain
 * @name RealtimeEventBean
 * @author shenyanwei
 * @date 2016年7月25日上午9:44:40
 */
public class RealtimeEventBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6175330231514106938L;
	private String boxId;
	/** 通道号 */
	private String channelNo;
	/** 通道类型 */
	private String channelType;
	/** 事件信息代码 */
	private String eventCode;
	/** 事件时间 */
	private Date eventTime;
	/** 事件类型 */
	private String eventType;
	/** 事件名称 */
	private String eventName;

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
	 * @return the channelNo
	 */
	public String getChannelNo() {
		return channelNo;
	}

	/**
	 * @param the
	 *            channelNo to set
	 */

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
	}

	/**
	 * @param the
	 *            channelType to set
	 */

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	/**
	 * @return the eventCode
	 */
	public String getEventCode() {
		return eventCode;
	}

	/**
	 * @param the
	 *            eventCode to set
	 */

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	/**
	 * @return the eventTime
	 */
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
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param the
	 *            eventType to set
	 */

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param the
	 *            eventName to set
	 */

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RealtimeEventBean [boxId=" + boxId + ", channelNo=" + channelNo
				+ ", channelType=" + channelType + ", eventCode=" + eventCode
				+ ", eventTime=" + eventTime + ", eventType=" + eventType
				+ ", eventName=" + eventName + "]";
	}

}
