/**
 * 
 */
package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.gis.ITestLogItemGpsPointService;

/**
 * 测试日志轨迹点service接口实现
 * 
 * @author yinzhipeng
 * @date:2015年11月26日 上午10:50:02
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TestLogItemGpsPointServiceImpl implements
		ITestLogItemGpsPointService {
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.ITestLogItemGpsPointService#getPointsByTestLogIds
	 * (java.lang.String)
	 */
	@Override
	public List<TestLogItemGpsPoint> getPointsByTestLogIds(String testLogItemIds) {
		List<TestLogItemGpsPoint> points = new ArrayList<>();
		if (!StringUtils.hasText(testLogItemIds)) {
			return points;
		}
		String[] logIds = testLogItemIds.trim().split(",");
		// 存储TestLogItem的id集合
		List<Long> ids = new ArrayList<>();
		for (int i = 0; i < logIds.length; i++) {
			if (StringUtils.hasText(logIds[i])) {
				try {
					ids.add(Long.parseLong(logIds[i].trim()));
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		points = testLogItemGpsPointDao.getTestLogItemGpsPoints(ids);
		return points;
	}

}
