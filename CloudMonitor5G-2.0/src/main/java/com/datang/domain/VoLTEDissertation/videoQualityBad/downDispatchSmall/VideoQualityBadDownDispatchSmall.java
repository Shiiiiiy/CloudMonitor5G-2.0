package com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;

/**
 * 视频质差分析---下行调度数小问题
 * 
 * @explain
 * @name VideoQualityBadDownDispatchSmall
 * @author shenyanwei
 * @date 2017年6月5日下午2:41:28
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_QB_DDS")
public class VideoQualityBadDownDispatchSmall extends VideoQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1166943804098075536L;

	/**
	 * PDCCH DL Grant Num/s
	 */
	private Integer pdcchDlGrant;

	/**
	 * @return the pdcchDlGrant
	 */
	@Column(name = "PDCCHDL_GRANT")
	public Integer getPdcchDlGrant() {
		return pdcchDlGrant;
	}

	/**
	 * @param the
	 *            pdcchDlGrant to set
	 */

	public void setPdcchDlGrant(Integer pdcchDlGrant) {
		this.pdcchDlGrant = pdcchDlGrant;
	}

}
