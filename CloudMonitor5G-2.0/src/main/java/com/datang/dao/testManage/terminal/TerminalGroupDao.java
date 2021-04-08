/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.dao.testManage.terminal;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.datang.bean.testManage.terminal.TerminalGroupQuery;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.StringUtils;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 自动路测分组DAO
 * 
 * @author dingzhongchang
 * @version 1.0.0
 */
@Repository
public class TerminalGroupDao extends GenericHibernateDao<TerminalGroup, Long> {

	/**
	 * 通过分组名称得到所有自动路测终端
	 * 
	 * @param teName
	 *            分组名称
	 * @return 自动路测终端列表
	 */
	public Set<Terminal> queryTerminals(String tgName) {
		TerminalGroup group = queryTerminalGroup(tgName);
		if (group == null) {
			return new TreeSet<Terminal>();
		}
		return group.getTerminals();
	}

	/**
	 * 通过分组名称得到所有自动路测终端
	 * 
	 * @param teName
	 *            分组名称
	 * @return 自动路测终端列表
	 */
	public List<TerminalGroup> queryTerminalGroups(Long... ids) {
		List<TerminalGroup> list = new LinkedList<TerminalGroup>();
		if (!CollectionUtils.isEmpty(Arrays.asList(ids))) {
			for (Long id : ids) {
				list.add(find(id));
			}
		}
		return list;
	}

	/**
	 * 通过分组名称得到所有自动路测终端
	 * 
	 * @param teName
	 *            分组名称
	 * @return 自动路测终端列表
	 */
	public List<TerminalGroup> queryTerminalGroups(Collection<Long> ids) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalGroup.class);
		if (!CollectionUtils.isEmpty(ids)) {
			criteria.add(Expression.in("id", ids));
		}
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}

	/**
	 * 通过分组名称得到分组
	 * 
	 * @param teName
	 *            分组名称
	 * @return 自动路测终端列表
	 */
	public TerminalGroup queryTerminalGroup(String tgName) {
		String hql = "from TerminalGroup tg left join fetch tg.terminals B where tg.name =:tgName";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("tgName", tgName);
		// if (tgName.equalsIgnoreCase(TerminalConstants.DEFAULT_GROUP_NAME) &&
		// CollectionUtils.isEmpty(query.list())) {
		// TerminalGroup group = new TerminalGroup();
		// group.setName(TerminalConstants.DEFAULT_GROUP_NAME);
		// group.setTerminals(new HashSet<Terminal>());
		// create(group);
		// }
		return (TerminalGroup) query.uniqueResult();
	}

	/**
	 * 分页查询自动路侧终端
	 * 
	 * @param terminalQuery
	 *            查询条件
	 * @return 查询出的结果
	 */
	public Collection<TerminalGroup> query(TerminalGroupQuery terminalGroupQuery) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalGroup.class);
		if (!StringUtils.hasText(terminalGroupQuery.getName())) {
			criteria.add(Expression.like("name", terminalGroupQuery.getName(),
					MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("name"));
		// yzp
		// criteria.setFirstResult(terminalGroupQuery.getPagerOffset());
		// criteria.setMaxResults(terminalGroupQuery.getPagerPerPage());
		Collection<TerminalGroup> tgs = criteria.list();
		// yzp
		// terminalGroupQuery.setQueryResults(tgs);
		// yzp
		// if (terminalGroupQuery.getPagerCount() == -1) {
		// int count = (Integer) criteria
		// .setProjection(Projections.rowCount()).uniqueResult();
		// terminalGroupQuery.setPagerCount(count);
		// }
		return tgs;
	}

	/**
	 * 排序查询自动路侧终端
	 * 
	 * @return 查询出的结果
	 */
	public Collection<TerminalGroup> queryAllOrderGroup() {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalGroup.class);
		criteria.addOrder(Order.asc("name"));
		Collection<TerminalGroup> tgs = criteria.list();
		return tgs;
	}

	/**
	 * 排序查询自动路侧终端
	 * 
	 * @return 查询出的结果
	 */
	public Collection<TerminalGroup> queryAllOrderGroup(String name) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalGroup.class);
		criteria.add(Expression.eq("name", name));
		criteria.addOrder(Order.asc("name"));
		Collection<TerminalGroup> tgs = criteria.list();
		return tgs;
	}

}
