/**
 * 
 */
package com.datang.dao.chart;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.constant.OneDimensionalChartType;
import com.datang.domain.chart.OneDimensionalChartConfig;

/**
 * 一维图表配置DAO
 * 
 * @author yinzhipeng
 * @date:2015年12月30日 下午2:29:46
 * @version
 */
@Repository
@SuppressWarnings("all")
public class OneDimensionalChartDao extends
		GenericHibernateDao<OneDimensionalChartConfig, Long> {
	/**
	 * 查询某个图表类型的配置
	 * 
	 * @param oneDimensionalChartType
	 * @return
	 */
	public OneDimensionalChartConfig queryOneDimensionalChartConfig(
			OneDimensionalChartType oneDimensionalChartType) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				OneDimensionalChartConfig.class);
		createCriteria.add(Restrictions.eq("chartType",
				oneDimensionalChartType.name()));
		return (OneDimensionalChartConfig) createCriteria.uniqueResult();
	}
}
