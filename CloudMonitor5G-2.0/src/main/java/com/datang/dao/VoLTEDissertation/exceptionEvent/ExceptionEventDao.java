/**
 * 
 */
package com.datang.dao.VoLTEDissertation.exceptionEvent;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEvent;

/**
 * volte质量专题---volte异常事件Dao
 * 
 * @author yinzhipeng
 * @date:2016年5月6日 上午10:20:21
 * @version
 */
@Repository
@SuppressWarnings("all")
public class ExceptionEventDao extends
		GenericHibernateDao<VolteExceptionEvent, Long> {
	/**
	 * 根据测试日志的id集合获取所有异常事件公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteExceptionEvent> queryVolteExceptionEventsByLogIds(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteExceptionEvent.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return criteria.list();
	}

	/**
	 * 添加某条异常事件的路段名称
	 * 
	 * @param roadName
	 * @param eeId
	 */
	public void addQBRRoadName(String roadName, Long eeId) {
		if (StringUtils.hasText(roadName) && null != eeId) {
			String addHQL = "update VolteExceptionEvent ee set ee.m_stRoadName=:ROADNAME where ee.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", eeId, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}
}
