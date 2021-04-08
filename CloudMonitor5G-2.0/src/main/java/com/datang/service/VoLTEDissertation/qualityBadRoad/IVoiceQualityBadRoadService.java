/**
 * 
 */
package com.datang.service.VoLTEDissertation.qualityBadRoad;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.coreNetwork.VolteQualityBadRoadCoreNetwork;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.VolteQualityBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem.VolteQualityBadRoadOther;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.paramError.VolteQualityBadRoadParamError;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.domain.testLogItem.TestLogItem;

/**
 * volte质量专题---volte语音质差Service接口
 * 
 * @author yinzhipeng
 * @date:2015年11月9日 下午4:00:18
 * @version
 */
public interface IVoiceQualityBadRoadService {

	/**
	 * 获取单个volte质差路段公共参数
	 * 
	 * @param qbrId
	 * @return
	 */
	public VolteQualityBadRoad getVolteQualityBadRoad(Long qbrId);

	/**
	 * 获取多个volte质差路段公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoad> getVolteQualityBadRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 汇总volte质差路段里程
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Float sumQBRDistance(List<Long> testLogItemIds);

	/**
	 * 整体路段分析
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @param queryTestLogItems
	 * @param ids
	 * @return
	 */
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids);

	/**
	 * 分析整体路段的指标呈现
	 * 
	 * @Date 20160411废弃,请使用doWholeIndex00Analysis
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @return
	 */
	@Deprecated
	public EasyuiPageList doWholeIndex0Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds);

	/**
	 * 分析整体路段的指标呈现
	 * 
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex00Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds);

	/**
	 * 分析整体路段的质差问题路段统计
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doWholeIndex1Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 分析整体页面的各质差路段数量占比
	 * 
	 * @param ids
	 * @return
	 */
	public EasyuiPageList doWholeIndex2Analysis(List<Long> ids);

	/**
	 * 分析整体页面所有质差问题路段的各问题小区数量
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex3Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds);

	/**
	 * 分析整体路段的主被叫位于不同制式下的mos指标
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex4Analysis(
			List<VolteQualityBadRoad> volteQualityBadRoadsByLogIds);

	/**
	 * 获取弱覆盖质差路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoadWeakCover> getWeakCoverRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取弱覆盖质差路段
	 * 
	 * @param id
	 * @return
	 */
	public VolteQualityBadRoadWeakCover getWeakCoverRoadById(Long id);

	/**
	 * 弱覆盖问题路段分析
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doWeakCoverAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段的指标呈现
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverIndexAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverCellInfoAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段天馈调整小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverTianKuiAdjustAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段天馈接反小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverTianKuiConnectReverseAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段建议加站小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverAdviceAddStationAnalysis(
			VolteQualityBadRoadWeakCover volteQualityBadRoadWeakCover);

	/**
	 * 获取干扰问题质差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteQualityBadRoadDisturbProblem> getDisturbRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取干扰问题质差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteQualityBadRoadDisturbProblem getDisturbRoadById(Long roadId);

	/**
	 * 干扰问题质差路段分析
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public Map<String, EasyuiPageList> doDisturbAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题路段的指标呈现
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbIndexAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题质差路段小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbCellInfoAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题质差路段PCI调整小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbPCIAdjustAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题质差路段天馈调整小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbTianKuiAdjustAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题质差路段三超小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbSanChaoCellAnalysis(
			VolteQualityBadRoadDisturbProblem disturbRoadById);

	/**
	 * 获取邻区配置问题质差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteQualityBadRoadNbDeficiency> getNbDeficiencyRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取邻区配置问题质差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteQualityBadRoadNbDeficiency getNbDeficiencyRoadById(Long roadId);

	/**
	 * 邻区配置问题质差路段分析
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public Map<String, EasyuiPageList> doNbDeficiencyAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency);

	/**
	 * 分析邻区配置问题LTE邻区添加建议详情表
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNbDeficiencyLTEAddAdviceAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency);

	/**
	 * 分析邻区配置问题GSM邻区添加建议详情表
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNbDeficiencyGSMAddAdviceAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency);

	/**
	 * 分析邻区配置问题邻区缺失的服务小区切换性能
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNbDeficiencyCoPerfAnalysis(
			VolteQualityBadRoadNbDeficiency volteQualityBadRoadNbDeficiency);

	/**
	 * 获取参数错误问题质差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteQualityBadRoadParamError> getParamErrorRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取参数错误问题质差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteQualityBadRoadParamError getParamErrorRoadById(Long roadId);

	/**
	 * 参数错误问题质差路段分析
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public Map<String, EasyuiPageList> doParamErrorAnalysis(
			VolteQualityBadRoadParamError volteQualityBadRoadParamError);

	/**
	 * 分析参数错误问题路段优化建议详情表
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doParamErrorOptimizeAdviceAnalysis(
			VolteQualityBadRoadParamError volteQualityBadRoadParamError);

	/**
	 * 获取核心网问题质差路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoadCoreNetwork> getCoreNetworkRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 核心网问题质差路段分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, List> doCoreNetworkAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds);

	/**
	 * 核心网问题质差路段rtp丢包率时间区间分布分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public List<Object> doCoreNetworkRtpPacketLostRatioHourChartAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds);

	/**
	 * 核心网问题质差路段rtp丢包率与sinr二维分布分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public List<Object> doCoreNetworkRtpPacketLostAndSinrChartAnalysis(
			List<Long> testLogItemIds);

	/**
	 * 核心网问题质差路段rtp丢包率与rsrp二维分布分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public List<Object> doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(
			List<Long> testLogItemIds);

	/**
	 * 获取其他问题质差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteQualityBadRoadOther> getOtherRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取其他问题质差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteQualityBadRoadOther getOtherRoadById(Long roadId);

	/**
	 * 其他问题质差路段分析
	 * 
	 * @param volteQualityBadRoadOther
	 * @return
	 */
	public Map<String, EasyuiPageList> doOtherAnalysis(
			VolteQualityBadRoadOther volteQualityBadRoadOther);

	/**
	 * 分析其他问题路段的指标呈现
	 * 
	 * @param volteQualityBadRoadOther
	 * @return
	 */
	public EasyuiPageList doOtherIndexAnalysis(
			VolteQualityBadRoadOther volteQualityBadRoadOther);

	/**
	 * 分析其他问题路段小区详情表
	 * 
	 * @param volteQualityBadRoadOther
	 * @return
	 */
	public EasyuiPageList doOtherCellInfoAnalysis(
			VolteQualityBadRoadOther volteQualityBadRoadOther);

	/**
	 * 其他问题路段信令多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doOtherRoadSignllingPageList(PageList pageList);

	/**
	 * 获取所有路段名称为空的质差路段
	 * 
	 * @return
	 */
	public List<VolteQualityBadRoad> getNullRoadNameQBR();

	/**
	 * 插入某条质差路段的名称
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public void addQBRRoadName(String roadName, Long qbrId);

	/**
	 * MOS差黑点分析
	 * 
	 * @param ids
	 * @param compareIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doMosBadAnalysis(List<Long> ids,
			List<Long> compareIds);

}
