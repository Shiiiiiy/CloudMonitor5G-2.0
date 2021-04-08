/**
 * 
 */
package com.datang.service.gis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.VoLTEDissertation.qualityBadRoad.NbCellDeficiencyLTEAddAdviceDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.NbCellLteAddAdvice;
import com.datang.service.gis.INbCellRoadCellToCelllService;

/**
 * 邻区缺失路段小区与邻区连线service接口实现
 * 
 * @author yinzhipeng
 * @date:2015年12月3日 下午2:16:12
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class NbCellRoadCellToCellService implements
		INbCellRoadCellToCelllService {
	@Autowired
	private NbCellDeficiencyLTEAddAdviceDao nbCellDeficiencyLTEAddAdviceDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.gis.INbCellRoadCellToCelllService#getCellToCellInfo
	 * (java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getCellToCellInfo(Long roadId) {
		List<Map<String, Object>> list = new ArrayList<>();
		List<NbCellLteAddAdvice> queryCellInfoByRoad = nbCellDeficiencyLTEAddAdviceDao
				.queryCellInfoByRoad(roadId);
		for (NbCellLteAddAdvice nbCellLteAddAdvice : queryCellInfoByRoad) {
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
