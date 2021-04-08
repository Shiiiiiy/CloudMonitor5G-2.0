/**
 * 
 */
package com.datang.dao.customTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;

/**
 * 
 * @author lucheng
 * @date 2020年12月22日 下午2:55:46
 */
@Repository
public class CustomReportTemplateDao extends GenericHibernateDao<CustomReportTemplatePojo, Long> {
	
	/**
	 * 查询规划参数表信息
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList){
		Criteria criteria = findByparam(pageList);
		
		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.addOrder(Order.desc("id"));
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List<CustomReportTemplatePojo> list = (List<CustomReportTemplatePojo>)criteria.list();
		List<String> templateNameList = new ArrayList<String>();
		List<CustomReportTemplatePojo> unionlist = new ArrayList<CustomReportTemplatePojo>();
		for (CustomReportTemplatePojo customReportTemplatePojo : list) {
			if(!templateNameList.contains(customReportTemplatePojo.getTemplateName())){
				templateNameList.add(customReportTemplatePojo.getTemplateName());
				CustomReportTemplatePojo pojo = new CustomReportTemplatePojo();
				pojo.setId(customReportTemplatePojo.getId());
				pojo.setGroup(customReportTemplatePojo.getGroup());
				pojo.setImportDate(customReportTemplatePojo.getImportDate());
				pojo.setRegion(customReportTemplatePojo.getRegion());
				pojo.setTemplateName(customReportTemplatePojo.getTemplateName());
				pojo.setUserName(customReportTemplatePojo.getUserName());
				unionlist.add(pojo);
			}
			
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(unionlist);
		easyuiPageList.setTotal(unionlist.size() + "");
		return easyuiPageList;
	}
	
	public Criteria findByparam(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(CustomReportTemplatePojo.class);
		Criteria createCriteria2 = criteria.createCriteria("group");
		Object startTime = pageList.getParam("beginImportDate");
		Object endTime = pageList.getParam("endImportDate");
		Object cityStr = pageList.getParam("citNames");
		Object cityIds = pageList.getParam("cityIds");
		Object templateName = pageList.getParam("templateName");
		
		if(startTime != null){
			criteria.add(Restrictions.ge("importDate", startTime));
		}
		if(endTime != null){
			criteria.add(Restrictions.le("importDate", endTime));
		}
		if(StringUtils.hasText((String)cityStr)){
			String[] cityNames = cityStr.toString().split(",");
			createCriteria2.add(Restrictions.in("name",cityNames));
		}
		if(StringUtils.hasText((String)cityIds)){
			String[] cityids = cityIds.toString().split(",");
			List<Long> idLong = new ArrayList<Long>();
			for (String cityid : cityids) {
				idLong.add(Long.valueOf(cityid));
			}
			createCriteria2.add(Restrictions.in("id",idLong));
		}
		if(templateName != null && StringUtils.hasText((String)templateName)){
			criteria.add(Restrictions.eq("templateName", templateName));
		}
		return criteria;
	}

	/**
	 * 根据参数查询模板信息
	 * @author lucheng
	 * @date 2020年12月25日 下午5:26:53
	 * @param pageList
	 * @return
	 */
	public List<CustomReportTemplatePojo> queryTemplateByParam(PageList pageList) {
		Criteria criteria = findByparam(pageList);
		
		criteria.addOrder(Order.desc("id"));
		List<CustomReportTemplatePojo> list = (List<CustomReportTemplatePojo>)criteria.list();
		return list;
	}
}
