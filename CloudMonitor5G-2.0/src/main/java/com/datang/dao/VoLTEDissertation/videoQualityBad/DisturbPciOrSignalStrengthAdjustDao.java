package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.disturb.DisturbPciOrSignalStrengthAdjust;
import com.datang.domain.VoLTEDissertation.videoQualityBad.disturb.VideoQualtyBadDisturb;

/**
 * volte质量专题---volte视频质差---干扰问题---优化建议：pci或信号强度调整Dao
 * 
 * @explain
 * @name DisturbPciOrSignalStrengthAdjustDao
 * @author shenyanwei
 * @date 2017年5月12日上午11:25:00
 */
@Repository
@SuppressWarnings("all")
public class DisturbPciOrSignalStrengthAdjustDao extends
		GenericHibernateDao<DisturbPciOrSignalStrengthAdjust, Long> {
	/**
	 * 根据干扰问题获取该问题中的优化建议：pci或信号强度调整
	 * 
	 * @param badRoadDisturbProblem
	 * @return
	 */
	public List<DisturbPciOrSignalStrengthAdjust> queryDisturbPciOrSignalStrengthAdjust(
			VideoQualtyBadDisturb videoQualtyBadDisturb) {
		if (null == videoQualtyBadDisturb
				|| null == videoQualtyBadDisturb.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				DisturbPciOrSignalStrengthAdjust.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualtyBadDisturb");
		criteria.add(Restrictions.eq("id", videoQualtyBadDisturb.getId()));
		return createCriteria.list();
	}

}
