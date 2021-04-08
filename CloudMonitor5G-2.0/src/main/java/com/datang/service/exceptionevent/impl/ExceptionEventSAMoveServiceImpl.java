package com.datang.service.exceptionevent.impl;

import com.datang.dao.exceptionevent.ExceptionEventSAMoveDao;
import com.datang.service.exceptionevent.ExceptionEventSAMoveService;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 17:52
 */
@Service
public class ExceptionEventSAMoveServiceImpl implements ExceptionEventSAMoveService {
    @Autowired
    private ExceptionEventSAMoveDao exceptionEventSAMoveDao;

    @Override
    public void updateDataOfExceptionEventSAMoveparams(ExceptionEventASAMoveDto moveDto) {
        exceptionEventSAMoveDao.updateDataOfExceptionEventSAMoveparams(moveDto);
    }

	@Override
	public ExceptionEventASAMoveDto queryData() {
		
		return exceptionEventSAMoveDao.queryData();
	}
}
