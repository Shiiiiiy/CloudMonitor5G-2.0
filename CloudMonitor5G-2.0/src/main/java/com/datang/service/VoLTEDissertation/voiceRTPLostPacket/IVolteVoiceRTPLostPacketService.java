package com.datang.service.VoLTEDissertation.voiceRTPLostPacket;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.VolteVoiceRTPLostPacket;
import com.datang.domain.testLogItem.TestLogItem;

/**
 * 语音RTP连续丢包公共指标Service接口
 * 
 * @explain
 * @name IVolteVoiceRTPLostPacketService
 * @author shenyanwei
 * @date 2017年2月15日上午10:19:00
 */
public interface IVolteVoiceRTPLostPacketService {

	/**
	 * 添加某条切换失败的路段名称
	 * 
	 * @param roadName
	 * @param droppingId
	 */
	public void addQBRRoadName(String roadName, Long id);

	/**
	 * 根据测试日志的id集合获取所有语音RTP连续丢包
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteVoiceRTPLostPacket> queryVolteVoiceRTPLostPacketsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据ID获取RTP连续丢包
	 * 
	 * @param id
	 * @return
	 */
	public VolteVoiceRTPLostPacket queryLostPacketById(Long id);

	/**
	 * 整体分析
	 */
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VolteVoiceRTPLostPacket> rtpLostPackets,
			List<TestLogItem> queryTestLogItems);

	/**
	 * 问题分析
	 */
	public Map<String, EasyuiPageList> doProblemAnalysis(
			VolteVoiceRTPLostPacket rtpLostPacket);

	/**
	 * 各端口包发送情况
	 * 
	 * @param rtpLostPacket
	 * @param index
	 * @return
	 */
	public EasyuiPageList getPublicRTPSend(
			VolteVoiceRTPLostPacket rtpLostPacket, Integer index);
}
