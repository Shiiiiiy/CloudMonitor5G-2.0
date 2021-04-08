package com.datang.service.exceptionevent.impl;

import com.datang.dao.exceptionevent.ExceptionEventBussinessDao;
import com.datang.service.exceptionevent.ExceptionEventBussinessService;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventBussinessDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 17:52
 */
@Service
public class ExceptionEventBussinessServiceImpl implements ExceptionEventBussinessService {
    @Autowired
    private ExceptionEventBussinessDao exceptionEventBussinessDao;

    @Override
    public void updateParams(ExceptionEventBussinessDto moveDto) {
    	exceptionEventBussinessDao.updateParams(moveDto);
    }

	@Override
	public ExceptionEventBussinessDto queryData() {
		
		return exceptionEventBussinessDao.queryData();
	}
}
