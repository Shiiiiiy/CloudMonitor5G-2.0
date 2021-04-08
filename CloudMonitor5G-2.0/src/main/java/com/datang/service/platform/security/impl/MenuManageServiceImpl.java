package com.datang.service.platform.security.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.dao.platform.security.MenuDao;
import com.datang.dao.platform.security.UserDao;
import com.datang.dao.testManage.terminal.TerminalDao;
import com.datang.domain.security.User;
import com.datang.domain.security.UserGroup;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.service.platform.security.IMenuManageService;

/**
 * menu服务实现
 * 
 * @author yinzhipeng
 * @date:2015年10月12日 下午1:13:59
 * @version
 */
@Service
@Transactional
public class MenuManageServiceImpl implements IMenuManageService {
	@Autowired
	private TerminalDao terminalDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private MenuDao menuDao;

	@Override
	public Collection<TerminalMenu> getAllTerminalMenus() {
		return menuDao.getTerminalMenus();
	}

	@Override
	public List<TerminalMenu> getCities() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		return getCities(user);
	}

	@Override
	public List<TerminalMenu> getCities(Long refid) {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		return getCities(user, refid);
	}

	@Override
	public List<TerminalMenu> getCitiesByPid(Long pid) {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		// 获取所有的终端菜单
		List<TerminalMenu> menus = getTerminalMenusAll(user, MenuType.City);
		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (!StringUtils.equals(menu.getType(),
						MenuType.City.toString())
						|| pid.longValue() != menu.getPid().longValue()) {
					menuItr.remove();
				}
			}
		}
		return menus;
	}

	@Override
	public List<TerminalMenu> getTerminalByPid(Long pid, MenuType type) {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		// 获取所有的终端菜单
		List<TerminalMenu> menus = getTerminalMenusAll(user, type);
		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (!StringUtils.equals(menu.getType(),
						MenuType.Terminal.toString())
						|| pid.longValue() != menu.getPid().longValue()) {
					menuItr.remove();
				}
			}
		}
		return menus;
	}

	@Override
	public Menu getMenuById(Long menuId) {
		return menuDao.find(menuId);
	}

	@Override
	public List<Menu> getMenus(User user) {
		List<Menu> result = new ArrayList<Menu>();
		// 添加终端菜单
		List<TerminalMenu> terminalMenus = getTerminalMenus(user);
		if (null != terminalMenus)
			result.addAll(terminalMenus);
		return result;
	}

	@Override
	public List<TerminalMenu> getProvinces() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		return getProvinces(user);
	}

	@Override
	public List<String> getTerminalBoxIDs() {
		List<String> result = new ArrayList<>();
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		List<TerminalMenu> menuSet = getTerminals(user);
		if (null != menuSet) {
			result = new ArrayList<String>();
			Iterator<TerminalMenu> menuItr = menuSet.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (null != menu.getRefId()) {
					Terminal terminal = terminalDao.find(menu.getRefId());
					if (null != terminal)
						result.add(terminal.getBoxId());
				}
			}
		}
		return result;
	}

	@Override
	public List<Long> getTerminalIDs() {
		List<Long> result = new ArrayList<>();
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		List<TerminalMenu> menuSet = getTerminals(user);
		if (null != menuSet) {
			result = new ArrayList<Long>();
			Iterator<TerminalMenu> menuItr = menuSet.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (null != menu.getRefId())
					result.add(menu.getRefId());
			}
		}
		return result;
	}

	@Override
	public List<TerminalMenu> getTerminals() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userDao.findByUsername(username);
		return getTerminals(user);
	}

	@Override
	public List<TerminalMenu> getTerminalMenusAll(User user, MenuType type) {
		List<TerminalMenu> result = new ArrayList<TerminalMenu>();
		// 超级用户权限为所有区域权限
		if (user.isPowerUser()) {
			List<TerminalMenu> menus = menuDao.getTerminalMenus();
			if (null != menus) {
				Iterator<TerminalMenu> menuItr = menus.iterator();
				while (menuItr.hasNext()) {
					TerminalMenu menu = menuItr.next();
					result.add(menu);
				}
			}
			return result;
		}
		Set<UserGroup> groupSet = user.getGroups();
		if (null != groupSet) {
			Iterator<UserGroup> groupItr = groupSet.iterator();
			Set<TerminalMenu> resultSet = new HashSet<TerminalMenu>();
			while (groupItr.hasNext()) {
				UserGroup group = groupItr.next();
				Set<TerminalMenu> terminalmenus = group.getTerminalmenus();
				if (!terminalmenus.isEmpty()) {

					if (type == MenuType.Terminal) {
						for (TerminalMenu menu : terminalmenus) {
							if (StringUtils.equals(menu.getType(),
									MenuType.City.toString())) {
								resultSet.addAll(menuDao.getTerminalByCity(menu
										.getId()));
							}
						}
					} else {
						resultSet.addAll(terminalmenus);
					}
				}
			}
			if (null != resultSet)
				result.addAll(resultSet);
		}
		return result;
	}

	@Override
	public List<Long> getCityIds() {
		List<Long> ids = new ArrayList<Long>();
		List<TerminalMenu> terminalMenus = this.getCities();
		if (terminalMenus != null) {
			for (TerminalMenu terminalMenu : terminalMenus) {
				if (terminalMenu != null && terminalMenu.getRefId() != null) {
					ids.add(terminalMenu.getRefId());
				}
			}
		}
		return ids;
	}

	private List<TerminalMenu> getCities(User user) {
		// 获取所有的终端菜单
		List<TerminalMenu> menus = getTerminalMenus(user);
		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (!StringUtils.equals(menu.getType(),
						MenuType.City.toString())) {
					menuItr.remove();
				}
			}
		}
		return menus;
	}

	private List<TerminalMenu> getCities(User user, Long refid) {
		// 获取所有的终端菜单
		List<TerminalMenu> menus = getTerminalMenus(user);
		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (!StringUtils.equals(menu.getType(),
						MenuType.City.toString())) {
					menuItr.remove();
				} else {

					// 去除不等于省级refid的菜单
					TerminalMenu provinceMenu = (TerminalMenu) menuDao
							.find(menu.getPid());
					if (null == provinceMenu) {
						menuItr.remove();
					} else {
						if (provinceMenu.getRefId().longValue() != refid
								.longValue())
							menuItr.remove();
					}

				}
			}
		}
		return menus;
	}

	private List<TerminalMenu> getProvinces(User user) {
		// 获取所有的终端菜单
		List<TerminalMenu> menus = getTerminalMenus(user);
		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (!StringUtils.equals(menu.getType(),
						MenuType.Province.toString())) {
					menuItr.remove();
				}
			}
		}
		return menus;
	}

	/**
	 * 获取用户的区域权限
	 * 
	 * @param user
	 * @return
	 */
	private List<TerminalMenu> getTerminalMenus(User user) {
		List<TerminalMenu> result = new ArrayList<TerminalMenu>();
		if (user.isPowerUser()) {
			return getSuperUseTerminalMenus();
		}
		Set<UserGroup> groupSet = user.getGroups();
		if (null != groupSet) {
			Iterator<UserGroup> groupItr = groupSet.iterator();
			Set<TerminalMenu> resultSet = new HashSet<TerminalMenu>();
			while (groupItr.hasNext()) {
				UserGroup group = groupItr.next();
				if (!group.getTerminalmenus().isEmpty())
					resultSet.addAll(group.getTerminalmenus());
			}
			if (null != resultSet)
				result.addAll(resultSet);
		}
		return result;
	}

	private List<TerminalMenu> getSuperUseTerminalMenus() {
		List<TerminalMenu> result = new ArrayList<TerminalMenu>();
		List<TerminalMenu> menus = menuDao.getTerminalMenus();
		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (!StringUtils.equals(menu.getType(),
						MenuType.Terminal.toString())) {
					result.add(menu);
				}
			}
		}
		return result;
	}

	private List<TerminalMenu> getTerminals(User user) {
		// 获取所有的终端菜单
		List<TerminalMenu> menus = getTerminalMenus(user);
		List<TerminalMenu> result = new ArrayList<TerminalMenu>();
		Set<TerminalMenu> resultSet = new HashSet<TerminalMenu>();

		if (null != menus) {
			Iterator<TerminalMenu> menuItr = menus.iterator();
			while (menuItr.hasNext()) {
				TerminalMenu menu = menuItr.next();
				if (StringUtils
						.equals(menu.getType(), MenuType.City.toString())) {
					resultSet.addAll(menuDao.getTerminalByCity(menu.getId()));
				}
			}
			if (null != resultSet) {
				result.addAll(resultSet);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IMenuManageService#getTopTerminalMenus
	 * ()
	 */
	@Override
	public List<TerminalMenu> getTopTerminalMenus() {
		return menuDao.getTopTerminalMenus();
	}

	@Override
	public void updateMenu(Menu menu) {
		menuDao.update(menu);
	}
}
