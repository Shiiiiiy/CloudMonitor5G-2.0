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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.DisturbProblemRoadCellInfoCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.VolteContinueWirelessBadRoadDisturbProblem;

/**
 * Volte质量专题---连续无线差==干扰问题Dao
 * 
 * @explain
 * @name DisturbCellInfoDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:09:41
 */
@Repository
@SuppressWarnings("all")
public class DisturbCellInfoCWBRDao extends
		GenericHibernateDao<DisturbProblemRoadCellInfoCWBR, Long> {
	/**
	 * 根据干扰问题路段获取该路段中的小区详情LIST
	 * 
	 * @param badRoadDisturbProblem
	 * @return
	 */
	public List<DisturbProblemRoadCellInfoCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadDisturbProblem badRoadDisturbProblem) {
		if (null == badRoadDisturbProblem
				|| null == badRoadDisturbProblem.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				DisturbProblemRoadCellInfoCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadDisturbProblem");
		criteria.add(Restrictions.eq("id", badRoadDisturbProblem.getId()));
		return createCriteria.list();
	}

}
