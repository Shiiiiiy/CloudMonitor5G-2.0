package com.datang.service.service5g.qualityBad5g.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.dao.dao5g.qualityBad5g.Disturb5gDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;
import com.datang.domain.domain5g.qualityBad5g.InterfereRoadPojo;
import com.datang.service.service5g.qualityBad5g.QualityBadRoad5GService;

@Service
@Transactional
public class QualityBadRoad5GServiceImpl implements QualityBadRoad5GService{

	@Autowired
	private Disturb5gDao disturb5gDao;
	
	@Override
	public List<InterfereRoadPojo> getDisturbRoadsByLogIds(
			List<Long> testLogItemIds) {
		return disturb5gDao.queryDisturbRoadsByLogIds(testLogItemIds);
	}

	@Override
	public Map<String, EasyuiPageList> doDisturbAnalysis(Long roadId) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("disturbRoadCellInfo",doDisturbCellInfoAnalysis(roadId));
		map.put("disturbRoadSanChaoCellInfo",doDisturbSanChaoCellAnalysis(roadId));
		map.put("disturbRoadHighCoverCellInfo",doDisturbTiankuiHighCoverAnalysis(roadId));
		map.put("disturbRoadOverCoverCellInfo",doDisturbTiankuiOverCoverAnalysis(roadId));
		return map;
	}

	@Override
	public EasyuiPageList doDisturbSanChaoCellAnalysis(Long roadId) {
		if (roadId == null) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturb5gDao.doDisturbSanChaoCellAnalysis(roadId));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDisturbTiankuiHighCoverAnalysis(Long roadId) {
		if (roadId == null) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturb5gDao.doDisturbTiankuiHighCoverAnalysis(roadId));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDisturbCellInfoAnalysis(Long roadId) {
		if (roadId == null) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturb5gDao.queryCellInfoByRoad(roadId));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDisturbTiankuiOverCoverAnalysis(Long roadId) {
		if (roadId == null) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturb5gDao.doDisturbTiankuiOverCoverAnalysis(roadId));
		return easyuiPageList;
	}

	@Override
	public InterfereRoadPojo getVolteQualityBadRoad(Long roadId) {
		return disturb5gDao.find(roadId);
	}

	@Override
	public List<InterfereRoadPojo> getQualityBadRoadByLogIds(List<Long> ids) {
		List<InterfereRoadPojo> queryAllBadRoad = disturb5gDao.queryAllBadRoadOfTestLogIds(ids);
		return queryAllBadRoad;
	}

	@Override
	public EasyuiPageList getCellBeamInfoAnalysis(Long cellInfoId) {
			if (null == cellInfoId) {
				return new EasyuiPageList();
			}
			EasyuiPageList easyuiPageList = new EasyuiPageList();
			easyuiPageList.setRows(disturb5gDao.queryCellBeamInfoByCell(cellInfoId));
			return easyuiPageList;
	}
}
