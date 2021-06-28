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
import com.datang.domain.monitor.TerminalInfo;
import com.datang.web.beans.monitor.TerminalInfoRequestBean;

/**
 * 终端信息Dao
 * 
 * @explain
 * @name TerminalInfoDao
 * @author shenyanwei
 * @date 2016年8月15日上午11:22:22
 */
@Repository
@SuppressWarnings("all")
public class TerminalInfoDao extends GenericHibernateDao<TerminalInfo, Long> {

	public long getPageItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalInfo.class);
		TerminalInfoRequestBean pageParams = (TerminalInfoRequestBean) pageList
				.getParam("pageQueryBean");
		// 筛选参数日志开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("deviceInfoTimeLong",
					beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("deviceInfoTimeLong",
					endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		long total = 0;
		criteria.setProjection(null);
		criteria.setFirstResult(0);
		total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return total;
	}	/**
	 * 多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalInfo.class);
		TerminalInfoRequestBean pageParams = (TerminalInfoRequestBean) pageList
				.getParam("pageQueryBean");
		// 筛选参数日志开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("deviceInfoTimeLong",
					beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("deviceInfoTimeLong",
					endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		criteria.addOrder(Order.desc("deviceInfoTimeLong"));
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

}
