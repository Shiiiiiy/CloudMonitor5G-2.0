package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamNeighbourAdvice;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamQualityBadNeighbourPlot;

/**
 * 流媒体专题---流媒体视频质差---邻区问题---优化建议添加邻区
 * 
 * @explain
 * @name NeighbourPlotAddNeighbourPlotDao
 * @author shenyanwei
 * @date 2017年10月23日上午9:43:17
 */
@Repository
@SuppressWarnings("all")
public class StreamNeighbourAdviceDao extends
		GenericHibernateDao<StreamNeighbourAdvice, Long> {

	/**
	 * 根据根据邻区问题获取该问题中的优化建议添加邻区
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<StreamNeighbourAdvice> queryStreamNeighbourAdvice(
			StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot) {

		if (null == streamQualityBadNeighbourPlot
				|| null == streamQualityBadNeighbourPlot.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamNeighbourAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadNeighbourPlot");
		criteria.add(Restrictions.eq("id",
				streamQualityBadNeighbourPlot.getId()));
		createCriteria.addOrder(Order.desc("duration"));
		return createCriteria.list();
	}

	/**
	 * 根据根据邻区配置路段id获取该质差路段中的LTE邻区添加建议详情LIST
	 * 
	 * @param roadId
	 * @return
	 */
	public List<StreamNeighbourAdvice> queryCellInfoByRoad(Long roadId) {

		if (null == roadId) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamNeighbourAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadNeighbourPlot");
		criteria.add(Restrictions.eq("id", roadId));
		return createCriteria.list();
	}
}
