package com.datang.service.exceptionevent.impl;

import com.datang.dao.exceptionevent.ExceptionEventNSADao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.service.exceptionevent.ExceptionEventNSAService;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventNSADto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:45
 */
@Service
public class ExceptionEventNSAServiceImpl implements ExceptionEventNSAService {
    @Autowired
    private ExceptionEventNSADao nsaDao;

    @Override
    public void updatePrams(ExceptionEventNSADto eventNSADto) {
        nsaDao.updatePrams(eventNSADto);
    }

	@Override
	public ExceptionEventNSADto queryData() {
		return nsaDao.queryData();
	}
}
