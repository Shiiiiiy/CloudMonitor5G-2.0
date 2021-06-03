package com.datang.dao.taskOrderManage;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.taskOrderManage.StationTaskLogNamePojo;

/**
 * 定点测试任务工单
 * @author lucheng
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class StationTaskLogDao extends GenericHibernateDao<StationTaskLogNamePojo, Long>{
	
	public List<StationTaskLogNamePojo> getLogNamesByOrder(String workOrder) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationTaskLogNamePojo.class);
		if(workOrder != null){
			criteria.add(Restrictions.eq("workOrderId", workOrder));
		}
		criteria.addOrder(Order.desc("id"));
		List<StationTaskLogNamePojo> list = (List<StationTaskLogNamePojo>)criteria.list();
		return list;
	}
	
	
}
