package com.datang.common.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析Excel文件, 组成数据对象：该解析器是一个解析器游标，可以逐行遍历数据。行元素是一个List<?>，行中的CELL元素是<?>
 * 
 * @author zhangchongfeng
 * 
 */
public class ExcelReader implements Iterator<List<?>> {

	private static final Logger logger = LoggerFactory
			.getLogger(ExcelReader.class);// LOG

	private InputStream is = null;// 输入流

	private Iterator<?> rowIterator = null;// 行游标,用于逐行遍历

	private Integer rowNum;// Excel读取器当前读取的记录行的行号

	private Workbook book;// 文件的Workbook

	private Sheet sheet;// 文件的Sheet

	/**
	 * 构造函数
	 * 
	 * @param excelFile
	 *            EXCEL文件的路径
	 */
	public ExcelReader(String excelFile) {
		book = null;// 文件的Workbook
		sheet = null;// 文件的Sheet
		try {
			is = new FileInputStream(excelFile);
			book = WorkbookFactory.create(is);
			sheet = book.getSheetAt(0);
			rowIterator = sheet.rowIterator();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param excelFile
	 *            EXCEL文件
	 */
	public ExcelReader(File excelFile) {
		book = null;// 文件的Workbook
		sheet = null;// 文件的Sheet
		try {
			is = new FileInputStream(excelFile);
			book = WorkbookFactory.create(is);
			sheet = book.getSheetAt(0);
			rowIterator = sheet.rowIterator();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 是否有存在下一行
	 */
	public boolean hasNext() {
		return rowIterator.hasNext();
	}

	/**
	 * 跳转到包含该字符串的Sheet,并返回Sheet Name
	 * 
	 * @param sheetName
	 */
	public String goToSheet(String containedSheetName) {
		String sheetName = new String();

		if (null != book) {
			for (int i = 0; i < book.getNumberOfSheets(); i++) {
				Sheet sheet = book.getSheetAt(i);
				if (sheet.getSheetName().contains(containedSheetName)) {
					sheetName = sheet.getSheetName();
					this.sheet = sheet;
					rowIterator = sheet.rowIterator();
				}
			}
		}

		return sheetName;

	}

	/**
	 * 跳转到包含该字符串的Sheet,并返回Sheet Name
	 * 
	 * @param sheetName
	 */
	public String goToSheet(int index) {
		String sheetName = new String();

		if (null != book) {
			if (index < book.getNumberOfSheets()) {
				Sheet sheet = book.getSheetAt(index);
				return sheet.getSheetName();
			}
		}

		return sheetName;

	}

	/**
	 * 获取下一行数据
	 */
	public List<String> next() {

		List<String> elementsInEveryRow = new ArrayList<String>();// 一行数据

		if (hasNext()) {

			HSSFRow row = (HSSFRow) rowIterator.next();

			/**
			 * set rowNum: row.getRowNum()从0开始,而真正数据记录的行号从1开始
			 */
			setRowNum(row.getRowNum() + 1);

			/**
			 * 读取Excel一行数据
			 */
			for (int i = 0; i < row.getLastCellNum(); i++) {
				HSSFCell cell = row.getCell(i);
				elementsInEveryRow.add(this.getCellValue(cell));
			}
		}

		return elementsInEveryRow;
	}

	/**
	 * Remove
	 */
	public void remove() {
		throw new UnsupportedOperationException("EXCEL解析器是只读的");
	}

	/**
	 * 获取Cell中的值
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(HSSFCell cell) {
		String value = new String();

		if (null == cell)
			return value;

		// 简单的查检列类型
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:// 字符串
			value = cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字
			// 读取实数
			double dd = cell.getNumericCellValue();
			long l = (long) dd;

			if (dd - l > 0) {
				// 说明是Double
				value = new Double(dd).toString().trim();
			} else {
				// 说明是Long
				value = new Long(l).toString().trim();
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = new String();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula()).trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:// boolean型值
			value = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue()).trim();
			break;
		default:
			break;
		}
		return value;
	}

	public void close() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}// close

	public static void main(String[] args) {
		ExcelReader ep = new ExcelReader("d:/zcf.xls");
		while (ep.hasNext()) {
			List<?> row = ep.next();
			System.out.println(row.get(0) + ", " + row.get(1));
		}
		ep.close();
	}

	/**
	 * @return the rowIterator
	 */
	public Iterator<?> getRowIterator() {
		return rowIterator;
	}

	/**
	 * @param rowIterator
	 *            the rowIterator to set
	 */
	public void setRowIterator(Iterator<?> rowIterator) {
		this.rowIterator = rowIterator;
	}

	/**
	 * @return the rowNum
	 */
	public Integer getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum
	 *            the rowNum to set
	 */
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

}
