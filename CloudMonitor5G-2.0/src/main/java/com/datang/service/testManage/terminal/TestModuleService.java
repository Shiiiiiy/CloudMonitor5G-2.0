/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal;

import java.util.List;

import com.datang.domain.testManage.terminal.TestModule;

/**
 * 终端模块
 * 
 * @author dingzhongchang
 * @version 1.0.0
 */
public interface TestModuleService {

	/**
	 * 添加
	 * 
	 * @param module
	 *            终端模块
	 */
	void addModule(TestModule module);

	/**
	 * 删除
	 * 
	 * @param moduleId
	 *            终端模块ID
	 */
	void deleteModule(Long moduleId);

	/**
	 * 更新
	 * 
	 * @param module
	 *            终端模块
	 */
	void updateModule(TestModule module);

	/**
	 * 查询 终端模块
	 * 
	 * @param ids
	 *            终端模块ID
	 * @return 终端模块
	 */
	List<TestModule> getTestModules(Long... ids);

}
