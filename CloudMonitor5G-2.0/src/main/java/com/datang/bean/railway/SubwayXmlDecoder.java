package com.datang.bean.railway; /**
 * Auto Generated Java Class.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @Description: 地铁线路xml生成
 * @Author: lc
 * @Date: 2021/11/19 10:22
 **/
public class SubwayXmlDecoder {
  BufferedWriter writer = null;
  int sid =0;
  /* ADD YOUR CODE HERE */
  public SubwayXmlDecoder(File file) throws Exception{
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
  
  public void writeLine(String linename,String speedKmperHperH,String maxSpeedKmperH,String start,String end) throws Exception{
    if(writer != null){
      //  <MetroLine LID="0" Name="地铁4号线(外圈(宜山路-宜山路))" Speed_KMperH="60" MaxSpeed_KMperH="80" StartTime="05:25" EndTime="22:35">
      writer.write("  <MetroLine LID=\"0\" ");
      writer.write(" Name=\""+linename+"\"");
      writer.write(" Speed_KMperH=\""+speedKmperHperH+"\"");
      writer.write(" MaxSpeed_KMperH=\""+maxSpeedKmperH+"\"");
      writer.write(" StartTime=\""+start+"\"");
      writer.write(" EndTime=\""+end+"\" >\r\n");
    }
  }
  
  public void endWriteLine() throws Exception{
    if(writer != null){
      writer.write("  </MetroLine>\r\n");
    }
  }
  
  public void writeStation(String name,String lon,String lat) throws Exception{
    if(writer != null){
      //   <Station SID="0" Name="宜山路" StationTime="0" Longitude="121.422564995" Latitude="31.1889963644" />
      SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");
      writer.write("   <Station SID=\""+sid+"\" ");
      sid++;
      writer.write(" Name=\""+name+"\"");
      writer.write(" StartTime=\"0\"");
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
