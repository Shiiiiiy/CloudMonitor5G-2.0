/**
 * 
 */
package com.datang.dao.testLogItem;

import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.web.beans.testLogItem.TestLogItemPageQueryRequestBean;

/**
 * 测试日志---dao
 * 
 * @author yinzhipeng
 * @date:2015年10月30日 下午1:23:31
 * @version
 */
@Repository
@SuppressWarnings("all")
public class TestLogItemDao extends GenericHibernateDao<TestLogItem, Long> {

	public long getPageTestLogItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
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
		Boolean isFinished = pageParams.getIsFinished();
		Integer testFileStatus = pageParams.getTestFileStatus();
		if (null == isFinished || !isFinished) {
			// 查询未上传完成的日志
			criteria.add(Restrictions.eqOrIsNull("testFileStatus", 0));
		} else {
			// 查询已经上传完成的日志,解析或者未解析完成的日志
			if (null != testFileStatus) {
				criteria.add(Restrictions.eq("testFileStatus", testFileStatus));
			} else {
				criteria.add(Restrictions.or(
						Restrictions.eq("testFileStatus", 1),
						Restrictions.eq("testFileStatus", 2)));
			}
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
	public EasyuiPageList getPageTestLogItem(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
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
		Boolean isFinished = pageParams.getIsFinished();
		Integer testFileStatus = pageParams.getTestFileStatus();
		if (null == isFinished || !isFinished) {
			// 查询未上传完成的日志
			criteria.add(Restrictions.eqOrIsNull("testFileStatus", 0));
		} else {
			// 查询已经上传完成的日志,解析或者未解析完成的日志
			if (null != testFileStatus) {
				criteria.add(Restrictions.eq("testFileStatus", testFileStatus));
			} else {
				criteria.add(Restrictions.or(
						Restrictions.eq("testFileStatus", 1),
						Restrictions.eq("testFileStatus", 2)));
			}
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
	public List<TestLogItem> getTestLogItems(List<Long> testLogIds) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == testLogIds) {
			return testLogItems;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
	//	criteria.add(Restrictions.in("recSeqNo", testLogIds));
		criteria.add(Restrictions.or(getSetOr("recSeqNo",new HashSet<>(testLogIds))));
		testLogItems = criteria.list();
		return testLogItems;
	}


	public  Criterion[] getSetOr(String propertyName, Set<Long> set){
		int stemp = 900;
		int all = set.size();
		List<Criterion> list = new ArrayList<>();
		if(set.size()>stemp){
			Set<Long> idTemp = new HashSet<>();
			int i = 0;
			for(Long s:set){
				i++;
				idTemp.add(s);
				if(i>=stemp){
					i =0;
					list.add(Restrictions.in(propertyName, new HashSet(idTemp)));
					idTemp.clear();
					all = all -stemp;
				}else{

				}

			}
			if(idTemp.size()>0){
				list.add(Restrictions.in(propertyName, new HashSet(idTemp)));
			}
		}else {
			list.add(Restrictions.in(propertyName, new HashSet(set)));
		}
		Criterion[] arr = new Criterion[list.size()];
		for(int i=0;i<list.size();i++){
			arr[i] = list.get(i);
		}

		return arr;
	}



	/**
	 * 根据日志链接查询日志
	 * @param filename
	 * @return
	 */
	public TestLogItem queryTestLogByLogName(String filename){
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
		criteria.add(Restrictions.eq("filelink", filename));
		TestLogItem testLogItems = (TestLogItem)criteria.uniqueResult();
		return testLogItems;
	}

	/**
	 * 根据BoxID集合查询
	 * 
	 * @param boxIds
	 * @return
	 */
	public List<TestLogItem> getTestLogItemsByBoxIds(List<String> boxIds) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == boxIds) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
		criteria.add(Restrictions.in("boxId", boxIds));
		testLogItems = criteria.list();
		return testLogItems;
	}

	/**
	 * 根据BoxId、测试域以及开始时间、结束时间查询
	 * 
	 * @param boxIds
	 * @return
	 */
	public List<TestLogItem> getTestLogItemsByOther(List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			Date beginDate, Date endDate) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == boxIds) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
		// 筛选参数日志开始时间
		if (null != beginDate) {
			criteria.add(Restrictions.ge("startDateLong", beginDate.getTime()));

		}
		// 筛选参数日志结束时间
		if (null != endDate) {
			criteria.add(Restrictions.le("endDateLong", endDate.getTime()));

		}
		// 筛选参数boxid确认权限范围的数据

		if (null != boxIds && 0 != boxIds.size()) {
			criteria.add(Restrictions.in("boxId", boxIds));
		}
		criteria.add(Restrictions.isNotNull("startDateLong"));
		criteria.add(Restrictions.isNotNull("endDateLong"));
		criteria.add(Restrictions.ne("startDateLong", new Long(0)));
		criteria.add(Restrictions.ne("endDateLong", new Long(0)));
		// 筛选参数测试域确认权限范围的数据
		/*
		 * if (null != terminalGroup && 0 != terminalGroup.size()) {
		 * criteria.add(Restrictions.in("terminalGroup", terminalGroup)); }
		 */
		// 筛选参数测试级别
		if (null != testRankList && 0 != testRankList.size()) {
			criteria.add(Restrictions.in("testLevel", testRankList));
		}

		testLogItems = criteria.list();
		return testLogItems;
	}

	/**
	 * 根据BoxId、测试域以及开始时间、结束时间查询CQT日志
	 * 
	 * @param boxIds
	 * @return
	 */
	public List<TestLogItem> getCQTTestLogItemsByOther(List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			Date beginDate, Date endDate) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == boxIds) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
		// 筛选参数日志开始时间
		if (null != beginDate) {
			criteria.add(Restrictions.ge("startDateLong", beginDate.getTime()));
		}
		// 筛选参数日志结束时间
		if (null != endDate) {
			criteria.add(Restrictions.le("endDateLong", endDate.getTime()));
		}
		// 筛选参数boxid确认权限范围的数据

		if (null != boxIds && 0 != boxIds.size()) {
			criteria.add(Restrictions.in("boxId", boxIds));
		}
		criteria.add(Restrictions.isNotNull("startDateLong"));
		criteria.add(Restrictions.isNotNull("endDateLong"));
		criteria.add(Restrictions.ne("startDateLong", new Long(0)));
		criteria.add(Restrictions.ne("endDateLong", new Long(0)));
		// 筛选日志为CQT日志
		criteria.add(Restrictions.eq("logSource", 1));
		// 筛选参数测试级别
		if (null != testRankList && 0 != testRankList.size()) {
			criteria.add(Restrictions.in("testLevel", testRankList));
		}

		testLogItems = criteria.list();
		return testLogItems;
	}

	/**
	 * 根据楼宇名称查询日志
	 * 
	 * @param floorName
	 * @return
	 */
	public List<TestLogItem> getCQTTestLogItemsByFloorName(String floorName) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == floorName) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);

		// 筛选日志为CQT日志
		criteria.add(Restrictions.eq("logSource", 1));
		// 筛选参数测试级别

		criteria.add(Restrictions.eq("floorName", floorName));

		testLogItems = criteria.list();
		return testLogItems;
	}

	/**
	 * 根据日志名查询采样点数据
	 * @author maxuancheng
	 * date:2020年5月15日 下午4:18:37
	 * @param allLogNames
	 * @return
	 */
	public List<StationSAMTralPojo> getGpsPointData(String allLogNames) {
		String[] names = allLogNames.split(",");
		Criteria criteria = this.getHibernateSession().createCriteria(StationSAMTralPojo.class);
		criteria.add(Restrictions.in("nrLogname", names));
		return criteria.list();
	}
	
	/**
	 * 根据boxid和日志名称集合查询CQT日志
	 * @author lucheng
	 * @date 2020年8月19日 下午5:02:27
	 * @param boxid
	 * @param logfileNames
	 * @return
	 */
	public List<TestLogItem> queryTestLogItemsByLogName(String boxid, List<String> logfileNames){
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == boxid && 0 == logfileNames.size()) {
			return testLogItems;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItem.class);
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
