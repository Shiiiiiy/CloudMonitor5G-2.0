package com.datang.web.beans.testPlan;

import java.io.Serializable;

/**
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:48:52
 * @version
 */
public class DateBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3489331354106656164L;
	/**
	 * 开始时间
	 */
	private String startDate;
	/**
	 * 结束时间
	 */
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
