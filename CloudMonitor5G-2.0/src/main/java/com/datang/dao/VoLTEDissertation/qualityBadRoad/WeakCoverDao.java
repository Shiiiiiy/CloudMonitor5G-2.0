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
import com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover;

/**
 * volte质量专题---volte语音质差---弱覆盖Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月9日 下午4:14:31
 * @version
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverDao extends
		GenericHibernateDao<VolteQualityBadRoadWeakCover, Long> {

	/**
	 * 根据测试日志的id集合获取弱覆盖质差路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoadWeakCover> queryWeakCoverRoadsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取弱覆盖质差路段数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryWeakCoverRoadNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoadWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
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
				VolteQualityBadRoadWeakCover.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
