/**
 * 
 */
package com.datang.web.action.testManage.testPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.domain.testPlan.Command;
import com.datang.domain.testPlan.HttpCommandURL;
import com.datang.domain.testPlan.TestPlan;
import com.datang.domain.testPlan.TestScheme;
import com.datang.domain.testPlan.Time;
import com.datang.exception.ApplicationException;
import com.datang.service.testPlan.HttpCommandURLService;
import com.datang.util.DateBeanUtils;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.testPlan.DateBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 测试任务操作Action
 * 
 * @author yinzhipeng
 * @date:2016年8月4日 下午1:48:20
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TestSuitAction extends PageAction implements
		ModelDriven<TestScheme> {

	/**
	 * 表单对象
	 */
	private TestScheme testSuit = new TestScheme();
	/**
	 * 测试任务开始结束日期和时间
	 */
	private String[] beginDate;
	private String[] endDate;
	private String[] beginTime;
	private String[] endTime;
	/**
	 * 测试命令唯一标识
	 */
	private String commandId;
	/**
	 * 测试命令名称数组
	 */
	private String[] commandNames;
	/**
	 * 测试命令名称
	 */
	private String commandName;
	
	@Autowired
	private HttpCommandURLService httpCommandURLService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public TestScheme getModel() {
		return testSuit;
	}

	/**
	 * 保存测试任务集
	 * 
	 * @return
	 */
	public String saveOrUpdate() {
		try {
			initProperties();
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 初始化属性
	 */
	private void initProperties() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		TestPlan testPlan = (TestPlan) session.getAttribute("testPlan");
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		if (testSchemes == null) {
			testSchemes = new ArrayList<TestScheme>();
			testPlan.setTestSuit(testSchemes);
		}
		this.initTimes(beginTime, endTime, testSuit);
		this.initDates(beginDate, endDate, testSuit);
		TestScheme findedTestScheme = testPlan.findTestScheme(testSuit
				.getName());
		Object editTestSuit = session.getAttribute("editTestSuit");
		boolean edit = false;
		if (editTestSuit != null && !"".equals(editTestSuit)) {
			edit = true;
		}
		testSuit.setDateBeans(DateBeanUtils.parse(testSuit.getExecutiveDate()));
		if (edit) {
			// 修改
			TestScheme oldTestScheme = (TestScheme) session
					.getAttribute("testSuit");
			if (findedTestScheme != null
					&& oldTestScheme.getName() != findedTestScheme.getName()) {
				throw new ApplicationException("测试任务名称在该测试计划中已存在");
			}
			oldTestScheme.setName(testSuit.getName());
			oldTestScheme.setDesc(testSuit.getDesc());
			oldTestScheme.setEnable(testSuit.getEnable());
			oldTestScheme.setDateBeans(testSuit.getDateBeans());
			oldTestScheme.setMsNo(testSuit.getMsNo());
			oldTestScheme.setName(testSuit.getName());
			oldTestScheme.setTimeCondition(testSuit.getTimeCondition());
			oldTestScheme.initTimes(testSuit.getTimes());
			oldTestScheme.setExecutiveDate(testSuit.getExecutiveDate());
			oldTestScheme.setModeLock(testSuit.getModeLock());
			if (null != testSuit.getRunOrder()) {
				oldTestScheme.setRunOrder(testSuit.getRunOrder());
			}
			oldTestScheme.getCommandList().setRepeat(
					testSuit.getCommandList().getRepeat());
			oldTestScheme
					.getCommandList()
					.getSynchronize()
					.setType(
							testSuit.getCommandList().getSynchronize()
									.getType());
		} else {
			// 新增
			TestScheme addTestScheme = (TestScheme) session
					.getAttribute("addTestScheme");
			if (testPlan.findTestScheme(testSuit.getName()) != null) {
				throw new ApplicationException("测试任务名称在该测试计划中已存在");
			}
			addTestScheme.setName(testSuit.getName());
			addTestScheme.setDesc(testSuit.getDesc());
			addTestScheme.setEnable(testSuit.getEnable());
			addTestScheme.setDateBeans(testSuit.getDateBeans());
			addTestScheme.setMsNo(testSuit.getMsNo());
			addTestScheme.setName(testSuit.getName());
			addTestScheme.setTimeCondition(testSuit.getTimeCondition());
			addTestScheme.initTimes(testSuit.getTimes());
			addTestScheme.setExecutiveDate(testSuit.getExecutiveDate());
			addTestScheme.setModeLock(testSuit.getModeLock());
			addTestScheme.setRunOrder(testSuit.getRunOrder());
			if (null == testSuit.getRunOrder()) {
				addTestScheme.setRunOrder(testSchemes.size() + 1);
			}
			addTestScheme.getCommandList().setRepeat(
					testSuit.getCommandList().getRepeat());
			addTestScheme
					.getCommandList()
					.getSynchronize()
					.setType(
							testSuit.getCommandList().getSynchronize()
									.getType());
			testSchemes.add(addTestScheme);
		}
	}

	/**
	 * 初始化时间段
	 * 
	 * @param beginTimes
	 * @param endTimes
	 * @param testScheme
	 */
	private void initTimes(String[] beginTimes, String[] endTimes,
			TestScheme testScheme) {
		if (beginTimes == null) {
			testScheme.initTimes(null);
		} else {
			List<Time> times = new ArrayList<Time>();
			for (int i = 0; i < beginTimes.length; i++) {
				Time time = new Time();
				time.setBeginTime(beginTimes[i]);
				time.setEndTime(endTimes[i]);
				times.add(time);
			}
			testScheme.initTimes(times);
		}
	}

	/**
	 * 初始化日期端
	 * 
	 * @param beginDates
	 * @param endDates
	 * @param testScheme
	 */
	private void initDates(String[] beginDates, String[] endDates,
			TestScheme testScheme) {
		if (beginDates == null) {
			testScheme.setExecutiveDate("");
		} else {
			List<DateBean> dateBeans = new ArrayList<DateBean>();
			for (int i = 0; i < beginDates.length; i++) {
				DateBean dateBean = new DateBean();
				dateBean.setStartDate(beginDates[i]);
				dateBean.setEndDate(endDates[i]);
				dateBeans.add(dateBean);
			}
			testScheme.setExecutiveDate(DateBeanUtils.parse(dateBeans));
			testScheme.setDateBeans(dateBeans);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.action.page.PageAction#doPageQuery(com.datang.common
	 * .action.page.PageList)
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// 获取当前测试计划的所有testScheme
		HttpSession session = ServletActionContext.getRequest().getSession();
		TestPlan testPlan = (TestPlan) session.getAttribute("testPlan");
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		if (testSchemes == null || testSchemes.size()==0) {
			testSchemes = new ArrayList<TestScheme>();
			testPlan.setTestSuit(testSchemes);
		}
		Collections.sort(testSchemes, new Comparator<TestScheme>() {
			@Override
			public int compare(TestScheme o1, TestScheme o2) {
				if (null == o1.getRunOrder() && null == o2.getRunOrder()) {
					return o1.getName().compareTo(o2.getName());
				} else if (null == o1.getRunOrder() && null != o2.getRunOrder()) {
					return 1;
				} else if (null == o2.getRunOrder() && null != o1.getRunOrder()) {
					return -1;
				} else if (o1.getRunOrder() == o2.getRunOrder()) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return o1.getRunOrder().compareTo(o2.getRunOrder());
				}
			}
		});
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setTotal(testSchemes.size() + "");
		easyuiPageList.setRows(testSchemes);
		return easyuiPageList;
	}

	/**
	 * 新建指令
	 * 
	 * @return
	 */
	public String newCommand() {
		Command command = getDefaultCommandById(commandId);// 根据commandID调用下面DefaultCommandById方法,返回一个测试指令
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		valueStack.set("command", command);
		List<HttpCommandURL> queryCanAddRandomURL = httpCommandURLService
				.queryCanAddRandomURL(null);
		for (HttpCommandURL httpCommandURL : queryCanAddRandomURL) {
			httpCommandURL.setSelected(true);
		}
		valueStack.set("urlRandomList", queryCanAddRandomURL);
		List<HttpCommandURL> queryCanAddMustURL = httpCommandURLService
				.queryCanAddMustURL(null);
		for (HttpCommandURL httpCommandURL : queryCanAddMustURL) {
			httpCommandURL.setSelected(true);
		}
		valueStack.set("urlMustList", queryCanAddMustURL);
		ServletActionContext.getRequest().getSession()
				.removeAttribute("editCommand");
		return "commandContent";
	}

	/**
	 * 根据id填充默认值
	 * 
	 * @param commandId
	 * @return
	 */
	private Command getDefaultCommandById(String commandId) {
		Command command = new Command();
		command.setId(commandId);
		
		if("0x8501".equals(commandId)){
			command.setName("微信实时通信被叫测试命令");
			command.setTestMOS("1");
			command.setCallModel("0");
			command.setMosLimit("3");
		}else 
		if("0x8500".equals(commandId)){
			command.setName("微信实时通信主叫");
			command.setCallName("aaaa");
			command.setRandomCall("0");
			command.setDuration("180");
			command.setRepeat("999");
			command.setInterval("100");
			command.setMaxTime("10");
			command.setCallModel("0");
			command.setTestMOS("进行MOS测试");
			command.setCallMOSServer("0");
			command.setTimeOut("180");
		}else 
		//并发业务
		if("0x0b0c".equals(commandId)){
			
			command.setRepeat("999");
			command.setInterval("100");
			command.setDuration("180");
			command.setMaxTime("10");
			command.setRandomCall("0");
			command.setTestMOS("1");
			command.setCallMOSServer("0");
			command.setMosLimit("2.0");
			command.setTimeOut("180");
			command.setApn("cmnet");
			command.setRemoteHost("211.136.93.245");
			command.setPort("21");
			command.setAccount("test");
			command.setPassword("abcd");
			command.setFileSize("1024");
			command.setBinary("二进制模式");
			command.setDownload("FTP文件下载");
			command.setPassive("主动模式");
			command.setRemoteFile("/2M.rar");
			command.setThreadNum("1");
			command.setMaxDialNum("3");
			command.setMaxFTPland("3");

			command.setName("并发业务");
		}else 
		// GSM/TD/WCDMA/LTE语音业务主叫
		if ("0x0500".equals(commandId)) {
			command.setInterval("20");
			command.setDuration("180");
			command.setMaxTime("10");
			command.setMosLimit("2.0");
			command.setTestMOS("1");
		} else
		// "GSM/TD/WCDMA语音业务被叫"
		if ("0x0501".equals(commandId)) {
			command.setTestMOS("1");
			command.setMosLimit("2.0");
		} else
		// "GSM/TD接收/发送短消息
		if ("0x0609".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("1");
			command.setDestination("13901234567");
			command.setServerCenterAddress("13800210500");
		} else
		// GPRS发送彩信
		if ("0x060A".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("10");
			command.setMediaFileSize("10");
			command.setDestination("13800138000");
			command.setSyncMSNO("0");
			command.setApn("2");
			command.setAgent("aa");
			command.setAccount("admin");
			command.setPassword("admin");
			command.setGateway("202.101.224.68");
			command.setPort("23");
			command.setServerAddress("127.0.0.1");
		} else
		// GPRS接收彩信
		if ("0x060B".equals(commandId)) {
			command.setSyncMSNOs("0");
			command.setPtimeOut("5");
			command.setTimeOut("10");
			command.setAgent("aa");
			command.setAccount("admin");
			command.setPassword("admin");
			command.setGateway("202.101.224.68");
			command.setPort("23");
		} else
		// GPRS PDP Activation/Deactivation
		if ("0x0603".equals(commandId)) {
			command.setInterval("10");
			command.setApn("2");
			command.setKeeptime("60");
		} else
		// GPRS/CDMA Wap 图铃下载
		if ("0x0608".equals(commandId)) {
			command.setRepeat("5");
			command.setInterval("5");
			command.setTimeOut("60");
			command.setAgent("ZTC");
			command.setConnectionMode("10");
			command.setPort("80");
			command.setGateway("10.0.0.172");
		} else

		// GPRS/TD流媒体测试
		/**
		 * 删除GPRS/TD流媒体测试" 新增“移动互联网应用层业务-流媒体”
		 * 
		 * 新增2个参数 :缓冲区总时长（单位 秒)BufferLength 缺省60, 缓冲播放门限（单位
		 * 秒）BufferPlayThreshold 缺省5
		 * 
		 * 
		 * 
		 * 名称的默认值改成“流媒体测试”
		 * 
		 * @author yinzhipeng
		 */
		if ("0x0611".equals(commandId)) {
			command.setInterval("100");
			command.setApn("2");
			command.setVersion("16");
			command.setUrl("www.lmt.com");
			command.setUsername("admin");
			command.setPassword("admin");
			command.setPreBufferLength("5000");
			command.setRebufferLength("5000");
			command.setPlayTime("150");
			command.setLocalRTPport("5004");
			command.setRtspHttpPort("0");

			command.setName("流媒体测试");
			command.setBufferLength("60");
			command.setBufferPlayThreshold("5");
		} else
		// GPRS/CDMA/TD HTTP下载

		// 移动互联网应用层业务-HTTP
		/**
		 * 删除"GPRS/CDMA/TD HTTP下载" 新增“移动互联网应用层业务-HTTP”
		 * 
		 * 新增7个参数 :业务类型Mode 缺省"登陆",下载文件大小（byte)ulFileSize 缺省25,代理端口ProxyPort
		 * 缺省6089, 必选浏览地址 UrlMust, 随机浏览地址 UrlRandomNum, 浏览时间间隔UrlInterval 缺省2,
		 * 每次随机地址个数UrlRandomNum 缺省4
		 * 
		 * 
		 * 
		 * 名称的默认值改成“HTTP业务”
		 * 
		 * @author yinzhipeng
		 */
		if ("0x060F".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("30");
			command.setApn("2");
			command.setPort("5566");
			command.setUrl("www.http.com");
			command.setAddress("10.109.16.250");

			command.setModel("0");// 登录
			command.setDlFileSize("25");
			command.setProxyPort("6089");
			command.setUrlInterval("2");
			command.setUrlRandomNum("4");

		} else
		// GSM/TD飞信登陆
		if ("0x060D".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("5");
			command.setTimeOut("60");
			command.setApn("2");
			command.setAgent("Java1.0");
			// TODO 模拟手机类型
		} else
		// GSM/TD飞信手机(短信)测试
		if ("0x060E".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("5");
			command.setTimeOut("60");
			command.setFetionNumber("55439635");
			command.setPassword("123456");
			command.setSdestination("13800000000");
			command.setApn("2");
			command.setServerCenterAddress("13800100500");
			command.setFdestination("10086");
			command.setReport("1");
		} else
		// GSM /CDMA/TD/WCDMA IDLE等待
		if ("0x0502".equals(commandId)) {
			command.setWaitTimes("60");
		} else
		// GPRS/CDMA/TD接收POP3邮件
		if ("0x0612".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("60");
			command.setApn("2");
			command.setMailServer("10.10.12.39");
			command.setPort("110");
			command.setPassword("1234");
			command.setUsername("admin");
			command.setPath("E:\\workspace");
		} else
		// GPRS/CDMA/TD发送SMTP邮件
		if ("0x0613".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("100");
			command.setApn("2");
			command.setSender("test");
			command.setFrom("test@sohu.com");
			command.setTo("dest@sohu.com");
			command.setFileSize("100");
			command.setSubject("测试");
			command.setBody("hello test");
			command.setAccount("admin");
			command.setPassword("admin");
			command.setPort("8080");
			// TODO缺少字段？
		} else
		/**
		 * 新增“0x0620 UDP测试”
		 * 
		 * @author yinzhipeng
		 */
		if ("0x0620".equals(commandId)) {
			command.setRepeat("999");
			command.setInterval("100");
			command.setTransferTime("600");
			command.setDropTime("300");
			command.setApn("1");
			command.setRemoteHost("211.136.93.245");
			command.setPort("21");
			command.setPassive("1");
			command.setBufferSize("512");
			command.setPacketSize("1400");
			command.setBandWidth("1");
		} else
		// CDMA语音被叫
		if ("0x0801".equals(commandId)) {
			command.setTestMOS("1");
			command.setMosLimit("2.0");
		} else
		// CDMA语音主叫
		if ("0x0800".equals(commandId)) {
			command.setCallNumber("13800000000");
			command.setDuration("180");
			command.setMaxTime("300");
			command.setMosLimit("2.0");
		} else
		// CDMA发送短消息
		if ("0x0802".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("100");
			command.setDestMSNO("0");
			command.setContent("cdma短信测试");
			command.setDestination("13900000000");
		} else
		// CDMA接收短消息
		if ("0x0803".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("10");
			command.setDestMSNO("0");
			command.setContent("cdma短信测试");
			command.setDestination("13900000000");
			command.setSynchronize("1");

		} else
		// GPRSAttach
		if ("0x0602".equals(commandId)) {
			command.setInterval("10");
			command.setTimeOut("60");
			command.setKeeptime("15");
			command.setWaitTime("10");
		} else
		// GPRS/CDMA/TD PING
		if ("0x0604".equals(commandId)) {
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("1000");
			command.setIp("192.168.1.13");
		} else
		// GPRS KJava
		if ("0x0605".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("100");
			command.setApn("2");
			command.setUrl("/doc");
			command.setAgent("nokia320");
			command.setPort("9201");
			command.setGateway("10.0.0.172");
		} else
		// GPRS/CDMA Wap 登录
		if ("0x0606".equals(commandId)) {
			command.setRepeat("5");
			command.setInterval("5");
			command.setConnectionMode("10");
			command.setPort("80");
			command.setTimeOut("60");
			command.setUrl("www.monternet.com");
			command.setAgent("nokia320");
			command.setGateway("10.0.0.172");
		} else
		// GPRS/CDMA Wap 页面刷新
		if ("0x0607".equals(commandId)) {
			command.setRepeat("5");
			command.setInterval("5");
			command.setConnectionMode("10");
			command.setPort("80");
			command.setDeep("3");
			command.setUrl("www.monternet.com");
			command.setGateway("10.0.0.172");
			command.setBlockLink("www.cnn.com");
			command.setAgent("nokia320");
			command.setTimeOut("60");
		} else

		// GPRS/CDMA/TD/WCDMA/EVDO FTP上传/下载
		/**
		 * 删除"GPRS/CDMA/TD/WCDMA/EVDO/LTE FTP上传/下载" 新增“移动互联网应用层业务-FTP上传/下载”
		 * 
		 * 新增3个参数 :线程个数ThreadNum 缺省1,单次业务最多可拨号次数MaxDialNum 缺省3 ,单次业务最多FTP登陆次数
		 * MaxFTPland 缺省3
		 * 
		 * 
		 * 名称的默认值改成“FTP上传/下载(不含WLAN)”
		 * 
		 * @author yinzhipeng
		 */
		if ("0x060C".equals(commandId)) {

			command.setInterval("5");
			command.setTimeOut("180");
			command.setApn("2");
			command.setRemoteHost("211.136.93.245");
			command.setPort("21");
			command.setUsername("egprs");
			command.setPassword("egprs123ftp");
			command.setFileSize("1024");
			command.setRemoteFile("/500K.rar");

			command.setName("FTP上传/下载(不含WLAN)");
			command.setThreadNum("1");
			command.setMaxDialNum("3");
			command.setMaxFTPland("3");
		}
		// WLAN AP关联测试
		if ("0x0901".equals(commandId)) {
			command.setRepeat("999");
			command.setInterval("5");
			command.setTimeOut("30");
			command.setHoldtime("15");
		}
		// WLAN WEB用户认证测试
		if ("0x0902".equals(commandId)) {
			command.setRepeat("999");
			command.setInterval("5");
			command.setTimeOut("30");
		}
		// WLAN Http网站访问测试
		if ("0x0903".equals(commandId)) {
			command.setRepeat("999");
			command.setInterval("5");
			command.setTimeOut("30");
		}
		// WLAN Http网站访问测试
		if ("0x0904".equals(commandId)) {
			command.setRepeat("999");
			command.setInterval("5");
			command.setTimeOut("30");
			command.setRemoteHost("211.136.93.245");
			command.setPort("21");
			command.setFtpAccount("egprs");
			command.setFtpPassword("egprs123ftp");
			command.setFileSize("1024");
			command.setRemoteFile("/wlan.rar");
		}
		// WLAN Ping
		if ("0x0905".equals(commandId)) {
			command.setRepeat("999");
			command.setInterval("10");
			command.setTimeOut("20");
		}
		// qq业务
		if ("0x0912".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("tcli.3g.qq.com");
		} else
		// 飞信业务
		if ("0x0913".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("mnav.fetion.com.cn");
		} else
		// 腾讯微博
		if ("0x0914".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("t3.qpic.cn");
		} else
		// 腾讯微信
		if ("0x0915".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("mmsns.qpic.cn");
		} else
		// 移动微博
		if ("0x0916".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("s.weibo.10086.cn");
		} else
		// uc浏览器
		if ("0x0917".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("ucus.ucweb.com");
		} else
		// 手机报
		if ("0x0918".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("http://wap.monternet.com/");
		} else
		// 音乐下载(wap方式)
		if ("0x0919".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("192.168.1.13");
		} else
		// 音乐随身听
		if ("0x0920".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("");
		} else
		// 飞信
		if ("0x0921".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("");
		} else
		// 手机游戏
		if ("0x0922".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("download.cmgame.com");
		} else
		// 手机阅读
		if ("0x0923".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("client.cmread.com");
		} else
		// 手机证券
		if ("0x0924".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("wap.shzq.com");
		} else
		// 手机视频
		if ("0x0925".equals(commandId)) {
			command.setRepeat("10");
			command.setInterval("100");
			command.setTimeOut("200");
			command.setApn("2");
			command.setPackagesize("");
		}
		return command;
	}

	/**
	 * 删除测试指令
	 * 
	 * @return
	 */
	public String deleteCommand() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Object editTestSuit = session.getAttribute("editTestSuit");
		TestScheme testScheme = null;// 测试任务
		boolean editSuit = false;
		if (editTestSuit != null && !"".equals(editTestSuit)) {
			editSuit = true;
		}
		if (editSuit) {
			testScheme = (TestScheme) session.getAttribute("testSuit");// 修改
		} else {
			testScheme = (TestScheme) session.getAttribute("addTestScheme");// 新增
		}
		List<Command> tmpCommands = new ArrayList<Command>();// 定义一个指令泛型集合
		List<Command> commands = testScheme.getCommandList().getSynchronize()// 串行还是并行
				.getCommands();// 得到一个命令列表集合
		tmpCommands.addAll(commands);// 把这个命令列表全部添加到另一个集合中
		for (String name : commandNames) {// 遍历页面上传过来的name数组
			tmpCommands.remove(testScheme.findCommand(name));// 根据页面的id从testScheme中查找,把找到的对象从tmpCommands集合中删除,进行循环遍历
		}
		testScheme.getCommandList().getSynchronize().setCommands(tmpCommands);// 命令列表(是个集合,一对多的关系)
		return ReturnType.JSON;
	}

	/**
	 * 获得测试指令内容
	 * 
	 * @return
	 */
	public String getCommandContent() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Object editTestSuit = session.getAttribute("editTestSuit");
		TestScheme testScheme = null;
		boolean editSuit = false;
		if (editTestSuit != null && !"".equals(editTestSuit)) {
			editSuit = true;
		}
		if (editSuit) {
			testScheme = (TestScheme) session.getAttribute("testSuit");
		} else {
			testScheme = (TestScheme) session.getAttribute("addTestScheme");
		}
		Command command = testScheme.findCommand(commandName);
		String urlRandom = command.getUrlRandom();
		if (urlRandom != null && !urlRandom.trim().equals("")) {
			List<HttpCommandURL> queryCanAddRandomURL = httpCommandURLService
					.queryCanAddRandomURL(null);
			for (HttpCommandURL httpCommandURL : queryCanAddRandomURL) {
				if (urlRandom.contains(httpCommandURL.getUrl().trim())) {
					httpCommandURL.setSelected(true);
				}
			}
			ActionContext.getContext().getValueStack()
					.set("urlRandomList", queryCanAddRandomURL);
		}
		String urlMust = command.getUrlMust();
		if (urlMust != null && !urlMust.trim().equals("")) {
			List<HttpCommandURL> queryCanAddMustURL = httpCommandURLService
					.queryCanAddMustURL(null);
			for (HttpCommandURL httpCommandURL : queryCanAddMustURL) {
				if (urlMust.contains(httpCommandURL.getUrl().trim())) {
					httpCommandURL.setSelected(true);
				}
			}
			ActionContext.getContext().getValueStack()
					.set("urlMustList", queryCanAddMustURL);
		}
		ActionContext.getContext().getValueStack().set("command", command);
		session.setAttribute("editCommand", command);
		return "commandContent";
	}

	/**
	 * @return the beginDatebeginDate
	 */
	public String[] getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(String[] beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDateendDate
	 */
	public String[] getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String[] endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the beginTimebeginTime
	 */
	public String[] getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(String[] beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTimeendTime
	 */
	public String[] getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String[] endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the testSuittestSuit
	 */
	public TestScheme getTestSuit() {
		return testSuit;
	}

	/**
	 * @param testSuit
	 *            the testSuit to set
	 */
	public void setTestSuit(TestScheme testSuit) {
		this.testSuit = testSuit;
	}

	/**
	 * @return the commandIdcommandId
	 */
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
	 * @return the commandNamescommandNames
	 */
	public String[] getCommandNames() {
		return commandNames;
	}

	/**
	 * @param commandNames
	 *            the commandNames to set
	 */
	public void setCommandNames(String[] commandNames) {
		this.commandNames = commandNames;
	}

	/**
	 * @return the commandNamecommandName
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

	
}
