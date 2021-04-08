package com.datang.domain.stream.streamVideoQualitybad.disturb;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;

/**
 * 流媒体视频质差分析---干扰问题
 * 
 * @explain
 * @name StreamVideoQualtyBadDisturb
 * @author shenyanwei
 * @date 2017年10月20日下午3:39:57
 */
@Entity
@Table(name = "STREAM_BQ_DISTURB")
public class StreamQualtyBadDisturb extends StreamQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5708630977509328296L;

}
