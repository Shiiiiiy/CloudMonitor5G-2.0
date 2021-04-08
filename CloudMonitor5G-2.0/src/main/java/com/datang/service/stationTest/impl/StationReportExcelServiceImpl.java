package com.datang.service.stationTest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.stationTest.StationReportDisplayDao;
import com.datang.domain.stationTest.StationReportExcelPojo;
import com.datang.service.stationTest.StationReportExcelService;

@Service
@Transactional
public class StationReportExcelServiceImpl implements StationReportExcelService {
	
	@Autowired
	public StationReportDisplayDao stationReportDisplayDao;
	
	@Override
	public void save(StationReportExcelPojo stationReportExcelPojo) {
		stationReportDisplayDao.create(stationReportExcelPojo);
	}
	@Override
	public void update(StationReportExcelPojo stationReportExcelPojo) {
		stationReportDisplayDao.update(stationReportExcelPojo);
	}
	@Override
	public StationReportExcelPojo find(Long id) {
		return stationReportDisplayDao.find(id);
	}
	@Override
	public void delete(List<Long> idList) {
		for (Long id : idList) {
			stationReportDisplayDao.delete(id);
		}
		
	}
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return stationReportDisplayDao.doPageQuery(pageList);
	}
	
	@Override
	public List<StationReportExcelPojo> findByParam(PageList pageList){
		return stationReportDisplayDao.findByParam(pageList);
	}

}
