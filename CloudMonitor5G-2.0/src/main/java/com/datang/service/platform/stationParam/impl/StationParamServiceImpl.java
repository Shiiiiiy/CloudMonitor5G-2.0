package com.datang.service.platform.stationParam.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.platform.stationParam.StationParamDao;
import com.datang.dao.platform.stationParam.StationReportTemplateDao;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationReportTemplatePojo;
import com.datang.service.platform.stationParam.StationParamService;
@Service
@Transactional
public class StationParamServiceImpl implements StationParamService {

	@Autowired
	public StationParamDao stationParamDao;
	
	@Autowired
	public StationReportTemplateDao stationReportTemplateDao;
	
	@Override
	public void save(StationParamPojo stationParamPojo) {
		stationParamDao.create(stationParamPojo);
	}
	@Override
	public void update(StationParamPojo stationParamPojo) {
		stationParamDao.update(stationParamPojo);
	}
	@Override
	public StationParamPojo find(Long id) {
		return stationParamDao.find(id);
	}
	@Override
	public StationParamPojo findByMenuId(Long cityId) {
		return stationParamDao.findByMenuId(cityId);
	}
	
	@Override
	public StationReportTemplatePojo findTemplateParam(Integer templteType,Long templteValue) {
		return stationReportTemplateDao.findTemplateParam(templteType,templteValue);
	}
	
	@Override
	public List<StationReportTemplatePojo> findTemplateByParam(Map<String, Object> param) {
		return stationReportTemplateDao.findTemplateByParam(param);
	}

}
