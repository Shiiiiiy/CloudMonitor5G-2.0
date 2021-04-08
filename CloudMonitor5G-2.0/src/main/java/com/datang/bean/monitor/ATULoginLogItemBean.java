package com.datang.bean.monitor;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * ATU登陆日志响应Bean
 * 
 * @explain
 * @name ATULoginLogItemBean
 * @author shenyanwei
 * @date 2016年7月25日上午9:31:16
 */
public class ATULoginLogItemBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String boxId;
	private Date loginTime;
	private Date offlineTime;
	private String status;
	private String failReason;
	private String sessionId;
	private String testPlanVersion;

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
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
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
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
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

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param the
	 *            status to set
	 */

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the failReason
	 */
	public String getFailReason() {
		return failReason;
	}

	/**
	 * @param the
	 *            failReason to set
	 */

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param the
	 *            sessionId to set
	 */

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the testPlanVersion
	 */
	public String getTestPlanVersion() {
		return testPlanVersion;
	}

	/**
	 * @param the
	 *            testPlanVersion to set
	 */

	public void setTestPlanVersion(String testPlanVersion) {
		this.testPlanVersion = testPlanVersion;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ATULoginLogItemBean [boxId=" + boxId + ", loginTime="
				+ loginTime + ", offlineTime=" + offlineTime + ", status="
				+ status + ", failReason=" + failReason + ", sessionId="
				+ sessionId + ", testPlanVersion=" + testPlanVersion + "]";
	}

}
