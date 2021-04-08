/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.testManage.terminal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 终端软件升级的历史表
 * 
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-10
 */
// @Entity
// @Table(name = "IADS_HIS_SOFTWARE_UPGRADE")
public class HistorySoftUpgrade implements Comparable<HistorySoftUpgrade> {

	private Long id;

	/**
	 * 下发的终端
	 */
	private Terminal terminal;

	/**
	 * 下发的时间
	 */
	private Date sendDate;

	/**
	 * 软件更新信息
	 */
	private SoftwareUpgrade softwareUpgrade;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
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
	 * @return the terminal
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Terminal.class)
	@JoinColumn(name = "TERMINAL_ID")
	public Terminal getTerminal() {
		return terminal;
	}

	/**
	 * @param terminal
	 *            the terminal to set
	 */
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	/**
	 * @return the sendDate
	 */
	@Column(name = "SEND_DATE")
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate
	 *            the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @param softwareUpgrade
	 *            the softwareUpgrade to set
	 */
	public void setSoftwareUpgrade(SoftwareUpgrade softwareUpgrade) {
		this.softwareUpgrade = softwareUpgrade;
	}

	/**
	 * @return the softwareUpgrade
	 */
	/**
	 * @return the histSoftUpgrades
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	public SoftwareUpgrade getSoftwareUpgrade() {
		return softwareUpgrade;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof HistorySoftUpgrade)) {
			return false;
		}
		HistorySoftUpgrade hs = (HistorySoftUpgrade) o;
		return hs.getSendDate().compareTo(getSendDate()) == 0;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + getSendDate().hashCode();
		return result;

	}

	@Override
	public int compareTo(HistorySoftUpgrade o) {
		if (o != null) {
			return o.getSendDate().compareTo(this.getSendDate());
		}
		return 0;
	}

}
