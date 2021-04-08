/**
 * 
 */
package com.datang.dao.VoLTEDissertation.qualityBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem;

/**
 * volte质量专题---volte语音质差---干扰问题路段Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月16日 上午10:12:19
 * @version
 */
@Repository
@SuppressWarnings("all")
public class DisturbDao extends
		GenericHibernateDao<VolteQualityBadRoadDisturbProblem, Long> {

	/**
	 * 根据测试日志的id集合获取干扰问题路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoadDisturbProblem> queryDisturbRoadsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadDisturbProblem.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.eq("serviceType", "1,"));
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取干扰问题路段数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryDisturbRoadNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadDisturbProblem.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取干扰问题路段的id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryDisturbRoadIdsByLogIds(List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadDisturbProblem.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
