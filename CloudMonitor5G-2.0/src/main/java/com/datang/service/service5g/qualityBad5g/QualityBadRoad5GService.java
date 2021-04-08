package com.datang.service.service5g.qualityBad5g;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.domain.domain5g.qualityBad5g.InterfereRoadPojo;
/**
 * 干扰路段分析
 * @author maxuancheng
 * @date 2019年3月14日
 */
public interface QualityBadRoad5GService {

	/**
	 * 获取干扰问题质差路段
	 * 
	 * @param ids
	 * @return
	 */
	public List<InterfereRoadPojo> getDisturbRoadsByLogIds(
			List<Long> testLogItemIds);

	/**
	 * 干扰问题质差路段分析
	 * @author maxuancheng
	 * @param roadId 路段id
	 */
	public Map<String, EasyuiPageList> doDisturbAnalysis(Long roadId);
	
	/**
	 * 三超小区详情
	 * @author maxuancheng
	 * @param roadId 路段id
	 * @return
	 */
	public EasyuiPageList doDisturbSanChaoCellAnalysis(Long roadId);
	
	/**
	 * 分析干扰问题质差路段小区详情表
	 * @author maxuancheng
	 * @param roadId 路段id
	 * @return
	 */
	public EasyuiPageList doDisturbCellInfoAnalysis(Long roadId);

	/**
	 * 建议天馈调整——重叠覆盖
	 * @author maxuancheng
	 * @param roadId 路段id
	 * @return 
	 */
	public EasyuiPageList doDisturbTiankuiOverCoverAnalysis(Long roadId);

	/**
	 * 建议天馈调整——过覆盖
	 * @author maxuancheng
	 * @param roadId 路段id
	 * @return 
	 */
	public EasyuiPageList doDisturbTiankuiHighCoverAnalysis(Long roadId);

	/**
	 * 获取单个质差路段公共参数
	 * 
	 * @param roadId
	 * @return
	 */
	public InterfereRoadPojo getVolteQualityBadRoad(Long roadId);

	/**
	 * 获取多个质差路段公共参数
	 * @author maxuancheng
	 * @param ids
	 * @param coverType
	 * @return
	 */
	public List<InterfereRoadPojo> getQualityBadRoadByLogIds(List<Long> ids);

	/**
	 * 获取波束详情
	 * @author maxuancheng
	 * @param cellInfoId
	 * @return
	 */
	public Object getCellBeamInfoAnalysis(Long cellInfoId);
}
