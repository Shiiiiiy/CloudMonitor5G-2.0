/**
 * 
 */
package com.datang.dao.testLogItem;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.common.util.MapUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.UnicomLogItem;
import com.datang.exception.ApplicationException;
import com.datang.web.action.testLogItem.UnicomLogItemAction;
import com.datang.web.beans.testLogItem.TestLogItemPageQueryRequestBean;
import com.datang.web.beans.testLogItem.UnicomLogItemPageQueryRequestBean;
import com.mchange.v2.lang.ObjectUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.omg.CORBA.TIMEOUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 测试日志---dao
 * 
 * @author yinzhipeng
 * @date:2015年10月30日 下午1:23:31
 * @version
 */
@Repository
@SuppressWarnings("all")
public class UnicomLogItemDao extends GenericHibernateDao<UnicomLogItem, Long> {


	private static Logger LOGGER = LoggerFactory
			.getLogger(UnicomLogItemAction.class);


	private static final String BOX_ID = "BOX_ID";
	private static final String FILE_NAME = "FILE_NAME";
	private static final String EVENTTYPE = "EVENTTYPE";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String LONGITUDE = "LONGITUDE";
	private static final String LATITUDE = "LATITUDE";


	@Resource
	private JdbcTemplate jdbcTemplate;

	public long getPageTestLogItemCount(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				UnicomLogItem.class);
		UnicomLogItemPageQueryRequestBean pageParams = (UnicomLogItemPageQueryRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志数据来源
		List<Integer> logSource = pageParams.getLogSource();
		if (null != logSource && 0 != logSource.size()) {
			criteria.add(Restrictions.in("logSource", logSource));
		}


		// 省
		String prov = pageParams.getProv();
		if (StringUtils.hasText(prov)) {
			criteria.add(Restrictions.eq("prov", prov));
		}
		// 市
		String city = pageParams.getCity();
		if (StringUtils.hasText(city)) {
			criteria.add(Restrictions.eq("city", city));
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
		Set<String> cityNameSet = pageParams.getCityName();
		if (null != cityNameSet && 0 != cityNameSet.size()) {
			criteria.add(Restrictions.in("city", cityNameSet));
		}
		// 筛选id
		Set<Long> idSet = pageParams.getIds();


		if (null != idSet && 0 != idSet.size()) {
			//criteria.add(Restrictions.in("recSeqNo", idSet));
			criteria.add(Restrictions.or(getSetOr("recSeqNo",idSet)));
		}


		// 筛选参数日志文件名
		String fileName = pageParams.getFileName();
		if (StringUtils.hasText(fileName)) {
			criteria.add(Restrictions.like("fileName", fileName.trim(),
					MatchMode.ANYWHERE));
		}

		// 筛选参数日志文件名
		List<String> fileNameList = pageParams.getFileNameList();
		if (fileNameList!=null && fileNameList.size()>0) {
			criteria.add(Restrictions.in("fileName", fileNameList));
		}

		// 筛选测试名称
		String testName = pageParams.getTestName();
		if (StringUtils.hasText(testName)) {
			criteria.add(Restrictions.like("testName", testName.trim(),
					MatchMode.ANYWHERE));
		}

		// 筛选测试名称
		String taskName = pageParams.getTaskName();
		if (StringUtils.hasText(taskName)) {
			criteria.add(Restrictions.like("taskName", taskName.trim(),
					MatchMode.ANYWHERE));
		}

		// 筛选测试名称
		List<String> md5Checks = pageParams.getMd5Check();
		if (null != md5Checks && 0 != md5Checks.size()) {
			criteria.add(Restrictions.in("md5Check", md5Checks));
		}


		// 筛选测试名称
		List<String> trafficCheck = pageParams.getTrafficCheck();
		if (null != trafficCheck && 0 != trafficCheck.size()) {
			criteria.add(Restrictions.in("trafficCheck", trafficCheck));
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
	//	int rowsCount = pageList.getRowsCount();// 每页记录数
	//	int pageNum = pageList.getPageNum();// 页码
	//	criteria.setFirstResult((pageNum - 1) * rowsCount);
	//	criteria.setMaxResults(rowsCount);
	//	List list = criteria.list();
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
				UnicomLogItem.class);
		UnicomLogItemPageQueryRequestBean pageParams = (UnicomLogItemPageQueryRequestBean) pageList
				.getParam("pageQueryBean");

		// 筛选参数日志数据来源
		List<Integer> logSource = pageParams.getLogSource();
		if (null != logSource && 0 != logSource.size()) {
			criteria.add(Restrictions.in("logSource", logSource));
		}


		// 省
		String prov = pageParams.getProv();
		if (StringUtils.hasText(prov)) {
			criteria.add(Restrictions.eq("prov", prov));
		}
		// 市
		String city = pageParams.getCity();
		if (StringUtils.hasText(city)) {
			criteria.add(Restrictions.eq("city", city));
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
		Set<String> cityNameSet = pageParams.getCityName();
		if (null != cityNameSet && 0 != cityNameSet.size()) {
			criteria.add(Restrictions.in("city", cityNameSet));
		}
		// 筛选id
		Set<Long> idSet = pageParams.getIds();


		if (null != idSet && 0 != idSet.size()) {
			//criteria.add(Restrictions.in("recSeqNo", idSet));
			criteria.add(Restrictions.or(getSetOr("recSeqNo",idSet)));
		}


		// 筛选参数日志文件名
		String fileName = pageParams.getFileName();
		if (StringUtils.hasText(fileName)) {
			criteria.add(Restrictions.like("fileName", fileName.trim(),
					MatchMode.ANYWHERE));
		}

		// 筛选参数日志文件名
		List<String> fileNameList = pageParams.getFileNameList();
		if (fileNameList!=null && fileNameList.size()>0) {
			criteria.add(Restrictions.in("fileName", fileNameList));
		}

		// 筛选测试名称
		String testName = pageParams.getTestName();
		if (StringUtils.hasText(testName)) {
			criteria.add(Restrictions.like("testName", testName.trim(),
					MatchMode.ANYWHERE));
		}

		// 筛选测试名称
		String taskName = pageParams.getTaskName();
		if (StringUtils.hasText(taskName)) {
			criteria.add(Restrictions.like("taskName", taskName.trim(),
					MatchMode.ANYWHERE));
		}

		// 筛选测试名称
		List<String> md5Checks = pageParams.getMd5Check();
		if (null != md5Checks && 0 != md5Checks.size()) {
			criteria.add(Restrictions.in("md5Check", md5Checks));
		}


		// 筛选测试名称
		List<String> trafficCheck = pageParams.getTrafficCheck();
		if (null != trafficCheck && 0 != trafficCheck.size()) {
			criteria.add(Restrictions.in("trafficCheck", trafficCheck));
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


	public  Criterion[] getSetOr(String propertyName,Set<Long> set){
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
	public List<UnicomLogItem> getTestLogItemsByOther(String prov,String city,List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			String filename,
			Date beginDate, Date endDate) {
		List<UnicomLogItem> testLogItems = new ArrayList<>();
		if (null == boxIds) {
			return null;
		}
		Criteria criteria = this.getHibernateSession().createCriteria(
				UnicomLogItem.class);


		// 筛选省
		if (null != prov && StringUtils.hasText(prov)) {
			criteria.add(Restrictions.eq("prov", prov));
		}

		// 筛选市
		if (null != city && StringUtils.hasText(city)) {
			criteria.add(Restrictions.eq("city", city));
		}
		if (null != filename && StringUtils.hasText(filename)) {
			criteria.add(Restrictions.like("fileName", filename,MatchMode.ANYWHERE));
		}

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

		criteria.addOrder((Order.asc("fileName")));


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

	/**
	 * 数据概览KPI计算
	 * 直接导出
	 * */
	public List<Map<String, Object>> dataOverview(String idStr) {
		List<Map<String, Object>> list = new ArrayList<>();
		list.addAll(dataOverviewSub(idStr,false));
		list.addAll(dataOverviewSub(idStr,true));
		return list;
	}


	/**
	 * 数据概览KPI计算
	 * 直接导出
	 * */
	public List<Map<String, Object>> dataOverviewSub(String idStr,boolean collect) {
		String innerJoin = "INNER JOIN (SELECT TASK_NAME,FILE_NAME FROM IADS_TESTLOG_ITEM WHERE RECSEQNO IN (" + idStr + ") )log ON tt.LOG_NAME=LOG.FILE_NAME AND tt.TASK_NAME=LOG.TASK_NAME\n";
		String groupby = "group by TASK_NAME, LOG_NAME";
		String nickName = "TASK_NAME,LOG_NAME";
		if(collect){
			groupby = "";
			nickName = "'汇总' as  TASK_NAME,'汇总' as  LOG_NAME";
		}
		String sqlTrue =
				"select \n" +
						"TASK_NAME,LOG_NAME,\n" +
						"ROUND(CAST((CASE WHEN KPI2097 = 0 then 0 else KPI2101/KPI2097*100 end) AS NUMERIC) ,2)as KK01,\n" +
						"ROUND(CAST((CASE WHEN KPI2015 = 0 then 0 else KPI3400/KPI2015*100 end) AS NUMERIC) ,2)as KK02,\n" +
						"ROUND(CAST((CASE WHEN KPI2015 = 0 then 0 else 0/KPI2015*100 end) AS NUMERIC) ,2)as KK03,\n" +
						"ROUND(CAST((CASE WHEN KPI2015 = 0 then 0 else KPI2024/KPI2015*100 end) AS NUMERIC) ,2)as KK04,\n" +
						"ROUND(CAST((CASE WHEN KPI2802 = 0 then 0 else (KPI2803/KPI2802)*100 end) AS NUMERIC) ,2)as KK05,\n" +
						"ROUND(CAST((CASE WHEN (KPI2151+KPI2154+KPI2621) =0 then 0 else (KPI2020/1000)/(KPI2151+KPI2154+KPI2621) end) AS NUMERIC) ,2)as KK06,\n" +
						"ROUND(CAST((CASE WHEN (KPI2604+KPI2607+KPI2159+KPI2162+KPI2116) =0 then null else (KPI2610+KPI2613+KPI2165+KPI2168+KPI2119)/(KPI2604+KPI2607+KPI2159+KPI2162+KPI2116)*100 end) AS NUMERIC) ,2)as KK07,\n" +
						"ROUND(CAST((CASE WHEN KPI2470 =0 then 0 else KPI2469/KPI2470*100 end) AS NUMERIC) ,2)as KK08,\n" +
						"ROUND(CAST((CASE WHEN KPI2472 =0 then 0 else KPI2473/KPI2472*100 end) AS NUMERIC) ,2)as KK09,\n" +
						"ROUND(CAST((KPI2560) AS NUMERIC) ,2)as KK10,\n" +
						"ROUND(CAST((KPI2301) AS NUMERIC) ,2)as KK11,\n" +
						"ROUND(CAST((CASE WHEN KPI3697 =0 then 0 else KPI3694/KPI3697*100 end) AS NUMERIC) ,2)as KK12,\n" +
						"ROUND(CAST((CASE WHEN KPI3697 =0 then 0 else KPI3696/KPI3697*100 end) AS NUMERIC) ,2)as KK13,\n" +
						"ROUND(CAST((KPI2300) AS NUMERIC) ,2)as KK14,\n" +
						"ROUND(CAST((CASE WHEN KPI3702 =0 then 0 else KPI3699/KPI3702*100 end) AS NUMERIC) ,2)as KK15,\n" +
						"ROUND(CAST((CASE WHEN KPI3702 =0 then 0 else KPI3701/KPI3702*100 end) AS NUMERIC) ,2)as KK16,\n" +
						"ROUND(CAST((0) AS NUMERIC) ,2)as KK17,\n" +
						"ROUND(CAST((KPI5047) AS NUMERIC) ,2)as KK18,\n" +
						"ROUND(CAST((0) AS NUMERIC) ,2)as KK19,\n" +
						"ROUND(CAST((0) AS NUMERIC) ,2)as KK20,\n" +
						"ROUND(CAST((CASE WHEN KPI2761 =0 then 0 else KPI2762/KPI2761*100 end) AS NUMERIC) ,2)as KK21,\n" +
						"ROUND(CAST((CASE WHEN (KPI2187+KPI2754) =0 then null else (KPI2204+KPI2755)/(KPI2187+KPI2754)*100 end) AS NUMERIC) ,2)as KK22,\n" +
						"ROUND(CAST((CASE WHEN KPI2800 =0 then 0 else KPI2801/KPI2800*100 end) AS NUMERIC) ,2)as KK23\n" +
						"from(\n" +
						"SELECT  "+ nickName + ",NULL AS KPI5065 ,sum(KPI2761) AS KPI2761 ,sum(KPI2762) AS KPI2762,sum(KPI2015) as KPI2015,sum(KPI2162) as KPI2162,sum(KPI2168) as KPI2168,sum(KPI2159) as KPI2159,sum(KPI2165) as KPI2165,sum(KPI2116) as KPI2116,sum(KPI2119) as KPI2119,sum(KPI2616) as KPI2616,sum(KPI2800) as KPI2800,sum(KPI2801) as KPI2801,sum(KPI2754) as KPI2754,sum(KPI2755) as KPI2755,sum(KPI2472) as KPI2472,sum(KPI2473) as KPI2473,sum(KPI2024) as KPI2024,sum(KPI2470) as KPI2470,sum(KPI2469) as KPI2469,sum(KPI2151) as KPI2151,sum(KPI2154) as KPI2154,sum(KPI2604) as KPI2604,sum(KPI2610) as KPI2610,sum(KPI2607) as KPI2607,sum(KPI2613) as KPI2613,sum(KPI2560) as KPI2560,sum(KPI2020) as KPI2020,sum(KPI2621) as KPI2621,sum(KPI2301) as KPI2301,sum(KPI2300) as KPI2300,sum(KPI2803) as KPI2803,sum(KPI2802) as KPI2802,sum(KPI2101) as KPI2101,sum(KPI2097) as KPI2097,sum(KPI2187) as KPI2187,sum(KPI2204) as KPI2204,sum(KPI3694) as KPI3694,sum(KPI3696) as KPI3696,sum(KPI3697) as KPI3697,sum(KPI3699) as KPI3699,sum(KPI3701) as KPI3701,sum(KPI3702) as KPI3702,sum(KPI3401) as KPI3401,sum(KPI3400) as KPI3400,NULL as KPI4924,NULL as KPI5041,NULL as KPI5040,NULL as KPI5057,NULL as KPI5058,NULL as KPI5047,NULL as KPI5049,NULL as KPI5051,NULL as KPI5050 from(\n" +
						"SELECT  ID,LOG.TASK_NAME,LOG_NAME,NULL AS KPI5065,KPI2761,KPI2762,KPI2015,KPI2162,KPI2168,KPI2159,KPI2165,KPI2116,KPI2119,KPI2616,KPI2800,KPI2801,KPI2754,KPI2755,KPI2472,KPI2473,KPI2024,KPI2470,KPI2469,KPI2151,KPI2154,KPI2604,KPI2610,KPI2607,KPI2613,KPI2560,KPI2020,KPI2621,KPI2301,KPI2300,KPI2803,KPI2802,KPI2101,KPI2097,KPI2187,KPI2204,null as KPI3694,null as KPI3696,null as KPI3697,null as KPI3699,null as KPI3701,null as KPI3702,null as KPI3401,null as KPI3400,null as KPI4924,null as KPI5041,null as KPI5040,null as KPI5057,null as KPI5058,null as KPI5047,null as KPI5049,null as KPI5051,null as KPI5050 FROM IADS_EXCEL_KPI_2 tt\n" +
						innerJoin +
						"UNION all\n" +
						"SELECT ID,LOG.TASK_NAME,LOG_NAME,NULL AS KPI5065,NULL AS KPI2761,NULL AS KPI2762,null as KPI2015,null as KPI2162,null as KPI2168,null as KPI2159,null as KPI2165,null as KPI2116,null as KPI2119,null as KPI2616,null as KPI2800,null as KPI2801,null as KPI2754,null as KPI2755,null as KPI2472,null as KPI2473,null as KPI2024,null as KPI2470,null as KPI2469,null as KPI2151,null as KPI2154,null as KPI2604,null as KPI2610,null as KPI2607,null as KPI2613,null as KPI2560,null as KPI2020,null as KPI2621,null as KPI2301,null as KPI2300,null as KPI2803,null as KPI2802,null as KPI2101,null as KPI2097,null as KPI2187,null as KPI2204,KPI3694,KPI3696,KPI3697,KPI3699,KPI3701,KPI3702,KPI3401,KPI3400,null as KPI4924,null as KPI5041,null as KPI5040,null as KPI5057,null as KPI5058,null as KPI5047,null as KPI5049,null as KPI5051,null as KPI5050 FROM IADS_EXCEL_KPI_3 tt\n" +
						innerJoin +
						"UNION all\n" +
						"SELECT ID,LOG.TASK_NAME,LOG_NAME,NULL AS KPI5065,NULL AS KPI2761,NULL AS KPI2762,null as KPI2015,null as KPI2162,null as KPI2168,null as KPI2159,null as KPI2165,null as KPI2116,null as KPI2119,null as KPI2616,null as KPI2800,null as KPI2801,null as KPI2754,null as KPI2755,null as KPI2472,null as KPI2473,null as KPI2024,null as KPI2470,null as KPI2469,null as KPI2151,null as KPI2154,null as KPI2604,null as KPI2610,null as KPI2607,null as KPI2613,null as KPI2560,null as KPI2020,null as KPI2621,null as KPI2301,null as KPI2300,null as KPI2803,null as KPI2802,null as KPI2101,null as KPI2097,null as KPI2187,null as KPI2204,null as KPI3694,null as KPI3696,null as KPI3697,null as KPI3699,null as KPI3701,null as KPI3702,null as KPI3401,null as KPI3400,null as KPI4924,null as KPI5041,null as KPI5040,null as KPI5057,null as KPI5058,null as KPI5047,null as KPI5049,null as KPI5051,null as KPI5050 FROM IADS_EXCEL_KPI_4 tt\n" +
						innerJoin +
						"UNION all\n" +
						"SELECT ID,LOG.TASK_NAME,LOG_NAME,NULL AS KPI5065,NULL AS KPI2761,NULL AS KPI2762,null as KPI2015,null as KPI2162,null as KPI2168,null as KPI2159,null as KPI2165,null as KPI2116,null as KPI2119,null as KPI2616,null as KPI2800,null as KPI2801,null as KPI2754,null as KPI2755,null as KPI2472,null as KPI2473,null as KPI2024,null as KPI2470,null as KPI2469,null as KPI2151,null as KPI2154,null as KPI2604,null as KPI2610,null as KPI2607,null as KPI2613,null as KPI2560,null as KPI2020,null as KPI2621,null as KPI2301,null as KPI2300,null as KPI2803,null as KPI2802,null as KPI2101,null as KPI2097,null as KPI2187,null as KPI2204,null as KPI3694,null as KPI3696,null as KPI3697,null as KPI3699,null as KPI3701,null as KPI3702,null as KPI3401,null as KPI3400,null as KPI4924,null as KPI5041,null as KPI5040,null as KPI5057,null as KPI5058,null as KPI5047,null as KPI5049,null as KPI5051,null as KPI5050 FROM IADS_EXCEL_KPI_5 tt\n" +
						innerJoin +
						")fff " + groupby +") tt\n"
				;

		return jdbcTemplate.objectQueryAll(sqlTrue);
	}


	/**
	 * 数据概览KPI计算
	 * 直接导出
	 * */
	public List<Map<String, Object>> doPageQueryKpi() {
		String kpiListTrueSQL = "select\n" +
				"TASK_NAME,LOG_NAME,\n" +
				"case when kpi1 is null then null else kpi1 || '%' end kpi1,\n" +
				"case when kpi2 is null then null else kpi2 || '%' end kpi2,\n" +
				"kpi3,\n" +
				"kpi4,\n" +
				"kpi5,\n" +
				"case when kpi6 is null then null else kpi6 || '%' end kpi6,\n" +
				"case when kpi7 is null then null else kpi7 || '%' end kpi7,\n" +
				"case when kpi8 is null then null else kpi8 || '%' end kpi8\n" +
				"from\n" +
				"(select \n" +
				"TASK_NAME,LOG_NAME,\n" +
				"ROUND(CAST(case when KPI2097=0 then 0 else KPI2101/KPI2097*100 end AS NUMERIC),2)as kpi2,\n" +
				"ROUND(CAST(KPI2301 AS NUMERIC),2)as kpi3,\n" +
				"ROUND(CAST(case when KPI2015=0 then 0 else KPI3400/KPI2015*100 end AS NUMERIC),2)as kpi1,\n" +
				"ROUND(CAST(KPI2300 AS NUMERIC),2)as kpi4,\n" +
				"ROUND(CAST(KPI5047 AS NUMERIC),2)as kpi5,\n" +
				"ROUND(CAST(case when KPI2616=0 then 0 else 0/KPI2616*100 end AS NUMERIC),2)as kpi6,\n" +
				"ROUND(CAST(case when (KPI2187+KPI2754)=0 then 0 else (KPI2204+KPI2755)/(KPI2187+KPI2754)*100 end AS NUMERIC),2)as kpi7,\n" +
				"ROUND(CAST(case when KPI2800=0 then 0 else KPI2801/KPI2800*100 end AS NUMERIC),2)as kpi8\n" +
				"from(\n" +
				"SELECT TASK_NAME,LOG_NAME,SUM(KPI2803) as KPI2803,SUM(KPI2801) as KPI2801,SUM(KPI2800) as KPI2800,SUM(KPI2755) as KPI2755,SUM(KPI2204) as KPI2204,SUM(KPI2187) as KPI2187,SUM(KPI2165) as KPI2165,SUM(KPI2154) as KPI2154,SUM(KPI2151) as KPI2151,SUM(KPI2621) as KPI2621,SUM(KPI2613) as KPI2613,SUM(KPI2560) as KPI2560,SUM(KPI2472) as KPI2472,SUM(KPI2469) as KPI2469,SUM(KPI2301) as KPI2301,SUM(KPI2300) as KPI2300,SUM(KPI2168) as KPI2168,SUM(KPI2162) as KPI2162,SUM(KPI2159) as KPI2159,SUM(KPI2119) as KPI2119,SUM(KPI2116) as KPI2116,SUM(KPI2101) as KPI2101,SUM(KPI2097) as KPI2097,SUM(KPI2024) as KPI2024,SUM(KPI2020) as KPI2020,SUM(KPI2015) as KPI2015,SUM(KPI2802) as KPI2802,SUM(KPI2754) as KPI2754,SUM(KPI2616) as KPI2616,SUM(KPI2610) as KPI2610,SUM(KPI2607) as KPI2607,SUM(KPI2604) as KPI2604,SUM(KPI2473) as KPI2473,SUM(KPI2470) as KPI2470,SUM(KPI3694) as KPI3694,SUM(KPI3696) as KPI3696,SUM(KPI3697) as KPI3697,SUM(KPI3699) as KPI3699,SUM(KPI3701) as KPI3701,SUM(KPI3702) as KPI3702,SUM(KPI3400) as KPI3400,SUM(KPI3401) as KPI3401,null as KPI4924,null as KPI5040,null as KPI5041,null as KPI5047,null as KPI5049,null as KPI5050,null as KPI5051,null as KPI5057,null as KPI5058 from(\n" +
				"SELECT ID,TASK_NAME,LOG_NAME,KPI2803,KPI2801,KPI2800,KPI2755,KPI2204,KPI2187,KPI2165,KPI2154,KPI2151,KPI2621,KPI2613,KPI2560,KPI2472,KPI2469,KPI2301,KPI2300,KPI2168,KPI2162,KPI2159,KPI2119,KPI2116,KPI2101,KPI2097,KPI2024,KPI2020,KPI2015,KPI2802,KPI2754,KPI2616,KPI2610,KPI2607,KPI2604,KPI2473,KPI2470,NULL AS KPI3694,NULL AS KPI3696,NULL AS KPI3697,NULL AS KPI3699,NULL AS KPI3701,NULL AS KPI3702,NULL AS KPI3400,NULL AS KPI3401,NULL AS KPI4924,NULL AS KPI5040,NULL AS KPI5041,NULL AS KPI5047,NULL AS KPI5049,NULL AS KPI5050,NULL AS KPI5051,NULL AS KPI5057,NULL AS KPI5058 FROM IADS_EXCEL_KPI_2\n" +
				"UNION all\n" +
				"SELECT ID,TASK_NAME,LOG_NAME,null as KPI2803, null as KPI2801, null as KPI2800, null as KPI2755, null as KPI2204, null as KPI2187, null as KPI2165, null as KPI2154, null as KPI2151, null as KPI2621, null as KPI2613, null as KPI2560, null as KPI2472, null as KPI2469, null as KPI2301, null as KPI2300, null as KPI2168, null as KPI2162, null as KPI2159, null as KPI2119, null as KPI2116, null as KPI2101, null as KPI2097, null as KPI2024, null as KPI2020, null as KPI2015, null as KPI2802, null as KPI2754, null as KPI2616, null as KPI2610, null as KPI2607, null as KPI2604, null as KPI2473, null as KPI2470, KPI3694, KPI3696, KPI3697, KPI3699, KPI3701, KPI3702, KPI3400, KPI3401, null as KPI4924, null as KPI5040, null as KPI5041, null as KPI5047, null as KPI5049, null as KPI5050, null as KPI5051, null as KPI5057, null as KPI5058 FROM IADS_EXCEL_KPI_3\n" +
				"UNION all\n" +
				"SELECT ID,TASK_NAME,LOG_NAME,null as KPI2803, null as KPI2801, null as KPI2800, null as KPI2755, null as KPI2204, null as KPI2187, null as KPI2165, null as KPI2154, null as KPI2151, null as KPI2621, null as KPI2613, null as KPI2560, null as KPI2472, null as KPI2469, null as KPI2301, null as KPI2300, null as KPI2168, null as KPI2162, null as KPI2159, null as KPI2119, null as KPI2116, null as KPI2101, null as KPI2097, null as KPI2024, null as KPI2020, null as KPI2015, null as KPI2802, null as KPI2754, null as KPI2616, null as KPI2610, null as KPI2607, null as KPI2604, null as KPI2473, null as KPI2470, null as KPI3694, null as KPI3696, null as KPI3697, null as KPI3699, null as KPI3701, null as KPI3702, null as KPI3400, null as KPI3401, null as KPI4924, null as KPI5040, null as KPI5041, null as KPI5047, null as KPI5049, null as KPI5050, null as KPI5051, null as KPI5057, null as KPI5058 FROM  IADS_EXCEL_KPI_4\n" +
				"UNION all\n" +
				"SELECT ID,TASK_NAME,LOG_NAME,null as KPI2803, null as KPI2801, null as KPI2800, null as KPI2755, null as KPI2204, null as KPI2187, null as KPI2165, null as KPI2154, null as KPI2151, null as KPI2621, null as KPI2613, null as KPI2560, null as KPI2472, null as KPI2469, null as KPI2301, null as KPI2300, null as KPI2168, null as KPI2162, null as KPI2159, null as KPI2119, null as KPI2116, null as KPI2101, null as KPI2097, null as KPI2024, null as KPI2020, null as KPI2015, null as KPI2802, null as KPI2754, null as KPI2616, null as KPI2610, null as KPI2607, null as KPI2604, null as KPI2473, null as KPI2470, null as KPI3694, null as KPI3696, null as KPI3697, null as KPI3699, null as KPI3701, null as KPI3702, null as KPI3400, null as KPI3401, null as KPI4924, null as KPI5040, null as KPI5041, null as KPI5047, null as KPI5049, null as KPI5050, null as KPI5051, null as KPI5057, null as KPI5058 FROM IADS_EXCEL_KPI_5\n" +
				") fff group by TASK_NAME, LOG_NAME) ffff) aaa";

		return jdbcTemplate.objectQueryAll(kpiListTrueSQL);
	}



	public List<Map<String, Object>> provInput() {
		String sql = "SELECT  DISTINCT prov AS LABEL,prov AS  VALUE  FROM IADS_TESTLOG_ITEM iti WHERE prov IS NOT NULL ORDER BY prov\n";
		return jdbcTemplate.objectQueryAll(sql);
	}


	public List<Map<String, Object>> cityInput(String prov){
		String sql = "";
		if(prov!=null && StringUtils.hasText(prov)){
			String city = prov;
			sql = "SELECT DISTINCT city AS LABEL,city AS  VALUE FROM IADS_TESTLOG_ITEM iti WHERE city IS NOT NULL AND prov='" + city + "'  ORDER BY city";
		}else {
			sql = "SELECT DISTINCT city AS LABEL,city AS  VALUE FROM IADS_TESTLOG_ITEM iti WHERE city IS NOT NULL ORDER BY city";
		}

		return jdbcTemplate.objectQueryAll(sql);
	}

	/**
	 * 根据 idList 拼接 Where 后 的SQL
	 *
	 * */
	private String logCheckIdWhere(List<String> idList){
		String idWhere = "";
		if(idList.size()>0){

			String idStr = "";
			for(int i=0;i<idList.size();i++){
				String t = "'" + idList.get(i)  + "'" ;
				if(i!=0){
					idStr = idStr +  " , " + t;
				}else{
					idStr = t;
				}
			}
			idWhere = "AND  LOG.RECSEQNO IN (" + idStr + ") ";
		}
		return idWhere;

	}


	/**
	 * 日志校验导出 MD5 sheet
	 * */
	public List<Map<String, Object>> logCheckMd5(Date beginDate,Date endDate,List<String> idList){

		String idWhere = logCheckIdWhere(idList);

		// 查出文件名
		String logFileWhere = "";
		String ccFileWhere = "";
			if(StringUtils.hasText(idWhere)){

			String fStr = "";
			String sql0 = "SELECT FILE_NAME FROM IADS_TESTLOG_ITEM LOG WHERE 1=1" + idWhere;
			List<Map<String, Object>> sqlObj0 = jdbcTemplate.objectQueryAll(sql0);
			for(int i=0;i<sqlObj0.size();i++){
				String fileName = (String)sqlObj0.get(i).get("FILE_NAME");
				if(fileName!=null && StringUtils.hasText(fileName)){
					fileName = "'" + fileName + "'";
					if(i!=0){
						fStr = fStr +  " , " + fileName;
					}else{
						fStr = fileName;
					}
				}
			}
			if(StringUtils.hasText(fStr)){
				logFileWhere = "AND LOG.FILE_NAME in (" + fStr + ") ";
				ccFileWhere = "AND CC.FILE_NAME in (" + fStr + ") ";
			}
		}

		// 时间
		// 规避处理，选了Id  取消时间限制
		String logTimeWhere ="";
		String ccTimeWhere ="";

		if(beginDate!=null
				&& endDate!=null
				&& (idWhere==null || (!StringUtils.hasText(idWhere)))){
			logTimeWhere = " AND LOG.START_DATE > " + beginDate.getTime() + " AND　" + "LOG.END_DATE <= " + endDate.getTime() +" ";
			ccTimeWhere  = " AND CC.START_TIME*1000 > " + beginDate.getTime() + " AND　" + "CC.END_TIME*1000 <= " + endDate.getTime() +" ";
		}

		/**
		 * 陈泉原始需求
		 * 缺失上传： 服务端以file_list表为基础核对在file_list_check表中的md5值，获得“缺失上传”的文件清单
		 * 未知文件：服务端需要以file_list_check表为基础核对不存在于file_list表中的md5值而生成“未知文件”清单文件名异常：
		 * 文件名异常：服务端需要比较file_list表和file_list_check表中md5值相同而文件名不同的文件，并生成“文件名异常”清单
		 *
		 * IADS_CUCC_LOGINFO 上报
		 * IADS_TESTLOG_ITEM 解析
		 *
		 * */

		String sql1= "\n" +
				"SELECT\n" +
				"\t\t\t\tcc.BOX_ID IMSI1,\n" +
				"\t\t\t\tcc.FILE_NAME1 FILE_NAME1,\n" +
				"\t\t\t\tcc.md51 MD51,\n" +
				"\t\t\t\tto_char(to_timestamp(cc.START_TIME), 'YYYY-MM-DD HH24:MI:SS') AS START_TIME,\n" +
				"\t\t\t\tto_char(to_timestamp(cc.END_TIME), 'YYYY-MM-DD HH24:MI:SS') AS END_TIME,\n" +
				"\t\t\t\tcc.min_lat MIN_LAT,\n" +
				"\t\t\t\tcc.max_lat MAX_LAT,\n" +
				"\t\t\t\tcc.min_long MIN_LONG,\n" +
				"\t\t\t\tcc.max_long MAX_LONG,\n" +
				"\t\t\t\tLOG.BOX_ID AS IMSI2,\n" +
				"\t\t\t\tLOG.FILE_NAME FILE_NAME2,\n" +
				"\t\t\t\tLOG.FILE_MD_VALUE MD52,\n" +
				"\t\t\t\tCASE WHEN LOG.FILE_NAME IS NULL THEN CC.FILE_NAME1 ELSE LOG.FILE_NAME END FILE_NAME3,\n" +
				"\t\t\t\tCASE WHEN LOG.FILE_MD_VALUE IS NULL THEN cc.md51 ELSE LOG.FILE_MD_VALUE END MD53," +
				"\t\t\t\tCASE WHEN LOG.FILE_NAME = cc.FILE_NAME1 then '成功' ELSE '失败' END  AS FILE_NAME_CHECK,\n" +
				"\t\t\t\tCASE WHEN LOG.FILE_MD_VALUE = cc.md51 then '成功' ELSE '失败' END  AS MD5_CHECK,\n" +

//				"\tcase WHEN LOG.FILE_NAME = CC.FILE_NAME1 THEN null ELSE\n" +
//				"\t\tCASE WHEN LOG.FILE_MD_VALUE = cc.md51 THEN '文件名异常' ELSE \n" +
//				"\t\t\tcase WHEN LOG.FILE_NAME IS NULL THEN　'缺失上传' ELSE \n" +
//				"\t\t\t\tcase WHEN CC.FILE_NAME1 IS NULL THEN　'未知文件' ELSE NULL END\n" +
//				"\t\t\tEND\n" +
//				"\t\tend\n" +
//				"\tend\n" +
//				"\tAS EXCEPTION" +

				"\tcase WHEN LOG.FILE_NAME = CC.FILE_NAME1 THEN null ELSE\n" +
				"\t\tCASE WHEN LOG.FILE_MD_VALUE = cc.md51 THEN '文件名异常' ELSE null\n" +
				"\t\tend\n" +
				"\tend\n" +
				"\tAS EXCEPTION" +

				"\t\t\t\tFROM\n" +
				"\t\t\t\t(SELECT BOX_ID,FILE_NAME,FILE_MD_VALUE FROM IADS_TESTLOG_ITEM LOG WHERE 1=1"   + logFileWhere +  logTimeWhere + ")  LOG\n" +
				"\t\t\t\tFULL JOIN ( SELECT \n" +
				"\t\t\t\tBOX_ID,\n" +
				"\t\t\t\tFILE_NAME FILE_NAME1,\n" +
				"\t\t\t\tMD5 md51,\n" +
				"\t\t\t\tSTART_TIME start_time,\n" +
				"\t\t\t\tEND_TIME end_time,\n" +
				"\t\t\t\tMIN_LAT min_lat,\n" +
				"\t\t\t\tMAX_LAT max_lat,\n" +
				"\t\t\t\tMIN_LONG min_long,\n" +
				"\t\t\t\tMAX_LONG max_long\n" +
				"\t\t\t\tFROM\n" +
				"\t\t\t\t(SELECT\n" +
				"l.BOX_ID BOX_ID,\n" +
				"l.FILE_NAME FILE_NAME,\n" +
				"l.MD5 MD5,\n" +
				"f.START_TIME START_TIME,\n" +
				"l.END_TIME END_TIME,\n" +
				"l.MIN_LAT MIN_LAT,\n" +
				"l.MAX_LAT MAX_LAT,\n" +
				"l.MIN_LONG MIN_LONG,\n" +
				"l.MAX_LONG MAX_LONG\n" +
				"FROM IADS_CUCC_LOGINFO f INNER JOIN \n" +
				"IADS_CUCC_LOGINFO l ON f.FILE_NAME = l.FILE_NAME AND f.TAG='first' AND l.TAG='last'  AND  f.BOX_ID = l.BOX_ID) cc\n" +
				"\t\t\t\tWHERE\n" +
				"\t\t\t\t1 = 1 " + ccFileWhere  + ccTimeWhere + " ) cc ON\n" +
				"\t\t\t\tcc.FILE_NAME1 = LOG.FILE_NAME";
		return jdbcTemplate.objectQueryAll(sql1);
	}

	/**
	 * 日志校验导出 业务事件 sheet
	 * */
	public List<Map<String, Object>> logCheckBiz(Date beginDate,Date endDate,List<String> idList){

		String idWhere = logCheckIdWhere(idList);

		// 查出文件名
		String logFileWhere = "";
		if(StringUtils.hasText(idWhere)){

			String fStr = "";
			String sql0 = "SELECT FILE_NAME FROM IADS_TESTLOG_ITEM LOG WHERE 1=1" + idWhere;
			List<Map<String, Object>> sqlObj0 = jdbcTemplate.objectQueryAll(sql0);
			for(int i=0;i<sqlObj0.size();i++){
				String fileName = (String)sqlObj0.get(i).get("FILE_NAME");
				if(fileName!=null && StringUtils.hasText(fileName)){
					fileName = "'" + fileName + "'";
					if(i!=0){
						fStr = fStr +  " , " + fileName;
					}else{
						fStr = fileName;
					}
				}
			}
			if(StringUtils.hasText(fStr)){
				logFileWhere = "AND LOG.FILE_NAME in (" + fStr + ") ";
			}
		}

		// 时间
		// 规避处理，选了Id  取消时间限制
		String logTimeWhere ="";
		if(beginDate!=null
				&& endDate!=null
				&& (idWhere==null || (!StringUtils.hasText(idWhere)))){
			logTimeWhere = " AND LOG.START_DATE > " + beginDate.getTime() + " AND　" + "LOG.END_DATE <= " + endDate.getTime() +" ";
		}


		/**
		 * 陈泉原始需求
		 规则1：服务端需要以event_list表为基础核对在event_list_check表中的相同文件的业务测试事件发生的次数，找出存在于event_list表而不存在于event_list_check表中的数据，以此获得“事件缺失”的业务事件清单；
		 规则2：服务端需要以event_list_check表为基础核对在event_list表中的相同文件的业务测试事件发生的次数，找出存在于event_list_check表而不存在于event_list表中的数据，以此获得 “未知业务”清单
		 规则3：在event_list与event_list_check中业务次数相同的情况下，分别比较每次业务事件的时间戳及经纬度信息，输出“业务时间不匹配”及“业务位置不匹配”清单

		 *
		 * IADS_CUCC_TRAFFICINFO 上报
		 * IADS_TESTLOG_TRAFFICINFO 解析
		 *
		 *
		 * */


try{
	List<Map<String, Object>> mockdata = bizCheckNew(logFileWhere, logTimeWhere);


	return mockdata;
}catch (Exception e){
	// 报错走旧逻辑
	LOGGER.error("bizCheckNew ",e);
	String sql2 = "SELECT\n" +
			"\tcc.FILE_NAME AS FILE_NAME1,\n" +
			"\tEVENTTYPE AS EVENTTYPE1,\n" +
			"\tCAST((CASE WHEN TIMESTAMP > 4102444800 OR TIMESTAMP < 0 THEN 0 ELSE TIMESTAMP END)/(60*60*24) AS NUMERIC) AS TIMESTAMP1,\n" +
			"\tLONGITUDE AS LONGITUDE1,\n" +
			"\tLATITUDE AS LATITUDE1,\n" +
			"\tcc.FILE_NAME AS FILE_NAME2,\n" +
			"\tEVENTTYPE AS EVENTTYPE2,\n" +
			"\tCAST((CASE WHEN TIMESTAMP > 4102444800 OR TIMESTAMP < 0 THEN 0 ELSE TIMESTAMP END)/(60*60*24) AS NUMERIC) AS TIMESTAMP2,\n" +
			"\tLONGITUDE AS LONGITUDE2,\n" +
			"\tLATITUDE AS LATITUDE2,\n" +
			"\tcc.BOX_ID AS IMSI,\n" +
			"\tcc.FILE_NAME AS FILE_NAME3,\n" +
			"\tEVENTTYPE AS EVENTTYPE3,\n" +
			"\tCAST((CASE WHEN TIMESTAMP > 4102444800 OR TIMESTAMP < 0 THEN 0 ELSE TIMESTAMP END)/(60*60*24) AS NUMERIC) AS TIMESTAMP3,\n" +
			"\tLONGITUDE AS LONGITUDE3,\n" +
			"\tLATITUDE AS LATITUDE3,\n" +
			"\t\t\t\tCASE WHEN cc.EVENTTYPE IS NULL THEN 'false' else 'true' END  AS EVENT_TYPE_CHECK,\n" +
			"\t\t\t\tCASE WHEN TIMESTAMP IS NULL THEN 'false' else 'true' END   AS TIMESTAMP_CHECK,\n" +
			"\t\t\t\tCASE WHEN LONGITUDE IS NULL THEN 'false' else 'true' END   AS LONGITUDE_CHECK,\n" +
			"\t\t\t\tCASE WHEN LATITUDE IS NULL THEN 'false' else 'true' END   AS LATITUDE_CHECK," +
			"\tNULL AS EXCEPTION\n" +
			"\tFROM\n" +
			"\tIADS_CUCC_TRAFFICINFO cc\n" +
			"RIGHT JOIN (SELECT * FROM IADS_TESTLOG_ITEM LOG WHERE 1=1 "+  idWhere +" )LOG ON\n" +
			"\tcc.FILE_NAME = log.FILE_NAME" +
			"\tORDER BY cc.id";
			return jdbcTemplate.objectQueryAll(sql2);
		}



	}


	/**
	 * 业务事件校验 新
	 * */
	private List<Map<String,Object>> bizCheckNew(String logFileWhere,String logTimeWhere){
		String sql2_cucc = "SELECT\n" +
				"\tLOG.BOX_ID BOX_ID,\n" +
				"\tLOG.FILE_NAME FILE_NAME,\n" +
				"\tEVENTTYPE,\n" +
				"\tCAST((CASE WHEN TIMESTAMP > 4102444800 OR TIMESTAMP < 0 THEN 0 ELSE TIMESTAMP END)/(60*60*24) AS NUMERIC) AS TIMESTAMP,\n" +
				"\tLONGITUDE,\n" +
				"\tLATITUDE\n" +
				"FROM\n" +
				"\tIADS_CUCC_TRAFFICINFO CC\n" +
				"RIGHT JOIN (\n" +
				"\tSELECT\n" +
				"\t\tBOX_ID,\n" +
				"\t\tFILE_NAME\n" +
				"\tFROM\n" +
				"\t\tIADS_TESTLOG_ITEM LOG\n" +
				"\tWHERE\n" +
				"\t\t1 = 1 " + logFileWhere +  logTimeWhere + ") LOG ON\n" +
				"\tCC.BOX_ID = LOG.BOX_ID\n" +
				"\tAND CC.FILE_NAME=LOG.FILE_NAME ORDER BY TIMESTAMP";

		String sql2_testlog = "SELECT\n" +
				"\tLOG.BOX_ID BOX_ID,\n" +
				"\tLOG.FILE_NAME FILE_NAME,\n" +
				"\tEVENTTYPE,\n" +
				"\tCAST((CASE WHEN TIMESTAMP > 4102444800 OR TIMESTAMP < 0 THEN 0 ELSE TIMESTAMP END)/(60*60*24) AS NUMERIC) AS TIMESTAMP,\n" +
				"\tLONGITUDE,\n" +
				"\tLATITUDE\n" +
				"FROM\n" +
				"\tIADS_TESTLOG_TRAFFICINFO CC\n" +
				"RIGHT JOIN (\n" +
				"\tSELECT\n" +
				"\t\tBOX_ID,\n" +
				"\t\tFILE_NAME\n" +
				"\tFROM\n" +
				"\t\tIADS_TESTLOG_ITEM LOG\n" +
				"\tWHERE\n" +
				"\t\t1 = 1" + logFileWhere +  logTimeWhere + " ) LOG ON\n" +
				"\tCC.BOX_ID = LOG.BOX_ID\n" +
				"\tAND CC.FILE_NAME = LOG.FILE_NAME ORDER BY TIMESTAMP";

		List<Map<String, Object>> cucc = jdbcTemplate.objectQueryAll(sql2_cucc);
		List<Map<String, Object>> testlog = jdbcTemplate.objectQueryAll(sql2_testlog);

		Map<CheckBizKey,List<Map<String, Object>>> cuccMap = bizListToMap(cucc);
		Map<CheckBizKey,List<Map<String, Object>>> testlogMap = bizListToMap(testlog);


		Set<CheckBizKey> allKey = new TreeSet<>();
		allKey.addAll(cuccMap.keySet());
		allKey.addAll(testlogMap.keySet());

		List<Map<String,Object>> mockdata = new ArrayList<>();
		for(CheckBizKey c:allKey){
			List<Map<String, Object>> cuccList = cuccMap.get(c);
			List<Map<String, Object>> testLogList = testlogMap.get(c);
			mockdata.addAll(mockData(cuccList, testLogList));
		}


		Map<CheckBizKey, List<Map<String, Object>>> checkBizKeyListMap = bizListToMapSimple(mockdata);
		Set<CheckBizKey> checkBizKeys = checkBizKeyListMap.keySet();

		List<Map<String,Object>> mockdataOrderBy = new ArrayList<>();
		for(CheckBizKey c:checkBizKeys){
			List<Map<String, Object>> mapList = checkBizKeyListMap.get(c);

			Collections.sort(mapList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {

					String timestamp1 = (String)o1.get("TIMESTAMP3");
					String timestamp2 = (String)o2.get("TIMESTAMP3");

					Date date1 = null;
					Date date2 = null;

					try{
						if(timestamp1==null){
							return 1;
						}
						date1 = DateUtils.parseDate(timestamp1, "yyyy-MM-dd HH:mm:ss");
					}catch (Exception e){
						return 1;
					}

					try{
						if(timestamp2==null){
							return -1;
						}
						date2 = DateUtils.parseDate(timestamp2, "yyyy-MM-dd HH:mm:ss");
					}catch (Exception e){
						return -1;
					}

					return date1.compareTo(date2);
				}
			});

			mockdataOrderBy.addAll(mapList);

		}

		return mockdataOrderBy;
	}


	private Map<CheckBizKey,List<Map<String, Object>>> bizListToMap(List<Map<String, Object>> cucc){
		Map<CheckBizKey,List<Map<String, Object>>> cuccMap = new HashMap<>();
		for(Map<String,Object> m:cucc){

			if(m.get(BOX_ID) == null){
				m.put(BOX_ID,"");
			}
			if(m.get(FILE_NAME) == null){
				m.put(FILE_NAME,"");
			}
			if(m.get(EVENTTYPE) == null){
				m.put(EVENTTYPE,"");
			}

			CheckBizKey checkBizKey = new CheckBizKey(m.get(BOX_ID).toString(), m.get(FILE_NAME).toString(), m.get(EVENTTYPE).toString());
			List<Map<String, Object>> mapsList = null;
			if (cuccMap.containsKey(checkBizKey)){
				mapsList = cuccMap.get(checkBizKey);
			}else{
				mapsList = new ArrayList<>();
				cuccMap.put(checkBizKey,mapsList);
			}
			mapsList.add(m);
		}
		return cuccMap;
	}



	private Map<CheckBizKey,List<Map<String, Object>>> bizListToMapSimple(List<Map<String, Object>> cucc){
		Map<CheckBizKey,List<Map<String, Object>>> cuccMap = new TreeMap<>();
		for(Map<String,Object> m:cucc){
			String filename = (String)m.get("FILE_NAME3");
			if( filename== null || !StringUtils.hasText(filename)){
				continue;
			}
			String imei = (String)m.get("IMEI");
			if( imei== null){
				imei="";
			}

			CheckBizKey checkBizKey = new CheckBizKey(imei, filename, null);
			List<Map<String, Object>> mapsList = null;
			if (cuccMap.containsKey(checkBizKey)){
				mapsList = cuccMap.get(checkBizKey);
			}else{
				mapsList = new ArrayList<>();
				cuccMap.put(checkBizKey,mapsList);
			}
			mapsList.add(m);
		}
		return cuccMap;
	}




	// 从两个map中找一个有值的
	private  Object getMapDataKey(Map<String,Object> m1,Map<String,Object> m2,String key){
		if(m1==null || m2==null){
			return null;
		}
		Object o1 = m1.get(key);
		Object o2 = m2.get(key);

		if(o1!=null){
			return o1;
		}

		if(o2!=null){
			return o2;
		}
		return null;

	}

	private boolean  mapKeyEquals(Map<String,Object> m1,Map<String,Object> m2,String key){
		return objEquals(m1.get(key),m2.get(key));
	}


	private  boolean objEquals(Object o1,Object o2){
		if(o1==null && o2==null){
			return false;
		}
		return ObjectUtils.eqOrBothNull(o1,o2);
	}

	/*
	** 1 正确
	*  -1 业务时间不匹配
	*  -2 业务位置不匹配
	*  -3 都不匹配
	* */
	private  int  compareBizMap(Map<String,Object> source,Map<String,Object> target){
		if(target==null){
			return -3;
		}
		int result = 1;
		if(!mapKeyEquals(source,target, TIMESTAMP)){
			result = result -1;
		}
		if((!mapKeyEquals(source,target,LONGITUDE)) || !mapKeyEquals(source,target,LATITUDE)){
			result = result - 2;
		}
		return result;
	}


	/**
	 * 返回excel输出的行
	 *
	 * */
	private Map<String,Object> resultBizMap(Map<String,Object> source,Map<String,Object> target,String exception){
		Map<String,Object> result = new HashMap<>();

		//IMSI
		result.put("IMSI",getMapDataKey(source, target, BOX_ID));

		// FILE_NAME
		result.put("FILE_NAME1",source.getOrDefault(FILE_NAME,""));
		result.put("FILE_NAME2",target.getOrDefault(FILE_NAME,""));
		result.put("FILE_NAME3",getMapDataKey(source, target, FILE_NAME));


		// EVENTTYPE
		result.put("EVENTTYPE1",source.getOrDefault(EVENTTYPE,""));
		result.put("EVENTTYPE2",target.getOrDefault(EVENTTYPE,""));
		result.put("EVENTTYPE3",getMapDataKey(source, target, EVENTTYPE));
		result.put("EVENT_TYPE_CHECK",mapKeyEquals(source,target,EVENTTYPE));

		// TIMESTAMP
		result.put("TIMESTAMP1",source.getOrDefault(TIMESTAMP,""));
		result.put("TIMESTAMP2",target.getOrDefault(TIMESTAMP,""));
		result.put("TIMESTAMP3",getMapDataKey(source, target, TIMESTAMP));
		result.put("TIMESTAMP_CHECK",mapKeyEquals(source,target,TIMESTAMP));

		// LONGITUDE
		result.put("LONGITUDE1",source.getOrDefault(LONGITUDE,""));
		result.put("LONGITUDE2",target.getOrDefault(LONGITUDE,""));
		result.put("LONGITUDE3",getMapDataKey(source, target, LONGITUDE));
		result.put("LONGITUDE_CHECK",mapKeyEquals(source,target,LONGITUDE));

		// TIMESTAMP
		result.put("LATITUDE1",source.getOrDefault(LATITUDE,""));
		result.put("LATITUDE2",target.getOrDefault(LATITUDE,""));
		result.put("LATITUDE3",getMapDataKey(source, target, LATITUDE));
		result.put("LATITUDE_CHECK",mapKeyEquals(source,target,LATITUDE));

		if(exception==null){
			exception = "";
		}
		result.put("EXCEPTION",exception);


		return result ;
	}


	private List<Map<String,Object>> mockData(List<Map<String,Object>> cucc,List<Map<String,Object>> testLog){
		if(cucc==null){
			cucc = new ArrayList<>();
		}

		if(testLog==null){
			testLog = new ArrayList<>();
		}

		List<Map<String,Object>> mockdata = new ArrayList<>();

		if(cucc.size()>testLog.size()){
			cucc = new ArrayList<>(cucc);
			testLog = new ArrayList<>(testLog);




			Iterator<Map<String,Object>> cuccIt = null;
			Iterator<Map<String,Object>> testLogIt = null;


			// 业务缺失 只关注丢失
			for(Map<String,Object> cuccEntity:cucc){
				boolean flag = false;
				Map<String,Object> testLogEntity = new HashMap<>();
				testLogIt = testLog.iterator();
				while(testLogIt.hasNext()){
					Map<String, Object> testEntityEach = testLogIt.next();
					int result = compareBizMap(cuccEntity, testEntityEach);
					if(result>0){
						flag = true;
						testLogEntity = testEntityEach;
						testLogIt.remove();
						break;
					}
				}

				// 找到了事件不用判断
				if(flag){
					mockdata.add(resultBizMap(cuccEntity,testLogEntity,""));
				}else{
					// 找不到事件为 事件缺失
					mockdata.add(resultBizMap(cuccEntity,testLogEntity,"事件缺失"));
				}
			}

			for(Map<String,Object> tl:testLog){
				mockdata.add(resultBizMap(new HashMap<>(),tl," "));
			}


		}else if(cucc.size()<testLog.size()){
			cucc = new ArrayList<>(cucc);
			testLog = new ArrayList<>(testLog);

			Iterator<Map<String,Object>> cuccIt = null;
			Iterator<Map<String,Object>> testLogIt = null;


			// 未知业务
			// 业务缺失 只关注丢失
			for(Map<String,Object> testLogEntity:testLog){
				boolean flag = false;
				Map<String,Object> cuccEntity = new HashMap<>();
				cuccIt = cucc.iterator();
				while(cuccIt.hasNext()){
					Map<String, Object> cuccEntityEach = cuccIt.next();
					int result = compareBizMap(testLogEntity, cuccEntityEach);
					if(result>0){
						flag = true;
						cuccEntity = cuccEntityEach;
						cuccIt.remove();
						break;
					}
				}

				// 找到了事件不用判断
				if(flag){
					mockdata.add(resultBizMap(cuccEntity,testLogEntity,""));
				}else{
					// 找不到事件为 事件缺失
					mockdata.add(resultBizMap(cuccEntity,testLogEntity,"未知业务"));
				}
			}

			for(Map<String,Object> cc:cucc){
				mockdata.add(resultBizMap(cc,new HashMap<>(),""));
			}

		}else{
			// 业务时间不匹配
			// 业务位置不匹配
			mockdata.addAll(biz3(cucc,testLog));
		}

		return mockdata;
	}




	private List<Map<String,Object>> biz3(List<Map<String,Object>> cucc,List<Map<String,Object>> testLog){
		cucc = new ArrayList<>(cucc);
		testLog = new ArrayList<>(testLog);

		List<Map<String,Object>> mockdata = new ArrayList<>();

		Iterator<Map<String,Object>> cuccIt = null;
		Iterator<Map<String,Object>> testLogIt = null;


		// 0 经纬度 时间都相同
		cuccIt = cucc.iterator();
		while (cuccIt.hasNext()){
			Map<String, Object> c = cuccIt.next();
			testLogIt = testLog.iterator();
			while (testLogIt.hasNext()){
				Map<String, Object> t = testLogIt.next();
				if(mapKeyEquals(c,t,TIMESTAMP) && ((mapKeyEquals(c,t,LONGITUDE)) && (mapKeyEquals(c,t,LATITUDE)))){
					mockdata.add(resultBizMap(c,t,""));
					cuccIt.remove();
					testLogIt.remove();
					break;
				}
			}
		}

		// 1 强制时间相同
		cuccIt = cucc.iterator();
		while (cuccIt.hasNext()){
			Map<String, Object> c = cuccIt.next();
			testLogIt = testLog.iterator();
			while (testLogIt.hasNext()){
				Map<String, Object> t = testLogIt.next();
				if(mapKeyEquals(c,t,TIMESTAMP) && ((!mapKeyEquals(c,t,LONGITUDE)) || (!mapKeyEquals(c,t,LATITUDE)))){
					mockdata.add(resultBizMap(c,t,"业务位置不匹配"));
					cuccIt.remove();
					testLogIt.remove();
					break;
				}
			}
		}



		// 2 强制经纬度相同
		cuccIt = cucc.iterator();
		while (cuccIt.hasNext()){
			Map<String, Object> c = cuccIt.next();
			testLogIt = testLog.iterator();
			while (testLogIt.hasNext()){
				Map<String, Object> t = testLogIt.next();
				if(!mapKeyEquals(c,t,TIMESTAMP) && ((mapKeyEquals(c,t,LONGITUDE)) && (mapKeyEquals(c,t,LATITUDE)))){
					mockdata.add(resultBizMap(c,t,"业务时间不匹配"));
					cuccIt.remove();
					testLogIt.remove();
					break;
				}
			}
		}

		// 大家都不匹配
		cuccIt = cucc.iterator();
		while (cuccIt.hasNext()){
			Map<String, Object> c = cuccIt.next();
			testLogIt = testLog.iterator();
			while (testLogIt.hasNext()){
				Map<String, Object> t = testLogIt.next();
				mockdata.add(resultBizMap(c,t,"业务位置不匹配,业务时间不匹配"));
				cuccIt.remove();
				testLogIt.remove();
				break;
			}
		}

		return mockdata;

	}



	private static class CheckBizKey implements Comparable<CheckBizKey>{
		private String boxId;
		private String fileName;
		private String eventType;

		public String getBoxId() {
			return boxId;
		}

		public void setBoxId(String boxId) {
			this.boxId = boxId;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getEventType() {
			return eventType;
		}

		public void setEventType(String eventType) {
			this.eventType = eventType;
		}

		public CheckBizKey(String boxId, String fileName, String eventType) {
			this.boxId = boxId;
			this.fileName = fileName;
			this.eventType = eventType;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			CheckBizKey that = (CheckBizKey) o;

			if (boxId != null ? !boxId.equals(that.boxId) : that.boxId != null) return false;
			if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
			if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = boxId != null ? boxId.hashCode() : 0;
			result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
			result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
			return result;
		}

		@Override
		public int compareTo(CheckBizKey o) {
			String fileName = "";
			if(this.fileName!=null){
				fileName = this.fileName;
			}

			String eventType = "";
			if(this.eventType!=null){
				eventType = this.eventType;
			}

			String boxId = "";
			if(this.boxId!=null){
				boxId = this.boxId;
			}


			String oFileName = "";
			if(o.fileName!=null){
				oFileName = o.fileName;
			}

			String oEventType = "";
			if(o.eventType!=null){
				oEventType = o.eventType;
			}

			String oBoxId = "";
			if(o.boxId!=null){
				oBoxId = o.boxId;
			}


			int fileNameCompare = fileName.compareTo(oFileName);
			if(fileNameCompare == 0){
				int eventTypeCompare = eventType.compareTo(oEventType);
				if(eventTypeCompare == 0){
					return boxId.compareTo(oBoxId);
				}
				return eventTypeCompare;
			}
			return fileNameCompare;
		}
	}
}
