package com.datang.web.action.testManage.terminal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.util.CollectionUtils;
import com.datang.common.util.StringUtils;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TestModule;
import com.datang.service.testManage.terminal.TestModuleService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 终端中包含测试模块的Acttion
 * 
 * @author yinzhipeng
 * @date:2016年7月12日 上午9:35:20
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TestModuleAction implements ModelDriven<TestModule> {
	/**
	 * 表单对象
	 */
	private TestModule testModule = new TestModule();
	private int[] channelsNos;
	private String testModuleNo;

	@Autowired
	private TestModuleService testModuleService;

	/**
	 * 删除
	 * 
	 * @return
	 */
	public String deleteTestModule() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Terminal terminal = (Terminal) session.getAttribute("terminal");
		List<TestModule> testModules = terminal.getTestModuls();
		List<TestModule> tmpTestModules = new LinkedList<TestModule>();
		if (!CollectionUtils.isEmpty(testModules)) {
			tmpTestModules.addAll(testModules);
			if (null != channelsNos) {
				for (int channelsNo : channelsNos) {
					for (TestModule testModule : testModules) {
						if (testModule.getChannelsNo() == channelsNo) {
							tmpTestModules.remove(testModule);
						}
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(tmpTestModules)) {
			Collections.sort(tmpTestModules);
		}
		terminal.setTestModuls(tmpTestModules);

		return ReturnType.JSON;
	}

	/**
	 * 更新或保存终端模块
	 * 
	 * @return
	 */
	public String saveOrUpdate() {
		HttpSession session = ServletActionContext.getRequest().getSession();

		// session
		TestModule sessionTestModule = (TestModule) session
				.getAttribute("testModule");

		Object obj = session.getAttribute("terminal");
		Terminal terminal = (Terminal) obj;
		List<TestModule> testModules = terminal.getTestModuls();
		if (testModules == null) {
			testModules = new LinkedList<TestModule>();
			terminal.setTestModuls(testModules);
		}
		// 不再校验sim卡号
		// if (checkSimCard(terminal, testModule)) {
		// ActionContext.getContext().getValueStack()
		// .set("errorMsg", "Sim卡号重复");
		// return ReturnType.JSON;
		// }
		TestModule oldTestModule = null;
		if (testModule != null) {
			for (TestModule module : testModules) {
				if (module != null
						&& testModule.getChannelsNo() == module.getChannelsNo()) {// 模块的重复
					oldTestModule = module;
					break;
				}
			}
		}
		if (oldTestModule == null) {
			testModules.add(testModule);
			if (!CollectionUtils.isEmpty(testModules)) {
				Collections.sort(testModules);// 升序排列
			}
			terminal.setTestModuls(testModules);
		} else {
			testModule.setChannelsNo(sessionTestModule.getChannelsNo());
			session.setAttribute("testModule", testModule);
			if (sessionTestModule.getChannelsNo() != oldTestModule
					.getChannelsNo()) {
				ActionContext.getContext().getValueStack()
						.set("errorMsg", "终端模块重复");
				return ReturnType.JSON;
			}
			oldTestModule.setBaudRate(testModule.getBaudRate());
			oldTestModule.setChannelsNo(testModule.getChannelsNo());
			oldTestModule.setClazz(testModule.getClazz());
			oldTestModule.setCmoschipType(testModule.getCmoschipType());
			oldTestModule.setConnectFailBarrier(testModule
					.getConnectFailBarrier());
			oldTestModule.setEnable(testModule.isEnable());
			oldTestModule.setIndependency(testModule.isIndependency());
			oldTestModule.setLoginFailBarrier(testModule.getLoginFailBarrier());
			oldTestModule.setModuleType(testModule.getModuleType());
			oldTestModule.setMsgAlarmNo(testModule.getMsgAlarmNo());
			oldTestModule.setMsgCenterNo(testModule.getMsgCenterNo());
			oldTestModule.setOperator(testModule.getOperator());
			oldTestModule.setPhase(testModule.getPhase());
			oldTestModule.setPptDialFailBarrier(testModule
					.getPptDialFailBarrier());
			oldTestModule.setSimCard(testModule.getSimCard());
			oldTestModule.setTimeout(testModule.getTimeout());
			session.removeAttribute("testModule");
			session.setAttribute("testModule", oldTestModule);
		}
		session.setAttribute("terminal", terminal);
		session.setAttribute("editTerminal", terminal.getId() == null);
		session.setAttribute("terminal", terminal);
		return ReturnType.JSON;
	}

	/**
	 * <code>true</code> Sim卡号重复
	 * 
	 * @param terminal
	 * @param testModule
	 * @return
	 */
	private boolean checkSimCard(Terminal terminal, TestModule testModule) {
		List<TestModule> modules = terminal.getTestModuls();
		if (!CollectionUtils.isEmpty(modules)) {
			for (TestModule module : modules) {
				if (testModule != null && module != null
						&& module.getSimCard().equals(testModule.getSimCard())
						&& testModule.getChannelsNo() != module.getChannelsNo()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取模块内容
	 * 
	 * @return
	 */
	public String getTestModuleContent() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		Terminal terminal = (Terminal) session.getAttribute("terminal");
		if (StringUtils.hasText(testModuleNo)) {
			Integer testModuleIntNo = Integer.parseInt(testModuleNo);
			for (TestModule testModule : terminal.getTestModuls()) {
				if (testModule.getChannelsNo() == testModuleIntNo.intValue()) {
					session.setAttribute("testModule", testModule);
					ActionContext.getContext().getValueStack().push(testModule);
					break;
				}
			}
		}
		return ReturnType.JSON;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public TestModule getModel() {
		return testModule;
	}

	/**
	 * @return the testModuletestModule
	 */
	public TestModule getTestModule() {
		return testModule;
	}

	/**
	 * @param testModule
	 *            the testModule to set
	 */
	public void setTestModule(TestModule testModule) {
		this.testModule = testModule;
	}

	/**
	 * @return the channelsNoschannelsNos
	 */
	public int[] getChannelsNos() {
		return channelsNos;
	}

	/**
	 * @param channelsNos
	 *            the channelsNos to set
	 */
	public void setChannelsNos(int[] channelsNos) {
		this.channelsNos = channelsNos;
	}

	/**
	 * @return the testModuleNotestModuleNo
	 */
	public String getTestModuleNo() {
		return testModuleNo;
	}

	/**
	 * @param testModuleNo
	 *            the testModuleNo to set
	 */
	public void setTestModuleNo(String testModuleNo) {
		this.testModuleNo = testModuleNo;
	}

}
