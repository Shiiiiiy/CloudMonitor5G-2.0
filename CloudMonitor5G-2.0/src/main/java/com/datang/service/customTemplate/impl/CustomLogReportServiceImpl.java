package com.datang.service.customTemplate.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.entity.PageListArray;
import com.datang.common.service.sql.SqlService;
import com.datang.common.util.StringUtils;
import com.datang.dao.customTemplate.CustomLogReportTaskDao;
import com.datang.dao.report.StatisticeTaskDao;
import com.datang.dao.testManage.terminal.TerminalGroupDao;
import com.datang.dao.testManage.terminal.TerminalMenuDao;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.customTemplate.CustomLogReportService;
import com.datang.service.report.IReportService;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * 自定义日志统计任务Service实现
 * @explain
 * @name CustomLogReportServiceImpl
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CustomLogReportServiceImpl implements CustomLogReportService {
	protected SqlService sqlService;
	@Autowired
	private CustomLogReportTaskDao customLogReportTaskDao;
	@Autowired
	private TerminalMenuDao terminalMenuDao = null;
	/**
	 * 终端分组
	 */
	@Autowired
	private TerminalGroupDao groupDao = null;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = customLogReportTaskDao.getPageItem(pageList);
		return pageItem;

	}

	@Override
	public void save(CustomLogReportTask customLogReportTask) {
		customLogReportTaskDao.create(customLogReportTask);
	}

	@Override
	public void delete(List<Long> ids) {
		customLogReportTaskDao.deleteList(ids);

	}

	@Override
	public CustomLogReportTask queryOneByID(Long id) {

		return customLogReportTaskDao.find(id);
	}
	
	@Override
	public List<CustomLogReportTask> queryTaskByIds(List<Long> ids) {
		List<CustomLogReportTask> list = new ArrayList<CustomLogReportTask>();
		for (Long id : ids) {
			CustomLogReportTask task = customLogReportTaskDao.find(id);
			list.add(task);
		}
		return list;
	}

	@Override
	public void update(CustomLogReportTask customLogReportTask) {
		customLogReportTaskDao.update(customLogReportTask);

	}
	
	@Override
	public List<CustomLogReportTask> findStatisticeTask(PageList pageList){
		return customLogReportTaskDao.findStatisticeTask(pageList);
	}

}
