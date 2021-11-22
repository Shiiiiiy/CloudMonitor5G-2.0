package com.datang.service.RailWayStation.impl;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.datang.bean.railway.*;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.common.util.StringUtils;
import com.datang.dao.railway.SubwayXmlTableDao;
import com.datang.dao.railway.TrainXmlTableDao;
import com.datang.domain.railway.SubwayXmlTablePojo;
import com.datang.domain.railway.TrainXmlTablePojo;
import com.datang.exception.ApplicationException;
import com.datang.service.RailWayStation.SubwayLineService;
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
public class SubwayLineServiceImpl implements SubwayLineService {

    @Value("${railwaySubwayLineFileUrl}")
    private String railwaySubwayLineFileUrl;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubwayXmlTableDao subwayXmlTableDao;

    @Override
    public Long importSubwayTrail(File importFile,String filename) {
        CsvReader csvReader = null;
        SubwayXmlDecoder decoder = null;
        Long xmlNum = 0L;
        try {
            if(!filename.contains("_")){
                throw new ApplicationException("导入文件名不符合格式");
            }
            String city = filename.split("_")[0];
            String name = filename.split("_")[1].substring(0,filename.split("_")[1].indexOf(".csv"));

            //生成xml
            String xmlFileName = city+"-"+name+"-"+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+".xml";
            File xmlfile= new File(railwaySubwayLineFileUrl+File.separator+"subwayLine"+File.separator+city+File.separator+xmlFileName);
            //如果文件夹不存在则创建
            if(!xmlfile.getParentFile().exists()  && !xmlfile.isDirectory()){
                xmlfile.getParentFile().mkdirs();
            }
            if(xmlfile.exists()) {
                xmlfile.delete();
            }
            xmlfile.createNewFile();
            decoder = new SubwayXmlDecoder(xmlfile);
            decoder.writeHead();
            decoder.writeCity(city);
            decoder.writeLine(name,"0","0","","");

            ArrayList<Point> points = new ArrayList<Point>();
            List<String> stationList = new ArrayList<>();

            csvReader = new CsvReader(new FileInputStream(importFile),',', Charset.forName("gbk"));  //高铁车次表.csv
            csvReader.readHeaders();
            while(csvReader.readRecord()){
                if(StringUtils.hasText(csvReader.get(1).trim())){
                    if(points.size()>0){
                        decoder.writeItems(points);
                        points.clear();
                    }
                    stationList.add(csvReader.get(1).trim());
                    decoder.writeStation(csvReader.get(1).trim(),csvReader.get(2),csvReader.get(3));
                }else{
                    //文件太大抽稀500m
                    if(csvReader.get(2)!=null && csvReader.get(3)!=null){
                        points.add(new Point(csvReader.get(2),csvReader.get(3)));
                    }
                }
            }
            decoder.endWriteLine();
            decoder.endWriteCity();
            decoder.writeTail();

            //保存数据库
            SubwayXmlTablePojo subwayXmlTablePojo = new SubwayXmlTablePojo();
            subwayXmlTablePojo.setCity(city);
            subwayXmlTablePojo.setLineNo(name);
            subwayXmlTablePojo.setStations(JSONObject.toJSONString(stationList));
            subwayXmlTablePojo.setLineXml(xmlFileName);
            subwayXmlTablePojo.setXmlFilePath(xmlfile.getPath());
            subwayXmlTablePojo.setUpdateTimeLong(new Date().getTime());

            PageList pagelist = new PageList();
            pagelist.putParam("lineXml",subwayXmlTablePojo.getLineXml());
            List<SubwayXmlTablePojo> subwayXmlTableList = subwayXmlTableDao.findSubwayXmlTable(pagelist);
            if(subwayXmlTableList==null || subwayXmlTableList.size()==0){
                subwayXmlTableDao.create(subwayXmlTablePojo);
            }else{
                subwayXmlTablePojo.setId(subwayXmlTableList.get(0).getId());
                subwayXmlTableDao.update(subwayXmlTablePojo);
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
        return xmlNum;
    }

    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
        return subwayXmlTableDao.doPageQuery(pageList);
    }

    @Override
    public List<SubwayXmlTablePojo> getSubwayXmlFile(String trainXmlIds) {
        List<SubwayXmlTablePojo> list = new ArrayList<>();
        String[] split = trainXmlIds.split(",");
        for (String id:split) {
            SubwayXmlTablePojo subwayXmlTablePojo = subwayXmlTableDao.find(Long.valueOf(id));
            list.add(subwayXmlTablePojo);
        }
        return list;
    }

    @Override
    public void deleteSubwayXml(Long id) {
        subwayXmlTableDao.delete(id);
    }

    @Override
    public List<SubwayXmlTablePojo> findSubwayXml(PageList pageList) {
        return subwayXmlTableDao.findSubwayXmlTable(pageList);
    }

    /**
     * 通过递归的方式罗列出所有的排列结果
     * @param a：初始数组
     * @param centerValArry：排列数组初始状态
     * @param resultIndex：比较的起始索引
     * @param result：结果集
     */
    public static void arrangementSort(List<String> a, String[] centerValArry, int resultIndex,List<List<String>> result){
        int centerLength = centerValArry.length;
        if(resultIndex >= centerLength){
            result.add(new ArrayList<>(Arrays.asList(centerValArry)));
            return;
        }
        for(int i=0; i<a.size(); i++){
            // 判断待选的数是否存在于排列的结果中
            boolean exist = false;
            for(int j=0; j<resultIndex; j++){
                if(a.get(i) == centerValArry[j]){  // 若已存在，则不能重复选
                    exist = true;
                    break;
                }
            }
            if(!exist){  // 若不存在，则可以选择
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
