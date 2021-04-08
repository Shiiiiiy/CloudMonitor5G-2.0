package com.datang.service.sql.impl;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.datang.common.service.sql.OrderByService;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;

@Service
public class OrderByServiceBean implements OrderByService {

	/**
	 * 
	 */
	private Object param;

	/**
	 * 1.second 获取Order By语句
	 * 
	 * @param param
	 * @return
	 */
	public String getOrderBy() {

		StringBuffer orderByInfo = new StringBuffer();

		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(param
				.getClass());

		if (null == fields || 0 == fields.length)
			return orderByInfo.toString();

		try {
			Object orderByStatementValue = null;

			for (PropertyDescriptor field : fields) {
				String fieldName = field.getName();
				if (fieldName.equals(ParamConstant.ORDERBY_STATEMENT))
					orderByStatementValue = ReflectUtil.getField(param,
							fieldName);
			}

			orderByInfo.append(getOrderByInfo(orderByStatementValue));

		} catch (Exception e) {

		}

		return orderByInfo.toString();
	}

	/**
	 * 获取排序信息
	 * 
	 * @param orderByStatementValue
	 * @return
	 */
	private StringBuffer getOrderByInfo(Object orderByStatementValue) {
		StringBuffer orderByInfo = new StringBuffer();

		if (null == orderByStatementValue
				|| !(orderByStatementValue instanceof String))
			return orderByInfo;
		String orderBy = (String) orderByStatementValue;

		orderByInfo.append(orderBy);

		return orderByInfo;
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
