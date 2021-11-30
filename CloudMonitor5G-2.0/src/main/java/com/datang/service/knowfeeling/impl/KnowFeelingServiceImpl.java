package com.datang.service.knowfeeling.impl;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.knowfeeling.KnowFeelingDao;
import com.datang.domain.knowfeeling.KnowFeelingReportTask;
import com.datang.service.knowfeeling.KnowFeelingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author shiyan
 * @date 2021/9/6 13:21
 */
@Service
public class KnowFeelingServiceImpl implements KnowFeelingService {



    @Autowired
    private KnowFeelingDao knowFeelingDao;

    @Override
    public AbstractPageList pageList(PageList pageList) {
        return  knowFeelingDao.getPageItem(pageList);
    }

    @Override
    public void save(KnowFeelingReportTask task) {
        knowFeelingDao.create(task);
    }

    @Override
    public List<Map<String,Object>> queryKnowFeelingStatics(String... s) {
        return knowFeelingDao.queryKnowFeelingStatics(s);
    }

    @Override
    public KnowFeelingReportTask find(Long id) {
        return knowFeelingDao.find(id);
    }

    @Override
    public void delete(List<Long> ids) {

        knowFeelingDao.deleteList(ids);


    }

    @Override
    public Map<String, Object> queryKnowFeelingStaticsByAreaAndTime(String prov, String city, Date begin, Date end) {
        return knowFeelingDao.queryKnowFeelingStaticsByAreaAndTime(prov,city,begin,end);
    }

    @Override
    public List<Map<String, Object>> queryKnowFeelingDetailInfo(String prov, String city, Date begin, Date end) {
        return knowFeelingDao.queryKnowFeelingDetailInfo(prov,city,begin,end);
    }

    @Override
    public List<Map<String, Object>> queryKnowFeelingByTraffic(String prov, String city, Date begin, Date end) {
         List<Map<String, Object>> traffic = knowFeelingDao.queryKnowFeelingByTraffic(prov, city, begin, end, true);

         traffic.addAll(knowFeelingDao.queryKnowFeelingByTraffic(prov, city, begin, end, false));

         return traffic;
    }

    @Override
    public List<Map<String, Object>> queryKnowFeelingByApp(String prov, String city, Date begin, Date end) {
        return knowFeelingDao.queryKnowFeelingByApp(prov,city,begin,end);
    }


}
