/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.voiceRTPLostPacket;

import java.io.Serializable;

/**
 * 节点分布占比统计响应bean
 * 
 * @explain
 * @name NodesDistributionResponseBean
 * @author shenyanwei
 * @date 2017年2月16日上午10:16:36
 */
public class NodesDistributionResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4429379720706258109L;
	/**
	 * 节点数量
	 */
	private Integer node1;
	private Integer node2;
	private Integer node3;
	private Integer node4;
	private Integer node5;
	private Integer node6;

	/**
	 * @return the node1
	 */
	public Integer getNode1() {
		return node1;
	}

	/**
	 * @param the
	 *            node1 to set
	 */

	public void setNode1(Integer node1) {
		this.node1 = node1;
	}

	/**
	 * @return the node2
	 */
	public Integer getNode2() {
		return node2;
	}

	/**
	 * @param the
	 *            node2 to set
	 */

	public void setNode2(Integer node2) {
		this.node2 = node2;
	}

	/**
	 * @return the node3
	 */
	public Integer getNode3() {
		return node3;
	}

	/**
	 * @param the
	 *            node3 to set
	 */

	public void setNode3(Integer node3) {
		this.node3 = node3;
	}

	/**
	 * @return the node4
	 */
	public Integer getNode4() {
		return node4;
	}

	/**
	 * @param the
	 *            node4 to set
	 */

	public void setNode4(Integer node4) {
		this.node4 = node4;
	}

	/**
	 * @return the node5
	 */
	public Integer getNode5() {
		return node5;
	}

	/**
	 * @param the
	 *            node5 to set
	 */

	public void setNode5(Integer node5) {
		this.node5 = node5;
	}

	/**
	 * @return the node6
	 */
	public Integer getNode6() {
		return node6;
	}

	/**
	 * @param the
	 *            node6 to set
	 */

	public void setNode6(Integer node6) {
		this.node6 = node6;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NodesDistributionResponseBean [ node1=" + node1 + ", node2="
				+ node2 + ", node3=" + node3 + ", node4=" + node4 + ", node5="
				+ node5 + ", node6=" + node6 + "]";
	}

}
