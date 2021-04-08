/**
 * 
 */
package com.datang.service.gis;

import java.util.List;
import java.util.Map;

/**
 * 视频质差邻区缺失路段小区与邻区连线service接口
 * 
 * @explain
 * @name IVQBNPlotCellToCelllService
 * @author shenyanwei
 * @date 2017年5月23日下午2:53:52
 */
public interface IVQBNPlotCellToCelllService {

	/**
	 * 获取某个邻区缺失路段小区与邻区连线信息
	 * 
	 * @param roadId
	 *            邻区缺失路段id
	 * @return
	 */
	public List<Map<String, Object>> getCellToCellInfo(Long roadId);

}
