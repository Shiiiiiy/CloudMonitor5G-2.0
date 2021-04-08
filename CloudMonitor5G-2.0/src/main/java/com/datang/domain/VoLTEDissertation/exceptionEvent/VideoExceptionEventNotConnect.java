/**
 * 
 */
package com.datang.domain.VoLTEDissertation.exceptionEvent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VoLTE质量专题----异常事件分析,视频未接通异常事件指标bean
 * 
 * @explain
 * @name VideoExceptionEventNotConnect
 * @author shenyanwei
 * @date 2017年5月11日上午9:51:11
 */
@Entity
@Table(name = "IADS_DISS_VIDEO_EE_NOT_CONN")
public class VideoExceptionEventNotConnect extends VolteExceptionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5854677256247059951L;

}
