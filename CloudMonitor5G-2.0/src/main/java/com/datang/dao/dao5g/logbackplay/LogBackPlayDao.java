package com.datang.dao.dao5g.logbackplay;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testLogItem.IEItem;
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
}