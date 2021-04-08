package com.datang.service.customTemplate;

import java.util.Date;
import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.customTemplate.CustomLogValidatePojo;
import com.datang.domain.customTemplate.CustomUploadLogItemPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;

public interface CustomReportUploadLogService {

	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);

	/**
	 * 根据{testLogIds}查询测试日志信息
	 * 
	 * @param testLogIds
	 *            测试日志按','分隔的id
	 * @return
	 */
	public List<CustomUploadLogItemPojo> queryTestLogItems(String testLogIds);

	/**
	 * 根据{testLogIds}查询测试日志信息
	 * 
	 * @param testLogIds
	 *            测试日志按id集合
	 * @return
	 */
	public List<CustomUploadLogItemPojo> queryTestLogItems(List<Long> testLogIds);

	/**
	 * 根据ID查询测试日志信息
	 * 
	 * @param recSeqNo
	 * @return
	 */
	public CustomUploadLogItemPojo queryTestLogById(Long recSeqNo);
	
	/**
	 * 根据日志链接查询日志信息
	 * @param filename
	 * @return
	 */
	public CustomUploadLogItemPojo queryTestLogByLogName(String fileLink);

	/**
	 * 根据BoxId查询
	 * 
	 * @param boxIds
	 *            测试日志按boxid集合
	 * @return
	 */
	public List<CustomUploadLogItemPojo> queryTestLogItemsByBoxIds(List<String> boxIds);

	
	/**
	 * 通过id数组删除测试日志
	 * @param testLogItemIds
	 */
	public void deleteTestLogItem(String testLogItemIds);
	
	/**
	 * 根据boxid和日志名称集合查询CQT日志
	 * @author lucheng
	 * @date 2020年8月19日 下午4:46:35
	 * @param boxid 终端id
	 * @param logfileNames 日志名称集合
	 * @return
	 */
	public List<CustomUploadLogItemPojo> queryTestLogItemsByLogName(String boxid, List<String> logfileNames);
	
	/**
	 * 根据日志名称查询对应的校验结果
	 * @author lucheng
	 * @date 2021年4月1日 下午1:27:48
	 * @param list
	 * @return
	 */
	public List<CustomLogValidatePojo> queryCheckLog(List<CustomUploadLogItemPojo> list);
}
