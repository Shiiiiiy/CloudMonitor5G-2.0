package com.datang.service.influx.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 网络参数配置45G汇总报表配置参数类
 */
@Data
@AllArgsConstructor
public class NetTotalConfig {
    private String networkType;
    private String sheetName;
    private String kpiIndex;
    private String srcIndex;
}
