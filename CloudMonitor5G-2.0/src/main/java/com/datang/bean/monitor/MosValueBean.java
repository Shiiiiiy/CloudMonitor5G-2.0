package com.datang.bean.monitor;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * MOS监控响应Bean
 * 
 * @explain
 * @name MosValueBean
 * @author shenyanwei
 * @date 2016年7月25日上午9:51:20
 */
public class MosValueBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8475309880036538790L;

	private String boxId;
	/** 通道号 */
	private String channelNo;
	/** 通道类型 */
	private String channelType;
	/** MOS时间 */
	private Date mosTime;
	/** 实时下行MOS值，Float类型 */
	private Float mosValue;

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
	 * @return the mosTime
	 */
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
	 * @return the mosValue
	 */
	public Float getMosValue() {
		return mosValue;
	}

	/**
	 * @param the
	 *            mosValue to set
	 */

	public void setMosValue(Float mosValue) {
		this.mosValue = mosValue;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MosValueBean [boxId=" + boxId + ", channelNo=" + channelNo
				+ ", channelType=" + channelType + ", mosTime=" + mosTime
				+ ", mosValue=" + mosValue + "]";
	}

}
