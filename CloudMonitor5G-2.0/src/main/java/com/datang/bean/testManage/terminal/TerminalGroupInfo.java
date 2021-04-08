/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.bean.testManage.terminal;

import java.util.List;

import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 子域信息
 * 
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-9
 */
public class TerminalGroupInfo implements Comparable<TerminalGroupInfo> {

	/**
	 * 终端分组信息
	 */
	private TerminalGroup group;

	/**
	 * 包含子域信息
	 */
	private List<TerminalGroupInfo> subGroupInfos;

	/**
	 * 分组名
	 */
	private String groupName;

	/**
	 * 分组包含的子域数量
	 */
	private Integer numGroups = 0;

	/**
	 * 所有子域包含的终端的数量
	 */
	private Integer numTerminals = 0;

	/**
	 * @return the group
	 */
	public TerminalGroup getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(TerminalGroup group) {
		this.group = group;
	}

	/**
	 * @return the numGroups
	 */
	public Integer getNumGroups() {
		return numGroups;
	}

	/**
	 * @param numGroups
	 *            the group to set
	 */
	public void setNumGroups(Integer numGroups) {
		this.numGroups = numGroups;
	}

	/**
	 * @return the numTerminals
	 */
	public Integer getNumTerminals() {
		return numTerminals;
	}

	/**
	 * 
	 * @param numTerminals
	 *            the group to set
	 */
	public void setNumTerminals(Integer numTerminals) {
		this.numTerminals = numTerminals;
	}

	/**
	 * @return the subGroupInfos
	 */
	public List<TerminalGroupInfo> getSubGroupInfos() {
		return subGroupInfos;
	}

	/**
	 * 
	 * @param numTerminals
	 *            the group to set
	 */
	public void setSubGroupInfos(List<TerminalGroupInfo> subGroupInfos) {
		this.subGroupInfos = subGroupInfos;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		groupName = group.getName();
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TerminalGroupInfo o) {
		if (o != null) {
			return o.getGroupName().compareTo(groupName);
		}
		return 0;
	}

}
