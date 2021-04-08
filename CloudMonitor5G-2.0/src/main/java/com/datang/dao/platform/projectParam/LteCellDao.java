/**
 * 
 */
package com.datang.dao.platform.projectParam;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.platform.projectParam.LteCell;

/**
 * LTE小区dao
 * 
 * @author yinzhipeng
 * @date:2015年10月20日 下午5:56:28
 * @version
 */
@Repository
public class LteCellDao extends GenericHibernateDao<LteCell, Long> {
	public LteCell queryLteCellByCellId(Long cellId) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				LteCell.class);
		createCriteria.add(Restrictions.eq("cellId", cellId));
		List<LteCell> list = (List<LteCell>) createCriteria.list();
		if (list != null && list.size() != 0)
			return list.get(0);
		else
			return null;
	}
}
