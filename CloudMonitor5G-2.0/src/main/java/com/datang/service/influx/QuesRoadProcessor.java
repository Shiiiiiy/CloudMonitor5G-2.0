package com.datang.service.influx;

import com.datang.common.influxdb.InfluxDBMutiConntion;
import com.datang.common.influxdb.InfluxDbConnection;
import com.datang.common.util.DateComputeUtils;
import com.datang.dao.quesroad.QuesRoadDao;
import com.datang.domain.platform.projectParam.Cell5G;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.exceptionevent.GisAndListShowServie;
import com.datang.service.influx.bean.QuesRoadThreshold;
import com.datang.service.influx.impl.InfluxServiceImpl;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.util.AdjPlaneArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 问题路段处理类
 */
@Service(value = "quesRoadService")
public class QuesRoadProcessor extends InfluxServiceImpl implements QuesRoadService {
    @Autowired
    private QuesRoadDao quesRoadDao;

    @Autowired
    private UnicomLogItemService unicomLogItemService;
    //问题路段获取基础采样点模板sql
    private static String BASE_ROAD_SAMP_SQL="SELECT Long,Lat,IEValue_51192,IEValue_51193,IEValue_53432,IEValue_53431,IEValue_50087,IEValue_53434,IEValue_53433,IEValue_50007,IEValue_71053,IEValue_71054,IEValue_71051,IEValue_53419,IEValue_71052,IEValue_54572,IEValue_53483,IEValue_54231,IEValue_50097,IEValue_50055,IEValue_50990,IEValue_53456,IEValue_53601,IEValue_50056,IEValue_50991,IEValue_50014,IEValue_53457,IEValue_74214,IEValue_53682,IEValue_71000,IEValue_73001,IEValue_73100,IEValue_73000 FROM {0} where  Lat=~/./ and Long=~/./ ";
    private static Map<String,String[]> WHERE_MAP=new HashMap<>();
    private static Map<String,String[]> EVTS_MAP=new HashMap<>();
    private static Map<String,String[]> START_EVTS_MAP=new HashMap<>();

    static {
        WHERE_MAP.put("上行质差路段",new String[]{"IEValue_50097","IEValue_54333"});
        WHERE_MAP.put("下行质差路段",new String[]{"IEValue_50055","IEValue_50056"});
        WHERE_MAP.put("弱覆盖路段",new String[]{"IEValue_50055"});
        WHERE_MAP.put("上行低速率路段",new String[]{"IEValue_54231"});
        WHERE_MAP.put("下行低速率路段",new String[]{"IEValue_53483"});
        EVTS_MAP.put("上行低速率路段",new String[]{"FTP Upload Send STOR","FTP Upload Last Data","FTP UpLoad Drop","FTP Upload Attempt","FTP Upload Success","FTP UpLoad Drop"});
        EVTS_MAP.put("下行低速率路段",new String[]{"FTP DownLoad Drop","FTP Download Send RETR","FTP Download Last Data","FTP Download Attempt","FTP Download Success","FTP Download Drop"});
        START_EVTS_MAP.put("上行低速率路段",new String[]{"FTP Upload Attempt","FTP Upload Send STOR"});
        START_EVTS_MAP.put("下行低速率路段",new String[]{"FTP Download Attempt","FTP Download Send RETR"});
    }
    @Autowired
    private GisAndListShowServie gisAndListShowServie;

    @Autowired
    private InfluxDBMutiConntion influxDBMutiConntion;

    /**
     * 问题路段分析入口
     * @param fileLogIds
     */
    @Override
    public Map<String, List<Map<String, Object>>> analysize(List<String> fileLogIds){
        List<TestLogItem> testLogItems = unicomLogItemService.queryTestLogItems(fileLogIds.stream().collect(Collectors.joining(",")));
        Map<Long, TestLogItem> id2LogBeanMap = testLogItems.stream().collect(Collectors.toMap(TestLogItem::getRecSeqNo, Function.identity()));
        List<QuesRoadThreshold> quesRoadThresholds = quesRoadDao.queryAll();
        Map<String,Double> thresholdMap=quesRoadThresholds.stream().collect(Collectors.toMap(QuesRoadThreshold::getName,QuesRoadThreshold::getValue));
        Map<String, List<Map<String, Object>>> result=Collections.synchronizedMap(new HashMap<>());
        fileLogIds.stream().forEach(id->{
            String sql=MessageFormat.format(BASE_ROAD_SAMP_SQL,InfluxReportUtils.getTableName(Long.parseLong(id),"IE"));
            TestLogItem testLogItem = id2LogBeanMap.get(Long.parseLong(id));
            InfluxDbConnection influxDbConnection = influxDBMutiConntion.getConn(testLogItem.getCity(), testLogItem.getUrl(), testLogItem.getDbName());
            List<Cell5G> nrCells = gisAndListShowServie.getCellsByRegion(testLogItem.getCity());
            Map<String, List<Cell5G>> nrPciFcn2BeanMap = nrCells.stream().collect(Collectors.groupingBy(item -> item.getPci() + "_" + item.getFrequency1()));
            WHERE_MAP.entrySet().parallelStream().forEach(entry->{
            //WHERE_MAP.entrySet().stream().forEach(entry->{
                String key=entry.getKey();
                String[] values=entry.getValue();
                List<Map<String, Object>> sampDatas;
                //有事件条件的处理
                if(EVTS_MAP.containsKey(key)){
                    List<Map<String, Object>> evtResults = evtPointTimes(Long.parseLong(id), EVTS_MAP.get(key));
                    if(existEvt(evtResults,START_EVTS_MAP.get(key))){
                        List<Map<String,String>> times=getTimeIntervals(evtResults,START_EVTS_MAP.get(key));
                        for(Map<String,String> time:times){
                            sampDatas = queryRoadSampDatas(influxDbConnection,sql, Arrays.asList(time),values);
                            Map<String, List<Map<String, Object>>> tempR=quesRoadAlgorithm(key,sampDatas,thresholdMap,testLogItem,nrPciFcn2BeanMap);
                            if(!tempR.isEmpty()){
                                if(result.containsKey(key)){
                                    result.get(key).addAll(tempR.get(key));
                                }else{
                                    result.putAll(tempR);
                                }
                            }
                        }
                    }
                }else{
                    sampDatas=queryRoadSampDatas(influxDbConnection,sql, Collections.emptyList(),values);
                    //问题路段算法
                    Map<String, List<Map<String, Object>>> tempR=quesRoadAlgorithm(key,sampDatas,thresholdMap,testLogItem,nrPciFcn2BeanMap);
                    if(!tempR.isEmpty()){
                        if(result.containsKey(key)){
                            result.get(key).addAll(tempR.get(key));
                        }else{
                            result.putAll(tempR);
                        }
                    }

                }

            });

        });
        return result;
    }

    /**
     * 问题路段算法
     * @param key
     * @param sampDatas
     * @param thresholdMap
     * @param testLogItem
     * @param nrPciFcn2BeanMap
     * @return
     */
    private Map<String, List<Map<String, Object>>> quesRoadAlgorithm(String key, List<Map<String, Object>> sampDatas, Map<String, Double> thresholdMap, TestLogItem testLogItem, Map<String, List<Cell5G>> nrPciFcn2BeanMap) {
       if(sampDatas==null||sampDatas.isEmpty()){
           return Collections.emptyMap();
       }
        List<int[]> quesRaods=new ArrayList<>();
        if("弱覆盖路段".equalsIgnoreCase(key)){
            Double tlen=thresholdMap.get("weakcoverroadlen");
            Double weakcoversamprate=thresholdMap.get("weakcoversamprate");
            quesRaods=new SlideWindowAlg(getMapPredicate2(thresholdMap, "IEValue_50055", "weakcoverrsrp"),sampDatas,tlen,weakcoversamprate).invoke();
        } else if("上行质差路段".equalsIgnoreCase(key)){
            Double tlen=thresholdMap.get("upqualitydiffroadlen");
            Double weakcoversamprate=thresholdMap.get("upqualitydiffsamprate");
            quesRaods=new SlideWindowAlg(getMapPredicate1(thresholdMap),sampDatas,tlen,weakcoversamprate).invoke();
        }else if("下行质差路段".equalsIgnoreCase(key)){
            Double tlen=thresholdMap.get("downqualitydiffroadlen");
            Double weakcoversamprate=thresholdMap.get("downqualitydiffsamprate");
            quesRaods= new SlideWindowAlg(getMapPredicate(thresholdMap),sampDatas,tlen,weakcoversamprate).invoke();
        }else if("上行低速率路段".equalsIgnoreCase(key)){
            Double tlen=thresholdMap.get("uplowerspeedroadlen");
            Double weakcoversamprate=thresholdMap.get("uplowerspeedsamprate");
            new SlideWindowAlg(getMapPredicate2(thresholdMap, "IEValue_54231", "uplowerspeedrlc"),sampDatas, tlen,weakcoversamprate).invoke();
        }else if("下行低速率路段".equalsIgnoreCase(key)){
            Double tlen=thresholdMap.get("downlowerspeedroadlen");
            Double weakcoversamprate=thresholdMap.get("downlowerspeedsamprate");
            quesRaods=new SlideWindowAlg(getMapPredicate2(thresholdMap, "IEValue_53483", "downlowerspeedrlc"),sampDatas,tlen,weakcoversamprate).invoke();
        }

        Map<String,List<Map<String,Object>>> result=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        int m=0;
        for(int[] item:quesRaods){
            List<Map<String, Object>> maps = sampDatas.subList(item[0], item[1] + 1);
            Map<String,Object> obj=new HashMap<>();
            obj.put("id",m);
            obj.put("logname",testLogItem.getFileName());
            obj.put("area",testLogItem.getCity());
            obj.put("contractor",testLogItem.getContractor());
            obj.put("starttime",DateComputeUtils.formatMicroTime(maps.get(0).get("time").toString()));
            double slong = Double.parseDouble(maps.get(0).get("Long").toString());
            double slat = Double.parseDouble(maps.get(0).get("Lat").toString());
            obj.put("slong", slong);
            obj.put("slat",slat);
            obj.put("endtime",DateComputeUtils.formatMicroTime(maps.get(maps.size()-1).get("time").toString()));
            double elong = Double.parseDouble(maps.get(maps.size()-1).get("Long").toString());
            double elat = Double.parseDouble(maps.get(maps.size()-1).get("Lat").toString());
            obj.put("elong",elong);
            obj.put("elat",elat);
            obj.put("totalLen",getSumKm(maps));
            Map<String,Integer> pciCountMap=new HashMap<>();
            maps.stream().filter(j->j.get("IEValue_50007")!=null).collect(Collectors.groupingBy(j -> j.get("IEValue_50007").toString())).forEach((a,b)->{
                pciCountMap.put(a,b.size());
            });
            List<Map.Entry<String, Integer>> collect = pciCountMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
            String pci1=collect.size()>0?collect.get(0).getKey():null;
            obj.put("pci1",pci1);
            obj.put("pci1count",collect.size()>0?collect.get(0).getValue():null);
            obj.put("pci2",collect.size()>1?collect.get(1).getKey():null);
            obj.put("pci2count",collect.size()>1?collect.get(1).getValue():null);
            obj.put("pci3",collect.size()>2?collect.get(2).getKey():null);
            obj.put("pci3count",collect.size()>2?collect.get(2).getValue():null);
            obj.put("sumrsrp",InfluxReportUtils.getSumKpi2(maps,"IEValue_50055"));
            obj.put("avgrsrp",InfluxReportUtils.getAvgKpi2(maps,"IEValue_50055"));
            obj.put("countrsrp",InfluxReportUtils.getCountKpi2(maps,"IEValue_50055"));
            obj.put("sumsinr",InfluxReportUtils.getSumKpi2(maps,"IEValue_50056"));
            obj.put("countsinr",InfluxReportUtils.getCountKpi2(maps,"IEValue_50056"));
            obj.put("avgsinr",InfluxReportUtils.getAvgKpi2(maps,"IEValue_50056"));
            obj.put("sumdistance",InfluxReportUtils.getSumKpi2(maps,"IEValue_50014"));
            obj.put("countdistance",InfluxReportUtils.getCountKpi2(maps,"IEValue_50014"));
            obj.put("maxdistance",InfluxReportUtils.getMaxKpi(maps,"IEValue_50014"));
            obj.put("mindistance",InfluxReportUtils.getMinKpi(maps,"IEValue_50014"));
            obj.put("avgdistance",InfluxReportUtils.getAvgKpi2(maps,"IEValue_50014"));

            Map<String,Integer> arfcnCountMap=new HashMap<>();
            maps.stream().filter(i->i.get("IEValue_53601")!=null).collect(Collectors.groupingBy(i -> i.get("IEValue_53601").toString())).forEach((a,b)->{
                arfcnCountMap.put(a,b.size());
            });
            List<Map.Entry<String, Integer>> collect1 = pciCountMap.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
            String arfcn1=collect1.size()>0?collect1.get(0).getKey():null;
            obj.put("arfcn1",arfcn1);
            obj.put("arfcn1count",collect1.size()>0?collect1.get(0).getValue():null);
            obj.put("arfcn2",collect1.size()>1?collect1.get(1).getKey():null);
            obj.put("arfcn2count",collect1.size()>1?collect1.get(1).getValue():null);
            obj.put("arfcn3",collect1.size()>2?collect1.get(2).getKey():null);
            obj.put("arfcn3count",collect1.size()>2?collect1.get(2).getValue():null);
            List<Cell5G> cell5GS = nrPciFcn2BeanMap.get(pci1 +  "_" + arfcn1);
            if(cell5GS!=null&&!cell5GS.isEmpty()){
                Cell5G cell = InfluxReportUtils.getCell((slat + elat) / 2, (slong + elong) / 2, cell5GS);
                obj.put("gnodebid1",cell!=null?cell.getgNBId():null);
                obj.put("sectorid1",cell!=null?cell.getLocalCellId():null);
            }
            obj.put("avgdlinitbler",InfluxReportUtils.getAvgKpi2(maps,"IEValue_73000"));
            obj.put("sumdlinitbler",InfluxReportUtils.getSumKpi2(maps,"IEValue_73000"));
            obj.put("countdlinitbler",InfluxReportUtils.getCountKpi2(maps,"IEValue_73000"));

            obj.put("avgdlresibler",InfluxReportUtils.getAvgKpi2(maps,"IEValue_73001"));
            obj.put("sumdlresibler",InfluxReportUtils.getSumKpi2(maps,"IEValue_73001"));
            obj.put("countdlresibler",InfluxReportUtils.getCountKpi2(maps,"IEValue_73001"));

            obj.put("avgdtxpower",InfluxReportUtils.getAvgKpi2(maps,"IEValue_50097"));
            obj.put("sumdtxpower",InfluxReportUtils.getSumKpi2(maps,"IEValue_50097"));
            obj.put("counttxpower",InfluxReportUtils.getCountKpi2(maps,"IEValue_50097"));

            obj.put("avgulinitbler",InfluxReportUtils.getAvgKpi2(maps,"IEValue_54572"));
            obj.put("sumulinitbler",InfluxReportUtils.getSumKpi2(maps,"IEValue_54572"));
            obj.put("countulinitbler",InfluxReportUtils.getCountKpi2(maps,"IEValue_54572"));

            obj.put("avgulresibler",InfluxReportUtils.getAvgKpi2(maps,"IEValue_71000"));
            obj.put("sumulresibler",InfluxReportUtils.getSumKpi2(maps,"IEValue_71000"));
            obj.put("countulresibler",InfluxReportUtils.getCountKpi2(maps,"IEValue_71000"));

            obj.put("sum53483",InfluxReportUtils.getSumKpi2(maps,"IEValue_53483"));
            obj.put("avg53483",InfluxReportUtils.getAvgKpi2(maps,"IEValue_53483"));
            Long ieValue_53483count = InfluxReportUtils.getCountKpi2(maps, "IEValue_53483");
            obj.put("count53483", ieValue_53483count);
            
            Predicate<Map<String,Object>> predicate=stringObjectMap->Double.parseDouble(stringObjectMap.get("IEValue_53483").toString())<=100;
            Long ieValue_53483lt100count = InfluxReportUtils.getCountFilterKpi2(maps, "IEValue_53483", predicate);
            obj.put("count53483lt100", ieValue_53483lt100count);
            obj.put("rate53483lt100",InfluxReportUtils.rate(ieValue_53483count,ieValue_53483lt100count));

            obj.put("sum50990",InfluxReportUtils.getSumKpi2(maps,"IEValue_50990"));
            obj.put("avg53419",InfluxReportUtils.getAvgKpi2(maps,"IEValue_53419"));
            obj.put("sum53419",InfluxReportUtils.getSumKpi2(maps,"IEValue_53419"));
            obj.put("count53419",InfluxReportUtils.getCountKpi2(maps,"IEValue_53419"));
            obj.put("sum51192",InfluxReportUtils.getSumKpi2(maps,"IEValue_51192"));
            obj.put("count51192",InfluxReportUtils.getCountKpi2(maps,"IEValue_51192"));
            obj.put("avg51192",InfluxReportUtils.getAvgKpi2(maps,"IEValue_51192"));
            obj.put("sum74214",InfluxReportUtils.getSumKpi2(maps,"IEValue_74214"));
            obj.put("avg74214",InfluxReportUtils.getAvgKpi2(maps,"IEValue_74214"));
            obj.put("count74214",InfluxReportUtils.getCountKpi2(maps,"IEValue_74214"));
            obj.put("sum53682",InfluxReportUtils.getSumKpi2(maps,"IEValue_53682"));
            obj.put("avg53682",InfluxReportUtils.getAvgKpi2(maps,"IEValue_53682"));
            obj.put("count53682",InfluxReportUtils.getCountKpi2(maps,"IEValue_53682"));
            obj.put("sum53456",InfluxReportUtils.getSumKpi2(maps,"IEValue_53456"));
            obj.put("count53456",InfluxReportUtils.getCountKpi2(maps,"IEValue_53456"));
            String sumieValue_53434 = InfluxReportUtils.getSumKpi2(maps, "IEValue_53434");
            String sumieValue_53433 = InfluxReportUtils.getSumKpi2(maps, "IEValue_53433");
            String sumieValue_53432 = InfluxReportUtils.getSumKpi2(maps, "IEValue_53432");
            String sumieValue_53431 = InfluxReportUtils.getSumKpi2(maps, "IEValue_53431");
            obj.put("sum53434", sumieValue_53434);
            obj.put("sum53433", sumieValue_53433);
            obj.put("sum53432", sumieValue_53432);
            obj.put("sum53431", sumieValue_53431);
            Double sum=InfluxReportUtils.add(sumieValue_53434, sumieValue_53433,
                    sumieValue_53432, sumieValue_53431);
            obj.put("53434rate",sumieValue_53434!=null?Double.parseDouble(sumieValue_53434)/sum:null);
            obj.put("53433rate",sumieValue_53433!=null?Double.parseDouble(sumieValue_53433)/sum:null);
            obj.put("53432rate",sumieValue_53432!=null?Double.parseDouble(sumieValue_53432)/sum:null);
            obj.put("53431rate",sumieValue_53431!=null?Double.parseDouble(sumieValue_53431)/sum:null);
            obj.put("sum50087",InfluxReportUtils.getSumKpi2(maps,"IEValue_50087"));
            obj.put("avg50087",InfluxReportUtils.getAvgKpi2(maps,"IEValue_50087"));
            obj.put("count50087",InfluxReportUtils.getCountKpi2(maps,"IEValue_50087"));
            obj.put("sum54231",InfluxReportUtils.getSumKpi2(maps,"IEValue_54231"));
            Long ieValue_54231count = InfluxReportUtils.getCountKpi2(maps, "IEValue_54231");
            obj.put("count54231", ieValue_54231count);
            obj.put("avg54231",InfluxReportUtils.getAvgKpi2(maps,"IEValue_54231"));
            Predicate<Map<String,Object>> predicate1=stringObjectMap->Double.parseDouble(stringObjectMap.get("IEValue_54231").toString())<5;
            Long ieValue_54231lt5count = InfluxReportUtils.getCountFilterKpi2(maps, "IEValue_54231", predicate1);
            obj.put("count54231lt5", ieValue_54231lt5count);
            obj.put("rate54231lt5",InfluxReportUtils.rate(ieValue_54231count,ieValue_54231lt5count));
            obj.put("sum50991",InfluxReportUtils.getSumKpi2(maps,"IEValue_50991"));
            obj.put("sum73100",InfluxReportUtils.getSumKpi2(maps,"IEValue_73100"));
            obj.put("avg73100",InfluxReportUtils.getAvgKpi2(maps,"IEValue_73100"));
            obj.put("count73100",InfluxReportUtils.getCountKpi2(maps,"IEValue_73100"));
            obj.put("sum51193",InfluxReportUtils.getSumKpi2(maps,"IEValue_51193"));
            obj.put("avg51193",InfluxReportUtils.getAvgKpi2(maps,"IEValue_51193"));
            obj.put("count51193",InfluxReportUtils.getCountKpi2(maps,"IEValue_51193"));
            obj.put("sum53457",InfluxReportUtils.getSumKpi2(maps,"IEValue_53457"));
            obj.put("count53457",InfluxReportUtils.getCountKpi2(maps,"IEValue_53457"));
            String sumIEValue_71054 = InfluxReportUtils.getSumKpi2(maps, "IEValue_71054");
            String sumIEValue_71053 = InfluxReportUtils.getSumKpi2(maps, "IEValue_71053");
            String sumIEValue_71052 = InfluxReportUtils.getSumKpi2(maps, "IEValue_71052");
            String sumIEValue_71051 = InfluxReportUtils.getSumKpi2(maps, "IEValue_71051");
            obj.put("sum71054", sumIEValue_71054);
            obj.put("sum71053", sumIEValue_71053);
            obj.put("sum71052", sumIEValue_71052);
            obj.put("sum71051", sumIEValue_71051);
            Double sum1=InfluxReportUtils.add(sumIEValue_71054, sumIEValue_71053,
                    sumIEValue_71052, sumIEValue_71051);
            obj.put("71054rate",sumIEValue_71054!=null?Double.parseDouble(sumIEValue_71054)/sum1:null);
            obj.put("71053rate",sumIEValue_71053!=null?Double.parseDouble(sumIEValue_71053)/sum1:null);
            obj.put("71052rate",sumIEValue_71052!=null?Double.parseDouble(sumIEValue_71052)/sum1:null);
            obj.put("71051rate",sumIEValue_71051!=null?Double.parseDouble(sumIEValue_71051)/sum1:null);
            list.add(obj);
            m++;
        }
        result.put(key,list);
        return result;
    }

    private Object getSumKm(List<Map<String,Object>> maps) {
        Double sumkm=0.0;
        for(int i=0;i<maps.size()-1;i++){
            double slong = Double.parseDouble(maps.get(i).get("Long").toString());
            double slat = Double.parseDouble(maps.get(i).get("Lat").toString());
            double elong = Double.parseDouble(maps.get(i+1).get("Long").toString());
            double elat = Double.parseDouble(maps.get(i+1).get("Lat").toString());
            sumkm+=AdjPlaneArithmetic.getDistance(slong, slat, elong, elat);
        }
        return sumkm;
    }

    private Predicate<Map<String, Object>> getMapPredicate2(Map<String, Double> thresholdMap, String ieValue_50055, String weakcoverrsrp) {
        return item -> null!=item.get(ieValue_50055)&&Double.parseDouble(item.get(ieValue_50055).toString()) <= thresholdMap.get(weakcoverrsrp);
    }

    private Predicate<Map<String, Object>> getMapPredicate1(Map<String, Double> thresholdMap) {
        return item->null!=item.get("IEValue_50097")&&null!=item.get("IEValue_54333")&&Double.parseDouble(item.get("IEValue_50097").toString())>=thresholdMap.get("upqualitydifftxpower")&&Double.parseDouble(item.get("IEValue_54333").toString())>=thresholdMap.get("upqualitydiffbler");
    }

    private Predicate<Map<String, Object>> getMapPredicate(Map<String, Double> thresholdMap) {
        return item->null!=item.get("IEValue_50056")&&null!=item.get("IEValue_50055")&&Double.parseDouble(item.get("IEValue_50055").toString())<=thresholdMap.get("downqualitydiffrsrp")&&Double.parseDouble(item.get("IEValue_50056").toString())<=thresholdMap.get("downqualitydiffsinr");
    }

    /**
     * 按照开始事件 切分时间段
     * @param evtResults
     * @param startEvts
     * @return
     */
    private List<Map<String,String>> getTimeIntervals(List<Map<String, Object>> evtResults, String[] startEvts) {
        List<Map<String,String>> r=new ArrayList<>();
        String startTime;
        String endTime;
        List<String> evts = Arrays.asList(startEvts);
        for(int i=0;i<evtResults.size();i++){
            if(evts.contains(evtResults.get(i).get("evtName").toString())){
                Map<String,String> item=new HashMap<>();
                startTime=evtResults.get(i).get("time").toString();
                item.put("startTime",startTime);
                if(evtResults.size()>i+1){
                    Map<String, Object> map1 = evtResults.get(i + 1);
                    if(evts.contains(map1.get("evtName").toString())){
                        continue;
                    }else{
                        endTime=map1.get("time").toString();
                        item.put("endTime",endTime);
                    }
                }
                r.add(item);
            }
        }
        return r;
    }

    /*public static void main(String[] args) {
        String s="AAAAAAA";
        List<String> r=new ArrayList<>();
        for(int i=0;i<s.length();i++){
            char m=s.charAt(i);
            StringBuilder sb=new StringBuilder();
            if('A'==m){
                sb.append(m);
                if(s.length()>i+1){
                    char n = s.charAt(i+1);
                    if('A'==n){
                        continue;
                    }else{
                        sb.append(n);
                    }
                }
                r.add(sb.toString());
            }
        }
        r.forEach(item->{
            System.out.println(item);
        });
    }*/




    /**
     * 是否存在事件
     * @param evtResults
     * @param evtName
     * @return
     */
    private boolean existEvt(List<Map<String, Object>> evtResults,String[] evtNames){
        for(Map<String, Object> item:evtResults){
            if(Arrays.asList(evtNames).contains(item.get("evtName").toString())){
                return true;
            }
        }
        return false;
    }

    private class SlideWindowAlg {
        private List<Map<String, Object>> sampDatas;
        private Double roadThresold;
        private Double rateThreshold;
        private Predicate<Map<String,Object>> mapPredicate1;

        public SlideWindowAlg(Predicate<Map<String,Object>> mapPredicate1, List<Map<String, Object>> sampDatas,Double roadThresold,Double rateThreshold) {
            this.sampDatas = sampDatas;
            this.roadThresold = roadThresold;
            this.rateThreshold = rateThreshold;
            this.mapPredicate1=mapPredicate1;
        }
        public List<int[]> invoke() {
            List<int[]> quesRaods=new ArrayList<>();
            Map<String, Object> point = sampDatas.get(0);
            Double lon=Double.parseDouble(point.get("Long").toString());
            Double lat=Double.parseDouble(point.get("Lat").toString());
            List<Map<String, Object>> slideWindow=new LinkedList<>();
            int start=0,end=0;//问题路段的起点和终点index
            boolean flag=false;//控制问题路段是否开始
            boolean flag1=false;//滑窗长度是否固定
            for(int i=0;i<sampDatas.size();i++){
                Map<String, Object> point2 = sampDatas.get(i);
                Double lon2=Double.parseDouble(point2.get("Long").toString());
                Double lat2=Double.parseDouble(point2.get("Lat").toString());
                if(!flag1){
                    slideWindow.add(sampDatas.get(i));
                    boolean f=isIntervalOver(slideWindow);
                    if(f){
                        slideWindow.clear();
                        slideWindow.add(sampDatas.get(i));
                        start=i;
                        end=i;
                        lon=lon2;
                        lat=lat2;
                        flag=false;
                        flag1=false;
                        continue;
                    }
                    Double distance = AdjPlaneArithmetic.getDistance(lon, lat, lon2, lat2);
                    if(distance>=roadThresold){
                        flag1=true;
                        Double rate=slideWindow.stream().filter(mapPredicate1).count()*1.0/slideWindow.size();
                        if(rate*100>rateThreshold){
                            if(!flag){
                                start=i-slideWindow.size()+1;
                            }
                            //滑窗滑到采样点最后一个满足问题路段开始条件
                            if(end==sampDatas.size()-1){
                                quesRaods.add(new int[]{start,end});
                            }
                            end++;
                            flag=true;
                        }
                    }else{
                        end++;
                    }
                }else{
                    slideWindow.remove(0);
                    slideWindow.add(sampDatas.get(i));
                    Double rate=slideWindow.stream().filter(mapPredicate1).count()*1.0/slideWindow.size();
                    if(rate*100>rateThreshold){
                        if(!flag){
                            start=i-slideWindow.size()+1;
                        }
                        //滑窗滑到采样点最后一个满足问题路段开始条件
                        if(end==sampDatas.size()-1){
                            quesRaods.add(new int[]{start,end});
                        }
                        flag=true;
                    }else if(rate*100<rateThreshold-20){
                        if(flag){
                            quesRaods.add(new int[]{start,end});
                            slideWindow.clear();
                            slideWindow.add(sampDatas.get(i));
                            start=i;
                            end=i;
                            lon=lon2;
                            lat=lat2;
                            flag=false;
                            flag1=false;
                            continue;
                        }
                    }
                    if(!flag){
                        boolean f=isIntervalOver(slideWindow);
                        if(f){
                            slideWindow.clear();
                            slideWindow.add(sampDatas.get(i));
                            start=i;
                            end=i;
                            lon=lon2;
                            lat=lat2;
                            flag=false;
                            flag1=false;
                            continue;
                        }
                        start++;
                    }else{
                        boolean f=isIntervalOver(slideWindow);
                        if(f){
                            slideWindow.clear();
                            slideWindow.add(sampDatas.get(i));
                            quesRaods.add(new int[]{start,end});
                            start=i;
                            end=i;
                            flag=false;
                            flag1=false;
                            lon=lon2;
                            lat=lat2;
                            continue;
                        }
                    }
                    end++;
                }
            }
            return quesRaods;
        }

        private boolean isIntervalOver(List<Map<String,Object>> slideWindow) {
            if(slideWindow.size()<=1){
                return false;
            }
            String time1=slideWindow.get(slideWindow.size()-1).get("time").toString();
            String time2=slideWindow.get(slideWindow.size()-2).get("time").toString();
            if(DateComputeUtils.toUtcDate(time1).getTimeInMillis()-5000>DateComputeUtils.toUtcDate(time2).getTimeInMillis()){
                return true;
            }
            return false;
        }

    }
}
