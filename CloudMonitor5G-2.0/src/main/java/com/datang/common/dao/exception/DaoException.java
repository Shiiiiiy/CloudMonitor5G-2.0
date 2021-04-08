/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.common.dao.exception;

/**
 * Dao异常
 * 
 * @author wangshuzhen
 * @version 1.0.0
 */
public class DaoException extends RuntimeException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4543090750567851816L;

	/**
	 * 数据库通用异常
	 */
	public static final int DB_EXCEPTION = 0;

	/**
	 * 数据已存在异常
	 */
	public static final int RECORD_EXIST = 1;

	/**
	 * 数据已不存在异常
	 */
	public static final int RECORD_NOT_EXIST = 2;
	
	

	/**
	 * 错误吗
	 */
	private int errorCode = DB_EXCEPTION;

	/**
	 * 构造函数
	 * 
	 * @param message 错误消息
	 */
	public DaoException(String message) {
		this(message, null);
	}

	/**
	 * 构造函数
	 * 
	 * @param message 错误消息
	 * @param cause 错误原因
	 */
	public DaoException(String message, Throwable cause) {
		this(DB_EXCEPTION, message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param errorCode 错误码
	 */
	public DaoException(int errorCode) {
		this(errorCode, "errorCode: " + errorCode);
	}

	/**
	 * 构造函数
	 * 
	 * @param errorCode 错误码
	 * @param message 错误信息
	 */
	public DaoException(int errorCode, String message) {
		this(errorCode, message, null);
	}

	/**
	 * 构造函数
	 * 
	 * @param errorCode 错误码
	 * @param cause 错误原因
	 */
	public DaoException(int errorCode, Throwable cause) {
		this(errorCode, null, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param errorCode 错误码
	 * @param message 错误信息
	 * @param cause 错误原因
	 */
	public DaoException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
