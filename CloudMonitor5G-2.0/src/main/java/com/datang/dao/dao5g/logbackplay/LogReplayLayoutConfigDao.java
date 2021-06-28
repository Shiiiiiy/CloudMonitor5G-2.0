package com.datang.dao.dao5g.logbackplay;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.testLogItem.LayoutConfig;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shiyan
 */
@Repository
public class LogReplayLayoutConfigDao extends GenericHibernateDao<LayoutConfig, Long> {

    public List<LayoutConfig> getLayoutConfig() {
        Criteria criteria = this.getHibernateSession().createCriteria(LayoutConfig.class);
        List<LayoutConfig> list = (List<LayoutConfig>)criteria.list();
        return list;
    }

    public LayoutConfig saveConfig(LayoutConfig entity) {
         create(entity);
         return entity;
    }

    public LayoutConfig getLayoutConfig(Long id) {

        Criteria criteria = this.getHibernateSession().createCriteria(LayoutConfig.class);
        criteria.add(Restrictions.eq("id",id));
        List<LayoutConfig> list = (List<LayoutConfig>)criteria.list();
        return list.get(0);
    }

    public LayoutConfig getDefaultLayoutConfig() {

        Criteria criteria = this.getHibernateSession().createCriteria(LayoutConfig.class);
        criteria.add(Restrictions.eq("status","1"));
        List<LayoutConfig> list = (List<LayoutConfig>)criteria.list();
        if(list!=null&& list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
}
