/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.domain.security.menu;

/**
 * 设备区域类型
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 上午10:42:35
 * @version
 */
public enum MenuType {

	/**
	 * 省级子域
	 */
	Province("Province"),

	/**
	 * 市级子域
	 */
	City("City"),

	/**
	 * 县级子域
	 */
	County("County"),

	/**
	 * 终端级子域
	 */
	Terminal("Terminal"),

	/**
	 * 未知
	 */
	Unknown("Unknown");

	private String menuType;

	MenuType(String menuType) {
		this.menuType = menuType;
	}

	public String toString() {
		return this.menuType;
	}

	/**
	 * @return the menuType
	 */
	public String getMenuType() {
		return menuType;
	}

	/**
	 * @param menuType
	 *            the menuType to set
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

}
