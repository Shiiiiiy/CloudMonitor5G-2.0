package com.datang.service.gis;

import java.util.List;
import java.util.Map;

/**
 * 邻区缺失路段小区与邻区连线service接口
 * 
 * @explain
 * @name ICWBRNbCellRoadCellToCelllService
 * @author shenyanwei
 * @date 2016年6月24日下午6:16:08
 */
public interface ICWBRNbCellRoadCellToCelllService {
	/**
	 * 获取某个邻区缺失路段小区与邻区连线信息
	 * 
	 * @param roadId
	 *            邻区缺失路段id
	 * @return
	 */
	public List<Map<String, Object>> getCellToCellInfo(Long roadId);
}
