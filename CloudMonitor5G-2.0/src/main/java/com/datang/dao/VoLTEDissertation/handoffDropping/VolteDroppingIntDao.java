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
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;

/**
 * volte质量专题---volte系统内切换失败Dao
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午1:47:02
 */
@Repository
@SuppressWarnings("all")
public class VolteDroppingIntDao extends
		GenericHibernateDao<VolteDroppingInt, Long> {
	/**
	 * 根据测试日志的id集合获取所有切换失败事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteDroppingInt> queryVolteDroppingIntsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingInt.class);
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
	public Integer queryVolteDroppingIntNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingInt.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取系统内部切换失败id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryVolteDroppingIntIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingInt.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}

	/**
	 * 根据原始测试日志的id集合和对比测试日志id的集合获取在两个结果集中目标小区相同的系统内切换失败事件
	 * 
	 * @param testLogItemIds
	 * @param compareTestLogItemIds
	 * @return
	 */
	public List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCompareLogIds(
			List<Long> testLogItemIds, List<Long> compareTestLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()
				|| null == compareTestLogItemIds
				|| 0 == compareTestLogItemIds.size()) {
			return new ArrayList<>();
		}

		// 查询对比日志系统内切换中的目标小区id
		DetachedCriteria compareFailIdCriteria = DetachedCriteria.forClass(
				VolteDroppingInt.class, "compareInt0");
		DetachedCriteria compareFailIdTestLogDetachedcriteria = compareFailIdCriteria
				.createCriteria("testLogItem");
		compareFailIdTestLogDetachedcriteria.add(Restrictions.in("recSeqNo",
				compareTestLogItemIds));
		compareFailIdCriteria.setProjection(Projections
				.property("compareInt0.failId"));
		compareFailIdCriteria.add(Restrictions.isNotNull("compareInt0.failId"));
		compareFailIdCriteria.add(Restrictions.eqProperty("compareInt0.failId",
				"oInt.failId"));
		compareFailIdCriteria.add(Restrictions.eqProperty("compareInt0.cellId",
				"oInt.cellId"));

		// 查询对比日志系统内切换中的原小区id
		DetachedCriteria compareDetachedCriteria = DetachedCriteria.forClass(
				VolteDroppingInt.class, "compareInt1");
		DetachedCriteria compareTestLogDetachedcriteria = compareDetachedCriteria
				.createCriteria("testLogItem");
		compareTestLogDetachedcriteria.add(Restrictions.in("recSeqNo",
				compareTestLogItemIds));
		compareDetachedCriteria.setProjection(Projections
				.property("compareInt1.cellId"));
		compareDetachedCriteria.add(Restrictions
				.isNotNull("compareInt1.cellId"));
		compareDetachedCriteria.add(Restrictions.eqProperty(
				"compareInt1.failId", "oInt.failId"));
		compareDetachedCriteria.add(Restrictions.eqProperty(
				"compareInt1.cellId", "oInt.cellId"));

		// 查询原始日志和对比日志系统内切换中相同目标小区id,满足条件(原始日志系统内切换中目标小区id和对比日志系统内切换中目标小区相等)
		DetachedCriteria detachedFailIdCriteria = DetachedCriteria.forClass(
				VolteDroppingInt.class, "oInt");
		DetachedCriteria testLogDetachedcriteria = detachedFailIdCriteria
				.createCriteria("testLogItem");
		testLogDetachedcriteria
				.add(Restrictions.in("recSeqNo", testLogItemIds));
		detachedFailIdCriteria.setProjection(Projections
				.property("oInt.failId"));
		detachedFailIdCriteria.add(Property.forName("oInt.failId").in(
				compareFailIdCriteria));
		detachedFailIdCriteria.add(Property.forName("oInt.cellId").in(
				compareDetachedCriteria));
		compareDetachedCriteria.add(Restrictions.eqProperty("outerSql.failId",
				"oInt.failId"));
		compareDetachedCriteria.add(Restrictions.eqProperty("outerSql.cellId",
				"oInt.cellId"));

		// 查询原始日志和对比日志系统内切换中相同原小区id,满足条件(原始日志系统内切换中原小区id和对比日志系统内切换中原小区相等)
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(
				VolteDroppingInt.class, "oInt");
		DetachedCriteria testLogDetachedcriteria1 = detachedCriteria
				.createCriteria("testLogItem");
		testLogDetachedcriteria1.add(Restrictions
				.in("recSeqNo", testLogItemIds));
		detachedCriteria.setProjection(Projections.property("oInt.cellId"));
		detachedCriteria.add(Property.forName("oInt.failId").in(
				compareFailIdCriteria));
		detachedCriteria.add(Property.forName("oInt.cellId").in(
				compareDetachedCriteria));
		compareDetachedCriteria.add(Restrictions.eqProperty("outerSql.failId",
				"oInt.failId"));
		compareDetachedCriteria.add(Restrictions.eqProperty("outerSql.cellId",
				"oInt.cellId"));

		// 根据原始日志和对比日志系统内切换中相同目标小区id相同原始小区
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingInt.class, "outerSql");
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		createCriteria.add(Property.forName("outerSql.failId").in(
				detachedFailIdCriteria));
		createCriteria.add(Property.forName("outerSql.cellId").in(
				detachedCriteria));
		List<Long> ids = new ArrayList<>();
		ids.addAll(testLogItemIds);
		ids.addAll(compareTestLogItemIds);
		criteria.add(Restrictions.in("recSeqNo", ids));
		createCriteria.addOrder(Order.asc("outerSql.failId"));
		createCriteria.addOrder(Order.asc("outerSql.cellId"));
		List list = createCriteria.list();
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合及小区ID和切换小区ID获取所有切换失败事件,按开始时间ASC
	 * 
	 * @param testLogItemIds
	 * @param cellId
	 * @param FailId
	 * @return
	 */
	public List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCellIdAndFailId(
			List<Long> testLogItemIds, Long cellId, Long failId) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()
				|| null == cellId || null == failId) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDroppingInt.class);
		createCriteria.add(Restrictions.eq("cellId", cellId));
		createCriteria.add(Restrictions.eq("failId", failId));
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.asc("startTime"));
		return createCriteria.list();
	}
}
