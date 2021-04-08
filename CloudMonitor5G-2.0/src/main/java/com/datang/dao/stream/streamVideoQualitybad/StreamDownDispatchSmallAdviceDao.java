package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall.StreamDownDispatchSmallAdvice;
import com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall.StreamQualityBadDownDispatchSmall;

/**
 * 流媒体专题---流媒体视频质差---下行调度数小--优化建议：核查小区用户数以及是否存在干扰Dao
 * 
 * @explain
 * @name StreamDownDispatchSmallAdviceDao
 * @author shenyanwei
 * @date 2017年10月23日上午9:35:05
 */
@Repository
@SuppressWarnings("all")
public class StreamDownDispatchSmallAdviceDao extends
		GenericHibernateDao<StreamDownDispatchSmallAdvice, Long> {
	/**
	 * 根据下行调度数小事件获取优化建议
	 * 
	 * @param
	 * @return
	 */
	public List<StreamDownDispatchSmallAdvice> queryStreamDownDispatchSmallAdvice(
			StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall) {

		if (null == streamQualityBadDownDispatchSmall
				|| null == streamQualityBadDownDispatchSmall.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamDownDispatchSmallAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadDownDispatchSmall");
		criteria.add(Restrictions.eq("id",
				streamQualityBadDownDispatchSmall.getId()));
		createCriteria.addOrder(Order.desc("duration"));
		return createCriteria.list();
	}

}
