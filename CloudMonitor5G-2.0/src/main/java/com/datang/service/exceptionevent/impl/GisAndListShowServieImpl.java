package com.datang.service.exceptionevent.impl;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.exceptionevent.NSAGisAndListShowDao;
import com.datang.domain.exceptionevent.Iads5gExceptionEventTable;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.service.exceptionevent.GisAndListShowServie;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-25 14:12
 */
@Service
public class GisAndListShowServieImpl implements GisAndListShowServie {

    @Autowired
    private NSAGisAndListShowDao nsaGisAndListShowDao;

    @Override
    public AbstractPageList pageList(PageList pageList) {
        return nsaGisAndListShowDao.getPageListOfNsa(pageList);
    }

    @Override
    public AbstractPageList pageListMove45g(PageList pageList) {
        return nsaGisAndListShowDao.getPageListOfSa45G(pageList);
    }


    @Override
    public AbstractPageList pageListMoveSwitch(PageList pageList) {
        return nsaGisAndListShowDao.getPageListOfSaSwitch(pageList);
    }

    @Override
    public AbstractPageList pageListAccess(PageList pageList) {
        return nsaGisAndListShowDao.getPageListOfSaAccess(pageList);
    }
    
    @Override
    public AbstractPageList pageListBussiness(PageList pageList) {
        return nsaGisAndListShowDao.getPageListOfBussiness(pageList);
    }

    @Override
    public Iads5gExceptionEventTable queryRecordById(String fileId) {
        return nsaGisAndListShowDao.queryRecordById(fileId);
    }

	@Override
	public List<Object> getGpsPointData(Long recSeqNo, Long timeLong, Integer timeHigh, Integer timeLow,String fileName) {
		
		return nsaGisAndListShowDao.getGpsPointData(recSeqNo,timeLong,timeHigh,timeLow,fileName);
	}

	@Override
	public List<Cell5G> getCellIdAndCellName(String nrPci, String point) {
		return nsaGisAndListShowDao.getCellIdAndCellName(nrPci,point);
	}
	
	@Override
	public List<LteCell> getLteCell(String lteFriendlyName) {
		return nsaGisAndListShowDao.getLteCell(lteFriendlyName);
	}
	
	@Override
	public List<Cell5G> getNrCell(String nrFriendlyName) {
		return nsaGisAndListShowDao.getNrCell(nrFriendlyName);
	}
}
