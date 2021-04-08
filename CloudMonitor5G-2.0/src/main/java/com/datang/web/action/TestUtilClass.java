package com.datang.web.action;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

/**
 * 测试类
 * @author lucheng
 * @date 2020年12月23日 下午1:01:23
 */
public class TestUtilClass {

	public static void main(String[] args) {
		
		
		System.out.println(System.nanoTime());
		
		Workbook workBook = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String filePath = "E:/ZZZ/stationReport/uploadReportTemplate/testExcel.xls";
		String filePath2 = "E:/ZZZ/stationReport/uploadReportTemplate/testExcel_fin.xls";
		try {
			//读取指定路径下的excel
			fis = new FileInputStream(filePath);
			//加载到workBook
			workBook = new HSSFWorkbook(fis);
			int numberOfSheets = workBook.getNumberOfSheets();
			for (int k = 0; k < numberOfSheets; k++) {
				//获取第一个sheet页
				Sheet sheetAt = workBook.getSheetAt(k);
				sheetAt.shiftRows(6,sheetAt.getLastRowNum(), 3, true, false);
				//获取excel有多少条数据
				int rowSize = sheetAt.getLastRowNum()+1;
				//遍历所有的数据
				for (int i = 0; i < 3; i++) {
					Row row = sheetAt.createRow(6 + i);
					//获取第I行的数据
//					Row row = sheetAt.getRow(i);
					//创建第I行  第4列的单位格
					Cell cell = row.createCell(3);
					//设置值
					NumberFormat nf = NumberFormat.getNumberInstance();
					nf.setMaximumFractionDigits(1);
					String value = nf.format(33.8144);
					cell.setCellValue(value);
					//设置单位格的风格
					CellStyle style = workBook.createCellStyle();
					//创建字体
					Font font = workBook.createFont();
					//设置字体的颜色
					font.setColor(Font.COLOR_NORMAL);
					style.setFont(font);
					//设置下
					style.setBorderBottom((short)1);
					style.setBorderTop((short)1);
					style.setBorderLeft((short)1);
					style.setBorderRight((short)1);
					cell.setCellStyle(style);
				}
			}
			//将更改后的excel写回去
			fos = new FileOutputStream(filePath2);
			workBook.write(fos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(workBook!=null){
				try {
					workBook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


	}
	
	/**
	 * 提取中括号中内容，忽略中括号中的中括号
	 * @param msg
	 * @return
	 */
	public static List<String> extractMessage(String msg) {
 
		List<String> list = new ArrayList<String>();
		int start = 0;
		int startFlag = 0;
		int endFlag = 0;
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '<') {
				startFlag++;
				if (startFlag == endFlag + 1) {
					start = i;
				}
			} else if (msg.charAt(i) == '>') {
				endFlag++;
				if (endFlag == startFlag) {
					list.add(msg.substring(start + 1, i));
				}
			}
		}
		return list;
	}


}
