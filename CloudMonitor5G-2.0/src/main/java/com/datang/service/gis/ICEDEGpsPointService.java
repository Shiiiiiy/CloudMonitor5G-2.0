package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * @explain 呼叫建立时延异常Gps Service
 * @name ICEDEGpsPointService
 * @author shenyanwei
 * @date 2016年6月27日上午8:50:35
 */
public interface ICEDEGpsPointService {
	/**
	 * 根据切换失败id和指标类型获取呼叫建立时延异常指标轨迹点
	 * 
	 * @param cedeId
	 *            呼叫建立时延异常id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByCedeIdAndIndexType(
			Long cedeId, Integer indexType);

	/**
	 * 根据呼叫建立时延异常id和指标类型获取呼叫建立时延异常指标行驶方向轨迹点
	 * 
	 * @param cedeId
	 *            异常事件ID
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByCedeIdAndIndexType(
			Long droppingId, Integer indexType);

	/**
	 * 根据呼叫建立时延异常id获取呼叫建立时延异常轨迹点
	 * 
	 * @param cedeId
	 *            切换失败id
	 * 
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByCedeId(Long cedeId);

	/**
	 * 获取测试日志下的某个呼叫建立时延异常下的某个类型失败图标的所有轨迹点
	 * 
	 * @param testLogItemIds
	 * @param iconType
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getEventPointsByTestlogIdsAndIconType(
			String testLogItemIds, Integer iconType);

	/**
	 * 根据呼叫建立时延异常id获取该呼叫建立时延异常下所有图标轨迹点
	 * 
	 * @param cedeId
	 *            切换失败ID
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getEventPointsByCedeIdAndIndexType(
			Long cedeId);

	/**
	 * 获取测试日志下的某个呼叫建立时延异常的所有轨迹点
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<List<Object>> getTheEveryGpsPointsByTestlogIdsAndCedeType(
			String testLogItemIds);
}
