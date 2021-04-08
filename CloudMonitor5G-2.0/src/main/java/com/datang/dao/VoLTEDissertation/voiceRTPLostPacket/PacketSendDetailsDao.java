package com.datang.dao.VoLTEDissertation.voiceRTPLostPacket;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.voiceRTPLostPacket.PacketSendDetails;

/**
 * @explain
 * @name PacketSendDetailsDao
 * @author shenyanwei
 * @date 2017年2月16日下午1:11:14
 */
@Repository
@SuppressWarnings("all")
public class PacketSendDetailsDao extends
		GenericHibernateDao<PacketSendDetails, Long> {
	/**
	 * 根据丢包ID和端口类型查询各端发送详情
	 * 
	 * @param parid
	 * @return
	 */
	public List<PacketSendDetails> queryPacketSendDetailsByPerid(Long parid,
			Integer index) {
		if (null == parid) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				PacketSendDetails.class);
		Criteria criteria = createCriteria.createCriteria("rtpLostPacket");
		criteria.add(Restrictions.eq("id", parid));
		createCriteria.add(Restrictions.eq("index", index));
		return createCriteria.list();
	}
}
