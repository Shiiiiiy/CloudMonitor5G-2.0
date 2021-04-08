package com.datang.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtil {

	/**
	 * Log
	 */
	protected static final Logger LOG = LoggerFactory
			.getLogger(ReflectUtil.class);

	/**
	 * Get the field value with the field name of the obj<Object>.
	 * 
	 * @param obj
	 *            Object
	 * @param fieldName
	 *            the field name of the obj
	 * @return the field value of the obj
	 */
	public static Object getField(Object obj, String fieldName) {

		try {
			Method method = findGetMethod(obj.getClass(), fieldName);
			if (null != method) {
				return method.invoke(obj);
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error("ReflectUtil getField fieldName:" + fieldName
					+ " error :" + e.getMessage());
			return null;
		}
	}

	/**
	 * Get the field value with the field of the obj<Object>.
	 * 
	 * @param obj
	 *            Object
	 * @param field
	 *            the <Field>field of the obj
	 * @return the field value of the obj
	 */
	public static Object getField(Object obj, Field field) {
		String fieldName = field.getName();

		try {
			Method method = findGetMethod(obj.getClass(), fieldName);
			if (null != method) {
				return method.invoke(obj);
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error("ReflectUtil getField  error :" + e.getMessage());
			return null;
		}

	}

	/**
	 * Get the field value with the field of the obj<Object>.
	 * 
	 * @param obj
	 *            Object
	 * @param field
	 *            the <PropertyDescriptor>field of the obj
	 * @return the field value of the obj
	 */
	public static Object getField(Object obj, PropertyDescriptor field) {
		String fieldName = field.getName();

		try {
			Method method = findGetMethod(obj.getClass(), fieldName);
			if (null != method) {
				return method.invoke(obj);
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error("ReflectUtil getField  error :" + e.getMessage());
			return null;
		}

	}

	/**
	 * Get the fields' value with the fields' name of the obj<Object>. Build a
	 * <Map> fieldMap.The key of fieldMap is the field name of this objet, and
	 * the value of fieldMap is the field value of this object
	 * 
	 * @param obj
	 *            Object
	 * @param fieldName
	 *            the field name of the obj
	 * @return the field value of the obj
	 */
	public static Map<String, Object> getFields(Object obj, String[] fieldNames) {
		Map<String, Object> fieldMap = new HashMap<String, Object>();

		try {
			Method method = null;
			for (String fieldName : fieldNames) {
				method = findGetMethod(obj.getClass(), fieldName);
				if (null != method) {
					Object fieldValu = method.invoke(obj);
					fieldMap.put(fieldName, fieldValu);
				}
			}
		} catch (Exception e) {
			LOG.error("ReflectUtil getFields  error :" + e.getMessage());
		}
		return fieldMap;
	}

	/**
	 * get All field include parents' private fields
	 * 
	 * @param obj
	 * @return
	 */
	public static Field[] getAllFiled(Object obj) {

		// new a empity fieldlist
		List<Field> fieldList = new ArrayList<Field>();

		// recursion fill the field
		fillFieldList(fieldList, obj.getClass());

		return fieldList.toArray(new Field[fieldList.size()]);
	}

	/**
	 * recursion:fillFieldList
	 * 
	 * @param fieldList
	 * @param oneClass
	 */
	private static void fillFieldList(List<Field> fieldList, Class<?> oneClass) {

		// get the fields of the Class
		Field[] fields = oneClass.getDeclaredFields();

		// fill the fieldList
		fieldList.addAll(Arrays.asList(fields));

		// if have parent : recursion
		if (null != oneClass.getSuperclass())
			fillFieldList(fieldList, oneClass.getSuperclass());
	}

	/**
	 * Set the field value with the field name and value of the obj<Object>.
	 * 
	 * @param obj
	 *            a <Object>
	 * @param fieldName
	 *            the field name of the obj
	 * @param fieldValue
	 *            the field value of the obj
	 */
	public static void setField(Object obj, String fieldName, Object fieldValue) {

		try {
			Method method = findSetMethod(obj.getClass(), fieldName, fieldValue);
			if (null != method) {
				method.invoke(obj, fieldValue);
			}
		} catch (Exception e) {
			LOG.error("ReflectUtil setField fieldName:" + fieldName
					+ " value: " + fieldValue + " error :" + e.getMessage());
		}

	}

	/**
	 * Set the fields of this obj with a <Map> fieldMap. The key of fieldMap is
	 * the field name of this objet, and the value of fieldMap is the field
	 * value of this object.
	 * 
	 * @param obj
	 *            a <Object>
	 * @param fieldMap
	 *            <fieldName, fieldValue>
	 */
	public static void setFields(Object obj, Map<String, Object> fieldMap) {
		try {

			// get the Class by reflect
			Class<?> oneClass = obj.getClass();

			// get the fields of the Class
			Field[] fields = oneClass.getDeclaredFields();

			// build new instance
			for (Field field : fields) {
				String fieldName = field.getName();// get Field Name
				if (fieldMap.containsKey(fieldName)) {
					Method method = findSetMethod(obj.getClass(), fieldName,
							fieldMap.get(fieldName));
					if (method != null) {
						method.invoke(obj, fieldMap.get(fieldName));
					}

				}
			}

		} catch (Exception e) {
			LOG.error("ReflectUtil setFields error:" + e.getMessage());
		}

	}

	/**
	 * recursion find the method from this Class and its parent
	 * 
	 * @param oneClass
	 *            <Class>
	 * @param fieldName
	 *            the field name of the Class
	 * @param fieldValue
	 *            the value of the field
	 * @param method
	 *            the set method of the field
	 * @return
	 */
	public static Method findSetMethod(Class<?> oneClass, String fieldName,
			Object fieldValue) {

		Method method = null;
		if (null == oneClass || null == fieldName || 0 == fieldName.length()
				|| null == fieldValue)
			return method;

		try {
			// find set method from this Class
			method = oneClass.getDeclaredMethod(
					"set" + fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1), fieldValue.getClass());
		} catch (NoSuchMethodException e) {
			// find set method from its parent Class
			method = findSetMethod(oneClass.getSuperclass(), fieldName,
					fieldValue);
		}
		return method;

	}

	/**
	 * recursion find the method from this Class and its parent
	 * 
	 * @param oneClass
	 *            <Class>
	 * @param fieldName
	 *            the field name of the Class
	 * @param method
	 *            the get method of the field
	 * @return
	 */
	public static Method findGetMethod(Class<?> oneClass, String fieldName) {

		Method method = null;
		if (null == oneClass || null == fieldName || 0 == fieldName.length())
			return method;

		try {
			// find get method from this Class
			method = oneClass.getDeclaredMethod("get"
					+ fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1));
		} catch (NoSuchMethodException e) {
			// find get method from its parent Class
			method = findGetMethod(oneClass.getSuperclass(), fieldName);
		}
		return method;

	}

}
