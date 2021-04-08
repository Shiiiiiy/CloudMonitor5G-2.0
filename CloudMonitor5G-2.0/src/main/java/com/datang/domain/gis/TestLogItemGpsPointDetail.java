/**
 * 
 */
package com.datang.domain.gis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 测试日志全量指标GPS轨迹点具体指标
 * 
 * @author yinzhipeng
 * @date:2015年11月26日 上午10:30:24
 * @modify yinzhipeng 2017年7月24日
 * @version
 */
@Entity
@Table(name = "IADS_TESTLOG_GPS_POINT_D_1974")
public class TestLogItemGpsPointDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7035116700379149379L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 日志全量指标GPS公共参数
	 */
	private TestLogItemGpsPoint testLogItemGpsPoint;
	/**
	 * 指标名
	 */
	private Integer indexType;
	/**
	 * 指标值
	 */
	private String indexValue;

	/**
	 * 指标时间
	 */
	private Long indexTime;

	/**
	 * 指标或者事件:0事件,1指标
	 */
	private Integer type;

	/**
	 * @return the idid
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	@JSON(serialize = false)
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
	 * @return the testLogItemGpsPointDetailtestLogItemGpsPointDetail
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GPSPOINT_ID")
	@JSON(serialize = false)
	public TestLogItemGpsPoint getTestLogItemGpsPoint() {
		return testLogItemGpsPoint;
	}

	/**
	 * @param testLogItemGpsPointDetail
	 *            the testLogItemGpsPointDetail to set
	 */
	public void setTestLogItemGpsPoint(TestLogItemGpsPoint testLogItemGpsPoint) {
		this.testLogItemGpsPoint = testLogItemGpsPoint;
	}

	/**
	 * @return the indexTypeindexType
	 */
	@Column(name = "INDEX_TYPE")
	public Integer getIndexType() {
		return indexType;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(Integer indexType) {
		this.indexType = indexType;
	}

	/**
	 * @return the indexValueindexValue
	 */
	@Column(name = "INDEX_VALUE")
	public String getIndexValue() {
		return indexValue;
	}

	/**
	 * @param indexValue
	 *            the indexValue to set
	 */
	public void setIndexValue(String indexValue) {
		this.indexValue = indexValue;
	}

	/**
	 * @return the indexTimeindexTime
	 */
	@Column(name = "TIMESTAMP")
	public Long getIndexTime() {
		return indexTime;
	}

	/**
	 * @param indexTime
	 *            the indexTime to set
	 */
	public void setIndexTime(Long indexTime) {
		this.indexTime = indexTime;
	}

	/**
	 * @return the typetype
	 */
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

}
