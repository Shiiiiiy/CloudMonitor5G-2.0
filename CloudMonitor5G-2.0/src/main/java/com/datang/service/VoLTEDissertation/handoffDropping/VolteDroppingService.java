package com.datang.service.VoLTEDissertation.handoffDropping;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDropping;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.TestLogMeasure;

/**
 * volte质量专题---volte切换失败Service接口
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午2:17:35
 */
public interface VolteDroppingService {
	/**
	 * 根据id获取切换失败
	 * 
	 * @param droppingId
	 * @return
	 */
	public VolteDropping getVolteDropping(Long droppingId);

	/**
	 * 根据测试日志ID集合获取切换失败
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteDropping> getVolteDroppingByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 汇总两类切换失败的个数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, Integer> sumEveryVolteDroppingNum(
			List<Long> testLogItemIds);

	/**
	 * 根据测试日志的id集合获取SRVCC切换失败事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteDroppingSRVCC> getVolteDroppingSRVCCsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据原始日志和对比日志获取两批日志具有相同目标小区的srvcc切换失败
	 * 
	 * @param testLogItemIds
	 * @param compareTestLogItemIds
	 * @return
	 */
	public List<VolteDroppingSRVCC> getVolteDroppingSRVCCsByLogIdsAndCompareLogIds(
			List<Long> testLogItemIds, List<Long> compareTestLogItemIds);

	/**
	 * SRVCC切换失败事件问题整体分析
	 * 
	 * @param volteDroppingSRVCCsByLogIds
	 * @param queryTestLogItems
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> droppingSRVCCWholeAnalysis(
			List<VolteDroppingSRVCC> volteDroppingSRVCCsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * SRVCC切换失败事件问题整体分析界面的指标分析
	 * 
	 * @param volteDroppingSRVCCsByLogIds
	 * @return
	 */
	public EasyuiPageList droppingSRVCCWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * SRVCC切换失败事件问题整体分析界面的整体统计
	 * 
	 * @param volteDroppingSRVCCsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList droppingSRVCCWholeIndex1Analysis(
			List<VolteDroppingSRVCC> volteDroppingSRVCCsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 获取SRVCC切换失败事件
	 * 
	 * @param id
	 * @return
	 */
	public VolteDroppingSRVCC getVolteDroppingSRVCCById(Long id);

	/**
	 * 根据测试日志的id集合获取系统内部切换失败事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteDroppingInt> getVolteDroppingIntsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据原始日志和对比日志获取两批日志具有相同目标小区的系统内切换失败
	 * 
	 * @param testLogItemIds
	 * @param compareTestLogItemIds
	 * @return
	 */
	public List<VolteDroppingInt> getVolteDroppingIntsByLogIdsAndCompareLogIds(
			List<Long> testLogItemIds, List<Long> compareTestLogItemIds);

	/**
	 * 系统内部切换失败事件问题整体分析
	 * 
	 * @param volteDroppingIntsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, EasyuiPageList> DroppingIntWholeAnalysis(
			List<VolteDroppingInt> volteDroppingIntsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 系统内部切换失败事件问题整体分析界面的指标分析
	 * 
	 * @param volteDroppingIntsByLogIds
	 * @return
	 */
	public EasyuiPageList droppingIntWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * 系统内部切换失败事件问题整体分析界面的整体统计
	 * 
	 * @param volteDroppingIntsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList droppingIntWholeIndex1Analysis(
			List<VolteDroppingInt> volteDroppingIntsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 获取系统内部切换失败事件
	 * 
	 * @param id
	 * @return
	 */
	public VolteDroppingInt getVolteDroppingIntById(Long id);

	/**
	 * 切换失败事件的信令或者对端信令多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList droppingSignllingPageList(PageList pageList);

	/**
	 * 插入某条切换失败路段的名称
	 * 
	 * @param roadName
	 * @param droppingId
	 */
	public void addQBRRoadName(String roadName, Long droppingId);

	/**
	 * 根据测试日志的id集合和目标小区Cellid查询所有SRVCC切换失败,并按开始时间排序
	 * 
	 * @param testLogItemIds
	 * @param failId
	 * @return
	 */
	public List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndFailId(
			List<Long> testLogItemIds, Long failId);

	/**
	 * 根据测试日志的id集合和小区cellid目标小区cellid(failid)查询所有系统内切换失败,并按开始时间排序
	 * 
	 * @param testLogItemIds
	 * @param cellId
	 * @param failId
	 * @return
	 */
	public List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCellIdAndFailId(
			List<Long> testLogItemIds, Long cellId, Long failId);

	/**
	 * 根据时间查询服务小区和邻区测量详情
	 * 
	 * @param begainDate
	 * @param endDate
	 * @return
	 */
	public List<TestLogMeasure> queryTestLogMeasuresByTime(Long begainDate,
			Long endDate);
}
