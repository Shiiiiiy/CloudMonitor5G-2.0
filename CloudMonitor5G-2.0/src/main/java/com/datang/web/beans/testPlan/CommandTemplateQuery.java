/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.web.beans.testPlan;

import com.datang.bean.AbstractPagedQuery;
import com.datang.domain.testPlan.CommandTemplate;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-29
 */
public class CommandTemplateQuery extends AbstractPagedQuery<CommandTemplate> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 258232574543766585L;
	/**
	 * 模版名称
	 */
	private String name;
	/**
	 * 模版类型
	 */
	private String type;

	/**
	 * @return the name
	 */
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
