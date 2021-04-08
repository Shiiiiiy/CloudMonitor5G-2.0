/**
 * 
 */
package com.datang.dao.VoLTEDissertation.continueWirelessBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;

/**
 * volte质量专题---连续无线差---邻区配置问题路段Dao
 * 
 * @explain
 * @name NbCellDeficiencyDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:41:42
 */
@Repository
@SuppressWarnings("all")
public class NbCellDeficiencyCWBRDao extends
		GenericHibernateDao<VolteContinueWirelessBadRoadNbDeficiency, Long> {

	/**
	 * 根据测试日志的id集合获取邻区配置问题路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteContinueWirelessBadRoadNbDeficiency> queryNbDeficiencyRoadsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteContinueWirelessBadRoadNbDeficiency.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取邻区配置问题路段数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryNbDeficiencyRoadNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteContinueWirelessBadRoadNbDeficiency.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取邻区配置问题路段id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryNbDeficiencyRoadIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteContinueWirelessBadRoadNbDeficiency.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
