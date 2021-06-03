package com.datang.dao.monitor;

import java.util.ArrayList;
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
import com.datang.domain.monitor.RealtimeAlarm;
import com.datang.web.beans.monitor.AlarmRequestBean;

/**
 * @explain
 * @name AlarmDao
 * @author shenyanwei
 * @date 2016年7月12日下午2:27:27
 */
@Repository
@SuppressWarnings("all")
public class AlarmDao extends GenericHibernateDao<RealtimeAlarm, Long> {
	/**
	 * 多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				RealtimeAlarm.class);
		AlarmRequestBean pageParams = (AlarmRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选 UE编号（模块号）
		Integer ueNo = pageParams.getUeNo();
		if (null != ueNo) {
			criteria.add(Restrictions.eq("channelNo", ueNo));
		}
		// 筛选告警代码
		Integer alarmCode = pageParams.getAlarmCode();
		if (null != alarmCode) {
			criteria.add(Restrictions.eq("alarmCode", alarmCode));
		}
		// 筛选发生时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("alarmTimeLong", beginDate.getTime()));
		}
		// 筛选结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("alarmTimeLong", endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		criteria.addOrder(Order.desc("alarmTimeLong"));
		long total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
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

	/**
	 * 通过时间和boxID查询告警信息
	 * 
	 * @param begainTime
	 * @param endTime
	 * @param boxID
	 * @return
	 */
	public List<Object> queryByTimeAndBoxId(Date begainTime, Date endTime,
			String boxID) {

		Criteria criteria = this.getHibernateSession().createCriteria(
				RealtimeAlarm.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("alarmTimeLong", begainTime.getTime()));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("alarmTimeLong", endTime.getTime()));
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
	 * 通过时间和boxID查询GPS未知异常告警
	 * 
	 * @param begainTime
	 * @param endTime
	 * @param boxID
	 * @return
	 */
	public List<RealtimeAlarm> queryGPSByTimeAndBoxId(Date begainTime,
			Date endTime, String boxID) {

		Criteria criteria = this.getHibernateSession().createCriteria(
				RealtimeAlarm.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("alarmTimeLong", begainTime.getTime()));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("alarmTimeLong", endTime.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		if (null != boxID) {
			criteria.add(Restrictions.eq("boxId", boxID.trim()));
		}
		// 筛选参数boxid确认权限范围的数据
		List<Integer> alarmCodeList = new ArrayList<>();
		alarmCodeList.add(4099);
		alarmCodeList.add(4608);
		criteria.add(Restrictions.in("alarmCode", alarmCodeList));
		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;
	}

}
