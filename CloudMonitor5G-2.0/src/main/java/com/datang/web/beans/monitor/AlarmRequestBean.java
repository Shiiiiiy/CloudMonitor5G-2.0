package com.datang.web.beans.monitor;

/**
 * 告警请求Bean
 * 
 * @explain
 * @name AlarmRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:36:04
 */
public class AlarmRequestBean extends PublicRequestBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176078388182457911L;
	/** 通道号 */
	private Integer ueNo;
	/** 告警代码 */
	private Integer alarmCode;

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
	 * @return the alarmCode
	 */
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

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlarmRequestBean [ueNo=" + ueNo + ", alarmCode=" + alarmCode
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", boxIds=" + boxIds + ", boxIdsSet="
				+ boxIdsSet + "]";
	}

}
