package com.datang.domain.stream.streamVideoQualitybad.pingPong;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;

/**
 * 流媒体视频质差分析---乒乓切换问题
 * 
 * @explain
 * @name VideoQualityBadPingPong
 * @author shenyanwei
 * @date 2017年10月20日下午4:21:08
 */
@Entity
@Table(name = "STREAM_BQ_PINGPONG")
public class StreamQualityBadPingPong extends StreamQualityBad {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1166943804098075536L;

}
