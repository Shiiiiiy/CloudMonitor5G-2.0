package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.VolteVoiceRTPLostPacket;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.VoLTEDissertation.voiceRTPLostPacket.IVolteVoiceRTPLostPacketService;
import com.datang.service.gis.ILostPacketGpsPointService;

/**
 * @explain
 * @name LostPacketGpsPointServiceImpl
 * @author shenyanwei
 * @date 2017年2月21日下午1:51:26
 */
@Service
@Transactional
@SuppressWarnings("all")
public class LostPacketGpsPointServiceImpl implements
		ILostPacketGpsPointService {
	@Autowired
	IVolteVoiceRTPLostPacketService lostPacketService;
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;

	@Override
	public List<List<Object>> getEveryLPGpsPointsByTestlogIds(
			String testLogItemIds) {
		String[] logIds = testLogItemIds.trim().split(",");
		// 存储TestLogItem的id集合
		List<Long> ids = new ArrayList<>();
		for (int i = 0; i < logIds.length; i++) {
			if (StringUtils.hasText(logIds[i])) {
				try {
					ids.add(Long.parseLong(logIds[i].trim()));
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		List<VolteVoiceRTPLostPacket> list0 = new ArrayList<>();
		List<VolteVoiceRTPLostPacket> list1 = new ArrayList<>();
		List<VolteVoiceRTPLostPacket> list2 = new ArrayList<>();
		List<VolteVoiceRTPLostPacket> list3 = new ArrayList<>();
		List<VolteVoiceRTPLostPacket> list4 = new ArrayList<>();
		List<VolteVoiceRTPLostPacket> list5 = new ArrayList<>();
		List<VolteVoiceRTPLostPacket> queryVolteVoiceRTPLostPacketsByLogIds = lostPacketService
				.queryVolteVoiceRTPLostPacketsByLogIds(ids);
		if (queryVolteVoiceRTPLostPacketsByLogIds != null
				&& queryVolteVoiceRTPLostPacketsByLogIds.size() != 0) {
			for (VolteVoiceRTPLostPacket volteVoiceRTPLostPacket : queryVolteVoiceRTPLostPacketsByLogIds) {
				switch (volteVoiceRTPLostPacket.getProblemNode()) {
				case 0:
					list0.add(volteVoiceRTPLostPacket);
					break;
				case 1:
					list1.add(volteVoiceRTPLostPacket);
					break;
				case 2:
					list2.add(volteVoiceRTPLostPacket);
					break;
				case 3:
					list3.add(volteVoiceRTPLostPacket);
					break;
				case 4:
					list4.add(volteVoiceRTPLostPacket);
					break;
				case 5:
					list5.add(volteVoiceRTPLostPacket);
					break;
				default:
					break;
				}
			}
		}
		List<List<Object>> lists = new ArrayList<>();
		// 节点1,发送端手机上行丢包'
		List<Object> sendUEList = new LinkedList<>();
		sendUEList.add(0, "sendUE");
		for (VolteVoiceRTPLostPacket lostPacket : list0) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByLPId(lostPacket);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				sendUEList.add(pointsByQBRId);
			}
		}
		// 节点2,发送端S1口丢包'
		List<Object> sendS1List = new LinkedList<>();
		sendS1List.add(0, "sendS1");
		for (VolteVoiceRTPLostPacket lostPacket : list1) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByLPId(lostPacket);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				sendS1List.add(pointsByQBRId);
			}
		}
		// 节点3,发送端SGi口丢包
		List<Object> sendSGiList = new LinkedList<>();
		sendSGiList.add(0, "sendSGi");
		for (VolteVoiceRTPLostPacket lostPacket : list2) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByLPId(lostPacket);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				sendSGiList.add(pointsByQBRId);
			}
		}
		// 节点4,接收端SGi口丢包'
		List<Object> receSGiList = new LinkedList<>();
		receSGiList.add(0, "receSGi");
		for (VolteVoiceRTPLostPacket lostPacket : list3) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByLPId(lostPacket);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				receSGiList.add(pointsByQBRId);
			}
		}
		// 节点5,接收端S1口丢包'
		List<Object> receS1List = new LinkedList<>();
		receS1List.add(0, "receS1");
		for (VolteVoiceRTPLostPacket lostPacket : list4) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByLPId(lostPacket);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				receS1List.add(pointsByQBRId);
			}
		}
		// 节点6,接收端Uu口丢包
		List<Object> receUEList = new LinkedList<>();
		receUEList.add(0, "receUE");
		for (VolteVoiceRTPLostPacket lostPacket : list5) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByLPId(lostPacket);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				receUEList.add(pointsByQBRId);
			}
		}
		lists.add(sendUEList);
		lists.add(sendS1List);
		lists.add(sendSGiList);
		lists.add(receSGiList);
		lists.add(receS1List);
		lists.add(receUEList);
		return lists;

	}

	public List<TestLogItemGpsPoint> getPointsByLPId(
			VolteVoiceRTPLostPacket lostPacket) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != lostPacket) {
			Long startTime = lostPacket.getStartTime();
			Long endTime = lostPacket.getEndTime();
			if ((endTime - startTime) < 10000) {
				Long time = (10000 - (endTime - startTime)) / 2;
				startTime = startTime - time;
				endTime = endTime + time;
			}
			gpsPoints = testLogItemGpsPointDao
					.getTestLogItemGpsIndexPointDirection(lostPacket
							.getTestLogItem().getRecSeqNo(), null, startTime,
							endTime);
		}
		return gpsPoints;
	}
}
