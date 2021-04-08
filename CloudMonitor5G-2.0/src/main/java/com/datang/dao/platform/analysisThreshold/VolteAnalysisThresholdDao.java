/**
 * 
 */
package com.datang.dao.platform.analysisThreshold;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;

/**
 * volte专题分析门限----dao实现
 * 
 * @author yinzhipeng
 * @date:2015年9月28日 下午4:00:34
 * @version
 */
@Repository
public class VolteAnalysisThresholdDao extends
		GenericHibernateDao<VolteAnalysisThreshold, Long> {
	/**
	 * 根据英文名称查询
	 * 
	 * @param nameEn
	 * @return
	 */
	public VolteAnalysisThreshold queryByEnName(String nameEn) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteAnalysisThreshold.class);
		createCriteria.add(Restrictions.eq("nameEn", nameEn));
		VolteAnalysisThreshold aboutThreshold = (VolteAnalysisThreshold) createCriteria
				.uniqueResult();
		return aboutThreshold;
	}

	/**
	 * 根据门限专题类别查询
	 * 
	 * @return
	 */
	public List<VolteAnalysisThreshold> queryBySubjectType(String subjectType) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteAnalysisThreshold.class);
		createCriteria.add(Restrictions.eq("subjectType", subjectType));
		createCriteria.addOrder(Order.asc("id"));
		return createCriteria.list();
	}
	
	/**
	 * 根据门限专题条件查询
	 * @return
	 */
	public List<VolteAnalysisThreshold> queryByMapParam(Map<String,Object> mapParam){
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteAnalysisThreshold.class);
		Object subjectType = mapParam.get("subjectType");
		Object thresholdType = mapParam.get("thresholdType");
		Object nameCh = mapParam.get("nameCh");
		Object nameEn = mapParam.get("nameEn");
		
		if(StringUtils.hasText((String)subjectType)){
			createCriteria.add(Restrictions.eq("subjectType", subjectType));
		}
		
		if(StringUtils.hasText((String)thresholdType)){
			createCriteria.add(Restrictions.eq("thresholdType", thresholdType));
		}
		
		if(StringUtils.hasText((String)nameCh)){
			createCriteria.add(Restrictions.eq("nameCh", nameCh));
		}
		
		if(StringUtils.hasText((String)nameEn)){
			createCriteria.add(Restrictions.eq("nameEn", nameEn));
		}
		
		createCriteria.addOrder(Order.asc("id"));
		return createCriteria.list();
	}
}
