package com.datang.dao.testLogItem;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testLogItem.TestLogMeasure;

/**
 * 服务小区和邻区测量详情Dao
 * 
 * @explain
 * @name TestLogMeasureDao
 * @author shenyanwei
 * @date 2016年8月3日下午4:27:59
 */
@Repository
@SuppressWarnings("all")
public class TestLogMeasureDao extends
		GenericHibernateDao<TestLogMeasure, Long> {
	/**
	 * 根据时间分页查询
	 * 
	 * @param
	 * @return
	 */
	public EasyuiPageList getTestLogMeasuresByTimePage(Long begainTime,
			Long endTime, int rows, int page) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogMeasure.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("timeLong", begainTime));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("timeLong", endTime));
		}
		long total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		criteria.addOrder(Order.desc("timeLong"));
		criteria.setProjection(null);
		criteria.setFirstResult((page - 1) * rows);
		criteria.setMaxResults(rows);
		List list = criteria.list();
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");

		return easyuiPageList;
	}

	/**
	 * 根据时间查询
	 * 
	 * @param
	 * @return
	 */
	public List<TestLogMeasure> getTestLogMeasuresByTime(Long begainTime,
			Long endTime) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogMeasure.class);
		// 筛选发生时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("timeLong", begainTime));
		}
		// 筛选结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("timeLong", endTime));
		}
		criteria.addOrder(Order.desc("timeLong"));

		List list = criteria.list();

		return list;
	}
}
