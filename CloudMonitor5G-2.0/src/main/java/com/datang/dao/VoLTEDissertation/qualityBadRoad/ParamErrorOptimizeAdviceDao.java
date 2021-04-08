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
import com.datang.domain.VoLTEDissertation.qualityBadRoad.paramError.ParamErrorOptimizeAdvice;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.paramError.VolteQualityBadRoadParamError;

/**
 * volte质量专题---volte语音质差---参数问题路段优化建议
 * 
 * @author yinzhipeng
 * @date:2015年12月3日 下午1:09:30
 * @version
 */
@Repository
@SuppressWarnings("all")
public class ParamErrorOptimizeAdviceDao extends
		GenericHibernateDao<ParamErrorOptimizeAdvice, Long> {
	/**
	 * 根据根据参数错误路段获取该质差路段中的优化建议
	 * 
	 * @param qualityBadRoadParamError
	 * @return
	 */
	public List<ParamErrorOptimizeAdvice> queryCellInfoByRoad(
			VolteQualityBadRoadParamError qualityBadRoadParamError) {

		if (null == qualityBadRoadParamError
				|| null == qualityBadRoadParamError.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				ParamErrorOptimizeAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("volteQualityBadRoadParamError");
		criteria.add(Restrictions.eq("id", qualityBadRoadParamError.getId()));
		return createCriteria.list();
	}
}
