/**
 * 
 */
package com.datang.service.VoLTEDissertation.exceptionEvent;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventDropCall;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventNotConnect;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEvent;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventCsfbFail;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventDropCall;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventImsRegistFail;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventNotConnect;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.TestLogMeasure;

/**
 * volte质量专题---volte异常事件Service接口
 * 
 * @author yinzhipeng
 * @date:2016年4月16日 下午3:49:23
 * @version
 */
public interface IVolteExceptionEventService {
	/**
	 * 根据id获取异常事件
	 * 
	 * @param eeId
	 * @return
	 */
	public VolteExceptionEvent getVolteExceptionEvent(Long eeId);

	/**
	 * 根据测试日志id集合获取该测试日志下的所有异常事件
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteExceptionEvent> getVolteExceptionEventByIds(List<Long> ids);

	/**
	 * 汇总各类异常事件的个数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, Integer> sumEveryExceptionEventNum(
			List<Long> testLogItemIds);

	/**
	 * 根据测试日志的id集合获取语音未接通异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEventNotConnect> getVolteExceptionEventNotConnectByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 语音未接通异常事件问题整体分析
	 * 
	 * @param volteExceptionEventNotConnectsByLogIds
	 * @param queryTestLogItems
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doNotConnectWholeAnalysis(
			List<VolteExceptionEventNotConnect> volteExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 语音未接通异常事件问题整体分析界面的指标分析
	 * 
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doNotConnectWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * 语音未接通异常事件问题整体分析界面的整体统计
	 * 
	 * @param volteExceptionEventNotConnectsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doNotConnectWholeIndex1Analysis(
			List<VolteExceptionEventNotConnect> volteExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 语音未接通异常事件问题整体分析界面的信令节点占比饼图
	 * 
	 * @param volteExceptionEventNotConnectsByLogIds
	 * @return
	 */
	public EasyuiPageList doNotConnectWholeIndex2Analysis(
			List<VolteExceptionEventNotConnect> volteExceptionEventNotConnectsByLogIds);

	/**
	 * 获取语音未接通异常事件
	 * 
	 * @param id
	 * @return
	 */
	public VolteExceptionEventNotConnect getVolteExceptionEventNotConnectById(
			Long id);

	/**
	 * 根据测试日志的id集合获取语音掉话异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEventDropCall> getVolteExceptionEventDropCallByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 语音掉话异常事件问题整体分析
	 * 
	 * @param volteExceptionEventDropCallsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, EasyuiPageList> doDropCallWholeAnalysis(
			List<VolteExceptionEventDropCall> volteExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 语音掉话异常事件问题整体分析界面的指标分析
	 * 
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doDropCallWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * 语音掉话异常事件问题整体分析界面的整体统计
	 * 
	 * @param volteExceptionEventDropCallsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doDropCallWholeIndex1Analysis(
			List<VolteExceptionEventDropCall> volteExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 获取语音掉话异常事件
	 * 
	 * @param id
	 * @return
	 */
	public VolteExceptionEventDropCall getVolteExceptionEventDropCallById(
			Long id);

	/**
	 * 根据测试日志的id集合获取IMS注册失败异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEventImsRegistFail> getVolteExceptionEventImsRegistFailByLogIds(
			List<Long> testLogItemIds);

	/**
	 * IMS注册失败异常事件问题整体分析
	 * 
	 * @param volteExceptionEventImsRegistFailsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, EasyuiPageList> doImsRegistFailWholeAnalysis(
			List<VolteExceptionEventImsRegistFail> volteExceptionEventImsRegistFailsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * IMS注册失败异常事件问题整体分析界面的指标分析
	 * 
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doImsRegistFailWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * IMS注册失败异常事件问题整体分析界面的整体统计
	 * 
	 * @param volteExceptionEventImsRegistFailsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doImsRegistFailWholeIndex1Analysis(
			List<VolteExceptionEventImsRegistFail> volteExceptionEventImsRegistFailsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * IMS注册失败异常事件问题整体分析界面的信令节点占比饼图
	 * 
	 * @param volteExceptionEventImsRegistFailsByLogIds
	 * @return
	 */
	public EasyuiPageList doImsRegistFailWholeIndex2Analysis(
			List<VolteExceptionEventImsRegistFail> volteExceptionEventImsRegistFailsByLogIds);

	/**
	 * 获取IMS注册失败异常事件
	 * 
	 * @param id
	 * @return
	 */
	public VolteExceptionEventImsRegistFail getVolteExceptionEventImsRegistFailById(
			Long id);

	/**
	 * 根据测试日志的id集合获取语音CSFB失败异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEventCsfbFail> getVolteExceptionEventCsfbFailByLogIds(
			List<Long> testLogItemIds);

	/**
	 * CSFB失败异常事件问题整体分析
	 * 
	 * @param volteExceptionEventCsfbFailsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, EasyuiPageList> doCsfbFailWholeAnalysis(
			List<VolteExceptionEventCsfbFail> volteExceptionEventCsfbFailsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * CSFB失败异常事件问题整体分析界面的指标分析
	 * 
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doCsfbFailWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * CSFB失败异常事件问题整体分析界面的整体统计
	 * 
	 * @param volteExceptionEventCsfbFailsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doCsfbFailWholeIndex1Analysis(
			List<VolteExceptionEventCsfbFail> volteExceptionEventCsfbFailsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 获取CSFB失败异常事件
	 * 
	 * @param id
	 * @return
	 */
	public VolteExceptionEventCsfbFail getVolteExceptionEventCsfbFailById(
			Long id);

	/**
	 * 异常事件的信令或者对端信令多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doExceptionEventSignllingPageList(PageList pageList);

	/**
	 * 插入某条异常事件路段的名称
	 * 
	 * @param roadName
	 * @param eeId
	 */
	public void addQBRRoadName(String roadName, Long eeId);

	/**
	 * 根据时间分页查询服务小区和邻区测量详情
	 * 
	 * @param begainDate
	 * @param endDate
	 * @return
	 */
	public EasyuiPageList queryTestLogMeasuresByTimePage(Long begainDate,
			Long endDate, int rows, int page);

	/**
	 * 根据时间查询服务小区和邻区测量详情
	 * 
	 * @param begainDate
	 * @param endDate
	 * @return
	 */
	public List<TestLogMeasure> queryTestLogMeasuresByTime(Long begainDate,
			Long endDate);

	/**
	 * 根据测试日志的id集合获取视频未接通异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoExceptionEventNotConnect> getVideoExceptionEventNotConnectByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 视频未接通异常事件问题整体分析
	 * 
	 * @param videoExceptionEventNotConnectsByLogIds
	 * @param queryTestLogItems
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoNotConnectWholeAnalysis(
			List<VideoExceptionEventNotConnect> videoExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 视频未接通异常事件问题整体分析界面的指标分析
	 * 
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doVideoNotConnectWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * 视频音未接通异常事件问题整体分析界面的整体统计
	 * 
	 * @param videoExceptionEventNotConnectsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doVideoNotConnectWholeIndex1Analysis(
			List<VideoExceptionEventNotConnect> videoExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 获取视频未接通异常事件
	 * 
	 * @param id
	 * @return
	 */
	public VideoExceptionEventNotConnect getVideoExceptionEventNotConnectById(
			Long id);

	/**
	 * 根据测试日志的id集合获取视频掉话异常事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoExceptionEventDropCall> getVideoExceptionEventDropCallByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 视频掉话异常事件问题整体分析
	 * 
	 * @param videoExceptionEventDropCallsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoDropCallWholeAnalysis(
			List<VideoExceptionEventDropCall> videoExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 视频掉话异常事件问题整体分析界面的指标分析
	 * 
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doVideoDropCallWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems);

	/**
	 * 视频掉话异常事件问题整体分析界面的整体统计
	 * 
	 * @param videoExceptionEventDropCallsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doVideoDropCallWholeIndex1Analysis(
			List<VideoExceptionEventDropCall> videoExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 获取视频掉话异常事件
	 * 
	 * @param id
	 * @return
	 */
	public VideoExceptionEventDropCall getVideoExceptionEventDropCallById(
			Long id);

}
