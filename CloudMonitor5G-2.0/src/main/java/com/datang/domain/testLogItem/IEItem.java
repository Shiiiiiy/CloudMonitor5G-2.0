package com.datang.domain.testLogItem;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "iads_cucc_playbackinfo")
@IdClass(IEItemId.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class IEItem {

    private long logId;
    private String time;
    private Long timeCol;
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

    private String ie_58001;
    private Double ie_58033;
    private Double ie_50166;
    private Double ie_50102;
    private Double ie_70071;
    private Double ie_50230;
    private Double ie_50294;
    private Double ie_70526;
    private Double ie_70006;

    private String ie_58002;
    private Double ie_58034;
    private Double ie_50167;
    private Double ie_50103;
    private Double ie_70072;
    private Double ie_50231;
    private Double ie_50295;
    private Double ie_70527;
    private Double ie_70007;

    private String ie_58003;
    private Double ie_58035;
    private Double ie_50168;
    private Double ie_50104;
    private Double ie_70073;
    private Double ie_50232;
    private Double ie_50296;
    private Double ie_70528;
    private Double ie_70008;

    private String ie_58004;
    private Double ie_58036;
    private Double ie_50169;
    private Double ie_50105;
    private Double ie_70074;
    private Double ie_50233;
    private Double ie_50297;
    private Double ie_70529;
    private Double ie_70009;

    private String ie_58005;
    private Double ie_58037;
    private Double ie_50170;
    private Double ie_50106;
    private Double ie_70075;
    private Double ie_50234;
    private Double ie_50298;
    private Double ie_70530;
    private Double ie_70010;

    private String ie_58006;
    private Double ie_58038;
    private Double ie_50171;
    private Double ie_50107;
    private Double ie_70076;
    private Double ie_50235;
    private Double ie_50299;
    private Double ie_70531;
    private Double ie_70011;

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
    private Double ie_54321;
    private Double ie_54322;
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

    private Double ie_40076;
    private Double ie_44207;
    private Double ie_40140;
    private Double ie_40172;
    private Double ie_40204;
    private Double ie_40300;
    private Double ie_40236;

    private Double ie_40077;
    private Double ie_44208;
    private Double ie_40141;
    private Double ie_40173;
    private Double ie_40205;
    private Double ie_40301;
    private Double ie_40237;

    private Double ie_40078;
    private Double ie_44209;
    private Double ie_40142;
    private Double ie_40174;
    private Double ie_40206;
    private Double ie_40302;
    private Double ie_40238;

    private Double ie_40079;
    private Double ie_44210;
    private Double ie_40143;
    private Double ie_40175;
    private Double ie_40207;
    private Double ie_40303;
    private Double ie_40239;

    private Double ie_40080;
    private Double ie_44211;
    private Double ie_40144;
    private Double ie_40176;
    private Double ie_40208;
    private Double ie_40304;
    private Double ie_40240;

    private Double ie_40081;
    private Double ie_44212;
    private Double ie_40145;
    private Double ie_40177;
    private Double ie_40209;
    private Double ie_40305;
    private Double ie_40241;

    private Double ie_40082;
    private Double ie_44213;
    private Double ie_40146;
    private Double ie_40178;
    private Double ie_40210;
    private Double ie_40306;
    private Double ie_40242;
    @Column(name = "ie_58001")
    public String getIe_58001() {
        return ie_58001;
    }

    public void setIe_58001(String ie_58001) {
        this.ie_58001 = ie_58001;
    }
    @Column(name = "ie_58033")
    public Double getIe_58033() {
        return ie_58033;
    }

    public void setIe_58033(Double ie_58033) {
        this.ie_58033 = ie_58033;
    }
    @Column(name = "ie_50166")
    public Double getIe_50166() {
        return ie_50166;
    }

    public void setIe_50166(Double ie_50166) {
        this.ie_50166 = ie_50166;
    }
    @Column(name = "ie_50102")
    public Double getIe_50102() {
        return ie_50102;
    }

    public void setIe_50102(Double ie_50102) {
        this.ie_50102 = ie_50102;
    }
    @Column(name = "ie_70071")
    public Double getIe_70071() {
        return ie_70071;
    }

    public void setIe_70071(Double ie_70071) {
        this.ie_70071 = ie_70071;
    }
    @Column(name = "ie_50230")
    public Double getIe_50230() {
        return ie_50230;
    }

    public void setIe_50230(Double ie_50230) {
        this.ie_50230 = ie_50230;
    }
    @Column(name = "ie_50294")
    public Double getIe_50294() {
        return ie_50294;
    }

    public void setIe_50294(Double ie_50294) {
        this.ie_50294 = ie_50294;
    }
    @Column(name = "ie_70526")
    public Double getIe_70526() {
        return ie_70526;
    }

    public void setIe_70526(Double ie_70526) {
        this.ie_70526 = ie_70526;
    }
    @Column(name = "ie_70006")
    public Double getIe_70006() {
        return ie_70006;
    }

    public void setIe_70006(Double ie_70006) {
        this.ie_70006 = ie_70006;
    }
    @Column(name = "ie_58002")
    public String getIe_58002() {
        return ie_58002;
    }

    public void setIe_58002(String ie_58002) {
        this.ie_58002 = ie_58002;
    }
    @Column(name = "ie_58034")
    public Double getIe_58034() {
        return ie_58034;
    }

    public void setIe_58034(Double ie_58034) {
        this.ie_58034 = ie_58034;
    }
    @Column(name = "ie_50167")
    public Double getIe_50167() {
        return ie_50167;
    }

    public void setIe_50167(Double ie_50167) {
        this.ie_50167 = ie_50167;
    }
    @Column(name = "ie_50103")
    public Double getIe_50103() {
        return ie_50103;
    }

    public void setIe_50103(Double ie_50103) {
        this.ie_50103 = ie_50103;
    }
    @Column(name = "ie_70072")
    public Double getIe_70072() {
        return ie_70072;
    }

    public void setIe_70072(Double ie_70072) {
        this.ie_70072 = ie_70072;
    }
    @Column(name = "ie_50231")
    public Double getIe_50231() {
        return ie_50231;
    }

    public void setIe_50231(Double ie_50231) {
        this.ie_50231 = ie_50231;
    }
    @Column(name = "ie_50295")
    public Double getIe_50295() {
        return ie_50295;
    }

    public void setIe_50295(Double ie_50295) {
        this.ie_50295 = ie_50295;
    }
    @Column(name = "ie_70527")
    public Double getIe_70527() {
        return ie_70527;
    }

    public void setIe_70527(Double ie_70527) {
        this.ie_70527 = ie_70527;
    }
    @Column(name = "ie_70007")
    public Double getIe_70007() {
        return ie_70007;
    }

    public void setIe_70007(Double ie_70007) {
        this.ie_70007 = ie_70007;
    }
    @Column(name = "ie_58003")
    public String getIe_58003() {
        return ie_58003;
    }

    public void setIe_58003(String ie_58003) {
        this.ie_58003 = ie_58003;
    }
    @Column(name = "ie_58035")
    public Double getIe_58035() {
        return ie_58035;
    }

    public void setIe_58035(Double ie_58035) {
        this.ie_58035 = ie_58035;
    }
    @Column(name = "ie_50168")
    public Double getIe_50168() {
        return ie_50168;
    }

    public void setIe_50168(Double ie_50168) {
        this.ie_50168 = ie_50168;
    }
    @Column(name = "ie_50104")
    public Double getIe_50104() {
        return ie_50104;
    }

    public void setIe_50104(Double ie_50104) {
        this.ie_50104 = ie_50104;
    }
    @Column(name = "ie_70073")
    public Double getIe_70073() {
        return ie_70073;
    }

    public void setIe_70073(Double ie_70073) {
        this.ie_70073 = ie_70073;
    }
    @Column(name = "ie_50232")
    public Double getIe_50232() {
        return ie_50232;
    }

    public void setIe_50232(Double ie_50232) {
        this.ie_50232 = ie_50232;
    }
    @Column(name = "ie_50296")
    public Double getIe_50296() {
        return ie_50296;
    }

    public void setIe_50296(Double ie_50296) {
        this.ie_50296 = ie_50296;
    }
    @Column(name = "ie_70528")
    public Double getIe_70528() {
        return ie_70528;
    }

    public void setIe_70528(Double ie_70528) {
        this.ie_70528 = ie_70528;
    }
    @Column(name = "ie_70008")
    public Double getIe_70008() {
        return ie_70008;
    }

    public void setIe_70008(Double ie_70008) {
        this.ie_70008 = ie_70008;
    }
    @Column(name = "ie_58004")
    public String getIe_58004() {
        return ie_58004;
    }

    public void setIe_58004(String ie_58004) {
        this.ie_58004 = ie_58004;
    }
    @Column(name = "ie_58036")
    public Double getIe_58036() {
        return ie_58036;
    }

    public void setIe_58036(Double ie_58036) {
        this.ie_58036 = ie_58036;
    }
    @Column(name = "ie_50169")
    public Double getIe_50169() {
        return ie_50169;
    }

    public void setIe_50169(Double ie_50169) {
        this.ie_50169 = ie_50169;
    }
    @Column(name = "ie_50105")
    public Double getIe_50105() {
        return ie_50105;
    }

    public void setIe_50105(Double ie_50105) {
        this.ie_50105 = ie_50105;
    }
    @Column(name = "ie_70074")
    public Double getIe_70074() {
        return ie_70074;
    }

    public void setIe_70074(Double ie_70074) {
        this.ie_70074 = ie_70074;
    }
    @Column(name = "ie_50233")
    public Double getIe_50233() {
        return ie_50233;
    }

    public void setIe_50233(Double ie_50233) {
        this.ie_50233 = ie_50233;
    }
    @Column(name = "ie_50297")
    public Double getIe_50297() {
        return ie_50297;
    }

    public void setIe_50297(Double ie_50297) {
        this.ie_50297 = ie_50297;
    }
    @Column(name = "ie_70529")
    public Double getIe_70529() {
        return ie_70529;
    }

    public void setIe_70529(Double ie_70529) {
        this.ie_70529 = ie_70529;
    }
    @Column(name = "ie_70009")
    public Double getIe_70009() {
        return ie_70009;
    }

    public void setIe_70009(Double ie_70009) {
        this.ie_70009 = ie_70009;
    }
    @Column(name = "ie_58005")
    public String getIe_58005() {
        return ie_58005;
    }

    public void setIe_58005(String ie_58005) {
        this.ie_58005 = ie_58005;
    }
    @Column(name = "ie_58037")
    public Double getIe_58037() {
        return ie_58037;
    }

    public void setIe_58037(Double ie_58037) {
        this.ie_58037 = ie_58037;
    }
    @Column(name = "ie_50170")
    public Double getIe_50170() {
        return ie_50170;
    }

    public void setIe_50170(Double ie_50170) {
        this.ie_50170 = ie_50170;
    }
    @Column(name = "ie_50106")
    public Double getIe_50106() {
        return ie_50106;
    }

    public void setIe_50106(Double ie_50106) {
        this.ie_50106 = ie_50106;
    }
    @Column(name = "ie_70075")
    public Double getIe_70075() {
        return ie_70075;
    }

    public void setIe_70075(Double ie_70075) {
        this.ie_70075 = ie_70075;
    }
    @Column(name = "ie_50234")
    public Double getIe_50234() {
        return ie_50234;
    }

    public void setIe_50234(Double ie_50234) {
        this.ie_50234 = ie_50234;
    }
    @Column(name = "ie_50298")
    public Double getIe_50298() {
        return ie_50298;
    }

    public void setIe_50298(Double ie_50298) {
        this.ie_50298 = ie_50298;
    }
    @Column(name = "ie_70530")
    public Double getIe_70530() {
        return ie_70530;
    }

    public void setIe_70530(Double ie_70530) {
        this.ie_70530 = ie_70530;
    }
    @Column(name = "ie_70010")
    public Double getIe_70010() {
        return ie_70010;
    }

    public void setIe_70010(Double ie_70010) {
        this.ie_70010 = ie_70010;
    }
    @Column(name = "ie_58006")
    public String getIe_58006() {
        return ie_58006;
    }

    public void setIe_58006(String ie_58006) {
        this.ie_58006 = ie_58006;
    }
    @Column(name = "ie_58038")
    public Double getIe_58038() {
        return ie_58038;
    }

    public void setIe_58038(Double ie_58038) {
        this.ie_58038 = ie_58038;
    }
    @Column(name = "ie_50171")
    public Double getIe_50171() {
        return ie_50171;
    }

    public void setIe_50171(Double ie_50171) {
        this.ie_50171 = ie_50171;
    }
    @Column(name = "ie_50107")
    public Double getIe_50107() {
        return ie_50107;
    }

    public void setIe_50107(Double ie_50107) {
        this.ie_50107 = ie_50107;
    }
    @Column(name = "ie_70076")
    public Double getIe_70076() {
        return ie_70076;
    }

    public void setIe_70076(Double ie_70076) {
        this.ie_70076 = ie_70076;
    }
    @Column(name = "ie_50235")
    public Double getIe_50235() {
        return ie_50235;
    }

    public void setIe_50235(Double ie_50235) {
        this.ie_50235 = ie_50235;
    }
    @Column(name = "ie_50299")
    public Double getIe_50299() {
        return ie_50299;
    }

    public void setIe_50299(Double ie_50299) {
        this.ie_50299 = ie_50299;
    }
    @Column(name = "ie_70531")
    public Double getIe_70531() {
        return ie_70531;
    }

    public void setIe_70531(Double ie_70531) {
        this.ie_70531 = ie_70531;
    }
    @Column(name = "ie_70011")
    public Double getIe_70011() {
        return ie_70011;
    }

    public void setIe_70011(Double ie_70011) {
        this.ie_70011 = ie_70011;
    }
    @Column(name = "ie_40076")
    public Double getIe_40076() {
        return ie_40076;
    }

    public void setIe_40076(Double ie_40076) {
        this.ie_40076 = ie_40076;
    }
    @Column(name = "ie_44207")
    public Double getIe_44207() {
        return ie_44207;
    }

    public void setIe_44207(Double ie_44207) {
        this.ie_44207 = ie_44207;
    }
    @Column(name = "ie_40140")
    public Double getIe_40140() {
        return ie_40140;
    }

    public void setIe_40140(Double ie_40140) {
        this.ie_40140 = ie_40140;
    }
    @Column(name = "ie_40172")
    public Double getIe_40172() {
        return ie_40172;
    }

    public void setIe_40172(Double ie_40172) {
        this.ie_40172 = ie_40172;
    }
    @Column(name = "ie_40204")
    public Double getIe_40204() {
        return ie_40204;
    }

    public void setIe_40204(Double ie_40204) {
        this.ie_40204 = ie_40204;
    }
    @Column(name = "ie_40300")
    public Double getIe_40300() {
        return ie_40300;
    }

    public void setIe_40300(Double ie_40300) {
        this.ie_40300 = ie_40300;
    }
    @Column(name = "ie_40236")
    public Double getIe_40236() {
        return ie_40236;
    }

    public void setIe_40236(Double ie_40236) {
        this.ie_40236 = ie_40236;
    }
    @Column(name = "ie_40077")
    public Double getIe_40077() {
        return ie_40077;
    }

    public void setIe_40077(Double ie_40077) {
        this.ie_40077 = ie_40077;
    }
    @Column(name = "ie_44208")
    public Double getIe_44208() {
        return ie_44208;
    }

    public void setIe_44208(Double ie_44208) {
        this.ie_44208 = ie_44208;
    }
    @Column(name = "ie_40141")
    public Double getIe_40141() {
        return ie_40141;
    }

    public void setIe_40141(Double ie_40141) {
        this.ie_40141 = ie_40141;
    }
    @Column(name = "ie_40173")
    public Double getIe_40173() {
        return ie_40173;
    }

    public void setIe_40173(Double ie_40173) {
        this.ie_40173 = ie_40173;
    }
    @Column(name = "ie_40205")
    public Double getIe_40205() {
        return ie_40205;
    }

    public void setIe_40205(Double ie_40205) {
        this.ie_40205 = ie_40205;
    }
    @Column(name = "ie_40301")
    public Double getIe_40301() {
        return ie_40301;
    }

    public void setIe_40301(Double ie_40301) {
        this.ie_40301 = ie_40301;
    }
    @Column(name = "ie_40237")
    public Double getIe_40237() {
        return ie_40237;
    }

    public void setIe_40237(Double ie_40237) {
        this.ie_40237 = ie_40237;
    }
    @Column(name = "ie_40078")
    public Double getIe_40078() {
        return ie_40078;
    }

    public void setIe_40078(Double ie_40078) {
        this.ie_40078 = ie_40078;
    }
    @Column(name = "ie_44209")
    public Double getIe_44209() {
        return ie_44209;
    }

    public void setIe_44209(Double ie_44209) {
        this.ie_44209 = ie_44209;
    }
    @Column(name = "ie_40142")
    public Double getIe_40142() {
        return ie_40142;
    }

    public void setIe_40142(Double ie_40142) {
        this.ie_40142 = ie_40142;
    }
    @Column(name = "ie_40174")
    public Double getIe_40174() {
        return ie_40174;
    }

    public void setIe_40174(Double ie_40174) {
        this.ie_40174 = ie_40174;
    }
    @Column(name = "ie_40206")
    public Double getIe_40206() {
        return ie_40206;
    }

    public void setIe_40206(Double ie_40206) {
        this.ie_40206 = ie_40206;
    }
    @Column(name = "ie_40302")
    public Double getIe_40302() {
        return ie_40302;
    }

    public void setIe_40302(Double ie_40302) {
        this.ie_40302 = ie_40302;
    }
    @Column(name = "ie_40238")
    public Double getIe_40238() {
        return ie_40238;
    }

    public void setIe_40238(Double ie_40238) {
        this.ie_40238 = ie_40238;
    }
    @Column(name = "ie_40079")
    public Double getIe_40079() {
        return ie_40079;
    }

    public void setIe_40079(Double ie_40079) {
        this.ie_40079 = ie_40079;
    }
    @Column(name = "ie_44210")
    public Double getIe_44210() {
        return ie_44210;
    }

    public void setIe_44210(Double ie_44210) {
        this.ie_44210 = ie_44210;
    }
    @Column(name = "ie_40143")
    public Double getIe_40143() {
        return ie_40143;
    }

    public void setIe_40143(Double ie_40143) {
        this.ie_40143 = ie_40143;
    }
    @Column(name = "ie_40175")
    public Double getIe_40175() {
        return ie_40175;
    }

    public void setIe_40175(Double ie_40175) {
        this.ie_40175 = ie_40175;
    }
    @Column(name = "ie_40207")
    public Double getIe_40207() {
        return ie_40207;
    }

    public void setIe_40207(Double ie_40207) {
        this.ie_40207 = ie_40207;
    }
    @Column(name = "ie_40303")
    public Double getIe_40303() {
        return ie_40303;
    }

    public void setIe_40303(Double ie_40303) {
        this.ie_40303 = ie_40303;
    }
    @Column(name = "ie_40239")
    public Double getIe_40239() {
        return ie_40239;
    }

    public void setIe_40239(Double ie_40239) {
        this.ie_40239 = ie_40239;
    }
    @Column(name = "ie_40080")
    public Double getIe_40080() {
        return ie_40080;
    }

    public void setIe_40080(Double ie_40080) {
        this.ie_40080 = ie_40080;
    }
    @Column(name = "ie_44211")
    public Double getIe_44211() {
        return ie_44211;
    }

    public void setIe_44211(Double ie_44211) {
        this.ie_44211 = ie_44211;
    }
    @Column(name = "ie_40144")
    public Double getIe_40144() {
        return ie_40144;
    }

    public void setIe_40144(Double ie_40144) {
        this.ie_40144 = ie_40144;
    }
    @Column(name = "ie_40176")
    public Double getIe_40176() {
        return ie_40176;
    }

    public void setIe_40176(Double ie_40176) {
        this.ie_40176 = ie_40176;
    }
    @Column(name = "ie_40208")
    public Double getIe_40208() {
        return ie_40208;
    }

    public void setIe_40208(Double ie_40208) {
        this.ie_40208 = ie_40208;
    }
    @Column(name = "ie_40304")
    public Double getIe_40304() {
        return ie_40304;
    }

    public void setIe_40304(Double ie_40304) {
        this.ie_40304 = ie_40304;
    }
    @Column(name = "ie_40240")
    public Double getIe_40240() {
        return ie_40240;
    }

    public void setIe_40240(Double ie_40240) {
        this.ie_40240 = ie_40240;
    }
    @Column(name = "ie_40081")
    public Double getIe_40081() {
        return ie_40081;
    }

    public void setIe_40081(Double ie_40081) {
        this.ie_40081 = ie_40081;
    }
    @Column(name = "ie_44212")
    public Double getIe_44212() {
        return ie_44212;
    }

    public void setIe_44212(Double ie_44212) {
        this.ie_44212 = ie_44212;
    }
    @Column(name = "ie_40145")
    public Double getIe_40145() {
        return ie_40145;
    }

    public void setIe_40145(Double ie_40145) {
        this.ie_40145 = ie_40145;
    }
    @Column(name = "ie_40177")
    public Double getIe_40177() {
        return ie_40177;
    }

    public void setIe_40177(Double ie_40177) {
        this.ie_40177 = ie_40177;
    }
    @Column(name = "ie_40209")
    public Double getIe_40209() {
        return ie_40209;
    }

    public void setIe_40209(Double ie_40209) {
        this.ie_40209 = ie_40209;
    }
    @Column(name = "ie_40305")
    public Double getIe_40305() {
        return ie_40305;
    }

    public void setIe_40305(Double ie_40305) {
        this.ie_40305 = ie_40305;
    }
    @Column(name = "ie_40241")
    public Double getIe_40241() {
        return ie_40241;
    }

    public void setIe_40241(Double ie_40241) {
        this.ie_40241 = ie_40241;
    }

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
    @Id
    @Column(name = "timestamp")
    public Long getTimeCol() {
        return timeCol;
    }

    public void setTimeCol(Long timeCol) {
        this.timeCol = timeCol;
    }

    public String getTime() {
       return sd.format(new Date(this.getTimeCol()));
    }
    private static final SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void setTime(String time) {
        this.time = time;
    }
    @Id
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
    @Column(name = "ie_54321")
    public Double getIe_54321() {
        return ie_54321;
    }

    public void setIe_54321(Double ie_54321) {
        this.ie_54321 = ie_54321;
    }
    @Column(name = "ie_54322")
    public Double getIe_54322() {
        return ie_54322;
    }

    public void setIe_54322(Double ie_54322) {
        this.ie_54322 = ie_54322;
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
