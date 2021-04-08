/**
 * 
 */
package com.datang.dao.VoLTEDissertation.qualityBadRoad;

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
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;

/**
 * volte质量专题---volte语音质差---整体分析Dao
 * 
 * @author yinzhipeng
 * @date:2015年12月14日 上午9:20:53
 * @version
 */
@Repository
@SuppressWarnings("all")
public class QualityBadRoadDao extends
		GenericHibernateDao<VolteQualityBadRoad, Long> {
	/**
	 * 根据测试日志的id集合获取所有质差路段公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteQualityBadRoad> queryVolteQualityBadRoadsByLogIds(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoad.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.add(Restrictions.eq("serviceType", "1,"));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 获取所有路段名称为空的质差路段
	 * 
	 * @return
	 */
	public List<VolteQualityBadRoad> queryNullRoadName() {
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteQualityBadRoad.class);
		createCriteria.add(Restrictions.isNull("m_stRoadName"));
		return createCriteria.list();
	}

	/**
	 * 添加某条质差路段的路段名称
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public void addQBRRoadName(String roadName, Long qbrId) {
		if (StringUtils.hasText(roadName) && null != qbrId) {
			String addHQL = "update VolteQualityBadRoad qbr set qbr.m_stRoadName=:ROADNAME where qbr.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", qbrId, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}
}
