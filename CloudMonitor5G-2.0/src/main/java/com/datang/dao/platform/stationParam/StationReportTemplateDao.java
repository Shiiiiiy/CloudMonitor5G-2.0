package com.datang.dao.platform.stationParam;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationReportTemplatePojo;
import com.datang.domain.security.UserGroup;

@Repository
public class StationReportTemplateDao extends GenericHibernateDao<StationReportTemplatePojo, Long>{

	
	public StationReportTemplatePojo findTemplateParam(Integer templteType,Long templteValue) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StationReportTemplatePojo.class);
		createCriteria.add(Restrictions.eq("templateType", templteType));
		createCriteria.add(Restrictions.eq("templateValue", templteValue));
		StationReportTemplatePojo pojo = (StationReportTemplatePojo) createCriteria.uniqueResult();
		return pojo;
	}

	public List<StationReportTemplatePojo> findTemplateByParam(Map<String, Object> param) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StationReportTemplatePojo.class);
		Object templateName = param.get("templateName");
		Object templateType = param.get("templateType");
		Object templateValue = param.get("templateValue");
		
		if(StringUtils.hasText((String)templateName)){
			createCriteria.add(Restrictions.eq("templateName", templateName));
		}
		if(templateType!=null){
			createCriteria.add(Restrictions.eq("templateType", Integer.valueOf(templateType.toString())));
		}
		if(templateValue!=null){
			createCriteria.add(Restrictions.eq("templateValue", Long.valueOf(templateValue.toString())));
		}
		List<StationReportTemplatePojo> pojoList = (List<StationReportTemplatePojo>) createCriteria.list();
		return pojoList;
	}

}
