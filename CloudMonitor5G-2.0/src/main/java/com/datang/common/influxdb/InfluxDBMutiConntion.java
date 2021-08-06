package com.datang.common.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InfluxDBMutiConntion {
    private static ConcurrentHashMap<String,InfluxDbConnection> connMap=new ConcurrentHashMap<>();
    @Autowired
    private InfluxDbConnection influxDbConnection;
    public InfluxDbConnection  getConn(String city,String url,String db){
        synchronized (this){
            StringBuilder sb=new StringBuilder();
            sb.append(city).append("_").append(url).append("_").append(db);
            if(connMap.containsKey(sb.toString())){
                return connMap.get(sb.toString());
            }
            InfluxDbConnection newInfluxDbConnection=new InfluxDbConnection();
            BeanUtils.copyProperties(influxDbConnection,newInfluxDbConnection);
            InfluxDB influxdb = InfluxDBFactory.connect("http://"+url, influxDbConnection.getUserName(), influxDbConnection.getPassword(), influxDbConnection.getClient());
            influxdb.setDatabase(db);
            newInfluxDbConnection.setInfluxdb(influxdb);
            connMap.put(sb.toString(),newInfluxDbConnection);
            return newInfluxDbConnection;
        }
    }
}
