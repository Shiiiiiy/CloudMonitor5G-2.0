package com.datang.domain.stream.streamVideoQualitybad.other;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;

/**
 * 流媒体视频质差分析---其他原因
 * 
 * @explain
 * @name VideoQualityBadOther
 * @author shenyanwei
 * @date 2017年10月20日下午4:12:28
 */
@Entity
@Table(name = "STREAM_BQ_OTHER")
public class StreamQualityBadOther extends StreamQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5969707893282692457L;
	/**
	 * PDCCH DL Grant Num/s采样点个数
	 */
	private Long pdcchdlNumber;
	/**
	 * PDCCH DL Grant Num/s总值
	 */
	private Float pdcchdlSum;
	/**
	 * 小区名
	 */
	private String cellName;
	/**
	 * 小区CELLID
	 */
	private Long cellId;

	/**
	 * @return the pdcchdlNumber
	 */
	@Column(name = "PDCCHDL_NUM")
	public Long getPdcchdlNumber() {
		return pdcchdlNumber;
	}

	/**
	 * @param the
	 *            pdcchdlNumber to set
	 */

	public void setPdcchdlNumber(Long pdcchdlNumber) {
		this.pdcchdlNumber = pdcchdlNumber;
	}

	/**
	 * @return the pdcchdlSum
	 */
	@Column(name = "PDCCHDL_SUM")
	public Float getPdcchdlSum() {
		return pdcchdlSum;
	}

	/**
	 * @param the
	 *            pdcchdlSum to set
	 */

	public void setPdcchdlSum(Float pdcchdlSum) {
		this.pdcchdlSum = pdcchdlSum;
	}

	/**
	 * @return the cellNamecellName
	 */
	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName
	 *            the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * @return the cellIdcellId
	 */
	@Column(name = "CELL_ID")
	public Long getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}
}
