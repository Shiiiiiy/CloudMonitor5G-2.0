package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;

/**
 * 弱覆盖问题实现Dao
 * 
 * @explain
 * @name VolteCallEstablishDelayExceptionWeakCoverDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:35:45
 */
@Repository
@SuppressWarnings("all")
public class VolteCallEstablishDelayExceptionWeakCoverDao extends
		GenericHibernateDao<VolteCallEstablishDelayExceptionWeakCover, Long> {
	/**
	 * 根据测试日志的id集合获弱覆盖原因
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionWeakCover> queryVolteCallEstablishDelayExceptionWeakCoversByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取弱覆盖质差路段id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryWeakCoverRoadIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
