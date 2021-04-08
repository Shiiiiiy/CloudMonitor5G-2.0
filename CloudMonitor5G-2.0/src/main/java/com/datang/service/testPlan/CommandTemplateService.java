/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testPlan;

import java.util.Collection;

import com.datang.domain.testPlan.CommandTemplate;
import com.datang.web.beans.testPlan.CommandTemplateQuery;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-28
 */
public interface CommandTemplateService {
	/**
	 * 添加命令模版 ???
	 * 
	 * @param commandTemplate
	 *            CommandTemplate
	 */
	public void addCommandTemplate(CommandTemplate commandTemplate);

	/**
	 * 通过id删除命令模版
	 * 
	 * @param id
	 *            命令模版id
	 */
	public void deleteCommandTemplate(Integer id);

	/**
	 * 更新命令模版
	 * 
	 * @param commandTemplate
	 *            CommandTemplate
	 */
	public void updateCommandTemplate(CommandTemplate commandTemplate);

	/**
	 * 通过id查询命令模版
	 * 
	 * @param id
	 *            id
	 * @return 命令模版对象
	 */
	public CommandTemplate getCommandTemplateById(Integer id);

	/**
	 * 通过模版名称得到模版对象
	 * 
	 * @param name
	 *            模版名称
	 * @return 命令模版对象
	 */
	public CommandTemplate getCommandTemplateByName(String name);

	/**
	 * 按条件查询CommandTemplate
	 * 
	 * @param commandTemplateQuery
	 *            CommandTemplateQuery
	 * @return 查询到的CommandTemplate集合
	 */
	public Collection<CommandTemplate> queryList(
			CommandTemplateQuery commandTemplateQuery);

}
