package com.datang.web.beans.report;


import com.datang.common.util.ClassUtil;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.service.influx.InfluxService;
import com.datang.service.influx.QuesRoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;


public class QuesRoadTemplate implements AnalyzeTemplate{
    private static Logger LOGGER = LoggerFactory
            .getLogger(AnalyzeNetworkTemplate.class);
    @Override
    public String getId() {
        return "4";
    }

    @Override
    public String getTemplateFileName() {
        return "4.问题路段";
    }

    @Override
    public InputStream getTemplateInputStream(InfluxService influxService) {
        return  ClassUtil.getResourceAsStream(BASE_TEMPLATE_CLASSPATH + "/" + getTemplate());
    }

    @Override
    public Map<String, Collection> getData(InfluxService influxService, List<String> logFileIdList) {
        List<Map<String,Object>> sqlObj1 = new ArrayList<>();
        List<Map<String,Object>> sqlObj2 = new ArrayList<>();
        List<Map<String,Object>> sqlObj3 = new ArrayList<>();
        List<Map<String,Object>> sqlObj4 = new ArrayList<>();
        List<Map<String,Object>> sqlObj5 = new ArrayList<>();
        QuesRoadService quesRoadService = (QuesRoadService) influxService;
        try{
            Map<String, List<Map<String, Object>>> netConfigReports = quesRoadService.analysize(logFileIdList);
            toSqlObj1(netConfigReports,sqlObj1);
            toSqlObj2(netConfigReports,sqlObj2);
            toSqlObj3(netConfigReports,sqlObj3);
            toSqlObj4(netConfigReports,sqlObj4);
            toSqlObj5(netConfigReports,sqlObj5);
            LOGGER.info(" influxDb " + netConfigReports.size() );
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("influx query :"+e.getMessage());
        }

        // 加 id
        TestLogItemUtils.addId(sqlObj1);
        TestLogItemUtils.addId(sqlObj2);
        TestLogItemUtils.addId(sqlObj3);
        TestLogItemUtils.addId(sqlObj4);
        TestLogItemUtils.addId(sqlObj5);
        // 转数字
        TestLogItemUtils.formatNumber(sqlObj1);
        TestLogItemUtils.formatNumber(sqlObj2);
        TestLogItemUtils.formatNumber(sqlObj3);
        TestLogItemUtils.formatNumber(sqlObj4);
        TestLogItemUtils.formatNumber(sqlObj5);
        Map<String, Collection> hashMap1 = new HashMap<>();
        hashMap1.put("sqlObj1", sqlObj1);
        hashMap1.put("sqlObj2", sqlObj2);
        hashMap1.put("sqlObj3", sqlObj3);
        hashMap1.put("sqlObj4", sqlObj4);
        hashMap1.put("sqlObj5", sqlObj5);
        return hashMap1;
    }


    //  5G
    private void toSqlObj1(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj1){
        List<Map<String, Object>> maps = map.get("弱覆盖路段");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj1.addAll(maps);
    }
    //  4G
    private void toSqlObj2(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj2){
        List<Map<String, Object>> maps = map.get("下行质差路段");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj2.addAll(maps);
    }
    //  5G→4G 基于测量切换详情
    private void toSqlObj3(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj3){
        List<Map<String, Object>> maps = map.get("上行质差路段");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj3.addAll(maps);
    }
    //  5G→4G 基于测量重定向详情
    private void toSqlObj4(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj4){
        List<Map<String, Object>> maps = map.get("下载速率低路段");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj4.addAll(maps);
    }
    //  4G→5G 基于测量切换详情
    private void toSqlObj5(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj5){
        List<Map<String, Object>> maps = map.get("上传速率低路段");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj5.addAll(maps);
    }
}
