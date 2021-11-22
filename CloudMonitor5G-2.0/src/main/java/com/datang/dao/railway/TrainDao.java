package com.datang.dao.railway;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.railway.TrainPojo;
import com.datang.domain.railway.TrainStationPojo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 14:03
 * @Version 1.0
 **/
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TrainDao extends GenericHibernateDao<TrainPojo, Long> {

    public TrainPojo findTrainByCode(String trainCode) {
        Criteria createCriteria = this.getHibernateSession().createCriteria(TrainPojo.class);
        createCriteria.add(Restrictions.eq("trainCode", trainCode));
        TrainPojo pojo = (TrainPojo)createCriteria.uniqueResult();
        return pojo;
    }
}