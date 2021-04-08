/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.bean.testManage.terminal;

import com.datang.bean.AbstractPagedQuery;
import com.datang.domain.testManage.terminal.Terminal;

/**
 * 终端分页查询
 * 
 * @author dingzhongchang
 * @version 1.0.0
 */
public class TerminalQuery extends AbstractPagedQuery<Terminal> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2710760422548908452L;

	/**
	 * boxId
	 */
	private String boxId;

	/**
	 * 终端名
	 */
	private String name;

	/**
	 * 排序属性
	 */
	private String orderProperty;

	/**
	 * 组ID
	 */
	private Long groupId;

	/**
	 * 是否升序
	 */
	private boolean asc;

	/**
	 * 在线状态
	 */
	private Boolean onlineStatus;

	/**
	 * @return the boxId
	 */
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param orderProperty
	 *            the orderProperty to set
	 */
	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	/**
	 * @return the orderProperty
	 */
	public String getOrderProperty() {
		return orderProperty;
	}

	/**
	 * @param asc
	 *            the asc to set
	 */
	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	/**
	 * @return the asc
	 */
	public boolean isAsc() {
		return asc;
	}

	/**
	 * @param onlineStatus
	 *            the onlineStatus to set
	 */
	public void setOnlineStatus(Boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	/**
	 * @return the onlineStatus
	 */
	public Boolean isOnlineStatus() {
		return onlineStatus;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

}
