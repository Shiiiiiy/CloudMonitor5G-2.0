package com.datang.exception;

/**
 * 该运行期异常表示是由于软件错误引起的
 * 
 * @author yinzhipeng
 * @date:2015年10月13日 下午1:05:22
 * @version
 */
public class ApplicationException extends NestedRuntimeException {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 621147947152378103L;

	/**
	 * @param errorMessage
	 *            异常信息
	 */
	public ApplicationException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * @param message
	 *            异常消息
	 * @param cause
	 *            异常原因，异常堆栈
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
