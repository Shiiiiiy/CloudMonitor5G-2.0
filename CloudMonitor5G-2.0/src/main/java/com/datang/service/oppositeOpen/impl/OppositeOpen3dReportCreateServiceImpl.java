package com.datang.service.oppositeOpen.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.oppositeOpen.OppositeOpen3dPerformanceReceiveDao;
import com.datang.dao.oppositeOpen.OppositeOpen3dReportCreateDao;
import com.datang.dao.oppositeOpen.OppositeOpen3dResultDao;
import com.datang.dao.oppositeOpen.OppositeOpen3dWirelessDao;
import com.datang.dao.platform.stationParam.StationBasicParamDao;
import com.datang.dao.stationTest.StationCellParamCensusDao;
import com.datang.dao.stationTest.StationNetoptReceiveTestDao;
import com.datang.dao.stationTest.StationPerformanceReceiveDao;
import com.datang.dao.testManage.terminal.TerminalMenuDao;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dPerformanceReceivePojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dResultPojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dWirelessPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.service.oppositeOpen.OppositeOpen3dReportCreateService;

@Service
public class OppositeOpen3dReportCreateServiceImpl implements OppositeOpen3dReportCreateService {

	@Autowired
	private OppositeOpen3dReportCreateDao oppositeOpen3dReportCreateDao;
	
	@Autowired
	private OppositeOpen3dWirelessDao oppositeOpen3dWirelessDao;
	
	@Autowired
	private OppositeOpen3dPerformanceReceiveDao oppositeOpen3dPerformanceReceiveDao;
	
	@Autowired
	private OppositeOpen3dResultDao oppositeOpen3dResultDao;
	
	@Autowired
	private StationCellParamCensusDao stationCellParamCensusDao;
	
	@Autowired
	private StationNetoptReceiveTestDao stationNetoptReceiveTestDao;
	
	@Autowired
	private StationPerformanceReceiveDao stationPerformanceReceiveDao;
	
	@Autowired
	private StationBasicParamDao stationBasicParamDao;
	
	/**
	 * 终端菜单
	 */
	@Autowired
	private TerminalMenuDao terminalMenuDao;
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return oppositeOpen3dReportCreateDao.doPageQuery(pageList);
	}

	@Override
	public Plan4GParam find(Long id) {
		return oppositeOpen3dReportCreateDao.find(id);
	}
	
	@Override
	public Plan4GParam find2(Long id) {
		Plan4GParam find = oppositeOpen3dReportCreateDao.find(id);
		if(find.getStationBasicParamPojoList()!=null && find.getStationBasicParamPojoList().size()>0){
			find.getStationBasicParamPojoList().get(0);
		}
		return find;
	}

	@Override
	public void update(Plan4GParam plan) {
		oppositeOpen3dReportCreateDao.update(plan);
	}

	@Override
	public void deleteReportByids(List<Long> idLong) {
		for (Long id : idLong) {
			Plan4GParam p4p1 = oppositeOpen3dReportCreateDao.find(id);
			
			OppositeOpen3dPerformanceReceivePojo oop = oppositeOpen3dPerformanceReceiveDao.findByParamId(id);
			p4p1.setOppositeOpen3dPerformanceReceivePojo(null);
			
			OppositeOpen3dWirelessPojo oow =  oppositeOpen3dWirelessDao.findByParamId(id);
			p4p1.setOppositeOpen3dWirelessPojo(null);
			
			OppositeOpen3dResultPojo oor = oppositeOpen3dResultDao.findByParamId(id);
			p4p1.setOppositeOpen3dResultPojo(null);
			
			StationBasicParamPojo sbp = stationBasicParamDao.findByParamId(id);
			p4p1.setStationBasicParamPojoList(null);
		
			oppositeOpen3dReportCreateDao.update(p4p1);
			
			
			if(oop != null){
				oppositeOpen3dPerformanceReceiveDao.delete(oop.getId());
			}
			if(oow != null){
				oppositeOpen3dWirelessDao.delete(oow.getId());
			}
			if(oor != null){
				oppositeOpen3dResultDao.delete(oor.getId());
				
			}
			if(sbp != null){
				stationBasicParamDao.delete(sbp.getId());
			}
		}
	}

	@Override
	public void saveWireless(OppositeOpen3dWirelessPojo oow) {
		oppositeOpen3dWirelessDao.create(oow);
	}
	
	@Override
	public List<Plan4GParam> findByIds(List<Long> idList) {
		
		return oppositeOpen3dReportCreateDao.findByIds(idList);
	}
	
	@Override
	public List<Plan4GParam> findByIds2(List<Long> idList) {
		List<Plan4GParam> plan4GParamList = oppositeOpen3dReportCreateDao.findByIds(idList);
		for (Plan4GParam plan4gParam : plan4GParamList) {
			if(plan4gParam.getStationBasicParamPojoList()!=null && plan4gParam.getStationBasicParamPojoList().size()>0){
				plan4gParam.getStationBasicParamPojoList().get(0);
			}
		}
		return oppositeOpen3dReportCreateDao.findByIds(idList);
	}
	
	@Override
	public OppositeOpen3dWirelessPojo findBySiteName(String siteName) {
		
		return oppositeOpen3dWirelessDao.findBySiteName(siteName);
	}

	@Override
	public void update(OppositeOpen3dWirelessPojo oow) {
		oppositeOpen3dWirelessDao.update(oow);
	}
	
	@Override
	public void savePerformanceReceivePojo(OppositeOpen3dPerformanceReceivePojo oop){
		oppositeOpen3dPerformanceReceiveDao.create(oop);
	}
	
	@Override
	public void updatePerformanceReceivePojo(OppositeOpen3dPerformanceReceivePojo oop){
		oppositeOpen3dPerformanceReceiveDao.update(oop);
	}

	@Override
	public void creteOOW(OppositeOpen3dWirelessPojo oow) {
		oppositeOpen3dWirelessDao.create(oow);
	}

	@Override
	public void updateOOW(OppositeOpen3dWirelessPojo oow) {
		oppositeOpen3dWirelessDao.update(oow);
	}

	@Override
	public void creatOOR(OppositeOpen3dResultPojo oor) {
		oppositeOpen3dResultDao.create(oor);
	}

	@Override
	public void updateOOR(OppositeOpen3dResultPojo oor) {
		oppositeOpen3dResultDao.create(oor);
	}

	@Override
	public List<Plan4GParam> findAll() {
		return (List<Plan4GParam>) oppositeOpen3dReportCreateDao.findAll();
	}
	
	@Override
	public List<Plan4GParam> getAllLocalCellId(String enbId){
		return oppositeOpen3dReportCreateDao.getAllLocalCellId(enbId);
	}
	
	@Override
	public List<Plan4GParam> getAllBySitename(String siteName){
		return oppositeOpen3dReportCreateDao.getAllBySitename(siteName);
	}

}
