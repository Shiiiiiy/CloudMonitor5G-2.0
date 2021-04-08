package com.datang.service.monitor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.web.beans.monitor.GPSRequestBean;

/**
 * 地图Service
 * 
 * @explain
 * @name MapMonitorService
 * @author shenyanwei
 * @date 2016年7月12日下午1:37:44
 */
public interface MapMonitorService {
	/**
	 * 
	 * 初始化查询GPS轨迹信息
	 * 
	 * @param pageList
	 * @return
	 */
	public List<Object> queryTerminalGpsPoint(List<String> boxIDs);

	/**
	 * 查询历史其他信息
	 * 
	 * @param gpsRequestBean
	 * @return
	 */
	public Map<String, EasyuiPageList> queryOtherInformation(
			GPSRequestBean gpsRequestBean);

	/**
	 * 获取多个终端实时地图监控实时刷新轨迹
	 * 
	 * @return
	 */
	public List<Object> queryGPSRefreshRequestParam(List<String> boxIDs,
			Map<String, Date> timeMap);

	/**
	 * 获取实时地图监控事件轨迹点信息
	 * 
	 * @return
	 */
	public List<Object> queryGPSEventRequestParam(Date begainDate,
			Date endDate, List<Integer> galleryNos, List<String> boxId);

	/**
	 * 根据时间和boxID查询单个终端历史轨迹信息
	 */
	public Map<String, EasyuiPageList> queryHistoryTerminalGpsPoint(
			Date begainTime, Date endTime, String boxID);

	/**
	 * 根据时间查询有Gps信息的BoxID
	 */
	public Set<String> queryGpsByTime(Date begainTime, Date endTime);
}
