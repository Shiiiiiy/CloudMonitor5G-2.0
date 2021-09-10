package com.datang.dao.knowfeeling;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.GenericHibernateDao;
import com.datang.common.util.StringUtils;
import com.datang.domain.knowfeeling.KnowFeelingReportTask;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.beans.report.ReportRequertBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shiyan
 * @date 2021/9/6 13:22
 */
@Repository
public class KnowFeelingDao extends GenericHibernateDao<KnowFeelingReportTask, Long> {



    @Autowired
    private ITestLogItemService testLogItemService;

    /**
     * 多条件分页
     *
     * @param pageList
     * @return
     */
    public EasyuiPageList getPageItem(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                KnowFeelingReportTask.class);
        ReportRequertBean pageParams = (ReportRequertBean) pageList
                .getParam("pageQueryBean");

        // 筛选创建人
        String createrName = pageParams.getCreaterName();
        if (StringUtils.hasText(createrName)) {
            criteria.add(Restrictions.like("createrName", createrName,
                    MatchMode.ANYWHERE));
        }

        String cityIds = pageParams.getCityIds();
        if (StringUtils.hasText(cityIds)) {
            String[] citys = cityIds.split(",");
            criteria.add(Restrictions.in("cityIds", Arrays.asList(citys)));
        }

        // 筛选任务名称
        String name = pageParams.getName();
        if (StringUtils.hasText(name)) {
            criteria.add(Restrictions.like("name", name.trim(),
                    MatchMode.ANYWHERE));
        }
        // 筛选发生时间
        Date beginDate = pageParams.getBeginDate();
        if (null != beginDate) {
            criteria.add(Restrictions.ge("creatDateLong", beginDate.getTime()));
        }
        // 筛选结束时间
        Date endDate = pageParams.getEndDate();
        if (null != endDate) {
            criteria.add(Restrictions.le("endDateLong", endDate.getTime()));
        }

        criteria.addOrder(Order.desc("creatDateLong"));
        criteria.setProjection(null);
        int rowsCount = pageList.getRowsCount();// 每页记录数
        int pageNum = pageList.getPageNum();// 页码
        criteria.setFirstResult((pageNum - 1) * rowsCount);
        criteria.setMaxResults(rowsCount);
        List list = criteria.list();

        long total = 0;
        total = getPageItemCount(pageList);
        EasyuiPageList easyuiPageList = new EasyuiPageList();
        easyuiPageList.setRows(list);
        easyuiPageList.setTotal(total + "");
        return easyuiPageList;
    }


    public long getPageItemCount(PageList pageList) {
        Criteria criteria = this.getHibernateSession().createCriteria(
                StatisticeTask.class);
        ReportRequertBean pageParams = (ReportRequertBean) pageList
                .getParam("pageQueryBean");

        // 筛选创建人
        String createrName = pageParams.getCreaterName();
        if (StringUtils.hasText(createrName)) {
            criteria.add(Restrictions.like("createrName", createrName,
                    MatchMode.ANYWHERE));
        }
        // 筛选任务名称
        String name = pageParams.getName();
        if (StringUtils.hasText(name)) {
            criteria.add(Restrictions.like("name", name.trim(),
                    MatchMode.ANYWHERE));
        }
        // 筛选发生时间
        Date beginDate = pageParams.getBeginDate();
        if (null != beginDate) {
            criteria.add(Restrictions.ge("creatDateLong", beginDate.getTime()));
        }
        // 筛选结束时间
        Date endDate = pageParams.getEndDate();
        if (null != endDate) {
            criteria.add(Restrictions.le("endDateLong", endDate.getTime()));
        }

        criteria.setProjection(null);
        long total = 0;
        criteria.setFirstResult(0);
        total = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();
        return total;
    }




    public List<Map<String,Object>> queryKnowFeelingStatics(String... logName) {

        String sql =
          "  SELECT    i.file_name , " +
          "  ( SELECT g.name FROM iads_terminal_group g WHERE g.id =   (SELECT terminal_group_id FROM IADS_TERMINAL  WHERE box_id = i.box_id  ) )   AS area , " +
          "   i.operator_name,  KF.* FROM  iads_testlog_item i LEFT JOIN   ( " +

                " SELECT  logname,traffic, to_char(to_timestamp(min(starttime)/1000),'YYYY-MM-DD HH24:MI:SS')  AS STARTTIME," +
                " to_char(to_timestamp(max(endtime) /1000),'YYYY-MM-DD HH24:MI:SS') AS ENDTIME,  " +
                "(CASE WHEN MAX(netmode) = 6 THEN 'LTE' WHEN MAX(netmode) = 8 THEN 'NR' ELSE '' END) AS KF01," +
                "TRUNC(CAST((CASE WHEN  SUM(ltersrp_p) = 0 THEN 0  ELSE  SUM(ltersrp_c)/SUM(ltersrp_p)  END ) AS NUMERIC),2)  AS KF02 ,"+
                "TRUNC(CAST((CASE WHEN  SUM(ltesinr_p) = 0 THEN 0  ELSE  SUM(ltesinr_c)/SUM(ltesinr_p)  END )  AS NUMERIC),2) AS KF03 ,"+
                "TRUNC(CAST((CASE WHEN  SUM(nrrsrp_p) = 0 THEN 0  ELSE  SUM(nrrsrp_c)/SUM(nrrsrp_p)  END ) AS NUMERIC),2) AS KF04 ,"+
                "TRUNC(CAST((CASE WHEN  SUM(nrsinr_p) = 0 THEN 0  ELSE  SUM(nrsinr_c)/SUM(nrsinr_p)  END ) AS NUMERIC),2) AS KF05 ,"+
                "TRUNC((CASE WHEN  SUM(lterrc_p) = 0 THEN 0  ELSE  SUM(lterrc_c)/SUM(lterrc_p)  END ),4)* 100 AS KF06 ,"+
                "TRUNC((CASE WHEN  SUM(nrrrc_p) = 0 THEN 0  ELSE  SUM(nrrrc_c)/SUM(nrrrc_p)  END ),4)* 100 AS KF07 ,"+
                "TRUNC((CASE WHEN  SUM(ltesetup_p) = 0 THEN 0  ELSE  SUM(ltesetup_c)/SUM(ltesetup_p) END ),4)* 100  AS KF08 ,"+
                "TRUNC((CASE WHEN  SUM(nrsetup_p) = 0 THEN 0  ELSE  SUM(nrsetup_c)/SUM(nrsetup_p)  END ),4)* 100 AS KF09 ,"+
                "TRUNC((CASE WHEN  SUM(nbruserplanednssucc) = 0 THEN 0  ELSE  SUM(alluserplanednsdelay)/SUM(nbruserplanednssucc)  END ),0) AS KF11 ,"+
                "TRUNC((CASE WHEN  SUM(nbruserplanednsreq) = 0 THEN 0  ELSE  SUM(nbruserplanednssucc)/SUM(nbruserplanednsreq)  END ),0) AS KF12 ,"+
                "TRUNC((CASE WHEN SUM(nbrtcpconnupsucc+nbrtcpconndownsucc) = 0 THEN 0 ELSE SUM(alltcpconnupdelay+alltcpconndowndelay)/SUM(nbrtcpconnupsucc + nbrtcpconndownsucc) END ),0) AS KF13 ,"+
                "TRUNC((CASE WHEN SUM(nbrtcpconnupreq+nbrtcpconndownreq) = 0 THEN 0 ELSE SUM(nbrtcpconnupsucc+nbrtcpconndownsucc)/SUM(nbrtcpconnupreq+nbrtcpconndownreq) END ),4)*100 AS KF14 ,"+
                "TRUNC((CASE WHEN SUM(alltcpuppackages+alltcpdownpackages) = 0 THEN 0 ELSE SUM(retrtcpuppackages+retrtcpdownpackages)/SUM(alltcpuppackages+alltcpdownpackages) END ),4)*100 AS KF15 ,"+
                "TRUNC((CASE WHEN SUM(nbrtexthttpacksucc+nbrpichttpacksucc+nbrvidhttpacksucc+nbraudhttpacksucc) = 0 THEN 0 ELSE SUM(alltexthttpsuccackdelay + allpichttpsuccackdelay + allvidhttpsuccackdelay + allaudhttpsuccackdelay)/SUM(nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc  ) END ),0) AS KF16 ,"+
                "TRUNC((CASE WHEN SUM(allhttpreq) = 0 THEN 0 ELSE SUM(nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc)/SUM(allhttpreq) END ),4)*100 AS KF17 ,"+
                "TRUNC((CASE WHEN  SUM(nbrtexthttpdlsucc) = 0 THEN 0  ELSE  SUM(alltexthttpsuccdldelay)/SUM(nbrtexthttpdlsucc)  END ),0) AS KF21 ,"+
                "TRUNC((CASE WHEN  SUM(nbrpichttpdlsucc) = 0 THEN 0  ELSE  SUM(allpichttpsuccdldelay)/SUM(nbrpichttpdlsucc)  END ),0) AS KF22 ,"+
                "TRUNC((CASE WHEN  SUM(nbrvidhttpdlsucc) = 0 THEN 0  ELSE  SUM(allvidhttpsuccackdelay)/SUM(nbrvidhttpdlsucc)  END ),0) AS KF23 ,"+
                "TRUNC((CASE WHEN  SUM(nbraudhttpdlsucc) = 0 THEN 0  ELSE  SUM(allaudhttpsuccdldelay)/SUM(nbraudhttpdlsucc)  END ),0) AS KF24"+

                " from iads_knowfeeling_traffic where logname in ( :logname ) GROUP BY logname,traffic   )  AS KF   ON  i.file_name = kf.logname   WHERE i.file_name  in ( :filename ) ";

        SQLQuery query = this
                .getHibernateSession()
                .createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameterList("logname", Arrays.asList(logName));
        query.setParameterList("filename", Arrays.asList(logName));
        List list = query.list();
        return list;

    }

    public static void main(String[] args) {

        String sql =
                "  SELECT    i.file_name , " +
                        "  ( SELECT g.name FROM iads_terminal_group g WHERE g.id =   (SELECT terminal_group_id FROM IADS_TERMINAL  WHERE box_id = i.box_id  ) )   AS area , " +
                        "   i.operator_name,  KF.* FROM  iads_testlog_item i LEFT JOIN   ( " +

                        " SELECT  logname,traffic, to_char(to_timestamp(min(starttime)/1000),'YYYY-MM-DD HH24:MI:SS')  AS STARTTIME," +
                        " to_char(to_timestamp(max(endtime) /1000),'YYYY-MM-DD HH24:MI:SS') AS ENDTIME,  " +
                        " (CASE WHEN MAX(netmode) = 6 THEN 'LTE' WHEN MAX(netmode) = 8 THEN 'NR' ELSE '' END) AS KF01," +
                        "TRUNC(CAST((CASE WHEN  SUM(ltersrp_p) = 0 THEN 0  ELSE  SUM(ltersrp_c)/SUM(ltersrp_p)  END ) AS NUMERIC),2)  AS KF02 ,"+
                        "TRUNC(CAST((CASE WHEN  SUM(ltesinr_p) = 0 THEN 0  ELSE  SUM(ltesinr_c)/SUM(ltesinr_p)  END )  AS NUMERIC),2) AS KF03 ,"+
                        "TRUNC(CAST((CASE WHEN  SUM(nrrsrp_p) = 0 THEN 0  ELSE  SUM(nrrsrp_c)/SUM(nrrsrp_p)  END ) AS NUMERIC),2) AS KF04 ,"+
                        "TRUNC(CAST((CASE WHEN  SUM(nrsinr_p) = 0 THEN 0  ELSE  SUM(nrsinr_c)/SUM(nrsinr_p)  END ) AS NUMERIC),2) AS KF05 ,"+
                        "TRUNC((CASE WHEN  SUM(lterrc_p) = 0 THEN 0  ELSE  SUM(lterrc_c)/SUM(lterrc_p)  END ),4)* 100 AS KF06 ,"+
                        "TRUNC((CASE WHEN  SUM(nrrrc_p) = 0 THEN 0  ELSE  SUM(nrrrc_c)/SUM(nrrrc_p)  END ),4)* 100 AS KF07 ,"+
                        "TRUNC((CASE WHEN  SUM(ltesetup_p) = 0 THEN 0  ELSE  SUM(ltesetup_c)/SUM(ltesetup_p) END ),4)* 100  AS KF08 ,"+
                        "TRUNC((CASE WHEN  SUM(nrsetup_p) = 0 THEN 0  ELSE  SUM(nrsetup_c)/SUM(nrsetup_p)  END ),4)* 100 AS KF09 ,"+
                        "TRUNC((CASE WHEN  SUM(nbruserplanednssucc) = 0 THEN 0  ELSE  SUM(alluserplanednsdelay)/SUM(nbruserplanednssucc)  END ),0) AS KF11 ,"+
                        "TRUNC((CASE WHEN  SUM(nbruserplanednsreq) = 0 THEN 0  ELSE  SUM(nbruserplanednssucc)/SUM(nbruserplanednsreq)  END ),0) AS KF12 ,"+
                        "TRUNC((CASE WHEN SUM(nbrtcpconnupsucc+nbrtcpconndownsucc) = 0 THEN 0 ELSE SUM(alltcpconnupdelay+alltcpconndowndelay)/SUM(nbrtcpconnupsucc + nbrtcpconndownsucc) END ),0) AS KF13 ,"+
                        "TRUNC((CASE WHEN SUM(nbrtcpconnupreq+nbrtcpconndownreq) = 0 THEN 0 ELSE SUM(nbrtcpconnupsucc+nbrtcpconndownsucc)/SUM(nbrtcpconnupreq+nbrtcpconndownreq) END ),4)*100 AS KF14 ,"+
                        "TRUNC((CASE WHEN SUM(alltcpuppackages+alltcpdownpackages) = 0 THEN 0 ELSE SUM(retrtcpuppackages+retrtcpdownpackages)/SUM(alltcpuppackages+alltcpdownpackages) END ),4)*100 AS KF15 ,"+
                        "TRUNC((CASE WHEN SUM(nbrtexthttpacksucc+nbrpichttpacksucc+nbrvidhttpacksucc+nbraudhttpacksucc) = 0 THEN 0 ELSE SUM(alltexthttpsuccackdelay + allpichttpsuccackdelay + allvidhttpsuccackdelay + allaudhttpsuccackdelay)/SUM(nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc  ) END ),0) AS KF16 ,"+
                        "TRUNC((CASE WHEN SUM(allhttpreq) = 0 THEN 0 ELSE SUM(nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc)/SUM(allhttpreq) END ),4)*100 AS KF17 ,"+
                        "TRUNC((CASE WHEN  SUM(nbrtexthttpdlsucc) = 0 THEN 0  ELSE  SUM(alltexthttpsuccdldelay)/SUM(nbrtexthttpdlsucc)  END ),0) AS KF21 ,"+
                        "TRUNC((CASE WHEN  SUM(nbrpichttpdlsucc) = 0 THEN 0  ELSE  SUM(allpichttpsuccdldelay)/SUM(nbrpichttpdlsucc)  END ),0) AS KF22 ,"+
                        "TRUNC((CASE WHEN  SUM(nbrvidhttpdlsucc) = 0 THEN 0  ELSE  SUM(allvidhttpsuccackdelay)/SUM(nbrvidhttpdlsucc)  END ),0) AS KF23 ,"+
                        "TRUNC((CASE WHEN  SUM(nbraudhttpdlsucc) = 0 THEN 0  ELSE  SUM(allaudhttpsuccdldelay)/SUM(nbraudhttpdlsucc)  END ),0) AS KF24"+

                        " from iads_knowfeeling_traffic where logname in ( :logname ) GROUP BY logname,traffic   )  AS KF   ON  i.file_name = kf.logname   WHERE i.file_name  in ( :filename ) ";


        System.out.println(sql);
    }


    public List<Map<String, Object>> getKnowFeelingData(String idStr, boolean collect) {


        List<TestLogItem> logList =  testLogItemService.queryTestLogItems(idStr);

        List<String> nameList = logList.stream().map(TestLogItem::getFileName).collect(Collectors.toList());


        String nickName = " '汇总' as log_name  ";
        String groupSql = "";


        String first = "";
        String last = "";


        if(!collect){

            first =
                    "  SELECT    i.file_name as log_name , " +

                            "  KF.* FROM  iads_testlog_item i LEFT JOIN   ( ";

            last =   "  )  AS KF   ON  i.file_name = kf.logname   WHERE i.file_name  in ( :filename ) ";



            nickName = " logname ";
            groupSql =  " GROUP BY logname ";
        }

        String sql =   first +

                        " SELECT  "+ nickName +"   ,  " +
                        "TRUNC((CASE WHEN  SUM(nbruserplanednssucc) = 0 THEN 0  ELSE  SUM(alluserplanednsdelay)/SUM(nbruserplanednssucc)  END ),0) AS KF11 ,"+
                        "TRUNC((CASE WHEN  SUM(nbruserplanednsreq) = 0 THEN 0  ELSE  SUM(nbruserplanednssucc)/SUM(nbruserplanednsreq)  END ),0) AS KF12 ,"+
                        "TRUNC((CASE WHEN SUM(nbrtcpconnupsucc+nbrtcpconndownsucc) = 0 THEN 0 ELSE SUM(alltcpconnupdelay+alltcpconndowndelay)/SUM(nbrtcpconnupsucc + nbrtcpconndownsucc) END ),0) AS KF13 ,"+
                        "TRUNC((CASE WHEN SUM(nbrtcpconnupreq+nbrtcpconndownreq) = 0 THEN 0 ELSE SUM(nbrtcpconnupsucc+nbrtcpconndownsucc)/SUM(nbrtcpconnupreq+nbrtcpconndownreq) END ),4)*100 AS KF14 ,"+
                        "TRUNC((CASE WHEN SUM(alltcpuppackages+alltcpdownpackages) = 0 THEN 0 ELSE SUM(retrtcpuppackages+retrtcpdownpackages)/SUM(alltcpuppackages+alltcpdownpackages) END ),4)*100 AS KF15 ,"+
                        "TRUNC((CASE WHEN SUM(nbrtexthttpacksucc+nbrpichttpacksucc+nbrvidhttpacksucc+nbraudhttpacksucc) = 0 THEN 0 ELSE SUM(alltexthttpsuccackdelay + allpichttpsuccackdelay + allvidhttpsuccackdelay + allaudhttpsuccackdelay)/SUM(nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc  ) END ),0) AS KF16 ,"+
                        "TRUNC((CASE WHEN SUM(allhttpreq) = 0 THEN 0 ELSE SUM(nbrtexthttpacksucc + nbrpichttpacksucc + nbrvidhttpacksucc + nbraudhttpacksucc)/SUM(allhttpreq) END ),4)*100 AS KF17 ,"+
                        "TRUNC((CASE WHEN  SUM(nbrtexthttpdlsucc) = 0 THEN 0  ELSE  SUM(alltexthttpsuccdldelay)/SUM(nbrtexthttpdlsucc)  END ),0) AS KF21 ,"+
                        "TRUNC((CASE WHEN  SUM(nbrpichttpdlsucc) = 0 THEN 0  ELSE  SUM(allpichttpsuccdldelay)/SUM(nbrpichttpdlsucc)  END ),0) AS KF22 ,"+
                        "TRUNC((CASE WHEN  SUM(nbrvidhttpdlsucc) = 0 THEN 0  ELSE  SUM(allvidhttpsuccackdelay)/SUM(nbrvidhttpdlsucc)  END ),0) AS KF23 ,"+
                        "TRUNC((CASE WHEN  SUM(nbraudhttpdlsucc) = 0 THEN 0  ELSE  SUM(allaudhttpsuccdldelay)/SUM(nbraudhttpdlsucc)  END ),0) AS KF24"+

                        " from iads_knowfeeling_traffic where logname in ( :logname )  "  + groupSql  + last ;

        SQLQuery query = this
                .getHibernateSession()
                .createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameterList("logname", nameList);

        if(!collect){
            query.setParameterList("filename", nameList);
        }

        List list = query.list();
        return list;
    }

    public void deleteList(List<Long> ids) {
        Query deleteQuery = this.getHibernateSession().createQuery(
                "delete KnowFeelingReportTask st where st.id in (:ids)");
        deleteQuery.setParameterList("ids", ids);
        deleteQuery.executeUpdate();
    }
}
