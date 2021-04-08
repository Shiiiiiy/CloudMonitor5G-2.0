/**
 * 
 */
package com.datang.dao.VoLTEDissertation.qualityBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.NbCellGsmAddAdvice;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.nbDeficiency.VolteQualityBadRoadNbDeficiency;

/**
 * volte质量专题---volte语音质差---GSM添加建议Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月17日 上午9:28:28
 * @version
 */
@Repository
@SuppressWarnings("all")
public class NbCellDeficiencyGSMAddAdviceDao extends
		GenericHibernateDao<NbCellGsmAddAdvice, Long> {

	/**
	 * 根据根据邻区配置路段获取该质差路段中的GSM邻区添加建议详情LIST
	 * 
	 * @param qualityBadRoadNbDeficiency
	 * @return
	 */
	public List<NbCellGsmAddAdvice> queryCellInfoByRoad(
			VolteQualityBadRoadNbDeficiency qualityBadRoadNbDeficiency) {

		if (null == qualityBadRoadNbDeficiency
				|| null == qualityBadRoadNbDeficiency.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				NbCellGsmAddAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("volteQualityBadRoadNbDeficiency");
		criteria.add(Restrictions.eq("id", qualityBadRoadNbDeficiency.getId()));
		return createCriteria.list();
	}
}
