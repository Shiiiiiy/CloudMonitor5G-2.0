package com.datang.dao.platform.security;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.security.Permission;

/**
 * 功能权限dao
 * 
 * @author yinzhipeng
 * @date:2015年10月10日 下午12:49:24
 * @version
 */
@Repository
public class PermissionDao extends GenericHibernateDao<Permission, Long> {

	public List<Permission> findTopPermissions() {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				Permission.class);
		createCriteria.add(Restrictions.isNull("parent"));
		return createCriteria.list();
	}
}
