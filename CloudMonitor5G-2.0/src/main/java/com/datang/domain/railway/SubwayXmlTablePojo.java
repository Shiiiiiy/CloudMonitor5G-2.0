package com.datang.domain.railway;

import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.json.annotations.JSON;

import javax.persistence.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 14:21
 * @Version 1.0
 **/
@Entity
@Data
@Table(name = "IADS_SUBWAY_XML_TABLE")
public class SubwayXmlTablePojo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private Long id; //编码

    @Column(name = "CITY")
    private String city;

    @Column(name = "LINENO")
    private String lineNo;

    @Column(name = "STATIONS")
    private String stations;

    @Column(name = "UPDATE_TIME_LONG")
    private Long updateTimeLong;

    @Transient
    private String updateTime;

    @Column(name = "LINE_XML")
    private String lineXml;

    @Column(name = "XML_FILE_PATH")
    private String xmlFilePath;

    @JSON(serialize = false)
    public String getStations() {
        return stations;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    @JSON(serialize = false)
    public String getXmlFilePath() {
        return xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    @JSON(serialize = false)
    public Long getUpdateTimeLong() {
        return updateTimeLong;
    }

    public void setUpdateTimeLong(Long updateTimeLong) {
        this.updateTimeLong = updateTimeLong;
        this.updateTime = DateFormatUtils.format(updateTimeLong, "yyyyMMdd");
    }

    public String getUpdateTime() {
        if(this.updateTimeLong!=null){
            this.updateTime =  DateFormatUtils.format(this.updateTimeLong, "yyyyMMdd");
        }
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
