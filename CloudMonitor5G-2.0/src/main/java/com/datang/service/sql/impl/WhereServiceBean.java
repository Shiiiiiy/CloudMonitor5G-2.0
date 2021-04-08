package com.datang.service.sql.impl;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.datang.common.service.sql.WhereService;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.ReflectUtil;

/**
 * 获取查询条件的抽象类
 * 
 * @author yinzhipeng
 * @date:2015年11月20日 下午3:32:33
 * @version
 */
@Service
public abstract class WhereServiceBean implements WhereService {

	/**
	 * 
	 */
	protected Object param;

	/**
	 * 获取where语句
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public String getWhere() {

		StringBuffer whereInfo = new StringBuffer();
		whereInfo.append(ParamConstant.IDENTICAL_EXP);
		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(param
				.getClass());
		if (null == fields || 0 == fields.length) {
			return whereInfo.toString();
		}
		try {

			Object testLogItemIdsValue = null;
			for (PropertyDescriptor field : fields) {
				String fieldName = field.getName();
				if (fieldName.equals(ParamConstant.TESTLOGITEM_IDS)) {
					testLogItemIdsValue = ReflectUtil
							.getField(param, fieldName);
				}
			}
			StringBuffer whereByTestLogItemIDS = getWhereByTestLogItemIDS(testLogItemIdsValue);
			if (0 != whereInfo.length() && 0 != whereByTestLogItemIDS.length()) {
				whereInfo.append(ParamConstant.SPACE);
				whereInfo.append(ParamConstant.AND);
				whereInfo.append(ParamConstant.SPACE);
				whereInfo.append(whereByTestLogItemIDS);
			} else {
				whereInfo.append(whereByTestLogItemIDS);
			}
		} catch (Exception e) {
		}
		return whereInfo.toString();
	}

	/**
	 * 根据TestLogItemIDS获取Where条件
	 * 
	 * @param testLogItemIdsValue
	 * @return
	 */
	protected StringBuffer getWhereByTestLogItemIDS(Object testLogItemIdsValue) {
		StringBuffer buffer = new StringBuffer();
		String testLogItemIds = null;
		if (null != testLogItemIdsValue
				&& (testLogItemIdsValue instanceof String)) {
			testLogItemIds = (String) testLogItemIdsValue;
			// RECSEQNO IN()
			buffer.append(ParamConstant.SPACE);
			buffer.append(ParamConstant.RECSEQNO);
			buffer.append(ParamConstant.SPACE);
			buffer.append(ParamConstant.IN);
			buffer.append(ParamConstant.LEFT_BRACKET);
			buffer.append(testLogItemIds);
			buffer.append(ParamConstant.RIGHT_BRACKET);
			buffer.append(ParamConstant.SPACE);
		}
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.WhereService#getWhereByIndicationCondition
	 * ()
	 */
	@Override
	public String getWhereByIndicationCondition() {
		StringBuffer whereInfo = new StringBuffer();
		whereInfo.append(ParamConstant.IDENTICAL_EXP);
		return whereInfo.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.service.sql.WhereService#getWhereByFilterEmptyKpi()
	 */
	@Override
	public String getWhereByFilterEmptyKpi() {
		// TODO Auto-generated method stub
		return new StringBuffer().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.common.service.sql.WhereService#getHaving()
	 */
	@Override
	public String getHaving() {
		// TODO Auto-generated method stub
		return new StringBuffer().toString();
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
