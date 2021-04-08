package com.datang.dao.VoLTEDissertation.videoQualityBad;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;

/**
 * volte质量专题---volte视频质差---整体分析Dao
 * 
 * @explain
 * @name VideoQualityBadDao
 * @author shenyanwei
 * @date 2017年5月12日上午11:11:31
 */
@Repository
@SuppressWarnings("all")
public class VideoQualityBadDao extends
		GenericHibernateDao<VideoQualityBad, Long> {
	/**
	 * 根据测试日志的id集合获取所有质差公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBad> queryVideoQualityBadsByLogIds(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBad.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("time"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据质差路段的id集合获取所有质差公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<VideoQualityBad> queryVideoQualityBadsByBadLogIds(List<Long> ids) {
		if (null == ids || 0 == ids.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				VideoQualityBad.class);
		if (ids.size() > 1000) {
			Disjunction dis = Restrictions.disjunction();
			List<List<Long>> segmentationList = segmentationList(ids, 999);
			for (List<Long> list : segmentationList) {
				dis.add(Restrictions.in("id", list));
			}
			createCriteria.add(dis);
		} else {
			createCriteria.add(Restrictions.in("id", ids));
		}
		createCriteria.addOrder(Order.desc("time"));
		return createCriteria.list();
	}

	/**
	 * 获取VMOS分布个数
	 * 
	 * @param recSeqNo
	 * @return
	 */
	public List queryList(List<Long> recSeqNo) {
		SQLQuery query = this
				.getHibernateSession()
				.createSQLQuery(
						"select sum(KPI776) as KPI776,sum(KPI777) as KPI777 ,sum(KPI778) as KPI778,sum(KPI779) as KPI779,sum(KPI775) as KPI775 from IADS_ADT_LTE_DATA where RECSEQNO in (:ids)");
		// query.setParameter(0, recSeqNo);
		query.setParameterList("ids", recSeqNo);
		List list = query.list();
		return list;
	}

	/**
	 * 分割List
	 * 
	 * @param targe
	 * @param size
	 * @return
	 */
	public static List<List<Long>> segmentationList(List<Long> targe, int size) {
		List<List<Long>> listArr = new ArrayList<List<Long>>();
		// 获取被拆分的数组个数
		int arrSize = targe.size() % size == 0 ? targe.size() / size : targe
				.size() / size + 1;
		for (int i = 0; i < arrSize; i++) {
			List<Long> sub = new ArrayList<Long>();
			// 把指定索引数据放入到list中
			for (int j = i * size; j <= size * (i + 1) - 1; j++) {
				if (j <= targe.size() - 1) {
					sub.add(targe.get(j));
				}
			}
			listArr.add(sub);
		}
		return listArr;
	}

	/**
	 * 添加某条质差路段的路段名称
	 * 
	 * @param roadName
	 * @param qbrId
	 */
	public void addQBRRoadName(String roadName, Long qbrId) {
		if (StringUtils.hasText(roadName) && null != qbrId) {
			String addHQL = "update VideoQualityBad qbr set qbr.m_stRoadName=:ROADNAME where qbr.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", qbrId, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}
}
