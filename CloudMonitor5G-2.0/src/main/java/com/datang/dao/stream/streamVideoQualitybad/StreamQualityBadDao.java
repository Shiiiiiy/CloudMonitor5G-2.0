package com.datang.dao.stream.streamVideoQualitybad;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;

/**
 * 流媒体专题----流媒体视频质差,视频质差公共指标Dao
 * 
 * @explain
 * @name StreamQualityBadDao
 * @author shenyanwei
 * @date 2017年10月23日上午9:11:30
 */
@Repository
@SuppressWarnings("all")
public class StreamQualityBadDao extends
		GenericHibernateDao<StreamQualityBad, Long> {
	/**
	 * 根据测试日志的id集合获取所有质差公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBad> queryStreamQualityBadsByLogIds(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamQualityBad.class);
		Criteria criteria = createCriteria.createCriteria("testLogItem");
		criteria.add(Restrictions.in("recSeqNo", testLogItemIds));
		createCriteria.addOrder(Order.desc("startTime"));
		criteria.addOrder(Order.desc("recSeqNo"));
		return createCriteria.list();
	}

	/**
	 * 根据质差路段的id集合获取所有质差公共参数
	 * 
	 * @param testLogItemIds
	 * @return
	 */
	public List<StreamQualityBad> queryStreamQualityBadsByBadLogIds(
			List<Long> ids) {
		if (null == ids || 0 == ids.size()) {
			return new ArrayList<>();
		}
		Criteria createCriteria = this.getHibernateSession().createCriteria(
				StreamQualityBad.class);
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
		createCriteria.addOrder(Order.desc("startTime"));
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
						"select sum(KPI960) as KPI960 from IADS_ADT_LTE_DATA where RECSEQNO in (:ids)");
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
			String addHQL = "update StreamQualityBad qbr set qbr.m_stRoadName=:ROADNAME where qbr.id=:ID";
			Query createQuery = this.getHibernateSession().createQuery(addHQL);
			createQuery.setParameter("ROADNAME", roadName, StringType.INSTANCE);
			createQuery.setParameter("ID", qbrId, LongType.INSTANCE);
			createQuery.executeUpdate();
		}
	}

	/**
	 * 查询Vmos分布值
	 * 
	 * @param recSeqNo
	 * @return
	 */
	public List queryVmosValue(List<Long> recSeqNo) {

		String hql = "select SUM(KPI963),SUM(KPI964),SUM(KPI965),SUM(KPI966),SUM(KPI967),SUM(KPI968) from IADS_ADT_LTE_DATA   ";

		if (recSeqNo.size() > 1000) {
			List<List<Long>> segmentationList = segmentationList(recSeqNo, 999);
			for (int i = 0; i < segmentationList.size(); i++) {
				if (-1 != hql.indexOf("WHERE")) {
					hql += " OR RECSEQNO IN (:logId" + i + ") ";
				} else {
					hql += " WHERE RECSEQNO IN (:logId" + i + ")  ";
				}
			}

		} else {
			hql += " WHERE RECSEQNO IN (:logId) ";
		}

		SQLQuery query = this.getHibernateSession().createSQLQuery(hql);
		if (recSeqNo.size() > 1000) {
			List<List<Long>> segmentationList = segmentationList(recSeqNo, 999);
			for (int i = 0; i < segmentationList.size(); i++) {
				query.setParameterList("logId" + i + "",
						segmentationList.get(i));
			}

		} else {
			query.setParameterList("logId", recSeqNo);
		}
		Object[] returnList = (Object[]) (query.list().get(0));

		return Arrays.asList(returnList);
	}
}
