package com.datang.service.monitor;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.monitor.CommandInteract;
import com.datang.domain.monitor.MonitorEntity;

/**
 * 命令交互监控Service接口
 * 
 * @explain
 * @name CommandInteractService
 * @author shenyanwei
 * @date 2016年7月11日下午2:26:58
 */
public interface CommandInteractService {
	/**
	 * 
	 * @param ciRecord
	 */
	public void addCIRecord(CommandInteract ciRecord);

	/**
	 * 
	 * @param ciRecord
	 */
	public void updateCIRecord(CommandInteract ciRecord);

	/**
	 * 
	 * @param ciRecord
	 */
	public void deleteCIRecord(CommandInteract ciRecord);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public CommandInteract queryCIRecord(Integer id);

	/**
	 * 
	 * @param terminalId
	 */
	public void deleteCommandInteractByID(Long terminalId);

	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);

	/**
	 * 
	 * @param entityClass
	 * @param terminalName
	 * @param maxLogNum
	 * @return
	 */
	public List<MonitorEntity> refreshCommandInteract(String terminalName,
			String maxLogNum, List<Long> managedTerminalIDs);
}
