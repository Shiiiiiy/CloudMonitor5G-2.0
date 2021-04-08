package com.datang.service.platform.stationParam.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.platform.stationParam.StationParamDao;
import com.datang.dao.platform.stationParam.StationProspectParamDao;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.service.platform.stationParam.StationParamService;
import com.datang.service.platform.stationParam.StationProspectParamService;
@Service
@Transactional
public class StationProspectParamServiceImpl implements StationProspectParamService {

	@Autowired
	public StationProspectParamDao stationProspectParamDao;
	
	@Override
	public void save(StationProspectParamPojo stationParamPojo) {
		stationProspectParamDao.create(stationParamPojo);
	}
	
	@Override
	public void update(StationProspectParamPojo stationParamPojo) {
		stationProspectParamDao.update(stationParamPojo);
	}
	
	@Override
	public StationProspectParamPojo find(Long id) {
		return stationProspectParamDao.find(id);
	}
	
	
	
	@Override
	public StationProspectParamPojo findBySiteName(String siteName) {
		return stationProspectParamDao.findBySiteName(siteName);
	}

}
