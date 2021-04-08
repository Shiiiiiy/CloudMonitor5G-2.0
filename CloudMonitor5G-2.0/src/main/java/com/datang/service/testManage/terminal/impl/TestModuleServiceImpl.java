/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.datang.common.util.CollectionUtils;
import com.datang.dao.testManage.terminal.TestModuleDao;
import com.datang.domain.testManage.terminal.TestModule;
import com.datang.service.testManage.terminal.TestModuleService;

/**
 * @author dingzhongchang
 * @version 1.0.0
 */
@Service
@Transactional
public class TestModuleServiceImpl implements TestModuleService {
	@Autowired
	private TestModuleDao moduleDao = null;

	/**
	 * 
	 * @param group
	 * @see com.datang.adc.service.terminal.TerminalGroupService#addGroup(com.datang.adc.domain.terminal.TerminalGroup)
	 */
	@Override
	public void addModule(TestModule module) {
		Assert.notNull(module, "arg module can't be Null");
		// Collection<TestModule> list = moduleDao.findAll();
		// if (!CollectionUtils.isEmpty(list)) {
		// for (TestModule m : list) {
		// if (m.equals(module)) {
		// throw new ApplicationException("TestModule.error.Exist",
		// "TestModule");
		// }
		// }
		// }
		moduleDao.create(module);
	}

	/**
	 * @param moduleId
	 * @see com.datang.adc.service.terminal.TestModuleService#deletemodule(java.lang.Long)
	 */
	@Override
	public void deleteModule(Long moduleId) {
		Assert.notNull(moduleId, "arg moduleId can't be Null");
		if (!moduleDao.isExist(moduleId)) {
			// yzp
			// throw new ApplicationException("TestModule.error.NotFoundmodule",
			// "TestModule");
		}
		moduleDao.delete(moduleId);
	}

	/**
	 * @param module
	 * @see com.datang.adc.service.terminal.TestModuleService#updatemodule(com.datang.adc.domain.terminal.TestModule)
	 */
	@Override
	public void updateModule(TestModule module) {
		Assert.notNull(module, "arg module can't be Null");
		if (!moduleDao.isExist(module.getId())) {
			// yzp
			// throw new ApplicationException("TestModule.error.NotFoundmodule",
			// "TestModule");
		}
		moduleDao.update(module);
	}

	@Override
	public List<TestModule> getTestModules(Long... ids) {
		List<TestModule> result = new LinkedList<TestModule>();
		if (!CollectionUtils.isEmpty(Arrays.asList(ids))) {
			for (Long id : ids) {
				result.add(moduleDao.find(id));
			}

		}
		return result;
	}

	/**
	 * @param moduleDao
	 *            the moduleDao to set
	 */
	public void setModuleDao(TestModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	/**
	 * @return the moduleDao
	 */
	public TestModuleDao getModuleDao() {
		return moduleDao;
	}

}
