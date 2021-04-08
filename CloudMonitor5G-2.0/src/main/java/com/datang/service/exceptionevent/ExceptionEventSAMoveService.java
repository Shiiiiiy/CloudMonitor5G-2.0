package com.datang.service.exceptionevent;

import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 17:44
 */
public interface ExceptionEventSAMoveService {
    void updateDataOfExceptionEventSAMoveparams(ExceptionEventASAMoveDto map);

    /**
     * 获取数据
     * @author maxuancheng
     * date:2019年11月26日 下午4:10:31
     * @return
     */
	public ExceptionEventASAMoveDto queryData();
}
