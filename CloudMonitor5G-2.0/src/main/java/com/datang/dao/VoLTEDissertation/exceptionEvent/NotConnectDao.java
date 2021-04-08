/**
 * 
 */
package com.datang.dao.VoLTEDissertation.exceptionEvent;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventNotConnect;

/**
 * volte质量专题---volte异常事件---语音未接通Dao
 * 
 * @author yinzhipeng
 * @date:2016年4月18日 上午9:04:24
 * @version
 */
@Repository
@SuppressWarnings("all")
public class NotConnectDao extends
		GenericHibernateDao<VolteExceptionEventNotConnect, Long> {
	/**
	 * 根据测试日志的id集合获取所有语音未接通异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEventNotConnect> queryVolteExceptionEventNotConnectsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEventNotConnect.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		/**
		 * 未接通事件值查询主叫的
		 */
		criteria.add(Restrictions.eq("callType", 0));
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取语音未接通异常事件数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryVolteExceptionEventNotConnectNumByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEventNotConnect.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		/**
		 * 未接通事件值查询主叫的
		 */
		criteria.add(Restrictions.eq("callType", 0));
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取语音未接通事件的id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryNotConnectIdsByLogIds(List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEventNotConnect.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		/**
		 * 未接通事件值查询主叫的
		 */
		criteria.add(Restrictions.eq("callType", 0));
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}

}
