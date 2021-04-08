/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal;

import java.util.Collection;
import java.util.List;

import com.datang.bean.testManage.terminal.TerminalMenuQuery;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;

/**
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-9
 */
public interface TerminalMenuService {

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单
	 */
	void create(TerminalMenu menu);

	/**
	 * 删除菜单
	 * 
	 * @param menuId
	 *            菜单ID
	 */
	void del(Long menuId);

	/**
	 * 更新菜单
	 * 
	 * @param menu
	 *            菜单
	 */
	void update(TerminalMenu menu);

	/**
	 * 查询菜单
	 * 
	 * @param name
	 *            菜单名称
	 * @return TerminalMenu
	 */
	TerminalMenu get(String name);
	
	/**
	 * 查询菜单
	 * 目的：去除多线程下懒加载的不利影响
	 * @param name
	 *            菜单名称
	 * @return TerminalMenu
	 */
	TerminalMenu get2(String name);

	/**
	 * 查询菜单
	 * 
	 * @param refId
	 *            映射ID
	 * @return 菜单
	 */
	TerminalMenu getByRefId(Long refId);

	/**
	 * 查询菜单
	 * 
	 * @param type
	 *            菜单类型
	 * @return Collection<TerminalMenu>
	 */
	Collection<TerminalMenu> get(MenuType type);

	/**
	 * 查询菜单
	 * 
	 * @param id
	 *            菜单Id
	 * @return TerminalMenu
	 */
	TerminalMenu get(Long id);

	/**
	 * 获取所有菜单
	 * 
	 * @return List<TerminalMenu>
	 */
	List<TerminalMenu> getAllMenuInfo();

	/**
	 * 查询菜单的id
	 * 
	 * @return List<TerminalMenu>
	 */
	List<Long> queryTerminalIds(TerminalMenuQuery terminalMenuQuery);

	/**
	 * 查询菜单的id
	 * 
	 * @param terminalMenuQuery
	 * @return List<TerminalMenu>
	 */

	List<Long> querySwTerminalIds(TerminalMenuQuery terminalMenuQuery);

}
