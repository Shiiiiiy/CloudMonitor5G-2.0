package com.datang.bean.railway; /**
 * Auto Generated Java Class.
 */

import lombok.Data;

import java.util.ArrayList;
@Data
public class Line {
  private Long id;
  private String name;
  private String startStation;
  private String startTime;
  private String destStation;
  private String arriveTime;
  private ArrayList<Stop> stops = new ArrayList<Stop>();
  /* ADD YOUR CODE HERE */
  public void print(){
    System.out.println(name+" "+startStation+"  "+startTime+"   "+destStation+"   "+arriveTime);
    for(Stop stop:stops)
    System.out.println(stop.name);
  }
}
