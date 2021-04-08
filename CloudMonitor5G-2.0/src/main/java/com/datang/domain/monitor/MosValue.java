package com.datang.domain.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * @explain
 * @name MosValue
 * @author shenyanwei
 * @date 2016年7月11日下午1:31:40
 */
@Entity
@Table(name = "IADS_REALTIME_MOS")
public class MosValue {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8475309880036538790L;
	private Long id;
	private String boxId;
	/** 通道号 */
	private Integer channelNo;
	/** 通道类型 */
	private Integer channelType;

	/** MOS时间 */
	private Date mosTime;
	private Long mosTimeLong;
	/** 实时下行MOS值，Float类型 */
	private Float mosValue;

	/**
	 * @return the mosValue
	 */
	@Column(name = "MOS_VALUE")
	public Float getMosValue() {
		return mosValue;
	}

	/**
	 * @param mosValue
	 *            the mosValue to set
	 */
	public void setMosValue(Float mosValue) {
		this.mosValue = mosValue;
	}

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
	 * @return the mosTime
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getMosTime() {
		return mosTime;
	}

	/**
	 * @param the
	 *            mosTime to set
	 */

	public void setMosTime(Date mosTime) {
		this.mosTime = mosTime;
	}

	/**
	 * @return the mosTimeLong
	 */
	@Column(name = "MOS_TIME")
	public Long getMosTimeLong() {
		return mosTimeLong;
	}

	/**
	 * @param the
	 *            mosTimeLong to set
	 */

	public void setMosTimeLong(Long mosTimeLong) {
		this.mosTimeLong = mosTimeLong;
		if (null != mosTimeLong) {
			this.mosTime = new Date(mosTimeLong);
		}
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MosValue [id=" + id + ", boxId=" + boxId + ", channelNo="
				+ channelNo + ", channelType=" + channelType + ", mosTime="
				+ mosTime + ", mosTimeLong=" + mosTimeLong + ", mosValue="
				+ mosValue + "]";
	}

}
