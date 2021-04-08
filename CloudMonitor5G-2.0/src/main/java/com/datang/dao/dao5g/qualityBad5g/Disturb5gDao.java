package com.datang.dao.dao5g.qualityBad5g;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.domain5g.embbCover.EmbbCoverBadRoad;
import com.datang.domain.domain5g.embbCover.EmbbCoverCellBeamInfo;
import com.datang.domain.domain5g.qualityBad5g.BadRoadCellBeamPojo;
import com.datang.domain.domain5g.qualityBad5g.CellIndexRoadPojo;
import com.datang.domain.domain5g.qualityBad5g.HighCoverRoadPojo;
import com.datang.domain.domain5g.qualityBad5g.InterfereRoadPojo;
import com.datang.domain.domain5g.qualityBad5g.OverlayCoverRoadPojo;
import com.datang.domain.domain5g.qualityBad5g.SanChaoSiteRoadPojo;

/**
 * 干扰路段dao
 * 
 * @author maxuancheng
 * @date:2019年3月12日 上午9:22:08
 * @version
 */
@Repository
@SuppressWarnings("all")
public class Disturb5gDao extends GenericHibernateDao<InterfereRoadPojo, Long>{

	/**
	 * 根据测试日志的id集合获取干扰问题路段
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<InterfereRoadPojo> queryDisturbRoadsByLogIds(
			List<Long> testLogItemIds) {

		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				InterfereRoadPojo.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		// criteria.add(Restrictions.eq("serviceType", "1,"));
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		List<InterfereRoadPojo> list = createCriteria.list();
		return list;
	}

	/**
	 * 分析干扰问题质差路段小区详情表
	 * @author maxuancheng
	 * @param roadId
	 * @return
	 */
	public List queryCellInfoByRoad(Long roadId) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(CellIndexRoadPojo.class);
		createCriteria.add(Restrictions.eq("rid", roadId));
		List<CellIndexRoadPojo> list = createCriteria.list();
		return list;
	}

	/**
	 * 三超站点整改详情表
	 * @author maxuancheng
	 * @param roadId
	 * @return
	 */
	public List doDisturbSanChaoCellAnalysis(Long roadId) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(SanChaoSiteRoadPojo.class);
		createCriteria.add(Restrictions.eq("rid", roadId));
		List<SanChaoSiteRoadPojo> list = createCriteria.list();
		return list;
	}

	/**
	 * 建议天馈调整——过覆盖
	 * @author maxuancheng
	 * @param roadId 路段id
	 * @return 
	 */
	public List doDisturbTiankuiHighCoverAnalysis(Long roadId) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(HighCoverRoadPojo.class);
		createCriteria.add(Restrictions.eq("rid", roadId));
		List<HighCoverRoadPojo> list = createCriteria.list();
		return list;
	}

	/**
	 * 建议天馈调整——重叠覆盖
	 * @author maxuancheng
	 * @param roadId 路段id
	 * @return 
	 */
	public List doDisturbTiankuiOverCoverAnalysis(Long roadId) {
		Criteria createCriteria = this.getHibernateSession().createCriteria(OverlayCoverRoadPojo.class);
		createCriteria.add(Restrictions.eq("rid", roadId));
		List<OverlayCoverRoadPojo> list = createCriteria.list();
		return list;
	}

	/**
	 * 根据测试日志的id集合获取所有质差路段
	 * @author maxuancheng
	 * @param ids
	 * @param coverType
	 * @return
	 */
	public List<InterfereRoadPojo> queryAllBadRoadOfTestLogIds(List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				InterfereRoadPojo.class);
		createCriteria.addOrder(Order.desc("startTime"));

		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		criteria.addOrder(Order.desc("recSeqNo"));

		return createCriteria.list();
	}

	public List queryCellBeamInfoByCell(Long cellInfoId) {
		if (null == cellInfoId) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				BadRoadCellBeamPojo.class);
		createCriteria.addOrder(Order.asc("beamNo"));
		createCriteria.add(Restrictions.eq("cid", cellInfoId));
		return createCriteria.list();
	}

	/**
	 * 获取多个日志id下所有质差路段
	 * @author maxuancheng
	 * @param ids
	 * @return
	 */
	public List<InterfereRoadPojo> getEmbbCoverBadRoadByLogIds(List<Long> ids) {
		if (null == ids || 0 == ids.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				InterfereRoadPojo.class);
		createCriteria.addOrder(Order.desc("startTime"));

		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", ids));
		criteria.addOrder(Order.desc("recSeqNo"));

		return createCriteria.list();
	}
}
