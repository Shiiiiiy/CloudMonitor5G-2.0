package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.OverlapCoverTianKuiAdjustCEDE;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverTianKuiAdjustCEDE;
/**
 * 弱覆盖原因--天馈调整小区详情实现Dao
 * @explain
 * @name WeakCoverTianKuiAdjustCEDEDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:41:37
 */
@Repository
@SuppressWarnings("all")
public class WeakCoverTianKuiAdjustCEDEDao extends GenericHibernateDao<WeakCoverTianKuiAdjustCEDE, Long> {


	/**
	 * 根据弱覆盖实体获取天馈小区详情
	 * 
	 * @param weakCover
	 * @return
	 */
	public List<WeakCoverTianKuiAdjustCEDE> queryWeakCoverTianKuiAdjustCEDEByWC(
			 VolteCallEstablishDelayExceptionWeakCover weakCover) {

		Criteria createCriteria = this.getHibernateSession().createCriteria(
				WeakCoverTianKuiAdjustCEDE.class);
		createCriteria.add(Restrictions.eq("volteCallEstablishDelayExceptionWeakCover", weakCover));
		List<WeakCoverTianKuiAdjustCEDE> list=(List<WeakCoverTianKuiAdjustCEDE>)createCriteria.list();
		if(list!=null)return list;
		else return null;
		}
}
