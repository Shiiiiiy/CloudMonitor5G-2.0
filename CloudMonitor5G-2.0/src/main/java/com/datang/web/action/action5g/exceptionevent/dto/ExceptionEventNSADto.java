package com.datang.web.action.action5g.exceptionevent.dto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:40
 */
@Data
public class ExceptionEventNSADto {
    /**
     * 门限类型 1 SA模式移动性异常事件分析参数设置 2 SA模式接入性异常事件分析参数设置 3  NSA模式异常事件分析参数设置 4、4/5G互操作的异常事件分析参数
     * 			5 业务类异常事件分析参数设置 
     */
    private Short gateType;

    /**
     * scell change失败无线评估范围上限（秒）
     */
    private Integer timeHighNSA;
    
    /**
     * scell change失败无线评估范围下限（秒）
     */
    private Integer timeLowNSA;
    
    /**
     * Diff_LTE_Diff_NR scell change失败弱覆盖原因判定比例（%)
     */
    private BigDecimal diffLteDiffNrCover;

    /**
     * Diff_LTE_Diff_NR scell change失败质差原因判定比例（%)
     */
    private BigDecimal diffLteDiffNrQuality;

    /**
     * Same_LTE_Diff_NR scell change失败弱覆盖原因判定比例（%)
     */
    private BigDecimal sameLteDiffNrCover;

    /**
     * Same_LTE_Diff_NR scell change失败质差原因判定比例（%)
     */
    private BigDecimal sameLteDiffNrQuality;

    /**
     * Diff_LTE_Same_NR scell change失败弱覆盖原因判定比例（%)
     */
    private BigDecimal diffLteSameNrCover;

    /**
     * Diff_LTE_Same_NR scell change失败质差原因判定比例（%)
     */
    private BigDecimal diffLteSameNrQuality;

    /**
     * scell add失败弱覆盖原因判定比例（%)
     */
    private BigDecimal scellAddCover;

    /**
     * scell add失败质差原因判定比例（%)
     */
    private BigDecimal scellAddQuality;
    
    /**
     *EN-DC无线链路失败弱覆盖原因判定比例（%)
     */
    private BigDecimal endcWirelessCover;

    /**
     *EN-DC无线链路失败质差原因判定比例（%)
     */
    private BigDecimal endcWirelessQuality;

    /**
     * 描述
     */
    private String description;
}
