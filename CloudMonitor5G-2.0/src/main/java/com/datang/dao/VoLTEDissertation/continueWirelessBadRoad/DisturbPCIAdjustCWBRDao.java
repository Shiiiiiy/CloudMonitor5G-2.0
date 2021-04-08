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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.DisturbProblemPCIAdjustCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.disturbProblem.VolteContinueWirelessBadRoadDisturbProblem;

/**
 * volte质量专题---连续无线差---干扰问题PCI调整小区详情Dao
 * 
 * @explain
 * @name DisturbPCIAdjustDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:49:56
 */
@Repository
@SuppressWarnings("all")
public class DisturbPCIAdjustCWBRDao extends
		GenericHibernateDao<DisturbProblemPCIAdjustCWBR, Long> {
	/**
	 * 根据干扰问题路段获取该质差路段中的PCI调整小区详情LIST
	 * 
	 * @param badRoadDisturbProblem
	 * @return
	 */
	public List<DisturbProblemPCIAdjustCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadDisturbProblem badRoadDisturbProblem) {
		if (null == badRoadDisturbProblem
				|| null == badRoadDisturbProblem.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				DisturbProblemPCIAdjustCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadDisturbProblem");
		criteria.add(Restrictions.eq("id", badRoadDisturbProblem.getId()));
		return createCriteria.list();
	}

}
