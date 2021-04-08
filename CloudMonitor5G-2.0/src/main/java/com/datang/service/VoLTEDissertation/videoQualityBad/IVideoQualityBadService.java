/**
 * 
 */
package com.datang.service.VoLTEDissertation.videoQualityBad;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;
import com.datang.domain.VoLTEDissertation.videoQualityBad.disturb.VideoQualtyBadDisturb;
import com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall.VideoQualityBadDownDispatchSmall;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.VideoQualityBadNeighbourPlot;
import com.datang.domain.VoLTEDissertation.videoQualityBad.other.VideoQualityBadOther;
import com.datang.domain.VoLTEDissertation.videoQualityBad.overCover.VideoQualityBadOverCover;
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.VideoQualityBadPatternSwitch;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.VideoQualityBadPingPong;
import com.datang.domain.VoLTEDissertation.videoQualityBad.weakCover.VideoQualityBadWeakCover;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoStatementeResponseBean;

/**
 * volte质量专题---volte视频质差Service接口
 * 
 * @explain
 * @name IVideoQualityBadService
 * @author shenyanwei
 * @date 2017年5月12日下午1:55:39
 */
public interface IVideoQualityBadService {

	/**
	 * 获取单个volte视频质差公共参数
	 * 
	 * @param qbrId
	 * @return
	 */
	public VideoQualityBad getVideoQualityBad(Long vqbId);

	/**
	 * 获取多个volte视频质差公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBad> getVideoQualityBadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 整体分析
	 * 
	 * @param videoQualityBadsByLogIds
	 * @param queryTestLogItems
	 * @param ids
	 * @return
	 */
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids);

	/**
	 * 分析整体的指标呈现
	 * 
	 * 
	 * @param videoQualityBadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex00Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds);

	/**
	 * 分析整体的质差问题指标汇总统计
	 * 
	 * @param videoQualityBadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doWholeIndex1Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids);

	/**
	 * 分析整体页面的各质差问题原因数量占比
	 * 
	 * @param ids
	 * @return
	 */
	public EasyuiPageList doWholeIndex2Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 分析整体页面所有质差问题的各问题小区数量等
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex3Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds);

	/**
	 * 分析整体的VMOS分布
	 * 
	 * @param volteQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex4Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds);

	/**
	 * 根据日志IDs获取弱覆盖问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBadWeakCover> getVideoWeakCoversByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取弱覆盖问题
	 * 
	 * @param id
	 * @return
	 */
	public VideoQualityBadWeakCover getVideoWeakCoverById(Long id);

	/**
	 * 弱覆盖问题分析
	 * 
	 * @param videoQualityBadWeakCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoWeakCoverAnalysis(
			VideoQualityBadWeakCover videoQualityBadWeakCover);

	/**
	 * 分析弱覆盖问题路段优化建议
	 * 
	 * @param volteQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverAdjustAntennaFeederAnalysis(
			VideoQualityBadWeakCover videoQualityBadWeakCover);

	/**
	 * 根据日志获取干扰问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<VideoQualtyBadDisturb> getVideoDisturbsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取干扰问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VideoQualtyBadDisturb getVideoDisturbById(Long roadId);

	/**
	 * 干扰问题分析
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoDisturbAnalysis(
			VideoQualtyBadDisturb videoDisturbById);

	/**
	 * 分析干扰问题优化建议：pci或信号强度调整
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbPciOrSignalStrengthAdjustAnalysis(
			VideoQualtyBadDisturb videoDisturbById);

	/**
	 * 根据日志获取邻区问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<VideoQualityBadNeighbourPlot> getVideoNeighbourPlotsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取邻区问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VideoQualityBadNeighbourPlot getVideoNeighbourPlotById(Long roadId);

	/**
	 * 邻区问题分析
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoNeighbourPlotAnalysis(
			VideoQualityBadNeighbourPlot videoQualityBadNeighbourPlot);

	/**
	 * 分析邻区问题优化建议添加邻区
	 * 
	 * @param volteQualityBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNeighbourPlotAddNeighbourPlotAnalysis(
			VideoQualityBadNeighbourPlot videoQualityBadNeighbourPlot);

	/**
	 * 根据日志获取重叠覆盖问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<VideoQualityBadOverCover> getVideoOverCoversByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取重叠覆盖问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VideoQualityBadOverCover getVideoOverCoverById(Long roadId);

	/**
	 * 重叠覆盖问题分析
	 * 
	 * @param videoQualityBadOverCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoOverCoverAnalysis(
			VideoQualityBadOverCover videoQualityBadOverCover);

	/**
	 * 分析重叠覆盖问题优化建议方位角下倾角调整
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doOverCoverAzimuthOrDowndipAngleAdjustAnalysis(
			VideoQualityBadOverCover videoQualityBadOverCover);

	/**
	 * 通过日志获取乒乓切换问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBadPingPong> getVideoPingPongsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过ID获取乒乓切换问题
	 */
	public VideoQualityBadPingPong getVideoPingPongById(Long id);

	/**
	 * 乒乓切换问题分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoPingPongAnalysis(
			VideoQualityBadPingPong videoQualityBadPingPong);

	/**
	 * 分析乒乓切换问题优化建议调整切换参数
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doPingPongAdjustCutParameterAnalysis(
			VideoQualityBadPingPong videoQualityBadPingPong);

	/**
	 * 分析乒乓切换问题切换事件详情
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doPingPongCutEventAnalysis(
			VideoQualityBadPingPong videoQualityBadPingPong);

	/**
	 * 根据日志获取其他问
	 * 
	 * @param ids
	 * @return
	 */
	public List<VideoQualityBadOther> getVideoOthersByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取其他问题
	 * 
	 * @param roadId
	 * @return
	 */
	public VideoQualityBadOther getVideoOtherById(Long roadId);

	/**
	 * 其他问题质差路段分析
	 * 
	 * @param volteQualityBadRoadOther
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoOtherAnalysis(
			VideoQualityBadOther videoQualityBadOther);

	/**
	 * 通过日志获取模式转换问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBadPatternSwitch> getVideoPatternSwitchsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过ID获取模式转换问题
	 */
	public VideoQualityBadPatternSwitch getVideoPatternSwitchById(Long id);

	/**
	 * 模式转换问题分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoPatternSwitchAnalysis(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch);

	/**
	 * 分析模式转换问题优化建议：调整模式转换设置值
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doPatternSwitchAdjustAnalysis(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch);

	/**
	 * 分析模式转换问题关键指标
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doPatternSwitchKeyIndexAnalysis(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch);

	/**
	 * 通过日志获取下行调度数小问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBadDownDispatchSmall> getVideoDownDispatchSmallsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过ID获取下行调度数小问题
	 */
	public VideoQualityBadDownDispatchSmall getVideoDownDispatchSmallById(
			Long id);

	/**
	 * 下行调度数小问题分析
	 * 
	 * @param rtpPacketLostRatioHourChart
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doVideoDownDispatchSmallAnalysis(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall);

	/**
	 * 分析下行调度数小问题优化建议：核查小区用户数以及是否存在干扰
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doDownDispatchSmallAdjustAnalysis(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall);

	/**
	 * 分析下行调度数小问题关键指标
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doDownDispatchSmallKeyIndexAnalysis(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall);

	/**
	 * 分析下行调度数小问题关键指标
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public LteCell queryLteCellInfoByCellId(Long cellId);

	/**
	 * 报表导出
	 */
	public List<VideoStatementeResponseBean> getDownloadInfo(
			List<VideoQualityBad> videoQualityBads);

	/**
	 * 路段回填
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public void addQBRRoadName(String roadName, Long qbrId);
}
