package com.datang.service.monitor.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.monitor.ATULoginLogItemBean;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.monitor.ATULogDao;
import com.datang.domain.monitor.ATULoginLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.service.monitor.ATULoginService;

/**
 * ATU登陆Service实现
 * 
 * @explain
 * @name ATULoginServiceImpl
 * @author shenyanwei
 * @date 2016年7月11日下午2:32:50
 */
@Service
@Transactional
@SuppressWarnings("all")
public class ATULoginServiceImpl implements ATULoginService {
	@Autowired
	private ATULogDao atuLoginDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#addATULoginLog(com.datang
	 * .adc.domain.monitor.ATULoginLogItem)
	 */
	@Override
	public void addATULoginLog(ATULoginLogItem atuItem) {
		atuLoginDao.addATULoginLog(atuItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#deleteATULoginLog(com.
	 * datang.adc.domain.monitor.ATULoginLogItem)
	 */
	@Override
	public void deleteATULoginLog(ATULoginLogItem atuItem) {
		atuLoginDao.deleteATULoginLog(atuItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#queryATULoginLog(java.
	 * lang.Long)
	 */
	@Override
	public ATULoginLogItem queryATULoginLog(Long id) {
		return atuLoginDao.queryATULoginLog(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#queryNearestATULoginItem
	 * (com.datang.adc.domain.terminal.Terminal, java.util.Date)
	 */
	@Override
	public ATULoginLogItem queryNearestATULoginItem(Terminal terminal,
			Date currentDate) {
		return atuLoginDao.queryNearestATULoginItem(terminal, currentDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#queryNearestATULoginItem
	 * (com.datang.adc.domain.terminal.Terminal, java.util.Date)
	 */
	@Override
	public ATULoginLogItem queryNearestATULoginItem(Long terminalId,
			Date currentDate) {
		return atuLoginDao.queryNearestATULoginItem(terminalId, currentDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#queryNearestATULoginItem
	 * (java.lang.String, java.util.Date)
	 */
	@Override
	public ATULoginLogItem queryNearestATULoginItem(String boxId,
			Date currentDate) {
		return atuLoginDao.queryNearestATULoginItem(boxId, currentDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#updateATULoginLog(com.
	 * datang.adc.domain.monitor.ATULoginLogItem)
	 */
	@Override
	public void updateATULoginLog(ATULoginLogItem atuItem) {
		atuLoginDao.updateATULoginLog(atuItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#queryATULoginItems(com
	 * .datang.adc.beans.monitor.ATULoginQuery, java.util.List)
	 */
	@Override
	public ATULoginLogItem getActiveNUm(Date pastDate,String id){
		return atuLoginDao.getActiveNUm(pastDate,id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#refreshATULoginItems(java
	 * .lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<ATULoginLogItem> refreshATULoginItems(String terminalName,
			String maxLogNum, List<Long> managedTerminalIDs) {
		return atuLoginDao.refreshATULoginItems(terminalName, maxLogNum,
				managedTerminalIDs);
	}

	@Override
	public AbstractPageList pageList(PageList pageList) {
		EasyuiPageList pageItem = atuLoginDao.getPageItem(pageList);
		ArrayList<ATULoginLogItemBean> list = new ArrayList<ATULoginLogItemBean>();
		List rows = pageItem.getRows();
		for (Object object : rows) {
			ATULoginLogItem atuLoginLogItem = (ATULoginLogItem) object;
			ATULoginLogItemBean atuLoginLogItemBean = new ATULoginLogItemBean();
			atuLoginLogItemBean.setBoxId(atuLoginLogItem.getBoxId());
			atuLoginLogItemBean.setFailReason(getFailReason(atuLoginLogItem
					.getFailReason()));
			atuLoginLogItemBean.setLoginTime(atuLoginLogItem.getLoginTime());
			if (0 != atuLoginLogItem.getOfflineTime().getTime()) {
				atuLoginLogItemBean.setOfflineTime(atuLoginLogItem
						.getOfflineTime());
			}

			atuLoginLogItemBean.setSessionId(String.valueOf(atuLoginLogItem
					.getSessionId()));
			atuLoginLogItemBean
					.setStatus(getStatus(atuLoginLogItem.getStatus()));
			atuLoginLogItemBean.setTestPlanVersion(String
					.valueOf(atuLoginLogItem.getTestPlanVersion()));
			list.add(atuLoginLogItemBean);
		}

		pageItem.setRows(list);
		return pageItem;

	}

	@Override
	public Set<String> queryGpsByTime(Date begainTime, Date endTime) {
		List<ATULoginLogItem> queryHistoryTerminalGpsPoint = atuLoginDao
				.queryByTimeAndBoxId(begainTime, endTime, null);
		Set<String> set = new HashSet<>();
		if (null != queryHistoryTerminalGpsPoint
				&& 0 != queryHistoryTerminalGpsPoint.size()) {
			for (Object obj : queryHistoryTerminalGpsPoint) {
				ATULoginLogItem atuLoginLogItem = (ATULoginLogItem) obj;
				set.add(atuLoginLogItem.getBoxId());
			}
		}
		return set;
	}

	private String getFailReason(Integer number) {
		if (number == 0) {
			return "成功，没有错误";
		} else if (number == 1) {
			return "配置错误";
		} else if (number == 2) {
			return "终端ID非法";
		} else if (number == 3) {
			return "密码错误";
		} else if (number == 4) {
			return "已经登录";
		} else if (number == 5) {
			return "终端未登录";
		} else if (number == 6) {
			return "未知的指令";
		} else if (number == 7) {
			return "升级文件丢失";
		} else if (number == 8) {
			return "无效数据";
		} else if (number == 9) {
			return "无效数据包";
		} else if (number == 10) {
			return "打开文件失败";
		} else if (number == 11) {
			return "关闭文件失败";
		} else if (number == 12) {
			return "测试配置错误";
		} else if (number == 100) {
			return "超时离线";
		} else {
			return "未知错误";
		}
	}

	/**
	 * 0 登录成功 1 登录失败 2 正常退出 3 退出异常
	 * 
	 * @param number
	 * @return
	 */
	private String getStatus(Integer number) {
		if (number == 0) {
			return "登录成功";
		} else if (number == 1) {
			return "登录失败";
		} else if (number == 2) {
			return "正常退出";
		} else if (number == 3) {
			return "退出异常";
		} else {
			return "未知状态";
		}
	}

}
