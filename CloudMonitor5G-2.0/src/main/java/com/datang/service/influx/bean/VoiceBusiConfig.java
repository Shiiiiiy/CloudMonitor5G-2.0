package com.datang.service.influx.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoiceBusiConfig {
    private String busiType;
    private String triggerEvt;
    private String network;
    private String fbtype;
    private String rlingEvt;
    private String rlingNet;
    private String callType;
}
