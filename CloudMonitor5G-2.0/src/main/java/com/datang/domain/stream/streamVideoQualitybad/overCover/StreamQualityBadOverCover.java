package com.datang.domain.stream.streamVideoQualitybad.overCover;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;

/**
 * 流媒体视频质差分析--- 重叠覆盖问题
 * 
 * @explain
 * @name VideoQualityBadOverCover
 * @author shenyanwei
 * @date 2017年10月20日下午4:06:35
 */
@Entity
@Table(name = "STREAM_BQ_OVERLAPCOVER")
public class StreamQualityBadOverCover extends StreamQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3549750199341524086L;

}
