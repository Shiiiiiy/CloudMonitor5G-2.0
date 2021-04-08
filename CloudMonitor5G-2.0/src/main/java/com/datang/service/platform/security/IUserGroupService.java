package com.datang.service.platform.security;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.security.Permission;
import com.datang.domain.security.UserGroup;

/**
 * 用户组Service接口
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 下午1:43:26
 * @version
 */
public interface IUserGroupService {
	/**
	 * 获取所有的用户组
	 * 
	 * @return
	 */
	public List<UserGroup> findUserGroups();

	/**
	 * 获取指定名称的用户组
	 * 
	 * @param userGroupName
	 * @return
	 */
	public UserGroup findUserGroupByUserGroupName(String userGroupName);

	/**
	 * 获取指定ID的用户组
	 * 
	 * @param UserGroupId
	 * @return
	 */
	public UserGroup findUserGroupById(Long userGroupId);

	/**
	 * 用户组多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList); // 多条件分页查询

	/**
	 * 获取功能权限的最顶部权限
	 * 
	 * @return
	 */
	public List<Permission> findTopPermissions();

	/**
	 * 获取所有功能权限
	 * 
	 * @return
	 */
	public List<Permission> findAllPermissions();

	/**
	 * 获取指定ID的用户组的所有功能权限
	 * 
	 * @param userGroupId
	 * @return
	 */
	public List<Permission> findPermissions(long userGroupId);

	/**
	 * 新增用户组
	 * 
	 * @param userGroup
	 */
	public void addUserGroup(UserGroup userGroup);

	/**
	 * 删除用户组
	 * 
	 * @param roleId
	 */
	public void delUserGroup(Long groupId);

	/**
	 * 修改用户组
	 * 
	 * @param group
	 */
	public void modifyUserGroup(UserGroup group);
}
