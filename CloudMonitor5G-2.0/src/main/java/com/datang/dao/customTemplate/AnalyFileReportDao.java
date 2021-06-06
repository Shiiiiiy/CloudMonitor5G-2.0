package com.datang.dao.customTemplate;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.customTemplate.AnalyFileReport;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.web.beans.report.AnalyFileReportRequertBean;
import com.datang.web.beans.report.ReportRequertBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
public class AnalyFileReportDao extends
		GenericHibernateDao<AnalyFileReport, Long> {

	public long getPageItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				AnalyFileReport.class);
		AnalyFileReportRequertBean pageParams = (AnalyFileReportRequertBean) pageList
				.getParam("pageQueryBean");


		// 用户定义的报表
		String reportId = pageParams.getReportId();
		if (StringUtils.hasText(reportId)) {
			criteria.add(Restrictions.eq("reportId", reportId));
		}

		// 筛选创建人
		Long taskId = pageParams.getTaskId();
		if (taskId!=null) {
			criteria.add(Restrictions.eq("taskId", taskId));
		}

		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
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
				AnalyFileReport.class);
		AnalyFileReportRequertBean pageParams = (AnalyFileReportRequertBean) pageList
				.getParam("pageQueryBean");


		// 用户定义的报表
		String reportId = pageParams.getReportId();
		if (StringUtils.hasText(reportId)) {
			criteria.add(Restrictions.eq("reportId", reportId));
		}

		// 筛选创建人
		Long taskId = pageParams.getTaskId();
		if (taskId!=null) {
			criteria.add(Restrictions.eq("taskId", taskId));
		}

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
	 * 批量删除
	 *
	 * @param ids
	 */
	public void deleteByTaskId(Long id) {
		Query deleteQuery = this.getHibernateSession().createQuery(
				"delete CustomLogReportTask st where st.taskId:id");
		deleteQuery.setLong("id", id);
		deleteQuery.executeUpdate();
	}



}
