/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datang.bean.testManage.terminal.TerminalQuery;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.HistoryTestPlan;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testPlan.TestPlan;

/**
 * 自动路测终端服务类
 * <p>
 * 增加 {@link #addTerminal(Terminal)}<br>
 * 删除 {@link #deleteTerminal(Long...)}<br>
 * 更新 {@link #updateTerminal(Terminal)}<br>
 * 查询 {@link #getAllTerminals()} {@link #getTerminal(String...)}
 * {@link #pageQueryTerminals(TerminalQuery)}<br>
 * 将终端加入分组中 {@link #pushInGroup(String, String[])}<br>
 * 将终端从分组中删除 {@link #popOutGroup(String[], String)}<br>
 * 发送测试计划 {@link #sendTestPlans(String, Integer[])}
 * {@link #sendTestPlans(String[], Integer[])}<br>
 * 
 * @author dingzhongchang
 * @version 1.0.0
 */
public interface TerminalService {

	/**
	 * 新增终端，并添加菜单信息
	 * 
	 * @param terminal
	 * @param refId
	 * @return
	 */
	TerminalMenu addTerminal(Terminal terminal, Long refId);

	/**
	 * 查询当前运行的测试计划
	 * 
	 * @param teId
	 *            终端的ID
	 */
	TestPlan getRunningTestPlan(Long teId);

	/**
	 * 获取当前的历史计划
	 * 
	 * @return HistoryTestPlan
	 */
	HistoryTestPlan getCurrentHistoryPlan(Long teId);

	/**
	 * 获取历史测试计划
	 * 
	 * @param teId
	 *            终端ID
	 * @param testPlanId
	 *            计划的ID
	 * @return 历史测试计划
	 */
	HistoryTestPlan getHisTestPlan(Long teId, Integer testPlanId);

	/**
	 * 获取历史测试计划
	 * 
	 * @param teId
	 *            终端ID
	 * @return 历史测试计划
	 */
	// yzp
	// Collection<HistoryTestPlanBean> getHisTestPlanBeans(Long teId);

	/**
	 * 查询
	 * 
	 * @return Collection<Terminal>
	 */
	List<Terminal> getAllTerminals();

	/**
	 * 查询
	 * 
	 * @param boxIds
	 *            终端名称
	 * @return 终端
	 */
	List<Terminal> getTerminals(String... boxIds);

	/**
	 * 查询
	 * 
	 * @param boxId
	 *            终端名称
	 * @return 终端
	 */
	Terminal getTerminal(String boxId);

	/**
	 * 查询
	 * 
	 * @param ids
	 *            终端ID
	 * @return 终端
	 */
	List<Terminal> getTerminals(Long... ids);

	/**
	 * 查询
	 * 
	 * @param ids
	 *            终端ID
	 * @param enable
	 *            是否可用
	 * @return 终端
	 */
	Collection<Terminal> getTerminals(Collection<Long> ids, boolean enable);

	/**
	 * 获取所有终端信息
	 * 
	 * @param menus
	 *            List<TerminalMenu>
	 * @return List<Terminal>
	 */
	List<Terminal> getTerminalsByMenus(List<TerminalMenu> menus);

	/**
	 * 查询
	 * 
	 * @param ids
	 *            终端ID
	 * @param enable
	 *            是否可用
	 * @param vender
	 *            厂商
	 * @return 终端
	 */
	Collection<Terminal> getTerminals(Collection<Long> ids, boolean enable,
			String vender);

	/**
	 * 查询
	 * 
	 * @param id
	 *            终端ID
	 * @return 终端
	 */
	Terminal getTerminal(Long id);
	
	/**
	 * 根据id只获取终端，不包含testModuls
	 * @param id
	 * @return
	 */
	Terminal getTerminalType(Long id);

	/**
	 * 分页查询
	 * 
	 * @param query
	 *            终端分页查询条件
	 * @return 终端列表
	 */
	List<Terminal> pageQueryTerminals(TerminalQuery query);

	/**
	 * 添加
	 * 
	 * @param te
	 *            自动路测终端
	 */
	void addTerminal(Terminal te);

	/**
	 * 删除
	 * 
	 * @param boxIds
	 *            自动路测终端
	 */
	void deleteTerminal(Long... ids);

	/**
	 * 更新
	 * 
	 * @param te
	 *            自动路测终端
	 */
	void updateTerminal(Terminal te);

	/**
	 * 更新车载终端属组
	 * 
	 * @param groupId
	 *            属组
	 * @param teIds
	 *            终端列表
	 */
	void updateGroup(Long groupId, Long... teIds);

	/**
	 * 模糊查询该终端下的历史计划
	 * 
	 * @param terminalName
	 *            终端名称
	 * @return 历史计划ID列表
	 */
	Set<Integer> queryTestPlanIdsByName(String terminalName);

	/**
	 * 导入终端列表
	 * 
	 * @param path
	 *            终端文件所在绝对路径
	 * @return 终端列表
	 */
	// yzp
	// ResultSet<Terminal> importTermianls(String path, Long groupId);

	/**
	 * 导出终端列表
	 * 
	 * @param path
	 *            终端文件所在绝对路径
	 * @param terminals
	 *            终端列表
	 */
	void exportTermianls(String path, List<Terminal> terminals);

	/**
	 * 多条件组合分页
	 * 
	 * @author yinzhipeng
	 * @param pageList
	 * @return
	 */
	public AbstractPageList queryPageTerminal(PageList pageList);

	/**
	 * 导入终端表
	 * 
	 * @param xlsFile
	 * @return
	 */
	public Map<Integer, Object> importTerminal(File xlsFile);

	/**
	 * 改变终端状态
	 * @author maxuancheng
	 * @param id
	 * @param b 状态 true 开启  false 禁用
	 * @return 
	 */
	public boolean updateTerminalEnable(Long id, boolean b);
	
	/**
	 * 根据条件查询数据
	 * @author maxuancheng
	 * date:2020年3月6日 上午10:34:48
	 * @param param
	 * @return
	 */
	public List<Terminal> findByParam(HashMap<String, Object> param);


}
