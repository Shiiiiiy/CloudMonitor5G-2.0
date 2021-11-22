package com.datang.dao.railway;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.railway.SubwayXmlTablePojo;
import com.datang.domain.railway.TrainXmlTablePojo;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 13:20
 * @Version 1.0
 **/
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class SubwayXmlTableDao extends GenericHibernateDao<SubwayXmlTablePojo, Long> {

    public List<SubwayXmlTablePojo> findSubwayXmlTable(PageList pageList) {
        Criteria createCriteria = this.getHibernateSession().createCriteria(SubwayXmlTablePojo.class);
        addParamCondition(createCriteria,pageList);
        List<SubwayXmlTablePojo> list = (List<SubwayXmlTablePojo>)createCriteria.list();
        return list;
    }

    public EasyuiPageList doPageQuery(PageList pageList){
        Criteria criteria = this.getHibernateSession().createCriteria(SubwayXmlTablePojo.class);

        addParamCondition(criteria,pageList);

        criteria.addOrder(Order.desc("id"));

        long total = 0;
        criteria.setProjection(null);
        int rowsCount = pageList.getRowsCount();// 每页记录数
        int pageNum = pageList.getPageNum();// 页码
        criteria.setFirstResult((pageNum - 1) * rowsCount);
        criteria.setMaxResults(rowsCount);
        List list = criteria.list();
        total = doPageQueryCount(pageList);
        EasyuiPageList easyuiPageList = new EasyuiPageList();
        easyuiPageList.setRows(list);
        easyuiPageList.setTotal(total + "");
        return easyuiPageList;
    }

    private long doPageQueryCount(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(SubwayXmlTablePojo.class);
        addParamCondition(criteria,pageList);
        Long total = 0L;
        criteria.setProjection(null);
        criteria.setFirstResult(0);
        total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return total;
    }

    private void addParamCondition(Criteria createCriteria, PageList pageList) {
        if ( pageList.getParam("startTime") != null) {
            createCriteria.add(Restrictions.ge("updateTimeLong", Long.valueOf(pageList.getParam("startTime").toString())));
        }
        if (pageList.getParam("endTime") != null) {
            createCriteria.add(Restrictions.le("updateTimeLong", Long.valueOf(pageList.getParam("endTime").toString())));
        }
        if ( pageList.getParam("lineXml") != null) {
            createCriteria.add(Restrictions.eq("lineXml", pageList.getParam("lineXml")));
        }

        if (StringUtils.hasText((String)pageList.getParam("city"))) {
            createCriteria.add(Restrictions.like("city", (String)pageList.getParam("city"),MatchMode.ANYWHERE));
        }

        if (StringUtils.hasText((String)pageList.getParam("lineNo"))) {
            createCriteria.add(Restrictions.like("lineNo", (String)pageList.getParam("lineNo"),MatchMode.ANYWHERE));
        }
    }
}
