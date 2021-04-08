/**
 * 
 */
package com.datang.service.VoLTEDissertation.continueWirelessBadRoad;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.VolteContinueWirelessBadRoadDisturbProblem;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.otherProblem.VolteContinueWirelessBadRoadOtherProblem;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover.VolteContinueWirelessBadRoadWeakCover;
import com.datang.domain.testLogItem.TestLogItem;

/**
 * Volte质量专题---连续无线差Service接口
 * 
 * @explain
 * @name IVolteContinueWirelessBadRoadService
 * @author shenyanwei
 * @date 2016年5月31日下午4:16:32
 */
public interface IVolteContinueWirelessBadRoadService {

	/**
	 * 获取单个连续无线差公共参数
	 * 
	 * @param cwbrId
	 * @return
	 */
	public VolteContinueWirelessBadRoad getVolteContinueWirelessBadRoad(
			Long cwbrId);

	/**
	 * 获取多个连续无线差路段公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteContinueWirelessBadRoad> getVolteContinueWirelessBadRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 汇总连续无线差路段里程
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public Float sumCWBRDistance(List<Long> testLogItemIds);

	/**
	 * 整体路段分析
	 * 
	 * @param volteContinueWirelessBadRoadsByLogIds
	 * @param queryTestLogItems
	 * @param ids
	 * @return
	 */
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids);

	/**
	 * 分析整体路段的指标呈现
	 * 
	 * 
	 * @param volteContinueWirelessBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex00Analysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds,
			List<Long> ids);

	/**
	 * 分析整体路段的连续无线差问题路段统计
	 * 
	 * @param volteContinueWirelessBadRoadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doWholeIndex1Analysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds,
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
	 * @param volteContinueWirelessBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex3Analysis(
			List<VolteContinueWirelessBadRoad> volteContinueWirelessBadRoadsByLogIds);

	/**
	 * 通过日志获取弱覆盖连续无线差路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteContinueWirelessBadRoadWeakCover> getWeakCoverRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过Id获取弱覆盖连续无线差路段
	 * 
	 * @param id
	 * @return
	 */
	public VolteContinueWirelessBadRoadWeakCover getWeakCoverRoadById(Long id);

	/**
	 * 弱覆盖问题路段分析
	 * 
	 * @param volteContinueWirelessBadRoadWeakCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doWeakCoverAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段小区详情表
	 * 
	 * @param volteContinueWirelessBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverCellInfoAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段天馈调整小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverTianKuiAdjustAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段天馈接反小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverTianKuiConnectReverseAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover);

	/**
	 * 分析弱覆盖问题路段建议加站小区详情表
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverAdviceAddStationAnalysis(
			VolteContinueWirelessBadRoadWeakCover volteContinueWirelessBadRoadWeakCover);

	/**
	 * 获取干扰问题连续无线差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteContinueWirelessBadRoadDisturbProblem> getDisturbRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取干扰问题连续无线差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteContinueWirelessBadRoadDisturbProblem getDisturbRoadById(
			Long roadId);

	/**
	 * 干扰问题路段连续无线差分析
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public Map<String, EasyuiPageList> doDisturbAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题连续无线差路段小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbCellInfoAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题连续无线差路段PCI调整小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbPCIAdjustAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题连续无线差路段天馈调整小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbTianKuiAdjustAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById);

	/**
	 * 分析干扰问题连续无线差路段三超小区详情表
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbSanChaoCellAnalysis(
			VolteContinueWirelessBadRoadDisturbProblem disturbRoadById);

	/**
	 * 获取邻区配置问题连续无线差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteContinueWirelessBadRoadNbDeficiency> getNbDeficiencyRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取邻区配置问题连续无线差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteContinueWirelessBadRoadNbDeficiency getNbDeficiencyRoadById(
			Long roadId);

	/**
	 * 邻区配置问题连续无线差路段分析
	 * 
	 * @param volteContinueWirelessBadRoadNbDeficiency
	 * @return
	 */
	public Map<String, EasyuiPageList> doNbDeficiencyAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency);

	/**
	 * 分析邻区配置问题LTE邻区添加建议详情表
	 * 
	 * @param volteContinueWirelessBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNbDeficiencyLTEAddAdviceAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency);

	/**
	 * 分析邻区配置问题GSM邻区添加建议详情表
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNbDeficiencyGSMAddAdviceAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency);

	/**
	 * 分析邻区配置问题邻区缺失的服务小区切换性能
	 * 
	 * @param volteContinueWirelessBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNbDeficiencyCoPerfAnalysis(
			VolteContinueWirelessBadRoadNbDeficiency volteContinueWirelessBadRoadNbDeficiency);

	/**
	 * 获取其他问题连续无线差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<VolteContinueWirelessBadRoadOtherProblem> getOtherRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 获取其他问题连续无线差路段
	 * 
	 * @param roadId
	 * @return
	 */
	public VolteContinueWirelessBadRoadOtherProblem getOtherRoadById(Long roadId);

	/**
	 * 其他问题连续无线差路段分析
	 * 
	 * @param volteContinueWirelessBadRoadOtherProblem
	 * @return
	 */
	public Map<String, EasyuiPageList> doOtherAnalysis(
			VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem);

	/**
	 * 分析其他问题路段小区详情表
	 * 
	 * @param volteContinueWirelessBadRoadOtherProblem
	 * @return
	 */
	public EasyuiPageList doOtherCellInfoAnalysis(
			VolteContinueWirelessBadRoadOtherProblem volteContinueWirelessBadRoadOtherProblem);

	/**
	 * 其他问题路段信令多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doOtherRoadSignllingPageList(PageList pageList);

	/**
	 * 获取所有路段名称为空的连续无线差路段
	 * 
	 * @return
	 */
	public List<VolteContinueWirelessBadRoad> getNullRoadNameCWBR();

	/**
	 * 插入某条连续无线差路段的名称
	 * 
	 * @param roadName
	 * @param cwbrId
	 */
	public void addCWBRoadName(String roadName, Long cwbrId);

}
