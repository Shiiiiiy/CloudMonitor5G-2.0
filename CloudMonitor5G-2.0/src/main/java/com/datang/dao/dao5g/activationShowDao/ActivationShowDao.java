package com.datang.dao.dao5g.activationShowDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.DateComputeUtils;
import com.datang.domain.testManage.terminal.Terminal;

@Repository
@SuppressWarnings("unchecked")
public class ActivationShowDao extends GenericHibernateDao<Terminal, Long> {

	/**
	 * 当前总在线数
	 * @author maxuancheng
	 * @param cityId 城市id，如果为空或者为-2则查询所有
	 * @return
	 */
	public Map<String, Long> getUserNumber(String cityId) {
		Session session = this.getHibernateSession();
//		String sqlCityId = "select * from IADS_TERMINAL_GROUP where id =" + cityId;
//		List<Object> groupId = session.createQuery(sqlCityId).list();
		String sql = "select t.online,COUNT(1) from Terminal t"; 
		if("-2".equals(cityId) || !StringUtils.hasText(cityId)){
			sql = sql + " GROUP BY P_ONLINE";
		}else{
			sql = sql + " where t.terminalGroup = "+ cityId +" GROUP BY P_ONLINE";
		}
		List<Object[]> list = session.createQuery(sql).list();
		Map<String, Long> map = new HashMap<>();
		Long onlineUser = 0L;
		if(list != null && list.size() > 0){
			for (Object[] objects : list) {
				if((boolean)objects[0] == true){
					onlineUser = (Long)objects[1];
				}
			}
		}
		map.put("onlineUser", onlineUser);
		return map;
	}

	/**
	 * 获取硬件最高版本号和用户数
	 * @author maxuancheng
	 * @param cityId 城市id，如果为空或者为-2则查询所有
	 * @return
	 */
	public Map<String, Object> getHardwareVersionAndUserNumber(String cityId) {
		Session session = this.getHibernateSession();
		String sql = "select t.hardwareVersion,COUNT(1) from Terminal t"; 
		if("-2".equals(cityId) || !StringUtils.hasText(cityId)){
			sql = sql + " GROUP BY HARDWRE_VERSION";
		}else{
			sql = sql + "where TERMINAL_GROUP_ID = "+ cityId +" GROUP BY HARDWRE_VERSION";
		}
		List<Object[]> list = session.createQuery(sql).list();
		Map<String, Object> map = new HashMap<>();
		Long newestHardwareVersionNumber = 0L;
		Double newestHardwareVersion = 0.0;
		Long hardwareSum = 0L;
		
		if(list != null && list.size() > 0){
			for (Object[] objects : list) {
				if(objects[0] != null){
					if(Double.valueOf((String) objects[0]) > newestHardwareVersion){
						newestHardwareVersion = Double.valueOf((String) objects[0]);
						newestHardwareVersionNumber = (Long)objects[1];
					}
				}
				hardwareSum = hardwareSum + (Long)objects[1];
			}
		}
		
		map.put("newestHardwareVersion", newestHardwareVersion);
		map.put("newestHardwareVersionNumber", newestHardwareVersionNumber);
		map.put("otherHardwareVersionNumber", hardwareSum - newestHardwareVersionNumber);
		return map;
	}

	/**
	 * 获取软件最高版本号和用户数
	 * @author maxuancheng
	 * @param cityId 城市id，如果为空或者为-2则查询所有
	 * @return
	 */
	public Map<String, Object> getSoftwareVersionAndUserNumber(String cityId) {
		Session session = this.getHibernateSession();
		String sql = "select t.softwareVersion,COUNT(1) from Terminal t"; 
		if("-2".equals(cityId) || !StringUtils.hasText(cityId)){
			sql = sql + " GROUP BY SOFTWARE_VERSION";
		}else{
			sql = sql + "where TERMINAL_GROUP_ID = "+ cityId +" GROUP BY SOFTWARE_VERSION";
		}
		List<Object[]> list = session.createQuery(sql).list();
		Map<String, Object> map = new HashMap<>();
		
		Long newestSoftwareVersionNumber = 0L;
		Double newestSoftwareVersion = 0.0;
		Long softwareSum = 0L;
		
		if(list != null && list.size() > 0){
			for (Object[] objects : list) {
				if(objects[0] != null){
					if(Double.valueOf((String) objects[0]) > newestSoftwareVersion){
						newestSoftwareVersion = Double.valueOf((String) objects[0]);
						newestSoftwareVersionNumber = (Long)objects[1];
					}
				}
				softwareSum = softwareSum + (Long)objects[1];
			}
		}
		
		map.put("newestSoftwareVersion", newestSoftwareVersion);
		map.put("newestSoftwareVersionNumber", newestSoftwareVersionNumber);
		map.put("otherSoftwareVersionNumber", softwareSum - newestSoftwareVersionNumber);
		return map;
	}

	/**
	 * 获取在线用户boxid
	 * @author maxuancheng
	 * @param cityId
	 * @return
	 */
	public List<Object> getOnLineUserBoxid(String cityId) {
		Session session = this.getHibernateSession();
		String sql = "select t.boxId from Terminal t where P_ONLINE = 1"; 
		if(!"-2".equals(cityId) && StringUtils.hasText(cityId)){
			sql = sql + " and t.terminalGroup = "+ cityId +" GROUP BY t.boxId";
		}
		List<Object> list = session.createQuery(sql).list();
		return list;
	}

}
