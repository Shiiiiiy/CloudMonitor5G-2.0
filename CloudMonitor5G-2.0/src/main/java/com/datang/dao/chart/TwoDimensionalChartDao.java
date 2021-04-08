/**
 * 
 */
package com.datang.dao.chart;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.constant.TwoDimensionalChartType;
import com.datang.domain.chart.TwoDimensionalChartConfig;

/**
 * 二维图表配置DAO
 * 
 * @author yinzhipeng
 * @date:2016年1月4日 下午2:44:53
 * @version
 */
@Repository
@SuppressWarnings("all")
public class TwoDimensionalChartDao extends
		GenericHibernateDao<TwoDimensionalChartConfig, Long> {
	/**
	 * 查询某个图表类型的配置
	 * 
	 * @param twoDimensionalChartType
	 * @return
	 */
	public TwoDimensionalChartConfig queryTwoDimensionalChartConfig(
			TwoDimensionalChartType twoDimensionalChartType) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				TwoDimensionalChartConfig.class);
		createCriteria.add(Restrictions.eq("chartType",
				twoDimensionalChartType.name()));
		return (TwoDimensionalChartConfig) createCriteria.uniqueResult();
	}
}
