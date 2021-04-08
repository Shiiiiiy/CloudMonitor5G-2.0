package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
/**
 * 网优验收测试表
 * @author maxuancheng
 *
 */
@Repository
public class StationNetoptReceiveTestDao extends GenericHibernateDao<StationNetoptReceiveTestPojo, Long>{

	/**
	 * 根据日志id查询数据
	 * @author maxuancheng
	 * date:2020年2月22日 下午4:13:35
	 * @param id
	 * @return
	 */
	public StationNetoptReceiveTestPojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationNetoptReceiveTestPojo.class);
		criteria.add(Restrictions.eq("cellParamId", id));
		List list = criteria.list();
		if(list.size() < 1){
			return null;
		}else{
			return (StationNetoptReceiveTestPojo) list.get(0);
		}
	}
	
	/**
	 * 获取同站下其余localcellid的网优验收测试表
	 * @param planParamPojo
	 * @return
	 */
	public List<StationNetoptReceiveTestPojo> getNetoptReceiveTest(String siteName){
		Criteria criteria = this.getHibernateSession().createCriteria(StationNetoptReceiveTestPojo.class);
		criteria.add(Restrictions.eq("siteName", siteName));
//		criteria.add(Restrictions.ne("cellParamId", planParamPojo.getId()));
		criteria.addOrder(Order.asc("localCellId"));
		List list = criteria.list();
		return list;
	}

}
