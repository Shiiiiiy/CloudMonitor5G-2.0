package com.datang.service.testLogItem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.stationTest.EceptionCellLogDao;
import com.datang.dao.testLogItem.StationVerificationTestDao;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.service.testLogItem.StationVerificationTestService;

@Service
@Transactional
public class StationVerificationTestServiceImpl implements StationVerificationTestService {

	@Autowired
	private StationVerificationTestDao StationVerificationTestDao;
	
	@Autowired
	private EceptionCellLogDao eceptionCellLogDao;
	
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		return StationVerificationTestDao.doPageQuery(pageList);
	}
	@Override
	public StationVerificationLogPojo find(Long id) {
		return StationVerificationTestDao.find(id);
	}
	@Override
	public void update(StationVerificationLogPojo svl) {
		StationVerificationTestDao.update(svl);
	}
	@Override
	public void delete(String idStr) {
		String[] StrArry = idStr.split(",");
		for (int i = 0; i < StrArry.length; i++) {
			StationVerificationTestDao.delete(Long.valueOf(StrArry[i]));
		}
	}
	@Override
	public List<StationVerificationLogPojo> findByCorrelativeCell(String cellName,String event,String wireStatus) {
		return StationVerificationTestDao.findByCorrelativeCell(cellName,event,wireStatus);
	}
	@Override
	public List<EceptionCellLogPojo> findEceptionCellLogByFileName(String fileName) {
		
		return eceptionCellLogDao.findByFileName(fileName);
	}
	@Override
	public void updateEcp(EceptionCellLogPojo ecp) {
		eceptionCellLogDao.update(ecp);
	}
	
	@Override
	public List<StationVerificationLogPojo> findByBoxid(String boxId){
		return StationVerificationTestDao.findByBoxid(boxId);
	}
	
	@Override
	public List<StationVerificationLogPojo> findOfBoxidLogName(String boxId,List<String> logNames){
		return StationVerificationTestDao.findOfBoxidLogName(boxId,logNames);
	}

}
