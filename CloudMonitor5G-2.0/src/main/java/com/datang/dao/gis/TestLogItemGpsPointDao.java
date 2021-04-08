/**
 * 
 */
package com.datang.dao.gis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.domain.gis.TestLogItemGpsPointDetail;

/**
 * 测试日志轨迹点Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月26日 上午10:54:04
 * @modify:yinzhipeng 2017年7月24日
 * @version 1.5.2
 */
@Repository
@SuppressWarnings("all")
public class TestLogItemGpsPointDao extends
		GenericHibernateDao<TestLogItemGpsPoint, Long> {
	/**
	 * 根据测试日志ID集合查询
	 * 
	 * @param testLogIds
	 * @return
	 */
	public List<TestLogItemGpsPoint> getTestLogItemGpsPoints(
			List<Long> testLogIds) {
		List<TestLogItemGpsPoint> points = new ArrayList<>();
		if (null == testLogIds) {
			return points;
		}
		// SELECT MIN(ID) ID, RECSEQNO,LONGITUDE,LATITUDE
		// FROM IADS_TESTLOG_GPS_POINT
		// WHERE RECSEQNO IN(?) GROUP BY RECSEQNO,LONGITUDE,LATITUDE
		// ORDER BY RECSEQNO ASC,ID ASC
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemGpsPoint.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList
				.add(Projections.groupProperty("recSeqNo").as("recSeqNo"));
		projectionList
				.add(Projections.groupProperty("latitude").as("latitude"));
		projectionList.add(Projections.groupProperty("longitude").as(
				"longitude"));
		projectionList.add(Projections.min("id").as("id"));
		criteria.setProjection(projectionList);
		criteria.add(Restrictions.in("recSeqNo", testLogIds));
		criteria.add(Restrictions.between("latitude",
				Double.valueOf("16.585919"), Double.valueOf("53.743108")));
		criteria.add(Restrictions.between("longitude",
				Double.valueOf("73.386567"), Double.valueOf("135.300369")));
		criteria.addOrder(Order.asc("recSeqNo"));
		criteria.addOrder(Order.asc("id"));
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				TestLogItemGpsPoint.class);
		criteria.setResultTransformer(resultTransformer);
		points = criteria.list();
		return points;
	}

	/**
	 * 获取测试日志下某个时间段的某个指标类型的轨迹点(不具备指标信息,轨迹按先后顺序排列)
	 * 
	 * @param recSeqNo
	 * @param indexType
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<TestLogItemGpsPoint> getTestLogItemGpsIndexPointDirection(
			Long recSeqNo, Integer indexType, Long startTime, Long endTime) {
		List<TestLogItemGpsPoint> points = new ArrayList<>();
		if (null == recSeqNo) {
			return points;
		}

		// 设置指标类型和时间筛选条件
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemGpsPointDetail.class);
		criteria.add(Restrictions.isNotNull("indexValue"));
		criteria.add(Restrictions.eq("type", 1));
		if (null != indexType) {
			criteria.add(Restrictions.eq("indexType", indexType));
			criteria.add(Restrictions.isNotNull("indexType"));
		}
		if (null != startTime) {
			criteria.add(Restrictions.ge("indexTime", startTime));
		}
		if (null != endTime) {
			criteria.add(Restrictions.le("indexTime", endTime));
		}

		// 设置日志和日志轨迹范围筛选条件
		Criteria createCriteria = criteria.createAlias("testLogItemGpsPoint",
				"testLogItemGpsPoint");
		createCriteria.add(Restrictions.eq("testLogItemGpsPoint.recSeqNo",
				recSeqNo));

		createCriteria.add(Restrictions.between("testLogItemGpsPoint.latitude",
				Double.valueOf("16.585919"), Double.valueOf("53.743108")));
		createCriteria.add(Restrictions.between(
				"testLogItemGpsPoint.longitude", Double.valueOf("73.386567"),
				Double.valueOf("135.300369")));

		// 设置应查询参数
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.max("indexTime").as("id"));
		projectionList.add(Projections.groupProperty(
				"testLogItemGpsPoint.latitude").as("latitude"));
		projectionList.add(Projections.groupProperty(
				"testLogItemGpsPoint.longitude").as("longitude"));
		criteria.setProjection(projectionList);

		// 设置排序条件
		criteria.addOrder(Order.asc("id"));

		// 设置返回结果集
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				TestLogItemGpsPoint.class);
		criteria.setResultTransformer(resultTransformer);
		points = criteria.list();
		return points;
	}

}
