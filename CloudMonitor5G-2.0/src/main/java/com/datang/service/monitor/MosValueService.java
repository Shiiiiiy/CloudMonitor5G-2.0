package com.datang.service.monitor;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;

/**
 * Mos值Service
 * 
 * @explain
 * @name MosValueService
 * @author shenyanwei
 * @date 2016年7月12日下午1:38:02
 */
public interface MosValueService {
	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);
}
