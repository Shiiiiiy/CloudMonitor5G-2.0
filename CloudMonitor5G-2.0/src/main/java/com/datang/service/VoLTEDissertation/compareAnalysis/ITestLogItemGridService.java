/**
 * 
 */
package com.datang.service.VoLTEDissertation.compareAnalysis;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.EasyuiPageList;

/**
 * 测试日志栅格service接口
 * 
 * @author yinzhipeng
 * @date:2016年6月30日 上午9:17:00
 * @version
 */
public interface ITestLogItemGridService {

	/**
	 * 栅格对比分析(用于界面)
	 * 
	 * @param ids
	 * @param compareIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doTestLogItemGridAnalysis(
			List<Long> ids, List<Long> compareIds);

	/**
	 * 栅格对比分析(用于地图)
	 * 
	 * @param ids
	 * @param compareIds
	 * @return
	 */
	public List<Map<String, Object>> doTestLogItemGridAnalysis(List<Long> ids,
			List<Long> compareIds, Integer indexType);

	/**
	 * 5G栅格对比分析(用于界面)
	 * 
	 * @param ids
	 * @param compareIds
	 * @return
	 */
	public Map<String, EasyuiPageList> doTestLogItemGridAnalysisFG(
			List<Long> ids, List<Long> compareIds);

}
