/**
 * 
 */
package com.datang.service.testLogItem;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.UnicomLogItem;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 测试日志Service----接口
 * 
 * @author yinzhipeng
 * @date:2015年10月30日 上午11:25:23
 * @version
 */
public interface UnicomLogItemService {

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
	public List<TestLogItem> queryTestLogItems(String testLogIds);

	/**
	 * 根据{testLogIds}查询测试日志信息
	 *
	 * @param testLogIds
	 *            测试日志按id集合
	 * @return
	 */
	public List<TestLogItem> queryTestLogItems(List<Long> testLogIds);

	/**
	 * 根据ID查询测试日志信息
	 *
	 * @param recSeqNo
	 * @return
	 */
	public UnicomLogItem queryTestLogById(Long recSeqNo);

	/**
	 * 根据日志链接查询日志信息
	 * @param filename
	 * @return
	 */
	public UnicomLogItem queryTestLogByLogName(String filename);

	/**
	 * 根据日志文件名查询日志信息
	 * @param filename
	 * @return
	 */
	public UnicomLogItem queryTestLogByLogName2(String filename);

	/**
	 * 根据日志名查找任务名
	 * @param fileName
	 * @return
	 */
	String queryTaskNameByLogName(String fileName);

	/**
	 * 根据BoxId查询
	 *
	 * @param boxIds
	 *            测试日志按boxid集合
	 * @return
	 */
	public List<TestLogItem> queryTestLogItemsByBoxIds(List<String> boxIds);

	/**
	 * 根据BoxId、测试域以及开始时间、结束时间查询
	 *
	 * @param boxIds
	 * @param terminalGroup
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<UnicomLogItem> queryTestLogItemsByOther(String prov,String city,List<String> boxIds,
                                                      List<String> terminalGroup, List<String> testRankList, String filename,
                                                      Date beginDate, Date endDate);

	/**
	 * 根据BoxId、测试域以及开始时间、结束时间查询CQT日志
	 *
	 * @param boxIds
	 * @param terminalGroup
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<TestLogItem> queryCQTTestLogItemsByOther(List<String> boxIds,
                                                         List<String> terminalGroup, List<String> testRankList,
                                                         Date beginDate, Date endDate);

	/**
	 * 根据楼宇名称查询CQT日志
	 * 
	 * @param floorName
	 * @return
	 */
	public List<TestLogItem> queryCQTByFloorName(String floorName);

	/**
	 * 根据日志名查询采样点数据
	 * @author maxuancheng
	 * date:2020年5月15日 下午4:17:21
	 * @param allLogNames
	 * @return
	 */
	public List<StationSAMTralPojo> getGpsPointData(String allLogNames);
	
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
	public List<TestLogItem> queryTestLogItemsByLogName(String boxid, List<String> logfileNames);


	/**
	 * 根据日志id返回数据概览结果
	 * @return
	 */
	public List<Map<String,Object>> dataOverview(String idStr);

	/**
	 * 获取页面查询的KPI
	 *
	 */
	public List<Map<String, Object>> doPageQueryKpi();
	/**
	 * 根据 IADS_TESTLOG_ITEM 获取页面省下拉框
	 *
	 */
	public List<Map<String, Object>> provInput();
	/**
	 * 根据 IADS_TESTLOG_ITEM 获取页面市下拉框 （级联）
	 *
	 */
	public List<Map<String, Object>> cityInput(String prov);

	/**
	 * 日志校验导出 MD5 sheet
	 * */
	public List<Map<String, Object>> logCheckMd5(Date beginDate,Date endDate,String testLogItemIds);

	/**
	 * 日志校验导出 业务事件 sheet
	 * */
	public List<Map<String, Object>> logCheckBiz(Date beginDate,Date endDate,String testLogItemIds);

	/**
	 * 栅格中间表转化换
	 * */
	public List<Map<String, Object>> middleGrid(List<String> idList);

}
