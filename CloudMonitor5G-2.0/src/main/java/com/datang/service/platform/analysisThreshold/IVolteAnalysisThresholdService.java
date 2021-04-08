/**
 * 
 */
package com.datang.service.platform.analysisThreshold;

import java.util.List;
import java.util.Map;

import com.datang.domain.platform.analysisThreshold.MapTrailThreshold;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;

/**
 * volte专题分析门限----service接口
 * 
 * @author yinzhipeng
 * @date:2015年9月28日 下午3:48:16
 * @version
 */
public interface IVolteAnalysisThresholdService {
	/**
	 * 更新某个门限
	 * 
	 * @param analysisThreshold
	 */
	public void updateVolteAnalysisThreshold(
			VolteAnalysisThreshold analysisThreshold);

	/**
	 * 根据门限英文名称获取该门限
	 * 
	 * @param nameEn
	 * @return VolteAnalysisThreshold
	 */
	public VolteAnalysisThreshold getVolteAnalysisThresholdByNameEn(
			String nameEn);

	/**
	 * 根据门限ID获取该门限
	 * 
	 * @param id
	 * @return VolteAnalysisThreshold
	 */
	public VolteAnalysisThreshold getVolteAnalysisThresholdById(Long id);

	/**
	 * 获取所有volte专题分析门限
	 * 
	 * @return
	 */
	public List<VolteAnalysisThreshold> queryBySubjectType(String subjectType);
	
	/**
	 * 根据门限专题条件查询
	 * @return
	 */
	public List<VolteAnalysisThreshold> queryByMapParam(Map<String,Object> mapParam);
	
	/**
	 * 根据门限专题条件查询轨迹图参数
	 * @return
	 */
	public List<MapTrailThreshold> queryTrailByMapParam(Map<String,Object> mapParam);
	
	/**
	 * 保存区域对应的轨迹图参数
	 * @author lucheng
	 * @date 2020年12月16日 上午10:32:26
	 * @param mapTrailThreshold
	 */
	public void saveMapTrailThreshold(MapTrailThreshold mapTrailThreshold);
	
	/**
	 * 更新区域对应的轨迹图参数
	 * @author lucheng
	 * @date 2020年12月16日 上午10:32:26
	 * @param mapTrailThreshold
	 */
	public void updateMapTrailThreshold(MapTrailThreshold mapTrailThreshold);
}
