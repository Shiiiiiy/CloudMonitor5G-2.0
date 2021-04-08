package com.datang.service.stationTest;

import java.util.List;

import com.datang.domain.planParam.PlanParamPojo;

/**
 * 单眼报告进度查看Service
 * @author maxuancheng
 *
 */
public interface StationCompletionShowService {
	
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
	public List<PlanParamPojo> getDataOfCityName(String cityName);

	/**
	 * 根据小区名和城市查询小区经纬度
	 * @author maxuancheng
	 * date:2020年5月6日 上午10:17:55
	 * @param cellName
	 * @param cityName 
	 * @return
	 */
	public List<PlanParamPojo> queryCellLonAndLat(String cellName, String cityName);

}
