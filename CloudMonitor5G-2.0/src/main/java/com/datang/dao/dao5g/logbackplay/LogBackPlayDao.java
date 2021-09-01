package com.datang.dao.dao5g.logbackplay;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testLogItem.IEItem;
import com.datang.domain.testLogItem.PcapData;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class LogBackPlayDao extends GenericHibernateDao<IEItem, Long> {

    public List<IEItem> getRecrodsByLogId(Long logId) {
        Criteria criteria = this.getHibernateSession().createCriteria(IEItem.class);
        criteria.add(Restrictions.eq("logId",logId));
        criteria.addOrder(Order.asc("time"));
        List<IEItem> list = (List<IEItem>)criteria.list();
        return list;
    }


    public List<PcapData> pcapDatas(long logId) {
        Criteria criteria = this.getHibernateSession().createCriteria(PcapData.class);
        criteria.add(Restrictions.eq("testlogitemrecseqno",logId));
        criteria.addOrder(Order.asc("testtime"));
        List<PcapData> list = (List<PcapData>)criteria.list();
        return list;
    }
}