/**
 * 
 */
package com.datang.domain.gis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 测试日志全量指标栅格信息(栅格大小500米)
 * 
 * @author yinzhipeng
 * @date:2016年6月24日 上午9:06:30
 * @version
 */
@Entity
@Table(name = "IADS_TESTLOG_GRID_500")
public class TestLogItemGrid500 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2585946505835336691L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 外键TestLogItem的id
	 */
	private Long recSeqNo;

	/**
	 * GIS地图栅格对象id
	 */
	private Long objectId;

	/**
	 * 指标类型
	 */
	private Integer indexType;

	/**
	 * 指标值总和
	 */
	private Float indexValueSum;
	/**
	 * 指标值总和(Double)
	 */
	private Double indexValueSumDouble;
	/**
	 * 指标值数量总和
	 */
	private Integer indexNumSum;

	/**
	 * 指标值数量总和(Long)
	 */
	private Long indexNumSumLong;

	/**
	 * 栅格左下右上两点经纬度
	 */
	private Float minx;
	private Float miny;
	private Float maxx;
	private Float maxy;

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
	 * @return the recSeqNorecSeqNo
	 */
	@Column(name = "RECSEQNO")
	@JSON(serialize = false)
	public Long getRecSeqNo() {
		return recSeqNo;
	}

	/**
	 * @param recSeqNo
	 *            the recSeqNo to set
	 */
	public void setRecSeqNo(Long recSeqNo) {
		this.recSeqNo = recSeqNo;
	}

	/**
	 * @return the objectIdobjectId
	 */
	@Column(name = "OBJECTID")
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId
	 *            the objectId to set
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
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
	 * @return the indexValueSumindexValueSum
	 */
	@Column(name = "INDEX_VALUE_SUM")
	public Float getIndexValueSum() {
		return indexValueSum;
	}

	/**
	 * @param indexValueSum
	 *            the indexValueSum to set
	 */
	public void setIndexValueSum(Float indexValueSum) {
		this.indexValueSum = indexValueSum;
	}

	/**
	 * @return the indexNumSumindexNumSum
	 */
	@Column(name = "INDEX_NUM_SUM")
	public Integer getIndexNumSum() {
		return indexNumSum;
	}

	/**
	 * @param indexNumSum
	 *            the indexNumSum to set
	 */
	public void setIndexNumSum(Integer indexNumSum) {
		this.indexNumSum = indexNumSum;
	}

	/**
	 * @return the minxminx
	 */
	@Column(name = "MINX")
	public Float getMinx() {
		return minx;
	}

	/**
	 * @param minx
	 *            the minx to set
	 */
	public void setMinx(Float minx) {
		this.minx = minx;
	}

	/**
	 * @return the minyminy
	 */
	@Column(name = "MINY")
	public Float getMiny() {
		return miny;
	}

	/**
	 * @param miny
	 *            the miny to set
	 */
	public void setMiny(Float miny) {
		this.miny = miny;
	}

	/**
	 * @return the maxxmaxx
	 */
	@Column(name = "MAXX")
	public Float getMaxx() {
		return maxx;
	}

	/**
	 * @param maxx
	 *            the maxx to set
	 */
	public void setMaxx(Float maxx) {
		this.maxx = maxx;
	}

	/**
	 * @return the maxymaxy
	 */
	@Column(name = "MAXY")
	public Float getMaxy() {
		return maxy;
	}

	/**
	 * @param maxy
	 *            the maxy to set
	 */
	public void setMaxy(Float maxy) {
		this.maxy = maxy;
	}

	/**
	 * @return the indexNumSumLongindexNumSumLong
	 */
	@Transient
	public Long getIndexNumSumLong() {
		return indexNumSumLong;
	}

	/**
	 * @param indexNumSumLong
	 *            the indexNumSumLong to set
	 */
	public void setIndexNumSumLong(Long indexNumSumLong) {
		this.indexNumSumLong = indexNumSumLong;
		if (null != indexNumSumLong) {
			this.indexNumSum = indexNumSumLong.intValue();
		}

	}

	/**
	 * @return the indexValueSumDoubleindexValueSumDouble
	 */
	@Transient
	public Double getIndexValueSumDouble() {
		return indexValueSumDouble;
	}

	/**
	 * @param indexValueSumDouble
	 *            the indexValueSumDouble to set
	 */
	public void setIndexValueSumDouble(Double indexValueSumDouble) {
		this.indexValueSumDouble = indexValueSumDouble;
		if (null != indexValueSumDouble) {
			this.indexValueSum = indexValueSumDouble.floatValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestLogItemGrid100 [id=" + id + ", recSeqNo=" + recSeqNo
				+ ", objectId=" + objectId + ", indexType=" + indexType
				+ ", indexValueSum=" + indexValueSum + ", indexNumSum="
				+ indexNumSum + ", minx=" + minx + ", miny=" + miny + ", maxx="
				+ maxx + ", maxy=" + maxy + "]";
	}

}
