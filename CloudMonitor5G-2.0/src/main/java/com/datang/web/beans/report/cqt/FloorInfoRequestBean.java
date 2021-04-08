package com.datang.web.beans.report.cqt;

import java.io.Serializable;

/**
 * 楼宇信息响应Bean
 * 
 * @explain
 * @name FloorInfoRequestBean
 * @author shenyanwei
 * @date 2016年10月31日上午11:13:05
 */
public class FloorInfoRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5431224132174048282L;
	/**
	 * 楼宇的名称
	 */
	private String floorName;
	/**
	 * 测试域
	 */
	private String atuName;

	/**
	 * @return the floorName
	 */
	public String getFloorName() {
		return floorName;
	}

	/**
	 * @param the
	 *            floorName to set
	 */

	public void setFloorName(String floorName) {
		this.floorName = floorName;
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
		return "FloorInfoRequestBean [floorName=" + floorName + ", atuName="
				+ atuName + "]";
	}

}
