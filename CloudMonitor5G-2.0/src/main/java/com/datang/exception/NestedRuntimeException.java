/**
 * Copyright 2009 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.datang.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.core.NestedExceptionUtils;

/**
 * UnChecked Exception的基类，用于封装 <code>RuntimeException</code> 并保留Root Cause.
 * 
 * <p>
 * 本类是 <code>abstract</code> ，强制开发人员继承该类. <code>getMessage</code> 方法会包括嵌套的异常信息;
 * <code>printStackTrace</code> 和其他类似方法会委托给任何封装的异常。
 * 
 * <p>
 * 本类和 {@link NestedException} 基本一致，但其为Checked Exception，需要强制try catch。
 * 
 * @author Lance Liao
 * @see #getMessage
 * @see #printStackTrace
 * @see #NestedException
 */

public abstract class NestedRuntimeException extends RuntimeException {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <code>NestedRuntimeException</code> 空构造器
	 */
	public NestedRuntimeException() {
		super();
	}

	/**
	 * <code>NestedRuntimeException</code> 构造器，根据详细信息构建对象.
	 * 
	 * @param msg
	 *            异常详细信息
	 */
	public NestedRuntimeException(String msg) {
		super(msg);
	}

	/**
	 * <code>NestedRuntimeException</code> 构造器，根据详细信息和嵌套异常构建对象
	 * 
	 * @param msg
	 *            异常详细信息
	 * @param cause
	 *            嵌套的异常
	 */
	public NestedRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * 返回详细信息，并且包括其中嵌套异常的信息（如果存在）
	 * 
	 * @return 异常的详细信息
	 */
	public String getMessage() {
		return NestedExceptionUtils
				.buildMessage(super.getMessage(), getCause());
	}

	/**
	 * 返回格式化的详细信息，类似ex.printStackTrace;
	 * 
	 * @return 格式化的详细信息
	 */
	public String getStackTraceString() {
		StringWriter sw = new StringWriter();
		printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	/**
	 * 获得最深层嵌套的异常信息.
	 * 
	 * @return 最深层嵌套的异常信息, 如果不存在则返回 <code>null</code>
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * 返回最具体的异常信息，可能是嵌套最深的异常信息，也可能是本异常。
	 * <p>
	 * 和 {@link #getRootCause()}的主要不同处在于如果不存在root cause则返回本异常，而非
	 * <code>null</code>
	 * 
	 * @return 最具体的异常信息 (不可能为 <code>null</code>)
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * 检查本异常中是否包含指定的异常类型，要么本异常就是指定的异常类型，要么本异常嵌套包含指定的异常类型
	 * 
	 * @param exType
	 *            检查是否包含的异常类型
	 * @return 返回是否嵌套异常中包含指定的异常类型
	 */
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedRuntimeException) {
			return ((NestedRuntimeException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

}
