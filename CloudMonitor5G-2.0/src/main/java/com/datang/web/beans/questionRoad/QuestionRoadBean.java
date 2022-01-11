package com.datang.web.beans.questionRoad;

import lombok.Data;

/**
 * 问题路段
 * @author shiyan
 * @date 2021/12/28 9:42
 */
@Data
public class QuestionRoadBean {

    /**
     * 日志名
     */
    private String logName;
    /**
     * 路段类型
     */
    private String roadType;

    /**
     * 起始经度
     */
    private String startLong;

    /**
     * 起始纬度
     */
    private String startLat;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 连续问题里程数
     */
    private String totalLen;


}
