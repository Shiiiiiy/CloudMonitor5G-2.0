package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.constant.QBRType;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.DisturbCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.NbCellDeficiencyCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.OtherCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoadCWBRDao;
import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.WeakCoverCWBRDao;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.gis.ICWBRGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 连续无线差路段轨迹点实现
 * 
 * @explain
 * @name CWBRGpsPointServiceImpl
 * @author shenyanwei
 * @date 2016年6月24日下午5:23:49
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CWBRGpsPointServiceImpl implements ICWBRGpsPointService {
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private WeakCoverCWBRDao weakCoverDao;
	@Autowired
	private DisturbCWBRDao disturbDao;
	@Autowired
	private NbCellDeficiencyCWBRDao nbCellDeficiencyDao;
	@Autowired
	private OtherCWBRDao otherDao;
	@Autowired
	private VolteContinueWirelessBadRoadCWBRDao cwbrDao;

	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByCWBRIdAndIndexType(
			Long badRoadId, Integer indexType) {
		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			VolteContinueWirelessBadRoad find = cwbrDao.find(badRoadId);
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
	public List<TestLogItemGpsPoint> getPointDirectionsByCWBRIdAndIndexType(
			Long badRoadId, Integer indexType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			VolteContinueWirelessBadRoad find = cwbrDao.find(badRoadId);
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
	public List<TestLogItemGpsPoint> getPointsByCWBRId(Long badRoadId) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId) {
			VolteContinueWirelessBadRoad find = cwbrDao.find(badRoadId);

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
	public List<List<Object>> getEveryCWBRGpsPointsByTestlogIds(
			String testLogItemIds) {
		List<List<Object>> lists = new ArrayList<>();
		// 计算弱覆盖gps采样点
		List<Object> weakCoverList = new LinkedList<>();
		weakCoverList.add(0, QBRType.WeakCover.toString());
		List<Long> weakCoverIds = queryCWBRIds(testLogItemIds,
				QBRType.WeakCover);
		for (Long weakCoverId : weakCoverIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCWBRId(weakCoverId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				weakCoverList.add(pointsByQBRId);
			}
		}
		// 计算干扰gps采样点
		List<Object> disturbList = new LinkedList<>();
		disturbList.add(0, QBRType.Disturb.toString());
		List<Long> disturbIds = queryCWBRIds(testLogItemIds, QBRType.Disturb);
		for (Long disturbId : disturbIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCWBRId(disturbId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				disturbList.add(pointsByQBRId);
			}
		}
		// 计算邻区配置gps采样点
		List<Object> nbCellList = new LinkedList<>();
		nbCellList.add(0, QBRType.NbCell.toString());
		List<Long> nbCellIds = queryCWBRIds(testLogItemIds, QBRType.NbCell);
		for (Long nbCellId : nbCellIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCWBRId(nbCellId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				nbCellList.add(pointsByQBRId);
			}
		}

		// 计算其他问题gps采样点
		List<Object> otherList = new LinkedList<>();
		otherList.add(0, QBRType.Other.toString());
		List<Long> otherIds = queryCWBRIds(testLogItemIds, QBRType.Other);
		for (Long otherId : otherIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByCWBRId(otherId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				otherList.add(pointsByQBRId);
			}
		}
		lists.add(weakCoverList);
		lists.add(disturbList);
		lists.add(nbCellList);
		lists.add(otherList);
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
	private List<Long> queryCWBRIds(String testLogItemIds, QBRType qbrType) {
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
			if (QBRType.WeakCover.equals(qbrType)) {
				List<Long> queryWeakCoverRoadIdsByLogIds = weakCoverDao
						.queryWeakCoverRoadIdsByLogIds(ids);
				if (null != queryWeakCoverRoadIdsByLogIds) {
					longs.addAll(queryWeakCoverRoadIdsByLogIds);
				}
			} else if (QBRType.Disturb.equals(qbrType)) {
				List<Long> queryDisturbRoadIdsByLogIds = disturbDao
						.queryDisturbRoadIdsByLogIds(ids);
				if (null != queryDisturbRoadIdsByLogIds) {
					longs.addAll(queryDisturbRoadIdsByLogIds);
				}
			} else if (QBRType.NbCell.equals(qbrType)) {
				List<Long> queryNbDeficiencyRoadIdsByLogIds = nbCellDeficiencyDao
						.queryNbDeficiencyRoadIdsByLogIds(ids);
				if (null != queryNbDeficiencyRoadIdsByLogIds) {
					longs.addAll(queryNbDeficiencyRoadIdsByLogIds);
				}
			} else if (QBRType.Other.equals(qbrType)) {
				List<Long> queryOtherRoadIdsByLogIds = otherDao
						.queryOtherRoadIdsByLogIds(ids);
				if (null != queryOtherRoadIdsByLogIds) {
					longs.addAll(queryOtherRoadIdsByLogIds);
				}
			}
		}
		return longs;
	}

}
