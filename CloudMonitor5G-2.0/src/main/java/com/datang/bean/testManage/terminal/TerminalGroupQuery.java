/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.bean.testManage.terminal;

import com.datang.bean.AbstractPagedQuery;
import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * @author dingzhongchang
 * @version 1.0.0
 */
public class TerminalGroupQuery extends AbstractPagedQuery<TerminalGroup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 终端分组名
	 */
	private String name;

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

}
