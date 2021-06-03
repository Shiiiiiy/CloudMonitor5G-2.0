/**
 * 
 */
package com.datang.dao.testLogItem;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testLogItem.TestLogItemSignalling;

/**
 * TestLogItem全量信令详情Dao
 * 
 * @author yinzhipeng
 * @date:2016年5月9日 上午9:22:08
 * @version
 */
@Repository
@SuppressWarnings("all")
public class TestLogItemSignallingDao extends
		GenericHibernateDao<TestLogItemSignalling, Long> {

	/**
	 * 根据pageList获取信令详情分页信息
	 * 
	 * @param pageList
	 *            中的param中须包含recseqno(TestLogItem的id,类型Long)和startTime(Long类型)
	 *            和endTime(Long类型)
	 * @return
	 */
	public EasyuiPageList getPageSignalling(PageList pageList) {

		if (null == pageList || null == pageList.getParam("recseqno")) {
			return new EasyuiPageList();
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemSignalling.class);

		if (null != pageList.getParam("recseqno")) {
			Criteria createCriteria = criteria.createCriteria("testLogItem");
			createCriteria.add(Restrictions.eq("recSeqNo",
					pageList.getParam("recseqno")));
		}
		if (null != pageList.getParam("startTime")) {
			criteria.add(Restrictions.ge("time", pageList.getParam("startTime")));
		}
		if (null != pageList.getParam("endTime")) {
			criteria.add(Restrictions.le("time", pageList.getParam("endTime")));
		}
		criteria.addOrder(Order.asc("time"));

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
		for (Object object : list) {

		}
		return easyuiPageList;
	}

}
