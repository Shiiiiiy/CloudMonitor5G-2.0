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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.NbCellGsmAddAdviceCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;

/**
 * volte质量专题---连续无线差---邻区GSM添加建议Dao
 * 
 * @explain
 * @name NbCellDeficiencyGSMAddAdviceDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:39:49
 */
@Repository
@SuppressWarnings("all")
public class NbCellDeficiencyGSMAddAdviceCWBRDao extends
		GenericHibernateDao<NbCellGsmAddAdviceCWBR, Long> {

	/**
	 * 根据根据邻区配置路段获取该质差路段中的GSM邻区添加建议详情LIST
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<NbCellGsmAddAdviceCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadNbDeficiency badRoadNbDeficiency) {

		if (null == badRoadNbDeficiency || null == badRoadNbDeficiency.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NbCellGsmAddAdviceCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadNbDeficiency");
		criteria.add(Restrictions.eq("id", badRoadNbDeficiency.getId()));
		return createCriteria.list();
	}
}
