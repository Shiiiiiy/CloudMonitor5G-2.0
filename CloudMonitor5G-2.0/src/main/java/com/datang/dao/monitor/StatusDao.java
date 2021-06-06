package com.datang.dao.monitor;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.monitor.StatusReport;
import com.datang.web.beans.monitor.StatusRequestBean;

/**
 * 硬件Dao
 * 
 * @explain
 * @name StatusDao
 * @author shenyanwei
 * @date 2016年7月12日下午2:36:41
 */
@Repository
@SuppressWarnings("all")
public class StatusDao extends GenericHibernateDao<StatusReport, Long> {

	public long getPageItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				StatusReport.class);
		StatusRequestBean pageParams = (StatusRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("statusReportTimeLong",
					beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("statusReportTimeLong",
					endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
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
				StatusReport.class);
		StatusRequestBean pageParams = (StatusRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("statusReportTimeLong",
					beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("statusReportTimeLong",
					endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		criteria.addOrder(Order.desc("statusReportTimeLong"));
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
	 * 通过时间和boxID查询硬件状态信息
	 * 
	 * @param begainTime
	 * @param endTime
	 * @param boxID
	 * @return
	 */
	public List<Object> queryByTimeAndBoxId(Date begainTime, Date endTime,
			String boxID) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				StatusReport.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("statusReportTimeLong",
					begainTime.getTime()));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("statusReportTimeLong",
					endTime.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		if (null != boxID) {
			criteria.add(Restrictions.eq("boxId", boxID.trim()));
		}
		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;
	}
}
