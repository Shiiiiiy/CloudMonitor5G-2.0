package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.weakCover.VideoQualityBadWeakCover;
import com.datang.domain.VoLTEDissertation.videoQualityBad.weakCover.WeakCoverAdjustAntennaFeeder;

/**
 * volte质量专题---volte视频质差---弱覆盖问题 ----优化建议调整天馈、功率Dao
 * 
 * @explain
 * @name WeakCoverAdjustAntennaFeederDao
 * @author shenyanwei
 * @date 2017年5月12日下午1:45:15
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverAdjustAntennaFeederDao extends
		GenericHibernateDao<WeakCoverAdjustAntennaFeeder, Long> {
	/**
	 * 根据弱覆盖问题获取弱覆盖问题中的优化建议调整天馈、功率
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<WeakCoverAdjustAntennaFeeder> queryWeakCoverAdjustAntennaFeeder(
			VideoQualityBadWeakCover videoQualityBadWeakCover) {

		if (null == videoQualityBadWeakCover
				|| null == videoQualityBadWeakCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverAdjustAntennaFeeder.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadWeakCover");
		criteria.add(Restrictions.eq("id", videoQualityBadWeakCover.getId()));
		return createCriteria.list();
	}

}
