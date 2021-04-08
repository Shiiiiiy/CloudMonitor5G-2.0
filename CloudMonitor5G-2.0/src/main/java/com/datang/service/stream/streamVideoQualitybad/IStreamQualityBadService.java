/**
 * 
 */
package com.datang.service.stream.streamVideoQualitybad;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;
import com.datang.domain.stream.streamVideoQualitybad.disturb.StreamQualtyBadDisturb;
import com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall.StreamQualityBadDownDispatchSmall;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamQualityBadNeighbourPlot;
import com.datang.domain.stream.streamVideoQualitybad.other.StreamQualityBadOther;
import com.datang.domain.stream.streamVideoQualitybad.overCover.StreamQualityBadOverCover;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamQualityBadPingPong;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamQualityBadWeakCover;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamStatementeResponseBean;

/**
 * 流媒体专题---流媒体视频质差Service接口
 * 
 * @explain
 * @name IStreamQualityBadService
 * @author shenyanwei
 * @date 2017年10月23日上午11:05:54
 */
public interface IStreamQualityBadService {

	/**
	 * 获取单个流媒体视频质差公共参数
	 * 
	 * @param qbrId
	 * @return
	 */
	public StreamQualityBad getStreamQualityBad(Long vqbId);

	/**
	 * 获取多个流媒体视频质差公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBad> getStreamQualityBadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 整体分析
	 * 
	 * @param streamQualityBadsByLogIds
	 * @param queryTestLogItems
	 * @param ids
	 * @return
	 */
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids);

	/**
	 * 分析整体的指标呈现
	 * 
	 * 
	 * @param streamQualityBadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex00Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds);

	/**
	 * 分析整体的质差问题指标汇总统计
	 * 
	 * @param streamQualityBadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doWholeIndex1Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids);

	/**
	 * 分析整体页面的各质差问题原因数量占比
	 * 
	 * @param ids
	 * @return
	 */
	public EasyuiPageList doWholeIndex2Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 分析整体页面所有质差问题的各问题小区数量等
	 * 
	 * @param streamQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex3Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds);

	/**
	 * 分析整体的VMOS分布
	 * 
	 * @param streamQualityBadRoadsByLogIds
	 * @return
	 */
	public EasyuiPageList doWholeIndex4Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds);

	/**
	 * 根据日志IDs获取弱覆盖问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBadWeakCover> getStreamWeakCoversByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取弱覆盖问题
	 * 
	 * @param id
	 * @return
	 */
	public StreamQualityBadWeakCover getStreamWeakCoverById(Long id);

	/**
	 * 弱覆盖问题分析
	 * 
	 * @param streamQualityBadWeakCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamWeakCoverAnalysis(
			StreamQualityBadWeakCover streamQualityBadWeakCover);

	/**
	 * 分析弱覆盖问题路段优化建议
	 * 
	 * @param streamQualityBadRoadWeakCover
	 * @return
	 */
	public EasyuiPageList doWeakCoverAdviceAnalysis(
			StreamQualityBadWeakCover streamQualityBadWeakCover);

	/**
	 * 根据日志获取干扰问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<StreamQualtyBadDisturb> getStreamDisturbsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取干扰问题
	 * 
	 * @param roadId
	 * @return
	 */
	public StreamQualtyBadDisturb getStreamDisturbById(Long roadId);

	/**
	 * 干扰问题分析
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamDisturbAnalysis(
			StreamQualtyBadDisturb streamDisturbById);

	/**
	 * 分析干扰问题优化建议：pci或信号强度调整
	 * 
	 * @param disturbRoadById
	 * @return
	 */
	public EasyuiPageList doDisturbAdviceAnalysis(
			StreamQualtyBadDisturb streamDisturbById);

	/**
	 * 根据日志获取邻区问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<StreamQualityBadNeighbourPlot> getStreamNeighbourPlotsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取邻区问题
	 * 
	 * @param roadId
	 * @return
	 */
	public StreamQualityBadNeighbourPlot getStreamNeighbourPlotById(Long roadId);

	/**
	 * 邻区问题分析
	 * 
	 * @param streamQualityBadRoadNbDeficiency
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamNeighbourPlotAnalysis(
			StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot);

	/**
	 * 分析邻区问题优化建议添加邻区
	 * 
	 * @param streamQualityBadRoadNbDeficiency
	 * @return
	 */
	public EasyuiPageList doNeighbourPlotAdviceAnalysis(
			StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot);

	/**
	 * 根据日志获取重叠覆盖问题
	 * 
	 * @param ids
	 * @return
	 */
	public List<StreamQualityBadOverCover> getStreamOverCoversByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取重叠覆盖问题
	 * 
	 * @param roadId
	 * @return
	 */
	public StreamQualityBadOverCover getStreamOverCoverById(Long roadId);

	/**
	 * 重叠覆盖问题分析
	 * 
	 * @param streamQualityBadOverCover
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamOverCoverAnalysis(
			StreamQualityBadOverCover streamQualityBadOverCover);

	/**
	 * 分析重叠覆盖问题优化建议方位角下倾角调整
	 * 
	 * @param
	 * @return
	 */
	public EasyuiPageList doOverCoverAdviceAnalysis(
			StreamQualityBadOverCover streamQualityBadOverCover);

	/**
	 * 通过日志获取乒乓切换问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBadPingPong> getStreamPingPongsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过ID获取乒乓切换问题
	 */
	public StreamQualityBadPingPong getStreamPingPongById(Long id);

	/**
	 * 乒乓切换问题分析
	 * 
	 * @param
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamPingPongAnalysis(
			StreamQualityBadPingPong streamQualityBadPingPong);

	/**
	 * 分析乒乓切换问题优化建议调整切换参数
	 * 
	 * @param
	 * @return
	 */
	public EasyuiPageList doPingPongAdviceAnalysis(
			StreamQualityBadPingPong streamQualityBadPingPong);

	/**
	 * 分析乒乓切换问题切换事件详情
	 * 
	 * @param
	 * @return
	 */
	public EasyuiPageList doPingPongCutEventAnalysis(
			StreamQualityBadPingPong streamQualityBadPingPong);

	/**
	 * 根据日志获取其他问
	 * 
	 * @param ids
	 * @return
	 */
	public List<StreamQualityBadOther> getStreamOthersByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取其他问题
	 * 
	 * @param roadId
	 * @return
	 */
	public StreamQualityBadOther getStreamOtherById(Long roadId);

	/**
	 * 其他问题质差路段分析
	 * 
	 * @param streamQualityBadRoadOther
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamOtherAnalysis(
			StreamQualityBadOther streamQualityBadOther);

	/**
	 * 通过日志获取下行调度数小问题
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBadDownDispatchSmall> getStreamDownDispatchSmallsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 通过ID获取下行调度数小问题
	 */
	public StreamQualityBadDownDispatchSmall getStreamDownDispatchSmallById(
			Long id);

	/**
	 * 下行调度数小问题分析
	 * 
	 * @param
	 * @param testLogItemIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doStreamDownDispatchSmallAnalysis(
			StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall);

	/**
	 * 分析下行调度数小问题优化建议：核查小区用户数以及是否存在干扰
	 * 
	 * @param volteQualityBadRoadParamError
	 * @return
	 */
	public EasyuiPageList doDownDispatchSmallAdviceAnalysis(
			StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall);

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
	public List<StreamStatementeResponseBean> getDownloadInfo(
			List<StreamQualityBad> streamQualityBads);

	/**
	 * 路段回填
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public void addQBRRoadName(String roadName, Long qbrId);

	/**
	 * 获取整体概览VMOS占比分布
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public List queryVmosValue(List<Long> testLogItemIds);
}
