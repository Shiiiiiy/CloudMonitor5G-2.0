/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.datang.bean.testManage.terminal.TerminalConstants;
import com.datang.bean.testManage.terminal.TerminalGroupInfo;
import com.datang.bean.testManage.terminal.TerminalGroupQuery;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.StringUtils;
import com.datang.dao.platform.security.UserGroupDao;
import com.datang.dao.testManage.terminal.HistorySoftUpgradeDao;
import com.datang.dao.testManage.terminal.TerminalDao;
import com.datang.dao.testManage.terminal.TerminalGroupDao;
import com.datang.dao.testManage.terminal.TerminalMenuDao;
import com.datang.domain.security.UserGroup;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.testManage.terminal.TerminalGroupService;

/**
 * @author dingzhongchang
 * @version 1.0.0
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TerminalGroupServiceImpl implements TerminalGroupService {

	/**
	 * 终端分组
	 */
	@Autowired
	private TerminalGroupDao groupDao = null;

	/**
	 * 终端
	 */
	@Autowired
	private TerminalDao terminalDao = null;

	/**
	 * 菜单
	 */
	@Autowired
	private TerminalMenuDao terminalMenuDao = null;

	/**
	 * 命令交互
	 */
	// private CommandInteractDao commandInteractDao;

	/**
	 * 历史软件更新信息
	 */
	@Autowired
	private HistorySoftUpgradeDao historySoftUpgradeDao;

	/**
	 * ATU日志
	 */
	// private ATULogDao atuLoginDao;

	/**
	 * GPS
	 */
	// private GPSDao gpsDao;

	/**
	 * 监控
	 */
	// private MonitorDao monitorDao;

	/**
	 * 用户组
	 */
	@Autowired
	private UserGroupDao userGroupDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getGroupsByMenus
	 * (java.util.List)
	 */
	@Override
	public List<TerminalGroup> getGroupsByMenus(List<TerminalMenu> menus) {
		List<TerminalGroup> groups = new ArrayList<TerminalGroup>();
		if (!CollectionUtils.isEmpty(menus)) {
			Collection<Long> menuRefIds = new ArrayList<Long>();
			for (TerminalMenu menu : menus) {
				if (!StringUtils.hasText(menu.getType())
						|| MenuType.Terminal.equals(menu.getType())) {
					continue;
				}
				menuRefIds.add(menu.getRefId());
			}
			if (!CollectionUtils.isEmpty(menuRefIds)) {
				groups.addAll(groupDao.queryTerminalGroups(menuRefIds));
			}
		}
		return groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getCityGroupByName
	 * (java.lang.String)
	 */
	@Override
	public Set<Terminal> getTerminalByCityName(String name) {
		// TODO Auto-generated method stub
		return groupDao.queryTerminals(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getAllProvinceGroup
	 * ()
	 */
	@Override
	public List<TerminalGroup> getAllProvinceGroup() {
		List<TerminalGroup> groups = new ArrayList<TerminalGroup>();
		Collection<Long> menuRefIds = terminalMenuDao
				.queryMenuRefId(MenuType.Province);
		if (!CollectionUtils.isEmpty(menuRefIds)) {
			groups.addAll(groupDao.queryTerminalGroups(menuRefIds));
		}
		return groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getAllCityGroup()
	 */
	@Override
	public List<TerminalGroup> getAllCityGroup() {
		List<TerminalGroup> groups = new ArrayList<TerminalGroup>();
		Collection<Long> menuRefIds = terminalMenuDao
				.queryMenuRefId(MenuType.City);
		if (!CollectionUtils.isEmpty(menuRefIds)) {
			groups.addAll(groupDao.queryTerminalGroups(menuRefIds));
		}
		return groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#addProvinceGroup
	 * (com.datang.adc.domain.terminal.TerminalGroup)
	 */
	@Override
	public TerminalMenu addProvinceGroup(TerminalGroup group) {
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// 去掉空格

		// 判定新增的省级设备组名是否存在
		if (checkMenuExising(group, null)) {
			throw new ApplicationException("分组名称已经存在");
		}
		groupDao.create(group);

		// 创建子菜单信息
		TerminalMenu menu = new TerminalMenu();
		menu.setName(group.getName());
		menu.setPid(null);
		menu.setRefId(group.getId());
		menu.setType(MenuType.Province.name());
		menu.setUrl(TerminalConstants.PROVINCE_INFO_URL + menu.getRefId());
		menu.setIcon(TerminalConstants.MENU_ICON);
		menu.setIconOpen(TerminalConstants.MENU_OPEN_ICON);
		terminalMenuDao.create(menu);
		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#addCityGroup(com
	 * .datang.adc.domain.terminal.TerminalGroup)
	 */
	@Override
	public TerminalMenu addCityGroup(TerminalGroup teGroup, Long refId) {
		Assert.notNull(teGroup, "arg group can't be Null");

		// 获取父menu和更新组信息,检验分组名称
		TerminalMenu pMenu = terminalMenuDao.find(refId);// 获取父菜单
		teGroup.setName(StringUtils.trimAllWhitespace(teGroup.getName()));// 去掉空格
		if (checkMenuExising(teGroup, pMenu)) {
			throw new ApplicationException("分组名称已经存在");
		}

		// 更新省级分组信息,及省级menu信息
		TerminalGroup group = findGroupById(pMenu.getRefId());
		group.addGroup(teGroup);
		updateGroup(group);
		TerminalGroup newGroup = group.getGroup(teGroup.getName());

		// 创建子菜单信息
		TerminalMenu menu = new TerminalMenu();
		menu.setName(teGroup.getName());
		menu.setPid(pMenu.getId());
		menu.setRefId(newGroup.getId());
		menu.setType(MenuType.City.name());
		menu.setIcon(TerminalConstants.MENU_LEAF_ICON);
		menu.setUrl(TerminalConstants.CITY_INFO_URL + menu.getRefId());
		terminalMenuDao.create(menu);
		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getAllGroupInfo()
	 */
	@Override
	public List<TerminalGroupInfo> getAllGroupInfo() {
		List<TerminalGroupInfo> groupInfos = new ArrayList<TerminalGroupInfo>();
		Collection<TerminalGroup> groups = groupDao.findAll();
		if (!CollectionUtils.isEmpty(groups)) {
			for (TerminalGroup group : groups) {
				TerminalGroupInfo groupInfo = constructGroupInfo(group);
				groupInfo.setGroup(group);
				groupInfos.add(groupInfo);
			}
		}
		return groupInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getGroupInfo(java
	 * .lang.Long)
	 */
	@Override
	public TerminalGroupInfo getGroupInfo(Long id) {
		Assert.notNull(id, "arg id can't be Null");
		TerminalGroup group = groupDao.find(id);
		TerminalGroupInfo groupInfo = constructGroupInfo(group);
		groupInfo.setGroup(group);
		return groupInfo;
	}

	/**
	 * 判断此分组是否存在
	 * 
	 * @param tgName
	 *            分组名称
	 * @return 分组已存在 true,否则false
	 */
	public boolean isExist(String tgName) {
		TerminalGroup group = groupDao.queryTerminalGroup(tgName);
		return group != null;
	}

	/**
	 * 判断此分组是否存在
	 * 
	 * @param groupId
	 *            分组
	 * @return 分组已存在 true,否则false
	 */
	public boolean isExist(Long groupId) {
		TerminalGroup group = groupDao.find(groupId);
		return group != null;
	}

	/**
	 * 
	 * @param group
	 * @see com.datang.adc.service.terminal.TerminalGroupService#addGroup(com.datang.adc.domain.terminal.TerminalGroup)
	 */
	@Override
	public void addGroup(TerminalGroup group) {
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// 去掉空格
		if (isExist(group.getName())) {
			// yzp
			// throw new ApplicationException("TerminalGroup.error.NameExist",
			// TerminalConstants.TERMINAL_PROPERTIES_BUNDLE);
		}
		groupDao.create(group);
	}

	/**
	 * @param groupId
	 *            Long
	 * @see com.datang.adc.service.terminal.TerminalGroupService#deleteGroup(java.lang.Long)
	 */
	@Override
	public TerminalMenu deleteGroup(Long groupId) {
		Assert.notNull(groupId, "arg groupId can't be Null");
		if (!isExist(groupId)) {
			throw new ApplicationException("该分组已经不存在");
		}
		TerminalGroup group = groupDao.find(groupId);
		TerminalMenu menu = terminalMenuDao.queryMenuByRef(groupId);
		List<Long> terminalIds = new ArrayList<Long>();
		Set<TerminalGroup> groups = group.getGroups();
		if (MenuType.Province.getMenuType().equals(menu.getType())
				&& !CollectionUtils.isEmpty(groups)) {
			for (TerminalGroup teGroup : groups) {
				pickUpTerminalIds(teGroup, terminalIds);
			}
		}
		if (MenuType.City.getMenuType().equals(menu.getType())) {
			pickUpTerminalIds(group, terminalIds);
		}
		// 如果包含在线终端，则抛出异常
		if (containsOnline(terminalIds)) {
			throw new ApplicationException("分组中包含在线终端，不建议删除在线终端");
		}
		// 删除历史数据
		if (!CollectionUtils.isEmpty(terminalIds)) {
			deleteHistories(terminalIds.toArray(new Long[terminalIds.size()]));
		}
		if (MenuType.Province.getMenuType().equals(menu.getType())) {
			Collection<TerminalMenu> cityMenus = terminalMenuDao.queryMenu(
					MenuType.City, menu.getId());
			updateCityMenus(cityMenus);
		}
		updateGroupMenu(menu);
		try {
			groupDao.delete(group);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException("分组中包含工参,不建议删除");
		}
		return terminalMenuDao.deleteByRef(groupId);
	}

	/**
	 * @param terminalIds
	 * @return
	 */
	private boolean containsOnline(List<Long> terminalIds) {
		boolean isOnline = false;
		if (!CollectionUtils.isEmpty(terminalIds)) {
			for (Long id : terminalIds) {
				Terminal ter = terminalDao.find(id);
				if (ter.isOnline()) {
					isOnline = true;
					break;
				}
			}
		}
		return isOnline;
	}

	/**
	 * @param cityMenus
	 *            Collection<TerminalMenu>
	 */
	private void updateCityMenus(Collection<TerminalMenu> cityMenus) {
		if (!CollectionUtils.isEmpty(cityMenus)) {
			for (TerminalMenu cityMenu : cityMenus) {
				updateGroupMenu(cityMenu);
			}
		}
	}

	/**
	 * @param cityMenu
	 *            TerminalMenu
	 */
	private void updateGroupMenu(TerminalMenu cityMenu) {
		Set<UserGroup> menuGroup = cityMenu.getGroups();
		if (!CollectionUtils.isEmpty(menuGroup)) {
			for (UserGroup user : menuGroup) {
				Set<Menu> menus = user.getMenus();
				if (!CollectionUtils.isEmpty(menus)) {
					menus.remove(cityMenu);
				}
				userGroupDao.update(user);
			}
		}
	}

	/**
	 * 提取IDs
	 * 
	 * @param group
	 *            组
	 * @param terminalIds
	 *            终端IDS
	 */
	private void pickUpTerminalIds(TerminalGroup group, List<Long> terminalIds) {
		Set<Terminal> terminals = group.getTerminals();
		if (!CollectionUtils.isEmpty(terminals)) {
			for (Terminal terminal : terminals) {
				terminalIds.add(terminal.getId());
			}
		}
	}

	/**
	 * 保持终端数据结构的一致性，删除历史数据
	 * 
	 * @param id
	 *            终端ID
	 */
	private void deleteHistories(Long... ids) {

		// yzp
		// historySoftUpgradeDao.deleteByTerminalIds(ids);// 删除下发的历史更新软件
		// atuLoginDao.deleteByTerminalIds(ids);
		// commandInteractDao.deleteCommandInteractByIDs(ids);
		terminalMenuDao.deleteByRefs(ids);
		// yzp
		// monitorDao.deleteByTerminalIds(ids);

		// gpsDao.deleteByTerminalIds(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#findGroupMenuById
	 * (java.lang.Long)
	 */

	@Override
	public TerminalGroup findGroupById(Long groupId) {
		Assert.notNull(groupId, "arg groupId can't be Null");
		if (!isExist(groupId)) {
			throw new ApplicationException("该分组已经不存在");
		}
		return groupDao.find(groupId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#findGroupMenuById
	 * (java.lang.Long)
	 */
	@Override
	public TerminalMenu findGroupMenuById(Long groupid) {
		Assert.notNull(groupid, "arg groupId can't be Null");
		if (!isExist(groupid)) {
			// yzp
			// throw new
			// ApplicationException("TerminalGroup.error.NotFoundGroup",
			// TerminalConstants.TERMINAL_PROPERTIES_BUNDLE);
		}
		return terminalMenuDao.queryMenuByRef(groupid);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// com.datang.adc.service.terminal.TerminalGroupService#findGroupByName(java.lang.String)
	// */
	// @Override
	// public TerminalGroup findGroupByName(String name) {
	// Assert.notNull(name, "arg name can't be Null");
	// if (!isExist(name)) {
	// throw new ApplicationException("TerminalGroup.error.NotFoundGroup",
	// TerminalConstants.TERMINAL_PROPERTIES_BUNDLE);
	// }
	// return groupDao.queryTerminalGroup(name);
	// }

	@Override
	public Collection<TerminalGroup> getGroupByIds(Long... groupIds) {
		Assert.notNull(groupIds, "arg groupIds must be not null");
		List<TerminalGroup> groups = new ArrayList<TerminalGroup>(0);
		for (Long id : groupIds) {
			groups.add(findGroupById(id));
		}
		return groups;
	}

	/**
	 * @param group
	 * @see com.datang.adc.service.terminal.TerminalGroupService#updateGroup(com.datang.adc.domain.terminal.TerminalGroup)
	 */
	@Override
	public void updateGroup(TerminalGroup group) {
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// 去掉空格
		if (!isExist(group.getId())) {
			throw new ApplicationException("该分组已经不存在");
		}
		groupDao.update(group);
		terminalMenuDao.updateByRef(group.getId(), group.getName());
	}

	/**
	 * @param group
	 * @see com.datang.adc.service.terminal.TerminalGroupService#updateGroup(com.datang.adc.domain.terminal.TerminalGroup)
	 */
	@Override
	public TerminalMenu updateGroupAndMenu(TerminalGroup group) {
		Assert.notNull(group, "arg group can't be Null");
		if (!isExist(group.getId())) {
			throw new ApplicationException("该分组已经不存在");
		}
		// 获取menu菜单
		TerminalMenu subMenu = terminalMenuDao.queryMenuByRef(group.getId());
		TerminalMenu pMenu = null;
		if (null != subMenu.getPid()) {
			pMenu = terminalMenuDao.find(subMenu.getPid());
		}
		if (checkMenuExising(group, pMenu)) {
			throw new ApplicationException("分组名称已经存在");
		}

		groupDao.update(group);
		return terminalMenuDao.updateByRef(group.getId(), group.getName());
	}

	/**
	 * @param group
	 *            TerminalGroup
	 */
	private boolean checkMenuExising(TerminalGroup teGroup, TerminalMenu pMenu) {
		if (pMenu == null) {// 父终端菜单为空，说明为省级终端菜单
			if (!CollectionUtils.isEmpty(terminalMenuDao.queryMenu(
					MenuType.Province, teGroup.getName()))) {
				return true;
			}
			// 如果父终端菜单不为空，说明为市级终端菜单:所有的市级菜单都不重复
		} else if (!CollectionUtils.isEmpty(terminalMenuDao.queryMenu(
				MenuType.City, teGroup.getName()))) {
			return true;
		}
		return false;
	}

	/**
	 * 更新终端分组信息 删除不必要的分组，添加需要的分组，同时更新菜单
	 */
	@Override
	public void updateGroupMember(Long terminalGroupId, Long... teIds) {
		throw new UnsupportedOperationException("updateGroupMember");
		// Collection<Terminal> selectedTerminals = null;
		// if (teIds != null && teIds.length != 0) {
		// selectedTerminals = terminalDao.query(Arrays.asList(teIds));
		// }
		// TerminalGroup defaultGroup =
		// findGroupByName(TerminalConstants.DEFAULT_GROUP_NAME);
		// TerminalGroup terminalGroup = findGroupById(terminalGroupId);
		// Set<Terminal> oldTerminalsInThisGroup = terminalGroup.getTerminals();
		// if (!CollectionUtils.isEmpty(oldTerminalsInThisGroup)) {
		// for (Terminal terminal : oldTerminalsInThisGroup) {
		// defaultGroup.addTerminal(terminal);
		// terminalDao.update(terminal);
		// terminalMenuDao.updateByRef(terminal.getId(),
		// defaultGroup.getId());// 更新PID放入默认组
		// }
		// // groupDao.update(defaultGroup);
		// }
		// // oldTerminalsInThisGroup.clear();
		// if (!CollectionUtils.isEmpty(selectedTerminals)) {
		// for (Terminal terminal : selectedTerminals) {
		// terminalGroup.addTerminal(terminal);
		// terminalDao.update(terminal);
		// terminalMenuDao.updateByRef(terminal.getId(),
		// terminalGroup.getId());// 更新PID
		// }
		// }
		// // groupDao.update(terminalGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.terminal.TerminalGroupService#getAllGroup()
	 */
	@Override
	public List<TerminalGroup> getAllGroup() {
		return (List<TerminalGroup>) groupDao.queryAllOrderGroup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalGroupService#getAllTerminalByGroupId
	 * (java.lang.Long)
	 */
	@Override
	public List<Terminal> getAllTerminalByGroupId(Long groupId) {
		List<Terminal> coll = new ArrayList<Terminal>();
		List<TerminalGroup> queryTerminalGroups = groupDao.queryTerminalGroups(groupId);
		if(queryTerminalGroups != null && queryTerminalGroups.size() > 0){		
			for (TerminalGroup group : queryTerminalGroups) {
				if (group != null && group.getId().equals(groupId)) {
					if (group.getTerminals() != null) {
						for (Terminal terminal : group.getTerminals()) {
							if (terminal != null
									&& terminal.getTestModuls() != null) {
								terminal.getTestModuls().size();
							}
						}
					}
					coll.addAll(group.getTerminals());
				}
			}
		}
		// yzp
		// Queue<String> boxIds = OnlineStatusContext.getInstance().getAll();
		// updatePageTesStatus(coll, boxIds);
		return coll;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// com.datang.adc.service.terminal.TerminalGroupService#getAllTerminalByGroupName(java.lang.String)
	// */
	// @Override
	// public List<Terminal> getAllTerminalByGroupName(String groupName) {
	// List<Terminal> coll = new ArrayList<Terminal>();
	// if (!StringUtils.isEmpty(groupName)) {
	// Collection<TerminalGroup> colGroup =
	// groupDao.queryAllOrderGroup(groupName);
	// if (!CollectionUtils.isEmpty(colGroup)) {
	// for (TerminalGroup group : colGroup) {
	// if (group.getName().equals(groupName)) {
	// coll.addAll(group.getTerminals());
	// }
	// }
	// Queue<String> boxIds = OnlineStatusContext.getInstance().getAll();
	// updatePageTesStatus(coll, boxIds);
	// }
	// }
	// return coll;
	// }

	private Collection<Terminal> updatePageTesStatus(
			Collection<Terminal> teList, Queue<String> boxIds) {
		if (!CollectionUtils.isEmpty(teList)) {
			for (Terminal terminal : teList) {
				terminal.setOnline(boxIds.contains(terminal.getBoxId()));
			}
		}
		return teList;
	}

	//
	// @Override
	// public Map<String, List<Terminal>> getGroupNameToTerminalMap() {
	// // 分组名称到终端的映射
	// Map<String, List<Terminal>> groupNameToTerminalMap = new HashMap<String,
	// List<Terminal>>();
	// // 查询出所有的分组
	// List<TerminalGroup> allTerminalGroups = getAllGroup();
	// // 找出每个分组下所包含的终端,放入Map中
	// for (TerminalGroup terminalGroup : allTerminalGroups) {
	// String groupName = terminalGroup.getName();
	// List<Terminal> terminals = getAllTerminalByGroupName(groupName);
	// groupNameToTerminalMap.put(groupName, terminals);
	// }
	// return groupNameToTerminalMap;
	// }
	/**
	 * 根据市级分组 获取 省级分组名称
	 */
	@Override
	public String getProvinceNameByCityGroup(TerminalGroup cityGroup) {
		TerminalMenu cMenu = terminalMenuDao.queryMenuByRef(cityGroup.getId());// 获取父菜单
		TerminalMenu pMenu = terminalMenuDao.find(cMenu.getPid());
		if (pMenu != null
				&& pMenu.getType().equals(MenuType.Province.getMenuType())) {
			return pMenu.getName();
		}
		return "";
	}

	@Override
	public Map<Long, List<Terminal>> getGroupIdToTerminalMap() {
		// 分组名称到终端的映射
		Map<Long, List<Terminal>> groupIdToTerminalMap = new HashMap<Long, List<Terminal>>();
		// 查询出所有的分组
		List<TerminalGroup> allTerminalGroups = getAllGroup();
		// 找出每个分组下所包含的终端,放入Map中
		for (TerminalGroup terminalGroup : allTerminalGroups) {
			Long groupId = terminalGroup.getId();
			List<Terminal> terminals = getAllTerminalByGroupId(groupId);
			groupIdToTerminalMap.put(groupId, terminals);
		}
		return groupIdToTerminalMap;
	}

	@Override
	public List<Terminal> getTerminalsInOtherGroups(Long thisGroupId) {
		// 不在该分组下的所有终端
		List<Terminal> terminalsInOtherGroups = new ArrayList<Terminal>();

		// 各个分组下包含的终端Map
		Map<Long, List<Terminal>> groupIdToTerminalMap = getGroupIdToTerminalMap();
		for (Long groupId : groupIdToTerminalMap.keySet()) {
			if (!groupId.equals(thisGroupId))
				terminalsInOtherGroups
						.addAll(groupIdToTerminalMap.get(groupId));
		}
		return terminalsInOtherGroups;
	}

	@Override
	public List<TerminalMenu> deleteCheckedGroups(Long[] checkedGroupIds) {
		List<TerminalMenu> menuList = new ArrayList<TerminalMenu>();
		if (null != checkedGroupIds) {
			for (Long id : checkedGroupIds) {
				menuList.add(deleteGroup(id));
			}
		}

		return menuList;
	}

	// @Override
	// public Set<TerminalGroup> getGroupByNames(String... groupNames) {
	// Assert.notNull(groupNames, "arg groupNames can't be Null");
	// // if (!isExist(TerminalConstants.DEFAULT_GROUP_NAME)) {
	// // TerminalGroup group = new TerminalGroup();
	// // group.setName(TerminalConstants.DEFAULT_GROUP_NAME);
	// // group.setTerminals(new HashSet<Terminal>());
	// // addCityGroup(group, 25L);
	// // }
	// Set<TerminalGroup> result = new HashSet<TerminalGroup>();
	// for (String name : groupNames) {
	// TerminalGroup group = groupDao.queryTerminalGroup(name);
	// if (group != null) {
	// result.add(group);
	// }
	// }
	// return result;
	// }

	@Override
	public List<TerminalGroupInfo> pageQueryGroups(
			TerminalGroupQuery terminalGroupQuery) {
		List<TerminalGroupInfo> groupInfos = new ArrayList<TerminalGroupInfo>();
		Collection<TerminalGroup> groups = groupDao.query(terminalGroupQuery);
		if (!CollectionUtils.isEmpty(groups)) {
			for (TerminalGroup group : groups) {
				TerminalGroupInfo groupInfo = constructGroupInfo(group);
				groupInfos.add(groupInfo);
			}
		}
		return groupInfos;

	}

	private TerminalGroupInfo constructGroupInfo(TerminalGroup group) {
		TerminalGroupInfo groupInfo = new TerminalGroupInfo();
		groupInfo.setGroup(group);
		groupInfo.setGroupName(group.getName());

		Integer numGroups = 0;
		if (!CollectionUtils.isEmpty(group.getGroups())) {
			numGroups = group.getGroups().size();
		}
		groupInfo.setNumGroups(numGroups);

		Integer numTerminals = 0;
		if (!CollectionUtils.isEmpty(group.getGroups())) {
			for (TerminalGroup gp : group.getGroups()) {
				if (!CollectionUtils.isEmpty(gp.getTerminals())) {
					numTerminals = numTerminals + gp.getTerminals().size();
				}
			}
		} else {
			if (!CollectionUtils.isEmpty(group.getTerminals())) {
				numTerminals = numTerminals + group.getTerminals().size();
			}
		}
		groupInfo.setNumTerminals(numTerminals);

		List<TerminalGroupInfo> subGroupInfos = null;
		if (subGroupInfos == null) {
			subGroupInfos = new ArrayList<TerminalGroupInfo>();
		} else {
			subGroupInfos.clear();
		}
		if (!CollectionUtils.isEmpty(group.getGroups())) {
			for (TerminalGroup gp : group.getGroups()) {
				TerminalGroupInfo subGroupInfo = new TerminalGroupInfo();
				subGroupInfo.setGroup(gp);
				subGroupInfos.add(subGroupInfo);
			}
		}
		groupInfo.setSubGroupInfos(subGroupInfos);

		return groupInfo;
	}

	// yzp
	/*
	 * class TerminalGroupComparator implements Comparator<TerminalGroup> {
	 * private TerminalGroupOrderField field; private boolean asc;
	 * 
	 * public TerminalGroupComparator(TerminalGroupOrderField field, boolean
	 * asc) { this.field = field; this.asc = asc; }
	 * 
	 * @Override public int compare(TerminalGroup o1, TerminalGroup o2) { if
	 * (field.equals(TerminalGroupOrderField.ID)) { if (asc) return
	 * o1.getId().compareTo(o2.getId()); else return
	 * o2.getId().compareTo(o1.getId()); } else if
	 * (field.equals(TerminalGroupOrderField.NAME)) { if (asc) return
	 * o1.getName().compareTo(o2.getName()); else return
	 * o2.getName().compareTo(o1.getName()); } return 0; }
	 * 
	 * public void setField(TerminalGroupOrderField field) { this.field = field;
	 * }
	 * 
	 * public TerminalGroupOrderField getField() { return field; }
	 * 
	 * public boolean isAsc() { return asc; }
	 * 
	 * public void setAsc(boolean asc) { this.asc = asc; }
	 * 
	 * }
	 */
}
