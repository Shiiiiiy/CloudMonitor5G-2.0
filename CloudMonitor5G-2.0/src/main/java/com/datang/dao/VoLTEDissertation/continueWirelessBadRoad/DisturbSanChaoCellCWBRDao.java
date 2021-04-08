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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.DisturbProblemSanChaoCellCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.VolteContinueWirelessBadRoadDisturbProblem;

/**
 * volte质量专题---连续无线差---干扰三超小区详情Dao
 * 
 * @explain
 * @name DisturbSanChaoCellDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:48:11
 */
@Repository
@SuppressWarnings("all")
public class DisturbSanChaoCellCWBRDao extends
		GenericHibernateDao<DisturbProblemSanChaoCellCWBR, Long> {
	/**
	 * 根据干扰问题路段获取该质差路段中的三超小区详情LIST
	 * 
	 * @param badRoadDisturbProblem
	 * @return
	 */
	public List<DisturbProblemSanChaoCellCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadDisturbProblem badRoadDisturbProblem) {
		if (null == badRoadDisturbProblem
				|| null == badRoadDisturbProblem.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				DisturbProblemSanChaoCellCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadDisturbProblem");
		criteria.add(Restrictions.eq("id", badRoadDisturbProblem.getId()));
		return createCriteria.list();
	}

}
