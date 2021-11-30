package com.datang.bean.railway; /**
 * Auto Generated Java Class.
 */

import lombok.Data;

import java.util.ArrayList;

@Data
public class Subway {
  String city;
  String lineName;
  ArrayList<Stop> stops = new ArrayList<Stop>();

}
