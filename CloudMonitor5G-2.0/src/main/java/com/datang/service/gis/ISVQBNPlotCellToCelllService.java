/**
 * 
 */
package com.datang.service.gis;

import java.util.List;
import java.util.Map;

/**
 * 流媒体视频质差邻区缺失路段小区与邻区连线service接口
 * 
 * @explain
 * @name ISVQBNPlotCellToCelllService
 * @author shenyanwei
 * @date 2017年11月28日上午9:35:04
 */
public interface ISVQBNPlotCellToCelllService {

	/**
	 * 获取某个邻区缺失路段小区与邻区连线信息
	 * 
	 * @param roadId
	 *            邻区缺失路段id
	 * @return
	 */
	public List<Map<String, Object>> getCellToCellInfo(Long roadId);

}
