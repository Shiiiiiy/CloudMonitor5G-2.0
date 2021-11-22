package com.datang.bean.railway;

import lombok.Data;

/**
 * Auto Generated Java Class.
 */
public class Point {
  
  /* ADD YOUR CODE HERE */
  private String lon="";
  private String lat="";

  public Point(String lon,String lat){
    this.lon=lon;
    this.lat=lat;
  }

  public String getLon() {
    return lon;
  }


  public String getLat() {
    return lat;
  }

}
