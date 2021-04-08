/**
 * 
 */
package com.datang.service.service5g.gis5g;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * EMBB覆盖路段轨迹点service接口
 * 
 * @author _YZP
 * 
 */
public interface IEmbbCoverGpsPointService {

	/**
	 * 根据EMBB覆盖路段id和指标类型获取采样点
	 * 
	 * @param badRoadId
	 * @param indexType
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByEmbbIdAndIndexType(
			Long badRoadId, Integer indexType);

	/**
	 * 根据EMBB覆盖路段id和指标类型获取采样点行驶方向
	 * 
	 * @param badRoadId
	 * @param indexType
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointDirectionsByEmbbIdAndIndexType(
			Long badRoadId, Integer indexType);

	/**
	 * 根据测试日志获取所有日志下的所有某种EMBB覆盖路段下的某个指标的所有采样点
	 * 
	 * @param testLogItemIds
	 * @param coverType
	 * @param indexType
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsByTestLogItem(
			String testLogItemIds, Integer coverType, Integer indexType);
	
	/**
	 * 根据测试日志名称获取日志详表下的某个指标的所有采样点
	 * @param logNameList
	 * @return
	 */
	public List<TestLogItemIndexGpsPoint> getPointsExceptionEtgTral(List<String> logNameList);

}
