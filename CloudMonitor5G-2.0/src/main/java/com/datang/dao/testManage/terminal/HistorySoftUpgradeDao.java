/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.dao.testManage.terminal;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.CollectionUtils;
import com.datang.domain.testManage.terminal.HistorySoftUpgrade;

/**
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-17
 */
@Repository
public class HistorySoftUpgradeDao extends
		GenericHibernateDao<HistorySoftUpgrade, Long> {

	@SuppressWarnings("unchecked")
	public HistorySoftUpgrade queryCurrentSoftUpgrade(String boxId) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				HistorySoftUpgrade.class);
		criteria.createCriteria("terminal").add(Expression.eq("boxId", boxId));
		criteria.addOrder(Order.desc("sendDate"));
		Collection<HistorySoftUpgrade> list = criteria.list();
		if (!CollectionUtils.isEmpty(list)) {
			return (HistorySoftUpgrade) ((List) list).get(0);
		}
		return null;
	}

	/**
	 * @param id
	 *            delete id
	 */
	public void deleteByTerminalId(Long id) {
		Query deleteQuery = this
				.getHibernateSession()
				.createQuery(
						"delete HistorySoftUpgrade hs where hs.terminal.id=:terminalId");
		deleteQuery.setLong("terminalId", id);
		deleteQuery.executeUpdate();
	}

	/**
	 * @param ids
	 */
	public void deleteByTerminalIds(Long[] ids) {
		Query deleteQuery = this.getHibernateSession().createQuery(
				"delete HistorySoftUpgrade hs where hs.terminal.id in (:ids)");
		deleteQuery.setParameterList("ids", ids);
		deleteQuery.executeUpdate();
	}
}
