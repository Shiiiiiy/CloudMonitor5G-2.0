/**
 * 
 */
package com.datang.domain.testLogItem;

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
 * 日志TestLogItem的全量信令
 * 
 * @author yinzhipeng
 * @date:2016年5月9日 上午8:58:50
 * @version
 */
@Entity
@Table(name = "IADS_TESTLOG_SIGNALLING")
public class TestLogItemSignalling implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5818068820156458523L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 所属日志
	 */
	private TestLogItem testLogItem;
	/**
	 * 时间
	 */
	private Long time;
	/**
	 * 方向
	 */
	private Integer dir;
	/**
	 * 制式
	 */
	private Integer rat;
	/**
	 * 协议层
	 */
	private String layer;
	/**
	 * 信道类型
	 */
	private String ch;
	/**
	 * 信令名称
	 */
	private String name;
	/**
	 * 码流
	 */
	private String stream;
	/**
	 * 码流2
	 */
	private String stream2;

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
	 * @return the testLogItemtestLogItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECSEQNO")
	@JSON(serialize = false)
	public TestLogItem getTestLogItem() {
		return testLogItem;
	}

	/**
	 * @param testLogItem
	 *            the testLogItem to set
	 */
	public void setTestLogItem(TestLogItem testLogItem) {
		this.testLogItem = testLogItem;
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
	 * @return the dirdir
	 */
	@Column(name = "DIR")
	public Integer getDir() {
		return dir;
	}

	/**
	 * @param dir
	 *            the dir to set
	 */
	public void setDir(Integer dir) {
		this.dir = dir;
	}

	/**
	 * @return the ratrat
	 */
	@Column(name = "RAT")
	public Integer getRat() {
		return rat;
	}

	/**
	 * @param rat
	 *            the rat to set
	 */
	public void setRat(Integer rat) {
		this.rat = rat;
	}

	/**
	 * @return the layerlayer
	 */
	@Column(name = "LAYER")
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * @return the chch
	 */
	@Column(name = "CH")
	public String getCh() {
		return ch;
	}

	/**
	 * @param ch
	 *            the ch to set
	 */
	public void setCh(String ch) {
		this.ch = ch;
	}

	/**
	 * @return the namename
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the streamstream
	 */
	@Column(name = "STREAM1")
	public String getStream() {
		return stream;
	}

	/**
	 * @param stream
	 *            the stream to set
	 */
	public void setStream(String stream) {
		this.stream = stream;
	}

	/**
	 * @return the stream2stream2
	 */
	@Column(name = "STREAM2")
	public String getStream2() {
		return stream2;
	}

	/**
	 * @param stream2
	 *            the stream2 to set
	 */
	public void setStream2(String stream2) {
		this.stream2 = stream2;
	}

}
