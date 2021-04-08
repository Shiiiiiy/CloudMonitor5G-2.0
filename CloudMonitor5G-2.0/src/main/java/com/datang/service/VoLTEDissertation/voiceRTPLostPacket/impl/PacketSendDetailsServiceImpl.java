package com.datang.service.VoLTEDissertation.voiceRTPLostPacket.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.VoLTEDissertation.voiceRTPLostPacket.PacketSendDetailsDao;
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.PacketSendDetails;
import com.datang.service.VoLTEDissertation.voiceRTPLostPacket.IPacketSendDetailsService;

/**
 * 各端口发送RTP包详情Service实现
 * 
 * @explain
 * @name PacketSendDetailsServiceImpl
 * @author shenyanwei
 * @date 2017年2月15日上午10:23:53
 */
@Service
@Transactional
public class PacketSendDetailsServiceImpl implements IPacketSendDetailsService {
	@Autowired
	private PacketSendDetailsDao packetSendDetailsDao;

	@Override
	public List<PacketSendDetails> queryPacketSendDetailsByPerid(Long parid,
			Integer index) {

		return packetSendDetailsDao.queryPacketSendDetailsByPerid(parid, index);
	}

}
