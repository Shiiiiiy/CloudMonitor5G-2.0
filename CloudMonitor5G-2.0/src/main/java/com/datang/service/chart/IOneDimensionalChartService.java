/**
 * 
 */
package com.datang.service.chart;

import com.datang.constant.OneDimensionalChartType;
import com.datang.domain.chart.OneDimensionalChartConfig;

/**
 * 一维图配置的service接口
 * 
 * @author yinzhipeng
 * @date:2015年12月30日 下午4:06:00
 * @version
 */
public interface IOneDimensionalChartService {
	/**
	 * 获取自定义配置坐标轴
	 * 
	 * @param chartType
	 * @return
	 */
	public OneDimensionalChartConfig findAxisCustomer(
			OneDimensionalChartType chartType);

}
