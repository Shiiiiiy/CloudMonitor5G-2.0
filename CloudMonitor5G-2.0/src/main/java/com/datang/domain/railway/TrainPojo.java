package com.datang.domain.railway;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 13:53
 * @Version 1.0
 **/
@Entity
@Data
@Table(name = "IADS_TRAIN_LIST")
public class TrainPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private Long id; //编码

    @Column(name = "TRAIN_CODE")
    private String trainCode; //车次',

    @Column(name = "START_STATION_NAME")
    private String startStationName; //始发站',

    @Column(name = "END_STATION_NAME")
    private String endStationName; //终点站',

    @Column(name = "TRAINNO")
    private String trainNo; //车次编码',

    // 0：快速 1：直特 2：特快 3：普快 4：普客 8：高速（高铁）
    @Column(name = "TRAIN_CLASS_CODE")
    private String trainClassCode; //车次类型号',

    @Column(name = "TRAIN_CLASS_NAME")
    private String trainClassName; //车次类型',

    @Column(name = "RUN_DATE")
    private String runDate; //运行时间',

    @Column(name = "CREATE_TIME")
    private String createTime; //创建时间',

    @Column(name = "UPDATE_TIME")
    private String updateTime; //更新时间',
}
