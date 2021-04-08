/**
 * 
 */
package com.datang.dao.gis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.gis.TestLogItemGrid500;
import com.datang.web.beans.VoLTEDissertation.compareAnalysis.TestLogItemGridBean;

/**
 * 测试日志全量指标栅格信息DAO(栅格大小500米)
 * 
 * @author yinzhipeng
 * @date:2016年6月30日 上午9:06:14
 * @version
 */
@Repository
@SuppressWarnings("all")
public class TestLogItemGrid500Dao extends
		GenericHibernateDao<TestLogItemGrid500, Long> {
	/**
	 * 根据测试日志id集合和指标类型查询汇总后的栅格(栅格汇总按栅格的id和栅格的左下和右上两点GroupBy)
	 * 
	 * @param logIds
	 * @param indexType
	 * @return
	 */
	public List<TestLogItemGridBean> getTestLogItemGrid500ByLogIdsAndIndexTypeGroupby(
			List<Long> logIds, Integer indexType) {
		List<TestLogItemGridBean> grids = new ArrayList<>();
		if (null == logIds || null == indexType) {
			return grids;
		}
		// SELECT SUM(indexNumSum) indexNumSum,SUM(indexValueSum) indexValueSum,
		// objectId,minx,miny,maxx,maxy
		// FROM TestLogItemGrid500
		// WHERE recSeqNo IN(?) AND indexType=?
		// GROUP BY objectId,minx,miny,maxx,maxy
		Criteria criteria = this.getHibernateSession().createCriteria(
				TestLogItemGrid500.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("minx").as("minx"));
		projectionList.add(Projections.groupProperty("miny").as("miny"));
		projectionList.add(Projections.groupProperty("maxx").as("maxx"));
		projectionList.add(Projections.groupProperty("maxy").as("maxy"));
		projectionList
				.add(Projections.groupProperty("objectId").as("objectId"));
		projectionList
				.add(Projections.sum("indexNumSum").as("indexNumSumLong"));
		projectionList.add(Projections.sum("indexValueSum").as(
				"indexValueSumDouble"));
		criteria.setProjection(projectionList);
		criteria.add(Restrictions.in("recSeqNo", logIds));
		criteria.add(Restrictions.eq("indexType", indexType));
		criteria.add(Restrictions.neOrIsNotNull("minx", 0f));
		criteria.add(Restrictions.neOrIsNotNull("miny", 0f));
		criteria.add(Restrictions.neOrIsNotNull("maxx", 0f));
		criteria.add(Restrictions.neOrIsNotNull("maxy", 0f));
		criteria.add(Restrictions.neOrIsNotNull("indexNumSum", 0));
		criteria.add(Restrictions.isNotNull("indexValueSum"));
		criteria.add(Restrictions.isNotNull("objectId"));
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				TestLogItemGridBean.class);
		criteria.setResultTransformer(resultTransformer);
		grids = criteria.list();
		return grids;
	}

}
