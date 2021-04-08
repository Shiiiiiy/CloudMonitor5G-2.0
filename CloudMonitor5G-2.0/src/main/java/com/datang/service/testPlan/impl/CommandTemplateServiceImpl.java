package com.datang.service.testPlan.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.testPlan.CommandTemplateDao;
import com.datang.domain.testPlan.CommandTemplate;
import com.datang.exception.ApplicationException;
import com.datang.service.testPlan.CommandTemplateService;
import com.datang.web.beans.testPlan.CommandTemplateQuery;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-28
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CommandTemplateServiceImpl implements CommandTemplateService {
	@Autowired
	private CommandTemplateDao commandTemplateDao;

	/*
	 * (non-Javadoc) 引用
	 * 
	 * @see
	 * com.datang.adc.service.testPlan.CommandTemplateService#addCommandTemplate
	 * (com.datang.adc.domain.testPlan.CommandTemplate)
	 */
	@Override
	public void addCommandTemplate(CommandTemplate commandTemplate) {

		if (this.commandTemplateDao.getCommandTemplateByName(commandTemplate
				.getName()) != null) {
			throw new ApplicationException("命令模版名称已存在");
		} else {
			this.commandTemplateDao.create(commandTemplate);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.testPlan.CommandTemplateService#deleteCommandTemplate
	 * (java.lang.Integer)
	 */
	@Override
	public void deleteCommandTemplate(Integer id) {

		this.deleteCommandTemplate(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.testPlan.CommandTemplateService#getCommandTemplateById
	 * (java.lang.Integer)
	 */
	@Override
	public CommandTemplate getCommandTemplateById(Integer id) {
		return this.commandTemplateDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.testPlan.CommandTemplateService#
	 * getCommandTemplateByName(java.lang.String)
	 */
	@Override
	public CommandTemplate getCommandTemplateByName(String name) {
		return this.commandTemplateDao.getCommandTemplateByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.testPlan.CommandTemplateService#updateCommandTemplate
	 * (com.datang.adc.domain.testPlan.CommandTemplate)
	 */
	@Override
	public void updateCommandTemplate(CommandTemplate commandTemplate) {
		this.commandTemplateDao.update(commandTemplate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.testPlan.CommandTemplateService#queryList(com.
	 * datang.adc.beans.testPlan.CommandTemplateQuery)
	 */
	@Override
	public Collection<CommandTemplate> queryList(
			CommandTemplateQuery commandTemplateQuery) {
		return this.queryList(commandTemplateQuery);
	}

}
