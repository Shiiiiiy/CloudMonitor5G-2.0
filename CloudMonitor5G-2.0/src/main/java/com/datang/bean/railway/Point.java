package com.datang.bean.railway;

import lombok.Data;

/**
 * Auto Generated Java Class.
 */
@Data
public class Point {
  
  /* ADD YOUR CODE HERE */
  String lon;
  String lat;

  public Point(){
  }

  public Point(String lon,String lat){
    this.lon=lon;
    this.lat=lat;
  }
}
