package com.datang.web.beans.testPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.datang.domain.testManage.terminal.Terminal;

public class SendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5835541097184324579L;

	private Integer testPlanId;

	private List<Terminal> successTerminals = new ArrayList<Terminal>();

	private List<Terminal> failTerminals = new ArrayList<Terminal>();

	private List<Terminal> offlineSuccessTerminals = new ArrayList<Terminal>();

	private List<Terminal> offlinefailTerminals = new ArrayList<Terminal>();

	/**
	 * 添加下发成功的测试终端
	 * 
	 * @param terminal
	 */
	public void addSendSuccess(Terminal terminal) {
		if (terminal != null) {
			successTerminals.add(terminal);
		}
	}

	/**
	 * 添加下发失败的测试终端
	 * 
	 * @param terminal
	 */
	public void addSendFail(Terminal terminal) {
		failTerminals.add(terminal);
	}

	/**
	 * 添加下发失败的测试终端
	 * 
	 * @param terminal
	 */
	public void addOfflineSendFail(Terminal terminal) {
		offlinefailTerminals.add(terminal);
	}

	/**
	 * 添加下发成功的测试终端
	 * 
	 * @param terminal
	 */
	public void addOfflineSendSuccess(Terminal terminal) {
		if (terminal != null) {
			offlineSuccessTerminals.add(terminal);
		}
	}

	/**
	 * @return the testPlanId
	 */
	public Integer getTestPlanId() {
		return testPlanId;
	}

	/**
	 * @param testPlanId
	 *            the testPlanId to set
	 */
	public void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}

	/**
	 * @return the successTerminals
	 */
	public List<Terminal> getSuccessTerminals() {
		return successTerminals;
	}

	/**
	 * @return the failTerminals
	 */
	public List<Terminal> getFailTerminals() {
		return failTerminals;
	}

	/**
	 * 重载tostring方法
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// sb.append("在线下发成功的终端有：\r\n");
		// for (Terminal terminal : successTerminals) {
		// sb.append(terminal.getName());
		// sb.append("\r\n");
		// }
		// sb.append("在线下发失败的终端有：\r\n");
		// for (Terminal terminal : failTerminals) {
		// sb.append(terminal.getName());
		// sb.append("\r\n");
		// }
		if (offlineSuccessTerminals != null
				&& !offlineSuccessTerminals.isEmpty()) {
			sb.append("下发成功的终端有：\r\n");
			for (Terminal terminal : offlineSuccessTerminals) {
				sb.append(terminal.getName());
				sb.append("\r\n");
			}
		}

		if (offlinefailTerminals != null && !offlinefailTerminals.isEmpty()) {
			sb.append("下发失败的终端有：\r\n");
			for (Terminal terminal : offlinefailTerminals) {
				sb.append("<font color='red'>" + terminal.getName() + "</font>");
				sb.append("\r\n");
			}
		}
		return sb.toString();

	}

	public List<Terminal> getOfflineSuccessTerminals() {
		return offlineSuccessTerminals;
	}

	public List<Terminal> getOfflinefailTerminals() {
		return offlinefailTerminals;
	}

}
