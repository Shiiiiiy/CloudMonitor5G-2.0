package com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole;

import java.io.Serializable;

public class WholeBean0 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6622435619689407773L;
	/**
	 * 呼叫建立时延
	 */
	private Float callEstablishDelay;
	/**
	 * INVITE->RRC连接建立完成时延
	 */
	private Float invite2rrcConnectionSeutpCompleteDelay;
	/**
	 * RRC连接建立完成->RRC连接重配置时延
	 */
	private Float rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay;
	/**
	 * INVITE 100->被叫paging时延
	 */
	private Float invite1002calledPagingDelay;
	/**
	 * 被叫INVITE->被叫INVITE 183时延
	 */
	private Float calledInvite2calledInvite183Delay;
	/**
	 * 主叫INVITE 183->主叫INVITE 180Ringing时延
	 */
	private Float callingInvite2callingInvite180RingingDelay;

	public Float getCallEstablishDelay() {
		return callEstablishDelay;
	}

	public void setCallEstablishDelay(Float callEstablishDelay) {
		this.callEstablishDelay = callEstablishDelay;
	}

	public Float getInvite2rrcConnectionSeutpCompleteDelay() {
		return invite2rrcConnectionSeutpCompleteDelay;
	}

	public void setInvite2rrcConnectionSeutpCompleteDelay(
			Float invite2rrcConnectionSeutpCompleteDelay) {
		this.invite2rrcConnectionSeutpCompleteDelay = invite2rrcConnectionSeutpCompleteDelay;
	}

	public Float getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay() {
		return rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay;
	}

	public void setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(
			Float rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay) {
		this.rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay = rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay;
	}

	public Float getInvite1002calledPagingDelay() {
		return invite1002calledPagingDelay;
	}

	public void setInvite1002calledPagingDelay(Float invite1002calledPagingDelay) {
		this.invite1002calledPagingDelay = invite1002calledPagingDelay;
	}

	public Float getCalledInvite2calledInvite183Delay() {
		return calledInvite2calledInvite183Delay;
	}

	public void setCalledInvite2calledInvite183Delay(
			Float calledInvite2calledInvite183Delay) {
		this.calledInvite2calledInvite183Delay = calledInvite2calledInvite183Delay;
	}

	public Float getCallingInvite2callingInvite180RingingDelay() {
		return callingInvite2callingInvite180RingingDelay;
	}

	public void setCallingInvite2callingInvite180RingingDelay(
			Float callingInvite2callingInvite180RingingDelay) {
		this.callingInvite2callingInvite180RingingDelay = callingInvite2callingInvite180RingingDelay;
	}

	@Override
	public String toString() {
		return "WholeBean0 [callEstablishDelay="
				+ callEstablishDelay
				+ ", invite2rrcConnectionSeutpCompleteDelay="
				+ invite2rrcConnectionSeutpCompleteDelay
				+ ", rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay="
				+ rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
				+ ", invite1002calledPagingDelay="
				+ invite1002calledPagingDelay
				+ ", calledInvite2calledInvite183Delay="
				+ calledInvite2calledInvite183Delay
				+ ", callingInvite2callingInvite180RingingDelay="
				+ callingInvite2callingInvite180RingingDelay + "]";
	}

}
