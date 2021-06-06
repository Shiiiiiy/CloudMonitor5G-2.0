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
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.taskOrderManage.StationTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 定点测试任务工单
 * @author lucheng
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class StationTaskOrderDao extends GenericHibernateDao<StationTaskOrderPojo, Long>{

	public long doPageQueryCount(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(StationTaskOrderPojo.class);
		
		Criteria terminal = criteria.createCriteria("terminal");
		Criteria terminalGroup = terminal.createCriteria("terminalGroup");
		
		Object cityId =  pageList.getParam("cityId");
		Object beginDate =  pageList.getParam("beginDate");
		Object endDate =  pageList.getParam("endDate");
		
		Object siteName =  pageList.getParam("siteName");
		Object networkType =  pageList.getParam("networkType");
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
		
		
		if(StringUtils.hasText((String)siteName)){
			criteria.add(Restrictions.like("siteName", (String) siteName,MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText((String)networkType)){
			criteria.add(Restrictions.eq("taskNetworkType", (String) networkType));
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
		
		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return total;
	}
	public EasyuiPageList doPageQuery(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(StationTaskOrderPojo.class);
		
		Criteria terminal = criteria.createCriteria("terminal");
		Criteria terminalGroup = terminal.createCriteria("terminalGroup");
		
		Object cityId =  pageList.getParam("cityId");
		Object beginDate =  pageList.getParam("beginDate");
		Object endDate =  pageList.getParam("endDate");
		
		Object siteName =  pageList.getParam("siteName");
		Object networkType =  pageList.getParam("networkType");
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
		
		
		if(StringUtils.hasText((String)siteName)){
			criteria.add(Restrictions.like("siteName", (String) siteName,MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText((String)networkType)){
			criteria.add(Restrictions.eq("taskNetworkType", (String) networkType));
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

		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		total = doPageQueryCount(pageList);
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
	
	public List<StationTaskOrderPojo> findStationTaskTask(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(StationTaskOrderPojo.class);
		
		Criteria terminal = criteria.createCriteria("terminal");
		Criteria terminalGroup = terminal.createCriteria("terminalGroup");
		
		Object cityId =  pageList.getParam("cityId");
		Object beginDate =  pageList.getParam("beginDate");
		Object endDate =  pageList.getParam("endDate");
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

		if(boxId != null){
			criteria.add(Restrictions.eq("boxId", (String) boxId));
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

		List<StationTaskOrderPojo> list = (List<StationTaskOrderPojo>)criteria.list();
		
		return list;
	}
	
	public List<StationEtgTralPojo> findTralById(List<String> fileNames,String pci){
		Criteria criteria = this.getHibernateSession().createCriteria(StationEtgTralPojo.class);
		criteria.add(Restrictions.in("nrLogname", fileNames));
		criteria.add(Restrictions.eq("nrPci", pci));
		return criteria.list();
	}
}
