/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.testManage.terminal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.datang.common.util.CollectionUtils;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;
import com.datang.domain.platform.projectParam.CellInfo;

/**
 * 终端域,一二级域
 * 
 * @author songzhigang
 * @version 1.0.0, 2010-12-22
 */
@Entity
@Table(name = "IADS_TERMINAL_GROUP")
public class TerminalGroup implements java.io.Serializable,
		Comparable<TerminalGroup> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4972811139514045040L;

	/**
	 * 终端分组id
	 */
	private Long id;

	/**
	 * 分组名称
	 */
	private String name;

	/**
	 * 分组包含的终端集合
	 */
	private Set<Terminal> terminals = new HashSet<>();

	/**
	 * 终端分组信息，组中可以包含组信息，用于建立一二级或者多级子域
	 */
	private Set<TerminalGroup> groups;

	/**
	 * 各运营商的小区信息
	 */
	private Set<CellInfo> cellInfos = new HashSet<>();
	
	/**
	 * 各区域的自定义报表模板信息
	 */
//	private Set<CustomReportTemplatePojo> customReportTemplates = new HashSet<>();

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
	 * @return the name
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the terminals
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "terminalGroup")
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("name")
	@JSON(serialize = false)
	public Set<Terminal> getTerminals() {
		return terminals;
	}

	/**
	 * @param terminals
	 *            the terminals to set
	 */
	public void setTerminals(Set<Terminal> terminals) {
		this.terminals = terminals;
	}

	/**
	 * @return the groups
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	@Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@Fetch(FetchMode.SUBSELECT)
	public Set<TerminalGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(Set<TerminalGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the cellInfoscellInfos
	 */

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = javax.persistence.CascadeType.ALL)
	@JSON(serialize = false)
	public Set<CellInfo> getCellInfos() {
		return cellInfos;
	}

	/**
	 * @param cellInfos
	 *            the cellInfos to set
	 */
	public void setCellInfos(Set<CellInfo> cellInfos) {
		this.cellInfos = cellInfos;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = javax.persistence.CascadeType.ALL)
//	@JSON(serialize = false)
//	public Set<CustomReportTemplatePojo> getCustomReportTemplates() {
//		return customReportTemplates;
//	}
//
//	public void setCustomReportTemplates(Set<CustomReportTemplatePojo> customReportTemplates) {
//		this.customReportTemplates = customReportTemplates;
//	}

	/**
	 * 添加group
	 * 
	 * @param TerminalGroup
	 *            group
	 */
	@Transient
	public void addGroup(TerminalGroup group) {
		if (this.groups == null) {
			this.groups = new HashSet<TerminalGroup>();
		}
		this.groups.add(group);
	}

	/**
	 * 获取分组
	 * 
	 * @param groupName
	 *            分组名
	 * @return 分组
	 */
	@Transient
	public TerminalGroup getGroup(String groupName) {
		if (!CollectionUtils.isEmpty(this.groups)) {
			for (TerminalGroup group : groups) {
				if (group.getName().equals(groupName)) {
					return group;
				}
			}
		}
		return null;
	}

	/**
	 * 删除group
	 * 
	 * @param TerminalGroup
	 *            group
	 */
	@Transient
	public void delGroup(TerminalGroup group) {
		if (this.groups == null) {
			this.groups = new HashSet<TerminalGroup>();
		}
		this.groups.remove(group);
	}

	/**
	 * 添加Terminal
	 * 
	 * @param terminal
	 *            Terminal
	 */
	@Transient
	public void addTerminal(Terminal terminal) {
		terminal.setTerminalGroup(this);
		if (this.terminals == null) {
			this.terminals = new HashSet<Terminal>();
		}
		this.terminals.add(terminal);
	}

	/**
	 * 删除Terminal
	 * 
	 * @param terminal
	 *            Terminal
	 */
	@Transient
	public void delTerminal(Terminal terminal) {
		terminal.setTerminalGroup(null);
		if (this.terminals == null) {
			this.terminals = new HashSet<Terminal>();
		}
		this.terminals.remove(terminal);
	}

	/**
	 * 获取运营商下的小区信息
	 * 
	 * @param operatorType
	 * @return
	 */
	@Transient
	public CellInfo getCellInfo(String operatorType) {
		for (CellInfo cellInfo : cellInfos) {
			if (cellInfo.getOperatorType().equals(operatorType)) {
				return cellInfo;
			}
		}
		return null;
	}


	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof TerminalGroup)) {
			return false;
		}
		TerminalGroup tg = (TerminalGroup) o;
		return id.equals(tg.id);
	}

	public int hashCode() {
		int result = 17;
		if (id != null) {
			result = (37 * result + (int) (id.longValue() & id.longValue() >>> 32));
		}
		return result;
	}

	@Override
	public int compareTo(TerminalGroup o) {
		if (getId().longValue() > o.getId().longValue()) {
			return 1;
		} else if (getId().longValue() == o.getId().longValue()) {
			return 0;
		} else {
			return -1;
		}
	}
}