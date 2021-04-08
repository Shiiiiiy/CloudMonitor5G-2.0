/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.bean.testManage.terminal;

import java.util.Set;

/**
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-9
 */
public class TerminalMenuQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5614701726483779824L;
	/**
	 * 菜单相关的省的id
	 */
	private Long provinceId;
	/**
	 * 菜单相关的市的id
	 */

	private Long cityId;
	/**
	 * 用户有权限操作的终端的id集合
	 */
	private Set<Long> terminalIds;
	/**
	 * 终端名称
	 */
	private String name;
	/**
	 * 测试目标
	 */
	private Integer testTarget;

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
	 * @return the provinceId
	 */
	public Long getProvinceId() {
		return provinceId;
	}

	/**
	 * @param provinceId
	 *            the provinceId to set
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	/**
	 * @return the cityId
	 */
	public Long getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the terminalIds
	 */
	public Set<Long> getTerminalIds() {
		return terminalIds;
	}

	/**
	 * @param terminalIds
	 *            the terminalIds to set
	 */
	public void setTerminalIds(Set<Long> terminalIds) {
		this.terminalIds = terminalIds;
	}

	public Integer getTestTarget() {
		return testTarget;
	}

	public void setTestTarget(Integer testTarget) {
		this.testTarget = testTarget;
	}

}
