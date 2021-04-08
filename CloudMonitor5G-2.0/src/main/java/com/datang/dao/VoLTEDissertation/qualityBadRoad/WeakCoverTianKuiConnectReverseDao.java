/**
 * 
 */
package com.datang.dao.VoLTEDissertation.qualityBadRoad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover.VolteQualityBadRoadWeakCover;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.weakCover.WeakCoverTianKuiConnectReverse;

/**
 * volte质量专题---volte语音质差---天馈接反小区详情Dao
 * 
 * @author yinzhipeng
 * @date:2015年11月13日 下午1:05:57
 * @version
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverTianKuiConnectReverseDao extends
		GenericHibernateDao<WeakCoverTianKuiConnectReverse, Long> {
	/**
	 * 根据根据弱覆盖路段获取弱覆盖质差路段中的天馈接反小区详情LIST
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<WeakCoverTianKuiConnectReverse> queryCellInfoByRoad(
			VolteQualityBadRoadWeakCover badRoadWeakCover) {

		if (null == badRoadWeakCover || null == badRoadWeakCover.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverTianKuiConnectReverse.class);
		Criteria criteria = createCriteria
				.createCriteria("volteQualityBadRoadWeakCover");
		criteria.add(Restrictions.eq("id", badRoadWeakCover.getId()));
		return createCriteria.list();
	}

}
