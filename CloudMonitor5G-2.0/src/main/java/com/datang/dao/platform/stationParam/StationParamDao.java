package com.datang.dao.platform.stationParam;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.security.UserGroup;

@Repository
public class StationParamDao extends GenericHibernateDao<StationParamPojo, Long>{

	/**
	 * 通过menuid获取数据
	 * @author maxuancheng
	 * date:2020年2月14日 下午5:19:47
	 * @param cityId
	 * @return
	 */
	public StationParamPojo findByMenuId(Long cityId) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StationParamPojo.class);
		Criteria criteria = createCriteria.createCriteria("menu");
		criteria.add(Restrictions.eq("id", cityId));
		StationParamPojo pojo = (StationParamPojo) createCriteria.uniqueResult();
		return pojo;
	}

}
