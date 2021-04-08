package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.disturb.StreamDisturbAdvice;
import com.datang.domain.stream.streamVideoQualitybad.disturb.StreamQualtyBadDisturb;

/**
 * 流媒体视频质差干扰问题---优化建议：pci或信号强度调整Dao
 * 
 * @explain
 * @name StreamDisturbAdviceDao
 * @author shenyanwei
 * @date 2017年10月23日上午9:27:04
 */
@Repository
@SuppressWarnings("all")
public class StreamDisturbAdviceDao extends
		GenericHibernateDao<StreamDisturbAdvice, Long> {
	/**
	 * 根据干扰问题获取该问题中的优化建议：pci或信号强度调整
	 * 
	 * @param badRoadDisturbProblem
	 * @return
	 */
	public List<StreamDisturbAdvice> queryStreamDisturbAdvice(
			StreamQualtyBadDisturb videoQualtyBadDisturb) {
		if (null == videoQualtyBadDisturb
				|| null == videoQualtyBadDisturb.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamDisturbAdvice.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualtyBadDisturb");
		criteria.add(Restrictions.eq("id", videoQualtyBadDisturb.getId()));
		createCriteria.addOrder(Order.desc("duration"));
		return createCriteria.list();
	}

}
