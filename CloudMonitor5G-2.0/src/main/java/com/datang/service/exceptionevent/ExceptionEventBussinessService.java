package com.datang.service.exceptionevent;

import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventBussinessDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 17:44
 */
public interface ExceptionEventBussinessService {
    void updateParams(ExceptionEventBussinessDto map);

    /**
     * 获取数据
     * @return
     */
	public ExceptionEventBussinessDto queryData();
}
