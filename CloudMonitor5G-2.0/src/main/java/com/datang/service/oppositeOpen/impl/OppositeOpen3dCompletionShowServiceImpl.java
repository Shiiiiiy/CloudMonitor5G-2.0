package com.datang.service.oppositeOpen.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.oppositeOpen.OppositeOpen3dCompletionShowDao;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.service.oppositeOpen.OppositeOpen3dCompletionShowService;

@Service
@Transactional
public class OppositeOpen3dCompletionShowServiceImpl implements OppositeOpen3dCompletionShowService {
	
	@Autowired
	private OppositeOpen3dCompletionShowDao oppositeOpen3dCompletionShowDao;
	
	@Override
	public long getReportAnalysssResult(String name,String city){
		return oppositeOpen3dCompletionShowDao.getReportAnalysssResult(name,city);
		
	}

	@Override
	public List<Plan4GParam> getDataOfCityName(String cityName) {
		
		return oppositeOpen3dCompletionShowDao.getDataOfCityName(cityName);
	}

	@Override
	public List<Plan4GParam> queryCellLonAndLat(String cellName, String cityName) {
		return oppositeOpen3dCompletionShowDao.queryCellLonAndLat(cellName,cityName);
	}

}
