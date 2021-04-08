/**
 * 
 */
package com.datang.dao.VoLTEDissertation.continueWirelessBadRoad;

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
import com.datang.domain.VoLTEDissertation.continueWirelessBadRoad.VolteContinueWirelessBadRoad;

/**
 * volte质量专题---连续无线差---整体分析Dao
 * 
 * @explain
 * @name QualityBadRoadDao
 * @author shenyanwei
 * @date 2016年5月31日下午5:31:27
 */
@Repository
@SuppressWarnings("all")
public class VolteContinueWirelessBadRoadCWBRDao extends
		GenericHibernateDao<VolteContinueWirelessBadRoad, Long> {
	/**
	 * 根据测试日志的id集合获取所有质差路段公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteContinueWirelessBadRoad> queryVolteContinueWirelessBadRoadsByLogIds(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteContinueWirelessBadRoad.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 获取所有路段名称为空的连续无线差路段
	 * 
	 * @return
	 */
	public List<VolteContinueWirelessBadRoad> queryNullRoadName() {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteContinueWirelessBadRoad.class);
		createCriteria.add(Restrictions.isNull("m_stRoadName"));
		return createCriteria.list();
	}

	/**
	 * 添加某条连续无线差路段的路段名称
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public void addCWBRRoadName(String roadName, Long qbrId) {
		if (StringUtils.hasText(roadName) && null != qbrId) {
			String addHQL = "update VolteContinueWirelessBadRoad cwbr set cwbr.m_stRoadName=:ROADNAME where cwbr.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", qbrId, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}
}
