/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.bean.testManage.terminal;

import java.util.Date;

import com.datang.bean.AbstractPagedQuery;
import com.datang.domain.testManage.terminal.SoftwareUpgrade;

/**
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-11
 */
public class SoftwareUpgradeQuery extends AbstractPagedQuery<SoftwareUpgrade> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 上传人
	 */
	private String userName;

	/**
	 * 软件版本
	 */
	private String softwareVersion;

	/**
	 * 开始时间（用于查询）
	 */
	private Date uploadBeginDate;

	/**
	 * 结束时间（用于查询）
	 */
	private Date uploadEndDate;

	/**
	 * 状态(下发（send）或者未下发(unsend))
	 */
	private String status;

	/**
	 * 厂商
	 */
	private String vender;
	/**
	 * 测试目标
	 */
	private Integer testTarget;

	/**
	 * @return the user
	 */
	public String getUser() {
		return userName;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion
	 *            the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the vender
	 */
	public String getVender() {
		return vender;
	}

	/**
	 * @param vender
	 *            the vender to set
	 */
	public void setVender(String vender) {
		this.vender = vender;
	}

	/**
	 * @param uploadBeginDate
	 *            the uploadBeginDate to set
	 */
	public void setUploadBeginDate(Date uploadBeginDate) {
		this.uploadBeginDate = uploadBeginDate;
	}

	/**
	 * @return the uploadBeginDate
	 */
	public Date getUploadBeginDate() {
		return uploadBeginDate;
	}

	/**
	 * @param uploadEndDate
	 *            the uploadEndDate to set
	 */
	public void setUploadEndDate(Date uploadEndDate) {
		this.uploadEndDate = uploadEndDate;
	}

	/**
	 * @return the uploadEndDate
	 */
	public Date getUploadEndDate() {
		return uploadEndDate;
	}

	public Integer getTestTarget() {
		return testTarget;
	}

	public void setTestTarget(Integer testTarget) {
		this.testTarget = testTarget;
	}
}
