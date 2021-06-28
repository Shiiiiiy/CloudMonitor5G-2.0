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
import com.datang.domain.monitor.RealtimeEvent;
import com.datang.web.beans.monitor.EventRequestBean;

/**
 * 事件Dao
 * 
 * @explain
 * @name EventDao
 * @author shenyanwei
 * @date 2016年7月12日下午2:31:40
 */
@Repository
@SuppressWarnings("all")
public class EventDao extends GenericHibernateDao<RealtimeEvent, Long> {

	public long getPageItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				RealtimeEvent.class);
		EventRequestBean pageParams = (EventRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选模块编号
		Integer ueNo = pageParams.getUeNo();
		if (null != ueNo) {
			criteria.add(Restrictions.eq("channelNo", ueNo));
		}
		// 筛选开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("eventTimeLong", beginDate.getTime()));
		}
		// 筛选结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("eventTimeLong", endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		// 筛选事件代码
		Integer eventCode = pageParams.getEventCode();
		if (eventCode != null) {
			criteria.add(Restrictions.eq("eventCode", eventCode));
		}
		long total =0;
		criteria.setProjection(null);
		criteria.setFirstResult(0);
		total =  (Long) criteria.setProjection(Projections.rowCount())
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
				RealtimeEvent.class);
		EventRequestBean pageParams = (EventRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选模块编号
		Integer ueNo = pageParams.getUeNo();
		if (null != ueNo) {
			criteria.add(Restrictions.eq("channelNo", ueNo));
		}
		// 筛选开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("eventTimeLong", beginDate.getTime()));
		}
		// 筛选结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("eventTimeLong", endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		// 筛选事件代码
		Integer eventCode = pageParams.getEventCode();
		if (eventCode != null) {
			criteria.add(Restrictions.eq("eventCode", eventCode));
		}
		criteria.addOrder(Order.desc("eventTimeLong"));
		criteria.addOrder(Order.asc("boxId"));
		long total =0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		total =  getPageItemCount(pageList);
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

	/**
	 * 通过时间和boxID查询事件信息
	 * 
	 * @param begainTime
	 * @param endTime
	 * @param boxID
	 * @return
	 */
	public List<Object> queryByTimeAndBoxId(Date begainTime, Date endTime,
			String boxID) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				RealtimeEvent.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("eventTimeLong", begainTime.getTime()));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("eventTimeLong", endTime.getTime()));
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

	/**
	 * 通过时间和boxID查询事件信息
	 * 
	 * @param begainTime
	 * @param endTime
	 * @param boxID
	 * @return
	 */
	public List<RealtimeEvent> queryByGalleryIDsAndBoxIds(Date begainDate,
			Date endDate, List<Integer> galleryIDs, List<String> boxIDs) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				RealtimeEvent.class);
		// 筛选发生时间
		if (null != begainDate) {
			criteria.add(Restrictions.ge("eventTimeLong", begainDate.getTime()));
		}
		// 筛选结束时间
		if (null != endDate) {
			criteria.add(Restrictions.le("eventTimeLong", endDate.getTime()));
		}
		// 筛选通道号
		if (null != galleryIDs && 0 != galleryIDs.size()) {
			criteria.add(Restrictions.in("channelNo", galleryIDs));
		}
		// 筛选参数boxid确认权限范围的数据
		if (null != boxIDs && 0 != boxIDs.size()) {
			criteria.add(Restrictions.in("boxId", boxIDs));
		}
		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;

	}
}
