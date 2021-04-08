/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.testManage.terminal;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 终端软件升级管理实体
 * 
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-10
 */
// @Entity
// @Table(name = "IADS_SOFTWARE_UPGRADE")
public class SoftwareUpgrade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	/**
	 * 上传人
	 */
	// yzp
	// private User user;

	/**
	 * 软件版本
	 */
	private String softwareVersion;

	/**
	 * 上传时间
	 */
	private Date uploadDate;

	/**
	 * 状态(下发（send）或者未下发(unSend))
	 */
	private String status;

	/**
	 * 厂商
	 */
	private String vender;

	/**
	 * 历史表
	 */
	private Set<HistorySoftUpgrade> histSoftUpgrades;

	/**
	 * 文件路径
	 */
	private String path;
	/**
	 * 测试目标
	 */
	private Integer testTarget;

	/**
	 * @return the user
	 */
	// yzp
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "SOFTWARE_UPGRADE_User_ID")
	// public User getUser() {
	// return user;
	// }

	/**
	 * @param user
	 *            the user to set
	 */
	// yzp
	// public void setUser(User user) {
	// this.user = user;
	// }

	/**
	 * @return the softwareVersion
	 */
	@Column(name = "SOFTWARE_VER")
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
	 * @return the uploadDate
	 */
	@Column(name = "UPLOAD_DATE")
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uploadDate
	 *            the uploadDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * @return the status
	 */
	@Column(name = "STATUS")
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
	@Column(name = "VENDER")
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
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	/**
	 * @param histSoftUpgrades
	 *            the histSoftUpgrades to set
	 */
	public void setHistSoftUpgrades(Set<HistorySoftUpgrade> histSoftUpgrades) {
		this.histSoftUpgrades = histSoftUpgrades;
	}

	/**
	 * @return the histSoftUpgrades
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "softwareUpgrade")
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	// @JoinColumn(name = "HISOFT_TERMINAL_ID", nullable = true)
	@Fetch(FetchMode.SUBSELECT)
	public Set<HistorySoftUpgrade> getHistSoftUpgrades() {
		return histSoftUpgrades;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	@Column(name = "FILE_PATH")
	public String getPath() {
		return path;
	}

	@Transient
	public void addHistory(HistorySoftUpgrade history) {
		if (histSoftUpgrades == null) {
			histSoftUpgrades = new HashSet<HistorySoftUpgrade>();
		}
		histSoftUpgrades.add(history);

	}

	@Column(name = "TEST_TARGET")
	public Integer getTestTarget() {
		return testTarget;
	}

	public void setTestTarget(Integer testTarget) {
		this.testTarget = testTarget;
	}

	@Transient
	public String getTestTargetStr() {
		if (this.getTestTarget() != null) {
			if (this.getTestTarget() == 0) {
				return "ADT";
			}
			if (this.getTestTarget() == 1) {
				return "CQT";
			}
			if (this.getTestTarget() == 2) {
				return "L1";
			}
		}
		return "";
	}
}
