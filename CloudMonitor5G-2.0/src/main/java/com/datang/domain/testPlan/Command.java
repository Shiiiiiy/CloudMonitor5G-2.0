package com.datang.domain.testPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 测试命令
 * 
 * @author yinzhipeng
 * @date:2016年7月8日 下午1:33:02
 * @version
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IADS_TP_COMMAND")
@XStreamAlias("Command")
public class Command implements Serializable {
	/**
	 * 执行序号
	 */
	@XStreamOmitField
	private Integer runOrder;
	/**
	 * 主键
	 */
	@XStreamOmitField
	private Integer commandId;
	/**
	 * 上行速度
	 */
	@XStreamAlias("UpLinkSpeed")
	private String upLinkSpeed;
	/**
	 * 下行速度
	 */
	@XStreamAlias("DownLinkSpeed")
	private String downLinkSpeed;

	/**
	 * 名称
	 */
	@XStreamOmitField
	private String name;
	/**
	 * 通道编号
	 */
	@XStreamOmitField
	private String channelNo;
	/**
	 * 呼叫类型
	 */
	@XStreamOmitField
	private String callStyle;
	/**
	 * 重复次数
	 */
	@XStreamAlias("Repeat")
	@XStreamAsAttribute
	private String repeat = "999";
	/**
	 * 命令id
	 */
	@XStreamAlias("ID")
	private String id;
	/**
	 * 远端站点
	 */
	@XStreamAlias("RemoteHost")
	private String remoteHost;
	/**
	 * 账户
	 */
	@XStreamAlias("Account")
	private String account;
	/**
	 * 密码
	 */
	@XStreamAlias("Password")
	private String password;
	/**
	 * 0：主动模式,1：被动模式
	 */
	@XStreamAlias("Passive")
	private String passive;
	/**
	 * 0：Text模式传输,1：二进制模式
	 */
	@XStreamAlias("Binary")
	private String binary;
	/**
	 * 0：FTP文件上传 1：FTP文件下载
	 */
	@XStreamAlias("Download")
	private String download;
	/**
	 * FTP文件下载时，远程文件地址 FTP文件上传时，远程保存文件地址
	 */
	@XStreamAlias("RemoteFile")
	private String remoteFile;
	/**
	 * 刷新深度 整数，缺省为3
	 */
	@XStreamAlias("Deep")
	private String deep;
	/**
	 * 是否下载图片 0：不下载图片 1：下载图片
	 */
	@XStreamAlias("DownloadPicture")
	private String downloadPicture;
	/**
	 * 禁用的链接,遇到该连接是不往下连接
	 */
	@XStreamAlias("BlockLink")
	private String blockLink;
	/**
	 * 测试间隔 单位 秒
	 */
	@XStreamAlias("Interval")
	private String interval;
	/**
	 * 超时时间 单位 秒
	 */
	@XStreamAlias("TimeOut")
	private String timeOut;
	/**
	 * url
	 */
	@XStreamAlias("URL")
	private String url;
	/**
	 * 1为cmwap，2为cmnet， 3为ctwap，4为ctnet
	 */
	@XStreamAlias("APN")
	private String apn;
	/**
	 * 模拟手机类型
	 */
	@XStreamAlias("Agent")
	private String agent;
	/**
	 * 页面需要特殊处理 有以下模式： 0:CONN_CL(WAP 1.x) 1:CONN_CO(WAP 1.x) 2:CONN_SCO(WAP 1.x)
	 * 3:CONN_SCL(WAP 1.x) 4:CONN_HTTPS(WAP 1.x) 10:CONN_HTTP(WAP 2.x)
	 * 
	 * 11:CONN_HTTPS(WAP 1.x)
	 */
	@XStreamAlias("ConnectionMode")
	private String connectionMode;
	/**
	 * 网关地址
	 */
	@XStreamAlias("Gateway")
	private String gateway;
	/**
	 * 端口
	 */
	@XStreamAlias("Port")
	private String port;
	/**
	 * 呼叫号码
	 */
	@XStreamAlias("CallNumber")
	private String callNumber;
	/**
	 * 随机通话时长测试 0 固定通话时长 1 随机通话时长
	 */
	@XStreamAlias("RandomCall")
	private String randomCall;

	/**
	 * 当进行固定通话时长测试时表示通话时长，当进行随机通话时长测试时表示最小通话时长 单位 秒
	 */
	@XStreamAlias("Duration")
	private String duration;
	/**
	 * 当进行随机通话时长测试时，最长的通话时间 单位 秒
	 */
	@XStreamAlias("MaxTime")
	private String maxTime;
	/**
	 * 呼叫过程中是否做MOS测试 0 不做MOS测试 1 进行MOS测试
	 */
	@XStreamAlias("TestMOS")
	private String testMOS;
	/**
	 * 是否呼叫语音评估服务器测试 1 呼叫语音评估服务器进行MOS测试 0 不呼叫语音评估服务器
	 */
	@XStreamAlias("CallMOSServer")
	private String callMOSServer;

	/**
	 * MOS值的最低门限，当进行下行MOS测试或者单元内MOS测试时，MOS值低于该值 则需要记录该通话的语音采样
	 */
	@XStreamAlias("MOSLimit")
	private String mosLimit;

	/**
	 * 空闲等待时间 单位秒
	 */
	@XStreamAlias("WaitTimes")
	private String waitTimes;
	/**
	 * 保持时长 单位 秒
	 */
	@XStreamAlias("Keeptime")
	private String keeptime;
	/**
	 * 包的大小 单位 byte
	 */
	@XStreamAlias("Packagesize")
	private String packagesize;
	/**
	 * ip的地址
	 */
	@XStreamAlias("IP")
	private String ip;
	/**
	 * 目标号码
	 */
	@XStreamAlias("Destination")
	private String destination;
	/**
	 * 是发送还是接受 0 接收短信 1 发送短信
	 */
	@XStreamAlias("Mode")
	private String mode;
	/**
	 * 是否发送文本模式 0 发送PDU模式 1 发送文本模式
	 */
	@XStreamAlias("Text")
	private String text;
	/**
	 * 是否要求有状态报告 0 不需要 1 需要 发送短信时有效
	 */
	@XStreamAlias("Report")
	private String report;
	/**
	 * 发送的短信内容 发送短信时有效
	 */
	@XStreamAlias("Content")
	private String content;
	/**
	 * 服务中心号码 13800100500
	 */
	@XStreamAlias("ServerCenterAddress")
	private String serverCenterAddress;

	/**
	 * 前端随机生成图片文件，文件大小 单位KB
	 */
	@XStreamAlias("MediaFileSize")
	private String mediaFileSize;

	/**
	 * 如果接收手机是单元内测试模块，则需要指定接收MMS的内部模块号。 如：2
	 */
	@XStreamAlias("SyncMSNO")
	private String syncMSNO;
	/**
	 * 服务器地址 如 http://mmsc.monternet.com
	 */
	@XStreamAlias("ServerAddress")
	private String serverAddress;

	/**
	 * 如果发送MMS手机是单元内测试模块，则需要指定发送MMS的内部模块号。
	 */
	@XStreamAlias("SyncMSNOs")
	private String syncMSNOs;

	/**
	 * PUSH超时时间 单位 秒
	 */
	@XStreamAlias("PTimeOut")
	private String ptimeOut;

	/**
	 * 登录的飞信号
	 */
	@XStreamAlias("FetionNumber")
	private String fetionNumber;

	/**
	 * 目标手机号码
	 */
	@XStreamAlias("SDestination")
	private String sdestination;
	/**
	 * 目标飞信号码
	 */
	@XStreamAlias("FDestination")
	private String fdestination;

	/**
	 * 是否使用代理 0 不使用代理 1 使用代理
	 */
	@XStreamAlias("Proxy")
	private String proxy;

	/**
	 * 代理地址
	 */
	@XStreamAlias("Address")
	private String address;
	/**
	 * 代理类型： 0: HTTP 1: SOCKS5 2: SOCKS4
	 */
	@XStreamAlias("ProxyType")
	private String proxyType;
	/**
	 * 版本号 缺省16
	 */
	@XStreamAlias("Version")
	private String version;
	/**
	 * 用户名
	 */
	@XStreamAlias("Username")
	private String username;
	/**
	 * 是否使用rtp over rtsp(tcp) 0 使用 1 不使用 缺省 0
	 */
	@XStreamAlias("RTP")
	private String rtp;

	/**
	 * rtsp/rtp转接到此http端口 缺省 0
	 */
	@XStreamAlias("RtspHttpPort")
	private String rtspHttpPort;

	/**
	 * 本地rtp/udp端口 缺省5004
	 */
	@XStreamAlias("LocalRTPport")
	private String localRTPport;

	/**
	 * 初始缓冲时间 单位 ms，缺省 5000
	 */
	@XStreamAlias("PreBufferLength")
	private String preBufferLength;

	/**
	 * 重新缓冲时间 单位 ms，缺省 5000
	 */
	@XStreamAlias("RebufferLength")
	private String rebufferLength;
	/**
	 * 测试播放时间 单位 秒，缺省 150
	 */
	@XStreamAlias("PlayTime")
	private String playTime;
	/**
	 * 服务器地址
	 */
	@XStreamAlias("MailServer")
	private String mailServer;

	/**
	 * 收邮件后是否自动删除邮件 0 不删除 1 删除
	 */
	@XStreamAlias("Deletemail")
	private String deletemail;

	/**
	 * 选择保存邮件附件的本地文件路径名
	 */
	@XStreamAlias("Path")
	private String path;
	/**
	 * 是否使用SSL 0 不使用 1 使用 需要服务器支持
	 */
	@XStreamAlias("SSL")
	private String ssl;

	/**
	 * 发送者的名称
	 */
	@XStreamAlias("Sender")
	private String sender;

	/**
	 * 发送者的邮件地址
	 */
	@XStreamAlias("From")
	private String from;
	/**
	 * 目的邮件地址
	 */
	@XStreamAlias("To")
	private String to;
	/**
	 * 上传附件时，前端随机生成的上传文件的大小 单位KB
	 */
	@XStreamAlias("FileSize")
	private String fileSize;
	/**
	 * 邮件主题
	 */
	@XStreamAlias("Subject")
	private String subject;

	/**
	 * 邮件内容
	 */
	@XStreamAlias("Body")
	private String body;
	/**
	 * 验证模式： 0:None 1:CRAM MD5 2:AUTH LOGIN 3:LOGIN PLAIN 需要服务器支持 缺省 0
	 */
	@XStreamAlias("Authentication")
	private String authentication;

	/**
	 * 编码模式： 0：Arabic (Windows) 1：Baltic (Windows) 2：Central European (ISO)
	 * 3：Central European (Windows) 4：Chinese Simplified (GB2312) 5：Chinese
	 * Simplified (HZ) 6：Chinese Traditional (Big5) 7：Cyrilic (KOI8-R)
	 * 8：Cyrillic (Windows) 9：Greek (Windows) 10：Hebrew (Windows) 11：Japanese
	 * (JIS) 12：Korean 13：Korean (EUC) 14：Latin 9 (ISO) 15：Thai (Windows)
	 * 16：Turkish (Windows) 17：Unicode (UTF-7) 18：Unicode (UTF-8) 19：Vietnamese
	 * (Windows) 20：Western European (ISO) 21：Western European (Windows)
	 */
	@XStreamAlias("Encoding")
	private String encoding;

	/**
	 * 是否使用HTML格式发送消息 0 不使用 1 使用 缺省 0
	 */
	@XStreamAlias("HTML")
	private String html;

	/**
	 * 呼叫号码
	 */
	@XStreamAlias("PhoneNumber")
	private String phoneNumber;
	/**
	 * 拨号方式 0 键盘模拟 1 AT指令 2 TRACE指令 以何种方式拨叫依赖于模块的特性
	 */
	@XStreamAlias("DialMode")
	private String dialMode;
	/**
	 * VP测试过程中是否做VMOS测试 0不做VMOS测试 1进行VMOS测试
	 */
	@XStreamAlias("TestVMOS")
	private String testVMOS;
	/**
	 * 是否呼叫语音评估服务器测试 1 呼叫语音评估服务器进行VMOS测试 0 不呼叫语音评估服务器
	 */
	@XStreamAlias("CallVMOSServer")
	private String callVMOSServer;

	/**
	 * 综合得分MOS-I值的最低门限，当进行下行MOS-I测试或者单元内MOS-I测试时，MOS-I值低于该值 则需要记录该通话的语音采样
	 */
	@XStreamAlias("MOS-ILimit")
	private String mosILimit;
	/**
	 * 视频样本文件本地全路径－固定样本类似语音MOS样本
	 */
	@XStreamAlias("SampleFile")
	private String sampleFile;
	/**
	 * 有以下几种模式： 0：Voice IS 96 1：8K Loopback 2：Markov old 8K 3：Reserved 4：Voice
	 * IS 96 A 5：Voice 13K 6：RateSet 2 Markov new 13K 7：RateSet 1 Markov new 8K
	 * 8：13K Loopback 9：13K Markov old 13K 10：EVRC
	 */
	@XStreamAlias("AMRRate")
	private String amrRate;
	/**
	 * 发送和接收是否同步处理 0 不同步 1 同步 单元内部两个模块进行发送接收测试时使用，缺省 0
	 */
	@XStreamAlias("Synchronize")
	private String synchronize;
	/**
	 * 如果做测试前端单元内模块间发送和接受同步测试时，指定接收的内部模块号，否则置为0
	 */
	@XStreamAlias("DestMSNO")
	private String destMSNO;
	/**
	 * SSID
	 */
	@XStreamAlias("SSID")
	private String ssid;

	/**
	 * 保持时间 (WLAN)
	 */
	@XStreamAlias("Holdtime")
	private String holdtime;

	/**
	 * 用户名 (WLAN)
	 */
	@XStreamAlias("User")
	private String user;

	/**
	 * FTP用户名 (WLAN)
	 */
	@XStreamAlias("FTPAccount")
	private String ftpAccount;

	/**
	 * FTP密码 (WLAN)
	 */
	@XStreamAlias("FTPPassword")
	private String ftpPassword;

	/**
	 * 线程个数 缺省1
	 * 
	 * @author yinzhipeng 2015-04-17
	 */
	@XStreamAlias("ThreadNum")
	private String threadNum;
	/**
	 * 单次业务最多可拨号次数 缺省3
	 * 
	 * @author yinzhipeng 2015-04-17
	 */
	@XStreamAlias("MaxDialNum")
	private String maxDialNum;
	/**
	 * 单次业务最多FTP登陆次数 缺省3
	 * 
	 * @author yinzhipeng 2015-04-17
	 */
	@XStreamAlias("MaxFTPland")
	private String maxFTPland;
	/**
	 * 业务类型0:登陆,1:页面刷新,2:文件下载,缺省0
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("Model")
	private String model;
	/**
	 * 下载文件大小（Byte）缺省25
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("FileSize")
	private String dlFileSize;
	/**
	 * 代理端口 缺省6089
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("ProxyPort")
	private String proxyPort;
	/**
	 * 必选浏览地址 地址之间用英文字母逗号“,”进行分割
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("UrlMust")
	private String urlMust;

	/**
	 * 随机浏览地址 地址之间用英文字母逗号“,”进行分割
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("UrlRandom")
	private String urlRandom;

	/**
	 * 浏览时间间隔 缺省2
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("UrlInterval")
	private String urlInterval;
	/**
	 * 每次随机地址个数 缺省4
	 * 
	 * @author yinzhipeng 2015-04-20
	 */
	@XStreamAlias("UrlRandomNum")
	private String urlRandomNum;

	/**
	 * 缓冲区总时长（单位 秒）缺省60
	 * 
	 * @author yinzhipeng 2015-04-23
	 */
	@XStreamAlias("BufferLength")
	private String bufferLength;
	/**
	 * 缓冲播放门限（单位 秒） 缺省5
	 * 
	 * @author yinzhipeng 2015-04-23
	 */
	@XStreamAlias("BufferPlayThreshold")
	private String bufferPlayThreshold;
	/**
	 * @yinzhipeng udp测试新增<br>
	 *             上传或下载传输时间
	 */
	@XStreamAlias("TransferTime")
	private String transferTime;
	/**
	 * @yinzhipeng udp测试新增<br>
	 *             掉线时间
	 */
	@XStreamAlias("Droptime")
	private String dropTime;
	/**
	 * @yinzhipeng udp测试新增<br>
	 *             缓冲大小
	 */
	@XStreamAlias("BufferSize")
	private String bufferSize;
	/**
	 * @yinzhipeng udp测试新增<br>
	 *             数据包大小
	 */
	@XStreamAlias("PacketSize")
	private String packetSize;
	/**
	 * @yinzhipeng udp测试新增<br>
	 *             带宽
	 */
	@XStreamAlias("BandWidth")
	private String bandWidth;

	/**
	 * 
	 * @yinzhipeng (E)GPRS Attach测试新增<br>
	 *             等待时长
	 */
	@XStreamAlias("WaitTime")
	private String waitTime;

	/**
	 * 
	 * @yinzhipeng 移动互联网应用层业务-PING/UDP测试<br>
	 *             网络连接类型
	 * 
	 */
	@XStreamAlias("NetLink")
	private String netLink;
	/**
	 * 
	 * 视频分辨率
	 * 
	 */
	@XStreamAlias("VideoResolution")
	private String videoResolution;
	
	/**
	 * 语音业务结束多久以后数据业务结束
	 */
	@XStreamAlias("FirstServiceEndDelay")
	private String firstServiceEndDelay;
	
	/**
	 * 数据业务开始多久以后语音呼叫
	 */
	@XStreamAlias("SecondServiceStartDelay")
	private String secondServiceStartDelay;
	
	@XStreamAlias("FirstServiceEndType")
	private String firstServiceEndType = "1";
	
	@XStreamAlias("SecondServiceStartType")
	private String secondServiceStartType = "1";
	
	@XStreamAlias("SecondServiceEndType")
	private String secondServiceEndType = "1";
	
	@XStreamAlias("CallName")
	private String callName;
	
	@XStreamAlias("CallModel")
	private String callModel;

	// ---------------------------------------------------------------------

	@Column(name = "HOLDTIME")
	public String getHoldtime() {
		return holdtime;
	}

	public void setHoldtime(String holdtime) {
		this.holdtime = holdtime;
	}

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
	 * @return the id
	 */
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	@Column(name = "P_AGENT")
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
	@Column(name = "P_CONNECTION_MODE")
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
	@Column(name = "P_GATEWAY")
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
	@Column(name = "P_PORT")
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
	@Id
	@GeneratedValue
	@Column(name = "COMMAND_ID")
	public Integer getCommandId() {
		return commandId;
	}

	/**
	 * @param commandId
	 *            the commandId to set
	 */
	public void setCommandId(Integer commandId) {
		this.commandId = commandId;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME")
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

	/**
	 * @return the callNumber
	 */
	@Column(name = "CALL_NUMBER")
	public String getCallNumber() {
		return callNumber;
	}

	/**
	 * @param callNumber
	 *            the callNumber to set
	 */
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	/**
	 * @return the randomCall
	 */
	@Column(name = "RANDOM_CALL")
	public String getRandomCall() {
		return randomCall;
	}

	/**
	 * @param randomCall
	 *            the randomCall to set
	 */
	public void setRandomCall(String randomCall) {
		this.randomCall = randomCall;
	}

	/**
	 * @return the duration
	 */
	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return the maxTime
	 */
	@Column(name = "MAX_TIME")
	public String getMaxTime() {
		return maxTime;
	}

	/**
	 * @param maxTime
	 *            the maxTime to set
	 */
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * @return the testMOS
	 */
	@Column(name = "TEST_MOS")
	public String getTestMOS() {
		return testMOS;
	}

	/**
	 * @param testMOS
	 *            the testMOS to set
	 */
	public void setTestMOS(String testMOS) {
		this.testMOS = testMOS;
	}

	/**
	 * @return the callMOSServer
	 */
	@Column(name = "CALL_MOS_SERVER")
	public String getCallMOSServer() {
		return callMOSServer;
	}

	/**
	 * @param callMOSServer
	 *            the callMOSServer to set
	 */
	public void setCallMOSServer(String callMOSServer) {
		this.callMOSServer = callMOSServer;
	}

	/**
	 * @return the mosLimit
	 */
	@Column(name = "MOS_LIMIT")
	public String getMosLimit() {
		return mosLimit;
	}

	/**
	 * @param mosLimit
	 *            the mosLimit to set
	 */
	public void setMosLimit(String mosLimit) {
		this.mosLimit = mosLimit;
	}

	/**
	 * @return the waitTimes
	 */
	@Column(name = "WAIT_TIMES")
	public String getWaitTimes() {
		return waitTimes;
	}

	/**
	 * @param waitTimes
	 *            the waitTimes to set
	 */
	public void setWaitTimes(String waitTimes) {
		this.waitTimes = waitTimes;
	}

	/**
	 * @return the keeptime
	 */
	@Column(name = "KEEP_TIME")
	public String getKeeptime() {
		return keeptime;
	}

	/**
	 * @param keeptime
	 *            the keeptime to set
	 */
	public void setKeeptime(String keeptime) {
		this.keeptime = keeptime;
	}

	/**
	 * @return the packagesize
	 */
	@Column(name = "P_PACKAGESIZE")
	public String getPackagesize() {
		return packagesize;
	}

	/**
	 * @param packagesize
	 *            the packagesize to set
	 */
	public void setPackagesize(String packagesize) {
		this.packagesize = packagesize;
	}

	/**
	 * @return the ip
	 */
	@Column(name = "P_IP")
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the destination
	 */
	@Column(name = "P_DESTINATION")
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the mode
	 */
	@Column(name = "P_MODE")
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the text
	 */
	@Column(name = "P_TEXT")
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the report
	 */
	@Column(name = "P_REPORT")
	public String getReport() {
		return report;
	}

	/**
	 * @param report
	 *            the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * @return the content
	 */
	@Column(name = "P_CONTENT")
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the serverCenterAddress
	 */
	@Column(name = "SERVER_CENTER_ADDRESS")
	public String getServerCenterAddress() {
		return serverCenterAddress;
	}

	/**
	 * @param serverCenterAddress
	 *            the serverCenterAddress to set
	 */
	public void setServerCenterAddress(String serverCenterAddress) {
		this.serverCenterAddress = serverCenterAddress;
	}

	/**
	 * @return the mediaFileSize
	 */
	@Column(name = "MEDIA_FILE_SIZE")
	public String getMediaFileSize() {
		return mediaFileSize;
	}

	/**
	 * @param mediaFileSize
	 *            the mediaFileSize to set
	 */
	public void setMediaFileSize(String mediaFileSize) {
		this.mediaFileSize = mediaFileSize;
	}

	/**
	 * @return the syncMSNO
	 */
	@Column(name = "SYNC_MSNO")
	public String getSyncMSNO() {
		return syncMSNO;
	}

	/**
	 * @param syncMSNO
	 *            the syncMSNO to set
	 */
	public void setSyncMSNO(String syncMSNO) {
		this.syncMSNO = syncMSNO;
	}

	/**
	 * @return the serverAddress
	 */
	@Column(name = "SERVER_ADDRESS")
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress
	 *            the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the syncMSNOs
	 */
	@Column(name = "SYNC_MSNOS")
	public String getSyncMSNOs() {
		return syncMSNOs;
	}

	/**
	 * @param syncMSNOs
	 *            the syncMSNOs to set
	 */
	public void setSyncMSNOs(String syncMSNOs) {
		this.syncMSNOs = syncMSNOs;
	}

	/**
	 * @return the ptimeOut
	 */
	@Column(name = "P_TIMEOUT")
	public String getPtimeOut() {
		return ptimeOut;
	}

	/**
	 * @param ptimeOut
	 *            the ptimeOut to set
	 */
	public void setPtimeOut(String ptimeOut) {
		this.ptimeOut = ptimeOut;
	}

	/**
	 * @return the fetionNumber
	 */
	@Column(name = "FETION_NUMBER")
	public String getFetionNumber() {
		return fetionNumber;
	}

	/**
	 * @param fetionNumber
	 *            the fetionNumber to set
	 */
	public void setFetionNumber(String fetionNumber) {
		this.fetionNumber = fetionNumber;
	}

	/**
	 * @return the sdestination
	 */
	@Column(name = "S_DESTINATION")
	public String getSdestination() {
		return sdestination;
	}

	/**
	 * @param sdestination
	 *            the sdestination to set
	 */
	public void setSdestination(String sdestination) {
		this.sdestination = sdestination;
	}

	/**
	 * @return the fdestination
	 */
	@Column(name = "F_DESTINATION")
	public String getFdestination() {
		return fdestination;
	}

	/**
	 * @param fdestination
	 *            the fdestination to set
	 */
	public void setFdestination(String fdestination) {
		this.fdestination = fdestination;
	}

	/**
	 * @return the proxy
	 */
	@Column(name = "P_PROXY")
	public String getProxy() {
		return proxy;
	}

	/**
	 * @param proxy
	 *            the proxy to set
	 */
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the address
	 */
	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the proxyType
	 */
	@Column(name = "PROXY_TYPE")
	public String getProxyType() {
		return proxyType;
	}

	/**
	 * @param proxyType
	 *            the proxyType to set
	 */
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	/**
	 * @return the version
	 */
	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the username
	 */
	@Column(name = "P_USERNAME")
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the rtp
	 */
	@Column(name = "RTP")
	public String getRtp() {
		return rtp;
	}

	/**
	 * @param rtp
	 *            the rtp to set
	 */
	public void setRtp(String rtp) {
		this.rtp = rtp;
	}

	/**
	 * @return the rtspHttpPort
	 */
	@Column(name = "RTS_HTTP_PORT")
	public String getRtspHttpPort() {
		return rtspHttpPort;
	}

	/**
	 * @param rtspHttpPort
	 *            the rtspHttpPort to set
	 */
	public void setRtspHttpPort(String rtspHttpPort) {
		this.rtspHttpPort = rtspHttpPort;
	}

	/**
	 * @return the localRTPport
	 */
	@Column(name = "LOCAL_RTP_PORT")
	public String getLocalRTPport() {
		return localRTPport;
	}

	/**
	 * @param localRTPport
	 *            the localRTPport to set
	 */
	public void setLocalRTPport(String localRTPport) {
		this.localRTPport = localRTPport;
	}

	/**
	 * @return the preBufferLength
	 */
	@Column(name = "PRE_BUFFER_LENGTH")
	public String getPreBufferLength() {
		return preBufferLength;
	}

	/**
	 * @param preBufferLength
	 *            the preBufferLength to set
	 */
	public void setPreBufferLength(String preBufferLength) {
		this.preBufferLength = preBufferLength;
	}

	/**
	 * @return the rebufferLength
	 */
	@Column(name = "REBUFFER_LENGTH")
	public String getRebufferLength() {
		return rebufferLength;
	}

	/**
	 * @param rebufferLength
	 *            the rebufferLength to set
	 */
	public void setRebufferLength(String rebufferLength) {
		this.rebufferLength = rebufferLength;
	}

	/**
	 * @return the playTime
	 */
	@Column(name = "PLAY_TIME")
	public String getPlayTime() {
		return playTime;
	}

	/**
	 * @param playTime
	 *            the playTime to set
	 */
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	/**
	 * @return the mailServer
	 */
	@Column(name = "MAIL_SERVER")
	public String getMailServer() {
		return mailServer;
	}

	/**
	 * @param mailServer
	 *            the mailServer to set
	 */
	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	/**
	 * @return the deletemail
	 */
	@Column(name = "DELETE_MAIL")
	public String getDeletemail() {
		return deletemail;
	}

	/**
	 * @param deletemail
	 *            the deletemail to set
	 */
	public void setDeletemail(String deletemail) {
		this.deletemail = deletemail;
	}

	/**
	 * @return the path
	 */
	@Column(name = "PATH")
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the ssl
	 */
	@Column(name = "SSL")
	public String getSsl() {
		return ssl;
	}

	/**
	 * @param ssl
	 *            the ssl to set
	 */
	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	/**
	 * @return the sender
	 */
	@Column(name = "SENDER")
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the from
	 */
	@Column(name = "P_FROM")
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	@Column(name = "P_TO")
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the fileSize
	 */
	@Column(name = "P_FILE_SIZE")
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the subject
	 */
	@Column(name = "P_SUBJECT")
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	@Column(name = "P_BODY")
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the authentication
	 */
	@Column(name = "P_AUTHENTICATION")
	public String getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication
	 *            the authentication to set
	 */
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return the encoding
	 */
	@Column(name = "P_ENCODING")
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the html
	 */
	@Column(name = "HTML")
	public String getHtml() {
		return html;
	}

	/**
	 * @param html
	 *            the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * @return the phoneNumber
	 */
	@Column(name = "PHONE_NUMBER")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the dialMode
	 */
	@Column(name = "DIAL_MODE")
	public String getDialMode() {
		return dialMode;
	}

	/**
	 * @param dialMode
	 *            the dialMode to set
	 */
	public void setDialMode(String dialMode) {
		this.dialMode = dialMode;
	}

	/**
	 * @return the testVMOS
	 */
	@Column(name = "TEST_VMOS")
	public String getTestVMOS() {
		return testVMOS;
	}

	/**
	 * @param testVMOS
	 *            the testVMOS to set
	 */
	public void setTestVMOS(String testVMOS) {
		this.testVMOS = testVMOS;
	}

	/**
	 * @return the callVMOSServer
	 */
	@Column(name = "CALL_VOMS_SERVER")
	public String getCallVMOSServer() {
		return callVMOSServer;
	}

	/**
	 * @param callVMOSServer
	 *            the callVMOSServer to set
	 */
	public void setCallVMOSServer(String callVMOSServer) {
		this.callVMOSServer = callVMOSServer;
	}

	/**
	 * @return the mosILimit
	 */
	@Column(name = "MOSI_LIMIT")
	public String getMosILimit() {
		return mosILimit;
	}

	/**
	 * @param mosILimit
	 *            the mosILimit to set
	 */
	public void setMosILimit(String mosILimit) {
		this.mosILimit = mosILimit;
	}

	/**
	 * @return the sampleFile
	 */
	@Column(name = "SAMPLE_FILE")
	public String getSampleFile() {
		return sampleFile;
	}

	/**
	 * @param sampleFile
	 *            the sampleFile to set
	 */
	public void setSampleFile(String sampleFile) {
		this.sampleFile = sampleFile;
	}

	/**
	 * @return the amrRate
	 */
	@Column(name = "AMR_RATE")
	public String getAmrRate() {
		return amrRate;
	}

	/**
	 * @param amrRate
	 *            the amrRate to set
	 */
	public void setAmrRate(String amrRate) {
		this.amrRate = amrRate;
	}

	/**
	 * @return the synchronize
	 */
	@Column(name = "P_SYNCHRONIZE")
	public String getSynchronize() {
		return synchronize;
	}

	/**
	 * @param synchronize
	 *            the synchronize to set
	 */
	public void setSynchronize(String synchronize) {
		this.synchronize = synchronize;
	}

	/**
	 * @return the destMSNO
	 */
	@Column(name = "DEST_MSNO")
	public String getDestMSNO() {
		return destMSNO;
	}

	/**
	 * @param destMSNO
	 *            the destMSNO to set
	 */
	public void setDestMSNO(String destMSNO) {
		this.destMSNO = destMSNO;
	}

	/**
	 * @return the upLinkSpeed
	 */
	@Column(name = "UP_LINK_SPEED")
	public String getUpLinkSpeed() {
		return upLinkSpeed;
	}

	/**
	 * @param upLinkSpeed
	 *            the upLinkSpeed to set
	 */
	public void setUpLinkSpeed(String upLinkSpeed) {
		this.upLinkSpeed = upLinkSpeed;
	}

	/**
	 * @return the downLinkSpeed
	 */
	@Column(name = "DOWN_LINK_SPEED")
	public String getDownLinkSpeed() {
		return downLinkSpeed;
	}

	/**
	 * @param downLinkSpeed
	 *            the downLinkSpeed to set
	 */
	public void setDownLinkSpeed(String downLinkSpeed) {
		this.downLinkSpeed = downLinkSpeed;
	}

	/**
	 * @return the channelNo
	 */
	@Column(name = "CHANNEL_NO")
	public String getChannelNo() {
		return channelNo;
	}

	/**
	 * @param channelNo
	 *            the channelNo to set
	 */
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	/**
	 * @return the callStyle
	 */
	@Column(name = "CALL_STYLE")
	public String getCallStyle() {
		return callStyle;
	}

	/**
	 * @param callStyle
	 *            the callStyle to set
	 */
	public void setCallStyle(String callStyle) {
		this.callStyle = callStyle;
	}

	@Column(name = "SSID")
	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Column(name = "W_USER")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Column(name = "FTPACCOUNT")
	public String getFtpAccount() {
		return ftpAccount;
	}

	public void setFtpAccount(String ftpAccount) {
		this.ftpAccount = ftpAccount;
	}

	@Column(name = "FTPPASSWORD")
	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	@Column(name = "THREADNUM")
	public String getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(String threadNum) {
		this.threadNum = threadNum;
	}

	@Column(name = "MAXDIALNUM")
	public String getMaxDialNum() {
		return maxDialNum;
	}

	public void setMaxDialNum(String maxDialNum) {
		this.maxDialNum = maxDialNum;
	}

	@Column(name = "MAXFTPLAND")
	public String getMaxFTPland() {
		return maxFTPland;
	}

	public void setMaxFTPland(String maxFTPland) {
		this.maxFTPland = maxFTPland;
	}

	@Column(name = "MODEL")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "DL_FILESIZE")
	public String getDlFileSize() {
		return dlFileSize;
	}

	public void setDlFileSize(String dlFileSize) {
		this.dlFileSize = dlFileSize;
	}

	@Column(name = "PROXYPORT")
	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	@Column(name = "URLMUST")
	public String getUrlMust() {
		return urlMust;
	}

	public void setUrlMust(String urlMust) {
		this.urlMust = urlMust;
	}

	@Column(name = "URLRANDOM")
	public String getUrlRandom() {
		return urlRandom;
	}

	public void setUrlRandom(String urlRandom) {
		this.urlRandom = urlRandom;
	}

	@Column(name = "URLINTERVAL")
	public String getUrlInterval() {
		return urlInterval;
	}

	public void setUrlInterval(String urlInterval) {
		this.urlInterval = urlInterval;
	}

	@Column(name = "URLRANDOMNUM")
	public String getUrlRandomNum() {
		return urlRandomNum;
	}

	public void setUrlRandomNum(String urlRandomNum) {
		this.urlRandomNum = urlRandomNum;
	}

	@Column(name = "BUFFERLENGTH")
	public String getBufferLength() {
		return bufferLength;
	}

	public void setBufferLength(String bufferLength) {
		this.bufferLength = bufferLength;
	}

	@Column(name = "BUFFERPLAYTHRESHOLD")
	public String getBufferPlayThreshold() {
		return bufferPlayThreshold;
	}

	public void setBufferPlayThreshold(String bufferPlayThreshold) {
		this.bufferPlayThreshold = bufferPlayThreshold;
	}

	/**
	 * @return the runOrderrunOrder
	 */
	@Column(name = "RUN_ORDER")
	public Integer getRunOrder() {
		return runOrder;
	}

	/**
	 * @param runOrder
	 *            the runOrder to set
	 */
	public void setRunOrder(Integer runOrder) {
		this.runOrder = runOrder;
	}

	/**
	 * @return the transferTimetransferTime
	 */
	@Column(name = "TRANSFER_TIME")
	public String getTransferTime() {
		return transferTime;
	}

	/**
	 * @param transferTime
	 *            the transferTime to set
	 */
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}

	/**
	 * @return the dropTimedropTime
	 */
	@Column(name = "DROP_TIME")
	public String getDropTime() {
		return dropTime;
	}

	/**
	 * @param dropTime
	 *            the dropTime to set
	 */
	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}

	/**
	 * @return the bufferSizebufferSize
	 */
	@Column(name = "BUFFER_SIZE")
	public String getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	public void setBufferSize(String bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the packetSizepacketSize
	 */
	@Column(name = "PACKET_SIZE")
	public String getPacketSize() {
		return packetSize;
	}

	/**
	 * @param packetSize
	 *            the packetSize to set
	 */
	public void setPacketSize(String packetSize) {
		this.packetSize = packetSize;
	}

	/**
	 * @return the bandWidthbandWidth
	 */
	@Column(name = "BAND_WIDTH")
	public String getBandWidth() {
		return bandWidth;
	}

	/**
	 * @param bandWidth
	 *            the bandWidth to set
	 */
	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}

	/**
	 * @return the waitTimewaitTime
	 */
	@Column(name = "WAIT_TIME")
	public String getWaitTime() {
		return waitTime;
	}

	/**
	 * @param waitTime
	 *            the waitTime to set
	 */
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * @return the netLinknetLink
	 */
	@Column(name = "NET_LINK")
	public String getNetLink() {
		return netLink;
	}

	/**
	 * @param netLink
	 *            the netLink to set
	 */
	public void setNetLink(String netLink) {
		this.netLink = netLink;
	}

	/**
	 * @return the videoResolution
	 */
	@Column(name = "VIDEORESOLUTION")
	public String getVideoResolution() {
		return videoResolution;
	}

	/**
	 * @param the
	 *            videoResolution to set
	 */

	public void setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
	}

	/**
	 * 语音业务结束多久以后数据业务结束
	 */
	@Column(name = "FIRST_SERVICE_END_DELAY")
	public String getFirstServiceEndDelay() {
		return firstServiceEndDelay;
	}

	/**
	 * 语音业务结束多久以后数据业务结束
	 */
	public void setFirstServiceEndDelay(String firstServiceEndDelay) {
		this.firstServiceEndDelay = firstServiceEndDelay;
	}

	/**
	 * 数据业务开始多久以后语音呼叫
	 */
	@Column(name = "SECOND_SERVICE_START_DELAY")
	public String getSecondServiceStartDelay() {
		return secondServiceStartDelay;
	}

	/**数据业务开始多久以后语音呼叫
	 * 
	 */
	public void setSecondServiceStartDelay(String secondServiceStartDelay) {
		this.secondServiceStartDelay = secondServiceStartDelay;
	}

	@Column(name = "FIRST_SERVICE_END_TYPE")
	public String getFirstServiceEndType() {
		return firstServiceEndType;
	}

	public void setFirstServiceEndType(String firstServiceEndType) {
		this.firstServiceEndType = firstServiceEndType;
	}

	@Column(name = "SECOND_SERVICE_START_TYPE")
	public String getSecondServiceStartType() {
		return secondServiceStartType;
	}

	public void setSecondServiceStartType(String secondServiceStartType) {
		this.secondServiceStartType = secondServiceStartType;
	}

	@Column(name = "SECOND_SERVICE_END_TYPE")
	public String getSecondServiceEndType() {
		return secondServiceEndType;
	}

	public void setSecondServiceEndType(String secondServiceEndType) {
		this.secondServiceEndType = secondServiceEndType;
	}
	
	@Column(name = "CALL_NAME")
	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	@Column(name = "CALL_MODEL")
	public String getCallModel() {
		return callModel;
	}

	public void setCallModel(String callModel) {
		this.callModel = callModel;
	}

}
