package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamQualityBadWeakCover;

/**
 * 流媒体专题---流媒体视频质差---弱覆盖Dao
 * 
 * @explain
 * @name StreamWeakCoverDao
 * @author shenyanwei
 * @date 2017年10月23日上午10:08:58
 */
@Repository
@SuppressWarnings("all")
public class StreamWeakCoverDao extends
		GenericHibernateDao<StreamQualityBadWeakCover, Long> {

	/**
	 * 根据测试日志的id集合获取弱覆盖问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBadWeakCover> queryStreamWeakCoversByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamQualityBadWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取弱覆盖问题数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryStreamWeakCoverNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamQualityBadWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取弱覆盖问题id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryStreamWeakCoverIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamQualityBadWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
