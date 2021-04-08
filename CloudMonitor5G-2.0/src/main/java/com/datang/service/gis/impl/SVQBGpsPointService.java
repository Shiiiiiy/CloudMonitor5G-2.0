/**
 * 
 */
package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.constant.VideoQBType;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamDisturbDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamDownDispatchSmallDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamNeighbourPlotDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamOtherDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamOverCoverDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamPingPongDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamQualityBadDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamWeakCoverDao;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;
import com.datang.service.gis.ISVQBGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 流媒体专题---视频质差轨迹点service接口实现
 * 
 * @explain
 * @name SVQBGpsPointService
 * @author shenyanwei
 * @date 2017年11月27日下午1:29:19
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SVQBGpsPointService implements ISVQBGpsPointService {
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private StreamDisturbDao streamDisturbDao;
	@Autowired
	private StreamNeighbourPlotDao streamNeighbourPlotDao;
	@Autowired
	private StreamOtherDao streamOtherDao;
	@Autowired
	private StreamOverCoverDao streamOverCoverDao;
	@Autowired
	private StreamPingPongDao streamPingPongDao;
	@Autowired
	private StreamQualityBadDao streamQualityBadDao;
	@Autowired
	private StreamDownDispatchSmallDao streamDownDispatchSmallDao;
	@Autowired
	private StreamWeakCoverDao streamWeakCoverDao;

	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByVQBIdAndIndexType(
			Long badRoadId, Integer indexType) {

		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId && null != indexType) {
			StreamQualityBad find = streamQualityBadDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint(find.getTestLogItem()
								.getRecSeqNo(), indexType, find.getStartTime(),
								find.getEndTime());
			}
		}
		return gpsPoints;

	}

	@Override
	public List<TestLogItemGpsPoint> getPointDirectionsByVQBIdAndIndexType(
			Long badRoadId, Integer indexType) {

		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			StreamQualityBad find = streamQualityBadDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDao
						.getTestLogItemGpsIndexPointDirection(find
								.getTestLogItem().getRecSeqNo(), indexType,
								find.getStartTime(), find.getEndTime());
			}
		}
		return gpsPoints;
	}

	@Override
	public List<TestLogItemGpsPoint> getPointsByVQBId(Long badRoadId) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId) {
			StreamQualityBad find = streamQualityBadDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDao
						.getTestLogItemGpsIndexPointDirection(find
								.getTestLogItem().getRecSeqNo(), null, find
								.getStartTime(), find.getEndTime());
			}
		}
		return gpsPoints;

	}

	@Override
	public List<List<Object>> getEveryVQBGpsPointsByTestlogIds(
			String testLogItemIds) {
		List<List<Object>> lists = new ArrayList<>();
		// 计算弱覆盖gps采样点
		List<Object> weakCoverList = new LinkedList<>();
		weakCoverList.add(0, VideoQBType.WeakCover.toString());
		List<Long> weakCoverIds = queryVQBIds(testLogItemIds,
				VideoQBType.WeakCover);
		List<TestLogItemGpsPoint> weakCoverPointsByQBRId = getPointsByVQBIds(weakCoverIds);
		if (null != weakCoverPointsByQBRId
				&& 0 != weakCoverPointsByQBRId.size()) {
			weakCoverList.addAll(weakCoverPointsByQBRId);
		}

		// 计算干扰gps采样点
		List<Object> disturbList = new LinkedList<>();
		disturbList.add(0, VideoQBType.Disturb.toString());
		List<Long> disturbIds = queryVQBIds(testLogItemIds, VideoQBType.Disturb);
		List<TestLogItemGpsPoint> disturbPointsByQBRId = getPointsByVQBIds(disturbIds);
		if (null != disturbPointsByQBRId && 0 != disturbPointsByQBRId.size()) {
			disturbList.addAll(disturbPointsByQBRId);
		}
		// 计算邻区配置gps采样点
		List<Object> neighbourList = new LinkedList<>();
		neighbourList.add(0, VideoQBType.Neighbour.toString());
		List<Long> nbCellIds = queryVQBIds(testLogItemIds,
				VideoQBType.Neighbour);
		List<TestLogItemGpsPoint> nbCellPointsByQBRId = getPointsByVQBIds(nbCellIds);
		if (null != nbCellPointsByQBRId && 0 != nbCellPointsByQBRId.size()) {
			neighbourList.addAll(nbCellPointsByQBRId);
		}
		// 计算乒乓切换gps采样点
		List<Object> pingPongList = new LinkedList<>();
		pingPongList.add(0, VideoQBType.PingPong.toString());
		List<Long> paramErrorIds = queryVQBIds(testLogItemIds,
				VideoQBType.PingPong);
		List<TestLogItemGpsPoint> paramErrorPointsByQBRId = getPointsByVQBIds(paramErrorIds);
		if (null != paramErrorPointsByQBRId
				&& 0 != paramErrorPointsByQBRId.size()) {
			pingPongList.addAll(paramErrorPointsByQBRId);
		}
		// 计算重叠覆盖gps采样点
		List<Object> overCoverList = new LinkedList<>();
		overCoverList.add(0, VideoQBType.OverCover.toString());
		List<Long> overCoverIds = queryVQBIds(testLogItemIds,
				VideoQBType.OverCover);
		List<TestLogItemGpsPoint> overCoverPointsByQBRId = getPointsByVQBIds(overCoverIds);
		if (null != overCoverPointsByQBRId
				&& 0 != overCoverPointsByQBRId.size()) {
			overCoverList.addAll(overCoverPointsByQBRId);
		}
		// 计算其他问题gps采样点
		List<Object> otherList = new LinkedList<>();
		otherList.add(0, VideoQBType.Other.toString());
		List<Long> otherIds = queryVQBIds(testLogItemIds, VideoQBType.Other);
		List<TestLogItemGpsPoint> otherPointsByQBRId = getPointsByVQBIds(otherIds);
		if (null != otherPointsByQBRId && 0 != otherPointsByQBRId.size()) {
			otherList.addAll(otherPointsByQBRId);
		}
		// 计算下行调度小采样点
		List<Object> downDispatchSmallList = new LinkedList<>();
		downDispatchSmallList.add(0, VideoQBType.DownDispatchSmall.toString());
		List<Long> downDispatchSmallIds = queryVQBIds(testLogItemIds,
				VideoQBType.DownDispatchSmall);
		List<TestLogItemGpsPoint> downDispatchSmallPointsByQBRId = getPointsByVQBIds(downDispatchSmallIds);
		if (null != downDispatchSmallPointsByQBRId
				&& 0 != downDispatchSmallPointsByQBRId.size()) {
			downDispatchSmallList.addAll(downDispatchSmallPointsByQBRId);
		}
		lists.add(weakCoverList);
		lists.add(disturbList);
		lists.add(neighbourList);
		lists.add(pingPongList);
		lists.add(overCoverList);
		lists.add(otherList);
		lists.add(downDispatchSmallList);
		return lists;
	}

	/**
	 * 获取某些测试日志下的某种质差问题路段id集合
	 * 
	 * @param testLogItemIds
	 *            测试日志id按','分隔字符串
	 * @param qbrType
	 *            质差路段类型
	 * @return
	 */
	private List<Long> queryVQBIds(String testLogItemIds, VideoQBType qbrType) {
		List<Long> longs = new ArrayList<>();
		if (null != qbrType && StringUtils.hasText(testLogItemIds)) {
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
			if (VideoQBType.WeakCover.equals(qbrType)) {
				List<Long> queryStreamWeakCoverIdsByLogIds = streamWeakCoverDao
						.queryStreamWeakCoverIdsByLogIds(ids);
				if (null != queryStreamWeakCoverIdsByLogIds) {
					longs.addAll(queryStreamWeakCoverIdsByLogIds);
				}
			} else if (VideoQBType.Disturb.equals(qbrType)) {
				List<Long> queryStreamDisturbIdsByLogIds = streamDisturbDao
						.queryStreamDisturbIdsByLogIds(ids);
				if (null != queryStreamDisturbIdsByLogIds) {
					longs.addAll(queryStreamDisturbIdsByLogIds);
				}
			} else if (VideoQBType.Neighbour.equals(qbrType)) {
				List<Long> queryStreamNeighbourPlotIdsByLogIds = streamNeighbourPlotDao
						.queryStreamNeighbourPlotIdsByLogIds(ids);
				if (null != queryStreamNeighbourPlotIdsByLogIds) {
					longs.addAll(queryStreamNeighbourPlotIdsByLogIds);
				}
			} else if (VideoQBType.PingPong.equals(qbrType)) {
				List<Long> queryStreamPingPongIdsByLogIds = streamPingPongDao
						.queryStreamPingPongIdsByLogIds(ids);
				if (null != queryStreamPingPongIdsByLogIds) {
					longs.addAll(queryStreamPingPongIdsByLogIds);
				}
			} else if (VideoQBType.OverCover.equals(qbrType)) {
				List<Long> queryStreamOverCoverIdsByLogIds = streamOverCoverDao
						.queryStreamOverCoverIdsByLogIds(ids);
				if (null != queryStreamOverCoverIdsByLogIds) {
					longs.addAll(queryStreamOverCoverIdsByLogIds);
				}
			} else if (VideoQBType.Other.equals(qbrType)) {
				List<Long> queryStreamOtherIdsByLogIds = streamOtherDao
						.queryStreamOtherIdsByLogIds(ids);
				if (null != queryStreamOtherIdsByLogIds) {
					longs.addAll(queryStreamOtherIdsByLogIds);
				}
			} else if (VideoQBType.DownDispatchSmall.equals(qbrType)) {
				List<Long> queryStreamDownDispatchSmallIdsByLogIds = streamDownDispatchSmallDao
						.queryStreamDownDispatchSmallIdsByLogIds(ids);
				if (null != queryStreamDownDispatchSmallIdsByLogIds) {
					longs.addAll(queryStreamDownDispatchSmallIdsByLogIds);
				}
			}
		}
		return longs;
	}

	/**
	 * 根据质差集合获取轨迹
	 * 
	 * @param badRoadId
	 * @return
	 */
	public List<TestLogItemGpsPoint> getPointsByVQBIds(List<Long> badRoadIds) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadIds) {
			List<StreamQualityBad> find = streamQualityBadDao
					.queryStreamQualityBadsByBadLogIds(badRoadIds);
			if (null != find && 0 != find.size()) {
				for (StreamQualityBad videoQualityBad : find) {
					List<TestLogItemGpsPoint> testLogItemGpsIndexPointDirection = testLogItemGpsPointDao
							.getTestLogItemGpsIndexPointDirection(
									videoQualityBad.getTestLogItem()
											.getRecSeqNo(), null,
									videoQualityBad.getStartTime(),
									videoQualityBad.getEndTime());
					gpsPoints.addAll(testLogItemGpsIndexPointDirection);
				}
			}
		}
		return gpsPoints;

	}

	@Override
	public List<TestLogItemIndexGpsPoint> getLTEPointsByVQBId(Long badRoadId) {
		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId) {
			StreamQualityBad find = streamQualityBadDao.find(badRoadId);
			if (null != find) {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(18);
				list.add(19);
				list.add(20);
				gpsPoints = testLogItemGpsPointDetailDao
						.getTestLogItemGpsEventPoint(find.getTestLogItem()
								.getRecSeqNo(), list, find.getStartTime(), find
								.getEndTime());
			}
		}
		return gpsPoints;

	}

}
