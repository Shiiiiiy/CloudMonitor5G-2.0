package com.datang.domain.railway;

import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 14:21
 * @Version 1.0
 **/
@Entity
@Data
@Table(name = "IADS_TRAIN_TIME_INTERFACE")
public class TrainTimeInterfacePojo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private Long id; //编码

    @Column(name = "QUREY_INTERFACE")
    private String queryInterface; //查询的接口',
}
