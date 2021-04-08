package com.datang.web.beans.monitor;

/**
 * ATU监控请求Bean
 * 
 * @explain
 * @name ATULoginRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:36:40
 */
public class ATULoginRequestBean extends PublicRequestBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ATULoginRequestBean() {
		super();
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ATULoginRequestBean [beginDate=" + beginDate + ", endDate="
				+ endDate + ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", boxIds=" + boxIds + ", boxIdsSet="
				+ boxIdsSet + "]";
	}

}
