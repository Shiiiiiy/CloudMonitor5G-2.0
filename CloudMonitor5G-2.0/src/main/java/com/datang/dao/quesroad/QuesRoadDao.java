package com.datang.dao.quesroad;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.service.influx.bean.QuesRoadThreshold;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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

    /**
     * 根据英文名称查询
     *
     * @param nameEn
     * @return
     */
    public QuesRoadThreshold queryByEnName(String nameEn) {
        Criteria createCriteria = this.getHibernateSession().createCriteria(QuesRoadThreshold.class);
        createCriteria.add(Restrictions.eq("name", nameEn));
        QuesRoadThreshold aboutThreshold = (QuesRoadThreshold) createCriteria .uniqueResult();
        return aboutThreshold;
    }




}