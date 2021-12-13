/**
 * 
 */
package com.datang.service.testLogItem.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.datang.dao.knowfeeling.KnowFeelingDao;
import com.datang.domain.testLogItem.UnicomLogItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.dao.testLogItem.TestLogItemDao;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.testLogItem.ITestLogItemService;

/**
 * 测试日志
 * 
 * @author yinzhipeng
 * @date:2015年10月30日 下午12:58:26
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TestLogItemServiceImpl implements ITestLogItemService {
	@Autowired
	private TestLogItemDao testLogItemDao;

	@Autowired
	private KnowFeelingDao knowFeelingDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.ITestLogItemService#pageList(com.datang
	 * .common.action.page.PageList)
	 */
	@Override
	public AbstractPageList pageList(PageList pageList) {
		return testLogItemDao.getPageTestLogItem(pageList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.ITestLogItemService#queryTestLogItems(
	 * java.lang.String)
	 */
	@Override
	public List<TestLogItem> queryTestLogItems(String testLogIds) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (!StringUtils.hasText(testLogIds)) {
			return testLogItems;
		}
		String[] logIds = testLogIds.trim().split(",");
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
		testLogItems = testLogItemDao.getTestLogItems(ids);
		return testLogItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.ITestLogItemService#queryTestLogItems(
	 * java.util.List)
	 */
	@Override
	public List<TestLogItem> queryTestLogItems(List<Long> testLogIds) {
		List<TestLogItem> testLogItems = new ArrayList<>();
		if (null == testLogIds || 0 == testLogIds.size()) {
			return testLogItems;
		}
		testLogItems = testLogItemDao.getTestLogItems(testLogIds);
		return testLogItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.ITestLogItemService#queryTestLogById(java
	 * .lang.Long)
	 */
	@Override
	public TestLogItem queryTestLogById(Long recSeqNo) {
		return testLogItemDao.find(recSeqNo);
	}
	
	@Override
	public TestLogItem queryTestLogByLogName(String filename){
		return testLogItemDao.queryTestLogByLogName(filename);
	}

	@Override
	public List<TestLogItem> queryTestLogItemsByBoxIds(List<String> boxIds) {
		return testLogItemDao.getTestLogItemsByBoxIds(boxIds);
	}

	@Override
	public List<TestLogItem> queryTestLogItemsByOther(List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			Date beginDate, Date endDate) {

		return testLogItemDao.getTestLogItemsByOther(boxIds, terminalGroup,
				testRankList, beginDate, endDate);
	}

	@Override
	public List<TestLogItem> queryCQTTestLogItemsByOther(List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			Date beginDate, Date endDate) {
		return testLogItemDao.getCQTTestLogItemsByOther(boxIds, terminalGroup,
				testRankList, beginDate, endDate);
	}

	@Override
	public List<TestLogItem> queryCQTByFloorName(String floorName) {
		return testLogItemDao.getCQTTestLogItemsByFloorName(floorName);
	}

	@Override
	public List<StationSAMTralPojo> getGpsPointData(String allLogNames) {
		return testLogItemDao.getGpsPointData(allLogNames);
	}
	
	@Override
	public void deleteTestLogItem(String testLogItemIds) {
		String[] idsArry = testLogItemIds.split(",");
		if(idsArry.length>0){
			for (String recseqno : idsArry) {
				TestLogItem testLogItem = testLogItemDao.find(Long.valueOf(recseqno));
				//20211213 直接物理删除
				/**
				testLogItem.setDeleteTag(1);
				testLogItemDao.update(testLogItem);
			 	**/
				testLogItemDao.delete(testLogItem);
				//删除关联感知数据
				knowFeelingDao.deleteKnowFeeling(testLogItem.getFileName());
			}
		}
	}
	
	@Override
	public List<TestLogItem> queryTestLogItemsByLogName(String boxid, List<String> logfileNames){
		return testLogItemDao.queryTestLogItemsByLogName(boxid, logfileNames);
	}

}
