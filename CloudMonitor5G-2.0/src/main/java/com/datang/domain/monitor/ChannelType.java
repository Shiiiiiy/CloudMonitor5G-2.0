package com.datang.domain.monitor;

/**
 * @explain
 * @name ChannelType
 * @author shenyanwei
 * @date 2016年7月11日下午1:52:20
 */
public enum ChannelType {
	GSM("GSM"), CDMA("CDMA"), TD_SCDMA("TD_SCDMA"), WCDMA("WCDMA"), EVDO("EVDO"), WLAN(
			"WLAN"), LTE("LTE");

	ChannelType(String channelType) {
		this.channelType = channelType;
	}

	private String channelType;

	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
	}

	/**
	 * @param channelType
	 *            the channelType to set
	 */
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String toString() {
		return this.channelType;
	}
}
