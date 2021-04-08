package com.datang.bean.monitor;

/**
 * 事件图标响应Bean
 * 
 * @explain
 * @name EventParamBean
 * @author shenyanwei
 * @date 2016年7月26日下午1:52:11
 */
public class EventParamBean {
	private Float longitude;
	private Float latitude;
	private String eventType;

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

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param the
	 *            eventType to set
	 */

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventParamBean [longitude=" + longitude + ", latitude="
				+ latitude + ", eventType=" + eventType + "]";
	}

}
