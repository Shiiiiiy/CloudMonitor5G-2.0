package com.datang.web.beans.report;

import com.datang.common.util.ClassUtil;
import com.datang.service.influx.InfluxService;
import lombok.Data;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface AnalyzeTemplate {
    public String getId();
    public String getTemplateFileName();
    public default String getTemplateSuffix(){
        return ".xlsx";
    }
    public default String getTemplate(){
        return getTemplateFileName() + getTemplateSuffix();
    }
    public default InputStream getTemplateInputStream(InfluxService influxService){
        return  ClassUtil.getResourceAsStream(BASE_TEMPLATE_CLASSPATH + "/" + getTemplate());
    }
    public String BASE_TEMPLATE_CLASSPATH = "templates";
    public Map<String, Collection> getData(InfluxService influxService, List<String> logFileIdList);
}
