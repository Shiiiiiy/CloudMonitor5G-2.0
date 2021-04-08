package com.datang.web.beans.report;

import java.io.Serializable;

public class CityInfoRequestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cityId;
	private String name;

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param the
	 *            cityId to set
	 */

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param the
	 *            name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CityInfoRequestBean [cityId=" + cityId + ", name=" + name + "]";
	}

}
