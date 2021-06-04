/**
 * Copyright 2010 Datang Mobile Co.,Ltd. All rights reserved.
 * DTM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.datang.service.testManage.terminal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import com.datang.common.dao.jdbc.JdbcTemplate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.bean.testManage.terminal.TerminalConstants;
import com.datang.bean.testManage.terminal.TerminalImport;
import com.datang.bean.testManage.terminal.TerminalQuery;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.Assert;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.StringUtils;
import com.datang.dao.testLogItem.NetworkTestLogItemDao;
import com.datang.dao.testLogItem.StationVerificationTestDao;
import com.datang.dao.testLogItem.TestLogItemDao;
import com.datang.dao.testManage.terminal.HistorySoftUpgradeDao;
import com.datang.dao.testManage.terminal.TerminalDao;
import com.datang.dao.testManage.terminal.TerminalGroupDao;
import com.datang.dao.testManage.terminal.TerminalMenuDao;
import com.datang.dao.testManage.terminal.TestModuleDao;
import com.datang.dao.testPlan.TestPlanDao;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testLogItem.NetworkTestLogItem;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.HistoryTestPlan;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.domain.testManage.terminal.TestModule;
import com.datang.domain.testManage.terminal.TestPlanStatus;
import com.datang.domain.testPlan.TestPlan;
import com.datang.exception.ApplicationException;
import com.datang.service.testManage.terminal.TerminalService;

/**
 * 设备service服务事项
 * 
 * @author dingzhongchang
 * @version 1.0.0
 * 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TerminalServiceImpl implements TerminalService {

	/**
	 * 终端
	 */
	@Autowired
	private TerminalDao terminalDao;

	/**
	 * 终端分组
	 */
	@Autowired
	private TerminalGroupDao groupDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 测试计划
	 */
	@Autowired
	private TestPlanDao testPlanDao;

	/**
	 * 命令交互
	 */
	// yzp
	// private CommandInteractDao commandInteractDao;

	/**
	 * 菜单
	 */
	@Autowired
	private TerminalMenuDao terminalMenuDao;

	/**
	 * 历史软件更新信息
	 */
	@Autowired
	private HistorySoftUpgradeDao historySoftUpgradeDao;
	
	
	/**
	 * 单站验证日志
	 */
	@Autowired
	private StationVerificationTestDao stationVerificationTestDao;
	
	@Autowired
	private NetworkTestLogItemDao networkTestLogItemDao;
	
	/**
	 * 测试模块
	 */
	@Autowired
	private TestModuleDao testModuleDao;
	
	@Autowired
	private TestLogItemDao testLogItemDao;

	/**
	 * ATU日志
	 */
	// yzp
	// private ATULogDao atuLoginDao;

	/**
	 * GPS
	 */
	// yzp
	// private GPSDao gpsDao;

	/**
	 * 监控
	 */
	// yzp
	// private MonitorDao monitorDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#getTerminalsByMenus(java
	 * .util.List)
	 */
	@Override
	public List<Terminal> getTerminalsByMenus(List<TerminalMenu> menus) {
		List<Terminal> teList = new ArrayList<Terminal>();
		if (!CollectionUtils.isEmpty(menus)) {
			Collection<Long> menuRefIds = new ArrayList<Long>();
			for (TerminalMenu menu : menus) {
				if (MenuType.Terminal.getMenuType().equals(menu.getType())) {
					menuRefIds.add(menu.getRefId());
				}
			}
			if (!CollectionUtils.isEmpty(menuRefIds)) {
				teList.addAll(terminalDao.query(menuRefIds));
				// yzp
				// Queue<String> boxIds = OnlineStatusContext.getInstance()
				// .getAll();
				// updatePageTesStatus(teList, boxIds);
			}
		}
		return teList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#getCurrentHistoryPlan
	 * (java.lang.Long)
	 */
	@Override
	public HistoryTestPlan getCurrentHistoryPlan(Long teId) {
		Terminal te = terminalDao.find(teId);
		Set<HistoryTestPlan> hisPlans = te.getHistoryTestPlans();
		if (!CollectionUtils.isEmpty(hisPlans)) {
			for (HistoryTestPlan plan : hisPlans) {
				if (plan.getTestPlanStatus() == TestPlanStatus.RUNNING) {
					return plan;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#getHisTestPlanBeans(java
	 * .lang.Long)
	 */
	// yzp
	// @Override
	// public Collection<HistoryTestPlanBean> getHisTestPlanBeans(Long teId) {
	// Terminal te = terminalDao.find(teId);
	// Set<HistoryTestPlan> hisPlans = te.getHistoryTestPlans();
	// List<HistoryTestPlanBean> hisPlanBeans = new
	// ArrayList<HistoryTestPlanBean>(
	// 0);
	// if (!CollectionUtils.isEmpty(hisPlans)) {
	// HistoryTestPlanBean runningPlan = null;
	// for (HistoryTestPlan hisPlan : hisPlans) {
	// HistoryTestPlanBean planBean = new HistoryTestPlanBean();
	// TestPlan testPlan = testPlanDao.find(hisPlan.getTestPlanId());
	// if (testPlan == null) {
	// continue;
	// }
	// planBean.setTestPlan(hisPlan);
	// planBean.setTestPlanName(testPlan.getName());
	// if (hisPlan.getTestPlanStatus().equals(TestPlanStatus.RUNNING)) {
	// runningPlan = planBean;
	// continue;
	// }
	// hisPlanBeans.add(planBean);
	// }
	// if (runningPlan != null) {
	// hisPlanBeans.add(0, runningPlan);
	// }
	// }
	// Collections.sort(hisPlanBeans);
	// return hisPlanBeans;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#addTerminal(com.datang
	 * .adc.domain.terminal.Terminal, java.lang.Long)
	 */
	@Override
	public TerminalMenu addTerminal(Terminal terminal, Long refId) {
		if (isExit(terminal.getBoxId())) {
			throw new ApplicationException("终端ID重复");
		}
		if (isNameExit(terminal.getName())) {
			throw new ApplicationException("终端命名重复");
		}
		TerminalMenu pMenu = terminalMenuDao.queryMenuByRef(refId);// 获取父菜单
		// 更新分组信息
		TerminalGroup group = groupDao.find(pMenu.getRefId());
		group.addTerminal(terminal);
		groupDao.update(group);

		// 创建子菜单信息
		TerminalMenu menu = new TerminalMenu();
		menu.setName(terminal.getName());
		menu.setPid(pMenu.getId());
		menu.setRefId(terminalDao.queryTerminal(terminal.getBoxId()).getId());
		menu.setType(MenuType.Terminal.name());
		menu.setUrl(TerminalConstants.TE_INFO_URL + menu.getRefId());
		terminalMenuDao.create(menu);
		return menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#getTerminals(java.util
	 * .Collection, boolean, java.lang.String)
	 */
	@Override
	public Collection<Terminal> getTerminals(Collection<Long> ids,
			boolean enable, String vender) {
		Collection<Terminal> tes = terminalDao.query(ids, enable, vender);// /返回包含终端的信恄1�7
		return tes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#getTerminals(java.util
	 * .Collection)
	 */
	@Override
	public Collection<Terminal> getTerminals(Collection<Long> ids,
			boolean enable) {
		Collection<Terminal> tes = terminalDao.query(ids, enable);// 根据终端ID列表和是否可用状态查询终端
		return tes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.adc.service.terminal.TerminalService#exportTermianls(java.
	 * lang.String, java.util.List)
	 */
	@Override
	public void exportTermianls(String path, List<Terminal> terminals) {
		// EITerminalFactory.getInstance().exportXmlTermianls(path, terminals);
	}

	// 导入终端
	// @Override
	// yzp
	/*
	 * public ResultSet<Terminal> importTermianls(String path, Long groupId) {
	 * ResultSet<Terminal> resultset = EITerminalFactory.getInstance()
	 * .importExcelTerminals(path);// 获取倒入导出终端服务单例 if (resultset.isReferable())
	 * { for (Result<Terminal> result : resultset.getResultset()) { Terminal
	 * terminal = (Terminal) result.getResultObj(); if (terminal != null &&
	 * result.isSuccess()) {
	 * terminal.setName(StringUtils.isEmpty(terminal.getName()) ? terminal
	 * .getBoxId() : terminal.getName()); TerminalGroup newTeGroup =
	 * groupDao.find(groupId); if (terminal.getTerminalGroup() == null) {
	 * terminal.setTerminalGroup(newTeGroup); } String boxId =
	 * terminal.getBoxId(); if (!isExit(boxId)) {// 判断终端是否存在、、、不存在进入添加
	 * addTerminal(terminal, groupId); // terminalDao.create(terminal); } else {
	 * 
	 * 满足L1要求新增代码
	 * 
	 * if (boxId.substring(0, 1).equals("3")) { // throw new //
	 * IOConnectException("此终端ID为3开头且已存在不允许导入:" + // boxId); // ????????
	 * //在这抛出一个异常（L1终端已经存在，不允许导入） Terminal oldTerminal = terminalDao
	 * .queryTerminal(boxId); // 对于重复的基本信息覆盖 oldTerminal =
	 * updateProperties(oldTerminal, terminal);// 新的覆盖老的，，，基本信息
	 * oldTerminal.setTerminalGroup(newTeGroup); // 更新测试模块信息 List<TestModule>
	 * oldTestModules = oldTerminal .getTestModuls();// 老的测试模块 List<TestModule>
	 * newTestModules = terminal .getTestModuls();// 新的测试模块 if
	 * (!CollectionUtils.isEmpty(newTestModules)) {// 判断这个新的测试模块一，二或三，是否为空 for
	 * (TestModule testModule : newTestModules) {// 遍历这个新的测试模块 if
	 * (!CollectionUtils .isEmpty(oldTestModules)) { // 对于重复的将被导入的数据覆盖掉
	 * TestModule oldTestModule = oldTerminal
	 * .getTestModule(String.valueOf(testModule .getChannelsNo())); if
	 * (oldTestModule != null) { Long tstModuleId = oldTestModule .getId();
	 * BeanConverter.copyProperties( oldTestModule, testModule);
	 * oldTestModule.setId(tstModuleId); } else { //
	 * oldTestModules,,循环遍历这个集合。拿到这个值 Iterator<TestModule> it = oldTestModules
	 * .iterator(); while (it.hasNext()) { TestModule tt = it.next();//
	 * 统一的接口来访问具体的集合元素. Long testId = tt.getId(); if (tt != null) {
	 * BeanConverter .copyProperties(tt, testModule); } tt.setId(testId); } } }
	 * else { oldTerminal.addTestModule(testModule); }
	 * updateTerminal(oldTerminal); // terminalDao.update(oldTerminal); } } }
	 * else { Terminal oldTerminal = terminalDao .queryTerminal(boxId); //
	 * 对于重复的基本信息覆盖 oldTerminal = updateProperties(oldTerminal, terminal);//
	 * 新的覆盖老的 oldTerminal.setTerminalGroup(newTeGroup); // 更新测试模块信息
	 * List<TestModule> oldTestModules = oldTerminal .getTestModuls();
	 * List<TestModule> newTestModules = terminal .getTestModuls(); if
	 * (!CollectionUtils.isEmpty(newTestModules)) {// 新的不为空进入 for (TestModule
	 * testModule : newTestModules) {// 遍历新的 if (!CollectionUtils
	 * .isEmpty(oldTestModules) && CollectionUtils.contains(
	 * oldTestModules.iterator(), testModule)) {// 包新的进入 // 对于重复的将被导入的数据覆盖掉
	 * TestModule oldTestModule = oldTerminal
	 * .getTestModule(String.valueOf(testModule .getChannelsNo())); if
	 * (oldTestModule != null) { Long tstModuleId = oldTestModule .getId();
	 * BeanConverter.copyProperties( oldTestModule, testModule);
	 * oldTestModule.setId(tstModuleId); } } else {
	 * oldTerminal.addTestModule(testModule); } updateTerminal(oldTerminal); //
	 * terminalDao.update(oldTerminal); } } } } } } } return resultset; }
	 */

	/**
	 * BOX编号 密码 厂商 硬件版本 软件版本 生产日期 出厂日期 备注
	 * 
	 * @param oldTerminal
	 * @param terminal
	 * @return
	 */
	private Terminal updateProperties(Terminal oldTerminal, Terminal terminal) {
		if (oldTerminal.getBoxId().equalsIgnoreCase(terminal.getBoxId())) {
			oldTerminal.setPassword(terminal.getPassword());
			oldTerminal.setManufacturer(terminal.getManufacturer());
			oldTerminal.setHardwareVersion(terminal.getHardwareVersion());
			oldTerminal.setSoftwareVersion(terminal.getSoftwareVersion());
			oldTerminal.setFactoryDate(terminal.getFactoryDate());
			oldTerminal.setProduceDate(terminal.getProduceDate());
			oldTerminal.setRemark(terminal.getRemark());
		}
		return oldTerminal;
	}

	@Override
	public void addTerminal(Terminal te) {
		Assert.notNull(te, "arg te Can't be Null");
		if (isExit(te.getBoxId())) {
			throw new ApplicationException("终端ID重复");
		}
		if (isNameExit(te.getName())) {
			throw new ApplicationException("终端命名重复");
		}
		terminalDao.create(te);
	}

	/**
	 * 
	 * @param boxId
	 * @return true 已经存在
	 */
	private boolean isExit(String boxId) {
		Terminal te = terminalDao.queryTerminal(boxId);
		return te != null;
	}

	private boolean isNameExit(String name) {
		return null != terminalDao.queryTerminalByName(name);
	}

	@Override
	public void updateTerminal(Terminal te) {
		Assert.notNull(te, "arg te Can't be Null");
		Terminal terminalDB = getTerminal(te.getId());
		if (null == terminalDB) {
			throw new ApplicationException("设备不存在");
		}
		if (isExit(te.getBoxId())
				&& !te.getId().equals(
						terminalDao.queryTerminal(te.getBoxId()).getId())) {
			throw new ApplicationException("终端ID重复");
		}
		if (isNameExit(te.getName())
				&& !te.getId().equals(
						terminalDao.queryTerminalByName(te.getName()).getId())) {
			throw new ApplicationException("终端命名重复");
		}
		
		List<String> boxids = new ArrayList<String>();
		boxids.add(te.getBoxId());
		List<TestLogItem> testLogItemsByBoxIds = testLogItemDao.getTestLogItemsByBoxIds(boxids);
		if(testLogItemsByBoxIds!=null && testLogItemsByBoxIds.size()>0){
			for (TestLogItem testLogItem : testLogItemsByBoxIds) {
				testLogItem.setTerminal(te);
				testLogItemDao.update(testLogItem);
			}
		}
		
		List<NetworkTestLogItem> networkTestLogItems = networkTestLogItemDao.findByboxId(te.getBoxId());
		if(networkTestLogItems!=null && networkTestLogItems.size()>0){
			for (NetworkTestLogItem networkTestLogItem : networkTestLogItems) {
				networkTestLogItem.setTerminal(te);
				networkTestLogItemDao.update(networkTestLogItem);
			}
		}
		
		List<StationVerificationLogPojo> findByBoxid = stationVerificationTestDao.findByBoxid(te.getBoxId());
		if(findByBoxid!=null && findByBoxid.size()>0){
			for (StationVerificationLogPojo stationVerificationLogPojo : findByBoxid) {
				stationVerificationLogPojo.setTerminal(te);
				stationVerificationTestDao.update(stationVerificationLogPojo);
			}
		}
		
		terminalDao.update(te);
		terminalMenuDao.updateByRef(te.getId(), te.getTerminalGroup().getId(),
				te.getName());
	}

	@Override
	public List<Terminal> getAllTerminals() {
		return terminalDao.queryAllTerminals();
	}

	@Override
	public List<Terminal> getTerminals(String... boxIds) {
		return terminalDao.queryTerminals(boxIds);
	}

	@Override
	public List<Terminal> pageQueryTerminals(TerminalQuery query) {
		Collection<Terminal> tes = terminalDao.query(query, null);
		return (List<Terminal>) tes;
	}

	private Collection<Terminal> updatePageTesStatus(
			Collection<Terminal> teList, Queue<String> boxIds) {
		if (!CollectionUtils.isEmpty(teList)) {
			for (Terminal terminal : teList) {
				terminal.setOnline(boxIds.contains(terminal.getBoxId()));// 包含指定元素返回为真///设置这个终端是否在线
			}
		}
		return teList;
	}

	@Override
	public void deleteTerminal(Long... ids) {
		if (ids == null || ids.length == 0) {
			return;
		}
		for (Long teId : ids) {
			Terminal oldTe = getTerminal(teId);
			if (oldTe != null && oldTe.isOnline()) {
				throw new ApplicationException("设备在线不建议删除");
			}
		}
		// 删除历史数据
		deleteHistories(ids);
		// 更新终端信息
		for (Long id : ids) {
			Terminal terminal = terminalDao.find(id);
			if (terminal != null) {
				if (!terminal.isOnline()) {
					TerminalGroup terminalGroup = terminal.getTerminalGroup();
					if (terminalGroup == null
							|| terminalGroup.getTerminals() == null) {
						continue;
					}
					
					List<String> boxids = new ArrayList<String>();
					boxids.add(terminal.getBoxId());
					List<TestLogItem> testLogItemsByBoxIds = testLogItemDao.getTestLogItemsByBoxIds(boxids);
					if(testLogItemsByBoxIds!=null && testLogItemsByBoxIds.size()>0){
						for (TestLogItem testLogItem : testLogItemsByBoxIds) {
							testLogItem.setBoxId(null);
							testLogItem.setTerminal(null);
							testLogItemDao.update(testLogItem);
						}
					}
					
					List<NetworkTestLogItem> networkTestLogItems = networkTestLogItemDao.findByboxId(terminal.getBoxId());
					if(networkTestLogItems!=null && networkTestLogItems.size()>0){
						for (NetworkTestLogItem networkTestLogItem : networkTestLogItems) {
							networkTestLogItem.setBoxId(null);
							networkTestLogItem.setTerminal(null);
							networkTestLogItemDao.update(networkTestLogItem);
						}
					}
					
					List<StationVerificationLogPojo> findByBoxid = stationVerificationTestDao.findByBoxid(terminal.getBoxId());
					if(findByBoxid!=null && findByBoxid.size()>0){
						for (StationVerificationLogPojo stationVerificationLogPojo : findByBoxid) {
							stationVerificationLogPojo.setBoxId(null);
							stationVerificationLogPojo.setTerminal(null);
							stationVerificationTestDao.update(stationVerificationLogPojo);
						}
					}
					terminal.setTerminalGroup(null);
					terminalDao.delete(terminal);
				}
			}
		}
	}

	/**
	 * 保持终端数据结构的一致性，删除历史数据
	 * 
	 * @param id
	 *            终端ID
	 */
	private void deleteHistories(Long... ids) {
		try {
			// yzp
			// historySoftUpgradeDao.deleteByTerminalIds(ids);// 删除下发的历史更新计划
			// atuLoginDao.deleteByTerminalIds(ids);
			// commandInteractDao.deleteCommandInteractByIDs(ids);
			terminalMenuDao.deleteByRefs(ids);
			// yzp
			// monitorDao.deleteByTerminalIds(ids);
			// gpsDao.deleteByTerminalIds(ids);
		} catch (Exception ex) {
			// yzp
			// throw new ApplicationException("terminal.error.delete",
			// TerminalConstants.TERMINAL_PROPERTIES_BUNDLE);
		}
	}

	@Override
	public List<Terminal> getTerminals(Long... ids) {
		Assert.notNull(ids, "arg Can't be Null");
		List<Terminal> tes = new LinkedList<Terminal>();
		for (Long id : ids) {
			tes.add(terminalDao.find(id));
		}
		return tes;
	}

	@Override
	public Terminal getTerminal(Long id) {
		return terminalDao.find(id);
	}
	
	@Override
	public Terminal getTerminalType(Long id) {
		return terminalDao.getTerminalType(id);
	}

	@Override
	public Terminal getTerminal(String boxId) {
		return terminalDao.queryTerminal(boxId);
	}

	/**
	 * 更新某分组下的终端
	 */
	@Override
	public void updateGroup(Long groupId, Long... teIds) {
		List<Terminal> notInTes = new ArrayList<Terminal>();
		Collection<Terminal> tes = terminalDao.findAll();
		Iterator<Terminal> iterator = tes.iterator();
		while (iterator.hasNext()) {
			Terminal te = iterator.next();
			boolean isContained = false;
			if (null != teIds) {
				for (Long id : teIds) {
					if (te.getId().longValue() == id.longValue()) {
						isContained = true;
						break;
					}
				}
				if (!isContained) {
					notInTes.add(te);
				}
			}
		}

		TerminalGroup group = groupDao.find(groupId);
		if (null != teIds) {
			for (Long id : teIds) {
				Terminal te = terminalDao.find(id);
				te.setTerminalGroup(group);
				terminalDao.update(te);
			}

		}
		if (!CollectionUtils.isEmpty(notInTes)) {
			for (Terminal elemnet : notInTes) {
				elemnet.setTerminalGroup(groupDao
						.queryTerminalGroup(TerminalConstants.DEFAULT_GROUP_NAME));
				terminalDao.update(elemnet);
			}
		}

		groupDao.update(group);

	}

	@Override
	public TestPlan getRunningTestPlan(Long teId) {
		Set<HistoryTestPlan> hisTestPlans = terminalDao.queryTestPlans(teId);
		Integer runningPlanId = -1;
		if (!CollectionUtils.isEmpty(hisTestPlans)) {
			for (HistoryTestPlan plan : hisTestPlans) {
				if (plan.getTestPlanStatus() == TestPlanStatus.RUNNING) {
					runningPlanId = plan.getTestPlanId();
					break;
				}
			}
		}
		TestPlan result = null;
		if (runningPlanId != -1) {
			result = testPlanDao.find(runningPlanId);
		}
		return result;
	}

	@Override
	public HistoryTestPlan getHisTestPlan(Long teId, Integer testPlanId) {
		Set<HistoryTestPlan> hisTestPlans = terminalDao.queryTestPlans(teId);
		HistoryTestPlan result = null;
		if (!CollectionUtils.isEmpty(hisTestPlans)) {
			for (HistoryTestPlan plan : hisTestPlans) {
				if (plan.getTestPlanId() == testPlanId) {
					result = plan;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public Set<Integer> queryTestPlanIdsByName(String terminalName) {
		Set<Integer> hisPlanIds = new TreeSet<Integer>();
		Terminal terminal = terminalDao.queryTerminalByName(terminalName);
		if (null != terminal) {
			Set<HistoryTestPlan> hisPlans = terminal.getHistoryTestPlans();
			if (!CollectionUtils.isEmpty(hisPlans)) {
				for (HistoryTestPlan plan : hisPlans) {
					hisPlanIds.add(plan.getTestPlanId());
				}
			}
		}
		return hisPlanIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testManage.terminal.TerminalService#queryPageFileItem
	 * (com.datang.common.action.page.PageList)
	 */
	@Override
	public AbstractPageList queryPageTerminal(PageList pageList) {
		return terminalDao.getPageTerminal(pageList);
	}


	/**
	 * EVENTTYPE
	 * LONGITUDE
	 * LATITUDE
	 * FILE_NAME
	 * */
	@Override
	public List<Map<String,Object>> getFullCuccTraffic() {
		String sql = "SELECT\n" +
				"\tt.ID,\n" +
				"\tt.BOX_ID,\n" +
				"\tt.INDEX,\n" +
				"\tt.EVENTTYPE,\n" +
				"\tt.FILE_NAME,\n" +
				"\tt.TIMESTAMP,\n" +
				"\tt.LONGITUDE,\n" +
				"\tt.LATITUDE\n" +
				"FROM\n" +
				"\t(\n" +
				"\tSELECT\n" +
				"\t\tt.ID,\n" +
				"\t\tt.BOX_ID,\n" +
				"\t\tt.INDEX,\n" +
				"\t\tt.EVENTTYPE,\n" +
				"\t\tt.FILE_NAME,\n" +
				"\t\tt.TIMESTAMP,\n" +
				"\t\tt.LONGITUDE,\n" +
				"\t\tt.LATITUDE,\n" +
				"\t\tROW_NUMBER() OVER(PARTITION BY BOX_ID ORDER BY TIMESTAMP DESC) rn\n" +
				"\tFROM\n" +
				"\t\t(\n" +
				"\t\tSELECT\n" +
				"\t\t\tID,\n" +
				"\t\t\tBOX_ID,\n" +
				"\t\t\tINDEX,\n" +
				"\t\t\tEVENTTYPE,\n" +
				"\t\t\tFILE_NAME,\n" +
				"\t\t\tTIMESTAMP,\n" +
				"\t\t\tLONGITUDE,\n" +
				"\t\t\tLATITUDE\n" +
				"\t\tFROM\n" +
				"\t\t\tIADS_CUCC_TRAFFICINFO ict) t )t\n" +
				"WHERE\n" +
				"\tt.rn = 1\n";
		return jdbcTemplate.objectQueryAll(sql);
	}

	@Override
	public Map<Integer, Object> importTerminal(File xlsFile) {
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(new FileInputStream(xlsFile));
		} catch (IOException e) {
			throw new ApplicationException("导入失败");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("导入失败");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("不是EXCEL文件");
			} catch (Exception e1) {
				throw new ApplicationException("导入失败");
			}
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		Sheet sheetAt = workbook.getSheetAt(0);
		// 总的excel中记录数
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		// int ss = sheetAt.getFirstRowNum();
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("导入EXCEL中没有数据");
		}
		int provinceGroup = -1;
		int cityGroup = -1;
		int name = -1;
		int boxId = -1;
		int testTarget = -1;
		int softwareVersion = -1;
		int hardwareVersion = -1;
		int testPlanVersion = -1;
		int produceDate = -1;
		int factoryDate = -1;
		int remark = -1;
		int manufacturer = -1;
		int enable = -1;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (null == cell) {
				continue;
			}
			String value = cell.getStringCellValue().trim().toLowerCase();
			if (!StringUtils.hasText(value)) {
				continue;
			}
			value = value.replaceAll("（", "(").replaceAll("）", ")");
			if ("*一级域".equals(value)) {
				provinceGroup = i;
			} else if ("*二级域".equals(value)) {
				cityGroup = i;
			} else if ("*终端名称".equals(value)) {
				name = i;
			} else if ("终端类型".equals(value)) {
				testTarget = i;
			} else if ("*id".equals(value)) {
				boxId = i;
			} else if ("软件版本".equals(value)) {
				softwareVersion = i;
			} else if ("硬件版本".equals(value)) {
				hardwareVersion = i;
			} else if ("测试计划版本".equals(value)) {
				testPlanVersion = i;
			} else if ("生产日期".equals(value)) {
				produceDate = i;
			} else if ("出厂日期".equals(value)) {
				factoryDate = i;
			} else if ("备注".equals(value)) {
				remark = i;
			} else if ("厂商".equals(value)) {
				manufacturer = i;
			} else if ("是否可用".equals(value)) {
				enable = i;
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// 失败的记录数
		int failRowNum = 0;
		// 存储tdl邻区
		List<TerminalImport> terminalImports = new ArrayList<>();
		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);
			// 防止空行
			if (null == rowContext) {
				totalRowNum = totalRowNum - 1;
				continue;
			} else {
				// 防止出现中间空白行和防止代码getLastRowNum()的不准确
				short firstCellNum = rowContext.getFirstCellNum();
				short lastCellNum = rowContext.getLastCellNum();
				if (0 == lastCellNum - firstCellNum) {
					totalRowNum = totalRowNum - 1;
					continue;
				}
			}
			TerminalImport terminalImport = new TerminalImport();
			try {
				if (provinceGroup != -1) {
					String value = getCellValue(rowContext
							.getCell(provinceGroup));

					if (StringUtils.hasText(value)) {
						terminalImport.setProvinceGroup(value);
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"一级域\"值");
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"一级域\"列");
				}
				if (cityGroup != -1) {
					String value = getCellValue(rowContext.getCell(cityGroup));

					if (StringUtils.hasText(value)) {
						terminalImport.setCityGroup(value);
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"二级域\"值");
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"二级域\"列");
				}
				if (boxId != -1) {
					String value = getCellValue(rowContext.getCell(boxId));

					if (StringUtils.hasText(value)) {
						terminalImport.setBoxId(value);
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"ID\"值");
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"ID\"列");
				}
				if (name != -1) {
					String value = getCellValue(rowContext.getCell(name));

					if (StringUtils.hasText(value)) {
						terminalImport.setName(value);
					} else {
						throw new ApplicationException("导入EXCEL中缺少\"终端名称\"值");
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"终端名称\"列");
				}
				if (testTarget != -1) {
					String value = getCellValue(rowContext.getCell(testTarget));

					if (StringUtils.hasText(value)) {
						terminalImport.setTestTarget(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"终端类型\"列");
				}
				if (softwareVersion != -1) {
					String value = getCellValue(rowContext
							.getCell(softwareVersion));

					if (StringUtils.hasText(value)) {
						terminalImport.setSoftwareVersion(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"软件版本\"列");
				}
				if (hardwareVersion != -1) {
					String value = getCellValue(rowContext
							.getCell(hardwareVersion));

					if (StringUtils.hasText(value)) {
						terminalImport.setHardwareVersion(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"硬件版本\"列");
				}
				if (testPlanVersion != -1) {
					String value = getCellValue(rowContext
							.getCell(testPlanVersion));

					if (StringUtils.hasText(value)) {
						terminalImport.setTestPlanVersion(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"测试计划版本\"列");
				}
				if (produceDate != -1) {
					String value = getCellValue(rowContext.getCell(produceDate));

					if (StringUtils.hasText(value)) {
						terminalImport.setProduceDate(dateFormat.parse(value));
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"生产日期\"列");
				}
				if (factoryDate != -1) {
					String value = getCellValue(rowContext.getCell(factoryDate));

					if (StringUtils.hasText(value)) {
						terminalImport.setFactoryDate(dateFormat.parse(value));
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"出厂日期\"列");
				}
				if (manufacturer != -1) {
					String value = getCellValue(rowContext
							.getCell(manufacturer));

					if (StringUtils.hasText(value)) {
						terminalImport.setManufacturer(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"厂商\"列");
				}
				if (enable != -1) {
					String value = getCellValue(rowContext.getCell(enable));

					if (StringUtils.hasText(value)) {
						terminalImport.setEnable(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"是否可用\"列");
				}
				if (remark != -1) {
					String value = getCellValue(rowContext.getCell(remark));

					if (StringUtils.hasText(value)) {
						terminalImport.setRemark(value);
					}
				} else {
					throw new ApplicationException("导入EXCEL中缺少\"备注\"列");
				}
				terminalImports.add(terminalImport);
			} catch (ApplicationException applicationException) {
				throw new ApplicationException(
						applicationException.getMessage());
			} catch (Exception e) {
				failRowNum++;
			}
		}
		String why = null;
		Map<Integer, Object> hashMap = new HashMap<Integer, Object>();
		try {
			for (TerminalImport terminalImport : terminalImports) {
				TerminalGroup terminalGroup1 = new TerminalGroup();
				TerminalGroup terminalGroup2 = new TerminalGroup();
				if (StringUtils.hasText(terminalImport.getProvinceGroup())
						&& StringUtils.hasText(terminalImport.getCityGroup())) {
					terminalGroup1.setName(terminalImport.getProvinceGroup());
					TerminalMenu addProvinceGroup = addProvinceGroup(terminalGroup1);
					terminalGroup2.setName(terminalImport.getCityGroup());
					TerminalMenu addCityGroup = addCityGroup(terminalGroup2,
							addProvinceGroup.getId());
					if (StringUtils.hasText(terminalImport.getBoxId())
							&& StringUtils.hasText(terminalImport.getName())) {
						Terminal queryTerminal = terminalDao
								.queryTerminal(terminalImport.getBoxId());
						if (queryTerminal == null) {
							Terminal terminal = new Terminal();
							terminal.setBoxId(terminalImport.getBoxId());
							terminal.setName(terminalImport.getName());
							terminal.setTestTarget(getTestTarget(terminalImport
									.getTestTarget()));
							terminal.setSoftwareVersion(terminalImport
									.getSoftwareVersion());
							terminal.setHardwareVersion(terminalImport
									.getHardwareVersion());
							terminal.setTestPlanVersion(terminalImport
									.getTestPlanVersion());
							terminal.setProduceDate(terminalImport.getProduceDate());
							terminal.setFactoryDate(terminalImport.getFactoryDate());
							terminal.setRemark(terminalImport.getRemark());
							terminal.setManufacturer(terminalImport
									.getManufacturer());
							terminal.setEnable(getEnable(terminalImport.getEnable()));

							TerminalGroup find = groupDao.find(addCityGroup
									.getRefId());
							terminal.setTerminalGroup(find);
							addTerminal(terminal, addCityGroup.getRefId());
							// terminalDao.create(terminal);
						} else {
							failRowNum++;
							why = "已导入同BOXID终端";
						}

					} else {
						failRowNum++;
						why = "BOXID或IMEI为空";
					}
				} else {
					failRowNum++;
					why = "一级域或二级域为空";
				}

			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			why = "一级域和二级域名称不能重复:"+e.getMessage();
			failRowNum = totalRowNum;
		}
		hashMap.put(0, why);
		hashMap.put(1, new int[] { totalRowNum, failRowNum });
		return hashMap;
	}

	/**
	 * 获取Cell中的值
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		String value = new String();

		if (null == cell)
			return value;

		// 简单的查检列类型
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:// 字符串
			value = cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字
			// 读取实数
			double dd = cell.getNumericCellValue();
			long l = (long) dd;

			if (dd - l > 0) {
				// 说明是Double
				value = new Double(dd).toString().trim();
			} else {
				// 说明是Long
				value = new Long(l).toString().trim();
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = new String();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula()).trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:// boolean型值
			value = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue()).trim();
			break;
		default:
			break;
		}
		return value;
	}

	private boolean getEnable(String str) {
		if (str == null) {
			return true;
		}
		if (str.trim().equals("是")) {
			return true;
		} else if (str.trim().equals("否")) {
			return false;
		} else {
			return true;
		}
	}

	private int getTestTarget(String str) {
		if (str == null) {
			return 0;
		}
		if (str.trim().equals("自动LTE")) {
			return 0;
		} else if (str.trim().equals("单模块商务终端")) {
			return 1;
		} else if (str.trim().equals("LTE-FI")) {
			return 2;
		} else {
			return 0;
		}
	}

	// 添加省级组信息
	public TerminalMenu addProvinceGroup(TerminalGroup group) throws HibernateException{
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// 去掉空格
		// 判定新增的省级设备组名是否存在
		if (checkMenuExising(group, null)) {
			// throw new ApplicationException("分组名称已经存在");
			TerminalMenu queryMenu = terminalMenuDao.queryMenu(group.getName());
			return queryMenu;
		} else {
			groupDao.create(group);
			// 创建子菜单信息
			TerminalMenu menu = new TerminalMenu();
			menu.setName(group.getName());
			menu.setPid(null);
			menu.setRefId(group.getId());
			menu.setType(MenuType.Province.name());
			menu.setUrl(TerminalConstants.PROVINCE_INFO_URL + menu.getRefId());
			menu.setIcon(TerminalConstants.MENU_ICON);
			menu.setIconOpen(TerminalConstants.MENU_OPEN_ICON);
			terminalMenuDao.create(menu);
			return menu;
		}

	}

	// 添加市级组信息
	public TerminalMenu addCityGroup(TerminalGroup teGroup, Long refId) {
		Assert.notNull(teGroup, "arg group can't be Null");

		// 获取父menu和更新组信息,检验分组名称
		TerminalMenu pMenu = terminalMenuDao.find(refId);// 获取父菜单
		teGroup.setName(StringUtils.trimAllWhitespace(teGroup.getName()));// 去掉空格
		if (checkMenuExising(teGroup, pMenu)) {
			TerminalMenu queryMenu = terminalMenuDao.queryMenu(teGroup
					.getName());
			return queryMenu;
		} else {
			// 更新省级分组信息,及省级menu信息
			TerminalGroup group = findGroupById(pMenu.getRefId());
			group.addGroup(teGroup);
			updateGroup(group);
			TerminalGroup newGroup = group.getGroup(teGroup.getName());

			// 创建子菜单信息
			TerminalMenu menu = new TerminalMenu();
			menu.setName(teGroup.getName());
			menu.setPid(pMenu.getId());
			menu.setRefId(newGroup.getId());
			menu.setType(MenuType.City.name());
			menu.setIcon(TerminalConstants.MENU_LEAF_ICON);
			menu.setUrl(TerminalConstants.CITY_INFO_URL + menu.getRefId());
			terminalMenuDao.create(menu);
			return menu;
		}
	}

	/**
	 * @param group
	 *            TerminalGroup
	 */
	private boolean checkMenuExising(TerminalGroup teGroup, TerminalMenu pMenu) {
		if (pMenu == null) {// 父终端菜单为空，说明为省级终端菜单
			if (!CollectionUtils.isEmpty(terminalMenuDao.queryMenu(
					MenuType.Province, teGroup.getName()))) {
				return true;
			}
			// 如果父终端菜单不为空，说明为市级终端菜单:所有的市级菜单都不重复
		} else if (!CollectionUtils.isEmpty(terminalMenuDao.queryMenu(
				MenuType.City, teGroup.getName()))) {
			return true;
		}
		return false;
	}

	public TerminalGroup findGroupById(Long groupId) {
		Assert.notNull(groupId, "arg groupId can't be Null");
		if (!isExist(groupId)) {
			throw new ApplicationException("该分组已经不存在");
		}
		return groupDao.find(groupId);
	}

	/**
	 * 判断此分组是否存在
	 * 
	 * @param groupId
	 *            分组
	 * @return 分组已存在 true,否则false
	 */
	public boolean isExist(Long groupId) {
		TerminalGroup group = groupDao.find(groupId);
		return group != null;
	}

	// 更新
	public void updateGroup(TerminalGroup group) {
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// 去掉空格
		if (!isExist(group.getId())) {
			throw new ApplicationException("该分组已经不存在");
		}
		groupDao.update(group);
		terminalMenuDao.updateByRef(group.getId(), group.getName());
	}

	@Override
	public boolean updateTerminalEnable(Long id, boolean b) {
		boolean flag = false;
		try {
			Terminal terminalDB = terminalDao.find(id);
			terminalDB.setEnable(b);
			terminalDao.update(terminalDB);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	

	@Override
	public List<Terminal> findByParam(HashMap<String, Object> param) {
		
		return terminalDao.findByParam(param);
	}
}
