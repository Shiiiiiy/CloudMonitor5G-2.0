package com.datang.service.VoLTEDissertation.callEstablishDelayException.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.constant.TwoDimensionalChartType;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.VolteCallEstablishDelayExceptionDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.callLocationUpdate.VolteCallEstablishDelayExceptionCallLocationUpdateDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.coreNet.VolteCallEstablishDelayExceptionCoreNetworkDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.other.VolteCallEstablishDelayExceptionOtherProblemDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.overlapCover.OverlapCoverTianKuiAdjustCEDEDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCoverDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCoverDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover.WeakCoverAdviceAddStationCEDEDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover.WeakCoverTianKuiAdjustCEDEDao;
import com.datang.dao.VoLTEDissertation.callEstabliahDelayException.weakCover.WeakCoverTianKuiConnectReverseCEDEDao;
import com.datang.dao.testLogItem.TestLogItemDao;
import com.datang.dao.testLogItem.TestLogItemSignallingDao;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.VolteCallEstablishDelayException;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.calledLocationUpdate.VolteCallEstablishDelayExceptionCalledLocationUpdate;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.coreNetwork.VolteCallEstablishDelayExceptionCoreNetwork;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.otherProblem.VolteCallEstablishDelayExceptionOtherProblem;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.overlapCover.VolteCallEstablishDelayExceptionOverlapCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.VolteCallEstablishDelayExceptionWeakCover;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverAdviceAddStationCEDE;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverTianKuiAdjustCEDE;
import com.datang.domain.VoLTEDissertation.callEstablishDelayException.weakCover.WeakCoverTianKuiConnectReverseCEDE;
import com.datang.domain.chart.OneDimensionalChartConfig;
import com.datang.domain.chart.TwoDimensionalChartValues;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.callEstablishDelayException.IcallEstablishDelayExceptionService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole.WholeBean0;
import com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole.WholeBean1;
import com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole.WholeBean2;
import com.datang.web.beans.VoLTEDissertation.callEstablishDelayException.whole.WholeBean3;

/**
 * 呼叫建立时延异常实现Service
 * 
 * @explain
 * @name CallEstablishDelayExceptionServiceImpl
 * @author shenyanwei
 * @date 2016年5月26日下午1:55:37
 */
@Service
@Transactional
public class CallEstablishDelayExceptionServiceImpl implements
		IcallEstablishDelayExceptionService {
	@Autowired
	private VolteCallEstablishDelayExceptionDao callEstablishDelayExceptionDao;
	@Autowired
	private VolteCallEstablishDelayExceptionCallLocationUpdateDao locationUpdateDao;
	@Autowired
	private VolteCallEstablishDelayExceptionCoreNetworkDao coreNetDao;
	@Autowired
	private VolteCallEstablishDelayExceptionOtherProblemDao otherDao;
	@Autowired
	private VolteCallEstablishDelayExceptionOverlapCoverDao overlapCoverDao;
	@Autowired
	private OverlapCoverTianKuiAdjustCEDEDao overlapCoverTianKuiAdjustCEDEDao;
	@Autowired
	private VolteCallEstablishDelayExceptionWeakCoverDao weakCoverDao;
	@Autowired
	private WeakCoverAdviceAddStationCEDEDao weakCoverAdviceAddStationCEDEDao;
	@Autowired
	private WeakCoverTianKuiAdjustCEDEDao weakCoverTianKuiAdjustCEDEDao;
	@Autowired
	private WeakCoverTianKuiConnectReverseCEDEDao weakCoverTianKuiConnectReverseCEDEDao;
	@Autowired
	private TestLogItemSignallingDao testLogItemSignallingDao;
	@Autowired
	private TestLogItemDao testLogItemDao;

	@Override
	public VolteCallEstablishDelayException getVolteCallEstablishDelayException(
			Long cedeId) {

		VolteCallEstablishDelayException find = callEstablishDelayExceptionDao
				.find(cedeId);
		if (find != null)
			return find;
		else
			return null;
	}

	@Override
	public List<VolteCallEstablishDelayException> getVolteCallEstablishDelayExceptionsByLogIds(
			List<Long> testLogItemIds) {
		return callEstablishDelayExceptionDao
				.queryVolteCallEstablishDelayExceptionsByLogIds(testLogItemIds);

	}

	@Override
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<Long> testLogItemIds,
			List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds) {

		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeRoadIndex0",
				getVolteCallEstablishDelayException(volteCallEstablishDelayExceptionsByLogIds));
		map.put("wholeRoadIndex1",
				doWholeIndex1Analysis(volteCallEstablishDelayExceptionsByLogIds));
		map.put("wholeRoadIndex2", doWholeIndex2Analysis(testLogItemIds));
		map.put("wholeRoadIndex3", doWholeIndex3Analysis(testLogItemIds));

		return map;
	}

	public EasyuiPageList getVolteCallEstablishDelayException(
			List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeBean0> rows = new ArrayList<WholeBean0>();
		WholeBean0 wholeBean0 = new WholeBean0();
		rows.add(wholeBean0);
		easyuiPageList.setRows(rows);
		Float callEstablishDelay = wholeBean0.getCallEstablishDelay();
		Float invite2rrcConnectionSeutpCompleteDelay = wholeBean0
				.getInvite2rrcConnectionSeutpCompleteDelay();
		Float rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay = wholeBean0
				.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay();
		Float invite1002calledPagingDelay = wholeBean0
				.getInvite1002calledPagingDelay();
		Float calledInvite2calledInvite183Delay = wholeBean0
				.getCalledInvite2calledInvite183Delay();
		Float callingInvite2callingInvite180RingingDelay = wholeBean0
				.getCallingInvite2callingInvite180RingingDelay();
		for (VolteCallEstablishDelayException volteCallEstablishDelayException : volteCallEstablishDelayExceptionsByLogIds) {

			if (null != volteCallEstablishDelayException
					.getCallEstablishDelay()) {
				callEstablishDelay = (callEstablishDelay == null ? volteCallEstablishDelayException
						.getCallEstablishDelay() : callEstablishDelay
						+ volteCallEstablishDelayException
								.getCallEstablishDelay());
			}
			if (null != volteCallEstablishDelayException
					.getInvite2rrcConnectionSeutpCompleteDelay()) {
				invite2rrcConnectionSeutpCompleteDelay = (invite2rrcConnectionSeutpCompleteDelay == null ? volteCallEstablishDelayException
						.getInvite2rrcConnectionSeutpCompleteDelay()
						: invite2rrcConnectionSeutpCompleteDelay
								+ volteCallEstablishDelayException
										.getInvite2rrcConnectionSeutpCompleteDelay());
			}
			if (null != volteCallEstablishDelayException
					.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay()) {
				rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay = (rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay == null ? volteCallEstablishDelayException
						.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay()
						: rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
								+ volteCallEstablishDelayException
										.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay());
			}
			if (null != volteCallEstablishDelayException
					.getInvite1002calledPagingDelay()) {
				invite1002calledPagingDelay = (invite1002calledPagingDelay == null ? volteCallEstablishDelayException
						.getInvite1002calledPagingDelay()
						: invite1002calledPagingDelay
								+ volteCallEstablishDelayException
										.getInvite1002calledPagingDelay());
			}
			if (null != volteCallEstablishDelayException
					.getCalledInvite2calledInvite183Delay()) {
				calledInvite2calledInvite183Delay = (calledInvite2calledInvite183Delay == null ? volteCallEstablishDelayException
						.getCalledInvite2calledInvite183Delay()
						: calledInvite2calledInvite183Delay
								+ volteCallEstablishDelayException
										.getCalledInvite2calledInvite183Delay());
			}
			if (null != volteCallEstablishDelayException
					.getCallingInvite2callingInvite180RingingDelay()) {
				callingInvite2callingInvite180RingingDelay = (callingInvite2callingInvite180RingingDelay == null ? volteCallEstablishDelayException
						.getCallingInvite2callingInvite180RingingDelay()
						: callingInvite2callingInvite180RingingDelay
								+ volteCallEstablishDelayException
										.getCallingInvite2callingInvite180RingingDelay());
			}

			if (callEstablishDelay != null
					&& volteCallEstablishDelayExceptionsByLogIds.size() != 0) {
				wholeBean0.setCallEstablishDelay(NumberFormatUtils
						.dfUtil(callEstablishDelay
								/ volteCallEstablishDelayExceptionsByLogIds
										.size()));
			}

			if (calledInvite2calledInvite183Delay != null
					&& volteCallEstablishDelayExceptionsByLogIds.size() != 0) {
				wholeBean0
						.setCalledInvite2calledInvite183Delay(NumberFormatUtils
								.dfUtil(calledInvite2calledInvite183Delay
										/ volteCallEstablishDelayExceptionsByLogIds
												.size()));
			}

			if (callingInvite2callingInvite180RingingDelay != null
					&& volteCallEstablishDelayExceptionsByLogIds.size() != 0) {
				wholeBean0
						.setCallingInvite2callingInvite180RingingDelay(NumberFormatUtils
								.dfUtil(callingInvite2callingInvite180RingingDelay
										/ volteCallEstablishDelayExceptionsByLogIds
												.size()));
			}

			if (invite1002calledPagingDelay != null
					&& volteCallEstablishDelayExceptionsByLogIds.size() != 0) {
				wholeBean0.setInvite1002calledPagingDelay(NumberFormatUtils
						.dfUtil(invite1002calledPagingDelay
								/ volteCallEstablishDelayExceptionsByLogIds
										.size()));
			}

			if (invite2rrcConnectionSeutpCompleteDelay != null
					&& volteCallEstablishDelayExceptionsByLogIds.size() != 0) {
				wholeBean0
						.setInvite2rrcConnectionSeutpCompleteDelay(NumberFormatUtils
								.dfUtil(invite2rrcConnectionSeutpCompleteDelay
										/ volteCallEstablishDelayExceptionsByLogIds
												.size()));
			}

			if (rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay != null
					&& volteCallEstablishDelayExceptionsByLogIds.size() != 0) {
				wholeBean0
						.setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(NumberFormatUtils
								.dfUtil(rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay
										/ volteCallEstablishDelayExceptionsByLogIds
												.size()));
			}

		}

		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex1Analysis(
			List<VolteCallEstablishDelayException> volteCallEstablishDelayExceptionsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeBean1> rows = new ArrayList<WholeBean1>();
		WholeBean1 wholeBean1 = new WholeBean1();
		rows.add(wholeBean1);
		easyuiPageList.setRows(rows);
		Integer zero = 0;
		Integer one = 0;
		Integer two = 0;
		Integer three = 0;
		Integer four = 0;
		Integer five = 0;
		Integer sum = 0;
		for (VolteCallEstablishDelayException volteCallEstablishDelayException : volteCallEstablishDelayExceptionsByLogIds) {
			String valueString = volteCallEstablishDelayException
					.getExceptionSignallingNode();
			String[] string = new String[6];
			string = valueString.split(",");
			for (String str : string) {
				if (str.equals("0")) {
					zero++;
				} else if (str.equals("1")) {
					one++;
				} else if (str.equals("2")) {
					two++;
				} else if (str.equals("3")) {
					three++;
				} else if (str.equals("4")) {
					four++;
				} else if (str.equals("5")) {
					five++;
				} else {
					// no thing to do
				}
				sum++;
			}
			// 各节点占比
			wholeBean1.setCallIn(zero);
			wholeBean1.setCallRAB(one);
			wholeBean1.setCallFunld(two);
			wholeBean1.setCallMov(three);
			wholeBean1.setCallMain(four);
			wholeBean1.setCallOther(five);

		}
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex2Analysis(List<Long> testLogItemIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeBean2> rows = new ArrayList<WholeBean2>();
		WholeBean2 wholeBean2 = new WholeBean2();
		rows.add(wholeBean2);
		easyuiPageList.setRows(rows);

		Long CEDESum = null; // 呼叫建立时延次数
		Long tryCallSum = null; // 尝试呼叫次数
		Set<Long> callId = new HashSet<Long>();// 时延异常小区数量
		Float cellTotalNum = null;// 所有测试日志:小区数量
		Set<String> boxIdSet = new HashSet<>();// 时延异常设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		List<VolteCallEstablishDelayException> list1 = callEstablishDelayExceptionDao
				.queryVolteCallEstablishDelayExceptionsByLogIds(testLogItemIds);
		List<TestLogItem> list2 = new ArrayList<TestLogItem>();
		for (Long id : testLogItemIds) {
			TestLogItem find = testLogItemDao.find(id);
			list2.add(find);
		}
		wholeBean2.setCEDESum(list1.size());

		for (VolteCallEstablishDelayException volteCallEstablishDelayException : list1) {
			Long id = volteCallEstablishDelayException.getCellId();
			if (id != null) {
				callId.add(id);
			}
			String boxId = volteCallEstablishDelayException.getTestLogItem()
					.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
		}

		for (TestLogItem testLogItem : list2) {
			Long testLogCellNum = testLogItem.getCellSumNum();
			if (null != testLogCellNum) {
				cellTotalNum = (cellTotalNum == null ? testLogCellNum
						: cellTotalNum + testLogCellNum);
			}
			Long CellNum = testLogItem.getVolteCallEstablishRequestNum();
			if (null != CellNum) {
				tryCallSum = (tryCallSum == null ? CellNum : tryCallSum
						+ CellNum);
			}

			String boxId = testLogItem.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdTotalSet.add(boxId);
			}

		}
		if (null != cellTotalNum && cellTotalNum != 0 && 0 != callId.size()) {
			wholeBean2.setHousProportion(NumberFormatUtils.format(callId.size()
					/ cellTotalNum * 100, 2));
		}
		if (tryCallSum != null) {
			wholeBean2.setTryCallSum(tryCallSum);
		}
		if (0 != boxIdTotalSet.size()) {
			int a = boxIdSet.size();
			Float b = (float) boxIdTotalSet.size();
			float size = a / b;
			wholeBean2.setTerminalProportion(NumberFormatUtils.format(
					size * 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex3Analysis(List<Long> testLogItemIds) {

		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<WholeBean3> rows = new ArrayList<WholeBean3>();
		WholeBean3 bean3 = new WholeBean3();
		rows.add(bean3);
		easyuiPageList.setRows(rows);
		List<VolteCallEstablishDelayException> list =

		callEstablishDelayExceptionDao
				.queryVolteCallEstablishDelayExceptionsByLogIds(testLogItemIds);
		List<VolteCallEstablishDelayExceptionWeakCover> weakCoversByLogIds = weakCoverDao
				.queryVolteCallEstablishDelayExceptionWeakCoversByLogIds(testLogItemIds);
		List<VolteCallEstablishDelayExceptionOverlapCover> overlapCoversByLogIds = overlapCoverDao
				.queryVolteCallEstablishDelayExceptionOverlapCoversByLogIds(testLogItemIds);
		List<VolteCallEstablishDelayExceptionCalledLocationUpdate> locationUpdatesByLogIds = locationUpdateDao
				.queryVolteCallEstablishDelayExceptionCalledLocationUpdatesByLogIds(testLogItemIds);
		List<VolteCallEstablishDelayExceptionCoreNetwork> networksByLogIds = coreNetDao
				.queryCoreNetworkRoadsByLogIds(testLogItemIds);
		List<VolteCallEstablishDelayExceptionOtherProblem> otherProblemsByLogIds = otherDao
				.queryVolteCallEstablishDelayExceptionOtherProblemsByLogIds(testLogItemIds);
		Integer weakCover = weakCoversByLogIds.size();
		Integer overlapCover = overlapCoversByLogIds.size();
		Integer locationUpdate = locationUpdatesByLogIds.size();
		Integer network = networksByLogIds.size();
		Integer other = otherProblemsByLogIds.size();
		bean3.setWeakCover(weakCover);
		bean3.setOverlapCover(overlapCover);
		bean3.setLocationUpdate(locationUpdate);
		bean3.setCoreNet(network);
		bean3.setOther(other);
		return easyuiPageList;
	}

	@Override
	public List<VolteCallEstablishDelayExceptionWeakCover> getWeakCoversByLogIds(
			List<Long> testLogItemIds) {
		return weakCoverDao
				.queryVolteCallEstablishDelayExceptionWeakCoversByLogIds(testLogItemIds);

	}

	@Override
	public VolteCallEstablishDelayExceptionWeakCover getWeakCoverById(Long id) {

		return weakCoverDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doWeakCoverAnalysis(
			VolteCallEstablishDelayExceptionWeakCover volteCallEstablishDelayExceptionWeakCover) {
		Map<String, EasyuiPageList> map = new HashMap<String, EasyuiPageList>();
		EasyuiPageList easyuiPageList0 = new EasyuiPageList();
		List<WeakCoverAdviceAddStationCEDE> rows0 = weakCoverAdviceAddStationCEDEDao
				.queryWeakCoverAdviceAddStationCEDEByWC(volteCallEstablishDelayExceptionWeakCover);
		easyuiPageList0.setRows(rows0);

		EasyuiPageList easyuiPageList1 = new EasyuiPageList();
		List<WeakCoverTianKuiAdjustCEDE> rows1 = weakCoverTianKuiAdjustCEDEDao
				.queryWeakCoverTianKuiAdjustCEDEByWC(volteCallEstablishDelayExceptionWeakCover);
		easyuiPageList1.setRows(rows1);

		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		List<WeakCoverTianKuiConnectReverseCEDE> rows2 = weakCoverTianKuiConnectReverseCEDEDao
				.queryWeakCoverTianKuiConnectReverseCEDEByWC(volteCallEstablishDelayExceptionWeakCover);
		easyuiPageList2.setRows(rows2);

		EasyuiPageList easyuiPageList3 = new EasyuiPageList();
		List<WholeBean0> rows3 = new ArrayList<WholeBean0>();
		WholeBean0 wholeBean0 = new WholeBean0();
		wholeBean0.setCallEstablishDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionWeakCover
						.getCallEstablishDelay()));
		wholeBean0.setCalledInvite2calledInvite183Delay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionWeakCover
						.getCalledInvite2calledInvite183Delay()));
		wholeBean0
				.setCallingInvite2callingInvite180RingingDelay(NumberFormatUtils.dfUtil(volteCallEstablishDelayExceptionWeakCover
						.getCallingInvite2callingInvite180RingingDelay()));
		wholeBean0.setInvite1002calledPagingDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionWeakCover
						.getInvite1002calledPagingDelay()));
		wholeBean0.setInvite2rrcConnectionSeutpCompleteDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionWeakCover
						.getInvite2rrcConnectionSeutpCompleteDelay()));
		wholeBean0
				.setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(NumberFormatUtils.dfUtil(volteCallEstablishDelayExceptionWeakCover
						.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay()));
		rows3.add(wholeBean0);
		easyuiPageList3.setRows(rows3);
		map.put("weakCoverRoadAdviceAddStationCellInfo", easyuiPageList0);
		map.put("weakCoverRoadTianKuiAdjustCellInfo", easyuiPageList1);
		map.put("weakCoverRoadTiankuiConnectReverseCellInfo", easyuiPageList2);
		map.put("callEstablishDelayException", easyuiPageList3);
		return map;
	}

	@Override
	public List<VolteCallEstablishDelayExceptionOverlapCover> getVolteCallEstablishDelayExceptionOverlapCoversByLogIds(
			List<Long> testLogItemIds) {
		return overlapCoverDao
				.queryVolteCallEstablishDelayExceptionOverlapCoversByLogIds(testLogItemIds);

	}

	@Override
	public VolteCallEstablishDelayExceptionOverlapCover getVolteCallEstablishDelayExceptionOverlapCoverById(
			Long roadId) {

		return overlapCoverDao.find(roadId);
	}

	@Override
	public Map<String, List> doOverlapCoverAnalysis(
			VolteCallEstablishDelayExceptionOverlapCover volteCallEstablishDelayExceptionOverlapCover) {
		Map<String, List> map = new HashMap<String, List>();
		List<WholeBean0> rows3 = new ArrayList<WholeBean0>();
		WholeBean0 wholeBean0 = new WholeBean0();
		wholeBean0.setCallEstablishDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOverlapCover
						.getCallEstablishDelay()));
		wholeBean0.setCalledInvite2calledInvite183Delay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOverlapCover
						.getCalledInvite2calledInvite183Delay()));
		wholeBean0
				.setCallingInvite2callingInvite180RingingDelay(NumberFormatUtils.dfUtil(volteCallEstablishDelayExceptionOverlapCover
						.getCallingInvite2callingInvite180RingingDelay()));
		wholeBean0.setInvite1002calledPagingDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOverlapCover
						.getInvite1002calledPagingDelay()));
		wholeBean0.setInvite2rrcConnectionSeutpCompleteDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOverlapCover
						.getInvite2rrcConnectionSeutpCompleteDelay()));
		wholeBean0
				.setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(NumberFormatUtils.dfUtil(volteCallEstablishDelayExceptionOverlapCover
						.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay()));
		rows3.add(wholeBean0);
		map.put("overlapCoverTianKuiAdjustCEDE",
				overlapCoverTianKuiAdjustCEDEDao
						.queryOverlapCoverTianKuiAdjustCEDEByOC(volteCallEstablishDelayExceptionOverlapCover));
		map.put("callEstablishDelayException", rows3);
		return map;
	}

	@Override
	public List<VolteCallEstablishDelayExceptionCalledLocationUpdate> getLocationUpdatesByLogIds(
			List<Long> testLogItemIds) {

		return locationUpdateDao
				.queryVolteCallEstablishDelayExceptionCalledLocationUpdatesByLogIds(testLogItemIds);
	}

	@Override
	public VolteCallEstablishDelayExceptionCalledLocationUpdate getLocationUpdateById(
			Long roadId) {

		return locationUpdateDao.find(roadId);
	}

	@Override
	public Map<String, List> doLocationUpdateAnalysis(
			VolteCallEstablishDelayExceptionCalledLocationUpdate volteCallEstablishDelayExceptionCalledLocationUpdate) {
		Map<String, List> map = new HashMap<String, List>();
		List<WholeBean0> rows3 = new ArrayList<WholeBean0>();
		WholeBean0 wholeBean0 = new WholeBean0();
		wholeBean0.setCallEstablishDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionCalledLocationUpdate
						.getCallEstablishDelay()));
		wholeBean0.setCalledInvite2calledInvite183Delay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionCalledLocationUpdate
						.getCalledInvite2calledInvite183Delay()));
		wholeBean0
				.setCallingInvite2callingInvite180RingingDelay(NumberFormatUtils
						.dfUtil(volteCallEstablishDelayExceptionCalledLocationUpdate
								.getCallingInvite2callingInvite180RingingDelay()));
		wholeBean0.setInvite1002calledPagingDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionCalledLocationUpdate
						.getInvite1002calledPagingDelay()));
		wholeBean0.setInvite2rrcConnectionSeutpCompleteDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionCalledLocationUpdate
						.getInvite2rrcConnectionSeutpCompleteDelay()));
		wholeBean0
				.setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(NumberFormatUtils
						.dfUtil(volteCallEstablishDelayExceptionCalledLocationUpdate
								.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay()));
		rows3.add(wholeBean0);
		map.put("callEstablishDelayException", rows3);
		return map;
	}

	@Override
	public AbstractPageList doLocationUpdateSignllingPageList(PageList pageList) {

		return testLogItemSignallingDao.getPageSignalling(pageList);
	}

	@Override
	public List<VolteCallEstablishDelayExceptionCoreNetwork> getCoreNetworksByLogIds(
			List<Long> testLogItemIds) {

		return coreNetDao.queryCoreNetworkRoadsByLogIds(testLogItemIds);
	}

	@Override
	public Map<String, List> doCoreNetworkAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds) {
		Map<String, List> map = new HashMap<>();
		map.put("sumRatioHourChart",
				doCoreNetworkRtpPacketLostRatioHourChartAnalysis(
						rtpPacketLostRatioHourChart, testLogItemIds));

		map.put("rsrpAndSinrChart",
				doCoreNetworkRtpPacketLostAndSinrChartAnalysis(testLogItemIds));

		return map;
	}

	@Override
	public List<Object> doCoreNetworkRtpPacketLostRatioHourChartAnalysis(
			OneDimensionalChartConfig rtpPacketLostRatioHourChart,
			List<Long> testLogItemIds) {
		if (null == testLogItemIds
				|| 0 == testLogItemIds.size()
				|| null == rtpPacketLostRatioHourChart
				|| !StringUtils.hasText(rtpPacketLostRatioHourChart
						.getAxisCustomer())) {
			return new ArrayList<>();
		}
		List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetDao
				.queryCoreNetworkRoadIdsByLogIds(testLogItemIds);

		if (null != queryCoreNetworkRoadIdsByLogIds
				&& 0 != queryCoreNetworkRoadIdsByLogIds.size()) {
			List<Object> queryRtpPacketDropRatioHour = coreNetDao
					.queryOneDimensionalChartValues(
							rtpPacketLostRatioHourChart,
							queryCoreNetworkRoadIdsByLogIds);

			return queryRtpPacketDropRatioHour;
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doCoreNetworkRtpPacketLostAndSinrChartAnalysis(java.util.List)
	 */
	@Override
	public List<Object> doCoreNetworkRtpPacketLostAndSinrChartAnalysis(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetDao
				.queryCoreNetworkRoadIdsByLogIds(testLogItemIds);

		if (null != queryCoreNetworkRoadIdsByLogIds
				&& 0 != queryCoreNetworkRoadIdsByLogIds.size()) {
			List<TwoDimensionalChartValues> queryTwoDimenSionalChartValues = coreNetDao
					.queryTwoDimenSionalChartValues(
							TwoDimensionalChartType.RsrpSinr,
							queryCoreNetworkRoadIdsByLogIds);

			if (null != queryTwoDimenSionalChartValues
					&& 0 != queryTwoDimenSionalChartValues.size()) {
				Long totalValue = null;
				for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
					String xAxis = twoDimensionalChartValues.getxAxis();
					String yAxis = twoDimensionalChartValues.getyAxis();
					Long value = twoDimensionalChartValues.getValue();
					if (!StringUtils.hasText(xAxis)
							|| !StringUtils.hasText(yAxis) || null == value) {
						continue;
					}
					totalValue = totalValue == null ? value : value
							+ totalValue;
				}
				if (null != totalValue && !totalValue.equals(0l)) {
					List<Object> list = new ArrayList<Object>();
					for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
						List<Object> vlues = new LinkedList<>();
						String xAxis = twoDimensionalChartValues.getxAxis();
						String yAxis = twoDimensionalChartValues.getyAxis();
						Long value = twoDimensionalChartValues.getValue();
						if (!StringUtils.hasText(xAxis)
								|| !StringUtils.hasText(yAxis) || null == value) {
							continue;
						}
						vlues.add("'" + xAxis + "'");
						vlues.add("'" + yAxis + "'");
						vlues.add(NumberFormatUtils.format((float) value
								/ totalValue * 100, 2));
						list.add(vlues);
					}
					return list;
				}
			}
		}
		return new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.qualityBadRoad.
	 * IVoiceQualityBadRoadService
	 * #doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(java.util.List)
	 */
	@Override
	public List<Object> doCoreNetworkRtpPacketLostAndRsrpChartAnalysis(
			List<Long> testLogItemIds) {
		if (null == testLogItemIds || 0 == testLogItemIds.size()) {
			return new ArrayList<>();
		}
		List<Long> queryCoreNetworkRoadIdsByLogIds = coreNetDao
				.queryCoreNetworkRoadIdsByLogIds(testLogItemIds);

		if (null != queryCoreNetworkRoadIdsByLogIds
				&& 0 != queryCoreNetworkRoadIdsByLogIds.size()) {
			List<TwoDimensionalChartValues> queryTwoDimenSionalChartValues = coreNetDao
					.queryTwoDimenSionalChartValues(
							TwoDimensionalChartType.RsrpSinr,
							queryCoreNetworkRoadIdsByLogIds);

			if (null != queryTwoDimenSionalChartValues
					&& 0 != queryTwoDimenSionalChartValues.size()) {
				Long totalValue = null;
				for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
					String xAxis = twoDimensionalChartValues.getxAxis();
					String yAxis = twoDimensionalChartValues.getyAxis();
					Long value = twoDimensionalChartValues.getValue();
					if (!StringUtils.hasText(xAxis)
							|| !StringUtils.hasText(yAxis) || null == value) {
						continue;
					}
					totalValue = totalValue == null ? value : value
							+ totalValue;
				}
				if (null != totalValue && !totalValue.equals(0l)) {
					List<Object> list = new ArrayList<Object>();
					for (TwoDimensionalChartValues twoDimensionalChartValues : queryTwoDimenSionalChartValues) {
						List<Object> vlues = new LinkedList<>();
						String xAxis = twoDimensionalChartValues.getxAxis();
						String yAxis = twoDimensionalChartValues.getyAxis();
						Long value = twoDimensionalChartValues.getValue();
						if (!StringUtils.hasText(xAxis)
								|| !StringUtils.hasText(yAxis) || null == value) {
							continue;
						}
						vlues.add("'" + xAxis + "'");
						vlues.add("'" + yAxis + "'");
						vlues.add(NumberFormatUtils.format((float) value
								/ totalValue * 100, 2));
						list.add(vlues);
					}

					return list;
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<VolteCallEstablishDelayExceptionOtherProblem> getOthersByLogIds(
			List<Long> testLogItemIds) {

		return otherDao
				.queryVolteCallEstablishDelayExceptionOtherProblemsByLogIds(testLogItemIds);
	}

	@Override
	public VolteCallEstablishDelayExceptionOtherProblem getOtherById(Long roadId) {

		return otherDao.find(roadId);
	}

	@Override
	public Map<String, List> doOtherAnalysis(
			VolteCallEstablishDelayExceptionOtherProblem volteCallEstablishDelayExceptionOtherProblem) {
		Map<String, List> map = new HashMap<String, List>();
		List<WholeBean0> rows3 = new ArrayList<WholeBean0>();
		WholeBean0 wholeBean0 = new WholeBean0();
		wholeBean0.setCallEstablishDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOtherProblem
						.getCallEstablishDelay()));
		wholeBean0.setCalledInvite2calledInvite183Delay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOtherProblem
						.getCalledInvite2calledInvite183Delay()));
		wholeBean0
				.setCallingInvite2callingInvite180RingingDelay(NumberFormatUtils.dfUtil(volteCallEstablishDelayExceptionOtherProblem
						.getCallingInvite2callingInvite180RingingDelay()));
		wholeBean0.setInvite1002calledPagingDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOtherProblem
						.getInvite1002calledPagingDelay()));
		wholeBean0.setInvite2rrcConnectionSeutpCompleteDelay(NumberFormatUtils
				.dfUtil(volteCallEstablishDelayExceptionOtherProblem
						.getInvite2rrcConnectionSeutpCompleteDelay()));
		wholeBean0
				.setRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay(NumberFormatUtils.dfUtil(volteCallEstablishDelayExceptionOtherProblem
						.getRrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay()));
		rows3.add(wholeBean0);
		map.put("callEstablishDelayException", rows3);
		return map;
	}

	@Override
	public AbstractPageList doOtherSignllingPageList(PageList pageList) {

		return testLogItemSignallingDao.getPageSignalling(pageList);
	}

	@Override
	public void addCEDERoadName(String roadName, Long Id) {
		VolteCallEstablishDelayException find = callEstablishDelayExceptionDao
				.find(Id);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			callEstablishDelayExceptionDao.addCEDERoadName(roadName, Id);
		}

	}

}
