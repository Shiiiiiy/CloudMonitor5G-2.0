package com.datang.dao.stationTest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.stationTest.StationReportExcelPojo;
/**
 * 生成的单站报告excel相关信息Dao
 * @author lucheng
 *
 */
@Repository
public class StationReportDisplayDao extends GenericHibernateDao<StationReportExcelPojo, Long> {

	public long doPageQueryCount(PageList pageList) {
		
		Criteria criteria = getCiteriaCondition(pageList);
		long total = 0;
		criteria.setProjection(null);
		criteria.setFirstResult(0);
		total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return total;
	}
	public AbstractPageList doPageQuery(PageList pageList) {
		
		Criteria criteria = getCiteriaCondition(pageList);
		
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
	
	public List<StationReportExcelPojo> findByParam(PageList pageList) {
		
		Criteria criteria = getCiteriaCondition(pageList);
		
		criteria.addOrder(Order.desc("id"));

		List<StationReportExcelPojo> list = (List<StationReportExcelPojo>)criteria.list();
		return list;
	}
	
	public Criteria getCiteriaCondition(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(StationReportExcelPojo.class);
		PlanParamPojo plan = (PlanParamPojo) pageList.getParam("planParamPojo");
		Object startTime = pageList.getParam("startTime");
		Object endTime = pageList.getParam("endTime");
		Object reportName = pageList.getParam("reportName");
		Object cityStr = pageList.getParam("cityStr");
		Object model = pageList.getParam("model");
		
		if(StringUtils.hasText((String)model)){
			criteria.add(Restrictions.eq("model", model));
		}
		
		if(plan!=null && StringUtils.hasText(plan.getCity()) && !"全部".equals(plan.getCity())){
			criteria.add(Restrictions.eq("region", plan.getCity()));
		}
		
		if(startTime != null){
			criteria.add(Restrictions.ge("reportCreatDate", startTime.toString()));
		}
		if(endTime != null){
			criteria.add(Restrictions.le("reportCreatDate", endTime.toString()));
		}
		if(StringUtils.hasText((String)reportName)){
			criteria.add(Restrictions.eq("reportName",(String)reportName));
		}
		if(plan!=null  && StringUtils.hasText(plan.getSiteName())){
			criteria.add(Restrictions.eq("siteName",plan.getSiteName()));
		}
		
		
		if(cityStr != null){
			String[] cityNames = cityStr.toString().split(",");
			criteria.add(Restrictions.in("region",cityNames));
		}
		
		return criteria;
	}
	

}
