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

import com.datang.dao.VoLTEDissertation.videoQualityBad.NeighbourPlotAddNeighbourPlotDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.NeighbourPlotAddNeighbourPlot;
import com.datang.service.gis.IVQBNPlotCellToCelllService;

/**
 * 视频质差邻区缺失路段小区与邻区连线service接口实现
 * 
 * @explain
 * @name VQBNPlotCellToCellService
 * @author shenyanwei
 * @date 2017年5月23日下午2:54:38
 */
@Service
@Transactional
@SuppressWarnings("all")
public class VQBNPlotCellToCellService implements IVQBNPlotCellToCelllService {
	@Autowired
	private NeighbourPlotAddNeighbourPlotDao neighbourPlotAddNeighbourPlotDao;

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
		List<NeighbourPlotAddNeighbourPlot> queryCellInfoByRoad = neighbourPlotAddNeighbourPlotDao
				.queryCellInfoByRoad(roadId);
		for (NeighbourPlotAddNeighbourPlot neighbourPlotAddNeighbourPlot : queryCellInfoByRoad) {
			if (null == neighbourPlotAddNeighbourPlot
					.getVideoQualityBadPingPong().getCellId()
					|| null == neighbourPlotAddNeighbourPlot.getCellId()) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cellId", neighbourPlotAddNeighbourPlot
					.getVideoQualityBadPingPong().getCellId());
			map.put("nbCellId", neighbourPlotAddNeighbourPlot.getCellId());
			list.add(map);
		}
		return list;
	}
}
