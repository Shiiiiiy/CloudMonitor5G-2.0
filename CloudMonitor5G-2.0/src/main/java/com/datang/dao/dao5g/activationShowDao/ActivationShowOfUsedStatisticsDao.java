package com.datang.dao.dao5g.activationShowDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.DateComputeUtils;
import com.datang.domain.domain5g.activationShow.ActivationShowPojo;

@Repository
public class ActivationShowOfUsedStatisticsDao extends GenericHibernateDao<ActivationShowPojo, Long> {
	
	/**
	 * 根据城市id获取设备应用统计
	 * @author maxuancheng
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public Map<String, Object> getTerminalUsedStatistics(String cityId) {
		Session session = this.getHibernateSession();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date thisDate = new Date();
		//Date dateComputeOfHour = DateComputeUtils.DateComputeOfHour(thisDate, 2);
		String startDate = sdf.format(thisDate);
		String endDate = sdf.format(DateComputeUtils.DateComputeOfDay(thisDate, -1));
		String sql = "select SUBSTR(TO_CHAR(t.startTime,'yyyy-mm-dd hh24'),12) as dateElement,t.softwareType,count(1),sum(t.useTime)/count(1) from ActivationShowPojo t";
		if("-2".equals(cityId) || !StringUtils.hasText(cityId)){
			sql = sql + " where ";
		}else{
			sql = sql + " where TERMINAL_GROUP_ID = "+ cityId +" and ";
		}
		sql = sql + "'"+ endDate +"' <= TO_CHAR(t.startTime,'yyyy-mm-dd hh24')"
				+ " and '"+ startDate +"' >= TO_CHAR(t.startTime,'yyyy-mm-dd hh24')"
				+ " GROUP BY TO_CHAR(t.startTime,'yyyy-mm-dd hh24'),t.softwareType  ORDER BY TO_CHAR(START_TIME,'yyyy-mm-dd hh24') ASC";
		List<Object[]> list = session.createQuery(sql).list();
		Map<String, Object> map = new HashMap<>();
		ArrayList<Object> softwareOpenNumberList = new ArrayList<>();
		ArrayList<Object> softwareOpenTimeList = new ArrayList<>();
		ArrayList<Object> hardwareareOpenNumberList = new ArrayList<>();
		ArrayList<Object> hardwareareOpenTimeList = new ArrayList<>();
		ArrayList<Object> tList = new ArrayList<>();
		Integer hour = Integer.valueOf(endDate.substring(11, 13));
		hour = hour + 2;
		if(hour > 24){
			hour = hour - 24;
		}
		
		if(list != null && list.size() > 0){
			Map<Integer, Integer> softwareOpenNumberMap = new LinkedMap();
			Map<Integer, Integer> softwareOpenTimeMap = new LinkedMap();
			Map<Integer, Integer> hardwareareOpenNumberMap = new LinkedMap();
			Map<Integer, Integer> hardwareareOpenTimeMap = new LinkedMap();
			for (Object[] objects : list) {
				//包含四个元素  0:小时时间点   1:类型  2:启动次数  3:使用时间均值
				if((Integer) objects[1] == 1){
					softwareOpenNumberMap.put(Integer.valueOf((String) objects[0]), Integer.valueOf(objects[2].toString()));
					softwareOpenTimeMap.put(Integer.valueOf((String) objects[0]), Integer.valueOf(objects[3].toString()));
				}else if((Integer) objects[1] == 2){
					hardwareareOpenNumberMap.put(Integer.valueOf((String) objects[0]), Integer.valueOf(objects[2].toString()));
					hardwareareOpenTimeMap.put(Integer.valueOf((String) objects[0]), Integer.valueOf(objects[3].toString()));
				}
			}
			
			for(int i = 0;i < 12;i++){
				if(i == 11){
					tList.add(hour);
					if(softwareOpenNumberMap.get(hour) == null){
						softwareOpenNumberList.add(0);
					}else{
						softwareOpenNumberList.add(softwareOpenNumberMap.get(hour));
					}
					if(softwareOpenTimeMap.get(hour) == null){
						softwareOpenTimeList.add(0);
					}else{
						softwareOpenTimeList.add(softwareOpenTimeMap.get(hour));
					}
					if(hardwareareOpenNumberMap.get(hour) == null){
						hardwareareOpenNumberList.add(0);
					}else{
						hardwareareOpenNumberList.add(hardwareareOpenNumberMap.get(hour));
					}
					if(hardwareareOpenTimeMap.get(hour) == null){
						hardwareareOpenTimeList.add(0);
					}else{
						hardwareareOpenTimeList.add(hardwareareOpenTimeMap.get(hour));
					}
				}else{
					tList.add(hour);
					int softwareOpenSum = 0;
					int softwareTimeSum = 0;
					int hardwareareOpenSum = 0;
					int hardwareareTimeSum = 0;
					for(int j = 0;j < 2;j++){
						if(softwareOpenNumberMap.get(hour) != null){
							softwareOpenSum = softwareOpenSum + softwareOpenNumberMap.get(hour);
						}
						if(softwareOpenTimeMap.get(hour) != null){
							softwareTimeSum = softwareTimeSum + softwareOpenTimeMap.get(hour);
						}
						if(hardwareareOpenNumberMap.get(hour) != null){
							hardwareareOpenSum = hardwareareOpenSum + hardwareareOpenNumberMap.get(hour);
						}
						if(hardwareareOpenTimeMap.get(hour) != null){
							hardwareareTimeSum = hardwareareTimeSum + hardwareareOpenTimeMap.get(hour);
						}
						if(hour == 23){
							hour = 0;
						}else{
							hour = hour + 1;
						}
					}
					softwareOpenNumberList.add(softwareOpenSum);
					softwareOpenTimeList.add(softwareTimeSum);
					hardwareareOpenNumberList.add(hardwareareOpenSum);
					hardwareareOpenTimeList.add(hardwareareTimeSum);
				}
			}
		}else{
			for(int i = 0;i < 12;i++){
				if(hour >= 24){
					tList.add(hour - 24);
				}else{
					tList.add(hour);
				}
				hour = hour + 2;
				softwareOpenNumberList.add(0);
				softwareOpenTimeList.add(0);
				hardwareareOpenNumberList.add(0);
				hardwareareOpenTimeList.add(0);
			}
		}
		map.put("softwareOpenNumberList", softwareOpenNumberList);
		map.put("softwareOpenTimeList", softwareOpenTimeList);
		map.put("hardwareareOpenNumberList", hardwareareOpenNumberList);
		map.put("hardwareareOpenTimeList", hardwareareOpenTimeList);
		map.put("tList", tList);
		return map;
	}
}
