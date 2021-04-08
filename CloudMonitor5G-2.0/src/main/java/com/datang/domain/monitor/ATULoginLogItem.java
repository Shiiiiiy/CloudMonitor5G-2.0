package com.datang.domain.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * ATU登陆日志
 * 
 * @explain
 * @name ATULoginLogItem
 * @author shenyanwei
 * @date 2016年7月11日下午1:52:51
 */
@Entity
@Table(name = "IADS_REALTIME_ATU_LOGIN")
public class ATULoginLogItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String boxId;
	private Date loginTime;
	private Long loginTimeLong;
	private Date offlineTime;
	private Long offlineTimeLong;
	private Integer status;
	private Integer failReason;
	private Integer sessionId;
	private Integer testPlanVersion;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the boxId
	 */
	@Column(name = "BOX_ID")
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param boxId
	 *            the boxId to set
	 */
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the status
	 */
	@Column(name = "LOGIN_STATUS")
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the failReason
	 */
	@Column(name = "LOGIN_FAIL_CAUSE")
	public Integer getFailReason() {
		return failReason;
	}

	/**
	 * @param failReason
	 *            the failReason to set
	 */
	public void setFailReason(Integer failReason) {
		this.failReason = failReason;
	}

	/**
	 * @return the sessionId
	 */
	@Column(name = "SESSION_ID")
	public Integer getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the testPlanVersion
	 */
	@Column(name = "TESTPLAN_VERSION")
	public Integer getTestPlanVersion() {
		return testPlanVersion;
	}

	/**
	 * @param testPlanVersion
	 *            the testPlanVersion to set
	 */
	public void setTestPlanVersion(Integer testPlanVersion) {
		this.testPlanVersion = testPlanVersion;
	}

	/**
	 * @return the loginTime
	 */
	@Transient
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
	 * @return the loginTimeLong
	 */
	@Column(name = "LOGIN_TIME")
	public Long getLoginTimeLong() {
		return loginTimeLong;
	}

	/**
	 * @param the
	 *            loginTimeLong to set
	 */

	public void setLoginTimeLong(Long loginTimeLong) {
		this.loginTimeLong = loginTimeLong;
		if (null != loginTimeLong) {
			this.loginTime = new Date(loginTimeLong);
		}
	}

	/**
	 * @return the offlineTime
	 */
	@Transient
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
	 * @return the offlineTimeLong
	 */
	@Column(name = "OFFLINE_TIME")
	public Long getOfflineTimeLong() {
		return offlineTimeLong;
	}

	/**
	 * @param the
	 *            offlineTimeLong to set
	 */

	public void setOfflineTimeLong(Long offlineTimeLong) {
		this.offlineTimeLong = offlineTimeLong;
		if (null != offlineTimeLong) {
			this.offlineTime = new Date(offlineTimeLong);
		}
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ATULoginLogItem [id=" + id + ", boxId=" + boxId
				+ ", loginTime=" + loginTime + ", loginTimeLong="
				+ loginTimeLong + ", offlineTime=" + offlineTime
				+ ", offlineTimeLong=" + offlineTimeLong + ", status=" + status
				+ ", failReason=" + failReason + ", sessionId=" + sessionId
				+ ", testPlanVersion=" + testPlanVersion + "]";
	}

	// /**
	// * @return the terminal
	// */
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "TERMINAL_ID")
	// public Terminal getTerminal() {
	// return terminal;
	// }
	//
	// /**
	// * @param terminal
	// * the terminal to set
	// */
	// public void setTerminal(Terminal terminal) {
	// this.terminal = terminal;
	// }

}
