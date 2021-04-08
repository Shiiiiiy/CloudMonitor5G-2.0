package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.NeighbourPlotAddNeighbourPlot;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.VideoQualityBadNeighbourPlot;

/**
 * volte质量专题---volte视频质差---邻区问题---优化建议添加邻区
 * 
 * @explain
 * @name NeighbourPlotAddNeighbourPlotDao
 * @author shenyanwei
 * @date 2017年5月12日下午1:14:08
 */
@Repository
@SuppressWarnings("all")
public class NeighbourPlotAddNeighbourPlotDao extends
		GenericHibernateDao<NeighbourPlotAddNeighbourPlot, Long> {

	/**
	 * 根据根据邻区问题获取该问题中的优化建议添加邻区
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<NeighbourPlotAddNeighbourPlot> queryNeighbourPlotAddNeighbourPlot(
			VideoQualityBadNeighbourPlot videoQualityBadNeighbourPlot) {

		if (null == videoQualityBadNeighbourPlot
				|| null == videoQualityBadNeighbourPlot.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NeighbourPlotAddNeighbourPlot.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadNeighbourPlot");
		criteria.add(Restrictions.eq("id", videoQualityBadNeighbourPlot.getId()));
		return createCriteria.list();
	}

	/**
	 * 根据根据邻区配置路段id获取该质差路段中的LTE邻区添加建议详情LIST
	 * 
	 * @param roadId
	 * @return
	 */
	public List<NeighbourPlotAddNeighbourPlot> queryCellInfoByRoad(Long roadId) {

		if (null == roadId) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NeighbourPlotAddNeighbourPlot.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadNeighbourPlot");
		criteria.add(Restrictions.eq("id", roadId));
		return createCriteria.list();
	}
}
