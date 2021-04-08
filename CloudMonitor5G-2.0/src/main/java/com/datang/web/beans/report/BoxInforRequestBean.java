package com.datang.web.beans.report;

import java.io.Serializable;

public class BoxInforRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5431224132174048282L;
	/**
	 * 终端的BOXID
	 */
	private String boxId;
	/**
	 * 测试目标或者终端类型:0自动LTE ,1单模块商务终端 ,2LTE-FI
	 */
	private String testTarget;
	/**
	 * 测试域
	 */
	private String atuName;

	/**
	 * @return the boxId
	 */
	public String getBoxId() {
		return boxId;
	}

	/**
	 * @param the
	 *            boxId to set
	 */

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	/**
	 * @return the testTarget
	 */
	public String getTestTarget() {
		return testTarget;
	}

	/**
	 * @param the
	 *            testTarget to set
	 */

	public void setTestTarget(String testTarget) {
		this.testTarget = testTarget;
	}

	/**
	 * @return the atuName
	 */
	public String getAtuName() {
		return atuName;
	}

	/**
	 * @param the
	 *            atuName to set
	 */

	public void setAtuName(String atuName) {
		this.atuName = atuName;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BoxInforRequestBean [boxId=" + boxId + ", testTarget="
				+ testTarget + ", atuName=" + atuName + "]";
	}

}
