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
import com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem.OtherProblemCellInfo;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.otherProblem.VolteQualityBadRoadOther;

/**
 * volte质量专题---volte语音质差---小区详情Dao
 * 
 * @author yinzhipeng
 * @date:2015年12月4日 下午1:01:34
 * @version
 */
@Repository
@SuppressWarnings("all")
public class OtherCellInfoDao extends
		GenericHibernateDao<OtherProblemCellInfo, Long> {

	/**
	 * 根据根据其他问题路段获取该质差路段中的小区详情LIST
	 * 
	 * @param qualityBadRoadOther
	 * @return
	 */
	public List<OtherProblemCellInfo> queryCellInfoByRoad(
			VolteQualityBadRoadOther qualityBadRoadOther) {

		if (null == qualityBadRoadOther || null == qualityBadRoadOther.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				OtherProblemCellInfo.class);
		Criteria criteria = createCriteria
				.createCriteria("volteQualityBadRoadOther");
		criteria.add(Restrictions.eq("id", qualityBadRoadOther.getId()));
		return createCriteria.list();
	}
}
