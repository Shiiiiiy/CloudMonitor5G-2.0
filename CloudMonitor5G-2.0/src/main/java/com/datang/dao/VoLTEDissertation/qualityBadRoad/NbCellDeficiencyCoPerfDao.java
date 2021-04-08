/**
 * 
 */
package com.datang.dao.VoLTEDissertation.qualityBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.NbCellDeficiencyCoPerf;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.NbCellLteAddAdvice;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.VolteQualityBadRoadNbDeficiency;

/**
 * volte质量专题---volte语音质差---邻区切换性能Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月17日 上午9:28:28
 * @version
 */
@Repository
@SuppressWarnings("all")
public class NbCellDeficiencyCoPerfDao extends
		GenericHibernateDao<NbCellDeficiencyCoPerf, Long> {

	/**
	 * 根据根据邻区配置路段获取该质差路段中的邻区切换性能详情LIST
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<NbCellDeficiencyCoPerf> queryCellInfoByRoad(
			VolteQualityBadRoadNbDeficiency qualityBadRoadNbDeficiency) {

		if (null == qualityBadRoadNbDeficiency
				|| null == qualityBadRoadNbDeficiency.getId()) {
			return new ArrayList<>();
		}

		// 查询LTE邻区添加建议的cellid
		DetachedCriteria nbCellLteAddAdviceCriteria = DetachedCriteria
				.forClass(NbCellLteAddAdvice.class, "nbCellAdd");
		DetachedCriteria volteQualityBadRoadNbDeficiencyCriteria = nbCellLteAddAdviceCriteria
				.createCriteria("volteQualityBadRoadNbDeficiency");
		volteQualityBadRoadNbDeficiencyCriteria.add(Restrictions.eq("id",
				qualityBadRoadNbDeficiency.getId()));
		nbCellLteAddAdviceCriteria
				.setProjection(Projections.property("cellId"));
		nbCellLteAddAdviceCriteria.add(Restrictions.isNotNull("cellId"));

		// 在全量信息里查询符合上述条件的NbCellDeficiencyCoPerf
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NbCellDeficiencyCoPerf.class);
		createCriteria.add(Property.forName("cellId").in(
				nbCellLteAddAdviceCriteria));
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.eq("recSeqNo", qualityBadRoadNbDeficiency
				.getTestLogItem().getRecSeqNo()));
		return createCriteria.list();
	}
}
