/**
 * 
 */
package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 视频质差轨迹点service接口
 * 
 * @explain
 * @name IVQBGpsPointService
 * @author shenyanwei
 * @date 2017年5月23日下午1:57:33
 */
public interface IVQBGpsPointService {

	/**
	 * 根据质差id和指标类型获取质差轨迹点
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByVQBIdAndIndexType(
			Long badRoadId, Integer indexType);

	/**
	 * 根据质差id和指标类型获取质差轨迹点行驶方向
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByVQBIdAndIndexType(
			Long badRoadId, Integer indexType);

	/**
	 * 根据质差id获取质差轨迹点
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * 
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByVQBId(Long badRoadId);

	/**
	 * 获取测试日志下的所有类型视频质差的轨迹点,按类型分类
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<List<Object>> getEveryVQBGpsPointsByTestlogIds(
			String testLogItemIds);

	/**
	 * 根据质差id获取LTE切换事件
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getLTEPointsByVQBId(Long badRoadId);
}
