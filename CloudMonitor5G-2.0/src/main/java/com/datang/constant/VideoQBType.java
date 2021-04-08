/**
 * 
 */
package com.datang.constant;

/**
 * 视频质差路段类型
 * 
 * @explain
 * @name VideoQBType
 * @author shenyanwei
 * @date 2017年5月22日下午5:46:07
 */
public enum VideoQBType {
	/**
	 * 弱覆盖问题质差
	 */
	WeakCover,
	/**
	 * 干扰问题质差
	 */
	Disturb,
	/**
	 * 邻区问题质差
	 */
	Neighbour,
	/**
	 * 乒乓切换质差
	 */
	PingPong,
	/**
	 * 其他问题质差路段
	 */
	Other,
	/**
	 * 重叠覆盖路段
	 */
	OverCover,
	/**
	 * 模式转换
	 */
	PatternSwitch,
	/**
	 * 下行调度数小
	 */
	DownDispatchSmall

}
