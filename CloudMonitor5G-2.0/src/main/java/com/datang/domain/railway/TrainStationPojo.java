package com.datang.domain.railway;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "IADS_TRAIN_STATION")
public class TrainStationPojo{

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id; //编码

    @Column(name = "TELEGRAPH_CODE")
    private String telegraphCode; //电报码

    @Column(name = "STATION_NAME")
    private String stationName; //站名

    @Column(name = "SPELL")
    private String spell; //拼音

    @Column(name = "INITIAL")
    private String initial; //首字母

    @Column(name = "PINYIN_CODE")
    private String pinyinCode; //拼音码

    @Column(name = "CREATE_TIME")
    private String createTime; //创建时间

    @Column(name = "UPDATE_TIME")
    private String updateTime; //更新时间
}
