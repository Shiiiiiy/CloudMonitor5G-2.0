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
import com.datang.domain.platform.analysisThreshold.MapTrailThreshold;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;

/**
 * 轨迹图阈值设置门限----dao实现
 * 
 * @author lucheng
 * @date:2020年12月15日 下午4:00:34
 * @version
 */
@Repository
public class MapTrailThrrsholdDao extends
		GenericHibernateDao<MapTrailThreshold, Long> {
	
	/**
	 * 根据门限专题条件查询
	 * @return
	 */
	public List<MapTrailThreshold> queryByMapParam(Map<String,Object> mapParam){
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				MapTrailThreshold.class);
		Object subjectType = mapParam.get("subjectType");
		Object thresholdType = mapParam.get("thresholdType");
		Object nameCh = mapParam.get("nameCh");
		Object nameEn = mapParam.get("nameEn");
		Object isDefault = mapParam.get("isDefault");
		Object cityId = mapParam.get("cityId");
		
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
		
		if(isDefault!=null){
			createCriteria.add(Restrictions.eq("isDefault", Integer.valueOf(isDefault.toString())));
		}
		
		if(cityId!=null){
			Criteria createCriteria2 = createCriteria
					.createCriteria("menu");
			createCriteria2.add(Restrictions.eq("refId", Long.valueOf(cityId.toString())));
		}
		
		createCriteria.addOrder(Order.asc("id"));
		return createCriteria.list();
	}
}
