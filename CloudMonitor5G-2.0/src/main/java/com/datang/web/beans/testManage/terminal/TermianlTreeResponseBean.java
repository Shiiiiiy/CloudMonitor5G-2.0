/**
 * 
 */
package com.datang.web.beans.testManage.terminal;

/**
 * 设备树返回bean
 * 
 * @author yinzhipeng
 * @date:2015年11月2日 上午10:10:11
 * @version
 */
public class TermianlTreeResponseBean {
	private String text;
	private String value;
	private String group;

	/**
	 * @return the texttext
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the valuevalue
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the groupgroup
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TermianlTreeResponseBean [text=" + text + ", value=" + value
				+ ", group=" + group + "]";
	}

}
