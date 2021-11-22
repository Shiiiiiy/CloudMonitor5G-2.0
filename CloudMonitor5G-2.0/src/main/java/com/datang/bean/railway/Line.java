package com.datang.bean.railway; /**
 * Auto Generated Java Class.
 */

import lombok.Data;

import java.util.ArrayList;
@Data
public class Line {
  String name;
  String startStation;
  String startTime;
  String destStation;
  String arriveTime;
  ArrayList<Stop> stops = new ArrayList<Stop>(); 
  /* ADD YOUR CODE HERE */
  public void print(){
    System.out.println(name+" "+startStation+"  "+startTime+"   "+destStation+"   "+arriveTime);
    for(Stop stop:stops)
    System.out.println(stop.name);
  }
}
