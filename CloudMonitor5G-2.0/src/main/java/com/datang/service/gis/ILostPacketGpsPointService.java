/**
 * 
 */
package com.datang.service.gis;

import java.util.List;

/**
 * 连续丢包路段轨迹点service接口
 * 
 * @explain
 * @name ILostPacketGpsPointService
 * @author shenyanwei
 * @date 2017年2月21日上午10:14:42
 */
public interface ILostPacketGpsPointService {
	/**
	 * 获取测试日志下的所有类型的连续丢包的轨迹点,按类型分类
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<List<Object>> getEveryLPGpsPointsByTestlogIds(
			String testLogItemIds);

}
