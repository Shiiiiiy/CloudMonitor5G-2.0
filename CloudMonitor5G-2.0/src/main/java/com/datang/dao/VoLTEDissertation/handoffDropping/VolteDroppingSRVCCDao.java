package com.datang.dao.VoLTEDissertation.handoffDropping;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;

/**
 * volte质量专题---volte切换失败Dao
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午1:47:02
 */
@Repository
@SuppressWarnings("all")
public class VolteDroppingSRVCCDao extends
		GenericHibernateDao<VolteDroppingSRVCC, Long> {
	/**
	 * 根据测试日志的id集合获取所有切换失败事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingSRVCC.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取切换失败事件数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryVolteDroppingSRVCCNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingSRVCC.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取SRVCC切换失败id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryVolteDroppingSRVCCIdsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingSRVCC.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}

	/**
	 * 根据原始测试日志的id集合和对比测试日志id的集合获取在两个结果集中目标小区相同的srvcc切换失败事件
	 * 
	 * @param testLogItemIds
	 * @param compareTestLogItemIds
	 * @return
	 */
	public List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndCompareLogIds(
			List<Long> testLogItemIds, List<Long> compareTestLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()
				|| null == compareTestLogItemIds
				|| 0 == compareTestLogItemIds.size()) {
			return new ArrayList<>();
		}

		// 查询对比日志srvcc中的目标小区id
		DetachedCriteria compareDetachedCriteria = DetachedCriteria.forClass(
				VolteDroppingSRVCC.class, "srvcc");
		DetachedCriteria compareTestLogDetachedcriteria = compareDetachedCriteria
				.createCriteria("testLogItem");
		compareTestLogDetachedcriteria.add(Restrictions.in("recSeqNo",
				compareTestLogItemIds));
		compareDetachedCriteria.setProjection(Projections.property("failId"));
		compareDetachedCriteria.add(Restrictions.isNotNull("failId"));
		compareDetachedCriteria.add(Restrictions.eqProperty("srvcc.failId",
				"compareSrvcc.failId"));

		// 查询原始日志和对比日志srvcc中相同目标小区id,满足条件(原始日志srvcc中目标小区id和对比日志srvcc中目标小区相等)
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(
				VolteDroppingSRVCC.class, "compareSrvcc");
		DetachedCriteria testLogDetachedcriteria = detachedCriteria
				.createCriteria("testLogItem");
		testLogDetachedcriteria
				.add(Restrictions.in("recSeqNo", testLogItemIds));
		detachedCriteria.setProjection(Projections.property("failId"));

		detachedCriteria.add(Property.forName("failId").in(
				compareDetachedCriteria));

		// 根据原始日志和对比日志srvcc中相同目标小区id,查找原始日志的srvcc和对比日志的srvcc
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingSRVCC.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		createCriteria.add(Property.forName("failId").in(detachedCriteria));
		List<Long> ids = new ArrayList<>();
		ids.addAll(testLogItemIds);
		ids.addAll(compareTestLogItemIds);
		criteria.add(Restrictions.in("recSeqNo", ids));
		createCriteria.addOrder(Order.asc("failId"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合和failId获取所有切换失败事件,按开始时间asc
	 * 
	 * @param testLogItemIds
	 * @param failId
	 * @return
	 */
	public List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndFailId(
			List<Long> testLogItemIds, Long failId) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()
				|| null == failId) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingSRVCC.class);
		createCriteria.add(Restrictions.eq("failId", failId));
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.asc("startTime"));
		return createCriteria.list();
	}

}
