/**
 * 
 */
package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 异常事件轨迹点service接口
 * 
 * @author yinzhipeng
 * @date:2016年5月3日 下午1:26:42
 * @modify:yinzhipeng 2017年7月24日
 * @version 1.5.2
 */
public interface IEEGpsPointService {
	/**
	 * 根据异常事件id和指标类型获取异常事件指标轨迹点
	 * 
	 * @param EEId
	 *            异常事件ID
	 * @param indexType
	 *            指标类型
	 * @param callType
	 *            主被叫
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByEEIdAndIndexType(
			Long EEId, Integer indexType, Integer callType);

	/**
	 * 根据异常事件id和指标类型获取异常事件指标行驶方向轨迹点
	 * 
	 * @param EEId
	 *            异常事件ID
	 * @param indexType
	 *            指标类型
	 * @param callType
	 *            主被叫
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByEEIdAndIndexType(
			Long EEId, Integer indexType, Integer callType);

	/**
	 * 根据异常事件id获取异常事件轨迹点
	 * 
	 * @param EEId
	 *            异常事件ID
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByEEId(Long EEId);

	/**
	 * 获取测试日志下的某个类型异常事件下的某个类型事件图标的所有轨迹点
	 * 
	 * @param testLogItemIds
	 * @param iconType
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getEventPointsByTestlogIdsAndIconType(
			String testLogItemIds, String iconType, Integer eeType);

	/**
	 * 根据异常事件id获取该异常事件下所有事件图标轨迹点
	 * 
	 * @param EEId
	 *            异常事件ID
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getEventPointsByEEIdAndIndexType(
			Long EEId, Integer callType);

	/**
	 * 获取测试日志下的某个类型异常事件的所有轨迹点
	 * 
	 * @param testLogItemIds
	 * @param eeType
	 *            异常事件类型
	 * @return
	 */
	public List<List<TestLogItemGpsPoint>> getEEGpsPointsByTestlogIdsAndEEType(
			String testLogItemIds, Integer eeType);

}
