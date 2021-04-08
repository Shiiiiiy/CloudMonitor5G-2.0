/**
 * 
 */
package com.datang.domain.VoLTEDissertation.exceptionEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * VoLTE质量专题----异常事件分析,未接通异常事件指标bean
 * 
 * @author yinzhipeng
 * @date:2016年4月16日 上午9:20:02
 * @version
 */
@Entity
@Table(name = "IADS_DISS_VOLTE_EE_NOT_CONN")
public class VolteExceptionEventNotConnect extends VolteExceptionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5854677256247059951L;

	/**
	 * 失败信令节点:0'主叫随机接入',1'主叫RRC建立',2'被叫寻呼',3'被叫随机接入'<br>
	 * 4'被叫RRC建立',5'主叫QCI=1的专载建立',6'被叫QCI=1的专载建立',7'主叫Invite交互',8'被叫invite交互 '
	 */
	private Integer failSignallingNode;

	/**
	 * @return the failSignallingNodefailSignallingNode
	 */
	@Column(name = "FAIL_SIGNALLING_NODE")
	public Integer getFailSignallingNode() {
		return failSignallingNode;
	}

	/**
	 * @param failSignallingNode
	 *            the failSignallingNode to set
	 */
	public void setFailSignallingNode(Integer failSignallingNode) {
		this.failSignallingNode = failSignallingNode;
	}

}
