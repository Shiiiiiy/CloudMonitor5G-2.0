/**
 * 
 */
package com.datang.service.platform.analysisThreshold.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.platform.analysisThreshold.MapTrailThrrsholdDao;
import com.datang.dao.platform.analysisThreshold.VolteAnalysisThresholdDao;
import com.datang.domain.platform.analysisThreshold.MapTrailThreshold;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;

/**
 * volte专题分析门限----service实现
 * 
 * @author yinzhipeng
 * @date:2015年9月28日 下午3:58:26
 * @version
 */
@Service
@Transactional
public class VolteAnalysisThresholdServiceImpl implements
		IVolteAnalysisThresholdService {
	@Autowired
	private VolteAnalysisThresholdDao analysisThresholdDao;
	
	@Autowired
	private MapTrailThrrsholdDao mapTrailThrrsholdDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService
	 * #
	 * updateVolteAnalysisThreshold(com.datang.domain.platform.analysisThreshold
	 * .VolteAnalysisThreshold)
	 */
	@Override
	public void updateVolteAnalysisThreshold(
			VolteAnalysisThreshold analysisThreshold) {
		analysisThresholdDao.update(analysisThreshold);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService
	 * #getVolteAnalysisThresholdByNameEn(java.lang.String)
	 */
	@Override
	public VolteAnalysisThreshold getVolteAnalysisThresholdByNameEn(
			String nameEn) {
		return analysisThresholdDao.queryByEnName(nameEn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService
	 * #getVolteAnalysisThresholdById(java.lang.Long)
	 */
	@Override
	public VolteAnalysisThreshold getVolteAnalysisThresholdById(Long id) {
		return analysisThresholdDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService
	 * #getAll()
	 */
	@Override
	public List<VolteAnalysisThreshold> queryBySubjectType(String subjectType) {
		List<VolteAnalysisThreshold> findAll = (List<VolteAnalysisThreshold>) analysisThresholdDao
				.queryBySubjectType(subjectType);
		return findAll;
	}
	
	@Override
	public List<VolteAnalysisThreshold> queryByMapParam(Map<String,Object> mapParam){
		List<VolteAnalysisThreshold> findAll = (List<VolteAnalysisThreshold>) analysisThresholdDao
				.queryByMapParam(mapParam);
		return findAll;
	}
	
	@Override
	public List<MapTrailThreshold> queryTrailByMapParam(Map<String,Object> mapParam){
		List<MapTrailThreshold> findAll = (List<MapTrailThreshold>) mapTrailThrrsholdDao
				.queryByMapParam(mapParam);
		return findAll;
	}
	
	@Override
	public void saveMapTrailThreshold(MapTrailThreshold mapTrailThreshold){
		mapTrailThrrsholdDao.create(mapTrailThreshold);
	}
	
	@Override
	public void updateMapTrailThreshold(MapTrailThreshold mapTrailThreshold){
		mapTrailThrrsholdDao.update(mapTrailThreshold);
	}

}
