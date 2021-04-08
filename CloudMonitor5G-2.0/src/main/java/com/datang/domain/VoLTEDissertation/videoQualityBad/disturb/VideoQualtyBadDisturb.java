package com.datang.domain.VoLTEDissertation.videoQualityBad.disturb;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;

/**
 * @explain 视频质差分析---干扰问题
 * @name VideoQualtyBadDisturb
 * @author shenyanwei
 * @date 2017年5月11日上午9:01:55
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_QB_DISTURB")
public class VideoQualtyBadDisturb extends VideoQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5708630977509328296L;

}
