package com.datang.bean.railway;

import lombok.Data;

/**
 * Auto Generated Java Class.
 */
@Data
public class Stop {
  String name;
  String arrive;
  String start;

  /* ADD YOUR CODE HERE */
  public Stop(){
  }

  public Stop(String name,String arrive,String start){
    this.name = name;
    this.arrive = arrive;
    this.start = start;
  }
}
