package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 连续无线差路段轨迹点Service
 * 
 * @explain
 * @name ICWBRGpsPointService
 * @author shenyanwei
 * @date 2016年6月24日下午5:20:42
 */
public interface ICWBRGpsPointService {
	/**
	 * 根据质差路段id和指标类型获取质差路段轨迹点
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByCWBRIdAndIndexType(
			Long badRoadId, Integer indexType);

	/**
	 * 根据质差路段id和指标类型获取质差路段轨迹点行驶方向
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByCWBRIdAndIndexType(
			Long badRoadId, Integer indexType);

	/**
	 * 根据质差路段id获取质差路段轨迹点
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * 
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByCWBRId(Long badRoadId);

	/**
	 * 获取测试日志下的所有类型的问题路段的轨迹点,按类型分类
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<List<Object>> getEveryCWBRGpsPointsByTestlogIds(
			String testLogItemIds);
}
