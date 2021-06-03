package com.datang.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对POI功能的扩展
 * 
 * @author yinzhipeng
 * @date:2015年7月30日 上午9:27:16
 * @version
 */
@SuppressWarnings("all")
public class POIExcelUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(POIExcelUtil.class);

	/**
	 * 删除excel中的sheet,只保留具有butSheetName的XSSFWorkbook
	 * 
	 * @param wb
	 *            :操作的excel
	 * @param butSheetName
	 *            :保留sheet的name
	 * @return
	 * @throws IOException
	 */
	public static Workbook removeOtherSheetButSheetName(Workbook wb,
			String butSheetName) throws IOException {
		int numberOfSheets = wb.getNumberOfSheets();
		int sheetIndex = wb.getSheetIndex(butSheetName);
		while (numberOfSheets != 1) {
			sheetIndex = wb.getSheetIndex(butSheetName);
			numberOfSheets = wb.getNumberOfSheets();
			inner: for (int j = 0; j < numberOfSheets; j++) {
				if (j != sheetIndex) {
					wb.removeSheetAt(j);
					break inner;
				}
			}
		}
		return wb;
	}


	/**
	 * 根据数据和Excel模板生成Excel
	 *
	 * @param beans
	 *            Map
	 * @param templateFilePath
	 *            模板文件路徑
	 * @return Workbook
	 * @throws IOException
	 */
	public static Workbook transformToExcel(Map beans, InputStream is) {
		// Transformer
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = null;
		try {
			workbook = transformer.transformXLS(is, beans);
		} catch (ParsePropertyException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InvalidFormatException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}
		return workbook;
	}


	/**
	 * 根据数据和Excel模板生成Excel
	 * 
	 * @param beans
	 *            Map
	 * @param templateFilePath
	 *            模板文件路徑
	 * @return Workbook
	 * @throws IOException
	 */
	public static Workbook transformToExcel(Map beans, String templateFilePath) {
		// Transformer
		XLSTransformer transformer = new XLSTransformer();
		InputStream is = ClassUtil.getResourceAsStream(templateFilePath);
		Workbook workbook = null;
		try {
			workbook = transformer.transformXLS(is, beans);
		} catch (ParsePropertyException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InvalidFormatException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}
		return workbook;
	}

	/**
	 * 删除excel中的sheet,只保留具有sheetName的XSSFWorkbook
	 * 
	 * @param beans
	 *            Map
	 * @param templateFilePath
	 *            模板文件路徑
	 * @return Workbook
	 * @throws IOException
	 */
	public static Workbook transformToExcel(Map beans, String templateFilePath,
			String sheetName) throws IOException {
		// 读取EXCEL模板
		InputStream is = ClassUtil.getResourceAsStream(templateFilePath);
		XLSTransformer transformer = new XLSTransformer();
		Workbook newWb = null;
		try {
			Workbook wb = transformer.transformXLS(is, beans);
			// 只保留一个sheet的excel
			newWb = POIExcelUtil.removeOtherSheetButSheetName(wb, sheetName);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new IOException(e.getMessage());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		return newWb;
	}

}
