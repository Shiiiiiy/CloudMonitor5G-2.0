package com.datang.web.action.action5g.exceptionevent.dto;

import javax.persistence.Column;

import lombok.Data;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 18:35
 */
@Data
public class ExceptionEventASAMoveDto {

    /**
     * 门限类型 1 SA模式移动性异常事件分析参数设置 2 SA模式接入性异常事件分析参数设置 3  NSA模式异常事件分析参数设置 4、4/5G互操作的异常事件分析参数
     * 			5 业务类异常事件分析参数设置 
     */
    private Short gateType;

    /**
     * 切换失败无线评估范围上限（秒）
     */
    private Integer timeHighSaMove;

    /**
     * 切换失败无线评估范围下限（秒)
     */
    private Integer timeLowSaMove;
    
    /**
     * 同频切换失败弱覆盖原因判定比例（%)
     */
    private Integer commenCover;

    /**
     * 同频切换失败质差原因判定比例（%)
     */
    private Integer commenQuality;

    /**
     * 异频切换失败弱覆盖原因判定比例（%)
     */
    private Integer asynchronousCover;

    /**
     * 异频切换失败质差原因判定比例（%)
     */
    private Integer asynchronousQuality;

    /**
     * RNA更新失败弱覆盖原因判定比例（%)
     */
    private Integer rnaUpdateCover;
    
    /**
     * RNA更新失败质差原因判定比例（%)
     */
    private Integer rnaUpdateSample;

    /**
     * 描述
     */
    private String description;



}
