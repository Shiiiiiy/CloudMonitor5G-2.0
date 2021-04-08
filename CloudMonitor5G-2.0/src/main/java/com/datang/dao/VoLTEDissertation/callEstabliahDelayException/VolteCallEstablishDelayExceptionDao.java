package com.datang.dao.VoLTEDissertation.callEstabliahDelayException;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;

/**
 * 呼叫建立时延公共类实现Dao
 * 
 * @explain
 * @name VolteCallEstablishDelayExceptionDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:16:57
 */
@Repository
@SuppressWarnings("all")
public class VolteCallEstablishDelayExceptionDao extends
		GenericHibernateDao<VolteCallEstablishDelayException, Long> {
	/**
	 * 根据测试日志的id集合获取呼叫建立时延异常
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteCallEstablishDelayException> queryVolteCallEstablishDelayExceptionsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayException.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据测试日志的id集合获取呼叫建立时延异常id投影
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<Long> queryVolteCedeIdsByLogIds(List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteCallEstablishDelayException.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.setProjection(Projections.property("id"));
		return createCriteria.list();
	}

	/**
	 * 添加某路段名称
	 * 
	 * @param roadName
	 * @param id
	 */
	public void addCEDERoadName(String roadName, Long id) {
		if (StringUtils.hasText(roadName) && null != id) {
			String addHQL = "update VolteCallEstablishDelayException cede set cede.m_stRoadName=:ROADNAME where cede.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", id, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}
}
