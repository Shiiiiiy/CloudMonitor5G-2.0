/**
 * 
 */
package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamPingPongAdvice;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamQualityBadPingPong;

/**
 * 流媒体专题---流媒体视频质差---乒乓切换---优化建议调整切换参数Dao
 * 
 * @explain
 * @name StreamPingPongAdviceDao
 * @author shenyanwei
 * @date 2017年10月23日上午10:05:17
 */
@Repository
@SuppressWarnings("all")
public class StreamPingPongAdviceDao extends
		GenericHibernateDao<StreamPingPongAdvice, Long> {
	/**
	 * 根据根据乒乓切换问题获取优化建议调整切换参数
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<StreamPingPongAdvice> queryStreamPingPongAdvice(
			StreamQualityBadPingPong streamQualityBadPingPong) {
		if (null == streamQualityBadPingPong
				|| null == streamQualityBadPingPong.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamPingPongAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadPingPong");
		criteria.add(Restrictions.eq("id", streamQualityBadPingPong.getId()));
		createCriteria.addOrder(Order.desc("duration"));
		return createCriteria.list();
	}

}
