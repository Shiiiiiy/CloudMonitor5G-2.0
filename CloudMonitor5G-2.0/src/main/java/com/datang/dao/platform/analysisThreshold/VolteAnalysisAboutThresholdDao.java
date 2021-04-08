package com.datang.dao.platform.analysisThreshold;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.security.User;


/**
 * Volte专题分析门限用户选取实现Dao
 * @author shenyanwei
 * @date 2016年4月28日下午1:21:01
 */
@Repository
public class VolteAnalysisAboutThresholdDao extends
		GenericHibernateDao<VolteAnalysisAboutThreshold, Long> {
/**
 * 根据用户查询
 * @param userId
 * @return
 */
	public List<VolteAnalysisAboutThreshold> selectByUser(User user){
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteAnalysisAboutThreshold.class);
		createCriteria.add(Restrictions.eq("user", user));
		List<VolteAnalysisAboutThreshold> list = (List<VolteAnalysisAboutThreshold>) createCriteria.list();
		if(list!=null)return list;
		else return null;
	}
	public VolteAnalysisAboutThreshold selectByType(String thresholdType){
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteAnalysisAboutThreshold.class);
		createCriteria.add(Restrictions.eq("thresholdType", thresholdType));
		VolteAnalysisAboutThreshold aboutThreshold = (VolteAnalysisAboutThreshold) createCriteria.uniqueResult();
		if(aboutThreshold!=null) return aboutThreshold;
		else return null;
	}
}
