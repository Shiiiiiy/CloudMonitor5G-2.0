package com.datang.service.monitor;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;

/**
 * 硬件状态Service
 * 
 * @explain
 * @name StatusService
 * @author shenyanwei
 * @date 2016年7月12日下午1:55:14
 */

public interface StatusService {
	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);
}
