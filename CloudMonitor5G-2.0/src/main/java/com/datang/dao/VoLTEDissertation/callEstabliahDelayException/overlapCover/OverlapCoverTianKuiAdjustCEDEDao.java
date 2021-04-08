package com.datang.dao.VoLTEDissertation.callEstabliahDelayException.overlapCover;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.OverlapCoverTianKuiAdjustCEDE;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCover;

/**
 * 重叠覆盖问题天馈小区详情实现Dao
 * 
 * @explain
 * @name OverlapCoverTianKuiAdjustCEDEDao
 * @author shenyanwei
 * @date 2016年5月25日上午9:33:17
 */
@Repository
@SuppressWarnings("all")
public class OverlapCoverTianKuiAdjustCEDEDao extends
		GenericHibernateDao<OverlapCoverTianKuiAdjustCEDE, Long> {

	/**
	 * 根据重叠覆盖的实体获取天馈小区详情
	 * 
	 * @param overlapCover
	 * @return
	 */
	public List<OverlapCoverTianKuiAdjustCEDE> queryOverlapCoverTianKuiAdjustCEDEByOC(
			VolteCallEstablishDelayExceptionOverlapCover overlapCover) {

		Criteria createCriteria = this.getHibernateSession().createCriteria(
				OverlapCoverTianKuiAdjustCEDE.class);
		createCriteria.add(Restrictions.eq(
				"volteCallEstablishDelayExceptionOverlapCover", overlapCover));
		List<OverlapCoverTianKuiAdjustCEDE> list = (List<OverlapCoverTianKuiAdjustCEDE>) createCriteria
				.list();
		if (list != null)
			return list;
		else
			return null;
	}
}
