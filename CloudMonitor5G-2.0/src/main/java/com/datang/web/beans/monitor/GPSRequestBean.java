package com.datang.web.beans.monitor;

import java.util.Date;

/**
 * 地图请求Bean
 * 
 * @explain
 * @name GPSRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:49:21
 */
public class GPSRequestBean {
	/**
	 * 开始时间
	 */
	public Date beginDate;
	/**
	 * 结束时间
	 */
	public Date endDate;
	/**
	 * 终端boxID
	 */
	public String boxID;

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param the
	 *            beginDate to set
	 */

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param the
	 *            endDate to set
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the boxID
	 */
	public String getBoxID() {
		return boxID;
	}

	/**
	 * @param the
	 *            boxID to set
	 */

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GPSRequestBean [beginDate=" + beginDate + ", endDate="
				+ endDate + ", boxID=" + boxID + "]";
	}

}
