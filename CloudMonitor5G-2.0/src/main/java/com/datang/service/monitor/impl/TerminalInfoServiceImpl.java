package com.datang.service.monitor.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.TerminalInfoDao;
import com.datang.domain.monitor.TerminalInfo;
import com.datang.service.monitor.TerminalInfoService;
import com.datang.util.NumberFormatUtils;

/**
 * @explain
 * @name TerminalInfoServiceImpl
 * @author shenyanwei
 * @date 2016年8月15日上午11:21:12
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TerminalInfoServiceImpl implements TerminalInfoService {
	/**
    *
    */
	@Autowired
	private TerminalInfoDao terminalInfoDao;

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = terminalInfoDao.getPageItem(pageList);
		List rows = pageItem.getRows();
		for (Object object : rows) {
			TerminalInfo terminalInfo = (TerminalInfo) object;
			terminalInfo.setCpu(NumberFormatUtils.format(terminalInfo.getCpu(),
					2));
			terminalInfo.setMemory(NumberFormatUtils.format(
					terminalInfo.getMemory(), 2));
		}
		return terminalInfoDao.getPageItem(pageList);

	}

}
