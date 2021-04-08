/**
 * 
 */
package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.VideoQualityBadPingPong;

/**
 * volte质量专题---volte视频质差---乒乓切换Dao
 * 
 * @explain
 * @name VideoPingPongDao
 * @author shenyanwei
 * @date 2017年5月12日下午1:30:23
 */
@Repository
@SuppressWarnings("all")
public class VideoPingPongDao extends
		GenericHibernateDao<VideoQualityBadPingPong, Long> {

	/**
	 * 根据测试日志的id集合获取乒乓切换问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBadPingPong> queryVideoPingPongsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBadPingPong.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("time"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取乒乓切换问题数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryVideoPingPongNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBadPingPong.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取乒乓切换问题id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryVideoPingPongIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBadPingPong.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
