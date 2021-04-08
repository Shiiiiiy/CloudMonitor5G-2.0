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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.otherProblem.OtherProblemCellInfoCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.otherProblem.VolteContinueWirelessBadRoadOtherProblem;

/**
 * Volte质量专题-连续无线差-其他原因小区详情Dao
 * 
 * @explain
 * @name OtherCellInfoDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:16:28
 */
@Repository
@SuppressWarnings("all")
public class OtherCellInfoCWBRDao extends
		GenericHibernateDao<OtherProblemCellInfoCWBR, Long> {

	/**
	 * 根据根据其他问题路段获取该质差路段中的小区详情LIST
	 * 
	 * @param qualityBadRoadOther
	 * @return
	 */
	public List<OtherProblemCellInfoCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadOtherProblem badRoadOther) {

		if (null == badRoadOther || null == badRoadOther.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				OtherProblemCellInfoCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadOtherProblem");
		criteria.add(Restrictions.eq("id", badRoadOther.getId()));
		return createCriteria.list();
	}
}
