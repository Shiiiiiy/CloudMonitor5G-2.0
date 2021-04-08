package com.datang.bean.monitor;

import java.io.Serializable;

/**
 * 监控地图响应Bean
 * 
 * @explain
 * @name GPSBean
 * @author shenyanwei
 * @date 2016年7月25日上午9:51:50
 */
public class GPSBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5658859020621472867L;
	private Float longitude;
	private Float latitude;

	/**
	 * @return the longitude
	 */
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param the
	 *            longitude to set
	 */

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param the
	 *            latitude to set
	 */

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GPSBean [longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}

}
