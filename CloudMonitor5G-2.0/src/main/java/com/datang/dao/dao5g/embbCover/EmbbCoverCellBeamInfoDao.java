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
import com.datang.domain.domain5g.embbCover.EmbbCoverCellBeamInfo;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellInfo;

/**
 * @author _YZP
 * 
 */
@Repository
@SuppressWarnings("all")
public class EmbbCoverCellBeamInfoDao extends
		GenericHibernateDao<EmbbCoverCellBeamInfo, Long> {
	/**
	 * 根据embb覆盖路段中覆盖小区获取小区的波束详情
	 * 
	 * @param embbCoverCellInfo
	 * @return
	 */
	public List<EmbbCoverCellBeamInfo> queryCellBeamInfoByCell(
			EmbbCoverCellInfo embbCoverCellInfo) {

		if (null == embbCoverCellInfo || null == embbCoverCellInfo.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				EmbbCoverCellBeamInfo.class);
		createCriteria.addOrder(Order.asc("beamNo"));
		Criteria criteria = createCriteria.createCriteria("embbCoverCellInfo");
		criteria.add(Restrictions.eq("id", embbCoverCellInfo.getId()));
		return createCriteria.list();
	}
}
