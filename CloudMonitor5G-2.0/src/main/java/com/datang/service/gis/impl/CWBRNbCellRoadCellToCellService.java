package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.VoLTEDissertation.continueWirelessBadRoad.NbCellDeficiencyLTEAddAdviceCWBRDao;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.NbCellLteAddAdviceCWBR;
import com.datang.service.gis.ICWBRNbCellRoadCellToCelllService;

/**
 * @explain
 * @name CWBRNbCellRoadCellToCellService
 * @author shenyanwei
 * @date 2016年6月24日下午6:17:48
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CWBRNbCellRoadCellToCellService implements
		ICWBRNbCellRoadCellToCelllService {
	@Autowired
	private NbCellDeficiencyLTEAddAdviceCWBRDao nbCellDeficiencyLTEAddAdviceDao;

	@Override
	public List<Map<String, Object>> getCellToCellInfo(Long roadId) {

		List<Map<String, Object>> list = new ArrayList<>();
		List<NbCellLteAddAdviceCWBR> queryCellInfoByRoad = nbCellDeficiencyLTEAddAdviceDao
				.queryCellInfoByRoad(roadId);
		for (NbCellLteAddAdviceCWBR nbCellLteAddAdvice : queryCellInfoByRoad) {
			if (null == nbCellLteAddAdvice.getCellId()
					|| null == nbCellLteAddAdvice.getNb_cellId()) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cellId", nbCellLteAddAdvice.getCellId());
			map.put("nbCellId", nbCellLteAddAdvice.getNb_cellId());
			list.add(map);
		}
		return list;
	}

}
