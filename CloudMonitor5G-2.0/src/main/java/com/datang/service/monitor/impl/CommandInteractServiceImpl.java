package com.datang.service.monitor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.CommandInteractDao;
import com.datang.domain.monitor.CommandInteract;
import com.datang.domain.monitor.MonitorEntity;
import com.datang.service.monitor.CommandInteractService;

/**
 * 命令交互Service实现
 * 
 * @explain
 * @name CommandInteractServiceImpl
 * @author shenyanwei
 * @date 2016年7月11日下午2:32:17
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CommandInteractServiceImpl implements CommandInteractService {
	@Autowired
	private CommandInteractDao commandInteractDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.CommandInteractService#addCIRecord(com
	 * .datang.adc.domain.monitor.CommandInteract)
	 */
	@Override
	public void addCIRecord(CommandInteract ciRecord) {
		commandInteractDao.addCIRecord(ciRecord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.CommandInteractService#deleteCIRecord(
	 * com.datang.adc.domain.monitor.CommandInteract)
	 */
	@Override
	public void deleteCIRecord(CommandInteract ciRecord) {
		commandInteractDao.deleteCIRecord(ciRecord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.CommandInteractService#updateCIRecord(
	 * com.datang.adc.domain.monitor.CommandInteract)
	 */
	@Override
	public void updateCIRecord(CommandInteract ciRecord) {
		commandInteractDao.updateCIRecord(ciRecord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.CommandInteractService#queryCIRecord(java
	 * .lang.Integer)
	 */
	@Override
	public CommandInteract queryCIRecord(Integer id) {
		// return commandInteractDao.queryCIRecord(id);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.monitor.CommandInteractService#
	 * deleteCommandInteractByID(java.lang.Long)
	 */
	@Override
	public void deleteCommandInteractByID(Long terminalId) {
		commandInteractDao.deleteCommandInteractByID(terminalId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.CommandInteractService#refreshCommandInteract
	 * (java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<MonitorEntity> refreshCommandInteract(String terminalName,
			String maxLogNum, List<Long> managedTerminalIDs) {
		return commandInteractDao.refreshCommandInteract(terminalName,
				maxLogNum, managedTerminalIDs);
	}

	@Override
	public AbstractPageList pageList(PageList pageList) {

		return commandInteractDao.getPageItem(pageList);
	}

}
