/**
 * 
 */
package com.datang.dao.customTemplate;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;
import com.datang.domain.customTemplate.MappingIeToKpiPojo;

/**
 * 
 * @author lucheng
 * @date 2020年12月22日 下午2:55:46
 */
@Repository
public class MappingIeToKpiDao extends GenericHibernateDao<MappingIeToKpiPojo, Long> {
	
	/**
	 * 查询映射值
	 * @author lucheng
	 * @date 2020年12月23日 下午6:05:43
	 * @param ieList ie指标的集合
	 * @param kpiList 映射的kpi值的集合
	 * @return
	 */
	public List<MappingIeToKpiPojo> findByParam(List<String> ieList,List<String> kpiList){
		Criteria criteria = this.getHibernateSession().createCriteria(MappingIeToKpiPojo.class);
		
		if(ieList != null){
			criteria.add(Restrictions.in("ieName", ieList));
		}
		if(kpiList != null){
			criteria.add(Restrictions.in("kpiName", kpiList));
		}
		
		criteria.addOrder(Order.asc("kpiName"));
		List<MappingIeToKpiPojo> list = (List<MappingIeToKpiPojo>)criteria.list();
		return list;
	}
}
