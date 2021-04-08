package com.datang.service.testManage.terminal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datang.bean.testManage.terminal.TerminalGroupInfo;
import com.datang.bean.testManage.terminal.TerminalGroupQuery;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;

/**
 * 终端分组服务
 * 
 * @author dingzhongchang
 * @version 1.0.0
 */
public interface TerminalGroupService {

	/**
	 * 添加市级组信息
	 * 
	 * @param group
	 *            市级组信息
	 */
	TerminalMenu addCityGroup(TerminalGroup teGroup, Long refId);

	/**
	 * 根据市级子域名称获取该市级分组
	 */

	Set<Terminal> getTerminalByCityName(String name);

	/**
	 * 添加省级组信息
	 * 
	 * @param group
	 *            省级组信息
	 */
	TerminalMenu addProvinceGroup(TerminalGroup group);

	/**
	 * 添加分组
	 * 
	 * @param group
	 *            终端分组
	 */
	void addGroup(TerminalGroup group);

	/**
	 * 删除分组
	 * 
	 * @param groupId
	 *            组ID
	 */
	TerminalMenu deleteGroup(Long groupId);

	/**
	 * 更新分组
	 * 
	 * @param group
	 *            终端分组
	 */
	void updateGroup(TerminalGroup group);

	//
	// /**
	// * 根据分组名获取所有终端
	// *
	// * @param name 组名
	// * @return 终端列表
	// */
	// List<Terminal> getAllTerminalByGroupName(String name);

	/**
	 * 根据分组ID获取其所有终端
	 * 
	 * @param groupId
	 *            分组ID
	 * @return 终端列表
	 */
	List<Terminal> getAllTerminalByGroupId(Long groupId);

	/**
	 * 找出在其他分组下的所有终端
	 * 
	 * @param thisGroupId
	 *            该分组ID
	 * @return
	 */
	List<Terminal> getTerminalsInOtherGroups(Long thisGroupId);

	// /**
	// * 获取所有车载终端，按分组归类
	// *
	// * @return Map<String, List<Terminal>> 分组名,终端列表
	// */
	// Map<String, List<Terminal>> getGroupNameToTerminalMap();

	/**
	 * 获取所有车载终端，按分组归类
	 * 
	 * @return Map<String, List<Terminal>> 分组ID,终端列表
	 */
	Map<Long, List<Terminal>> getGroupIdToTerminalMap();

	/**
	 * 获取所有分组
	 * 
	 * @return TerminalGroup列表
	 */
	List<TerminalGroup> getAllGroup();

	/**
	 * 更新某分组下的终端
	 * 
	 * @param terminalGroupId
	 *            分组ID
	 * @param teIds
	 *            该分组当前包含的终端
	 */
	void updateGroupMember(Long terminalGroupId, Long... teIds);

	// /**
	// * 根据组的Name获取所有分组
	// *
	// * @return TerminalGroup列表
	// */
	// Collection<TerminalGroup> getGroupByNames(String... groupNames);

	/**
	 * 根据组的ID得到组的对象
	 * 
	 * @param groupIds
	 *            分组ID列表
	 * @return 分组列表
	 */
	Collection<TerminalGroup> getGroupByIds(Long... groupIds);

	/**
	 * 获取所有组信息(根据菜单类型区分)
	 * 
	 * @param menus
	 *            List<TerminalMenu>
	 * @return List<TerminalGroup>
	 */
	List<TerminalGroup> getGroupsByMenus(List<TerminalMenu> menus);

	/**
	 * 获取所有市级组信息
	 * 
	 * @return TerminalGroup
	 */
	List<TerminalGroup> getAllCityGroup();

	/**
	 * 获取所有市级组信息
	 * 
	 * @return TerminalGroup
	 */
	List<TerminalGroup> getAllProvinceGroup();

	/**
	 * 删除多个组
	 * 
	 * @param checkedGroupIds
	 *            选择分组ID列表
	 */
	List<TerminalMenu> deleteCheckedGroups(Long checkedGroupIds[]);

	/**
	 * 根据ID得到组对象
	 * 
	 * @param testgroupid
	 * @return
	 */
	TerminalGroup findGroupById(Long testgroupid);

	/**
	 * 根据ID得到组对象
	 * 
	 * @param testgroupid
	 * @return TerminalMenu
	 */
	TerminalMenu findGroupMenuById(Long testgroupid);

	// /**
	// * 根据name得到组对象
	// *
	// * @param name
	// * @return
	// */
	// TerminalGroup findGroupByName(String name);

	/**
	 * 分页查询所有分组信息
	 * 
	 * @param terminalGroupQuery
	 *            分页查询条件
	 */
	List<TerminalGroupInfo> pageQueryGroups(
			TerminalGroupQuery terminalGroupQuery);

	/**
	 * 获取分组信息
	 * 
	 * @param id
	 *            分组信息ID
	 * @return 分组信息
	 */
	TerminalGroupInfo getGroupInfo(Long id);

	/**
	 * 获取所有分组信息
	 * 
	 * @return 分组信息
	 */
	List<TerminalGroupInfo> getAllGroupInfo();

	/**
	 * 修改终端分组信息
	 * 
	 * @param group
	 *            终端分组
	 * @return 终端分组对应的菜单信息
	 */
	TerminalMenu updateGroupAndMenu(TerminalGroup group);

	/**
	 * 根据市级分组 获取 省级分组名称
	 */
	String getProvinceNameByCityGroup(TerminalGroup cityGroup);
}
