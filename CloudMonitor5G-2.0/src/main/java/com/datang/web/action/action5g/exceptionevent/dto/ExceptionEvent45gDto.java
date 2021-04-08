package com.datang.web.action.action5g.exceptionevent.dto;

import javax.persistence.Column;

import lombok.Data;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-17 18:35
 */
@Data
public class ExceptionEvent45gDto {

    /**
     * 门限类型 1 SA模式移动性异常事件分析参数设置 2 SA模式接入性异常事件分析参数设置 3  NSA模式异常事件分析参数设置 4、4/5G互操作的异常事件分析参数
     * 			5 业务类异常事件分析参数设置 
     */
    private Short gateType;

    /**
     * 4/5G互操作失败无线评估范围上限（秒）
     */
    private Integer timeHigh45g;
    
    /**
     * 4/5G互操作失败无线评估范围下限（秒）
     */
    private Integer timeLow45g;
    
    /**
     * LTE to NR切换失败弱覆盖原因判定比例（%)
     */
    private Integer switchCover4To5G;

    /**
     * LTE to NR切换失败质差原因判定比例（%)
     */
    private Integer switchQuality4To5G;
    
    /**
     * LTE to NR重定位失败弱覆盖原因判定比例（%)
     */
    private Integer relocateCover4To5G;

    /**
     * LTE to NR重定位失败质差原因判定比例（%)
     */
    private Integer relocateQuality4To5G;
    
    /**
     * LTE to NR重选失败弱覆盖原因判定比例（%)
     */
    private Integer reselectCover4To5G;
    
    /**
     * LTE to NR重选失败质差原因判定比例（%)
     */
    private Integer reselectQuality4To5G;
    
    /**
     * NR to LTE切换失败弱覆盖原因判定比例（%)
     */
    private Integer switchCover5To4G;

    /**
     * NR to LTE切换失败质差原因判定比例（%)
     */
    private Integer switchQuality5To4G;
    
    /**
     * NR to LTE重定位失败弱覆盖原因判定比例（%)
     */
    private Integer relocateCover5To4G;

    /**
     * NR to LTE重定位失败质差原因判定比例（%)
     */
    private Integer relocateQuality5To4G;
    
    /**
     * NR to LTE重选失败弱覆盖原因判定比例（%)
     */
    private Integer reselectCover5To4G;
    
    /**
     * NR to LTE重选失败质差原因判定比例（%)
     */
    private Integer reselectQuality5To4G;

    /**
     * 描述
     */
    private String description;



}
