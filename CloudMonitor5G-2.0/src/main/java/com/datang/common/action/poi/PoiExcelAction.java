/**
 * 
 */
package com.datang.common.action.poi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;

/**
 * POI导出Excel公用action
 * 子类继承该类是需在页面设置rows--每页显示的记录条数,page--查询的当前页码,orderDirection--
 * 正序或者倒序,orderField--按那个字段排序,且复写doPageExcel方法解析自定义参数和根据需要返回响应的WorkBook
 * 
 * @author yinzhipeng
 * @date:2015年7月30日 上午10:03:47
 * @version
 */
@SuppressWarnings("all")
public abstract class PoiExcelAction extends PageAction {
	private static Logger LOGGER = LoggerFactory
			.getLogger(PoiExcelAction.class);

	/**
	 * 公用EXCEL导出Action,对应的结果集name为"downloadExcel",type为"stream", 且param
	 * name="inputName"为downloadExcel
	 * 
	 * @return
	 */
	public String downloadExcel() {
		return "downloadExcel";
	}

	public InputStream getDownloadExcel() {
		PageList pageList = new PageList();
		if (0 == this.getRows()) {// 导出当前条件的所有,默认只导出1w条
			pageList.setRowsCount(10000);
			pageList.setPageNum(1);
		} else {
			pageList.setRowsCount(this.getRows());
			pageList.setPageNum(this.getPage());
		}
		if (this.getOrderDirection() != null
				&& this.getOrderDirection().length() != 0) {
			pageList.setOrderDirection(this.getOrderDirection());
		}
		if (this.getOrderField() != null && this.getOrderField().length() != 0) {
			pageList.setOrderField(this.getOrderField());
		}
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			Workbook doPageExcel = doPageExcel(pageList);
			if (null != doPageExcel) {
				doPageExcel.write(byteOutputStream);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 
	 * 该方法传入PageList对象,PageList中封装了页面公用的查询参数(rows:每页显示的记录条,page:查询的当前页码,
	 * orderDirection:正序或者倒序,orderField:按那个字段排序),根据需要返回WorkBook
	 * 子类必须覆盖此方法,子类需自己实现自定义的页面查询条件的封装
	 * 
	 * @param pageList
	 * @return Workbook 包含用pageList参数查询到的数据集生成的Excel
	 * @throws IOException
	 */
	public abstract Workbook doPageExcel(PageList pageList) throws IOException;

}
