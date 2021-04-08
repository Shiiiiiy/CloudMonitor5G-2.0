package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.callLocationUpdate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.calledLocationUpdate.VolteCallEstablishDelayExceptionCalledLocationUpdate;

/**
 * 呼叫建立时延异常----呼叫位置更新Dao
 * 
 * @explain
 * @name VolteCallEstablishDelayExceptionCallLocationUpdateDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:20:46
 */
@Repository
@SuppressWarnings("all")
public class VolteCallEstablishDelayExceptionCallLocationUpdateDao
		extends
		GenericHibernateDao<VolteCallEstablishDelayExceptionCalledLocationUpdate, Long> {
	/**
	 * 根据测试日志的id集合获取呼叫位置更新
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionCalledLocationUpdate> queryVolteCallEstablishDelayExceptionCalledLocationUpdatesByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionCalledLocationUpdate.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取被叫位置更新质差路段id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryLocationUpdateRoadIdsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionCalledLocationUpdate.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
