package com.datang.domain.exceptionevent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.datang.domain.testLogItem.TestLogItem;

import lombok.Data;

@Data
@Component
@Entity
@Table(name = "IADS_5G_EXCEPTION_EVENT_TABLE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Iads5gExceptionEventTable {
    /**
     * 列表中各条记录的顺序编号
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * 异常类型  1：Nsa模式异常事件分析;2:SA模式移动-系统内切换失败事件分析;3：SA模式接入性异常事件分析;4:4/5G互操作的异常事件分析;
     */
    @Column(name = "E_TYPE")
    private String eType;

    /**
     * 地市
     */
    @Column(name = "CITY")
    private String city;

    /**
     * Scell Change请求发起时间/切换请求发起时间/RRC请求发起时间/互操作请求发起时间
     */
    @Column(name = "SCELL_CHANGE_REQ_START_TIME")
    private String scellChangeReqStartTime;
    
    /**
     * Scell Change请求发起时间/切换请求发起时间/RRC请求发起时间/互操作请求发起时间 时间戳
     */
    @Column(name = "SCELL_CHANGE_REQ_START_TIME_L")
    private Long scellChangeReqStartTimeLong;

	/**
     * Scell Change失败发生时间/切换失败发生时间/接入失败发生时间/互操作失败发生时间
     */
    @Column(name = "SCELL_CHANGE_FAIL_TIME")
    private String scellChangeFailTime;
    
    /**
     * Scell Change失败发生时间/切换失败发生时间/接入失败发生时间/互操作失败发生时间 时间戳
     */
    @Column(name = "SCELL_CHANGE_FAIL_TIME_L")
    private Long scellChangeFailTimeLong;

    /**
     * 文件名称
     */
    @Column(name = "FILE_NAME")
    private String fileName;

	/**
     * 道路
     */
    @Column(name = "ROAD_NAME")
    private String roadName;

    /**
     * 经度
     */
    @Column(name = "LONGITUDE")
    private String longitude;

    /**
     * 维度
     */
    @Column(name = "DIMENSION")
    private String dimension;

    /**
     * Scell Change失败类型/同频或异频/接入失败类型/互操作失败类型
     */
    @Column(name = "SCELL_CHANGE_FAIL_TYPE")
    private String scellChangeFailType;

    /**
     * 无线原因分析
     */
    @Column(name = "WIRELESS_FAIL_REASON")
    private String wirelessFailReason;

    /**
     * Scell Change失败发生时候的LTE DL IBLER(%)
     */
    @Column(name = "DL_IBLER")
    private String dlIbler;

    /**
     * LTE_TAC
     */
    @Column(name = "LTE_TAC")
    private String lteTac;

    /**
     * LTE锚点小区的CELLID
     */
    @Column(name = "LTE_CELLID")
    private String lteCellid;

    /**
     * LTE锚点小区的小区友好名
     */
    @Column(name = "LTE_FRIENDLY_NAME")
    private String lteFriendlyName;

    /**
     * 当前LTE锚点小区的频点
     */
    @Column(name = "LTE_FREQUENCY_POINT")
    private String lteFrequencyPoint;

    /**
     * 当前LTE锚点小区的PCI
     */
    @Column(name = "LTE_PCI")
    private String ltePci;

    /**
     * Scell Change失败时候的LTE PCC_RSRP
     */
    @Column(name = "LTE_PCC_RSRP")
    private String ltePccRsrp;

    /**
     * Scell Change失败发生时候的NR/5G_源小区/5G_服务    DL IBLER(%)
     */
    @Column(name = "NR_DL_IBLER")
    private String nrDlIbler;

    /**
     * NR辅小区/5G_源小区/5G_服务   TAC
     */
    @Column(name = "NR_TAC")
    private String nrTac;

    /**
     * NR辅小区/5G_源小区/5G_服务   的CELLID
     */
    @Column(name = "NR_CELLID")
    private String nrCellid;

    /**
     * NR辅小区/5G_源小区/5G_服务   的小区友好名
     */
    @Column(name = "NR_FRIENDLY_NAME")
    private String nrFriendlyName;

    /**
     * 当前NR辅小区/5G_源小区/5G_服务   的频点
     */
    @Column(name = "NR_FREQUENCY_POINT")
    private String nrFrequencyPoint;

    /**
     * 当前NR辅小区/5G_源小区/5G_服务   的PCI
     */
    @Column(name = "NR_PCI")
    private String nrPci;

    /**
     * nr PCC RSRP///5G_源小区/5G_服务SS-RSRP
     */
    @Column(name = "NR_PCC_RSRP")
    private String nrPccRsrp;

    /**
     * LTE目标小区TAC
     */
    @Column(name = "LTE_TARGET_CELL_TAC")
    private String lteTargetCellTac;

    /**
     * LTE目标小区CELLID
     */
    @Column(name = "LTE_TARGET_CELL_CELLID")
    private String lteTargetCellCellid;
    
    /**
     * LTE目标小区友好名
     */
    @Column(name = "LTE_TARGET_CELL_CELLNAME")
    private String lteTargetCellCellName;

    /**
     * LTE目标小区频点
     */
    @Column(name = "LTE_TARGET_CELL_FREQUENCY")
    private String lteTargetCellFrequency;

    /**
     * LTE目标小区PCI
     */
    @Column(name = "LTE_TARGET_CELL_PCI")
    private String lteTargetCellPci;

    /**
     * LTE目标小区ss-rsrp
     */
    @Column(name = "LTE_TARGET_CELL_SS_RSRP")
    private String lteTargetCellSsRsrp;

    /**
     * NR/5G  目标小区TAC
     */
    @Column(name = "NR_TARGET_CELL_TAC")
    private String nrTargetCellTac;

    /**
     * NR/5G  目标小区CELLID
     */
    @Column(name = "NR_TARGET_CELL_CELLID")
    private String nrTargetCellCellid;
    
    /**
     * NR/5G  目标小区友好名
     */
    @Column(name = "NR_TARGET_CELL_CELLNAME")
    private String nrTargetCellCellName;

    /**
     * NR/5G  目标小区频点
     */
    @Column(name = "NR_TARGET_CELL_FREQUENCY")
    private String nrTargetCellFrequency;

    /**
     * NR/5G  目标小区PCI
     */
    @Column(name = "NR_TARGET_CELL_PCI")
    private String nrTargetCellPci;

    /**
     * NR/5G  目标小区ss-rsrp
     */
    @Column(name = "NR_TARGET_CELL_SS_RSRP")
    private String nrTargetCellSsRsrp;
    
    /**
     * 制式
     */
    @Column(name = "SA_MODEL")
    private String saModel;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private String createTime;

	/**
     * 更新时间
     */
    @Column(name = "UPDATE_TIME")
    private String updateTime;
   

	/**
     *
     */
    @Column(name = "DEL_FLAG")
    private String delFlag;

    /**
     *
     */
    @Column(name = "STATUS")
    private String status;

    /**
     *
     */
    @Column(name = "DESCRIPTION")
    private String description;
    
    /**
	 * 测试日志
	 */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO", nullable = false, referencedColumnName = "RECSEQNO")
	private TestLogItem testLogItem;
	
    /**
     * 小区TAC
     */
    @Transient
    private String cellTac;
    
    /**
     * 小区CELLID
     */
    @Transient
    private String cellId;
    
    /**
     * 小区友好名
     */
    @Transient
    private String cellFriendlyName;
    
    /**
     * 小区频点
     */
    @Transient
    private String cellFrequencyPoint;
    
    /**
     * 小区PCI
     */
    @Transient
    private String cellPci;
    
    /**
     * 小区ss-rsrp
     */
    @Transient
    private String cellSSRsrp;
    
    /**
     * 小区DL_ibler
     */
    @Transient
    private String cellDLIbler;
}



