package com.datang.common.influxdb;

import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassNmae lc
 * @Description influxdb数据库的查询结果转换工具
 * @Author lucheng
 * @Date 2021/2/5 15:21
 * @Version 1.0
 **/
public class InfludbUtil {

    /**
     * @Description: 一条查询语句的查询结果转换
     * @Author: lc
     * @Date: 2021/2/5 15:26
     * @param query:
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public static List<Map<String,Object>> paraseQueryResult(QueryResult query){
        List<Map<String,Object>> queryRltList = new ArrayList<>();

        List<QueryResult.Result> results = query.getResults();
        if(results==null || results.size() ==0 ){
            return queryRltList;
        }
        //默认只有一条查询语句取第一条查询结果即可
        QueryResult.Result result = results.get(0);
        if (result.getSeries() != null) {
//            List<List<Object>> valueList = result.getSeries().stream().map(QueryResult.Series::getValues)
//                    .collect(Collectors.toList()).get(0);
            for(QueryResult.Series series:result.getSeries()){
                for (List<Object> objectList: series.getValues()) {
                    Map<String,Object> map = new HashMap<>();
                    for (int i=0;i<series.getColumns().size();i++) {
                        map.put(series.getColumns().get(i), objectList.get(i));
                    }
                    queryRltList.add(map);
                }

            }
        }
        return queryRltList;
    }
}

