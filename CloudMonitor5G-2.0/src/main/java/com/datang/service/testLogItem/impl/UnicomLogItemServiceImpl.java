/**
 * 
 */
package com.datang.service.testLogItem.impl;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.dao.testLogItem.UnicomLogItemDao;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.UnicomLogItem;
import com.datang.service.influx.InfluxService;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.web.action.testLogItem.UnicomLogItemAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
public class UnicomLogItemServiceImpl implements UnicomLogItemService {


	private static final String LIANTONG = "联通";
	private static final String YIDONG = "移动";
	private static final String DIANXIN = "电信";
	private static final String COLUMN_OPERATOR = "operator";




	private static Logger LOGGER = LoggerFactory
			.getLogger(UnicomLogItemServiceImpl.class);

	@Autowired
	private UnicomLogItemDao unicomLogItemDao;

	@Autowired
	private InfluxService influxService;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.ITestLogItemService#pageList(com.datang
	 * .common.action.page.PageList)
	 */
	@Override
	public AbstractPageList pageList(PageList pageList) {
		return unicomLogItemDao.getPageTestLogItem(pageList);
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
		testLogItems = unicomLogItemDao.getTestLogItems(ids);
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
		testLogItems = unicomLogItemDao.getTestLogItems(testLogIds);
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
	public UnicomLogItem queryTestLogById(Long recSeqNo) {
		return unicomLogItemDao.find(recSeqNo);
	}
	
	@Override
	public UnicomLogItem queryTestLogByLogName(String filename){
		return unicomLogItemDao.queryTestLogByLogName(filename);
	}
	@Override
	public UnicomLogItem queryTestLogByLogName2(String filename){
		return unicomLogItemDao.queryTestLogByLogName2(filename);
	}

	@Override
	public List<TestLogItem> queryTestLogItemsByBoxIds(List<String> boxIds) {
		return unicomLogItemDao.getTestLogItemsByBoxIds(boxIds);
	}

	@Override
	public List<UnicomLogItem> queryTestLogItemsByOther(String prov,String city,List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			String filename,
			Date beginDate, Date endDate) {

		return unicomLogItemDao.getTestLogItemsByOther(prov,city,boxIds, terminalGroup,
				testRankList,filename, beginDate, endDate);
	}

	@Override
	public List<TestLogItem> queryCQTTestLogItemsByOther(List<String> boxIds,
			List<String> terminalGroup, List<String> testRankList,
			Date beginDate, Date endDate) {
		return unicomLogItemDao.getCQTTestLogItemsByOther(boxIds, terminalGroup,
				testRankList, beginDate, endDate);
	}

	@Override
	public List<TestLogItem> queryCQTByFloorName(String floorName) {
		return unicomLogItemDao.getCQTTestLogItemsByFloorName(floorName);
	}

	@Override
	public List<StationSAMTralPojo> getGpsPointData(String allLogNames) {
		return unicomLogItemDao.getGpsPointData(allLogNames);
	}
	
	@Override
	public void deleteTestLogItem(String testLogItemIds) {
		String[] idsArry = testLogItemIds.split(",");
		if(idsArry.length>0){
			for (String recseqno : idsArry) {
				UnicomLogItem testLogItem = unicomLogItemDao.find(Long.valueOf(recseqno));
				testLogItem.setDeleteTag(1);
				unicomLogItemDao.update(testLogItem);
			}
		}
	}
	
	@Override
	public List<TestLogItem> queryTestLogItemsByLogName(String boxid, List<String> logfileNames){
		return unicomLogItemDao.queryTestLogItemsByLogName(boxid, logfileNames);
	}

	@Override
	public List<Map<String, Object>> dataOverview(String idStr) {
		return unicomLogItemDao.dataOverview(idStr);
	}

	@Override
	public List<Map<String, Object>> doPageQueryKpi() {
		return unicomLogItemDao.doPageQueryKpi();
	}

	@Override
	public List<Map<String, Object>> provInput() {
		return unicomLogItemDao.provInput();
	}

	@Override
	public List<Map<String, Object>> cityInput(String prov) {
		return unicomLogItemDao.cityInput(prov);
	}


	/**
	 * id分割字符串转list
	 * */
	private List<String> testLogItemIdsToList( String testLogItemIds){
		if(testLogItemIds==null || !StringUtils.hasText(testLogItemIds)) return new ArrayList<>();
		String[] idsArry =  testLogItemIds.split(",");
		List<String> idList = new ArrayList<>();
		for(String id:idsArry){
			idList.add(id);
		}
		return idList;
	}


	@Override
	public List<Map<String, Object>> logCheckMd5(Date beginDate, Date endDate, String testLogItemIds) {
		List<String> idList = testLogItemIdsToList(testLogItemIds);
		return unicomLogItemDao.logCheckMd5(beginDate,endDate,idList);
	}


	@Override
	public List<Map<String, Object>> logCheckBiz(Date beginDate, Date endDate, String testLogItemIds) {
		List<String> idList = testLogItemIdsToList(testLogItemIds);
		return unicomLogItemDao.logCheckBiz(beginDate,endDate,idList);
	}

	@Override
	public List<Map<String, Object>> middleGrid(List<String> idList) {

		Integer errorMsg = 0;

		// influxDb的翻译
		Map<String,String> operatorNameDb = new HashMap<>();

		// 联通
		operatorNameDb.put(LIANTONG,"LIANTONG");
		// 移动
		operatorNameDb.put(YIDONG,"YIDONG");
		// 电信
		operatorNameDb.put(DIANXIN,"DIANXIN");
		List<Map<String,Object>> kpiList = new ArrayList<>();
		try{

			// lon_lat_height
			// 		operator
			//	    kpi....
			List<Map.Entry<String, List<Map<String, Object>>>> gridDatasByLogFiles = influxService.getGridDatasByLogFiles(idList);
			LOGGER.error("middleGrid ~ : " + gridDatasByLogFiles.size());

			if(gridDatasByLogFiles==null || gridDatasByLogFiles.size()==0) return null;


			Integer index = 0;

			for(Map.Entry<String, List<Map<String, Object>>> entry:gridDatasByLogFiles){
				String key = entry.getKey();
				String[] split = org.apache.commons.lang3.StringUtils.split(key, "_");
				// key 异常
				// key ：lon_lat_height_operator
				if(split.length<3){
					continue;
				}

				Map<String,Object> valueMap = new HashMap<>();
				kpiList.add(valueMap);
				// 栅格号
				valueMap.put("BASIC_GRID_INDEX",index++);
				// 精度
				valueMap.put("BASIC_LONGITUDE",formatNull(split[0]));
				// 维度
				valueMap.put("BASIC_LATITUDE",formatNull(split[1]));
				// 高度
				valueMap.put("BASIC_HEIGHT",formatNull(split[2]));

				// 查找arraylist中 对应的厂家
				for(Map<String,Object> m:entry.getValue()){
					String operator = (String)m.get(COLUMN_OPERATOR);
					// 跳过空厂家
					if(operator==null||operator.equals("null")){
						 errorMsg++;
						 continue;
					}
					String operatorNick = operatorNameDb.get(operator);
					if(operatorNick==null){
						errorMsg++;
						continue;
					}
					for(Map.Entry<String, Object> k:m.entrySet()){
						// 跳过厂家字段
						if(COLUMN_OPERATOR.equals(k.getKey())) continue;
						// 跳过空
						if(k.getKey()==null || k.getValue()==null) continue;
						valueMap.put(operatorNick+"_" + k.getKey(),k.getValue());
					}
				}
			}

		}catch (Exception e){
			LOGGER.error("influxDb Error",e);
		}
		if(errorMsg>0){
			LOGGER.error("ignore: " + errorMsg + " empty operator data");
		}
		return kpiList;
	}


	private String formatNull(String ss){
		if(ss==null || ss.equals("null")){
			return "";
		}
		return ss;
	}

}
