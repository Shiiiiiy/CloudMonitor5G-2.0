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
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.DisturbProblemSanChaoCell;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.disturbProblem.VolteQualityBadRoadDisturbProblem;

/**
 * volte质量专题---volte语音质差---天馈调整小区详情Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月16日 下午1:58:47
 * @version
 */
@Repository
@SuppressWarnings("all")
public class DisturbSanChaoCellDao extends
		GenericHibernateDao<DisturbProblemSanChaoCell, Long> {
	/**
	 * 根据干扰问题路段获取该质差路段中的三超小区详情LIST
	 * 
	 * @param badRoadDisturbProblem
	 * @return
	 */
	public List<DisturbProblemSanChaoCell> queryCellInfoByRoad(
			VolteQualityBadRoadDisturbProblem badRoadDisturbProblem) {
		if (null == badRoadDisturbProblem
				|| null == badRoadDisturbProblem.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				DisturbProblemSanChaoCell.class);
		Criteria criteria = createCriteria
				.createCriteria("volteQualityBadRoadDisturbProblem");
		criteria.add(Restrictions.eq("id", badRoadDisturbProblem.getId()));
		return createCriteria.list();
	}

}
