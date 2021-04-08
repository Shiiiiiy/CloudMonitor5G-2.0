/**
 * 
 */
package com.datang.service.service5g.gis5g.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.util.StringUtils;
import com.datang.dao.gis.TestLogItemGpsPointDao;
import com.datang.dao.gis.TestLogItemGpsPointDetailDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.gis.TestLogItemGpsPoint;
import com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService;
import com.datang.service.service5g.gis5g.IEmbbCoverGpsPointService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

/**
 * EMBB覆盖路段轨迹点service接口
 * 
 * @author _YZP
 * 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class EmbbCoverGpsPointService implements IEmbbCoverGpsPointService {
	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private IEmbbCoverBadRoadService embbCoverBadRoadService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.gis5g.IEmbbCoverGpsPointService#
	 * getPointsByEmbbIdAndIndexType(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByEmbbIdAndIndexType(
			Long badRoadId, Integer indexType) {
		List<TestLogItemIndexGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			EmbbCoverBadRoad embbCoverBadRoad = embbCoverBadRoadService
					.getEmbbCoverBadRoad(badRoadId);
			if (null != embbCoverBadRoad) {
				gpsPoints = testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint5g(embbCoverBadRoad
								.getTestLogItem().getRecSeqNo(), indexType,
								embbCoverBadRoad.getStartTime(),
								embbCoverBadRoad.getEndTime());
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.gis5g.IEmbbCoverGpsPointService#
	 * getPointDirectionsByEmbbIdAndIndexType(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemGpsPoint> getPointDirectionsByEmbbIdAndIndexType(
			Long badRoadId, Integer indexType) {
		List<TestLogItemGpsPoint> gpsPoints = new ArrayList<>();
		if (null != badRoadId || null != indexType) {
			EmbbCoverBadRoad embbCoverBadRoad = embbCoverBadRoadService
					.getEmbbCoverBadRoad(badRoadId);
			if (null != embbCoverBadRoad) {
				gpsPoints = testLogItemGpsPointDao
						.getTestLogItemGpsIndexPointDirection(embbCoverBadRoad
								.getTestLogItem().getRecSeqNo(), indexType,
								embbCoverBadRoad.getStartTime(),
								embbCoverBadRoad.getEndTime());
			}
		}
		return gpsPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.service5g.gis5g.IEmbbCoverGpsPointService#
	 * getPointsByTestLogItem(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<TestLogItemIndexGpsPoint> getPointsByTestLogItem(
			String testLogItemIds, Integer coverType, Integer indexType) {
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
			List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds = embbCoverBadRoadService
					.getEmbbCoverBadRoadByLogIds(ids, coverType);
			for (EmbbCoverBadRoad embbCoverBadRoad : embbCoverBadRoadByLogIds) {
				returnList.addAll(testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint5g(embbCoverBadRoad
								.getTestLogItem().getRecSeqNo(), indexType,
								embbCoverBadRoad.getStartTime(),
								embbCoverBadRoad.getEndTime()));
			}
		}
		return returnList;
	}
	
	@Override
	public List<TestLogItemIndexGpsPoint> getPointsExceptionEtgTral(List<String> logNameList){
		return testLogItemGpsPointDetailDao.getPointsExceptionEtgTral(logNameList);
	}

}
