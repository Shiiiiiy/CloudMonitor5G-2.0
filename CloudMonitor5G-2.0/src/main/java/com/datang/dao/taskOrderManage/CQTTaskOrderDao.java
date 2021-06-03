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
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 定点测试任务工单
 * @author lucheng
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class CQTTaskOrderDao extends GenericHibernateDao<FixedPointTaskOrderPojo, Long>{

	public EasyuiPageList doPageQuery(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(FixedPointTaskOrderPojo.class);
		
		Criteria terminal = criteria.createCriteria("terminal");
		Criteria terminalGroup = terminal.createCriteria("terminalGroup");
		
		Object cityId =  pageList.getParam("cityId");
		Object beginDate =  pageList.getParam("beginDate");
		Object endDate =  pageList.getParam("endDate");
		Object loctionDeparture =  pageList.getParam("loctionDeparture");
		Object occupyNrCellName =  pageList.getParam("occupyNrCellName");
		Object occupyLteCellName =  pageList.getParam("occupyLteCellName");
		Object boxId =  pageList.getParam("boxId");
		Object workOrderState =  pageList.getParam("workOrderState");
		Object taskInitiator =  pageList.getParam("taskInitiator");
		Object taskTimeLimit =  pageList.getParam("taskTimeLimit");
		

		if(cityId != null && Long.valueOf(cityId.toString()) != -1){
			terminalGroup.add(Restrictions.eq("id", Long.valueOf(cityId.toString())));			
		}
		
		if(beginDate != null){
			criteria.add(Restrictions.ge("taskCreatTime", (Date)beginDate));
		}
		
		if(endDate != null){
			criteria.add(Restrictions.le("taskCreatTime", (Date)endDate));
		}
		
		if(loctionDeparture !=null){
			if (Integer.valueOf(loctionDeparture.toString()) == 0) {
				//小于100米
				criteria.add(Restrictions.lt("testLocationSkewing", 100L));
				
			} else if (Integer.valueOf(loctionDeparture.toString()) == 1) {
				//大于等于100米
				criteria.add(Restrictions.ge("testLocationSkewing", 100L));
				//小于等于500米
				criteria.add(Restrictions.le("testLocationSkewing", 500L));
				
			} else if (Integer.valueOf(loctionDeparture.toString()) == 2) {
				//大于500米
				criteria.add(Restrictions.gt("testLocationSkewing", 500L));
			}
		}
		
		if(StringUtils.hasText((String)occupyNrCellName)){
			criteria.add(Restrictions.like("occupyNrCellName", (String) occupyNrCellName,MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText((String)occupyLteCellName)){
			criteria.add(Restrictions.like("occupyLteCellName", (String) occupyLteCellName,MatchMode.ANYWHERE));
		}

		if(StringUtils.hasText((String)boxId)){
			criteria.add(Restrictions.eq("boxId", (String) boxId));
		}

		if(StringUtils.hasText((String)workOrderState)){
			criteria.add(Restrictions.eq("workOrderState", (String)workOrderState));
		}
		
		if(StringUtils.hasText((String)taskInitiator)){
			criteria.add(Restrictions.eq("taskInitiator", (String) taskInitiator));
		}

		if(StringUtils.hasText((String)taskTimeLimit)){
			criteria.add(Restrictions.eq("taskTimeLimit", (String) taskTimeLimit));
		}
		
		criteria.addOrder(Order.desc("id"));

		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

	public List<PlanParamPojo> getNrCell(Long cityId) {
		Criteria criteria = this.getHibernateSession().createCriteria(PlanParamPojo.class);
		if(cityId != null){
			Criteria createCriteria2 = criteria.createCriteria("cellInfo");
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
		}
		criteria.addOrder(Order.desc("id"));
		List<PlanParamPojo> list = (List<PlanParamPojo>)criteria.list();
		return list;
	}
	
	
	public List<Plan4GParam> getLteCell(Long cityId) {
		Criteria criteria = this.getHibernateSession().createCriteria(Plan4GParam.class);
		if(cityId != null){
			Criteria createCriteria2 = criteria.createCriteria("cellInfo");
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("id", cityId));
		}
		criteria.addOrder(Order.desc("id"));
		List<Plan4GParam> list = (List<Plan4GParam>)criteria.list();
		return list;
	}
	
	public List<FixedPointTaskOrderPojo> findFixedPointTask(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(FixedPointTaskOrderPojo.class);
		
		Criteria terminal = criteria.createCriteria("terminal");
		Criteria terminalGroup = terminal.createCriteria("terminalGroup");
		
		Object cityId =  pageList.getParam("cityId");
		Object beginDate =  pageList.getParam("beginDate");
		Object endDate =  pageList.getParam("endDate");
		Object occupyNrCellName =  pageList.getParam("occupyNrCellName");
		Object occupyLteCellName =  pageList.getParam("occupyLteCellName");
		Object boxId =  pageList.getParam("boxId");
		Object workOrderState =  pageList.getParam("workOrderState");
		Object taskInitiator =  pageList.getParam("taskInitiator");
		Object taskTimeLimit =  pageList.getParam("taskTimeLimit");
		Object workOrderType =  pageList.getParam("workOrderType");
		Object workOrderId =  pageList.getParam("workOrderId");

		if(cityId != null && Long.valueOf(cityId.toString()) != -1){
			terminalGroup.add(Restrictions.eq("id", Long.valueOf(cityId.toString())));			
		}
		
		if(beginDate != null){
			criteria.add(Restrictions.ge("taskCreatTime", (Date)beginDate));
		}
		
		if(endDate != null){
			criteria.add(Restrictions.le("taskCreatTime", (Date)endDate));
		}
		
		if(StringUtils.hasText((String)occupyNrCellName)){
			criteria.add(Restrictions.eq("occupyNrCellName", (String) occupyNrCellName));
		}
		
		if(StringUtils.hasText((String)occupyLteCellName)){
			criteria.add(Restrictions.eq("occupyLteCellName", (String) occupyLteCellName));
		}

		if(boxId != null){
			criteria.add(Restrictions.eq("boxId", boxId.toString()));
		}

		if(workOrderState != null){
			criteria.add(Restrictions.eq("workOrderState", (Long)workOrderState));
		}
		
		if(StringUtils.hasText((String)taskInitiator)){
			criteria.add(Restrictions.eq("taskInitiator", (String) taskInitiator));
		}

		if(StringUtils.hasText((String)taskTimeLimit)){
			criteria.add(Restrictions.eq("taskTimeLimit", (String) taskTimeLimit));
		}
		
		if(StringUtils.hasText((String)workOrderType)){
			criteria.add(Restrictions.like("workOrderId", (String) workOrderType,MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText((String)workOrderId)){
			criteria.add(Restrictions.eq("workOrderId", (String) workOrderId));
		}
		
		criteria.addOrder(Order.desc("id"));

		List<FixedPointTaskOrderPojo> list = (List<FixedPointTaskOrderPojo>)criteria.list();
		
		return list;
	}

	public List<PlanParamPojo> getNrCell(String cityName) {
		Criteria criteria = getHibernateSession().createCriteria(PlanParamPojo.class);
		if(cityName != null){
			Criteria createCriteria2 = criteria.createCriteria("cellInfo");
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("name", cityName));
		}
		List<PlanParamPojo> list = (List<PlanParamPojo>)criteria.list();
		return list;
	}

	public List<Plan4GParam> getLteCell(String cityName) {
		Criteria criteria = getHibernateSession().createCriteria(Plan4GParam.class);
		if(cityName != null){
			Criteria createCriteria2 = criteria.createCriteria("cellInfo");
			Criteria createCriteria3 = createCriteria2.createCriteria("group");
			createCriteria3.add(Restrictions.eq("name", cityName));
		}
		List<Plan4GParam> list = (List<Plan4GParam>)criteria.list();
		return list;
	}
}
