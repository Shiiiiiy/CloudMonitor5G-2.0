package com.datang.domain.testLogItem;

import lombok.Data;

import javax.persistence.*;

/**
 * 日志回放，布局配置
 */
@Data
@Entity
@Table(name = "iads_testlog_replay_layout")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class LayoutConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String status;

    private String name;

    private String value;







}
