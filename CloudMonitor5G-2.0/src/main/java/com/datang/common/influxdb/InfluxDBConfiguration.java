package com.datang.common.influxdb;

import okhttp3.OkHttpClient;
import org.influxdb.BatchOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Influxdb 配置类
 */
@Configuration
public class InfluxDBConfiguration {
    @Bean
    InfluxDbProperties influxDbProperties() {
        return new InfluxDbProperties();
    }

    @Bean
    public InfluxDbConnection influxDbConnection(InfluxDbProperties influxDbProperties) {
        BatchOptions batchOptions = BatchOptions.DEFAULTS;
        batchOptions = batchOptions.actions(influxDbProperties.getActions());
        batchOptions = batchOptions.flushDuration(influxDbProperties.getFlushDuration());
        batchOptions = batchOptions.jitterDuration(influxDbProperties.getJitterDuration());
        batchOptions = batchOptions.bufferLimit(influxDbProperties.getBufferLimit());
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(influxDbProperties.getTimeout(),TimeUnit.SECONDS);
        InfluxDbConnection influxDbConnection = new InfluxDbConnection(influxDbProperties.getUsername(), influxDbProperties.getPassword(),
                influxDbProperties.getUrl(), influxDbProperties.getDatabase(), influxDbProperties.getRetentionPolicy(),
                influxDbProperties.getRetentionPolicyTime(), batchOptions,client);
        influxDbConnection.createRetentionPolicy();
        return influxDbConnection;
    }
}
