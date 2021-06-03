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
import com.datang.dao.testLogItem.StationVerificationTestDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.taskOrderManage.FixedPointTaskLogNamePojo;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import com.datang.service.testLogItem.StationVerificationTestService;

@Service
@Transactional
public class CQTTaskOrderServiceImpl implements CQTTaskOrderService {
	
	@Autowired
	private CQTTaskOrderDao fixedPointTaskOrderDao;

	@Autowired
	private StationVerificationTestDao StationVerificationTestDao;
	
	@Autowired
	private EceptionCellLogDao eceptionCellLogDao;
	
	@Autowired
	private CQTTaskLogDao fixedPointTaskLogDao;
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return fixedPointTaskOrderDao.doPageQuery(pageList);
	}
	
	@Override
	public FixedPointTaskOrderPojo find(Long id) {
		return fixedPointTaskOrderDao.find(id);
	}
	
	@Override
	public void update(FixedPointTaskOrderPojo svl) {
		fixedPointTaskOrderDao.update(svl);
	}
	
	@Override
	public void delete(String idStr) {
		String[] StrArry = idStr.split(",");
		for (int i = 0; i < StrArry.length; i++) {
			fixedPointTaskOrderDao.delete(Long.valueOf(StrArry[i]));
		}
	}
	
	@Override
	public List<PlanParamPojo> getNrCell(Long cityId) {
		return fixedPointTaskOrderDao.getNrCell(cityId);
	}

	@Override
	public List<PlanParamPojo> getNrCell(String cityName) {
		return fixedPointTaskOrderDao.getNrCell(cityName);
	}
	
	@Override
	public List<Plan4GParam> getLteCell(Long cityId) {
		return fixedPointTaskOrderDao.getLteCell(cityId);
	}

	@Override
	public List<Plan4GParam> getLteCell(String cityName) {
		return fixedPointTaskOrderDao.getLteCell(cityName);
	}

	@Override
	public List<FixedPointTaskOrderPojo> findFixedPointTask(PageList pageList){
		return fixedPointTaskOrderDao.findFixedPointTask(pageList);
	}
	
	@Override
	public List<FixedPointTaskLogNamePojo> getLogNamesByOrder(String workOrder){
		return fixedPointTaskLogDao.getLogNamesByOrder(workOrder);
	}
	
	@Override
	public void addCQTLogName(FixedPointTaskLogNamePojo fixedPointTaskLogNamePojo){
		fixedPointTaskLogDao.create(fixedPointTaskLogNamePojo);
	}
	

}
