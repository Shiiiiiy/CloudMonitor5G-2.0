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
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.VideoQualityBadPatternSwitch;

/**
 * volte质量专题---volte视频质差---模式转换Dao
 * 
 * @explain
 * @name VideoPatternSwitchDao
 * @author shenyanwei
 * @date 2017年6月5日下午3:21:35
 */
@Repository
@SuppressWarnings("all")
public class VideoPatternSwitchDao extends
		GenericHibernateDao<VideoQualityBadPatternSwitch, Long> {

	/**
	 * 根据测试日志的id集合获取模式转换问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBadPatternSwitch> queryVideoPatternSwitchsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBadPatternSwitch.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("time"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取模式转换问题数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryVideoPatternSwitchNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBadPatternSwitch.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取模式转换问题id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryVideoPatternSwitchIdsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBadPatternSwitch.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}
}
