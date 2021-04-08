package com.datang.bean.monitor;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * 告警 监控响应Bean
 * 
 * @explain
 * @name RealtimeAlarmBean
 * @author shenyanwei
 * @date 2016年7月25日下午1:21:22
 */
public class RealtimeAlarmBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7498152737414574114L;

	private String boxId;
	/** 通道号 */
	private String channelNo;
	/** 通道类型 */
	private String channelType;
	/** 告警信息代码 */
	private String alarmCode;
	/** 告警类型 */
	private String alarmType;
	/** 告警产生的原因代码（如果有的话） */
	private String alarmReason;
	/** 告警发生时间 */
	private Date alarmTime;
	/** 告警清除时间 */
	private Date alarmClearTime;

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

	public void setChannelType(String channelType2) {
		this.channelType = channelType2;
	}

	/**
	 * @return the alarmCode
	 */
	public String getAlarmCode() {
		return alarmCode;
	}

	/**
	 * @param the
	 *            alarmCode to set
	 */

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	/**
	 * @return the alarmType
	 */
	public String getAlarmType() {
		return alarmType;
	}

	/**
	 * @param the
	 *            alarmType to set
	 */

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	/**
	 * @return the alarmReason
	 */
	public String getAlarmReason() {
		return alarmReason;
	}

	/**
	 * @param the
	 *            alarmReason to set
	 */

	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}

	/**
	 * @return the alarmTime
	 */
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getAlarmTime() {
		return alarmTime;
	}

	/**
	 * @param the
	 *            alarmTime to set
	 */

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	/**
	 * @return the alarmClearTime
	 */
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getAlarmClearTime() {
		return alarmClearTime;
	}

	/**
	 * @param the
	 *            alarmClearTime to set
	 */

	public void setAlarmClearTime(Date alarmClearTime) {
		this.alarmClearTime = alarmClearTime;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RealtimeAlarmBean [boxId=" + boxId + ", channelNo=" + channelNo
				+ ", channelType=" + channelType + ", alarmCode=" + alarmCode
				+ ", alarmType=" + alarmType + ", alarmReason=" + alarmReason
				+ ", alarmTime=" + alarmTime + ", alarmClearTime="
				+ alarmClearTime + "]";
	}

}
