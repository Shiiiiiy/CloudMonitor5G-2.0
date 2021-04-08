/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.tools.xml.exception;

/**
 * 解析/序列化异常
 * 
 * @author wangshuzhen
 * @version 1.0.0
 */
public class ParseException extends Exception {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6075658280657630271L;

	/**
	 * 构造函数
	 * 
	 * @param message 错误消息
	 */
	public ParseException(String message) {
		this(message, null);
	}

	/**
	 * 解析异常
	 * 
	 * @param message 错误消息
	 * @param e 原始异常
	 */
	public ParseException(String message, Exception e) {
		super(message, e);
	}
}
