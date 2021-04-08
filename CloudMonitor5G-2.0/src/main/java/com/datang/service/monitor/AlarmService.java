package com.datang.service.monitor;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;

/**
 * 警告Service
 * 
 * @explain
 * @name AlarmService
 * @author shenyanwei
 * @date 2016年7月12日下午1:36:00
 */
public interface AlarmService {
	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);
}
