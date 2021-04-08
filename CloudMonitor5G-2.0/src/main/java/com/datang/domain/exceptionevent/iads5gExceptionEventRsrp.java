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

import org.springframework.stereotype.Component;

import com.datang.domain.testLogItem.TestLogItem;

import lombok.Data;

/**
 * 异常事件采样点数据表
 * @author maxuancheng
 *
 */
@Data
@Component
@Entity
@Table(name = "IADS_5G_EXCEPTION_EVENT_RSRP")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class iads5gExceptionEventRsrp {

    /**
     * 列表中各条记录的顺序编号
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    /**
     * 经度
     */
    @Column(name = "LATITUDE")
    private String latitude;
    /**
     * 纬度
     */
    @Column(name = "LONGITUDE")
    private String longitude;
    /**
     * 时间戳
     */
    @Column(name = "TIME_LONG")
    private Long timeLong;
    /**
     * NR RSRP
     */
    @Column(name = "NR_RSRP")
    private Double nrRsrp;
    /**
     * LTE RSRP
     */
    @Column(name = "LTE_RSRP")
    private Double lteRsrp;
    /**
     * NR ibler
     */
    @Column(name = "NR_IBLER")
    private String nrIbler;
    /**
     * LTE SINR
     */
    @Column(name = "LTE_SINR")
    private String lteSinr;
    /**
     * Beam数量
     */
    @Column(name = "BEAM_SUM")
    private String beamSum;
    /**
     * NR RSRQ
     */
    @Column(name = "NR_RSRQ")
    private String nrRsrq;
    /**
     * NR频点
     */
    @Column(name = "NR_POINT")
    private String nrPoint;
    /**
     * LTE频点
     */
    @Column(name = "LTE_POINT")
    private String ltePoint;
    /**
     * LTE小区id
     */
    @Column(name = "LTE_CELLID")
    private String lteCellid;
    /**
     * nr小区id
     */
    @Column(name = "NR_CELLID")
    private String nrCellid;
    /**
     * NR PCI
     */
    @Column(name = "NR_PCI")
    private String nrPCI;
    /**
     * LTE PCI
     */
    @Column(name = "LTE_PCI")
    private String ltePCI;
    /**
     * 服务小区波束编号
     */
    @Column(name = "NR_CELL_NUMBER")
    private String nrCellNumber;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER1")
    private String nrNcellNumber1;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER2")
    private String nrNcellNumber2;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER3")
    private String nrNcellNumber3;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER4")
    private String nrNcellNumber4;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER5")
    private String nrNcellNumber5;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER6")
    private String nrNcellNumber6;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER7")
    private String nrNcellNumber7;
    /**
     * 邻小区波束编号
     */
    @Column(name = "NR_NCELL_NUMBER8")
    private String nrNcellNumber8;
    
    /**
	 * 测试日志
	 */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO", nullable = false, referencedColumnName = "RECSEQNO")
	private TestLogItem testLogItem;
}
