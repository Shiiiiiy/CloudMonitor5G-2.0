package com.datang.service.platform.security;

import java.util.Collection;
import java.util.List;

import com.datang.domain.security.User;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;

/**
 * Menu服务
 * 
 * @author yinzhipeng
 * @date:2015年10月12日 下午1:09:04
 * @version
 */
public interface IMenuManageService {
	/**
	 * 获取用户对应的菜单
	 * 
	 * @param user
	 * @return
	 */
	public List<Menu> getMenus(User user);

	/**
	 * 获取所有区域菜单
	 * 
	 * @return
	 */
	public Collection<TerminalMenu> getAllTerminalMenus();

	/**
	 * 根据ID获取Menu对象
	 * 
	 * @param menuId
	 *            用户id
	 * @return
	 */
	public Menu getMenuById(Long menuId);

	/**
	 * 得到用户可以有权限操作的省
	 * 
	 * @return
	 */
	public List<TerminalMenu> getProvinces();

	/**
	 * 得到用户有权限操作的市
	 * 
	 * @return
	 */
	public List<TerminalMenu> getCities(Long refid);

	/**
	 * 得到用户有权限操作的市
	 * 
	 * @return
	 */
	public List<TerminalMenu> getCities();

	/**
	 * 得到用户有权限操作的市的id
	 * 
	 * @return
	 */
	public List<Long> getCityIds();

	/**
	 * 得到用户有权限操作的市根据PID
	 * 
	 * @param pid
	 * @return
	 */
	public List<TerminalMenu> getCitiesByPid(Long pid);

	/**
	 * 得到当前用户有权限操作的终端
	 * 
	 * @return
	 */
	public List<TerminalMenu> getTerminals();

	/**
	 * 得到当前用户有权限操作的终端ID列表
	 * 
	 * @return
	 */
	public List<Long> getTerminalIDs();

	/**
	 * 得到当前用户有权限操作的终端boxID列表
	 * 
	 * @return
	 */
	public List<String> getTerminalBoxIDs();

	/**
	 * 获取用户的区域菜单集合
	 * 
	 * @param user
	 * @return
	 */
	public List<TerminalMenu> getTerminalMenusAll(User user, MenuType type);

	/**
	 * 
	 * 
	 * @param pid
	 * @param type
	 * @return
	 */
	public List<TerminalMenu> getTerminalByPid(Long pid, MenuType type);

	/**
	 * 获取根区域权限
	 * 
	 * @return
	 */
	public List<TerminalMenu> getTopTerminalMenus();

	/**
	 * 修改Menu
	 * @author maxuancheng
	 * date:2020年2月14日 下午2:15:14
	 * @param menu
	 */
	public void updateMenu(Menu menu);
}
