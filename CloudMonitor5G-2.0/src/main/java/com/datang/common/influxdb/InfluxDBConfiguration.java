package com.datang.common.influxdb;

import org.influxdb.BatchOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Influxdb 配置类
 */
//@Configuration
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

        InfluxDbConnection influxDbConnection = new InfluxDbConnection(influxDbProperties.getUserName(), influxDbProperties.getPassword(),
                influxDbProperties.getUrl(), influxDbProperties.getDatabase(), influxDbProperties.getRetentionPolicy(),
                influxDbProperties.getRetentionPolicyTime(), batchOptions);
        influxDbConnection.createRetentionPolicy();
        return influxDbConnection;
    }
}
