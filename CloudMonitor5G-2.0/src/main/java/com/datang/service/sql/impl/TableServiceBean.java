package com.datang.service.sql.impl;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.datang.common.service.sql.TableService;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;

/**
 * 获取表名的抽象类
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 下午4:51:54
 * @version
 */
@Service
public abstract class TableServiceBean implements TableService {

	/**
	 * 
	 */
	protected Object param;

	/**
	 * 1.first 获取TABLE NAME语句
	 * 
	 * @param param
	 * @return
	 */
	public String getTableName() {

		StringBuffer tableInfo = new StringBuffer();

		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(param
				.getClass());
		if (null == fields || 0 == fields.length) {
			return tableInfo.toString();
		}
		try {
			Object queryTableValue = null;

			for (PropertyDescriptor field : fields) {
				String fieldName = field.getName();
				if (fieldName.equals(ParamConstant.QUERY_TABLE)) {
					queryTableValue = ReflectUtil.getField(param, fieldName);
				}
			}
			if (null != queryTableValue && queryTableValue instanceof String) {
				tableInfo.append((String) queryTableValue);
			}
		} catch (Exception e) {
		}
		return tableInfo.toString();
	}

	/**
	 * @return the param
	 */
	public Object getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Object param) {
		this.param = param;
	}

}
