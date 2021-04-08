/**
 * 
 */
package com.datang.service.chart.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.constant.OneDimensionalChartType;
import com.datang.dao.chart.OneDimensionalChartDao;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.service.chart.IOneDimensionalChartService;

/**
 * 一维图配置的service实现
 * 
 * @author yinzhipeng
 * @date:2015年12月30日 下午4:09:59
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class OneDimensionalChartServiceImpl implements
		IOneDimensionalChartService {
	@Autowired
	private OneDimensionalChartDao oneDimensionalChartDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.chart.IOneDimensionalChartService#findAxisCustomer
	 * (com.datang.constant.OneDimensionalChartType)
	 */
	@Override
	public OneDimensionalChartConfig findAxisCustomer(
			OneDimensionalChartType chartType) {
		if (null == chartType) {
			return new OneDimensionalChartConfig();
		}
		OneDimensionalChartConfig queryOneDimensionalChartConfig = oneDimensionalChartDao
				.queryOneDimensionalChartConfig(chartType);
		return queryOneDimensionalChartConfig;
	}

}
