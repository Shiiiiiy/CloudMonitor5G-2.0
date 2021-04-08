package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
/**
 * 性能验收测试表
 * @author maxuancheng
 *
 */
@Repository
public class StationPerformanceReceiveDao extends GenericHibernateDao<StationPerformanceReceivePojo, Long> {

	/**
	 * 根据日志id查询数据
	 * @author maxuancheng
	 * date:2020年2月22日 下午4:13:35
	 * @param id
	 * @return
	 */
	public StationPerformanceReceivePojo findByParamId(Long id) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationPerformanceReceivePojo.class);
		criteria.add(Restrictions.eq("cellParamId", id));
		List list = criteria.list();
		if(list.size() < 1){
			return null;
		}else{
			return (StationPerformanceReceivePojo) list.get(0);
		}
	}

	/**
	 * 获取同站下其余localcellid的性能验收测试表
	 * @param planParamPojo
	 * @return
	 */
	public List<StationPerformanceReceivePojo> getPerformanceReceiveList(String siteName){
		Criteria criteria = this.getHibernateSession().createCriteria(StationPerformanceReceivePojo.class);
		criteria.add(Restrictions.eq("siteName",siteName));
//		criteria.add(Restrictions.ne("cellParamId", planParamPojo.getId()));
		criteria.addOrder(Order.asc("localCellId"));
		List list = criteria.list();
		return list;
	}
}
