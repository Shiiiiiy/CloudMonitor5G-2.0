package com.datang.service.exceptionevent;

import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:27
 */
public interface ExceptionEventASAAccessService {
    void updateParams(ExceptionEventASAAccessDto eventDto);

    /**
     * 
     * @author maxuancheng
     * date:2019年11月28日 下午3:45:21
     * @param l
     * @return
     */
	public ExceptionEventASAAccessDto queryData();
}
