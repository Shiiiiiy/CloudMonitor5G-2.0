package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverTianKuiAdjustCEDE;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverTianKuiConnectReverseCEDE;
/**
 * 呼叫建立时延异常弱覆盖原因--天馈接反详情Dao
 * @explain
 * @name WeakCoverTianKuiConnectReverseCEDEDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:44:38
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverTianKuiConnectReverseCEDEDao extends
		GenericHibernateDao<WeakCoverTianKuiConnectReverseCEDE, Long> {

	/**
	 * 根据弱覆盖实体获取天馈接反详情
	 * 
	 * @param weakCover
	 * @return
	 */
	public List<WeakCoverTianKuiConnectReverseCEDE> queryWeakCoverTianKuiConnectReverseCEDEByWC(
			 VolteCallEstablishDelayExceptionWeakCover weakCover) {

		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverTianKuiConnectReverseCEDE.class);
		createCriteria.add(Restrictions.eq("volteCallEstablishDelayExceptionWeakCover", weakCover));
		List<WeakCoverTianKuiConnectReverseCEDE> list=(List<WeakCoverTianKuiConnectReverseCEDE>)createCriteria.list();
		if(list!=null)return list;
		else return null;
		}
}
