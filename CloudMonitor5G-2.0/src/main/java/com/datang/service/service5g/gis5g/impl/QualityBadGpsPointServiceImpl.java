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
import com.datang.domain.domain5g.qualityBad5g.InterfereRoadPojo;
import com.datang.service.service5g.embbCover.IEmbbCoverBadRoadService;
import com.datang.service.service5g.gis5g.QualityBadGpsPointService;
import com.datang.service.service5g.qualityBad5g.QualityBadRoad5GService;
import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;
@Service
@Transactional
@SuppressWarnings("all")
public class QualityBadGpsPointServiceImpl implements QualityBadGpsPointService{

	@Autowired
	private TestLogItemGpsPointDao testLogItemGpsPointDao;
	@Autowired
	private TestLogItemGpsPointDetailDao testLogItemGpsPointDetailDao;
	@Autowired
	private QualityBadRoad5GService qualityBadRoad5GService;
	
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
			List<InterfereRoadPojo> embbCoverBadRoadByLogIds = qualityBadRoad5GService.getQualityBadRoadByLogIds(ids);
			for (InterfereRoadPojo ifp : embbCoverBadRoadByLogIds) {
				returnList.addAll(testLogItemGpsPointDetailDao
						.getTestLogItemGpsIndexPoint5g(ifp
								.getTestLogItem().getRecSeqNo(), indexType,
								ifp.getStartTime(),
								ifp.getStartTime() + ifp.getTestDuration()));
			}
		}
		return returnList;
	}
}
