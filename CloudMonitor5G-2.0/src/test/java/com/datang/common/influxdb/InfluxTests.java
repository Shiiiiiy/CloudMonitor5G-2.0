package com.datang.common.influxdb;

import com.datang.dao.knowfeeling.KnowFeelingDao;
import com.datang.dao.testLogItem.UnicomLogItemDao;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.testLogItem.IEItem;
import com.datang.service.influx.InfluxService;
import com.datang.service.influx.QuesRoadProcessor;
import com.datang.service.service5g.logbackplay.LogIEService;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
//@Transactional
public class InfluxTests {
    @Autowired
    private InfluxService influxService;
    @Autowired
    private CQTTaskOrderService cqtTaskOrderService;
    @Autowired
    private LogIEService logIEService;
    @Autowired
    private QuesRoadProcessor quesRoadProcessor;
    @Autowired
    private UnicomLogItemDao unicomLogItemDao;

    @Autowired
    private KnowFeelingDao knowFeelingDao;

    @Test
    public void myTest(){

        List<String> a = new ArrayList<>();


        String[] a1 = new String[]{};


        final List list = knowFeelingDao.queryKnowFeelingStatics("海南省_海口市_1.1联通卡5G网络DT室外下行业务测试_电信_华为_大唐_华为P40_杨玉杰_20210518084108_1.CU");


        System.out.printf("1");
    }


    public void test(String... a){

    }


    @Test
    public void getLogPlayDatas(){
        List<IEItem> recrods= logIEService.getRecrodsByLogId(41075l);
        System.out.println(recrods.size());
        List<Map<String, Object>> maps = logIEService.evtWindowData(41075l, null, null);
        List<Map<String, Object>> maps2 = logIEService.sigleWindowData(41075l, null, null);
        List<Map<String, Object>> maps3 = logIEService.lineChartDatas(41075l, "2021-06-05 08:13:35.644", "2021-06-05 09:13:35.644");
        List<Map<String, Object>> maps4 = logIEService.synOper(41075l, "2021-06-05 08:13:35.644");
        System.out.println(recrods.size());
        System.out.println(maps2.size());
        System.out.println(maps3.size());
        System.out.println(maps4.size());
        System.out.println(maps.size());
    }
    @Test
    public void testQuesroad(){
        Map<String, List<Map<String, Object>>> analysize = quesRoadProcessor.analysize(Arrays.asList("41320"

        ));
        System.out.println(analysize.size());

    }

    public void getDatas(){
        List<String> fileNames=new ArrayList<>();
        fileNames.add("none28");
        List<Map<String, Object>> mapTrailByLogFiles = influxService.getMapTrailByLogFiles(fileNames);
        System.out.println(mapTrailByLogFiles.size());
    }
    @Test
    public void test1(){
        List<PlanParamPojo> nrCell = cqtTaskOrderService.getNrCell("贵阳");
        System.out.println(nrCell.size());

    }

    @Test
    public void test2(){
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(100,TimeUnit.SECONDS);
        String sql="SELECT * from (SELECT count(Lat) from EVT GROUP BY evtName);";
        InfluxDB connect = InfluxDBFactory.connect("http://192.168.0.42:8086", "admin", "admin",client);
        connect.setDatabase("Task_2306");
        QueryResult query=connect.query(new Query(sql, "Task_2306"));
        List<Map<String, Object>> result = InfludbUtil.paraseQueryResult(query);
        System.out.println(result.size());
    }
    @Test
    public void test4(){
        List<String> fileNames=new ArrayList<>();
        //fileNames.addAll(Arrays.asList("40543","41228","40476"
        fileNames.addAll(Arrays.asList("41360"
        ));
        /*List<Map<String, Object>> eventByLogFiles = influxService.getEventByLogFiles(fileNames);
        System.out.println(eventByLogFiles.size());*/
        /*List<Map<String, Object>> abEvtAnaList = influxService.getAbEvtAnaList(fileNames);
        System.out.println(abEvtAnaList);*/

       /* List<Map<String, Object>> mapTrailByLogFiles = influxService.getMapTrailByLogFiles(fileNames);
        System.out.println(mapTrailByLogFiles.size());*/
        /*List<Map<String, Object>> sampPointByLogFiles = influxService.getSampPointByLogFiles(fileNames);
        System.out.println(sampPointByLogFiles.size());*/
        List<Map.Entry<String, List<Map<String, Object>>>> gridDatasByLogFiles = influxService.getGridDatasByLogFiles(fileNames);
        System.out.println(gridDatasByLogFiles.size());

        /*Map<String, List<Map<String, Object>>> netConfigReports = influxService.getNetConfigReports(fileNames);
        System.out.println(netConfigReports);

        List<Map<String, Object>> reportCellKpi = influxService.getVoiceBusiReports(fileNames);
        System.out.println(reportCellKpi.size());*/

       /* List<Map<String, Object>> reportCellKpi1 = influxService.getReportCellKpi(fileNames);
        System.out.println(reportCellKpi1.size());*/
    }


}
