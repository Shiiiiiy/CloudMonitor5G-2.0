package com.datang.dao.VoLTEDissertation.voiceRTPLostPacket;

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
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.VolteVoiceRTPLostPacket;

/**
 * 语音RTP连续丢包公共指标Dao
 * 
 * @explain
 * @name VolteVoiceRTPLostPacketDao
 * @author shenyanwei
 * @date 2017年2月14日上午9:43:47
 */
@Repository
@SuppressWarnings("all")
public class VolteVoiceRTPLostPacketDao extends
		GenericHibernateDao<VolteVoiceRTPLostPacket, Long> {

	/**
	 * 添加某条切换失败的路段名称
	 * 
	 * @param roadName
	 * @param droppingId
	 */
	public void addQBRRoadName(String roadName, Long id) {
		if (StringUtils.hasText(roadName) && null != id) {
			String addHQL = "update VolteVoiceRTPLostPacket vd set vd.m_stRoadName=:ROADNAME where vd.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", id, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}

	/**
	 * 根据测试日志的id集合获取所有语音RTP连续丢包
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VolteVoiceRTPLostPacket> queryVolteVoiceRTPLostPacketsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VolteVoiceRTPLostPacket.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}
}
