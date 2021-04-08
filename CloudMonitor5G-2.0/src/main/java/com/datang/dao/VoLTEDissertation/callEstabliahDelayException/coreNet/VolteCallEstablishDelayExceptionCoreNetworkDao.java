package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.coreNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.constant.TwoDimensionalChartType;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.coreNetwork.VolteCallEstablishDelayExceptionCoreNetwork;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.domain.chart.OneDimensionalChartValues;
import com.datang.domain.chart.TwoDimensionalChartValues;

/**
 * 核心网原因实现Dao
 * 
 * @explain
 * @name VolteCallEstablishDelayExceptionCoreNetworkDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:24:13
 */
@Repository
@SuppressWarnings("all")
public class VolteCallEstablishDelayExceptionCoreNetworkDao extends
		GenericHibernateDao<VolteCallEstablishDelayExceptionCoreNetwork, Long> {

	/**
	 * 根据测试日志的id集合获取核心网问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionCoreNetwork> queryCoreNetworkRoadsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionCoreNetwork.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取核心网问题数量
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Integer queryCoreNetworkRoadNumByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return null;
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionCoreNetwork.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		Long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return null == total || 0 == total ? null : total.intValue();
	}

	/**
	 * 根据测试日志的id集合获取核心网问题的id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryCoreNetworkRoadIdsByLogIds(List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayExceptionCoreNetwork.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		List list = createCriteria.list();
		return list;
	}

	/**
	 * 获取所有核心网问题呼叫时延的某个一维图值序列
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param qbrId
	 * @return
	 */
	public List<Object> queryOneDimensionalChartValues(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> qbrId) {
		if (null == qbrId
				|| 0 == qbrId.size()
				|| null == rtpPacketLostRatioHourChart
				|| !StringUtils.hasText(rtpPacketLostRatioHourChart
						.getAxisCustomer())
				|| !StringUtils.hasText(rtpPacketLostRatioHourChart
						.getAxisDefault())) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				OneDimensionalChartValues.class);

		Criteria createCriteria2 = createCriteria
				.createCriteria("oneDimensionalChartConfig");
		createCriteria2.add(Restrictions.eq("chartType",
				rtpPacketLostRatioHourChart.getChartType()));

		// createCriteria.add(Restrictions.in("qbrId", qbrId));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections
				.groupProperty("oneDimensionalChartConfig"));
		String axisCustomer = rtpPacketLostRatioHourChart.getAxisCustomer();
		String axisDefault = rtpPacketLostRatioHourChart.getAxisDefault();
		String[] axisCustomerSplit = axisCustomer.trim().split(",");
		String[] axisDefaultSplit = axisDefault.trim().split(",");
		for (String customer : axisCustomerSplit) {
			inner: for (int i = 0; i < axisDefaultSplit.length; i++) {
				if (axisDefaultSplit[i].trim().equals(customer.trim())) {
					projectionList.add(Projections.sum("seq" + i));
					break inner;
				}
			}
		}
		createCriteria.setProjection(projectionList);
		Object uniqueResult = createCriteria.uniqueResult();

		if (uniqueResult instanceof Object[]) {
			Object[] values = (Object[]) uniqueResult;
			Object[] copyOfRange = Arrays.copyOfRange(values, 1, values.length);
			for (int i = 0; i < copyOfRange.length; i++) {
				copyOfRange[i] = null == copyOfRange[i] ? "'-'"
						: copyOfRange[i];
			}
			List<Object> asList = Arrays.asList(copyOfRange);
			return asList;

		}
		return new ArrayList<>();
	}

	/**
	 * 获取所有核心网问题呼叫时延的某个二维图值对象list
	 * 
	 * @param chartType
	 * @param qbrId
	 * @return
	 */
	public List<TwoDimensionalChartValues> queryTwoDimenSionalChartValues(
			TwoDimensionalChartType chartType, List<Long> qbrId) {
		if (null == qbrId || 0 == qbrId.size() || null == chartType) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				TwoDimensionalChartValues.class);

		Criteria createCriteria2 = createCriteria
				.createCriteria("twoDimensionalChartConfig");
		createCriteria2.add(Restrictions.eq("chartType", chartType.name()));

		// createCriteria.add(Restrictions.in("qbrId", qbrId));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("xAxis").as("xAxis"));
		projectionList.add(Projections.groupProperty("yAxis").as("yAxis"));
		projectionList.add(Projections.sum("value").as("value"));

		createCriteria.setProjection(projectionList);
		AliasToBeanResultTransformer aliasToBeanResultTransformer = new AliasToBeanResultTransformer(
				TwoDimensionalChartValues.class);
		createCriteria.setResultTransformer(aliasToBeanResultTransformer);

		return createCriteria.list();
	}

}
