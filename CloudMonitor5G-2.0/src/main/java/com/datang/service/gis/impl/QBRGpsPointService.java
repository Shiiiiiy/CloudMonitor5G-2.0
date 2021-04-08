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
import com.datang.constant.QBRType;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.CoreNetworkDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.DisturbDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.NbCellDeficiencyDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.OtherDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.ParamErrorDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.QualityBadRoadDao;
import com.datang.dao.VoLTEDissertation.qualityBadRoad.WeakCoverDao;
import com.datang.dao.dao5g.qualityBad5g.Disturb5gDao;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.domain5g.qualityBad5g.InterfereRoadPojo;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.gis.IQBRGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * 质差路段轨迹点service接口实现
 * 
 * @author yinzhipeng
 * @date:2015年12月2日 上午10:04:04
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class QBRGpsPointService implements IQBRGpsPointService {
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private WeakCoverDao weakCoverDao;
	@Autowired
	private DisturbDao disturbDao;
	@Autowired
	private NbCellDeficiencyDao nbCellDeficiencyDao;
	@Autowired
	private ParamErrorDao paramErrorDao;
	@Autowired
	private CoreNetworkDao coreNetworkDao;
	@Autowired
	private OtherDao otherDao;
	@Autowired
	private QualityBadRoadDao qualityBadRoadDao;
	@Autowired
	private Disturb5gDao disturb5gDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IQBRGpsPointService#getPointsByQBRIdAndIndexType
	 * (java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByQBRIdAndIndexType(
			Long badRoadId, Integer indexType) {
		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			VolteQualityBadRoad find = qualityBadRoadDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint(find.getTestLogItem()
								.getRecSeqNo(), indexType, find.getStartTime(),
								find.getEndTime());
			}
		}
		return gpsPoints;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IQBRGpsPointService#getPointsByQBRIdAndIndexType
	 * (java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByQBRIdAndIndexType5g(
			Long badRoadId, Integer indexType) {
		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			InterfereRoadPojo find = disturb5gDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDetailDao.getTestLogItemGpsIndexPoint(
						find.getTestLogItem().getRecSeqNo(), 
						indexType, 
						find.getStartTime(),
						find.getStartTime() + find.getTestDuration());
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.gis.IQBRGpsPointService#
	 * getPointDirectionsByQBRIdAndIndexType(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemGpsPoint> getPointDirectionsByQBRIdAndIndexType(
			Long badRoadId, Integer indexType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			VolteQualityBadRoad find = qualityBadRoadDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDao
						.getTestLogItemGpsIndexPointDirection(find
								.getTestLogItem().getRecSeqNo(), indexType,
								find.getStartTime(), find.getEndTime());
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.gis.IQBRGpsPointService#
	 * getPointDirectionsByQBRIdAndIndexType(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemGpsPoint> getPointDirectionsByQBRIdAndIndexType5g(
			Long badRoadId, Integer indexType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			InterfereRoadPojo find = disturb5gDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDao.getTestLogItemGpsIndexPointDirection(
								find.getTestLogItem().getRecSeqNo(), 
								indexType,
								find.getStartTime(),
								null);
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IQBRGpsPointService#getPointsByQBRId(java.lang
	 * .Long)
	 */
	@Override
	public List<TestLogItemGpsPoint> getPointsByQBRId(Long badRoadId) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId) {
			VolteQualityBadRoad find = qualityBadRoadDao.find(badRoadId);
			if (null != find) {
				gpsPoints = testLogItemGpsPointDao
						.getTestLogItemGpsIndexPointDirection(find
								.getTestLogItem().getRecSeqNo(), null, find
								.getStartTime(), find.getEndTime());
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.IQBRGpsPointService#getEveryQBRGpsPointsByTestlogIds
	 * (java.lang.String)
	 */
	@Override
	public List<List<Object>> getEveryQBRGpsPointsByTestlogIds(
			String testLogItemIds) {
		List<List<Object>> lists = new ArrayList<>();
		// 计算弱覆盖gps采样点
		List<Object> weakCoverList = new LinkedList<>();
		weakCoverList.add(0, QBRType.WeakCover.toString());
		List<Long> weakCoverIds = queryQBRIds(testLogItemIds, QBRType.WeakCover);
		for (Long weakCoverId : weakCoverIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByQBRId(weakCoverId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				weakCoverList.add(pointsByQBRId);
			}
		}
		// 计算干扰gps采样点
		List<Object> disturbList = new LinkedList<>();
		disturbList.add(0, QBRType.Disturb.toString());
		List<Long> disturbIds = queryQBRIds(testLogItemIds, QBRType.Disturb);
		for (Long disturbId : disturbIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByQBRId(disturbId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				disturbList.add(pointsByQBRId);
			}
		}
		// 计算邻区配置gps采样点
		List<Object> nbCellList = new LinkedList<>();
		nbCellList.add(0, QBRType.NbCell.toString());
		List<Long> nbCellIds = queryQBRIds(testLogItemIds, QBRType.NbCell);
		for (Long nbCellId : nbCellIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByQBRId(nbCellId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				nbCellList.add(pointsByQBRId);
			}
		}
		// 计算参数错误gps采样点
		List<Object> paramErrorList = new LinkedList<>();
		paramErrorList.add(0, QBRType.ParamError.toString());
		List<Long> paramErrorIds = queryQBRIds(testLogItemIds,
				QBRType.ParamError);
		for (Long paramErrorId : paramErrorIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByQBRId(paramErrorId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				paramErrorList.add(pointsByQBRId);
			}
		}
		// 计算核心网gps采样点
		List<Object> coreNetworkList = new LinkedList<>();
		coreNetworkList.add(0, QBRType.CoreNetwork.toString());
		List<Long> coreNetworkIds = queryQBRIds(testLogItemIds,
				QBRType.CoreNetwork);
		for (Long coreNetworkId : coreNetworkIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByQBRId(coreNetworkId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				coreNetworkList.add(pointsByQBRId);
			}
		}
		// 计算其他问题gps采样点
		List<Object> otherList = new LinkedList<>();
		otherList.add(0, QBRType.Other.toString());
		List<Long> otherIds = queryQBRIds(testLogItemIds, QBRType.Other);
		for (Long otherId : otherIds) {
			List<TestLogItemGpsPoint> pointsByQBRId = getPointsByQBRId(otherId);
			if (null != pointsByQBRId && 0 != pointsByQBRId.size()) {
				otherList.add(pointsByQBRId);
			}
		}
		lists.add(weakCoverList);
		lists.add(disturbList);
		lists.add(nbCellList);
		lists.add(paramErrorList);
		lists.add(coreNetworkList);
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
	private List<Long> queryQBRIds(String testLogItemIds, QBRType qbrType) {
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
			} else if (QBRType.ParamError.equals(qbrType)) {
				List<Long> queryParamErrorRoadIdsByLogIds = paramErrorDao
						.queryParamErrorRoadIdsByLogIds(ids);
				if (null != queryParamErrorRoadIdsByLogIds) {
					longs.addAll(queryParamErrorRoadIdsByLogIds);
				}
			} else if (QBRType.CoreNetwork.equals(qbrType)) {
				List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetworkDao
						.queryCoreNetworkRoadIdsByLogIds(ids);
				if (null != queryCoreNetworkRoadIdsByLogIds) {
					longs.addAll(queryCoreNetworkRoadIdsByLogIds);
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

	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByTestLogItem(String testLogItemIds, Integer indexType) {
		List<TestLogItemIndexGpsPoint> returnList = new ArrayList<>();
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
			List<InterfereRoadPojo> embbCoverBadRoadByLogIds = disturb5gDao.getEmbbCoverBadRoadByLogIds(ids);
			for (InterfereRoadPojo embbCoverBadRoad : embbCoverBadRoadByLogIds) {
				returnList.addAll(testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint5g(embbCoverBadRoad
								.getTestLogItem().getRecSeqNo(), indexType,
								embbCoverBadRoad.getStartTime(),
								embbCoverBadRoad.getStartTime() + embbCoverBadRoad.getTestDuration()));
			}
		}
		return returnList;
	}

}
