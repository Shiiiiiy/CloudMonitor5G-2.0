/**
 * 
 */
package com.datang.dao.VoLTEDissertation.continueWirelessBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover.VolteContinueWirelessBadRoadWeakCover;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover.WeakCoverCellInfoCWBR;

/**
 * Volte质量专题---连续无线差--弱覆盖小区详情Dao
 * 
 * @explain
 * @name WeakCoverCellInfoDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:28:13
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverCellInfoCWBRDao extends
		GenericHibernateDao<WeakCoverCellInfoCWBR, Long> {
	/**
	 * 根据根据弱覆盖路段获取弱覆盖质差路段中的小区详情LIST
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<WeakCoverCellInfoCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadWeakCover badRoadWeakCover) {

		if (null == badRoadWeakCover || null == badRoadWeakCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverCellInfoCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadWeakCover");
		criteria.add(Restrictions.eq("id", badRoadWeakCover.getId()));
		return createCriteria.list();
	}

}
