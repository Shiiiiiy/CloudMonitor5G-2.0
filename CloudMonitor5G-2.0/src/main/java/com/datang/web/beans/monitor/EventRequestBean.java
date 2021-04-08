package com.datang.web.beans.monitor;

/**
 * 事件监控请求Bean
 * 
 * @explain
 * @name EventRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:45:57
 */
public class EventRequestBean extends PublicRequestBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 通道号
	 */
	private Integer ueNo;
	/**
	 * 事件代码
	 */
	private Integer eventCode;

	/**
	 * @return the ueNo
	 */
	public Integer getUeNo() {
		return ueNo;
	}

	/**
	 * @param the
	 *            ueNo to set
	 */

	public void setUeNo(Integer ueNo) {
		this.ueNo = ueNo;
	}

	/**
	 * @return the eventCode
	 */
	public Integer getEventCode() {
		return eventCode;
	}

	/**
	 * @param the
	 *            eventCode to set
	 */

	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventRequestBean [ueNo=" + ueNo + ", eventCode=" + eventCode
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", boxIds=" + boxIds + ", boxIdsSet="
				+ boxIdsSet + "]";
	}

}
