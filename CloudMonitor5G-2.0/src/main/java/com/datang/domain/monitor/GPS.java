package com.datang.domain.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 监控地图
 * 
 * @explain
 * @name GPS
 * @author shenyanwei
 * @date 2016年7月11日下午1:46:02
 */
@Entity
@Table(name = "IADS_REALTIME_GPS")
public class GPS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5658859020621472867L;

	private Long id;
	private String boxId;
	private Date gpsTime;
	private Long gpsTimeLong;
	private Float longitude;
	private Float latitude;
	/**
	 * 轨迹点算法时：该点是否删除
	 */
	private boolean removed = false;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "GPS_LONGITUDE")
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	@Column(name = "GPS_LATITUDE")
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the boxId
	 */
	@Column(name = "BOX_ID")
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
	 * @return the gpsTime
	 */
	@Transient
	@JSON(format = "yyyy-MM-dd HH:mm:ss.SSS")
	public Date getGpsTime() {
		return gpsTime;
	}

	/**
	 * @param the
	 *            gpsTime to set
	 */

	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}

	/**
	 * @return the gpsTimeLong
	 */
	@Column(name = "GPS_TIME")
	public Long getGpsTimeLong() {
		return gpsTimeLong;
	}

	/**
	 * @param the
	 *            gpsTimeLong to set
	 */

	public void setGpsTimeLong(Long gpsTimeLong) {
		this.gpsTimeLong = gpsTimeLong;

		if (null != gpsTimeLong) {
			this.gpsTime = new Date(gpsTimeLong);
		}
	}

	@Transient
	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GPS [id=" + id + ", boxId=" + boxId + ", gpsTime=" + gpsTime
				+ ", gpsTimeLong=" + gpsTimeLong + ", longitude=" + longitude
				+ ", latitude=" + latitude + "]";
	}

}
