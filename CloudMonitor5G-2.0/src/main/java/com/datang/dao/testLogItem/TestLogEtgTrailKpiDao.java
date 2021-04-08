/**
 * 
 */
package com.datang.dao.testLogItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.testLogItem.TestLogEtgTrailKpiPojo;

/**
 * 轨迹指标---dao
 * 
 * @author lucheng
 * @date:2020年9月9日 下午1:23:31
 * @version
 */
@Repository
@SuppressWarnings("all")
public class TestLogEtgTrailKpiDao extends GenericHibernateDao<TestLogEtgTrailKpiPojo, Long> {
	/**
	 * 多条件分页
	 * 
	 * @param pageList
	 * @return
	 */
	public List<TestLogEtgTrailKpiPojo> findTrailKpiByParam(PageList pageList) {
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogEtgTrailKpiPojo.class);

		Object nameCn = pageList.getParam("nameCn");
		Object nameEn = pageList.getParam("nameEn");
		Object testService = pageList.getParam("testService");
		Object type = pageList.getParam("type");
		Object comments = pageList.getParam("comments");
		Object classify = pageList.getParam("classify");
		Object trailLevel = pageList.getParam("trailLevel");
		
		criteria.add(Restrictions.eq("deleteTag", "0"));
		
		if (StringUtils.hasText((String)nameCn)) {
			criteria.add(Restrictions.eq("nameCn", nameCn));
		}
		
		if (StringUtils.hasText((String)nameEn)) {
			criteria.add(Restrictions.eq("nameEn", nameEn));
		}
		
		if (StringUtils.hasText((String)testService)) {
			criteria.add(Restrictions.eq("testService", testService));
		}
		
		if (StringUtils.hasText((String)type)) {
			criteria.add(Restrictions.eq("type", type));
		}
		
		if (StringUtils.hasText((String)comments)) {
			criteria.add(Restrictions.eq("comment", comments));
		}
		
		if (StringUtils.hasText((String)classify)) {
			criteria.add(Restrictions.eq("classify", classify));
		}
		
		if (StringUtils.hasText((String)trailLevel)) {
			String[] str = trailLevel.toString().replace(" ", "").replace("0", "小区类").replace("1", "基站类").split(",");
			List<String> asList = Arrays.asList(str);
			criteria.add(Restrictions.in("kpiLevel", asList));
		}
		
		criteria.addOrder(Order.asc("id"));

		List list = criteria.list();
		
		return list;
	}

}
