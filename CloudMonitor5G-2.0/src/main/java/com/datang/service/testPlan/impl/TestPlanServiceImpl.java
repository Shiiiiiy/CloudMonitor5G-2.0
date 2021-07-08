package com.datang.service.testPlan.impl;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.zeromq.ZMQ.Socket;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.testManage.terminal.TerminalDao;
import com.datang.dao.testPlan.TestPlanDao;
import com.datang.domain.testManage.terminal.ChannelType;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TestModule;
import com.datang.domain.testPlan.Command;
import com.datang.domain.testPlan.TestPlan;
import com.datang.domain.testPlan.TestScheme;
import com.datang.exception.ApplicationException;
import com.datang.service.testPlan.TestPlanService;
import com.datang.util.CommandMap;
import com.datang.util.XStreamUtil;
import com.datang.util.ZMQUtils;
import com.datang.web.beans.testPlan.TestPlanQuery;

/**
 * @author songzhigang
 * @version 1.0.0, 2010-12-27
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TestPlanServiceImpl implements TestPlanService {

	private static Logger logger = LoggerFactory
			.getLogger(TestPlanServiceImpl.class);
	@Value("${decode.signalling.ip}")
	private String decodeIp;
	@Value("${decode.signalling.port}")
	private String decodePort;
	@Value("${testPlan.saveFileLink}")
	private String testPlanFileLink;

	@Autowired
	private TestPlanDao testPlanDao;
	@Autowired
	private TerminalDao terminalDao;

	/**
	 * 添加测试计划
	 * 
	 * @param testPlan
	 *            TestPlan
	 * @see com.datang.adc.service.testPlan.TestPlanService#addTestPlan(com.datang.adc.domain.testPlan.TestPlan)
	 */
	@Override
	public void addTestPlan(TestPlan testPlan) {
		if (testPlan == null) {
			return;
		}
		Collection<TestPlan> testPlans = this.testPlanDao.findTestPlan(testPlan
				.getName());
		if (testPlans != null && !testPlans.isEmpty()) {
			throw new ApplicationException("同版本测试计划名称已经存在");
		}
		testPlan.setCreateDate(new Date());
		this.testPlanDao.create(testPlan);

	}

	/**
	 * 通过id查找测试计划
	 * 
	 * @param id
	 *            Integer
	 * @see com.datang.adc.service.testPlan.TestPlanService#getTestPlan(java.lang.Integer)
	 */
	@Override
	public TestPlan getTestPlan(Integer id) {
		return this.testPlanDao.find(id);

	}

	/**
	 * 更新测试计划
	 * 
	 * @param testPlan
	 *            TestPlan
	 * @see com.datang.adc.service.testPlan.TestPlanService#updateTestPlan(com.datang.adc.domain.testPlan.TestPlan)
	 */
	@Override
	public synchronized void updateTestPlan(TestPlan testPlan) {

		if (testPlan == null) {
			return;
		}
		Collection<TestPlan> testPlans = this.testPlanDao.findTestPlan(testPlan
				.getName());
		TestPlan oldTestPlan = this.testPlanDao.find(testPlan.getId());
		if (testPlans != null && !testPlans.isEmpty()
				&& !oldTestPlan.getName().equals(testPlan.getName())) {
			throw new ApplicationException("同版本测试计划名称已经存在");
		}
		if (testPlan.isSended()) {
			throw new ApplicationException("测试计划已下发过不能更新");
		} else {

		}
		this.testPlanDao.update(testPlan);

	}

	/**
	 * 通过计划名称查找计划
	 * 
	 * @param planName
	 *            String
	 * @return TestPlan对象
	 * @see com.datang.adc.service.testPlan.TestPlanService#queryTestPlanByName(java.lang.String)
	 */
	@Override
	public TestPlan getTestPlanByName(String planName) {
		return this.testPlanDao.getTestPlanByName(planName);
	}

	/**
	 * 
	 * @param version
	 * @return
	 * @see com.datang.adc.service.testPlan.TestPlanService#getTestPlanByVersion(java.lang.Integer)
	 */
	@Override
	public TestPlan getTestPlanByVersion(Integer version) {
		return this.testPlanDao.getTestPlanByVersion(version);
	}

	/**
	 * 根据查询条件查询测试计划
	 * 
	 * @param testPlanQuery
	 *            TestPlanQuery
	 * @return 查询到的结果
	 * @see com.datang.adc.service.testPlan.TestPlanService#queryTestPlan(com.datang.adc.beans.testPlan.TestPlanQuery)
	 */
	@Override
	public Collection<TestPlan> queryTestPlan(TestPlanQuery testPlanQuery) {
		return this.testPlanDao.queryList(testPlanQuery);
	}

	/**
	 * @return the testPlanDao
	 */
	public TestPlanDao getTestPlanDao() {
		return testPlanDao;
	}

	/**
	 * @param testPlanDao
	 *            the testPlanDao to set
	 */
	public void setTestPlanDao(TestPlanDao testPlanDao) {
		this.testPlanDao = testPlanDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.testPlan.TestPlanService#deleteTestPlan(java.lang
	 * .Integer)
	 */
	@Override
	public void deleteTestPlan(Integer id) {
		TestPlan testPlan = this.testPlanDao.find(id);
		if (testPlan == null) {
			return;
		}
		if (testPlan.isSended()) {
			throw new ApplicationException("测试计划已经下发过不能删除");
		} else {
			this.testPlanDao.delete(testPlan);
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.testPlan.TestPlanService#sendTestPlan(java.lang.Integer,
	 *      java.util.Collection)
	 */
	// yzp
	// @Override
	// public BatchResult sendTestPlan(Integer testPlanId,
	// Collection<Long> terminalIds) {
	// TestPlan testPlan = this.getTestPlan(testPlanId);
	// testPlan.getTestUnit().setTestSuit(testPlan.getTestSuit());
	// BatchResult batchResult = new BatchResult();
	// for (Long terminalId : terminalIds) {
	//
	// batchResult.addSingletonReslut(this.sendTestPlan(testPlan,
	// terminalId));
	// }
	// return batchResult;
	//
	// }

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.testPlan.TestPlanService#sendTestPlan(java.lang.Integer,
	 *      java.util.Collection)
	 */
	public void sendTestPlan(Integer testPlanId, Long terminalId) {
		TestPlan testPlan = this.getTestPlan(testPlanId);
		this.sendTestPlan(testPlan, terminalId);
	}

	/**
	 * 下发测试计划
	 * 
	 * @modify yinzhipeng
	 * @param testPlan
	 * @param terminalId
	 * @return
	 */
	private void sendTestPlan(final TestPlan testPlan, Long terminalId) {
		testPlan.getTestUnit().setTestSuit(testPlan.getTestSuit());
		List<TestScheme> testSuit = testPlan.getTestSuit();
		final Terminal terminal = this.terminalDao.find(terminalId);
		// 合法性验证
		this.validate(testPlan, terminal);
		// 设置呼叫号码
		setCallNo(testPlan, terminal);
		// 设置ModelLock
		fliterModelLock(testPlan, terminal);
		String fileLink = testPlanFileLink +  File.separator  + terminal.getBoxId() +  File.separator
				+ testPlan.getName()
				+ ".xml";
		File testPlanFile = new File(fileLink);
		if (!testPlanFile.exists() && !testPlanFile.getParentFile().exists()) {
			boolean mkdirs = testPlanFile.getParentFile().mkdirs();
			if (!mkdirs) {
				logger.error("Save TestPlan Error : 创建目录失败");
				throw new ApplicationException("下发到终端时,保存测试计划文件异常!");
			}
		}
		String xmlString;
		try {
			xmlString = XStreamUtil.toXMLStr(testPlan).replaceAll(
					"\r|\n", "\r\n");
			FileCopyUtils.copy(xmlString.getBytes(), testPlanFile);
		} catch (Exception e) {
			logger.error("Save TestPlan Error : " + e.getMessage());
			throw new ApplicationException("下发到终端时,保存测试计划文件异常!");
		}

		// 获取版本号
		long configVersion = testPlan.getAutoTestUnit().getVersion();
		// 下发
		// 请求:{"conftestplan":{"boxid":"00100001","version":111,"testplanfilelink":"xx/xx"}}
		// 应答:{"conftestplan":{"result":0}}
		Map<String, Object> map = new HashMap<>();
		map.put("boxid", terminal.getBoxId());
		map.put("version", configVersion);
		map.put("testplanfilelink", fileLink);
		map.put("testplanfilecontent",xmlString);
		JSONObject requJson = new JSONObject();
		requJson.put("conftestplan", map);
		String request = requJson.toString();
		logger.info("请求参数为:"+request);
		Socket socket = ZMQUtils.getZMQSocket();
		try {
			socket.setReceiveTimeOut(5000);
			socket.connect("tcp://" + decodeIp + ":" + decodePort); // 与response端建立连接
			socket.send(request.getBytes()); // 向reponse端发送数据
			byte[] responseBytes = socket.recv(); // 接收response发送回来的数据
			if (null == responseBytes) {
				logger.error("返回结果为空");
				throw new ApplicationException("下发到终端时,后台通信异常!");
			} else {
				ZMQUtils.releaseZMQSocket(socket);
				Integer result = null;
				String response = new String(responseBytes, "UTF8");
				logger.info("返回结果为:"+response);
				JSONObject respJson = JSONObject.fromObject(response);
				if (null != respJson) {
					JSONObject conftestplan = respJson
							.getJSONObject("conftestplan");
					if (null != conftestplan) {
						result = conftestplan.getInt("result");
					}
				}
				if (null == result) {
					logger.error("result 字段为空");
					throw new ApplicationException("下发到终端时,后台下发异常!");
				} else {
					switch (result) {
					case -1:
						// 终端离线,下发成功
						break;
					case 0:
						// 终端在线,下发成功
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if (e instanceof ApplicationException) {
				throw new ApplicationException(e.getMessage());
			} else {
				socket.close();
				throw new ApplicationException("下发到终端时,后台通信异常!");
			}
		}
		// 设置终端的版本号
		terminal.setConfigVersion(configVersion);
		// 设置终端的测试计划路径
		terminal.setTestPlanFileLink(fileLink);
		terminalDao.update(terminal);
		// 设置测试计划为已下发
		testPlan.setSended(true);
		testPlan.setSendDate(new Date());
		testPlanDao.update(testPlan);
	}

	public static void setCallNo(TestPlan testPlan, Terminal terminal) {
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		for (TestScheme testScheme : testSchemes) {
			if (testScheme == null) {
				continue;
			}
			if (testScheme.getCommandList() != null
					&& testScheme.getCommandList().getSynchronize() != null) {
				List<Command> commands = testScheme.getCommandList()
						.getSynchronize().getCommands();
				if (commands != null) {
					for (Command command : commands) {
						if (command == null) {
							continue;
						}
						String commandId = command.getId();
						if (CommandMap.isNeedSetCallNumber(commandId)) {
							if (isChannelNoCallStyle(command)) {
								TestModule module = terminal
										.getTestModule(command.getChannelNo());
								if (module != null) {
									command.setCallNumber(module.getSimCard());
								}
							}
						}
						if (CommandMap.isNeedSetPhoneNumber(commandId)) {
							if (isChannelNoCallStyle(command)) {
								TestModule module = terminal
										.getTestModule(command.getChannelNo());
								command.setPhoneNumber(module.getSimCard());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 是否按通道呼叫
	 * 
	 * @param command
	 * @return
	 */
	public static boolean isChannelNoCallStyle(Command command) {
		return "1".equals(command.getCallStyle());
	}

	/**
	 * 根据模块类型处理modelLock字段
	 * 
	 * @param testPlan
	 *            TestPlan
	 * @param terminal
	 *            Terminal
	 */
	public static void fliterModelLock(TestPlan testPlan, Terminal terminal) {
		if (testPlan == null || terminal == null) {
			return;
		}
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		if (testSchemes == null) {
			return;
		}
		for (TestScheme testScheme : testSchemes) {
			if (testScheme == null) {
				continue;
			}
			String msNo = testScheme.getMsNo();
			TestModule testModule = terminal.getTestModule(msNo);
			if (isSetNull(testModule)) {
				testScheme.setModeLock(null);
			}
		}

	}

	public static boolean isSetNull(TestModule testModule) {
		return ChannelType.GSM.toString().equals(
				testModule.getModuleType().toString())
				|| ChannelType.CDMA.toString().equals(
						testModule.getModuleType().toString());
	}

	private void validate(TestPlan testPlan, Terminal terminal) {
		List<TestScheme> testSchemes = testPlan.getTestSuit();
		StringBuilder stringBuilder = null;
		for (TestScheme testScheme : testSchemes) {
			if (testScheme == null) {
				continue;
			}
			String msNo = testScheme.getMsNo();
			TestModule testModule = terminal.getTestModule(msNo);
			if (testModule == null) {
				if (stringBuilder == null) {
					stringBuilder = new StringBuilder();
				}
				stringBuilder.append("终端没有");
				stringBuilder.append(msNo);
				stringBuilder.append("通道模块/");
			} else {
				if (testScheme.getCommandList() != null
						&& testScheme.getCommandList().getSynchronize() != null) {
					List<Command> commands = testScheme.getCommandList()
							.getSynchronize().getCommands();
					if (commands != null) {
						for (Command command : commands) {
							if (command == null) {
								continue;
							}
							// 进行修改
							String commandId = command.getId();
							String netType = CommandMap.getNetType(commandId);// 根据这个ID去查询COMMAND_MAP
							String[] netTypes = {};
							if(netType != null){	
								netTypes = netType.split("/");// 分割类型
							}
							boolean flag = false;
							for (String type : netTypes) {
								// 和数据库中的模块类型进行对比
								if (type.equals(testModule.getModuleType()
										.toString())) {
									flag = true;
									break;
								}
							}
							if (!flag) {
								if (stringBuilder == null) {
									stringBuilder = new StringBuilder();
								}
								stringBuilder.append("命令");
								stringBuilder.append(command.getName());
								stringBuilder.append("无法在终端通道");
								stringBuilder.append(msNo);
								stringBuilder.append("上执行/");
							}
						}
					}
				}
			}
		}
		if (stringBuilder != null) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			throw new ApplicationException(stringBuilder.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.adc.service.testPlan.TestPlanService#queryAll()
	 */
	@Override
	public Collection<TestPlan> queryAll() {
		return this.testPlanDao.findAll();
	}

	@Override
	public void saveOrUpadate(TestPlan testPlan) {
		if (testPlan == null) {
			return;
		}
		if (testPlan.getId() == null) {
			this.addTestPlan(testPlan);
		} else {
			this.updateTestPlan(testPlan);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testPlan.TestPlanService#queryPageTestPlan(com.datang
	 * .common.action.page.PageList)
	 */
	@Override
	public AbstractPageList queryPageTestPlan(PageList pageList) {
		return testPlanDao.getPageTestPlan(pageList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testPlan.TestPlanService#queryTestPlanLastVersion(
	 * java.lang.Long)
	 */
	@Override
	public Long queryTestPlanLastVersion(Long terminalId) {
		return testPlanDao.queryTestPlanVersion(terminalId);
	}
	
	@Override
	public List<TestPlan> queryTestPlanByBoxid(PageList pageList){
		return testPlanDao.queryTestPlanByBoxid(pageList);
	}

}
