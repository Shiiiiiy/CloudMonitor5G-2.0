package com.datang.web.beans.questionRoad;

import lombok.Data;

/**
 * 问题路段
 * @author shiyan
 * @date 2021/12/29 9:16
 */
@Data
public class ExceptionEventBean {

    /**
     * 日志名
     */
    private String logName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 事件名称
     */
    private String evtName;

    /**
     * 发生时间
     */
    private String time;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 事件发生依据
     */
    private String causeBy;

}
