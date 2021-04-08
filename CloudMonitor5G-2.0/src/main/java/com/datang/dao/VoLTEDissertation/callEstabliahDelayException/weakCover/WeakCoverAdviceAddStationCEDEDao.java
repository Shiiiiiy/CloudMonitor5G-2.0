package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverAdviceAddStationCEDE;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverTianKuiAdjustCEDE;
/**
 * 弱覆盖问题---建议加站实现Dao
 * @explain
 * @name WeakCoverAdviceAddStationCEDEDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:38:44
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverAdviceAddStationCEDEDao extends
		GenericHibernateDao<WeakCoverAdviceAddStationCEDE, Long> {
	/**
	 * 根据弱覆盖实体获取建议加站详情
	 * 
	 * @param weakCover
	 * @return
	 */
	public List<WeakCoverAdviceAddStationCEDE> queryWeakCoverAdviceAddStationCEDEByWC(
			 VolteCallEstablishDelayExceptionWeakCover weakCover) {

		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverAdviceAddStationCEDE.class);
		createCriteria.add(Restrictions.eq("volteCallEstablishDelayExceptionWeakCover", weakCover));
		List<WeakCoverAdviceAddStationCEDE> list=(List<WeakCoverAdviceAddStationCEDE>)createCriteria.list();
		if(list!=null)return list;
		else return null;
		}
}
