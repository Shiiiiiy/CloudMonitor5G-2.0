package com.datang.dao.customTemplate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.report.StatisticeTask;
import com.datang.web.beans.report.ReportRequertBean;

/**
 * 统计任务Dao
 * 
 * @explain
 * @name CustomLogReportTaskDao
 * @author shenyanwei
 * @date 2016年9月13日下午1:18:39
 */
@Repository
@SuppressWarnings("all")
public class CustomLogReportTaskDao extends
		GenericHibernateDao<CustomLogReportTask, Long> {

	public long getPageItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomLogReportTask.class);
		ReportRequertBean pageParams = (ReportRequertBean) pageList
				.getParam("pageQueryBean");


		// 用户定义的报表
		String taskType = pageParams.getTaskType();
		if (StringUtils.hasText(taskType)) {
			criteria.add(Restrictions.eq("taskType", taskType));
		}

		// 筛选创建人
		String createrName = pageParams.getCreaterName();
		if (StringUtils.hasText(createrName)) {
			criteria.add(Restrictions.like("createrName", createrName,
					MatchMode.ANYWHERE));
		}
		// 筛选任务名称
		String name = pageParams.getName();
		if (StringUtils.hasText(name)) {
			criteria.add(Restrictions.like("name", name.trim(),
					MatchMode.ANYWHERE));
		}
		// 筛选发生时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("creatDateLong", beginDate.getTime()));
		}
		// 筛选结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("creatDateLong", endDate.getTime()));
		}

		long total = 0;
		criteria.setProjection(null);
		criteria.setFirstResult(0);
		total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return total;
	}
	/**
	 * 多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomLogReportTask.class);
		ReportRequertBean pageParams = (ReportRequertBean) pageList
				.getParam("pageQueryBean");


		// 用户定义的报表
		String taskType = pageParams.getTaskType();
		if (StringUtils.hasText(taskType)) {
			criteria.add(Restrictions.eq("taskType", taskType));
		}

		// 筛选创建人
		String createrName = pageParams.getCreaterName();
		if (StringUtils.hasText(createrName)) {
			criteria.add(Restrictions.like("createrName", createrName,
					MatchMode.ANYWHERE));
		}
		// 筛选任务名称
		String name = pageParams.getName();
		if (StringUtils.hasText(name)) {
			criteria.add(Restrictions.like("name", name.trim(),
					MatchMode.ANYWHERE));
		}
		// 筛选发生时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("creatDateLong", beginDate.getTime()));
		}
		// 筛选结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("creatDateLong", endDate.getTime()));
		}

		criteria.addOrder(Order.desc("creatDateLong"));
		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		total = getPageItemCount(pageList);
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	public void deleteList(List<Long> ids) {
		Query deleteQuery = this.getHibernateSession().createQuery(
				"delete CustomLogReportTask st where st.id in (:ids)");
		deleteQuery.setParameterList("ids", ids);
		deleteQuery.executeUpdate();
	}
	
	/**
	 * 查询统计任务
	 * @author lucheng
	 * @date 2020年8月19日 下午7:30:06
	 * @param pageList
	 * @return
	 */
	public List<CustomLogReportTask> findStatisticeTask(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomLogReportTask.class);
		
		Object boxid = pageList.getParam("boxid");
		Object workOrderId = pageList.getParam("workOrderId");

		if (StringUtils.hasText((String)boxid)) {
			criteria.add(Restrictions.eq("boxIds", (String) boxid));
		}
		if (StringUtils.hasText((String)workOrderId)) {
			criteria.add(Restrictions.like("name", (String) workOrderId,
					MatchMode.ANYWHERE));
		}
		
		List list = criteria.list();
		return list;
	}
}
