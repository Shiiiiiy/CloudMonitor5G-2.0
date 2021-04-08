package com.datang.web.action.testManage.testPlan;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import com.datang.domain.testPlan.TestScheme;
import com.datang.exception.ApplicationException;
import com.datang.service.testPlan.HttpCommandURLService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 测试命令操作Action
 * 
 * @author yinzhipeng
 * @date:2016年8月12日 下午5:13:51
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class CommandAction extends PageAction implements ModelDriven<Command> {
	private Command command = new Command();
	private String[] randomUrlIds;
	private String[] mustUrlIds;
	private boolean mustUrl;
	@Autowired
	private HttpCommandURLService httpCommandURLService;

	/**
	 * 保存测试命令
	 * 
	 * @return
	 */
	public String saveCommand() {
		// 存储随机浏览地址
		if (randomUrlIds != null) {
			StringBuffer stringBuffer = new StringBuffer();
			for (String randomUrlId : randomUrlIds) {
				HttpCommandURL queryHttpCommandURL = httpCommandURLService
						.queryHttpCommandURL(Integer.valueOf(randomUrlId));
				if (queryHttpCommandURL != null) {
					stringBuffer.append(queryHttpCommandURL.getUrl() + ",");
				}
			}
			if (stringBuffer.length() != 0) {
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			command.setUrlRandom(stringBuffer.toString());
		}
		// 存储必须浏览地址
		if (mustUrlIds != null) {
			StringBuffer stringBuffer = new StringBuffer();
			for (String mustUrlId : mustUrlIds) {
				HttpCommandURL queryHttpCommandURL = httpCommandURLService
						.queryHttpCommandURL(Integer.valueOf(mustUrlId));
				if (queryHttpCommandURL != null) {
					stringBuffer.append(queryHttpCommandURL.getUrl() + ",");
				}
			}
			if (stringBuffer.length() != 0) {
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			}
			command.setUrlMust(stringBuffer.toString());
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		Object editTestSuit = session.getAttribute("editTestSuit");
		Object editCommand = session.getAttribute("editCommand");
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
		Command oldCommand = testScheme.findCommand(command.getName());// 和删除命令调用同一个方法.拿到这个值,和集合里的值进行对比,得到这个值
		// 新增情况
		if (oldCommand != null && editCommand == null) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "该命令名称在该测试任务集下已存在");
			return ReturnType.JSON;
		}
		// 编辑情况
		if (oldCommand != null && editCommand != null
				&& editCommand != oldCommand) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", "该命令名称在该测试任务集下已存在");
			return ReturnType.JSON;
		}

		if (editCommand != null) {
			Command cmd = (Command) editCommand;
			Integer id = cmd.getCommandId();
			command.setCommandId(id);
			try {
				BeanUtils.copyProperties(cmd, command);
			} catch (IllegalAccessException e) {
				throw new ApplicationException("未知异常");
			} catch (InvocationTargetException e) {
				throw new ApplicationException("未知异常");
			}// 在不同层之间进行属性的拷贝.
		} else {
			if (null == command.getRunOrder()) {
				command.setRunOrder(testScheme.getCommandList()
						.getSynchronize().getCommands().size() + 1);
			}
			testScheme.addCommand(command);
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
		// 获取当前testScheme的所有command
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
		List<Command> commands = testScheme.getCommandList().getSynchronize()
				.getCommands();
		if (commands == null) {
			commands = new ArrayList<Command>();
		}
		Collections.sort(commands, new Comparator<Command>() {
			@Override
			public int compare(Command o1, Command o2) {
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
		easyuiPageList.setTotal(commands.size() + "");
		easyuiPageList.setRows(commands);
		return easyuiPageList;
	}

	/**
	 * @return the randomUrlIdsrandomUrlIds
	 */
	public String[] getRandomUrlIds() {
		return randomUrlIds;
	}

	/**
	 * @param randomUrlIds
	 *            the randomUrlIds to set
	 */
	public void setRandomUrlIds(String[] randomUrlIds) {
		this.randomUrlIds = randomUrlIds;
	}

	/**
	 * @return the mustUrlIdsmustUrlIds
	 */
	public String[] getMustUrlIds() {
		return mustUrlIds;
	}

	/**
	 * @param mustUrlIds
	 *            the mustUrlIds to set
	 */
	public void setMustUrlIds(String[] mustUrlIds) {
		this.mustUrlIds = mustUrlIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public Command getModel() {
		return command;
	}

	/**
	 * @return the commandcommand
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * @return the mustUrlmustUrl
	 */
	public boolean isMustUrl() {
		return mustUrl;
	}

	/**
	 * @param mustUrl
	 *            the mustUrl to set
	 */
	public void setMustUrl(boolean mustUrl) {
		this.mustUrl = mustUrl;
	}

}
