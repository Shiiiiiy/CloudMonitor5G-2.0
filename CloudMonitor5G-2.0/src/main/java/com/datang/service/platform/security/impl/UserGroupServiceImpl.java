package com.datang.service.platform.security.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.platform.security.PermissionDao;
import com.datang.dao.platform.security.UserGroupDao;
import com.datang.domain.security.Permission;
import com.datang.domain.security.UserGroup;
import com.datang.service.platform.security.IUserGroupService;

/**
 * 用户组Service实现
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 下午1:59:00
 * @version
 */
@Service
@Transactional
public class UserGroupServiceImpl implements IUserGroupService {

	@Autowired
	private UserGroupDao userGroupDao;
	@Autowired
	private PermissionDao permissionDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#findUserGroups()
	 */
	@Override
	public List<UserGroup> findUserGroups() {
		List<UserGroup> userGroups = new ArrayList<>();
		userGroups.addAll(userGroupDao.findAll());
		return userGroups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.platform.security.IUserGroupService#
	 * findRoleByUserGroupName(java.lang.String)
	 */
	@Override
	public UserGroup findUserGroupByUserGroupName(String userGroupName) {
		return userGroupDao.findUserGroup(userGroupName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#findRoleById(java
	 * .lang.Long)
	 */
	@Override
	public UserGroup findUserGroupById(Long userGroupId) {
		return userGroupDao.find(userGroupId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#findTopPermissions
	 * ()
	 */
	@Override
	public List<Permission> findTopPermissions() {
		List<Permission> findTopPermissions = permissionDao
				.findTopPermissions();
		return findTopPermissions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#findAllPermissions
	 * ()
	 */
	@Override
	public List<Permission> findAllPermissions() {
		List<Permission> list = new ArrayList<>();
		list.addAll(permissionDao.findAll());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#pageList(com.datang
	 * .common.action.page.PageList)
	 */
	@Override
	public AbstractPageList pageList(PageList pageList) {
		return userGroupDao.getPageList(pageList, UserGroup.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#findPermissions
	 * (long)
	 */
	@Override
	public List<Permission> findPermissions(long userGroupId) {
		UserGroup find = userGroupDao.find(userGroupId);
		List<Permission> list = new ArrayList<>();
		if (null != find) {
			list.addAll(find.getPermissions());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#addUserGroup(com
	 * .datang.domain.security.UserGroup)
	 */
	@Override
	public void addUserGroup(UserGroup userGroup) {
		userGroupDao.create(userGroup);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#delUserGroup(java
	 * .lang.Long)
	 */
	@Override
	public void delUserGroup(Long groupId) {
		userGroupDao.delete(groupId);
		// UserGroup find = userGroupDao.find(groupId);
		// if (null != find) {
		// find.getUsers().clear();
		// find.getPermissions().clear();
		// find.getMenus().clear();
		// // userGroupDao.update(find);
		//
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserGroupService#modifyUserGroup
	 * (com.datang.domain.security.UserGroup)
	 */
	@Override
	public void modifyUserGroup(UserGroup group) {
		userGroupDao.update(group);
	}

}
