package com.datang.domain.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 告警 监控
 * 
 * @explain
 * @name RealtimeAlarm
 * @author shenyanwei
 * @date 2016年7月11日下午1:55:16
 */
@Entity
@Table(name = "IADS_REALTIME_ALARM")
public class RealtimeAlarm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7498152737414574114L;
	private Long id;
	private String boxId;
	/** 通道号 */
	private Integer channelNo;
	/** 通道类型 */
	private Integer channelType;
	/** 告警信息代码 */
	private Integer alarmCode;
	/** 告警发生时间 */
	private Date alarmTime;
	private Long alarmTimeLong;
	/** 告警清除时间 */
	private Date alarmClearTime;
	private Long alarmClearTimeLong;

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
	 * @return the alarmCode
	 */
	@Column(name = "ALARM_CODE")
	public Integer getAlarmCode() {
		return alarmCode;
	}

	/**
	 * @param the
	 *            alarmCode to set
	 */

	public void setAlarmCode(Integer alarmCode) {
		this.alarmCode = alarmCode;
	}

	// /**
	// * @return the alarmReason
	// */
	// @Column(name = "ALARM_REASON")
	// public String getAlarmReason() {
	// return alarmReason;
	// }
	//
	// /**
	// * @param the
	// * alarmReason to set
	// */
	//
	// public void setAlarmReason(String alarmReason) {
	// this.alarmReason = alarmReason;
	// }

	/**
	 * @return the alarmTime
	 */
	@Transient
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
	 * @return the alarmTimeLong
	 */
	@Column(name = "ALARM_TIME")
	public Long getAlarmTimeLong() {
		return alarmTimeLong;
	}

	/**
	 * @param the
	 *            alarmTimeLong to set
	 */

	public void setAlarmTimeLong(Long alarmTimeLong) {
		this.alarmTimeLong = alarmTimeLong;
		if (null != alarmTimeLong) {
			this.alarmTime = new Date(alarmTimeLong);
		}
	}

	/**
	 * @return the alarmClearDate
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getAlarmClearTime() {
		return alarmClearTime;
	}

	/**
	 * @param the
	 *            alarmClearDate to set
	 */

	public void setAlarmClearTime(Date alarmClearTime) {
		this.alarmClearTime = alarmClearTime;
	}

	/**
	 * @return the alarmClearTimeLong
	 */
	@Column(name = "ALARM_CLEAR_TIME")
	public Long getAlarmClearTimeLong() {
		return alarmClearTimeLong;
	}

	/**
	 * @param the
	 *            alarmClearTimeLong to set
	 */

	public void setAlarmClearTimeLong(Long alarmClearTimeLong) {
		this.alarmClearTimeLong = alarmClearTimeLong;
		if (null != alarmClearTimeLong) {
			this.alarmClearTime = new Date(alarmClearTimeLong);
		}
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RealtimeAlarm [id=" + id + ", boxId=" + boxId + ", channelNo="
				+ channelNo + ", channelType=" + channelType + ", alarmCode="
				+ alarmCode + ",  alarmTime=" + alarmTime + ", alarmTimeLong="
				+ alarmTimeLong + ", alarmClearTime=" + alarmClearTime
				+ ", alarmClearTimeLong=" + alarmClearTimeLong + "]";
	}

}
