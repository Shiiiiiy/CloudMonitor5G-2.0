/**
 * 
 */
package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.overCover.OverCoverAzimuthOrDowndipAngleAdjust;
import com.datang.domain.VoLTEDissertation.videoQualityBad.overCover.VideoQualityBadOverCover;

/**
 * volte质量专题---volte视频质差---重叠覆盖问题 ----优化建议方位角下倾角调整Dao
 * 
 * @explain
 * @name OverCoverAzimuthOrDowndipAngleAdjustDao
 * @author shenyanwei
 * @date 2017年5月12日下午1:26:16
 */
@Repository
@SuppressWarnings("all")
public class OverCoverAzimuthOrDowndipAngleAdjustDao extends
		GenericHibernateDao<OverCoverAzimuthOrDowndipAngleAdjust, Long> {
	/**
	 * 根据重叠覆盖获取重叠覆盖中的优化建议方位角下倾角调整
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<OverCoverAzimuthOrDowndipAngleAdjust> queryOverCoverAzimuthOrDowndipAngleAdjust(
			VideoQualityBadOverCover videoQualityBadOverCover) {
		if (null == videoQualityBadOverCover
				|| null == videoQualityBadOverCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				OverCoverAzimuthOrDowndipAngleAdjust.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadOverCover");
		criteria.add(Restrictions.eq("id", videoQualityBadOverCover.getId()));
		return createCriteria.list();
	}

}
