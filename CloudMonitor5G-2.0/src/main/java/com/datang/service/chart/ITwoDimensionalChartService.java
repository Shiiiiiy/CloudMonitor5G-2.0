/**
 * 
 */
package com.datang.service.chart;

import com.datang.constant.TwoDimensionalChartType;
import com.datang.domain.chart.TwoDimensionalChartConfig;

/**
 * 二维图配置的service接口
 * 
 * @author yinzhipeng
 * @date:2016年1月4日 下午2:47:33
 * @version
 */
public interface ITwoDimensionalChartService {
	/**
	 * 获取二维图坐标配置
	 * 
	 * @param chartType
	 * @return
	 */
	public TwoDimensionalChartConfig findTwoDimensionalChartConfig(
			TwoDimensionalChartType chartType);

}
