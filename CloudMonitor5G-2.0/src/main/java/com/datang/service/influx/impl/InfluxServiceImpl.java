package com.datang.service.influx.impl;

import com.datang.common.influxdb.InfludbUtil;
import com.datang.common.influxdb.InfluxDBMutiConntion;
import com.datang.common.influxdb.InfluxDbConnection;
import com.datang.common.util.DateComputeUtils;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.exceptionevent.GisAndListShowServie;
import com.datang.service.influx.InfluxReportUtils;
import com.datang.service.influx.InfluxService;
import com.datang.service.influx.InitialConfig;
import com.datang.service.influx.bean.AbEventConfig;
import com.datang.service.influx.bean.NetTotalConfig;
import com.datang.service.influx.bean.VoiceBusiConfig;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.util.AdjPlaneArithmetic;
import com.datang.util.GPSUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service(value = "influxService")
public class InfluxServiceImpl implements InfluxService {

    @Value("${nr.cmcc.length}")
    private int cmccGlen;
    @Value("${nr.ctcc.length}")
    private int ctccGlen;
    @Value("${nr.cucc.length}")
    private int cuccGlen;

    private static String MAPTRAILSQL="SELECT last({0}) as LTE_PCC_RSRP, last({1}) as LTE_PCC_SINR, last({2}) as NR_SS_RSRP, last({3}) as NR_SS_SINR,last(Lat) as Lat,last(Long) as Long FROM {4}  where Lat=~/./ and Long=~/./ GROUP BY time(1s) fill(none)";
    private static String[] refStrs=new String[]{"LTE PCC_RSRP","LTE PCC_SINR","NR SS-RSRP","NR SS-SINR"};
    private static String[] refStrsGrid=new String[]{"X","Y","Height","NR SS-RSRP","NR SS-RSRQ","NR SS-SINR","NR PCI","NR SSB ARFCN","NR ssb-Index[0]",
            "NR Ncell PCI[0]","NR Ncell ARFCNSSB_DL[0]","NR Ncell SS-RSRP[0]","NR Ncell SS-RSRQ[0]","NR Ncell SS-SINR[0]","NR Ncell ssb-index[0]",
            "NR Ncell PCI[1]","NR Ncell ARFCNSSB_DL[1]","NR Ncell SS-RSRP[1]","NR Ncell SS-RSRQ[1]","NR Ncell SS-SINR[1]","NR Ncell ssb-index[1]",
            "NR Ncell PCI[2]","NR Ncell ARFCNSSB_DL[2]","NR Ncell SS-RSRP[2]","NR Ncell SS-RSRQ[2]","NR Ncell SS-SINR[2]","NR Ncell ssb-index[2]",
            "NR Ncell PCI[3]","NR Ncell ARFCNSSB_DL[3]","NR Ncell SS-RSRP[3]","NR Ncell SS-RSRQ[3]","NR Ncell SS-SINR[3]","NR Ncell ssb-index[3]",
            "NR Ncell PCI[4]","NR Ncell ARFCNSSB_DL[4]","NR Ncell SS-RSRP[4]","NR Ncell SS-RSRQ[4]","NR Ncell SS-SINR[4]","NR Ncell ssb-index[4]",
            "NR Ncell PCI[5]","NR Ncell ARFCNSSB_DL[5]","NR Ncell SS-RSRP[5]","NR Ncell SS-RSRQ[5]","NR Ncell SS-SINR[5]","NR Ncell ssb-index[5]",
            "NR Ncell PCI[6]","NR Ncell ARFCNSSB_DL[6]","NR Ncell SS-RSRP[6]","NR Ncell SS-RSRQ[6]","NR Ncell SS-SINR[6]","NR Ncell ssb-index[6]",
            "NR Ncell PCI[7]","NR Ncell ARFCNSSB_DL[7]","NR Ncell SS-RSRP[7]","NR Ncell SS-RSRQ[7]","NR Ncell SS-SINR[7]","NR Ncell ssb-index[7]",
            "NR RLC Thrput DL(Mbps)","NR RLC Thrput UL(Mbps)","NR DL GrantNum","NR DL IBLER","NR DL RBLER","NR DL MCS","NR AVG CQI",
            "NR Rank Indicator","NR CW0 DL 256QAM Num","NR CW1 DL 256QAM Num","NR CW0 DL 64QAM Num",
            "NR CW1 DL 64QAM Num","NR CW0 DL 16QAM Num","NR CW1 DL 16QAM Num","NR CW0 DL QPSK Num","NR CW1 DL QPSK Num","NR UL MCS",
            "NR DL PRBNum/slot","NR UL IBLER","NR UL RBLER","NR UL RI Avr","NR UL 256QAM Num","NR UL 64QAM Num","NR UL 16QAM Num","NR UL QPSK Num","NR UL pi/2 BPSK Num",
            "NR PUSCH Pathloss","NR UL PRBNum/slot","NR PUSCH Txpower","LTE PCC_SINR","LTE PCC_RSRP","Registration Delay","MOS_MOSLQO"
    };

    private static DecimalFormat df = new DecimalFormat("#0.00");

    private static String lineChartSql="SELECT IEValue_40028 as RSRP, IEValue_40035 as SINR, IEValue_50055 as SS_RSRP, IEValue_50056 as SS_SINR ,IEValue_43724 as \"LTE PDCP Thrput DL(Mbps)\",IEValue_43725 as \"LTE PDCP Thrput UL(Mbps)\",IEValue_50962 as \"NR PHY Thrput UL(Mbps)\",IEValue_51213 as \"NR PHY Thrput DL(Mbps)\" FROM {0}";

    @Override
    public List<Map<String, Object>> lineChartDatas(long logId, String startTime, String endTime) {
        String sql=MessageFormat.format(lineChartSql,InfluxReportUtils.getTableName(logId,"IE"));
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(logId+"");
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        TestLogItem testLogItem = id2LogBeanMap.get(logId);
        InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
        return commonQueryDatas(influxDbConnection,sql,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> sigleDatas(long logId, String startTime, String endTime) {
        String sql=MessageFormat.format(sigSql,InfluxReportUtils.getTableName(logId,"SIG"));
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(logId+"");
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        TestLogItem testLogItem = id2LogBeanMap.get(logId);
        InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
        return commonQueryDatas(influxDbConnection, sql,startTime,endTime);
    }

    private static final String evtSql="SELECT evtName,Netmode,extrainfo,MsgID from {0}";
    private static final String sigSql="select Netmode,signalName,Dir,DetailMsg,MsgID from {0}";
    @Override
    public List<Map<String, Object>> evtDatas(long logId, String startTime, String endTime) {
        String sql=MessageFormat.format(evtSql,InfluxReportUtils.getTableName(logId,"EVT"));
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(logId+"");
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        TestLogItem testLogItem = id2LogBeanMap.get(logId);
        InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
        return commonQueryDatas(influxDbConnection, sql,startTime,endTime);
    }

   /* public static void main(String[] args) {
        int[] s=new int[]{40075,44206,40139,40171,40203,40299,40235};
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<8;i++){
            for(int m:s){
                int j=m+i;
                sb.append(",last(IEValue_"+j+") as ie_"+j+"");
            }
        }
        System.out.println(sb.toString());
    }*/

    private static final String synSql="select last(IEValue_40001) as ie_40001,last(IEValue_40002) as ie_40002,last(IEValue_40006) as ie_40006,last(IEValue_40007) as ie_40007,last(IEValue_40008) as ie_40008,last(IEValue_40009) as ie_40009,last(IEValue_40010) as ie_40010,last(IEValue_40014) as ie_40014,last(IEValue_40015) as ie_40015,last(IEValue_40016) as ie_40016,last(IEValue_40017) as ie_40017,last(IEValue_40019) as ie_40019,last(IEValue_40028) as ie_40028,last(IEValue_40031) as ie_40031,last(IEValue_40034) as ie_40034,last(IEValue_40035) as ie_40035,last(IEValue_40075) as ie_40075,last(IEValue_40139) as ie_40139,last(IEValue_40171) as ie_40171,last(IEValue_40203) as ie_40203,last(IEValue_40235) as ie_40235,last(IEValue_40299) as ie_40299,last(IEValue_44206) as ie_44206,last(IEValue_50001) as ie_50001,last(IEValue_50002) as ie_50002,last(IEValue_50003) as ie_50003,last(IEValue_50006) as ie_50006,last(IEValue_50007) as ie_50007,last(IEValue_50013) as ie_50013,last(IEValue_50015) as ie_50015,last(IEValue_50016) as ie_50016,last(IEValue_50017) as ie_50017,last(IEValue_50021) as ie_50021,last(IEValue_54321) as ie_54321,last(IEValue_54322) as ie_54322,last(IEValue_50055) as ie_50055,last(IEValue_50056) as ie_50056,last(IEValue_50057) as ie_50057,last(IEValue_50101) as ie_50101,last(IEValue_50165) as ie_50165,last(IEValue_50229) as ie_50229,last(IEValue_50293) as ie_50293,last(IEValue_53601) as ie_53601,last(IEValue_58000) as ie_58000,last(IEValue_58032) as ie_58032,last(IEValue_70005) as ie_70005,last(IEValue_70070) as ie_70070,last(IEValue_70525) as ie_70525" +
            ",last(IEValue_58001) as ie_58001,last(IEValue_58033) as ie_58033,last(IEValue_50166) as ie_50166,last(IEValue_50102) as ie_50102,last(IEValue_70071) as ie_70071,last(IEValue_50230) as ie_50230,last(IEValue_50294) as ie_50294,last(IEValue_70526) as ie_70526,last(IEValue_70006) as ie_70006,last(IEValue_58002) as ie_58002,last(IEValue_58034) as ie_58034,last(IEValue_50167) as ie_50167,last(IEValue_50103) as ie_50103,last(IEValue_70072) as ie_70072,last(IEValue_50231) as ie_50231,last(IEValue_50295) as ie_50295,last(IEValue_70527) as ie_70527,last(IEValue_70007) as ie_70007,last(IEValue_58003) as ie_58003,last(IEValue_58035) as ie_58035,last(IEValue_50168) as ie_50168,last(IEValue_50104) as ie_50104,last(IEValue_70073) as ie_70073,last(IEValue_50232) as ie_50232,last(IEValue_50296) as ie_50296,last(IEValue_70528) as ie_70528,last(IEValue_70008) as ie_70008,last(IEValue_58004) as ie_58004,last(IEValue_58036) as ie_58036,last(IEValue_50169) as ie_50169,last(IEValue_50105) as ie_50105,last(IEValue_70074) as ie_70074,last(IEValue_50233) as ie_50233,last(IEValue_50297) as ie_50297,last(IEValue_70529) as ie_70529,last(IEValue_70009) as ie_70009,last(IEValue_58005) as ie_58005,last(IEValue_58037) as ie_58037,last(IEValue_50170) as ie_50170,last(IEValue_50106) as ie_50106,last(IEValue_70075) as ie_70075,last(IEValue_50234) as ie_50234,last(IEValue_50298) as ie_50298,last(IEValue_70530) as ie_70530,last(IEValue_70010) as ie_70010,last(IEValue_58006) as ie_58006,last(IEValue_58038) as ie_58038,last(IEValue_50171) as ie_50171,last(IEValue_50107) as ie_50107,last(IEValue_70076) as ie_70076,last(IEValue_50235) as ie_50235,last(IEValue_50299) as ie_50299,last(IEValue_70531) as ie_70531,last(IEValue_70011) as ie_70011,last(IEValue_58007) as ie_58007,last(IEValue_58039) as ie_58039,last(IEValue_50172) as ie_50172,last(IEValue_50108) as ie_50108,last(IEValue_70077) as ie_70077,last(IEValue_50236) as ie_50236,last(IEValue_50300) as ie_50300,last(IEValue_70532) as ie_70532,last(IEValue_70012) as ie_70012,last(IEValue_40076) as ie_40076,last(IEValue_44207) as ie_44207,last(IEValue_40140) as ie_40140,last(IEValue_40172) as ie_40172,last(IEValue_40204) as ie_40204,last(IEValue_40300) as ie_40300,last(IEValue_40236) as ie_40236,last(IEValue_40077) as ie_40077,last(IEValue_44208) as ie_44208,last(IEValue_40141) as ie_40141,last(IEValue_40173) as ie_40173,last(IEValue_40205) as ie_40205,last(IEValue_40301) as ie_40301,last(IEValue_40237) as ie_40237,last(IEValue_40078) as ie_40078,last(IEValue_44209) as ie_44209,last(IEValue_40142) as ie_40142,last(IEValue_40174) as ie_40174,last(IEValue_40206) as ie_40206,last(IEValue_40302) as ie_40302,last(IEValue_40238) as ie_40238,last(IEValue_40079) as ie_40079,last(IEValue_44210) as ie_44210,last(IEValue_40143) as ie_40143,last(IEValue_40175) as ie_40175,last(IEValue_40207) as ie_40207,last(IEValue_40303) as ie_40303,last(IEValue_40239) as ie_40239,last(IEValue_40080) as ie_40080,last(IEValue_44211) as ie_44211,last(IEValue_40144) as ie_40144,last(IEValue_40176) as ie_40176,last(IEValue_40208) as ie_40208,last(IEValue_40304) as ie_40304,last(IEValue_40240) as ie_40240,last(IEValue_40081) as ie_40081,last(IEValue_44212) as ie_44212,last(IEValue_40145) as ie_40145,last(IEValue_40177) as ie_40177,last(IEValue_40209) as ie_40209,last(IEValue_40305) as ie_40305,last(IEValue_40241) as ie_40241,last(IEValue_40082) as ie_40082,last(IEValue_44213) as ie_44213,last(IEValue_40146) as ie_40146,last(IEValue_40178) as ie_40178,last(IEValue_40210) as ie_40210,last(IEValue_40306) as ie_40306,last(IEValue_40242) as ie_40242 from {0}";
    @Override
    public List<Map<String, Object>> syncIEWindow(Long logId, String time) {
        Date date = DateComputeUtils.formatDate(time);
        Date startDate = DateUtils.truncate(date, Calendar.SECOND);
        String sql=MessageFormat.format(synSql,InfluxReportUtils.getTableName(logId,"IE"));
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(logId+"");
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        TestLogItem testLogItem = id2LogBeanMap.get(logId);
        InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
        return commonQueryDatas(influxDbConnection, sql,startDate,time);
    }
    @Autowired
    private InfluxDBMutiConntion influxDBMutiConntion;

    private static final String evtPointSql="SELECT evtName from {0} WHERE ";
    @Override
    public List<Map<String, Object>> evtPointTimes(Long logId,String[] evts) {
        StringBuilder sb=new StringBuilder(evtPointSql);
        Set<String> cols=new HashSet<>();
        for(String evt:evts){
            cols.add("evtName=''"+evt+"''");
        };
        sb.append(cols.stream().collect(Collectors.joining(" or ")));
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(logId.toString());
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        TestLogItem testLogItem = id2LogBeanMap.get(logId);
        InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
        QueryResult query=influxDbConnection.query(MessageFormat.format(sb.toString(),InfluxReportUtils.getTableName(logId,"EVT")));
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        return result;
    }


    private List<Map<String, Object>> commonQueryDatas(InfluxDbConnection influxDbConnection, String sql, Object startTime, Object endTime) {
        StringBuilder sb=new StringBuilder(sql);
        if(startTime instanceof String){
            sb.append(" where time>='"+ DateComputeUtils.localToUTC(DateComputeUtils.formatDate(startTime.toString()))+"'");
        }else if(startTime instanceof java.util.Date){
            sb.append(" where time>='"+ DateComputeUtils.localToUTC((Date)startTime)+"'");
        }
        if(endTime instanceof String){
            sb.append( "AND time<'"+DateComputeUtils.localToUTC(DateComputeUtils.formatDate(endTime.toString()))+"'");
        }else if(endTime instanceof java.util.Date){
            sb.append(" AND time<'"+DateComputeUtils.localToUTC((Date)endTime)+"'");
        }
        QueryResult query=influxDbConnection.query(sb.toString());
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        result.forEach(item->{
            item.put("time", DateComputeUtils.formatMicroTime(item.get("time").toString()));
        });
        return result;
    }
    @Override
    public List<Map<String, Object>> queryRoadSampDatas(InfluxDbConnection influxDbConnection, String sql, List<Map<String, String>> timeLists, String[] wheres) {
        List<String> timeWheres=new ArrayList<>();
        StringBuilder sbSql=new StringBuilder(sql);
        timeLists.forEach(item->{
            StringBuilder sb=new StringBuilder("AND (");
            String startTime=item.get("startTime");
            String endTime=item.get("endTime");
            if(StringUtils.isNotBlank(startTime)){
                sb.append(" time>='"+ startTime+"'");
            }
            if(StringUtils.isNotBlank(startTime)){
                sb.append( "AND time<='"+endTime+"'");
            }
            sb.append(")");
            timeWheres.add(sb.toString());
        });
        sbSql.append(timeWheres.stream().collect(Collectors.joining(" or ")));
        if(null!=wheres){
            for(String where:wheres){
                sbSql.append(MessageFormat.format(" and {0}>"+ -5555+"",where));
            }
        }
        QueryResult query=influxDbConnection.query(sbSql.toString());
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        return result;
    }

    @Override
    public List<Map<String, Object>> getMapTrailByLogFiles(List<String> fileLogIds) {
        List<String> columns=new ArrayList<>();
        Arrays.asList(refStrs).forEach(item->{
            columns.add(InfluxReportUtils.getColumnName(item));
        });
        Object[] objects = columns.toArray();
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        fileLogIds.parallelStream().forEach(id->{
            Map<String,Object> resultMap=new HashMap<>();
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(id));
            String s=MessageFormat.format(MAPTRAILSQL, objects[0],objects[1],objects[2],objects[3],InfluxReportUtils.getTableName(id,"IE"));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(s);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            resultMap.put("logName", testLogItem.getFileName());
            resultMap.put("gpsData",result);
            results.add(resultMap);
        });
        return results;
    }

    @Autowired
    private UnicomLogItemService unicomLogItemService;
    @Autowired
    private GisAndListShowServie gisAndListShowServie;
    @Override
    public List<Map<String, Object>> getSampPointByLogFiles(List<String> ids) {
        List<Map<String, Object>> results=new ArrayList<>();
        List<String> sqlFreg=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        sb.append("select last(Height) as Height,");
        InitialConfig.map2.forEach((key,value)->{
            if(InitialConfig.map.containsKey(key)){
                sqlFreg.add("last("+InfluxReportUtils.getColumnName(key)+") as "+InfluxReportUtils.getColumnName(key));
            }
        });
        sqlFreg.add("last(Lat) as Lat");
        sqlFreg.add("last(Long) as Long");
        String collect = sqlFreg.stream().collect(Collectors.joining(","));
        sb.append(collect).append(" from {0} GROUP BY time(1s) fill(none)");
        String sql=sb.toString();
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(ids.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        ids.parallelStream().forEach(id->{
            String s=MessageFormat.format(sql,InfluxReportUtils.getTableName(id,"IE"));
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(id));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(s);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            results.addAll(fillColumns(id,result,id2LogBeanMap));

        });
        return results;
    }

    @Override
    public List<Map<String, Object>> getEventByLogFiles(List<String> fileLogIds) {
        List<Map<String, Object>> results=new ArrayList<>();
        List<String> sqlFreg=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT evtName,Lat,Long,Height,Netmode,Pci,Enfarcn,SellID,Rsrp,Sinr from {0} where ");
        InitialConfig.map3.forEach((key,value)->{
            String[] evtEnNames=value.split("???");
            List<String> evtNameList = Arrays.stream(evtEnNames).filter(item -> StringUtils.isNotBlank(item)).distinct().collect(Collectors.toList());
            for (String evtEnName : evtNameList) {
                sqlFreg.add("evtName =''"+evtEnName+"''");
            }
        });
        sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
        String sql=sb.toString();
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        fileLogIds.parallelStream().forEach(file->{
            String s=MessageFormat.format(sql,InfluxReportUtils.getTableName(file,"EVT"));
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(s);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            results.addAll(fillColumns(result,testLogItem));
        });
        return results;
    }


    private List<Map<String, Object>> getEventKpisByLogFiles(InfluxDbConnection influxDbConnection, String file) {
        StringBuilder sb=new StringBuilder();
        Collection<String> map = InitialConfig.kpiMap.values();
        Set<String> cols=new HashSet<>();
        map.forEach(item->{
            for (String s:InfluxReportUtils.getEnStr(item)) {
                cols.add("evtName='"+s+"'");
            }
        });
        //????????????group by
        String sql="SELECT Lat,X,Y,evtName from {1} where {0}";
        sb.append(cols.stream().collect(Collectors.joining(" or ")));
        String execSql=MessageFormat.format(sql,sb.toString(),InfluxReportUtils.getTableName(file,"EVT"));
        QueryResult query=influxDbConnection.query(execSql);
        List<Map<String, Object>> temp = InfludbUtil.paraseQueryResult(query);
        Map<String, List<Map<String, Object>>> collect = temp.stream().collect(Collectors.groupingBy(item -> item.get("X") + "-" + item.get("Y") + "-" + item.get("evtName")));
        List<Map<String, Object>> result=new ArrayList<>();
        collect.forEach((key,value)->{
            Map<String, Object> item=new HashMap<>();
            String[] split = key.split("-", -1);
            item.put("X", split[0]);
            item.put("Y", split[1]);
            item.put("evtName", split[2]);
            item.put("num", value.size());
            result.add(item);
        });
        return result;
    }

    //top1 ?????????????????????
    static String[] top1ServCellColumns = new String[]{"coverage11","coverage12","coverage13","coverage14","coverage15","coverage16","coverage17",
            "coverage18","coverage19","coverage20","coverage21","coverage22","coverage23","coverage10","coverage24","coverage25","data33","data34",
            "data35","data36","data37","data38","data39","data40","data41","data42","data43","data44","data45","data46","data47","data48",
            "data49","data50","data51","data52","data53","data54","data55","data56","data57","data58","data59","data60","data61","data62","data63","data64"
    };
    static String[] top2ServCellColumns = new String[]{"coverage45","coverage46","coverage47","coverage48","coverage49","coverage50","coverage51",
            "coverage52","coverage53","coverage54","coverage55","coverage56","coverage57","coverage44","coverage58","coverage59","data65","data66",
            "data67","data68","data69","data70","data71","data72","data73","data74","data75","data76","data77","data78","data79","data80",
            "data81","data82","data83","data84","data85","data86","data87","data88","data89","data90","data91","data92","data93","data94","data95","data96"
    };
    static String[] top1ncellServsColumns = new String[]{"coverage26","coverage33","coverage34","coverage27","coverage28","coverage29","coverage30","coverage31","coverage32","coverage35","coverage42","coverage43","coverage36","coverage37","coverage38","coverage39","coverage40","coverage41"
    };
    static String[] top2ncellServsColumns = new String[]{"coverage60","coverage67","coverage68","coverage61","coverage62","coverage63","coverage64","coverage65","coverage66","coverage69","coverage76","coverage77","coverage70","coverage71","coverage72","coverage73","coverage74","coverage75"
    };

    static String[] allColumns=new String[]{"coverage01","coverage02","coverage03","coverage04","coverage05","coverage06","coverage07","coverage08","coverage09","data01","data02","data03","data04","data05","data06","data07","data08","data09","data10","data11","data12","data13","data14","data15","data16","data17","data18","data19","data20","data21","data22","data23","data24","data25","data26","data27","data28","data29","data30","data31","data32","vfb97","vfb98","vfb99","vfb100","vfb101","vfb102","vfb103","vfb104","vfb105","vfb106","vfb107","vfb108","vfb109"};

    private static String samplat = "30.402345";
    private static String samplon ="120.523456";
    @Override
    public List<Map.Entry<String, List<Map<String, Object>>>>   getGridDatasByLogFiles(List<String> fileLogIds) {
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        List<String> sqlFreg=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        sb.append("select ");
        Arrays.stream(refStrsGrid).forEach(key->{
            if(InitialConfig.map.containsKey(key)){
                sqlFreg.add(InfluxReportUtils.getColumnName(key));
            }else {
                sqlFreg.add(key);
            }
        });
        sqlFreg.add("Long as Lon");
        sqlFreg.add("Lat");
        sqlFreg.add("Height");
        String collect = sqlFreg.stream().collect(Collectors.joining(","));
        sb.append(collect).append(" from {0} where X!=-1 AND Y!=-1");

        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Assert.notEmpty(testLogItems);
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<Map<String,Object>> opratorMaps=Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> eventKpis=Collections.synchronizedList(new ArrayList<>());
        fileLogIds.parallelStream().forEach(file->{
            TestLogItem testLogItem= id2LogBeanMap.get(Long.parseLong(file));
            //sb.append(" AND time>=''"+ DateComputeUtils.localToUTC(testLogItem.getStartDate())+"'' AND time<=''"+DateComputeUtils.localToUTC(testLogItem.getEndDate())+"''");
            String sql=sb.toString();
            Map<String,Object> opratorMap=new HashMap<>();
            String execSql=MessageFormat.format(sql,InfluxReportUtils.getTableName(file,"IE"));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(execSql);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            opratorMap.put("operator",testLogItem.getOperatorName());
            opratorMap.put("city",testLogItem.getCity());
            opratorMap.put("list",result);
            List<Map<String, Object>> eventKpisByLogFiles = getEventKpisByLogFiles(influxDbConnection,file);
            opratorMap.put("evtList",eventKpisByLogFiles);
            eventKpis.addAll(eventKpisByLogFiles);
            opratorMaps.add(opratorMap);
        });
        Map<String, List<Map<String, Object>>> totalDatas = opratorMaps.stream().collect(Collectors.groupingBy(item -> item.get("operator") + "_" + item.get("city")));
        totalDatas.entrySet().stream().parallel().forEach(entry-> {
            String m=entry.getKey();
            List<Map<String, Object>> list = entry.getValue();
            String[] ss=m.split("_");
            String city=ss[1];
            String operator=ss[0];
            List<Map<String, Object>> sampResult=new ArrayList<>();
            List<Map<String, Object>> evtKpiResult=new ArrayList<>();
            //???????????????????????????
            list.forEach(item->{
                List<Map<String, Object>> l = (List<Map<String, Object>>) item.get("list");
                List<Map<String, Object>> e = (List<Map<String, Object>>) item.get("evtList");
                sampResult.addAll(l);
                evtKpiResult.addAll(e);
            });
            //????????????????????????
            Map<String, List<Map<String, Object>>> evtxyGroupby = evtKpiResult.stream().collect(Collectors.groupingBy(item -> item.get("X") + "_" + item.get("Y")));
            Map<String, Map<String, Object>> evtRecords = evtKpitranferCol(evtxyGroupby);

            //??????????????????group by
            Map<String, List<Map<String, Object>>> xyGroupby = sampResult.stream().filter(item->!item.get("X").toString().equals("0")&&!item.get("Y").toString().equals("0")).collect(Collectors.groupingBy(item -> item.get("X") + "_" + item.get("Y")));
            List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(city);
            Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
            xyGroupby.entrySet().parallelStream().forEach(en -> {
                List<Map<String, Object>> items = en.getValue();
                Double heiht=items.stream().filter(item->null!=item.get("Height")).mapToDouble(item->Double.parseDouble(item.get("Height").toString())).average().getAsDouble();
                String key = en.getKey();
                Map<String, Object> rm = new HashMap<>();
                //???????????????????????????
                fillGridKpiColumns(rm,items,allColumns);
                //pci?????????group by
                Map<String, List<Map<String, Object>>> pciFcnGroupby = items.stream().collect(Collectors.groupingBy(item -> item.get(InfluxReportUtils.getColumnName("NR PCI")) + "_" + item.get(InfluxReportUtils.getColumnName("NR SSB ARFCN"))));
                Map<String, Double> pciFcn2avgRsrpMap = new HashMap<>();
                pciFcnGroupby.entrySet().stream().forEach(e -> {
                    Double rsrpAvg=InfluxReportUtils.getAvgKpi1(e.getValue(),"NR SS-RSRP");
                    pciFcn2avgRsrpMap.put(e.getKey(), rsrpAvg);
                });
                List<Map.Entry<String, Double>> pciFcn2avgRsrpListSorted = pciFcn2avgRsrpMap.entrySet().stream().filter(e->Objects.nonNull(e.getValue())).sorted(Comparator.comparingDouble((e) -> -e.getValue())).collect(Collectors.toList());
                Double[] doubles=new Double[2];
                if(pciFcn2avgRsrpListSorted!=null&&pciFcn2avgRsrpListSorted.size()>0){
                    Map.Entry<String, Double> firstRsrp = pciFcn2avgRsrpListSorted.get(0);
                    String lat = key.split("_")[1];
                    String longg = key.split("_")[0];
                    doubles = GPSUtils.xy2Lonlat(new BigDecimal(longg),new BigDecimal(lat),Double.parseDouble(samplon),Double.parseDouble(samplat));
                    fillServCellColumns(nrPciFcn2BeanMap, rm, pciFcnGroupby,firstRsrp,doubles[1], doubles[0], top1ServCellColumns);
                    fillNcellColumnsByServCell(nrPciFcn2BeanMap, rm, pciFcnGroupby, firstRsrp, doubles[1], doubles[0], top1ncellServsColumns);
                    rm.put("lon",doubles[0]);rm.put("lat",doubles[1]);rm.put("heiht",heiht);
                    rm.put("operator",operator);
                    if(evtRecords!=null&&evtRecords.containsKey(longg+"_"+lat)){
                        rm.putAll(evtRecords.get(longg+"_"+lat));
                    }
                }

                //TOP2??????????????????
                if(pciFcn2avgRsrpListSorted.size()>1){
                    Map.Entry<String, Double> secondRsrp = pciFcn2avgRsrpListSorted.get(1);
                    fillServCellColumns(nrPciFcn2BeanMap, rm, pciFcnGroupby, secondRsrp,doubles[1], doubles[0], top2ServCellColumns);
                    fillNcellColumnsByServCell(nrPciFcn2BeanMap, rm, pciFcnGroupby, secondRsrp,doubles[1], doubles[0], top2ncellServsColumns);
                }
                results.add(rm);
            });
        });
        return results.stream().filter(item->item.get("lon")!=null&&item.get("lat")!=null).collect(Collectors.groupingBy(item -> item.get("lon") +"_"+ item.get("lat") +"_"+ item.get("heiht"))).entrySet().stream().sorted(Comparator.comparing(MapCompare::comparingByLon).reversed().thenComparing(MapCompare::comparingByLat).reversed()).collect(Collectors.toList());
    }


    static class MapCompare {
        public  static  Double comparingByLon(Map.Entry<String,List<Map<String,Object>>> entry) {
            return Double.parseDouble(entry.getKey().split("_")[0]);
        }
        public  static Double comparingByLat(Map.Entry<String,List<Map<String,Object>>> entry) {
            return Double.parseDouble(entry.getKey().split("_")[1]);
        }
    }




    /**
     * ??????kpi??????
     * @param evtxyGroupby
     */
    private Map<String, Map<String, Object>> evtKpitranferCol(Map<String,List<Map<String,Object>>> evtxyGroupby) {
        Map<String,Map<String,Object>> records=new HashMap<>();
        evtxyGroupby.forEach((grid,list)->{
            Map<String,Object> record=new HashMap<>();
            list.stream().collect(Collectors.groupingBy(item->item.get("evtName").toString())).forEach((key,l)->{
                record.put(key,l.stream().mapToDouble(a->Double.parseDouble(a.get("num").toString())).sum());
            });
            Map<String, Object> map = InfluxReportUtils.evtKpiRateSet(InitialConfig.kpiMap,InitialConfig.countMap,record);
            records.put(grid,map);
        });
        return records;
    }






    /**
     * ??????????????????????????????
     * @param pciFcn2BeanMap
     * @param rm
     * @param pciFcnGroupby
     * @param firstRsrp
     * @param lat
     * @param longg
     * @param top1ServCellColumns
     */
    private void fillServCellColumns(Map<String, List<Cell5G>> pciFcn2BeanMap, Map<String, Object> rm, Map<String, List<Map<String, Object>>> pciFcnGroupby, Map.Entry<String, Double> firstRsrp, Double lat, Double longg, String[] top1ServCellColumns) {
        List<Object> l=new ArrayList<>();
        l.add(firstRsrp.getKey().split("_")[0]);
        l.add(firstRsrp.getKey().split("_")[1]);
        String sinrAvg = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-SINR");
        String rsrqAvg = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-RSRQ");
        Map<String, Long> ssIndex2countMap = pciFcnGroupby.get(firstRsrp.getKey()).stream().filter(item -> item.get(InfluxReportUtils.getColumnName("NR ssb-Index[0]")) != null).map(f -> String.valueOf(f.get(InfluxReportUtils.getColumnName("NR ssb-Index[0]")))).collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        List<Map.Entry<String, Long>> ssIndex2countMapSorted = ssIndex2countMap.entrySet().stream().sorted(Comparator.comparingLong(f -> -f.getValue())).collect(Collectors.toList());
        if(ssIndex2countMapSorted!=null&&!ssIndex2countMapSorted.isEmpty()){
            l.add(ssIndex2countMapSorted.get(0).getKey());
        }else{
            l.add(null);
        }
        long top1rsrpSampCount = InfluxReportUtils.getCountKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-RSRP");
        long top1sinrSampCount = InfluxReportUtils.getCountKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-SINR");
        l.add(top1rsrpSampCount);
        l.add(df.format(firstRsrp.getValue()));
        //TOP1????????????SS-RSRP???-105dBm??????
        Predicate<Map<String,Object>> predicate=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-105;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-RSRP",predicate));
        //TOP1????????????SS-RSRP???-110dBm??????
        Predicate<Map<String,Object>> predicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-110;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-RSRP",predicate1));
        l.add(top1sinrSampCount);
        l.add(sinrAvg);
        //TOP1????????????SS-SINR???0dB?????????
        Predicate<Map<String,Object>> predicate2=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=0.0;
        long top1servcellsinr0db = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-SINR",predicate2);
        l.add(top1servcellsinr0db);
        //TOP1????????????SS-SINR???-3dB?????????
        Predicate<Map<String,Object>> predicate3=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=-3;
        long top1servcellsinr3db = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-SINR",predicate3);
        l.add(top1servcellsinr3db);
        //TOP1????????????SS-RSRP???-105dBm&SS-SINR???-3dB??????
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,pciFcnGroupby.get(firstRsrp.getKey()).stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))!=null)&&item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))!=null&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))))>=-3&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))))>=-105).count());
        //TOP1????????????SS AVG RSRQ(dB)
        l.add(rsrqAvg);
        //TOP1???????????? ?????????????????????
        if(pciFcn2BeanMap.get(firstRsrp.getKey())!=null){
            setCellMsg(l,lat,longg,pciFcn2BeanMap.get(firstRsrp.getKey()));
        }else{
            l.add(null);
            l.add(null);
            l.add(null);
        }
        //TOP1??????NR RLC ????????????????????????????????????
        long nrrlckpi1 = pciFcnGroupby.get(firstRsrp.getKey()).stream().filter(item -> item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")) != null).count();
        l.add(nrrlckpi1);
        //TOP1??????NR RLC ?????????????????????(Mbps)
        String nrrlckpi2 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR RLC Thrput DL(Mbps)");
        l.add(nrrlckpi2);
        //TOP1??????NR RLC ????????????????????????????????????
        long nrrlckpi3 = pciFcnGroupby.get(firstRsrp.getKey()).stream().filter(item -> item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")) != null).count();
        l.add(nrrlckpi3);
        //TOP1??????NR RLC ?????????????????????(Mbps)
        String nrrlckpi4 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR RLC Thrput UL(Mbps)");
        l.add(nrrlckpi4);
        //TOP1??????NR RLC????????????????????????(???100M)
        Predicate<Map<String,Object>> predicate20=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")).toString())<=100;
        long countFilterKpi = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput DL(Mbps)", predicate20);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi);
        //TOP1??????NR RLC????????????????????????(???500M)
        Predicate<Map<String,Object>> predicate21=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")).toString())>=500;
        long countFilterKpi2 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput DL(Mbps)", predicate21);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi2);
        //TOP1??????NR RLC????????????????????????(???800M)
        Predicate<Map<String,Object>> predicate22=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")).toString())>=800;
        long countFilterKpi3 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput DL(Mbps)", predicate22);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi3);
        //TOP1??????NR RLC????????????????????????(???10M)
        Predicate<Map<String,Object>> predicate23=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")).toString())<=10;
        long countFilterKpi4 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput UL(Mbps)", predicate23);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi4);
        //TOP1??????NR RLC????????????????????????(???120M)
        Predicate<Map<String,Object>> predicate24=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")).toString())>=120;
        long countFilterKpi5 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput UL(Mbps)", predicate24);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi5);
        //TOP1??????NR RLC????????????????????????(???160M)
        Predicate<Map<String,Object>> predicate25=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")).toString())>=160;
        long countFilterKpi6 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput UL(Mbps)", predicate25);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi6);
        //TOP1??????NR_PDCCH DL Grant Count
        String nrrlckpi11 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL GrantNum");
        l.add(nrrlckpi11);
        //TOP1??????NR_DL Initial BLER(%)
        String nrrlckpi12 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL IBLER");

        l.add(nrrlckpi12);
        //TOP1??????NR_DL Residual BLER(%)
        String nrrlckpi13 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL RBLER");
        l.add(nrrlckpi13);
        //TOP1??????NR_DL Avg MCS
        String nrrlckpi14 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL MCS");
        l.add(nrrlckpi14);
        //TOP1??????NR_ DL Avg CQI
        String nrrlckpi15 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR AVG CQI");
        l.add(nrrlckpi15);
        //TOP1??????NR_DL Avg Rank
        String nrrlckpi16 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR Rank Indicator");
        l.add(nrrlckpi16);
        Double x1=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 256QAM Num"));
        Double sum = InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL QPSK Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL QPSK Num"));
        //TOP1??????NR??????256QAM??????(%)
        InfluxReportUtils.addKpi(l, sum, x1);
        Double x2=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 64QAM Num"));
        //TOP1??????NR??????64QAM??????(%)
        InfluxReportUtils.addKpi(l, sum, x2);
        Double x3=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 16QAM Num"));
        //TOP1??????NR??????16QAM??????(%)
        InfluxReportUtils.addKpi(l, sum, x3);
        Double x4=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL QPSK Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL QPSK Num"));
        //TOP1??????NR??????QPSK??????(%)
        InfluxReportUtils.addKpi(l, sum, x4);
        //TOP1??????????????????PRB?????????/slot
        String nrrlckpi21 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL PRBNum/slot");
        l.add(nrrlckpi21);
        //TOP1??????NR_UL Avg MCS
        String nrrlckpi22 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL MCS");
        l.add(nrrlckpi22);
        //TOP1??????NR_UL Initial BLER(%)
        String nrrlckpi23 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL IBLER");
        l.add(nrrlckpi23);
        //TOP1??????NR_UL Residual BLER(%)
        String nrrlckpi24 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL RBLER");
        l.add(nrrlckpi24);
        //TOP1??????NR_UL Avg Rank
        String nrrlckpi25 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL RI Avr");
        l.add(nrrlckpi25);
        Double sum2 = InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL 256QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL 64QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL 16QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL QPSK Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL pi/2 BPSK Num"));
        String x6=InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL 256QAM Num");
        String x7=InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL 64QAM Num");
        String x8=InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL 16QAM Num");
        String x9=InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL QPSK Num");
        //TOP1??????NR??????256QAM??????(%)
        InfluxReportUtils.addKpi(l, sum2, x6);
        //TOP1??????NR?????? 64QAM??????(%)
        InfluxReportUtils.addKpi(l, sum2, x7);
        //TOP1??????NR?????? 16QAM??????(%)
        InfluxReportUtils.addKpi(l, sum2, x8);
        //TOP1??????NR?????? QPSK??????(%)
        InfluxReportUtils.addKpi(l, sum2, x9);
        //TOP1??????NR_Path Loss(dB)
        String nrrlckpi30 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR PUSCH Pathloss");
        l.add(nrrlckpi30);
        //TOP1??????????????????PRB?????????/slot
        String nrrlckpi31 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL PRBNum/slot");
        l.add(nrrlckpi31);
        //TOP1??????PUSCH ??????TxPower???dBm???
        String nrrlckpi32 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR PUSCH Txpower");
        l.add(nrrlckpi32);
        for(int i=0;i<top1ServCellColumns.length;i++){
            rm.put(top1ServCellColumns[i],l.get(i));
        }

    }

    private void setCellMsg(List<Object> l, Double dlat, Double dlong, List<Cell5G> cell5GS) {
        if(dlat==null||dlong==null){
            return;
        }
        AtomicReference<Double> minDis= new AtomicReference<>(0.0);
        AtomicReference<Cell5G> planParamPojo=new AtomicReference<>();
        if(cell5GS==null||cell5GS.isEmpty()){
            return;
        }
        cell5GS.forEach(item->{
            Double distance=GPSUtils.distance(dlat,dlong,item.getLatitude(),item.getLongitude());
            if(distance<3000){
                if(minDis.get()>distance){
                    minDis.set(distance);
                    planParamPojo.set(item);
                }
            }
        });
        Cell5G planParamPojo1 = planParamPojo.get();
        l.add(planParamPojo1 ==null?null:planParamPojo1.getCellName());
        l.add(minDis ==null?null:minDis.get());
        l.add(planParamPojo1 ==null?null:AdjPlaneArithmetic.calculateAngle(dlong,(double)planParamPojo1.getLongitude(),dlat,(double)planParamPojo1.getLatitude(),(double)planParamPojo1.getAzimuth()));
    }



    /**
     * ??????????????????????????????
     * @param rm
     * @param pciFcnGroupby
     * @param top1ServCellColumns
     */
    private void fillGridKpiColumns(Map<String, Object> rm, List<Map<String, Object>> pciFcnGroupby, String[] top1ServCellColumns) {
        List<Object> l=new ArrayList<>();
        String sinrAvg = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR SS-SINR");
        String rsrpAvg = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR SS-RSRP");
        Long top1rsrpSampCount = InfluxReportUtils.getCountKpi(pciFcnGroupby,"NR SS-RSRP");
        Long top1sinrSampCount = InfluxReportUtils.getCountKpi(pciFcnGroupby,"NR SS-SINR");
        l.add(top1rsrpSampCount);
        l.add(top1sinrSampCount);
        l.add(rsrpAvg);
        l.add(sinrAvg);

        //??????SS-RSRP???-105dBm??????
        Predicate<Map<String,Object>> predicate=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-105;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-RSRP",predicate));
        //??????SS-RSRP???-110dBm??????
        Predicate<Map<String,Object>> predicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-110;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-RSRP",predicate1));
        //SS-SINR???0dB?????????
        Predicate<Map<String,Object>> predicate2=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=0.0;
        Long top1servcellsinr0db = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-SINR",predicate2);
        l.add(top1servcellsinr0db);
        //SS-SINR???-3dB??????
        Predicate<Map<String,Object>> predicate3=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=-3;
        Long top1servcellsinr3db = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-SINR",predicate3);
        InfluxReportUtils.addKpi(l,top1sinrSampCount,top1servcellsinr3db);
        //SS-RSRP???-105dBm&SS-SINR???-3dB??????
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))!=null)&&item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))!=null&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))))>=-3&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))))>=-105).count());

        //??????NR RLC ????????????????????????????????????
        Long nrrlckpi1 = InfluxReportUtils.getCountKpi(pciFcnGroupby,"NR RLC Thrput DL(Mbps)");
        l.add(nrrlckpi1);
        //??????NR RLC ?????????????????????(Mbps)
        String nrrlckpi2 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR RLC Thrput DL(Mbps)");
        l.add(nrrlckpi2);
        //??????NR RLC ????????????????????????????????????
        Long nrrlckpi3 = InfluxReportUtils.getCountKpi(pciFcnGroupby,"NR RLC Thrput UL(Mbps)");
        l.add(nrrlckpi3);
        //??????NR RLC ?????????????????????(Mbps)
        String nrrlckpi4 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR RLC Thrput UL(Mbps)");
        l.add(nrrlckpi4);
        //??????NR RLC????????????????????????(???100M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))))<=100).count());
        //??????NR RLC????????????????????????(???500M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))))>=500).count());
        //??????NR RLC????????????????????????(???800M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))))>=800).count());
        //??????NR RLC????????????????????????(???10M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))))<=10).count());
        //??????NR RLC????????????????????????(???120M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))))>=120).count());
        //??????NR RLC????????????????????????(???160M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))))>=160).count());
        //??????NR_PDCCH DL Grant Count
        String nrrlckpi11 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL GrantNum");
        l.add(nrrlckpi11);
        //??????NR_DL Initial BLER(%)
        String nrrlckpi12 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL IBLER");
        l.add(nrrlckpi12);
        //??????NR_DL Residual BLER(%)
        String nrrlckpi13 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL RBLER");
        l.add(nrrlckpi13);
        //??????NR_DL Avg MCS
        String nrrlckpi14 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL MCS");
        l.add(nrrlckpi14);
        //??????NR_ DL Avg CQI
        String nrrlckpi15 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR AVG CQI");
        l.add(nrrlckpi15);
        //??????NR_DL Avg Rank
        String nrrlckpi16 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR Rank Indicator");
        l.add(nrrlckpi16);
        Double x1=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 256QAM Num"));
        Double sum = InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 256QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL QPSK Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL QPSK Num"));
        //??????NR??????256QAM??????(%)
        InfluxReportUtils.addKpi(l,sum,x1);
        Double x2=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 64QAM Num"));
        //??????NR??????64QAM??????(%)
        InfluxReportUtils.addKpi(l,sum,x2);
        Double x3=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 16QAM Num"));
        //??????NR??????16QAM??????(%)
        InfluxReportUtils.addKpi(l,sum,x3);
        Double x4=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL QPSK Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL QPSK Num"));
        //??????NR??????QPSK??????(%)
        InfluxReportUtils.addKpi(l,sum,x4);
        //??????????????????PRB?????????/slot
        String nrrlckpi21 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL PRBNum/slot");
        l.add(nrrlckpi21);
        //??????NR_UL Avg MCS
        String nrrlckpi22 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL MCS");
        l.add(nrrlckpi22);
        //??????NR_UL Initial BLER(%)
        String nrrlckpi23 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL IBLER");
        l.add(nrrlckpi23);
        //??????NR_UL Residual BLER(%)
        String nrrlckpi24 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL RBLER");
        l.add(nrrlckpi24);
        //??????NR_UL Avg Rank
        String nrrlckpi25 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL RI Avr");
        l.add(nrrlckpi25);
        Double sum2 = InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL 256QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL 64QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL 16QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL QPSK Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL pi/2 BPSK Num"));
        String x6=InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL 256QAM Num");
        String x7=InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL 64QAM Num");
        String x8=InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL 16QAM Num");
        String x9=InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR UL QPSK Num");
        //??????NR??????256QAM??????(%)
        InfluxReportUtils.addKpi(l,sum2,x6);
        //??????NR?????? 64QAM??????(%)
        InfluxReportUtils.addKpi(l,sum2,x7);
        //??????NR?????? 16QAM??????(%)
        InfluxReportUtils.addKpi(l,sum2,x8);
        //??????NR?????? QPSK??????(%)
        InfluxReportUtils.addKpi(l,sum2,x9);
        //??????NR_Path Loss(dB)
        String nrrlckpi30 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR PUSCH Pathloss");
        l.add(nrrlckpi30);
        //??????????????????PRB?????????/slot
        String nrrlckpi31 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL PRBNum/slot");
        l.add(nrrlckpi31);
        //??????PUSCH ??????TxPower???dBm
        String nrrlckpi32 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR PUSCH Txpower");
        l.add(nrrlckpi32);

        //LTE_RSRP???????????????
        Long ltekpi33=InfluxReportUtils.getCountKpi(pciFcnGroupby,"LTE PCC_RSRP");
        l.add(ltekpi33);
        //LTE_SINR????????????
        Long ltekpi34=InfluxReportUtils.getCountKpi(pciFcnGroupby,"LTE PCC_SINR");
        l.add(ltekpi34);
        //LTE_Serving  AVG RSRP(dBm)
        String ltekpi35=InfluxReportUtils.getAvgKpi(pciFcnGroupby,"LTE PCC_RSRP");
        l.add(ltekpi35);
        //LTE_Serving  AVG SINR(dB)
        String ltekpi36=InfluxReportUtils.getAvgKpi(pciFcnGroupby,"LTE PCC_SINR");
        l.add(ltekpi36);
        //LTE_RSRP???-112dBm???????????????
        Predicate<Map<String,Object>> ltepredicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("LTE PCC_RSRP")).toString())>=-112;
        InfluxReportUtils.addKpi(l,ltekpi33,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"LTE PCC_RSRP",ltepredicate1));
        //LTE_SINR???-2dBm???????????????
        Predicate<Map<String,Object>> ltepredicate2=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("LTE PCC_SINR")).toString())>=-2;
        InfluxReportUtils.addKpi(l,ltekpi34,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"LTE PCC_SINR",ltepredicate2));
        //LTE???????????????(RSRP???-112dBm&SINR???-2dB)
        Predicate<Map<String,Object>> ltepredicate3=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("LTE PCC_RSRP")).toString())>=-112;
        Predicate<Map<String,Object>> ltepredicate4=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("LTE PCC_SINR")).toString())>=-2;
        List<Predicate<Map<String,Object>>> lp=new ArrayList<>();
        lp.add(ltepredicate3);
        lp.add(ltepredicate4);
        InfluxReportUtils.addKpi(l,ltekpi33,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,new String[]{"LTE PCC_RSRP","LTE PCC_SINR"},lp));
        //IMS????????????(s)
        l.add(InfluxReportUtils.getAvgKpi(pciFcnGroupby,"Registration Delay"));
        //MOS??????????????????
        l.add(InfluxReportUtils.getAvgKpi(pciFcnGroupby,"MOS_MOSLQO"));
        //MOS???3.0????????????????????????
        Predicate<Map<String,Object>> ltepredicate13=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("MOS_MOSLQO")).toString())>=3.0;
        Long mos_moslqo1 = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby, "MOS_MOSLQO", ltepredicate13);
        l.add(mos_moslqo1);
        //MOS???3.0???????????????????????????
        Long mos_moslqo = InfluxReportUtils.getCountKpi(pciFcnGroupby, "MOS_MOSLQO");
        InfluxReportUtils.addKpi(l,mos_moslqo,mos_moslqo1);
        //MOS???3.5????????????????????????
        Predicate<Map<String,Object>> ltepredicate14=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("MOS_MOSLQO")).toString())>=3.5;
        Long mos_moslqo2 = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby, "MOS_MOSLQO", ltepredicate14);
        l.add(mos_moslqo2);
        //MOS???3.5???????????????????????????
        InfluxReportUtils.addKpi(l,mos_moslqo,mos_moslqo2);
        for(int i=0;i<top1ServCellColumns.length;i++){
            rm.put(top1ServCellColumns[i],l.get(i));
        }

    }

    /**
     * ???????????????????????????????????????
     * @param pciFcn2BeanMap
     * @param rm
     * @param pciFcnGroupby
     * @param firstRsrp
     * @param lat
     * @param longg
     * @param topnServsCols
     */
    void fillNcellColumnsByServCell(Map<String, List<Cell5G>> pciFcn2BeanMap, Map<String, Object> rm, Map<String, List<Map<String, Object>>> pciFcnGroupby, Map.Entry<String, Double> firstRsrp, Double lat, Double longg, String[] topnServsCols) {
        //??????????????????RSRP????????????????????????
        List<Map<String,Object>> ncell2rsrpMapList=new ArrayList<>();
        pciFcnGroupby.get(firstRsrp.getKey()).forEach(item->{
            Map<String,Object> ncell2rsrpMap0=new HashMap<>();
            //???????????????????????????pci???fcn
            Object pci0 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[0]"))).orElse(null);
            Object fcn0=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[0]"))).orElse(null);
            Object rsrp0=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[0]"))).orElse(null);
            Object rsrq0=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[0]"))).orElse(null);
            Object sinr0=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[0]"))).orElse(null);
            Object ssbIndex0=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[0]"))).orElse(null);
            if(pci0!=null&&fcn0!=null&&rsrp0!=null){
                ncell2rsrpMap0.put("key",pci0+"_"+fcn0);
                ncell2rsrpMap0.put("value",rsrp0);
                ncell2rsrpMap0.put("rsrq",rsrq0);
                ncell2rsrpMap0.put("sinr",sinr0);
                ncell2rsrpMap0.put("ssbIndex",ssbIndex0);
                ncell2rsrpMapList.add(ncell2rsrpMap0);
            }
            Map<String,Object> ncell2rsrpMap1=new HashMap<>();
            //???????????????????????????pci???fcn
            Object pci1 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[1]"))).orElse(null);
            Object fcn1=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[1]"))).orElse(null);
            Object rsrp1=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[1]"))).orElse(null);
            Object rsrq1=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[1]"))).orElse(null);
            Object sinr1=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[1]"))).orElse(null);
            Object ssbIndex1=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[1]"))).orElse(null);
            if(pci1!=null&&fcn1!=null&&rsrp1!=null){
                ncell2rsrpMap1.put("key",pci1+"_"+fcn1);
                ncell2rsrpMap1.put("value",rsrp1);
                ncell2rsrpMap1.put("rsrq",rsrq1);
                ncell2rsrpMap1.put("sinr",sinr1);
                ncell2rsrpMap1.put("ssbIndex",ssbIndex1);
                ncell2rsrpMapList.add(ncell2rsrpMap1);
            }
            //???????????????????????????pci???fcn
            Map<String,Object> ncell2rsrpMap2=new HashMap<>();
            Object pci2 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[2]"))).orElse(null);
            Object fcn2=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[2]"))).orElse(null);
            Object rsrp2=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[2]"))).orElse(null);
            Object rsrq2=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[2]"))).orElse(null);
            Object sinr2=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[2]"))).orElse(null);
            Object ssbIndex2=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[2]"))).orElse(null);
            if(pci2!=null&&fcn2!=null&&rsrp2!=null){
                ncell2rsrpMap2.put("key",pci2+"_"+fcn2);
                ncell2rsrpMap2.put("value",rsrp2);
                ncell2rsrpMap2.put("rsrq",rsrq2);
                ncell2rsrpMap2.put("sinr",sinr2);
                ncell2rsrpMap2.put("ssbIndex",ssbIndex2);
                ncell2rsrpMapList.add(ncell2rsrpMap2);
            }
            //???????????????????????????pci???fcn
            Map<String,Object> ncell2rsrpMap3=new HashMap<>();
            Object pci3 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[3]"))).orElse(null);
            Object fcn3=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[3]"))).orElse(null);
            Object rsrp3=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[3]"))).orElse(null);
            Object rsrq3=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[3]"))).orElse(null);
            Object sinr3=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[3]"))).orElse(null);
            Object ssbIndex3=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[3]"))).orElse(null);
            if(pci3!=null&&fcn3!=null&&rsrp3!=null){
                ncell2rsrpMap3.put("key",pci3+"_"+fcn3);
                ncell2rsrpMap3.put("value",rsrp3);
                ncell2rsrpMap3.put("rsrq",rsrq3);
                ncell2rsrpMap3.put("sinr",sinr3);
                ncell2rsrpMap3.put("ssbIndex",ssbIndex3);
                ncell2rsrpMapList.add(ncell2rsrpMap3);
            }
            //???????????????????????????pci???fcn
            Map<String,Object> ncell2rsrpMap4=new HashMap<>();
            Object pci4 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[4]"))).orElse(null);
            Object fcn4=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[4]"))).orElse(null);
            Object rsrp4=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[4]"))).orElse(null);
            Object rsrq4=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[4]"))).orElse(null);
            Object sinr4=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[4]"))).orElse(null);
            Object ssbIndex4=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[4]"))).orElse(null);

            if(pci4!=null&&fcn4!=null&&rsrp4!=null){
                ncell2rsrpMap4.put("key",pci4+"_"+fcn4);
                ncell2rsrpMap4.put("value",rsrp4);
                ncell2rsrpMap4.put("rsrq",rsrq4);
                ncell2rsrpMap4.put("sinr",sinr4);
                ncell2rsrpMap4.put("ssbIndex",ssbIndex4);
                ncell2rsrpMapList.add(ncell2rsrpMap4);
            }
            //???????????????5?????????pci???fcn
            Map<String,Object> ncell2rsrpMap5=new HashMap<>();
            Object pci5 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[5]"))).orElse(null);
            Object fcn5=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[5]"))).orElse(null);
            Object rsrp5=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[5]"))).orElse(null);
            Object rsrq5=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[5]"))).orElse(null);
            Object sinr5=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[5]"))).orElse(null);
            Object ssbIndex5=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[5]"))).orElse(null);
            if(pci5!=null&&fcn5!=null&&rsrp5!=null){
                ncell2rsrpMap5.put("key",pci5+"_"+fcn5);
                ncell2rsrpMap5.put("value",rsrp5);
                ncell2rsrpMap5.put("rsrq",rsrq5);
                ncell2rsrpMap5.put("sinr",sinr5);
                ncell2rsrpMap5.put("ssbIndex",ssbIndex5);
                ncell2rsrpMapList.add(ncell2rsrpMap5);
            }
            //???????????????6?????????pci???fcn
            Map<String,Object> ncell2rsrpMap6=new HashMap<>();
            Object pci6 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[6]"))).orElse(null);
            Object fcn6=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[6]"))).orElse(null);
            Object rsrp6=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[6]"))).orElse(null);
            Object rsrq6=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[6]"))).orElse(null);
            Object sinr6=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[6]"))).orElse(null);
            Object ssbIndex6=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[6]"))).orElse(null);
            if(pci6!=null&&fcn6!=null&&rsrp6!=null){
                ncell2rsrpMap6.put("key",pci6+"_"+fcn6);
                ncell2rsrpMap6.put("value",rsrp6);
                ncell2rsrpMap6.put("rsrq",rsrq6);
                ncell2rsrpMap6.put("sinr",sinr6);
                ncell2rsrpMap6.put("ssbIndex",ssbIndex6);
                ncell2rsrpMapList.add(ncell2rsrpMap6);
            }
            //???????????????7?????????pci???fcn
            Map<String,Object> ncell2rsrpMap7=new HashMap<>();
            Object pci7 = Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[7]"))).orElse(null);
            Object fcn7=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[7]"))).orElse(null);
            Object rsrp7=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRP[7]"))).orElse(null);
            Object rsrq7=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-RSRQ[7]"))).orElse(null);
            Object sinr7=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell SS-SINR[7]"))).orElse(null);
            Object ssbIndex7=Optional.ofNullable(item.get(InfluxReportUtils.getColumnName("NR Ncell ssb-index[7]"))).orElse(null);
            if(pci7!=null&&fcn7!=null&&rsrp7!=null){
                ncell2rsrpMap7.put("key",pci7+"_"+fcn7);
                ncell2rsrpMap7.put("value",rsrp7);
                ncell2rsrpMap7.put("rsrq",rsrq7);
                ncell2rsrpMap7.put("sinr",sinr7);
                ncell2rsrpMap7.put("ssbIndex",ssbIndex7);
                ncell2rsrpMapList.add(ncell2rsrpMap7);
            }
        });
        Map<String,Double> ncell2rsrpMap=new HashMap<>();
        Map<String,Double> ncell2rsrqMap=new HashMap<>();
        Map<String,Double> ncell2sinrMap=new HashMap<>();
        Map<String,String> ncell2ssIndexMap=new HashMap<>();

        ncell2rsrpMapList.stream().collect(Collectors.groupingBy(m->m.get("key"))).forEach((i, list)->{
            OptionalDouble rsrpOp = list.stream().mapToDouble(n -> Double.parseDouble(n.get("value").toString())).average();
            OptionalDouble rsrqOp = list.stream().filter(n->n.get("rsrq")!=null).mapToDouble(n -> Double.parseDouble(n.get("rsrq").toString())).average();
            OptionalDouble sinrOp = list.stream().filter(n->n.get("sinr")!=null).mapToDouble(n -> Double.parseDouble(n.get("sinr").toString())).average();
            Map<String,Long> ssIndex2countMap = list.stream().filter(n -> n.get("ssbIndex") != null).collect(Collectors.groupingBy(n -> n.get("ssbIndex").toString(), Collectors.counting()));
            List<Map.Entry<String, Long>> ssIndex2countMapSorted = ssIndex2countMap.entrySet().stream().sorted(Comparator.comparingLong(entry -> -entry.getValue())).collect(Collectors.toList());
            ncell2rsrpMap.put(i.toString(),Double.parseDouble(df.format(rsrpOp.getAsDouble())));
            ncell2rsrqMap.put(i.toString(),Double.parseDouble(df.format(rsrqOp.getAsDouble())));
            ncell2sinrMap.put(i.toString(),Double.parseDouble(df.format(sinrOp.getAsDouble())));
            ncell2ssIndexMap.put(i.toString(),ssIndex2countMapSorted.get(0).getKey());
        });
        List<Map.Entry<String, Double>> ncellPciFcn2avgRsrpListSorted = ncell2rsrpMap.entrySet().stream().sorted(Comparator.comparingDouble((entry) -> -entry.getValue())).collect(Collectors.toList());
       if(Objects.nonNull(ncellPciFcn2avgRsrpListSorted)&&ncellPciFcn2avgRsrpListSorted.size()>0){
           //TOP1????????????????????????
           Map.Entry<String, Double> ncellFirst = ncellPciFcn2avgRsrpListSorted.get(0);
           //TOP1???????????????????????? ?????????????????????
           if(pciFcn2BeanMap.get(ncellFirst.getKey())!=null){
               setCellMsg(rm,lat,longg,pciFcn2BeanMap.get(ncellFirst.getKey()),new String[]{topnServsCols[0],topnServsCols[1],topnServsCols[2]});
           }
           //TOP1????????????????????????PCI
           rm.put(topnServsCols[3],ncellFirst.getKey().split("_")[0]);
           //TOP1????????????????????????arfcn
           rm.put(topnServsCols[4],ncellFirst.getKey().split("_")[1]);
           //TOP1????????????????????????SSB?????????
           rm.put(topnServsCols[5],ncell2ssIndexMap.get(ncellFirst.getKey()));
           //TOP1????????????????????????RSRP
           rm.put(topnServsCols[6],ncellFirst.getValue());
           //TOP1????????????????????????RSRQ
           rm.put(topnServsCols[7],ncell2rsrqMap.get(ncellFirst.getKey()));
           //????????????SINR
           rm.put(topnServsCols[8],ncell2sinrMap.get(ncellFirst.getKey()));
       }

        //TOP1????????????????????????
       if(ncellPciFcn2avgRsrpListSorted!=null&&ncellPciFcn2avgRsrpListSorted.size()>1){
           final Map.Entry<String, Double> ncellSec = ncellPciFcn2avgRsrpListSorted.get(1);
           if(pciFcn2BeanMap.get(ncellSec.getKey())!=null){
               setCellMsg(rm,lat,longg,pciFcn2BeanMap.get(ncellSec.getKey()),new String[]{topnServsCols[9],topnServsCols[10],topnServsCols[11]});
           }
           //TOP1????????????????????????PCI
           rm.put(topnServsCols[12],ncellSec.getKey().split("_")[0]);
           //TOP1????????????????????????arfcn
           rm.put(topnServsCols[13],ncellSec.getKey().split("_")[1]);
           //TOP1????????????????????????SSB?????????
           rm.put(topnServsCols[14],ncell2ssIndexMap.get(ncellSec.getKey()));
           //TOP1????????????????????????RSRP
           rm.put(topnServsCols[15],ncellSec.getValue());
           //TOP1????????????????????????RSRQ
           rm.put(topnServsCols[16],ncell2rsrqMap.get(ncellSec.getKey()));
           //TOP1????????????????????????SINR
           rm.put(topnServsCols[17],ncell2sinrMap.get(ncellSec.getKey()));
       }
    }


    /**
     * ????????????????????????????????????
     * @param result
     * @param testLogItem
     * @return
     */
    private List<Map<String,Object>> fillColumns(List<Map<String, Object>> result, TestLogItem testLogItem) {
        List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(testLogItem.getCity());
        List<LteCell> lteCells = gisAndListShowServie.getLteCellsByRegion(testLogItem.getCity());
        Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
        Map<String, List<LteCell>> ltePciFcn2BeanMap = lteCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
        result.forEach(record->{
            record.put("logName",testLogItem.getFileName());
            record.put("busiType",testLogItem.getTestName());
            record.put("operator", testLogItem.getOperatorName());
            record.put("time", DateComputeUtils.formatMicroTime(record.get("time").toString()));
            String netType=String.valueOf(record.get("Netmode"));
            String pci=String.valueOf(record.get("Pci"));
            String fcn=String.valueOf(record.get("Enfarcn"));
            Double lat=Double.parseDouble(String.valueOf(record.get("Lat")));
            Double longg=Double.parseDouble(String.valueOf(record.get("Long")));
            List<Cell5G> nrPlanParamPojos = nrPciFcn2BeanMap.get(pci + "_" + fcn);
            List<LteCell> ltePlanParamPojos = ltePciFcn2BeanMap.get(pci + "_" + fcn);
            //lte
            if(netType.equalsIgnoreCase("6")&&ltePlanParamPojos!=null){
                LteCell cell = InfluxReportUtils.getCell(longg, lat, ltePlanParamPojos);
                record.put("cellName",cell==null?null:cell.getCellName());
                record.put("SellID",cell==null?null:cell.geteNBId());
            }
            //nr
            if(netType.equalsIgnoreCase("8")&&nrPlanParamPojos!=null){
                Cell5G cell =  InfluxReportUtils.getCell(longg, lat, nrPlanParamPojos);
                record.put("cellName",cell==null?null:cell.getCellName());
                record.put("SellID",cell==null?null:cell.getgNBId());
            }
        });
        return result;
    }

    /**
     * ????????????????????????????????????
     * @param fileName
     * @param result
     * @param id2LogBeanMap
     * @return
     */
    private List<Map<String,Object>> fillColumns(String fileName, List<Map<String, Object>> result, Map<Long, TestLogItem> id2LogBeanMap) {
        TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(fileName));
        List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(testLogItem.getCity());
        Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
        result.stream().parallel().forEach(item->{
            String pci=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR PCI"))).replace(".0","");
            String fcn=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SSB ARFCN"))).replace(".0","");
            String pci0=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[0]"))).replace(".0","");
            String fcn0=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[0]"))).replace(".0","");
            String pci1=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[1]"))).replace(".0","");
            String fcn1=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[1]"))).replace(".0","");
            String pci2=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[2]"))).replace(".0","");
            String fcn2=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[2]"))).replace(".0","");
            String pci3=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell PCI[3]"))).replace(".0","");
            String fcn3=String.valueOf(item.get(InfluxReportUtils.getColumnName("NR Ncell ARFCNSSB_DL[3]"))).replace(".0","");
            Object lat=item.get("Lat");
            Object longg=item.get("Long");
            Double dlat = null,dlon=null;
            if(lat!=null&&longg!=null){
                dlat=Double.parseDouble(lat.toString());
                dlon=Double.parseDouble(longg.toString());
            }
            List<Cell5G> planParamPojos = nrPciFcn2BeanMap.get(pci + "_" + fcn);
            List<Cell5G> planParamPojos0 = nrPciFcn2BeanMap.get(pci0 + "_" + fcn0);
            List<Cell5G> planParamPojos1 = nrPciFcn2BeanMap.get(pci1 + "_" + fcn1);
            List<Cell5G> planParamPojos2 = nrPciFcn2BeanMap.get(pci2 + "_" + fcn2);
            List<Cell5G> planParamPojos3 = nrPciFcn2BeanMap.get(pci3 + "_" + fcn3);
            item.put("logName",testLogItem.getFileName());
            item.put("busiType",testLogItem.getTestName());
            item.put("operator", testLogItem.getOperatorName());
            item.put("time", DateComputeUtils.formatMicroTime(item.get("time").toString()));
            item.put("lon", longg);
            item.put("lat", lat);
            item.put("altitude", item.get(InfluxReportUtils.getColumnName("altitude")));
            if(planParamPojos!=null){
                setCellMsg(item,dlat,dlon,planParamPojos,new String[]{"cellName","distance","angle"});
            }
            if(planParamPojos0!=null){
                setCellMsg(item,dlat,dlon,planParamPojos0,new String[]{"firstAdjCellName","firstAdjDistance","firstAdjAngle"});
            }
            if(planParamPojos1!=null){
                setCellMsg(item,dlat,dlon,planParamPojos1,new String[]{"secAdjCellName","secAdjDistance","secAdjAngle"});
            }
            if(planParamPojos2!=null){
                setCellMsg(item,dlat,dlon,planParamPojos2,new String[]{"thirdAdjCellName","thirdAdjDistance","thirdAdjAngle"});
            }
            if(planParamPojos3!=null){
                setCellMsg(item,dlat,dlon,planParamPojos3,new String[]{"fourAdjCellName","fourAdjDistance","fourAdjAngle"});
            }
        });
        return result;
    }

    /**
     * ????????????????????????
     * @param record
     * @param dlat
     * @param dlong
     * @param planParamPojos
     * @param strs
     */
    private void setCellMsg(Map<String,Object> record, Double dlat, Double dlong, List<Cell5G> planParamPojos, String[] strs){
        if(dlat==null||dlong==null){
            return;
        }
        AtomicReference<Double> minDis= new AtomicReference<>(3100.0);
        AtomicReference<Cell5G> planParamPojo=new AtomicReference<>();
        if(planParamPojos==null||planParamPojos.isEmpty()){
            return;
        }
        planParamPojos.forEach(item->{
            Double distance=GPSUtils.distance(dlat,dlong,item.getLatitude(),item.getLongitude());
            if(distance<3000){
                if(minDis.get()>distance){
                    minDis.set(distance);
                    planParamPojo.set(item);
                }
            }
        });
        Cell5G planParamPojo1 = planParamPojo.get();
        record.put(strs[0],planParamPojo1 ==null?null:planParamPojo1.getCellName());
        record.put(strs[1],minDis ==null?null:minDis.get());
        record.put(strs[2],planParamPojo1 ==null?null:AdjPlaneArithmetic.calculateAngle(dlong,(double)planParamPojo1.getLongitude(),dlat,(double)planParamPojo1.getLatitude(),(double)planParamPojo1.getAzimuth()));
    }

    /**
     * ????????????????????????
     * @param record
     * @param dlat
     * @param dlong
     * @param planParamPojos
     */
    private boolean setNrCellID(Map<String,Object> record, Double dlat, Double dlong, List<Cell5G> planParamPojos){
        if(dlat==null||dlong==null){
            return false;
        }
        AtomicReference<Double> minDis= new AtomicReference<>(3100.0);
        AtomicReference<Cell5G> planParamPojo=new AtomicReference<>();
        if(planParamPojos==null||planParamPojos.isEmpty()){
            return false;
        }
        planParamPojos.forEach(item->{
            Double distance=GPSUtils.distance(dlat,dlong,item.getLatitude(),item.getLongitude());
            if(distance<3000){
                if(minDis.get()>distance){
                    minDis.set(distance);
                    planParamPojo.set(item);
                }
            }
        });
        Cell5G planParamPojo1 = planParamPojo.get();
        if(planParamPojo1!=null){
            record.put("swdestcellId",planParamPojo1.getCellId());
            record.put("swdestgnbId",planParamPojo1.getgNBId());
            return true;
        }else{
            record.put("swdestcellId",null);
            record.put("swdestgnbId",null);
        }
        return false;
    }

    /**
     * ????????????????????????
     * @param record
     * @param dlat
     * @param dlong
     * @param planParamPojos
     */
    private boolean setLteCellID(Map<String,Object> record, Double dlat, Double dlong, List<LteCell> planParamPojos){
        if(dlat==null||dlong==null){
            return false;
        }
        AtomicReference<Double> minDis= new AtomicReference<>(3100.0);
        AtomicReference<LteCell> planParamPojo=new AtomicReference<>();
        if(planParamPojos==null||planParamPojos.isEmpty()){
            return false;
        }
        planParamPojos.forEach(item->{
            Double distance=GPSUtils.distance(dlat,dlong,item.getLatitude(),item.getLongitude());
            if(distance<3000){
                if(minDis.get()>distance){
                    minDis.set(distance);
                    planParamPojo.set(item);
                }
            }
        });
        LteCell planParamPojo1 = planParamPojo.get();
        if(planParamPojo1!=null){
            record.put("swdestcellId",planParamPojo1.getCellId());
            record.put("swdestgnbId",planParamPojo1.geteNBId());
            return true;
        }else{
            record.put("swdestcellId",null);
            record.put("swdestgnbId",null);
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getAbEvtAnaList(List<String> fileLogIds) {
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Assert.notEmpty(testLogItems);
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        fileLogIds.parallelStream().forEach(file-> {
            String id=file.trim();
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file.trim()));
            List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(testLogItem.getCity());
            List<LteCell> lteCells = gisAndListShowServie.getLteCellsByRegion(testLogItem.getCity());
            Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
            Map<String, List<LteCell>> ltePciFcn2BeanMap = lteCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
            Map<String,AbEventConfig> evtCnNameMap=new HashMap<>();
            InitialConfig.abEventConfigs.stream().forEach(item->{
                Arrays.stream(item.getTriggerEvt()).forEach(evt->{
                    if(StringUtils.isNotBlank(evt)){
                        evtCnNameMap.put(evt, item);
                    }
                });
            });

            List<String> sqlFreg=new ArrayList<>();
            StringBuilder sb=new StringBuilder();
            sb.append("SELECT evtName,Lat,Long,Height,Netmode,Pci,Enfarcn,SellID,Rsrp,Sinr,MsgID,UEID,TimeStamp from {0} where ");
            Set<String> evts = Arrays.asList(startEvts).stream().collect(Collectors.toSet());
            Set<String> allevts = Arrays.asList(swfailureEvts).stream().collect(Collectors.toSet());
            allevts.addAll(evtCnNameMap.keySet());
            allevts.add("LTE TAU Failure");
            allevts.addAll(evts);
            allevts.forEach(item->{
                sqlFreg.add("evtName =''"+item+"''");
            });
            sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
            String execSql=MessageFormat.format(sb.toString(),InfluxReportUtils.getTableName(file,"EVT"));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(execSql);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            result.forEach(obj->{
                String evtName = obj.get("evtName").toString();
                if(evtCnNameMap.keySet().contains(evtName)){
                    AbEventConfig abEventConfig = evtCnNameMap.get(evtName);
                    String[] preCons = abEventConfig.getPreCon();
                    String[] suffCons = abEventConfig.getSuffCon();
                    String[] sigs = abEventConfig.getTriggerSig();
                    Set<String> preEvts = Arrays.stream(preCons).collect(Collectors.toSet());
                    Set<String> suffEvts = Arrays.stream(suffCons).collect(Collectors.toSet());
                    List<Map<String, Object>> interDatas = DateComputeUtils.getPreDatasToEndEvt(result, obj.get("time").toString(), abEventConfig.getStartEvt()[0]);
                    List<Map<String, Object>> afterDatas = DateComputeUtils.getAfterDatas(result, obj.get("time").toString());
                    Map<Integer,Boolean> conditionMap=new HashedMap();
                    boolean count1;
                    boolean count2;
                    boolean count3=false;
                    if(preCons !=null&& preCons.length>0){
                        count1 = interDatas.stream().filter(i -> preEvts.contains(i.get("evtName").toString())).findAny().isPresent();
                        conditionMap.put(0,count1);
                    }
                    if(suffCons !=null&& suffCons.length>0){
                        count2 = afterDatas.stream().filter(i -> suffEvts.contains(i.get("evtName").toString())).findAny().isPresent();
                        conditionMap.put(1,count2);
                    }
                    if(sigs!=null&&sigs.length>0){
                        List<Map<String, Object>> sigDatas = getRecordByMsgIDFromSign(influxDbConnection,obj, file);
                        for (String sig : sigs) {
                            String[] sigConfis = sig.split(";");
                            count3=sigDatas.stream().filter(i->sigConfis[0].equals(i.get("Dir").toString())&&sigConfis[1].equals(i.get("signalName").toString())).findAny().isPresent();
                            if(count3){
                                break;
                            }
                        }
                        conditionMap.put(2,count3);
                    }
                    Boolean reduce;
                    if(conditionMap.size()>0){
                        reduce = conditionMap.entrySet().stream().map(item -> item.getValue()).reduce((x1, x2) -> x1 && x2).get();
                    }else{
                        reduce=true;
                    }
                    if(reduce){
                        Map<String, Object> rm=new HashMap<>();
                        rm.put("logName",testLogItem.getFileName());
                        rm.put("testName",testLogItem.getTestName());
                        rm.put("network",testLogItem.getNetworkStandard());
                        rm.put("busiType", abEventConfig.getType());
                        rm.put("evtName", abEventConfig.getEvtCnName());
                        rm.put("time",DateComputeUtils.formatMicroTime(obj.get("time").toString()));
                        rm.put("lon",obj.get("Lat"));
                        rm.put("lat",obj.get("Long"));
                        rm.put("causeBy",obj.get("evtName"));
                        rm.put("network01",InfluxReportUtils.getNetWork(obj.get("Netmode")));
                        rm.put("cellId",obj.get("SellID"));
                        rm.put("gnbId",getGnbID(testLogItem.getOperatorName(),InfluxReportUtils.getNetWork(obj.get("Netmode")),obj.get("SellID")));
                        rm.put("pci",obj.get("Pci"));
                        rm.put("fcn",obj.get("Enfarcn"));
                        rm.put("destTac",null);
                        setEvtMsg(interDatas,rm,nrPciFcn2BeanMap,ltePciFcn2BeanMap);
                        if("lte".equalsIgnoreCase(InfluxReportUtils.getNetWork(obj.get("Netmode")))){
                            InfluxReportUtils.setAbevtKpi(influxDbConnection,rm,InitialConfig.abevtLteKpiMap,obj.get("time").toString(),id);
                        }
                        if("nr".equalsIgnoreCase(InfluxReportUtils.getNetWork(obj.get("Netmode")))){
                            InfluxReportUtils.setAbevtKpi(influxDbConnection,rm,InitialConfig.abevtNrKpiMap,obj.get("time").toString(),id);
                        }
                        results.add(rm);
                    }
                }
            });
        });
        return results;
    }

    @Override
    public List<Map<String, Object>> getAbEvtAnaGerView(List<String> fileLogIds) {
        List<Map<String, Object>> results=new ArrayList<>();
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        Map<String, AbEventConfig> evtCnNameMap = new HashMap<>();
        InitialConfig.abEventConfigs.stream().forEach(item -> {
            Arrays.stream(item.getTriggerEvt()).forEach(evt -> {
                if(StringUtils.isNotBlank(evt)){
                    evtCnNameMap.put(evt, item);
                }
            });
        });
        LongAdder la=new LongAdder();
        fileLogIds.parallelStream().forEach(file-> {
            String id=file.trim();
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(id));
            List<String> sqlFreg = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT evtName,Lat,Long from {0} where ");
            evtCnNameMap.keySet().forEach(item -> {
                sqlFreg.add("evtName =''" + item + "''");
            });
            sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
            String execSql=MessageFormat.format(sb.toString(),InfluxReportUtils.getTableName(file,"EVT"));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(execSql);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            result.forEach(map->{
                la.increment();
                AbEventConfig abEvtConfig = evtCnNameMap.get(map.get("evtName").toString());
                map.put("logName",testLogItem.getFileName());
                map.put("time",DateComputeUtils.formatMicroTime(map.get("time").toString()));
                map.put("type", abEvtConfig.getType());
                map.put("id",la.longValue());
                map.put("evtCnName", abEvtConfig.getEvtCnName());
            });
            results.addAll(result);
        });
        return results;
    }

    /**
     * ??????evt??????2s?????????
     * @param rm
     * @param endTime
     */
    static String[] swfailureEvts=new String[]{"LTE Intrafreq Handover Failure","LTE Interfreq Handover Failure","EUTRA Handover to NR Failure",
            "NR IntraFreq-Handover Failure","NR InterFreq-Handover Failure","Mobility From NR Failure"};
    static String[] startEvts=new String[]{"LTE Intrafreq Handover Start","LTE Interfreq Handover Start",
            "NR IntraFreq-Handover Start","NR InterFreq-Handover Start"};
    private void setEvtMsg(List<Map<String, Object>> datas, Map<String, Object> rm, Map<String, List<Cell5G>> nrPciFcn2BeanMap, Map<String, List<LteCell>> ltePciFcn2BeanMap) {
        if(datas!=null&&!datas.isEmpty()){
            Set<String> evtName1 = datas.stream().map(item -> item.get("evtName").toString()).collect(Collectors.toSet());
            List<Map<String, Object>> swFailsDatas = datas.stream().filter(item -> Arrays.asList(swfailureEvts).contains(item.get("evtName").toString())).collect(Collectors.toList());
            List<Map<String, Object>> startSwDatas = datas.stream().filter(item -> Arrays.asList(startEvts).contains(item.get("evtName").toString())).collect(Collectors.toList());
            if(evtName1.contains("LTE TAU Failure")){
                rm.put("exsistTauFail","???");
            }else{
                rm.put("exsistTauFail","???");
            }
            if(swFailsDatas!=null&&!swFailsDatas.isEmpty()){
                rm.put("exsistSwFail","???");
                Map<String, Object> map = startSwDatas.get(0);
                if(map.get("evtName").toString().contains("LTE")){
                    rm.put("swdestnetwork","4G");
                    fillLteColumns(rm,map,ltePciFcn2BeanMap);

                }else if(map.get("evtName").toString().contains("NR")){
                    rm.put("swdestnetwork","5G");
                    fillNrColumns(rm,map,nrPciFcn2BeanMap);
                }
            }else{
                rm.put("exsistSwFail","???");
                rm.put("swdestnetwork",null);
            }
        }else{
            rm.put("exsistTauFail","???");
            rm.put("exsistSwFail","???");
        }
    }

    private void fillNrColumns(Map<String, Object> em,Map<String, Object> record, Map<String, List<Cell5G>> nrPciFcn2BeanMap) {
        String pci=String.valueOf(record.get("Pci"));
        String fcn=String.valueOf(record.get("Enfarcn"));
        Double lat=Double.parseDouble(String.valueOf(record.get("Lat")));
        Double longg=Double.parseDouble(String.valueOf(record.get("Long")));
        List<Cell5G> nrPlanParamPojos = nrPciFcn2BeanMap.get(pci + "_" + fcn);
        boolean flag=setNrCellID(record,longg,lat,nrPlanParamPojos);
        if(!flag){
            em.put("swdestcellId","PCI:"+pci+"FCN"+fcn);
        }
    }

    private void fillLteColumns(Map<String, Object> em,Map<String, Object> record, Map<String, List<LteCell>> nrPciFcn2BeanMap) {
        String pci=String.valueOf(record.get("Pci"));
        String fcn=String.valueOf(record.get("Enfarcn"));
        Double lat=Double.parseDouble(String.valueOf(record.get("Lat")));
        Double longg=Double.parseDouble(String.valueOf(record.get("Long")));
        List<LteCell> nrPlanParamPojos = nrPciFcn2BeanMap.get(pci + "_" + fcn);
        boolean flag=setLteCellID(record,longg,lat,nrPlanParamPojos);
        if(!flag){
            em.put("swdestcellId","PCI:"+pci+","+"FCN"+fcn);
        }
    }



    private Integer getGnbID(String operator,String network,Object cellId){
        if(cellId==null||StringUtils.isBlank(cellId.toString())||StringUtils.isBlank(network)){
            return null;
        }
        long cellid=Long.parseLong(cellId.toString());
        if(network.equalsIgnoreCase("NR")){
            if(operator.equalsIgnoreCase("??????")){
                return new Double(cellid/Math.pow(2.0,36-cuccGlen)).intValue();
            }
            if(operator.equalsIgnoreCase("??????")){
                return new Double((cellid/Math.pow(2.0,36-cmccGlen))).intValue();
            }
            if(operator.equalsIgnoreCase("??????")){
                return new Double((cellid/Math.pow(2.0,36-ctccGlen))).intValue();
            }
            return new Double((cellid/Math.pow(2.0,36-cuccGlen))).intValue();
        }else{
            return new Long(cellid/256).intValue();
        }
    }
    @Override
    public Map<String, String> getAbevtKpiConfig(){
        return InitialConfig.abevtKpiMap2;
    }

    @Override
    public Map<String, List<Map<String, Object>>> getNetConfigReports(List<String> fileLogIds) {
        Map<String, List<Map<String, Object>>> mixReportsMap=Collections.synchronizedMap(new LinkedHashMap<>());
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        Map<String, List<NetTotalConfig>> lteSheetMap = InitialConfig.lteNetToatls.stream().collect(Collectors.groupingBy(config->config.getNetworkType()+"_"+config.getSheetName()));
        Map<String, List<NetTotalConfig>> nrSheetMap = InitialConfig.nrNetToatls.stream().collect(Collectors.groupingBy(config->config.getNetworkType()+"_"+config.getSheetName()));
        fileLogIds.parallelStream().forEach(file-> {
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file));
            InitialConfig.netConfigs.forEach(item->{
                List<Map<String, Object>> sheetDatas = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT evtName,Pci,Enfarcn,SellID from {0} where ");
                sb.append(getNetWhereCondition(item));
                String execSql=MessageFormat.format(sb.toString(),InfluxReportUtils.getTableName(file,"EVT"));
                InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
                QueryResult query=influxDbConnection.query(execSql);
                List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
                if(!result.isEmpty()){
                    //???????????????????????????map??????
                    Map<String, List<Map<String, Object>>> evtNameDatas = result.stream().collect(Collectors.groupingBy(i -> i.get("evtName").toString()));
                    //???????????????????????????
                    List<Map.Entry<String, List<Map<String, Object>>>> triggerEvtDatas = evtNameDatas.entrySet().stream().filter(entry ->
                            Arrays.asList(item.getTriggerEvt()).contains(entry.getKey())
                    ).collect(Collectors.toList());
                    if(item.getType().equalsIgnoreCase("lte")){
                        evtNetConfigBusiProcess(influxDbConnection,testLogItem, item, result, triggerEvtDatas,InitialConfig.netLteKpiMap,sheetDatas,file);
                    }
                    if(item.getType().equalsIgnoreCase("nr")){
                        evtNetConfigBusiProcess(influxDbConnection,testLogItem, item, result, triggerEvtDatas,InitialConfig.netNrKpiMap,sheetDatas,file);
                    }
                    if(mixReportsMap.containsKey(item.getDetailSheet())){
                        List<Map<String, Object>> maps = mixReportsMap.get(item.getDetailSheet());
                        maps.addAll(sheetDatas);
                        mixReportsMap.put(item.getDetailSheet(),maps);
                    }else{
                        mixReportsMap.put(item.getDetailSheet(),sheetDatas);
                    }
                }
            });
        });
        //??????4G????????????
        createTotalReport("4G",mixReportsMap, lteSheetMap);
        //??????5G????????????
        createTotalReport("5G",mixReportsMap,nrSheetMap);
        return mixReportsMap;
    }
    String[] esfbColumnIndexs=new String[]{"epsfb01","epsfb02","epsfb03","epsfb04","epsfb05","epsfb06","epsfb07","epsfb08","epsfb09","epsfb10","epsfb11","epsfb12","epsfb13","epsfb14","epsfb15","epsfb16","epsfb17","epsfb18","epsfb19","epsfb20","epsfb21","epsfb22","epsfb23","epsfb24","epsfb25","epsfb26","epsfb27","epsfb28","epsfb29","epsfb30","epsfb31","epsfb32","epsfb33","epsfb34"
    };
    String[] dropColumnIndexs=new String[]{"drop01","drop02","drop03","drop04","drop05","drop06","drop07","drop08","drop09","drop10","drop11","drop12","drop13","drop14"
    };
    String[] frColumnIndexs=new String[]{"fr01","fr02","fr03","fr04","fr05","fr06","fr07","fr08","fr09","fr10","fr11","fr12","fr13"
    };
    String[] activeRingEvt=new String[]{"Outgoing SIP Ringing","CSFB Alerting MO"};
    String[] positiveRingEvt=new String[]{"Incoming SIP Ringing","CSFB Alerting MO"};
    String[] activeConnEvt=new String[]{"Outgoing SIP Call Connect","CSFB Connect MO","Outgoing Call Connect"};
    String[] positiveConnRingEvt=new String[]{"Incoming SIP Call Connect","CSFB Connect MT","Incoming Call Connect"};
    String[] activeCallEndEvt=new String[]{"Outgoing SIP Call End","Outgoing SIP Call Drop","Outgoing Call End","Outgoing Call Drop","CSFB Call End MO","CSFB Drop MO"};
    String[] positiveCallEndEvt=new String[]{"Incoming SIP Call End","Incoming SIP Call Drop","Incoming Call End","Incoming Call Drop","CSFB Call End MT","CSFB Call Drop MT"};
    String[] activeCallFailEvt=new String[]{"Outgoing SIP Call Block","Outgoing Call Block"};
    String[] positiveCallFailEvt=new String[]{"Incoming SIP Call Block","Incoming Call Block"};
    String[] activeDropFailEvt=new String[]{"Outgoing SIP Call Drop","CSFB Drop MO","Outgoing Call Drop"};
    String[] positiveDropFailEvt=new String[]{"Incoming SIP Call Drop","CSFB Drop MT","Incoming Call Drop"};

    String[] esfFailCauses=new String[]{"Mobility From NR Failure","N2L_REDIRECT_FAIL","LTE TAU Failure"};
    @Override
    public List<Map<String, Object>> getVoiceBusiReports(List<String> fileLogIds) {
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Assert.notEmpty(testLogItems);
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        fileLogIds.parallelStream().forEach(file-> {
            String id=file.trim();
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file.trim()));
            List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(testLogItem.getCity());
            List<LteCell> lteCells = gisAndListShowServie.getLteCellsByRegion(testLogItem.getCity());
            List<VoiceBusiConfig> activeConfigs = InitialConfig.voiceBusiConfigs.stream().filter(item -> item.getBusiType().equalsIgnoreCase("??????")).collect(Collectors.toList());
            List<VoiceBusiConfig> positiveConfigs =InitialConfig.voiceBusiConfigs.stream().filter(item->item.getBusiType().equalsIgnoreCase("??????")).collect(Collectors.toList());
            List<String[]> activeColumnList=new ArrayList<>();
            activeColumnList.add(activeRingEvt);
            activeColumnList.add(activeConnEvt);
            activeColumnList.add(activeCallEndEvt);
            activeColumnList.add(activeCallFailEvt);
            activeColumnList.add(activeDropFailEvt);

            List<String[]> positiveColumnList=new ArrayList<>();
            positiveColumnList.add(positiveRingEvt);
            positiveColumnList.add(positiveConnRingEvt);
            positiveColumnList.add(positiveCallEndEvt);
            positiveColumnList.add(positiveCallFailEvt);
            positiveColumnList.add(positiveDropFailEvt);
            callAna(results, testLogItem, nrCells, lteCells,activeConfigs,  activeColumnList,"??????",id);
            callAna(results, testLogItem, nrCells, lteCells,positiveConfigs, positiveColumnList,"??????",id);
        });
        return results.parallelStream().sorted(Comparator.comparing((Map<String, Object> f) -> f.get("basic01").toString()).thenComparing((Map<String, Object> f) -> f.get("call02").toString())).collect(Collectors.toList());
    }

    private String getCellType(String busiType, String triggerEvt, String netmode, boolean flag, String ringNet){
        if(busiType.equalsIgnoreCase("??????")){
            if("Outgoing SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&"nr".equalsIgnoreCase(ringNet)){
                return "VoNR";
            }else if("Outgoing SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"lte".equalsIgnoreCase(netmode)&&"lte".equalsIgnoreCase(ringNet)){
                return "VoLTE";
            }else if("Outgoing SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&flag){
                return "EPS FallBack";
            }else if("CSFB Start MO".equalsIgnoreCase(triggerEvt)){
                return "CSFB";
            }
            else if("Outgoing Call Attempt".equalsIgnoreCase(triggerEvt)){
                return "23G??????";
            }
        } else if(busiType.equalsIgnoreCase("??????")){
            if("Incoming SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&"nr".equalsIgnoreCase(ringNet)){
                return "VoNR";
            } else if("Incoming SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"lte".equalsIgnoreCase(netmode)&&"lte".equalsIgnoreCase(ringNet)){
                return "VoLTE";
            } else if("Incoming SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&flag){
                return "EPS FallBack";
            } else if("CSFB Start MT".equalsIgnoreCase(triggerEvt)){
                return "CSFB";
            }
            else if("Incoming Call Attempt".equalsIgnoreCase(triggerEvt)){
                return "23G??????";
            }
        }
        return null;
    }

    /**
     * ????????????
     * @param results
     * @param testLogItem
     * @param nrCells
     * @param lteCells
     * @param activeConfigs
     * @param activeColumnList
     * @param busiType
     * @param id
     */
    void callAna(List<Map<String, Object>> results, TestLogItem testLogItem, List<Cell5G> nrCells, List<LteCell> lteCells, List<VoiceBusiConfig> activeConfigs, List<String[]> activeColumnList, String busiType, String id) {
        Set<String> collect = activeConfigs.stream().map(VoiceBusiConfig::getTriggerEvt).collect(Collectors.toSet());
        Set<String> allevts = activeColumnList.stream().flatMap(strs -> Arrays.stream(strs)).collect(Collectors.toSet());
        allevts.addAll(collect);
        List<String> sqlFreg=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT UEID,MsgID,evtName,Lat,Long,Height,Netmode,Pci,Enfarcn,SellID,Rsrp,Sinr,extrainfo,TimeStamp from {0} where ");
        allevts.add("EPSFallBack Start");
        allevts.add("NR Event B1");
        allevts.add("NR Event B2");
        allevts.add("EPSFallBack Success");
        allevts.add("Fast_Return_Start");
        allevts.add("Fast_Return_Success");
        allevts.add("Fast_Return_Failure");
        allevts.addAll(Arrays.asList());
        allevts.forEach(i->{
            sqlFreg.add("evtName =''"+i+"''");
        });
        sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
        InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
        QueryResult query = influxDbConnection.query(MessageFormat.format(sb.toString(),InfluxReportUtils.getTableName(id,"EVT")));
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        List<List<Map<String, Object>>> datasBySplitEvt = DateComputeUtils.getDatasBySplitEvt(result, collect);

        for(List<Map<String, Object>> objs:datasBySplitEvt){
            Map<String, Object> rm = getCallDetailMap(testLogItem, objs,busiType,activeColumnList);
            Map<String, Object> epsFallBack_failure = getEvtRecord(objs, "EPSFallBack Failure");
            String time = epsFallBack_failure==null?null:epsFallBack_failure.get("time").toString();
            if(epsFallBack_failure!=null){
                rm.put(esfbColumnIndexs[13],InfluxReportUtils.getNetWork(epsFallBack_failure.get("Netmode")));
                List<Map<String, Object>> datas = DateComputeUtils.getPreDatasToEndEvt(objs, time, "EPSFallBack Start");
                List<String> evtRecords = getEvtValues(datas, esfFailCauses);
                rm.put(esfbColumnIndexs[14], evtRecords.get(0));
                rm.put(esfbColumnIndexs[15], evtRecords.stream().collect(Collectors.joining(",")));
                rm.put(esfbColumnIndexs[16],epsFallBack_failure.get("Long"));
                rm.put(esfbColumnIndexs[17],epsFallBack_failure.get("Lat"));
                setNrVoiceIEKpi(influxDbConnection,nrVoiceMap,testLogItem, rm, epsFallBack_failure,"lte",id);
                setLteVoiceIEKpi(influxDbConnection,lteVoiceMap,testLogItem, rm, epsFallBack_failure,"NR",id);
            }else{
                for(int i=13;i<34;i++){
                    rm.put(esfbColumnIndexs[i],null);
                }
            }
            //????????????
            dropBusi(influxDbConnection,nrCells, lteCells, objs, rm,id);
            //fr??????
            frFailBusi(influxDbConnection, objs, rm,id);
            results.add(rm);
        }
    }

    /**
     * ??????????????????
     * @param testLogItem
     * @param objs
     * @param busiType
     * @param columnList
     * @return
     */
    Map<String, Object> getCallDetailMap(TestLogItem testLogItem,List<Map<String, Object>> objs,String busiType,List<String[]> columnList) {
        Map<String, Object> fallBackRecord = getEvtRecord(objs,"EPSFallBack Start");
        Map<String, Object> rm = new HashMap<>();
        String ringTime = exsistEvtTime(objs, columnList.get(0));
        Double ringTimeStamp = exsistEvtTimestamp(objs, columnList.get(0));
        String attemptTime = DateComputeUtils.formatMicroTime(objs.get(0).get("time").toString());
        Double attemptTimestamp = objs.get(0).containsKey("TimeStamp")?Double.parseDouble(objs.get(0).get("TimeStamp").toString()):null;
        String ringNet=getRingNet(objs, columnList.get(0));
        String connTime = exsistEvtTime(objs, columnList.get(1));
        Double connTimeStamp = exsistEvtTimestamp(objs, columnList.get(1));
        String endTime = exsistEvtTime(objs, columnList.get(2));
        Double endTimeStamp = exsistEvtTimestamp(objs, columnList.get(2));
        //LOG??????
        rm.put("basic01", testLogItem.getFileName());
        //??????/??????
        rm.put("basic02",busiType);
        boolean flag=fallBackRecord==null?false:true;
        //????????????
        rm.put("call01",getCellType(busiType,objs.get(0).get("evtName").toString(),InfluxReportUtils.getNetWork(objs.get(0).get("Netmode")),flag,ringNet));
        //????????????
        rm.put("call02", attemptTime);
        //????????????
        rm.put("call03", ringTime);
        //????????????
        rm.put("call04", connTime);
        //????????????(s)
        rm.put("call05", DateComputeUtils.getDelay(attemptTimestamp, ringTimeStamp));
        //??????????????????
        rm.put("call06", endTime);
        //??????????????????(s)
        rm.put("call07", DateComputeUtils.getDelay(connTimeStamp, endTimeStamp));
        //??????????????????
        rm.put("call08", exsistEvtTime(objs, columnList.get(3)));
        //????????????
        rm.put("call09", exsistEvtTime(objs, columnList.get(4)));
        if(fallBackRecord!=null){
            String fbackWay = getFbackWay(fallBackRecord, objs);
            String time = DateComputeUtils.formatMicroTime(fallBackRecord.get("time").toString());
            //????????????
            rm.put(esfbColumnIndexs[0], fbackWay);
            if("??????".equalsIgnoreCase(fbackWay)){
                String time1=getBehindExsistEvtTime(fallBackRecord, objs,"HandoverTargetRAT","EPSFallBack Success");
                String time2=getBehindExsistEvtTime(fallBackRecord, objs,"HandoverTargetRAT","EPSFallBack Failure");
                //??????????????????
                rm.put(esfbColumnIndexs[1], time);
                //??????????????????
                rm.put(esfbColumnIndexs[2],time1);
                //??????????????????
                rm.put(esfbColumnIndexs[3],time2);
                if(null!=time&&null!=time1){
                    //??????????????????
                    rm.put(esfbColumnIndexs[4],DateComputeUtils.getDelay(time, time1));
                }else{
                    rm.put(esfbColumnIndexs[4],null);
                }
            }else if(null!=fbackWay&&!"??????".equalsIgnoreCase(fbackWay)){
                String time1=getBehindExsistEvtTime(fallBackRecord, objs,"RedirectedTargetRAT","EPSFallBack Success");
                String time2=getBehindExsistEvtTime(fallBackRecord, objs,"RedirectedTargetRAT","EPSFallBack Failure");
                //?????????????????????
                rm.put(esfbColumnIndexs[5], time);
                //?????????????????????
                rm.put(esfbColumnIndexs[6],time1);
                //?????????????????????
                rm.put(esfbColumnIndexs[7],time2);
                if(null!=time1&&null!=time){
                    rm.put(esfbColumnIndexs[8],DateComputeUtils.getDelay(time, time1));
                }else{
                    rm.put(esfbColumnIndexs[8],null);
                }
            }else{
                rm.put(esfbColumnIndexs[5],null);
                rm.put(esfbColumnIndexs[6],null);
                rm.put(esfbColumnIndexs[7],null);
                rm.put(esfbColumnIndexs[8],null);
            }
            //??????5G??????	??????	?????????Fast_Return_Start???
            String fastrstime = exsistEvtTime(objs, new String[]{"Fast_Return_Start"});
            rm.put(esfbColumnIndexs[9], fastrstime);
            //??????5G??????	??????	?????????Fast_Return_Success???
            String fastrsucctime = exsistEvtTime(objs, new String[]{"Fast_Return_Success"});
            rm.put(esfbColumnIndexs[10], fastrsucctime);
            //??????5G??????	??????	?????????Fast_Return_Failure???
            String fastrfailtime = exsistEvtTime(objs, new String[]{"Fast_Return_Failure"});
            rm.put(esfbColumnIndexs[11], fastrfailtime);
            //??????5G??????	??????	?????????5G???????????????-?????????5G???????????????
            if(null!=fastrstime&&null!=fastrsucctime){
                rm.put(esfbColumnIndexs[12],DateComputeUtils.getDelay(fastrstime, fastrsucctime));
            }else{
                rm.put(esfbColumnIndexs[12],null);
            }
        }else {
            for(int i=0;i<13;i++){
                rm.put(esfbColumnIndexs[i],null);
            }
        }
        return rm;
    }

    /**
     * fr ????????????
     * @param influxDbConnection
     * @param objs
     * @param rm
     */
    private void frFailBusi(InfluxDbConnection influxDbConnection, List<Map<String, Object>> objs, Map<String, Object> rm, String id) {
        Map<String, Object> frs=null;
        String time1=null;
        String time2=null;
        for(Map<String, Object> obj:objs){
            //?????????????????????Fast_Return_Start???
            if("Fast_Return_Start".equalsIgnoreCase(obj.get("evtName").toString())){
                frs=obj;
            }
            //?????????????????????Fast_Return_Failure???
            if("Fast_Return_Failure".equalsIgnoreCase(obj.get("evtName").toString())){
                rm.put(frColumnIndexs[3],"???");
                List<Map<String, Object>> result1 = getRecordByMsgIDFromSign(influxDbConnection, obj,id);
                if(result1!=null&&!result1.isEmpty()){
                    rm.put(frColumnIndexs[4],result1.get(0).get("signalName"));
                }
                time1=obj.get("time").toString();
                //nr
                Map<String, String> abevtKpiMap=new HashMap<>();
                abevtKpiMap.put(frColumnIndexs[5],"avg(50055)");
                abevtKpiMap.put(frColumnIndexs[6],"avg(50056)");
                InfluxReportUtils.setNetIEKpi(influxDbConnection,rm,abevtKpiMap,time1,id);
                rm.put(frColumnIndexs[10],null);
                rm.put(frColumnIndexs[11],null);
                break;
            }else{
                rm.put(frColumnIndexs[3],"???");
            }
            if(frs!=null){
                time2=frs.get("time").toString();
                String longg=frs.get("Long").toString();
                String lat=frs.get("Lat").toString();
                Map<String, String> abevtKpiMap=new HashMap<>();
                abevtKpiMap.put(frColumnIndexs[0],"???");
                abevtKpiMap.put(frColumnIndexs[1],longg);
                abevtKpiMap.put(frColumnIndexs[2],lat);
                rm.putAll(abevtKpiMap);
            }
        }
        if(time1!=null&&time2!=null){
            rm.putAll(exsistByContentFromSign(influxDbConnection,time2,time1,id));
        }
    }
    /**
     * ????????????
     * @param influxDbConnection
     * @param nrCells
     * @param lteCells
     * @param objs
     * @param rm
     */
    void dropBusi(InfluxDbConnection influxDbConnection, List<Cell5G> nrCells, List<LteCell> lteCells, List<Map<String, Object>> objs, Map<String, Object> rm, String id) {
        List<String> dropFailEvts=new ArrayList<>();
        dropFailEvts.addAll(Arrays.asList(activeDropFailEvt));
        dropFailEvts.addAll(Arrays.asList(positiveDropFailEvt));
        Map<String, Object> tauFailObj=null;
        for(Map<String, Object> obj:objs){
            if("LTE TAU Failure".equalsIgnoreCase(obj.get("evtName").toString())){
                tauFailObj=obj;
            }
            //??????????????????????????????????????????????????????
            if(dropFailEvts.contains(obj.get("evtName"))){
                String pci=String.valueOf(obj.get("Pci"));
                String fcn=String.valueOf(obj.get("Enfarcn"));
                String time1=String.valueOf(obj.get("time"));
                Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item1 -> item1.getPci() + "_" + item1.getFrequency1()));
                Map<String, List<LteCell>> ltePciFcn2BeanMap = lteCells.stream().collect(Collectors.groupingBy(item1 -> item1.getPci() + "_" + item1.getFrequency1()));
                List<Cell5G> nrPlanParamPojos = nrPciFcn2BeanMap.get(pci + "_" + fcn);
                List<LteCell> ltePlanParamPojos = ltePciFcn2BeanMap.get(pci + "_" + fcn);
                List<Map<String, Object>> result1 = getRecordByMsgIDFromSign(influxDbConnection, obj,id);
                if(result1!=null&&!result1.isEmpty()){
                    rm.put(dropColumnIndexs[0],result1.get(0).get("signalName"));
                }
                String netType=obj.get("Netmode").toString();
                Double longg=Double.parseDouble(obj.get("Long").toString());
                Double lat=Double.parseDouble(obj.get("Lat").toString());
                rm.put(dropColumnIndexs[1],obj.get("Long"));
                rm.put(dropColumnIndexs[2],obj.get("Lat"));
                //lte
                if(netType.equalsIgnoreCase("6")&&ltePlanParamPojos!=null){
                    LteCell cell = InfluxReportUtils.getCell(longg, lat, ltePlanParamPojos);
                    rm.put(dropColumnIndexs[3],cell==null?null:cell.getCellName());
                    rm.put(dropColumnIndexs[4],cell==null?null:cell.getCellId());
                    rm.put(dropColumnIndexs[5],cell==null?null:cell.geteNBId());
                    Map<String, String> abevtKpiMap=new HashMap<>();
                    abevtKpiMap.put(dropColumnIndexs[8],"avg(40028)");
                    abevtKpiMap.put(dropColumnIndexs[9],"avg(40035)");
                    InfluxReportUtils.setNetIEKpi(influxDbConnection,rm,abevtKpiMap,time1,id);
                }
                //nr
                if(netType.equalsIgnoreCase("8")&&nrPlanParamPojos!=null){
                    Cell5G cell = InfluxReportUtils.getCell(longg, lat, nrPlanParamPojos);
                    rm.put(dropColumnIndexs[3],cell==null?null:cell.getCellName());
                    rm.put(dropColumnIndexs[4],cell==null?null:cell.getCellId());
                    rm.put(dropColumnIndexs[5],cell==null?null:cell.getgNBId());
                    Map<String, String> abevtKpiMap=new HashMap<>();
                    abevtKpiMap.put(dropColumnIndexs[8],"avg(50055)");
                    abevtKpiMap.put(dropColumnIndexs[9],"avg(50056)");
                    InfluxReportUtils.setNetIEKpi(influxDbConnection,rm,abevtKpiMap,time1,id);
                }
                rm.put(dropColumnIndexs[6],pci);
                rm.put(dropColumnIndexs[7],fcn);
                rm.put(dropColumnIndexs[10],null);
                rm.put(dropColumnIndexs[11],null);
                break;
            }
        }
        if(tauFailObj!=null){
            Map<String, String> abevtKpiMap=new HashMap<>();
            abevtKpiMap.put(dropColumnIndexs[12],"???");
            abevtKpiMap.put(dropColumnIndexs[13],"LAST(40009)");
            InfluxReportUtils.setVoiceIEKpi(influxDbConnection,abevtKpiMap, tauFailObj.get("time").toString(),id);
        }

    }

    /**
     * ??????msgid???????????????????????????
     *
     * @param influxDbConnection
     * @param obj
     * @param id
     * @return
     */
    List<Map<String, Object>> getRecordByMsgIDFromSign(InfluxDbConnection influxDbConnection, Map<String, Object> obj, String id) {
        int msgID=Integer.parseInt(obj.get("MsgID").toString());
        String ueID=obj.get("UEID").toString();
        BigDecimal timeStamp=new BigDecimal(obj.get("TimeStamp").toString());
        String sigSql="select MsgID,Dir,signalName from "+InfluxReportUtils.getTableName(id,"SIG")+" where UEID=''{0}'' and TimeStamp={1}";
        String formatSql = MessageFormat.format(sigSql, ueID,timeStamp.toPlainString());
        QueryResult query1 = influxDbConnection.query(formatSql);
        List<Map<String, Object>> list = InfludbUtil.paraseQueryResult(query1);
        return list.stream().filter(item->Integer.parseInt(item.get("MsgID").toString())<msgID).sorted(Comparator.comparingInt((Map<String, Object> item)->Integer.parseInt(item.get("MsgID").toString())).reversed()).collect(Collectors.toList());
    }

    /**
     * ???????????????????????????????????????
     *
     * @param influxDbConnection
     * @param start
     * @param end
     * @return
     */
    private Map<String,String> exsistByContentFromSign(InfluxDbConnection influxDbConnection, String start, String end, String id) {
        Map<String,String> conMap=new HashMap<>();
        conMap.put(frColumnIndexs[8],"mib");
        conMap.put(frColumnIndexs[9],"systemInformationBlockType1");
        conMap.put(frColumnIndexs[10],"Registration request");
        conMap.put(frColumnIndexs[11],"Registration accept/Registration complete");
        conMap.put(frColumnIndexs[12],"8:AUTHENTICATION FAILURE");
        conMap.put(frColumnIndexs[13],"Registration Reject");
        String sigSql="select signalName,Netmode from "+InfluxReportUtils.getTableName(id,"SIG")+" where time>=''{0}'' and time<=''{1}'' and {2}";
        List<String> sqlFreg=new ArrayList<>();
        conMap.forEach((key,value)->{
            if(value.contains(":")){
                String[] s=value.split(":",-1);
                String s1="Netmode ='"+s[0]+"'";
                if(s[1].contains("/")){
                    String[] s2=s[1].split("/");
                    for(String m:s2){
                        s1+="and signalName=~/"+m+"/";
                    }

                }
                sqlFreg.add(s1);
            }else{
                if(value.contains("/")){
                    String[] s2=value.split("/");
                    for(String m:s2){
                        sqlFreg.add("signalName=~/"+m+"/");
                    }

                }else{
                    sqlFreg.add("signalName=~/"+value+"/");
                }
            }
            String formatSql = MessageFormat.format(sigSql, start,end,"("+sqlFreg.stream().collect(Collectors.joining(" or "))+")");
            QueryResult query1 = influxDbConnection.query(formatSql);
            List<Map<String, Object>> maps = InfludbUtil.paraseQueryResult(query1);
            if(maps!=null&&!maps.isEmpty()){
                conMap.put(key,"???");
            }else{
                conMap.put(key,"???");
            }
        });
        return conMap;
    }


    @Override
    public List<Map<String, Object>> getReportCellKpi(List<String> fileLogIds) {
        String sql="select  LAST(IEValue_53601) as fcn,LAST(IEValue_50007) as pci,LAST(IEValue_40028) as LTE_PCC_RSRP from {0} GROUP by time(1s) fill(none)";
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> results1=new ArrayList<>();
        fileLogIds.parallelStream().forEach(id->{
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(id.trim()));
            String execSql=MessageFormat.format(sql,InfluxReportUtils.getTableName(id,"IE"));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            QueryResult query=influxDbConnection.query(execSql);
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            result.forEach(item->{
                item.put("operator",id2LogBeanMap.get(Long.parseLong(id)).getOperatorName());
                item.put("nettype",item.get("LTE_PCC_RSRP")!=null?"NSA":"SA");
            });
            results.addAll(result);
        });
        List<String> collect = results.stream().map(item -> item.get("fcn") + "_" + item.get("pci") + "_" + item.get("nettype")+"_"+item.get("operator")).distinct().collect(Collectors.toList());
        collect.forEach(item->{
            Map<String, Object> i=new HashMap<>();
            String[] ss=item.split("_",-1);
            i.put("fcn",ss[0]);
            i.put("pci",ss[1]);
            i.put("nettype",ss[2]);
            i.put("operator",ss[3]);
            results1.add(i);
        });
        return results1;
    }



    static Map<String,String> nrVoiceMap=new LinkedHashMap<>();
    static Map<String,String> lteVoiceMap=new LinkedHashMap<>();
    static {
        nrVoiceMap.put("epsfb19","LAST(50006)");
        nrVoiceMap.put("epsfb20","LAST(57100)");
        nrVoiceMap.put("epsfb21","LAST(50007)");
        nrVoiceMap.put("epsfb22","LAST(53601)");
        nrVoiceMap.put("epsfb23","LAST(50055)");
        nrVoiceMap.put("epsfb24","LAST(50056)");
        nrVoiceMap.put("epsfb25","LAST(50097)");
        nrVoiceMap.put("epsfb26","LAST(74214)");
        lteVoiceMap.put("epsfb27","LAST(40007)");
        lteVoiceMap.put("epsfb28","LAST(43900)");
        lteVoiceMap.put("epsfb29","LAST(40008)");
        lteVoiceMap.put("epsfb30","LAST(40010)");
        lteVoiceMap.put("epsfb31","LAST(40028)");
        lteVoiceMap.put("epsfb32","LAST(40035)");
        lteVoiceMap.put("epsfb33","LAST(40056)");
        lteVoiceMap.put("epsfb34","LAST(43368)");
    }
    private void setNrVoiceIEKpi(InfluxDbConnection influxDbConnection, Map<String, String> columnMap, TestLogItem testLogItem, Map<String, Object> rm, Map<String, Object> epsFallBack_failure, String destnetwork, String id) {
        Map<String, Object> map = InfluxReportUtils.setVoiceIEKpi(influxDbConnection, columnMap, epsFallBack_failure.get("time").toString(),id);
        if(rm.get(esfbColumnIndexs[13]).toString().equalsIgnoreCase(destnetwork)){
            rm.putAll(map);
        }else{
            rm.put(esfbColumnIndexs[18], epsFallBack_failure.get("SellID"));
            rm.put(esfbColumnIndexs[19],getGnbID(testLogItem.getOperatorName(),"NR",epsFallBack_failure.get("SellID")));
            rm.put(esfbColumnIndexs[20],epsFallBack_failure.get("Pci"));
            rm.put(esfbColumnIndexs[21],epsFallBack_failure.get("Enfarcn"));
            rm.put(esfbColumnIndexs[22],epsFallBack_failure.get("Rsrp"));
            rm.put(esfbColumnIndexs[23],epsFallBack_failure.get("Sinr"));
            rm.put(esfbColumnIndexs[24],map.get(esfbColumnIndexs[24]));
            rm.put(esfbColumnIndexs[25],map.get(esfbColumnIndexs[25]));
        }
    }

    private void setLteVoiceIEKpi(InfluxDbConnection influxDbConnection, Map<String, String> columnMap, TestLogItem testLogItem, Map<String, Object> rm, Map<String, Object> epsFallBack_failure, String destnetwork, String id) {
        Map<String, Object> map = InfluxReportUtils.setVoiceIEKpi(influxDbConnection, columnMap, epsFallBack_failure.get("time").toString(),id);
        if(rm.get(esfbColumnIndexs[13]).toString().equalsIgnoreCase(destnetwork)){
            rm.putAll(map);
        }else{
            rm.put(esfbColumnIndexs[26], epsFallBack_failure.get("SellID"));
            rm.put(esfbColumnIndexs[27],getGnbID(testLogItem.getOperatorName(),"NR",epsFallBack_failure.get("SellID")));
            rm.put(esfbColumnIndexs[28],epsFallBack_failure.get("Pci"));
            rm.put(esfbColumnIndexs[29],epsFallBack_failure.get("Enfarcn"));
            rm.put(esfbColumnIndexs[30],epsFallBack_failure.get("Rsrp"));
            rm.put(esfbColumnIndexs[31],epsFallBack_failure.get("Sinr"));
            rm.put(esfbColumnIndexs[32],map.get(esfbColumnIndexs[32]));
            rm.put(esfbColumnIndexs[33],map.get(esfbColumnIndexs[33]));
        }
    }


    String[] fallbackEvts=new String[]{"NR Event B1","NR Event B2"};
    /**
     * ??????epsfallback ????????????
     *
     * @return
     */
    private String getFbackWay( Map<String, Object> epsFallBackRecord, List<Map<String, Object>> result){
        String extraInfo = epsFallBackRecord==null?"":epsFallBackRecord.containsKey("extrainfo")?epsFallBackRecord.get("extrainfo").toString():"";
        if(extraInfo.contains("HandoverTargetRAT")){
            return "??????";
        }else if(extraInfo.contains("RedirectedTargetRAT")){
            List<Map<String, Object>> timePre2sDatas = DateComputeUtils.getPre2sDatas(result, epsFallBackRecord.get("time").toString());
            boolean flag=timePre2sDatas.stream().filter(item->Arrays.asList(fallbackEvts).contains(item.get("evtName").toString()))
                    .findAny().isPresent();
            if(flag){
                return "?????????????????????";
            }else{
                return "????????????";
            }
        }
        return null;
    }

    private String getBehindExsistEvtTime( Map<String, Object> epsFallBackRecord, List<Map<String, Object>> result,String containValue,String evt){
        String extraInfo = epsFallBackRecord==null?"":epsFallBackRecord.containsKey("extrainfo")?epsFallBackRecord.get("extrainfo").toString():"";
        if(extraInfo.contains(containValue)){
            List<Map<String, Object>> timeAfterDatas = DateComputeUtils.getAfterDatas(result, epsFallBackRecord.get("time").toString());
            Optional<Map<String, Object>> optinal = timeAfterDatas.stream().filter(item -> item.get("evtName").toString().equalsIgnoreCase(evt))
                    .findAny();
            if(optinal.isPresent()){
                return DateComputeUtils.formatMicroTime(optinal.get().get("time").toString());
            }
            return null;
        }
        return null;
    }

    private String null2EmptyStr(Object o){
        if(o==null){
            return "";
        }
        return o.toString();
    }


    /**
     * ??????evts ?????????????????????
     * @param rs
     * @param evts
     * @return
     */
    private String exsistEvtTime(List<Map<String, Object>> rs, String[] evts){
        for(Map<String, Object> r:rs){
            if(Arrays.asList(evts).contains(r.get("evtName").toString())){
                return DateComputeUtils.formatMicroTime(r.get("time").toString());
            }
        }
        return null;
    }

    private Double exsistEvtTimestamp(List<Map<String, Object>> rs, String[] evts){
        for(Map<String, Object> r:rs){
            if(Arrays.asList(evts).contains(r.get("evtName").toString())){
                return r.containsKey("TimeStamp")?Double.parseDouble(r.get("TimeStamp").toString()):null;
            }
        }
        return null;
    }

    private String getRingNet(List<Map<String, Object>> rs, String[] evts){
        for(Map<String, Object> r:rs){
            if(Arrays.asList(evts).contains(r.get("evtName").toString())){
                String netmode = r.get("Netmode").toString();
                if(netmode.equalsIgnoreCase("6")){
                    return "LTE";
                }else if(netmode.equalsIgnoreCase("8")){
                    return "NR";
                }
            }
        }
        return null;
    }


    private Map<String,Object> getEvtRecord(List<Map<String, Object>> rs,String evt){
        for(Map<String, Object> item:rs){
            if(item.get("evtName").toString().equalsIgnoreCase(evt)){
                return item;
            }
        }
        return null;
    }

    private List<String> getEvtValues(List<Map<String, Object>> rs,String[] evts){
        List<String> evtNames=new ArrayList<>();
        for(Map<String, Object> item:rs){
            if(Arrays.asList(evts).contains(item.get("evtName").toString())){
                evtNames.add(item.get("evtName").toString());
            }
        }
        return evtNames;
    }

    /**
     * ??????????????????
     * @param mixReportsMap
     * @param lteVoiceSheetList
     */
    private void createTotalReport(String sheetName,Map<String, List<Map<String, Object>>> mixReportsMap, Map<String, List<NetTotalConfig>> lteVoiceSheetList){
        Set<String> keys=new HashSet<>();
        //??????????????????
        List<Map<String,Object>> voicePartSheet=new ArrayList<>();
        List<Map<String,Object>> dataPartSheet=new ArrayList<>();
        lteVoiceSheetList.forEach((s,list)->{
            String[] splits = s.split("_");
            final List<Map<String, Object>> partFofSheet=new ArrayList<>();
            Arrays.asList(splits[2].split("/")).forEach(sheet->{
                if(mixReportsMap.containsKey(sheet)){
                    List<Map<String, Object>> maps = mixReportsMap.get(sheet);
                    partFofSheet.addAll(maps);
                    keys.addAll(maps.stream().collect(Collectors.groupingBy(item->item.get("prov").toString()+"_"+item.get("city").toString()+"_"+item.get("pci").toString()+"_"+item.get("fcn").toString()
                            +"_"+item.get("cellId").toString()+"_"+item.get("builder").toString()+"_"+item.get("contractor").toString())).keySet());
                }
            });
            Map<String, List<Map<String, Object>>> collect = partFofSheet.stream().collect(Collectors.groupingBy(item -> item.get("prov") + "_" + item.get("city") + "_" +
                    item.get("pci") + "_" + item.get("fcn") + "_" + item.get("cellId") + "_" + item.get("builder")
                    + "_" + item.get("contractor")));
            //????????????????????????????????????????????????
            if("voice".equalsIgnoreCase(splits[1])){
                Predicate<Map<String,Object>> p=item->item.get("logName").toString().contains("EPSFallBack?????????")&&item.get("time")!=null;
                Map<String,Object> voiceCellFirstRecordMap=creatPartOfSheetData(list, collect,p);
                voicePartSheet.add(voiceCellFirstRecordMap);
            }
            //??????????????????????????????????????????????????????
            if("data".equalsIgnoreCase(splits[1])){
                Predicate<Map<String,Object>> p1=item->!item.get("logName").toString().contains("EPSFallBack?????????")&&item.get("time")!=null;
                Map<String,Object> dataCellFirstRecordMap=creatPartOfSheetData(list, collect,p1);
                dataPartSheet.add(dataCellFirstRecordMap);
            }
        });
        List<Map<String,Object>> rel=new ArrayList<>();
        keys.stream().forEach(cellKey->{
            Map<String,Object> r=new HashMap<>();
            voicePartSheet.forEach(item->{
                if(item!=null&&item.containsKey(cellKey))
                r.putAll((Map<String,Object>)item.get(cellKey));
            });
            dataPartSheet.forEach(item->{
                if(item!=null&&item.containsKey(cellKey))
                    r.putAll((Map<String,Object>)item.get(cellKey));
            });
            if(r.size()>0&&sheetName.equalsIgnoreCase("4G")){
                if(StringUtils.isNotBlank(r.get("kpi1").toString())&&StringUtils.isNotBlank(r.get("kpi2").toString())&&StringUtils.isNotBlank(r.get("kpi3").toString())){
                    r.put("exsistTestRediectDetail","???");
                }else{
                    r.put("exsistTestRediectDetail","???");
                }
            }
            r.put("key",cellKey);
            rel.add(r);
        });
        mixReportsMap.put(sheetName,rel);
    }

    private Map<String, Object> creatPartOfSheetData(List<NetTotalConfig> list, Map<String, List<Map<String, Object>>> collect,Predicate<Map<String,Object>> p) {
        Map<String,Object> dataCellFirstRecordMap=new HashMap<>();
        collect.forEach((k,value)->{
            List<Map<String, Object>> voiceOrderList = value.stream().filter(p).sorted(Comparator.comparing(item -> item.get("time").toString(), new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return DateComputeUtils.toUtcDate(o1).before(DateComputeUtils.toUtcDate(o2)) ? 1 : -1;
                }
            })).collect(Collectors.toList());
            if(voiceOrderList!=null&&!voiceOrderList.isEmpty()){
                Map<String, Object> firstR = voiceOrderList.get(0);
                Map<String,Object> obj=new HashMap<>();
                //?????????????????????????????????????????? ???????????????
                list.forEach(item->{
                    obj.put(item.getKpiIndex(),firstR.get(item.getSrcIndex()));
                });
                dataCellFirstRecordMap.put(k,obj);
            }
        });
        return dataCellFirstRecordMap;
    }

    private void evtNetConfigBusiProcess(InfluxDbConnection influxDbConnection, TestLogItem testLogItem, AbEventConfig item, List<Map<String, Object>> result, List<Map.Entry<String, List<Map<String, Object>>>> triggerEvtDatas, Map<String, String> netKpiMap, List<Map<String, Object>> sheetDatas, String id) {
        triggerEvtDatas.forEach(entry->{
            List<Map<String, Object>> value = entry.getValue();
            value.forEach(record->{
                Map<String, Object> r=new HashMap<>();
                String triggerTime = record.get("time").toString();
                Object cellId = Optional.ofNullable(record.get("SellID")).orElse("");
                Object fcn = Optional.ofNullable(record.get("Enfarcn")).orElse("");
                Object pci = Optional.ofNullable(record.get("Pci")).orElse("");
                //???????????????????????????
                String[] preCons = item.getPreCon();
                boolean flag;
                if(preCons.length>0){
                    List<Map<String, Object>> pre2sDatas = DateComputeUtils.getPre2sDatas(result, triggerTime);
                    //???????????????????????????
                    if(preCons[0].contains("???")){
                        if(pre2sDatas!=null&&!pre2sDatas.isEmpty()) {
                            //?????????????????????????????????????????????
                            flag = pre2sDatas.stream().allMatch(a -> !Arrays.asList(preCons).contains(a.get("evtName")));
                        }else{
                            //??????????????????????????????
                            flag=true;
                        }
                    }else{
                        if(pre2sDatas!=null&&!pre2sDatas.isEmpty()) {
                            flag = pre2sDatas.stream().anyMatch(a -> Arrays.asList(preCons).contains(a.get("evtName")));
                        }else{
                            flag=false;
                        }
                    }
                    //??????????????????????????????
                    if(flag){
                        addNetConfigItem(influxDbConnection,testLogItem, item, result, netKpiMap, sheetDatas, id, r, triggerTime, cellId, fcn, pci);
                    }
                }else{
                    //???????????????
                    addNetConfigItem(influxDbConnection,testLogItem, item, result, netKpiMap, sheetDatas, id, r, triggerTime, cellId, fcn, pci);
                }
            });
        });
    }

    private void addNetConfigItem(InfluxDbConnection influxDbConnection, TestLogItem testLogItem, AbEventConfig item, List<Map<String, Object>> result, Map<String, String> netKpiMap, List<Map<String, Object>> sheetDatas, String id, Map<String, Object> r, String triggerTime, Object cellId, Object fcn, Object pci) {
        if(item.getType().equalsIgnoreCase("lte")){
            r.put("enbID",getGnbID(testLogItem.getOperatorName(),"lte",cellId));
        }else if(item.getType().equalsIgnoreCase("nr")){
            r.put("gnbID",getGnbID(testLogItem.getOperatorName(),"nr",cellId));
        }
        r.put("prov",testLogItem.getProv());
        r.put("city",testLogItem.getCity());
        r.put("pci",pci);
        r.put("fcn",fcn);
        r.put("cellId",cellId);
        r.put("builder",testLogItem.getBuilder());
        r.put("contractor",testLogItem.getContractor());
        r.put("logName",testLogItem.getFileName());
        r.put("triggerTime",DateComputeUtils.formatMicroTime(triggerTime));
        setBebindValue(r,item,result,triggerTime);
        InfluxReportUtils.setNetIEKpi(influxDbConnection,r,netKpiMap,triggerTime,id);
        sheetDatas.add(r);
    }

    private void setBebindValue(Map<String, Object> record,AbEventConfig item,List<Map<String, Object>> result,String triggerTime){
        String[] preCons = item.getPreCon();
        //??????????????????????????????
        List<Map<String, Object>> afterDatas = DateComputeUtils.getAfterDatas(result, triggerTime);
        if(afterDatas!=null&&!afterDatas.isEmpty()){
            //?????????????????????????????????????????????
            String[] suffCon = item.getSuffCon();
            boolean flag1 = afterDatas.stream().anyMatch(a -> Arrays.asList(suffCon).contains(a.get("evtName")));
            int m=Integer.MAX_VALUE,n=0;
            Map<String,Object> r=null;
            if(flag1){
                for(int i=0;i<afterDatas.size();i++){
                    if(Arrays.asList(preCons).contains(afterDatas.get(i).get("evtName"))){
                        m=i;
                    }
                    if(Arrays.asList(suffCon).contains(afterDatas.get(i).get("evtName"))){
                        n=i;
                        r=afterDatas.get(i);
                        break;
                    }
                }
                //????????????????????? ????????????????????????????????????????????????
                if(n<m){
                    record.put("endTime",DateComputeUtils.formatMicroTime(r.get("time").toString()));
                }else{
                    record.put("endTime",null);
                }
            }else{
                record.put("endTime",null);
            }
        }
    }


    private String getNetWhereCondition(AbEventConfig config){
        StringBuilder sb=new StringBuilder();
        List<String> sqlFreg = new ArrayList<>();
        //??????????????????
        for(String s:config.getTriggerEvt()){
            sqlFreg.add("evtName =''" + s.trim() + "''");
        }
        //????????????
        for(String s:config.getPreCon()){
            s=s.replace("???","");
            sqlFreg.add("evtName =''" + s.trim() + "''");
        }
        //????????????
        for(String s:config.getSuffCon()){
            sqlFreg.add("evtName =''" + s.trim() + "''");
        }
        return sqlFreg.stream().collect(Collectors.joining(" or "));
    }





}
