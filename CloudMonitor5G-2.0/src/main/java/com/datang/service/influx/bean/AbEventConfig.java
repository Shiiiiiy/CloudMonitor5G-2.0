package com.datang.service.influx.bean;

import lombok.Data;

/**
 * 异常事件配置
 */
@Data
public class AbEventConfig {
    private Integer id;
    private String type;
    private String evtCnName;
    private String detailSheet;
    private String[] preCon;
    private String[] triggerEvt;
    private String[] triggerSig;
    private String[] suffCon;
    private String[] startEvt;
}
