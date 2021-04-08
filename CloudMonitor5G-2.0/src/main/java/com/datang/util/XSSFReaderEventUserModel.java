/**
 * 
 */
package com.datang.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * POI的EXCEL2007中 XSSFReaderEventUserModel解析器
 * 
 * @author yinzhipeng
 * @date:2016年8月29日 下午12:20:02
 * @version
 */
public class XSSFReaderEventUserModel {
	private List<String> head = new ArrayList<>();// 表头
	private List<String[]> rows = new ArrayList<>();// 行内容

	/**
	 * 解析绝对路径的Excel2007(只解析第一个sheet)
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void processOneSheet(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		InputStream sheet = r.getSheet("rId1");
		InputSource sheetSource = new InputSource(sheet);
		parser.parse(sheetSource);
		sheet.close();
		POIExcel2007SheetHandler sheetHandler = (POIExcel2007SheetHandler) parser
				.getContentHandler();
		head = sheetHandler.getHeadlist();
		rows = sheetHandler.getRowList();

	}

	/**
	 * 解析当前文件的Excel2007(只解析第一个sheet)
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void processOneSheet(File file) throws Exception {
		OPCPackage pkg = OPCPackage.open(file);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		InputStream sheet = r.getSheet("rId1");
		InputSource sheetSource = new InputSource(sheet);
		parser.parse(sheetSource);
		sheet.close();
		POIExcel2007SheetHandler sheetHandler = (POIExcel2007SheetHandler) parser
				.getContentHandler();
		head = sheetHandler.getHeadlist();
		rows = sheetHandler.getRowList();
	}

	// 解析绝对路径下的Excel2007(只解析全部sheet)
	// public void processAllSheets(String filename) throws Exception {
	// OPCPackage pkg = OPCPackage.open(filename);
	// XSSFReader r = new XSSFReader(pkg);
	// SharedStringsTable sst = r.getSharedStringsTable();
	// XMLReader parser = fetchSheetParser(sst);
	// Iterator<InputStream> sheets = r.getSheetsData();
	// while (sheets.hasNext()) {
	// System.out.println("Processing new sheet:\n");
	// InputStream sheet = sheets.next();
	// InputSource sheetSource = new InputSource(sheet);
	// parser.parse(sheetSource);
	// sheet.close();
	// }
	// }
	private XMLReader fetchSheetParser(SharedStringsTable sst)
			throws SAXException, ParserConfigurationException {
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader parser = saxParser.getXMLReader();
		ContentHandler handler = new POIExcel2007SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * 解析后的表头
	 * 
	 * @return the head
	 */
	public List<String> getHead() {
		return head;
	}

	/**
	 * 解析后的行内容
	 * 
	 * @return the rows
	 */
	public List<String[]> getRows() {
		return rows;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(System.currentTimeMillis());
		XSSFReaderEventUserModel example = new XSSFReaderEventUserModel();
		// example.processOneSheet("D:\\上海移动投诉项目\\GSM小区表.xlsx");
		example.processOneSheet("D:\\基站数据库文件GSM0919.xlsx");
		// example.processOneSheet("D:\\上海移动投诉项目\\基站数据库文件GSM.xlsx");
		// example.processAllSheets("D:\\上海移动投诉项目\\GSM小区表.xlsx");
		System.out.println(System.currentTimeMillis());
		List<String> head2 = example.getHead();
		List<String[]> rows2 = example.getRows();
		System.out.println(head2.size());
		System.out.println(rows2.size());
		System.out.println(rows2.get(0).length);
		for (String string : head2) {
			System.out.println(string);
		}
		System.out.println(System.currentTimeMillis());
	}
}