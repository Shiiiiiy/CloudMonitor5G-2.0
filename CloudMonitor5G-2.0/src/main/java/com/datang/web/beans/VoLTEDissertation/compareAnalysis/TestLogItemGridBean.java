/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.compareAnalysis;

import java.io.Serializable;

/**
 * 栅格数据bean
 * 
 * @author yinzhipeng
 * @date:2016年6月30日 上午10:06:33
 * @version
 */
public class TestLogItemGridBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8798728477463481667L;

	/**
	 * GIS地图栅格对象id
	 */
	private Long objectId;

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
	 * @return the objectIdobjectId
	 */
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
	 * @return the indexValueSumindexValueSum
	 */
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
		return "TestLogItemGridBean [objectId=" + objectId + ", indexValueSum="
				+ indexValueSum + ", indexValueSumDouble="
				+ indexValueSumDouble + ", indexNumSum=" + indexNumSum
				+ ", indexNumSumLong=" + indexNumSumLong + ", minx=" + minx
				+ ", miny=" + miny + ", maxx=" + maxx + ", maxy=" + maxy + "]";
	}

}
