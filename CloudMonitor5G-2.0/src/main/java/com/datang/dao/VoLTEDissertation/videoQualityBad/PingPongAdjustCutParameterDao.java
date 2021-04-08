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
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.PingPongAdjustCutParameter;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.VideoQualityBadPingPong;

/**
 * volte质量专题---volte视频质差---乒乓切换---优化建议调整切换参数Dao
 * 
 * @explain
 * @name PingPongAdjustCutParameterDao
 * @author shenyanwei
 * @date 2017年5月12日下午1:34:20
 */
@Repository
@SuppressWarnings("all")
public class PingPongAdjustCutParameterDao extends
		GenericHibernateDao<PingPongAdjustCutParameter, Long> {
	/**
	 * 根据根据乒乓切换问题获取优化建议调整切换参数
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<PingPongAdjustCutParameter> queryPingPongAdjustCutParameter(
			VideoQualityBadPingPong videoQualityBadPingPong) {
		if (null == videoQualityBadPingPong
				|| null == videoQualityBadPingPong.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				PingPongAdjustCutParameter.class);
		Criteria criteria = createCriteria
				.createCriteria("videoQualityBadPingPong");
		criteria.add(Restrictions.eq("id", videoQualityBadPingPong.getId()));
		return createCriteria.list();
	}

}
