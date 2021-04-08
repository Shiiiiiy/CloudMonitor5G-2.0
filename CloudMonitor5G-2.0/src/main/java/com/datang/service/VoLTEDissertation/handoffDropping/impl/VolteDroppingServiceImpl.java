package com.datang.service.VoLTEDissertation.handoffDropping.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.dao.VoLTEDissertation.handoffDropping.VolteDroppingDao;
import com.datang.dao.VoLTEDissertation.handoffDropping.VolteDroppingIntDao;
import com.datang.dao.VoLTEDissertation.handoffDropping.VolteDroppingSRVCCDao;
import com.datang.dao.testLogItem.TestLogItemSignallingDao;
import com.datang.dao.testLogItem.TestLogMeasureDao;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDropping;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingInt;
import com.datang.domain.VoLTEDissertation.handoffDropping.VolteDroppingSRVCC;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.TestLogMeasure;
import com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService;
import com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.handoffDropping.DroppingBean;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * 切换失败Service借口的实现
 * 
 * @author shenyanwei
 * @date 2016年4月20日下午3:15:40
 */
@Service
@Transactional
public class VolteDroppingServiceImpl implements VolteDroppingService {
	/**
	 * 动态kpi指标获取service
	 */
	@Resource(name = "voLTEDissWholePreviewBean")
	private IVoLTEService wholePreviewService;
	@Autowired
	private VolteDroppingDao droppingDao;
	@Autowired
	private VolteDroppingIntDao droppingIntDao;
	@Autowired
	private VolteDroppingSRVCCDao droppingSRVCCDao;
	@Autowired
	private TestLogItemSignallingDao testLogItemSignallingDao;
	@Autowired
	private TestLogMeasureDao testLogMeasureDao;

	@Override
	public Map<String, Integer> sumEveryVolteDroppingNum(
			List<Long> testLogItemIds) {
		Map<String, Integer> eventNumMap = new HashMap<String, Integer>();
		eventNumMap.put("droppingSRVCCDao", droppingSRVCCDao
				.queryVolteDroppingSRVCCNumByLogIds(testLogItemIds));
		eventNumMap
				.put("droppingIntDao", droppingIntDao
						.queryVolteDroppingIntNumByLogIds(testLogItemIds));
		return eventNumMap;
	}

	@Override
	public List<VolteDroppingSRVCC> getVolteDroppingSRVCCsByLogIds(
			List<Long> testLogItemIds) {
		return droppingSRVCCDao
				.queryVolteDroppingSRVCCsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService
	 * #getVolteDroppingSRVCCsByLogIdsAndCompareLogIds(java.util.List)
	 */
	@Override
	public List<VolteDroppingSRVCC> getVolteDroppingSRVCCsByLogIdsAndCompareLogIds(
			List<Long> testLogItemIds, List<Long> compareTestLogItemIds) {
		List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndCompareLogIds = droppingSRVCCDao
				.queryVolteDroppingSRVCCsByLogIdsAndCompareLogIds(
						testLogItemIds, compareTestLogItemIds);
		return queryVolteDroppingSRVCCsByLogIdsAndCompareLogIds;
	}

	@Override
	public Map<String, EasyuiPageList> droppingSRVCCWholeAnalysis(
			List<VolteDroppingSRVCC> volteDroppingSRVCCsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0",
				droppingSRVCCWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				droppingSRVCCWholeIndex1Analysis(volteDroppingSRVCCsByLogIds,
						queryTestLogItems));
		return map;
	}

	@Override
	public EasyuiPageList droppingSRVCCWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems) {
		if (null == queryTestLogItems || 0 == queryTestLogItems.size()) {
			return new EasyuiPageList();
		}
		StringBuffer testLogItemIds = new StringBuffer();
		for (int i = 0; i < queryTestLogItems.size(); i++) {
			testLogItemIds
					.append((i == queryTestLogItems.size() - 1) ? (queryTestLogItems
							.get(i).getRecSeqNo()) : (queryTestLogItems.get(i)
							.getRecSeqNo() + ","));
		}
		VoLTEWholePreviewParam eeWholeIndexParam = new VoLTEWholePreviewParam();
		eeWholeIndexParam.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
		eeWholeIndexParam.setReportType(ParamConstant.REPOR_TTYPE_VOLTE);
		eeWholeIndexParam
				.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_HOF);
		eeWholeIndexParam.setTestLogItemIds(testLogItemIds.toString());
		return (EasyuiPageList) wholePreviewService.queryKpi(eeWholeIndexParam);
	}

	@Override
	public EasyuiPageList droppingSRVCCWholeIndex1Analysis(
			List<VolteDroppingSRVCC> volteDroppingSRVCCsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<DroppingBean> rows = new ArrayList<>();
		DroppingBean droppingBean = new DroppingBean();
		rows.add(droppingBean);
		easyuiPageList.setRows(rows);
		if (null == volteDroppingSRVCCsByLogIds
				|| 0 == volteDroppingSRVCCsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置切换失败事件数量
		 */
		droppingBean.setEventNum(volteDroppingSRVCCsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有SRVCC切换失败事件：小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有SRVCC切换失败事件： 设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteDroppingSRVCC exceptionEvent : volteDroppingSRVCCsByLogIds) {
			/**
			 * 计算所有SRVCC切换失败事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有SRVCC切换失败事件:小区数量,以cellid为准
			 */
			Long cellId = exceptionEvent.getCellId();
			if (null != cellId) {
				cellIdSet.add(cellId);
			}
		}
		for (TestLogItem testLog : queryTestLogItems) {
			/**
			 * 计算所有测试日志:小区数
			 * 
			 */
			Long testLogCellNum = testLog.getCellSumNum();
			if (null != testLogCellNum) {
				cellTotalNum = (cellTotalNum == null ? testLogCellNum
						: cellTotalNum + testLogCellNum);
			}

			/**
			 * 计算所有测试日志:设备数量,以设备boxid为准
			 */
			String boxId = testLog.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdTotalSet.add(boxId);
			}
		}
		/**
		 * 汇总小区占比,终端数量占比
		 */
		if (null != cellTotalNum && 0 != cellTotalNum && 0 != cellIdSet.size()) {
			droppingBean.setCellNumRatio(NumberFormatUtils.format(
					cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			droppingBean.setTerminalNumRatio(NumberFormatUtils.format(
					(float) boxIdSet.size() / (float) boxIdTotalSet.size()
							* 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public VolteDroppingSRVCC getVolteDroppingSRVCCById(Long id) {

		return droppingSRVCCDao.find(id);
	}

	@Override
	public List<VolteDroppingInt> getVolteDroppingIntsByLogIds(
			List<Long> testLogItemIds) {

		return droppingIntDao.queryVolteDroppingIntsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService
	 * #getVolteDroppingIntsByLogIdsAndCompareLogIds(java.util.List,
	 * java.util.List)
	 */
	@Override
	public List<VolteDroppingInt> getVolteDroppingIntsByLogIdsAndCompareLogIds(
			List<Long> testLogItemIds, List<Long> compareTestLogItemIds) {
		List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCompareLogIds = droppingIntDao
				.queryVolteDroppingIntsByLogIdsAndCompareLogIds(testLogItemIds,
						compareTestLogItemIds);
		return queryVolteDroppingIntsByLogIdsAndCompareLogIds;
	}

	@Override
	public Map<String, EasyuiPageList> DroppingIntWholeAnalysis(
			List<VolteDroppingInt> volteDroppingIntsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0",
				droppingIntWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				droppingIntWholeIndex1Analysis(volteDroppingIntsByLogIds,
						queryTestLogItems));
		return map;
	}

	@Override
	public EasyuiPageList droppingIntWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems) {

		return droppingSRVCCWholeIndex0Analysis(queryTestLogItems);
	}

	@Override
	public EasyuiPageList droppingIntWholeIndex1Analysis(
			List<VolteDroppingInt> volteDroppingIntsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<DroppingBean> rows = new ArrayList<>();
		DroppingBean droppingBean = new DroppingBean();
		rows.add(droppingBean);
		easyuiPageList.setRows(rows);
		if (null == volteDroppingIntsByLogIds
				|| 0 == volteDroppingIntsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置切换失败事件数量
		 */
		droppingBean.setEventNum(volteDroppingIntsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有系统内部切换失败事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有系统内部切换失败事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteDroppingInt exceptionEvent : volteDroppingIntsByLogIds) {
			/**
			 * 计算所有系统内部切换失败事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有系统内部切换失败事件:小区数量,以cellid为准
			 */
			Long cellId = exceptionEvent.getCellId();
			if (null != cellId) {
				cellIdSet.add(cellId);
			}
		}
		for (TestLogItem testLog : queryTestLogItems) {
			/**
			 * 计算所有测试日志:小区数
			 * 
			 */
			Long testLogCellNum = testLog.getCellSumNum();
			if (null != testLogCellNum) {
				cellTotalNum = (cellTotalNum == null ? testLogCellNum
						: cellTotalNum + testLogCellNum);
			}

			/**
			 * 计算所有测试日志:设备数量,以设备boxid为准
			 */
			String boxId = testLog.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdTotalSet.add(boxId);
			}
		}
		/**
		 * 汇总小区占比,终端数量占比
		 */
		if (null != cellTotalNum && 0 != cellTotalNum && 0 != cellIdSet.size()) {
			droppingBean.setCellNumRatio(NumberFormatUtils.format(
					cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			droppingBean.setTerminalNumRatio(NumberFormatUtils.format(
					(float) boxIdSet.size() / (float) boxIdTotalSet.size()
							* 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public VolteDroppingInt getVolteDroppingIntById(Long id) {
		return droppingIntDao.find(id);
	}

	@Override
	public AbstractPageList droppingSignllingPageList(PageList pageList) {
		return testLogItemSignallingDao.getPageSignalling(pageList);

	}

	@Override
	public void addQBRRoadName(String roadName, Long droppingId) {
		VolteDropping find = droppingDao.find(droppingId);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			droppingDao.addQBRRoadName(roadName, droppingId);
		}
	}

	@Override
	public VolteDropping getVolteDropping(Long droppingId) {
		return droppingDao.find(droppingId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService
	 * #queryVolteDroppingSRVCCsByLogIdsAndCellId(java.util.List,
	 * java.lang.Long)
	 */
	@Override
	public List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndFailId(
			List<Long> testLogItemIds, Long failId) {
		List<VolteDroppingSRVCC> queryVolteDroppingSRVCCsByLogIdsAndFailId = droppingSRVCCDao
				.queryVolteDroppingSRVCCsByLogIdsAndFailId(testLogItemIds,
						failId);
		return queryVolteDroppingSRVCCsByLogIdsAndFailId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService
	 * #queryVolteDroppingIntsByLogIdsAndCellIdAndFailId(java.util.List,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCellIdAndFailId(
			List<Long> testLogItemIds, Long cellId, Long failId) {
		List<VolteDroppingInt> queryVolteDroppingIntsByLogIdsAndCellIdAndFailId = droppingIntDao
				.queryVolteDroppingIntsByLogIdsAndCellIdAndFailId(
						testLogItemIds, cellId, failId);
		return queryVolteDroppingIntsByLogIdsAndCellIdAndFailId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService
	 * #getVolteDroppingByLogIds(java.util.List)
	 */
	@Override
	public List<VolteDropping> getVolteDroppingByLogIds(
			List<Long> testLogItemIds) {
		List<VolteDropping> volteDroppingsByLogIds = droppingDao
				.queryVolteDroppingsByLogIds(testLogItemIds);
		return volteDroppingsByLogIds;
	}

	/*
	 * *
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.VoLTEDissertation.handoffDropping.VolteDroppingService
	 * #queryTestLogMeasuresByTime(java.util.Date, java.util.Date)
	 */
	@Override
	public List<TestLogMeasure> queryTestLogMeasuresByTime(Long begainDate,
			Long endDate) {

		return testLogMeasureDao.getTestLogMeasuresByTime(begainDate, endDate);
	}
}
