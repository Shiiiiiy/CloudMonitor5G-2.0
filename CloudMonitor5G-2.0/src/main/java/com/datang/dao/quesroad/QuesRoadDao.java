package com.datang.dao.quesroad;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.service.influx.bean.QuesRoadThreshold;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class QuesRoadDao extends GenericHibernateDao<QuesRoadThreshold, Long> {

    public List<QuesRoadThreshold> queryAll() {
        Collection<QuesRoadThreshold> list = findAll();
        return new ArrayList<>(list);
    }
}