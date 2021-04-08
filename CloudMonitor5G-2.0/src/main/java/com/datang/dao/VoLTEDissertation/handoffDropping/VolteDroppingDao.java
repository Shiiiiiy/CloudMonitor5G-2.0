package com.datang.dao.VoLTEDissertation.handoffDropping;

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
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDropping;

/**
 * 
 * @explain 切换失败Dao
 * @name VolteDroppingDao
 * @author shenyanwei
 * @date 2016年5月10日下午4:07:38
 */
@Repository
@SuppressWarnings("all")
public class VolteDroppingDao extends GenericHibernateDao<VolteDropping, Long> {

	/**
	 * 添加某条切换失败的路段名称
	 * 
	 * @param roadName
	 * @param droppingId
	 */
	public void addQBRRoadName(String roadName, Long droppingId) {
		if (StringUtils.hasText(roadName) && null != droppingId) {
			String addHQL = "update VolteDropping vd set vd.m_stRoadName=:ROADNAME where vd.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", droppingId, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}

	/**
	 * 根据测试日志的id集合获取所有切换失败事件
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteDropping> queryVolteDroppingsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteDropping.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}
}
