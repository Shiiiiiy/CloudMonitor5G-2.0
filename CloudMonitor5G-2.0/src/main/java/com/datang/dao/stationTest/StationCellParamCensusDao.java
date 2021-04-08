package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stationTest.StationCellParamCensusPojo;

@Repository
public class StationCellParamCensusDao extends GenericHibernateDao<StationCellParamCensusPojo, Long>{

	/**
	 * 根据paramid查询数据
	 * @author maxuancheng
	 * date:2020年2月21日 下午7:05:31
	 * @param id
	 * @return
	 */
	public StationCellParamCensusPojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationCellParamCensusPojo.class);
		criteria.add(Restrictions.eq("cellParamId", id));
		List list = criteria.list();
		if(list.size() < 1){
			return null;
		}else{
			return (StationCellParamCensusPojo) list.get(0);
		}
	}

}
