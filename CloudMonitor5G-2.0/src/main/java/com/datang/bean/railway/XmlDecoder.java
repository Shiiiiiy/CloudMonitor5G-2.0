package com.datang.bean.railway; /**
 * Auto Generated Java Class.
 */
import java.io.*;
import java.util.*;
import java.text.*;

/**
 * @Description: 高铁线路xml生成
 * @Author: lc
 * @Date: 2021/11/19 10:21
 **/
public class XmlDecoder {
  BufferedWriter writer = null;
  int sid =0;
  /* ADD YOUR CODE HERE */
  public XmlDecoder(File file) throws Exception{
    writer = new BufferedWriter(
     new OutputStreamWriter(
       new FileOutputStream(file), "UTF-8"));
  }
  
  public void writeHead() throws Exception{
    if(writer != null){
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
      writer.write("<MetroDataManager>\r\n");
    }
  }
  
  public void writeTail() throws Exception{
    if(writer != null){
      writer.write("</MetroDataManager>\r\n");
      writer.flush();
      writer.close();
    }
  }
  
  public void writeCity(String city) throws Exception{
    if(writer != null){
      writer.write(" <City Name=\""+city+"\">\r\n");
    }
  }
  
    public void endWriteCity() throws Exception{
    if(writer != null){
      writer.write("  </City>\r\n");
    }
  }
  
  public void writeLine(String linename,String start,String end) throws Exception{
    if(writer != null){
      //  <MetroLine LID="0" Name="C7213" Speed_KMperH="---" MaxSpeed_KMperH="---" StartTime="---" EndTime="---">
      writer.write("  <MetroLine LID=\"0\" ");
      writer.write(" Name=\""+linename+"\"");
      writer.write(" StartTime=\""+start+"\"");
      writer.write(" EndTime=\""+end+"\" >\r\n");
    }
  }
  
  public void endWriteLine() throws Exception{
    if(writer != null){
      writer.write("  </MetroLine>\r\n");
    }
  }
  
  public void writeStation(Stop stop,String lon,String lat) throws Exception{
    if(writer != null){
      //   <Station SID="0" Name="������" StationTime="0" WaitTime="0" ArriveTime="17:08" StartTime="17:08" StopoverTime="0" Longitude="113.262805542161" Latitude="22.990753497346"/>
      long arriveTime, startTime;
      SimpleDateFormat ft = new SimpleDateFormat ("hh:mm"); 
      arriveTime = ft.parse(stop.arrive).getTime(); 
      startTime = ft.parse(stop.start).getTime();
      writer.write("   <Station SID=\""+sid+"\" ");
      sid++;
      writer.write(" Name=\""+stop.name+"\"");
      writer.write(" WaitTime=\""+(startTime-arriveTime)/(1000)+"\"");
      writer.write(" ArriveTime=\""+stop.arrive+"\"");
      writer.write(" StartTime=\""+stop.start+"\"");
      writer.write(" StopoverTime=\""+(startTime-arriveTime)/(1000*60)+"\"");
      writer.write(" Longitude=\""+lon+"\"");
      writer.write(" Latitude=\""+lat+"\"");
      writer.write(" />\r\n");
    }
  }
  
  public void writeItems(ArrayList<Point> items) throws Exception{
    if(writer != null){
      writer.write("   <SubItems>\r\n");
      for(Point item:items){
        //    <SubItem Longitude="113.264289551368" Latitude="22.988702959239"/>
        writer.write("    <SubItem Longitude=\""+item.getLon()+"\" ");
        writer.write(" Latitude=\""+item.getLat()+"\"/>\r\n");
      }
      writer.write("   </SubItems>\r\n");
    }
  }
  
}
