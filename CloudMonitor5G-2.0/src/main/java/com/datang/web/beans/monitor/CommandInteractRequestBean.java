package com.datang.web.beans.monitor;

/**
 * 命令交互请求Bean
 * 
 * @explain
 * @name CommandInteractRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:42:00
 */
public class CommandInteractRequestBean extends PublicRequestBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -711988444517481931L;
	/**
	 * 命令名称
	 */
	private String commandName;

	/**
	 * @return the commandName
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * @param commandName
	 *            the commandName to set
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	/* *
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommandInteractRequestBean [commandName=" + commandName
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", cityIds=" + cityIds + ", prestorecityIds="
				+ prestorecityIds + ", boxIds=" + boxIds + ", boxIdsSet="
				+ boxIdsSet + "]";
	}

}
