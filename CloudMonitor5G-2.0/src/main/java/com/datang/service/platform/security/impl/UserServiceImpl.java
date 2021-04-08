package com.datang.service.platform.security.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.platform.security.UserDao;
import com.datang.dao.platform.security.UserGroupDao;
import com.datang.domain.security.User;
import com.datang.domain.security.UserGroup;
import com.datang.service.platform.security.IUserService;
import com.datang.service.platform.security.PasswordHelper;

/**
 * 用户Service实现
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 下午1:43:37
 * @version
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserGroupDao userGroupDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#createUser(com.datang
	 * .domain.security.User, java.util.List)
	 */
	@Override
	public void createUser(User user, List<Long> userGroupIds) {
		// 封装用户的用户组属性
		for (Long userGroupId : userGroupIds) {
			UserGroup userGroup = new UserGroup();
			userGroup.setId(userGroupId);
			user.getGroups().add(userGroup);
		}
		PasswordHelper.encryptPassword(user);
		userDao.create(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#deleteUser(java.lang
	 * .Long)
	 */
	@Override
	public void deleteUser(Long userId) {
		userDao.delete(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#modifyUser(com.datang
	 * .domain.security.User)
	 */
	@Override
	public void modifyUser(User user) {
		userDao.update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#changePassword(java
	 * .lang.Long, java.lang.String)
	 */
	@Override
	public void changePassword(Long userId, String newPassword) {
		User user = userDao.find(userId);
		user.setPassword(newPassword);
		PasswordHelper.encryptPassword(user);
		userDao.update(user);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#findByUsername(java
	 * .lang.String)
	 */
	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#findUserGroupSet(java
	 * .lang.String)
	 */
	@Override
	public Set<UserGroup> findUserGroupSet(String username) {
		return userDao.findUserGroupSet(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#findUserGroupNames(
	 * java.lang.String)
	 */
	@Override
	public Set<String> findUserGroupNames(String username) {
		return userDao.findUserGroupNames(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#findPermissions(java
	 * .lang.String)
	 */
	@Override
	public Set<String> findPermissions(String username) {
		return userDao.findPermissions(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#pageList(com.datang
	 * .common.action.page.PageList)
	 */
	@Override
	public AbstractPageList pageList(PageList pageList) {
		return userDao.getPageList(pageList, User.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.platform.security.IUserService#changePassword(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(String username, String newPassword) {
		User user = userDao.findByUsername(username);
		user.setPassword(newPassword);
		PasswordHelper.encryptPassword(user);
		userDao.update(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.platform.security.IUserService#findByUserId(long)
	 */
	@Override
	public User findByUserId(long id) {
		return userDao.find(id);
	}
	
	@Override
	public List<User> findAllUsers(){
		List<User> users = new ArrayList<>();
		users.addAll(userDao.findAll());
		return users;
	}

}
