package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.constant.DroppingType;
import com.datang.dao.VoLTEDissertation.handoffDropping.VolteDroppingDao;
import com.datang.dao.VoLTEDissertation.handoffDropping.VolteDroppingIntDao;
import com.datang.dao.VoLTEDissertation.handoffDropping.VolteDroppingSRVCCDao;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDropping;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.gis.IDroppingGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 切换失败轨迹Service实现
 * 
 * @author shenyanwei
 * @date 2016年5月5日上午10:52:52
 */
@Service
@Transactional
@SuppressWarnings("all")
public class DroppingGpsPointImpl implements IDroppingGpsPointService {
	@Autowired
	private VolteDroppingIntDao intDao;
	@Autowired
	private VolteDroppingSRVCCDao SRVCCDao;
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private VolteDroppingDao droppingDao;

	// 切换失败类型(0SRVCC切换失败,1系统内切换失败)
	@Override
	public List<TestLogItemIndexGpsPoint> getEventPointsByTestlogIdsAndIconType(
			String testLogItemIds, Integer iconType, Integer deType) {
		String droppingType = null;
		if (deType == 1) {
			droppingType = "DroppingInt";
		}
		if (deType == 0) {
			droppingType = "DroppingSRVCC";
		}
		List<VolteDropping> queryDEs = queryDroppings(testLogItemIds,
				droppingType);
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(iconType);
		List<TestLogItemIndexGpsPoint> eventEventPoints = new ArrayList<>();
		if (queryDEs != null && queryDEs.size() != 0) {
			for (VolteDropping volteDropping : queryDEs) {
				List<TestLogItemIndexGpsPoint> testLogItemGpsIndexPoint = testLogItemGpsPointDetailDao
						.getTestLogItemGpsEventPoint(volteDropping
								.getTestLogItem().getRecSeqNo(), arrayList,
								volteDropping.getStartTime(), volteDropping
										.getEndTime());
				if (testLogItemGpsIndexPoint != null
						&& testLogItemGpsIndexPoint.size() != 0) {
					eventEventPoints.addAll(testLogItemGpsIndexPoint);
				}
			}
		}
		return eventEventPoints;

	}

	@Override
	public List<TestLogItemIndexGpsPoint> getEventPointsByHopIdAndIndexType(
			Long DEId) {
		List<TestLogItemIndexGpsPoint> eventEventPoints = new ArrayList<>();
		if (DEId != null) {
			VolteDropping find = droppingDao.find(DEId);
			eventEventPoints = testLogItemGpsPointDetailDao
					.getTestLogItemGpsEventPoint(find.getTestLogItem()
							.getRecSeqNo(), null, find.getStartTime(), find
							.getEndTime());
		}
		return eventEventPoints;
	}

	/**
	 * 获取某些测试日志下的某种切换失败id集合
	 * 
	 * @param testLogItemIds
	 *            测试日志id按','分隔字符串
	 * @param droppingType
	 *            切换失败类型
	 * @return
	 */
	private List<Long> queryDroppingIds(String testLogItemIds,
			String droppingType) {
		List<Long> longs = new ArrayList<>();
		if (null != droppingType && StringUtils.hasText(testLogItemIds)) {
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
			if (DroppingType.DroppingInt.toString().equals(droppingType)) {
				List<Long> queryDroppingIntIdsByLogIds = intDao
						.queryVolteDroppingIntIdsByLogIds(ids);
				if (null != queryDroppingIntIdsByLogIds) {
					longs.addAll(queryDroppingIntIdsByLogIds);
				}
			} else if (DroppingType.DroppingSRVCC.toString().equals(
					droppingType)) {
				List<Long> queryDroppingSRVCCIdsByLogIds = SRVCCDao
						.queryVolteDroppingSRVCCIdsByLogIds(ids);
				if (null != queryDroppingSRVCCIdsByLogIds) {
					longs.addAll(queryDroppingSRVCCIdsByLogIds);
				}
			}
		}
		return longs;
	}

	/**
	 * 获取某些测试日志下的某种切换失败集合
	 * 
	 * @param testLogItemIds
	 *            测试日志id按','分隔字符串
	 * @param droppingType
	 *            切换失败类型
	 * @return
	 */
	private List<VolteDropping> queryDroppings(String testLogItemIds,
			String droppingType) {
		List<VolteDropping> longs = new ArrayList<>();
		if (null != droppingType && StringUtils.hasText(testLogItemIds)) {
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
			if (DroppingType.DroppingInt.toString().equals(droppingType)) {
				List<VolteDroppingInt> queryVolteDroppingIntsByLogIds = intDao
						.queryVolteDroppingIntsByLogIds(ids);
				if (null != queryVolteDroppingIntsByLogIds) {
					longs.addAll(queryVolteDroppingIntsByLogIds);
				}
			} else if (DroppingType.DroppingSRVCC.toString().equals(
					droppingType)) {
				List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIds = SRVCCDao
						.queryVolteDroppingSRVCCsByLogIds(ids);
				if (null != queryVolteDroppingSRVCCsByLogIds) {
					longs.addAll(queryVolteDroppingSRVCCsByLogIds);
				}
			}
		}
		return longs;
	}

	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByvolteDroppingIdAndIndexType(
			Long volteDroppingId, Integer indexType) {
		List<TestLogItemIndexGpsPoint> pointsByEventGpsPoints = new ArrayList<>();
		if (null != volteDroppingId || null != indexType) {
			VolteDropping find = droppingDao.find(volteDroppingId);
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
	public List<TestLogItemGpsPoint> getPointDirectionsByDroppingIdAndIndexType(
			Long droppingId, Integer indexType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != droppingId || null != indexType) {
			VolteDropping find = droppingDao.find(droppingId);
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
	public List<TestLogItemGpsPoint> getPointsByvolteDroppingId(
			Long volteDroppingId) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != volteDroppingId) {
			VolteDropping find = droppingDao.find(volteDroppingId);
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
	public List<List<TestLogItemGpsPoint>> getTheEveryDroppingGpsPointsByTestlogIdsAndHofType(
			String testLogItemIds, Integer hofType) {
		List<List<TestLogItemGpsPoint>> lists = new ArrayList<>();
		// 添加所有hoftype的事件轨迹数据
		String droppingType = null;
		if (hofType == 1) {
			droppingType = "DroppingInt";
		}
		if (hofType == 0) {
			droppingType = "DroppingSRVCC";
		}
		List<Long> queryDEIds = queryDroppingIds(testLogItemIds, droppingType);
		for (Long hofId : queryDEIds) {
			List<TestLogItemGpsPoint> pointsByDroppingId = getPointsByvolteDroppingId(hofId);
			if (null != pointsByDroppingId && 0 != pointsByDroppingId.size()) {
				lists.add(pointsByDroppingId);
			}
		}
		return lists;
	}

}
