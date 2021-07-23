package com.datang.web.beans.report;


import com.datang.common.util.ClassUtil;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.service.influx.InfluxService;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;


public class AnalyzeNetworkTemplate implements AnalyzeTemplate{
    private static Logger LOGGER = LoggerFactory
            .getLogger(AnalyzeNetworkTemplate.class);
    @Override
    public String getId() {
        return "1";
    }

    @Override
    public String getTemplateFileName() {
        return "1.网络参数配置";
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
        List<Map<String,Object>> sqlObj6 = new ArrayList<>();

//        for(String log:logFileIdList){
//            List<String> each = new ArrayList<>();
//            each.add(log);
            try{
                Map<String, List<Map<String, Object>>> netConfigReports = influxService.getNetConfigReports(logFileIdList);
                toSqlObj1(netConfigReports,sqlObj1);
                toSqlObj2(netConfigReports,sqlObj2);
                toSqlObj3(netConfigReports,sqlObj3);
                toSqlObj4(netConfigReports,sqlObj4);
                toSqlObj5(netConfigReports,sqlObj5);
                toSqlObj6(netConfigReports,sqlObj6);
                LOGGER.info(" influxDb " + netConfigReports.size() );
            }catch (Exception e){
                LOGGER.error("influx query :"+e.getMessage());
            }
//        }

        // 加 id
        TestLogItemUtils.addId(sqlObj3);
        TestLogItemUtils.addId(sqlObj4);
        TestLogItemUtils.addId(sqlObj5);
        TestLogItemUtils.addId(sqlObj6);


        // 转数字
        TestLogItemUtils.formatNumber(sqlObj1);
        TestLogItemUtils.formatNumber(sqlObj2);
        TestLogItemUtils.formatNumber(sqlObj3);
        TestLogItemUtils.formatNumber(sqlObj4);
        TestLogItemUtils.formatNumber(sqlObj5);
        TestLogItemUtils.formatNumber(sqlObj6);

        Map<String, Collection> hashMap1 = new HashMap<>();
        hashMap1.put("sqlObj1", sqlObj1);
        hashMap1.put("sqlObj2", sqlObj2);
        hashMap1.put("sqlObj3", sqlObj3);
        hashMap1.put("sqlObj4", sqlObj4);
        hashMap1.put("sqlObj5", sqlObj5);
        hashMap1.put("sqlObj6", sqlObj6);
        return hashMap1;
    }
    private void addSplitValue(Map<String, Object> each){
        int splitLen = 7;
        String  key = (String)each.get("key");
        if(key==null) return;
        String[] split = key.split("_");
        if(split.length<splitLen){
            split = Arrays.copyOf(split,splitLen);
        }
        each.put("base0",StringUtils.defaultIfBlank(split[0],""));
        each.put("base1",StringUtils.defaultIfBlank(split[1],""));
        each.put("base2",StringUtils.defaultIfBlank(split[2],""));
        each.put("base3",StringUtils.defaultIfBlank(split[3],""));
        each.put("base4",StringUtils.defaultIfBlank(split[4],""));
        each.put("base5",StringUtils.defaultIfBlank(split[5],""));
        each.put("base6",StringUtils.defaultIfBlank(split[6],""));
    }


    //  5G
    private void toSqlObj1(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj1){
        List<Map<String, Object>> maps = map.get("5G");
        if(CollectionUtils.isEmpty(maps)) return;
        for(Map<String, Object> each:maps){
            addSplitValue(each);
        }
        sqlObj1.addAll(maps);
    }
    //  4G
    private void toSqlObj2(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj2){
        List<Map<String, Object>> maps = map.get("4G");
        if(CollectionUtils.isEmpty(maps)) return;
        for(Map<String, Object> each:maps){
            addSplitValue(each);
        }
        sqlObj2.addAll(maps);
    }
    //  5G→4G 基于测量切换详情
    private void toSqlObj3(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj3){
        List<Map<String, Object>> maps = map.get("5G→4G 基于测量切换详情");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj3.addAll(maps);
    }
    //  5G→4G 基于测量重定向详情
    private void toSqlObj4(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj4){
        List<Map<String, Object>> maps = map.get("5G→4G 基于测量重定向详情");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj4.addAll(maps);
    }
    //  4G→5G 基于测量切换详情
    private void toSqlObj5(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj5){
        List<Map<String, Object>> maps = map.get("4G→5G 基于测量切换详情");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj5.addAll(maps);
    }
    //  4G→5G 基于测量重定向详情
    private void toSqlObj6(Map<String, List<Map<String, Object>>> map, List<Map<String,Object>> sqlObj6){
        List<Map<String, Object>> maps = map.get("4G→5G 基于测量重定向详情");
        if(CollectionUtils.isEmpty(maps)) return;
        sqlObj6.addAll(maps);
    }






}
