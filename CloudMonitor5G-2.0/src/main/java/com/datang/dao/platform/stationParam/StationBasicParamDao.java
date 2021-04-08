package com.datang.dao.platform.stationParam;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.security.UserGroup;

@Repository
public class StationBasicParamDao extends GenericHibernateDao<StationBasicParamPojo, Long>{

	/**
	 * 根据paramid查询数据
	 * @author lucheng
	 * date:2020年月6月22日 下午7:05:31
	 * @param id
	 * @return
	 */
	public StationBasicParamPojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationBasicParamPojo.class);
		criteria.add(Restrictions.eq("cellParamId", id));
		List list = criteria.list();
		if(list.size() < 1){
			return null;
		}else{
			return (StationBasicParamPojo) list.get(0);
		}
	}
	
	/**
	 * 获取同站下其余localcellid的基站工程参数表
	 * @param planParamPojo
	 * @return
	 */
	public List<StationBasicParamPojo> getStationBasicParamList(String siteName){
		Criteria criteria = this.getHibernateSession().createCriteria(StationBasicParamPojo.class);
		criteria.add(Restrictions.eq("siteName", siteName));
//		criteria.add(Restrictions.ne("cellParamId", planParamPojo.getId()));
		criteria.addOrder(Order.asc("localCellId"));
		List list = criteria.list();
		return list;
	}

}
