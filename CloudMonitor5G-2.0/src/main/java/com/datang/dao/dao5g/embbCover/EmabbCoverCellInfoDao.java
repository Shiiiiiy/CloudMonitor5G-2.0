/**
 * 
 */
package com.datang.dao.dao5g.embbCover;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;

/**
 * embb覆盖专题---覆盖小区详情Dao
 * 
 * @author _YZP
 * 
 */
@Repository
@SuppressWarnings("all")
public class EmabbCoverCellInfoDao extends
		GenericHibernateDao<EmbbCoverCellInfo, Long> {
	/**
	 * 根据根据embb覆盖路段获取覆盖路段中的小区详情LIST
	 * 
	 * @param embbCoverBadRoad
	 * @return
	 */
	public List<EmbbCoverCellInfo> queryCellInfoByRoad(
			EmbbCoverBadRoad embbCoverBadRoad) {

		if (null == embbCoverBadRoad || null == embbCoverBadRoad.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				EmbbCoverCellInfo.class);
		createCriteria.addOrder(Order.desc("testTime"));
		Criteria criteria = createCriteria.createCriteria("embbCoverBadRoad");
		criteria.add(Restrictions.eq("id", embbCoverBadRoad.getId()));
		return createCriteria.list();
	}

}
