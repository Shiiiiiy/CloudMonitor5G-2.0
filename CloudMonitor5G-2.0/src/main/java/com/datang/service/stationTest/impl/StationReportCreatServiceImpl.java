package com.datang.service.stationTest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.platform.stationParam.StationBasicParamDao;
import com.datang.dao.stationTest.StationCellParamCensusDao;
import com.datang.dao.stationTest.StationEtgTralDao;
import com.datang.dao.stationTest.StationNetoptReceiveTestDao;
import com.datang.dao.stationTest.StationPerformanceReceiveDao;
import com.datang.dao.stationTest.StationReportCreatDao;
import com.datang.dao.testLogItem.TestLogEtgTrailKpiDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogEtgTrailKpiPojo;
import com.datang.service.stationTest.StationReportCreatService;

@Service
@Transactional
public class StationReportCreatServiceImpl implements StationReportCreatService {

	@Autowired
	private StationReportCreatDao stationReportCreatDao;
	
	@Autowired
	private StationCellParamCensusDao stationCellParamCensusDao;
	
	@Autowired
	private StationNetoptReceiveTestDao stationNetoptReceiveTestDao;
	
	@Autowired
	private StationPerformanceReceiveDao stationPerformanceReceiveDao;
	
	@Autowired
	private StationBasicParamDao stationBasicParamDao;
	
	@Autowired
	private StationEtgTralDao stationEtgTralDao;
	
	@Autowired
	private TestLogEtgTrailKpiDao testLogEtgTrailKpiDao;
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		
		return stationReportCreatDao.doPageQuery(pageList);
	}
	
	@Override
	public PlanParamPojo find(Long id) {
		return stationReportCreatDao.find(id);
	}
	
	@Override
	public PlanParamPojo find1(Long id) {
		PlanParamPojo find = stationReportCreatDao.find(id);
		if(find.getStationNetoptReceiveTestList()!=null && find.getStationNetoptReceiveTestList().size()>0){
			find.getStationNetoptReceiveTestList().get(0);
		}
		if(find.getStationBasicParamPojoList()!=null && find.getStationBasicParamPojoList().size()>0){
			find.getStationBasicParamPojoList().get(0);
		}
		if(find.getStationPerformanceReceiveList()!=null && find.getStationPerformanceReceiveList().size()>0){
			find.getStationPerformanceReceiveList().get(0);
		}
		if(find.getStationCellParamCensusList()!=null && find.getStationCellParamCensusList().size()>0){
			find.getStationCellParamCensusList().get(0);
		}
		return find;
	}
	
	@Override
	public void update(PlanParamPojo plan) {
		stationReportCreatDao.update(plan);
	}
	
	@Override
	public List<EceptionCellLogPojo> getExceptionCellLogOfCellName(String cellName) {
		
		return stationReportCreatDao.getExceptionCellLogOfCellName(cellName);
	}
	
	@Override
	public void createExceptionCellParam(StationCellParamCensusPojo cellParamCensus) {
		stationCellParamCensusDao.create(cellParamCensus);
	}
	
	@Override
	public void deleteByids(List<Long> idLong) {
		for (Long id : idLong) {
			StationCellParamCensusPojo ssp = stationCellParamCensusDao.findByParamId(id);
			StationNetoptReceiveTestPojo snt = stationNetoptReceiveTestDao.findByParamId(id);
			StationPerformanceReceivePojo spr = stationPerformanceReceiveDao.findByParamId(id);
			StationBasicParamPojo sbp = stationBasicParamDao.findByParamId(id);
			if(ssp != null){
				stationCellParamCensusDao.delete(ssp.getId());
			}
			if(snt!= null){
				stationNetoptReceiveTestDao.delete(snt.getId());
			}
			if(spr != null){
				stationPerformanceReceiveDao.delete(spr.getId());
			}
			if(sbp != null){
				stationBasicParamDao.delete(sbp.getId());
			}
		}
	}
	
	@Override
	public void createNetoptReceiveTest(StationNetoptReceiveTestPojo netoptReceiveTest) {
		stationNetoptReceiveTestDao.create(netoptReceiveTest);
	}
	
	@Override
	public void createPerformanceReceive(StationPerformanceReceivePojo performanceReceive) {
		stationPerformanceReceiveDao.create(performanceReceive);
	}
	
	@Override
	public void createStationBasicParamTest(StationBasicParamPojo stationBasicParamPojo){
		stationBasicParamDao.create(stationBasicParamPojo);
	}
	
	@Override
	public List<PlanParamPojo> findByIds(List<Long> idList) {
		
		return stationReportCreatDao.findByIds(idList);
	}
	
	@Override
	public List<PlanParamPojo> findByIds2(List<Long> idList) {
		List<PlanParamPojo> findByIds = stationReportCreatDao.findByIds(idList);
		for (PlanParamPojo find : findByIds) {
			if(find.getStationNetoptReceiveTestList()!=null && find.getStationNetoptReceiveTestList().size()>0){
				find.getStationNetoptReceiveTestList().get(0);
			}
			if(find.getStationBasicParamPojoList()!=null && find.getStationBasicParamPojoList().size()>0){
				find.getStationBasicParamPojoList().get(0);
			}
			if(find.getStationPerformanceReceiveList()!=null && find.getStationPerformanceReceiveList().size()>0){
				find.getStationPerformanceReceiveList().get(0);
			}
			if(find.getStationCellParamCensusList()!=null && find.getStationCellParamCensusList().size()>0){
				find.getStationCellParamCensusList().get(0);
			}
		}
		return findByIds;
	}
	
	@Override
	public List<PlanParamPojo> findAll() {
		return (List<PlanParamPojo>) stationReportCreatDao.findAll();
	}
	
	@Override
	public List<StationEtgTralPojo> findTralById(String fileName,String pci) {
		return stationEtgTralDao.findTralById(fileName,pci);
	}
	
	@Override
	public List<StationEtgTralPojo> findEtgTralByReportId(String fileName,String pci,String event){
		return stationEtgTralDao.findEtgTralByReportId(fileName,pci,event);
	}
	
	@Override
	public List<StationSAMTralPojo> findSamTralByReportId(String fileName,String pci,String event){
		return stationEtgTralDao.findSamTralByReportId(fileName,pci,event);
	}
	
	@Override
	public StationEtgTralPojo findMaxTralByReportId(String fileName, String event) {
		return stationEtgTralDao.findMaxTralByReportId(fileName,event);
	}
	
	@Override
	public StationEtgTralPojo findMaxTralTimeByReportName(String fileName,String type){
		return stationEtgTralDao.findMaxTralTimeByReportName(fileName,type);
	}
	
	@Override
	public List<StationEtgTralPojo> findTralByMaxTime(String fileName, String event, String maxTime, Long timeLength) {
		return stationEtgTralDao.findTralByMaxTime(fileName,event,maxTime,timeLength);
	}
	
	@Override
	public List<PlanParamPojo> getAllLocalCellId(String siteName){
		return stationReportCreatDao.getAllLocalCellId(siteName);
	}
	
	@Override
	public List<StationNetoptReceiveTestPojo> getNetoptReceiveTest(String siteName){
		return stationNetoptReceiveTestDao.getNetoptReceiveTest(siteName);
	}
	
	@Override
	public List<StationPerformanceReceivePojo> getPerformanceReceiveList(String siteName){
		return stationPerformanceReceiveDao.getPerformanceReceiveList(siteName);
	}
	
	@Override
	public List<StationBasicParamPojo> getStationBasicParamList(String siteName){
		return stationBasicParamDao.getStationBasicParamList(siteName);
	}
	
	@Override
	public void updateNetoptReceiveTest(StationNetoptReceiveTestPojo netoptReceiveTest) {
		stationNetoptReceiveTestDao.update(netoptReceiveTest);
	}
	
	@Override
	public void updatePerformanceReceive(StationPerformanceReceivePojo performanceReceive) {
		stationPerformanceReceiveDao.update(performanceReceive);
	}
	
	@Override
	public void updateStationBasicParamTest(StationBasicParamPojo stationBasicParamPojo){
		stationBasicParamDao.update(stationBasicParamPojo);
	}
	
	@Override
	public List<EceptionCellLogPojo> getExceptionCellLogOfCellLog(String cellName,List<String> logNameList,String testService,String wireStatus){
		return stationReportCreatDao.getExceptionCellLogOfCellLog(cellName,logNameList,testService,wireStatus);
	}
	
	@Override
	public List<TestLogEtgTrailKpiPojo> findTrailKpiByParam(PageList pageList){
		return testLogEtgTrailKpiDao.findTrailKpiByParam(pageList);
	}

}
