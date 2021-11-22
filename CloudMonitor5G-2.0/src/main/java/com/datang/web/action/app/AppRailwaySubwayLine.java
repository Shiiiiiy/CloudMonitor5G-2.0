package com.datang.web.action.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.domain.railway.SubwayXmlTablePojo;
import com.datang.domain.railway.TrainXmlTablePojo;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.exception.ApplicationException;
import com.datang.service.RailWayStation.SubwayLineService;
import com.datang.service.RailWayStation.TrainLineService;
import com.datang.util.ZipMultiFile;
import com.datang.util.ZipUtils;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/19 13:36
 * @Version 1.0
 **/
@Controller
@Scope("prototype")
public class AppRailwaySubwayLine {

    @Value("${railwaySubwayLineFileUrl}")
    private String railwaySubwayLineFileUrl;

    @Autowired
    private TrainLineService trainLineService;

    @Autowired
    private SubwayLineService subwayLineService;

    //出发点
    private String startStationName;

    //目的地
    private String endStationName;

    //查询的日期
    private String queryDateTime;

    //车次、线路号
    private String trainCode;

    //城市
    private String city;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String queryTrainList(){
        try {
            List<Map<String,String>> list = trainLineService.queryTrainList(startStationName,endStationName,queryDateTime);
            ActionContext.getContext().getValueStack().push(list);
        } catch (Exception e) {
            e.printStackTrace();
            ActionContext.getContext().getValueStack().set("errorMsg", "查询过程中发生异常:"+e);
        }
        return ReturnType.JSON;
    }

    public InputStream downloadTrainXml(){
        try {
            PageList pageList = new PageList();
            if(trainCode!=null){
                pageList.putParam("trainCode", trainCode);
                if(queryDateTime!=null){
                    Date dateTime = simpleDateFormat.parse(queryDateTime);
                    pageList.putParam("startTime", dateTime.getTime());
                }
                List<TrainXmlTablePojo> xmlList = trainLineService.findTrainXml(pageList);
                if(xmlList==null || xmlList.size()==0){
                    throw new ApplicationException("找不到车次"+trainCode+"对应的xml");
                }
                File file1 = new File(railwaySubwayLineFileUrl+ "/trainZipFile/");
                if (!file1.exists()) {
                    file1.mkdirs();
                }

                deleteFile(file1);
                File zipFile = new File(railwaySubwayLineFileUrl + "/trainZipFile/" +trainCode+"-"+queryDateTime+ ".zip");

                List<File> fileList = new ArrayList<File>();
                for (TrainXmlTablePojo pojo : xmlList) {
                    if (null != pojo && StringUtils.hasText(pojo.getXmlFilePath())) {
                        String filePath = pojo.getXmlFilePath();
                        File xml = new File(filePath);
                        if (xml.exists() && xml.isFile()) {
                            fileList.add(xml);
                        }
                    }
                }
                ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
                ZipMultiFile.zipFiles(fileList, zipFile);
                FileInputStream zipIn = new FileInputStream(zipFile);
                zipFile.delete();
                return zipIn;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ActionContext.getContext().getValueStack().set("errorMsg", "查询过程中发生异常:"+e);
        }
        return null;
    }

    public InputStream downloadSubwayXml(){
        try {
            PageList pageList = new PageList();
            if(city!=null && trainCode!=null){
                pageList.putParam("lineNo", trainCode);
                pageList.putParam("city", city);
                List<SubwayXmlTablePojo> xmlList = subwayLineService.findSubwayXml(pageList);
                if(xmlList==null || xmlList.size()==0){
                    throw new ApplicationException("找不到"+trainCode+"对应的xml");
                }
                File file1 = new File(railwaySubwayLineFileUrl+ "/subwayZipFile/");
                if (!file1.exists()) {
                    file1.mkdirs();
                }

                deleteFile(file1);
                File zipFile = new File(railwaySubwayLineFileUrl + "/subwayZipFile/" +city+"-"+trainCode+ ".zip");

                List<File> fileList = new ArrayList<File>();
                for (SubwayXmlTablePojo pojo : xmlList) {
                    if (null != pojo && StringUtils.hasText(pojo.getXmlFilePath())) {
                        String filePath = pojo.getXmlFilePath();
                        File xml = new File(filePath);
                        if (xml.exists() && xml.isFile()) {
                            fileList.add(xml);
                        }
                    }
                }
                ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
                ZipMultiFile.zipFiles(fileList, zipFile);
                FileInputStream zipIn = new FileInputStream(zipFile);
                zipFile.delete();
                return zipIn;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ActionContext.getContext().getValueStack().set("errorMsg", "查询过程中发生异常:"+e);
        }
        return null;
    }

    public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            f.delete();
        }
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getQueryDateTime() {
        return queryDateTime;
    }

    public void setQueryDateTime(String queryDateTime) {
        this.queryDateTime = queryDateTime;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
