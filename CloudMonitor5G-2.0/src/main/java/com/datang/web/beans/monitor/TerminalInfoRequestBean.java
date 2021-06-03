package com.datang.web.beans.monitor;

/**
 * 终端信息请求Bean
 * 
 * @explain
 * @name TerminalInfoRequestBean
 * @author shenyanwei
 * @date 2016年8月15日上午11:07:47
 */
public class TerminalInfoRequestBean extends PublicRequestBean {
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
