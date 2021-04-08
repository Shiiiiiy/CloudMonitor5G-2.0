/**
 * 
 */
package com.datang.constant;

/**
 * 质差路段类型
 * 
 * @author yinzhipeng
 * @date:2015年12月18日 上午10:23:53
 * @version
 */
public enum QBRType {
	/**
	 * 弱覆盖问题质差路段
	 */
	WeakCover,
	/**
	 * 干扰问题质差路段
	 */
	Disturb,
	/**
	 * 邻区问题质差路段
	 */
	NbCell,
	/**
	 * 参数错误质差路段
	 */
	ParamError,
	/**
	 * 核心网问题质差路段
	 */
	CoreNetwork,
	/**
	 * 其他问题质差路段
	 */
	Other,
	/**
	 * 重叠覆盖路段
	 */
	OverlapCover,
	/**
	 * 被叫位置更新路段
	 */
	LocationUpdate
}
