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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.weakCover.WeakCoverTianKuiAdjustCWBR;

/**
 * Volte质量专题-连续无线差-弱覆盖天馈调整小区详情Dao
 * 
 * @explain
 * @name WeakCoverTianKuiAdjustDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:20:45
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverTianKuiAdjustCWBRDao extends
		GenericHibernateDao<WeakCoverTianKuiAdjustCWBR, Long> {
	/**
	 * 根据根据弱覆盖路段获取弱覆盖连续无线差路段中的天馈调整小区详情LIST
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<WeakCoverTianKuiAdjustCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadWeakCover badRoadWeakCover) {

		if (null == badRoadWeakCover || null == badRoadWeakCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverTianKuiAdjustCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadWeakCover");
		criteria.add(Restrictions.eq("id", badRoadWeakCover.getId()));
		return createCriteria.list();
	}

}
