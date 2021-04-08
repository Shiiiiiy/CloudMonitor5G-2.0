/**
 * 
 */
package com.datang.service.VoLTEDissertation.exceptionEvent.impl;

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
import com.datang.dao.VoLTEDissertation.exceptionEvent.CsfbFailDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.DropCallDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.ExceptionEventDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.ImsRegistFailDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.NotConnectDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.VideoDropCallDao;
import com.datang.dao.VoLTEDissertation.exceptionEvent.VideoNotConnectDao;
import com.datang.dao.testLogItem.TestLogItemSignallingDao;
import com.datang.dao.testLogItem.TestLogMeasureDao;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventDropCall;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VideoExceptionEventNotConnect;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEvent;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventCsfbFail;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventDropCall;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventImsRegistFail;
import com.datang.domain.VoLTEDissertation.exceptionEvent.VolteExceptionEventNotConnect;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testLogItem.TestLogMeasure;
import com.datang.service.VoLTEDissertation.exceptionEvent.IVolteExceptionEventService;
import com.datang.service.VoLTEDissertation.wholePreview.IVoLTEService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.exceptionEvent.ExceptionEventResponseBean;
import com.datang.web.beans.VoLTEDissertation.exceptionEvent.ExceptionEventResponseBean1;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * volte质量专题---volte异常事件Service接口实现
 * 
 * @author yinzhipeng
 * @date:2016年4月18日 上午9:00:11
 * @version
 */
@Service
@Transactional
public class VolteExceptionEventServiceImpl implements
		IVolteExceptionEventService {
	/**
	 * 动态kpi指标获取service
	 */
	@Resource(name = "voLTEDissWholePreviewBean")
	private IVoLTEService wholePreviewService;
	@Autowired
	private ExceptionEventDao exceptionEventDao;
	@Autowired
	private NotConnectDao notConnectDao;
	@Autowired
	private DropCallDao dropCallDao;
	@Autowired
	private ImsRegistFailDao imsRegistFailDao;
	@Autowired
	private CsfbFailDao csfbFailDao;
	@Autowired
	private TestLogItemSignallingDao testLogItemSignallingDao;
	@Autowired
	private TestLogMeasureDao testLogMeasureDao;
	@Autowired
	private VideoNotConnectDao videoNotConnectDao;
	@Autowired
	private VideoDropCallDao videoDropCallDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#getVolteExceptionEvent(java.lang.Long)
	 */
	@Override
	public VolteExceptionEvent getVolteExceptionEvent(Long eeId) {
		return exceptionEventDao.find(eeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#getVolteExceptionEventByIds(java.util.List)
	 */
	@Override
	public List<VolteExceptionEvent> getVolteExceptionEventByIds(List<Long> ids) {
		return exceptionEventDao.queryVolteExceptionEventsByLogIds(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#sumEveryExceptionEventNum(java.util.List)
	 */
	@Override
	public Map<String, Integer> sumEveryExceptionEventNum(
			List<Long> testLogItemIds) {
		Map<String, Integer> eventNumMap = new HashMap<String, Integer>();
		eventNumMap.put("notConnect", notConnectDao
				.queryVolteExceptionEventNotConnectNumByLogIds(testLogItemIds));
		eventNumMap.put("dropCall", dropCallDao
				.queryVolteExceptionEventDropCallNumByLogIds(testLogItemIds));
		eventNumMap
				.put("imsRegistFail",
						imsRegistFailDao
								.queryVolteExceptionEventImsRegistFailNumByLogIds(testLogItemIds));
		eventNumMap.put("videoDropCall", videoDropCallDao
				.queryVideoExceptionEventDropCallNumByLogIds(testLogItemIds));
		eventNumMap.put("videoNotConnect", videoNotConnectDao
				.queryVideoExceptionEventNotConnectNumByLogIds(testLogItemIds));
		eventNumMap.put("csfbFail", csfbFailDao
				.queryVolteExceptionEventCsfbFailNumByLogIds(testLogItemIds));
		return eventNumMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventNotConnectByLogIds(java.util.List)
	 */
	@Override
	public List<VolteExceptionEventNotConnect> getVolteExceptionEventNotConnectByLogIds(
			List<Long> testLogItemIds) {
		return notConnectDao
				.queryVolteExceptionEventNotConnectsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doNotConnectWholeAnalysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doNotConnectWholeAnalysis(
			List<VolteExceptionEventNotConnect> volteExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0",
				doNotConnectWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				doNotConnectWholeIndex1Analysis(
						volteExceptionEventNotConnectsByLogIds,
						queryTestLogItems));
		map.put("wholeIndex2",
				doNotConnectWholeIndex2Analysis(volteExceptionEventNotConnectsByLogIds));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doNotConnectWholeIndex0Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doNotConnectWholeIndex0Analysis(
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
				.setStairClassify(ParamConstant.STAIR_CLASSIFY_VOLTE_EE);
		eeWholeIndexParam.setTestLogItemIds(testLogItemIds.toString());
		return (EasyuiPageList) wholePreviewService.queryKpi(eeWholeIndexParam);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doNotConnectWholeIndex1Analysis(java.util.List, java.util.List)
	 */
	@Override
	public EasyuiPageList doNotConnectWholeIndex1Analysis(
			List<VolteExceptionEventNotConnect> volteExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean> rows = new ArrayList<>();
		ExceptionEventResponseBean exceptionEventResponseBean = new ExceptionEventResponseBean();
		rows.add(exceptionEventResponseBean);
		easyuiPageList.setRows(rows);
		if (null == volteExceptionEventNotConnectsByLogIds
				|| 0 == volteExceptionEventNotConnectsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置语音未接通异常事件数量
		 */
		exceptionEventResponseBean
				.setEventNum(volteExceptionEventNotConnectsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有语音未接通异常事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有语音未接通异常事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteExceptionEventNotConnect exceptionEvent : volteExceptionEventNotConnectsByLogIds) {
			/**
			 * 计算所有语音未接通异常事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有语音未接通异常事件:小区数量,以cellid为准
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
			exceptionEventResponseBean.setCellNumRatio(NumberFormatUtils
					.format(cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			exceptionEventResponseBean.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doNotConnectWholeIndex2Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doNotConnectWholeIndex2Analysis(
			List<VolteExceptionEventNotConnect> volteExceptionEventNotConnectsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean1> rows = new ArrayList<>();
		ExceptionEventResponseBean1 exceptionEventResponseBean1 = new ExceptionEventResponseBean1();
		rows.add(exceptionEventResponseBean1);
		easyuiPageList.setRows(rows);
		if (null == volteExceptionEventNotConnectsByLogIds
				|| 0 == volteExceptionEventNotConnectsByLogIds.size()) {
			return easyuiPageList;
		}
		for (VolteExceptionEventNotConnect exceptionEvent : volteExceptionEventNotConnectsByLogIds) {
			Integer failSignallingNode = exceptionEvent.getFailSignallingNode();
			if (null != failSignallingNode) {
				switch (failSignallingNode) {
				/**
				 * 失败信令节点:0'主叫随机接入',1'主叫RRC建立',2'被叫寻呼',3'被叫随机接入', 4 '被叫RRC建立',<br>
				 * 5'主叫QCI=1的专载建立',6'被叫QCI=1的专载建立',7'主叫Invite交互',8'被叫invite交 互 '
				 */
				case 0:
					Integer signallingNode0 = exceptionEventResponseBean1
							.getSignallingNode0();
					exceptionEventResponseBean1
							.setSignallingNode0(null == signallingNode0 ? 1
									: ++signallingNode0);
					break;
				case 1:
					Integer signallingNode1 = exceptionEventResponseBean1
							.getSignallingNode1();
					exceptionEventResponseBean1
							.setSignallingNode1(null == signallingNode1 ? 1
									: ++signallingNode1);
					break;
				case 2:
					Integer signallingNode2 = exceptionEventResponseBean1
							.getSignallingNode2();
					exceptionEventResponseBean1
							.setSignallingNode2(null == signallingNode2 ? 1
									: ++signallingNode2);
					break;
				case 3:
					Integer signallingNode3 = exceptionEventResponseBean1
							.getSignallingNode3();
					exceptionEventResponseBean1
							.setSignallingNode3(null == signallingNode3 ? 1
									: ++signallingNode3);
					break;
				case 4:
					Integer signallingNode4 = exceptionEventResponseBean1
							.getSignallingNode4();
					exceptionEventResponseBean1
							.setSignallingNode4(null == signallingNode4 ? 1
									: ++signallingNode4);
					break;
				case 5:
					Integer signallingNode5 = exceptionEventResponseBean1
							.getSignallingNode5();
					exceptionEventResponseBean1
							.setSignallingNode5(null == signallingNode5 ? 1
									: ++signallingNode5);
					break;
				case 6:
					Integer signallingNode6 = exceptionEventResponseBean1
							.getSignallingNode6();
					exceptionEventResponseBean1
							.setSignallingNode6(null == signallingNode6 ? 1
									: ++signallingNode6);
					break;
				case 7:
					Integer signallingNode7 = exceptionEventResponseBean1
							.getSignallingNode7();
					exceptionEventResponseBean1
							.setSignallingNode7(null == signallingNode7 ? 1
									: ++signallingNode7);
					break;
				case 8:
					Integer signallingNode8 = exceptionEventResponseBean1
							.getSignallingNode8();
					exceptionEventResponseBean1
							.setSignallingNode8(null == signallingNode8 ? 1
									: ++signallingNode8);
					break;
				default:
					break;
				}
			}
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventNotConnectById(java.lang.Long)
	 */
	@Override
	public VolteExceptionEventNotConnect getVolteExceptionEventNotConnectById(
			Long id) {
		return notConnectDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventDropCallByLogIds(java.util.List)
	 */
	@Override
	public List<VolteExceptionEventDropCall> getVolteExceptionEventDropCallByLogIds(
			List<Long> testLogItemIds) {
		return dropCallDao
				.queryVolteExceptionEventDropCallsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doDropCallWholeAnalysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doDropCallWholeAnalysis(
			List<VolteExceptionEventDropCall> volteExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0", doDropCallWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				doDropCallWholeIndex1Analysis(
						volteExceptionEventDropCallsByLogIds, queryTestLogItems));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doDropCallWholeIndex0Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doDropCallWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems) {
		return doNotConnectWholeIndex0Analysis(queryTestLogItems);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doDropCallWholeIndex1Analysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public EasyuiPageList doDropCallWholeIndex1Analysis(
			List<VolteExceptionEventDropCall> volteExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean> rows = new ArrayList<>();
		ExceptionEventResponseBean exceptionEventResponseBean = new ExceptionEventResponseBean();
		rows.add(exceptionEventResponseBean);
		easyuiPageList.setRows(rows);
		if (null == volteExceptionEventDropCallsByLogIds
				|| 0 == volteExceptionEventDropCallsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置语音掉话异常事件数量
		 */
		exceptionEventResponseBean
				.setEventNum(volteExceptionEventDropCallsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有语音语音掉话异常事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有语音掉话异常事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteExceptionEventDropCall exceptionEvent : volteExceptionEventDropCallsByLogIds) {
			/**
			 * 计算所有语音掉话异常事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有语音掉话异常事件:小区数量,以cellid为准
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
			exceptionEventResponseBean.setCellNumRatio(NumberFormatUtils
					.format(cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			exceptionEventResponseBean.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventDropCallById(java.lang.Long)
	 */
	@Override
	public VolteExceptionEventDropCall getVolteExceptionEventDropCallById(
			Long id) {
		return dropCallDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventImsRegistFailByLogIds(java.util.List)
	 */
	@Override
	public List<VolteExceptionEventImsRegistFail> getVolteExceptionEventImsRegistFailByLogIds(
			List<Long> testLogItemIds) {
		return imsRegistFailDao
				.queryVolteExceptionEventImsRegistFailsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doImsRegistFailWholeAnalysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doImsRegistFailWholeAnalysis(
			List<VolteExceptionEventImsRegistFail> volteExceptionEventImsRegistFailsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0",
				doImsRegistFailWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				doImsRegistFailWholeIndex1Analysis(
						volteExceptionEventImsRegistFailsByLogIds,
						queryTestLogItems));
		map.put("wholeIndex2",
				doImsRegistFailWholeIndex2Analysis(volteExceptionEventImsRegistFailsByLogIds));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doImsRegistFailWholeIndex0Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doImsRegistFailWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems) {
		return doNotConnectWholeIndex0Analysis(queryTestLogItems);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doImsRegistFailWholeIndex1Analysis(java.util.List, java.util.List)
	 */
	@Override
	public EasyuiPageList doImsRegistFailWholeIndex1Analysis(
			List<VolteExceptionEventImsRegistFail> volteExceptionEventImsRegistFailsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean> rows = new ArrayList<>();
		ExceptionEventResponseBean exceptionEventResponseBean = new ExceptionEventResponseBean();
		rows.add(exceptionEventResponseBean);
		easyuiPageList.setRows(rows);
		if (null == volteExceptionEventImsRegistFailsByLogIds
				|| 0 == volteExceptionEventImsRegistFailsByLogIds.size()) {
			return easyuiPageList;
		}

		Integer attachFailNum = null;// attach失败次数
		Integer imsFailNum = null;// ims注册失败次数
		Integer epsFailNum = null;// 默认承载建立失败次数
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有IMS注册失败异常事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有IMS注册失败异常事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteExceptionEventImsRegistFail exceptionEvent : volteExceptionEventImsRegistFailsByLogIds) {
			/**
			 * 计算所有IMS注册失败异常事件:attach失败个数,ims注册失败个数
			 * 失败类型:0Attach失败,1默认承载建立失败,2IMS注册失败
			 */
			Integer failType = exceptionEvent.getFailType();
			if (null != failType) {
				switch (failType) {
				case 0:
					attachFailNum = (null == attachFailNum ? 1
							: ++attachFailNum);
					break;
				case 1:
					epsFailNum = (null == epsFailNum ? 1 : ++epsFailNum);
					break;
				case 2:
					imsFailNum = (null == imsFailNum ? 1 : ++imsFailNum);
					break;
				default:
					break;
				}
			}
			/**
			 * 计算所有语音掉话异常事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有语音掉话异常事件:小区数量,以cellid为准
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
		 * 汇总attach失败个数,ims注册失败个数,默认承载建立失败次数
		 */
		exceptionEventResponseBean.setEventNum(attachFailNum);
		exceptionEventResponseBean.setEventNum1(imsFailNum);
		exceptionEventResponseBean.setEventNum2(epsFailNum);
		/**
		 * 汇总小区占比,终端数量占比
		 */
		if (null != cellTotalNum && 0 != cellTotalNum && 0 != cellIdSet.size()) {
			exceptionEventResponseBean.setCellNumRatio(NumberFormatUtils
					.format(cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			exceptionEventResponseBean.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doImsRegistFailWholeIndex2Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doImsRegistFailWholeIndex2Analysis(
			List<VolteExceptionEventImsRegistFail> volteExceptionEventImsRegistFailsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean1> rows = new ArrayList<>();
		ExceptionEventResponseBean1 exceptionEventResponseBean1 = new ExceptionEventResponseBean1();
		rows.add(exceptionEventResponseBean1);
		easyuiPageList.setRows(rows);
		if (null == volteExceptionEventImsRegistFailsByLogIds
				|| 0 == volteExceptionEventImsRegistFailsByLogIds.size()) {
			return easyuiPageList;
		}
		for (VolteExceptionEventImsRegistFail exceptionEvent : volteExceptionEventImsRegistFailsByLogIds) {
			Integer failSignallingNode = exceptionEvent.getFailSignallingNode();
			if (null != failSignallingNode) {
				switch (failSignallingNode) {
				/**
				 * 失败信令节点:0'随机接入',1'RRC建立',2'鉴权',3'QCI=9的专载建立'<br>
				 * 4'QCI=5的专载建立',5'IMS注册'
				 */
				case 0:
					Integer signallingNode0 = exceptionEventResponseBean1
							.getSignallingNode0();
					exceptionEventResponseBean1
							.setSignallingNode0(null == signallingNode0 ? 1
									: ++signallingNode0);
					break;
				case 1:
					Integer signallingNode1 = exceptionEventResponseBean1
							.getSignallingNode1();
					exceptionEventResponseBean1
							.setSignallingNode1(null == signallingNode1 ? 1
									: ++signallingNode1);
					break;
				case 2:
					Integer signallingNode2 = exceptionEventResponseBean1
							.getSignallingNode2();
					exceptionEventResponseBean1
							.setSignallingNode2(null == signallingNode2 ? 1
									: ++signallingNode2);
					break;
				case 3:
					Integer signallingNode3 = exceptionEventResponseBean1
							.getSignallingNode3();
					exceptionEventResponseBean1
							.setSignallingNode3(null == signallingNode3 ? 1
									: ++signallingNode3);
					break;
				case 4:
					Integer signallingNode4 = exceptionEventResponseBean1
							.getSignallingNode4();
					exceptionEventResponseBean1
							.setSignallingNode4(null == signallingNode4 ? 1
									: ++signallingNode4);
					break;
				case 5:
					Integer signallingNode5 = exceptionEventResponseBean1
							.getSignallingNode5();
					exceptionEventResponseBean1
							.setSignallingNode5(null == signallingNode5 ? 1
									: ++signallingNode5);
					break;
				default:
					break;
				}
			}
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventImsRegistFailById(java.lang.Long)
	 */
	@Override
	public VolteExceptionEventImsRegistFail getVolteExceptionEventImsRegistFailById(
			Long id) {
		return imsRegistFailDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventCsfbFailByLogIds(java.util.List)
	 */
	@Override
	public List<VolteExceptionEventCsfbFail> getVolteExceptionEventCsfbFailByLogIds(
			List<Long> testLogItemIds) {
		return csfbFailDao
				.queryVolteExceptionEventCsfbFailsByLogIds(testLogItemIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doCsfbFailWholeAnalysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public Map<String, EasyuiPageList> doCsfbFailWholeAnalysis(
			List<VolteExceptionEventCsfbFail> volteExceptionEventCsfbFailsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0", doCsfbFailWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				doCsfbFailWholeIndex1Analysis(
						volteExceptionEventCsfbFailsByLogIds, queryTestLogItems));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doCsfbFailWholeIndex0Analysis(java.util.List)
	 */
	@Override
	public EasyuiPageList doCsfbFailWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems) {
		return doNotConnectWholeIndex0Analysis(queryTestLogItems);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#doCsfbFailWholeIndex1Analysis(java.util.List,
	 * java.util.List)
	 */
	@Override
	public EasyuiPageList doCsfbFailWholeIndex1Analysis(
			List<VolteExceptionEventCsfbFail> volteExceptionEventCsfbFailsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean> rows = new ArrayList<>();
		ExceptionEventResponseBean exceptionEventResponseBean = new ExceptionEventResponseBean();
		rows.add(exceptionEventResponseBean);
		easyuiPageList.setRows(rows);
		if (null == volteExceptionEventCsfbFailsByLogIds
				|| 0 == volteExceptionEventCsfbFailsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置csfb失败异常事件数量
		 */
		exceptionEventResponseBean
				.setEventNum(volteExceptionEventCsfbFailsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有csfb失败异常事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有csfb失败异常事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VolteExceptionEventCsfbFail exceptionEvent : volteExceptionEventCsfbFailsByLogIds) {
			/**
			 * 计算所有csfb失败异常事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有csfb失败异常事件:小区数量,以cellid为准
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
			exceptionEventResponseBean.setCellNumRatio(NumberFormatUtils
					.format(cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			exceptionEventResponseBean.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #getVolteExceptionEventCsfbFailById(java.lang.Long)
	 */
	@Override
	public VolteExceptionEventCsfbFail getVolteExceptionEventCsfbFailById(
			Long id) {
		return csfbFailDao.find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService
	 * #doNotConnectSignllingPageList(com.datang.common.action.page.PageList)
	 */
	@Override
	public AbstractPageList doExceptionEventSignllingPageList(PageList pageList) {
		return testLogItemSignallingDao.getPageSignalling(pageList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#addQBRRoadName(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public void addQBRRoadName(String roadName, Long eeId) {
		VolteExceptionEvent find = exceptionEventDao.find(eeId);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			exceptionEventDao.addQBRRoadName(roadName, eeId);
		}

	}

	/*
	 * *
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.VoLTEDissertation.exceptionEvent.
	 * IVolteExceptionEventService#queryTestLogMeasuresByTime(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public EasyuiPageList queryTestLogMeasuresByTimePage(Long begainDate,
			Long endDate, int rows, int page) {

		return testLogMeasureDao.getTestLogMeasuresByTimePage(begainDate,
				endDate, rows, page);
	}

	@Override
	public List<TestLogMeasure> queryTestLogMeasuresByTime(Long begainDate,
			Long endDate) {

		return testLogMeasureDao.getTestLogMeasuresByTime(begainDate, endDate);
	}

	@Override
	public List<VideoExceptionEventNotConnect> getVideoExceptionEventNotConnectByLogIds(
			List<Long> testLogItemIds) {
		return videoNotConnectDao
				.queryVideoExceptionEventNotConnectsByLogIds(testLogItemIds);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoNotConnectWholeAnalysis(
			List<VideoExceptionEventNotConnect> videoExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		map.put("wholeIndex0",
				doVideoNotConnectWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				doVideoNotConnectWholeIndex1Analysis(
						videoExceptionEventNotConnectsByLogIds,
						queryTestLogItems));
		// map.put("wholeIndex2",
		// doVideoNotConnectWholeIndex2Analysis(videoExceptionEventNotConnectsByLogIds));
		return map;
	}

	@Override
	public EasyuiPageList doVideoNotConnectWholeIndex0Analysis(
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
		eeWholeIndexParam.setReportType(ParamConstant.REPOR_TTYPE_VOLTE_VIDEO);
		eeWholeIndexParam
				.setStairClassify(ParamConstant.STAIR_CLASSIFY_KPISTATISYICE);
		eeWholeIndexParam.setTestLogItemIds(testLogItemIds.toString());
		AbstractPageList queryKpi = wholePreviewService
				.queryKpi(eeWholeIndexParam);
		eeWholeIndexParam
				.setStairClassify(ParamConstant.STAIR_CLASSIFY_QUALITY);
		AbstractPageList queryKpi2 = wholePreviewService
				.queryKpi(eeWholeIndexParam);
		List rows = queryKpi.getRows();
		List rows2 = queryKpi2.getRows();
		Map<String, Object> rowMaps = new HashMap<>();
		if (null != rows && 1 == rows.size()) {
			Map<String, Object> rowMap = (Map<String, Object>) rows.get(0);
			rowMaps.putAll(rowMap);
		}
		if (null != rows2 && 1 == rows2.size()) {
			Map<String, Object> rowMap = (Map<String, Object>) rows2.get(0);
			rowMaps.putAll(rowMap);
		}

		queryKpi.getRows().clear();
		queryKpi.getRows().add(rowMaps);
		return (EasyuiPageList) queryKpi;
	}

	@Override
	public EasyuiPageList doVideoNotConnectWholeIndex1Analysis(
			List<VideoExceptionEventNotConnect> videoExceptionEventNotConnectsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean> rows = new ArrayList<>();
		ExceptionEventResponseBean exceptionEventResponseBean = new ExceptionEventResponseBean();
		rows.add(exceptionEventResponseBean);
		easyuiPageList.setRows(rows);
		if (null == videoExceptionEventNotConnectsByLogIds
				|| 0 == videoExceptionEventNotConnectsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置视频未接通异常事件数量
		 */
		exceptionEventResponseBean
				.setEventNum(videoExceptionEventNotConnectsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有视频未接通异常事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有视频未接通异常事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VideoExceptionEventNotConnect exceptionEvent : videoExceptionEventNotConnectsByLogIds) {
			/**
			 * 计算所有视频未接通异常事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有视频未接通异常事件:小区数量,以cellid为准
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
			exceptionEventResponseBean.setCellNumRatio(NumberFormatUtils
					.format(cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			exceptionEventResponseBean.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public VideoExceptionEventNotConnect getVideoExceptionEventNotConnectById(
			Long id) {
		return videoNotConnectDao.find(id);
	}

	@Override
	public List<VideoExceptionEventDropCall> getVideoExceptionEventDropCallByLogIds(
			List<Long> testLogItemIds) {
		return videoDropCallDao
				.queryVideoExceptionEventDropCallsByLogIds(testLogItemIds);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoDropCallWholeAnalysis(
			List<VideoExceptionEventDropCall> videoExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("wholeIndex0",
				doVideoDropCallWholeIndex0Analysis(queryTestLogItems));
		map.put("wholeIndex1",
				doVideoDropCallWholeIndex1Analysis(
						videoExceptionEventDropCallsByLogIds, queryTestLogItems));

		return map;
	}

	@Override
	public EasyuiPageList doVideoDropCallWholeIndex0Analysis(
			List<TestLogItem> queryTestLogItems) {
		return doVideoNotConnectWholeIndex0Analysis(queryTestLogItems);
	}

	@Override
	public EasyuiPageList doVideoDropCallWholeIndex1Analysis(
			List<VideoExceptionEventDropCall> videoExceptionEventDropCallsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<ExceptionEventResponseBean> rows = new ArrayList<>();
		ExceptionEventResponseBean exceptionEventResponseBean = new ExceptionEventResponseBean();
		rows.add(exceptionEventResponseBean);
		easyuiPageList.setRows(rows);
		if (null == videoExceptionEventDropCallsByLogIds
				|| 0 == videoExceptionEventDropCallsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置视频掉话异常事件数量
		 */
		exceptionEventResponseBean
				.setEventNum(videoExceptionEventDropCallsByLogIds.size());
		Float cellTotalNum = null;
		Set<Long> cellIdSet = new HashSet<>();// 所有视频掉话异常事件:小区数量,以cellid为准
		Set<String> boxIdSet = new HashSet<>();// 所有视频掉话异常事件:设备数量,以设备boxid为准
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VideoExceptionEventDropCall exceptionEvent : videoExceptionEventDropCallsByLogIds) {
			/**
			 * 计算所有视频掉话异常事件:设备数量,以设备boxid为准
			 */
			String boxId = exceptionEvent.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
			/**
			 * 计算所有视频掉话异常事件:小区数量,以cellid为准
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
			exceptionEventResponseBean.setCellNumRatio(NumberFormatUtils
					.format(cellIdSet.size() / cellTotalNum * 100, 2));
		}
		if (0 != boxIdTotalSet.size() && 0 != boxIdSet.size()) {
			exceptionEventResponseBean.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public VideoExceptionEventDropCall getVideoExceptionEventDropCallById(
			Long id) {
		return videoDropCallDao.find(id);
	}
}
