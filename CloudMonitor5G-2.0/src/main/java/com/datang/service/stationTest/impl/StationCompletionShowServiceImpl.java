package com.datang.service.stationTest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.stationTest.StationCompletionShowDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.service.stationTest.StationCompletionShowService;

@Service
@Transactional
public class StationCompletionShowServiceImpl implements StationCompletionShowService {
	
	@Autowired
	private StationCompletionShowDao stationCompletionShowDao;
	
	@Override
	public long getReportAnalysssResult(String name,String city){
		return stationCompletionShowDao.getReportAnalysssResult(name,city);
		
	}

	@Override
	public List<PlanParamPojo> getDataOfCityName(String cityName) {
		
		return stationCompletionShowDao.getDataOfCityName(cityName);
	}

	@Override
	public List<PlanParamPojo> queryCellLonAndLat(String cellName, String cityName) {
		return stationCompletionShowDao.queryCellLonAndLat(cellName,cityName);
	}

}
