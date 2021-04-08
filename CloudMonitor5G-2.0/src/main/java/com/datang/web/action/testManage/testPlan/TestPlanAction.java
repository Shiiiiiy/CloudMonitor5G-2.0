package com.datang.web.action.testManage.testPlan;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.apache.commons.jexl2.Main;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testPlan.AutoTestUnit;
import com.datang.domain.testPlan.Command;
import com.datang.domain.testPlan.GeneralItem;
import com.datang.domain.testPlan.NetWork;
import com.datang.domain.testPlan.SwitchLog;
import com.datang.domain.testPlan.TestPlan;
import com.datang.domain.testPlan.TestScheme;
import com.datang.exception.ApplicationException;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.service.testPlan.TestPlanService;
import com.datang.util.DateBeanUtils;
import com.datang.util.XStreamUtil;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 测试计划操作action
 * 
 * @author yinzhipeng
 * @date:2016年7月20日 上午9:20:29
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TestPlanAction extends PageAction implements ModelDriven<TestPlan> {
	/**
	 * 设备服务
	 */
	@Autowired
	private TerminalService terminalService;

	/**
	 * 测试计划服务
	 */
	@Autowired
	private TestPlanService testPlanService;
	/**
	 * 测试计划默认IP和端口
	 */
	@Value("${testPlan.server.default.ip}")
	private String defaultIp;
	@Value("${testPlan.server.port}")
	private String defaultPort;
	
	private String tIds;

	/**
	 * 表单对象
	 */
	private TestPlan testPlan = new TestPlan();
	/**
	 * 测试计划名称
	 */
	private String testPlanName;
	/**
	 * 测试计划版本号
	 */
	private Long testPlanVersion;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	/**
	 * 开始时间
	 */
	private Date endTime;
	
	/**
	 * 测试计划id字符串按","分隔
	 */
	private String testPlanIds;

	/**
	 * 测试任务名称数组
	 */
	private String[] testSchemeNames;
	/**
	 * 测试任务名称
	 */
	private String testSuitName;
	
	/**
	 * 树节点类型
	 */
	private String nodetype;

	/**
	 * 跳转到测试计划页面
	 * 
	 * @return
	 */
	public String testPlanUI() {
		return "testPlan";
	}

	/**
	 * 跳转到测试计划list页面
	 * 
	 * @return
	 */
	public String testPlanListUI() {
		if(tIds != null){
			String[] split = tIds.split(",");
			if(split.length == 1){
				Terminal terminal = terminalService.getTerminal(Long.valueOf(split[0]));
				//ActionContext.getContext().getValueStack().push(terminal);
				ActionContext.getContext().getValueStack().set("id", terminal.getId());
				ActionContext.getContext().getValueStack().set("name", terminal.getName());
			}
			ActionContext.getContext().getValueStack().set("tIds", tIds);
		}else if(null != testPlan.getTerminalId()) {
			Terminal terminal = terminalService.getTerminal(testPlan
					.getTerminalId());
			ActionContext.getContext().getValueStack().set("id", terminal.getId());
			ActionContext.getContext().getValueStack().set("name", terminal.getName());
			ActionContext.getContext().getValueStack().set("tIds", testPlan.getTerminalId());
		}
		if(nodetype != null && nodetype.equals("ter")){
			ActionContext.getContext().getValueStack().set("nodetype", "none");
		}else if(nodetype != null && nodetype.equals("city")){
			ActionContext.getContext().getValueStack().set("nodetype", "contents");
		}
		
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到添加测试计划界面
	 * 
	 * @return
	 */
	public String newTestPlanUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		if (null != testPlan.getTerminalId()) {
			Terminal terminal = terminalService.getTerminal(testPlan
					.getTerminalId());
			valueStack.push(terminal);
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute("testPlan");
		TestPlan testPlan = new TestPlan();
		testPlan.setTerminalId(this.testPlan.getTerminalId());
		testPlan.getAutoTestUnit().getNetWork().setPortalIP(defaultIp);
		testPlan.getAutoTestUnit().getNetWork().setPortalPort(defaultPort);
		session.setAttribute("testPlan", testPlan);
		session.removeAttribute("showTestPlan");
		valueStack.set("testPlan", testPlan);
		return ReturnType.ADD;
	}

	/**
	 * 获取测试计划内容
	 * 
	 * @return
	 */
	public String getTestplanContent() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute("testPlan");
		TestPlan testPlanDB = testPlanService.getTestPlan(testPlan.getId());
		if (null != testPlanDB.getTerminalId()) {
			Terminal terminal = terminalService.getTerminal(testPlanDB
					.getTerminalId());
			valueStack.push(terminal);
		}
		session.setAttribute("testPlan", testPlanDB);
		session.removeAttribute("showTestPlan");
		valueStack.set("testPlan", testPlanDB);
		return ReturnType.ADD;
	}

	/**
	 * 更新或保存测试计划
	 * 
	 * @return
	 */
	public String saveOrUpdate() {
		try {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			TestPlan sessionTestPlan = (TestPlan) session
					.getAttribute("testPlan");
			setProperties(sessionTestPlan, testPlan);
			if (null == sessionTestPlan.getId()) {
				String level = sessionTestPlan.getLevel();
				Long version = testPlanService
						.queryTestPlanLastVersion(sessionTestPlan
								.getTerminalId());
				version = (null == version ? 0l : version + 1);
				sessionTestPlan.getAutoTestUnit().setVersion(version);
				if (level != null) {
					if (level.equals("organizationCheck")) {
						sessionTestPlan.setName("组织巡检-"
								+ sessionTestPlan.getName() + "-" + version);
					} else if (level.equals("dailyOptimiz")) {
						sessionTestPlan.setName("日常优化-"
								+ sessionTestPlan.getName() + "-" + version);
					} else if (level.equals("deviceDebug")) {
						sessionTestPlan.setName("设备调试-"
								+ sessionTestPlan.getName() + "-" + version);
					}
				}
			}
			testPlanService.saveOrUpadate(sessionTestPlan);
			session.removeAttribute("testPlan");
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 设置参数
	 * 
	 * @param testPlan
	 *            session中对应的测试计划
	 * @param newTestPlan
	 *            和页面对应的测试计划页面
	 */
	private void setProperties(TestPlan testPlan, TestPlan newTestPlan) {

		testPlan.setName(newTestPlan.getName());
		testPlan.setCreateDate(newTestPlan.getCreateDate());
		testPlan.setDescription(newTestPlan.getDescription());
		testPlan.setLevel(newTestPlan.getLevel());
		testPlan.setSended(newTestPlan.isSended());
		testPlan.setPlanSendDate(newTestPlan.getPlanSendDate());
		testPlan.setLoseDate(newTestPlan.getLoseDate());

		NetWork netWork = testPlan.getAutoTestUnit().getNetWork();
		NetWork newNetWork = newTestPlan.getAutoTestUnit().getNetWork();
		netWork.setApn(newNetWork.getApn());
		netWork.setDialNumber(newNetWork.getDialNumber());
		netWork.setDialUpPassword(newNetWork.getDialUpPassword());
		netWork.setPassword(newNetWork.getPassword());
		netWork.setPortalIP(newNetWork.getPortalIP());
		netWork.setPortalPort(newNetWork.getPortalPort());
		netWork.setSendData(newNetWork.getSendData());
		netWork.setUseLAN(newNetWork.getUseLAN());
		netWork.setUser(newNetWork.getUser());

		SwitchLog switchLog = testPlan.getAutoTestUnit().getLogProcess()
				.getSwitchLog();
		SwitchLog newSwitchLog = newTestPlan.getAutoTestUnit().getLogProcess()
				.getSwitchLog();
		switchLog.setCondition(newSwitchLog.getCondition());
		switchLog.setEnable(newSwitchLog.getEnable());
		switchLog.setTestTime(newSwitchLog.getTestTime());
		switchLog.setType(newSwitchLog.getType());
		switchLog.setPfileSize(newSwitchLog.getPfileSize());

		AutoTestUnit autoTestUnit = testPlan.getAutoTestUnit();
		GeneralItem newGeneralItem = newTestPlan.getAutoTestUnit()
				.getGeneralItem();
		GeneralItem generalItem = autoTestUnit.getGeneralItem();
		if (newGeneralItem.getTestTarget().equals("DT")) {
			generalItem.setTestTarget(newGeneralItem.getTestTarget());
			generalItem.setTestPoint(null);
			generalItem.setTestType(null);
		} else {
			generalItem.setTestPoint("1");
			generalItem.setTestType(testPlan.getLevel());
			autoTestUnit.setFpTestInfo(" ");
			generalItem.setTestTarget(newGeneralItem.getTestTarget());
			List<TestScheme> testSuit = testPlan.getTestSuit();
			for (TestScheme testScheme : testSuit) {
				testScheme.setEtype("0");
			}
		}
		generalItem.setGpsCondition(newGeneralItem.getGpsCondition());
		generalItem.setLeftTopLat(newGeneralItem.getLeftTopLat());
		generalItem.setLeftTopLon(newGeneralItem.getLeftTopLon());
		generalItem.setRightBottomLat(newGeneralItem.getRightBottomLat());
		generalItem.setRightBottomLon(newGeneralItem.getRightBottomLon());
		generalItem.setSpeedCondition(newGeneralItem.getSpeedCondition());
		generalItem.setMaxSpeed(newGeneralItem.getMaxSpeed());
		generalItem.setMaxSpeedTime(newGeneralItem.getMaxSpeedTime());
		generalItem.setMinSpeed(newGeneralItem.getMinSpeed());
		generalItem.setMinSpeedTime(newGeneralItem.getMinSpeedTime());

	}

	/**
	 * 删除测试计划
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delelteTestplan() {
		if (StringUtils.hasText(testPlanIds)) {
			String[] ids = testPlanIds.split(",");
			for (String idStr : ids) {
				try {
					Integer id = Integer.parseInt(idStr);
					this.testPlanService.deleteTestPlan(id);
				} catch (NumberFormatException e) {
				} catch (ApplicationException e) {
					ActionContext.getContext().getValueStack()
							.set("errorMsg", e.getMessage());
				}
			}
		}
		return ReturnType.JSON;
	}

	/**
	 * 导出测试计划
	 * 
	 * @return
	 */
	public String export() {
		if (null != testPlan.getId()) {
			TestPlan testPlanDB = this.testPlanService.getTestPlan(testPlan
					.getId());
			if (null != testPlanDB) {
				return "exportTestPlan";
			}
		}
		ServletOutputStream outputStream = null;
		try {
			String respString = "<!DOCTYPE html><html><head><title></title></head><body><h2>测试计划不存在,可能被别的用户删除!</h2><input type='button' value='返回' onclick='self.window.history.go(-1);' /></body></html>";
			outputStream = ServletActionContext.getResponse().getOutputStream();
			outputStream.write(respString.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 导出测试计划
	 * 
	 * @return
	 */
	public InputStream getExportTestPlan() {
		TestPlan testPlanDB = this.testPlanService
				.getTestPlan(testPlan.getId());
		testPlanDB.setTestUnit(testPlanDB.getTestUnit());
		String testplanStr = XStreamUtil.toXMLStr(testPlanDB);
		try {
			ActionContext.getContext().put(
					"fileName",
					new String((testPlanDB.getName() + ".xml").getBytes(),
							"ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(testplanStr.getBytes());
	}

	/**
	 * 另存为测试计划
	 * 
	 * @return
	 */
	public String saveAsTestplan() {
		if (null != testPlan.getId()) {
			TestPlan testPlanDB = this.testPlanService.getTestPlan(testPlan
					.getId());
			if (null != testPlanDB) {
				TestPlan newTestPlan = createTestPlan(testPlanDB);
				// 更改测试计划版本号和测试计划名称
				Long version = testPlanService
						.queryTestPlanLastVersion(testPlanDB.getTerminalId());
				version = (null == version ? 0l : version + 1);
				newTestPlan.getAutoTestUnit().setVersion(version);
				newTestPlan.setTerminalId(testPlanDB.getTerminalId());
				newTestPlan.setName(testPlanDB.getName().substring(0,
						testPlanDB.getName().lastIndexOf("-") + 1)
						+ version);
				try {
					ActionContext.getContext().getValueStack()
							.set("testPlanName", newTestPlan.getName());
					this.testPlanService.addTestPlan(newTestPlan);
				} catch (ApplicationException e) {
					ActionContext.getContext().getValueStack()
							.set("errorMsg", e.getMessage());
				}
			} else {
				ActionContext.getContext().getValueStack()
						.set("errorMsg", "测试计划不存在,可能被别的用户删除!");
			}
		} else {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "测试计划不存在,可能被别的用户删除!");
		}
		return ReturnType.JSON;
	}

	/**
	 * 另存为的时候新建测试计划
	 * 
	 * @param testPlan
	 *            原测试计划
	 * @return 另存为的测试计划
	 */
	private TestPlan createTestPlan(TestPlan testPlan) {
		testPlan.getTestUnit().setTestSuit(testPlan.getTestSuit());
		String xmlStr = XStreamUtil.toXMLStr(testPlan);
		TestPlan paln = null;
		try {
			paln = XStreamUtil.fromXmlStr(TestPlan.class, xmlStr, null);
		} catch (RuntimeException e) {
			throw e;
		}

		paln.setTestSuit(paln.getTestUnit().getTestSuit());

		/**
		 * 回填上传文件大小,xml解析出来filesize不知道存哪一个
		 */
		List<TestScheme> fTestSuit = testPlan.getTestSuit();
		List<TestScheme> tTestSuit = paln.getTestSuit();
		outter: for (TestScheme testScheme : fTestSuit) {
			String name = testScheme.getName();
			List<Command> fCommands = testScheme.getCommandList()
					.getSynchronize().getCommands();
			if (0 == fCommands.size()) {
				break outter;
			}
			inner: for (int i = 0; i < tTestSuit.size(); i++) {
				if (tTestSuit.get(i).getName().equals(name)) {
					List<Command> tCommands = tTestSuit.get(i).getCommandList()
							.getSynchronize().getCommands();
					inner1: for (int j = 0; j < tCommands.size(); j++) {
						String tCommandsName = tCommands.get(j).getName();
						inner2: for (int k = 0; k < fCommands.size(); k++) {
							if (fCommands.get(k).getName()
									.equals(tCommandsName)) {
								tCommands.get(j).setFileSize(
										fCommands.get(k).getFileSize());
								tCommands.get(j).setDlFileSize(
										fCommands.get(k).getDlFileSize());
								break inner2;
							}
						}
					}
					break inner;
				}
			}
		}

		paln.setLevel(testPlan.getLevel());
		paln.setSended(false);
		paln.setDescription(testPlan.getDescription());
		paln.setName(testPlan.getName());
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		List<TestScheme> schemes = paln.getTestSuit();
		if (testSchemes != null) {
			for (int i = 0; i < testSchemes.size(); i++) {
				if (schemes.get(i) == null) {
					continue;
				}
				schemes.get(i).setName(testSchemes.get(i).getName());

				if (testSchemes.get(i).getCommandList() != null
						&& testSchemes.get(i).getCommandList().getSynchronize() != null
						&& schemes.get(i).getCommandList() != null
						&& schemes.get(i).getCommandList().getSynchronize() != null) {
					List<Command> testCommands = testSchemes.get(i)
							.getCommandList().getSynchronize().getCommands();
					List<Command> commands = schemes.get(i).getCommandList()
							.getSynchronize().getCommands();
					if (commands != null) {
						for (int j = 0; j < commands.size(); j++) {
							if (commands.get(j) == null) {
								continue;
							}
							commands.get(j).setName(
									testCommands.get(j).getName());
							commands.get(j).setChannelNo(
									testCommands.get(j).getChannelNo());
							commands.get(j).setCallStyle(
									testCommands.get(j).getCallStyle());
							/**
							 * 回填上传文件大小,xml解析出来filesize不知道存哪一个
							 */
							commands.get(j).setFileSize(
									testCommands.get(j).getFileSize());
							commands.get(j).setDlFileSize(
									testCommands.get(j).getDlFileSize());
						}
					}

				}

			}
		}
		return paln;
	}
	private String names;
	
	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	/**
	 * 下发测试计划
	 * 
	 * @return
	 */
	public String sendTestplan() {
		if(StringUtils.hasText(tIds)){
			String[] ids = tIds.split(",");
			String[] name = names.split(",");
			String errorMsg = "";
			for (int i = 0;i < ids.length;i++) {
				TestPlan testPlanDB = this.testPlanService.getTestPlan(Integer.valueOf(ids[i]));
				if (null != testPlanDB) {
					try {
						testPlanService.sendTestPlan(testPlanDB.getId(),
								testPlanDB.getTerminalId());
					} catch (ApplicationException e) {
						if(errorMsg == ""){
							errorMsg = name[i] +":"+ e.getMessage();
						}else{
							errorMsg = errorMsg + "," + name[i] +":"+ e.getMessage();
						}
						ActionContext.getContext().getValueStack().set("errorMsg", errorMsg);
					}
				}
			}
		}else if (null != testPlan.getId()) {
			TestPlan testPlanDB = this.testPlanService.getTestPlan(testPlan
					.getId());
			if (null != testPlanDB) {
				try {
					testPlanService.sendTestPlan(testPlanDB.getId(),
							testPlanDB.getTerminalId());
				} catch (ApplicationException e) {
					ActionContext.getContext().getValueStack()
							.set("errorMsg", e.getMessage());
				}
			} else {
				ActionContext.getContext().getValueStack()
						.set("errorMsg", "测试计划不存在,可能被别的用户删除!");
			}
		} else {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "测试计划不存在,可能被别的用户删除!");
		}
		return ReturnType.JSON;
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
		List<Terminal> terminals = new ArrayList<Terminal>();
		if (StringUtils.hasText(tIds)) {
			//terminal = terminalService.getTerminal(testPlan.getTerminalId());
			String[] tidlist = tIds.split(",");
			Long[] idL = new Long[tidlist.length];
			for(int i = 0;i < tidlist.length;i++){
				idL[i] = Long.valueOf(tidlist[i]);
			}
			terminals = terminalService.getTerminals(idL);
		}
		Map<Long, String> terBoxIdMap = new HashMap<Long, String>();
		for (Terminal terminal : terminals) {
			terBoxIdMap.put(terminal.getId(), terminal.getBoxId());
		}
		pageList.putParam("terminalIds", tIds);
		pageList.putParam("name", testPlanName);
		pageList.putParam("version", testPlanVersion);
		pageList.putParam("startTime", startTime);
		pageList.putParam("endTime", endTime);
		pageList.putParam("terBoxIdMap", terBoxIdMap);
		return testPlanService.queryPageTestPlan(pageList);
	}

	/**
	 * 获取测试计划内容
	 * 
	 * @return
	 */
	public String showTestplanContent() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute("testPlan");
		TestPlan testPlanDB = testPlanService.getTestPlan(testPlan.getId());
		if (null != testPlanDB.getTerminalId()) {
			Terminal terminal = terminalService.getTerminal(testPlanDB
					.getTerminalId());
			valueStack.push(terminal);
		}
		session.setAttribute("testPlan", testPlanDB);
		session.setAttribute("showTestPlan", true);
		valueStack.set("testPlan", testPlanDB);
		return ReturnType.ADD;
	}

	/**
	 * 新建测试任务集
	 * 
	 * @return
	 */
	public String newTestScheme() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute("editTestSuit");
		session.removeAttribute("testSuit");
		session.removeAttribute("addTestScheme");
		TestScheme testScheme = new TestScheme();
		session.setAttribute("addTestScheme", testScheme);
		ActionContext.getContext().getValueStack().set("testSuit", testScheme);
		return ReturnType.JSON;
	}

	/**
	 * 删除测试计划中的测试任务集
	 * 
	 * @return
	 */
	public String deleteTestSuit() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		TestPlan testPlan = (TestPlan) session.getAttribute("testPlan");
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		List<TestScheme> tmpTestSchemes = new ArrayList<TestScheme>();
		tmpTestSchemes.addAll(testSchemes);
		if (null != testSchemeNames) {
			for (String name : testSchemeNames) {
				for (TestScheme testScheme : testSchemes) {
					if (name.equals(testScheme.getName())) {
						tmpTestSchemes.remove(testScheme);
					}
				}
			}
		}
		testPlan.setTestSuit(tmpTestSchemes);
		return ReturnType.JSON;
	}

	/**
	 * 获得测试集内容
	 * 
	 * @return
	 */
	public String getTestSuitContent() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		TestPlan testPlan = (TestPlan) session.getAttribute("testPlan");
		TestScheme testScheme = testPlan.findTestScheme(testSuitName);
		testScheme.setDateBeans(DateBeanUtils.parse(testScheme
				.getExecutiveDate()));
		session.setAttribute("testSuit", testScheme);
		session.setAttribute("editTestSuit", true);
		session.removeAttribute("addTestScheme");
		ActionContext.getContext().getValueStack().set("testSuit", testScheme);
		return ReturnType.JSON;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public TestPlan getModel() {
		return testPlan;
	}

	/**
	 * @return the testPlantestPlan
	 */
	public TestPlan getTestPlan() {
		return testPlan;
	}

	/**
	 * @param testPlan
	 *            the testPlan to set
	 */
	public void setTestPlan(TestPlan testPlan) {
		this.testPlan = testPlan;
	}

	/**
	 * @return the testPlanNametestPlanName
	 */
	public String getTestPlanName() {
		return testPlanName;
	}

	/**
	 * @param testPlanName
	 *            the testPlanName to set
	 */
	public void setTestPlanName(String testPlanName) {
		this.testPlanName = testPlanName;
	}

	/**
	 * @return the testPlanIdstestPlanIds
	 */
	public String getTestPlanIds() {
		return testPlanIds;
	}

	/**
	 * @param testPlanIds
	 *            the testPlanIds to set
	 */
	public void setTestPlanIds(String testPlanIds) {
		this.testPlanIds = testPlanIds;
	}

	/**
	 * @return the testPlanVersiontestPlanVersion
	 */
	public Long getTestPlanVersion() {
		return testPlanVersion;
	}

	/**
	 * @param testPlanVersion
	 *            the testPlanVersion to set
	 */
	public void setTestPlanVersion(Long testPlanVersion) {
		this.testPlanVersion = testPlanVersion;
	}

	/**
	 * @return the testSchemeNamestestSchemeNames
	 */
	public String[] getTestSchemeNames() {
		return testSchemeNames;
	}

	/**
	 * @param testSchemeNames
	 *            the testSchemeNames to set
	 */
	public void setTestSchemeNames(String[] testSchemeNames) {
		this.testSchemeNames = testSchemeNames;
	}

	/**
	 * @return the testSuitNametestSuitName
	 */
	public String getTestSuitName() {
		return testSuitName;
	}

	/**
	 * @param testSuitName
	 *            the testSuitName to set
	 */
	public void setTestSuitName(String testSuitName) {
		this.testSuitName = testSuitName;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String gettIds() {
		return tIds;
	}

	public void settIds(String tIds) {
		this.tIds = tIds;
	}

	public String getNodetype() {
		return nodetype;
	}

	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}
	
	


}