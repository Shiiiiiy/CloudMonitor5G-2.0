package com.datang.dao.testPlan;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testPlan.CommandTemplate;
import com.datang.web.beans.testPlan.CommandTemplateQuery;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-28
 */
@Repository
public class CommandTemplateDao extends
		GenericHibernateDao<CommandTemplate, Integer> {
	/**
	 * 通过commandTemplateQuery查询CommandTemplate对象
	 * 
	 * @param commandTemplateQuery
	 *            查询条件
	 * @return 查询到的CommandTemplate对象集合
	 */
	@SuppressWarnings("unchecked")
	public Collection<CommandTemplate> queryList(
			CommandTemplateQuery commandTemplateQuery) {

		Criteria criteria = this.getHibernateSession().createCriteria(
				CommandTemplate.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (commandTemplateQuery.getName() != null) {
			criteria.add(Restrictions.eq("name", commandTemplateQuery.getName()));
		}
		if (commandTemplateQuery.getType() != null) {
			criteria.add(Restrictions.eq("type", commandTemplateQuery.getType()));
		}

		criteria.setFirstResult(commandTemplateQuery.getPagerOffset());
		criteria.setMaxResults(commandTemplateQuery.getPagerPerPage());
		Collection<CommandTemplate> result = criteria.list();
		commandTemplateQuery.setQueryResults(result);
		if (commandTemplateQuery.getPagerCount() == -1) {
			int count = (Integer) criteria
					.setProjection(Projections.rowCount()).uniqueResult();
			commandTemplateQuery.setPagerCount(count);
		}
		return result;

	}

	/**
	 * 通过name查找CommandTemplate
	 * 
	 * @param name
	 *            名称
	 * @return 查找到的CommandTemplate对象
	 */
	public CommandTemplate getCommandTemplateByName(String name) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				CommandTemplate.class);
		criteria.add(Restrictions.eq("name", name));
		Object obj = criteria.uniqueResult();
		if (obj == null) {
			return null;
		} else {
			return (CommandTemplate) obj;
		}
	}

}
