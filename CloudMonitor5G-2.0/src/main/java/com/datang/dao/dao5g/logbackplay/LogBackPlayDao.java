package com.datang.dao.dao5g.logbackplay;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.testLogItem.IEItem;
import com.datang.domain.testLogItem.PcapData;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        criteria.setFirstResult(0);
        criteria.setMaxResults(3000);

        List<PcapData> list = (List<PcapData>)criteria.list();
        return list;
    }

    public List<PcapData> syncPcapDatas(long logId, String time) throws ParseException {

        List<PcapData> list = new ArrayList<>();

        if(StringUtils.hasText(time)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if(time.length() > 19 ){
                time = time.substring(0,19);
            }
            Date date = sdf.parse(time);

            Criteria criteria = this.getHibernateSession().createCriteria(PcapData.class);
            criteria.add(Restrictions.eq("testlogitemrecseqno",logId));
            criteria.add(Restrictions.ge("testtime", date.getTime()));


            criteria.addOrder(Order.asc("testtime"));
            criteria.setFirstResult(0);
            criteria.setMaxResults(3000);
            list = (List<PcapData>)criteria.list();

            if(list.size()<1){
                Criteria criteria2 = this.getHibernateSession().createCriteria(PcapData.class);
                criteria2.add(Restrictions.eq("testlogitemrecseqno",logId));
                criteria2.add(Restrictions.le("testtime", date.getTime()));

                criteria2.addOrder(Order.desc("testtime"));
                criteria2.setFirstResult(0);
                criteria2.setMaxResults(3000);
                list = (List<PcapData>)criteria2.list();
            }
        }
        return list;
    }
}