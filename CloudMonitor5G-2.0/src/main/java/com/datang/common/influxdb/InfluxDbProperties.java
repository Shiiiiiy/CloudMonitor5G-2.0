package com.datang.common.influxdb;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/2/5 10:56
 * @Version 1.0
 **/
@Data
@Configuration
public class InfluxDbProperties {
    @Value("${influxdb.db}")
    private String database;
    private String retentionPolicy = "autogen";
    private String retentionPolicyTime = "0s";
    private int actions = 2000;
    private int flushDuration = 1000;
    private int jitterDuration = 0;
    private int bufferLimit = 10000;

    @Value("${influxdb.url}")
    private String url;
    @Value("${influxdb.password}")
    private String password;
    @Value("${influxdb.username}")
    private String username;
    @Value("${influxdb.timeout}")
    private long timeout;
    @Value("${influxdb.radius}")
    private long radius;
}
