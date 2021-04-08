/**
 * 
 */
package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 质差路段轨迹点service接口
 * 
 * @author yinzhipeng
 * @date:2015年12月2日 上午10:00:03
 * @version
 */
public interface IQBRGpsPointService {

	/**
	 * 根据质差路段id和指标类型获取质差路段轨迹点
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByQBRIdAndIndexType(
			Long badRoadId, Integer indexType);
	/**
	 * 根据质差路段id和指标类型获取质差路段轨迹点(5G)
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByQBRIdAndIndexType5g(
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
	public List<TestLogItemGpsPoint> getPointDirectionsByQBRIdAndIndexType(
			Long badRoadId, Integer indexType);
	
	/**
	 * 根据质差路段id和指标类型获取质差路段轨迹点行驶方向(5g)
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * @param indexType
	 *            指标类型
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByQBRIdAndIndexType5g(
			Long badRoadId, Integer indexType);

	/**
	 * 根据质差路段id获取质差路段轨迹点
	 * 
	 * @param badRoadId
	 *            质差路段id
	 * 
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByQBRId(Long badRoadId);

	/**
	 * 获取测试日志下的所有类型的问题路段的轨迹点,按类型分类
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<List<Object>> getEveryQBRGpsPointsByTestlogIds(
			String testLogItemIds);
	/**
	 * 获取多个日志id下所有质差路段
	 * @author maxuancheng
	 * @param testLogItemIds
	 * @param indexType
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByTestLogItem(String testLogItemIds, Integer indexType);

}
