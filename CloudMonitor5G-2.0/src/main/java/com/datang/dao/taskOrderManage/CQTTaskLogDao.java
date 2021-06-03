package com.datang.dao.taskOrderManage;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 定点测试任务工单
 * @author lucheng
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class CQTTaskLogDao extends GenericHibernateDao<FixedPointTaskLogNamePojo, Long>{
	
	public List<FixedPointTaskLogNamePojo> getLogNamesByOrder(String workOrder) {
		Criteria criteria = this.getHibernateSession().createCriteria(FixedPointTaskLogNamePojo.class);
		if(workOrder != null){
			criteria.add(Restrictions.eq("workOrderId", workOrder));
		}
		criteria.addOrder(Order.desc("id"));
		List<FixedPointTaskLogNamePojo> list = (List<FixedPointTaskLogNamePojo>)criteria.list();
		return list;
	}
	
	
}
