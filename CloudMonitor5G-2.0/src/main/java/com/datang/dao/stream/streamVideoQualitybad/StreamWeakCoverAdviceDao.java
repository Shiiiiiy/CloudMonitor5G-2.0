package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamQualityBadWeakCover;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamWeakCoverAdvice;

/**
 * 流媒体专题---流媒体视频质差---弱覆盖问题 ----优化建议调整天馈、功率Dao
 * 
 * @explain
 * @name StreamWeakCoverAdviceDao
 * @author shenyanwei
 * @date 2017年10月23日上午10:11:13
 */
@Repository
@SuppressWarnings("all")
public class StreamWeakCoverAdviceDao extends
		GenericHibernateDao<StreamWeakCoverAdvice, Long> {
	/**
	 * 根据弱覆盖问题获取弱覆盖问题中的优化建议调整天馈、功率
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<StreamWeakCoverAdvice> queryStreamWeakCoverAdvice(
			StreamQualityBadWeakCover streamQualityBadWeakCover) {

		if (null == streamQualityBadWeakCover
				|| null == streamQualityBadWeakCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamWeakCoverAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadWeakCover");
		criteria.add(Restrictions.eq("id", streamQualityBadWeakCover.getId()));
		createCriteria.addOrder(Order.desc("duration"));
		return createCriteria.list();
	}

}
