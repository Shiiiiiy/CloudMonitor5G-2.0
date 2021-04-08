/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.datang.bean.testManage.terminal.TerminalMenuQuery;
import com.datang.dao.testManage.terminal.TerminalDao;
import com.datang.dao.testManage.terminal.TerminalMenuDao;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.service.testManage.terminal.TerminalMenuService;

/**
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-9
 */
@Service
@Transactional
public class TerminalMenuServiceImpl implements TerminalMenuService {

	/**
	 * 终端菜单
	 */
	@Autowired
	private TerminalMenuDao terminalMenuDao;

	/**
	 * 终端
	 */
	@Autowired
	private TerminalDao terminalDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#create(com.datang
	 * .adc.domain.menu.TerminalMenu)
	 */
	@Override
	public void create(TerminalMenu menu) {
		// User user = CurrentUserContext.getUser();
		// Set<UserGroup> userGroup = user.getGroups();
		// for (UserGroup group : userGroup) {
		// group
		// }
		Assert.notNull(menu, "arg menu can't be Null");
		terminalMenuDao.create(menu);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#del(java.lang.Long)
	 */
	@Override
	public void del(Long menuId) {
		Assert.notNull(menuId, "arg menuId can't be Null");
		terminalMenuDao.delete(menuId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#get(java.lang.String)
	 */
	@Override
	public TerminalMenu get(String name) {
		Assert.notNull(name, "arg name can't be Null");
		return terminalMenuDao.queryMenu(name);

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#get(java.lang.String)
	 */
	@Override
	public TerminalMenu get2(String name) {
		Assert.notNull(name, "arg name can't be Null");
		TerminalMenu queryMenu = terminalMenuDao.queryMenu(name);
		StationParamPojo stationParamPojo = queryMenu.getStationParamPojo();
		stationParamPojo.getSingleStationMOdelSelect();
		stationParamPojo.getContrastOpen3DMOdelSelect();
		return queryMenu;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#getByRefId(java.lang
	 * .String)
	 */
	@Override
	public TerminalMenu getByRefId(Long refId) {
		Assert.notNull(refId, "arg refId can't be Null");
		return terminalMenuDao.queryMenuByRef(refId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#get(java.lang.Long)
	 */
	@Override
	public TerminalMenu get(Long id) {
		return terminalMenuDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#queryTerminalIds(
	 * com.datang.adc.beans.terminal.TerminalMenuQuery)
	 */
	@Override
	public List<Long> queryTerminalIds(TerminalMenuQuery terminalMenuQuery) {
		Collection<TerminalMenu> terminalMenus = this.terminalMenuDao
				.query(terminalMenuQuery);// 查询菜单内容
		if (terminalMenus == null || terminalMenus.isEmpty()) {
			return null;
		} else {
			int target = terminalMenuQuery.getTestTarget();
			List<Long> terminalIds = new ArrayList<Long>();
			for (TerminalMenu terminalMenu : terminalMenus) {
				if (terminalMenu == null) {
					continue;
				} else {
					Terminal te = terminalDao.find(terminalMenu.getRefId());// 通过终端名称得到自动路测终端所包含的模块
					if (te == null) {
						continue;
					}
					String boxId = te.getBoxId();
					if ((target == 0 && boxId.startsWith("0"))
							|| (target == 1 && boxId.startsWith("2"))
							|| (target == 2 && boxId.startsWith("3"))
							|| (target == 3)
							|| (target == 4 && (boxId.startsWith("3") || boxId
									.startsWith("2")))) {
						terminalIds.add(terminalMenu.getRefId());// 菜单对应实体的数据库ID,用于查询对应实体信息
					}
				}
			}
			return terminalIds;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#queryTerminalIds(
	 * com.datang.adc.beans.terminal.TerminalMenuQuery)
	 */
	@Override
	public List<Long> querySwTerminalIds(TerminalMenuQuery terminalMenuQuery) {
		Collection<TerminalMenu> terminalMenus = this.terminalMenuDao
				.query(terminalMenuQuery);
		if (terminalMenus == null || terminalMenus.isEmpty()) {
			return null;
		} else {
			int target = terminalMenuQuery.getTestTarget();
			List<Long> terminalIds = new ArrayList<Long>();
			for (TerminalMenu terminalMenu : terminalMenus) {
				if (terminalMenu == null) {
					continue;
				} else {
					Terminal te = terminalDao.find(terminalMenu.getRefId());
					if (te == null) {
						continue;
					}
					String boxId = te.getBoxId();
					if ((target == 0 && boxId.startsWith("0"))
							|| (target == 1 && boxId.startsWith("2"))
							|| (target == 2 && boxId.startsWith("3"))) {
						terminalIds.add(terminalMenu.getRefId());
					}
				}
			}
			return terminalIds;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#get(com.datang.adc
	 * .domain.security.menu.MenuType)
	 */
	@Override
	public Collection<TerminalMenu> get(MenuType type) {
		return terminalMenuDao.queryMenu(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalMenuService#update(com.datang
	 * .adc.domain.menu.TerminalMenu)
	 */
	@Override
	public void update(TerminalMenu menu) {
		Assert.notNull(menu, "arg menu can't be Null");
		terminalMenuDao.update(menu);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.terminal.TerminalMenuService#getAllMenuInfo()
	 */
	@Override
	public List<TerminalMenu> getAllMenuInfo() {
		return terminalMenuDao.queryAll();
	}

}
