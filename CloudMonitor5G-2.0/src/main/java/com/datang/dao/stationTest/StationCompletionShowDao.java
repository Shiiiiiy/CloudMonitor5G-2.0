package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.planParam.PlanParamPojo;

@Repository
public class StationCompletionShowDao extends GenericHibernateDao<PlanParamPojo, Long> {

	/**
	 * 根据名称和城市查询不通过的测试项
	 * @param name
	 * @param city
	 * @return
	 */
	public long getReportAnalysssResult(String name,String city) {
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		criteria.add(Restrictions.eq("city", city));
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
	public List<PlanParamPojo> getDataOfCityName(String cityName) {
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		criteria.add(Restrictions.eq("city", cityName));
		return criteria.list();
	}

	/**
	 * 根据小区名查询小区经纬度
	 * @author maxuancheng
	 * date:2020年5月6日 上午10:19:27
	 * @param cellName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PlanParamPojo> queryCellLonAndLat(String cellName, String cityName) {
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		criteria.add(Restrictions.eq("city", cityName));
		criteria.add(Restrictions.like("cellName", cellName,MatchMode.ANYWHERE));
		List list = criteria.list();
		return list;
	}

}
