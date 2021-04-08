package com.datang.service.platform.analysisThreshold;

import java.util.List;

import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.security.User;

/**
 * Volte分析门限用户选取值Service接口
 * 
 * @author shenyanwei
 * @date 2016年4月28日下午1:35:09
 */
public interface IVolteAnalysisAboutThresholdService {
	/*
	 * 根据用户查询对比分析门限
	 */
	public List<VolteAnalysisAboutThreshold> selectCompareByUser(User user);

	/**
	 * 通过Id查
	 * 
	 * @param id
	 * @return 查询到的VolteAnalysisAboutThreshold
	 */
	public VolteAnalysisAboutThreshold selectById(Long id);

	/**
	 * 插入
	 * 
	 * @param volteAnalysisAboutThreshold
	 */
	public void save(VolteAnalysisAboutThreshold volteAnalysisAboutThreshold);

	/**
	 * 根据 用户Id查
	 * 
	 * @param userId
	 * @return 查询到的VolteAnalysisAboutThreshold
	 */
	public List<VolteAnalysisAboutThreshold> selectByUser(User user);

	/**
	 * 修改选取的门限值
	 * 
	 * @param volteAnalysisAboutThreshold
	 */
	public void update(VolteAnalysisAboutThreshold volteAnalysisAboutThreshold);

	/**
	 * 删除一个选取的值
	 * 
	 * @param id
	 */
	public void delete(Long id);

	/**
	 * 根据类型查
	 */
	public VolteAnalysisAboutThreshold selectByType(String thresholdType);
}
