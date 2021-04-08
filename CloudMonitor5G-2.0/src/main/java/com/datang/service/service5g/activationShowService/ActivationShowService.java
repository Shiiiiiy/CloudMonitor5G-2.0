package com.datang.service.service5g.activationShowService;

import java.util.List;
import java.util.Map;


/**
 * 活跃度统计service
 * @author maxuancheng
 * @date 2019年7月23日
 */
public interface ActivationShowService {

	/**
	 * 获取当前用户总数、当前在线用户数和峰值用户数
	 * @author maxuancheng
	 * @param cityId 城市id，如果为空或者为-2则查询所有
	 * @return
	 */
	public Map<String, Long> getUserNumber(String cityId);

	/**
	 * 获取APP和Outum最高版本号和用户数
	 * @author maxuancheng
	 * @param cityId 城市id，如果为空或者为-2则查询所有
	 * @return
	 */
	public Map<String, Object> getVersionAndUserNumber(String cityId);

	/**
	 * 终端使用情况统计
	 * @author maxuancheng
	 * @param cityId
	 * @return
	 */
	public Map<String, Object> getTerminalUsedStatistics(String cityId);

	/**
	 * 获取在线用户boxid
	 * @author maxuancheng
	 * @param cityId
	 * @return
	 */
	public List<Object> getOnLineUserBoxid(String cityId);

}
