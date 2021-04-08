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
import com.datang.common.util.StringUtils;
import com.datang.domain.gis.TestLogItemGpsPointDetail;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 测试日志指标轨迹点Dao
 * 
 * @author yinzhipeng
 * @date:2016年5月10日 上午9:40:21
 * @modify:yinzhipeng 2017年7月24日
 * @version 1.5.2
 */
@Repository
@SuppressWarnings("all")
public class TestLogItemGpsPointDetailDao extends
		GenericHibernateDao<TestLogItemGpsPointDetail, Long> {

	/**
	 * 获取测试日志下某个时间段的某个指标类型的轨迹点(具备指标信息,轨迹按先后顺序排列)
	 * 
	 * @param recSeqNo
	 *            日志主键
	 * @param indexType
	 *            指标类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getTestLogItemGpsIndexPoint(
			Long recSeqNo, Integer indexType, Long startTime, Long endTime) {
		List<TestLogItemIndexGpsPoint> points = new ArrayList<>();
		if (null == recSeqNo || null == indexType) {
			return points;
		}
		// 设置指标类型和时间段筛选条件
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemGpsPointDetail.class);
		criteria.add(Restrictions.eq("indexType", indexType));
		criteria.add(Restrictions.isNotNull("indexType"));
		criteria.add(Restrictions.isNotNull("indexValue"));
		criteria.add(Restrictions.eq("type", 1));
		if (null != startTime) {
			criteria.add(Restrictions.ge("indexTime", startTime));
		}
		if (null != endTime) {
			criteria.add(Restrictions.le("indexTime", endTime));
		}

		// 设置日志和日志轨迹筛选条件
		Criteria createCriteria = criteria.createAlias("testLogItemGpsPoint",
				"testLogItemGpsPoint");
		createCriteria.add(Restrictions.eq("testLogItemGpsPoint.recSeqNo",
				recSeqNo));
		createCriteria.add(Restrictions.between("testLogItemGpsPoint.latitude",
				Double.valueOf("16.585919"), Double.valueOf("53.743108")));
		createCriteria.add(Restrictions.between(
				"testLogItemGpsPoint.longitude", Double.valueOf("73.386567"),
				Double.valueOf("135.300369")));

		// 设置应查询的参数
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("testLogItemGpsPoint.latitude")
				.as("latitude"));
		projectionList.add(Projections
				.property("testLogItemGpsPoint.longitude").as("longitude"));
		projectionList.add(Projections.property("testLogItemGpsPoint.cellId")
				.as("cellId"));
		projectionList.add(Projections.property("indexValue").as("indexValue"));
		criteria.setProjection(projectionList);

		// 设置排序条件
		criteria.addOrder(Order.asc("indexTime"));

		// 设置返回结果集
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				TestLogItemIndexGpsPoint.class);
		criteria.setResultTransformer(resultTransformer);
		points = criteria.list();
		return points;
	}

	/**
	 * 获取5G测试日志下某个时间段的某个指标类型的轨迹点(具备指标信息,轨迹按先后顺序排列)
	 * 
	 * @param recSeqNo
	 *            日志主键
	 * @param indexType
	 *            指标类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getTestLogItemGpsIndexPoint5g(
			Long recSeqNo, Integer indexType, Long startTime, Long endTime) {
		List<TestLogItemIndexGpsPoint> points = new ArrayList<>();
		if (null == recSeqNo || null == indexType) {
			return points;
		}
		// 设置指标类型和时间段筛选条件
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemGpsPointDetail.class);
		criteria.add(Restrictions.eq("indexType", indexType));
		criteria.add(Restrictions.isNotNull("indexType"));
		criteria.add(Restrictions.isNotNull("indexValue"));
		criteria.add(Restrictions.eq("type", 1));
		if (null != startTime) {
			criteria.add(Restrictions.ge("indexTime", startTime));
		}
		if (null != endTime) {
			criteria.add(Restrictions.le("indexTime", endTime));
		}

		// 设置日志和日志轨迹筛选条件
		Criteria createCriteria = criteria.createAlias("testLogItemGpsPoint",
				"testLogItemGpsPoint");
		createCriteria.add(Restrictions.eq("testLogItemGpsPoint.recSeqNo",
				recSeqNo));
		createCriteria.add(Restrictions.between("testLogItemGpsPoint.latitude",
				Double.valueOf("16.585919"), Double.valueOf("53.743108")));
		createCriteria.add(Restrictions.between(
				"testLogItemGpsPoint.longitude", Double.valueOf("73.386567"),
				Double.valueOf("135.300369")));

		// 设置应查询的参数
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("testLogItemGpsPoint.latitude")
				.as("latitude"));
		projectionList.add(Projections
				.property("testLogItemGpsPoint.longitude").as("longitude"));
		projectionList.add(Projections.property("testLogItemGpsPoint.cellId")
				.as("cellId"));
		projectionList.add(Projections.property("testLogItemGpsPoint.pci").as(
				"pci"));
		projectionList.add(Projections
				.property("testLogItemGpsPoint.maxBeamNo").as("maxBeamNo"));
		projectionList.add(Projections.property("testLogItemGpsPoint.maxRsrp")
				.as("maxRsrp"));
		projectionList.add(Projections.property("testLogItemGpsPoint.beamNo")
				.as("beamNo"));
		projectionList.add(Projections.property("testLogItemGpsPoint.rsrp").as(
				"rsrp"));

		projectionList.add(Projections.property("testLogItemGpsPoint.nc1").as(
				"nc1"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc2").as(
				"nc2"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc3").as(
				"nc3"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc4").as(
				"nc4"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc5").as(
				"nc5"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc6").as(
				"nc6"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc7").as(
				"nc7"));
		projectionList.add(Projections.property("testLogItemGpsPoint.nc8").as(
				"nc8"));

		projectionList.add(Projections.property("indexValue").as("indexValue"));
		criteria.setProjection(projectionList);

		// 设置排序条件
		criteria.addOrder(Order.asc("indexTime"));

		// 设置返回结果集
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				TestLogItemIndexGpsPoint.class);
		criteria.setResultTransformer(resultTransformer);
		points = criteria.list();
		return points;
	}

	/**
	 * 获取测试日志下某个时间段的某些事件类型的轨迹点(具备事件信息,轨迹按先后顺序排列)
	 * 
	 * @param recSeqNo
	 *            日志主键
	 * @param iconTypes
	 *            事件类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getTestLogItemGpsEventPoint(
			Long recSeqNo, List<Integer> iconTypes, Long startTime, Long endTime) {
		List<TestLogItemIndexGpsPoint> points = new ArrayList<>();
		if (null == recSeqNo) {
			return points;
		}
		// 设置事件类型和时间段筛选条件
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemGpsPointDetail.class);
		if (null != iconTypes && 0 != iconTypes.size()) {
			criteria.add(Restrictions.in("indexType", iconTypes));
		}
		criteria.add(Restrictions.isNotNull("indexType"));
		criteria.add(Restrictions.isNull("indexValue"));
		criteria.add(Restrictions.eq("type", 0));
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

		// 设置应查询的参数
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("testLogItemGpsPoint.latitude")
				.as("latitude"));
		projectionList.add(Projections
				.property("testLogItemGpsPoint.longitude").as("longitude"));
		projectionList.add(Projections.property("testLogItemGpsPoint.cellId")
				.as("cellId"));
		projectionList.add(Projections.property("indexType").as("indexType"));
		criteria.setProjection(projectionList);

		// 设置排序条件
		criteria.addOrder(Order.asc("indexTime"));

		// 设置返回结果集
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				TestLogItemIndexGpsPoint.class);
		criteria.setResultTransformer(resultTransformer);
		points = criteria.list();
		return points;
	}
	
	public List<TestLogItemIndexGpsPoint> getPointsExceptionEtgTral(List<String> logNameList) {
		List<TestLogItemIndexGpsPoint> points = new ArrayList<>();
		// 设置指标类型和时间段筛选条件
		Criteria criteria = this.getHibernateSession().createCriteria(
				StationSAMTralPojo.class);
		criteria.add(Restrictions.in("nrLogname", logNameList));
//		criteria.add(Restrictions.eq("indexType", indexType));
//		criteria.add(Restrictions.isNotNull("indexType"));
//		criteria.add(Restrictions.isNotNull("indexValue"));
//		criteria.add(Restrictions.eq("type", 1));
//		if (null != startTime) {
//			criteria.add(Restrictions.ge("indexTime", startTime));
//		}
//		if (null != endTime) {
//			criteria.add(Restrictions.le("indexTime", endTime));
//		}
//
//		// 设置日志和日志轨迹筛选条件
//		Criteria createCriteria = criteria.createAlias("testLogItemGpsPoint",
//				"testLogItemGpsPoint");
//		createCriteria.add(Restrictions.eq("testLogItemGpsPoint.recSeqNo",
//				recSeqNo));
//		createCriteria.add(Restrictions.between("testLogItemGpsPoint.latitude",
//				Double.valueOf("16.585919"), Double.valueOf("53.743108")));
//		createCriteria.add(Restrictions.between(
//				"testLogItemGpsPoint.longitude", Double.valueOf("73.386567"),
//				Double.valueOf("135.300369")));

//		// 设置应查询的参数
//		ProjectionList projectionList = Projections.projectionList();
//		projectionList.add(Projections.property("latitude")
//				.as("latitude"));
//		projectionList.add(Projections
//				.property("longtitude").as("longitude"));
//		projectionList.add(Projections.property("testLogItemGpsPoint.cellId")
//				.as("cellId"));
//		projectionList.add(Projections.property("testLogItemGpsPoint.pci").as(
//				"pci"));
//		projectionList.add(Projections
//				.property("testLogItemGpsPoint.maxBeamNo").as("maxBeamNo"));
//		projectionList.add(Projections.property("testLogItemGpsPoint.maxRsrp")
//				.as("maxRsrp"));
//		projectionList.add(Projections.property("testLogItemGpsPoint.beamNo")
//				.as("beamNo"));
//		projectionList.add(Projections.property("testLogItemGpsPoint.rsrp").as(
//				"rsrp"));

//		projectionList.add(Projections.property("nrNcellnumber1").as(
//				"nc1"));
//		projectionList.add(Projections.property("nrNcellnumber2").as(
//				"nc2"));
//		projectionList.add(Projections.property("nrNcellnumber3").as(
//				"nc3"));
//		projectionList.add(Projections.property("nrNcellnumber4").as(
//				"nc4"));
//		projectionList.add(Projections.property("nrNcellnumber5").as(
//				"nc5"));
//		projectionList.add(Projections.property("nrNcellnumber6").as(
//				"nc6"));
//		projectionList.add(Projections.property("nrNcellnumber7").as(
//				"nc7"));
//		projectionList.add(Projections.property("nrNcellnumber8").as(
//				"nc8"));

//		projectionList.add(Projections.property("indexValue").as("indexValue"));
//		criteria.setProjection(projectionList);
//
		// 设置排序条件
		criteria.addOrder(Order.asc("timeLong"));
//
//		// 设置返回结果集
//		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
//				TestLogItemIndexGpsPoint.class);
//		criteria.setResultTransformer(resultTransformer);
		List<StationSAMTralPojo> list= (List<StationSAMTralPojo>)criteria.list();
		int i=0;
		for (StationSAMTralPojo stationSAMTralPojo : list) {
			TestLogItemIndexGpsPoint testLogItemIndexGpsPoint = new TestLogItemIndexGpsPoint();
			if(StringUtils.hasText(stationSAMTralPojo.getLongtitude())&&Double.valueOf(stationSAMTralPojo.getLongtitude())>0){
				testLogItemIndexGpsPoint.setLongitude(Double.valueOf(stationSAMTralPojo.getLongtitude()));
				testLogItemIndexGpsPoint.setLatitude(Double.valueOf(stationSAMTralPojo.getLatitude()));
//				testLogItemIndexGpsPoint.setCellId(Long.valueOf(i));
			}
			if(StringUtils.hasText(stationSAMTralPojo.getLteCellid())){
				testLogItemIndexGpsPoint.setCellId(Long.valueOf(stationSAMTralPojo.getLteCellid()));
			}else if(StringUtils.hasText(stationSAMTralPojo.getNrCellid())){
				testLogItemIndexGpsPoint.setCellId(Long.valueOf(stationSAMTralPojo.getNrCellid()));
			}else{
				testLogItemIndexGpsPoint.setCellId(Long.valueOf(-1));
			}
			points.add(testLogItemIndexGpsPoint);
//			i++;
//			if(i==30){
//				break;
//			}
		}
		return points;
	}

}
