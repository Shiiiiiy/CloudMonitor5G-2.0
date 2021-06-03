package com.datang.service.taskOrderManage.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.stationTest.EceptionCellLogDao;
import com.datang.dao.taskOrderManage.CQTTaskLogDao;
import com.datang.dao.taskOrderManage.CQTTaskOrderDao;
import com.datang.dao.taskOrderManage.StationTaskLogDao;
import com.datang.dao.taskOrderManage.StationTaskOrderDao;
import com.datang.dao.testLogItem.StationVerificationTestDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.taskOrderManage.StationTaskLogNamePojo;
import com.datang.domain.taskOrderManage.StationTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import com.datang.service.taskOrderManage.StationTaskOrderService;
import com.datang.service.testLogItem.StationVerificationTestService;

@Service
@Transactional
public class StationTaskOrderServiceImpl implements StationTaskOrderService {
	
	@Autowired
	private StationTaskOrderDao stationTaskOrderDao;

	
	@Autowired
	private StationTaskLogDao stationTaskLogDao;
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return stationTaskOrderDao.doPageQuery(pageList);
	}
	
	@Override
	public StationTaskOrderPojo find(Long id) {
		return stationTaskOrderDao.find(id);
	}
	
	@Override
	public void update(StationTaskOrderPojo svl) {
		stationTaskOrderDao.update(svl);
	}
	
	@Override
	public void delete(String idStr) {
		String[] StrArry = idStr.split(",");
		for (int i = 0; i < StrArry.length; i++) {
			stationTaskOrderDao.delete(Long.valueOf(StrArry[i]));
		}
	}
	
	@Override
	public List<PlanParamPojo> getNrCell(Long cityId) {
		return stationTaskOrderDao.getNrCell(cityId);
	}
	
	@Override
	public List<Plan4GParam> getLteCell(Long cityId) {
		return stationTaskOrderDao.getLteCell(cityId);
	}
	
	@Override
	public List<StationTaskOrderPojo> findStationTaskTask(PageList pageList){
		return stationTaskOrderDao.findStationTaskTask(pageList);
	}
	
	@Override
	public List<StationTaskLogNamePojo> getLogNamesByOrder(String workOrder){
		return stationTaskLogDao.getLogNamesByOrder(workOrder);
	}
	
	@Override
	public void addCQTLogName(StationTaskLogNamePojo stationTaskLogNamePojo){
		stationTaskLogDao.create(stationTaskLogNamePojo);
	}
	
	@Override
	public List<StationEtgTralPojo> findTralById(List<String> fileNames,String pci){
		return stationTaskOrderDao.findTralById(fileNames,pci);
	}
	

}
