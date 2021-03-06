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
 * ??????service????????????
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
	 * ??????
	 */
	@Autowired
	private TerminalDao terminalDao;

	/**
	 * ????????????
	 */
	@Autowired
	private TerminalGroupDao groupDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * ????????????
	 */
	@Autowired
	private TestPlanDao testPlanDao;

	/**
	 * ????????????
	 */
	// yzp
	// private CommandInteractDao commandInteractDao;

	/**
	 * ??????
	 */
	@Autowired
	private TerminalMenuDao terminalMenuDao;

	/**
	 * ????????????????????????
	 */
	@Autowired
	private HistorySoftUpgradeDao historySoftUpgradeDao;
	
	
	/**
	 * ??????????????????
	 */
	@Autowired
	private StationVerificationTestDao stationVerificationTestDao;
	
	@Autowired
	private NetworkTestLogItemDao networkTestLogItemDao;
	
	/**
	 * ????????????
	 */
	@Autowired
	private TestModuleDao testModuleDao;
	
	@Autowired
	private TestLogItemDao testLogItemDao;

	/**
	 * ATU??????
	 */
	// yzp
	// private ATULogDao atuLoginDao;

	/**
	 * GPS
	 */
	// yzp
	// private GPSDao gpsDao;

	/**
	 * ??????
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
			throw new ApplicationException("??????ID??????");
		}
		if (isNameExit(terminal.getName())) {
			throw new ApplicationException("??????????????????");
		}
		TerminalMenu pMenu = terminalMenuDao.queryMenuByRef(refId);// ???????????????
		// ??????????????????
		TerminalGroup group = groupDao.find(pMenu.getRefId());
		group.addTerminal(terminal);
		groupDao.update(group);

		// ?????????????????????
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
		Collection<Terminal> tes = terminalDao.query(ids, enable, vender);// /???????????????????????????1???7
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
		Collection<Terminal> tes = terminalDao.query(ids, enable);// ????????????ID???????????????????????????????????????
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

	// ????????????
	// @Override
	// yzp
	/*
	 * public ResultSet<Terminal> importTermianls(String path, Long groupId) {
	 * ResultSet<Terminal> resultset = EITerminalFactory.getInstance()
	 * .importExcelTerminals(path);// ???????????????????????????????????? if (resultset.isReferable())
	 * { for (Result<Terminal> result : resultset.getResultset()) { Terminal
	 * terminal = (Terminal) result.getResultObj(); if (terminal != null &&
	 * result.isSuccess()) {
	 * terminal.setName(StringUtils.isEmpty(terminal.getName()) ? terminal
	 * .getBoxId() : terminal.getName()); TerminalGroup newTeGroup =
	 * groupDao.find(groupId); if (terminal.getTerminalGroup() == null) {
	 * terminal.setTerminalGroup(newTeGroup); } String boxId =
	 * terminal.getBoxId(); if (!isExit(boxId)) {// ??????????????????????????????????????????????????????
	 * addTerminal(terminal, groupId); // terminalDao.create(terminal); } else {
	 * 
	 * ??????L1??????????????????
	 * 
	 * if (boxId.substring(0, 1).equals("3")) { // throw new //
	 * IOConnectException("?????????ID???3?????????????????????????????????:" + // boxId); // ????????
	 * //???????????????????????????L1??????????????????????????????????????? Terminal oldTerminal = terminalDao
	 * .queryTerminal(boxId); // ????????????????????????????????? oldTerminal =
	 * updateProperties(oldTerminal, terminal);// ???????????????????????????????????????
	 * oldTerminal.setTerminalGroup(newTeGroup); // ???????????????????????? List<TestModule>
	 * oldTestModules = oldTerminal .getTestModuls();// ?????????????????? List<TestModule>
	 * newTestModules = terminal .getTestModuls();// ?????????????????? if
	 * (!CollectionUtils.isEmpty(newTestModules)) {// ???????????????????????????????????????????????????????????? for
	 * (TestModule testModule : newTestModules) {// ?????????????????????????????? if
	 * (!CollectionUtils .isEmpty(oldTestModules)) { // ?????????????????????????????????????????????
	 * TestModule oldTestModule = oldTerminal
	 * .getTestModule(String.valueOf(testModule .getChannelsNo())); if
	 * (oldTestModule != null) { Long tstModuleId = oldTestModule .getId();
	 * BeanConverter.copyProperties( oldTestModule, testModule);
	 * oldTestModule.setId(tstModuleId); } else { //
	 * oldTestModules,,?????????????????????????????????????????? Iterator<TestModule> it = oldTestModules
	 * .iterator(); while (it.hasNext()) { TestModule tt = it.next();//
	 * ?????????????????????????????????????????????. Long testId = tt.getId(); if (tt != null) {
	 * BeanConverter .copyProperties(tt, testModule); } tt.setId(testId); } } }
	 * else { oldTerminal.addTestModule(testModule); }
	 * updateTerminal(oldTerminal); // terminalDao.update(oldTerminal); } } }
	 * else { Terminal oldTerminal = terminalDao .queryTerminal(boxId); //
	 * ????????????????????????????????? oldTerminal = updateProperties(oldTerminal, terminal);//
	 * ?????????????????? oldTerminal.setTerminalGroup(newTeGroup); // ????????????????????????
	 * List<TestModule> oldTestModules = oldTerminal .getTestModuls();
	 * List<TestModule> newTestModules = terminal .getTestModuls(); if
	 * (!CollectionUtils.isEmpty(newTestModules)) {// ????????????????????? for (TestModule
	 * testModule : newTestModules) {// ???????????? if (!CollectionUtils
	 * .isEmpty(oldTestModules) && CollectionUtils.contains(
	 * oldTestModules.iterator(), testModule)) {// ??????????????? // ?????????????????????????????????????????????
	 * TestModule oldTestModule = oldTerminal
	 * .getTestModule(String.valueOf(testModule .getChannelsNo())); if
	 * (oldTestModule != null) { Long tstModuleId = oldTestModule .getId();
	 * BeanConverter.copyProperties( oldTestModule, testModule);
	 * oldTestModule.setId(tstModuleId); } } else {
	 * oldTerminal.addTestModule(testModule); } updateTerminal(oldTerminal); //
	 * terminalDao.update(oldTerminal); } } } } } } } return resultset; }
	 */

	/**
	 * BOX?????? ?????? ?????? ???????????? ???????????? ???????????? ???????????? ??????
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
			throw new ApplicationException("??????ID??????");
		}
		if (isNameExit(te.getName())) {
			throw new ApplicationException("??????????????????");
		}
		terminalDao.create(te);
	}

	/**
	 * 
	 * @param boxId
	 * @return true ????????????
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
			throw new ApplicationException("???????????????");
		}
		if (isExit(te.getBoxId())
				&& !te.getId().equals(
						terminalDao.queryTerminal(te.getBoxId()).getId())) {
			throw new ApplicationException("??????ID??????");
		}
		if (isNameExit(te.getName())
				&& !te.getId().equals(
						terminalDao.queryTerminalByName(te.getName()).getId())) {
			throw new ApplicationException("??????????????????");
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
				terminal.setOnline(boxIds.contains(terminal.getBoxId()));// ??????????????????????????????///??????????????????????????????
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
				throw new ApplicationException("???????????????????????????");
			}
		}
		// ??????????????????
		deleteHistories(ids);
		// ??????????????????
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
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @param id
	 *            ??????ID
	 */
	private void deleteHistories(Long... ids) {
		try {
			// yzp
			// historySoftUpgradeDao.deleteByTerminalIds(ids);// ?????????????????????????????????
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
	 * ???????????????????????????
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
			throw new ApplicationException("????????????");
		} catch (IllegalArgumentException e) {
			try {
				workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (IOException e1) {
				throw new ApplicationException("????????????");
			} catch (IllegalArgumentException e1) {
				throw new ApplicationException("??????EXCEL??????");
			} catch (Exception e1) {
				throw new ApplicationException("????????????");
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
		// ??????excel????????????
		int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
		if (0 == totalRowNum) {
			throw new ApplicationException("??????EXCEL???????????????");
		}
		// int ss = sheetAt.getFirstRowNum();
		Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
		if (null == row) {
			throw new ApplicationException("??????EXCEL???????????????");
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
			value = value.replaceAll("???", "(").replaceAll("???", ")");
			if ("*?????????".equals(value)) {
				provinceGroup = i;
			} else if ("*?????????".equals(value)) {
				cityGroup = i;
			} else if ("*????????????".equals(value)) {
				name = i;
			} else if ("????????????".equals(value)) {
				testTarget = i;
			} else if ("*id".equals(value)) {
				boxId = i;
			} else if ("????????????".equals(value)) {
				softwareVersion = i;
			} else if ("????????????".equals(value)) {
				hardwareVersion = i;
			} else if ("??????????????????".equals(value)) {
				testPlanVersion = i;
			} else if ("????????????".equals(value)) {
				produceDate = i;
			} else if ("????????????".equals(value)) {
				factoryDate = i;
			} else if ("??????".equals(value)) {
				remark = i;
			} else if ("??????".equals(value)) {
				manufacturer = i;
			} else if ("????????????".equals(value)) {
				enable = i;
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// ??????????????????
		int failRowNum = 0;
		// ??????tdl??????
		List<TerminalImport> terminalImports = new ArrayList<>();
		for (int i = sheetAt.getFirstRowNum() + 1; i <= sheetAt.getLastRowNum(); i++) {
			Row rowContext = sheetAt.getRow(i);
			// ????????????
			if (null == rowContext) {
				totalRowNum = totalRowNum - 1;
				continue;
			} else {
				// ??????????????????????????????????????????getLastRowNum()????????????
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
						throw new ApplicationException("??????EXCEL?????????\"?????????\"???");
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"?????????\"???");
				}
				if (cityGroup != -1) {
					String value = getCellValue(rowContext.getCell(cityGroup));

					if (StringUtils.hasText(value)) {
						terminalImport.setCityGroup(value);
					} else {
						throw new ApplicationException("??????EXCEL?????????\"?????????\"???");
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"?????????\"???");
				}
				if (boxId != -1) {
					String value = getCellValue(rowContext.getCell(boxId));

					if (StringUtils.hasText(value)) {
						terminalImport.setBoxId(value);
					} else {
						throw new ApplicationException("??????EXCEL?????????\"ID\"???");
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"ID\"???");
				}
				if (name != -1) {
					String value = getCellValue(rowContext.getCell(name));

					if (StringUtils.hasText(value)) {
						terminalImport.setName(value);
					} else {
						throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (testTarget != -1) {
					String value = getCellValue(rowContext.getCell(testTarget));

					if (StringUtils.hasText(value)) {
						terminalImport.setTestTarget(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (softwareVersion != -1) {
					String value = getCellValue(rowContext
							.getCell(softwareVersion));

					if (StringUtils.hasText(value)) {
						terminalImport.setSoftwareVersion(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (hardwareVersion != -1) {
					String value = getCellValue(rowContext
							.getCell(hardwareVersion));

					if (StringUtils.hasText(value)) {
						terminalImport.setHardwareVersion(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (testPlanVersion != -1) {
					String value = getCellValue(rowContext
							.getCell(testPlanVersion));

					if (StringUtils.hasText(value)) {
						terminalImport.setTestPlanVersion(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"??????????????????\"???");
				}
				if (produceDate != -1) {
					String value = getCellValue(rowContext.getCell(produceDate));

					if (StringUtils.hasText(value)) {
						terminalImport.setProduceDate(dateFormat.parse(value));
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (factoryDate != -1) {
					String value = getCellValue(rowContext.getCell(factoryDate));

					if (StringUtils.hasText(value)) {
						terminalImport.setFactoryDate(dateFormat.parse(value));
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (manufacturer != -1) {
					String value = getCellValue(rowContext
							.getCell(manufacturer));

					if (StringUtils.hasText(value)) {
						terminalImport.setManufacturer(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"??????\"???");
				}
				if (enable != -1) {
					String value = getCellValue(rowContext.getCell(enable));

					if (StringUtils.hasText(value)) {
						terminalImport.setEnable(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"????????????\"???");
				}
				if (remark != -1) {
					String value = getCellValue(rowContext.getCell(remark));

					if (StringUtils.hasText(value)) {
						terminalImport.setRemark(value);
					}
				} else {
					throw new ApplicationException("??????EXCEL?????????\"??????\"???");
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
							why = "????????????BOXID??????";
						}

					} else {
						failRowNum++;
						why = "BOXID???IMEI??????";
					}
				} else {
					failRowNum++;
					why = "???????????????????????????";
				}

			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			why = "???????????????????????????????????????:"+e.getMessage();
			failRowNum = totalRowNum;
		}
		hashMap.put(0, why);
		hashMap.put(1, new int[] { totalRowNum, failRowNum });
		return hashMap;
	}

	/**
	 * ??????Cell?????????
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		String value = new String();

		if (null == cell)
			return value;

		// ????????????????????????
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:// ?????????
			value = cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// ??????
			// ????????????
			double dd = cell.getNumericCellValue();
			long l = (long) dd;

			if (dd - l > 0) {
				// ?????????Double
				value = new Double(dd).toString().trim();
			} else {
				// ?????????Long
				value = new Long(l).toString().trim();
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = new String();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula()).trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:// boolean??????
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
		if (str.trim().equals("???")) {
			return true;
		} else if (str.trim().equals("???")) {
			return false;
		} else {
			return true;
		}
	}

	private int getTestTarget(String str) {
		if (str == null) {
			return 0;
		}
		if (str.trim().equals("??????LTE")) {
			return 0;
		} else if (str.trim().equals("?????????????????????")) {
			return 1;
		} else if (str.trim().equals("LTE-FI")) {
			return 2;
		} else {
			return 0;
		}
	}

	// ?????????????????????
	public TerminalMenu addProvinceGroup(TerminalGroup group) throws HibernateException{
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// ????????????
		// ?????????????????????????????????????????????
		if (checkMenuExising(group, null)) {
			// throw new ApplicationException("????????????????????????");
			TerminalMenu queryMenu = terminalMenuDao.queryMenu(group.getName());
			return queryMenu;
		} else {
			groupDao.create(group);
			// ?????????????????????
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

	// ?????????????????????
	public TerminalMenu addCityGroup(TerminalGroup teGroup, Long refId) {
		Assert.notNull(teGroup, "arg group can't be Null");

		// ?????????menu??????????????????,??????????????????
		TerminalMenu pMenu = terminalMenuDao.find(refId);// ???????????????
		teGroup.setName(StringUtils.trimAllWhitespace(teGroup.getName()));// ????????????
		if (checkMenuExising(teGroup, pMenu)) {
			TerminalMenu queryMenu = terminalMenuDao.queryMenu(teGroup
					.getName());
			return queryMenu;
		} else {
			// ????????????????????????,?????????menu??????
			TerminalGroup group = findGroupById(pMenu.getRefId());
			group.addGroup(teGroup);
			updateGroup(group);
			TerminalGroup newGroup = group.getGroup(teGroup.getName());

			// ?????????????????????
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
		if (pMenu == null) {// ???????????????????????????????????????????????????
			if (!CollectionUtils.isEmpty(terminalMenuDao.queryMenu(
					MenuType.Province, teGroup.getName()))) {
				return true;
			}
			// ????????????????????????????????????????????????????????????:?????????????????????????????????
		} else if (!CollectionUtils.isEmpty(terminalMenuDao.queryMenu(
				MenuType.City, teGroup.getName()))) {
			return true;
		}
		return false;
	}

	public TerminalGroup findGroupById(Long groupId) {
		Assert.notNull(groupId, "arg groupId can't be Null");
		if (!isExist(groupId)) {
			throw new ApplicationException("????????????????????????");
		}
		return groupDao.find(groupId);
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param groupId
	 *            ??????
	 * @return ??????????????? true,??????false
	 */
	public boolean isExist(Long groupId) {
		TerminalGroup group = groupDao.find(groupId);
		return group != null;
	}

	// ??????
	public void updateGroup(TerminalGroup group) {
		Assert.notNull(group, "arg group can't be Null");
		group.setName(StringUtils.trimAllWhitespace(group.getName()));// ????????????
		if (!isExist(group.getId())) {
			throw new ApplicationException("????????????????????????");
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
