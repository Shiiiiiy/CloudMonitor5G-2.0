/**
 * 
 */
package com.datang.dao.dao5g.embbCover;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;

/**
 * eMBB覆盖专题----弱过重叠覆盖路段dao
 * 
 * @author _YZP
 * 
 */
@Repository
@SuppressWarnings("all")
public class EmbbCoverBadRoadDao extends
		GenericHibernateDao<EmbbCoverBadRoad, Long> {
	/**
	 * 根据测试日志的id集合获取所有覆盖路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<EmbbCoverBadRoad> queryAllEmbbCoverRoad(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				EmbbCoverBadRoad.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取所有覆盖路段(区分弱过重叠覆盖)
	 * 
	 * @param testLogItemIds
	 * @param coverTypeNum
	 * @return
	 */
	public List<EmbbCoverBadRoad> queryAllEmbbCoverRoad(
			List<Long> testLogItemIds, int coverTypeNum) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				EmbbCoverBadRoad.class);
		createCriteria.add(Restrictions.eq("coverTypeNum", coverTypeNum));
		createCriteria.addOrder(Order.desc("startTime"));

		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.addOrder(Order.desc("recSeqNo"));

		return createCriteria.list();
	}

}
