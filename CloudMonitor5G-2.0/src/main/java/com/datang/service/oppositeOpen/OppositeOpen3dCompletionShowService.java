package com.datang.service.oppositeOpen;

import java.util.List;

import com.datang.domain.platform.projectParam.Plan4GParam;

/**
 * 单眼报告进度查看Service
 * @author maxuancheng
 *
 */
public interface OppositeOpen3dCompletionShowService {
	
	/**
	 * 根据名称和城市查询不通过的测试项
	 * @param name
	 * @param city
	 * @return
	 */
	long getReportAnalysssResult(String name,String city);

	/**
	 * 根据城市名获取数据
	 * @author maxuancheng
	 * date:2020年3月4日 下午5:25:06
	 * @param cityName
	 * @return
	 */
	public List<Plan4GParam> getDataOfCityName(String cityName);

	/**
	 * 根据城市小区名查询小区
	 * @author maxuancheng
	 * date:2020年5月6日 下午3:21:47
	 * @param cellName
	 * @param cityName
	 * @return
	 */
	public List<Plan4GParam> queryCellLonAndLat(String cellName, String cityName);

}
