package com.datang.service.monitor;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.monitor.ATULoginLogItem;
import com.datang.domain.testManage.terminal.Terminal;

/**
 * ATU登陆监控Service接口
 * 
 * @explain
 * @name ATULoginService
 * @author shenyanwei
 * @date 2016年7月11日下午2:27:54
 */
public interface ATULoginService {
	/**
	 * 增加终端登录日志记录
	 * 
	 * @param atuItem
	 *            日志记录
	 */
	public void addATULoginLog(ATULoginLogItem atuItem);

	/**
	 * 删除终端登录日志记录
	 * 
	 * @param atuItem
	 *            日志记录
	 */
	public void deleteATULoginLog(ATULoginLogItem atuItem);

	/**
	 * 更新终端登录日志记录
	 * 
	 * @param atuItem
	 *            日志记录
	 */
	public void updateATULoginLog(ATULoginLogItem atuItem);

	/**
	 * 更新终端登录日志记录
	 * 
	 * @param id
	 *            日志记录的主键值
	 * @return 日志记录
	 */
	public ATULoginLogItem queryATULoginLog(Long id);

	/**
	 * 
	 * 获取指定终端的离指定时间最近的登录记录
	 * 
	 * @param terminal
	 *            终端实体
	 * @param currentDate
	 *            指定的时间
	 * @return 找到的登录记录
	 */
	public ATULoginLogItem queryNearestATULoginItem(Terminal terminal,
			Date currentDate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.monitor.ATULoginService#queryNearestATULoginItem
	 * (com.datang.adc.domain.terminal.Terminal, java.util.Date)
	 */

	public ATULoginLogItem queryNearestATULoginItem(Long terminalId,
			Date currentDate);

	/**
	 * 
	 * 获取指定终端的离指定时间最近的登录记录
	 * 
	 * @param terminal
	 *            终端实体
	 * @param currentDate
	 *            指定的时间
	 * @return 找到的登录记录
	 */
	public ATULoginLogItem queryNearestATULoginItem(String boxId,
			Date currentDate);

	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);

	/**
	 * 实时刷新ATU终端登录日志记录
	 * 
	 * @param terminalName
	 * @param maxLogNum
	 * @return
	 */
	public List<ATULoginLogItem> refreshATULoginItems(String terminalName,
			String maxLogNum, List<Long> managedTerminalIDs);

	/**
	 * 根据时间查询有登录信息的BoxID
	 */
	public Set<String> queryGpsByTime(Date begainTime, Date endTime);

	/**
	 * 查询最近一段时间内的对应终端是否活跃
	 * @return
	 */
	public ATULoginLogItem getActiveNUm(Date pastDate,String id);
}
