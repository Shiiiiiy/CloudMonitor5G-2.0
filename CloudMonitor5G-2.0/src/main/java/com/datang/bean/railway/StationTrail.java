package com.datang.bean.railway;

import com.datang.common.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/16 15:41
 * @Version 1.0
 **/
public class StationTrail {
    private String stationName;

    private String lon;

    private String lat;

    private List<Point> ponitList = new ArrayList<>();

    public StationTrail() {
    }

    public void addPoint(Point point) {
        if(StringUtils.hasText(point.getLon()) && StringUtils.hasText(point.getLat())) {
            this.ponitList.add(point);
        }
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
}

    public void setLat(String lat) {
        this.lat = lat;
    }

    public List<Point> getPonitList() {
        return ponitList;
    }

    public void setPonitList(List<Point> ponitList) {
        this.ponitList = ponitList;
    }
}
