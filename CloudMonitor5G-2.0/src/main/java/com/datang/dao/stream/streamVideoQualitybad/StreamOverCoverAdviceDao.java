package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.overCover.StreamOverCoverAdvice;
import com.datang.domain.stream.streamVideoQualitybad.overCover.StreamQualityBadOverCover;

/**
 * 流媒体专题---流媒体视频质差---重叠覆盖问题 ----优化建议方位角下倾角调整Dao
 * 
 * @explain
 * @name StreamOverCoverAdviceDao
 * @author shenyanwei
 * @date 2017年10月23日上午9:54:17
 */
@Repository
@SuppressWarnings("all")
public class StreamOverCoverAdviceDao extends
		GenericHibernateDao<StreamOverCoverAdvice, Long> {
	/**
	 * 根据重叠覆盖获取重叠覆盖中的优化建议方位角下倾角调整
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<StreamOverCoverAdvice> queryStreamOverCoverAdvice(
			StreamQualityBadOverCover streamQualityBadOverCover) {
		if (null == streamQualityBadOverCover
				|| null == streamQualityBadOverCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamOverCoverAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadOverCover");
		criteria.add(Restrictions.eq("id", streamQualityBadOverCover.getId()));
		createCriteria.addOrder(Order.desc("duration"));
		return createCriteria.list();
	}

}
