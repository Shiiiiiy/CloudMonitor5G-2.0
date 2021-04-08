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

import com.datang.dao.stream.streamVideoQualitybad.StreamNeighbourAdviceDao;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamNeighbourAdvice;
import com.datang.service.gis.ISVQBNPlotCellToCelllService;

/**
 * 流媒体视频质差邻区缺失路段小区与邻区连线service接口实现
 * 
 * @explain
 * @name SVQBNPlotCellToCellService
 * @author shenyanwei
 * @date 2017年11月28日上午9:35:58
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SVQBNPlotCellToCellService implements ISVQBNPlotCellToCelllService {
	@Autowired
	private StreamNeighbourAdviceDao streamNeighbourAdviceDao;

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
		List<StreamNeighbourAdvice> queryCellInfoByRoad = streamNeighbourAdviceDao
				.queryCellInfoByRoad(roadId);
		for (StreamNeighbourAdvice neighbourPlotAddNeighbourPlot : queryCellInfoByRoad) {
			if (null == neighbourPlotAddNeighbourPlot.getCellId()
					|| null == neighbourPlotAddNeighbourPlot.getNcellId()) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cellId", neighbourPlotAddNeighbourPlot.getCellId());
			map.put("nbCellId", neighbourPlotAddNeighbourPlot.getNcellId());
			list.add(map);
		}
		return list;
	}
}
