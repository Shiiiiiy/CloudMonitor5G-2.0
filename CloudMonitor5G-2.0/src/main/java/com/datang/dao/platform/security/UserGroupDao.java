package com.datang.dao.platform.security;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.security.UserGroup;

/**
 * 用户组Dao
 * 
 * @author yinzhipeng
 * @date:2015年10月10日 下午12:47:23
 * @version
 */
@Repository
public class UserGroupDao extends GenericHibernateDao<UserGroup, Long> {

	/**
	 * 根据用户组查找用户组
	 * 
	 * @param userGroupName
	 * @return
	 */
	public UserGroup findUserGroup(String userGroupName) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				UserGroup.class);
		createCriteria.add(Restrictions.eq("name", userGroupName));
		return (UserGroup) createCriteria.uniqueResult();
	}

}
