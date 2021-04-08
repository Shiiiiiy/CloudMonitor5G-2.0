/**
 * 
 */
package com.datang.domain.VoLTEDissertation.voiceRTPLostPacket;

import java.io.Serializable;
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
 * VoLTE质量专题----语音RTP连续丢包,各端口RTP包的发送详情
 * 
 * @explain
 * @name packetSendDetails
 * @author shenyanwei
 * @date 2017年2月14日上午9:06:24
 */
@Entity
@Table(name = "IADS_DISS_PACKET_SEND")
public class PacketSendDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 关联指标
	 */
	private VolteVoiceRTPLostPacket rtpLostPacket;
	/**
	 * 时间
	 */
	private Long time;
	private Long ssrc;
	private Integer sequence;
	private Integer payloadLen;
	private Long timestamp;
	/**
	 * 发送端UE 接口RTP上行包：0 发送端S1接口RTP上行包：1 发送端SGi接口RTP上行包：2 接收端SGi接口RTP下行包：3
	 * 接收端S1接口RTP下行包：4 接收端UE接口RTP下行包：5
	 */
	private Integer index;

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
	 * @return the rtpLostPacket
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARID", nullable = false, referencedColumnName = "ID")
	@JSON(serialize = false)
	public VolteVoiceRTPLostPacket getRtpLostPacket() {
		return rtpLostPacket;
	}

	/**
	 * @param the
	 *            rtpLostPacket to set
	 */

	public void setRtpLostPacket(VolteVoiceRTPLostPacket rtpLostPacket) {
		this.rtpLostPacket = rtpLostPacket;
	}

	/**
	 * @return the timetime
	 */
	@Column(name = "TIME")
	@JSON(serialize = false)
	public Long getTime() {

		return time;
	}

	@Transient
	@JSON(name = "time", format = "HH:mm:ss.SSS")
	public Date getDate() {
		return null == time ? null : new Date(time);
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * @return the ssrc
	 */
	@Column(name = "SSRC")
	public Long getSsrc() {
		return ssrc;
	}

	/**
	 * @param the
	 *            ssrc to set
	 */

	public void setSsrc(Long ssrc) {
		this.ssrc = ssrc;
	}

	/**
	 * @return the sequence
	 */
	@Column(name = "SEQUENCE")
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param the
	 *            sequence to set
	 */

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the payloadLen
	 */
	@Column(name = "PAYLOADLEN")
	public Integer getPayloadLen() {
		return payloadLen;
	}

	/**
	 * @param the
	 *            payloadLen to set
	 */

	public void setPayloadLen(Integer payloadLen) {
		this.payloadLen = payloadLen;
	}

	/**
	 * @return the timestamp
	 */
	@Column(name = "TIMESTAMP")
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param the
	 *            timestamp to set
	 */

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the index
	 */
	@Column(name = "INDEXNO")
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param the
	 *            index to set
	 */

	public void setIndex(Integer index) {
		this.index = index;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "packetSendDetails [id=" + id + ", rtpLostPacket="
				+ rtpLostPacket + ", time=" + time + ", ssrc=" + ssrc
				+ ", sequence=" + sequence + ", payloadLen=" + payloadLen
				+ ", timestamp=" + timestamp + ", index=" + index + "]";
	}

}
