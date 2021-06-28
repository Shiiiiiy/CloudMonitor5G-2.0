/**
 * 
 */
package com.datang.dao.testLogItem;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.testLogItem.NetworkTestLogItem;
import com.datang.web.beans.testLogItem.TestLogItemPageQueryRequestBean;

/**
 * 网络侧日志----dao
 * 
 * @author yinzhipeng
 * @date:2017年2月6日 下午1:10:37
 * @version
 */
@Repository
@SuppressWarnings("all")
public class NetworkTestLogItemDao extends
		GenericHibernateDao<NetworkTestLogItem, Long> {

	public long getPageTestLogItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				NetworkTestLogItem.class);
		TestLogItemPageQueryRequestBean pageParams = (TestLogItemPageQueryRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("startDateLong", beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("endDateLong", endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		// 筛选参数日志文件名
		String fileName = pageParams.getFileName();
		if (StringUtils.hasText(fileName)) {
			criteria.add(Restrictions.like("fileName", fileName.trim(),
					MatchMode.ANYWHERE));
		}
		// 筛选参数日志通道号
		Set<String> galleryNo = pageParams.getGalleryNoList();
		if (null != galleryNo && 0 != galleryNo.size()) {
			criteria.add(Restrictions.in("moduleNo", galleryNo));
		}
		// 查询已经上传完成的日志,解析或者未解析完成的日志
		Integer testFileStatus = pageParams.getTestFileStatus();
		if (null != testFileStatus) {
			if (2 == testFileStatus) {
				// 上传成功,统计完成
				criteria.add(Restrictions.ge("testFileStatus", testFileStatus));
			} else {
				// 上传成功,待统计或者统计中
				criteria.add(Restrictions.eq("testFileStatus", testFileStatus));
			}
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
	public EasyuiPageList getPageTestLogItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				NetworkTestLogItem.class);
		TestLogItemPageQueryRequestBean pageParams = (TestLogItemPageQueryRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志开始时间
		Date beginDate = pageParams.getBeginDate();
		if (null != beginDate) {
			criteria.add(Restrictions.ge("startDateLong", beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		Date endDate = pageParams.getEndDate();
		if (null != endDate) {
			criteria.add(Restrictions.le("endDateLong", endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据
		Set<String> boxIdsSet = pageParams.getBoxIdsSet();
		if (null != boxIdsSet && 0 != boxIdsSet.size()) {
			criteria.add(Restrictions.in("boxId", boxIdsSet));
		}
		// 筛选参数日志文件名
		String fileName = pageParams.getFileName();
		if (StringUtils.hasText(fileName)) {
			criteria.add(Restrictions.like("fileName", fileName.trim(),
					MatchMode.ANYWHERE));
		}
		// 筛选参数日志通道号
		Set<String> galleryNo = pageParams.getGalleryNoList();
		if (null != galleryNo && 0 != galleryNo.size()) {
			criteria.add(Restrictions.in("moduleNo", galleryNo));
		}
		// 查询已经上传完成的日志,解析或者未解析完成的日志
		Integer testFileStatus = pageParams.getTestFileStatus();
		if (null != testFileStatus) {
			if (2 == testFileStatus) {
				// 上传成功,统计完成
				criteria.add(Restrictions.ge("testFileStatus", testFileStatus));
			} else {
				// 上传成功,待统计或者统计中
				criteria.add(Restrictions.eq("testFileStatus", testFileStatus));
			}
		}
		criteria.addOrder(Order.desc("startDateLong"));
		criteria.addOrder(Order.asc("fileName"));
		long total = 0;
		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		criteria.setFirstResult((pageNum - 1) * rowsCount);
		criteria.setMaxResults(rowsCount);
		List list = criteria.list();
		total = getPageTestLogItemCount(pageList);
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}

	/**
	 * 根据文件名称获取网络侧日志
	 * 
	 * @param fileName
	 * @return
	 */
	public NetworkTestLogItem findByName(String fileName) {
		if (!StringUtils.hasText(fileName)) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				NetworkTestLogItem.class);
		criteria.add(Restrictions.eq("fileName", fileName));
		List list = criteria.list();
		if (null != list && 0 != list.size()) {
			return (NetworkTestLogItem) list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据文件名称获取网络侧日志
	 * 
	 * @param fileName
	 * @return
	 */
	public List<NetworkTestLogItem> findByboxId(String boxId) {
		if (!StringUtils.hasText(boxId)) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				NetworkTestLogItem.class);
		criteria.add(Restrictions.eq("boxId", boxId));
		List list = criteria.list();
		return list;
	}
	
	public List<NetworkTestLogItem> findByParam(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(
				NetworkTestLogItem.class);
		Object fileName = pageList.getParam("fileName");
		Object boxId = pageList.getParam("boxId");
		
		if(StringUtils.hasText((String)fileName)){
			criteria.add(Restrictions.eq("fileName", (String)fileName));
		}
		
		if(StringUtils.hasText((String)boxId)){
			criteria.add(Restrictions.eq("boxId", (String)boxId));
		}
		
		List list = criteria.list();
		if (null != list && 0 != list.size()) {
			return list;
		}
		return null;
	}

}
