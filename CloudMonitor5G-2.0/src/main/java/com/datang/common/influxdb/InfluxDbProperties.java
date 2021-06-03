package com.datang.common.influxdb;

import lombok.Data;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/2/5 10:56
 * @Version 1.0
 **/
@Data
public class InfluxDbProperties {
    private String url;
    private String userName;
    private String password;
    private String database;
    private String retentionPolicy = "autogen";
    private String retentionPolicyTime = "0s";
    private int actions = 2000;
    private int flushDuration = 1000;
    private int jitterDuration = 0;
    private int bufferLimit = 10000;
}
