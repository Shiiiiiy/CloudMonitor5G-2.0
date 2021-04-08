package com.datang.service.report.impl;

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
import com.datang.dao.report.CQTStatisticeTaskDao;
import com.datang.dao.testManage.terminal.TerminalGroupDao;
import com.datang.dao.testManage.terminal.TerminalMenuDao;
import com.datang.domain.report.CQTStatisticeTask;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.report.ICQTReportService;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * CQT统计任务Service实现
 * 
 * @explain
 * @name CQTReportServiceImpl
 * @author shenyanwei
 * @date 2016年10月26日上午10:51:55
 */
@Service
@Transactional
@SuppressWarnings("all")
public class CQTReportServiceImpl implements ICQTReportService {
	protected SqlService sqlService;
	@Autowired
	private CQTStatisticeTaskDao statisticeTaskDao;
	/**
	 * 终端分组
	 */
	@Autowired
	private TerminalGroupDao groupDao = null;
	@Autowired
	private TerminalMenuDao terminalMenuDao = null;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = statisticeTaskDao.getPageItem(pageList);
		List rows = pageItem.getRows();
		for (Object object : rows) {
			CQTStatisticeTask statisticeTask = (CQTStatisticeTask) object;
			String[] cityIds = statisticeTask.getCityIds().trim().split(",");
			// 存储TestLogItem的id集合
			List<String> ids = new ArrayList<>();
			for (int i = 0; i < cityIds.length; i++) {
				if (StringUtils.hasText(cityIds[i])) {
					try {
						ids.add(cityIds[i].trim());
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}

			if (ids.size() > 1) {
				HashSet<String> nameSet = new HashSet<String>();
				for (String cityId : ids) {
					if (cityId == null) {
						continue;
					} else {
						Long id = Long.valueOf(cityId);
						TerminalMenu cMenu = terminalMenuDao.queryMenuByRef(id);// 获取父菜单
						TerminalMenu pMenu = terminalMenuDao.find(cMenu
								.getPid());
						if (pMenu != null
								&& pMenu.getType().equals(
										MenuType.Province.getMenuType())) {
							nameSet.add(pMenu.getName());
						}
					}
				}
				if (nameSet.size() > 1) {
					statisticeTask.setTerminalGroup("全国");
				} else {
					statisticeTask
							.setTerminalGroup((String) nameSet.toArray()[0]);
				}
			} else {
				Long id = Long.valueOf(ids.get(0));
				TerminalGroup terminalGroup = groupDao.find(id);
				if (terminalGroup != null) {
					statisticeTask.setTerminalGroup(terminalGroup.getName());
				}
			}
		}
		return pageItem;

	}

	@Override
	public void save(CQTStatisticeTask statisticeTask) {
		statisticeTaskDao.create(statisticeTask);
	}

	@Override
	public void delete(List<Long> ids) {
		statisticeTaskDao.deleteList(ids);

	}

	@Override
	public CQTStatisticeTask queryOneByID(Long id) {

		return statisticeTaskDao.find(id);
	}

	@Override
	public void update(CQTStatisticeTask statisticeTask) {
		statisticeTaskDao.update(statisticeTask);

	}

	@Override
	public AbstractPageList queryKpi(VoLTEWholePreviewParam inputParam) {
		if (null == inputParam
				|| !StringUtils.hasText(inputParam.getTestLogItemIds())) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		PageListArray<Object[]> result = new PageListArray<Object[]>();
		System.out.println(inputParam);
		result = sqlService.parse(inputParam);
		System.out.println(sqlService.getSql());
		List<Object[]> datas = result.getDatas();
		List rows = easyuiPageList.getRows();
		// List<Map<String, Object>> list = new ArrayList<>();
		for (Object[] objects : datas) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			List<String> fields = result.getFields();
			for (String string : fields) {
				int index = result.getIndex(string);
				map.put(string, objects[index]);
			}
			// list.add(map);
			rows.add(map);
		}
		// easyuiPageList.setRows(list);
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService#exportKpi
	 * (
	 * com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam
	 * )
	 */
	@Override
	public AbstractPageList exportKpi(VoLTEWholePreviewParam inputParam) {
		return queryKpi(inputParam);
	}

	/**
	 * @return the sqlService
	 */
	public SqlService getSqlService() {
		return sqlService;
	}

	@Resource(name = "voLTEDissWholePreviewSqlServiceBean")
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

}
