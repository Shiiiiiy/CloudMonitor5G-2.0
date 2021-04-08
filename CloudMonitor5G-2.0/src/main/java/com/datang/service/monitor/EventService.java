package com.datang.service.monitor;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;

/**
 * 事件Service
 * 
 * @explain
 * @name EventService
 * @author shenyanwei
 * @date 2016年7月12日下午1:37:18
 */
public interface EventService {
	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);
}
