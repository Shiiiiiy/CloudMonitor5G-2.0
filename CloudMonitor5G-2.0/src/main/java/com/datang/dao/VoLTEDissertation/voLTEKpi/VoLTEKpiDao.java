/**
 * 
 */
package com.datang.dao.VoLTEDissertation.voLTEKpi;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.wholePreview.VoLteKpi;

/**
 * volteKPI的DAO层
 * 
 * @author yinzhipeng
 * @date:2015年11月21日 上午11:57:45
 * @version
 */
@Repository
@SuppressWarnings("all")
public class VoLTEKpiDao extends GenericHibernateDao<VoLteKpi, Integer> {
	/**
	 * 获取所有VoLTEKpi
	 * 
	 * @return
	 */
	public List<VoLteKpi> queryAllVoLTEKpi() {
		List<VoLteKpi> kpis = new ArrayList<>();
		kpis.addAll(findAll());
		return kpis;
	}

	/**
	 * 根据{reportType}报表类型{stairClassify}一级分类获取{visible}=1的VoLTEKpi
	 * 
	 * @param reportType
	 * @param stairClassify
	 * @return
	 */
	public List<VoLteKpi> queryVoLTEKpiListByParam(String reportType,
			String stairClassify) {
		if (!StringUtils.hasText(reportType)
				|| !StringUtils.hasText(stairClassify)) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VoLteKpi.class);
		createCriteria.add(Restrictions.eq("reportType", reportType.trim()));
		createCriteria.add(Restrictions.like("stairClassify",
				stairClassify.trim(), MatchMode.ANYWHERE));
		createCriteria.add(Restrictions.eq("visible", 1));
		return createCriteria.list();
	}
}
