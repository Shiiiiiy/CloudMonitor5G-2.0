/**
 * 
 */
package com.datang.service.gis;

import java.util.List;

import com.datang.domain.gis.TestLogItemGpsPoint;

/**
 * 测试日志轨迹点service接口
 * 
 * @author yinzhipeng
 * @date:2015年11月26日 上午10:45:08
 * @version
 */
public interface ITestLogItemGpsPointService {

	/**
	 * 根据测试日志的id查询所有轨迹点
	 * 
	 * @param testLogItemIds
	 *            按","拼接的id字符串
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByTestLogIds(String testLogItemIds);

}
