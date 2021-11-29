package com.datang.bean.railway;

import com.datang.common.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto Generated Java Class.
 */
@Data
public class Stop {
  String name;
  String arrive;
  String start;
  List<Point> ponitList = new ArrayList<>();

  /* ADD YOUR CODE HERE */
  public Stop(){
  }

  public Stop(String name,String arrive,String start){
    this.name = name;
    this.arrive = arrive;
    this.start = start;
  }

  public void addPoint(Point point) {
    if(StringUtils.hasText(point.getLon()) && StringUtils.hasText(point.getLat())) {
      this.ponitList.add(point);
    }
  }
}
