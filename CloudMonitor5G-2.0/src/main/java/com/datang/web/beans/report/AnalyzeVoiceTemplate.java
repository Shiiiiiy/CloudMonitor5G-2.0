package com.datang.web.beans.report;


import com.datang.service.influx.InfluxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class AnalyzeVoiceTemplate implements AnalyzeTemplate{
    private static Logger LOGGER = LoggerFactory
            .getLogger(AnalyzeVoiceTemplate.class);

    @Override
    public String getId() {
        return "3";
    }

    @Override
    public String getTemplateFileName() {
        return "3.语音业务分析";
    }

    @Override
    public Map<String, Collection> getData(InfluxService influxService, List<String> logFileIdList) {
        List<Map<String,Object>> list = new ArrayList<>();
        for(String log:logFileIdList){
            List<String> each = new ArrayList<>();
            each.add(log);
            try{
                List<Map<String, Object>> abEvtAnaList = influxService.getVoiceBusiReports(each);
                list.addAll(abEvtAnaList);
                LOGGER.info(" influxDb " + abEvtAnaList.size() );
            }catch (Exception e){
                LOGGER.error("influx query :"+e.getMessage());
            }
        }
        Map<String, Collection> hashMap1 = new HashMap<>();
        hashMap1.put("sqlObj1", list);
        return hashMap1;
    }
}
