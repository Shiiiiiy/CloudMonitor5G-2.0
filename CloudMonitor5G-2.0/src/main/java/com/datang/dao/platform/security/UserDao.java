package com.datang.dao.platform.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.security.Permission;
import com.datang.domain.security.User;
import com.datang.domain.security.UserGroup;

/**
 * 用户DAO
 * 
 * @author yinzhipeng
 * @date:2015年10月9日 下午5:11:13
 * @version
 */
@Repository
public class UserDao extends GenericHibernateDao<User, Long> {

	/**
	 * 根据用户名查找用户
	 * 
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				User.class);
		createCriteria.add(Restrictions.eq("username", username));
		User user = (User) createCriteria.uniqueResult();
		return user;
	}

	/**
	 * 根据用户名获取用户的用户组
	 * 
	 * @param username
	 * @return
	 */
	public Set<UserGroup> findUserGroupSet(String username) {
		Set<UserGroup> set = new HashSet<UserGroup>();
		User user = findByUsername(username);
		for (UserGroup userGroup : user.getGroups()) {
			set.add(userGroup);
		}
		return set;
	}

	/**
	 * 根据用户名获取用户的所有用户组名称
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findUserGroupNames(String username) {
		Set<String> set = new HashSet<String>();
		User user = findByUsername(username);
		for (UserGroup userGroup : user.getGroups()) {
			set.add(userGroup.getName());
		}
		return set;
	}

	/**
	 * 根据用户名获取用户的所有权限
	 * 
	 * @param username
	 * @return
	 */
	public Set<String> findPermissions(String username) {
		Set<String> set = new HashSet<String>();
		User user = findByUsername(username);
		Set<UserGroup> userGroups = user.getGroups();
		if (null != userGroups && 0 != userGroups.size()) {
			for (UserGroup userGroup : userGroups) {
				Set<Permission> permissions = userGroup.getPermissions();
				if (null != permissions && 0 != permissions.size()) {
					Iterator<Permission> iter = permissions.iterator();
					while (iter.hasNext()) {
						Permission next = iter.next();
						String permissionName = next.getWildcardpermission();
						set.add(permissionName);
					}
				}
			}
		}
		return set;
	}
}
