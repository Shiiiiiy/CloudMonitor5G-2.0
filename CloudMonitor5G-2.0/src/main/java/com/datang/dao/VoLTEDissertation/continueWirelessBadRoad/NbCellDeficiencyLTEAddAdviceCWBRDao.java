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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.NbCellLteAddAdviceCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;

/**
 * volte质量专题---连续无线差---邻区LTE添加建议Dao
 * 
 * @explain
 * @name NbCellDeficiencyLTEAddAdviceDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:35:56
 */
@Repository
@SuppressWarnings("all")
public class NbCellDeficiencyLTEAddAdviceCWBRDao extends
		GenericHibernateDao<NbCellLteAddAdviceCWBR, Long> {

	/**
	 * 根据根据邻区配置路段获取该质差路段中的LTE邻区添加建议详情LIST
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<NbCellLteAddAdviceCWBR> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadNbDeficiency badRoadNbDeficiency) {

		if (null == badRoadNbDeficiency || null == badRoadNbDeficiency.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NbCellLteAddAdviceCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadNbDeficiency");
		criteria.add(Restrictions.eq("id", badRoadNbDeficiency.getId()));
		return createCriteria.list();
	}

	/**
	 * 根据根据邻区配置路段id获取该质差路段中的LTE邻区添加建议详情LIST
	 * 
	 * @param roadId
	 * @return
	 */
	public List<NbCellLteAddAdviceCWBR> queryCellInfoByRoad(Long roadId) {

		if (null == roadId) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NbCellLteAddAdviceCWBR.class);
		Criteria criteria = createCriteria
				.createCriteria("volteContinueWirelessBadRoadNbDeficiency");
		criteria.add(Restrictions.eq("id", roadId));
		return createCriteria.list();
	}
}
