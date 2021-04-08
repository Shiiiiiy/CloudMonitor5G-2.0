package com.datang.service.sql.impl;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.datang.common.service.sql.GroupByService;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;

/**
 * 获取分组的抽象类
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 下午3:26:02
 * @version
 */
@Service
public abstract class GroupByServiceBean implements GroupByService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.common.service.sql.GroupByService#getGroupBy()
	 */
	@Override
	public String getGroupBy() {
		StringBuffer groupByInfo = new StringBuffer();
		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(param
				.getClass());
		if (null == fields || 0 == fields.length) {
			return groupByInfo.toString();
		}
		try {
			Object groupByValue = null;
			for (PropertyDescriptor field : fields) {
				String fieldName = field.getName();
				if (fieldName.equals(ParamConstant.GROUPBY_FIELD)) {
					groupByValue = ReflectUtil.getField(param, fieldName);
				}
			}
			if (null != groupByValue && groupByValue instanceof String) {
				groupByInfo.append((String) groupByValue);
			}
		} catch (Exception e) {
		}
		return groupByInfo.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.common.service.sql.GroupByService#getGroupByResult()
	 */
	@Override
	public String getGroupByResult() {
		return new StringBuffer().toString();
	}

	/**
	 * 
	 */
	protected Object param;

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
