package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.other;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.otherProblem.VolteCallEstablishDelayExceptionOtherProblem;

/**
 * 其他问题实现Dao
 * 
 * @explain
 * @name VolteCallEstablishDelayExceptionOtherProblem
 * @author shenyanwei
 * @date 2016年5月25日上午9:26:27
 */
@Repository
@SuppressWarnings("all")
public class VolteCallEstablishDelayExceptionOtherProblemDao extends
		GenericHibernateDao<VolteCallEstablishDelayExceptionOtherProblem, Long> {

	/**
	 * 根据测试日志的id集合获取其他问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionOtherProblem> queryVolteCallEstablishDelayExceptionOtherProblemsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionOtherProblem.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取其他质差路段id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryOtherRoadIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionOtherProblem.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
