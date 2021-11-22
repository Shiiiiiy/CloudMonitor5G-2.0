package com.datang.dao.railway;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.railway.TrainFahrplanPojo;
import com.datang.domain.railway.TrainPojo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 14:33
 * @Version 1.0
 **/
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TrainFahrplanDao extends GenericHibernateDao<TrainFahrplanPojo, Long> {

    public TrainFahrplanPojo findFahrplanByCode(String trainCode) {
        Criteria createCriteria = this.getHibernateSession().createCriteria(TrainFahrplanPojo.class);
        createCriteria.add(Restrictions.eq("trainCode", trainCode));
        TrainFahrplanPojo pojo = (TrainFahrplanPojo)createCriteria.uniqueResult();
        return pojo;
    }
}
