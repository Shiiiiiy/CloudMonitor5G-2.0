/**
 * 
 */
package com.datang.service.gis;

import java.util.List;
import java.util.Map;

/**
 * 邻区缺失路段小区与邻区连线service接口
 * 
 * @author yinzhipeng
 * @date:2015年12月3日 下午2:12:18
 * @version
 */
public interface INbCellRoadCellToCelllService {

	/**
	 * 获取某个邻区缺失路段小区与邻区连线信息
	 * 
	 * @param roadId
	 *            邻区缺失路段id
	 * @return
	 */
	public List<Map<String, Object>> getCellToCellInfo(Long roadId);

}
