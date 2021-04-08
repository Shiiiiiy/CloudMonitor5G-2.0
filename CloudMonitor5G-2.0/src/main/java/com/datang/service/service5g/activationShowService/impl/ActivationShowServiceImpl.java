package com.datang.service.service5g.activationShowService.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datang.dao.dao5g.activationShowDao.ActivationShowDao;
import com.datang.dao.dao5g.activationShowDao.ActivationShowOfUsedStatisticsDao;
import com.datang.service.service5g.activationShowService.ActivationShowService;

@Service
@Transactional
public class ActivationShowServiceImpl implements ActivationShowService {

	@Autowired
	private ActivationShowDao activationShowDao;
	
	@Autowired
	private ActivationShowOfUsedStatisticsDao activationShowOfUsedStatisticsDao;
	
	@Override
	public Map<String, Long> getUserNumber(String cityId) {
		
		return activationShowDao.getUserNumber(cityId);
	}

	@Override
	public Map<String, Object> getVersionAndUserNumber(String cityId) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> hm = activationShowDao.getHardwareVersionAndUserNumber(cityId);
		Map<String, Object> sm = activationShowDao.getSoftwareVersionAndUserNumber(cityId);
		map.putAll(hm);
		map.putAll(sm);
		return map;
	}

	@Override
	public Map<String, Object> getTerminalUsedStatistics(String cityId) {
		
		return activationShowOfUsedStatisticsDao.getTerminalUsedStatistics(cityId);
	}

	@Override
	public List<Object> getOnLineUserBoxid(String cityId) {
		return activationShowDao.getOnLineUserBoxid(cityId);
	}

}
