package com.datang.service.exceptionevent.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datang.dao.exceptionevent.ExceptionEventASAAccessDao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.service.exceptionevent.ExceptionEventASAAccessService;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:29
 */
@Service
public class ExceptionEventASAAccessServiceImpl implements ExceptionEventASAAccessService {

    @Autowired
    private ExceptionEventASAAccessDao accessDao;


    @Override
    public void updateParams(ExceptionEventASAAccessDto eventDto) {
        accessDao.updateParams(eventDto);
    }


	@Override
	public ExceptionEventASAAccessDto queryData() {
		 return accessDao.queryData();
	}
}
