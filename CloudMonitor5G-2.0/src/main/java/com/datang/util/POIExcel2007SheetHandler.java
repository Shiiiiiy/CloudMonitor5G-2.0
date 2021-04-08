/**
 * 
 */
package com.datang.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * POI的Excel2007解析(XML解析器)
 * 
 * @author yinzhipeng
 * @date:2016年8月29日 下午12:15:35
 * @version
 */
public class POIExcel2007SheetHandler extends DefaultHandler {
	private SharedStringsTable sst;
	private String lastContents;// 单元格内容
	private boolean nextIsString;

	private List<String> headlist = new ArrayList<String>();// sheet的表头信息
	private List<String[]> rowList = new ArrayList<>();// sheet的数据行信息
	private String[] curList;// 当前行数据
	private int curRow = 0;// 当前行
	private int curCol = 0;// 当前单元格
	private int cellIndex = -1;// 单元格角标

	public POIExcel2007SheetHandler(SharedStringsTable sst) {
		this.sst = sst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (name.equals("c")) {// c => cell单元格开始
			// 获取单元格"AAZ1"格式在数组中的角标
			String excelCellIndex = attributes.getValue("r");
			cellIndex = columnToIndex(excelCellIndex.replaceAll("[0-9]+", "")) - 1;
			// 如果单元格是字符串则v标签的值为该字符串在SST中的索引
			String cellType = attributes.getValue("t");
			if (cellType != null && cellType.equals("s")) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
		} else if (name.equals("row")) {// row => row行开始
			if (0 != curRow) {// sheet的行(不包括表头)
				curList = new String[headlist.size()];
				cellIndex = -1;
			}
		}
		// 清除单元格内容
		lastContents = "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
		if (nextIsString) {
			int idx = Integer.parseInt(lastContents);
			lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
					.toString();
			nextIsString = false;
		}
		// c => 单元格结束
		if (0 == curRow) {// 处理sheet的表头,将单元格内容加入headlist中，在这之前先去掉字符串前后的空白符
			if (name.equals("c")) {
				if (lastContents.trim().equals("")) {
					headlist.add(curCol, null);
				} else {
					headlist.add(curCol, lastContents.trim());
				}
				curCol++;
			} else if (name.equals("row")) {// 如果标签名称为 row ，这说明已到行尾
				curRow++;
				curCol = 0;
				cellIndex = -1;
			}
		} else {// 处理sheet的行(不包括表头),将单元格内容加入rowList中，在这之前先去掉字符串前后的空白符
			if (name.equals("c")) {
				if (cellIndex < headlist.size()) {
					if (lastContents.trim().equals("")) {
						curList[cellIndex] = null;
					} else {
						curList[cellIndex] = lastContents.trim();
					}
				}
				cellIndex = -1;
				curCol++;
			} else if (name.equals("row")) {// 如果标签名称为 row ，这说明已到行尾
				rowList.add(curList);
				curRow++;
				curCol = 0;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		lastContents += new String(ch, start, length);
	}

	/**
	 * @return the headlistheadlist
	 */
	public List<String> getHeadlist() {
		return headlist;
	}

	/**
	 * @return the rowListrowList
	 */
	public List<String[]> getRowList() {
		return rowList;
	}

	/**
	 * 用于将Excel表格中列号字母转成列索引，从1对应A开始
	 * 
	 * @param column
	 *            列号
	 * @return 列索引
	 */
	private static int columnToIndex(String column) {
		if (!column.matches("[A-Z]+")) {
			try {
				throw new Exception("Invalid parameter");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int index = 0;
		char[] chars = column.toUpperCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			index += ((int) chars[i] - (int) 'A' + 1)
					* (int) Math.pow(26d, chars.length - i - 1d);
		}
		return index;
	}

	/**
	 * 用于将excel表格中列索引转成列号字母，从A对应1开始
	 * 
	 * @param index
	 *            列索引
	 * @return 列号
	 */
	private static String indexToColumn(int index) {
		if (index <= 0) {
			try {
				throw new Exception("Invalid parameter");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		index--;
		String column = "";
		do {
			if (column.length() > 0) {
				index--;
			}
			column = ((char) (index % 26 + (int) 'A')) + column;
			index = (int) ((index - index % 26) / 26);
		} while (index > 0);
		return column;
	}

	public static void main(String[] args) {
		String test = "AA1123".replaceAll("[0-9]+", "");
		String test1 = "AG1123".replaceAll("[0-9]+", "");
		System.out.println(columnToIndex(test));
		System.out.println(columnToIndex(test1));
	}
}
