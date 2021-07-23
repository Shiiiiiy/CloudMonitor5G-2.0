package com.datang.service.influx;

import com.datang.common.util.IOUtils;
import com.datang.service.influx.bean.AbEventConfig;
import com.datang.service.influx.bean.NetTotalConfig;
import com.datang.service.influx.bean.VoiceBusiConfig;
import com.datang.service.influx.impl.InfluxServiceImpl;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfluxDb查询业务参数初始读取配置类
 */
public class InitialConfig {

    public static final Pattern p=Pattern.compile("name=\"(.*?)\".*mapnameroot=\"(.*?)\"");
    //提取网络参数配置前置条件的正则表达式
    public static final Pattern p1=Pattern.compile("\"(.*?)\"");
    //保存IECONFIG.XML 中的mapnameroot:name 的键值对
    public static final Map<String,String> map=new HashMap<>(5000);
    //读取采样点报表配置文件samp_title.csv中的配置
    public static final Map<String,String> map2=new HashMap<>(100);
    //读取事件报表配置文件evt_define.csv中的配置
    public static final Map<String,String> map3=new HashMap<>(100);
    //读取栅格报表KPI配置文件evt_kpi_define.csv中的配置
    public static final Map<String,String> kpiMap=new HashMap<>();
    public static final Map<String,Set<String>> countMap=new HashMap<>();
    public static final List<AbEventConfig> abEventConfigs=new ArrayList<>();
    //读取异常事件分析指标定义配置文件的配置
    public static final Map<String,String> abevtLteKpiMap=new LinkedHashMap<>();
    public static final Map<String,String> abevtNrKpiMap=new LinkedHashMap<>();
    public static final Map<String,String> abevtKpiMap2=new LinkedHashMap<>();
    //读取网络配置配置文件
    public static final List<AbEventConfig> netConfigs=new ArrayList<>();
    public static final Map<String,String> netLteKpiMap=new LinkedHashMap<>();
    public static final Map<String,String> netNrKpiMap=new LinkedHashMap<>();
    public static final List<NetTotalConfig> lteNetToatls=new ArrayList<>();
    public static final List<NetTotalConfig> nrNetToatls=new ArrayList<>();
    //语音业务配置
    public static final List<VoiceBusiConfig> voiceBusiConfigs=new ArrayList<>();
    static{
        InputStream resourceAsStream = InitialConfig.class.getResourceAsStream("/IEConfig.xml");
        InputStream sampcsvresourceAsStream = InitialConfig.class.getResourceAsStream("/samp_title.csv");
        InputStream evtcsvresourceAsStream = InitialConfig.class.getResourceAsStream("/evt_define.csv");
        InputStream evtkpicsvresourceAsStream = InitialConfig.class.getResourceAsStream("/evt_kpi_define.csv");
        InputStream abevtStream = InitialConfig.class.getResourceAsStream("/异常事件分析.csv");
        InputStream abevtKpiStream = InitialConfig.class.getResourceAsStream("/异常事件分析指标定义.csv");
        InputStream netStream = InitialConfig.class.getResourceAsStream("/网络参数.csv");
        InputStream netKpiStream = InitialConfig.class.getResourceAsStream("/网络参数IE列配置.csv");
        InputStream netToatlStream = InitialConfig.class.getResourceAsStream("/网络参数4G5G报表配置.csv");
        InputStream voiceStream = InitialConfig.class.getResourceAsStream("/语音业务分析主被叫配置.csv");
        BufferedReader fr= null;
        BufferedReader fr2= null;
        BufferedReader fr3= null;
        BufferedReader fr4= null;
        BufferedReader fr5= null;
        BufferedReader fr6= null;
        BufferedReader fr7= null;
        BufferedReader fr8= null;
        BufferedReader fr9= null;
        BufferedReader fr10= null;
        try {
            fr = new BufferedReader(new InputStreamReader(resourceAsStream,"UTF-8"));
            fr2 = new BufferedReader(new InputStreamReader(sampcsvresourceAsStream,"UTF-8"));
            fr3 = new BufferedReader(new InputStreamReader(evtcsvresourceAsStream,"UTF-8"));
            fr4 = new BufferedReader(new InputStreamReader(evtkpicsvresourceAsStream,"UTF-8"));
            fr5 = new BufferedReader(new InputStreamReader(abevtStream,"UTF-8"));
            fr6 = new BufferedReader(new InputStreamReader(abevtKpiStream,"UTF-8"));
            fr7 = new BufferedReader(new InputStreamReader(netStream,"UTF-8"));
            fr8 = new BufferedReader(new InputStreamReader(netKpiStream,"UTF-8"));
            fr9 = new BufferedReader(new InputStreamReader(netToatlStream,"UTF-8"));
            fr10 = new BufferedReader(new InputStreamReader(voiceStream,"UTF-8"));
            String line;
            while ((line = fr.readLine()) != null){
                if(StringUtils.isNotBlank((line))) {
                    Matcher matcher = p.matcher(line);
                    while (matcher.find()) {
                        map.put(matcher.group(2), matcher.group(1));
                    }
                }
            }
            String line2;
            while ((line2 = fr2.readLine()) != null){
                if(StringUtils.isNotBlank((line2))) {
                    String[] ss = line2.split(",",-1);
                    map2.put(ss[0].trim(), ss[1].trim());
                }
            }
            String line3;
            fr3.readLine();
            while ((line3 = fr3.readLine()) != null){
                if(StringUtils.isNotBlank((line3))) {
                    String[] ss=line3.split(",",-1);
                    map3.put(ss[0].trim(),ss[1].trim());
                }
            }
            String line4;
            fr4.readLine();
            while ((line4 = fr4.readLine()) != null){
                if(StringUtils.isNotBlank((line4))) {
                    String[] ss = line4.split(",",-1);
                    kpiMap.put(ss[0].trim(), ss[2].trim());
                }
            }
            kpiMap.forEach((k,v)->{
                countMap.put(k,InfluxReportUtils.getEnStr(v));
            });
            String line5;
            fr5.readLine();
            while ((line5 = fr5.readLine()) != null){
                if(StringUtils.isNotBlank((line5))) {
                    String[] ss = line5.split(",",-1);
                    AbEventConfig abEventConfig = new AbEventConfig();
                    abEventConfig.setId(Integer.parseInt(ss[0]));
                    abEventConfig.setType(ss[1]);
                    abEventConfig.setEvtCnName(ss[2]);
                    abEventConfig.setPreCon(StringUtils.split(ss[3], "/"));
                    abEventConfig.setTriggerEvt(StringUtils.split(ss[4], "/"));
                    abEventConfig.setTriggerSig(StringUtils.split(ss[5], "/"));
                    abEventConfig.setSuffCon(StringUtils.split(ss[6], "/"));
                    abEventConfig.setStartEvt(StringUtils.split(ss[7], "/"));
                    abEventConfigs.add(abEventConfig);
                }
            }
            String line6;
            fr6.readLine();
            while ((line6 = fr6.readLine()) != null){
                if(StringUtils.isNotBlank((line6))){
                    String[] ss=line6.split(",",-1);
                    if(ss[0].equalsIgnoreCase("lte")){
                        abevtLteKpiMap.put(ss[1],ss[3]);
                    }
                    if(ss[0].equalsIgnoreCase("lte")){
                        abevtNrKpiMap.put(ss[1],ss[3]);
                    }
                    abevtKpiMap2.put(ss[1],ss[2]);
                }
            }

            String line7;
            fr7.readLine();
            while ((line7 = fr7.readLine()) != null){
                if(StringUtils.isNotBlank((line7))) {
                    String[] ss = line7.split(",",-1);
                    AbEventConfig abEventConfig = new AbEventConfig();
                    abEventConfig.setType(ss[0]);
                    abEventConfig.setDetailSheet(ss[1]);
                    abEventConfig.setPreCon(StringUtils.split(ss[2], "/"));
                    abEventConfig.setTriggerEvt(StringUtils.split(ss[3], "/"));
                    abEventConfig.setSuffCon(StringUtils.split(ss[4], "/"));
                    netConfigs.add(abEventConfig);
                }
            }
            String line8;
            fr8.readLine();
            while ((line8 = fr8.readLine()) != null){
                if(StringUtils.isNotBlank((line8))){
                    String[] ss=line8.split(",",-1);
                    if(ss[0].equalsIgnoreCase("lte")){
                        netLteKpiMap.put(ss[1],ss[2]);
                    }
                    if(ss[0].equalsIgnoreCase("nr")){
                        netNrKpiMap.put(ss[1],ss[2]);
                    }
                }
            }
            String line9;
            fr9.readLine();
            while ((line9 = fr9.readLine()) != null){
                if(StringUtils.isNotBlank((line9))){
                    String[] ss=line9.split(",",-1);
                    if(ss[0].toLowerCase().contains("lte")){
                        lteNetToatls.add(new NetTotalConfig(ss[0],ss[1],ss[2],ss[3]));
                    }
                    if(ss[0].toLowerCase().contains("nr")){
                        nrNetToatls.add(new NetTotalConfig(ss[0],ss[1],ss[2],ss[3]));
                    }
                }
            }
            String line10;
            fr10.readLine();
            while ((line10 = fr10.readLine()) != null){
                if(StringUtils.isNotBlank((line10))){
                    String[] ss=line10.split(",",-1);
                   voiceBusiConfigs.add(new VoiceBusiConfig(ss[0],ss[1],ss[2],ss[3],ss[4],ss[5],ss[6]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(fr);
            IOUtils.close(fr2);
            IOUtils.close(resourceAsStream);
            IOUtils.close(sampcsvresourceAsStream);


            IOUtils.close(fr3);
            IOUtils.close(evtcsvresourceAsStream);

            IOUtils.close(fr4);
            IOUtils.close(evtkpicsvresourceAsStream);

            IOUtils.close(fr5);
            IOUtils.close(fr6);
            IOUtils.close(abevtStream);
            IOUtils.close(netKpiStream);

            IOUtils.close(fr7);
            IOUtils.close(fr8);
            IOUtils.close(netStream);
            IOUtils.close(fr9);
            IOUtils.close(netToatlStream);
            IOUtils.close(fr10);
            IOUtils.close(voiceStream);
        }
    }
}
