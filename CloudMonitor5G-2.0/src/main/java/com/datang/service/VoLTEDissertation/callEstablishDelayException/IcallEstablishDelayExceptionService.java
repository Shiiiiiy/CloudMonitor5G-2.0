/**
 * 
 */
package com.datang.service.VoLTEDissertation.callEstablishDelayException;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.calledLocationUpdate.VolteCallEstablishDelayExceptionCalledLocationUpdate;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.coreNetwork.VolteCallEstablishDelayExceptionCoreNetwork;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.otherProblem.VolteCallEstablishDelayExceptionOtherProblem;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;
import com.datang.domain.chart.OneDimensionalChartConfig;

/**
 * 呼叫建立时延Service接口
 * 
 * @explain
 * @name IcallEstablishDelayExceptionService
 * @author shenyanwei
 * @date 2016年5月24日下午2:52:06
 */
public interface IcallEstablishDelayExceptionService {

	/**
	 * 获取单个呼叫建立时延公共参数
	 * 
	 * @param cedeId
	 * @return
	 */
	public VolteCallEstablishDelayException getVolteCallEstablishDelayException(
			Long cedeId);

	/**
	 * 获取多个呼叫建立时延公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayException> getVolteCallEstablishDelayExceptionsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 整体分析
	 * 
	 * @param testLogItemIds
	 * @param volteCallEstablishDelayExceptionsByLogIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<Long> testLogItemIds,
			List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds);

	/**
	 * 整体呼叫建立时延异常的节点占比统计
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */

	public EasyuiPageList doWholeIndex1Analysis(
			List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds);

	/**
	 * 整体分析异常统计
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex2Analysis(List<Long> testLogItemIds);

	/**
	 * 整体分析异常原因占比
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex3Analysis(List<Long> testLogItemIds);

	/**
	 * 通过日志获取弱覆盖
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionWeakCover> getWeakCoversByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过Id获取弱覆盖
	 * 
	 * @param id
	 * @return
	 */
	public VolteCallEstablishDelayExceptionWeakCover getWeakCoverById(Long id);

	/**
	 * 弱覆盖问题分析
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doWeakCoverAnalysis(
			VolteCallEstablishDelayExceptionWeakCover volteCallEstablishDelayExceptionWeakCover);

	/**
	 * 根据日志获取重叠覆盖问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionOverlapCover> getVolteCallEstablishDelayExceptionOverlapCoversByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取重叠覆盖问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteCallEstablishDelayExceptionOverlapCover getVolteCallEstablishDelayExceptionOverlapCoverById(
			Long roadId);

	/**
	 * 重叠覆盖问题分析
	 * 
	 * @param
	 * @return
	 */
	public Map<String, List> doOverlapCoverAnalysis(
			VolteCallEstablishDelayExceptionOverlapCover volteCallEstablishDelayExceptionOverlapCover);

	/**
	 * 根据日志获取被叫位置更新问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionCalledLocationUpdate> getLocationUpdatesByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据Id获取被叫位置更新问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteCallEstablishDelayExceptionCalledLocationUpdate getLocationUpdateById(
			Long roadId);

	/**
	 * 被叫位置更新问题分析
	 * 
	 * @param
	 * @return
	 */
	public Map<String, List> doLocationUpdateAnalysis(
			VolteCallEstablishDelayExceptionCalledLocationUpdate volteCallEstablishDelayExceptionCalledLocationUpdate);

	/**
	 * 被叫位置更新问题信令多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doLocationUpdateSignllingPageList(PageList pageList);

	/**
	 * 获取核心网问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionCoreNetwork> getCoreNetworksByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 核心网问题分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, List> doCoreNetworkAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds);

	/**
	 * 核心网问题区间分布分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public List<Object> doCoreNetworkRtpPacketLostRatioHourChartAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds);

	/**
	 * 核心网问题sinr二维分布分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public List<Object> doCoreNetworkRtpPacketLostAndSinrChartAnalysis(
			List<Long> testLogItemIds);

	/**
	 * 核心网问题rsrp二维分布分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public List<Object> doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(
			List<Long> testLogItemIds);

	/**
	 * 通过日志获取其他问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteCallEstablishDelayExceptionOtherProblem> getOthersByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过Id获取其他问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteCallEstablishDelayExceptionOtherProblem getOtherById(Long roadId);

	/**
	 * 其他问题质差路段分析
	 * 
	 * @param volteQualityBadRoadOther
	 * @return
	 */
	public Map<String, List> doOtherAnalysis(
			VolteCallEstablishDelayExceptionOtherProblem volteCallEstablishDelayExceptionOtherProblem);

	/**
	 * 其他问题信令多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doOtherSignllingPageList(PageList pageList);

	/**
	 * 插入某条呼叫建立时延异常路段的名称
	 * 
	 * @param roadName
	 * @param Id
	 */
	public void addCEDERoadName(String roadName, Long id);

}
