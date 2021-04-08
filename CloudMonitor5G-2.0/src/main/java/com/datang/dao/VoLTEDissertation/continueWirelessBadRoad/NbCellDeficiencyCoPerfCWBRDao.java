/**
 * 
 */
package com.datang.dao.VoLTEDissertation.continueWirelessBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.NbCellLteAddAdviceCWBR;
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.nbDeficiency.VolteContinueWirelessBadRoadNbDeficiency;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.NbCellDeficiencyCoPerf;

/**
 * volte质量专题---连续无线差---邻区切换性能Dao
 * 
 * @explain
 * @name NbCellDeficiencyCoPerfDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:43:06
 */
@Repository
@SuppressWarnings("all")
public class NbCellDeficiencyCoPerfCWBRDao extends
		GenericHibernateDao<NbCellDeficiencyCoPerf, Long> {

	/**
	 * 根据根据邻区配置路段获取该质差路段中的邻区切换性能详情LIST
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<NbCellDeficiencyCoPerf> queryCellInfoByRoad(
			VolteContinueWirelessBadRoadNbDeficiency badRoadNbDeficiency) {

		if (null == badRoadNbDeficiency || null == badRoadNbDeficiency.getId()) {
			return new ArrayList<>();
		}
		// 查询LTE邻区添加建议的cellid
		DetachedCriteria nbCellLteAddAdviceCriteria = DetachedCriteria
				.forClass(NbCellLteAddAdviceCWBR.class, "nbCellAdd");
		DetachedCriteria volteQualityBadRoadNbDeficiencyCriteria = nbCellLteAddAdviceCriteria
				.createCriteria("volteContinueWirelessBadRoadNbDeficiency");
		volteQualityBadRoadNbDeficiencyCriteria.add(Restrictions.eq("id",
				badRoadNbDeficiency.getId()));
		nbCellLteAddAdviceCriteria
				.setProjection(Projections.property("cellId"));
		nbCellLteAddAdviceCriteria.add(Restrictions.isNotNull("cellId"));

		// 在全量信息里查询符合上述条件的NbCellDeficiencyCoPerf
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NbCellDeficiencyCoPerf.class);
		createCriteria.add(Property.forName("cellId").in(
				nbCellLteAddAdviceCriteria));
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.eq("recSeqNo", badRoadNbDeficiency
				.getTestLogItem().getRecSeqNo()));
		return createCriteria.list();
	}
}
