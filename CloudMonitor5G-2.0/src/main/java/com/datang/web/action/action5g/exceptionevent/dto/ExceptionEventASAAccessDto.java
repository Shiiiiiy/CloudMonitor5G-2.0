package com.datang.web.action.action5g.exceptionevent.dto;

import javax.persistence.Column;

import lombok.Data;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 18:35
 */
@Data
public class ExceptionEventASAAccessDto {

    /**
     * 门限类型 1 SA模式移动性异常事件分析参数设置 2 SA模式接入性异常事件分析参数设置 3  NSA模式异常事件分析参数设置 4、4/5G互操作的异常事件分析参数
     * 			5 业务类异常事件分析参数设置 
     */
    private Short gateType;

    private Integer timeHighSaAccess;

    /**
     * 接入失败无线评估范围下限（秒)
     */
    private Integer timeLowSaAccess;
    
    /**
     * RRC失败弱覆盖原因判定比例（%)
     */
    private Integer rrcCover;

    /**
     * RRC失败质差原因判定比例（%)
     */
    private Integer rrcQuality;
    
    /**
     * 注册失败弱覆盖原因判定比例（%)
     */
    private Integer registerCover;

    /**
     * 注册切换失败质差原因判定比例（%)
     */
    private Integer registerQuality;
    
    /**
     * 服务建立请求失败弱覆盖原因判定比例（%)
     */
    private Integer serveBulidCover;
    
    /**
     * 服务建立请求失败质差原因判定比例（%)
     */
    private Integer serveBulidQuality;
    
    /**
     * PDU会话失败弱覆盖原因判定比例（%)
     */
    private Integer pduSessionConfirmCover;

    /**
     * PDU会话失败质差原因判定比例（%)
     */
    private Integer pduSessionConfirmQuality;

    /**
     * 描述
     */
    private String description;



}
