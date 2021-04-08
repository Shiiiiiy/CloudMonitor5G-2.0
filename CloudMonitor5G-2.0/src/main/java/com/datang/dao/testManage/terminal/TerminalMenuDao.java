/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.dao.testManage.terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.datang.bean.testManage.terminal.TerminalMenuQuery;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.CollectionUtils;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;

/**
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-9
 */
@Repository
public class TerminalMenuDao extends GenericHibernateDao<TerminalMenu, Long> {

	/**
	 * 通过REFID删除菜单
	 * 
	 * @param refId
	 *            REFID
	 */
	public TerminalMenu deleteByRef(Long refId) {
		TerminalMenu menu = queryMenuByRef(refId);
		if (MenuType.Province.getMenuType().equals(menu.getType())) {
			deleteMenu(MenuType.City, menu.getId());
		}
		if (menu != null) {
			delete(menu);
		}
		return menu;
	}

	/**
	 * 通过REFID更新菜单
	 * 
	 * @param refId
	 *            REFID
	 * @param name
	 *            菜单对应的名字
	 */
	public TerminalMenu updateByRef(Long refId, Long refPid) {
		TerminalMenu menu = queryMenuByRef(refId);
		TerminalMenu pMenu = queryMenuByRef(refPid);
		if (menu != null && pMenu != null
				&& !menu.getPid().equals(pMenu.getId())) {
			menu.setPid(pMenu.getId());
			update(menu);
		}
		return menu;
	}

	/**
	 * 通过REFID更新菜单
	 * 
	 * @param refId
	 *            REFID
	 * @param name
	 *            菜单对应的名字
	 */
	public TerminalMenu updateByRef(Long refId, Long refPid, String name) {
		TerminalMenu menu = queryMenuByRef(refId);
		TerminalMenu pMenu = queryMenuByRef(refPid);
		if (menu != null) {
			if (pMenu != null && !menu.getPid().equals(pMenu.getId())) {
				menu.setPid(pMenu.getId());
			}
			if (!menu.getName().equals(name)) {
				menu.setName(name);
			}
			update(menu);
		}

		return menu;
	}

	/**
	 * 通过REFID更新菜单
	 * 
	 * @param refId
	 *            REFID
	 * @param name
	 *            菜单对应的名字
	 */
	public TerminalMenu updateByRef(Long refId, String name) {
		TerminalMenu menu = queryMenuByRef(refId);
		if (menu != null && !menu.getName().equals(name)) {
			menu.setName(name);
			update(menu);
		}
		return menu;
	}

	/**
	 * 通过名称查询菜单信息
	 * 
	 * @param name
	 *            菜单名称
	 * @return 菜单信息
	 */
	public TerminalMenu queryMenu(String name) throws HibernateException{
		String hql = "from TerminalMenu tm  where tm.name =:name";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("name", name);
		return (TerminalMenu) query.uniqueResult();
	}

	/**
	 * 通过菜单类型获取查询菜单信息
	 * 
	 * @param type
	 *            菜单类型
	 * @return 菜单信息
	 */
	public Collection<TerminalMenu> queryMenu(MenuType type) {
		String hql = "from TerminalMenu tm  where tm.type =:type";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("type", type.getMenuType());
		return query.list();
	}

	/**
	 * 通过菜单类型获取删除菜单信息
	 * 
	 * @param type
	 *            菜单类型
	 * @return 菜单信息
	 */
	public void deleteMenu(MenuType type, Long pid) {
		String hql = "delete TerminalMenu tm  where tm.type =:type and tm.pid=:pid";
		Query deleteQuery = this.getHibernateSession().createQuery(hql);
		deleteQuery.setParameter("type", type.getMenuType());
		deleteQuery.setParameter("pid", pid);
		deleteQuery.executeUpdate();
	}

	/**
	 * 通过菜单类型获取查询菜单信息
	 * 
	 * @param type
	 *            菜单类型
	 * @return 菜单信息
	 */
	public Collection<TerminalMenu> queryMenu(MenuType type, Long pid) {
		String hql = "from TerminalMenu tm  where tm.type =:type  and tm.pid=:pid";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("type", type.getMenuType());
		query.setParameter("pid", pid);
		return query.list();
	}

	/**
	 * 通过菜单类型获取查询菜单信息
	 * 
	 * @param type
	 *            菜单类型
	 * @return 菜单信息
	 */
	public Collection<TerminalMenu> queryMenu(MenuType type, Long pid,
			String name) {
		String hql = "from TerminalMenu tm  where tm.type =:type and tm.name =:name and tm.pid=:pid";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("type", type.getMenuType());
		query.setParameter("name", name);
		query.setParameter("pid", pid);
		return query.list();
	}

	/**
	 * 通过菜单类型获取查询菜单信息
	 * 
	 * @param type
	 *            菜单类型
	 * @return 菜单信息
	 */
	public Collection<TerminalMenu> queryMenu(MenuType type, String name) {
		String hql = "from TerminalMenu tm  where tm.type =:type and tm.name =:name";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("type", type.getMenuType());
		query.setParameter("name", name);
		return query.list();
	}

	/**
	 * 通过菜单类型获取查询菜单信息
	 * 
	 * @param type
	 *            菜单类型
	 * @return 菜单信息
	 */
	public Collection<Long> queryMenuRefId(MenuType type) {
		List<Long> refIds = new ArrayList<Long>();
		String hql = "from TerminalMenu tm  where tm.type =:type";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("type", type.name());
		Collection<TerminalMenu> menus = query.list();
		if (!CollectionUtils.isEmpty(menus)) {
			for (TerminalMenu menu : menus) {
				refIds.add(menu.getRefId());
			}
		}
		return refIds;
	}

	/**
	 * 通过菜单refId获取查询菜单信息
	 * 
	 * @param refId
	 *            菜单refId
	 * @return 菜单信息
	 */
	public TerminalMenu queryMenuByRef(Long refId) {
		String hql = "from TerminalMenu tm  where tm.refId =:refId";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("refId", refId);
		return (TerminalMenu) query.uniqueResult();
	}

	/**
	 * 通过菜单refId获取查询菜单信息
	 * 
	 * @param refId
	 *            菜单refId
	 * @return 菜单信息
	 */
	public TerminalMenu queryMenuByPid(Long pId) {
		String hql = "from TerminalMenu tm  where tm.pid =:pId";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("pId", pId);
		return (TerminalMenu) query.uniqueResult();
	}

	/**
	 * 通过查询条件查询菜单信息
	 * 
	 * @param terminalMenuQuery
	 *            查询条件
	 * @return 查询出的结果
	 */
	public Collection<TerminalMenu> query(TerminalMenuQuery terminalMenuQuery) {
		Long provinceMenuId = terminalMenuQuery.getProvinceId();// 得到查询条件的省级ID
		Long cityMneuId = terminalMenuQuery.getCityId();// 菜单相关的市及的id
		Collection<TerminalMenu> tms = null;
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalMenu.class);// 去查整个类//hibernate的的一种用法

		if (provinceMenuId == null && cityMneuId == null) {
			// do nothing
		} else if (provinceMenuId == null && cityMneuId != null) {
			criteria.add(Expression.eq("pid", cityMneuId));

		} else if (provinceMenuId != null && cityMneuId == null) {
			List<Long> cityIds = this
					.getHibernateSession()
					.createQuery(
							"select id from TerminalMenu where pid="
									+ provinceMenuId).list();
			if (cityIds == null || cityIds.isEmpty()) {
				return tms;
			}
			criteria.add(Expression.in("pid", cityIds));

		} else if (provinceMenuId != null && cityMneuId != null) {
			List<Long> cityIds = this
					.getHibernateSession()
					.createQuery(
							"select id from TerminalMenu where pid="
									+ provinceMenuId).list();
			if (cityIds == null || cityIds.isEmpty()) {
				return tms;
			}
			criteria.add(Expression.in("pid", cityIds));
			criteria.add(Expression.eq("pid", cityMneuId));
		}

		// if(terminalMenuQuery != null && terminalMenuQuery.getTestTarget() !=
		// null && terminalMenuQuery.getTestTarget() >= 0) {
		// List<Long> testTargetIds =
		// this.getHibernateSession().createQuery("select id from Terminal where testTarget="
		// + terminalMenuQuery.getTestTarget()).list();
		// if (testTargetIds == null || testTargetIds.isEmpty()) {
		// return tms;
		// }
		// if(terminalMenuQuery.getTerminalIds() != null) {
		// for(Long id : testTargetIds) {
		// if(!terminalMenuQuery.getTerminalIds().contains(id)) {
		// terminalMenuQuery.getTerminalIds().remove(id);
		// }
		// }
		// }
		// }

		if (null != terminalMenuQuery.getName()) {
			criteria.add(Expression.like("name", terminalMenuQuery.getName(),
					MatchMode.ANYWHERE));
		}
		if (terminalMenuQuery.getTerminalIds() != null
				&& !terminalMenuQuery.getTerminalIds().isEmpty()) {
			criteria.add(Expression.in("refId",
					terminalMenuQuery.getTerminalIds()));
		}

		criteria.add(Expression.eq("type", MenuType.Terminal.name()));

		// criteria.setFirstResult(terminalMenuQuery.getPagerOffset());
		// criteria.setMaxResults(terminalMenuQuery.getPagerPerPage());
		criteria.addOrder(Order.desc("refId"));
		tms = criteria.list();
		// terminalMenuQuery.setQueryResults(tms);

		// if (terminalMenuQuery.getPagerCount() == -1) {
		// int count = (Integer) criteria
		// .setProjection(Projections.rowCount()).uniqueResult();
		// terminalMenuQuery.setPagerCount(count);
		// }
		return tms;
	}

	/**
	 * 获取所有菜单信息
	 */
	public List<TerminalMenu> queryAll() {
		List<TerminalMenu> menus = new ArrayList<TerminalMenu>();
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalMenu.class);
		criteria.addOrder(Order.asc("name"));
		Collection<TerminalMenu> terminalMenus = criteria.list();
		if (!CollectionUtils.isEmpty(terminalMenus)) {
			menus.addAll(terminalMenus);
		}
		return menus;
	}

	/**
	 * @param ids
	 */
	public void deleteByRefs(Long[] ids) {
		String hql = "delete TerminalMenu";
		List<List<Long>> list = new ArrayList<List<Long>>();
		if (ids.length >= 1000) {
			list = segmentationList(ids, 999);
			for (int i = 0; i < list.size(); i++) {
				if (-1 != hql.indexOf("WHERE")) {
					hql += " AND refId in (:ids" + i + ") ";
				} else {
					hql += " WHERE refId in (:ids" + i + ")  ";
				}
			}
		} else {
			list.add(Arrays.asList(ids));
			hql += " WHERE refId in (:ids)  ";
		}

		Query deleteQuery = this.getHibernateSession().createQuery(hql);
		if (list.size() > 1) {
			for (int i = 0; i < list.size(); i++) {
				deleteQuery.setParameterList("ids" + i, list.get(i));
			}
		} else {
			deleteQuery.setParameterList("ids", list.get(0));
		}
		deleteQuery.executeUpdate();
	}

	/**
	 * 分割List
	 * 
	 * @param targe
	 * @param size
	 * @return
	 */
	public static List<List<Long>> segmentationList(Long[] targe, int size) {
		List<List<Long>> listArr = new ArrayList<List<Long>>();
		// 获取被拆分的数组个数
		int arrSize = targe.length % size == 0 ? targe.length / size
				: targe.length / size + 1;
		for (int i = 0; i < arrSize; i++) {
			List<Long> sub = new ArrayList<Long>();
			// 把指定索引数据放入到list中
			for (int j = i * size; j <= size * (i + 1) - 1; j++) {
				if (j <= targe.length - 1) {
					sub.add(targe[j]);
				}
			}
			listArr.add(sub);
		}
		return listArr;
	}
}
