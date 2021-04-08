package com.datang.dao.oppositeOpen;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;

@Repository
public class OppositeOpen3dCompletionShowDao extends GenericHibernateDao<PlanParamPojo, Long> {

	/**
	 * 根据名称和城市查询不通过的测试项
	 * @param name
	 * @param city
	 * @return
	 */
	public long getReportAnalysssResult(String name,String city) {
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		criteria.add(Restrictions.eq("region", city));
		criteria.add(Restrictions.eq("noPassTestEvent", name));
		List list = criteria.list();
		return list.size();
	}

	/**
	 * 根据城市名获取数据
	 * @author maxuancheng
	 * date:2020年3月4日 下午5:27:44
	 * @param cityName
	 * @return
	 */
	public List<Plan4GParam> getDataOfCityName(String cityName) {
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		criteria.add(Restrictions.eq("region", cityName));
		return criteria.list();
	}

	/**
	 * 根据小区名和城市名 查询小区数据
	 * @author maxuancheng
	 * date:2020年5月6日 下午3:24:01
	 * @param cellName
	 * @param cityName
	 * @return
	 */
	public List<Plan4GParam> queryCellLonAndLat(String cellName, String cityName) {
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		criteria.add(Restrictions.like("cellName", cellName,MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("region", cityName));
		return criteria.list();
	}

}
