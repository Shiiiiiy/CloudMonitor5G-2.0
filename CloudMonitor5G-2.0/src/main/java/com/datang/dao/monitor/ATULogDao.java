package com.datang.dao.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.monitor.ATULoginLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.web.beans.monitor.ATULoginRequestBean;

/**
 * 终端ATU Dao
 * 
 * @explain
 * @name ATULogDao
 * @author shenyanwei
 * @date 2016年7月11日下午1:12:27
 */
@Repository
@SuppressWarnings("all")
public class ATULogDao extends GenericHibernateDao<ATULoginLogItem, Long> {
	/**
	 * 多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				ATULoginLogItem.class);
		ATULoginRequestBean pageParams = (ATULoginRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选登陆时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("loginTimeLong", beginDate.getTime()));
		}
		// 筛选退出时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("offlineTimeLong", endDate.getTime()));
		}

		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {

			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		long total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		criteria.addOrder(Order.desc("loginTimeLong"));
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
	 * 通过时间和boxID查询登陆信息
	 * 
	 * @param begainTime
	 * @param endTime
	 * @param boxID
	 * @return
	 */
	public List<ATULoginLogItem> queryByTimeAndBoxId(Date begainTime,
			Date endTime, List<String> boxID) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				ATULoginLogItem.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("loginTimeLong", begainTime.getTime()));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("offlineTimeLong", endTime.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		if (null != boxID) {
			criteria.add(Restrictions.in("boxId", boxID));
		}
		criteria.addOrder(Order.asc("loginTimeLong"));
		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;

	}

	/**
	 * 增加终端登录日志记录
	 * 
	 * @param atuItem
	 *            日志记录
	 */
	public void addATULoginLog(ATULoginLogItem atuItem) {
		create(atuItem);
	}

	/**
	 * 删除终端登录日志记录
	 * 
	 * @param atuItem
	 *            日志记录
	 */
	public void deleteATULoginLog(ATULoginLogItem atuItem) {
		delete(atuItem);
	}

	/**
	 * 更新终端登录日志记录
	 * 
	 * @param atuItem
	 *            日志记录
	 */
	public void updateATULoginLog(ATULoginLogItem atuItem) {
		update(atuItem);
	}

	/**
	 * 更新终端登录日志记录
	 * 
	 * @param id
	 *            日志记录的主键值
	 * @return 日志记录
	 */
	public ATULoginLogItem queryATULoginLog(Long id) {
		return find(id);
	}

	/**
	 * 
	 * 获取指定终端的离指定时间最近的登录记录
	 * 
	 * @param terminal
	 *            终端实体
	 * @param currentDate
	 *            指定的时间
	 * @return 找到的登录记录
	 */
	public ATULoginLogItem queryNearestATULoginItem(Terminal terminal,
			Date currentDate) {
		return queryNearestATULoginItem(terminal.getBoxId(), currentDate);
	}

	/**
	 * 获取指定终端的离指定时间最近的登录记录
	 * 
	 * @param boxId
	 *            终端Id
	 * @param currentDate
	 *            指定的时间
	 * @return 找到的登录记录
	 */
	@SuppressWarnings("unchecked")
	public ATULoginLogItem queryNearestATULoginItem(String boxId,
			Date currentDate) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				ATULoginLogItem.class);

		// 终端
		if (null != boxId) {
			Criteria terminalCriteria = criteria.createCriteria("terminal");
			terminalCriteria.add(Restrictions.eq("boxId", boxId));
		}
		if (null != currentDate) {
			criteria.add(Restrictions.le("loginDate", currentDate));
		}
		criteria.addOrder(Order.desc("loginDate"));
		criteria.setMaxResults(1);
		List<ATULoginLogItem> result = criteria.list();

		return (result == null || result.size() == 0) ? null : result.get(0);
	}

	/**
	 * 获取指定终端的离指定时间最近的登录记录
	 * 
	 * @param boxId
	 *            终端Id
	 * @param currentDate
	 *            指定的时间
	 * @return 找到的登录记录
	 */
	@SuppressWarnings("unchecked")
	public ATULoginLogItem queryNearestATULoginItem(Long terminalId,
			Date currentDate) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				ATULoginLogItem.class);

		// 终端
		if (null != terminalId) {
			Criteria terminalCriteria = criteria.createCriteria("terminal");
			terminalCriteria.add(Restrictions.eq("id", terminalId));
		}
		if (null != currentDate) {
			criteria.add(Restrictions.le("loginDate", currentDate));
		}
		criteria.addOrder(Order.desc("loginDate"));
		criteria.setMaxResults(1);
		List<ATULoginLogItem> result = criteria.list();

		return (result == null || result.size() == 0) ? null : result.get(0);
	}

	/**
	 * 
	 * @param terminalName
	 * @param maxLogNum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ATULoginLogItem> refreshATULoginItems(String terminalName,
			String maxLogNum, List<Long> managedTerminalIDs) {
		if (managedTerminalIDs == null || managedTerminalIDs.size() == 0) {
			return new ArrayList<ATULoginLogItem>();
		}

		Criteria criteria = this.getHibernateSession().createCriteria(
				ATULoginLogItem.class);
		Criteria terminalCriteria = criteria.createCriteria("terminal");

		// name
		if (StringUtils.hasText(terminalName)) {
			terminalCriteria.add(Restrictions.eq("name", terminalName));
		}

		terminalCriteria.add(Restrictions.in("id", managedTerminalIDs));

		criteria.addOrder(Order.desc("loginDate"));
		criteria.setMaxResults(Integer.parseInt(maxLogNum));
		List<ATULoginLogItem> result = criteria.list();
		// int logNum = Math.min(result.size(), Integer.parseInt(maxLogNum));
		// return result.subList(0, logNum);
		return result;
	}

	/**
	 * @param id
	 */
	public void deleteByTerminalId(Long id) {
		Query deleteQuery = getHibernateSession().createQuery(
				"delete ATULoginLogItem al where al.terminal.id=:terminalId");
		deleteQuery.setLong("terminalId", id);
		deleteQuery.executeUpdate();

	}

	/**
	 * @param ids
	 */
	public void deleteByTerminalIds(Long[] ids) {
		Query deleteQuery = getHibernateSession().createQuery(
				"delete  ATULoginLogItem al where al.terminal.id in (:ids)");
		deleteQuery.setParameterList("ids", ids);
		deleteQuery.executeUpdate();
	}

	/**
	 * 查询最近一段时间内的终端是否活跃
	 */
	public ATULoginLogItem getActiveNUm(Date pastDate,String id) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				ATULoginLogItem.class);
		
//		// 终端
//		if (null != terminalId) {
//			Criteria terminalCriteria = criteria.createCriteria("terminal");
//			terminalCriteria.add(Restrictions.eq("id", terminalId));
//		}
		long time = pastDate.getTime();
		if (null != pastDate) {
			criteria.add(Restrictions.ge("loginTimeLong", time));
		}
		criteria.add(Restrictions.eq("boxId", id));
		criteria.addOrder(Order.desc("loginTimeLong"));
		criteria.setMaxResults(1);
		List<ATULoginLogItem> result = criteria.list();

		return (result == null || result.size() == 0) ? null : result.get(0);
		
	}
}
