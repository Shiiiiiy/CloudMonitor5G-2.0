/**
 * 
 */
package com.datang.service.stream.streamVideoQualitybad.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.util.StringUtils;
import com.datang.dao.platform.projectParam.LteCellDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamDisturbAdviceDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamDisturbDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamDownDispatchSmallAdviceDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamDownDispatchSmallDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamNeighbourAdviceDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamNeighbourPlotDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamOtherDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamOverCoverAdviceDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamOverCoverDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamPingPongAdviceDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamPingPongCutEventDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamPingPongDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamQualityBadDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamWeakCoverAdviceDao;
import com.datang.dao.stream.streamVideoQualitybad.StreamWeakCoverDao;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.stream.streamVideoQualitybad.StreamQualityBad;
import com.datang.domain.stream.streamVideoQualitybad.disturb.StreamDisturbAdvice;
import com.datang.domain.stream.streamVideoQualitybad.disturb.StreamQualtyBadDisturb;
import com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall.StreamDownDispatchSmallAdvice;
import com.datang.domain.stream.streamVideoQualitybad.downDispatchSmall.StreamQualityBadDownDispatchSmall;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamNeighbourAdvice;
import com.datang.domain.stream.streamVideoQualitybad.neighbourPlot.StreamQualityBadNeighbourPlot;
import com.datang.domain.stream.streamVideoQualitybad.other.StreamQualityBadOther;
import com.datang.domain.stream.streamVideoQualitybad.overCover.StreamOverCoverAdvice;
import com.datang.domain.stream.streamVideoQualitybad.overCover.StreamQualityBadOverCover;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamPingPongAdvice;
import com.datang.domain.stream.streamVideoQualitybad.pingPong.StreamQualityBadPingPong;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamQualityBadWeakCover;
import com.datang.domain.stream.streamVideoQualitybad.weakCover.StreamWeakCoverAdvice;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.stream.streamVideoQualitybad.IStreamQualityBadService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamStatementeResponseBean;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamWholeResponseBean0;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamWholeResponseBean2;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamWholeResponseBean3;
import com.datang.web.beans.stream.streamVideoQualitybad.StreamWholeResponseBean4;

/**
 * ???????????????---?????????????????????Service????????????
 * 
 * @explain
 * @name StreamQualityBadServiceImpl
 * @author shenyanwei
 * @date 2017???10???23?????????1:09:23
 */
@Service
@Transactional
public class StreamQualityBadServiceImpl implements IStreamQualityBadService {

	@Autowired
	private StreamDisturbAdviceDao streamDisturbAdviceDao;
	@Autowired
	private StreamNeighbourAdviceDao streamNeighbourAdviceDao;
	@Autowired
	private StreamOverCoverAdviceDao streamOverCoverAdviceDao;
	@Autowired
	private StreamPingPongAdviceDao streamPingPongAdviceDao;
	@Autowired
	private StreamPingPongCutEventDao streamPingPongCutEventDao;
	@Autowired
	private StreamDisturbDao streamDisturbDao;
	@Autowired
	private StreamNeighbourPlotDao streamNeighbourPlotDao;
	@Autowired
	private StreamOtherDao streamOtherDao;
	@Autowired
	private StreamOverCoverDao streamOverCoverDao;
	@Autowired
	private StreamPingPongDao streamPingPongDao;
	@Autowired
	private StreamQualityBadDao streamQualityBadDao;
	@Autowired
	private StreamWeakCoverDao streamWeakCoverDao;
	@Autowired
	private StreamWeakCoverAdviceDao streamWeakCoverAdviceDao;
	@Autowired
	private ITestLogItemService testLogItemService;
	@Autowired
	private StreamDownDispatchSmallDao streamDownDispatchSmallDao;
	@Autowired
	private StreamDownDispatchSmallAdviceDao streamDownDispatchSmallAdviceDao;
	@Autowired
	private LteCellDao lteCellDao;

	@Override
	public StreamQualityBad getStreamQualityBad(Long vqbId) {

		return streamQualityBadDao.find(vqbId);
	}

	@Override
	public List<StreamQualityBad> getStreamQualityBadsByLogIds(
			List<Long> testLogItemIds) {

		return streamQualityBadDao
				.queryStreamQualityBadsByLogIds(testLogItemIds);
	}

	@Override
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		// map.put("wholeRoadIndex0",doWholeIndex0Analysis(volteQualityBadRoadsByLogIds));
		map.put("wholeRoadIndex0",
				doWholeIndex00Analysis(streamQualityBadsByLogIds));
		// map.put("wholeRoadIndex1",
		// doWholeIndex1Analysis(streamQualityBadsByLogIds,
		// queryTestLogItems, ids));
		// map.put("wholeRoadIndex5",
		// doWholeIndex5Analysis(streamQualityBadsByLogIds,
		// queryTestLogItems, ids));
		map.put("wholeRoadIndex2",
				doWholeIndex2Analysis(streamQualityBadsByLogIds,
						queryTestLogItems));
		map.put("wholeRoadIndex3",
				doWholeIndex3Analysis(streamQualityBadsByLogIds));
		map.put("wholeRoadIndex4",
				doWholeIndex4Analysis(streamQualityBadsByLogIds));
		return map;
	}

	@Override
	public EasyuiPageList doWholeIndex00Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<StreamWholeResponseBean0> rows = new ArrayList<>();
		StreamWholeResponseBean0 wholeIndexResponseBean0 = new StreamWholeResponseBean0();
		rows.add(wholeIndexResponseBean0);
		easyuiPageList.setRows(rows);

		if (null == streamQualityBadsByLogIds
				|| 0 == streamQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}

		StreamQualityBad videoQualityBad0 = new StreamQualityBad();// ??????bean
		for (StreamQualityBad videoQualityBad : streamQualityBadsByLogIds) {
			/**
			 * ????????????????????????RSRP??????SINR
			 */
			Float rsrpSum = videoQualityBad0.getRsrpSum();
			Float rsrpSum2 = videoQualityBad.getRsrpSum();
			Float sinrSum = videoQualityBad0.getSinrSum();
			Float sinrSum2 = videoQualityBad.getSinrSum();
			Integer rsrporsinrNumber = videoQualityBad0.getRsrporsinrNumber();
			Integer rsrporsinrNumber2 = videoQualityBad.getRsrporsinrNumber();
			if (null != rsrpSum2) {
				videoQualityBad0.setRsrpSum(rsrpSum == null ? rsrpSum2
						: rsrpSum + rsrpSum2);
			}
			if (null != sinrSum2) {
				videoQualityBad0.setSinrSum(sinrSum == null ? sinrSum2
						: sinrSum + sinrSum2);
			}
			if (null != rsrporsinrNumber2) {
				videoQualityBad0
						.setRsrporsinrNumber(rsrporsinrNumber == null ? rsrporsinrNumber2
								: rsrporsinrNumber + rsrporsinrNumber2);
			}

			/**
			 * ????????????????????????????????????
			 */
			Float stallingratioSum = videoQualityBad0.getStallingratioSum();
			Float stallingratioSum2 = videoQualityBad.getStallingratioSum();
			Integer stallingratioNumber = videoQualityBad0
					.getStallingratioNumber();
			Integer stallingratioNumber2 = videoQualityBad
					.getStallingratioNumber();
			if (null != stallingratioSum2) {
				videoQualityBad0
						.setStallingratioSum(stallingratioSum == null ? stallingratioSum2
								: stallingratioSum + stallingratioSum2);
			}
			if (null != stallingratioNumber2) {
				videoQualityBad0
						.setStallingratioNumber(stallingratioNumber == null ? stallingratioNumber2
								: stallingratioNumber + stallingratioNumber2);
			}
			/**
			 * ??????????????????????????????????????????
			 */
			Float initialbuffertimeSum = videoQualityBad0
					.getInitialbuffertimeSum();
			Float initialbuffertimeSum2 = videoQualityBad
					.getInitialbuffertimeSum();
			Integer initialbuffertimeNumber = videoQualityBad0
					.getInitialbuffertimeNumber();
			Integer initialbuffertimeNumber2 = videoQualityBad
					.getInitialbuffertimeNumber();
			if (null != initialbuffertimeSum2) {
				videoQualityBad0
						.setInitialbuffertimeSum(initialbuffertimeSum == null ? initialbuffertimeSum2
								: initialbuffertimeSum + initialbuffertimeSum2);
			}
			if (null != initialbuffertimeNumber2) {
				videoQualityBad0
						.setInitialbuffertimeNumber(initialbuffertimeNumber == null ? initialbuffertimeNumber2
								: initialbuffertimeNumber
										+ initialbuffertimeNumber2);
			}
			/**
			 * ????????????????????????????????????????????????
			 */
			Float videoresolutionSum = videoQualityBad0.getVideoresolutionSum();
			Float videoresolutionSum2 = videoQualityBad.getVideoresolutionSum();
			Integer videoresolutionNumber = videoQualityBad0
					.getVideoresolutionNumber();
			Integer videoresolutionNumber2 = videoQualityBad
					.getVideoresolutionNumber();
			if (null != videoresolutionSum2) {
				videoQualityBad0
						.setVideoresolutionSum(videoresolutionSum == null ? videoresolutionSum2
								: videoresolutionSum + videoresolutionSum2);
			}
			if (null != videoresolutionNumber2) {
				videoQualityBad0
						.setVideoresolutionNumber(videoresolutionNumber == null ? videoresolutionNumber2
								: videoresolutionNumber
										+ videoresolutionNumber2);
			}
			/**
			 * ??????????????????VMOS??????
			 */
			Float vmosSum = videoQualityBad0.getVmosSum();
			Float vmosSum2 = videoQualityBad.getVmosSum();
			Integer vmosNumber = videoQualityBad0.getVmosNumber();
			Integer vmosNumber2 = videoQualityBad.getVmosNumber();
			if (null != vmosSum2) {
				videoQualityBad0.setVmosSum(vmosSum == null ? vmosSum2
						: vmosSum + vmosSum2);
			}
			if (null != vmosNumber2) {
				videoQualityBad0.setVmosNumber(vmosNumber == null ? vmosNumber2
						: vmosNumber + vmosNumber2);
			}

		}
		/**
		 * ??????rsrp??????
		 */
		Float rsrpValue = videoQualityBad0.getRsrpSum();
		Integer rsrporsinrNumber = videoQualityBad0.getRsrporsinrNumber();
		if (null != rsrpValue && rsrporsinrNumber != 0) {
			wholeIndexResponseBean0.setRsrpValueAvg(NumberFormatUtils.format(
					rsrpValue / rsrporsinrNumber, 2));
		}
		/**
		 * ??????sinr??????
		 */
		Float sinrValue = videoQualityBad0.getSinrSum();
		if (null != sinrValue && rsrporsinrNumber != 0) {
			wholeIndexResponseBean0.setSinrValueAvg(NumberFormatUtils.format(
					sinrValue / rsrporsinrNumber, 2));
		}

		/**
		 * ????????????????????????????????????
		 */
		Float stallingratioSum = videoQualityBad0.getStallingratioSum();
		Integer stallingratioNumber = videoQualityBad0.getStallingratioNumber();
		if (null != stallingratioSum && stallingratioSum != 0
				&& stallingratioNumber != 0) {
			wholeIndexResponseBean0.setStallingratioAvg(NumberFormatUtils
					.format(stallingratioSum / stallingratioNumber, 2));
		}
		/**
		 * ??????????????????????????????????????????
		 */
		Float initialbuffertimeSum = videoQualityBad0.getInitialbuffertimeSum();
		Integer initialbuffertimeNumber = videoQualityBad0
				.getInitialbuffertimeNumber();
		if (null != initialbuffertimeSum && initialbuffertimeSum != 0
				&& initialbuffertimeNumber != 0) {
			wholeIndexResponseBean0.setInitialbuffertimeAvg(NumberFormatUtils
					.format(initialbuffertimeSum / initialbuffertimeNumber, 2));
		}
		/**
		 * ????????????????????????????????????????????????
		 */
		Float videoresolutionSum = videoQualityBad0.getVideoresolutionSum();
		Integer videoresolutionNumber = videoQualityBad0
				.getVideoresolutionNumber();
		if (null != videoresolutionSum && videoresolutionSum != 0
				&& videoresolutionNumber != 0) {
			wholeIndexResponseBean0.setVideoresolutionAvg(NumberFormatUtils
					.format(videoresolutionSum / videoresolutionNumber, 2));
		}
		/**
		 * ??????????????????VMOS??????
		 */
		Float vmosSum = videoQualityBad0.getVmosSum();
		Integer vmosNumber = videoQualityBad0.getVmosNumber();
		if (null != vmosSum && vmosSum != 0 && vmosNumber != 0) {
			wholeIndexResponseBean0.setVmos(NumberFormatUtils.format(vmosSum
					/ vmosNumber, 2));
		}
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex1Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		// if (null == streamQualityBadsByLogIds
		// || 0 == streamQualityBadsByLogIds.size()) {
		// return easyuiPageList;
		// }
		// Integer valueIn1_25 = 0;
		// Integer valueIn25_3 = 0;
		// Integer valueIn3_35 = 0;
		// Integer valueIn35_5 = 0;
		// Integer valueSum = 0;
		// // for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
		// // if (null != videoQualityBad.getVmos()) {
		// // if (1 <= videoQualityBad.getVmos()
		// // && videoQualityBad.getVmos() < 2.5) {
		// // valueIn1_25++;
		// // } else if (2.5 <= videoQualityBad.getVmos()
		// // && videoQualityBad.getVmos() < 3) {
		// // valueIn25_3++;
		// // } else if (3 <= videoQualityBad.getVmos()
		// // && videoQualityBad.getVmos() < 3.5) {
		// // valueIn3_35++;
		// // } else if (3.5 <= videoQualityBad.getVmos()
		// // && videoQualityBad.getVmos() < 5) {
		// // valueIn35_5++;
		// // }
		// // }
		// // }
		// List queryList = streamQualityBadDao.queryList(ids);
		// Object[] object = (Object[]) queryList.get(0);
		// if (object != null && object.length != 0) {
		// for (int i = 0; i < object.length; i++) {
		// if (object[i] != null) {
		// switch (i) {
		// case 0:
		// valueIn1_25 = Integer
		// .valueOf(String.valueOf(object[i])) / 1000;
		// // System.out.println(Integer.valueOf(String
		// // .valueOf(object[i])) / 1000);
		// break;
		// case 1:
		// valueIn25_3 = Integer
		// .valueOf(String.valueOf(object[i])) / 1000;
		// // System.out.println(Integer.valueOf(String
		// // .valueOf(object[i])) / 1000);
		// break;
		// case 2:
		// valueIn3_35 = Integer
		// .valueOf(String.valueOf(object[i])) / 1000;
		// // System.out.println(Integer.valueOf(String
		// // .valueOf(object[i])) / 1000);
		// break;
		// case 3:
		// valueIn35_5 = Integer
		// .valueOf(String.valueOf(object[i])) / 1000;
		// // System.out.println(Integer.valueOf(String
		// // .valueOf(object[i])) / 1000);
		// break;
		// case 4:
		// valueSum = Integer.valueOf(String.valueOf(object[i])) / 1000;
		// // System.out.println(Integer.valueOf(String
		// // .valueOf(object[i])) / 1000);
		// break;
		// default:
		// break;
		// }
		// }
		//
		// }
		// }
		//
		// List<VideoWholeResponseBean1> rows = new ArrayList<>();
		// // Integer sumsize = valueIn1_25 + valueIn25_3 + valueIn35_5 +
		// // valueIn3_35;
		// if (valueSum != 0) {
		// VideoWholeResponseBean1 wholeIndexResponseBean10 = new
		// VideoWholeResponseBean1();
		// wholeIndexResponseBean10.setValue1to25(NumberFormatUtils.format(
		// (float) (valueIn1_25 / 1.0 / valueSum), 4));
		//
		// wholeIndexResponseBean10.setValue25to3(NumberFormatUtils.format(
		// (float) (valueIn25_3 / 1.0 / valueSum), 4));
		//
		// wholeIndexResponseBean10.setValue3to35(NumberFormatUtils.format(
		// (float) (valueIn3_35 / 1.0 / valueSum), 4));
		//
		// wholeIndexResponseBean10.setValue35to5(NumberFormatUtils.format(
		// (float) (valueIn35_5 / 1.0 / valueSum), 4));
		// rows.add(wholeIndexResponseBean10);
		// }
		//
		// easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	/**
	 * ??????Vmos???????????????????????????
	 * 
	 * @param videoQualityBadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doWholeIndex5Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		// if (null == streamQualityBadsByLogIds
		// || 0 == streamQualityBadsByLogIds.size()) {
		// return easyuiPageList;
		// }
		// EasyuiPageList doWholeIndex1Analysis = doWholeIndex1Analysis(
		// streamQualityBadsByLogIds, queryTestLogItems, ids);
		// List<VideoWholeResponseBean5> rows = new ArrayList<>();
		// VideoWholeResponseBean5 ResponseBean1tu25 = new
		// VideoWholeResponseBean5();
		// VideoWholeResponseBean5 ResponseBean25tu3 = new
		// VideoWholeResponseBean5();
		// VideoWholeResponseBean5 ResponseBean3tu35 = new
		// VideoWholeResponseBean5();
		// VideoWholeResponseBean5 ResponseBean35tu5 = new
		// VideoWholeResponseBean5();
		// if (doWholeIndex1Analysis.getRows().size() > 0) {
		// VideoWholeResponseBean1 bean1 = (VideoWholeResponseBean1)
		// doWholeIndex1Analysis
		// .getRows().get(0);
		// ResponseBean1tu25.setName("[1,2.5)");
		// ResponseBean1tu25.setValue(bean1.getValue1to25() * 100);
		// ResponseBean25tu3.setName("[2.5,3)");
		// ResponseBean25tu3.setValue(bean1.getValue25to3() * 100);
		// ResponseBean3tu35.setName("[3,3.5)");
		// ResponseBean3tu35.setValue(bean1.getValue3to35() * 100);
		// ResponseBean35tu5.setName("[3.5,5)");
		// ResponseBean35tu5.setValue(bean1.getValue35to5() * 100);
		// }
		//
		// rows.add(ResponseBean1tu25);
		// rows.add(ResponseBean25tu3);
		// rows.add(ResponseBean3tu35);
		// rows.add(ResponseBean35tu5);
		// easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex2Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<StreamWholeResponseBean2> rows = new ArrayList<>();
		StreamWholeResponseBean2 streamWholeResponseBean2 = new StreamWholeResponseBean2();
		rows.add(streamWholeResponseBean2);
		easyuiPageList.setRows(rows);
		if (null == streamQualityBadsByLogIds
				|| 0 == streamQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * ????????????????????????
		 */
		streamWholeResponseBean2.setRoadNum(streamQualityBadsByLogIds.size());
		Integer vmosPointNum = 0;// ??????????????????:vmos???????????????
		Integer cellNum = 0;// ??????????????????:????????????
		Set<Long> cellIdSet = new HashSet<>();
		Set<String> boxIdSet = new HashSet<>();// ??????????????????:????????????,?????????boxid??????
		Float vmosPointTotalNum = null;// ??????????????????:vmos???????????????
		Float cellTotalNum = null;// ??????????????????:????????????
		Set<String> boxIdTotalSet = new HashSet<>();// ??????????????????:????????????,?????????boxid??????
		for (StreamQualityBad videoQualityBad : streamQualityBadsByLogIds) {
			/**
			 * ????????????????????????:vmos???????????????????????????
			 * 
			 */
			Integer vmosPointNum1 = videoQualityBad.getVmosNumber();
			Integer cellNum1 = videoQualityBad.getCellNum();
			if (null != vmosPointNum1) {
				vmosPointNum = (vmosPointNum == null ? vmosPointNum1
						: vmosPointNum + vmosPointNum1);
			}
			if (null != cellNum1) {
				cellNum = (cellNum == null ? cellNum1 : cellNum + cellNum1);
			}

			/**
			 * ????????????????????????:????????????,?????????boxid??????
			 */
			String boxId = videoQualityBad.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
		}
		ArrayList<Long> idsList = new ArrayList<Long>();
		for (TestLogItem testLog : queryTestLogItems) {
			/**
			 * ????????????????????????:mos???????????????????????????
			 * 
			 */

			Long testLogCellNum = testLog.getCellSumNum();

			if (null != testLogCellNum) {
				cellTotalNum = (cellTotalNum == null ? testLogCellNum
						: cellTotalNum + testLogCellNum);
			}
			/**
			 * ????????????????????????:????????????,?????????boxid??????
			 */
			String boxId = testLog.getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdTotalSet.add(boxId);
			}
			idsList.add(testLog.getRecSeqNo());
		}
		List queryList = streamQualityBadDao.queryList(idsList);
		if (queryList != null && queryList.size() != 0) {
			vmosPointTotalNum = Float
					.valueOf(String.valueOf(queryList.get(0) == null ? "0"
							: queryList.get(0))) / 1000;
		}
		// ???????????????????????????,????????????,??????????????????
		if (null != vmosPointTotalNum && vmosPointTotalNum != 0
				&& null != vmosPointNum) {
			streamWholeResponseBean2.setVmosPointNumRatio(NumberFormatUtils
					.format(vmosPointNum / vmosPointTotalNum * 100, 2));
		}
		// for (Long longs : cellIdSet) {
		// System.out.println(cellIdSet.size());
		// }
		if (null != cellTotalNum && cellTotalNum != 0) {
			streamWholeResponseBean2.setCellNumRatio(NumberFormatUtils.format(
					cellNum / cellTotalNum * 100f, 4));
		}
		if (0 != boxIdTotalSet.size()) {
			streamWholeResponseBean2.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex3Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		if (null == streamQualityBadsByLogIds
				|| 0 == streamQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		// ????????????????????????????????????
		StreamWholeResponseBean3 kartunRateBean3 = new StreamWholeResponseBean3();
		StreamWholeResponseBean3 initialbuffertimeBean3 = new StreamWholeResponseBean3();
		StreamWholeResponseBean3 videoresolutionBean3 = new StreamWholeResponseBean3();
		StreamWholeResponseBean3 otherBean3 = new StreamWholeResponseBean3();
		StreamWholeResponseBean3 collectRateBean3 = new StreamWholeResponseBean3();
		for (StreamQualityBad videoQualityBad : streamQualityBadsByLogIds) {
			switch (videoQualityBad.getCritialCause()) {
			case 0:// ????????????
				kartunRateBean3
						.setCollectNum(kartunRateBean3.getCollectNum() + 1);

				if (videoQualityBad instanceof StreamQualityBadPingPong) {
					kartunRateBean3.setPingPongNum(kartunRateBean3
							.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadNeighbourPlot) {
					kartunRateBean3.setAdjacentNum(kartunRateBean3
							.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadWeakCover) {
					kartunRateBean3.setWeakCoverNum(kartunRateBean3
							.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualtyBadDisturb) {
					kartunRateBean3.setDisturbNum(kartunRateBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOverCover) {
					kartunRateBean3.setOverCoverNum(kartunRateBean3
							.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOther) {
					kartunRateBean3
							.setOtherNum(kartunRateBean3.getOtherNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadDownDispatchSmall) {
					kartunRateBean3.setDownDispatchSmallNum(kartunRateBean3
							.getDownDispatchSmallNum() + 1);
				}

				break;
			case 1:// ??????????????????
				initialbuffertimeBean3.setCollectNum(initialbuffertimeBean3
						.getCollectNum() + 1);

				if (videoQualityBad instanceof StreamQualityBadPingPong) {
					initialbuffertimeBean3
							.setPingPongNum(initialbuffertimeBean3
									.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadNeighbourPlot) {
					initialbuffertimeBean3
							.setAdjacentNum(initialbuffertimeBean3
									.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadWeakCover) {
					initialbuffertimeBean3
							.setWeakCoverNum(initialbuffertimeBean3
									.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualtyBadDisturb) {
					initialbuffertimeBean3.setDisturbNum(initialbuffertimeBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOverCover) {
					initialbuffertimeBean3
							.setOverCoverNum(initialbuffertimeBean3
									.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOther) {
					initialbuffertimeBean3.setOtherNum(initialbuffertimeBean3
							.getOtherNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadDownDispatchSmall) {
					initialbuffertimeBean3
							.setDownDispatchSmallNum(initialbuffertimeBean3
									.getDownDispatchSmallNum() + 1);
				}

				break;
			case 2:// ????????????????????????
				videoresolutionBean3.setCollectNum(videoresolutionBean3
						.getCollectNum() + 1);

				if (videoQualityBad instanceof StreamQualityBadPingPong) {
					videoresolutionBean3.setPingPongNum(videoresolutionBean3
							.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadNeighbourPlot) {
					videoresolutionBean3.setAdjacentNum(videoresolutionBean3
							.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadWeakCover) {
					videoresolutionBean3.setWeakCoverNum(videoresolutionBean3
							.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualtyBadDisturb) {
					videoresolutionBean3.setDisturbNum(videoresolutionBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOverCover) {
					videoresolutionBean3.setOverCoverNum(videoresolutionBean3
							.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOther) {
					videoresolutionBean3.setOtherNum(videoresolutionBean3
							.getOtherNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadDownDispatchSmall) {
					videoresolutionBean3
							.setDownDispatchSmallNum(videoresolutionBean3
									.getDownDispatchSmallNum() + 1);
				}

				break;
			case -1:// ????????????
				otherBean3.setCollectNum(otherBean3.getCollectNum() + 1);

				if (videoQualityBad instanceof StreamQualityBadPingPong) {
					otherBean3.setPingPongNum(otherBean3.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadNeighbourPlot) {
					otherBean3.setAdjacentNum(otherBean3.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadWeakCover) {
					otherBean3
							.setWeakCoverNum(otherBean3.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualtyBadDisturb) {
					otherBean3.setDisturbNum(otherBean3.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOverCover) {
					otherBean3
							.setOverCoverNum(otherBean3.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadOther) {
					otherBean3.setOtherNum(otherBean3.getOtherNum() + 1);
				} else if (videoQualityBad instanceof StreamQualityBadDownDispatchSmall) {
					otherBean3.setDownDispatchSmallNum(otherBean3
							.getDownDispatchSmallNum() + 1);
				}
				break;

			default:
				break;
			}
			// ??????????????????

			if (videoQualityBad instanceof StreamQualityBadPingPong) {
				collectRateBean3.setPingPongNum(collectRateBean3
						.getPingPongNum() != null ? collectRateBean3
						.getPingPongNum() + 1 : 1);
			} else if (videoQualityBad instanceof StreamQualityBadNeighbourPlot) {
				collectRateBean3.setAdjacentNum(collectRateBean3
						.getAdjacentNum() != null ? collectRateBean3
						.getAdjacentNum() + 1 : 1);
			} else if (videoQualityBad instanceof StreamQualityBadWeakCover) {
				collectRateBean3.setWeakCoverNum(collectRateBean3
						.getWeakCoverNum() != null ? collectRateBean3
						.getWeakCoverNum() + 1 : 1);
			} else if (videoQualityBad instanceof StreamQualtyBadDisturb) {
				collectRateBean3
						.setDisturbNum(collectRateBean3.getDisturbNum() != null ? collectRateBean3
								.getDisturbNum() + 1 : 1);
			} else if (videoQualityBad instanceof StreamQualityBadOverCover) {
				collectRateBean3.setOverCoverNum(collectRateBean3
						.getOverCoverNum() != null ? collectRateBean3
						.getOverCoverNum() + 1 : 1);
			} else if (videoQualityBad instanceof StreamQualityBadOther) {
				collectRateBean3
						.setOtherNum(collectRateBean3.getOtherNum() != null ? collectRateBean3
								.getOtherNum() + 1 : 1);
			} else if (videoQualityBad instanceof StreamQualityBadDownDispatchSmall) {
				collectRateBean3.setDownDispatchSmallNum(collectRateBean3
						.getDownDispatchSmallNum() != null ? collectRateBean3
						.getDownDispatchSmallNum() + 1 : 1);
			}
			collectRateBean3
					.setCollectNum(collectRateBean3.getCollectNum() + 1);
		}

		List<StreamWholeResponseBean3> rows = new ArrayList<>();
		// ????????????
		// ????????????????????????????????????
		if (kartunRateBean3.getCollectNum() != null
				&& kartunRateBean3.getCollectNum() > 0) {
			kartunRateBean3
					.setPingPongRatio(NumberFormatUtils.format(
							(kartunRateBean3.getPingPongNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getPingPongNum())
									/ Float.valueOf(kartunRateBean3
											.getCollectNum())) * 100, 2));
			kartunRateBean3
					.setAdjacentRatio(NumberFormatUtils.format(
							(kartunRateBean3.getAdjacentNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getAdjacentNum())
									/ Float.valueOf(kartunRateBean3
											.getCollectNum())) * 100, 2));
			kartunRateBean3
					.setWeakCoverRatio(NumberFormatUtils.format(
							(kartunRateBean3.getWeakCoverNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getWeakCoverNum())
									/ Float.valueOf(kartunRateBean3
											.getCollectNum())) * 100, 2));
			kartunRateBean3
					.setDisturbRatio(NumberFormatUtils.format(
							(kartunRateBean3.getDisturbNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getDisturbNum())
									/ Float.valueOf(kartunRateBean3
											.getCollectNum())) * 100, 2));
			kartunRateBean3
					.setOverCoverRatio(NumberFormatUtils.format(
							(kartunRateBean3.getOverCoverNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getOverCoverNum())
									/ Float.valueOf(kartunRateBean3
											.getCollectNum())) * 100, 2));

			kartunRateBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(kartunRateBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(kartunRateBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(kartunRateBean3
													.getCollectNum())) * 100, 2));
			kartunRateBean3
					.setOtherRatio(NumberFormatUtils.format(
							(kartunRateBean3.getOtherNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getOtherNum())
									/ Float.valueOf(kartunRateBean3
											.getCollectNum())) * 100, 2));
			kartunRateBean3
					.setCollectRatio(NumberFormatUtils.format(
							(kartunRateBean3.getCollectNum() == 0 ? 0 : Float
									.valueOf(kartunRateBean3.getCollectNum())
									/ Float.valueOf(streamQualityBadsByLogIds
											.size())) * 100, 2));

		}
		kartunRateBean3.setCause("????????????");
		// ??????????????????????????????????????????
		if (initialbuffertimeBean3.getCollectNum() != null
				&& initialbuffertimeBean3.getCollectNum() > 0) {
			initialbuffertimeBean3.setPingPongRatio(NumberFormatUtils.format(
					(initialbuffertimeBean3.getPingPongNum() == 0 ? 0 : Float
							.valueOf(initialbuffertimeBean3.getPingPongNum())
							/ Float.valueOf(initialbuffertimeBean3
									.getCollectNum())) * 100, 2));
			initialbuffertimeBean3.setAdjacentRatio(NumberFormatUtils.format(
					(initialbuffertimeBean3.getAdjacentNum() == 0 ? 0 : Float
							.valueOf(initialbuffertimeBean3.getAdjacentNum())
							/ Float.valueOf(initialbuffertimeBean3
									.getCollectNum())) * 100, 2));
			initialbuffertimeBean3.setWeakCoverRatio(NumberFormatUtils.format(
					(initialbuffertimeBean3.getWeakCoverNum() == 0 ? 0 : Float
							.valueOf(initialbuffertimeBean3.getWeakCoverNum())
							/ Float.valueOf(initialbuffertimeBean3
									.getCollectNum())) * 100, 2));
			initialbuffertimeBean3.setDisturbRatio(NumberFormatUtils.format(
					(initialbuffertimeBean3.getDisturbNum() == 0 ? 0 : Float
							.valueOf(initialbuffertimeBean3.getDisturbNum())
							/ Float.valueOf(initialbuffertimeBean3
									.getCollectNum())) * 100, 2));
			initialbuffertimeBean3.setOverCoverRatio(NumberFormatUtils.format(
					(initialbuffertimeBean3.getOverCoverNum() == 0 ? 0 : Float
							.valueOf(initialbuffertimeBean3.getOverCoverNum())
							/ Float.valueOf(initialbuffertimeBean3
									.getCollectNum())) * 100, 2));

			initialbuffertimeBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(initialbuffertimeBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(initialbuffertimeBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(initialbuffertimeBean3
													.getCollectNum())) * 100, 2));
			initialbuffertimeBean3.setOtherRatio(NumberFormatUtils.format(
					(initialbuffertimeBean3.getOtherNum() == 0 ? 0 : Float
							.valueOf(initialbuffertimeBean3.getOtherNum())
							/ Float.valueOf(initialbuffertimeBean3
									.getCollectNum())) * 100, 2));
			initialbuffertimeBean3
					.setCollectRatio(NumberFormatUtils.format(
							(initialbuffertimeBean3.getCollectNum() == null ? 0
									: Float.valueOf(initialbuffertimeBean3
											.getCollectNum())
											/ Float.valueOf(streamQualityBadsByLogIds
													.size())) * 100, 2));
		}
		initialbuffertimeBean3.setCause("??????????????????");
		// ????????????????????????????????????????????????
		if (videoresolutionBean3.getCollectNum() != null
				&& videoresolutionBean3.getCollectNum() > 0) {
			videoresolutionBean3.setPingPongRatio(NumberFormatUtils.format(
					(videoresolutionBean3.getPingPongNum() == 0 ? 0 : Float
							.valueOf(videoresolutionBean3.getPingPongNum())
							/ Float.valueOf(videoresolutionBean3
									.getCollectNum())) * 100, 2));
			videoresolutionBean3.setAdjacentRatio(NumberFormatUtils.format(
					(videoresolutionBean3.getAdjacentNum() == 0 ? 0 : Float
							.valueOf(videoresolutionBean3.getAdjacentNum())
							/ Float.valueOf(videoresolutionBean3
									.getCollectNum())) * 100, 2));
			videoresolutionBean3.setWeakCoverRatio(NumberFormatUtils.format(
					(videoresolutionBean3.getWeakCoverNum() == 0 ? 0 : Float
							.valueOf(videoresolutionBean3.getWeakCoverNum())
							/ Float.valueOf(videoresolutionBean3
									.getCollectNum())) * 100, 2));
			videoresolutionBean3.setDisturbRatio(NumberFormatUtils.format(
					(videoresolutionBean3.getDisturbNum() == 0 ? 0 : Float
							.valueOf(videoresolutionBean3.getDisturbNum())
							/ Float.valueOf(videoresolutionBean3
									.getCollectNum())) * 100, 2));
			videoresolutionBean3.setOverCoverRatio(NumberFormatUtils.format(
					(videoresolutionBean3.getOverCoverNum() == 0 ? 0 : Float
							.valueOf(videoresolutionBean3.getOverCoverNum())
							/ Float.valueOf(videoresolutionBean3
									.getCollectNum())) * 100, 2));

			videoresolutionBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(videoresolutionBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(videoresolutionBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(videoresolutionBean3
													.getCollectNum())) * 100, 2));
			videoresolutionBean3.setOtherRatio(NumberFormatUtils.format(
					(videoresolutionBean3.getOtherNum() == 0 ? 0 : Float
							.valueOf(videoresolutionBean3.getOtherNum())
							/ Float.valueOf(videoresolutionBean3
									.getCollectNum())) * 100, 2));
			videoresolutionBean3
					.setCollectRatio(NumberFormatUtils.format(
							(videoresolutionBean3.getCollectNum() == 0 ? 0
									: Float.valueOf(videoresolutionBean3
											.getCollectNum())
											/ Float.valueOf(streamQualityBadsByLogIds
													.size())) * 100, 2));
		}
		videoresolutionBean3.setCause("??????????????????");
		// ??????????????????????????????
		if (otherBean3.getCollectNum() != null
				&& otherBean3.getCollectNum() > 0) {
			otherBean3.setPingPongRatio(NumberFormatUtils.format(
					(otherBean3.getPingPongNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getPingPongNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));
			otherBean3.setAdjacentRatio(NumberFormatUtils.format(
					(otherBean3.getAdjacentNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getAdjacentNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));
			otherBean3.setWeakCoverRatio(NumberFormatUtils.format(
					(otherBean3.getWeakCoverNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getWeakCoverNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));
			otherBean3.setDisturbRatio(NumberFormatUtils.format(
					(otherBean3.getDisturbNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getDisturbNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));
			otherBean3.setOverCoverRatio(NumberFormatUtils.format(
					(otherBean3.getOverCoverNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getOverCoverNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));

			otherBean3.setDownDispatchSmallRatio(NumberFormatUtils.format(
					(otherBean3.getDownDispatchSmallNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getDownDispatchSmallNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));
			otherBean3.setOtherRatio(NumberFormatUtils.format(
					(otherBean3.getOtherNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getOtherNum())
							/ Float.valueOf(otherBean3.getCollectNum())) * 100,
					2));
			otherBean3
					.setCollectRatio(NumberFormatUtils.format(
							(otherBean3.getCollectNum() == 0 ? 0 : Float
									.valueOf(otherBean3.getCollectNum())
									/ Float.valueOf(streamQualityBadsByLogIds
											.size())) * 100, 2));
		}
		otherBean3.setCause("????????????");
		// ??????????????????????????????
		if (collectRateBean3.getCollectNum() != null
				&& collectRateBean3.getCollectNum() > 0) {
			collectRateBean3
					.setPingPongRatio(NumberFormatUtils.format(
							(collectRateBean3.getPingPongNum() == 0 ? 0 : Float
									.valueOf(collectRateBean3.getPingPongNum())
									/ Float.valueOf(collectRateBean3
											.getCollectNum())) * 100, 2));
			collectRateBean3
					.setAdjacentRatio(NumberFormatUtils.format(
							(collectRateBean3.getAdjacentNum() == 0 ? 0 : Float
									.valueOf(collectRateBean3.getAdjacentNum())
									/ Float.valueOf(collectRateBean3
											.getCollectNum())) * 100, 2));
			collectRateBean3
					.setWeakCoverRatio(NumberFormatUtils.format(
							(collectRateBean3.getWeakCoverNum() == 0 ? 0
									: Float.valueOf(collectRateBean3
											.getWeakCoverNum())
											/ Float.valueOf(collectRateBean3
													.getCollectNum())) * 100, 2));
			collectRateBean3
					.setDisturbRatio(NumberFormatUtils.format(
							(collectRateBean3.getDisturbNum() == 0 ? 0 : Float
									.valueOf(collectRateBean3.getDisturbNum())
									/ Float.valueOf(collectRateBean3
											.getCollectNum())) * 100, 2));
			collectRateBean3
					.setOverCoverRatio(NumberFormatUtils.format(
							(collectRateBean3.getOverCoverNum() == 0 ? 0
									: Float.valueOf(collectRateBean3
											.getOverCoverNum())
											/ Float.valueOf(collectRateBean3
													.getCollectNum())) * 100, 2));

			collectRateBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(collectRateBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(collectRateBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(collectRateBean3
													.getCollectNum())) * 100, 2));
			collectRateBean3
					.setOtherRatio(NumberFormatUtils.format(
							(collectRateBean3.getOtherNum() == 0 ? 0 : Float
									.valueOf(collectRateBean3.getOtherNum())
									/ Float.valueOf(collectRateBean3
											.getCollectNum())) * 100, 2));
			collectRateBean3
					.setCollectRatio(NumberFormatUtils.format(
							(collectRateBean3.getCollectNum() == 0 ? 0 : Float
									.valueOf(collectRateBean3.getCollectNum())
									/ Float.valueOf(streamQualityBadsByLogIds
											.size())) * 100, 2));
		}
		collectRateBean3.setCause("??????");
		rows.add(kartunRateBean3);
		rows.add(initialbuffertimeBean3);
		rows.add(videoresolutionBean3);
		rows.add(otherBean3);
		rows.add(collectRateBean3);
		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex4Analysis(
			List<StreamQualityBad> streamQualityBadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		if (null == streamQualityBadsByLogIds
				|| 0 == streamQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		// ????????????????????????
		StreamWholeResponseBean4 pingPongBean4 = new StreamWholeResponseBean4();
		StreamWholeResponseBean4 adjacentBean4 = new StreamWholeResponseBean4();
		StreamWholeResponseBean4 weakCoverBean4 = new StreamWholeResponseBean4();
		StreamWholeResponseBean4 disturbBean4 = new StreamWholeResponseBean4();
		StreamWholeResponseBean4 overCoverBean4 = new StreamWholeResponseBean4();
		StreamWholeResponseBean4 downDispatchSmallBean4 = new StreamWholeResponseBean4();
		StreamWholeResponseBean4 otherBean4 = new StreamWholeResponseBean4();
		EasyuiPageList doWholeIndex3Analysis = doWholeIndex3Analysis(streamQualityBadsByLogIds);
		List<StreamWholeResponseBean3> rows = (List<StreamWholeResponseBean3>) doWholeIndex3Analysis
				.getRows();
		if (rows.size() != 0) {
			for (StreamWholeResponseBean3 object : rows) {
				if (object.getCause() != null) {
					switch (object.getCause()) {
					case "????????????":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "??????????????????":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "??????????????????":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "????????????":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;

					default:
						break;
					}
				}
			}
		}
		pingPongBean4.setName("????????????");
		adjacentBean4.setName("????????????");
		weakCoverBean4.setName("?????????");
		disturbBean4.setName("??????");
		overCoverBean4.setName("????????????");
		downDispatchSmallBean4.setName("??????????????????");
		otherBean4.setName("??????");
		List<StreamWholeResponseBean4> rows2 = new ArrayList<>();
		rows2.add(pingPongBean4);
		rows2.add(adjacentBean4);
		rows2.add(weakCoverBean4);
		rows2.add(disturbBean4);
		rows2.add(overCoverBean4);
		rows2.add(downDispatchSmallBean4);
		rows2.add(otherBean4);
		easyuiPageList.setRows(rows2);
		return easyuiPageList;
	}

	@Override
	public List<StreamQualityBadWeakCover> getStreamWeakCoversByLogIds(
			List<Long> testLogItemIds) {

		return streamWeakCoverDao.queryStreamWeakCoversByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualityBadWeakCover getStreamWeakCoverById(Long id) {

		return streamWeakCoverDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamWeakCoverAnalysis(
			StreamQualityBadWeakCover streamQualityBadWeakCover) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("weakCoverAdvice",
				doWeakCoverAdviceAnalysis(streamQualityBadWeakCover));
		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		ArrayList<StreamQualityBadWeakCover> arrayList2 = new ArrayList<>();
		arrayList2.add(streamQualityBadWeakCover);
		easyuiPageList2.setRows(arrayList2);
		map.put("weakCoverKey", easyuiPageList2);
		return map;
	}

	@Override
	public EasyuiPageList doWeakCoverAdviceAnalysis(
			StreamQualityBadWeakCover streamQualityBadWeakCover) {
		if (null == streamQualityBadWeakCover
				|| null == streamQualityBadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(streamWeakCoverAdviceDao
				.queryStreamWeakCoverAdvice(streamQualityBadWeakCover));
		return easyuiPageList;
	}

	@Override
	public List<StreamQualtyBadDisturb> getStreamDisturbsByLogIds(
			List<Long> testLogItemIds) {

		return streamDisturbDao.queryStreamDisturbsByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualtyBadDisturb getStreamDisturbById(Long roadId) {

		return streamDisturbDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamDisturbAnalysis(
			StreamQualtyBadDisturb streamDisturbById) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("disturbAdvice", doDisturbAdviceAnalysis(streamDisturbById));
		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		ArrayList<StreamQualtyBadDisturb> arrayList2 = new ArrayList<>();
		arrayList2.add(streamDisturbById);
		easyuiPageList2.setRows(arrayList2);
		map.put("disturbKey", easyuiPageList2);
		return map;
	}

	@Override
	public EasyuiPageList doDisturbAdviceAnalysis(
			StreamQualtyBadDisturb streamDisturbById) {
		if (null == streamDisturbById || null == streamDisturbById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(streamDisturbAdviceDao
				.queryStreamDisturbAdvice(streamDisturbById));
		return easyuiPageList;
	}

	@Override
	public List<StreamQualityBadNeighbourPlot> getStreamNeighbourPlotsByLogIds(
			List<Long> testLogItemIds) {

		return streamNeighbourPlotDao
				.queryStreamNeighbourPlotsByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualityBadNeighbourPlot getStreamNeighbourPlotById(Long roadId) {

		return streamNeighbourPlotDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamNeighbourPlotAnalysis(
			StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("neighbourPlotAdvice",
				doNeighbourPlotAdviceAnalysis(streamQualityBadNeighbourPlot));
		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		ArrayList<StreamQualityBadNeighbourPlot> arrayList2 = new ArrayList<>();
		arrayList2.add(streamQualityBadNeighbourPlot);
		easyuiPageList2.setRows(arrayList2);
		map.put("neighbourPlotKey", easyuiPageList2);
		return map;
	}

	@Override
	public EasyuiPageList doNeighbourPlotAdviceAnalysis(
			StreamQualityBadNeighbourPlot streamQualityBadNeighbourPlot) {
		if (null == streamQualityBadNeighbourPlot
				|| null == streamQualityBadNeighbourPlot.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(streamNeighbourAdviceDao
				.queryStreamNeighbourAdvice(streamQualityBadNeighbourPlot));
		return easyuiPageList;
	}

	@Override
	public List<StreamQualityBadOverCover> getStreamOverCoversByLogIds(
			List<Long> testLogItemIds) {

		return streamOverCoverDao.queryStreamOverCoversByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualityBadOverCover getStreamOverCoverById(Long roadId) {

		return streamOverCoverDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamOverCoverAnalysis(
			StreamQualityBadOverCover streamQualityBadOverCover) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("overCoverAdvice",
				doOverCoverAdviceAnalysis(streamQualityBadOverCover));
		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		ArrayList<StreamQualityBadOverCover> arrayList2 = new ArrayList<>();
		arrayList2.add(streamQualityBadOverCover);
		easyuiPageList2.setRows(arrayList2);
		map.put("overCoverKey", easyuiPageList2);
		return map;
	}

	@Override
	public EasyuiPageList doOverCoverAdviceAnalysis(
			StreamQualityBadOverCover streamQualityBadOverCover) {
		if (null == streamQualityBadOverCover
				|| null == streamQualityBadOverCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(streamOverCoverAdviceDao
				.queryStreamOverCoverAdvice(streamQualityBadOverCover));
		return easyuiPageList;
	}

	@Override
	public List<StreamQualityBadPingPong> getStreamPingPongsByLogIds(
			List<Long> testLogItemIds) {

		return streamPingPongDao.queryStreamPingPongsByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualityBadPingPong getStreamPingPongById(Long id) {

		return streamPingPongDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamPingPongAnalysis(
			StreamQualityBadPingPong streamQualityBadPingPong) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		ArrayList<Long> arrayList = new ArrayList<>();
		EasyuiPageList doPingPongAdviceAnalysis = doPingPongAdviceAnalysis(streamQualityBadPingPong);
		map.put("pingPongAdvice", doPingPongAdviceAnalysis);
		if (doPingPongAdviceAnalysis.getRows() != null
				&& doPingPongAdviceAnalysis.getRows().size() != 0) {
			for (Object object : doPingPongAdviceAnalysis.getRows()) {
				StreamPingPongAdvice pongAdjustCutParameter = (StreamPingPongAdvice) object;
				arrayList.add(pongAdjustCutParameter.getCellId());
			}
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(arrayList);
		map.put("cellIdList", easyuiPageList);
		map.put("pingPongCutEvent",
				doPingPongCutEventAnalysis(streamQualityBadPingPong));
		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		ArrayList<StreamQualityBadPingPong> arrayList2 = new ArrayList<>();
		arrayList2.add(streamQualityBadPingPong);
		easyuiPageList2.setRows(arrayList2);
		map.put("pingPongKey", easyuiPageList2);
		return map;
	}

	@Override
	public EasyuiPageList doPingPongAdviceAnalysis(
			StreamQualityBadPingPong streamQualityBadPingPong) {
		if (null == streamQualityBadPingPong
				|| null == streamQualityBadPingPong.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(streamPingPongAdviceDao
				.queryStreamPingPongAdvice(streamQualityBadPingPong));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doPingPongCutEventAnalysis(
			StreamQualityBadPingPong streamQualityBadPingPong) {
		if (null == streamQualityBadPingPong
				|| null == streamQualityBadPingPong.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		// List<PingPongCutEvent> queryPingPongCutEvent = pingPongCutEventDao
		// .queryPingPongCutEvent(videoQualityBadPingPong);
		// // ??????
		// if (queryPingPongCutEvent != null && queryPingPongCutEvent.size() !=
		// 0) {
		// HashMap<String, PingPongCutEvent> map = new HashMap<>();
		// for (PingPongCutEvent pingPongCutEvent : queryPingPongCutEvent) {
		// map.put(pingPongCutEvent.getCutTime() + ","
		// + pingPongCutEvent.getDstPci() + ","
		// + pingPongCutEvent.getSrcPci() + ","
		// + pingPongCutEvent.getEventName(), pingPongCutEvent);
		// }
		// Collection<PingPongCutEvent> values = map.values();
		// List<PingPongCutEvent> list = new ArrayList<>();
		// for (PingPongCutEvent pingPongCutEvent : values) {
		// list.add(pingPongCutEvent);
		// }
		// // ???????????????
		// Collections.sort(list, new Comparator<PingPongCutEvent>() {
		// public int compare(PingPongCutEvent o1, PingPongCutEvent o2) {
		// if (o1.getCutTime() > o2.getCutTime()) {
		// return 1;
		// }
		// if (o1.getCutTime() == o2.getCutTime()) {
		// return 0;
		// }
		// return -1;
		// }
		// });
		//
		// easyuiPageList.setRows(list);
		easyuiPageList.setRows(streamPingPongCutEventDao
				.queryPingPongCutEvent(streamQualityBadPingPong));
		// }

		return easyuiPageList;
	}

	@Override
	public List<StreamQualityBadOther> getStreamOthersByLogIds(
			List<Long> testLogItemIds) {

		return streamOtherDao.queryStreamOthersByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualityBadOther getStreamOtherById(Long roadId) {

		return streamOtherDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamOtherAnalysis(
			StreamQualityBadOther streamQualityBadOther) {

		Map<String, EasyuiPageList> map = new HashMap<>();
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.getRows().add(streamQualityBadOther);
		map.put("otherKey", easyuiPageList);

		return map;
	}

	@Override
	public List<StreamQualityBadDownDispatchSmall> getStreamDownDispatchSmallsByLogIds(
			List<Long> testLogItemIds) {

		return streamDownDispatchSmallDao
				.queryStreamDownDispatchSmallsByLogIds(testLogItemIds);
	}

	@Override
	public StreamQualityBadDownDispatchSmall getStreamDownDispatchSmallById(
			Long id) {

		return streamDownDispatchSmallDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doStreamDownDispatchSmallAnalysis(
			StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("downDispatchSmallAdvice",
				doDownDispatchSmallAdviceAnalysis(streamQualityBadDownDispatchSmall));
		EasyuiPageList easyuiPageList2 = new EasyuiPageList();
		ArrayList<StreamQualityBadDownDispatchSmall> arrayList2 = new ArrayList<>();
		arrayList2.add(streamQualityBadDownDispatchSmall);
		easyuiPageList2.setRows(arrayList2);
		map.put("downDispatchSmallKey", easyuiPageList2);
		return map;
	}

	@Override
	public EasyuiPageList doDownDispatchSmallAdviceAnalysis(
			StreamQualityBadDownDispatchSmall streamQualityBadDownDispatchSmall) {
		if (null == streamQualityBadDownDispatchSmall
				|| null == streamQualityBadDownDispatchSmall.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList
				.setRows(streamDownDispatchSmallAdviceDao
						.queryStreamDownDispatchSmallAdvice(streamQualityBadDownDispatchSmall));
		return easyuiPageList;
	}

	// @Override
	// public EasyuiPageList doDownDispatchSmallKeyIndexAnalysis(
	// VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall) {
	// EasyuiPageList easyuiPageList = new EasyuiPageList();
	// List<VideoWholeResponseBean0> rows = new ArrayList<>();
	// VideoWholeResponseBean0 wholeIndexResponseBean0 = new
	// VideoWholeResponseBean0();
	// rows.add(wholeIndexResponseBean0);
	// easyuiPageList.setRows(rows);
	//
	// /**
	// * ????????????????????????????????????
	// */
	// Float videoBitRate = videoQualityBadDownDispatchSmall.getVideoBitRate();
	// if (null != videoBitRate && videoBitRate != 0) {
	// wholeIndexResponseBean0.setVideoBitRate(NumberFormatUtils.format(
	// videoBitRate, 2));
	// }
	// /**
	// * ????????????????????????????????????
	// */
	// Float videoFrameRate = videoQualityBadDownDispatchSmall
	// .getVideoFrameRate();
	// if (null != videoFrameRate && videoFrameRate != 0) {
	// wholeIndexResponseBean0.setVideoFrameRate(NumberFormatUtils.format(
	// videoFrameRate, 2));
	// }
	// /**
	// * ???????????????????????????????????????
	// */
	// Long audioPacketNum = videoQualityBadDownDispatchSmall
	// .getAudioPacketNum();
	// Long audioPacketlossNum = videoQualityBadDownDispatchSmall
	// .getAudioPacketlossNum();
	// Long videoPacketNum = videoQualityBadDownDispatchSmall
	// .getVideoPacketNum();
	// Long videoPacketlossNum = videoQualityBadDownDispatchSmall
	// .getVideoPacketlossNum();
	// Long packetNum = audioPacketNum + videoPacketNum;
	// Long packetlossNum = audioPacketlossNum + videoPacketlossNum;
	// if (null != packetNum && packetNum != 0 && null != packetlossNum
	// && packetlossNum != 0) {
	// wholeIndexResponseBean0.setPackLossRate(NumberFormatUtils.format(
	// packetlossNum * 100f / packetNum, 4));
	// }
	// /**
	// * ??????????????????i_RTT??????
	// */
	// Float irtt = videoQualityBadDownDispatchSmall.getIrtt();
	// if (null != irtt && irtt != 0) {
	// wholeIndexResponseBean0.setIrtt(NumberFormatUtils.format(irtt, 2));
	// }
	// /**
	// * ????????????????????????????????????
	// */
	// Float audioBitRate = videoQualityBadDownDispatchSmall.getAudioBitRate();
	// if (null != audioBitRate && audioBitRate != 0) {
	// wholeIndexResponseBean0.setAudioBitRate(NumberFormatUtils.format(
	// audioBitRate, 2));
	// }
	//
	// return easyuiPageList;
	// }

	@Override
	public LteCell queryLteCellInfoByCellId(Long cellId) {

		return lteCellDao.queryLteCellByCellId(cellId);
	}

	@Override
	public List<StreamStatementeResponseBean> getDownloadInfo(
			List<StreamQualityBad> videoQualityBads) {
		List<StreamStatementeResponseBean> videoOthersByLogIds = new ArrayList<>();
		if (null != videoQualityBads && videoQualityBads.size() != 0) {
			for (StreamQualityBad videoQualityBad : videoQualityBads) {
				StreamStatementeResponseBean videoStatementeResponseBean = new StreamStatementeResponseBean();
				videoStatementeResponseBean.setBoxId(videoQualityBad
						.getTestLogItem().getBoxId());
				videoStatementeResponseBean.setEvenName("?????????vMOS??????");
				if (videoQualityBad.getCritialCause() != null) {
					switch (videoQualityBad.getCritialCause()) {
					case 0:
						videoStatementeResponseBean.setReason("????????????");
						break;
					case 1:
						videoStatementeResponseBean.setReason("??????????????????");
						break;
					case 2:
						videoStatementeResponseBean.setReason("??????????????????");
						break;
					case -1:
						videoStatementeResponseBean.setReason("??????");
						break;

					default:
						break;
					}
				}
				if (videoQualityBad instanceof StreamQualityBadPingPong) {
					videoStatementeResponseBean.setReasonWireless("????????????");
					List<StreamPingPongAdvice> queryPingPongAdjustCutParameter = streamPingPongAdviceDao
							.queryStreamPingPongAdvice((StreamQualityBadPingPong) videoQualityBad);
					StreamPingPongAdvice streamPingPongAdvice = queryPingPongAdjustCutParameter
							.get(0);
					if (streamPingPongAdvice.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamPingPongAdvice
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					if (queryPingPongAdjustCutParameter != null
							&& queryPingPongAdjustCutParameter.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("??????<"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellId()
								+ ">,<"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellPci()
								+ ">),??????"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellEarfcn()
								+ ",RSRP:"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellRsrp()
								+ ",????????????????????????(m):"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellToProblemDotDistance());
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellId()
								+ ">,<"
								+ queryPingPongAdjustCutParameter.get(0)
										.getCellPci() + ">),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("??????<>??????(<>,<>),??????"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,????????????????????????(m):");
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<>??????(<>,<>),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					}

				} else if (videoQualityBad instanceof StreamQualityBadNeighbourPlot) {
					videoStatementeResponseBean.setReasonWireless("????????????");
					List<StreamNeighbourAdvice> queryNeighbourPlotAddNeighbourPlot = streamNeighbourAdviceDao
							.queryStreamNeighbourAdvice((StreamQualityBadNeighbourPlot) videoQualityBad);
					StreamNeighbourAdvice streamPingPongAdvice = queryNeighbourPlotAddNeighbourPlot
							.get(0);
					if (streamPingPongAdvice.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamPingPongAdvice
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					if (queryNeighbourPlotAddNeighbourPlot != null
							&& queryNeighbourPlotAddNeighbourPlot.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("??????<"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getCellId()
								+ ">,<"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getPci()
								+ ">),??????"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getRsrp()
								+ ",????????????????????????(m):"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getToProblemDotDistance());
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getCellId()
								+ ">,<"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getPci() + ">),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("??????<>??????(<>,<>),??????"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,????????????????????????(m):");
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<>??????(<>,<>),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					}

				} else if (videoQualityBad instanceof StreamQualityBadWeakCover) {
					videoStatementeResponseBean.setReasonWireless("?????????");
					List<StreamWeakCoverAdvice> queryWeakCoverAdjustAntennaFeeder = streamWeakCoverAdviceDao
							.queryStreamWeakCoverAdvice((StreamQualityBadWeakCover) videoQualityBad);
					StreamWeakCoverAdvice streamPingPongAdvice = queryWeakCoverAdjustAntennaFeeder
							.get(0);
					if (streamPingPongAdvice.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamPingPongAdvice
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					if (queryWeakCoverAdjustAntennaFeeder != null
							&& queryWeakCoverAdjustAntennaFeeder.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("??????<"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getCellId()
								+ ">,<"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getPci()
								+ ">),??????"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getRsrp()
								+ ",????????????????????????(m):"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getToProblemDotDistance()
								+ ",????????????(m):"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getProjectHeight()
								+ ",???????????????:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getProjectDowndipAngle()
								+ ",???????????????:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getProjectAzimuth());
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getCellId()
								+ ">,<"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getPci() + ">),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("??????<>??????(<>,<>),??????"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,????????????????????????(m):,????????????(m):,???????????????:,???????????????:");
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<>??????(<>,<>),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					}

				} else if (videoQualityBad instanceof StreamQualtyBadDisturb) {
					videoStatementeResponseBean.setReasonWireless("??????");
					List<StreamDisturbAdvice> queryDisturbPciOrSignalStrengthAdjust = streamDisturbAdviceDao
							.queryStreamDisturbAdvice((StreamQualtyBadDisturb) videoQualityBad);
					StreamDisturbAdvice streamPingPongAdvice = queryDisturbPciOrSignalStrengthAdjust
							.get(0);
					if (streamPingPongAdvice.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamPingPongAdvice
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					if (queryDisturbPciOrSignalStrengthAdjust != null
							&& queryDisturbPciOrSignalStrengthAdjust.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("??????<"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getCellId()
								+ ">,<"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getPci()
								+ ">),??????"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getRsrp()
								+ ",????????????????????????(m)"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getToProblemDotDistance());
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getCellName()
								+ ">??????(<"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getCellId()
								+ ">,<"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getPci() + ">),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("??????<>??????(<>,<>),??????"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,????????????????????????(m):");
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<>??????(<>,<>),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					}

				} else if (videoQualityBad instanceof StreamQualityBadOverCover) {
					videoStatementeResponseBean.setReasonWireless("????????????");
					List<StreamOverCoverAdvice> queryOverCoverAzimuthOrDowndipAngleAdjust = streamOverCoverAdviceDao
							.queryStreamOverCoverAdvice((StreamQualityBadOverCover) videoQualityBad);
					StreamOverCoverAdvice streamPingPongAdvice = queryOverCoverAzimuthOrDowndipAngleAdjust
							.get(0);
					if (streamPingPongAdvice.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamPingPongAdvice
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					if (queryOverCoverAzimuthOrDowndipAngleAdjust != null
							&& queryOverCoverAzimuthOrDowndipAngleAdjust.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("??????<"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getCellName()
								+ ">??????(<"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getCellId()
								+ ">,<"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getPci()
								+ ">),??????"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getEarfcn()
								+ ",RSRP:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getRsrp()
								+ ",????????????????????????(m)"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getToProblemDotDistance()
								+ ",???????????????:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getProjectDowndipAngle()
								+ ",???????????????:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getProjectAzimuth());
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getCellName()
								+ ">??????(<"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getCellId()
								+ ">,<"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getPci() + ">),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("??????<>??????(<>,<>),??????"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,????????????????????????(m):,???????????????:,???????????????:");
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<>??????(<>,<>),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					}

				} else if (videoQualityBad instanceof StreamQualityBadOther) {
					StreamQualityBadOther streamQualityBadOther = (StreamQualityBadOther) videoQualityBad;
					if (streamQualityBadOther.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamQualityBadOther
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					videoStatementeResponseBean.setOptimizationInfo("??????<"
							+ streamQualityBadOther.getCellName() + ">??????(<"
							+ streamQualityBadOther.getCellId() + ">,<>),??????"
							+ videoQualityBad.getOptimization()
							+ ",EARFCN:,RSRP:,????????????????????????(m):");
					videoStatementeResponseBean.setCellInfo("???<"
							+ videoStatementeResponseBean.getRoadName()
							+ ">????????????<" + streamQualityBadOther.getCellName()
							+ ">??????(<" + streamQualityBadOther.getCellId()
							+ ">,<>),?????????" + videoQualityBad.getRsrpAvg()
							+ "dbm,SINR???<" + videoQualityBad.getSinrAvg()
							+ ">db,??????????????????:" + "????????????"
							+ videoQualityBad.getVideobitrateAvg() + ",???????????????"
							+ videoQualityBad.getStallingratioAvg()
							+ ",?????????????????????"
							+ videoQualityBad.getInitialbuffertimeAvg()
							+ ",???????????????????????????"
							+ videoQualityBad.getVideoresolutionAvg());
				} else if (videoQualityBad instanceof StreamQualityBadDownDispatchSmall) {
					videoStatementeResponseBean.setReasonWireless("???????????????");
					List<StreamDownDispatchSmallAdvice> queryPingPongCutEvent = streamDownDispatchSmallAdviceDao
							.queryStreamDownDispatchSmallAdvice((StreamQualityBadDownDispatchSmall) videoQualityBad);
					StreamDownDispatchSmallAdvice streamPingPongAdvice = queryPingPongCutEvent
							.get(0);
					if (streamPingPongAdvice.getCellId() != null) {
						LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(streamPingPongAdvice
								.getCellId());
						if (queryLteCellInfoByCellId != null) {
							videoStatementeResponseBean
									.setRegion(queryLteCellInfoByCellId
											.getRegion());
							videoStatementeResponseBean
									.setVender(queryLteCellInfoByCellId
											.getVender());
							videoStatementeResponseBean
									.setCoverScene(queryLteCellInfoByCellId
											.getCoverScene());
							videoStatementeResponseBean
									.setIsBelongtoNetwork(queryLteCellInfoByCellId
											.getIsBelongtoNetwork());
						}

					}
					if (queryPingPongCutEvent != null
							&& queryPingPongCutEvent.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("??????<"
								+ queryPingPongCutEvent.get(0).getCellName()
								+ ">??????(<"
								+ queryPingPongCutEvent.get(0).getCellId()
								+ ">,<"
								+ queryPingPongCutEvent.get(0).getPci()
								+ ">),??????"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryPingPongCutEvent.get(0).getEarfcn()
								+ ",RSRP:"
								+ queryPingPongCutEvent.get(0).getRsrp()
								+ ",????????????????????????(m)"
								+ queryPingPongCutEvent.get(0)
										.getToProblemDotDistance() + ",SINR:"
								+ queryPingPongCutEvent.get(0).getSinr());
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<"
								+ queryPingPongCutEvent.get(0).getCellName()
								+ ">??????(<"
								+ queryPingPongCutEvent.get(0).getCellId()
								+ ">,<" + queryPingPongCutEvent.get(0).getPci()
								+ ">),?????????" + videoQualityBad.getRsrpAvg()
								+ "dbm,SINR???<" + videoQualityBad.getSinrAvg()
								+ ">db,??????????????????:" + "????????????"
								+ videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("??????<>??????(<>,<>),??????"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,????????????????????????(m):,SINR:");
						videoStatementeResponseBean.setCellInfo("???<"
								+ videoStatementeResponseBean.getRoadName()
								+ ">????????????<>??????(<>,<>),?????????"
								+ videoQualityBad.getRsrpAvg() + "dbm,SINR???<"
								+ videoQualityBad.getSinrAvg() + ">db,??????????????????:"
								+ "????????????" + videoQualityBad.getVideobitrateAvg()
								+ ",???????????????"
								+ videoQualityBad.getStallingratioAvg()
								+ ",?????????????????????"
								+ videoQualityBad.getInitialbuffertimeAvg()
								+ ",???????????????????????????"
								+ videoQualityBad.getVideoresolutionAvg());
					}

				}
				videoStatementeResponseBean.setBeginTime(videoQualityBad
						.getTimeValue());
				if (videoQualityBad.getM_stRoadName() != null) {
					videoStatementeResponseBean.setRoadName(videoQualityBad
							.getM_stRoadName());
				} else {
					videoStatementeResponseBean.setRoadName("??????:"
							+ videoQualityBad.getCourseLongitude() + ","
							+ "??????:" + videoQualityBad.getCourseLatitude());
				}
				videoStatementeResponseBean.setLongitude(videoQualityBad
						.getCourseLongitude() != null ? String
						.valueOf(videoQualityBad.getCourseLongitude()) : null);
				videoStatementeResponseBean.setLatitude(videoQualityBad
						.getCourseLatitude() != null ? String
						.valueOf(videoQualityBad.getCourseLatitude()) : null);
				videoStatementeResponseBean.setDistance("-");
				videoStatementeResponseBean.setQuestionTime("-");
				videoStatementeResponseBean.setRsrpAvg(videoQualityBad
						.getRsrpAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getRsrpAvg(), 2)) : null);

				videoStatementeResponseBean.setRsrpMin(videoQualityBad
						.getRsrpMin() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getRsrpMin(), 2)) : null);

				videoStatementeResponseBean.setSinrAvg(videoQualityBad
						.getSinrAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getSinrAvg(), 2)) : null);

				videoStatementeResponseBean.setSinrMin(videoQualityBad
						.getSinrMin() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getSinrMin(), 2)) : null);

				videoStatementeResponseBean.setTestLogName(videoQualityBad
						.getTestLogItem().getFileName());
				videoStatementeResponseBean.setOptimization(videoQualityBad
						.getOptimization());
				/**
				 * ???????????????????????????
				 */
				videoStatementeResponseBean.setStallingratio(videoQualityBad
						.getStallingratioAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getStallingratioAvg(), 2))
						: null);
				videoStatementeResponseBean
						.setInitialbuffertime(videoQualityBad
								.getInitialbuffertimeAvg() != null ? String
								.valueOf(NumberFormatUtils.format(
										videoQualityBad
												.getInitialbuffertimeAvg(), 2))
								: null);
				videoStatementeResponseBean.setVideoresolution(videoQualityBad
						.getVideoresolutionAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getVideoresolutionAvg(), 2))
						: null);
				videoStatementeResponseBean.setVmos(videoQualityBad
						.getVmosAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getVmosAvg(), 2)) : null);
				videoOthersByLogIds.add(videoStatementeResponseBean);
			}
		}
		return videoOthersByLogIds;
	}

	@Override
	public void addQBRRoadName(String roadName, Long qbrId) {
		StreamQualityBad find = streamQualityBadDao.find(qbrId);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			streamQualityBadDao.addQBRRoadName(roadName, qbrId);
		}
	}

	@Override
	public List queryVmosValue(List<Long> testLogItemIds) {

		return streamQualityBadDao.queryVmosValue(testLogItemIds);
	}
}
