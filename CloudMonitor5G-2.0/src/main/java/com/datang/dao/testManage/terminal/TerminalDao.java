/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.dao.testManage.terminal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.datang.bean.testManage.terminal.TerminalQuery;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.testManage.terminal.HistoryTestPlan;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.domain.testManage.terminal.TestModule;

/**
 * 自动路侧终端DAO
 * 
 * @author dingzhongchang
 * @version 1.0.0
 */
@Repository
public class TerminalDao extends GenericHibernateDao<Terminal, Long> {
	/**
	 * 通过终端BoxId得到自动路测终端所包含的历史计划
	 * 
	 * @param boxId
	 *            自动路测终端名称
	 * @return 自动路测终端所包含的历史计划
	 */
	public Set<HistoryTestPlan> queryTestPlans(String boxId) {
		Terminal te = queryTerminal(boxId);
		if (te == null) {
			return new TreeSet<HistoryTestPlan>();
		}
		return te.getHistoryTestPlans();
	}

	/**
	 * 通过ID得到自动路测终端所包含的历史计划
	 * 
	 * @param id
	 *            自动路测终端ID
	 * @return 自动路测终端所包含的历史计划
	 */
	public Set<HistoryTestPlan> queryTestPlans(Long id) {
		Terminal terminal = find(id);
		return terminal.getHistoryTestPlans();
	}

	/**
	 * 通过终端名称得到自动路测终端所包含的模块
	 * 
	 * @param boxId
	 *            自动路测终端名称
	 * @return 自动路测终端所包含的模块
	 */
	public List<TestModule> queryTestModules(String boxId) {
		Terminal te = queryTerminal(boxId);
		if (te == null) {
			return new LinkedList<TestModule>();
		}
		return te.getTestModuls();
	}

	/**
	 * 通过终端名称得到自动路测终端所包含的模块
	 * 
	 * @param boxId
	 *            自动路测终端名称
	 * @return 自动路测终端所包含的模块
	 */
	private Terminal getTerminal(Long id) {

		Session session = this.getHibernateSession();

		Criteria criteria = session.createCriteria(Terminal.class);

		criteria.setFetchMode("testModuls", FetchMode.JOIN);

		// criteria.setFetchMode("terminalGroup", FetchMode.JOIN);

		criteria.add(Expression.eq("id", id));

		Object obj = criteria.uniqueResult();
		if (obj == null) {
			return null;
		}
		return (Terminal) obj;

	}

	/**
	 * 按ID返回实体对象.
	 * 
	 * @param id
	 *            实体ID.
	 * @return T 按返回实体。
	 */

	public Terminal find(Long id) {
		return this.getTerminal(id);
	}
	
	/**
	 * 按ID返回实体对象.
	 * 
	 * @param id
	 *            实体ID.
	 * @return T 按返回实体。
	 */

	public Terminal getTerminalType(Long id) {
		Session session2 = this.getHibernateSession();

		Criteria criteria = session2.createCriteria(Terminal.class);

		criteria.add(Restrictions.eq("id", id));

		Object obj = criteria.uniqueResult();
		if (obj == null) {
			return null;
		}
		return (Terminal) obj;
	}

	/**
	 * 通过终端名称得到自动路测终端所属分组
	 * 
	 * @param boxId
	 *            自动路测终端名称
	 * @return 自动路测终端所属分组列表
	 */
	public TerminalGroup queryGroup(String boxId) {
		Terminal te = queryTerminal(boxId);
		if (te == null) {
			return null;
		}
		return te.getTerminalGroup();
	}

	// private Collection<Terminal> updatePageTesStatus(Collection<Terminal>
	// teList) {
	// if (!CollectionUtils.isEmpty(teList)) {
	// for (Terminal terminal : teList) {
	// terminal.setOnline(OnlineStatusContext.getInstance().isOnLine(terminal.getBoxId()));
	// }
	// }
	// return teList;
	// }
	/**
	 * 获取所有终端
	 * 
	 * @return 终端列表
	 */
	public List<Terminal> queryAllTerminals() {
		Collection<Terminal> findAll = findAll();
		// yzp
		// return (List<Terminal>) findAllByCriteria(getDetachedCriteria());
		return new ArrayList<Terminal>(findAll);
	}

	/**
	 * 通过名称得到自动路侧终端
	 * 
	 * @param boxIds
	 *            自动路侧终端名称
	 * @return 自动路侧终端列表
	 */
	public List<Terminal> queryTerminals(String... boxIds) {
		List<Terminal> teCollection = new ArrayList<Terminal>();
		if (boxIds == null) {
			teCollection.addAll(queryAllTerminals());
		}
		for (String boxId : boxIds) {
			teCollection.add(queryTerminal(boxId));
		}
		return teCollection;

	}

	/**
	 * 通过名称得到自动路侧终端
	 * 
	 * @param boxId
	 *            自动路侧终端名称
	 * @return 自动路侧终端列表
	 */
	public Terminal queryTerminal(String boxId) {
		String hql = "from Terminal te left join fetch te.testModuls left join fetch te.terminalGroup  where te.boxId =:boxId";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("boxId", boxId);
		Object obj = query.uniqueResult();
		if (obj == null) {
			return null;
		} else {
			Terminal terminal = (Terminal) obj;

			return terminal;
		}

	}

	public Terminal queryTerminalBoxid(String boxId) {
		String hql = "from Terminal te   where te.boxId =:boxId";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("boxId", boxId);
		Object obj = query.uniqueResult();
		if (obj == null) {
			return null;
		} else {
			Terminal terminal = (Terminal) obj;

			return terminal;
		}

	}

	/**
	 * 通过名称得到自动路侧终端
	 * 
	 * @param boxId
	 *            自动路侧终端名称
	 * @return 自动路侧终端列表
	 */
	public Terminal queryTerminalByName(String name) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				Terminal.class);
		if (null != name) {
			criteria.add(Restrictions.eq("name", name));
		}
		return (Terminal) criteria.uniqueResult();
	}

	/**
	 * 根据终端ID列表查询终端信息
	 * 
	 * @param ids
	 *            ID列表
	 * @return 终端信息
	 */
	public List<Terminal> query(Collection<Long> ids) {
		List<Terminal> list = new ArrayList<Terminal>();
		if (!CollectionUtils.isEmpty(ids)) {
			Criteria criteria = this.getHibernateSession().createCriteria(
					Terminal.class);
			criteria.add(Expression.in("id", ids));
			criteria.addOrder(Order.asc("name"));
			list.addAll(criteria.list());
		}

		return list;

	}

	/**
	 * 根据终端ID列表和是否可用状态查询终端信息
	 * 
	 * @param ids
	 *            ID列表
	 * @return 终端信息
	 */
	public List<Terminal> query(Collection<Long> ids, boolean isEnable) {
		List<Terminal> list = new ArrayList<Terminal>();
		if (!CollectionUtils.isEmpty(ids)) {
			Criteria criteria = this.getHibernateSession().createCriteria(
					Terminal.class);
			criteria.setFetchMode("terminalGroup", FetchMode.JOIN);
			criteria.add(Expression.in("id", ids));
			criteria.add(Expression.eq("enable", isEnable));
			criteria.addOrder(Order.asc("id"));
			list.addAll(criteria.list());
		}

		return list;

	}

	private DetachedCriteria getDetachedCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Terminal.class);
		criteria.addOrder(Order.desc("name"));
		return criteria;
	}

	/**
	 * @param ids
	 * @param enable
	 * @param vender
	 * @return
	 */
	public Collection<Terminal> query(Collection<Long> ids, boolean enable,
			String vender) {
		List<Terminal> list = new ArrayList<Terminal>();
		if (!CollectionUtils.isEmpty(ids)) {
			Criteria criteria = this.getHibernateSession().createCriteria(
					Terminal.class);// 查询终端的全部属性
			criteria.setFetchMode("terminalGroup", FetchMode.JOIN);// 性能优化的作用
			if (null != ids) {
				criteria.add(Expression.in("id", ids));
			}
			if (!StringUtils.hasText(vender)) {
				criteria.add(Expression.like("manufacturer", vender,
						MatchMode.ANYWHERE));
			}
			if (enable) {
				criteria.add(Expression.eq("enable", enable));
			}
			criteria.addOrder(Order.asc("id"));
			list.addAll(criteria.list());
		}

		return list;
	}

	/**
	 * 分页查询自动路侧终端
	 * 
	 * @param terminalQuery
	 *            查询条件
	 * @return 查询出的结果
	 */
	public Collection<Terminal> query(TerminalQuery terminalQuery,
			Collection<String> onlineBoxIds) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				Terminal.class);
		if (!StringUtils.hasText(terminalQuery.getBoxId())) {
			criteria.add(Expression.like("boxId", terminalQuery.getBoxId(),
					MatchMode.ANYWHERE));
		}
		if (!StringUtils.hasText(terminalQuery.getName())) {
			criteria.add(Expression.like("name", terminalQuery.getName(),
					MatchMode.ANYWHERE));
		}
		if (null != terminalQuery.getGroupId()) {
			Criteria groupCriteria = criteria.createCriteria("terminalGroup");
			groupCriteria.add(Expression.eq("id", terminalQuery.getGroupId()));
		}

		if (!StringUtils.hasText(terminalQuery.getOrderProperty())
				&& !terminalQuery.getOrderProperty().equals("online")) {
			criteria.addOrder(terminalQuery.isAsc() ? Order.asc(terminalQuery
					.getOrderProperty()) : Order.desc(terminalQuery
					.getOrderProperty()));
		} else {
			criteria.addOrder(Order.desc("name"));
		}
		if (null != terminalQuery.isOnlineStatus()) {
			if (terminalQuery.isOnlineStatus()) {
				if (CollectionUtils.isEmpty(onlineBoxIds)) {
					// 在线状态下 如果没有终端在线则什么查不到
					criteria.add(Expression.eq("boxId", ""));
				} else {
					criteria.add(Expression.in("boxId", onlineBoxIds));
				}
			} else {
				if (!CollectionUtils.isEmpty(onlineBoxIds)) {
					criteria.add(Expression.not(Expression.in("boxId",
							onlineBoxIds)));
				}

			}
		}
		// yzp
		// criteria.setFirstResult(terminalQuery.getPagerOffset());
		// criteria.setMaxResults(terminalQuery.getPagerPerPage());
		// Collection<Object[]> objects = criteria.list();
		// Collection<Terminal> tes = new ArrayList<Terminal>(objects.size());
		// for (Object[] obj : objects) {
		// tes.add((Terminal) (obj[1]));
		// }
		Collection<Terminal> tes = criteria.list();
		// yzp
		// terminalQuery.setQueryResults(tes);
		// if (terminalQuery.getPagerCount() == -1) {
		// int count = (Integer) criteria
		// .setProjection(Projections.rowCount()).uniqueResult();
		// terminalQuery.setPagerCount(count);
		// }
		return tes;
	}

	/**
	 * 多条件查询
	 * 
	 * @author yinzhipeng
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageTerminal(PageList pageList) {
		if (null == pageList || null == pageList.getParam("cityId")) {
			return new EasyuiPageList();
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				Terminal.class);
		// boxid
		Object boxId = pageList.getParam("boxId");
		// terminalGroup
		Object cityId = pageList.getParam("cityId");
		// name
		Object name = pageList.getParam("name");
		// online
		Object online = pageList.getParam("online");
		// name
		Object testTarget = pageList.getParam("testTarget");
		// online
		Object installDate = pageList.getParam("installDate");

		// 设置boxid筛选条件
		if (StringUtils.hasText((String) boxId)) {
			criteria.add(Restrictions.like("boxId", (String) boxId,
					MatchMode.ANYWHERE));
		}
		// 设置name筛选条件
		if (StringUtils.hasText((String) name)) {
			criteria.add(Restrictions.like("name", (String) name,
					MatchMode.ANYWHERE));
		}
		// 设置city筛选条件
		Criteria groupCriteria = criteria.createCriteria("terminalGroup");
		groupCriteria.add(Restrictions.eq("id", pageList.getParam("cityId")));
		criteria.addOrder(Order.desc("id"));
		// 设置online筛选条件
		if (null != online) {
			// online
			criteria.add(Restrictions.eq("online", online));
		}
		
		//设置终端类型
		if(testTarget != null){
			criteria.add(Restrictions.eq("testTarget", (Integer) testTarget));
		}
		//设置下载时间
		if(installDate != null){
			criteria.add(Restrictions.ge("installDate", installDate));
		}


		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		long total = 0;
		if(list.size() > 0){
			total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
	
	/**
	 * 根据参数查询条件
	 * @author maxuancheng
	 * date:2020年3月6日 上午10:41:39
	 * @param param
	 * @return
	 */
	public List<Terminal> findByParam(HashMap<String, Object> param) {
		Criteria criteria = this.getHibernateSession().createCriteria(Terminal.class);
		// boxid
		Object boxId = param.get("boxId");
		// terminalGroup
		Object cityId = param.get("cityId");
		// name
		Object name = param.get("name");
		// online
		Object online = param.get("online");
		// name
		Object testTarget = param.get("testTarget");
		// online
		Object installDate = param.get("installDate");

		// 设置boxid筛选条件
		if (boxId != null && StringUtils.hasText((String) boxId)) {
			criteria.add(Restrictions.like("boxId", (String) boxId,MatchMode.ANYWHERE));
		}
		// 设置name筛选条件
		if (name != null && StringUtils.hasText((String) name)) {
			criteria.add(Restrictions.like("name", (String) name,MatchMode.ANYWHERE));
		}
		// 设置online筛选条件
		if (null != online) {
			// online
			criteria.add(Restrictions.eq("online", online));
		}
		//设置终端类型
		if(testTarget != null){
			criteria.add(Restrictions.eq("testTarget", (Integer) testTarget));
		}
		//设置下载时间
		if(installDate != null){
			criteria.add(Restrictions.ge("installDate",installDate));
		}
		// 设置city筛选条件
		Criteria groupCriteria = criteria.createCriteria("terminalGroup");
		groupCriteria.add(Restrictions.eq("id", cityId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

}
