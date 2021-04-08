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
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventImsRegistFail;

/**
 * volte质量专题---volte异常事件---IMS注册失败Dao
 * 
 * @author yinzhipeng
 * @date:2016年4月18日 上午9:55:21
 * @version
 */
@Repository
@SuppressWarnings("all")
public class ImsRegistFailDao extends
		GenericHibernateDao<VolteExceptionEventImsRegistFail, Long> {
	/**
	 * 根据测试日志的id集合获取所有ims注册失败异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEventImsRegistFail> queryVolteExceptionEventImsRegistFailsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEventImsRegistFail.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取ims注册失败异常事件数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryVolteExceptionEventImsRegistFailNumByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEventImsRegistFail.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取IMS注册失败事件的id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryImsRegistFailIdsByLogIds(List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEventImsRegistFail.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
