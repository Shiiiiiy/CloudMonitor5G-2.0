package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamPingPongCutEvent;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamQualityBadPingPong;

/**
 * 流媒体专题---流媒体视频质差---乒乓切换--切换事件详情Dao
 * 
 * @explain
 * @name PingPongCutEventDao
 * @author shenyanwei
 * @date 2017年10月23日上午10:00:25
 */
@Repository
@SuppressWarnings("all")
public class StreamPingPongCutEventDao extends
		GenericHibernateDao<StreamPingPongCutEvent, Long> {
	/**
	 * 根据乒乓切换问题获取乒乓切换中的切换事件详情
	 * 
	 * @param badRoadWeakCover
	 * @return
	 */
	public List<StreamPingPongCutEvent> queryPingPongCutEvent(
			StreamQualityBadPingPong streamQualityBadPingPong) {

		if (null == streamQualityBadPingPong
				|| null == streamQualityBadPingPong.getId()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamPingPongCutEvent.class);
		Criteria criteria = createCriteria
				.createCriteria("streamQualityBadPingPong");
		criteria.add(Restrictions.eq("id", streamQualityBadPingPong.getId()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("cutTime"), "cutTime");
		projectionList.add(Projections.groupProperty("srcPci"), "srcPci");
		projectionList.add(Projections.groupProperty("dstPci"), "dstPci");
		projectionList.add(Projections.groupProperty("eventName"), "eventName");
		projectionList.add(Projections.groupProperty("srcCellName"),
				"srcCellName");
		projectionList.add(Projections.groupProperty("srcCellId"), "srcCellId");
		projectionList.add(Projections.groupProperty("srcEarfcn"), "srcEarfcn");
		projectionList.add(Projections.groupProperty("dstCellName"),
				"dstCellName");
		projectionList.add(Projections.groupProperty("dstCellId"), "dstCellId");
		projectionList.add(Projections.groupProperty("dstEarfcn"), "dstEarfcn");
		// projectionList.add(Projections.);
		createCriteria.setProjection(projectionList);
		createCriteria.add(Restrictions.gt("srcEarfcn", 0L));
		createCriteria.add(Restrictions.gt("dstEarfcn", 0L));
		createCriteria.addOrder(Order.asc("cutTime"));
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				StreamPingPongCutEvent.class);
		createCriteria.setResultTransformer(resultTransformer);
		// createCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return createCriteria.list();
	}

}
