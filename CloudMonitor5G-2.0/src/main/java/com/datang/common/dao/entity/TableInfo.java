package com.datang.common.dao.entity;

public class TableInfo {

	private String filedName;
	private String type;
	/**
	 * @return the filedName
	 */
	public String getFiledName() {
		return filedName;
	}
	/**
	 * @param filedName the filedName to set
	 */
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	public TableInfo(String filedName, String type) {
		super();
		this.filedName = filedName;
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
