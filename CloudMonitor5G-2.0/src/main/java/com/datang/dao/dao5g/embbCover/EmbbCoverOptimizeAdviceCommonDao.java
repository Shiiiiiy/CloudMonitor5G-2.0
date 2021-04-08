/**
 * 
 */
package com.datang.dao.dao5g.embbCover;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;

/**
 * embb覆盖专题----优化建议详情dao
 * 
 * @author _YZP
 * 
 */
@Repository
@SuppressWarnings("all")
public class EmbbCoverOptimizeAdviceCommonDao extends
		GenericHibernateDao<EmbbCoverBadRoad, Long> {
	/**
	 * 根据根据embb覆盖路段获取路段中的优化建议详情
	 * 
	 * @param embbCoverBadRoad
	 * @param clazz
	 * @return
	 */
	public List queryOptimizeAdviceByRoad(EmbbCoverBadRoad embbCoverBadRoad,
			Class clazz) {
		if (null == embbCoverBadRoad || null == embbCoverBadRoad.getId()
				|| null == clazz) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				clazz);
		Criteria criteria = createCriteria.createCriteria("embbCoverBadRoad");
		criteria.add(Restrictions.eq("id", embbCoverBadRoad.getId()));
		return createCriteria.list();
	}

}
