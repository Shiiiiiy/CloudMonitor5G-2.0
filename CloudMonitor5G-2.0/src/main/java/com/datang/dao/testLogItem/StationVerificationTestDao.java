package com.datang.dao.testLogItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
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
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 单站验证日志
 * @author maxuancheng
 *
 */
@Repository
@SuppressWarnings("rawtypes")
public class StationVerificationTestDao extends GenericHibernateDao<StationVerificationLogPojo, Long>{

	public EasyuiPageList doPageQuery(PageList pageList){
		Criteria criteria = this.getHibernateSession().createCriteria(StationVerificationLogPojo.class);
		Criteria terminal = criteria.createCriteria("terminal");
		Criteria terminalGroup = terminal.createCriteria("terminalGroup");
		Object cityId =  pageList.getParam("cityId");
		Object testTimeStartQuery = pageList.getParam("testTimeStartQuery");
		Object testTimeEndQuery = pageList.getParam("testTimeEndQuery");
		Object cityidStrInit = pageList.getParam("cityidStrInit");
		
		StationVerificationLogPojo stationVerificationLogPojo = (StationVerificationLogPojo) pageList.getParam("stationVerificationLogPojo");
		if(cityId == null || Long.valueOf(cityId.toString()) == -1){
			Long[] cityidsArry = (Long[])ConvertUtils.convert(cityidStrInit.toString().split(","),Long.class);
			terminalGroup.add(Restrictions.in("id", cityidsArry));			
		}
		if(cityId != null && Long.valueOf(cityId.toString()) != -1){
			terminalGroup.add(Restrictions.eq("id", cityId));			
		}
		
		if(testTimeStartQuery != null){
			criteria.add(Restrictions.ge("testTime", Long.valueOf(testTimeStartQuery.toString())));
		}
		
		if(testTimeEndQuery != null){
			criteria.add(Restrictions.le("testTime", Long.valueOf(testTimeEndQuery.toString())));
		}
		
		if(StringUtils.hasText(stationVerificationLogPojo.getLogSource())){
			criteria.add(Restrictions.like("logSource", stationVerificationLogPojo.getLogSource(),MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText(stationVerificationLogPojo.getFileName())){
			criteria.add(Restrictions.like("fileName", stationVerificationLogPojo.getFileName(),MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText(stationVerificationLogPojo.getOperatorName())){
			criteria.add(Restrictions.eq("operatorName", stationVerificationLogPojo.getOperatorName()));
		}

		if(stationVerificationLogPojo.getTestFileStatus() != null){
			criteria.add(Restrictions.eq("testFileStatus", stationVerificationLogPojo.getTestFileStatus()));
		}

		if(StringUtils.hasText(stationVerificationLogPojo.getCorrelativeCell())){
			criteria.add(Restrictions.like("correlativeCell", stationVerificationLogPojo.getCorrelativeCell(),MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasText(stationVerificationLogPojo.getTestService())){
			if(stationVerificationLogPojo.getTestService().equals("PING#32") || stationVerificationLogPojo.getTestService().equals("PING#1500") ){
				criteria.add(Restrictions.like("testService", "PING",MatchMode.ANYWHERE));
			}else{
				criteria.add(Restrictions.eq("testService", stationVerificationLogPojo.getTestService()));
			}
		}

		if(StringUtils.hasText(stationVerificationLogPojo.getWirelessStatus())){
			criteria.add(Restrictions.eq("wirelessStatus", stationVerificationLogPojo.getWirelessStatus()));
		}
		
		criteria.addOrder(Order.desc("id"));

//		long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
//		criteria.setProjection(null);
		int rowsCount = pageList.getRowsCount();// 每页记录数
		int pageNum = pageList.getPageNum();// 页码
//		criteria.setFirstResult((pageNum - 1) * rowsCount);
//		criteria.setMaxResults(rowsCount);
		List<StationVerificationLogPojo> list = (List<StationVerificationLogPojo>)criteria.list();
		
		List<StationVerificationLogPojo> filterList = new ArrayList<StationVerificationLogPojo>();
		
		if(StringUtils.hasText(stationVerificationLogPojo.getTestService())){
			for (StationVerificationLogPojo realPojo : list) {
					if(realPojo.getTestService().contains("（") && realPojo.getTestService().contains("）")){
						String pingName = realPojo.getTestService().substring(realPojo.getTestService().indexOf("（")+1, realPojo.getTestService().indexOf("）")) ;
						
						if(stationVerificationLogPojo.getTestService().equals("PING#32")){
							if(StringUtils.hasText(pingName)){
								if (Float.valueOf(pingName)<200) { //ping32业务
									filterList.add(realPojo);
								}
							}
						}else if(stationVerificationLogPojo.getTestService().equals("PING#1500")){
							if(StringUtils.hasText(pingName)){
								if (Float.valueOf(pingName)>=200) { //ping1500业务
									filterList.add(realPojo);
								}
							}
						}
					}else{
						filterList.add(realPojo);
					}
			}
		}else{
			filterList = list;
		}
		
		List<StationVerificationLogPojo> realList = new ArrayList<StationVerificationLogPojo>();
		for (int i = (pageNum - 1) * rowsCount; i < pageNum*rowsCount; i++) {
			if(i<filterList.size()){
				realList.add(filterList.get(i));
			}
		}
		
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(realList);
		easyuiPageList.setTotal(filterList.size() + "");
		return easyuiPageList;
	}

	public List<StationVerificationLogPojo> findByCorrelativeCell(String cellName,String event,String wireStatus) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationVerificationLogPojo.class);
		if(StringUtils.hasText(cellName)){
			List<String> cellNamelist = Arrays.asList(cellName.replace(" ", "").split(","));
			criteria.add(Restrictions.in("correlativeCell", cellNamelist));
		}
		if(StringUtils.hasText(event)){
			List<String> eventlist = Arrays.asList(event.replace(" ", "").split(","));
			criteria.add(Restrictions.in("testService",eventlist));
		}
		if(StringUtils.hasText(wireStatus)){
			List<String> wirelist = Arrays.asList(wireStatus.replace(" ", "").split(","));
			criteria.add(Restrictions.in("wirelessStatus",wirelist));
		}
		
		return criteria.list();
	}
	
	
	public List<StationVerificationLogPojo> findByBoxid(String boxId) {
		Criteria criteria = this.getHibernateSession().createCriteria(StationVerificationLogPojo.class);
		criteria.add(Restrictions.eq("boxId", boxId));
		return criteria.list();
	}
	
	public List<StationVerificationLogPojo> findOfBoxidLogName(String boxId,List<String> logNames){
		Criteria criteria = this.getHibernateSession().createCriteria(StationVerificationLogPojo.class);
		criteria.add(Restrictions.eq("boxId", boxId));
		criteria.add(Restrictions.in("fileName", logNames));
		return criteria.list();
	}
}
