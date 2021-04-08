package com.datang.domain.testManage.terminal;

/**
 * 模块类型
 * 
 * @author yinzhipeng
 * @date:2016年7月11日 下午3:20:15
 * @version
 */
public enum ChannelType {

	GSM("GSM"), CDMA("CDMA"), TDS("TDS"), WCDMA("WCDMA"), EVDO("EVDO"), TD_LTE(
			"TD_LTE"), FDD_LTE("FDD_LTE"), NB_IoT("NB_IoT"), eMTC("eMTC"), FG(
			"FG"),WECHAT("WECHAT"),APP_FG("APP_FG");
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
