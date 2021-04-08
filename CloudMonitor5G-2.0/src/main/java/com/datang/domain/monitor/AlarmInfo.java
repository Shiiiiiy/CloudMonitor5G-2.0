package com.datang.domain.monitor;

/**
 * @explain
 * @name AlarmInfo
 * @author shenyanwei
 * @date 2016年7月11日下午1:54:08
 */
public class AlarmInfo {
	/**
	 * 告警码
	 */
	private String code;
	/**
	 * 告警消息
	 */
	private String message;
	/**
	 * 告警名称
	 */
	private String name;
	/**
	 * 告警原因
	 */
	private String reason;
	/**
	 * 告警类型
	 */
	private String type;
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 额外信息
	 */
	private String remark;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 
	 */
	public String toString() {
		return "[code=" + this.code + ",message=" + this.message + ",name="
				+ this.name + ",reason=" + this.reason + ",type=" + this.type
				+ ",moduleName=" + this.moduleName + ",remark=" + this.remark
				+ "]";
	}
}
