package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall.DownDispatchSmallInspectCellUserAndExistDisturb;
import com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall.VideoQualityBadDownDispatchSmall;

/**
 * volte质量专题---volte视频质差---下行调度数小--优化建议：核查小区用户数以及是否存在干扰Dao
 * 
 * @explain
 * @name DownDispatchSmallInspectCellUserAndExistDisturbDao
 * @author shenyanwei
 * @date 2017年6月5日下午3:34:16
 */
@Repository
@SuppressWarnings("all")
public class DownDispatchSmallInspectCellUserAndExistDisturbDao
		extends
		GenericHibernateDao<DownDispatchSmallInspectCellUserAndExistDisturb, Long> {
	/**
	 * 根据下行调度数小事件获取优化建议
	 * 
	 * @param videoQualityBadDownDispatchSmall
	 * @return
	 */
	public List<DownDispatchSmallInspectCellUserAndExistDisturb> queryPingPongCutEvent(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall) {

		if (null == videoQualityBadDownDispatchSmall
				|| null == videoQualityBadDownDispatchSmall.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				DownDispatchSmallInspectCellUserAndExistDisturb.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadDownDispatchSmall");
		criteria.add(Restrictions.eq("id",
				videoQualityBadDownDispatchSmall.getId()));
		return createCriteria.list();
	}

}
