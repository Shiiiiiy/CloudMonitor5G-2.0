/**
 * 
 */
package com.datang.common.action.page;

import java.util.ArrayList;
import java.util.List;

/**
 * easyui分页返回Bean专用
 * 
 * @author yinzhipeng
 * @date:2015年6月12日 下午4:23:34
 * @version
 */
public class EasyuiPageList extends AbstractPageList {
	private List footer = new ArrayList();

	/**
	 * @return the footerfooter
	 */
	public List getFooter() {
		return footer;
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public void setFooter(List footer) {
		this.footer = footer;
	}

}
