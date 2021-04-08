package com.datang.domain.domain5g.activationShow;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 活跃度统计
 * @author maxuancheng
 * @date 2019年3月15日
 */
@Entity
@Table(name = "IADS_5G_SOFTWARE_USETIME")
public class ActivationShowPojo {

	private Long id;
	
	private String softwareVersion;
	
	private int softwareType;
	
	private Date startTime;
	
	private Long useTime;
	
	private TerminalGroup terminalGroup;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 软件版本
	 */
	@Column(name = "SOFTWARE_VERSION")
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	
	/**
	 * 软件版本
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * 软件类型 1 APP 2 Outum
	 */
	@Column(name = "SOFTWARE_TYPE")
	public int getSoftwareType() {
		return softwareType;
	}

	/**
	 * 软件类型 1 APP 2 Outum
	 */
	public void setSoftwareType(int softwareType) {
		this.softwareType = softwareType;
	}

	/**
	 * 软件开始使用时间
	 */
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 软件开始使用时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 软件使用时长(分钟)
	 */
	@Column(name = "USE_TIME")
	public Long getUseTime() {
		return useTime;
	}

	/**
	 * 软件使用时长(分钟)
	 */
	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}

	/**
	 * 分组
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TERMINAL_GROUP_ID")
	public TerminalGroup getTerminalGroup() {
		return terminalGroup;
	}
	
	/**
	 * 分组
	 */
	public void setTerminalGroup(TerminalGroup terminalGroup) {
		this.terminalGroup = terminalGroup;
	}
	
	
}
