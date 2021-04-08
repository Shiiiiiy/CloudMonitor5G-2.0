package com.datang.service.VoLTEDissertation.voiceRTPLostPacket;

import java.util.List;

import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.PacketSendDetails;

/**
 * 各端口发送RTP包详情Service接口
 * 
 * @explain
 * @name IPacketSendDetailsService
 * @author shenyanwei
 * @date 2017年2月15日上午10:15:11
 */
public interface IPacketSendDetailsService {
	/**
	 * 根据关联ID和端口类型查询发包详情
	 * 
	 * @param parid
	 * @param index
	 * @return
	 */
	public List<PacketSendDetails> queryPacketSendDetailsByPerid(Long parid,
			Integer index);
}
