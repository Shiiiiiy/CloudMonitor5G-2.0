/**
 * 
 */
package com.datang.dao.mos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.domain.mos.AppTestInfo;

/**
 * APP上传测试信息DAO
 * 
 * @author yinzhipeng
 * @date:2015年11月4日 下午6:05:47
 * @version
 */
@Repository
@SuppressWarnings("all")
public class AppTestInfoDAO extends GenericHibernateDao<AppTestInfo, Long> {

	/**
	 * @param pageList
	 * @return
	 */
	public EasyuiPageList pageList(PageList pageList) {
		if (null == pageList) {
			return new EasyuiPageList();
		}
		
		Criteria createCriteria = getParam(pageList);
		
		createCriteria.addOrder(Order.desc("id"));
		long total = (Long) createCriteria
				.setProjection(Projections.rowCount()).uniqueResult();
		createCriteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
		createCriteria.setFirstResult((pageNum - 1) * rowsCount);
		createCriteria.setMaxResults(rowsCount);
		List list = createCriteria.list();
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(list);
		easyuiPageList.setTotal(total + "");
		return easyuiPageList;
	}
	
	/**
	 * 城市维度分析粒度平均值
	 * @param pageList
	 * @return
	 */
	public List<AppTestInfo> getCityAnalysisInfo(PageList pageList) {
		if (null == pageList) {
			return new ArrayList<AppTestInfo>();
		}
		
		Session session = this.getHibernateSession();
		
		Object beginDate = pageList.getParam("beginDate");
		Object endDate = pageList.getParam("endDate");
		Object provinces = pageList.getParam("provinces");
		Object citys = pageList.getParam("citys");
		Object statisticType = pageList.getParam("statisticType");
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("select AVG(e.nrRsrp) as nrRsrp,AVG(e.nrSinr) as nrSinr,AVG(e.lteNetwork) as lteNetwork,AVG(e.sinr) as sinr,"
				+ "AVG(e.pingDelay) as pingDelay,AVG(e.upSpeedAvg) as upSpeedAvg,AVG(e.downSpeedAvg) as downSpeedAvg,"
				+ "AVG(e.vmos) as vmos,AVG(e.sQuality) as sQuality,AVG(e.sLoading) as sLoading,AVG(e.sStalling) as sStalling,"
				+ "AVG(e.httpDlRate) as httpDlRate,AVG(e.httpTimeDelay) as httpTimeDelay, ");
		
		String region = "";
		String groupSQL ="";
		List<String> regionList = new ArrayList<String>();
		if(statisticType.toString().equals("0")){
			if(StringUtils.hasText((String)provinces)){
				regionList = Arrays.asList(provinces.toString().replace(" ","").split(","));
				hql.append("province from AppTestInfo e where province in (:alist) ");
				groupSQL = "GROUP BY province";
			}
		}else if(statisticType.toString().equals("1")){
			if(StringUtils.hasText((String)citys)){
				regionList = Arrays.asList(citys.toString().replace(" ","").split(","));
				hql.append("province,city from AppTestInfo e where city in (:alist) ");
				groupSQL = "GROUP BY city,province";
			}
		}else if(statisticType.toString().equals("2")){
			if(StringUtils.hasText((String)citys)){
				regionList = Arrays.asList(citys.toString().replace(" ","").split(","));
				hql.append("province,city,district from AppTestInfo e where city in (:alist) ");
				groupSQL = "GROUP BY district,province,city";
			}
		}
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//防止日期为空
		String beginDateStr="1970-01-01 00:00:00";
		String endDateStr="2500-01-01 00:00:00";
		if(beginDate!=null){
			beginDateStr = sf.format(beginDate); 
			hql.append("and timestamp >= '"+beginDateStr+"' ");
		}
		
		if(endDate!=null){
			endDateStr = sf.format(endDate); 
			hql.append("and timestamp <= '"+endDateStr+"' ");
		}
		hql.append(groupSQL);
		
		Query createQuery = session.createQuery(new String(hql));
		createQuery.setParameterList("alist", regionList);
		
		List<Object[]> list = createQuery.list();
		List<AppTestInfo> appinfo = new ArrayList<AppTestInfo>();
		if(list!=null && list.size()>0){
			DecimalFormat format = new DecimalFormat("0.0");
			for (Object[] objects : list) {
				AppTestInfo info = new AppTestInfo();
				if(objects[0]!=null){
					info.setNrRsrp(format.format(new BigDecimal(objects[0].toString())));
				}
				if(objects[1]!=null){
					info.setNrSinr(format.format(new BigDecimal(objects[1].toString())));
				}
				if(objects[2]!=null){
					info.setLteNetwork(format.format(new BigDecimal(objects[2].toString())));
				}
				if(objects[3]!=null){
					info.setSinr(format.format(new BigDecimal(objects[3].toString())));
				}
				if(objects[4]!=null){
					info.setPingDelay(format.format(new BigDecimal(objects[4].toString())));
				}
				if(objects[5]!=null){
					info.setUpSpeedAvg(format.format(new BigDecimal(objects[5].toString())));
				}
				if(objects[6]!=null){
					info.setDownSpeedAvg(format.format(new BigDecimal(objects[6].toString())));
				}
				if(objects[7]!=null){
					info.setVmos(format.format(new BigDecimal(objects[7].toString())));
				}
				if(objects[8]!=null){
					info.setsQuality(format.format(new BigDecimal(objects[8].toString())));
				}
				if(objects[9]!=null){
					info.setsLoading(format.format(new BigDecimal(objects[9].toString())));
				}
				if(objects[10]!=null){
					info.setsStalling(format.format(new BigDecimal(objects[10].toString())));
				}
				if(objects[11]!=null){
					info.setHttpDlRate(Math.round(Double.valueOf(objects[11].toString())));
				}
				if(objects[12]!=null){
					info.setHttpTimeDelay(Math.round(Double.valueOf(objects[12].toString())));
				}
				if(statisticType.toString().equals("0")){
					//保存省字段
					info.setProvince((String)objects[13]);
				}else if(statisticType.toString().equals("1")){
					//保存省字段
					info.setProvince((String)objects[13]);
					//保存市字段
					info.setCity((String)objects[14]);
				}else if(statisticType.toString().equals("2")){
					//保存省字段
					info.setProvince((String)objects[13]);
					//保存市字段
					info.setCity((String)objects[14]);
					//保存区字段
					info.setDistrict((String)objects[15]);
				}
				appinfo.add(info);
			}
		}
		
		return appinfo;
	}
	
	public List<AppTestInfo> findAppTestInfo(PageList pageList){
		Criteria createCriteria = getParam(pageList);
		
		createCriteria.add(Restrictions.isNotNull("province"));
		createCriteria.add(Restrictions.isNotNull("city"));
		
		createCriteria.addOrder(Order.desc("id"));
		List list = createCriteria.list();
		return list;
	}
	
	/**
	 * 根据条件查询分析粒度排名
	 * @author lucheng
	 * @date 2020年9月22日 下午1:57:10
	 * @param pageList 参数值集合
	 * @param rankType AppTestInfo对象参数名称，不能为空
	 * @return
	 */
	public Map<String, Object> findRankData(PageList pageList , String rankType){
		Session session = this.getHibernateSession();
		
		Object beginDate = pageList.getParam("beginDate");
		Object endDate = pageList.getParam("endDate");
		Object provinces = pageList.getParam("provinces");
		Object citys = pageList.getParam("citys");
		Object statisticType = pageList.getParam("statisticType");
		
		StringBuffer hql = new StringBuffer();
		String groupSQL = "";
		List<String> regionList = new ArrayList<String>();
		String sortType ="";
		
		if(rankType!=null){
			hql.append("select AVG("+rankType+") as rankValue, ");
			if(rankType.equals("pingDelay")){
				//（升序）
				sortType = "asc";
			}else{
				//（降序）
				sortType = "desc";
			}
		}else{
			return null;
		}
		
		if(statisticType.toString().equals("0")){
			if(StringUtils.hasText((String)provinces)){
				regionList = Arrays.asList(provinces.toString().replace(" ","").split(","));
//				sql = sql + "province from AppTestInfo e where "+rankType+" is not null and province is not null and province is in (:alist) GROUP BY province ORDER BY AVG("+rankType+") "+sortType;
				hql.append("province from AppTestInfo e where "+rankType+" is not null and province in (:alist) ");
				groupSQL = "GROUP BY province ORDER BY AVG("+rankType+") "+sortType;
			}
		}else if(statisticType.toString().equals("1")){
			if(StringUtils.hasText((String)citys)){
				regionList = Arrays.asList(citys.toString().replace(" ","").split(","));
				hql.append("city from AppTestInfo e where "+rankType+" is not null and city in (:alist) ");
				groupSQL = "GROUP BY city ORDER BY AVG("+rankType+") "+sortType;
			}
		}else if(statisticType.toString().equals("2")){
			if(StringUtils.hasText((String)citys)){
				regionList = Arrays.asList(citys.toString().replace(" ","").split(","));
				hql.append("district from AppTestInfo e where "+rankType+" is not null and city in (:alist) ");
				groupSQL = "GROUP BY district ORDER BY AVG("+rankType+") "+sortType;
			}
		}
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginDateStr="1970-01-01 00:00:00";
		String endDateStr="2500-01-01 00:00:00";
		if(beginDate!=null){
			beginDateStr = sf.format(beginDate); 
			hql.append("and timestamp >= '"+beginDateStr+"' ");
		}
		
		if(endDate!=null){
			endDateStr = sf.format(endDate); 
			hql.append("and timestamp <= '"+endDateStr+"' ");
		}
		
		hql.append(groupSQL);
		
		Query createQuery = session.createQuery(new String(hql));
		createQuery.setParameterList("alist", regionList);
		
		List<Object[]> list = createQuery.list();
		Map<String, Object> map = new HashMap<>();
		
		if(list != null && list.size() > 0){
			for (int i=0;i<list.size();i++) {
				if(list.get(i)[1].toString() != null){
					map.put(list.get(i)[1].toString(), i+1);
				}
			}
		}
		return map;
	}
	
	/*
	 * 创建Criteria，根据pageList的参数创建查询条件
	 */
	public Criteria getParam(PageList pageList) {
		Object beginDate = pageList.getParam("beginDate");
		Object endDate = pageList.getParam("endDate");
		Object provinces = pageList.getParam("provinces");
		Object citys = pageList.getParam("citys");
		Object statisticType = pageList.getParam("statisticType");
		
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				AppTestInfo.class);
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(beginDate!=null){
			String beginDateStr = sf.format(beginDate); 
			createCriteria.add(Restrictions.ge("timestamp", beginDateStr));
		}
		
		if(endDate!=null){
			String endDateStr = sf.format(endDate); 
			createCriteria.add(Restrictions.le("timestamp", endDateStr));
		}
		
		if(!StringUtils.hasText((String)statisticType)){
			statisticType = "1";
		}
		
		//分析粒度类型
		if(statisticType.toString().equals("0")){
			//省
			if(StringUtils.hasText((String)provinces)){
				List<String> provincesList = Arrays.asList(provinces.toString().replace(" ","").split(","));
				createCriteria.add(Restrictions.in("province", provincesList));
			}
		}else if(statisticType.toString().equals("1")){
			//市
			if(StringUtils.hasText((String)citys)){
				List<String> citysList = Arrays.asList(citys.toString().replace(" ","").split(","));
				createCriteria.add(Restrictions.in("city", citysList));
			}
		}else if(statisticType.toString().equals("2")){
			//区，默认所有区全选
			if(StringUtils.hasText((String)citys)){
				List<String> citysList = Arrays.asList(citys.toString().replace(" ","").split(","));
				createCriteria.add(Restrictions.in("city", citysList));
			}
		}
		
		return createCriteria;
	}
}
