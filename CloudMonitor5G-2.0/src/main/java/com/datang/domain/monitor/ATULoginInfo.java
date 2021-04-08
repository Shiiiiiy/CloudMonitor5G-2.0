package com.datang.domain.monitor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @explain
 * @name ATULoginInfo
 * @author shenyanwei
 * @date 2016年7月28日下午5:35:56
 */
public class ATULoginInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5886194134622314120L;
	private String boxId;
	private Date loginTime;
	private Date offlineTime;

	public String getLoginTimeString() {
		if (null != loginTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(loginTime.getTime()));
		}
		return null;
	}

	public String getOfflineTimeString() {
		if (null != offlineTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(offlineTime.getTime()));
		}
		return null;
	}

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
	 * @return the loginTime
	 */

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ATULoginInfo [boxId=" + boxId + ", loginTime=" + loginTime
				+ ", offlineTime=" + offlineTime + "]";
	}

	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * @param the
	 *            loginTime to set
	 */

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the offlineTime
	 */
	public Date getOfflineTime() {
		return offlineTime;
	}

	/**
	 * @param the
	 *            offlineTime to set
	 */

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

}
