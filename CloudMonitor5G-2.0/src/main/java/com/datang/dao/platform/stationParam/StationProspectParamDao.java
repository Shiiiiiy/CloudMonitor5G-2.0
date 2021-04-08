package com.datang.dao.platform.stationParam;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.domain.security.UserGroup;

@Repository
public class StationProspectParamDao extends GenericHibernateDao<StationProspectParamPojo, Long>{

	/**
	 * 通过基站名称获取基站勘察表数据
	 * @author lucheng
	 * date:2020年6月29日
	 * @param siteName
	 * @return
	 */
	public StationProspectParamPojo findBySiteName(String siteName) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StationProspectParamPojo.class);
		createCriteria.add(Restrictions.eq("siteName", siteName));
		List pojoList= createCriteria.list();
		if(pojoList.size()>0){
			return (StationProspectParamPojo)pojoList.get(0);
		}
		return null;
	}

}
