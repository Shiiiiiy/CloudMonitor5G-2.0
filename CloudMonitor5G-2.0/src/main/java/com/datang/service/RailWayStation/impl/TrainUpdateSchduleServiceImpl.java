package com.datang.service.RailWayStation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datang.bean.railway.Node;
import com.datang.bean.railway.TrainInfoNew;
import com.datang.constant.RailWayHttpConstants;
import com.datang.dao.railway.TrainDao;
import com.datang.dao.railway.TrainFahrplanDao;
import com.datang.dao.railway.TrainStationDao;
import com.datang.domain.railway.TrainFahrplanPojo;
import com.datang.domain.railway.TrainPojo;
import com.datang.domain.railway.TrainStationPojo;
import com.datang.service.RailWayStation.TrainUpdateSchduleService;
import com.datang.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 10:15
 * @Version 1.0
 **/
@Slf4j
@Service
public class TrainUpdateSchduleServiceImpl implements TrainUpdateSchduleService {

    @Autowired
    private TrainStationDao trainStationDao;
    @Autowired
    private TrainDao trainDao;
    @Autowired
    private TrainFahrplanDao trainFahrplanDao;

    @Override
    @Transactional
    public void analysisStationInfo(){
        try{
            //获取全路客运车站信息
            String result = HttpClientUtils.httpGet(RailWayHttpConstants.TRAIN_12306_STATION_JS);
            if(StringUtils.isBlank(result)){
                log.error("获取全路客运车站信息失败");
                return;
            }
            if(result.indexOf(RailWayHttpConstants.TRAIN_STATION_JS_PREFIX_CHARACTER) == -1){
                log.error("解析失败，请确认是否官网改版");
                return;
            }
            // result.length() -1 去掉后面分号
            result = result.trim().replaceAll(RailWayHttpConstants.TRAIN_STATION_JS_REGEX_TRIM,"");
            String str = result.substring(RailWayHttpConstants.TRAIN_STATION_JS_PREFIX_CHARACTER.length(), result.length() - 1);
            String[] stations = str.split(RailWayHttpConstants.TRAIN_STATION_JS_REGEX_SPLIT);
            log.info("全路客运车站信息总记录数：{}", stations.length - 1);
            List<TrainStationPojo> list = new ArrayList<TrainStationPojo>();
            for (String station: stations) {
                //过滤站点切割为空的数据
                if(StringUtils.isNotBlank(station)){
                    //根据格式解析以下存储：bjb|北京北|VAP|beijingbei|bjb|0
                    String[] array = station.split("\\|");
                    if(array.length < 6){
                        //检测格式和长度不对，直接终止
                        log.error("全路客运车站信息解析格式长度错误：{}" + station);
                        break;
                    }
                    TrainStationPojo trainStation = new TrainStationPojo();
                    trainStation.setId(Long.valueOf(array[5])); //ID
                    trainStation.setTelegraphCode(array[2]); //电报码
                    trainStation.setStationName(array[1]); //站名
                    trainStation.setSpell(array[3]); //拼音
                    trainStation.setInitial(array[0]); //首字母
                    trainStation.setPinyinCode(array[4]); //拼音码
                    list.add(trainStation);
//                log.info("正在添加站点 - {}", trainStation.getStationName());
                }
            }
            //遍历插入或更新车站信息
            if(list.size() >0){
                for (TrainStationPojo trainStation : list) {
                    //通过name字段检测出现重复站点名称不会插入，避免数据不完整插入改由id字段检测
                    //TrainStation station = trainStationDao.findStationByName(trainStation.getStationName());
                    TrainStationPojo station = trainStationDao.find(trainStation.getId());
                    if(station == null){
//                    log.info("新增全路客运车站信息：{}", JSON.toJSONString(trainStation));
                        trainStation.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
                        trainStationDao.create(trainStation);
                    }else{
//                    log.info("更新全路客运车站信息：{}", JSON.toJSONString(trainStation));
                        trainStation.setCreateTime(station.getCreateTime());
                        trainStation.setUpdateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
                        trainStationDao.update(trainStation);
                    }
                }
            }
            log.info("全路客运车站信息解析实际插入记录数：{}", list.size());
        }catch (Exception e){
            log.error("解析全路客运车站信息异常：{}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void syncTrainListByStation() {
        try{
            String runDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            List<TrainStationPojo> list = (List<TrainStationPojo>) trainStationDao.findAll();
            List<TrainPojo> trainList = new ArrayList<>();
            Set<String> trainCodeList = new HashSet<>();
            for (TrainStationPojo station: list) {
                StringBuffer uri = new StringBuffer(RailWayHttpConstants.TRAIN_12306_QUERY_LIST);
                uri.append("?train_start_date=").append(runDate);//当前时间
                uri.append("&train_station_code=").append(station.getTelegraphCode());
                log.info("根据车站获取所有在该站办客的车次请求地址：{}", uri.toString());
                //根据车站获取所有在该站办客的车次
                String resultString = HttpClientUtils.httpGet(uri.toString());
                if(StringUtils.isBlank(resultString)){
                    log.error("获取车站办客的车次响应失败：车站 - {}，电报码 - {}", station.getStationName(), station.getTelegraphCode());
                    continue;
                }
                JSONObject result = JSON.parseObject(resultString);
                if(result.getIntValue("httpstatus") != 200){
                    log.error("获取车站办客的车次数据失败：车站 - {}，电报码 - {}", station.getStationName(), station.getTelegraphCode());
                    continue;
                }
                JSONObject pdata = result.getJSONObject("data");
                if(pdata.isEmpty()){
                    log.error("获取车站办客的车次数据失败：车站 - {}，电报码 - {}", station.getStationName(), station.getTelegraphCode());
                    continue;
                }
                JSONArray items = pdata.getJSONArray("data");
                if(!pdata.getBoolean("flag") || items.size() <= 0){
                    log.error("获取车站办客的车次无数据，请核查电报码是否正确：车站 - {}，电报码 - {}", station.getStationName(), station.getTelegraphCode());
                    continue;
                }
                for (Object item: items) {
                    JSONObject data = (JSONObject) item;
                    //以下data参数可以根据实际需要的字段取值
                    String trainCode = data.getString("station_train_code");
                    if(!trainCodeList.contains(trainCode)){
                        String startStationName = data.getString("start_station_name");
                        String endStationName = data.getString("end_station_name");
                        String trainNo = data.getString("train_no");
                        String trainClassCode = data.getString("train_class_code");
                        String trainClassName = data.getString("train_class_name");
                        TrainPojo train = new TrainPojo();
                        train.setTrainCode(trainCode); //车次',
                        train.setStartStationName(startStationName); //始发站',
                        train.setEndStationName(endStationName); //终点站',
                        train.setTrainNo(trainNo); //车次编码',
                        train.setRunDate(runDate); //运行时间',
                        train.setTrainClassCode(trainClassCode); //车次类型号
                        train.setTrainClassName(trainClassName); //车次类型
                        trainCodeList.add(trainCode);
                        trainList.add(train);
                    }
                }
            }

            for (TrainPojo curTrain: trainList) {
                TrainPojo train = trainDao.findTrainByCode(curTrain.getTrainCode());
                if(train == null){
                    curTrain.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
                    trainDao.create(curTrain);
                    saveOrUpdateFahraplan(curTrain);
                }else{
                    curTrain.setId(train.getId());
                    curTrain.setCreateTime(train.getCreateTime());
                    curTrain.setUpdateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));;
                    trainDao.update(curTrain);
                }
            }
            log.info("获取车站办客的车次完成");
        }catch (Exception e){
            log.error("获取车站办客的车次异常：{}", e.getMessage());
            throw e;
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void saveOrUpdateFahraplan(TrainPojo train){
        TrainFahrplanPojo trainFahrplan = trainFahrplanDao.findFahrplanByCode(train.getTrainCode());
        if(trainFahrplan == null){
            //新增时刻表
            trainFahrplan = new TrainFahrplanPojo();
            trainFahrplan.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));;
            trainFahrplan.setTrainCode(train.getTrainCode());
            trainFahrplan.setTrainNo(train.getTrainNo());
            trainFahrplanDao.create(trainFahrplan);
        }else{
            //更新时刻表
            trainFahrplan.setTrainCode(train.getTrainCode());
            trainFahrplan.setTrainNo(train.getTrainNo());
            trainFahrplanDao.update(trainFahrplan);
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void autoSyncTrainFahrplan() {
        try{
            //所有车次数据遍历（过滤冻结的车次）
            List<TrainFahrplanPojo> list =  (List<TrainFahrplanPojo>)trainFahrplanDao.findAll();
            for (TrainFahrplanPojo fahrplan : list) {
                TrainFahrplanPojo trainFahrplan = syncTrainFahrplan(fahrplan.getTrainCode());
                if(trainFahrplan!=null && StringUtils.isNotBlank(trainFahrplan.getTrainInfoNew()) && !trainFahrplan.getTrainInfoNew().equals("{}")){
                    fahrplan.setUpdateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));;
                    fahrplan.setTrainInfoNew(trainFahrplan.getTrainInfoNew());
                    fahrplan.setTrainInfos(trainFahrplan.getTrainInfos());
                    trainFahrplanDao.update(fahrplan);
                }
            }
        }catch (Exception e){
            log.error("自动同步车次时刻信息数据异常：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public TrainFahrplanPojo syncTrainFahrplan(String trainCode) {
        TrainPojo train = trainDao.findTrainByCode(trainCode);
        TrainFahrplanPojo trainFahrplan = new TrainFahrplanPojo();
        if(train == null){
            //如果没有更新到车次表，直接爬取携程数据
//            trainCtripCrawler.getCtripTrainInfo(trainFahrplan, trainCode);
            //没有爬取到数据或解析异常直接返回
            return trainFahrplan;
        }
        String startStationName = train.getStartStationName();
        String endStationName = train.getEndStationName();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TrainStationPojo fromTrainStationPojo = trainStationDao.findTelegraphCodeByName(startStationName);
        String fromCode = fromTrainStationPojo.getTelegraphCode();
        if(fromCode == null){
            fromCode = "";
        }
        TrainStationPojo endTrainStationPojo = trainStationDao.findTelegraphCodeByName(endStationName);
        String endCode = endTrainStationPojo.getTelegraphCode();
        if(endCode == null){
            endCode = "";
        }
        String rundate = train.getRunDate();
        if(StringUtils.isNotBlank(rundate)){
            rundate = format.format(cal.getTime());
        }
        //优先去携程网站抓取
        getCtripTrainInfo(trainFahrplan, trainCode);
        if(!validateData(trainFahrplan)){
            //没有抓到直接爬取12306数据
            trainFahrplan = new TrainFahrplanPojo();
            get12306TrainInfo(trainFahrplan, trainCode, train.getTrainNo(), fromCode, endCode, rundate);
//            if(validateData(trainFahrplan)){
//                //没有抓到直接爬取去哪儿数据（已经废弃）
//                trainFahrplan = new TrainFahrplan();
//                trainQunaerCrawler.getQunarTrainInfo(trainFahrplan, trainCode, startStationName, endStationName, rundate);
//            }
        }
        return trainFahrplan;
    }

    private TrainFahrplanPojo getCtripTrainInfo(TrainFahrplanPojo fahrplan, String trainNum){
        Document doc;
        try {
            String uri = RailWayHttpConstants.TRAIN_CTRIP_QUERY_TSEATDETAIL + trainNum;
            log.info("携程官网爬取时刻表请求地址：{}", uri);
            doc = Jsoup.connect(uri).get();
            //获取div > ctl00_MainContentPlaceHolder_pnlResult
            Elements result = doc.select("div#ctl00_MainContentPlaceHolder_pnlResult");
            //获取div中车次信息
            Elements trainTables =  result.select("div.s_bd > table.tb_result");//tb_result
            TrainInfoNew infoNew = new TrainInfoNew();
            //检测列对应值是否正确
            if(trainTables.size()>0){
                Elements trainHeadArray = trainTables.get(1).select("thead").select("tr");
                Elements ths = trainHeadArray.get(0).select("th");
                String arlabl = ths.get(3).text().trim();//进站时间
                String stlabl = ths.get(4).text().trim();//发车时间
                Elements trainDataArray = trainTables.get(1).select("tbody").select("tr");
                Elements trElement = trainTables.get(0).select("tbody").select("tr");
                Elements tainInfo = trElement.get(0).select("td");
                String startStationName = tainInfo.get(1).text().trim();
                String endStationName = tainInfo.get(2).text().trim();
                infoNew.setStartStationName(startStationName);
                infoNew.setEndStationName(endStationName);
                infoNew.setTrainCode(trainNum);
                infoNew.setTrainLength(String.valueOf(trainDataArray.size()));
                ArrayList<Node> nodeList=new ArrayList<Node>();
                infoNew.setItems(nodeList);
                List<String> trainInfoList = new ArrayList<>();
                for(int i = 0;i<trainDataArray.size();i++){
                    Elements tds = trainDataArray.get(i).select("td");
                    Node node = new Node();
                    int num = (i + 1);
                    String serialNo  = num >= 10 ? String.valueOf(num) : "0"+num;
                    node.setStationNo(serialNo);
                    for(int j = 0;j<tds.size();j++){
                        String text = tds.get(j).text().trim();
                        switch (String.valueOf(j)) {
                            case "2":
                                //站名
                                node.setStationName(text);
                                trainInfoList.add(text);
                                break;
                            case "3":
                                //进站时间，防止进站与发车调换情况
                                if(arlabl.equals("发车时间")){
                                    node.setStartTime(text);
                                }else{
                                    node.setArriveTime(text);
                                }
                                break;
                            case "4":
                                //发车时间，防止进站与发车调换情况
                                if(stlabl.equals("进站时间")){
                                    node.setArriveTime(text);
                                }else{
                                    node.setStartTime(text);
                                }
                                break;
                            case "5":
                                //停留时间
                                if("----".equals(text)){
                                    node.setOverTime(text);
                                }else{
                                    node.setOverTime(text+"分钟");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    nodeList.add(node);
                }
                fahrplan.setTrainInfoNew(JSONObject.toJSONString(infoNew));
                fahrplan.setTrainInfos(StringUtils.join(trainInfoList.toArray(), ","));
            }
        } catch (Exception e) {
            log.error("携程 - 解析错误或无法爬取到时刻表数据");
            e.printStackTrace();
        }
        return fahrplan;

    }

    private void get12306TrainInfo(TrainFahrplanPojo fahrplan, String localTrainCode, String trainNo, String fromcode, String tocode, String date){
        JSONArray dataJsonArray = new JSONArray();
        try{
            StringBuffer uri = new StringBuffer(RailWayHttpConstants.TRAIN_12306_QUERY_TRAIN_FAHRPLAN_BY_TRAINNO);
            uri.append("?train_no=").append(trainNo);
            uri.append("&from_station_telecode=").append(fromcode);
            uri.append("&to_station_telecode=").append(tocode);
            uri.append("&depart_date=").append(date);
            log.info("12306 官网时刻表获取请求地址：{}", uri.toString());
            //根据车次编码获取时刻表信息
            String resultString = HttpClientUtils.httpGet(uri.toString());
            if(StringUtils.isNotBlank(resultString)){
                JSONObject result = JSON.parseObject(resultString);
                if(!result.isEmpty() && (result.getIntValue("httpstatus") == 200 && result.containsKey("data"))){
                    JSONObject data = result.getJSONObject("data");
                    if(!data.isEmpty() && data.containsKey("data")){
                        dataJsonArray = data.getJSONArray("data");
                        int size = dataJsonArray.size();
                        //infoNew
                        TrainInfoNew infoNew = new TrainInfoNew();
                        List<String> trainInfoList = new ArrayList<>();
                        if(size>0){
                            ArrayList<Node> nodeList = new ArrayList<Node>();
                            infoNew.setItems(nodeList);
                            infoNew.setTrainLength(String.valueOf(size));
                            for(int index = 1;index <= size; index++){
                                JSONObject json = dataJsonArray.getJSONObject(index-1);
                                Node node = new Node();
                                String startTime = json.getString("start_time");
                                String arriveTime = json.getString("arrive_time");
                                String overTime = json.getString("stopover_time");
                                String stationName = json.getString("station_name");
//	                            String stationNo = json.getString("station_no");
                                if(index == 1){
//                                  String trainClassName = json.getString("train_class_name");
                                    String trainCode = json.getString("station_train_code");
                                    String startStationName = json.getString("start_station_name");
                                    String endStationName = json.getString("end_station_name");

                                    infoNew.setTrainCode(trainCode);
                                    infoNew.setStartStationName(startStationName);
                                    infoNew.setEndStationName(endStationName);
                                    node.setArriveTime("----");
                                    node.setOverTime("----");
                                    node.setStartTime(startTime);
                                }else{
                                    //处理arriveTime
                                    node.setArriveTime(arriveTime);
                                    node.setStartTime(startTime);
                                    node.setOverTime(overTime);
                                }
                                node.setStationName(stationName);
                                trainInfoList.add(stationName);
                                if(index < 10){
                                    node.setStationNo("0"+index);
                                }else{
                                    node.setStationNo(String.valueOf(index));
                                }
                                nodeList.add(node);
                                if(index == size){
                                    node.setOverTime("----");
                                    node.setStartTime(arriveTime);
                                }
                            }
                        }
                        fahrplan.setTrainInfoNew(JSONObject.toJSONString(infoNew));
                        fahrplan.setTrainInfos(StringUtils.join(trainInfoList.toArray(), ","));
                        log.info("12306 - 根据车次编码获取时刻表信息 --> infoNew：{}",JSONObject.toJSONString(infoNew));
                    }
                }
            }
        }catch(Exception ex){
            log.error("12306-解析错误或无法爬取到时刻表数据");
            ex.printStackTrace();
        }
        //避免以上无法获取情况，补偿通过官方时刻表js方式先获取最新的trainNo再次查询
        if(dataJsonArray.isEmpty() || dataJsonArray.size() == 0){
            //调用官网上爬取数据
            log.debug("12306 - 再次进行官网数据获取数据:" + localTrainCode);
            get12306TrainInfo(fahrplan, localTrainCode, date);
        }
    }

    /**
     *  采用通过官网爬取，官网地址：https://kyfw.12306.cn/otn/queryTrainInfo/init（原始12306接口数据2019年12月30之后未更新，去哪儿也查不到数据）
     * @param fahrplan
     * @param localTrainCode
     * @param date
     */
    public void get12306TrainInfo(TrainFahrplanPojo fahrplan, String localTrainCode, String date){

        try{
            //步骤1、新增或更新车次列表信息
            StringBuffer train_uri = new StringBuffer(RailWayHttpConstants.TRAIN_12306_QUERY_TRAINNO);
            train_uri.append("?keyword=").append(localTrainCode);
            train_uri.append("&date=").append(date.replaceAll("-", ""));
            log.info("12306 官网获取车次编码请求地址：{}", train_uri.toString());
            //根据车次编码获取时刻表信息
            String trainResultString = HttpClientUtils.httpGet(train_uri.toString());
            if(StringUtils.isBlank(trainResultString)) {
                return;
            }
            JSONObject trainResult = JSON.parseObject(trainResultString);
            if (trainResult.isEmpty()
                    || !trainResult.containsKey("data")
                    || !trainResult.getBoolean("status")) {
                return;
            }
            JSONArray trainArray = trainResult.getJSONArray("data");
            if(trainArray == null || trainArray.isEmpty()){
                return;
            }
            //该请求是模糊查询，则取第一个值即可
            //{"date":"20200708","from_station":"九江","station_train_code":"G1581","to_station":"常州北","total_num":"14","train_no":"57000G15810C"}
            JSONObject trainDateJson = trainArray.getJSONObject(0);
            TrainPojo train = new TrainPojo();
            train.setTrainCode(trainDateJson.getString("station_train_code"));
            train.setStartStationName(trainDateJson.getString("from_station"));
            train.setEndStationName(trainDateJson.getString("to_station"));
            train.setTrainNo(trainDateJson.getString("train_no"));
            String dateStr = trainDateJson.getString("date");
            if (StringUtils.isNotBlank(dateStr)) {
                dateStr = DateFormatUtils.format(DateUtils.parseDate(dateStr, "yyyyMMdd"), "yyyy-MM-dd");
            }
            train.setRunDate(dateStr);//运行时间
            //更新获取车次
            TrainPojo trainTemp = trainDao.findTrainByCode(train.getTrainCode());
            if(trainTemp == null){
                trainDao.create(train);
                log.info("12306 - 新增车次 code：{},startStation：{},endStation：{}", train.getTrainCode(), train.getStartStationName(),train.getEndStationName() );
            }else{
                trainTemp.setStartStationName(train.getStartStationName());
                trainTemp.setEndStationName(train.getEndStationName());
                trainTemp.setTrainNo(train.getTrainNo());
                trainTemp.setRunDate(train.getRunDate());//运行时间
                trainDao.update(trainTemp);
            }

            //步骤2、根据获取到的车次编码读取车次时刻表信息
            StringBuffer fahrplan_uri = new StringBuffer(RailWayHttpConstants.TRAIN_12306_QUERY_TRAIN_FAHRPLAN);
            fahrplan_uri.append("?leftTicketDTO.train_no=").append(train.getTrainNo());
            fahrplan_uri.append("&leftTicketDTO.train_date=").append(date);
            fahrplan_uri.append("&rand_code=");
            log.info("12306 官网获取时刻表信息请求地址：{}", fahrplan_uri.toString());
            //根据车次编码获取时刻表信息
            String fahrplanResultString = HttpClientUtils.httpGet(fahrplan_uri.toString());
            if(StringUtils.isBlank(fahrplanResultString)) {
                return;
            }
            JSONObject fahrplanResult = JSON.parseObject(fahrplanResultString);
            if (fahrplanResult.isEmpty()
                    || !fahrplanResult.containsKey("data")) {
                return;
            }

            JSONObject fahrplanData = fahrplanResult.getJSONObject("data");
            if (fahrplanData.isEmpty()
                    || !fahrplanData.containsKey("data")) {
                return;
            }
            JSONArray fahrplanArray = fahrplanData.getJSONArray("data");
            if(fahrplanArray == null || fahrplanArray.isEmpty()){
                return;
            }
            int size = fahrplanArray.size();
            //步骤3、解析车次时刻信息
            //infoNew
            TrainInfoNew infoNew = new TrainInfoNew();
            List<String>  trainInfoList = new ArrayList<>();
            ArrayList<Node> nodeList = new ArrayList<Node>();
            infoNew.setItems(nodeList);
            infoNew.setTrainLength(String.valueOf(size));
            for(int index = 1; index <= size; index++){
                JSONObject json = fahrplanArray.getJSONObject(index-1);
                Node node = new Node();
                String startTime = json.getString("start_time");
                String arriveTime = json.getString("arrive_time");
                String overTime = json.getString("running_time");
                String stationName = json.getString("station_name");
//                String stationNo = json.getString"station_no");
                if(index==1){
                    String trainClassName = json.getString("train_class_name");
                    String trainCode = json.getString("station_train_code");
                    String startStationName = json.getString("start_station_name");
                    String endStationName = json.getString("end_station_name");

                    infoNew.setTrainCode(trainCode);
                    infoNew.setStartStationName(startStationName);
                    infoNew.setEndStationName(endStationName);
                    node.setArriveTime("----");
                    node.setOverTime("----");
                    node.setStartTime(startTime);
                }else{
                    //处理arriveTime
                    node.setArriveTime(arriveTime);
                    node.setStartTime(startTime);
                    node.setOverTime(overTime);
                }
                node.setStationName(stationName);
                trainInfoList.add(stationName);
                if(index<10){
                    node.setStationNo("0"+index);
                }else{
                    node.setStationNo(String.valueOf(index));
                }
                nodeList.add(node);
                if(index == size){
                    node.setOverTime("----");
                    node.setStartTime(arriveTime);
                }
            }
            fahrplan.setTrainInfoNew(JSONObject.toJSONString(infoNew));
            fahrplan.setTrainInfos(StringUtils.join(trainInfoList.toArray(), ","));
            log.info("12306(补偿) - 根据车次编码获取时刻表信息 --> infoNew：{}",JSONObject.toJSONString(infoNew));
        }catch(Exception ex){
            log.error("解析错误或无法爬取到12306(补偿)数据");
            ex.printStackTrace();
        }
    }

    //GC-高铁/城际D-动车Z-直达T-特快K-快速 其他
    private String getTrainTypeName(String trainNum){
        String prefixKey = trainNum.substring(0, 1);
        String trainClassName = "";
        if("GC".contains(prefixKey)){
            trainClassName = "高铁/城际";
        }else if("D".contains(prefixKey)){
            trainClassName = "动车";
        }else if("Z".contains(prefixKey)){
            trainClassName = "直达";
        }else if("T".contains(prefixKey)){
            trainClassName = "特快";
        }else if("K".contains(prefixKey)){
            trainClassName = "快速";
        }else {
            trainClassName = "其他";
        }
        return trainClassName;
    }

    private Boolean validateData(TrainFahrplanPojo trainFahrplan) {
        String trainInfoNew = trainFahrplan.getTrainInfoNew();
        Boolean isNull = false;
        if(StringUtils.isNotBlank(trainInfoNew)){
            JSONObject jsonObject = JSONObject.parseObject(trainInfoNew);
            String length = jsonObject.getString("trainLength");
            if(Integer.parseInt(length) == 0){
                isNull = true;
            }
        }
        if(StringUtils.isBlank(trainInfoNew)||isNull){
            return  false;
        }
        return  true;
    }
}
