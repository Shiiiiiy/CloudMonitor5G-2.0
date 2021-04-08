package com.datang.dao.monitor;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.monitor.GPS;

/**
 * 地图Dao
 * 
 * @explain
 * @name GPSDao
 * @author shenyanwei
 * @date 2016年7月11日下午2:10:48
 */
@Repository
@SuppressWarnings("all")
public class GPSDao extends GenericHibernateDao<GPS, Long> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GPSDao.class);

	/**
	 * 历史轨迹多条件查询
	 * 
	 * @param pageList
	 * @return
	 */
	public List<GPS> queryHistoryTerminalGpsPoint(Date begainTime,
			Date endTime, String boxID) {
		Criteria criteria = this.getHibernateSession()
				.createCriteria(GPS.class);

		// 筛选参数日志开始时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("gpsTimeLong", begainTime.getTime()));
		}
		// 筛选参数日志结束时间
		if (null != endTime) {
			criteria.add(Restrictions.le("gpsTimeLong", endTime.getTime()));
		}
		// 筛选参数boxid
		if (null != boxID) {
			criteria.add(Restrictions.eq("boxId", boxID.trim()));
		}
		// latitude 16.585919~53.743108
		// longitude 73.386567~135.300369
		// 筛选经纬度
		// 筛选经纬度
		criteria.add(Restrictions.between("latitude",
				Float.valueOf("16.585919"), Float.valueOf("53.743108")));
		criteria.add(Restrictions.between("longitude",
				Float.valueOf("73.386567"), Float.valueOf("135.300369")));
		criteria.addOrder(Order.desc("gpsTimeLong"));
		criteria.addOrder(Order.desc("gpsTimeLong"));
		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;
	}

	/**
	 * 初始化查询GPS轨迹信息
	 * 
	 * @param boxIDs
	 * @return
	 */
	public List<GPS> queryTerminalGpsPoint(Date begain, Date end,
			List<String> boxIDs) {
		Criteria criteria = this.getHibernateSession()
				.createCriteria(GPS.class);
		// Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.HOUR_OF_DAY, -1);// 1小时前
		// Date begain = cal.getTime();
		// 筛选参数日志开始时间
		if (null != begain) {
			criteria.add(Restrictions.ge("gpsTimeLong", begain.getTime()));
		}
		// 筛选参数日志结束时间
		if (null != end) {
			criteria.add(Restrictions.le("gpsTimeLong", end.getTime()));
		}
		// 筛选参数boxid
		if (null != boxIDs && 0 != boxIDs.size()) {
			criteria.add(Restrictions.in("boxId", boxIDs));
		}
		// 筛选经纬度
		criteria.add(Restrictions.between("latitude",
				Float.valueOf("16.585919"), Float.valueOf("53.743108")));
		criteria.add(Restrictions.between("longitude",
				Float.valueOf("73.386567"), Float.valueOf("135.300369")));
		criteria.addOrder(Order.desc("gpsTimeLong"));
		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;
	}

	/**
	 * 获取多个终端实时地图监控实时刷新轨迹
	 * 
	 * @param boxIDs
	 * @param begainTime
	 * @return
	 */
	public List<GPS> queryGPSRefreshRequestParam(String boxIDs, Date begainTime) {
		Criteria criteria = this.getHibernateSession()
				.createCriteria(GPS.class);
		// 筛选参数日志开始时间
		if (null != begainTime) {
			criteria.add(Restrictions.ge("gpsTimeLong", begainTime.getTime()));
		}
		// 筛选参数boxid
		if (null != boxIDs) {
			criteria.add(Restrictions.eq("boxId", boxIDs.trim()));
		}
		// 筛选经纬度
		// 筛选经纬度
		criteria.add(Restrictions.between("latitude",
				Float.valueOf("16.585919"), Float.valueOf("53.743108")));
		criteria.add(Restrictions.between("longitude",
				Float.valueOf("73.386567"), Float.valueOf("135.300369")));
		criteria.addOrder(Order.desc("gpsTimeLong"));
		criteria.addOrder(Order.desc("gpsTimeLong"));

		List list = criteria.list();
		if (list != null && list.size() != 0) {
			return list;
		} else
			return null;
	}

	/**
	 * 
	 * @param ciRecord
	 */
	public void addGps(GPS gps) {
		create(gps);
	}

	/**
	 * 
	 * @param ciRecord
	 */
	public void updateGps(GPS gps) {
		update(gps);
	}

	/**
	 * 
	 * @param ciRecord
	 */
	public void deleteGPS(GPS gps) {
		delete(gps);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public GPS queryGps(Long id) {
		return find(id);
	}

}