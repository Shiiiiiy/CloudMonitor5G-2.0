package com.datang.web.beans.monitor;

/**
 * 硬件状态请求Bean
 * 
 * @explain
 * @name StatusRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:57:45
 */
public class StatusRequestBean extends PublicRequestBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatusRequestBean [beginDate=" + beginDate + ", endDate="
				+ endDate + ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", boxIds=" + boxIds + ", boxIdsSet="
				+ boxIdsSet + "]";
	}

}
