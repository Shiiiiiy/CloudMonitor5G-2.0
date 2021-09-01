package com.datang.domain.testLogItem;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shiyan
 * @date 2021/8/20 14:19
 */
@Entity
@Table(name = "iads_pcaplog_rawdata")
@Data
public class PcapData {

    @Id
    @Column(name = "recseqno")
    private Long recseqno;

    @Column(name = "testlogitemrecseqno")
    private Long testlogitemrecseqno;
  //  private String logfile;
  //  private long filelineseq;

    @Column(name = "testtime")
    private Long testtime;


    private String time;

    /**
     * 源ip
     */
    @Column(name = "sourceip")
    private String sourceip;
    /**
     * 目标ip
     */
    @Column(name = "destip")
    private String destip;

    /**
     * 协议
     */
    @Column(name = "protocol")
    private String  protocol;


    @Column(name = "rawdatainfo")
    private String rawdata;

    private static final SimpleDateFormat SDF =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Transient
    public String getTime() {
        return SDF.format(this.testtime);
    }

}
