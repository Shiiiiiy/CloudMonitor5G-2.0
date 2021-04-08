package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.constant.QBRType;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.VolteCallEstablishDelayExceptionDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.callLocationUpdate.VolteCallEstablishDelayExceptionCallLocationUpdateDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.coreNet.VolteCallEstablishDelayExceptionCoreNetworkDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.other.VolteCallEstablishDelayExceptionOtherProblemDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCoverDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCoverDao;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.gis.ICEDEGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * @explain 呼叫建立时延异常Gps Service实现
 * @name CEDEGpsPointServiceImpl
 * @author shenyanwei
 * @date 2016年6月27日上午9:01:24
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CEDEGpsPointServiceImpl implements ICEDEGpsPointService {
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private VolteCallEstablishDelayExceptionDao volteCallEstablishDelayExceptionDao;
	@Autowired
	private VolteCallEstablishDelayExceptionCallLocationUpdateDao locationUpdateDao;
	@Autowired
	private VolteCallEstablishDelayExceptionCoreNetworkDao coreNetworkDao;
	@Autowired
	private VolteCallEstablishDelayExceptionOtherProblemDao otherProblemDao;
	@Autowired
	private VolteCallEstablishDelayExceptionOverlapCoverDao overlapCoverDao;
	@Autowired
	private VolteCallEstablishDelayExceptionWeakCoverDao weakCoverDao;

	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByCedeIdAndIndexType(
			Long cedeId, Integer indexType) {
		List<TestLogItemIndexGpsPoint> pointsByEventGpsPoints = new ArrayList<>();
		if (null != cedeId || null != indexType) {
			VolteCallEstablishDelayException find = volteCallEstablishDelayExceptionDao
					.find(cedeId);
			if (null != find) {
				pointsByEventGpsPoints = testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint(find.getTestLogItem()
								.getRecSeqNo(), indexType, find.getStartTime(),
								find.getEndTime());
			}
		}

		return pointsByEventGpsPoints;
	}

	@Override
	public List<TestLogItemGpsPoint> getPointDirectionsByCedeIdAndIndexType(
			Long droppingId, Integer indexType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != droppingId || null != indexType) {
			VolteCallEstablishDelayException find = volteCallEstablishDelayExceptionDao
					.find(droppingId);
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
	public List<TestLogItemGpsPoint> getPointsByCedeId(Long cedeId) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != cedeId) {
			VolteCallEstablishDelayException find = volteCallEstablishDelayExceptionDao
					.find(cedeId);
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
	public List<TestLogItemIndexGpsPoint> getEventPointsByTestlogIdsAndIconType(
			String testLogItemIds, Integer iconType) {
		if (StringUtils.hasText(testLogItemIds)) {
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
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			arrayList.add(iconType);
			List<VolteCallEstablishDelayException> queryVolteCallEstablishDelayExceptionsByLogIds = volteCallEstablishDelayExceptionDao
					.queryVolteCallEstablishDelayExceptionsByLogIds(ids);
			List<TestLogItemIndexGpsPoint> eventEventPoints = new ArrayList<>();
			for (VolteCallEstablishDelayException volteCallEstablishDelayException : queryVolteCallEstablishDelayExceptionsByLogIds) {
				List<TestLogItemIndexGpsPoint> testLogItemGpsIndexPoint = testLogItemGpsPointDetailDao
						.getTestLogItemGpsEventPoint(
								volteCallEstablishDelayException
										.getTestLogItem().getRecSeqNo(),
								arrayList, volteCallEstablishDelayException
										.getStartTime(),
								volteCallEstablishDelayException.getEndTime());
				if (testLogItemGpsIndexPoint != null
						&& testLogItemGpsIndexPoint.size() != 0) {
					eventEventPoints.addAll(testLogItemGpsIndexPoint);
				}
			}

			return eventEventPoints;
		}
		return null;
	}

	@Override
	public List<TestLogItemIndexGpsPoint> getEventPointsByCedeIdAndIndexType(
			Long cedeId) {
		List<TestLogItemIndexGpsPoint> eventEventPoints = new ArrayList<>();
		if (cedeId != null) {
			VolteCallEstablishDelayException find = volteCallEstablishDelayExceptionDao
					.find(cedeId);
			eventEventPoints = testLogItemGpsPointDetailDao
					.getTestLogItemGpsEventPoint(find.getTestLogItem()
							.getRecSeqNo(), null, find.getStartTime(), find
							.getEndTime());
		}
		return eventEventPoints;
	}

	@Override
	public List<List<Object>> getTheEveryGpsPointsByTestlogIdsAndCedeType(
			String testLogItemIds) {
		List<List<Object>> lists = new ArrayList<>();
		// 计算弱覆盖gps采样点
		List<Object> weakCoverList = new LinkedList<>();
		weakCoverList.add(0, QBRType.WeakCover.toString());
		List<Long> weakCoverIds = queryCedeIds(testLogItemIds,
				QBRType.WeakCover);
		for (Long weakCoverId : weakCoverIds) {
			List<TestLogItemGpsPoint> pointsByCEDEId = getPointsByCedeId(weakCoverId);
			if (null != pointsByCEDEId && 0 != pointsByCEDEId.size()) {
				weakCoverList.add(pointsByCEDEId);
			}
		}
		// 计算重叠覆盖gps采样点
		List<Object> overlapCoverList = new LinkedList<>();
		overlapCoverList.add(0, QBRType.OverlapCover.toString());
		List<Long> overlapCoverIds = queryCedeIds(testLogItemIds,
				QBRType.Disturb);
		for (Long overlapCoverId : overlapCoverIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCedeId(overlapCoverId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				overlapCoverList.add(pointsByQBRId);
			}
		}
		// 计算被叫位置更新gps采样点
		List<Object> locationUpdateList = new LinkedList<>();
		locationUpdateList.add(0, QBRType.LocationUpdate.toString());
		List<Long> nbCellIds = queryCedeIds(testLogItemIds, QBRType.NbCell);
		for (Long nbCellId : nbCellIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCedeId(nbCellId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				locationUpdateList.add(pointsByQBRId);
			}
		}

		// 计算核心网gps采样点
		List<Object> coreNetworkList = new LinkedList<>();
		coreNetworkList.add(0, QBRType.CoreNetwork.toString());
		List<Long> coreNetworkIds = queryCedeIds(testLogItemIds,
				QBRType.CoreNetwork);
		for (Long coreNetworkId : coreNetworkIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCedeId(coreNetworkId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				coreNetworkList.add(pointsByQBRId);
			}
		}
		// 计算其他问题gps采样点
		List<Object> otherList = new LinkedList<>();
		otherList.add(0, QBRType.Other.toString());
		List<Long> otherIds = queryCedeIds(testLogItemIds, QBRType.Other);
		for (Long otherId : otherIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCedeId(otherId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				otherList.add(pointsByQBRId);
			}
		}
		if (weakCoverList.size() != 0) {
			lists.add(weakCoverList);
		}
		if (overlapCoverList.size() != 0) {
			lists.add(overlapCoverList);
		}
		if (locationUpdateList.size() != 0) {
			lists.add(locationUpdateList);
		}
		if (coreNetworkList.size() != 0) {
			lists.add(coreNetworkList);
		}
		if (otherList.size() != 0) {
			lists.add(otherList);
		}

		return lists;
	}

	/**
	 * 获取某些测试日志下的某种id集合
	 * 
	 * @param testLogItemIds
	 *            测试日志id按','分隔字符串
	 * @param droppingType
	 *            切换失败类型
	 * @return
	 */
	private List<Long> queryCedeIds(String testLogItemIds, QBRType cedeType) {
		List<Long> longs = new ArrayList<>();
		if (null != cedeType && StringUtils.hasText(testLogItemIds)) {
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
			if (QBRType.WeakCover.equals(cedeType)) {
				List<Long> queryWeakCoverRoadIdsByLogIds = weakCoverDao
						.queryWeakCoverRoadIdsByLogIds(ids);
				if (null != queryWeakCoverRoadIdsByLogIds) {
					longs.addAll(queryWeakCoverRoadIdsByLogIds);
				}
			} else if (QBRType.OverlapCover.equals(cedeType)) {
				List<Long> queryDisturbRoadIdsByLogIds = overlapCoverDao
						.queryOverlapOverRoadIdsByLogIds(ids);
				if (null != queryDisturbRoadIdsByLogIds) {
					longs.addAll(queryDisturbRoadIdsByLogIds);
				}
			} else if (QBRType.LocationUpdate.equals(cedeType)) {
				List<Long> queryNbDeficiencyRoadIdsByLogIds = locationUpdateDao
						.queryLocationUpdateRoadIdsByLogIds(ids);
				if (null != queryNbDeficiencyRoadIdsByLogIds) {
					longs.addAll(queryNbDeficiencyRoadIdsByLogIds);
				}
			} else if (QBRType.CoreNetwork.equals(cedeType)) {
				List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetworkDao
						.queryCoreNetworkRoadIdsByLogIds(ids);
				if (null != queryCoreNetworkRoadIdsByLogIds) {
					longs.addAll(queryCoreNetworkRoadIdsByLogIds);
				}
			} else if (QBRType.Other.equals(cedeType)) {
				List<Long> queryOtherRoadIdsByLogIds = otherProblemDao
						.queryOtherRoadIdsByLogIds(ids);
				if (null != queryOtherRoadIdsByLogIds) {
					longs.addAll(queryOtherRoadIdsByLogIds);
				}
			}
		}
		return longs;
	}
}
