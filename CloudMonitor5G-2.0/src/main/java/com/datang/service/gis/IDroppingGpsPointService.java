/**
 * 
 */
package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 切换失败Gps轨迹点Service
 * 
 * @author shenyanwei
 * @date 2016年5月5日上午10:32:10
 */
public interface IDroppingGpsPointService {

	/**
	 * 根据切换失败id和指标类型获取切换失败指标轨迹点
	 * 
	 * @param volteDroppingId
	 *            切换失败id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByvolteDroppingIdAndIndexType(
			Long volteDroppingId, Integer indexType);

	/**
	 * 根据切换失败id和指标类型获取切换失败指标行驶方向轨迹点
	 * 
	 * @param droppingId
	 *            异常事件ID
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByDroppingIdAndIndexType(
			Long droppingId, Integer indexType);

	/**
	 * 根据切换失败id获取切换失败轨迹点
	 * 
	 * @param volteDroppingId
	 *            切换失败id
	 * 
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByvolteDroppingId(
			Long volteDroppingId);

	/**
	 * 获取测试日志下的某个类型切换失败下的某个类型失败图标的所有轨迹点
	 * 
	 * @param testLogItemIds
	 * @param iconType
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getEventPointsByTestlogIdsAndIconType(
			String testLogItemIds, Integer iconType, Integer deType);

	/**
	 * 根据切换失败id获取该切换失败下所有图标轨迹点
	 * 
	 * @param DEId
	 *            切换失败ID
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getEventPointsByHopIdAndIndexType(
			Long DEId);

	/**
	 * 获取测试日志下的某个切换失败的所有轨迹点
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<List<TestLogItemGpsPoint>> getTheEveryDroppingGpsPointsByTestlogIdsAndHofType(
			String testLogItemIds, Integer hofType);
}
