/**
 * Copyright 2011 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.dao.testManage.terminal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.datang.bean.testManage.terminal.SoftwareUpgradeQuery;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.StringUtils;
import com.datang.domain.testManage.terminal.SoftwareUpgrade;

/**
 * 
 * @author dingzhongchang
 * @version 1.1.0, 2011-5-11
 */
@Repository
public class SoftwareUpgradeDao extends
		GenericHibernateDao<SoftwareUpgrade, Integer> {

	/**
	 * 通过查询条件查询
	 * 
	 * @param terminalMenuQuery
	 *            查询条件
	 * @return 查询出的结果
	 */
	public Collection<SoftwareUpgrade> query(
			SoftwareUpgradeQuery softwareUpgradeQuery) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				SoftwareUpgrade.class);
		if (!StringUtils.hasText(softwareUpgradeQuery.getUser())) {
			Criteria useCriteria = criteria.createCriteria("user");
			useCriteria.add(Expression.like("name",
					softwareUpgradeQuery.getUser(), MatchMode.ANYWHERE));
		}
		if (!StringUtils.hasText(softwareUpgradeQuery.getVender())) {
			criteria.add(Expression.eq("vender",
					softwareUpgradeQuery.getVender()));
		}
		if (!StringUtils.hasText(softwareUpgradeQuery.getSoftwareVersion())) {
			criteria.add(Expression.like("softwareVersion",
					softwareUpgradeQuery.getSoftwareVersion(),
					MatchMode.ANYWHERE));
		}
		if (softwareUpgradeQuery.getTestTarget() != null
				&& softwareUpgradeQuery.getTestTarget() != -1) {
			criteria.add(Expression.eq("testTarget",
					softwareUpgradeQuery.getTestTarget()));
		}
		if (null != softwareUpgradeQuery.getStatus()) {
			criteria.add(Expression.like("status",
					softwareUpgradeQuery.getStatus(), MatchMode.ANYWHERE));
		}
		if (null != softwareUpgradeQuery.getUploadBeginDate()) {
			if (null != softwareUpgradeQuery.getUploadEndDate()) {
				criteria.add(Expression.between("uploadDate",
						softwareUpgradeQuery.getUploadBeginDate(),
						softwareUpgradeQuery.getUploadEndDate()));
			} else {
				criteria.add(Expression.between("uploadDate",
						softwareUpgradeQuery.getUploadBeginDate(), new Date()));
			}
		} else if (null != softwareUpgradeQuery.getUploadEndDate()) {
			criteria.add(Expression.between("uploadDate",
					softwareUpgradeQuery.getUploadEndDate(),
					softwareUpgradeQuery.getUploadEndDate()));
		}
		criteria.addOrder(Order.desc("uploadDate"));
		// yzp
		// criteria.setFirstResult(softwareUpgradeQuery.getPagerOffset());
		// criteria.setMaxResults(softwareUpgradeQuery.getPagerPerPage());
		Collection<SoftwareUpgrade> sus = criteria.list();
		// yzp
		// softwareUpgradeQuery.setQueryResults(sus);
		//
		// if (softwareUpgradeQuery.getPagerCount() == -1) {
		// int count = (Integer) criteria
		// .setProjection(Projections.rowCount()).uniqueResult();
		// softwareUpgradeQuery.setPagerCount(count);
		// }
		return sus;
	}

	/**
	 * 获取所有菜单信息
	 */
	public List<SoftwareUpgrade> queryAll() {
		List<SoftwareUpgrade> softwareUpgrades = new ArrayList<SoftwareUpgrade>();
		Collection<SoftwareUpgrade> softwareUpgradeList = findAll();
		if (!CollectionUtils.isEmpty(softwareUpgradeList)) {
			softwareUpgrades.addAll(softwareUpgradeList);
		}
		return softwareUpgrades;
	}

	/**
	 * 获取当前路径下的软件升级信息
	 * 
	 * @param path
	 *            路径
	 * @return 软件升级信息
	 */
	public SoftwareUpgrade querySoftUpgrade(String path) {
		String hql = "from SoftwareUpgrade te where te.path =:path";
		Query query = this.getHibernateSession().createQuery(hql);
		query.setParameter("path", path);
		return (SoftwareUpgrade) query.uniqueResult();
	}
}
