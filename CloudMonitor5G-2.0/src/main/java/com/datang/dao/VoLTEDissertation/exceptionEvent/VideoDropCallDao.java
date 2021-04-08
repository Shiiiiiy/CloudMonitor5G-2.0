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
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventDropCall;

/**
 * volte质量专题---volte异常事件---视频掉话Dao
 * 
 * @explain
 * @name VideoDropCallDao
 * @author shenyanwei
 * @date 2017年5月11日上午10:12:19
 */
@Repository
@SuppressWarnings("all")
public class VideoDropCallDao extends
		GenericHibernateDao<VideoExceptionEventDropCall, Long> {
	/**
	 * 根据测试日志的id集合获取所有视频掉话异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoExceptionEventDropCall> queryVideoExceptionEventDropCallsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoExceptionEventDropCall.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取视频掉话异常事件数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryVideoExceptionEventDropCallNumByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoExceptionEventDropCall.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取视频掉话事件的id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryVideoDropCallIdsByLogIds(List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoExceptionEventDropCall.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
