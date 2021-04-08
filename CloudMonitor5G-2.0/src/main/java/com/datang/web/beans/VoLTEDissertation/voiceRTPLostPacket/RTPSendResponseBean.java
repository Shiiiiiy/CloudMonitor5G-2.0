/**
 * 
 */
package com.datang.web.beans.VoLTEDissertation.voiceRTPLostPacket;

import java.io.Serializable;

/**
 * 语音RTP连续丢包--RTP包的发送情况--响应bean
 * 
 * @explain
 * @name RTPSendResponseBean
 * @author shenyanwei
 * @date 2017年2月17日上午11:22:23
 */
public class RTPSendResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 问题数量
	 */
	private Integer problemNumber;
	/**
	 * 测试总时长
	 */
	private Long testTime;
	/**
	 * MOS均值
	 */
	private String MosAvg;
	/**
	 * 主叫数量
	 */
	private Integer mainCallNumber;
	/**
	 * 被叫数量
	 */
	private Integer callsNumber;
	/**
	 * 成功关联网络侧数量
	 */
	private Integer succeedRelevance;

	/**
	 * @return the problemNumber
	 */
	public Integer getProblemNumber() {
		return problemNumber;
	}

	/**
	 * @param the
	 *            problemNumber to set
	 */

	public void setProblemNumber(Integer problemNumber) {
		this.problemNumber = problemNumber;
	}

	/**
	 * @return the testTime
	 */
	public Long getTestTime() {
		return testTime;
	}

	/**
	 * @param the
	 *            testTime to set
	 */

	public void setTestTime(Long testTime) {
		this.testTime = testTime;
	}

	/**
	 * @return the MosAvg
	 */
	public String getMosAvg() {
		return MosAvg;
	}

	/**
	 * @param the
	 *            MosAvg to set
	 */

	public void setMosAvg(String mosAvg) {
		MosAvg = mosAvg;
	}

	/**
	 * @return the mainCallNumber
	 */
	public Integer getMainCallNumber() {
		return mainCallNumber;
	}

	/**
	 * @param the
	 *            mainCallNumber to set
	 */

	public void setMainCallNumber(Integer mainCallNumber) {
		this.mainCallNumber = mainCallNumber;
	}

	/**
	 * @return the callsNumber
	 */
	public Integer getCallsNumber() {
		return callsNumber;
	}

	/**
	 * @param the
	 *            callsNumber to set
	 */

	public void setCallsNumber(Integer callsNumber) {
		this.callsNumber = callsNumber;
	}

	/**
	 * @return the succeedRelevance
	 */
	public Integer getSucceedRelevance() {
		return succeedRelevance;
	}

	/**
	 * @param the
	 *            succeedRelevance to set
	 */

	public void setSucceedRelevance(Integer succeedRelevance) {
		this.succeedRelevance = succeedRelevance;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LostPacketCollectResponseBean [problemNumber=" + problemNumber
				+ ", testTime=" + testTime + ", MosAvg=" + MosAvg
				+ ", mainCallNumber=" + mainCallNumber + ", callsNumber="
				+ callsNumber + ", succeedRelevance=" + succeedRelevance + "]";
	}

}
