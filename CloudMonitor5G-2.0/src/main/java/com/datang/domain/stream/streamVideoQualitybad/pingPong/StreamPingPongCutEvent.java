package com.datang.domain.stream.streamVideoQualitybad.pingPong;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 流媒体视频质差乒乓切换--切换事件
 * 
 * @explain
 * @name StreamPingPongCutEvent
 * @author shenyanwei
 * @date 2017年10月20日下午4:30:24
 */
@Entity
@Table(name = "STREAM_BQ_PP_EVENT")
public class StreamPingPongCutEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2951927122886667151L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 时间
	 */
	private Long cutTime;
	/**
	 * 所属问题(乒乓切换问题)
	 */
	private StreamQualityBadPingPong streamQualityBadPingPong;
	/**
	 * 源小区名
	 */
	private String srcCellName;
	/**
	 * 源小区CELLID
	 */
	private Long srcCellId;
	/**
	 * 源PCI
	 */
	private Long srcPci;
	/**
	 * 源EARFCN
	 */
	private Long srcEarfcn;
	/**
	 * 目标小区名
	 */
	private String dstCellName;
	/**
	 * 目标小区CELLID
	 */
	private Long dstCellId;
	/**
	 * 目标PCI
	 */
	private Long dstPci;
	/**
	 * 目标EARFCN
	 */
	private Long dstEarfcn;
	/**
	 * 事件名称
	 */
	private String eventName;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
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
	 * @return the
	 *         volteQualityBadRoadNbDeficiencyvolteQualityBadRoadNbDeficiency
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RID")
	@JSON(serialize = false)
	public StreamQualityBadPingPong getStreamQualityBadPingPong() {
		return streamQualityBadPingPong;
	}

	/**
	 * @param volteQualityBadRoadNbDeficiency
	 *            the volteQualityBadRoadNbDeficiency to set
	 */
	public void setStreamQualityBadPingPong(
			StreamQualityBadPingPong streamQualityBadPingPong) {
		this.streamQualityBadPingPong = streamQualityBadPingPong;
	}

	/**
	 * @return the eventName
	 */
	@Column(name = "EVENTNAME")
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param the
	 *            eventName to set
	 */

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the srcCellName
	 */
	@Column(name = "SRC_CELL_NAME")
	public String getSrcCellName() {
		return srcCellName;
	}

	/**
	 * @param the
	 *            srcCellName to set
	 */

	public void setSrcCellName(String srcCellName) {
		this.srcCellName = srcCellName;
	}

	/**
	 * @return the srcCellId
	 */
	@Column(name = "SRC_CELL_ID")
	public Long getSrcCellId() {
		return srcCellId;
	}

	/**
	 * @param the
	 *            srcCellId to set
	 */

	public void setSrcCellId(Long srcCellId) {
		this.srcCellId = srcCellId;
	}

	/**
	 * @return the srcPci
	 */
	@Column(name = "SRC_PCI")
	public Long getSrcPci() {
		return srcPci;
	}

	/**
	 * @param the
	 *            srcPci to set
	 */

	public void setSrcPci(Long srcPci) {
		this.srcPci = srcPci;
	}

	/**
	 * @return the srcEarfcn
	 */
	@Column(name = "SRC_EARFCN")
	public Long getSrcEarfcn() {
		return srcEarfcn;
	}

	/**
	 * @param the
	 *            srcEarfcn to set
	 */

	public void setSrcEarfcn(Long srcEarfcn) {
		this.srcEarfcn = srcEarfcn;
	}

	/**
	 * @return the dstCellName
	 */
	@Column(name = "DST_CELL_NAME")
	public String getDstCellName() {
		return dstCellName;
	}

	/**
	 * @param the
	 *            dstCellName to set
	 */

	public void setDstCellName(String dstCellName) {
		this.dstCellName = dstCellName;
	}

	/**
	 * @return the dstCellId
	 */
	@Column(name = "DST_CELL_ID")
	public Long getDstCellId() {
		return dstCellId;
	}

	/**
	 * @param the
	 *            dstCellId to set
	 */

	public void setDstCellId(Long dstCellId) {
		this.dstCellId = dstCellId;
	}

	/**
	 * @return the dstPci
	 */
	@Column(name = "DST_PCI")
	public Long getDstPci() {
		return dstPci;
	}

	/**
	 * @param the
	 *            dstPci to set
	 */

	public void setDstPci(Long dstPci) {
		this.dstPci = dstPci;
	}

	/**
	 * @return the dstEarfcn
	 */
	@Column(name = "DST_EARFCN")
	public Long getDstEarfcn() {
		return dstEarfcn;
	}

	/**
	 * @param the
	 *            dstEarfcn to set
	 */

	public void setDstEarfcn(Long dstEarfcn) {
		this.dstEarfcn = dstEarfcn;
	}

	/**
	 * @return the cutTime
	 */
	@Column(name = "TIME")
	public Long getCutTime() {
		return cutTime;
	}

	/**
	 * @param the
	 *            cutTime to set
	 */

	public void setCutTime(Long cutTime) {
		this.cutTime = cutTime;
	}

	@Transient
	public String getCutTimeValue() {
		if (null != cutTime) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date(cutTime));
		}
		return null;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cutTime == null) ? 0 : cutTime.hashCode());
		result = prime * result
				+ ((dstCellId == null) ? 0 : dstCellId.hashCode());
		result = prime * result
				+ ((dstCellName == null) ? 0 : dstCellName.hashCode());
		result = prime * result
				+ ((dstEarfcn == null) ? 0 : dstEarfcn.hashCode());
		result = prime * result + ((dstPci == null) ? 0 : dstPci.hashCode());
		result = prime * result
				+ ((eventName == null) ? 0 : eventName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((srcCellId == null) ? 0 : srcCellId.hashCode());
		result = prime * result
				+ ((srcCellName == null) ? 0 : srcCellName.hashCode());
		result = prime * result
				+ ((srcEarfcn == null) ? 0 : srcEarfcn.hashCode());
		result = prime * result + ((srcPci == null) ? 0 : srcPci.hashCode());
		result = prime
				* result
				+ ((streamQualityBadPingPong == null) ? 0
						: streamQualityBadPingPong.hashCode());
		return result;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StreamPingPongCutEvent other = (StreamPingPongCutEvent) obj;
		if (cutTime == null) {
			if (other.cutTime != null)
				return false;
		} else if (!cutTime.equals(other.cutTime))
			return false;
		if (dstCellId == null) {
			if (other.dstCellId != null)
				return false;
		} else if (!dstCellId.equals(other.dstCellId))
			return false;
		if (dstCellName == null) {
			if (other.dstCellName != null)
				return false;
		} else if (!dstCellName.equals(other.dstCellName))
			return false;
		if (dstEarfcn == null) {
			if (other.dstEarfcn != null)
				return false;
		} else if (!dstEarfcn.equals(other.dstEarfcn))
			return false;
		if (dstPci == null) {
			if (other.dstPci != null)
				return false;
		} else if (!dstPci.equals(other.dstPci))
			return false;
		if (eventName == null) {
			if (other.eventName != null)
				return false;
		} else if (!eventName.equals(other.eventName))
			return false;
		if (srcCellId == null) {
			if (other.srcCellId != null)
				return false;
		} else if (!srcCellId.equals(other.srcCellId))
			return false;
		if (srcCellName == null) {
			if (other.srcCellName != null)
				return false;
		} else if (!srcCellName.equals(other.srcCellName))
			return false;
		if (srcEarfcn == null) {
			if (other.srcEarfcn != null)
				return false;
		} else if (!srcEarfcn.equals(other.srcEarfcn))
			return false;
		if (srcPci == null) {
			if (other.srcPci != null)
				return false;
		} else if (!srcPci.equals(other.srcPci))
			return false;
		if (streamQualityBadPingPong == null) {
			if (other.streamQualityBadPingPong != null)
				return false;
		} else if (!streamQualityBadPingPong
				.equals(other.streamQualityBadPingPong))
			return false;
		return true;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PingPongCutEvent [id=" + id + ", cutTime=" + cutTime
				+ ", videoQualityBadPingPong=" + streamQualityBadPingPong
				+ ", srcCellName=" + srcCellName + ", srcCellId=" + srcCellId
				+ ", srcPci=" + srcPci + ", srcEarfcn=" + srcEarfcn
				+ ", dstCellName=" + dstCellName + ", dstCellId=" + dstCellId
				+ ", dstPci=" + dstPci + ", dstEarfcn=" + dstEarfcn
				+ ", eventName=" + eventName + "]";
	}

}
