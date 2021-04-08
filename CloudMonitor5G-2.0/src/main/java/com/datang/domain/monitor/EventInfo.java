package com.datang.domain.monitor;

/**
 * 事件 信息
 * 
 * @explain
 * @name EventInfo
 * @author shenyanwei
 * @date 2016年7月11日下午1:48:36
 */
public class EventInfo {
	private String code;
	private String name;
	private String meaning;
	private String type;
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
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}

	/**
	 * @param meaning
	 *            the meaning to set
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
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

	public String toString() {
		return "[code=" + this.code + ",name=" + this.name + ",meaning="
				+ this.meaning + ",type=" + this.type + ",remark="
				+ this.remark + "]";
	}

}
