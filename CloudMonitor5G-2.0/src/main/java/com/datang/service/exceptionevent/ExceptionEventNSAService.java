package com.datang.service.exceptionevent;

import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventNSADto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:44
 */
public interface ExceptionEventNSAService {
    void updatePrams(ExceptionEventNSADto eventNSADto);

    /**
     * 根据id查询数据
     * @author maxuancheng
     * date:2019年11月28日 下午4:13:47
     * @param l
     * @return
     */
	public ExceptionEventNSADto queryData();
}
