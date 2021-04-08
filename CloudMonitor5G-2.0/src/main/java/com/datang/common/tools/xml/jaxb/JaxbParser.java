/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.tools.xml.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.xmlbeans.XmlException;

import com.datang.common.tools.xml.exception.ParseException;
import com.datang.common.util.IOUtils;
import com.datang.common.util.ResourceUtils;

/**
 * Jaxb封装
 * <p>
 * <li>提供如下功能：
 * <ol>
 * 从XML文件中反序列化对象；
 * <ol>
 * 序列化对象到文件XML。
 * <li>
 * <ol>
 * 对象序列化到byte[]；
 * <ol>
 * byte[]反序列化到对象。
 * <li>
 * 
 * @author wangshuzhen
 * @version 1.0.0
 * 
 * @param <T>
 *            实体类
 */
public class JaxbParser<T> {
	/**
	 * jaxbContext
	 */
	private JAXBContext context;

	/**
	 * 序列化，不能直接使用，使用{@link #getMarshaller()}获取
	 */
	private Marshaller marshaller;

	/**
	 * 反序列化，不能直接使用，使用{@link #getUnMarshaller()}获取
	 */
	private Unmarshaller unmarshaller;

	/**
	 * java package names that contain schema derived classes
	 */
	private String contextPath;

	/**
	 * 构造函数
	 * 
	 * @param contextPath
	 *            java package names that contain schema derived classes
	 * @throw ParseException
	 */
	public JaxbParser(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * 反序列化
	 * 
	 * @param <T>
	 *            实体类型
	 * @param data
	 *            数据
	 * @return 实体
	 */
	@SuppressWarnings("unchecked")
	public T unmarshalObject(byte[] data) throws ParseException {
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(data);
			Unmarshaller ums = getUnmarshaller();
			Object obj = ums.unmarshal(in);
			return (T) obj;
		} catch (Exception e) {
			throw new ParseException("unmarshalObject failed", e);
		} finally {
			IOUtils.close(in);
		}
	}

	/**
	 * 序列化
	 * 
	 * @param obj
	 *            对象
	 * @return byte[]
	 */
	public byte[] marshalObject(Object obj) throws ParseException {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			Marshaller ms = getMarshaller();
			ms.marshal(obj, out);
			byte[] buffer = out.toByteArray();
			return buffer;
		} catch (Exception e) {
			throw new ParseException("marshalObject failed", e);
		} finally {
			IOUtils.close(out);
		}
	}

	/**
	 * XML2Java
	 * 
	 * @param <T>
	 *            Java对象
	 * @param xmlPath
	 *            XML路径
	 * @return Java对象
	 * @throws XmlException
	 *             异常信息
	 */
	@SuppressWarnings("unchecked")
	public T fromXML(String xmlPath) throws ParseException {
		InputStream in = null;
		try {
			in = ResourceUtils.getResourceAsStream(xmlPath);
			if (in == null) {
				throw new ParseException("xml file[" + xmlPath + "] not found");
			}
			Object obj = unmarshaller.unmarshal(in);
			return (T) obj;
		} catch (Exception e) {
			throw new ParseException("XML2Java failed", e);
		} finally {
			IOUtils.close(in);
		}
	}

	/**
	 * Java2XML
	 * 
	 * @param <T>
	 *            对象类型
	 * @param object
	 *            实体
	 * @param xmlPath
	 *            XMLPath
	 * @throws XmlException
	 *             异常信息
	 */
	public void toXML(T object, String xmlPath) throws ParseException {
		OutputStreamWriter writer = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(xmlPath);
			writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
			Marshaller mr = this.getMarshaller();
			mr.marshal(object, out);
		} catch (Exception e) {
			throw new ParseException("Java2XML failed", e);
		} finally {
			IOUtils.close(writer);
			IOUtils.close(out);
		}
	}

	/**
	 * 获取JAXBContext
	 * 
	 * @return JAXBContext
	 * @throws JAXBException
	 */
	private JAXBContext getContext() throws JAXBException {
		if (context == null) {
			context = JAXBContext.newInstance(contextPath);
		}

		return context;
	}

	/**
	 * 获取反序列化对象
	 * 
	 * @return Unmarshaller
	 * @throws JAXBException
	 */
	private Unmarshaller getUnmarshaller() throws JAXBException {
		if (unmarshaller == null) {
			unmarshaller = getContext().createUnmarshaller();
		}

		return unmarshaller;
	}

	/**
	 * 获取序列化对象
	 * 
	 * @return Marshaller
	 * @throws JAXBException
	 */
	private Marshaller getMarshaller() throws JAXBException {
		if (marshaller == null) {
			marshaller = getContext().createMarshaller();
		}

		return marshaller;
	}
}
