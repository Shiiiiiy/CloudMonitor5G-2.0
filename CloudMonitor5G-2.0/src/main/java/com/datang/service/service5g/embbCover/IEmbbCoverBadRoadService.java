/**
 * 
 */
package com.datang.service.service5g.embbCover;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;

/**
 * 5G专题----EMBB覆盖专题service接口
 * 
 * @author _YZP
 * 
 */
public interface IEmbbCoverBadRoadService {
	/**
	 * 获取单个embb覆盖路段
	 * 
	 * @param ecbrId
	 * @return
	 */
	public EmbbCoverBadRoad getEmbbCoverBadRoad(Long ecbrId);

	/**
	 * 根据测试日志ID集合获取多个embb覆盖路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<EmbbCoverBadRoad> getEmbbCoverBadRoadByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 根据测试日志ID集合获取多个embb覆盖路段(区分弱过重叠覆盖)
	 * 
	 * @param testLogItemIds
	 * @param coverTypeNum
	 * @return
	 */
	public List<EmbbCoverBadRoad> getEmbbCoverBadRoadByLogIds(
			List<Long> testLogItemIds, int coverTypeNum);

	/**
	 * 获取embb覆盖路段中的单个覆盖小区
	 * 
	 * @param ecciId
	 * @return
	 */
	public EmbbCoverCellInfo getEmbbCoverCellInfo(Long ecciId);

	/**
	 * 总体设计界面分析
	 * 
	 * @param ids
	 * @return
	 */
	public Map<String, Map<String, Object>> doWholeAnalysis(List<Long> ids);

	/**
	 * 弱过重叠覆盖总体指标统计
	 * 
	 * @param embbCoverBadRoadByLogIds
	 * @return
	 */
	public Map<String, Object> doEmbbCoverTotalIndexAnalysis(
			List<EmbbCoverBadRoad> embbCoverBadRoadByLogIds);

	/**
	 * 弱过重叠覆盖问题路段分析
	 * 
	 * @param embbCoverBadRoad
	 * @return
	 */
	public Map<String, EasyuiPageList> doEmbbCoverAnalysis(
			EmbbCoverBadRoad embbCoverBadRoad);

	/**
	 * 分析弱过重叠覆盖问题路段小区详情表
	 * 
	 * @param embbCoverBadRoad
	 * @return
	 */
	public EasyuiPageList doEmbbCoverCellInfoAnalysis(
			EmbbCoverBadRoad embbCoverBadRoad);

	/**
	 * 分析弱过重叠覆盖问题路段优化建议详情表
	 * 
	 * @param embbCoverBadRoad
	 * @return
	 */
	public EasyuiPageList doEmbbCoverOptimizeAdviceAnalysis(
			EmbbCoverBadRoad embbCoverBadRoad);

	/**
	 * 分析弱过重叠覆盖问题路段覆盖小区的波束详情表
	 * 
	 * @param embbCoverCellInfo
	 * @return
	 */
	public EasyuiPageList doEmbbCoverCellBeamInfoAnalysis(
			EmbbCoverCellInfo embbCoverCellInfo);

}
