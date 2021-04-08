/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.dao.testManage.terminal;

import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testManage.terminal.TestModule;

/**
 * @author dingzhongchang
 * @version 1.0.0
 */
@Repository
public class TestModuleDao extends GenericHibernateDao<TestModule, Long> {

	/**
	 * 判断此测试模块是否存在
	 * 
	 * @param groupId
	 *            测试模块
	 * @return 测试模块已存在 true,否则false
	 */
	public boolean isExist(Long testModuleId) {
		TestModule testModule = find(testModuleId);
		return testModule != null;
	}
}
