package com.datang.service.VoLTEDissertation.voiceRTPLostPacket.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.dao.VoLTEDissertation.voiceRTPLostPacket.PacketSendDetailsDao;
import com.datang.dao.VoLTEDissertation.voiceRTPLostPacket.VolteVoiceRTPLostPacketDao;
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.PacketSendDetails;
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.VolteVoiceRTPLostPacket;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.voiceRTPLostPacket.IVolteVoiceRTPLostPacketService;
import com.datang.web.beans.VoLTEDissertation.voiceRTPLostPacket.LostPacketCollectResponseBean;
import com.datang.web.beans.VoLTEDissertation.voiceRTPLostPacket.NodesDistributionResponseBean;

/**
 * 语音RTP连续丢包公共指标Service实现
 * 
 * @explain
 * @name VolteVoiceRTPLostPacketServiceImpl
 * @author shenyanwei
 * @date 2017年2月15日上午10:23:23
 */
@Service
@Transactional
public class VolteVoiceRTPLostPacketServiceImpl implements
		IVolteVoiceRTPLostPacketService {
	@Autowired
	private VolteVoiceRTPLostPacketDao volteVoiceRTPLostPacketDao;
	@Autowired
	private PacketSendDetailsDao packetSendDetailsDao;

	@Override
	public void addQBRRoadName(String roadName, Long id) {
		volteVoiceRTPLostPacketDao.addQBRRoadName(roadName, id);

	}

	@Override
	public List<VolteVoiceRTPLostPacket> queryVolteVoiceRTPLostPacketsByLogIds(
			List<Long> testLogItemIds) {
		return volteVoiceRTPLostPacketDao
				.queryVolteVoiceRTPLostPacketsByLogIds(testLogItemIds);

	}

	@Override
	public VolteVoiceRTPLostPacket queryLostPacketById(Long id) {

		return volteVoiceRTPLostPacketDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VolteVoiceRTPLostPacket> rtpLostPackets,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		// 汇总问题路段指标
		LostPacketCollectResponseBean lostPacketCollectResponseBean = new LostPacketCollectResponseBean();
		// 汇总问题数量
		lostPacketCollectResponseBean.setProblemNumber(rtpLostPackets.size());
		// 汇总总测试时长
		Long time = null;
		// 汇总mos均值
		// Float sumValue = null;
		// Long sumNumber = null;
		// 汇总主叫个数
		Integer sumMainCall = 0;
		// 汇总被叫个数
		Integer sumCalls = 0;
		// 汇总成功关联网络侧问题数
		Integer sumSucceed = 0;
		// 汇总饼图数据
		Integer number1 = 0;
		Integer number2 = 0;
		Integer number3 = 0;
		Integer number4 = 0;
		Integer number5 = 0;
		Integer number6 = 0;
		for (VolteVoiceRTPLostPacket volteVoiceRTPLostPacket : rtpLostPackets) {
			if (volteVoiceRTPLostPacket.getStartTime() != null
					&& volteVoiceRTPLostPacket.getEndTime() != null) {
				if (time == null) {
					time = (volteVoiceRTPLostPacket.getEndTime() - volteVoiceRTPLostPacket
							.getStartTime());
				} else {
					time += (volteVoiceRTPLostPacket.getEndTime() - volteVoiceRTPLostPacket
							.getStartTime());
				}

			}

			// sumValue += volteVoiceRTPLostPacket.getM_ulMosPointSum();
			// sumNumber += volteVoiceRTPLostPacket.getM_ulMosPointNum();
			if (volteVoiceRTPLostPacket.getTestLogItem().getCallType() == 0) {
				sumMainCall++;
			} else if (volteVoiceRTPLostPacket.getTestLogItem().getCallType() == 1) {
				sumCalls++;
			} else {

			}

			if (volteVoiceRTPLostPacket.getHasNetworkTestLogItem()) {
				sumSucceed++;
			}

			if (volteVoiceRTPLostPacket.getProblemNode() == 0) {
				number1++;
			} else if (volteVoiceRTPLostPacket.getProblemNode() == 1) {
				number2++;
			} else if (volteVoiceRTPLostPacket.getProblemNode() == 2) {
				number3++;
			} else if (volteVoiceRTPLostPacket.getProblemNode() == 3) {
				number4++;
			} else if (volteVoiceRTPLostPacket.getProblemNode() == 4) {
				number5++;
			} else if (volteVoiceRTPLostPacket.getProblemNode() == 5) {
				number6++;
			}
		}
		lostPacketCollectResponseBean.setTestTime(time);
		// if (sumNumber != null && sumNumber != 0) {
		// lostPacketCollectResponseBean.setMosAvg(String.valueOf(sumValue
		// / sumNumber));
		// }

		lostPacketCollectResponseBean.setMainCallNumber(sumMainCall);
		lostPacketCollectResponseBean.setCallsNumber(sumCalls);
		lostPacketCollectResponseBean.setSucceedRelevance(sumSucceed);
		EasyuiPageList easyuiPageList0 = new EasyuiPageList();
		List<LostPacketCollectResponseBean> rows0 = new ArrayList<>();
		rows0.add(lostPacketCollectResponseBean);
		easyuiPageList0.setRows(rows0);
		NodesDistributionResponseBean nodesDistributionResponseBean = new NodesDistributionResponseBean();
		ArrayList<NodesDistributionResponseBean> rows1 = new ArrayList<>();
		EasyuiPageList easyuiPageList1 = new EasyuiPageList();
		nodesDistributionResponseBean.setNode1(number1);
		nodesDistributionResponseBean.setNode2(number2);
		nodesDistributionResponseBean.setNode3(number3);
		nodesDistributionResponseBean.setNode4(number4);
		nodesDistributionResponseBean.setNode5(number5);
		nodesDistributionResponseBean.setNode6(number6);
		rows1.add(nodesDistributionResponseBean);
		easyuiPageList1.setRows(rows1);
		map.put("wholeIndex0", easyuiPageList0);
		map.put("wholeIndex1", easyuiPageList1);
		return map;
	}

	@Override
	public Map<String, EasyuiPageList> doProblemAnalysis(
			VolteVoiceRTPLostPacket rtpLostPacket) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		ArrayList<VolteVoiceRTPLostPacket> rows = new ArrayList<>();
		rows.add(rtpLostPacket);
		easyuiPageList.setRows(rows);
		map.put("problemIndex0", easyuiPageList);
		map.put("problemIndex1", getPublicRTPSend(rtpLostPacket, 0));
		map.put("problemIndex2", getPublicRTPSend(rtpLostPacket, 1));
		map.put("problemIndex3", getPublicRTPSend(rtpLostPacket, 2));
		map.put("problemIndex4", getPublicRTPSend(rtpLostPacket, 3));
		map.put("problemIndex5", getPublicRTPSend(rtpLostPacket, 4));
		map.put("problemIndex6", getPublicRTPSend(rtpLostPacket, 5));
		return map;
	}

	/**
	 * 获取各端口RTP包发送情况
	 * 
	 * @return
	 */
	@Override
	public EasyuiPageList getPublicRTPSend(
			VolteVoiceRTPLostPacket rtpLostPacket, Integer index) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		ArrayList<PacketSendDetails> rows = new ArrayList<>();
		if (rtpLostPacket != null && index != null) {
			List<PacketSendDetails> queryPacketSendDetailsByPerid = packetSendDetailsDao
					.queryPacketSendDetailsByPerid(rtpLostPacket.getId(), index);
			rows.addAll(queryPacketSendDetailsByPerid);
		}
		easyuiPageList.setRows(rows);
		return easyuiPageList;

	}

}
