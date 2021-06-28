package com.datang.service.influx.impl;

import com.datang.common.influxdb.InfludbUtil;
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
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service(value = "influxService")
public class InfluxServiceImpl implements InfluxService {

    @Value("${influxdb.url}")
    private String url;
    @Value("${influxdb.password}")
    private String password;
    @Value("${influxdb.username}")
    private String username;
    @Value("${influxdb.timeout}")
    private long timeout;
    @Value("${influxdb.radius}")
    private long radius;

    @Value("${nr.cmcc.length}")
    private int cmccGlen;
    @Value("${nr.ctcc.length}")
    private int ctccGlen;
    @Value("${nr.cucc.length}")
    private int cuccGlen;

    private static String MAPTRAILSQL="SELECT last({0}) as LTE_PCC_RSRP, last({1}) as LTE_PCC_SINR, last({2}) as NR_SS_RSRP, last({3}) as NR_SS_SINR,last(Lat) as Lat,last(Long) as Long FROM IE  where Lat!=''91.0'' and Long!=''181.0'' GROUP BY time(1s) fill(none)";
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

    private static String lineChartSql="SELECT IEValue_40028 as RSRP, IEValue_40035 as SINR, IEValue_50229 as SS_RSRP, IEValue_70525 as SS_SINR ,IEValue_43724 as \"LTE PDCP Thrput DL(Mbps)\",IEValue_43725 as \"LTE PDCP Thrput UL(Mbps)\",IEValue_50962 as \"NR PHY Thrput UL(Mbps)\",IEValue_51213 as \"NR PHY Thrput DL(Mbps)\" FROM IE";

    @Override
    public List<Map<String, Object>> lineChartDatas(long logId, String startTime, String endTime) {
        return commonQueryDatas(lineChartSql,logId,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> sigleDatas(long logId, String startTime, String endTime) {
        return commonQueryDatas(sigSql,logId,startTime,endTime);
    }

    private static final String evtSql="SELECT evtName,Netmode,extrainfo from EVT";
    private static final String sigSql="select Netmode,signalName,Dir,DetailMsg from SIG";
    @Override
    public List<Map<String, Object>> evtDatas(long logId, String startTime, String endTime) {
        return commonQueryDatas(evtSql,logId,startTime,endTime);
    }
    private static final String synSql="select last(IEValue_40001) as ie_40001,last(IEValue_40002) as ie_40002,last(IEValue_40006) as ie_40006,last(IEValue_40007) as ie_40007,last(IEValue_40008) as ie_40008,last(IEValue_40009) as ie_40009,last(IEValue_40010) as ie_40010,last(IEValue_40014) as ie_40014,last(IEValue_40015) as ie_40015,last(IEValue_40016) as ie_40016,last(IEValue_40017) as ie_40017,last(IEValue_40019) as ie_40019,last(IEValue_40028) as ie_40028,last(IEValue_40031) as ie_40031,last(IEValue_40034) as ie_40034,last(IEValue_40035) as ie_40035,last(IEValue_40075) as ie_40075,last(IEValue_40139) as ie_40139,last(IEValue_40171) as ie_40171,last(IEValue_40203) as ie_40203,last(IEValue_40235) as ie_40235,last(IEValue_40299) as ie_40299,last(IEValue_44206) as ie_44206,last(IEValue_50001) as ie_50001,last(IEValue_50002) as ie_50002,last(IEValue_50003) as ie_50003,last(IEValue_50006) as ie_50006,last(IEValue_50007) as ie_50007,last(IEValue_50013) as ie_50013,last(IEValue_50015) as ie_50015,last(IEValue_50016) as ie_50016,last(IEValue_50017) as ie_50017,last(IEValue_50021) as ie_50021,last(IEValue_50046) as ie_50046,last(IEValue_50051) as ie_50051,last(IEValue_50055) as ie_50055,last(IEValue_50056) as ie_50056,last(IEValue_50057) as ie_50057,last(IEValue_50101) as ie_50101,last(IEValue_50165) as ie_50165,last(IEValue_50229) as ie_50229,last(IEValue_50293) as ie_50293,last(IEValue_53601) as ie_53601,last(IEValue_58000) as ie_58000,last(IEValue_58032) as ie_58032,last(IEValue_70005) as ie_70005,last(IEValue_70070) as ie_70070,last(IEValue_70525) as ie_70525 from IE";
    @Override
    public List<Map<String, Object>> syncIEWindow(Long logId, String time) {
        Date date = DateComputeUtils.formatDate(time);
        Date startDate = DateUtils.truncate(date, Calendar.SECOND);
        return commonQueryDatas(synSql,logId,startDate,time);
    }

    private static final String evtPointSql="SELECT evtName from EVT WHERE ";
    @Override
    public List<Map<String, Object>> evtPointTimes(Long logId,String[] evts) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        StringBuilder sb=new StringBuilder(evtPointSql);
        Set<String> cols=new HashSet<>();
        for(String evt:evts){
            cols.add("evtName='"+evt+"'");
        };
        sb.append(cols.stream().collect(Collectors.joining(" or ")));
        InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
        connect.setDatabase("Task_"+logId);
        QueryResult query=connect.query(new Query(sb.toString(), "Task_"+logId));
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        connect.close();
        return result;
    }


    private List<Map<String, Object>> commonQueryDatas(String sql,long logId, Object startTime, Object endTime) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
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

        InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
        connect.setDatabase("Task_"+logId);
        QueryResult query=connect.query(new Query(sb.toString(), "Task_"+logId));
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        result.forEach(item->{
            item.put("time", DateComputeUtils.formatMicroTime(item.get("time").toString()));
        });
        connect.close();
        return result;
    }
    @Override
    public List<Map<String, Object>> queryRoadSampDatas(String sql,long logId,  List<Map<String,String>> timeLists,String[] wheres) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<String> timeWheres=new ArrayList<>();
        StringBuilder sbSql=new StringBuilder(sql);
        timeLists.forEach(item->{
            StringBuilder sb=new StringBuilder("(");
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
                sbSql.append(MessageFormat.format(" and {0}!="+ -1+"",where));
            }
        }
        InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
        connect.setDatabase("Task_"+logId);
        QueryResult query=connect.query(new Query(sbSql.toString(), "Task_"+logId));
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        connect.close();
        return result;
    }

    @Override
    public List<Map<String, Object>> getMapTrailByLogFiles(List<String> fileLogIds) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<String> columns=new ArrayList<>();
        Arrays.asList(refStrs).forEach(item->{
            columns.add(InfluxReportUtils.getColumnName(item));
        });
        String sql=MessageFormat.format(MAPTRAILSQL,columns.toArray());
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        fileLogIds.parallelStream().forEach(id->{
            Map<String,Object> resultMap=new HashMap<>();
            InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
            connect.setDatabase("Task_"+id);
            QueryResult query=connect.query(new Query(sql, "Task_"+id));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            resultMap.put("logName",id2LogBeanMap.get(Long.parseLong(id)).getFileName());
            resultMap.put("gpsData",result);
            results.add(resultMap);
            connect.close();
        });
        return results;
    }

    @Autowired
    private UnicomLogItemService unicomLogItemService;
    @Autowired
    private GisAndListShowServie gisAndListShowServie;
    @Override
    public List<Map<String, Object>> getSampPointByLogFiles(List<String> ids) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<Map<String, Object>> results=new ArrayList<>();
        List<String> sqlFreg=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        sb.append("select last(Height) as Height,");
        InitialConfig.map2.forEach((key,value)->{
            if(InitialConfig.map.containsKey(key)){
                System.out.println(InfluxReportUtils.getColumnName(key)+","+value);
                sqlFreg.add("last("+InfluxReportUtils.getColumnName(key)+") as "+InfluxReportUtils.getColumnName(key));
            }
        });
        sqlFreg.add("last(Lat) as Lat");
        sqlFreg.add("last(Long) as Long");
        String collect = sqlFreg.stream().collect(Collectors.joining(","));
        sb.append(collect).append(" from IE GROUP BY time(1s) fill(none)");
        String sql=sb.toString();

        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(ids.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        ids.parallelStream().forEach(id->{
            InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
            connect.setDatabase("Task_"+id);
            QueryResult query=connect.query(new Query(sql, "Task_"+id));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            connect.close();
            results.addAll(fillColumns(id,result,id2LogBeanMap));

        });
        return results;
    }

    @Override
    public List<Map<String, Object>> getEventByLogFiles(List<String> fileLogIds) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<Map<String, Object>> results=new ArrayList<>();
        List<String> sqlFreg=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT evtName,Lat,Long,Height,Netmode,Pci,Enfarcn,SellID,Rsrp,Sinr from EVT where ");
        InitialConfig.map3.forEach((key,value)->{
            String[] evtEnNames=value.split("、");
            List<String> evtNameList = Arrays.stream(evtEnNames).filter(item -> StringUtils.isNotBlank(item)).distinct().collect(Collectors.toList());
            for (String evtEnName : evtNameList) {
                sqlFreg.add("evtName ='"+evtEnName+"'");
            }
        });
        sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
        String sql=sb.toString();
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        fileLogIds.parallelStream().forEach(file->{
            InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
            connect.setDatabase("Task_"+file);
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file));
            QueryResult query=connect.query(new Query(sql, "Task_"+file));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            connect.close();
            results.addAll(fillColumns(result,testLogItem));
        });
        return results;
    }


    private List<Map<String, Object>> getEventKpisByLogFiles(String file) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        StringBuilder sb=new StringBuilder();
        Collection<String> map = InitialConfig.kpiMap.values();
        Set<String> cols=new HashSet<>();
        map.forEach(item->{
            for (String s:InfluxReportUtils.getEnStr(item)) {
                cols.add("evtName='"+s+"'");
            }
        });
        //改造去除group by
        String sql="SELECT Lat,X,Y,evtName from EVT where {0}";
        sb.append(cols.stream().collect(Collectors.joining(" or ")));
        String execSql=MessageFormat.format(sql,sb.toString());
        InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
        connect.setDatabase("Task_"+file);
        QueryResult query=connect.query(new Query(execSql, "Task_"+file));
        List<Map<String, Object>> temp = InfludbUtil.paraseQueryResult(query);
        connect.close();
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

    //top1 服务小区列设值
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
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<Map<String, Object>> results=new ArrayList<>();
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
        String collect = sqlFreg.stream().collect(Collectors.joining(","));
        sb.append(collect).append(" from IE where X!=-1 AND Y!=-1");

        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Assert.notEmpty(testLogItems);
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<Map<String,Object>> opratorMaps=Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> eventKpis=Collections.synchronizedList(new ArrayList<>());
        fileLogIds.parallelStream().forEach(file->{
            TestLogItem testLogItem= id2LogBeanMap.get(Long.parseLong(file));
            sb.append(" AND time>='"+ DateComputeUtils.localToUTC(testLogItem.getStartDate())+"' AND time<='"+DateComputeUtils.localToUTC(testLogItem.getEndDate())+"'");
            String sql=sb.toString();
            Map<String,Object> opratorMap=new HashMap<>();
            InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
            connect.setDatabase("Task_"+file);

            QueryResult query=connect.query(new Query(sql, "Task_"+file));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            opratorMap.put("operator",testLogItem.getOperatorName());
            opratorMap.put("city",testLogItem.getCity());
            opratorMap.put("list",result);
            List<Map<String, Object>> eventKpisByLogFiles = getEventKpisByLogFiles(file);
            opratorMap.put("evtList",eventKpisByLogFiles);
            eventKpis.addAll(eventKpisByLogFiles);
            opratorMaps.add(opratorMap);
            connect.close();
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
            //处理采样点数据合并
            list.forEach(item->{
                List<Map<String, Object>> l = (List<Map<String, Object>>) item.get("list");
                List<Map<String, Object>> e = (List<Map<String, Object>>) item.get("evtList");
                sampResult.addAll(l);
                evtKpiResult.addAll(e);
            });
            //处理事件数据合并
            Map<String, List<Map<String, Object>>> evtxyGroupby = evtKpiResult.stream().collect(Collectors.groupingBy(item -> item.get("X") + "_" + item.get("Y")));
            Map<String, Map<String, Object>> evtRecords = evtKpitranferCol(evtxyGroupby);

            //按栅格经纬度group by
            Map<String, List<Map<String, Object>>> xyGroupby = sampResult.stream().filter(item->!item.get("X").toString().equals("0")&&!item.get("Y").toString().equals("0")).collect(Collectors.groupingBy(item -> item.get("X") + "_" + item.get("Y")+"_"+item.get("Height")));
            List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(city);
            Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
            Map<String, Object> gridrm = new HashMap<>();
            xyGroupby.forEach((key, items) -> {
                Map<String, Object> rm = new HashMap<>();
                //对栅格指标进行赋值
                fillGridKpiColumns(rm,items,allColumns);
                //pci，频点group by
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
                    String heiht=key.split("_")[2];
                    fillServCellColumns(nrPciFcn2BeanMap, rm, pciFcnGroupby,firstRsrp,doubles[1], doubles[0], top1ServCellColumns);
                    fillNcellColumnsByServCell(nrPciFcn2BeanMap, rm, pciFcnGroupby, firstRsrp, doubles[1], doubles[0], top1ncellServsColumns);
                    rm.put("lon",doubles[0]);rm.put("lat",doubles[1]);rm.put("heiht",heiht);
                    rm.put("operator",operator);
                    if(evtRecords!=null&&evtRecords.containsKey(longg+"_"+lat)){
                        rm.putAll(evtRecords.get(longg+"_"+lat));
                    }
                }


                //TOP2服务小区设值
                if(pciFcn2avgRsrpListSorted.size()>1){
                    Map.Entry<String, Double> secondRsrp = pciFcn2avgRsrpListSorted.get(1);
                    fillServCellColumns(nrPciFcn2BeanMap, rm, pciFcnGroupby, secondRsrp,doubles[1], doubles[0], top2ServCellColumns);
                    fillNcellColumnsByServCell(nrPciFcn2BeanMap, rm, pciFcnGroupby, secondRsrp,doubles[1], doubles[0], top2ncellServsColumns);
                }

                rm.putAll(gridrm);
                results.add(rm);
            });
        });
        return results.stream().filter(item->item.get("lon")!=null&&item.get("lat")!=null).collect(Collectors.groupingBy(item -> item.get("lon") +"_"+ item.get("lat") +"_"+ item.get("height"))).entrySet().stream().sorted(Comparator.comparing(MapCompare::comparingByLon).reversed().thenComparing(MapCompare::comparingByLat).reversed()).collect(Collectors.toList());
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
     * 事件kpi转换
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
     * 对服务小区相关列赋值
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
        //TOP1服务小区SS-RSRP≥-105dBm占比
        Predicate<Map<String,Object>> predicate=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-105;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-RSRP",predicate));
        //TOP1服务小区SS-RSRP≥-110dBm占比
        Predicate<Map<String,Object>> predicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-110;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-RSRP",predicate1));
        l.add(top1sinrSampCount);
        l.add(sinrAvg);
        //TOP1服务小区SS-SINR≥0dB样点数
        Predicate<Map<String,Object>> predicate2=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=0.0;
        long top1servcellsinr0db = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-SINR",predicate2);
        l.add(top1servcellsinr0db);
        //TOP1服务小区SS-SINR≥-3dB样点数
        Predicate<Map<String,Object>> predicate3=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=-3;
        long top1servcellsinr3db = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(),pciFcnGroupby,"NR SS-SINR",predicate3);
        l.add(top1servcellsinr3db);
        //TOP1服务小区SS-RSRP≥-105dBm&SS-SINR≥-3dB占比
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,pciFcnGroupby.get(firstRsrp.getKey()).stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))!=null)&&item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))!=null&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))))>=-3&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))))>=-105).count());
        //TOP1服务小区SS AVG RSRQ(dB)
        l.add(rsrqAvg);
        //TOP1服务小区 小区信息列回填
        if(pciFcn2BeanMap.get(firstRsrp.getKey())!=null){
            setCellMsg(l,lat,longg,pciFcn2BeanMap.get(firstRsrp.getKey()));
        }else{
            l.add(null);
            l.add(null);
            l.add(null);
        }
        //TOP1小区NR RLC 下行平均吞吐率采样点总数
        long nrrlckpi1 = pciFcnGroupby.get(firstRsrp.getKey()).stream().filter(item -> item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")) != null).count();
        l.add(nrrlckpi1);
        //TOP1小区NR RLC 下行平均吞吐率(Mbps)
        String nrrlckpi2 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR RLC Thrput DL(Mbps)");
        l.add(nrrlckpi2);
        //TOP1小区NR RLC 上行平均吞吐率采样点总数
        long nrrlckpi3 = pciFcnGroupby.get(firstRsrp.getKey()).stream().filter(item -> item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")) != null).count();
        l.add(nrrlckpi3);
        //TOP1小区NR RLC 上行平均吞吐率(Mbps)
        String nrrlckpi4 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR RLC Thrput UL(Mbps)");
        l.add(nrrlckpi4);
        //TOP1小区NR RLC层下行低速率占比(≤100M)
        Predicate<Map<String,Object>> predicate20=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")).toString())<=100;
        long countFilterKpi = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput DL(Mbps)", predicate20);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi);
        //TOP1小区NR RLC层下行高速率占比(≥500M)
        Predicate<Map<String,Object>> predicate21=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")).toString())>=500;
        long countFilterKpi2 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput DL(Mbps)", predicate21);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi2);
        //TOP1小区NR RLC层下行高速率占比(≥800M)
        Predicate<Map<String,Object>> predicate22=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)")).toString())>=800;
        long countFilterKpi3 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput DL(Mbps)", predicate22);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi3);
        //TOP1小区NR RLC层上行低速率占比(≤10M)
        Predicate<Map<String,Object>> predicate23=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")).toString())<=10;
        long countFilterKpi4 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput UL(Mbps)", predicate23);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi4);
        //TOP1小区NR RLC层上行高速率占比(≥120M)
        Predicate<Map<String,Object>> predicate24=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")).toString())>=120;
        long countFilterKpi5 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput UL(Mbps)", predicate24);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi5);
        //TOP1小区NR RLC层上行高速率占比(≥160M)
        Predicate<Map<String,Object>> predicate25=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)")).toString())>=160;
        long countFilterKpi6 = InfluxReportUtils.getCountFilterKpi(firstRsrp.getKey(), pciFcnGroupby, "NR RLC Thrput UL(Mbps)", predicate25);
        InfluxReportUtils.addKpi(l,nrrlckpi1,countFilterKpi6);
        //TOP1小区NR_PDCCH DL Grant Count
        String nrrlckpi11 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL GrantNum");
        l.add(nrrlckpi11);
        //TOP1小区NR_DL Initial BLER(%)
        String nrrlckpi12 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL IBLER");

        l.add(nrrlckpi12);
        //TOP1小区NR_DL Residual BLER(%)
        String nrrlckpi13 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL RBLER");
        l.add(nrrlckpi13);
        //TOP1小区NR_DL Avg MCS
        String nrrlckpi14 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL MCS");
        l.add(nrrlckpi14);
        //TOP1小区NR_ DL Avg CQI
        String nrrlckpi15 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR AVG CQI");
        l.add(nrrlckpi15);
        //TOP1小区NR_DL Avg Rank
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
        //TOP1小区NR下行256QAM比例(%)
        InfluxReportUtils.addKpi(l, sum, x1);
        Double x2=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 64QAM Num"));
        //TOP1小区NR下行64QAM比例(%)
        InfluxReportUtils.addKpi(l, sum, x2);
        Double x3=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL 16QAM Num"));
        //TOP1小区NR下行16QAM比例(%)
        InfluxReportUtils.addKpi(l, sum, x3);
        Double x4=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW0 DL QPSK Num"),
                InfluxReportUtils.getSumKpi(firstRsrp.getKey(),pciFcnGroupby,"NR CW1 DL QPSK Num"));
        //TOP1小区NR下行QPSK比例(%)
        InfluxReportUtils.addKpi(l, sum, x4);
        //TOP1小区下行调用PRB平均数/slot
        String nrrlckpi21 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR DL PRBNum/slot");
        l.add(nrrlckpi21);
        //TOP1小区NR_UL Avg MCS
        String nrrlckpi22 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL MCS");
        l.add(nrrlckpi22);
        //TOP1小区NR_UL Initial BLER(%)
        String nrrlckpi23 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL IBLER");
        l.add(nrrlckpi23);
        //TOP1小区NR_UL Residual BLER(%)
        String nrrlckpi24 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL RBLER");
        l.add(nrrlckpi24);
        //TOP1小区NR_UL Avg Rank
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
        //TOP1小区NR上行256QAM比例(%)
        InfluxReportUtils.addKpi(l, sum2, x6);
        //TOP1小区NR上行 64QAM比例(%)
        InfluxReportUtils.addKpi(l, sum2, x7);
        //TOP1小区NR上行 16QAM比例(%)
        InfluxReportUtils.addKpi(l, sum2, x8);
        //TOP1小区NR上行 QPSK比例(%)
        InfluxReportUtils.addKpi(l, sum2, x9);
        //TOP1小区NR_Path Loss(dB)
        String nrrlckpi30 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR PUSCH Pathloss");
        l.add(nrrlckpi30);
        //TOP1小区上行调用PRB平均数/slot
        String nrrlckpi31 = InfluxReportUtils.getAvgKpi(firstRsrp.getKey(),pciFcnGroupby,"NR UL PRBNum/slot");
        l.add(nrrlckpi31);
        //TOP1小区PUSCH 平均TxPower（dBm）
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
     * 对栅格记录相关列赋值
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

        //小区SS-RSRP≥-105dBm占比
        Predicate<Map<String,Object>> predicate=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-105;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-RSRP",predicate));
        //小区SS-RSRP≥-110dBm占比
        Predicate<Map<String,Object>> predicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-RSRP")).toString())>=-110;
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-RSRP",predicate1));
        //SS-SINR≥0dB样点数
        Predicate<Map<String,Object>> predicate2=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=0.0;
        Long top1servcellsinr0db = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-SINR",predicate2);
        l.add(top1servcellsinr0db);
        //SS-SINR≥-3dB占比
        Predicate<Map<String,Object>> predicate3=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("NR SS-SINR")).toString())>=-3;
        Long top1servcellsinr3db = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"NR SS-SINR",predicate3);
        InfluxReportUtils.addKpi(l,top1sinrSampCount,top1servcellsinr3db);
        //SS-RSRP≥-105dBm&SS-SINR≥-3dB占比
        InfluxReportUtils.addKpi(l,top1rsrpSampCount,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))!=null)&&item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))!=null&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-SINR"))))>=-3&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR SS-RSRP"))))>=-105).count());

        //小区NR RLC 下行平均吞吐率采样点总数
        Long nrrlckpi1 = InfluxReportUtils.getCountKpi(pciFcnGroupby,"NR RLC Thrput DL(Mbps)");
        l.add(nrrlckpi1);
        //小区NR RLC 下行平均吞吐率(Mbps)
        String nrrlckpi2 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR RLC Thrput DL(Mbps)");
        l.add(nrrlckpi2);
        //小区NR RLC 上行平均吞吐率采样点总数
        Long nrrlckpi3 = InfluxReportUtils.getCountKpi(pciFcnGroupby,"NR RLC Thrput UL(Mbps)");
        l.add(nrrlckpi3);
        //小区NR RLC 上行平均吞吐率(Mbps)
        String nrrlckpi4 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR RLC Thrput UL(Mbps)");
        l.add(nrrlckpi4);
        //小区NR RLC层下行低速率占比(≤100M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))))<=100).count());
        //小区NR RLC层下行高速率占比(≥500M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))))>=500).count());
        //小区NR RLC层下行高速率占比(≥800M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput DL(Mbps)"))))>=800).count());
        //小区NR RLC层上行低速率占比(≤10M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))))<=10).count());
        //小区NR RLC层上行高速率占比(≥120M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))))>=120).count());
        //小区NR RLC层上行高速率占比(≥160M)
        InfluxReportUtils.addKpi(l,nrrlckpi1,pciFcnGroupby.stream()
                .filter(item->(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))!=null)&&Double.parseDouble(String.valueOf(item.get(InfluxReportUtils.getColumnName("NR RLC Thrput UL(Mbps)"))))>=160).count());
        //小区NR_PDCCH DL Grant Count
        String nrrlckpi11 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL GrantNum");
        l.add(nrrlckpi11);
        //小区NR_DL Initial BLER(%)
        String nrrlckpi12 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL IBLER");
        l.add(nrrlckpi12);
        //小区NR_DL Residual BLER(%)
        String nrrlckpi13 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL RBLER");
        l.add(nrrlckpi13);
        //小区NR_DL Avg MCS
        String nrrlckpi14 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL MCS");
        l.add(nrrlckpi14);
        //小区NR_ DL Avg CQI
        String nrrlckpi15 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR AVG CQI");
        l.add(nrrlckpi15);
        //小区NR_DL Avg Rank
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
        //小区NR下行256QAM比例(%)
        InfluxReportUtils.addKpi(l,sum,x1);
        Double x2=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 64QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 64QAM Num"));
        //小区NR下行64QAM比例(%)
        InfluxReportUtils.addKpi(l,sum,x2);
        Double x3=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL 16QAM Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL 16QAM Num"));
        //小区NR下行16QAM比例(%)
        InfluxReportUtils.addKpi(l,sum,x3);
        Double x4=InfluxReportUtils.add(InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW0 DL QPSK Num"),
                InfluxReportUtils.getSumKpi(pciFcnGroupby,"NR CW1 DL QPSK Num"));
        //小区NR下行QPSK比例(%)
        InfluxReportUtils.addKpi(l,sum,x4);
        //小区下行调用PRB平均数/slot
        String nrrlckpi21 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR DL PRBNum/slot");
        l.add(nrrlckpi21);
        //小区NR_UL Avg MCS
        String nrrlckpi22 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL MCS");
        l.add(nrrlckpi22);
        //小区NR_UL Initial BLER(%)
        String nrrlckpi23 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL IBLER");
        l.add(nrrlckpi23);
        //小区NR_UL Residual BLER(%)
        String nrrlckpi24 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL RBLER");
        l.add(nrrlckpi24);
        //小区NR_UL Avg Rank
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
        //小区NR上行256QAM比例(%)
        InfluxReportUtils.addKpi(l,sum2,x6);
        //小区NR上行 64QAM比例(%)
        InfluxReportUtils.addKpi(l,sum2,x7);
        //小区NR上行 16QAM比例(%)
        InfluxReportUtils.addKpi(l,sum2,x8);
        //小区NR上行 QPSK比例(%)
        InfluxReportUtils.addKpi(l,sum2,x9);
        //小区NR_Path Loss(dB)
        String nrrlckpi30 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR PUSCH Pathloss");
        l.add(nrrlckpi30);
        //小区上行调用PRB平均数/slot
        String nrrlckpi31 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR UL PRBNum/slot");
        l.add(nrrlckpi31);
        //小区PUSCH 平均TxPower（dBm
        String nrrlckpi32 = InfluxReportUtils.getAvgKpi(pciFcnGroupby,"NR PUSCH Txpower");
        l.add(nrrlckpi32);

        //LTE_RSRP总采样点数
        Long ltekpi33=InfluxReportUtils.getCountKpi(pciFcnGroupby,"LTE PCC_RSRP");
        l.add(ltekpi33);
        //LTE_SINR总采样点
        Long ltekpi34=InfluxReportUtils.getCountKpi(pciFcnGroupby,"LTE PCC_SINR");
        l.add(ltekpi34);
        //LTE_Serving  AVG RSRP(dBm)
        String ltekpi35=InfluxReportUtils.getAvgKpi(pciFcnGroupby,"LTE PCC_RSRP");
        l.add(ltekpi35);
        //LTE_Serving  AVG SINR(dB)
        String ltekpi36=InfluxReportUtils.getAvgKpi(pciFcnGroupby,"LTE PCC_SINR");
        l.add(ltekpi36);
        //LTE_RSRP≥-112dBm采样点占比
        Predicate<Map<String,Object>> ltepredicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("LTE PCC_RSRP")).toString())>=-112;
        InfluxReportUtils.addKpi(l,ltekpi33,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"LTE PCC_RSRP",ltepredicate1));
        //LTE_SINR≥-2dBm采样点占比
        Predicate<Map<String,Object>> ltepredicate2=stringObjectMap->Double.parseDouble(stringObjectMap.get(InfluxReportUtils.getColumnName("LTE PCC_SINR")).toString())>=-2;
        InfluxReportUtils.addKpi(l,ltekpi34,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,"LTE PCC_SINR",ltepredicate2));
        //LTE无线覆盖率(RSRP≥-112dBm&SINR≥-2dB)
        Predicate<Map<String,Object>> ltepredicate3=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("LTE PCC_RSRP")).toString())>=-112;
        Predicate<Map<String,Object>> ltepredicate4=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("LTE PCC_SINR")).toString())>=-2;
        List<Predicate<Map<String,Object>>> lp=new ArrayList<>();
        lp.add(ltepredicate3);
        lp.add(ltepredicate4);
        InfluxReportUtils.addKpi(l,ltekpi33,InfluxReportUtils.getCountFilterKpi(pciFcnGroupby,new String[]{"LTE PCC_RSRP","LTE PCC_SINR"},lp));
        //IMS注册时延(s)
        l.add(InfluxReportUtils.getAvgKpi(pciFcnGroupby,"Registration Delay"));
        //MOS均值（可选）
        l.add(InfluxReportUtils.getAvgKpi(pciFcnGroupby,"MOS_MOSLQO"));
        //MOS≥3.0采样点数（可选）
        Predicate<Map<String,Object>> ltepredicate13=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("MOS_MOSLQO")).toString())>=3.0;
        Long mos_moslqo1 = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby, "MOS_MOSLQO", ltepredicate13);
        l.add(mos_moslqo1);
        //MOS≥3.0采样点占比（可选）
        Long mos_moslqo = InfluxReportUtils.getCountKpi(pciFcnGroupby, "MOS_MOSLQO");
        InfluxReportUtils.addKpi(l,mos_moslqo,mos_moslqo1);
        //MOS≥3.5采样点数（可选）
        Predicate<Map<String,Object>> ltepredicate14=m->Double.parseDouble(m.get(InfluxReportUtils.getColumnName("MOS_MOSLQO")).toString())>=3.5;
        Long mos_moslqo2 = InfluxReportUtils.getCountFilterKpi(pciFcnGroupby, "MOS_MOSLQO", ltepredicate14);
        l.add(mos_moslqo2);
        //MOS≥3.5采样点占比（可选）
        InfluxReportUtils.addKpi(l,mos_moslqo,mos_moslqo2);
        for(int i=0;i<top1ServCellColumns.length;i++){
            rm.put(top1ServCellColumns[i],l.get(i));
        }

    }

    /**
     * 对服务小区的邻区相关列赋值
     * @param pciFcn2BeanMap
     * @param rm
     * @param pciFcnGroupby
     * @param firstRsrp
     * @param lat
     * @param longg
     * @param topnServsCols
     */
    void fillNcellColumnsByServCell(Map<String, List<Cell5G>> pciFcn2BeanMap, Map<String, Object> rm, Map<String, List<Map<String, Object>>> pciFcnGroupby, Map.Entry<String, Double> firstRsrp, Double lat, Double longg, String[] topnServsCols) {
        //服务小区邻区RSRP均值从大到小排序
        List<Map<String,Object>> ncell2rsrpMapList=new ArrayList<>();
        pciFcnGroupby.get(firstRsrp.getKey()).forEach(item->{
            Map<String,Object> ncell2rsrpMap0=new HashMap<>();
            //获取采样点最强邻区pci、fcn
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
            //获取采样点次强邻区pci、fcn
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
            //获取采样点三强邻区pci、fcn
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
            //获取采样点四强邻区pci、fcn
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
            //获取采样点四强邻区pci、fcn
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
            //获取采样点5强邻区pci、fcn
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
            //获取采样点6强邻区pci、fcn
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
            //获取采样点7强邻区pci、fcn
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
           //TOP1服务小区最强邻区
           Map.Entry<String, Double> ncellFirst = ncellPciFcn2avgRsrpListSorted.get(0);
           //TOP1服务小区最强邻区 小区信息列回填
           if(pciFcn2BeanMap.get(ncellFirst.getKey())!=null){
               setCellMsg(rm,lat,longg,pciFcn2BeanMap.get(ncellFirst.getKey()),new String[]{topnServsCols[0],topnServsCols[1],topnServsCols[2]});
           }
           //TOP1服务小区最强邻区PCI
           rm.put(topnServsCols[3],ncellFirst.getKey().split("_")[0]);
           //TOP1服务小区最强邻区arfcn
           rm.put(topnServsCols[4],ncellFirst.getKey().split("_")[1]);
           //TOP1服务小区最强邻区SSB索引号
           rm.put(topnServsCols[5],ncell2ssIndexMap.get(ncellFirst.getKey()));
           //TOP1服务小区最强邻区RSRP
           rm.put(topnServsCols[6],ncellFirst.getValue());
           //TOP1服务小区最强邻区RSRQ
           rm.put(topnServsCols[7],ncell2rsrqMap.get(ncellFirst.getKey()));
           //最强邻区SINR
           rm.put(topnServsCols[8],ncell2sinrMap.get(ncellFirst.getKey()));
       }

        //TOP1服务小区次强邻区
       if(ncellPciFcn2avgRsrpListSorted!=null&&ncellPciFcn2avgRsrpListSorted.size()>1){
           final Map.Entry<String, Double> ncellSec = ncellPciFcn2avgRsrpListSorted.get(1);
           if(pciFcn2BeanMap.get(ncellSec.getKey())!=null){
               setCellMsg(rm,lat,longg,pciFcn2BeanMap.get(ncellSec.getKey()),new String[]{topnServsCols[9],topnServsCols[10],topnServsCols[11]});
           }
           //TOP1服务小区次强邻区PCI
           rm.put(topnServsCols[12],ncellSec.getKey().split("_")[0]);
           //TOP1服务小区次强邻区arfcn
           rm.put(topnServsCols[13],ncellSec.getKey().split("_")[1]);
           //TOP1服务小区次强邻区SSB索引号
           rm.put(topnServsCols[14],ncell2ssIndexMap.get(ncellSec.getKey()));
           //TOP1服务小区次强邻区RSRP
           rm.put(topnServsCols[15],ncellSec.getValue());
           //TOP1服务小区次强邻区RSRQ
           rm.put(topnServsCols[16],ncell2rsrqMap.get(ncellSec.getKey()));
           //TOP1服务小区次强邻区SINR
           rm.put(topnServsCols[17],ncell2sinrMap.get(ncellSec.getKey()));
       }
    }


    /**
     * 事件报表回填需要计算的列
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
     * 采样点报表需要回填的字段
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
            item.put("time", DateComputeUtils.formatTime(item.get("time").toString()));
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
     * 设置小区相关信息
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
     * 设置小区相关信息
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
     * 设置小区相关信息
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
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<Map<String, Object>> results=new ArrayList<>();
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
            InfluxDB connect = InfluxDBFactory.connect(url, username, password, client);
            connect.setDatabase("Task_" + id);
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
            sb.append("SELECT evtName,Lat,Long,Height,Netmode,Pci,Enfarcn,SellID,Rsrp,Sinr from EVT where ");
            Set<String> evts = Arrays.asList(startEvts).stream().collect(Collectors.toSet());
            Set<String> allevts = Arrays.asList(swfailureEvts).stream().collect(Collectors.toSet());
            allevts.addAll(evtCnNameMap.keySet());
            allevts.add("LTE TAU Failure");
            allevts.addAll(evts);
            allevts.forEach(item->{
                sqlFreg.add("evtName ='"+item+"'");
            });
            sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
            QueryResult query = connect.query(new Query(sb.toString()));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            result.forEach(obj->{
                if(evtCnNameMap.keySet().contains(obj.get("evtName").toString())){
                    Map<String, Object> rm=new HashMap<>();
                    rm.put("logName",testLogItem.getFileName());
                    rm.put("testName",testLogItem.getTestName());
                    rm.put("network",testLogItem.getNetworkStandard());
                    rm.put("busiType",evtCnNameMap.get(obj.get("evtName").toString()).getType());
                    rm.put("evtName",evtCnNameMap.get(obj.get("evtName").toString()).getEvtCnName());
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
                    List<Map<String, Object>> startEvtDatas = result.stream().filter(item -> Arrays.asList(startEvts).contains(item.get("evtName").toString())).collect(Collectors.toList());
                    List<Map<String, Object>> pre2sDatas = DateComputeUtils.getPre2sDatas(result, obj.get("time").toString());
                    setEvtMsg(pre2sDatas,startEvtDatas,rm,nrPciFcn2BeanMap,ltePciFcn2BeanMap);
                    if("lte".equalsIgnoreCase(InfluxReportUtils.getNetWork(obj.get("Netmode")))){
                        InfluxReportUtils.setAbevtKpi(connect,rm,InitialConfig.abevtLteKpiMap,obj.get("time").toString());
                    }
                    if("nr".equalsIgnoreCase(InfluxReportUtils.getNetWork(obj.get("Netmode")))){
                        InfluxReportUtils.setAbevtKpi(connect,rm,InitialConfig.abevtNrKpiMap,obj.get("time").toString());
                    }
                    results.add(rm);
                }
            });
            connect.close();
        });
        return results;
    }

    @Override
    public List<Map<String, Object>> getAbEvtAnaGerView(List<String> fileLogIds) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
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
            InfluxDB connect = InfluxDBFactory.connect(url, username, password, client);
            connect.setDatabase("Task_" + id);
            List<String> sqlFreg = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT evtName,Lat,Long from EVT where ");
            evtCnNameMap.keySet().forEach(item -> {
                sqlFreg.add("evtName ='" + item + "'");
            });
            sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
            QueryResult query = connect.query(new Query(sb.toString()));
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
     * 查询evt表前2s的信息
     * @param rm
     * @param endTime
     */
    static String[] swfailureEvts=new String[]{"LTE Intrafreq Handover Failure","LTE Interfreq Handover Failure","EUTRA Handover to NR Failure",
            "NR IntraFreq-Handover Failure","NR InterFreq-Handover Failure","Mobility From NR Failure"};
    static String[] startEvts=new String[]{"LTE Intrafreq Handover Start","LTE Interfreq Handover Start",
            "NR IntraFreq-Handover Start","NR InterFreq-Handover Start"};
    private void setEvtMsg(List<Map<String, Object>> pre2sDatas, List<Map<String, Object>> startEvtDatas, Map<String, Object> rm, Map<String, List<Cell5G>> nrPciFcn2BeanMap, Map<String, List<LteCell>> ltePciFcn2BeanMap) {
        if(pre2sDatas!=null&&!pre2sDatas.isEmpty()){
            Set<String> evtName1 = pre2sDatas.stream().map(item -> item.get("evtName").toString()).collect(Collectors.toSet());
            List<Map<String, Object>> swFailsDatas = pre2sDatas.stream().filter(item -> Arrays.asList(swfailureEvts).contains(item.get("evtName").toString())).collect(Collectors.toList());
            if(evtName1.contains("LTE TAU Failure")){
                rm.put("exsistTauFail","是");
            }else{
                rm.put("exsistTauFail","否");
            }
            if(swFailsDatas!=null&&!swFailsDatas.isEmpty()){
                rm.put("exsistSwFail","是");
                Map<String, Object> map = swFailsDatas.get(swFailsDatas.size() - 1);
                if(map.get("evtName").toString().contains("LTE")){
                    rm.put("swdestnetwork","4G");
                    List<Map<String, Object>> swFailPreDatas = DateComputeUtils.getPreDatas(startEvtDatas, map.get("time").toString());
                    if(swFailPreDatas!=null&&!swFailPreDatas.isEmpty()){
                        fillLteColumns(rm,swFailPreDatas.get(0),ltePciFcn2BeanMap);
                    }
                }else if(map.get("evtName").toString().contains("NR")){
                    rm.put("swdestnetwork","5G");
                    List<Map<String, Object>> swFailPreDatas = DateComputeUtils.getPreDatas(startEvtDatas, map.get("time").toString());
                    if(swFailPreDatas!=null&&!swFailPreDatas.isEmpty())
                        fillNrColumns(rm,swFailPreDatas.get(0),nrPciFcn2BeanMap);
                }

            }else{
                rm.put("exsistSwFail","否");
                rm.put("swdestnetwork",null);
            }
        }else{
            rm.put("exsistTauFail","否");
            rm.put("exsistSwFail","否");
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
            em.put("pci",pci);
            em.put("fcn",fcn);
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
            em.put("pci",pci);
            em.put("fcn",fcn);
        }
    }



    private Integer getGnbID(String operator,String network,Object cellId){
        if(cellId==null||StringUtils.isBlank(cellId.toString())||StringUtils.isBlank(network)){
            return null;
        }
        long cellid=Long.parseLong(cellId.toString());
        if(network.equalsIgnoreCase("NR")){
            if(operator.equalsIgnoreCase("联通")){
                return (int)(cellid/Math.pow(2.0,36-cuccGlen));
            }
            if(operator.equalsIgnoreCase("移动")){
                return (int)(cellid/Math.pow(2.0,36-cmccGlen));
            }
            if(operator.equalsIgnoreCase("电信")){
                return (int)(cellid/Math.pow(2.0,36-ctccGlen));
            }
            return (int)(cellid/Math.pow(2.0,36-cuccGlen));
        }else{
            return (int)cellid/256;
        }
    }
    @Override
    public Map<String, String> getAbevtKpiConfig(){
        return InitialConfig.abevtKpiMap2;
    }

    @Override
    public Map<String, List<Map<String, Object>>> getNetConfigReports(List<String> fileLogIds) {
        Map<String, List<Map<String, Object>>> mixReportsMap=Collections.synchronizedMap(new LinkedHashMap<>());
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        Map<String, List<NetTotalConfig>> lteSheetMap = InitialConfig.lteNetToatls.stream().collect(Collectors.groupingBy(config->config.getNetworkType()+"_"+config.getSheetName()));
        Map<String, List<NetTotalConfig>> nrSheetMap = InitialConfig.nrNetToatls.stream().collect(Collectors.groupingBy(config->config.getNetworkType()+"_"+config.getSheetName()));
        fileLogIds.parallelStream().forEach(file-> {
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file));
            InfluxDB connect = InfluxDBFactory.connect(url, username, password, client);
            connect.setDatabase("Task_" + file);
            InitialConfig.netConfigs.forEach(item->{
                List<Map<String, Object>> sheetDatas = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT evtName,Pci,Enfarcn,SellID from EVT where ");
                sb.append(getNetWhereCondition(item));
                QueryResult query = connect.query(new Query(sb.toString()));
                List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
                //生成事件名和记录的map集合
                Map<String, List<Map<String, Object>>> evtNameDatas = result.stream().collect(Collectors.groupingBy(i -> i.get("evtName").toString()));
                //筛选触发事件的记录
                List<Map.Entry<String, List<Map<String, Object>>>> triggerEvtDatas = evtNameDatas.entrySet().stream().filter(entry ->
                    Arrays.asList(item.getTriggerEvt()).contains(entry.getKey())
                ).collect(Collectors.toList());
                if(item.getType().equalsIgnoreCase("lte")){
                    evtNetConfigBusiProcess(testLogItem, connect, item, result, triggerEvtDatas,InitialConfig.netLteKpiMap,sheetDatas);
                }
                if(item.getType().equalsIgnoreCase("nr")){
                    evtNetConfigBusiProcess(testLogItem, connect, item, result, triggerEvtDatas,InitialConfig.netNrKpiMap,sheetDatas);
                }
                if(mixReportsMap.containsKey(item.getDetailSheet())){
                    List<Map<String, Object>> maps = mixReportsMap.get(item.getDetailSheet());
                    maps.addAll(sheetDatas);
                    mixReportsMap.put(item.getDetailSheet(),maps);
                }else{
                    mixReportsMap.put(item.getDetailSheet(),sheetDatas);
                }
            });
        });
        //创建4G汇总报表
        createTotalReport("4G",mixReportsMap, lteSheetMap);
        //创建5G汇总报表
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
    String[] positiveRingEvt=new String[]{"Incoming SIP Ring","CSFB Alerting MO"};
    String[] activeConnEvt=new String[]{"Outgoing SIP Call Connect","CSFB Connect MO","Outgoing Call Connect"};
    String[] positiveConnRingEvt=new String[]{"Incoming SIP Call Connect","CSFB Connect MT","Incoming Call Connect"};
    String[] activeCallEndEvt=new String[]{"Outgoing SIP Call End","Outgoing SIP Call Drop","Outgoing Call End","Outgoing Call Drop","CSFB Call End MO","CSFB Drop MO"};
    String[] positiveCallEndEvt=new String[]{"Incoming SIP Call End","Incoming SIP Call Drop","Incoming Call End","Incoming Call Drop","CSFB Call End MT","CSFB Call Drop MT"};
    String[] activeCallFailEvt=new String[]{"Outgoing SIP Call Block","Outgoing Call Block"};
    String[] positiveCallFailEvt=new String[]{"Incoming SIP Call Block","Incoming Call Block"};
    String[] activeDropFailEvt=new String[]{"Outgoing SIP Call Drop","CSFB Drop MO、Outgoing Call Drop"};
    String[] positiveDropFailEvt=new String[]{"Incoming SIP Call Drop","CSFB Drop MT、Incoming Call Drop"};

    String[] esfFailCauses=new String[]{"Mobility From NR Failure","N2L_REDIRECT_FAIL","LTE TAU Failure"};
    @Override
    public List<Map<String, Object>> getVoiceBusiReports(List<String> fileLogIds) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        List<Map<String, Object>> results=new ArrayList<>();
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Assert.notEmpty(testLogItems);
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        fileLogIds.parallelStream().forEach(file-> {
            String id=file.trim();
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(file.trim()));
            List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(testLogItem.getCity());
            List<LteCell> lteCells = gisAndListShowServie.getLteCellsByRegion(testLogItem.getCity());
            InfluxDB connect = InfluxDBFactory.connect(url, username, password, client);
            connect.setDatabase("Task_" + id);
            List<VoiceBusiConfig> activeConfigs = InitialConfig.voiceBusiConfigs.stream().filter(item -> item.getBusiType().equalsIgnoreCase("主叫")).collect(Collectors.toList());
            List<VoiceBusiConfig> positiveConfigs =InitialConfig.voiceBusiConfigs.stream().filter(item->item.getBusiType().equalsIgnoreCase("被叫")).collect(Collectors.toList());
            Map<String, String> callTypeMap = InitialConfig.voiceBusiConfigs.stream().collect(Collectors.toMap(item -> item.getBusiType() + item.getTriggerEvt() + item.getNetwork() + item.getFbtype() + item.getRlingNet(), item -> item.getCallType()));
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
            callAna(results, testLogItem, nrCells, lteCells, connect, activeConfigs, callTypeMap, activeColumnList,"主叫");
            callAna(results, testLogItem, nrCells, lteCells, connect, positiveConfigs, callTypeMap, positiveColumnList,"被叫");
            connect.close();
        });
        return results;
    }

    private String getCellType(String busiType, String triggerEvt, String netmode, boolean flag, String ringNet){
        if(busiType.equalsIgnoreCase("主叫")){
            if("Outgoing SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&"nr".equalsIgnoreCase(ringNet)){
                return "VoNR";
            }else if("Outgoing SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"lte".equalsIgnoreCase(netmode)&&"lte".equalsIgnoreCase(ringNet)){
                return "VoNR";
            }else if("Outgoing SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&flag){
                return "EPS FallBack";
            }else if("CSFB Start MO".equalsIgnoreCase(triggerEvt)){
                return "CSFB";
            }
            else if("Outgoing Call Attempt".equalsIgnoreCase(triggerEvt)){
                return "23G语音";
            }
        } else if(busiType.equalsIgnoreCase("被叫")){
            if("Incoming SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&"nr".equalsIgnoreCase(ringNet)){
                return "VoNR";
            } else if("Incoming SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"lte".equalsIgnoreCase(netmode)&&"lte".equalsIgnoreCase(ringNet)){
                return "VoNR";
            } else if("Incoming SIP Call Attempt".equalsIgnoreCase(triggerEvt)&&"nr".equalsIgnoreCase(netmode)&&flag){
                return "EPS FallBack";
            } else if("CSFB Start MT".equalsIgnoreCase(triggerEvt)){
                return "CSFB";
            }
            else if("Incoming Call Attempt".equalsIgnoreCase(triggerEvt)){
                return "23G语音";
            }
        }
        return null;
    }

    /**
     * 呼叫分析
     * @param results
     * @param testLogItem
     * @param nrCells
     * @param lteCells
     * @param connect
     * @param activeConfigs
     * @param callTypeMap
     * @param activeColumnList
     * @param busiType
     */
    void callAna(List<Map<String, Object>> results, TestLogItem testLogItem, List<Cell5G> nrCells, List<LteCell> lteCells, InfluxDB connect, List<VoiceBusiConfig> activeConfigs, Map<String, String> callTypeMap, List<String[]> activeColumnList,String busiType) {
        activeConfigs.stream().map(VoiceBusiConfig::getTriggerEvt).collect(Collectors.toSet()).forEach(item->{
            List<String> sqlFreg=new ArrayList<>();
            StringBuilder sb=new StringBuilder();
            sb.append("SELECT MsgID,evtName,Lat,Long,Height,Netmode,Pci,Enfarcn,SellID,Rsrp,Sinr,extrainfo from EVT where ");
            Set<String> allevts = Arrays.asList(activeRingEvt).stream().collect(Collectors.toSet());
            allevts.addAll(Arrays.asList(activeConnEvt).stream().collect(Collectors.toSet()));
            allevts.addAll(Arrays.asList(activeCallEndEvt).stream().collect(Collectors.toSet()));
            allevts.addAll(Arrays.asList(activeCallFailEvt).stream().collect(Collectors.toSet()));
            allevts.addAll(Arrays.asList(activeDropFailEvt).stream().collect(Collectors.toSet()));
            allevts.add("EPSFallBack Start");
            allevts.add("NR Event B1");
            allevts.add("NR Event B2");
            allevts.add("EPSFallBack Success");
            allevts.add("Fast_Return_Start");
            allevts.add("Fast_Return_Success");
            allevts.add("Fast_Return_Failure");
            allevts.addAll(Arrays.asList());
            allevts.forEach(i->{
                sqlFreg.add("evtName ='"+i+"'");
            });
            sqlFreg.add("evtName ='"+item+"'");
            sb.append(sqlFreg.stream().collect(Collectors.joining(" or ")));
            QueryResult query = connect.query(new Query(sb.toString()));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            List<List<Map<String, Object>>> datasBySplitEvt = DateComputeUtils.getDatasBySplitEvt(result, item);

            for(List<Map<String, Object>> objs:datasBySplitEvt){
                Map<String, Object> rm = getCallDetailMap(testLogItem, item, objs,busiType,activeColumnList);
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
                    setNrVoiceIEKpi(connect,nrVoiceMap,testLogItem, rm, epsFallBack_failure,"lte");
                    setLteVoiceIEKpi(connect,lteVoiceMap,testLogItem, rm, epsFallBack_failure,"NR");
                }else{
                    for(int i=13;i<34;i++){
                        rm.put(esfbColumnIndexs[i],null);
                    }
                }
                //掉话业务
                dropBusi(nrCells, lteCells, connect, objs, rm);
                //fr失败
                frFailBusi(connect, objs, rm);
                results.add(rm);
            }
        });
    }

    /**
     * 获取呼叫详情
     * @param testLogItem
     * @param item
     * @param objs
     * @param busiType
     * @param columnList
     * @return
     */
    Map<String, Object> getCallDetailMap(TestLogItem testLogItem, String item, List<Map<String, Object>> objs,String busiType,List<String[]> columnList) {
        Map<String, Object> fallBackRecord = getEvtRecord(objs,"EPSFallBack Start");
        Map<String, Object> rm = new HashMap<>();
        String ringTime = exsistEvtTime(objs, columnList.get(0));
        String ringNet=getRingNet(objs, columnList.get(0));
        String connTime = exsistEvtTime(objs, columnList.get(1));
        String endTime = exsistEvtTime(objs, columnList.get(2));
        //LOG名称
        rm.put("basic01", testLogItem.getFileName());
        //主叫/被叫
        rm.put("basic02",busiType);
        boolean flag=fallBackRecord==null?false:true;
        //呼叫类型
        rm.put("call01",getCellType(busiType,item,InfluxReportUtils.getNetWork(objs.get(0).get("Netmode")),flag,ringNet));
        //起呼时间
        rm.put("call02", DateComputeUtils.formatMicroTime(objs.get(0).get("time").toString()));
        //振铃时间
        rm.put("call03", ringTime);
        //接通时间
        rm.put("call04", connTime);
        //呼叫时延(s)
        rm.put("call05", DateComputeUtils.getDelay(ringTime, connTime));
        //呼叫结束时间
        rm.put("call06", endTime);
        //呼叫保持时长(s)
        rm.put("call07", DateComputeUtils.getDelay(connTime, endTime));
        //呼叫失败时间
        rm.put("call08", exsistEvtTime(objs, columnList.get(3)));
        //掉话时间
        rm.put("call09", exsistEvtTime(objs, columnList.get(4)));
        if(fallBackRecord!=null){
            String fbackWay = getFbackWay(fallBackRecord, objs);
            //回落方式
            rm.put(esfbColumnIndexs[0], fbackWay);
            String time = DateComputeUtils.formatMicroTime(fallBackRecord.get("time").toString());
            //切换回落尝试
            rm.put(esfbColumnIndexs[1], time);
            if("切换".equalsIgnoreCase(fbackWay)){
                String time1=getBehindExsistEvtTime(fallBackRecord, objs,"HandoverTargetRAT","EPSFallBack Success");
                String time2=getBehindExsistEvtTime(fallBackRecord, objs,"HandoverTargetRAT","EPSFallBack Failure");
               //切换回落成功
                rm.put(esfbColumnIndexs[2],time1);
                //切换回落失败
                rm.put(esfbColumnIndexs[3],time2);
                if(null!=time1&&null!=time2){
                    //切换回落时延
                    rm.put(esfbColumnIndexs[4],DateComputeUtils.getDelay(time1, time));
                }
            }else if(!"切换".equalsIgnoreCase(fbackWay)){
                String time1=getBehindExsistEvtTime(fallBackRecord, objs,"RedirectedTargetRAT","EPSFallBack Success");
                String time2=getBehindExsistEvtTime(fallBackRecord, objs,"RedirectedTargetRAT","EPSFallBack Failure");
                //重定向回落尝试
                rm.put(esfbColumnIndexs[4], time);
                //重定向回落尝试
                rm.put(esfbColumnIndexs[5],time1);
                //重定向回落成功
                rm.put(esfbColumnIndexs[6],time1);
                //重定向回落失败
                rm.put(esfbColumnIndexs[7],time2);
                if(null!=time1&&null!=time2){
                    rm.put(esfbColumnIndexs[8],DateComputeUtils.getDelay(time1, time));
                }else{
                    rm.put(esfbColumnIndexs[8],null);
                }
            }else{
                rm.put(esfbColumnIndexs[5],null);
                rm.put(esfbColumnIndexs[6],null);
                rm.put(esfbColumnIndexs[7],null);
            }
            //返回5G尝试	时间	事件“Fast_Return_Start”
            String fastrstime = exsistEvtTime(objs, new String[]{"Fast_Return_Start"});
            rm.put(esfbColumnIndexs[8], fastrstime);
            //返回5G成功	时间	事件“Fast_Return_Success”
            String fastrsucctime = exsistEvtTime(objs, new String[]{"Fast_Return_Success"});
            rm.put(esfbColumnIndexs[9], fastrsucctime);
            //返回5G失败	时间	事件“Fast_Return_Failure”
            String fastrfailtime = exsistEvtTime(objs, new String[]{"Fast_Return_Failure"});
            rm.put(esfbColumnIndexs[11], fastrfailtime);
            //返回5G时延	时延	“返回5G成功时间”-“返回5G尝试时间”
            if(null!=fastrstime&&null!=fastrsucctime){
                rm.put(esfbColumnIndexs[12],DateComputeUtils.getDelay(fastrsucctime, fastrstime));
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
     * fr 失败业务
     * @param connect
     * @param objs
     * @param rm
     */
    private void frFailBusi(InfluxDB connect, List<Map<String,Object>> objs, Map<String,Object> rm) {
        List<String> dropFailEvts=new ArrayList<>();
        dropFailEvts.addAll(Arrays.asList(new String[]{"Fast_Return_Failure"}));
        Map<String, Object> frs=null;
        String time1=null;
        String time2=null;
        for(Map<String, Object> obj:objs){
            //是否存在事件“Fast_Return_Start”
            if("Fast_Return_Start".equalsIgnoreCase(obj.get("evtName").toString())){
                frs=obj;
            }
            //是否存在事件“Fast_Return_Failure”
            if("Fast_Return_Failure".equalsIgnoreCase(obj.get("evtName").toString())){
                List<Map<String, Object>> result1 = getRecordByMsgIDFromSign(connect, obj);
                if(result1!=null&&!result1.isEmpty()){
                    rm.put(frColumnIndexs[3],"是");
                    rm.put(frColumnIndexs[4],result1.get(0).get("signalName"));
                }else{
                    rm.put(frColumnIndexs[3],"否");
                }
                time1=obj.get("time").toString();
                //nr
                Map<String, String> abevtKpiMap=new HashMap<>();
                abevtKpiMap.put(frColumnIndexs[5],"avg(50055)");
                abevtKpiMap.put(frColumnIndexs[6],"avg(50056)");
                InfluxReportUtils.setNetIEKpi(connect,rm,abevtKpiMap,time1);

                rm.put(dropColumnIndexs[10],null);
                rm.put(dropColumnIndexs[11],null);
                break;
            }
            if(frs!=null){
                time2=frs.get("time").toString();
                String longg=frs.get("Long").toString();
                String lat=frs.get("Lat").toString();
                Map<String, String> abevtKpiMap=new HashMap<>();
                abevtKpiMap.put(dropColumnIndexs[0],"是");
                abevtKpiMap.put(dropColumnIndexs[1],longg);
                abevtKpiMap.put(dropColumnIndexs[2],lat);

            }
        }
        if(time1!=null&&time2!=null){
            exsistByContentFromSign(connect,time2,time1);
        }
    }

    /**
     * 掉话业务
     * @param nrCells
     * @param lteCells
     * @param connect
     * @param objs
     * @param rm
     */
    void dropBusi(List<Cell5G> nrCells, List<LteCell> lteCells, InfluxDB connect, List<Map<String, Object>> objs, Map<String, Object> rm) {
        List<String> dropFailEvts=new ArrayList<>();
        dropFailEvts.addAll(Arrays.asList(activeDropFailEvt));
        dropFailEvts.addAll(Arrays.asList(positiveDropFailEvt));
        Map<String, Object> tauFailObj=null;
        for(Map<String, Object> obj:objs){
            if("LTE TAU Failure".equalsIgnoreCase(obj.get("evtName").toString())){
                tauFailObj=obj;
            }
            //判断记录是否包含于掉话失败事件列表中
            if(dropFailEvts.contains(obj.get("evtName"))){
                String pci=String.valueOf(obj.get("Pci"));
                String fcn=String.valueOf(obj.get("Enfarcn"));
                String time1=String.valueOf(obj.get("time"));
                Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item1 -> item1.getPci() + "_" + item1.getFrequency1()));
                Map<String, List<LteCell>> ltePciFcn2BeanMap = lteCells.stream().collect(Collectors.groupingBy(item1 -> item1.getPci() + "_" + item1.getFrequency1()));
                List<Cell5G> nrPlanParamPojos = nrPciFcn2BeanMap.get(pci + "_" + fcn);
                List<LteCell> ltePlanParamPojos = ltePciFcn2BeanMap.get(pci + "_" + fcn);
                List<Map<String, Object>> result1 = getRecordByMsgIDFromSign(connect, obj);
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
                    InfluxReportUtils.setNetIEKpi(connect,rm,abevtKpiMap,time1);
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
                    InfluxReportUtils.setNetIEKpi(connect,rm,abevtKpiMap,time1);
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
            abevtKpiMap.put(dropColumnIndexs[12],"是");
            abevtKpiMap.put(dropColumnIndexs[13],"LAST(40009)");
            InfluxReportUtils.setVoiceIEKpi(connect,rm,abevtKpiMap, tauFailObj.get("time").toString());
        }

    }

    /**
     * 根据msgid从信令表中获取记录
     * @param connect
     * @param obj
     * @return
     */
    List<Map<String, Object>> getRecordByMsgIDFromSign(InfluxDB connect, Map<String, Object> obj) {
        String msgID=obj.get("MsgID").toString();
        String sigSql="select signalName from SIG where MsgID=''{0}''";
        String formatSql = MessageFormat.format(sigSql, msgID);
        QueryResult query1 = connect.query(new Query(formatSql));
        return InfludbUtil.paraseQueryResult(query1);
    }

    /**
     * 在时间段内是否存在信令内容
     * @param connect
     * @param start
     * @param end
     * @return
     */
    private Map<String,String> exsistByContentFromSign(InfluxDB connect,String start,String end) {
        Map<String,String> conMap=new HashMap<>();
        conMap.put(frColumnIndexs[8],"mib");
        conMap.put(frColumnIndexs[9],"systemInformationBlockType1");
        conMap.put(frColumnIndexs[10],"Registration request");
        conMap.put(frColumnIndexs[11],"Registration accept/Registration complete");
        conMap.put(frColumnIndexs[12],"8:AUTHENTICATION FAILURE");
        conMap.put(frColumnIndexs[13],"Registration Reject");

        String sigSql="select signalName,Netmode from SIG where time>=''{0}'' and time<=''{1}'' and {2}";
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
            QueryResult query1 = connect.query(new Query(formatSql));
            List<Map<String, Object>> maps = InfludbUtil.paraseQueryResult(query1);
            if(maps!=null&&!maps.isEmpty()){
                conMap.put(key,"是");
            }else{
                conMap.put(key,"否");
            }
        });
        return conMap;
    }


    @Override
    public List<Map<String, Object>> getReportCellKpi(List<String> fileLogIds) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(timeout,TimeUnit.SECONDS);
        String sql="select  LAST(IEValue_53601) as fcn,LAST(IEValue_50007) as pci,LAST(IEValue_40028) as LTE_PCC_RSRP from IE GROUP by time(1s) fill(none)";
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<Map<String, Object>> results=Collections.synchronizedList(new ArrayList<>());
        List<Map<String, Object>> results1=new ArrayList<>();
        fileLogIds.parallelStream().forEach(id->{
            InfluxDB connect = InfluxDBFactory.connect(url, username, password,client);
            connect.setDatabase("Task_"+id);
            QueryResult query=connect.query(new Query(sql));
            List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
            result.forEach(item->{
                item.put("operator",id2LogBeanMap.get(Long.parseLong(id)).getOperatorName());
                item.put("nettype",item.get("LTE_PCC_RSRP")!=null?"NSA":"SA");
            });
            results.addAll(result);
            connect.close();
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
    private void setNrVoiceIEKpi(InfluxDB conn,Map<String, String> columnMap, TestLogItem testLogItem, Map<String, Object> rm, Map<String, Object> epsFallBack_failure,String destnetwork) {
        Map<String, Object> map = InfluxReportUtils.setVoiceIEKpi(conn, rm, columnMap, epsFallBack_failure.get("time").toString());
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

    private void setLteVoiceIEKpi(InfluxDB conn,Map<String, String> columnMap, TestLogItem testLogItem, Map<String, Object> rm, Map<String, Object> epsFallBack_failure, String destnetwork) {
        Map<String, Object> map = InfluxReportUtils.setVoiceIEKpi(conn, rm, columnMap, epsFallBack_failure.get("time").toString());
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
     * 获取epsfallback 回落方式
     *
     * @return
     */
    private String getFbackWay( Map<String, Object> epsFallBackRecord, List<Map<String, Object>> result){
        String extraInfo = epsFallBackRecord==null?"":epsFallBackRecord.containsKey("ExtraInfo")?epsFallBackRecord.get("ExtraInfo").toString():"";
        if(extraInfo.contains("HandoverTargetRAT")){
            return "切换";
        }else if(extraInfo.contains("RedirectedTargetRAT")){
            List<Map<String, Object>> timePre2sDatas = DateComputeUtils.getPre2sDatas(result, epsFallBackRecord.get("time").toString());
            boolean flag=timePre2sDatas.stream().filter(item->Arrays.asList(fallbackEvts).contains(item.get("evtName").toString()))
                    .findAny().isPresent();
            if(flag){
                return "基于测量重定向";
            }else{
                return "盲重定向";
            }
        }
        return null;
    }

    private String getBehindExsistEvtTime( Map<String, Object> epsFallBackRecord, List<Map<String, Object>> result,String containValue,String evt){
        String extraInfo = epsFallBackRecord==null?"":epsFallBackRecord.containsKey("ExtraInfo")?epsFallBackRecord.get("ExtraInfo").toString():"";
        if(extraInfo.contains(containValue)){
            List<Map<String, Object>> timePre2sDatas = DateComputeUtils.getPre2sDatas(result, epsFallBackRecord.get("time").toString());
            Optional<Map<String, Object>> optinal = timePre2sDatas.stream().filter(item -> item.get("evtName").toString().equalsIgnoreCase(evt))
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
     * 获取evts 包含事件的时间
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
     * 创建综合报表
     * @param mixReportsMap
     * @param lteVoiceSheetList
     */
    private void createTotalReport(String sheetName,Map<String, List<Map<String, Object>>> mixReportsMap, Map<String, List<NetTotalConfig>> lteVoiceSheetList){
        Set<String> keys=new HashSet<>();
        //合并详表数据
        List<Map<String,Object>> partSheet=new ArrayList<>();
        lteVoiceSheetList.forEach((s,list)->{
            String[] splits = s.split("_");
            final List<Map<String, Object>> partFofSheet=new ArrayList<>();
            Arrays.asList(splits[2].split("/")).forEach(sheet->{
                if(mixReportsMap.containsKey(sheet)){
                    List<Map<String, Object>> maps = mixReportsMap.get(sheet);
                    partFofSheet.addAll(maps);
                    List<String> keyList=new ArrayList<>();
                    maps.forEach(record->{
                        keyList.add(record.get("prov").toString());
                        keyList.add(record.get("city").toString());
                        keyList.add(record.get("pci").toString());
                        keyList.add(record.get("fcn").toString());
                        keyList.add(record.get("cellId").toString());
                        keyList.add(record.get("builder").toString());
                        keyList.add(record.get("contractor").toString());
                        keys.add(keyList.stream().collect(Collectors.joining("_")));
                    });
                }
            });
            Map<String, List<Map<String, Object>>> collect = partFofSheet.stream().collect(Collectors.groupingBy(item -> item.get("prov") + "_" + item.get("city") + "_" +
                    item.get("pci") + "_" + item.get("fcn") + "_" + item.get("cellId") + "_" + item.get("builder")
                    + "_" + item.get("contractor")));
            //语音日志小区对应的最近的一条记录
            Predicate<Map<String,Object>> p=item->item.get("logName").toString().contains("EPSFallBack语音业");
            Map<String,Object> voiceCellFirstRecordMap=creatPartOfSheetData(list, collect,p);
            //数据业务日志小区对应的最近的一条记录
            Predicate<Map<String,Object>> p1=item->!item.get("logName").toString().contains("EPSFallBack语音业");
            Map<String,Object> dataCellFirstRecordMap=creatPartOfSheetData(list, collect,p1);
            partSheet.add(voiceCellFirstRecordMap);
            partSheet.add(dataCellFirstRecordMap);
        });
        List<Map<String,Object>> rel=new ArrayList<>();
        keys.stream().forEach(cellKey->{
            Map<String,Object> r=new HashMap<>();
            partSheet.forEach(item->{
                if(item!=null&&item.containsKey(cellKey))
                r.putAll((Map<String,Object>)item.get(cellKey));
            });
            if(r.size()>0&&sheetName.equalsIgnoreCase("4G")){
                if(StringUtils.isNotBlank(r.get("kpi1").toString())&&StringUtils.isNotBlank(r.get("kpi2").toString())&&StringUtils.isNotBlank(r.get("kpi3").toString())){
                    r.put("exsistTestRediectDetail","是");
                }else{
                    r.put("exsistTestRediectDetail","否");
                }
                r.put("key",cellKey);
                rel.add(r);
            }
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
                //筛选出汇总报表每个小项中的列 并进行设值
                list.forEach(item->{
                    obj.put(item.getKpiIndex(),firstR.get(item.getSrcIndex()));
                });
                dataCellFirstRecordMap.put(k,obj);
            }
        });
        return dataCellFirstRecordMap;
    }

    private void evtNetConfigBusiProcess(TestLogItem testLogItem, InfluxDB connect, AbEventConfig item, List<Map<String, Object>> result, List<Map.Entry<String, List<Map<String, Object>>>> triggerEvtDatas, Map<String, String> netKpiMap, List<Map<String, Object>> sheetDatas) {
        triggerEvtDatas.forEach(entry->{
            List<Map<String, Object>> value = entry.getValue();
            value.forEach(record->{
                Map<String, Object> r=new HashMap<>();
                String triggerTime = record.get("time").toString();
                Object cellId = Optional.ofNullable(record.get("SellID")).orElse("");
                Object fcn = Optional.ofNullable(record.get("Enfarcn")).orElse("");
                Object pci = Optional.ofNullable(record.get("Pci")).orElse("");
                //判断是否有前置条件
                String[] preCons = item.getPreCon();
                boolean flag;
                if(preCons.length>0){
                    List<Map<String, Object>> pre2sDatas = DateComputeUtils.getPre2sDatas(result, triggerTime);
                    //判断前置条件的类型
                    if(preCons[0].contains("无")){
                        if(pre2sDatas!=null&&!pre2sDatas.isEmpty()) {
                            //结果集不能包含“无”后面的事件
                            flag = pre2sDatas.stream().allMatch(a -> !Arrays.asList(preCons).contains(a.get("evtName")));
                        }else{
                            //结果集为空自然不包含
                            flag=true;
                        }
                    }else{
                        if(pre2sDatas!=null&&!pre2sDatas.isEmpty()) {
                            flag = pre2sDatas.stream().anyMatch(a -> !Arrays.asList(preCons).contains(a.get("evtName")));
                        }else{
                            flag=false;
                        }
                    }
                    //判断前置条件是否成立
                    if(flag){
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
                        InfluxReportUtils.setNetIEKpi(connect,r,netKpiMap,triggerTime);
                        sheetDatas.add(r);
                    }
                }else{
                    //无前置条件
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
                    InfluxReportUtils.setNetIEKpi(connect,r,netKpiMap,triggerTime);
                    sheetDatas.add(r);
                }
            });
        });
    }

    private void setBebindValue(Map<String, Object> record,AbEventConfig item,List<Map<String, Object>> result,String triggerTime){
        String[] preCons = item.getPreCon();
        //获取触发事件后的数据
        List<Map<String, Object>> afterDatas = DateComputeUtils.getAfterDatas(result, triggerTime);
        if(afterDatas!=null&&!afterDatas.isEmpty()){
            //判断之后的数据是否包含后置条件
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
                //说明触发事件后 没有触发事件在后置事件之前的情况
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
        //触发事件条件
        for(String s:config.getTriggerEvt()){
            sqlFreg.add("evtName ='" + s.trim() + "'");
        }
        //前置条件
        for(String s:config.getPreCon()){
            s=s.replace("无","");
            sqlFreg.add("evtName ='" + s.trim() + "'");
        }
        //后置条件
        for(String s:config.getSuffCon()){
            sqlFreg.add("evtName ='" + s.trim() + "'");
        }
        return sqlFreg.stream().collect(Collectors.joining(" or "));
    }





}
