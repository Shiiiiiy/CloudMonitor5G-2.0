package com.datang.dao.customTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.customTemplate.CustomUploadLogItemPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.web.beans.testLogItem.TestLogItemPageQueryRequestBean;

/**
 * 自定义上传测试日志---dao
 */
@Repository
@SuppressWarnings("all")
public class CustomReportLogItemDao extends GenericHibernateDao<CustomUploadLogItemPojo, Long> {

	public long getPageTestLogItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomUploadLogItemPojo.class);
		TestLogItemPageQueryRequestBean pageParams = (TestLogItemPageQueryRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志数据来源
		List<Integer> logSource = pageParams.getLogSource();
		if (null != logSource && 0 != logSource.size()) {
			criteria.add(Restrictions.in("logSource", logSource));
		}
		// 筛选参数日志运营商
		List<String> operators = pageParams.getOperators();
		if (null != operators && 0 != operators.size()) {
			criteria.add(Restrictions.in("operatorName", operators));
		}
		// 筛选参数日志业务类型
		List<Integer> serviceType = pageParams.getServiceType();
		if (null != serviceType && 0 != serviceType.size()) {
			Disjunction dis = Restrictions.disjunction();
			for (Integer integer : serviceType) {
				dis.add(Restrictions.like("serviceType", integer + ",",
						MatchMode.ANYWHERE));
			}
			criteria.add(dis);
		}
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
		// 筛选参数是否上传完成
		Integer testFileStatus = pageParams.getTestFileStatus();
		// 查询解析或者未解析完成的日志
		if (null != testFileStatus) {
			criteria.add(Restrictions.eq("testFileStatus", testFileStatus));
		}
		
		// 在对比分析中存放已经选中的日志,后台查询应该排除这些结果集
		List<Long> selectTestLogItemIds = pageParams.getSelectTestLogItemIds();
		if (null != selectTestLogItemIds && 0 != selectTestLogItemIds.size()) {
			criteria.add(Restrictions.not(Restrictions.in("recSeqNo",
					selectTestLogItemIds)));
		}
		criteria.add(Restrictions.or(Restrictions.eq("deleteTag", 0), Restrictions.isNull("deleteTag")));

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
				CustomUploadLogItemPojo.class);
		TestLogItemPageQueryRequestBean pageParams = (TestLogItemPageQueryRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志数据来源
		List<Integer> logSource = pageParams.getLogSource();
		if (null != logSource && 0 != logSource.size()) {
			criteria.add(Restrictions.in("logSource", logSource));
		}
		// 筛选参数日志运营商
		List<String> operators = pageParams.getOperators();
		if (null != operators && 0 != operators.size()) {
			criteria.add(Restrictions.in("operatorName", operators));
		}
		// 筛选参数日志业务类型
		List<Integer> serviceType = pageParams.getServiceType();
		if (null != serviceType && 0 != serviceType.size()) {
			Disjunction dis = Restrictions.disjunction();
			for (Integer integer : serviceType) {
				dis.add(Restrictions.like("serviceType", integer + ",",
						MatchMode.ANYWHERE));
			}
			criteria.add(dis);
		}
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
		// 筛选参数是否上传完成
		Integer testFileStatus = pageParams.getTestFileStatus();
		// 查询解析或者未解析完成的日志
		if (null != testFileStatus) {
			criteria.add(Restrictions.eq("testFileStatus", testFileStatus));
		}
		
		// 在对比分析中存放已经选中的日志,后台查询应该排除这些结果集
		List<Long> selectTestLogItemIds = pageParams.getSelectTestLogItemIds();
		if (null != selectTestLogItemIds && 0 != selectTestLogItemIds.size()) {
			criteria.add(Restrictions.not(Restrictions.in("recSeqNo",
					selectTestLogItemIds)));
		}
		criteria.add(Restrictions.or(Restrictions.eq("deleteTag", 0), Restrictions.isNull("deleteTag")));
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
	 * 根据ID集合查询
	 * 
	 * @param testLogIds
	 * @return
	 */
	public List<CustomUploadLogItemPojo> getTestLogItems(List<Long> testLogIds) {
		List<CustomUploadLogItemPojo> testLogItems = new ArrayList<>();
		if (null == testLogIds) {
			return testLogItems;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomUploadLogItemPojo.class);
		criteria.add(Restrictions.in("recSeqNo", testLogIds));
		testLogItems = criteria.list();
		return testLogItems;
	}
	
	/**
	 * 根据日志链接查询日志
	 * @param filename
	 * @return
	 */
	public CustomUploadLogItemPojo queryTestLogByLogName(String fileLink){
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomUploadLogItemPojo.class);
		criteria.add(Restrictions.eq("filelink", fileLink));
		CustomUploadLogItemPojo testLogItems = (CustomUploadLogItemPojo)criteria.uniqueResult();
		return testLogItems;
	}

	/**
	 * 根据BoxID集合查询
	 * 
	 * @param boxIds
	 * @return
	 */
	public List<CustomUploadLogItemPojo> getTestLogItemsByBoxIds(List<String> boxIds) {
		List<CustomUploadLogItemPojo> testLogItems = new ArrayList<>();
		if (null == boxIds) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomUploadLogItemPojo.class);
		criteria.add(Restrictions.in("boxId", boxIds));
		testLogItems = criteria.list();
		return testLogItems;
	}

	
	/**
	 * 根据boxid和日志名称集合查询CQT日志
	 * @author lucheng
	 * @date 2020年8月19日 下午5:02:27
	 * @param boxid
	 * @param logfileNames
	 * @return
	 */
	public List<CustomUploadLogItemPojo> queryTestLogItemsByLogName(String boxid, List<String> logfileNames){
		List<CustomUploadLogItemPojo> testLogItems = new ArrayList<>();
		if (null == boxid && 0 == logfileNames.size()) {
			return testLogItems;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				CustomUploadLogItemPojo.class);
		// 筛选参数boxid确认权限范围的数据
		criteria.add(Restrictions.eq("boxId", boxid));
		
//		criteria.add(Restrictions.isNotNull("startDateLong"));
//		criteria.add(Restrictions.isNotNull("endDateLong"));
//		criteria.add(Restrictions.ne("startDateLong", new Long(0)));
		
		criteria.add(Restrictions.in("fileName", logfileNames));

		testLogItems = criteria.list();
		return testLogItems;
	}
}
