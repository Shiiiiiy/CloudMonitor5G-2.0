package com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;

/**
 * 流媒体视频质差分析---下行调度数小问题
 * 
 * @explain
 * @name VideoQualityBadDownDispatchSmall
 * @author shenyanwei
 * @date 2017年10月20日下午4:47:21
 */
@Entity
@Table(name = "STREAM_BQ_PDCCHDL")
public class StreamQualityBadDownDispatchSmall extends StreamQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1166943804098075536L;

	/**
	 * PDCCH DL Grant Num/s 个数
	 */
	private Long pdcchDlNumber;
	/**
	 * PDCCH DL Grant Num/s 值
	 */
	private Float pdcchDlSum;

	/**
	 * @return the pdcchDlNumber
	 */
	@Column(name = "PDCCHDL_NUM")
	public Long getPdcchDlNumber() {
		return pdcchDlNumber;
	}

	/**
	 * @param the
	 *            pdcchDlNumber to set
	 */

	public void setPdcchDlNumber(Long pdcchDlNumber) {
		this.pdcchDlNumber = pdcchDlNumber;
	}

	/**
	 * @return the pdcchDlSum
	 */
	@Column(name = "PDCCHDL_SUM")
	public Float getPdcchDlSum() {
		return pdcchDlSum;
	}

	/**
	 * @param the
	 *            pdcchDlSum to set
	 */

	public void setPdcchDlSum(Float pdcchDlSum) {
		this.pdcchDlSum = pdcchDlSum;
	}

}
