package com.datang.service.exceptionevent.impl;

import com.datang.dao.exceptionevent.ExceptionEvent45gDao;
import com.datang.dao.exceptionevent.ExceptionEventSAMoveDao;
import com.datang.service.exceptionevent.ExceptionEvent45gService;
import com.datang.service.exceptionevent.ExceptionEventSAMoveService;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 17:52
 */
@Service
public class ExceptionEvent45gServiceImpl implements ExceptionEvent45gService {
    @Autowired
    private ExceptionEvent45gDao exceptionEvent45gDao;

    @Override
    public void updateDataOfExceptionEventSAMoveparams(ExceptionEvent45gDto moveDto) {
    	exceptionEvent45gDao.updateDataOfExceptionEventSAMoveparams(moveDto);
    }

	@Override
	public ExceptionEvent45gDto queryData() {
		
		return exceptionEvent45gDao.queryData();
	}
}
