package com.datang.domain.testLogItem;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "iads_cucc_playbackinfo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class IEItem {

    private long id;

    private long logId;
    private String time;
    //NR主邻区信息窗口
    private String ie_58000;
    private Double ie_58032;
    private Double ie_50165;
    private Double ie_50101;
    private Double ie_70070;
    private Double ie_50229;
    private Double ie_50293;
    private Double ie_70525;
    private Double ie_70005;

    private String ie_58007;
    private Double ie_58039;
    private Double ie_50172;
    private Double ie_50108;
    private Double ie_70077;
    private Double ie_50236;
    private Double ie_50300;
    private Double ie_70532;
    private Double ie_70012;
   //NR主小区信息窗口
    private String ie_50013;
    private Double ie_50006;
    private Double ie_53601;
    private Double ie_50007;
    private Double ie_50015;
    private Double ie_50055;
    private Double ie_50056;
    private Double ie_50057;

    private Double ie_50001;
    private Double ie_50002;
    private Double ie_50003;
    private Double ie_50046;
    private Double ie_50051;
    private Double ie_50016;
    private Double ie_50017;
    private Double ie_50021;

    //LTE主小区信息窗口
    private String ie_40006;
    private Double ie_40007;
    private Double ie_40010;
    private Double ie_40008;
    private Double ie_40028;
    private Double ie_40035;
    private Double ie_40031;
    private Double ie_40034;
    private Double ie_40001;
    private Double ie_40002;
    private Double ie_40009;
    private Double ie_40014;
    private Double ie_40019;
    private Double ie_40015;
    private Double ie_40016;
    private Double ie_40017;


    //LTE主邻区信息窗口
    private Double ie_40075;
    private Double ie_44206;
    private Double ie_40139;
    private Double ie_40171;
    private Double ie_40203;
    private Double ie_40299;
    private Double ie_40235;

    private Double ie_40082;
    private Double ie_44213;
    private Double ie_40146;
    private Double ie_40178;
    private Double ie_40210;
    private Double ie_40306;
    private Double ie_40242;
    @Column(name = "ie_58007")
    public String getIe_58007() {
        return ie_58007;
    }

    public void setIe_58007(String ie_58007) {
        this.ie_58007 = ie_58007;
    }
    @Column(name = "ie_58039")
    public Double getIe_58039() {
        return ie_58039;
    }

    public void setIe_58039(Double ie_58039) {
        this.ie_58039 = ie_58039;
    }
    @Column(name = "ie_50172")
    public Double getIe_50172() {
        return ie_50172;
    }

    public void setIe_50172(Double ie_50172) {
        this.ie_50172 = ie_50172;
    }
    @Column(name = "ie_50108")
    public Double getIe_50108() {
        return ie_50108;
    }

    public void setIe_50108(Double ie_50108) {
        this.ie_50108 = ie_50108;
    }
    @Column(name = "ie_70077")
    public Double getIe_70077() {
        return ie_70077;
    }

    public void setIe_70077(Double ie_70077) {
        this.ie_70077 = ie_70077;
    }
    @Column(name = "ie_50236")
    public Double getIe_50236() {
        return ie_50236;
    }

    public void setIe_50236(Double ie_50236) {
        this.ie_50236 = ie_50236;
    }
    @Column(name = "ie_50300")
    public Double getIe_50300() {
        return ie_50300;
    }

    public void setIe_50300(Double ie_50300) {
        this.ie_50300 = ie_50300;
    }
    @Column(name = "ie_70532")
    public Double getIe_70532() {
        return ie_70532;
    }

    public void setIe_70532(Double ie_70532) {
        this.ie_70532 = ie_70532;
    }
    @Column(name = "ie_70012")
    public Double getIe_70012() {
        return ie_70012;
    }

    public void setIe_70012(Double ie_70012) {
        this.ie_70012 = ie_70012;
    }
    @Column(name = "ie_40082")
    public Double getIe_40082() {
        return ie_40082;
    }

    public void setIe_40082(Double ie_40082) {
        this.ie_40082 = ie_40082;
    }
    @Column(name = "ie_44213")
    public Double getIe_44213() {
        return ie_44213;
    }

    public void setIe_44213(Double ie_44213) {
        this.ie_44213 = ie_44213;
    }
    @Column(name = "ie_40146")
    public Double getIe_40146() {
        return ie_40146;
    }

    public void setIe_40146(Double ie_40146) {
        this.ie_40146 = ie_40146;
    }
    @Column(name = "ie_40178")
    public Double getIe_40178() {
        return ie_40178;
    }

    public void setIe_40178(Double ie_40178) {
        this.ie_40178 = ie_40178;
    }
    @Column(name = "ie_40210")
    public Double getIe_40210() {
        return ie_40210;
    }

    public void setIe_40210(Double ie_40210) {
        this.ie_40210 = ie_40210;
    }
    @Column(name = "ie_40306")
    public Double getIe_40306() {
        return ie_40306;
    }

    public void setIe_40306(Double ie_40306) {
        this.ie_40306 = ie_40306;
    }
    @Column(name = "ie_40242")
    public Double getIe_40242() {
        return ie_40242;
    }

    public void setIe_40242(Double ie_40242) {
        this.ie_40242 = ie_40242;
    }

    @Column(name = "timestamp")
    public String getTime() {
       return time;
    }
    private static final SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void setTime(String time) {
        this.time = sd.format(new Date(Long.parseLong(time)));
    }

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Column(name = "logcode")
    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }
    @Column(name = "ie_58000")
    public String getIe_58000() {
        return ie_58000;
    }

    public void setIe_58000(String ie_58000) {
        this.ie_58000 = ie_58000;
    }
    @Column(name = "ie_58032")
    public Double getIe_58032() {
        return ie_58032;
    }

    public void setIe_58032(Double ie_58032) {
        this.ie_58032 = ie_58032;
    }
    @Column(name = "ie_50165")
    public Double getIe_50165() {
        return ie_50165;
    }

    public void setIe_50165(Double ie_50165) {
        this.ie_50165 = ie_50165;
    }
    @Column(name = "ie_50101")
    public Double getIe_50101() {
        return ie_50101;
    }

    public void setIe_50101(Double ie_50101) {
        this.ie_50101 = ie_50101;
    }
    @Column(name = "ie_70070")
    public Double getIe_70070() {
        return ie_70070;
    }

    public void setIe_70070(Double ie_70070) {
        this.ie_70070 = ie_70070;
    }
    @Column(name = "ie_50229")
    public Double getIe_50229() {
        return ie_50229;
    }

    public void setIe_50229(Double ie_50229) {
        this.ie_50229 = ie_50229;
    }
    @Column(name = "ie_50293")
    public Double getIe_50293() {
        return ie_50293;
    }

    public void setIe_50293(Double ie_50293) {
        this.ie_50293 = ie_50293;
    }
    @Column(name = "ie_70525")
    public Double getIe_70525() {
        return ie_70525;
    }

    public void setIe_70525(Double ie_70525) {
        this.ie_70525 = ie_70525;
    }
    @Column(name = "ie_70005")
    public Double getIe_70005() {
        return ie_70005;
    }

    public void setIe_70005(Double ie_70005) {
        this.ie_70005 = ie_70005;
    }
    @Column(name = "ie_50013")
    public String getIe_50013() {
        return ie_50013;
    }

    public void setIe_50013(String ie_50013) {
        this.ie_50013 = ie_50013;
    }
    @Column(name = "ie_50006")
    public Double getIe_50006() {
        return ie_50006;
    }

    public void setIe_50006(Double ie_50006) {
        this.ie_50006 = ie_50006;
    }
    @Column(name = "ie_53601")
    public Double getIe_53601() {
        return ie_53601;
    }

    public void setIe_53601(Double ie_53601) {
        this.ie_53601 = ie_53601;
    }
    @Column(name = "ie_50007")
    public Double getIe_50007() {
        return ie_50007;
    }

    public void setIe_50007(Double ie_50007) {
        this.ie_50007 = ie_50007;
    }
    @Column(name = "ie_50015")
    public Double getIe_50015() {
        return ie_50015;
    }

    public void setIe_50015(Double ie_50015) {
        this.ie_50015 = ie_50015;
    }
    @Column(name = "ie_50055")
    public Double getIe_50055() {
        return ie_50055;
    }

    public void setIe_50055(Double ie_50055) {
        this.ie_50055 = ie_50055;
    }
    @Column(name = "ie_50056")
    public Double getIe_50056() {
        return ie_50056;
    }

    public void setIe_50056(Double ie_50056) {
        this.ie_50056 = ie_50056;
    }
    @Column(name = "ie_50057")
    public Double getIe_50057() {
        return ie_50057;
    }

    public void setIe_50057(Double ie_50057) {
        this.ie_50057 = ie_50057;
    }
    @Column(name = "ie_50001")
    public Double getIe_50001() {
        return ie_50001;
    }

    public void setIe_50001(Double ie_50001) {
        this.ie_50001 = ie_50001;
    }
    @Column(name = "ie_50002")
    public Double getIe_50002() {
        return ie_50002;
    }

    public void setIe_50002(Double ie_50002) {
        this.ie_50002 = ie_50002;
    }
    @Column(name = "ie_50003")
    public Double getIe_50003() {
        return ie_50003;
    }

    public void setIe_50003(Double ie_50003) {
        this.ie_50003 = ie_50003;
    }
    @Column(name = "ie_50046")
    public Double getIe_50046() {
        return ie_50046;
    }

    public void setIe_50046(Double ie_50046) {
        this.ie_50046 = ie_50046;
    }
    @Column(name = "ie_50051")
    public Double getIe_50051() {
        return ie_50051;
    }

    public void setIe_50051(Double ie_50051) {
        this.ie_50051 = ie_50051;
    }
    @Column(name = "ie_50016")
    public Double getIe_50016() {
        return ie_50016;
    }

    public void setIe_50016(Double ie_50016) {
        this.ie_50016 = ie_50016;
    }
    @Column(name = "ie_50017")
    public Double getIe_50017() {
        return ie_50017;
    }

    public void setIe_50017(Double ie_50017) {
        this.ie_50017 = ie_50017;
    }
    @Column(name = "ie_50021")
    public Double getIe_50021() {
        return ie_50021;
    }

    public void setIe_50021(Double ie_50021) {
        this.ie_50021 = ie_50021;
    }
    @Column(name = "ie_40006")
    public String getIe_40006() {
        return ie_40006;
    }

    public void setIe_40006(String ie_40006) {
        this.ie_40006 = ie_40006;
    }
    @Column(name = "ie_40007")
    public Double getIe_40007() {
        return ie_40007;
    }

    public void setIe_40007(Double ie_40007) {
        this.ie_40007 = ie_40007;
    }
    @Column(name = "ie_40010")
    public Double getIe_40010() {
        return ie_40010;
    }

    public void setIe_40010(Double ie_40010) {
        this.ie_40010 = ie_40010;
    }
    @Column(name = "ie_40008")
    public Double getIe_40008() {
        return ie_40008;
    }

    public void setIe_40008(Double ie_40008) {
        this.ie_40008 = ie_40008;
    }
    @Column(name = "ie_40028")
    public Double getIe_40028() {
        return ie_40028;
    }

    public void setIe_40028(Double ie_40028) {
        this.ie_40028 = ie_40028;
    }
    @Column(name = "ie_40035")
    public Double getIe_40035() {
        return ie_40035;
    }

    public void setIe_40035(Double ie_40035) {
        this.ie_40035 = ie_40035;
    }
    @Column(name = "ie_40031")
    public Double getIe_40031() {
        return ie_40031;
    }

    public void setIe_40031(Double ie_40031) {
        this.ie_40031 = ie_40031;
    }
    @Column(name = "ie_40034")
    public Double getIe_40034() {
        return ie_40034;
    }

    public void setIe_40034(Double ie_40034) {
        this.ie_40034 = ie_40034;
    }
    @Column(name = "ie_40001")
    public Double getIe_40001() {
        return ie_40001;
    }

    public void setIe_40001(Double ie_40001) {
        this.ie_40001 = ie_40001;
    }
    @Column(name = "ie_40002")
    public Double getIe_40002() {
        return ie_40002;
    }

    public void setIe_40002(Double ie_40002) {
        this.ie_40002 = ie_40002;
    }
    @Column(name = "ie_40009")
    public Double getIe_40009() {
        return ie_40009;
    }

    public void setIe_40009(Double ie_40009) {
        this.ie_40009 = ie_40009;
    }
    @Column(name = "ie_40014")
    public Double getIe_40014() {
        return ie_40014;
    }

    public void setIe_40014(Double ie_40014) {
        this.ie_40014 = ie_40014;
    }
    @Column(name = "ie_40019")
    public Double getIe_40019() {
        return ie_40019;
    }

    public void setIe_40019(Double ie_40019) {
        this.ie_40019 = ie_40019;
    }
    @Column(name = "ie_40015")
    public Double getIe_40015() {
        return ie_40015;
    }

    public void setIe_40015(Double ie_40015) {
        this.ie_40015 = ie_40015;
    }
    @Column(name = "ie_40016")
    public Double getIe_40016() {
        return ie_40016;
    }

    public void setIe_40016(Double ie_40016) {
        this.ie_40016 = ie_40016;
    }
    @Column(name = "ie_40017")
    public Double getIe_40017() {
        return ie_40017;
    }

    public void setIe_40017(Double ie_40017) {
        this.ie_40017 = ie_40017;
    }
    @Column(name = "ie_40075")
    public Double getIe_40075() {
        return ie_40075;
    }

    public void setIe_40075(Double ie_40075) {
        this.ie_40075 = ie_40075;
    }
    @Column(name = "ie_44206")
    public Double getIe_44206() {
        return ie_44206;
    }

    public void setIe_44206(Double ie_44206) {
        this.ie_44206 = ie_44206;
    }
    @Column(name = "ie_40139")
    public Double getIe_40139() {
        return ie_40139;
    }

    public void setIe_40139(Double ie_40139) {
        this.ie_40139 = ie_40139;
    }
    @Column(name = "ie_40171")
    public Double getIe_40171() {
        return ie_40171;
    }

    public void setIe_40171(Double ie_40171) {
        this.ie_40171 = ie_40171;
    }
    @Column(name = "ie_40203")
    public Double getIe_40203() {
        return ie_40203;
    }

    public void setIe_40203(Double ie_40203) {
        this.ie_40203 = ie_40203;
    }
    @Column(name = "ie_40299")
    public Double getIe_40299() {
        return ie_40299;
    }

    public void setIe_40299(Double ie_40299) {
        this.ie_40299 = ie_40299;
    }
    @Column(name = "ie_40235")
    public Double getIe_40235() {
        return ie_40235;
    }

    public void setIe_40235(Double ie_40235) {
        this.ie_40235 = ie_40235;
    }
}
