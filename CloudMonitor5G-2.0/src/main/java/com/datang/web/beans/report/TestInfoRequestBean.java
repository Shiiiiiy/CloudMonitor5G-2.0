package com.datang.web.beans.report;

import java.io.Serializable;

public class TestInfoRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5303629440885959493L;
	private Long id;
	private String fileName;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param the
	 *            fileName to set
	 */

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param the
	 *            id to set
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestInfoRequestBean [fileName=" + fileName + "]";
	}

}
