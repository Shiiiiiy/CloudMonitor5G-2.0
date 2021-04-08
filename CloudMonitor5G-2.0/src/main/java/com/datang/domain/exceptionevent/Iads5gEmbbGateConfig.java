package com.datang.domain.exceptionevent;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "IADS_5G_EMBB_GATE_CONFIG")
@Component
public class Iads5gEmbbGateConfig {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * 门限类型 1 SA模式移动性异常事件分析参数设置 2 SA模式接入性异常事件分析参数设置 3  NSA模式异常事件分析参数设置 4、4/5G互操作的异常事件分析参数
     *			5 业务类异常事件分析参数设置 
     */
    @Column(name = "GATE_TYPE")
    private Short gateType;

    //SA模式移动性异常事件分析参数设置：
    /**
     * 切换失败无线评估范围上限（秒）
     */
    @Column(name = "TIME_HIGH_SAMOVE")
    private Integer timeHighSaMove;

    /**
     * 切换失败无线评估范围下限（秒)
     */
    @Column(name = "TIME_LOW_SAMOVE")
    private Integer timeLowSaMove;
    
    /**
     * 同频切换失败弱覆盖原因判定比例（%)
     */
    @Column(name = "COMMEN_COVER")
    private Integer commenCover;

    /**
     * 同频切换失败质差原因判定比例（%)
     */
    @Column(name = "COMMEN_QUALITY")
    private Integer commenQuality;

    /**
     * 异频切换失败弱覆盖原因判定比例（%)
     */
    @Column(name = "ASYNCHRONOUS_COVER")
    private Integer asynchronousCover;

    /**
     * 异频切换失败质差原因判定比例（%)
     */
    @Column(name = "ASYNCHRONOUS_QUALITY")
    private Integer asynchronousQuality;

    /**
     * RNA更新失败弱覆盖原因判定比例（%)
     */
    @Column(name = "RNA_UPDATE_COVER")
    private Integer rnaUpdateCover;
    
    /**
     * RNA更新失败质差原因判定比例（%)
     */
    @Column(name = "RNA_UPDATE_SAMPLE")
    private Integer rnaUpdateSample;
    
    
    //SA模式接入性异常事件分析参数设置：
    /**
     * 接入失败无线评估范围上限（秒）
     */
    @Column(name = "TIME_HIGH_SAACCESS")
    private Integer timeHighSaAccess;

    /**
     * 接入失败无线评估范围下限（秒)
     */
    @Column(name = "TIME_LOW_SAACCESS")
    private Integer timeLowSaAccess;
    
    /**
     * RRC失败弱覆盖原因判定比例（%)
     */
    @Column(name = "RRC_COVER")
    private Integer rrcCover;

    /**
     * RRC失败质差原因判定比例（%)
     */
    @Column(name = "RRC_QUALITY")
    private Integer rrcQuality;
    
    /**
     * 注册失败弱覆盖原因判定比例（%)
     */
    @Column(name = "REGISTER_COVER")
    private Integer registerCover;

    /**
     * 注册切换失败质差原因判定比例（%)
     */
    @Column(name = "REGISTER_QUALITY")
    private Integer registerQuality;
    
    /**
     * 服务建立请求失败弱覆盖原因判定比例（%)
     */
    @Column(name = "SERVE_BULID_COVER")
    private Integer serveBulidCover;
    
    /**
     * 服务建立请求失败质差原因判定比例（%)
     */
    @Column(name = "SERVE_BULID_QUALITY")
    private Integer serveBulidQuality;
    
    /**
     * PDU会话失败弱覆盖原因判定比例（%)
     */
    @Column(name = "PDU_SESSION_CONFIRM_COVER")
    private Integer pduSessionConfirmCover;

    /**
     * PDU会话失败质差原因判定比例（%)
     */
    @Column(name = "PDU_SESSION_CONFIRM_QUALITY")
    private Integer pduSessionConfirmQuality;
    
    
    //NSA模式异常事件分析参数设置：
    /**
     * scell change失败无线评估范围上限（秒）
     */
    @Column(name = "TIME_HIGH_NSA")
    private Integer timeHighNSA;
    
    /**
     * scell change失败无线评估范围下限（秒）
     */
    @Column(name = "TIME_LOW_NSA")
    private Integer timeLowNSA;
    
    /**
     * Diff_LTE_Diff_NR scell change失败弱覆盖原因判定比例（%)
     */
    @Column(name = "DIFF_LTE_DIFF_NR_COVER")
    private BigDecimal diffLteDiffNrCover;

    /**
     * Diff_LTE_Diff_NR scell change失败质差原因判定比例（%)
     */
    @Column(name = "DIFF_LTE_DIFF_NR_QUALITY")
    private BigDecimal diffLteDiffNrQuality;

    /**
     * Same_LTE_Diff_NR scell change失败弱覆盖原因判定比例（%)
     */
    @Column(name = "SAME_LTE_DIFF_NR_COVER")
    private BigDecimal sameLteDiffNrCover;

    /**
     * Same_LTE_Diff_NR scell change失败质差原因判定比例（%)
     */
    @Column(name = "SAME_LTE_DIFF_NR_QUALITY")
    private BigDecimal sameLteDiffNrQuality;

    /**
     * Diff_LTE_Same_NR scell change失败弱覆盖原因判定比例（%)
     */
    @Column(name = "DIFF_LTE_SAME_NR_COVER")
    private BigDecimal diffLteSameNrCover;

    /**
     * Diff_LTE_Same_NR scell change失败质差原因判定比例（%)
     */
    @Column(name = "DIFF_LTE_SAME_NR_QUALITY")
    private BigDecimal diffLteSameNrQuality;

    /**
     * scell add失败弱覆盖原因判定比例（%)
     */
    @Column(name = "SCELL_ADD_COVER")
    private BigDecimal scellAddCover;

    /**
     * scell add失败质差原因判定比例（%)
     */
    @Column(name = "SCELL_ADD_QUALITY")
    private BigDecimal scellAddQuality;
    
    /**
     * EN-DC无线链路失败弱覆盖原因判定比例（%)
     */
    @Column(name = "ENDC_WIRELESS_COVER")
    private BigDecimal endcWirelessCover;

    /**
     * EN-DC无线链路失败质差原因判定比例（%)
     */
    @Column(name = "ENDC_WIRELESS_QUALITY")
    private BigDecimal endcWirelessQuality;
    
    
    //4/5G互操作的异常事件分析参数：
    /**
     * 4/5G互操作失败无线评估范围上限（秒）
     */
    @Column(name = "TIME_HIGH_45G")
    private Integer timeHigh45g;
    
    /**
     * 4/5G互操作失败无线评估范围下限（秒）
     */
    @Column(name = "TIME_LOW_45G")
    private Integer timeLow45g;
    
    /**
     * LTE to NR切换失败弱覆盖原因判定比例（%)
     */
    @Column(name = "SWIITCH_COVER_4TO5G")
    private Integer switchCover4To5G;

    /**
     * LTE to NR切换失败质差原因判定比例（%)
     */
    @Column(name = "SWIITCH_QUALITY_4TO5G")
    private Integer switchQuality4To5G;
    
    /**
     * LTE to NR重定位失败弱覆盖原因判定比例（%)
     */
    @Column(name = "RELOCATE_COVER_4TO5G")
    private Integer relocateCover4To5G;

    /**
     * LTE to NR重定位失败质差原因判定比例（%)
     */
    @Column(name = "RELOCATE_QUALITY_4TO5G")
    private Integer relocateQuality4To5G;
    
    /**
     * LTE to NR重选失败弱覆盖原因判定比例（%)
     */
    @Column(name = "RESELECT_COVER_4TO5G")
    private Integer reselectCover4To5G;
    
    /**
     * LTE to NR重选失败质差原因判定比例（%)
     */
    @Column(name = "RESELECT_QUALITY_4TO5G")
    private Integer reselectQuality4To5G;
    
    /**
     * NR to LTE切换失败弱覆盖原因判定比例（%)
     */
    @Column(name = "SWIITCH_COVER_5TO4G")
    private Integer switchCover5To4G;

    /**
     * NR to LTE切换失败质差原因判定比例（%)
     */
    @Column(name = "SWIITCH_QUALITY_5TO4G")
    private Integer switchQuality5To4G;
    
    /**
     * NR to LTE重定位失败弱覆盖原因判定比例（%)
     */
    @Column(name = "RELOCATE_COVER_5TO4G")
    private Integer relocateCover5To4G;

    /**
     * NR to LTE重定位失败质差原因判定比例（%)
     */
    @Column(name = "RELOCATE_QUALITY_5TO4G")
    private Integer relocateQuality5To4G;
    
    /**
     * NR to LTE重选失败弱覆盖原因判定比例（%)
     */
    @Column(name = "RESELECT_COVER_5TO4G")
    private Integer reselectCover5To4G;
    
    /**
     * NR to LTE重选失败质差原因判定比例（%)
     */
    @Column(name = "RESELECT_QUALITY_5TO4G")
    private Integer reselectQuality5To4G;
    
    
    //业务类异常事件分析参数：
    /**
     * 业务类失败无线评估范围上限（秒）
     */
    @Column(name = "TIME_HIGH_BUSINESS")
    private Integer timeHighBusiness;
    
    /**
     * 业务类失败失败无线评估范围下限（秒）
     */
    @Column(name = "TIME_LOW_BUSINESS")
    private Integer timeLowBusiness;
    
    /**
     * FTP下载掉线弱覆盖原因判定比例
     */
    @Column(name = "FTPDL_LOSTCONNECT_COVER")
    private Integer ftpDLLostConnectCover;
    
    /**
     * FTP下载掉线质差原因判定比例
     */
    @Column(name = "FTPDL_LOSTCONNECT_QUALITY")
    private Integer ftpDLLostConnectQuality;
    
    /**
     * FTP上传掉线弱覆盖原因判定比例
     */
    @Column(name = "FTPUL_LOSTCONNECT_COVER")
    private Integer ftpULLostConnectCover;
    
    /**
     * FTP上传掉线质差原因判定比例
     */
    @Column(name = "FTPUL_LOSTCONNECT_QUALITY")
    private Integer ftpULLostConnectQuality;
    
    /**
     * ping失败弱覆盖原因判定比例
     */
    @Column(name = "PING_FAIL_COVER")
    private Integer pingFailCover;
    
    /**
     * ping失败质差原因判定比例
     */
    @Column(name = "PING_FAIL_QUALITY")
    private Integer pingFailQuality;
    
    
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 是否删除 0 正常 1 删除
     */
    @Column(name = "FLAG")
    private Short flag;

    /**
     * 状态 备用
     */
    @Column(name = "STATUS")
    private Short status;
}

