package com.datang.service.monitor;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;

/**
 * 终端信息Service
 * 
 * @explain
 * @name TerminalInfoService
 * @author shenyanwei
 * @date 2016年8月15日上午11:13:41
 */
public interface TerminalInfoService {
	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);
}
