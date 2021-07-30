package com.datang.service.influx;

import com.datang.common.influxdb.InfludbUtil;
import com.datang.common.influxdb.InfluxDbConnection;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.util.GPSUtils;
import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang.StringUtils;
import org.influxdb.dto.QueryResult;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InfluxReportUtils {
    private static Pattern p1=Pattern.compile("[\\(（](.*?)[\\)）]");
    public static String FILED_PREFIX="IEValue_";
    public static Set<String> getEnStr(String s){
        Set<String> c=new HashSet<>();
        Matcher m = p1.matcher(s);
        while (m.find())
        {
            String group = m.group();
            c.add(group.trim().replaceAll("[（(]","").replaceAll("[）)]",""));
        }
        return c;
    }

    private static DecimalFormat df = new DecimalFormat("#0.0000");

    public void putRateInMap(Map<String,Object> r,Map<String,Object> record,String key,Double x,Double sum2){
        if(sum2!=0.0&&x!=0.0){
            String rate26=df.format(x/sum2);
            r.put(key,rate26);
        }else{
            r.put(key,null);
        }
    }

    private static Double getTimes(Map<String,Object> record, String key){
        if(record.containsKey(key)){
            return Double.parseDouble(record.get(key).toString());
        }
        return null;
    }

    /**
     * 事件kpi比例设值
     * @param record
     */
    public static Map<String, Object> evtKpiRateSet(Map<String,String> kpiMap,Map<String,Set<String>> countMap,Map<String, Object> record) {
        Map<String, Object> r=new HashMap<>();
        Map<String, Object> copy=new HashMap<>();
        record.forEach((key,value)->{
            copy.put(key.replaceAll("[\\s-]","_"),value);
        });
        countMap.forEach((key,set)->{
            if(set.size()>1){
                List<String> s=new ArrayList<>();
                set.forEach(i->{
                    String m=i.replaceAll("[\\s-]","_");
                    if(!record.containsKey(i)){
                        copy.put(m,0);
                    }
                });
                String exp=kpiMap.get(key);
                exp=exp.replaceAll("[\\s-]","_")
                        .replaceAll("count","").replaceAll("\\[","")
                        .replaceAll("]","");
                if(StringUtils.isNotBlank(exp)){
                    try {
                        r.put(key, df.format(AviatorEvaluator.execute(exp, copy)));
                    }catch (Exception e){
                        r.put(key,null);
                    }
                }else{
                    r.put(key,null);
                }
            }else{
                r.put(key,getTimes(record,set.toArray()[0].toString()));
            }
        });
        return r;
    }


    public static void addKpi(List<Object> l, Double sum2, String x6) {
        if(StringUtils.isNotBlank(x6)&&sum2!=null){
            Double a=Double.parseDouble(x6);
            if(sum2!=0.0&&a!=0.0){
                String rate26=df.format(a/sum2);
                l.add(rate26);
            }else{
                l.add(null);
            }
        }else{
            l.add(null);
        }
    }

    public static void addKpi(List<Object> l, Double sum2, Double x6) {
        if(x6==null||sum2==null){
            l.add(null);
            return;
        }
        if(sum2!=0.0&&x6!=0.0){
            String rate26=df.format(x6/sum2);
            l.add(rate26);
        }else{
            l.add(null);
        }
    }
    public static void addKpi(List<Object> l, Long sum2, Long x6) {
        if(x6==null||sum2==null){
            l.add(null);
            return;
        }
        if(sum2!=0&&x6!=0){
            String rate26=df.format(x6/sum2);
            l.add(rate26);
        }else{
            l.add(null);
        }
    }

    public static Double rate(Long sum2, Long x6) {
        if(x6==null||sum2==null){
            return null;
        }
        if(sum2!=0){
            return x6*1.0/sum2;
        }
        return null;
    }

    public static Double add(String...ds){
        Double r=0.0;
        for (String d:ds){
            if(d==null){
                continue;
            }else{
                r=Double.parseDouble(d)+r;
            }
        }
        return r;
    }

    public static Double add2(Double...ds){
        Double r=0.0;
        for (Double d:ds){
            if(d==null){
                continue;
            }else{
                r=d+r;
            }
        }
        return r;
    }

    public static String getAvgKpi(String key,Map<String, List<Map<String, Object>>> pciFcnGroupby,String index){
        List<Map<String, Object>> collect = pciFcnGroupby.get(key).stream().filter(item -> item.get(getColumnName(index)) != null).collect(Collectors.toList());
        return !collect.isEmpty()?df.format(collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(getColumnName(index))))).average().getAsDouble()):null;
    }

    public static String getAvgKpi(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(getColumnName(index)) != null).collect(Collectors.toList());
        return !collect.isEmpty()?df.format(collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(getColumnName(index))))).average().getAsDouble()):null;
    }

    public static Double getAvgKpi1(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(getColumnName(index)) != null).collect(Collectors.toList());
        return !collect.isEmpty()?collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(getColumnName(index))))).average().getAsDouble():null;
    }

    public static Double getAvgKpi2(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(index) != null).collect(Collectors.toList());
        return !collect.isEmpty()?collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(index)))).average().getAsDouble():null;
    }

    public static Long getCountKpi(List<Map<String, Object>> list,String index){
        return list.stream().filter(item -> item.get(getColumnName(index)) != null).count();
    }
    public static Long getCountKpi2(List<Map<String, Object>> list,String index){
        return list.stream().filter(item -> item.get(index) != null).count();
    }

    public static Long getCountFilterKpi2(List<Map<String, Object>> list,String index, Predicate<? super Map<String, Object>> predicate){
        return list.stream().filter(item -> item.get(index) != null).filter(item->predicate.test(item)).count();
    }

    public static Double getMaxKpi(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(index) != null).collect(Collectors.toList());
        return collect!=null&&!collect.isEmpty()?collect.stream().filter(item -> item.get(index) != null).map(item->Double.parseDouble(item.get("index").toString())).max(Comparator.comparingDouble(Double::doubleValue)).get():null;
    }
    public static Double getMinKpi(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(index) != null).collect(Collectors.toList());
        return collect!=null&&!collect.isEmpty()?collect.stream().filter(item -> item.get(index) != null).map(item->Double.parseDouble(item.get("index").toString())).min(Comparator.comparingDouble(Double::doubleValue)).get():null;
    }

    public static Long getCountKpi(String key,Map<String, List<Map<String, Object>>> pciFcnGroupby,String index){
        return pciFcnGroupby.get(key).stream().filter(item -> item.get(getColumnName(index)) != null).count();
    }

    public static Long getCountFilterKpi(String key, Map<String, List<Map<String, Object>>> pciFcnGroupby, String index, Predicate<? super Map<String, Object>> predicate){
        return pciFcnGroupby.get(key).stream().filter(item -> item.get(getColumnName(index)) != null).filter(item->predicate.test(item)).count();
    }

    public static Long getCountFilterKpi(List<Map<String, Object>> list, String index, Predicate<? super Map<String, Object>> predicate){
        return list.stream().filter(item -> item.get(getColumnName(index)) != null).filter(item->predicate.test(item)).count();
    }
    public static Long getCountFilterKpi(List<Map<String, Object>> list, String[] indexs, List<Predicate<Map<String,Object>>> predicates){
        List<Map<String, Object>> a=list;
        for(int i=0;i<indexs.length;i++){
            int finalI = i;
            a=a.stream().filter(item -> item.get(getColumnName(indexs[finalI])) != null).filter(item -> predicates.get(finalI).test(item)).collect(Collectors.toList());
        }
        return (long) a.size();
    }
    public static String getSumKpi(String key,Map<String, List<Map<String, Object>>> pciFcnGroupby,String index){
        List<Map<String, Object>> collect = pciFcnGroupby.get(key).stream().filter(item -> item.get(getColumnName(index)) != null).collect(Collectors.toList());
        return !collect.isEmpty()?df.format(collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(getColumnName(index))))).sum()):null;
    }

    public static String getSumKpi(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(getColumnName(index)) != null).collect(Collectors.toList());
        return !collect.isEmpty()?df.format(collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(getColumnName(index))))).sum()):null;
    }

    public static String getSumKpi2(List<Map<String, Object>> list,String index){
        List<Map<String, Object>> collect = list.stream().filter(item -> item.get(index) != null).collect(Collectors.toList());
        return !collect.isEmpty()?df.format(collect.stream().mapToDouble(f -> Double.parseDouble(String.valueOf(f.get(index)))).sum()):null;
    }

    public static String getColumnName(String name){
        if(InitialConfig.map.containsKey(name)){
            return FILED_PREFIX+InitialConfig.map.get(name);
        }else{
            return FILED_PREFIX+name;
        }
    }


    /**
     * 查询IE表前2s的信息
     * 设置 异常事件分析指标值
     * @param connect
     * @param rm
     * @param abevtKpiMap
     */
    public static void setAbevtKpi(InfluxDbConnection connect,Map<String, Object> rm, Map<String, String> abevtKpiMap, String endTime,String id) {
        String sql="select {1} from "+getTableName(id,"IE")+" WHERE time<''{0}'' and time>=''{0}''-2s";
        List<String> fregs=new ArrayList<>();
        if(rm.get("exsistTauFail").toString().equalsIgnoreCase("是")){
            fregs.add(FILED_PREFIX+"40009 as srcTac");
        }else{
            rm.put("srcTac",null);
        }
        rm.putAll(commonIEVlaueSet(connect,abevtKpiMap, endTime, sql, fregs));
    }

    /**
     * 网络参数报表查询IE表前2s的信息
     * @param connect
     * @param rm
     * @param abevtKpiMap
     */
    public static void setNetIEKpi(InfluxDbConnection connect, Map<String, Object> rm, Map<String, String> abevtKpiMap, String endTime,String id) {
        String sql="select {1} from "+getTableName(id,"IE")+" WHERE time<''{0}'' and time>=''{0}''-2s";
        List<String> fregs=new ArrayList<>();
        rm.putAll(commonIEVlaueSet(connect, abevtKpiMap, endTime, sql, fregs));
    }

    /**
     * 语音报表查询IE最近信息
     * @param connect
     * @param abevtKpiMap
     */
    public static Map<String, Object> setVoiceIEKpi(InfluxDbConnection connect, Map<String, String> abevtKpiMap, String endTime,String id) {
        String sql="select {1} from "+getTableName(id,"IE")+" WHERE time<''{0}''";
        List<String> fregs=new ArrayList<>();
        return commonIEVlaueSet(connect, abevtKpiMap, endTime, sql, fregs);
    }

    public static Map<String, Object> commonIEVlaueSet(InfluxDbConnection connect, Map<String, String> abevtKpiMap, String endTime, String sql, List<String> fregs) {
        abevtKpiMap.forEach((key,value)->{
            if(value.toLowerCase().contains("avg")){
                fregs.add(value.replaceAll("avg\\s*\\(\\s*","MEAN\\("+FILED_PREFIX)+" as "+key);
            }else if(value.toLowerCase().contains("min")){
                fregs.add(value.replaceAll("min\\s*\\(\\s*","MIN\\("+FILED_PREFIX)+" as "+key);
            }else if(value.toLowerCase().contains("max")){
                fregs.add(value.replaceAll("max\\s*\\(\\s*","MAX\\("+FILED_PREFIX)+" as "+key);
            }else if(value.toUpperCase().contains("LAST")){
                fregs.add(value.replaceAll("LAST\\s*\\(\\s*","LAST\\("+FILED_PREFIX)+" as "+key);
            }
        });
        String kpi=fregs.stream().collect(Collectors.joining(","));
        sql=MessageFormat.format(sql,endTime,kpi);
        QueryResult query = connect.query(sql);
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        if(result!=null&&!result.isEmpty()){
            result.get(0).remove("time");
            return result.get(0);
        }
        return Collections.emptyMap();
    }

    public static String getTableName(Long logid,String suffix){
        return "Task_"+logid+"_"+suffix;
    }

    public static String getTableName(String logid,String suffix){
        return "Task_"+logid+"_"+suffix;
    }
    /**
     * 通过计算3公里范围内的小区距离计算出离坐标最近的小区
     * @param lat
     * @param longg
     * @param planParamPojos
     * @param <T>
     * @return
     */
    public static <T> T getCell(Double lat, Double longg, List<T> planParamPojos){
        if(lat==null||longg==null){
            return null;
        }
        AtomicReference<Double> minDis= new AtomicReference<>(3100.0);
        AtomicReference<T> planParamPojo=new AtomicReference<>();
        planParamPojos.forEach(item->{
            Double distance=0.0;
            if(item instanceof LteCell){
                distance=GPSUtils.distance(lat,longg,((LteCell)item).getLatitude(),((LteCell)item).getLatitude());
            }
            if(distance<3000){
                if(minDis.get()>distance){
                    minDis.set(distance);
                    planParamPojo.set(item);
                }
            }
        });
        return planParamPojo.get();
    }

    public static String getNetWork(Object o){
        if(o.toString().equals("6")){
            return "LTE";
        }else if(o.toString().equals("8")){
            return "NR";
        }
        return "";
    }

}
