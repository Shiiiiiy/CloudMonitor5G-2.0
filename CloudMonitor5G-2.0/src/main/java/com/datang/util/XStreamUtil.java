/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datang.common.util.ClassUtil;
import com.datang.exception.ApplicationException;
import com.thoughtworks.xstream.XStream;

/**
 * 是Xstream的专用工具,用来读写xml配置文件
 * 
 * <pre>
 * fromXML用法1:OPServerContext context = XStreamUtil.fromXML(OPServerContext.class, &quot;config\\op-server.xml&quot;);
 * fromXML用法2:Class&lt;?&gt;[] class_context = new Class[] { OPServerContext.class,
 * 					OPServer.class, OPMsgType.class, OPMsgChannel.class, OPJndi.class };
 * 			OPServerContext context = XStreamUtil.fromXML(
 * 					OPServerContext.class, &quot;config\\op-server.xml&quot;, class_context);
 * toXML用法1:XStreamUtil.toXML(context, &quot;d:\\test.xml&quot;);
 * toXML用法2:XStreamUtil.toXML(context, &quot;d:\\test2.xml&quot;, class_context);
 * </pre>
 * 
 * @author chaichunyi
 * @version 1.0.0, 2009-8-4
 */
public final class XStreamUtil {

	private static Logger logger = LoggerFactory.getLogger(XStreamUtil.class);

	/**
	 * 私有构造函数
	 */
	private XStreamUtil() {
	}

	/**
	 * 从xml读取数据到配置文件的对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlOutStructure
	 *            配置文件的最外层标签对应的对象的类名
	 * @param resourceName
	 *            配置文件的名称
	 * @return 从xml读取数据到配置文件的对象并返回该对象
	 */
	public static <T> T fromXML(Class<T> xmlOutStructure, String resourceName) {
		return fromXML(xmlOutStructure, resourceName, null);
	}

	/**
	 * 从xml读取数据到配置文件的对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlOutStructure
	 *            配置文件的最外层标签对应的对象的类名
	 * @param xmlStr
	 *            xml
	 * @param xmlStructures
	 *            通过annotation标注的用来映射XML的所有类,使得xml解析的性能更高
	 * @return 从xml读取数据到配置文件的对象并返回该对象
	 */
	public static <T> T fromXmlStr(Class<T> xmlOutStructure, String xmlStr,
			Class<?>[] xmlStructures) {
		XStream xstream = new XStream();
		if (xmlStructures == null) {
			xstream.autodetectAnnotations(true);
			xstream.processAnnotations(xmlOutStructure);
		} else {
			xstream.processAnnotations(xmlStructures);
		}
		return (T) xstream.fromXML(xmlStr);

	}

	/**
	 * 从xml读取数据到配置文件的对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlOutStructure
	 *            配置文件的最外层标签对应的对象的类名
	 * @param resourceName
	 *            配置文件的名称
	 * @param xmlStructures
	 *            通过annotation标注的用来映射XML的所有类,使得xml解析的性能更高
	 * @return 从xml读取数据到配置文件的对象并返回该对象
	 */
	public static <T> T fromXML(Class<T> xmlOutStructure, String resourceName,
			Class<?>[] xmlStructures) {
		XStream xstream = new XStream();
		if (xmlStructures == null) {
			xstream.autodetectAnnotations(true);
			xstream.processAnnotations(xmlOutStructure);
		} else {
			xstream.processAnnotations(xmlStructures);
		}
		InputStream inputStream = null;
		try {
			inputStream = ClassUtil.getResourceAsStream(resourceName);
			// inputStream = new
			// ClassPathResource(resourceName).getInputStream();
			if (inputStream == null) {
				throw new ApplicationException("fail to get inputStream of "
						+ resourceName);
			}
			T temp = (T) xstream.fromXML(inputStream);
			return temp;
		} catch (Exception e) {
			throw new ApplicationException("fail to operate fromXML:"
					+ resourceName, e);
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}
	}

	/**
	 * 从xml读取数据到配置文件的对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlOutStructure
	 *            配置文件的最外层标签对应的对象的类名
	 * @param inputStream
	 *            流文件
	 * @param xmlStructures
	 *            通过annotation标注的用来映射XML的所有类,使得xml解析的性能更高
	 * @return 从xml读取数据到配置文件的对象并返回该对象
	 */
	public static <T> T fromXML(Class<T> xmlOutStructure,
			InputStream inputStream, Class<?>[] xmlStructures) {
		XStream xstream = new XStream();
		if (xmlStructures == null) {
			xstream.autodetectAnnotations(true);
			xstream.processAnnotations(xmlOutStructure);
		} else {
			xstream.processAnnotations(xmlStructures);
		}
		try {
			T temp = (T) xstream.fromXML(inputStream);
			return temp;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}
	}

	/**
	 * 把配置文件对象写到xml配置文件中
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlObj
	 *            要写入配置文件的对象
	 * @param xmlURI
	 *            配置文件的地址
	 */
	public static <T> void toXML(T xmlObj, String xmlURI) {
		toXML(xmlObj, xmlURI, null);
	}

	/**
	 * 将对象转为xml的字符串
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlObj
	 *            配置文件的对象
	 * @return xml的字符串
	 */
	public static <T> String toXMLStr(T xmlObj) {
		return toXMLStr(xmlObj, null);

	}

	/**
	 * 将对象转为xml的字符串
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlObj
	 *            配置文件的对象
	 * @return xml的字符串
	 * @param xmlStructures
	 *            通过annotation标注的用来映射XML的所有类,使得xml解析的性能更高
	 */
	public static <T> String toXMLStr(T xmlObj, Class<?>[] xmlStructures) {
		XStream xstream = new XStream();
		if (xmlStructures == null) {
			xstream.autodetectAnnotations(true);
		} else {
			xstream.processAnnotations(xmlStructures);
		}
		String source = xstream.toXML(xmlObj);

		StringBuilder stringBuilder = new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		stringBuilder.append(source);

		String tmp = stringBuilder.toString();

		try {
			return new String(tmp.getBytes("utf-8"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return tmp;

	}

	/**
	 * 把配置文件对象写到xml配置文件中
	 * 
	 * @param <T>
	 *            泛型
	 * @param xmlObj
	 *            要写入配置文件的对象
	 * @param xmlURI
	 *            配置文件的地址
	 * @param xmlStructures
	 *            通过annotation标注的用来映射XML的所有类,使得xml解析的性能更高
	 */
	public static <T> void toXML(T xmlObj, String xmlURI,
			Class<?>[] xmlStructures) {
		XStream xstream = new XStream();
		if (xmlStructures == null) {
			xstream.autodetectAnnotations(true);
		} else {
			xstream.processAnnotations(xmlStructures);
		}
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(xmlURI);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream,
					Charset.forName("UTF-8"));
			xstream.toXML(xmlObj, writer);
		} catch (Exception e) {
			throw new ApplicationException("fail to operate toXML:" + xmlURI, e);
		} finally {
			try {
				if (null != outputStream) {
					outputStream.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}
	}
}
