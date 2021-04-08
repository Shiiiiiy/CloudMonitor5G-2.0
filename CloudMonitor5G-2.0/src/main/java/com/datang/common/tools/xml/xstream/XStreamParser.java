/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.tools.xml.xstream;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.datang.common.tools.xml.exception.ParseException;
import com.datang.common.util.ClassUtils;
import com.datang.common.util.IOUtils;
import com.thoughtworks.xstream.XStream;

/**
 * XStream封装
 * <p>
 * <li>提供如下功能：
 * <ol>
 * 从XML文件中反序列化对象；
 * <ol>
 * 序列化对象到文件XML。
 * <li>
 * 
 * @author wangshuzhen
 * @version 1.0.0
 * 
 * @param <T>
 *            实体类
 */
public class XStreamParser<T> {
	/**
	 * 关联类，通过annotation标注的用来映射XML的所有类，使得xml解析的性能更高
	 */
	private Class<?>[] classContext;

	/**
	 * XSteam
	 */
	private XStream xstream;

	/**
	 * 构造函数，制定实体关联对象类型，提高解析速度
	 * 
	 * @param classContext
	 *            实体关联对象类型
	 */
	public XStreamParser(Class<?>... classContext) {
		this.classContext = classContext;
		init();
	}

	/**
	 * 初始化XStream
	 */
	private void init() {
		xstream = new XStream();
		if (classContext != null) {
			xstream.processAnnotations(classContext);
		}
		xstream.autodetectAnnotations(true);
	}

	/**
	 * 从xml读取数据到配置文件的对象
	 * 
	 * @param xmlFile
	 *            配置文件classpath
	 * @throws ParseException
	 *             解析异常
	 */
	@SuppressWarnings("unchecked")
	public T fromXML(String xmlPath) throws ParseException {
		InputStream in = null;
		try {
			in = ClassUtils.getResourceAsStream(xmlPath);
			if (in == null) {
				throw new ParseException("fail to get inputStream of "
						+ xmlPath);
			}
			Object obj = xstream.fromXML(in);
			return (T) obj;
		} catch (Exception e) {
			throw new ParseException("fail to operate fromXML: " + xmlPath, e);
		} finally {
			IOUtils.close(in);
		}
	}

	/**
	 * 把配置文件对象写到xml配置文件中
	 * 
	 * @param object
	 *            要写入配置文件的对象
	 * @param xmlPath
	 *            XML文件路径
	 * @throws ParseException
	 *             解析异常
	 */
	public void toXML(T object, String xmlPath) throws ParseException {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(xmlPath);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream,
					Charset.forName("UTF-8"));
			xstream.toXML(object, writer);
		} catch (Exception e) {
			throw new ParseException("fail to operate toXML:" + xmlPath, e);
		} finally {
			IOUtils.close(outputStream);
		}
	}
}
