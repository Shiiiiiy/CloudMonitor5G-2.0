package com.datang.web.beans.monitor;

/**
 * Mos监控请求Bean
 * 
 * @explain
 * @name MosValueRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:53:20
 */
public class MosValueRequestBean extends PublicRequestBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3775476937613244676L;

	/** 通道号 */
	private Integer ueNo;
	/**
	 * 最小值
	 */
	private Float minMosValue;
	/**
	 * 最大值
	 */
	private Float maxMosValue;

	/**
	 * @return the ueNo
	 */
	public Integer getUeNo() {
		return ueNo;
	}

	/**
	 * @param the
	 *            ueNo to set
	 */

	public void setUeNo(Integer ueNo) {
		this.ueNo = ueNo;
	}

	/**
	 * @return the minMosValue
	 */
	public Float getMinMosValue() {
		return minMosValue;
	}

	/**
	 * @param the
	 *            minMosValue to set
	 */

	public void setMinMosValue(Float minMosValue) {
		this.minMosValue = minMosValue;
	}

	/**
	 * @return the maxMosValue
	 */
	public Float getMaxMosValue() {
		return maxMosValue;
	}

	/**
	 * @param the
	 *            maxMosValue to set
	 */

	public void setMaxMosValue(Float maxMosValue) {
		this.maxMosValue = maxMosValue;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MosValueRequestBean [ueNo=" + ueNo + ", minMosValue="
				+ minMosValue + ", maxMosValue=" + maxMosValue + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", cityIds=" + cityIds
				+ ", prestorecityIds=" + prestorecityIds + ", boxIds=" + boxIds
				+ ", boxIdsSet=" + boxIdsSet + "]";
	}

}
