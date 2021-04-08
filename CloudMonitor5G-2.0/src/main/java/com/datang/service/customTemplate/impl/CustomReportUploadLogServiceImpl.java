package com.datang.service.customTemplate.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.common.util.StringUtils;
import com.datang.dao.customTemplate.CustomReportLogItemDao;
import com.datang.dao.testLogItem.TestLogItemDao;
import com.datang.domain.customTemplate.CustomLogValidatePojo;
import com.datang.domain.customTemplate.CustomUploadLogItemPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.customTemplate.CustomReportUploadLogService;
import com.datang.service.testLogItem.ITestLogItemService;

/**
 * 自定义上传测试日志
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CustomReportUploadLogServiceImpl implements CustomReportUploadLogService {
	
	@Resource
	JdbcTemplate jdbc;
	
	@Autowired
	private CustomReportLogItemDao customReportLogItemDao;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		return customReportLogItemDao.getPageTestLogItem(pageList);
	}

	@Override
	public List<CustomUploadLogItemPojo> queryTestLogItems(String testLogIds) {
		List<CustomUploadLogItemPojo> testLogItems = new ArrayList<>();
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
		testLogItems = customReportLogItemDao.getTestLogItems(ids);
		return testLogItems;
	}

	@Override
	public List<CustomUploadLogItemPojo> queryTestLogItems(List<Long> testLogIds) {
		List<CustomUploadLogItemPojo> testLogItems = new ArrayList<>();
		if (null == testLogIds || 0 == testLogIds.size()) {
			return testLogItems;
		}
		testLogItems = customReportLogItemDao.getTestLogItems(testLogIds);
		return testLogItems;
	}

	@Override
	public CustomUploadLogItemPojo queryTestLogById(Long recSeqNo) {
		return customReportLogItemDao.find(recSeqNo);
	}
	
	@Override
	public CustomUploadLogItemPojo queryTestLogByLogName(String fileLink){
		return customReportLogItemDao.queryTestLogByLogName(fileLink);
	}

	@Override
	public List<CustomUploadLogItemPojo> queryTestLogItemsByBoxIds(List<String> boxIds) {
		return customReportLogItemDao.getTestLogItemsByBoxIds(boxIds);
	}
	
	@Override
	public void deleteTestLogItem(String testLogItemIds) {
		String[] idsArry = testLogItemIds.split(",");
		if(idsArry.length>0){
			for (String recseqno : idsArry) {
				CustomUploadLogItemPojo testLogItem = customReportLogItemDao.find(Long.valueOf(recseqno));
				testLogItem.setDeleteTag(1);
				customReportLogItemDao.update(testLogItem);
			}
		}
	}
	
	@Override
	public List<CustomUploadLogItemPojo> queryTestLogItemsByLogName(String boxid, List<String> logfileNames){
		return customReportLogItemDao.queryTestLogItemsByLogName(boxid, logfileNames);
	}
	
	@Override
	public List<CustomLogValidatePojo> queryCheckLog(List<CustomUploadLogItemPojo> list){
		List<CustomLogValidatePojo> ritList = new ArrayList<CustomLogValidatePojo>();
		for (CustomUploadLogItemPojo pojo1 : list) {
			CustomLogValidatePojo validatePojo= new CustomLogValidatePojo();
			validatePojo.setFilenameUpload(pojo1.getFileName());
			//查询MD5信息
			String md5Sql = "select * from IADS_CUCC_LOGINFO where FILE_NAME="+pojo1.getFileName();
			List<Map<String, Object>> objectQueryAll = jdbc.objectQueryAll(md5Sql);
			if(objectQueryAll.size()==2){
				for (Map<String, Object> map : objectQueryAll) {
					if(map.get("TAG").equals("first")){
						if(map.get("START_TIME")!=null){
							long starttime = Float.valueOf(map.get("START_TIME").toString()).longValue();
							validatePojo.setStartTimeUpload(String.valueOf(starttime));
						}
					}else if(map.get("TAG").equals("last")){
						if(map.get("END_TIME")!=null){
							long endtime = Float.valueOf(map.get("END_TIME").toString()).longValue();
							validatePojo.setEndTimeUpload(String.valueOf(endtime));
						}
						if(map.get("BOX_ID")!=null){
							String imeiVlaue = map.get("BOX_ID").toString();
							validatePojo.setImeiUpload(imeiVlaue);
						}
						if(map.get("MD5")!=null){
							String md5Vlaue = map.get("MD5").toString();
							validatePojo.setMd5Upload(md5Vlaue);
						}
						if(map.get("MIN_LAT")!=null){
							String minLatVlaue = map.get("MIN_LAT").toString();
							validatePojo.setMinLatUpload(minLatVlaue);
						}
						if(map.get("MAX_LAT")!=null){
							String maxLatVlaue = map.get("MAX_LAT").toString();
							validatePojo.setMaxLatUpload(maxLatVlaue);
						}
						if(map.get("MIN_LONG")!=null){
							String minLonVlaue = map.get("MIN_LONG").toString();
							validatePojo.setMinLongUpload(minLonVlaue);
						}
						if(map.get("MAX_LONG")!=null){
							String maxLonVlaue = map.get("MAX_LONG").toString();
							validatePojo.setMaxLongUpload(maxLonVlaue);
						}
					}
				}
			}else if(objectQueryAll.size()==1){
				for (Map<String, Object> map : objectQueryAll) {
					if(map.get("START_TIME")!=null){
						long starttime = Float.valueOf(map.get("START_TIME").toString()).longValue();
						validatePojo.setStartTimeUpload(String.valueOf(starttime));
					}
					if(map.get("END_TIME")!=null){
						long endtime = Float.valueOf(map.get("END_TIME").toString()).longValue();
						validatePojo.setEndTimeUpload(String.valueOf(endtime));
					}
					if(map.get("BOX_ID")!=null){
						String imeiVlaue = map.get("BOX_ID").toString();
						validatePojo.setImeiUpload(imeiVlaue);
					}
					if(map.get("MD5")!=null){
						String md5Vlaue = map.get("MD5").toString();
						validatePojo.setMd5Upload(md5Vlaue);
					}
					if(map.get("MIN_LAT")!=null){
						String minLatVlaue = map.get("MIN_LAT").toString();
						validatePojo.setMinLatUpload(minLatVlaue);
					}
					if(map.get("MAX_LAT")!=null){
						String maxLatVlaue = map.get("MAX_LAT").toString();
						validatePojo.setMaxLatUpload(maxLatVlaue);
					}
					if(map.get("MIN_LONG")!=null){
						String minLonVlaue = map.get("MIN_LONG").toString();
						validatePojo.setMinLongUpload(minLonVlaue);
					}
					if(map.get("MAX_LONG")!=null){
						String maxLonVlaue = map.get("MAX_LONG").toString();
						validatePojo.setMaxLongUpload(maxLonVlaue);
					}
				}
			}
			if(objectQueryAll.size()>0 && pojo1.getTestFileStatus()==2){
				validatePojo.setImeiDecode(validatePojo.getImeiUpload());
				validatePojo.setFilenameDecode(validatePojo.getFilenameUpload());
				validatePojo.setMd5Decode(validatePojo.getMd5Decode());
				validatePojo.setFilenameCheck("YES");
				validatePojo.setMd5Check("YES");
			}else{
				validatePojo.setFilenameCheck("NO");
				validatePojo.setMd5Check("NO");
			}
			
			//查询业务事件信息
			String eventSql = "select * from IADS_CUCC_TRAFFICINFO where FILE_NAME="+pojo1.getFileName();
			Map<String, Object> eventMap = jdbc.objectQueryNoPage(eventSql);
			if(!eventMap.isEmpty()){
				if(eventMap.get("EVENTTYPE")!=null){
					String eventTypeVlaue = eventMap.get("EVENTTYPE").toString();
					validatePojo.setEventTypeUpload(eventTypeVlaue);
				}
				if(eventMap.get("TIMESTAMP")!=null){
					String timeVlaue = eventMap.get("TIMESTAMP").toString();
					validatePojo.setTimestampUpload(timeVlaue);
				}
				if(eventMap.get("LONGITUDE")!=null){
					String lonVlaue = eventMap.get("LONGITUDE").toString();
					validatePojo.setLongitudeUpload(lonVlaue);
				}
				if(eventMap.get("LATITUDE")!=null){
					String latVlaue = eventMap.get("LATITUDE").toString();
					validatePojo.setLatitudeUpload(latVlaue);
				}
			}
		}
		return ritList;
	}
}
	
