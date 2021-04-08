package com.datang.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelToHtml {
	
    private static String[] bordesr={"border-top:","border-right:","border-bottom:","border-left:"};
    private static String[] borderStyles={"solid ","dotted ","dotted ","dotted ","solid ","solid ","solid ","solid ","solid ","solid","solid","solid","solid","solid"};
	
	public static void main(String[] args) throws Exception {
		String filePath = "E:/ZZZ/stationReport/uploadReportTemplate/测试报表_遵义_1608797764823_fin.xls";
		String filePath2 = "E:/ZZZ/stationReport/uploadReportTemplate/xx.html";
		ReadExcelToHtml.readExcelToHtml(filePath,filePath2,true);

	}
	
	
	  /**
	   * @author lucheng
	   * @date 2021年1月6日 下午2:48:43
	   * @param filePath 源文件文件的路径
	   * @param isWithStyle 是否需要表格样式 包含 字体 颜色 边框 对齐方式
	   * @return 返回分sheet的html内容展示
	   */
	public static List<Map<String, String>> readExcelToMap(String filePath, boolean isWithStyle){
      
      InputStream is = null;
      List<Map<String, String>> sheetMapExcelInfo =null;
      String htmlContent = null;
      try {
          File sourcefile = new File(filePath);
          is = new FileInputStream(sourcefile);
          Workbook wb = WorkbookFactory.create(is);
          if (wb instanceof XSSFWorkbook) {   //03版excel处理方法
              XSSFWorkbook xWb = (XSSFWorkbook) wb;
              sheetMapExcelInfo = getSheetMapExcelInfo(xWb,isWithStyle);
          }else if(wb instanceof HSSFWorkbook){  //07及10版以后的excel处理方法
              HSSFWorkbook hWb = (HSSFWorkbook) wb;
              sheetMapExcelInfo = getSheetMapExcelInfo(hWb,isWithStyle);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }finally{
          try {
              is.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      return sheetMapExcelInfo;
  }
	
	  /**
	   * @author lucheng
	   * @date 2021年1月6日 下午2:48:43
	   * @param filePath 源文件文件的路径
	   * @param isWithStyle 是否需要表格样式 包含 字体 颜色 边框 对齐方式
	   * @return 返回html内容展示
	   */
	public static String readExcelToString(String filePath, boolean isWithStyle){
        
        InputStream is = null;
        String htmlExcel = null;
        String htmlContent = null;
        try {
            File sourcefile = new File(filePath);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {   //03版excel处理方法
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                htmlExcel = getExcelInfo(xWb,isWithStyle);
            }else if(wb instanceof HSSFWorkbook){  //07及10版以后的excel处理方法
                HSSFWorkbook hWb = (HSSFWorkbook) wb;
                htmlExcel = getExcelInfo(hWb,isWithStyle);
            }
            htmlContent = convertHtml(htmlExcel);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlContent;
    }
	
	  /**
	   * 
	   * @author lucheng
	   * @date 2021年1月6日 下午2:48:43
	   * @param filePath 源文件文件的路径
	   * @param htmlPositon 生成的html文件的路径 
	   * @param isWithStyle 是否需要表格样式 包含 字体 颜色 边框 对齐方式
	   * @return 保存html文件的路径
	   */
      public static String readExcelToHtml(String filePath ,String htmlPositon, boolean isWithStyle){
          
          InputStream is = null;
          String htmlExcel = null;
          try {
              File sourcefile = new File(filePath);
              is = new FileInputStream(sourcefile);
              Workbook wb = WorkbookFactory.create(is);
              if (wb instanceof XSSFWorkbook) {   //03版excel处理方法
                  XSSFWorkbook xWb = (XSSFWorkbook) wb;
                  htmlExcel = getExcelInfo(xWb,isWithStyle);
              }else if(wb instanceof HSSFWorkbook){  //07及10版以后的excel处理方法
                  HSSFWorkbook hWb = (HSSFWorkbook) wb;
                  htmlExcel = getExcelInfo(hWb,isWithStyle);
              }
              writeFile(htmlExcel,htmlPositon);
          } catch (Exception e) {
              e.printStackTrace();
          }finally{
              try {
                  is.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return htmlPositon;
      }
      
     
     /**
      * 直接返回表格样式 
      * @author lucheng
      * @date 2021年1月13日 下午4:29:46
      * @param wb
      * @param isWithStyle
      * @return String
      */
      private static String getExcelInfo(Workbook wb,boolean isWithStyle){
          
          StringBuffer sb = new StringBuffer();
          sb.append("<table style='border-collapse:collapse;' width='100%'>");
          
          int numberOfSheets = wb.getNumberOfSheets();
		  for (int i = 0; i < numberOfSheets; i++) {
			  Sheet sheet = wb.getSheetAt(i);//获取第一个Sheet的内容
	          int lastRowNum = sheet.getLastRowNum();
	          Map<String, String> map[] = getRowSpanColSpanMap(sheet); 
	          Row row = null;        //兼容
	          Cell cell = null;    //兼容

	          //sheet名称
	          String sheetName = sheet.getSheetName();
	          sb.append("<tr><td ><nobr>"+sheetName+"</nobr></td></tr>");
	          for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
	              row = sheet.getRow(rowNum);
	              if (row == null) {
	                  sb.append("<tr><td ><nobr> </nobr></td></tr>");
	                  continue;
	              }
	              sb.append("<tr>");
	              int lastColNum = row.getLastCellNum();
	              for (int colNum = 0; colNum < lastColNum; colNum++) {
	                  cell = row.getCell(colNum);
	                  if (cell == null) {    //特殊情况 空白的单元格会返回null
	                      sb.append("<td> </td>");
	                      continue;
	                  }
	  
	                  String stringValue = getCellValue(cell);
	                  //对stringValue的<>进行转换防止被当作标签
	                  stringValue = convertFormat(stringValue);
	                  if (map[0].containsKey(rowNum + "," + colNum)) {
	                      String pointString = map[0].get(rowNum + "," + colNum);
	                      map[0].remove(rowNum + "," + colNum);
	                      int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
	                      int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
	                      int rowSpan = bottomeRow - rowNum + 1;
	                      int colSpan = bottomeCol - colNum + 1;
	                      sb.append("<td rowspan= '" + rowSpan + "' colspan= '"+ colSpan + "' ");
	                  } else if (map[1].containsKey(rowNum + "," + colNum)) {
	                      map[1].remove(rowNum + "," + colNum);
	                      continue;
	                  } else {
	                      sb.append("<td ");
	                  }
	                  
	                  //判断是否需要样式
	                  if(isWithStyle){
	                	  //customExcelStyle ：按照自定义的样式返回
	                	  //dealExcelStyle ：按照表格的样式返回
	                      dealExcelStyle(wb, sheet, cell, sb);//处理单元格样式
	                  }
	                  
	                  sb.append("><nobr>");
	                  if (stringValue == null || "".equals(stringValue.trim())) {
	                      sb.append("   ");
	                  } else {
	                      // 将ascii码为160的空格转换为html下的空格（ ）
	                      sb.append(stringValue.replace(String.valueOf((char) 160)," "));
	                  }
	                  sb.append("</nobr></td>");
	              }
	              sb.append("</tr>");
	          }
	          //分割行
              sb.append("<tr height=\'20px\'><td ><nobr> </nobr></td></tr>");
		  }
          sb.append("</table>");
          return sb.toString();
      }
      
      /**
       * 返回map格式，包含表格和sheeetName
       * @author lucheng
       * @date 2021年1月13日 下午4:30:36
       * @param wb
       * @param isWithStyle
       * @return List<Map<String,String>>
       */
      private static List<Map<String,String>> getSheetMapExcelInfo(Workbook wb,boolean isWithStyle){
    	  List<Map<String,String>> sheetList = new ArrayList<Map<String,String>>();
          StringBuffer sb = null;
          int numberOfSheets = wb.getNumberOfSheets();
		  for (int i = 0; i < numberOfSheets; i++) {
			  Map<String,String> sheetMap = new HashMap<String, String>(); 
			  
			  sb = new StringBuffer();
			  sb.append("<table style='border-collapse:collapse;' width='100%'>");
			  Sheet sheet = wb.getSheetAt(i);//获取第一个Sheet的内容
	          int lastRowNum = sheet.getLastRowNum();
	          Map<String, String> map[] = getRowSpanColSpanMap(sheet); 
	          Row row = null;        //兼容
	          Cell cell = null;    //兼容

	          int renderColorRowNm = 0; 
	          //sheet名称
	          String sheetName = sheet.getSheetName();
	          for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
	              row = sheet.getRow(rowNum);
	              if (row == null) {
	                  sb.append("<tr><td ><nobr> </nobr></td></tr>");
	                  continue;
	              }
	              sb.append("<tr>");
	              int lastColNum = row.getLastCellNum();
	              for (int colNum = 0; colNum < lastColNum; colNum++) {
	                  cell = row.getCell(colNum);
	                  if (cell == null) {    //特殊情况 空白的单元格会返回null
	                      sb.append("<td> </td>");
	                      continue;
	                  }
	  
	                  String stringValue = getCellValue(cell);
	                  //对stringValue的<>进行转换防止被当作标签
	                  stringValue = convertFormat(stringValue);
	                  if (map[0].containsKey(rowNum + "," + colNum)) {
	                      String pointString = map[0].get(rowNum + "," + colNum);
	                      map[0].remove(rowNum + "," + colNum);
	                      int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
	                      int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
	                      int rowSpan = bottomeRow - rowNum + 1;
	                      int colSpan = bottomeCol - colNum + 1;
	                      sb.append("<td rowspan= '" + rowSpan + "' colspan= '"+ colSpan + "' ");
	                      //导入的excel的首行首列是合并的单元格，保存合并的最后一行的index，默认不合并为0
	                      if(rowNum==0 && colNum==0){
	                    	  renderColorRowNm = bottomeRow;
	                      }
	                  } else if (map[1].containsKey(rowNum + "," + colNum)) {
	                      map[1].remove(rowNum + "," + colNum);
	                      continue;
	                  } else {
	                      sb.append("<td ");
	                  }
	                  
	                  //判断是否需要样式
	                  if(isWithStyle){
	                	  //customExcelStyle ：按照自定义的样式返回
	                	  //dealExcelStyle ：按照表格的样式返回
	                	  if(rowNum<=renderColorRowNm){
	                		  customExcelStyle(wb, sheet, cell, sb,true);//处理单元格样式
	                	  }else{
	                		  customExcelStyle(wb, sheet, cell, sb,false);//处理单元格样式
	                	  }
	                  }
	                  
	                  sb.append("><nobr>");
	                  if (stringValue == null || "".equals(stringValue.trim())) {
	                      sb.append("   ");
	                  } else {
	                      // 将ascii码为160的空格转换为html下的空格（ ）
	                      sb.append(stringValue.replace(String.valueOf((char) 160)," "));
	                  }
	                  sb.append("</nobr></td>");
	              }
	              sb.append("</tr>");
	          }
	          //分割行
              sb.append("<tr height=\'20px\'><td ><nobr> </nobr></td></tr>");
              sb.append("</table>");
              sheetMap.put("sheetName", sheetName);
              sheetMap.put("html", sb.toString());
              sheetList.add(sheetMap);
		  }
          return sheetList;
      }
      
      private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
  
          Map<String, String> map0 = new HashMap<String, String>();
          Map<String, String> map1 = new HashMap<String, String>();
          int mergedNum = sheet.getNumMergedRegions();
          CellRangeAddress range = null;
          for (int i = 0; i < mergedNum; i++) {
              range = sheet.getMergedRegion(i);
              int topRow = range.getFirstRow();
              int topCol = range.getFirstColumn();
              int bottomRow = range.getLastRow();
              int bottomCol = range.getLastColumn();
              map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
              // System.out.println(topRow + "," + topCol + "," + bottomRow + "," + bottomCol);
              int tempRow = topRow;
              while (tempRow <= bottomRow) {
                  int tempCol = topCol;
                  while (tempCol <= bottomCol) {
                      map1.put(tempRow + "," + tempCol, "");
                      tempCol++;
                  }
                  tempRow++;
              }
              map1.remove(topRow + "," + topCol);
          }
          Map[] map = { map0, map1 };
          return map;
      }
      
      
      /**
       * 获取表格单元格Cell内容
       * @param cell
       * @return
       */
      private static String getCellValue(Cell cell) {
  
          String result = new String();  
          switch (cell.getCellType()) {  
          case Cell.CELL_TYPE_NUMERIC:// 数字类型  
              if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
                  SimpleDateFormat sdf = null;  
                  if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {  
                      sdf = new SimpleDateFormat("HH:mm");  
                  } else {// 日期  
                      sdf = new SimpleDateFormat("yyyy-MM-dd");  
                  }  
                  Date date = cell.getDateCellValue();  
                  result = sdf.format(date);  
              } else if (cell.getCellStyle().getDataFormat() == 58) {  
                  // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
                  double value = cell.getNumericCellValue();  
                  Date date = org.apache.poi.ss.usermodel.DateUtil  
                          .getJavaDate(value);  
                  result = sdf.format(date);  
              } else {  
                  double value = cell.getNumericCellValue();  
                  CellStyle style = cell.getCellStyle();  
                  DecimalFormat format = new DecimalFormat();  
                  String temp = style.getDataFormatString();  
                  // 单元格设置成常规  
                  if (temp.equals("General")) {  
                      format.applyPattern("#");  
                  }  
                  result = format.format(value);  
              }  
              break;  
          case Cell.CELL_TYPE_STRING:// String类型  
              result = cell.getRichStringCellValue().toString();  
              break;  
          case Cell.CELL_TYPE_BLANK:  
              result = "";  
              break; 
          default:  
              result = "";  
              break;  
          }  
          return result;  
      }
      
      /**
       * 转换格式，防止被当作标签
       * @author lucheng
       * @date 2021年1月6日 下午3:28:12
       * @param cellValue
       * @return
       */
      private static String convertFormat(String cellValue) {
    	  String result = ""; 
    	  if(!StringUtils.isBlank(cellValue)){
    		  result = cellValue.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    	  }
    	  return result;
      }
      
      /**
       * 按照表格的样式处理表格并展现
       * @param wb
       * @param sheet
       * @param sb
       */
      private static void dealExcelStyle(Workbook wb,Sheet sheet,Cell cell,StringBuffer sb){
          
          CellStyle cellStyle = cell.getCellStyle();
          if (cellStyle != null) {
              short alignment = cellStyle.getAlignment();
          //    sb.append("align='" + convertAlignToHtml(alignment) + "' ");//单元格内容的水平对齐方式
              short verticalAlignment = cellStyle.getVerticalAlignment();
              sb.append("valign='"+ convertVerticalAlignToHtml(verticalAlignment)+ "' ");//单元格中内容的垂直排列方式
              
              if (wb instanceof XSSFWorkbook) {
                              
                  XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont(); 
                  short boldWeight = xf.getBoldweight();
                  String  align = convertAlignToHtml(alignment);
                  sb.append("style='");
                  sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
                  sb.append("font-size: " + xf.getFontHeight() / 2 + "%;"); // 字体大小
                  int columnWidth = sheet.getColumnWidth(cell.getColumnIndex()) ;
                  sb.append("width:" + columnWidth + "px;");
                  sb.append("text-align:" + align + ";");//表头排版样式
                  XSSFColor xc = xf.getXSSFColor();
                  if (xc != null && !"".equals(xc)) {
                      sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
                  }
                  
                  XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
                  if (bgColor != null && !"".equals(bgColor)) {
                      sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
                  }
                  sb.append(getBorderStyle(0,cellStyle.getBorderTop(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
                  sb.append(getBorderStyle(1,cellStyle.getBorderRight(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
                  sb.append(getBorderStyle(2,cellStyle.getBorderBottom(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
                  sb.append(getBorderStyle(3,cellStyle.getBorderLeft(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));
                      
              }else if(wb instanceof HSSFWorkbook){
                  
                  HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
                  short boldWeight = hf.getBoldweight();
                  short fontColor = hf.getColor();
                  sb.append("style='");
                  HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
                  HSSFColor hc = palette.getColor(fontColor);
                  sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
                  sb.append("font-size: " + hf.getFontHeight() / 2 + "%;"); // 字体大小
                  String  align = convertAlignToHtml(alignment);
                  sb.append("text-align:" + align + ";");//表头排版样式
                  String fontColorStr = convertToStardColor(hc);
                  if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
                      sb.append("color:" + fontColorStr + ";"); // 字体颜色
                  }
                  int columnWidth = sheet.getColumnWidth(cell.getColumnIndex()) ;
                  sb.append("width:" + columnWidth + "px;");
                  short bgColor = cellStyle.getFillForegroundColor();
                  hc = palette.getColor(bgColor);
                  String bgColorStr = convertToStardColor(hc);
                  if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
                      sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
                  }
                  sb.append( getBorderStyle(palette,0,cellStyle.getBorderTop(),cellStyle.getTopBorderColor()));
                  sb.append( getBorderStyle(palette,1,cellStyle.getBorderRight(),cellStyle.getRightBorderColor()));
                  sb.append( getBorderStyle(palette,3,cellStyle.getBorderLeft(),cellStyle.getLeftBorderColor()));
                  sb.append( getBorderStyle(palette,2,cellStyle.getBorderBottom(),cellStyle.getBottomBorderColor()));
              }
  
              sb.append("' ");
          }
      }
      
      /**
       * 自定义表格样式展现
       * @param wb
       * @param sheet
       * @param sb
       * @param render 是否渲染固定的#F2F2F2颜色
       */
      private static void customExcelStyle(Workbook wb,Sheet sheet,Cell cell,StringBuffer sb, boolean render){
          
          CellStyle cellStyle = cell.getCellStyle();
          if (cellStyle != null) {
              short alignment = cellStyle.getAlignment();
          //    sb.append("align='" + convertAlignToHtml(alignment) + "' ");//单元格内容的水平对齐方式
              short verticalAlignment = cellStyle.getVerticalAlignment();
              sb.append("valign='"+ convertVerticalAlignToHtml(verticalAlignment)+ "' ");//单元格中内容的垂直排列方式
              
              if (wb instanceof XSSFWorkbook) {
                              
                  XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont(); 
                  short boldWeight = xf.getBoldweight();
                  String  align = convertAlignToHtml(alignment);
                  sb.append("style='");
                  sb.append("font-family:微软雅黑,Arial;"); // 字体样式 设置为微软雅黑
                  sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
                  sb.append("font-size: " + xf.getFontHeight() / 2 + "%;"); // 字体大小
                  int columnWidth = sheet.getColumnWidth(cell.getColumnIndex()) ;
                  sb.append("width:" + columnWidth + "px;");
                  sb.append("text-align:" + align + ";");//表头排版样式
                  XSSFColor xc = xf.getXSSFColor();
                  if (xc != null && !"".equals(xc)) {
                      sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
                  }
                  
                  XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
                  if (bgColor != null && !"".equals(bgColor)) {
                	  if(!render){
                		  sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
                	  }else{
                		  sb.append("background-color:#F2F2F2;"); // 背景颜色
                	  }
                  }
                  if(!render){
                	  if (bgColor != null && !"".equals(bgColor)) {
                		  sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
                      }
            	  }else{
            		  sb.append("background-color:#F2F2F2;"); // 背景颜色
            	  }
                  sb.append(getBorderStyle(0,cellStyle.getBorderTop(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
                  sb.append(getBorderStyle(1,cellStyle.getBorderRight(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
                  sb.append(getBorderStyle(2,cellStyle.getBorderBottom(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
                  sb.append(getBorderStyle(3,cellStyle.getBorderLeft(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));
                      
              }else if(wb instanceof HSSFWorkbook){
                  
                  HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
                  short boldWeight = hf.getBoldweight();
                  short fontColor = hf.getColor();
                  sb.append("style='");
                  HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
                  HSSFColor hc = palette.getColor(fontColor);
                  sb.append("font-family:微软雅黑,Arial;"); // 字体样式 设置为微软雅黑
                  sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
                  sb.append("font-size: " + hf.getFontHeight() / 2 + "%;"); // 字体大小
                  String  align = convertAlignToHtml(alignment);
                  sb.append("text-align:" + align + ";");//表头排版样式
                  String fontColorStr = convertToStardColor(hc);
                  if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
                      sb.append("color:" + fontColorStr + ";"); // 字体颜色
                  }
                  int columnWidth = sheet.getColumnWidth(cell.getColumnIndex()) ;
                  sb.append("width:" + columnWidth + "px;");
                  short bgColor = cellStyle.getFillForegroundColor();
                  hc = palette.getColor(bgColor);
                  String bgColorStr = convertToStardColor(hc);
                  if(!render){
                	  if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
                		  sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
                      }
            	  }else{
            		  sb.append("background-color:#F2F2F2;"); // 背景颜色
            	  }
                  sb.append( getBorderStyle(palette,0,cellStyle.getBorderTop(),cellStyle.getTopBorderColor()));
                  sb.append( getBorderStyle(palette,1,cellStyle.getBorderRight(),cellStyle.getRightBorderColor()));
                  sb.append( getBorderStyle(palette,3,cellStyle.getBorderLeft(),cellStyle.getLeftBorderColor()));
                  sb.append( getBorderStyle(palette,2,cellStyle.getBorderBottom(),cellStyle.getBottomBorderColor()));
              }
  
              sb.append("' ");
          }
      }
      
      /**
       * 单元格内容的水平对齐方式
       * @param alignment
       * @return
       */
      private static String convertAlignToHtml(short alignment) {
  
          String align = "center";
          switch (alignment) {
          case CellStyle.ALIGN_LEFT:
              align = "left";
              break;
          case CellStyle.ALIGN_CENTER:
              align = "center";
              break;
          case CellStyle.ALIGN_RIGHT:
              align = "right";
              break;
          default:
              break;
          }
          return align;
      }
  
      /**
       * 单元格中内容的垂直排列方式
       * @param verticalAlignment
       * @return
       */
      private static String convertVerticalAlignToHtml(short verticalAlignment) {
  
          String valign = "middle";
          switch (verticalAlignment) {
          case CellStyle.VERTICAL_BOTTOM:
              valign = "bottom";
              break;
          case CellStyle.VERTICAL_CENTER:
              valign = "center";
              break;
          case CellStyle.VERTICAL_TOP:
              valign = "top";
              break;
          default:
              break;
          }
          return valign;
      }
      
      private static String convertToStardColor(HSSFColor hc) {
  
          StringBuffer sb = new StringBuffer("");
          if (hc != null) {
              if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
                  return null;
              }
              sb.append("#");
              for (int i = 0; i < hc.getTriplet().length; i++) {
                  sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
              }
          }
  
          return sb.toString();
      }
      
      private static String fillWithZero(String str) {
          if (str != null && str.length() < 2) {
              return "0" + str;
          }
          return str;
      }
  
      private static  String getBorderStyle(  HSSFPalette palette ,int b,short s, short t){
           
          if(s==0)return  bordesr[b]+borderStyles[s]+"#d0d7e5 1px;";;
          String borderColorStr = convertToStardColor( palette.getColor(t));
          borderColorStr=borderColorStr==null|| borderColorStr.length()<1?"#000000":borderColorStr;
          return bordesr[b]+borderStyles[s]+borderColorStr+" 1px;";
          
      }
      
      private static  String getBorderStyle(int b,short s, XSSFColor xc){
           
           if(s==0)return  bordesr[b]+borderStyles[s]+"#d0d7e5 1px;";;
           if (xc != null && !"".equals(xc)) {
               String borderColorStr = xc.getARGBHex();//t.getARGBHex();
               borderColorStr=borderColorStr==null|| borderColorStr.length()<1?"#000000":borderColorStr.substring(2);
               return bordesr[b]+borderStyles[s]+borderColorStr+" 1px;";
           }
          
           return "";
      }
      
      /**
       * 生成完整的html内容
       * @author lucheng
       * @date 2021年1月6日 下午4:20:18
       * @param content 生成的excel表格标签
       * @return
       */
      private static String convertHtml(String content){
           StringBuilder sb = new StringBuilder();
           sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Html Test</title></head><body>");
           sb.append("<div>");
           sb.append(content);
           sb.append("</div>");
           sb.append("</body></html>");
           return sb.toString();//回传字符串
      }
      
      /*
       * @param content 生成的excel表格标签
       * @param htmlPath 生成的html文件地址
      */
      private static void writeFile(String content,String htmlPath){
          File file2 = new File(htmlPath);
           StringBuilder sb = new StringBuilder();
           try {
	            file2.createNewFile();//创建文件
	          
	            sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Html Test</title><%@ include file=\"../../../taglibs/jquery.jsp\"%><%@ include file=\"../../../taglibs/easyui.jsp\"%><script type=\"text/javascript\" src=\"${pageContext.request.contextPath}/js/common.js\" ></script></head><body>");
	            sb.append("<div>");
	            sb.append(content);
	            sb.append("</div>");
	            sb.append("</body></html>");
	           
	            PrintStream printStream = new PrintStream(new FileOutputStream(file2));
	           
	            printStream.println(sb.toString());//将字符串写入文件
           
           } catch (IOException e) {
        	   e.printStackTrace();
           }
          
      }
     
}
