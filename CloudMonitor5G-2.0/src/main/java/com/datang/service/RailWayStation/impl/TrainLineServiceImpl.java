package com.datang.service.RailWayStation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.datang.bean.railway.*;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.common.util.StringUtils;
import com.datang.constant.RailWayHttpConstants;
import com.datang.dao.railway.TrainStationDao;
import com.datang.dao.railway.TrainTimeInterfaceDao;
import com.datang.dao.railway.TrainXmlTableDao;
import com.datang.domain.railway.TrainStationPojo;
import com.datang.domain.railway.TrainTimeInterfacePojo;
import com.datang.domain.railway.TrainXmlTablePojo;
import com.datang.exception.ApplicationException;
import com.datang.service.RailWayStation.TrainLineService;
import com.datang.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/16 14:47
 * @Version 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor=Exception.class)
public class TrainLineServiceImpl implements TrainLineService {

    @Value("${railwaySubwayLineFileUrl}")
    private String railwaySubwayLineFileUrl;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TrainXmlTableDao trainXmlTableDao;

    @Autowired
    private TrainTimeInterfaceDao trainTimeInterfaceDao;

    @Autowired
    private TrainStationDao trainStationDao;


    @Override
    public Long importRailwayTrail(File importFile) {
        CsvReader csvReader = null;
        XmlDecoder decoder = null;
        Long xmlNum = 0L;
        try {
            csvReader = new CsvReader(new FileInputStream(importFile),',', Charset.forName("gbk"));  //???????????????.csv
            csvReader.readHeaders();
            //csv???????????????????????? ??????
            List<StationTrail> orderTrailList = new ArrayList<StationTrail>();
            //csv???????????????????????? ??????
            List<StationTrail> reverseTrailList = new ArrayList<StationTrail>();
            List<String> stationNameList = new ArrayList<>();
            int pointNo1 = 0;
            StationTrail orderTrail = null;
            StationTrail reverseTrail = null;
            while(csvReader.readRecord()){
                if(StringUtils.hasText(csvReader.get(1).trim())){
                    if(orderTrail!=null){
                        orderTrailList.add(orderTrail);
                    }
                    orderTrail = new StationTrail();
                    stationNameList.add(csvReader.get(1).trim());
                    orderTrail.setStationName(csvReader.get(1).trim());
                    orderTrail.setLon(csvReader.get(2));
                    orderTrail.setLat(csvReader.get(3));

                    if(reverseTrail==null){
                        reverseTrail =  new StationTrail();
                    }
                    reverseTrail.setStationName(csvReader.get(1).trim());
                    reverseTrail.setLon(csvReader.get(2));
                    reverseTrail.setLat(csvReader.get(3));
                    reverseTrailList.add(reverseTrail);
                    reverseTrail =  new StationTrail();
                }else{
                    if(pointNo1 % 10 == 0){
                        //??????????????????500m
                        if(csvReader.get(2)!=null && csvReader.get(3)!=null){
                            orderTrail.addPoint(new Point(csvReader.get(2),csvReader.get(3)));
                            reverseTrail.addPoint(new Point(csvReader.get(2),csvReader.get(3)));
                        }
                    }
                    pointNo1++;
                }
            }
            orderTrailList.add(orderTrail);
            Collections.reverse(reverseTrailList);
            List<List<String>> rangeList = new ArrayList<>();
            arrangementSort(stationNameList,new String[2],0,rangeList);
            Map<String,String> map = new HashMap<>();
            for (List<String> range:rangeList) {
                List<Map<String, Object>>  templatePojoList = null;
                String sql = "SELECT * FROM iads_train_fahrplan WHERE  train_info like '%"+range.get(0)+"%' and train_info like '%"
                        +range.get(1)+"%' AND strpos(train_info,'"+range.get(0)+"')>strpos(train_info,'"+range.get(1)+"');";
                templatePojoList = jdbcTemplate.objectQueryAll(sql);
                if (null != templatePojoList && 0 != templatePojoList.size()) {
                    outer:for (Map<String, Object> m : templatePojoList) {
                        if(m.get("train_code")==null || m.get("train_info_new")==null) {
                            continue outer;
                        }
                        String trainStationInfo = (String)m.get("train_info");
                        String[] trainStationArry = trainStationInfo.split(",");
                        inner:for (String stationName:trainStationArry) {
                            if(!stationNameList.contains(stationName)){
                                continue outer;
                            }
                        }
                        String trainCode = (String)m.get("train_code");
                        String trainInfoNew = (String)m.get("train_info_new");
                        map.put(trainCode,trainInfoNew);
                    }
                }
            }
            for (Map.Entry<String, String> mapEntry:map.entrySet()) {
                String trainCode = mapEntry.getKey();
                TrainInfo trainInfo = JSONObject.parseObject(mapEntry.getValue(), TrainInfo.class);
                Line line = getLine(trainInfo);

                int startStationIndex = -1;
                int destStationIndex = -1;
                for(int index=0;index<orderTrailList.size();index++){
                    StationTrail trails = orderTrailList.get(index);
                    if(trails.getStationName().equals(line.getStartStation())){
                        startStationIndex = index;
                    }
                    if(trails.getStationName().equals(line.getDestStation())){
                        destStationIndex = index;
                    }
                }

                List<StationTrail> newTrailList = new ArrayList<StationTrail>();
                if(startStationIndex>destStationIndex){
                    newTrailList.addAll(reverseTrailList);
                }else{
                    newTrailList.addAll(orderTrailList);
                }

                //??????xml
                String xmlFileName = trainCode+"-"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")+".xml";
                File xmlfile= new File(railwaySubwayLineFileUrl+File.separator+"railwayLine"+File.separator+trainCode+File.separator+xmlFileName);
                //?????????????????????????????????
                if(!xmlfile.getParentFile().exists()  && !xmlfile.isDirectory()){
                    xmlfile.getParentFile().mkdirs();
                }
                if(xmlfile.exists()) {
                    xmlfile.delete();
                }
                xmlfile.createNewFile();
                decoder = new XmlDecoder(xmlfile);
                decoder.writeHead();
                decoder.writeCity(line.getStartStation());
                decoder.writeLine(line.getName(),line.getStartTime(),line.getArriveTime());

                ArrayList<Point> points = new ArrayList<>();
                int index = 0;
                boolean isBegin = false;
                for(Stop stop:line.getStops()){
                    for(int i=index;i<newTrailList.size();i++){
                        StationTrail stationTrail = newTrailList.get(i);
                        if(stationTrail.getStationName().equals(stop.getName())){
                            isBegin = true;
                            if(points.size()>0){
                                decoder.writeItems(points);
                                points.clear();
                            }
                            decoder.writeStation(stop,stationTrail.getLon(),stationTrail.getLat());
                            points.addAll(stationTrail.getPonitList());
                            index = i+1;
                            break;
                        }else{
                            if(isBegin){
                                points.add(new Point(stationTrail.getLon(),stationTrail.getLat()));
                                points.addAll(stationTrail.getPonitList());
                            }
                        }
                    }
                }
                decoder.endWriteLine();
                decoder.endWriteCity();
                decoder.writeTail();

                //???????????????
                TrainXmlTablePojo trainXmlTablePojo = new TrainXmlTablePojo();
                trainXmlTablePojo.setTrainCode(line.getName());
                trainXmlTablePojo.setStartStation(line.getStartStation());
                trainXmlTablePojo.setStartTime(line.getStartTime());
                trainXmlTablePojo.setDestStation(line.getDestStation());
                trainXmlTablePojo.setArriveTime(line.getArriveTime());
                trainXmlTablePojo.setLineXml(xmlFileName);
                trainXmlTablePojo.setXmlFilePath(xmlfile.getPath());
                trainXmlTablePojo.setUpdateTimeLong(new Date().getTime());

                PageList pagelist = new PageList();
                pagelist.putParam("lineXml",trainXmlTablePojo.getLineXml());
                List<TrainXmlTablePojo> trainXmlTableList = trainXmlTableDao.findTrainXmlTable(pagelist);
                if(trainXmlTableList==null || trainXmlTableList.size()==0){
                    trainXmlTableDao.create(trainXmlTablePojo);
                }else{
                    trainXmlTablePojo.setId(trainXmlTableList.get(0).getId());
                    trainXmlTableDao.update(trainXmlTablePojo);
                }
                xmlNum++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(
                    e.getMessage());
        } finally {
            try{
                if(csvReader!=null){
                    csvReader.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
                throw new ApplicationException(
                        e.getMessage());
            }
        }
        return xmlNum;
    }

    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
        return trainXmlTableDao.doPageQuery(pageList);
    }

    @Override
    public List<TrainXmlTablePojo> getTrainXmlFile(String trainXmlIds) {
        List<TrainXmlTablePojo> list = new ArrayList<>();
        String[] split = trainXmlIds.split(",");
        for (String id:split) {
            TrainXmlTablePojo trainXmlTablePojo = trainXmlTableDao.find(Long.valueOf(id));
            list.add(trainXmlTablePojo);
        }
        return list;
    }

    @Override
    public void deleteTrainXml(Long id) {
        trainXmlTableDao.delete(id);
    }

    @Override
    public List<Map<String, String>> queryTrainList(String startStationName, String endStationName, String queryDateTime) throws Exception {
        List<Map<String,String>> list = new ArrayList<>();
        List<TrainTimeInterfacePojo> interfaceList = (List<TrainTimeInterfacePojo>)trainTimeInterfaceDao.findAll();

        TrainStationPojo fromTrainStationPojo = trainStationDao.findTelegraphCodeByName(startStationName);
        String fromCode = fromTrainStationPojo.getTelegraphCode();
        if(fromCode==null){
            throw new Exception("??????????????????????????????????????????");
        }
        TrainStationPojo endTrainStationPojo = trainStationDao.findTelegraphCodeByName(endStationName);
        String endCode = endTrainStationPojo.getTelegraphCode();
        if(endCode == null){
            throw new Exception("??????????????????????????????????????????");
        }

        StringBuffer uri = new StringBuffer(RailWayHttpConstants.TRAIN_12306_QUERY_REALTIME);
        outer:for (TrainTimeInterfacePojo interfacePojo:interfaceList) {
            uri.append(interfacePojo.getQueryInterface()).append("?");
            //https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2021-11-25&leftTicketDTO.from_station=BJP&
            //leftTicketDTO.to_station=SHH&purpose_codes=ADULT
            uri.append("leftTicketDTO.train_date=").append(queryDateTime);
            uri.append("&leftTicketDTO.from_station=").append(fromCode);
            uri.append("&leftTicketDTO.to_station=").append(endCode);
            uri.append("&purpose_codes=ADULT");
//            log.debug("12306???????????????????????????{}", uri.toString());
            String resultString = HttpClientUtils.httpCookieGet(uri.toString());
            if(org.apache.commons.lang3.StringUtils.isNotBlank(resultString) && resultString.contains("data")) {
                JSONObject result = JSON.parseObject(resultString);
                if(!result.isEmpty() && (result.getIntValue("httpstatus") == 200 && result.containsKey("data"))){
                    JSONObject jsonData = result.getJSONObject("data");
                    if(!jsonData.isEmpty() && jsonData.containsKey("result")){
                        JSONObject jsonMap = jsonData.getJSONObject("map");
                        JSONArray resultArray = jsonData.getJSONArray("result");
                        for(int index = 0;index < resultArray.size(); index++){
                            String data = resultArray.get(index).toString();
                            if(data.contains("|??????|") && data.contains("|Y|")){
                                Map<String,String> map = new HashMap<>();
                                int locationIndex2 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 2);
                                int locationIndex3 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 3);
                                int locationIndex4 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 4);
                                int locationIndex5 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 5);
                                int locationIndex6 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 6);
                                int locationIndex7 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 7);
                                int locationIndex8 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 8);
                                int locationIndex9 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 9);
                                int locationIndex10 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 10);
                                int locationIndex11 = org.apache.commons.lang3.StringUtils.ordinalIndexOf(data, "|", 11);

                                String trainNo = data.substring(locationIndex2 + 1, locationIndex3); //????????????
                                String trainCode = data.substring(locationIndex3 + 1, locationIndex4); //??????
                                String startStation = data.substring(locationIndex4 + 1, locationIndex5); //?????????
                                String endStation = data.substring(locationIndex5 + 1, locationIndex6); //?????????
                                String beginStation = data.substring(locationIndex6 + 1, locationIndex7); //?????????
                                String arriveStation = data.substring(locationIndex7 + 1, locationIndex8); //?????????
                                String beginTime = data.substring(locationIndex8 + 1, locationIndex9); //????????????
                                String endTime = data.substring(locationIndex9 + 1, locationIndex10); //????????????
                                String overTime = data.substring(locationIndex10 + 1, locationIndex11); //??????
                                map.put("trainNo",trainNo);
                                map.put("trainCode",trainCode);
                                map.put("startStation",startStation);
                                map.put("endStation",endStation);
                                map.put("beginStation",beginStation);
                                map.put("arriveStation",arriveStation);
                                map.put("beginTime",beginTime);
                                map.put("endTime",endTime);
                                map.put("overTime",overTime);
                                list.add(map);
                            }
                        }
                        break outer;
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<TrainXmlTablePojo> findTrainXml(PageList pageList) {
        return trainXmlTableDao.findTrainXmlTable(pageList);
    }

    @Override
    public void manualAddTrainXml(Line line) {
        CsvReader csvReader = null;
        XmlDecoder decoder = null;
        Long xmlNum = 0L;
        try {
            //csv???????????????????????? ??????
            List<StationTrail> newTrailList = new ArrayList<StationTrail>();
            ArrayList<Stop> stops = line.getStops();
            stops.stream().forEach(stop->{
                StationTrail trail = new StationTrail();
                trail.setStationName(stop.getName());
                trail.setLon(stop.getLon());
                trail.setLat(stop.getLat());
                trail.getPonitList().addAll(stop.getPoints());
                newTrailList.add(trail);
            });

            String trainCode = line.getName();

            //??????xml
            String xmlFileName = trainCode+"-"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")+".xml";
            File xmlfile= new File(railwaySubwayLineFileUrl+File.separator+"railwayLine"+File.separator+trainCode+File.separator+xmlFileName);
            //?????????????????????????????????
            if(!xmlfile.getParentFile().exists()  && !xmlfile.isDirectory()){
                xmlfile.getParentFile().mkdirs();
            }
            if(xmlfile.exists()) {
                xmlfile.delete();
            }
            xmlfile.createNewFile();
            decoder = new XmlDecoder(xmlfile);
            decoder.writeHead();
            decoder.writeCity(line.getStartStation());
            decoder.writeLine(line.getName(),line.getStartTime(),line.getArriveTime());

            ArrayList<Point> points = new ArrayList<>();
            int index = 0;
            boolean isBegin = false;
            for(Stop stop:line.getStops()){
                for(int i=index;i<newTrailList.size();i++){
                    StationTrail stationTrail = newTrailList.get(i);
                    if(stationTrail.getStationName().equals(stop.getName())){
                        isBegin = true;
                        if(points.size()>0){
                            decoder.writeItems(points);
                            points.clear();
                        }
                        decoder.writeStation(stop,stationTrail.getLon(),stationTrail.getLat());
                        points.addAll(stationTrail.getPonitList());
                        index = i+1;
                        break;
                    }else{
                        if(isBegin){
                            points.add(new Point(stationTrail.getLon(),stationTrail.getLat()));
                            points.addAll(stationTrail.getPonitList());
                        }
                    }
                }
            }
            decoder.endWriteLine();
            decoder.endWriteCity();
            decoder.writeTail();

            //???????????????
            TrainXmlTablePojo trainXmlTablePojo;
            if(line.getId() != null){
                trainXmlTablePojo = trainXmlTableDao.find(line.getId());
            }else{
                trainXmlTablePojo = new TrainXmlTablePojo();
            }

            trainXmlTablePojo.setTrainCode(line.getName());
            trainXmlTablePojo.setStartStation(line.getStartStation());
            trainXmlTablePojo.setStartTime(line.getStartTime());
            trainXmlTablePojo.setDestStation(line.getDestStation());
            trainXmlTablePojo.setArriveTime(line.getArriveTime());
            trainXmlTablePojo.setLineXml(xmlFileName);
            trainXmlTablePojo.setXmlFilePath(xmlfile.getPath());
            trainXmlTablePojo.setUpdateTimeLong(System.currentTimeMillis());

            if(line.getId()!=null){
                trainXmlTableDao.update(trainXmlTablePojo);
            }else{
                PageList pagelist = new PageList();
                pagelist.putParam("lineXml",trainXmlTablePojo.getLineXml());
                List<TrainXmlTablePojo> trainXmlTableList = trainXmlTableDao.findTrainXmlTable(pagelist);
                if(trainXmlTableList==null || trainXmlTableList.size()==0){
                    trainXmlTableDao.create(trainXmlTablePojo);
                }else{
                    trainXmlTablePojo.setId(trainXmlTableList.get(0).getId());
                    trainXmlTableDao.update(trainXmlTablePojo);
                }
            }

            xmlNum++;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(
                    e.getMessage());
        } finally {
            try{
                if(csvReader!=null){
                    csvReader.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
                throw new ApplicationException(
                        e.getMessage());
            }
        }
    }

    @Override
    public TrainXmlTablePojo find(Long id) {
        return trainXmlTableDao.find(id);
    }

    /**
     * ???????????????????????????????????????????????????
     * @param a???????????????
     * @param centerValArry???????????????????????????
     * @param resultIndex????????????????????????
     * @param result????????????
     */
    public static void arrangementSort(List<String> a, String[] centerValArry, int resultIndex,List<List<String>> result){
        int centerLength = centerValArry.length;
        if(resultIndex >= centerLength){
            result.add(new ArrayList<>(Arrays.asList(centerValArry)));
            return;
        }
        for(int i=0; i<a.size(); i++){
            // ???????????????????????????????????????????????????
            boolean exist = false;
            for(int j=0; j<resultIndex; j++){
                if(a.get(i) == centerValArry[j]){  // ?????????????????????????????????
                    exist = true;
                    break;
                }
            }
            if(!exist){  // ??????????????????????????????
                centerValArry[resultIndex] = a.get(i);
                arrangementSort(a, centerValArry, resultIndex+1,result);
            }
        }
    }

    public static Line getLine(TrainInfo trainInfo){
        Line line = new Line();
        String stationName = trainInfo.getTrainCode();
        line.setName(stationName);
        line.setStartStation(trainInfo.getStartStationName());
        line.setDestStation(trainInfo.getEndStationName());

        List<StationItem> itemList = trainInfo.getItems();
        itemList.stream().forEach(item->{
            if(item.getStationNum()==1){
                line.setStartTime(item.getStartTime());
                line.getStops().add(new Stop(item.getStationName(),item.getStartTime(),item.getStartTime()));
            }else if(item.getStationNum()==itemList.size()){
                line.setArriveTime(item.getArriveTime());
                line.getStops().add(new Stop(item.getStationName(),item.getArriveTime(),item.getArriveTime()));
            }else{
                line.getStops().add(new Stop(item.getStationName(),item.getArriveTime(),item.getStartTime()));
            }
        });
        return line;
    }
}
