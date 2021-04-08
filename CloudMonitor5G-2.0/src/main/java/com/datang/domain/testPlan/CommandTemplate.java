package com.datang.domain.testPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 命令模版
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:37:51
 * @version
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IADS_TP_COMMAND_TEMPLATE")
public class CommandTemplate implements Serializable {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 命令id
	 */
	private String commandId;
	/**
	 * 重复次数
	 */
	private String repeat;
	/**
	 * 主键
	 */
	private Integer commandTemplateId;
	/**
	 * 远端站点
	 */

	private String remoteHost;
	/**
	 * 账户
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 模式：主动模式/被动模式
	 */
	private String passive;
	/**
	 * 传输模式：text模式/二进制模式
	 */
	private String binary;
	/**
	 * ftp文件上传/ftp文件下载
	 */
	private String download;
	/**
	 * ftp下载时，远程文件地址；ftp文件上传时，远程保存文件地址
	 */
	private String remoteFile;
	/**
	 * 刷新深度
	 */
	private String deep;
	/**
	 * 是否下载图片：0：不下载图片，1：下载图片
	 */
	private String downloadPicture;
	/**
	 * 禁用的链接
	 */
	private String blockLink;
	/**
	 * 测试间隔
	 */
	private String interval;
	/**
	 * 超时时间
	 */
	private String timeOut;
	/**
	 * url
	 */
	private String url;
	/**
	 * 接入点号码
	 */
	private String apn;
	/**
	 * 模拟手机类型
	 */
	private String agent;
	/**
	 * 连接模式
	 */
	private String connectionMode;
	/**
	 * 网关地址
	 */
	private String gateway;
	/**
	 * 端口
	 */
	private String port;

	/**
	 * @return the repeat
	 */
	@Column(name = "REPEAT")
	public String getRepeat() {
		return repeat;
	}

	/**
	 * @param repeat
	 *            the repeat to set
	 */

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	/**
	 * @return the remoteHost
	 */
	@Column(name = "REMOTE_HOST")
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @param remoteHost
	 *            the remoteHost to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * @return the account
	 */
	@Column(name = "ACCOUNT")
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the password
	 */
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passive
	 */
	@Column(name = "PASSIVE")
	public String getPassive() {
		return passive;
	}

	/**
	 * @param passive
	 *            the passive to set
	 */
	public void setPassive(String passive) {
		this.passive = passive;
	}

	/**
	 * @return the binary
	 */
	@Column(name = "BINARY_COLUMN")
	public String getBinary() {
		return binary;
	}

	/**
	 * @param binary
	 *            the binary to set
	 */
	public void setBinary(String binary) {
		this.binary = binary;
	}

	/**
	 * @return the download
	 */
	@Column(name = "DOWNLOAD")
	public String getDownload() {
		return download;
	}

	/**
	 * @param download
	 *            the download to set
	 */
	public void setDownload(String download) {
		this.download = download;
	}

	/**
	 * @return the remoteFile
	 */
	@Column(name = "REMOTE_FILE")
	public String getRemoteFile() {
		return remoteFile;
	}

	/**
	 * @param remoteFile
	 *            the remoteFile to set
	 */
	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}

	/**
	 * @return the deep
	 */
	@Column(name = "DEEP")
	public String getDeep() {
		return deep;
	}

	/**
	 * @param deep
	 *            the deep to set
	 */
	public void setDeep(String deep) {
		this.deep = deep;
	}

	/**
	 * @return the downloadPicture
	 */
	@Column(name = "DOWNLOAD_PICTURE")
	public String getDownloadPicture() {
		return downloadPicture;
	}

	/**
	 * @param downloadPicture
	 *            the downloadPicture to set
	 */
	public void setDownloadPicture(String downloadPicture) {
		this.downloadPicture = downloadPicture;
	}

	/**
	 * @return the blockLink
	 */
	@Column(name = "BLOCK_LINK")
	public String getBlockLink() {
		return blockLink;
	}

	/**
	 * @param blockLink
	 *            the blockLink to set
	 */
	public void setBlockLink(String blockLink) {
		this.blockLink = blockLink;
	}

	/**
	 * @return the interval
	 */
	@Column(name = "INTERVAL")
	public String getInterval() {
		return interval;
	}

	/**
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(String interval) {
		this.interval = interval;
	}

	/**
	 * @return the timeOut
	 */
	@Column(name = "TIMEOUT")
	public String getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return the url
	 */
	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the apn
	 */
	@Column(name = "APN")
	public String getApn() {
		return apn;
	}

	/**
	 * @param apn
	 *            the apn to set
	 */
	public void setApn(String apn) {
		this.apn = apn;
	}

	/**
	 * @return the agent
	 */
	@Column(name = "AGENT")
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return the connectionMode
	 */
	@Column(name = "CONNECTION_MODE")
	public String getConnectionMode() {
		return connectionMode;
	}

	/**
	 * @param connectionMode
	 *            the connectionMode to set
	 */
	public void setConnectionMode(String connectionMode) {
		this.connectionMode = connectionMode;
	}

	/**
	 * @return the gateway
	 */
	@Column(name = "GATEWAY")
	public String getGateway() {
		return gateway;
	}

	/**
	 * @param gateway
	 *            the gateway to set
	 */
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	/**
	 * @return the port
	 */
	@Column(name = "PORT")
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the commandId
	 */
	@Column(name = "COMMAND_ID")
	public String getCommandId() {
		return commandId;
	}

	/**
	 * @param commandId
	 *            the commandId to set
	 */
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	/**
	 * @return the commandTemplateId
	 */
	@Id
	@GeneratedValue
	@Column(name = "COMMAND_TEMPLATE_ID")
	public Integer getCommandTemplateId() {
		return commandTemplateId;
	}

	/**
	 * @param commandTemplateId
	 *            the commandTemplateId to set
	 */
	public void setCommandTemplateId(Integer commandTemplateId) {
		this.commandTemplateId = commandTemplateId;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	@Column(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
