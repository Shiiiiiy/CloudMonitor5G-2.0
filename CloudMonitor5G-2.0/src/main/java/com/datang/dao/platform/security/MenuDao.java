package com.datang.dao.platform.security;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;

/**
 * menu服务DAO
 * 
 * @author yinzhipeng
 * @date:2015年10月12日 下午1:25:27
 * @version
 */
@Repository
public class MenuDao extends GenericHibernateDao<Menu, Long> {

	/**
	 * 获取所有的终端菜单
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TerminalMenu> getTerminalMenus() {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalMenu.class);
		criteria.addOrder(Order.desc("id"));
		List<TerminalMenu> result = criteria.list();
		return result;
	}

	/**
	 * 获取顶层区域菜单权限
	 * 
	 * @return
	 */
	public List<TerminalMenu> getTopTerminalMenus() {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TerminalMenu.class);
		criteria.add(Restrictions.isNull("pid"));
		criteria.addOrder(Order.desc("id"));
		List<TerminalMenu> result = criteria.list();
		return result;
	}

	/**
	 * 获取城市下的所有的终端菜单
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TerminalMenu> getTerminalByCity(Long cityID) {
		List<TerminalMenu> result = null;
		if (null != cityID) {
			Criteria criteria = this.getHibernateSession().createCriteria(
					TerminalMenu.class);
			criteria.add(Restrictions.eq("pid", cityID));
			criteria.addOrder(Order.desc("id"));
			result = criteria.list();
		}
		return result;
	}

}
