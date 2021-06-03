package com.datang.dao.errorLogManage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.errorLogManage.ErrorLogManagePojo;

/**
 * 错误日志管理Dao层
 * @author maxuancheng
 * @date 2019年5月31日
 */
@Repository
public class ErrorLogManageDao extends GenericHibernateDao<ErrorLogManagePojo, Long>{

	/**
	 * 多条件查询
	 * 
	 * @author maxuancheng
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList getPageDataOfFactor(PageList pageList) {
		if (null == pageList) {
			return new EasyuiPageList();
		}
		Criteria criteria = this.getHibernateSession().createCriteria(ErrorLogManagePojo.class);
		ErrorLogManagePojo errorLogManagePojo = (ErrorLogManagePojo)pageList.getParam("errorLogManagePojo");
		if(errorLogManagePojo != null){
			String city = errorLogManagePojo.getCity();
			String softwareV = errorLogManagePojo.getSoftwareV();
			String osV = errorLogManagePojo.getOsV();
			Integer terminalType = errorLogManagePojo.getTerminalType();
			
			// 设置city筛选条件
			if (StringUtils.hasText((String) city)) {
				criteria.add(Restrictions.like("city", city,MatchMode.ANYWHERE));
			}
			// 设置softwareV筛选条件
			if (StringUtils.hasText((String) softwareV)) {
				criteria.add(Restrictions.like("softwareV", softwareV,MatchMode.ANYWHERE));
			}
			// 设置osV筛选条件
			if (StringUtils.hasText((String) osV)) {
				criteria.add(Restrictions.like("osV", osV,MatchMode.ANYWHERE));
			}
			// 设置terminalType筛选条件
			if (terminalType != 0) {
				criteria.add(Restrictions.eq("terminalType", terminalType));
			}
			// 设置uploatTime筛选条件
			if ( pageList.getParam("startTime") != null &&  pageList.getParam("endTime") != null) {
				criteria.add(Restrictions.between("uploadTime", pageList.getParam("startTime"), pageList.getParam("endTime")));
			} else if(pageList.getParam("startTime") != null){
				criteria.add(Restrictions.gt("uploadTime", pageList.getParam("startTime")));
			} else if(pageList.getParam("endTime") != null){
				criteria.add(Restrictions.lt("uploadTime", pageList.getParam("endTime")));
			}
		}
		criteria.addOrder(Order.desc("id"));
		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
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
	 * 存储数据
	 * @author maxuancheng
	 * @param errorLogManagePojo
	 * @return
	 */
	public Boolean errorLogManageService(ErrorLogManagePojo errorLogManagePojo) {
		Boolean flag = false;
		try {
			this.create(errorLogManagePojo);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 根据城市名获取APP和Outum最高版本号及用户数(所有的)
	 * @author maxuancheng
	 * @param cityName
	 * @return
	 */
	public Map<String, Object> getVersionAndUserNumber(String cityName) {
		Session session = this.getHibernateSession();
		String sql = "select e.terminalType,e.softwareV,COUNT(e.softwareV) from ErrorLogManagePojo e"; 
		if(StringUtils.hasText(cityName)){
			sql = sql + " where e.city = '" + cityName  + "'";
		}
		sql = sql + " GROUP BY e.terminalType,e.softwareV";
		
		List<Object[]> list = session.createQuery(sql).list();
		Map<String, Object> map = new HashMap<>();
		Double newestAppVersion = 0.0;
		Integer newestAppUserSum = 0;
		Double newestOutumVersion = 0.0;
		Integer newestOutumUserSum = 0;
		Integer appUserSum = 0;
		Integer outumUserSum = 0;
		
		if(list != null && list.size() > 0){
			for (Object[] objects : list) {
				if((Integer)objects[0] == 1){
					if(Double.valueOf((String) objects[1]) > newestAppVersion){
						newestAppVersion = Double.valueOf((String) objects[1]);
						newestAppUserSum = Integer.valueOf(objects[2].toString());
					}
					appUserSum = appUserSum + Integer.valueOf(objects[2].toString());
				}
				
				if((Integer) objects[0] == 2){
					if(Double.valueOf((String) objects[1]) > newestOutumVersion){
						newestOutumVersion = Double.valueOf((String) objects[1]);
						newestOutumUserSum = Integer.valueOf(objects[2].toString());
					}
					outumUserSum = outumUserSum + Integer.valueOf(objects[2].toString());
				}
			}
		}
		
		map.put("newestAppVersion", newestAppVersion);
		map.put("newestOutumVersion", newestOutumVersion);
		map.put("newestAppUserSum", newestAppUserSum);
		map.put("newestOutumUserSum", newestOutumUserSum);
		map.put("otherAppVersionSum", appUserSum - newestAppUserSum);
		map.put("otherOutumVersionSum", outumUserSum - newestOutumUserSum);
		return map;
	}

	/**
	 * 根据boxid集合获取APP或Outum最高版本号和用户数
	 * @author maxuancheng
	 * @param cityName
	 * @param list
	 * @return
	 */
	public Map<String, Object> getOnLineVersionAndUserNumber(List<Object> list) {
		Map<String, Object> map = new HashMap<>();
		Double newestAppVersionOnLine = 0.0;
		Double newestOutumVersionOnLine = 0.0;
		Integer newestAppUserSumOnLine = 0;
		Integer newestOutumUserSumOnLine = 0;
		Integer appUserSumOnLine = 0;
		Integer outumUserSumOnLine = 0;
		if(list != null && list.size() > 0){
			Session session = this.getHibernateSession();
			StringBuilder boxidList = new StringBuilder();
			for(int i = 0; i < list.size(); i++){
				if(i == 0){
					boxidList.append(list.get(i).toString());
				}else{
					boxidList.append("," + list.get(i).toString());
				}
			}
			String sql = "select e.terminalType,e.softwareV,COUNT(e.softwareV) from ErrorLogManagePojo e where e.boxid in (" + boxidList +")";
			List<Object[]> numberList = session.createQuery(sql).list();
			
			if(list != null && list.size() > 0){
				for (Object[] objects : numberList) {
					if((Integer) objects[0] == 1){
						if(Double.valueOf((String) objects[1]) > newestAppVersionOnLine){
							newestAppVersionOnLine = Double.valueOf((String) objects[1]);
							newestAppUserSumOnLine = Integer.valueOf(objects[2].toString());
						}
						appUserSumOnLine = appUserSumOnLine + Integer.valueOf(objects[2].toString());
					}
					
					if((Integer)objects[0] == 2){
						if(Double.valueOf((String) objects[1]) > newestOutumVersionOnLine){
							newestOutumVersionOnLine = Double.valueOf((String) objects[1]);
							newestOutumUserSumOnLine = Integer.valueOf(objects[2].toString());
						}
						outumUserSumOnLine = outumUserSumOnLine + Integer.valueOf(objects[2].toString());
					}
				}
			}
		}
		map.put("newestAppVersionOnLine", newestAppVersionOnLine);
		map.put("newestOutumVersionOnLine", newestOutumVersionOnLine);
		map.put("newestAppUserSumOnLine", newestAppUserSumOnLine);
		map.put("newestOutumUserSumOnLine", newestOutumUserSumOnLine);
		map.put("otherAppVersionSumOnLine", appUserSumOnLine - newestAppUserSumOnLine);
		map.put("otherOutumVersionSumOnLine", outumUserSumOnLine - newestOutumUserSumOnLine);
		return map;
	}

	/**
	 * 根据城市名获取终端类型统计
	 * @author maxuancheng
	 * @param cityName 为null时获取所有城市
	 * @return
	 */
	public List<Object> getTerminalNumber(String cityName) {
		Session session = this.getHibernateSession();	
		String sql = "select t.num,t.s from("
				+ " select e.TERMINAL_NUMBER num,COUNT(e.TERMINAL_NUMBER) s from IADS_5G_ERROR_LOG_MANAGE e "
				+ " WHERE e.TERMINAL_TYPE = ";
		
		String sqlOfApp = sql + " 1";
		String sqlOfOutum = sql + " 2";
		if(StringUtils.hasText(cityName)){
			sqlOfApp = sqlOfApp + " AND CITY = '" + cityName + "'";
			sqlOfOutum = sqlOfOutum + " AND CITY = '" + cityName + "'";
		}
		sqlOfApp = sqlOfApp + " GROUP BY e.TERMINAL_NUMBER ORDER BY count(e.TERMINAL_NUMBER) DESC) t WHERE ROWNUM < 6";
		sqlOfOutum = sqlOfOutum + " GROUP BY e.TERMINAL_NUMBER ORDER BY count(e.TERMINAL_NUMBER) DESC) t WHERE ROWNUM < 6";
		//BigDecimal
		List<Object[]> listOfApp = session.createSQLQuery(sqlOfApp).list();
		List<Object[]> listOfOutum = session.createSQLQuery(sqlOfOutum).list();
		
		List<Object> list = new ArrayList<>();
		for(int i = 0;i < 5;i++){
			Map<String, Object> map = new HashMap<>();
			if(listOfApp.size() <= i){
				map.put("appTypeNumber", null);
				map.put("appInstallSum", null);
			}else{
				map.put("appTypeNumber", listOfApp.get(i)[0]);
				map.put("appInstallSum", listOfApp.get(i)[1]);
			}
			
			if(listOfOutum.size() <= i){
				map.put("outumTypeNumber", null);
				map.put("outumInstallSum", null);
			}else{
				map.put("outumTypeNumber", listOfOutum.get(i)[0]);
				map.put("outumInstallSum", listOfOutum.get(i)[1]);
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 获取用户活跃度和经纬度
	 * @author maxuancheng
	 * @return
	 */
	public List<List<Object>> getUserPlaceShow(List<String> colorList) {
		Session session = this.getHibernateSession();
		String sql = "select LON,LAT, "+
		" CASE "+
			" WHEN "+
				" ceil((TO_DATE(TO_CHAR(SYSDATE,'yyyy-mm-dd hh24-mi-ss'),'yyyy-mm-dd hh24-mi-ss')  "+
				" - TO_DATE(TO_CHAR(SOFTWARE_START_TIME,'yyyy-mm-dd hh24-mi-ss'),'yyyy-mm-dd hh24-mi-ss'))* 24) < 72  "+
			" THEN '" + colorList.get(0) + "' "+
			" WHEN "+
				" ceil((TO_DATE(TO_CHAR(SYSDATE,'yyyy-mm-dd hh24-mi-ss'),'yyyy-mm-dd hh24-mi-ss')  "+
				" - TO_DATE(TO_CHAR(SOFTWARE_START_TIME,'yyyy-mm-dd hh24-mi-ss'),'yyyy-mm-dd hh24-mi-ss'))* 24) < 168 "+
			" THEN '" + colorList.get(1) + "' "+
			" ELSE '" + colorList.get(2) + "' "+
		" END	  "+
		" from (  "+
				" select e.*, row_number() over(partition by e.BOXID order by e.SOFTWARE_START_TIME desc) rn from IADS_5G_ERROR_LOG_MANAGE e "+
		" ) t where t.rn <=1";
		List<Object[]> list = session.createSQLQuery(sql).list();
		List<List<Object>> array = new ArrayList<>();
		List<Object> list1 = new ArrayList<>();
		if(list != null && list.size() > 0){
			List<Object> LonAndLat = null;
			for(Object[] objects:list){
				LonAndLat = new ArrayList<>();
				LonAndLat.add(objects[0]);
				LonAndLat.add(objects[1]);
				LonAndLat.add(objects[2]);
				list1.add(LonAndLat);
				/*if(Integer.valueOf(objects[2].toString()) == 1){
				}else if(Integer.valueOf(objects[2].toString()) == 2){
					list2.add(LonAndLat);
				}else if(Integer.valueOf(objects[2].toString()) == 3){
					list3.add(LonAndLat);
				}*/
				array.add(LonAndLat);
			}
		}
		/*array.add(list1);
		array.add(list2);
		array.add(list3);*/
		return array;
	}

}
