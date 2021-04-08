/**
 * 
 */
package com.datang.service.chart.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.constant.TwoDimensionalChartType;
import com.datang.dao.chart.TwoDimensionalChartDao;
import com.datang.domain.chart.TwoDimensionalChartConfig;
import com.datang.service.chart.ITwoDimensionalChartService;

/**
 * 二维图配置的service实现
 * 
 * @author yinzhipeng
 * @date:2016年1月4日 下午3:00:39
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TwoDimensionalChartServiceImpl implements
		ITwoDimensionalChartService {
	@Autowired
	private TwoDimensionalChartDao twoDimensionalChartDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.chart.ITwoDimensionalChartService#
	 * findTwoDimensionalChartConfig
	 * (com.datang.constant.TwoDimensionalChartType)
	 */
	@Override
	public TwoDimensionalChartConfig findTwoDimensionalChartConfig(
			TwoDimensionalChartType chartType) {
		if (null == chartType) {
			return new TwoDimensionalChartConfig();
		}
		TwoDimensionalChartConfig queryTwoDimensionalChartConfig = twoDimensionalChartDao
				.queryTwoDimensionalChartConfig(chartType);
		return queryTwoDimensionalChartConfig;
	}

}
