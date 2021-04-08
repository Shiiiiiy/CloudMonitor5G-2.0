package com.datang.web.action.action5g.exceptionevent.dto;

import lombok.Data;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-20 11:27
 */
@Data
public class GisAndListShowDto {

    /**
     * 道路名称
     */
    private String roadName;

    /**
     * 文件名称
     */
    private String logName;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页显示的条数
     */
    private Integer pageSize;
    
    /**
     * 日志id
     */
    private String reqSeqNo;
}
