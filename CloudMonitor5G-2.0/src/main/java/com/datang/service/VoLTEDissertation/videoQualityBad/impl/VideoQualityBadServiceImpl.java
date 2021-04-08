/**
 * 
 */
package com.datang.service.VoLTEDissertation.videoQualityBad.impl;

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
import com.datang.dao.VoLTEDissertation.videoQualityBad.DisturbPciOrSignalStrengthAdjustDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.DownDispatchSmallInspectCellUserAndExistDisturbDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.NeighbourPlotAddNeighbourPlotDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.OverCoverAzimuthOrDowndipAngleAdjustDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.PatternSwitchAdjustPatternSwitchSetValueDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.PingPongAdjustCutParameterDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.PingPongCutEventDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoDisturbDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoDownDispatchSmallDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoNeighbourPlotDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoOtherDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoOverCoverDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoPatternSwitchDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoPingPongDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoQualityBadDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.VideoWeakCoverDao;
import com.datang.dao.VoLTEDissertation.videoQualityBad.WeakCoverAdjustAntennaFeederDao;
import com.datang.dao.platform.projectParam.LteCellDao;
import com.datang.domain.VoLTEDissertation.videoQualityBad.VideoQualityBad;
import com.datang.domain.VoLTEDissertation.videoQualityBad.disturb.DisturbPciOrSignalStrengthAdjust;
import com.datang.domain.VoLTEDissertation.videoQualityBad.disturb.VideoQualtyBadDisturb;
import com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall.DownDispatchSmallInspectCellUserAndExistDisturb;
import com.datang.domain.VoLTEDissertation.videoQualityBad.downDispatchSmall.VideoQualityBadDownDispatchSmall;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.NeighbourPlotAddNeighbourPlot;
import com.datang.domain.VoLTEDissertation.videoQualityBad.neighbourPlot.VideoQualityBadNeighbourPlot;
import com.datang.domain.VoLTEDissertation.videoQualityBad.other.VideoQualityBadOther;
import com.datang.domain.VoLTEDissertation.videoQualityBad.overCover.OverCoverAzimuthOrDowndipAngleAdjust;
import com.datang.domain.VoLTEDissertation.videoQualityBad.overCover.VideoQualityBadOverCover;
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.PatternSwitchAdjustPatternSwitchSetValue;
import com.datang.domain.VoLTEDissertation.videoQualityBad.patternSwitch.VideoQualityBadPatternSwitch;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.PingPongAdjustCutParameter;
import com.datang.domain.VoLTEDissertation.videoQualityBad.pingPong.VideoQualityBadPingPong;
import com.datang.domain.VoLTEDissertation.videoQualityBad.weakCover.VideoQualityBadWeakCover;
import com.datang.domain.VoLTEDissertation.videoQualityBad.weakCover.WeakCoverAdjustAntennaFeeder;
import com.datang.domain.platform.projectParam.LteCell;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.VoLTEDissertation.videoQualityBad.IVideoQualityBadService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.NumberFormatUtils;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoStatementeResponseBean;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoWholeResponseBean0;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoWholeResponseBean1;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoWholeResponseBean2;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoWholeResponseBean3;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoWholeResponseBean4;
import com.datang.web.beans.VoLTEDissertation.videoQualityBad.VideoWholeResponseBean5;

/**
 * volte质量专题---volte视频质差Service接口实现
 * 
 * @explain
 * @name VideoQualityBadServiceImpl
 * @author shenyanwei
 * @date 2017年5月12日下午1:53:26
 */
@Service
@Transactional
public class VideoQualityBadServiceImpl implements IVideoQualityBadService {

	@Autowired
	private DisturbPciOrSignalStrengthAdjustDao disturbPciOrSignalStrengthAdjustDao;
	@Autowired
	private NeighbourPlotAddNeighbourPlotDao neighbourPlotAddNeighbourPlotDao;
	@Autowired
	private OverCoverAzimuthOrDowndipAngleAdjustDao overCoverAzimuthOrDowndipAngleAdjustDao;
	@Autowired
	private PingPongAdjustCutParameterDao pingPongAdjustCutParameterDao;
	@Autowired
	private PingPongCutEventDao pingPongCutEventDao;
	@Autowired
	private VideoDisturbDao videoDisturbDao;
	@Autowired
	private VideoNeighbourPlotDao videoNeighbourPlotDao;
	@Autowired
	private VideoOtherDao videoOtherDao;
	@Autowired
	private VideoOverCoverDao videoOverCoverDao;
	@Autowired
	private VideoPingPongDao videoPingPongDao;
	@Autowired
	private VideoQualityBadDao videoQualityBadDao;
	@Autowired
	private VideoWeakCoverDao videoWeakCoverDao;
	@Autowired
	private WeakCoverAdjustAntennaFeederDao weakCoverAdjustAntennaFeederDao;
	@Autowired
	private ITestLogItemService testLogItemService;
	@Autowired
	private VideoDownDispatchSmallDao videoDownDispatchSmallDao;
	@Autowired
	private DownDispatchSmallInspectCellUserAndExistDisturbDao downDispatchSmallInspectCellUserAndExistDisturbDao;
	@Autowired
	private VideoPatternSwitchDao videoPatternSwitchDao;
	@Autowired
	private PatternSwitchAdjustPatternSwitchSetValueDao patternSwitchAdjustPatternSwitchSetValueDao;
	@Autowired
	private LteCellDao lteCellDao;

	@Override
	public VideoQualityBad getVideoQualityBad(Long vqbId) {

		return videoQualityBadDao.find(vqbId);
	}

	@Override
	public List<VideoQualityBad> getVideoQualityBadsByLogIds(
			List<Long> testLogItemIds) {

		return videoQualityBadDao.queryVideoQualityBadsByLogIds(testLogItemIds);
	}

	@Override
	public Map<String, EasyuiPageList> doWholeAnalysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		// map.put("wholeRoadIndex0",doWholeIndex0Analysis(volteQualityBadRoadsByLogIds));
		map.put("wholeRoadIndex0",
				doWholeIndex00Analysis(videoQualityBadsByLogIds));
		map.put("wholeRoadIndex1",
				doWholeIndex1Analysis(videoQualityBadsByLogIds,
						queryTestLogItems, ids));
		map.put("wholeRoadIndex5",
				doWholeIndex5Analysis(videoQualityBadsByLogIds,
						queryTestLogItems, ids));
		map.put("wholeRoadIndex2",
				doWholeIndex2Analysis(videoQualityBadsByLogIds,
						queryTestLogItems));
		map.put("wholeRoadIndex3",
				doWholeIndex3Analysis(videoQualityBadsByLogIds));
		map.put("wholeRoadIndex4",
				doWholeIndex4Analysis(videoQualityBadsByLogIds));
		return map;
	}

	@Override
	public EasyuiPageList doWholeIndex00Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<VideoWholeResponseBean0> rows = new ArrayList<>();
		VideoWholeResponseBean0 wholeIndexResponseBean0 = new VideoWholeResponseBean0();
		rows.add(wholeIndexResponseBean0);
		easyuiPageList.setRows(rows);

		if (null == videoQualityBadsByLogIds
				|| 0 == videoQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}

		VideoQualityBad videoQualityBad0 = new VideoQualityBad();// 主叫bean
		for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
			/**
			 * 问题路段下的视频码率
			 */
			Float videoBitRate = videoQualityBad0.getVideoBitRate();
			Float videoBitRate2 = videoQualityBad.getVideoBitRate();
			if (null != videoBitRate2) {
				videoQualityBad0
						.setVideoBitRate(videoBitRate == null ? videoBitRate2
								: videoBitRate + videoBitRate2);
			}
			/**
			 * 问题路段下的视频帧率
			 */
			Float videoFrameRate = videoQualityBad0.getVideoFrameRate();
			Float videoFrameRate2 = videoQualityBad.getVideoFrameRate();
			if (null != videoFrameRate2) {
				videoQualityBad0
						.setVideoFrameRate(videoFrameRate == null ? videoFrameRate2
								: videoFrameRate + videoFrameRate2);
			}
			/**
			 * 问题路段下发送的音频数据包数量
			 */
			Long audioPacketNum = videoQualityBad0.getAudioPacketNum();
			Long audioPacketNum2 = videoQualityBad.getAudioPacketNum();
			if (null != audioPacketNum2) {
				videoQualityBad0
						.setAudioPacketNum(audioPacketNum == null ? audioPacketNum2
								: audioPacketNum + audioPacketNum2);
			}
			/**
			 * 问题路段下丢失的音频数据包数量
			 */
			Long audioPacketlossNum = videoQualityBad0.getAudioPacketlossNum();
			Long audioPacketlossNum2 = videoQualityBad.getAudioPacketlossNum();
			if (null != audioPacketlossNum2) {
				videoQualityBad0
						.setAudioPacketlossNum(audioPacketlossNum == null ? audioPacketlossNum2
								: audioPacketlossNum + audioPacketlossNum2);
			}
			/**
			 * 问题路段下发送的视频数据包数量
			 */
			Long videoPacketNum = videoQualityBad0.getVideoPacketNum();
			Long videoPacketNum2 = videoQualityBad.getVideoPacketNum();
			if (null != videoPacketNum2) {
				videoQualityBad0
						.setVideoPacketNum(videoPacketNum == null ? videoPacketNum2
								: videoPacketNum + videoPacketNum2);
			}
			/**
			 * 问题路段下丢失的视频数据包数量
			 */
			Long videoPacketlossNum = videoQualityBad0.getVideoPacketlossNum();
			Long videoPacketlossNum2 = videoQualityBad.getVideoPacketlossNum();
			if (null != videoPacketlossNum2) {
				videoQualityBad0
						.setVideoPacketlossNum(videoPacketlossNum == null ? videoPacketlossNum2
								: videoPacketlossNum + videoPacketlossNum2);
			}
			/**
			 * 问题路段下i_RTT
			 */
			Float irtt = videoQualityBad0.getIrtt();
			Float irtt2 = videoQualityBad.getIrtt();
			if (null != irtt2) {
				videoQualityBad0.setIrtt(irtt == null ? irtt2 : irtt + irtt2);
			}
			/**
			 * 问题路段下音频码率
			 */
			Float audioBitRate = videoQualityBad0.getAudioBitRate();
			Float audioBitRate2 = videoQualityBad.getAudioBitRate();
			if (null != audioBitRate2) {
				videoQualityBad0
						.setAudioBitRate(audioBitRate == null ? audioBitRate2
								: audioBitRate + audioBitRate2);
			}
			/**
			 * 问题路段下VMOS
			 */
			Float vmos = videoQualityBad0.getVmos();
			Float vmos2 = videoQualityBad.getVmos();
			if (null != vmos2) {
				videoQualityBad0.setVmos(vmos == null ? vmos2 : vmos + vmos2);
			}
			/**
			 * 问题路段下sinr值
			 */
			Float sinrValue = videoQualityBad0.getSinrValue();
			Float sinrValue2 = videoQualityBad.getSinrValue();
			if (null != sinrValue2) {
				videoQualityBad0.setSinrValue(sinrValue == null ? sinrValue2
						: sinrValue + sinrValue2);
			}
			/**
			 * 问题路段下rsrp值
			 */
			Float rsrpValue = videoQualityBad0.getRsrpValue();
			Float rsrpValue2 = videoQualityBad.getRsrpValue();
			if (null != rsrpValue2) {
				videoQualityBad0.setRsrpValue(rsrpValue == null ? rsrpValue2
						: rsrpValue + rsrpValue2);
			}
			/**
			 * 问题路段下rsrp采样点个数,问题路段sinr采样点个数,问题路段总的采样点个数
			 */
			/*
			 * Long rsrpOrSinrPointNum =
			 * videoQualityBad0.getRsrpOrSinrPointNum(); Long
			 * rsrpOrSinrPointNum2 = videoQualityBad.getRsrpOrSinrPointNum(); if
			 * (null != rsrpOrSinrPointNum2) { videoQualityBad0
			 * .setRsrpOrSinrPointNum(rsrpOrSinrPointNum == null ?
			 * rsrpOrSinrPointNum2 : rsrpOrSinrPointNum + rsrpOrSinrPointNum2);
			 * }
			 */

		}
		int size = videoQualityBadsByLogIds.size();
		/**
		 * 计算rsrp均值
		 */
		Float rsrpValue = videoQualityBad0.getRsrpValue();
		if (null != rsrpValue && size != 0) {
			wholeIndexResponseBean0.setRsrpValueAvg(NumberFormatUtils.format(
					rsrpValue / size, 2));
		}
		/**
		 * 计算sinr均值
		 */
		Float sinrValue = videoQualityBad0.getSinrValue();
		if (null != sinrValue && size != 0) {
			wholeIndexResponseBean0.setSinrValueAvg(NumberFormatUtils.format(
					sinrValue / size, 2));
		}

		/**
		 * 计算问题路段视频码率均值
		 */
		Float videoBitRate = videoQualityBad0.getVideoBitRate();
		if (null != videoBitRate && videoBitRate != 0 && size != 0) {
			wholeIndexResponseBean0.setVideoBitRate(NumberFormatUtils.format(
					videoBitRate / size, 2));
		}
		/**
		 * 计算问题路段视频帧率均值
		 */
		Float videoFrameRate = videoQualityBad0.getVideoFrameRate();
		if (null != videoFrameRate && videoFrameRate != 0 && size != 0) {
			wholeIndexResponseBean0.setVideoFrameRate(NumberFormatUtils.format(
					videoFrameRate / size, 2));
		}
		/**
		 * 计算问题路段语音丢包率均值
		 */
		Long audioPacketNum = videoQualityBad0.getAudioPacketNum();
		Long audioPacketlossNum = videoQualityBad0.getAudioPacketlossNum();
		// if (null != audioPacketNum && audioPacketNum != 0
		// && null != audioPacketlossNum && audioPacketlossNum != 0) {
		// wholeIndexResponseBean0.setPackLossRateForVoice(NumberFormatUtils
		// .format(audioPacketlossNum * 100 / audioPacketNum, 2));
		// }
		/**
		 * 计算问题路段视频丢包率均值
		 */
		Long videoPacketNum = videoQualityBad0.getVideoPacketNum()
				+ audioPacketNum;
		Long videoPacketlossNum = videoQualityBad0.getVideoPacketlossNum()
				+ audioPacketlossNum;
		if (null != videoPacketNum && videoPacketNum != 0
				&& null != videoPacketlossNum && videoPacketlossNum != 0) {
			wholeIndexResponseBean0.setPackLossRateForVideo(NumberFormatUtils
					.format(videoPacketlossNum * 100f / videoPacketNum, 4));
		}
		/**
		 * 计算问题路段i_RTT均值
		 */
		Float irtt = videoQualityBad0.getIrtt();
		if (null != irtt && irtt != 0 && size != 0) {
			wholeIndexResponseBean0.setIrtt(NumberFormatUtils.format(irtt
					/ size, 2));
		}
		/**
		 * 计算问题路段音频码率均值
		 */
		Float audioBitRate = videoQualityBad0.getAudioBitRate();
		if (null != audioBitRate && audioBitRate != 0 && size != 0) {
			wholeIndexResponseBean0.setAudioBitRate(NumberFormatUtils.format(
					audioBitRate / size, 2));
		}
		/**
		 * 计算问题路段VMOS均值
		 */
		Float vmos = videoQualityBad0.getVmos();
		if (null != vmos && vmos != 0 && size != 0) {
			wholeIndexResponseBean0.setVmos(NumberFormatUtils.format(vmos
					/ size, 2));
		}
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex1Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		if (null == videoQualityBadsByLogIds
				|| 0 == videoQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		Integer valueIn1_25 = 0;
		Integer valueIn25_3 = 0;
		Integer valueIn3_35 = 0;
		Integer valueIn35_5 = 0;
		Integer valueSum = 0;
		// for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
		// if (null != videoQualityBad.getVmos()) {
		// if (1 <= videoQualityBad.getVmos()
		// && videoQualityBad.getVmos() < 2.5) {
		// valueIn1_25++;
		// } else if (2.5 <= videoQualityBad.getVmos()
		// && videoQualityBad.getVmos() < 3) {
		// valueIn25_3++;
		// } else if (3 <= videoQualityBad.getVmos()
		// && videoQualityBad.getVmos() < 3.5) {
		// valueIn3_35++;
		// } else if (3.5 <= videoQualityBad.getVmos()
		// && videoQualityBad.getVmos() < 5) {
		// valueIn35_5++;
		// }
		// }
		// }
		List queryList = videoQualityBadDao.queryList(ids);
		Object[] object = (Object[]) queryList.get(0);
		if (object != null && object.length != 0) {
			for (int i = 0; i < object.length; i++) {
				if (object[i] != null) {
					switch (i) {
					case 0:
						valueIn1_25 = Integer
								.valueOf(String.valueOf(object[i])) / 1000;
						// System.out.println(Integer.valueOf(String
						// .valueOf(object[i])) / 1000);
						break;
					case 1:
						valueIn25_3 = Integer
								.valueOf(String.valueOf(object[i])) / 1000;
						// System.out.println(Integer.valueOf(String
						// .valueOf(object[i])) / 1000);
						break;
					case 2:
						valueIn3_35 = Integer
								.valueOf(String.valueOf(object[i])) / 1000;
						// System.out.println(Integer.valueOf(String
						// .valueOf(object[i])) / 1000);
						break;
					case 3:
						valueIn35_5 = Integer
								.valueOf(String.valueOf(object[i])) / 1000;
						// System.out.println(Integer.valueOf(String
						// .valueOf(object[i])) / 1000);
						break;
					case 4:
						valueSum = Integer.valueOf(String.valueOf(object[i])) / 1000;
						// System.out.println(Integer.valueOf(String
						// .valueOf(object[i])) / 1000);
						break;
					default:
						break;
					}
				}

			}
		}

		List<VideoWholeResponseBean1> rows = new ArrayList<>();
		// Integer sumsize = valueIn1_25 + valueIn25_3 + valueIn35_5 +
		// valueIn3_35;
		if (valueSum != 0) {
			VideoWholeResponseBean1 wholeIndexResponseBean10 = new VideoWholeResponseBean1();
			wholeIndexResponseBean10.setValue1to25(NumberFormatUtils.format(
					(float) (valueIn1_25 / 1.0 / valueSum), 4));

			wholeIndexResponseBean10.setValue25to3(NumberFormatUtils.format(
					(float) (valueIn25_3 / 1.0 / valueSum), 4));

			wholeIndexResponseBean10.setValue3to35(NumberFormatUtils.format(
					(float) (valueIn3_35 / 1.0 / valueSum), 4));

			wholeIndexResponseBean10.setValue35to5(NumberFormatUtils.format(
					(float) (valueIn35_5 / 1.0 / valueSum), 4));
			rows.add(wholeIndexResponseBean10);
		}

		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	/**
	 * 获取Vmos分布占比及分布范围
	 * 
	 * @param videoQualityBadsByLogIds
	 * @param queryTestLogItems
	 * @return
	 */
	public EasyuiPageList doWholeIndex5Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems, List<Long> ids) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		if (null == videoQualityBadsByLogIds
				|| 0 == videoQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		EasyuiPageList doWholeIndex1Analysis = doWholeIndex1Analysis(
				videoQualityBadsByLogIds, queryTestLogItems, ids);
		List<VideoWholeResponseBean5> rows = new ArrayList<>();
		VideoWholeResponseBean5 ResponseBean1tu25 = new VideoWholeResponseBean5();
		VideoWholeResponseBean5 ResponseBean25tu3 = new VideoWholeResponseBean5();
		VideoWholeResponseBean5 ResponseBean3tu35 = new VideoWholeResponseBean5();
		VideoWholeResponseBean5 ResponseBean35tu5 = new VideoWholeResponseBean5();
		if (doWholeIndex1Analysis.getRows().size() > 0) {
			VideoWholeResponseBean1 bean1 = (VideoWholeResponseBean1) doWholeIndex1Analysis
					.getRows().get(0);
			ResponseBean1tu25.setName("[1,2.5)");
			ResponseBean1tu25.setValue(bean1.getValue1to25() * 100);
			ResponseBean25tu3.setName("[2.5,3)");
			ResponseBean25tu3.setValue(bean1.getValue25to3() * 100);
			ResponseBean3tu35.setName("[3,3.5)");
			ResponseBean3tu35.setValue(bean1.getValue3to35() * 100);
			ResponseBean35tu5.setName("[3.5,5)");
			ResponseBean35tu5.setValue(bean1.getValue35to5() * 100);
		}

		rows.add(ResponseBean1tu25);
		rows.add(ResponseBean25tu3);
		rows.add(ResponseBean3tu35);
		rows.add(ResponseBean35tu5);
		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex2Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds,
			List<TestLogItem> queryTestLogItems) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<VideoWholeResponseBean2> rows = new ArrayList<>();
		VideoWholeResponseBean2 videoWholeResponseBean2 = new VideoWholeResponseBean2();
		rows.add(videoWholeResponseBean2);
		easyuiPageList.setRows(rows);
		if (null == videoQualityBadsByLogIds
				|| 0 == videoQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		/**
		 * 设置视频质差数量
		 */
		videoWholeResponseBean2.setRoadNum(videoQualityBadsByLogIds.size());
		Integer vmosPointNum = 0;// 所有问题路段:mos采样点数量
		// Integer cellNum = 0;// 所有问题路段:小区数量
		Set<Long> cellIdSet = new HashSet<>();
		Set<String> boxIdSet = new HashSet<>();// 所有问题路段:设备数量,以设备boxid为准
		Float vmosPointTotalNum = null;// 所有测试日志:mos采样点数量
		Float cellTotalNum = null;// 所有测试日志:小区数量
		Set<String> boxIdTotalSet = new HashSet<>();// 所有测试日志:设备数量,以设备boxid为准
		for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
			/**
			 * 计算所有问题路段:vmos采样点数量和小区数
			 * 
			 */
			/*
			 * Long vmosPointNum1 = videoQualityBad.getVmosPointNum(); Long
			 * cellNum1 = videoQualityBad.getCellNum(); if (null !=
			 * vmosPointNum1) { vmosPointNum = (vmosPointNum == null ?
			 * vmosPointNum1 : vmosPointNum + vmosPointNum1); } if (null !=
			 * cellNum1) { cellNum = (cellNum == null ? cellNum1 : cellNum +
			 * cellNum1); }
			 */
			vmosPointNum++;
			if (videoQualityBad.getCellId() != null) {
				cellIdSet.add(videoQualityBad.getCellId());
			}

			/**
			 * 计算所有问题路段:设备数量,以设备boxid为准
			 */
			String boxId = videoQualityBad.getTestLogItem().getBoxId();
			if (StringUtils.hasText(boxId)) {
				boxIdSet.add(boxId);
			}
		}
		ArrayList<Long> idsList = new ArrayList<Long>();
		for (TestLogItem testLog : queryTestLogItems) {
			/**
			 * 计算所有测试日志:mos采样点数量和小区数
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
			idsList.add(testLog.getRecSeqNo());
		}
		List queryList = videoQualityBadDao.queryList(idsList);
		Object[] object = (Object[]) queryList.get(0);
		if (object != null && object.length != 0) {
			vmosPointTotalNum = Float.valueOf(String.valueOf(object[4])) / 1000;
		}
		// 汇总计算采样点占比,小区占比,终端数量占比
		if (null != vmosPointTotalNum && vmosPointTotalNum != 0
				&& null != vmosPointNum) {
			videoWholeResponseBean2.setVmosPointNumRatio(NumberFormatUtils
					.format(vmosPointNum / vmosPointTotalNum * 100, 2));
		}
		// for (Long longs : cellIdSet) {
		// System.out.println(cellIdSet.size());
		// }
		if (null != cellTotalNum && cellTotalNum != 0) {
			videoWholeResponseBean2.setCellNumRatio(NumberFormatUtils.format(
					cellIdSet.size() / cellTotalNum * 100f, 4));
		}
		if (0 != boxIdTotalSet.size()) {
			videoWholeResponseBean2.setTerminalNumRatio(NumberFormatUtils
					.format((float) boxIdSet.size()
							/ (float) boxIdTotalSet.size() * 100, 2));
		}
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex3Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		if (null == videoQualityBadsByLogIds
				|| 0 == videoQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		// 汇总各个关键参数原因问题
		VideoWholeResponseBean3 packLossRateBean3 = new VideoWholeResponseBean3();
		VideoWholeResponseBean3 videoBitRateBean3 = new VideoWholeResponseBean3();
		VideoWholeResponseBean3 videoFrameRateBean3 = new VideoWholeResponseBean3();
		VideoWholeResponseBean3 audioBitRateBean3 = new VideoWholeResponseBean3();
		VideoWholeResponseBean3 otherBean3 = new VideoWholeResponseBean3();
		VideoWholeResponseBean3 collectRateBean3 = new VideoWholeResponseBean3();
		for (VideoQualityBad videoQualityBad : videoQualityBadsByLogIds) {
			switch (videoQualityBad.getKeyParameterCause()) {
			case 0:// 丢包率原因
				packLossRateBean3.setCollectNum(packLossRateBean3
						.getCollectNum() + 1);

				if (videoQualityBad instanceof VideoQualityBadPingPong) {
					packLossRateBean3.setPingPongNum(packLossRateBean3
							.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
					packLossRateBean3.setAdjacentNum(packLossRateBean3
							.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
					packLossRateBean3.setWeakCoverNum(packLossRateBean3
							.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
					packLossRateBean3.setDisturbNum(packLossRateBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
					packLossRateBean3.setOverCoverNum(packLossRateBean3
							.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOther) {
					packLossRateBean3.setOtherNum(packLossRateBean3
							.getOtherNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
					packLossRateBean3.setPatternSwitchNum(packLossRateBean3
							.getPatternSwitchNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
					packLossRateBean3.setDownDispatchSmallNum(packLossRateBean3
							.getDownDispatchSmallNum() + 1);
				}

				break;
			case 1:// 视频码率原因
				videoBitRateBean3.setCollectNum(videoBitRateBean3
						.getCollectNum() + 1);

				if (videoQualityBad instanceof VideoQualityBadPingPong) {
					videoBitRateBean3.setPingPongNum(videoBitRateBean3
							.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
					videoBitRateBean3.setAdjacentNum(videoBitRateBean3
							.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
					videoBitRateBean3.setWeakCoverNum(videoBitRateBean3
							.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
					videoBitRateBean3.setDisturbNum(videoBitRateBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
					videoBitRateBean3.setOverCoverNum(videoBitRateBean3
							.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOther) {
					videoBitRateBean3.setOtherNum(videoBitRateBean3
							.getOtherNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
					videoBitRateBean3.setPatternSwitchNum(videoBitRateBean3
							.getPatternSwitchNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
					videoBitRateBean3.setDownDispatchSmallNum(videoBitRateBean3
							.getDownDispatchSmallNum() + 1);
				}

				break;
			case 2:// 视频帧率原因
				videoFrameRateBean3.setCollectNum(videoFrameRateBean3
						.getCollectNum() + 1);

				if (videoQualityBad instanceof VideoQualityBadPingPong) {
					videoFrameRateBean3.setPingPongNum(videoFrameRateBean3
							.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
					videoFrameRateBean3.setAdjacentNum(videoFrameRateBean3
							.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
					videoFrameRateBean3.setWeakCoverNum(videoFrameRateBean3
							.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
					videoFrameRateBean3.setDisturbNum(videoFrameRateBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
					videoFrameRateBean3.setOverCoverNum(videoFrameRateBean3
							.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOther) {
					videoFrameRateBean3.setOtherNum(videoFrameRateBean3
							.getOtherNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
					videoFrameRateBean3.setPatternSwitchNum(videoFrameRateBean3
							.getPatternSwitchNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
					videoFrameRateBean3
							.setDownDispatchSmallNum(videoFrameRateBean3
									.getDownDispatchSmallNum() + 1);
				}

				break;
			case 3:// 音频码率原因
				audioBitRateBean3.setCollectNum(audioBitRateBean3
						.getCollectNum() + 1);

				if (videoQualityBad instanceof VideoQualityBadPingPong) {
					audioBitRateBean3.setPingPongNum(audioBitRateBean3
							.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
					audioBitRateBean3.setAdjacentNum(audioBitRateBean3
							.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
					audioBitRateBean3.setWeakCoverNum(audioBitRateBean3
							.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
					audioBitRateBean3.setDisturbNum(audioBitRateBean3
							.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
					audioBitRateBean3.setOverCoverNum(audioBitRateBean3
							.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOther) {
					audioBitRateBean3.setOtherNum(audioBitRateBean3
							.getOtherNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
					audioBitRateBean3.setPatternSwitchNum(audioBitRateBean3
							.getPatternSwitchNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
					audioBitRateBean3.setDownDispatchSmallNum(audioBitRateBean3
							.getDownDispatchSmallNum() + 1);
				}
				break;
			case -1:// 音频码率原因
				otherBean3.setCollectNum(otherBean3.getCollectNum() + 1);

				if (videoQualityBad instanceof VideoQualityBadPingPong) {
					otherBean3.setPingPongNum(otherBean3.getPingPongNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
					otherBean3.setAdjacentNum(otherBean3.getAdjacentNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
					otherBean3
							.setWeakCoverNum(otherBean3.getWeakCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
					otherBean3.setDisturbNum(otherBean3.getDisturbNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
					otherBean3
							.setOverCoverNum(otherBean3.getOverCoverNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadOther) {
					otherBean3.setOtherNum(otherBean3.getOtherNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
					otherBean3.setPatternSwitchNum(otherBean3
							.getPatternSwitchNum() + 1);
				} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
					otherBean3.setDownDispatchSmallNum(otherBean3
							.getDownDispatchSmallNum() + 1);
				}
				break;

			default:
				break;
			}
			// 统计汇总个数

			if (videoQualityBad instanceof VideoQualityBadPingPong) {
				collectRateBean3.setPingPongNum(collectRateBean3
						.getPingPongNum() != null ? collectRateBean3
						.getPingPongNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
				collectRateBean3.setAdjacentNum(collectRateBean3
						.getAdjacentNum() != null ? collectRateBean3
						.getAdjacentNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
				collectRateBean3.setWeakCoverNum(collectRateBean3
						.getWeakCoverNum() != null ? collectRateBean3
						.getWeakCoverNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
				collectRateBean3
						.setDisturbNum(collectRateBean3.getDisturbNum() != null ? collectRateBean3
								.getDisturbNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
				collectRateBean3.setOverCoverNum(collectRateBean3
						.getOverCoverNum() != null ? collectRateBean3
						.getOverCoverNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualityBadOther) {
				collectRateBean3
						.setOtherNum(collectRateBean3.getOtherNum() != null ? collectRateBean3
								.getOtherNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
				collectRateBean3.setPatternSwitchNum(collectRateBean3
						.getPatternSwitchNum() != null ? collectRateBean3
						.getPatternSwitchNum() + 1 : 1);
			} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
				collectRateBean3.setDownDispatchSmallNum(collectRateBean3
						.getDownDispatchSmallNum() != null ? collectRateBean3
						.getDownDispatchSmallNum() + 1 : 1);
			}
			collectRateBean3
					.setCollectNum(collectRateBean3.getCollectNum() + 1);
		}

		List<VideoWholeResponseBean3> rows = new ArrayList<>();
		// 计算占比
		// 计算丢包率原因下各占比
		if (packLossRateBean3.getCollectNum() != null
				&& packLossRateBean3.getCollectNum() > 0) {
			packLossRateBean3
					.setPingPongRatio(NumberFormatUtils.format(
							(packLossRateBean3.getPingPongNum() == 0 ? 0
									: Float.valueOf(packLossRateBean3
											.getPingPongNum())
											/ Float.valueOf(packLossRateBean3
													.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setAdjacentRatio(NumberFormatUtils.format(
							(packLossRateBean3.getAdjacentNum() == 0 ? 0
									: Float.valueOf(packLossRateBean3
											.getAdjacentNum())
											/ Float.valueOf(packLossRateBean3
													.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setWeakCoverRatio(NumberFormatUtils.format(
							(packLossRateBean3.getWeakCoverNum() == 0 ? 0
									: Float.valueOf(packLossRateBean3
											.getWeakCoverNum())
											/ Float.valueOf(packLossRateBean3
													.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setDisturbRatio(NumberFormatUtils.format(
							(packLossRateBean3.getDisturbNum() == 0 ? 0 : Float
									.valueOf(packLossRateBean3.getDisturbNum())
									/ Float.valueOf(packLossRateBean3
											.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setOverCoverRatio(NumberFormatUtils.format(
							(packLossRateBean3.getOverCoverNum() == 0 ? 0
									: Float.valueOf(packLossRateBean3
											.getOverCoverNum())
											/ Float.valueOf(packLossRateBean3
													.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setPatternSwitchRatio(NumberFormatUtils.format(
							(packLossRateBean3.getPatternSwitchNum() == 0 ? 0
									: Float.valueOf(packLossRateBean3
											.getPatternSwitchNum())
											/ Float.valueOf(packLossRateBean3
													.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(packLossRateBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(packLossRateBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(packLossRateBean3
													.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setOtherRatio(NumberFormatUtils.format(
							(packLossRateBean3.getOtherNum() == 0 ? 0 : Float
									.valueOf(packLossRateBean3.getOtherNum())
									/ Float.valueOf(packLossRateBean3
											.getCollectNum())) * 100, 2));
			packLossRateBean3
					.setCollectRatio(NumberFormatUtils.format(
							(packLossRateBean3.getCollectNum() == 0 ? 0 : Float
									.valueOf(packLossRateBean3.getCollectNum())
									/ Float.valueOf(videoQualityBadsByLogIds
											.size())) * 100, 2));

		}
		packLossRateBean3.setCause("丢包率");
		// 计算视频码率原因下各占比
		if (videoBitRateBean3.getCollectNum() != null
				&& videoBitRateBean3.getCollectNum() > 0) {
			videoBitRateBean3
					.setPingPongRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getPingPongNum() == 0 ? 0
									: Float.valueOf(videoBitRateBean3
											.getPingPongNum())
											/ Float.valueOf(videoBitRateBean3
													.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setAdjacentRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getAdjacentNum() == 0 ? 0
									: Float.valueOf(videoBitRateBean3
											.getAdjacentNum())
											/ Float.valueOf(videoBitRateBean3
													.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setWeakCoverRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getWeakCoverNum() == 0 ? 0
									: Float.valueOf(videoBitRateBean3
											.getWeakCoverNum())
											/ Float.valueOf(videoBitRateBean3
													.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setDisturbRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getDisturbNum() == 0 ? 0 : Float
									.valueOf(videoBitRateBean3.getDisturbNum())
									/ Float.valueOf(videoBitRateBean3
											.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setOverCoverRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getOverCoverNum() == 0 ? 0
									: Float.valueOf(videoBitRateBean3
											.getOverCoverNum())
											/ Float.valueOf(videoBitRateBean3
													.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setPatternSwitchRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getPatternSwitchNum() == 0 ? 0
									: Float.valueOf(videoBitRateBean3
											.getPatternSwitchNum())
											/ Float.valueOf(videoBitRateBean3
													.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(videoBitRateBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(videoBitRateBean3
													.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setOtherRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getOtherNum() == 0 ? 0 : Float
									.valueOf(videoBitRateBean3.getOtherNum())
									/ Float.valueOf(videoBitRateBean3
											.getCollectNum())) * 100, 2));
			videoBitRateBean3
					.setCollectRatio(NumberFormatUtils.format(
							(videoBitRateBean3.getCollectNum() == null ? 0
									: Float.valueOf(videoBitRateBean3
											.getCollectNum())
											/ Float.valueOf(videoQualityBadsByLogIds
													.size())) * 100, 2));
		}
		videoBitRateBean3.setCause("视频码率");
		// 计算视频帧率原因下各占比
		if (videoFrameRateBean3.getCollectNum() != null
				&& videoFrameRateBean3.getCollectNum() > 0) {
			videoFrameRateBean3.setPingPongRatio(NumberFormatUtils.format(
					(videoFrameRateBean3.getPingPongNum() == 0 ? 0
							: Float.valueOf(videoFrameRateBean3
									.getPingPongNum())
									/ Float.valueOf(videoFrameRateBean3
											.getCollectNum())) * 100, 2));
			videoFrameRateBean3.setAdjacentRatio(NumberFormatUtils.format(
					(videoFrameRateBean3.getAdjacentNum() == 0 ? 0
							: Float.valueOf(videoFrameRateBean3
									.getAdjacentNum())
									/ Float.valueOf(videoFrameRateBean3
											.getCollectNum())) * 100, 2));
			videoFrameRateBean3.setWeakCoverRatio(NumberFormatUtils.format(
					(videoFrameRateBean3.getWeakCoverNum() == 0 ? 0
							: Float.valueOf(videoFrameRateBean3
									.getWeakCoverNum())
									/ Float.valueOf(videoFrameRateBean3
											.getCollectNum())) * 100, 2));
			videoFrameRateBean3
					.setDisturbRatio(NumberFormatUtils.format(
							(videoFrameRateBean3.getDisturbNum() == 0 ? 0
									: Float.valueOf(videoFrameRateBean3
											.getDisturbNum())
											/ Float.valueOf(videoFrameRateBean3
													.getCollectNum())) * 100, 2));
			videoFrameRateBean3.setOverCoverRatio(NumberFormatUtils.format(
					(videoFrameRateBean3.getOverCoverNum() == 0 ? 0
							: Float.valueOf(videoFrameRateBean3
									.getOverCoverNum())
									/ Float.valueOf(videoFrameRateBean3
											.getCollectNum())) * 100, 2));
			videoFrameRateBean3.setPatternSwitchRatio(NumberFormatUtils.format(
					(videoFrameRateBean3.getPatternSwitchNum() == 0 ? 0
							: Float.valueOf(videoFrameRateBean3
									.getPatternSwitchNum())
									/ Float.valueOf(videoFrameRateBean3
											.getCollectNum())) * 100, 2));
			videoFrameRateBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(videoFrameRateBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(videoFrameRateBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(videoFrameRateBean3
													.getCollectNum())) * 100, 2));
			videoFrameRateBean3.setOtherRatio(NumberFormatUtils.format(
					(videoFrameRateBean3.getOtherNum() == 0 ? 0
							: Float.valueOf(videoFrameRateBean3.getOtherNum())
									/ Float.valueOf(videoFrameRateBean3
											.getCollectNum())) * 100, 2));
			videoFrameRateBean3
					.setCollectRatio(NumberFormatUtils.format(
							(videoFrameRateBean3.getCollectNum() == 0 ? 0
									: Float.valueOf(videoFrameRateBean3
											.getCollectNum())
											/ Float.valueOf(videoQualityBadsByLogIds
													.size())) * 100, 2));
		}
		videoFrameRateBean3.setCause("视频帧率");
		// 计算音频码率原因下各占比
		if (audioBitRateBean3.getCollectNum() != null
				&& audioBitRateBean3.getCollectNum() > 0) {
			audioBitRateBean3
					.setPingPongRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getPingPongNum() == 0 ? 0
									: Float.valueOf(audioBitRateBean3
											.getPingPongNum())
											/ Float.valueOf(audioBitRateBean3
													.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setAdjacentRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getAdjacentNum() == 0 ? 0
									: Float.valueOf(audioBitRateBean3
											.getAdjacentNum())
											/ Float.valueOf(audioBitRateBean3
													.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setWeakCoverRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getWeakCoverNum() == 0 ? 0
									: Float.valueOf(audioBitRateBean3
											.getWeakCoverNum())
											/ Float.valueOf(audioBitRateBean3
													.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setDisturbRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getDisturbNum() == 0 ? 0 : Float
									.valueOf(audioBitRateBean3.getDisturbNum())
									/ Float.valueOf(audioBitRateBean3
											.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setOverCoverRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getOverCoverNum() == 0 ? 0
									: Float.valueOf(audioBitRateBean3
											.getOverCoverNum())
											/ Float.valueOf(audioBitRateBean3
													.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setPatternSwitchRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getPatternSwitchNum() == 0 ? 0
									: Float.valueOf(audioBitRateBean3
											.getPatternSwitchNum())
											/ Float.valueOf(audioBitRateBean3
													.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setDownDispatchSmallRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getDownDispatchSmallNum() == 0 ? 0
									: Float.valueOf(audioBitRateBean3
											.getDownDispatchSmallNum())
											/ Float.valueOf(audioBitRateBean3
													.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setOtherRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getOtherNum() == 0 ? 0 : Float
									.valueOf(audioBitRateBean3.getOtherNum())
									/ Float.valueOf(audioBitRateBean3
											.getCollectNum())) * 100, 2));
			audioBitRateBean3
					.setCollectRatio(NumberFormatUtils.format(
							(audioBitRateBean3.getCollectNum() == 0 ? 0 : Float
									.valueOf(audioBitRateBean3.getCollectNum())
									/ Float.valueOf(videoQualityBadsByLogIds
											.size())) * 100, 2));
		}
		audioBitRateBean3.setCause("音频码率");
		// 计算其它原因下各占比
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
			otherBean3.setPatternSwitchRatio(NumberFormatUtils.format(
					(otherBean3.getPatternSwitchNum() == 0 ? 0 : Float
							.valueOf(otherBean3.getPatternSwitchNum())
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
									/ Float.valueOf(videoQualityBadsByLogIds
											.size())) * 100, 2));
		}
		otherBean3.setCause("其它原因");
		// 计算汇总原因下各占比
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
					.setPatternSwitchRatio(NumberFormatUtils.format(
							(collectRateBean3.getPatternSwitchNum() == 0 ? 0
									: Float.valueOf(collectRateBean3
											.getPatternSwitchNum())
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
									/ Float.valueOf(videoQualityBadsByLogIds
											.size())) * 100, 2));
		}
		collectRateBean3.setCause("汇总");
		rows.add(packLossRateBean3);
		rows.add(videoBitRateBean3);
		rows.add(videoFrameRateBean3);
		rows.add(audioBitRateBean3);
		rows.add(otherBean3);
		rows.add(collectRateBean3);
		easyuiPageList.setRows(rows);
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doWholeIndex4Analysis(
			List<VideoQualityBad> videoQualityBadsByLogIds) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();

		if (null == videoQualityBadsByLogIds
				|| 0 == videoQualityBadsByLogIds.size()) {
			return easyuiPageList;
		}
		// 汇总各个原因问题
		VideoWholeResponseBean4 pingPongBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 adjacentBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 weakCoverBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 disturbBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 overCoverBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 patternSwitchBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 downDispatchSmallBean4 = new VideoWholeResponseBean4();
		VideoWholeResponseBean4 otherBean4 = new VideoWholeResponseBean4();
		EasyuiPageList doWholeIndex3Analysis = doWholeIndex3Analysis(videoQualityBadsByLogIds);
		List<VideoWholeResponseBean3> rows = (List<VideoWholeResponseBean3>) doWholeIndex3Analysis
				.getRows();
		if (rows.size() != 0) {
			for (VideoWholeResponseBean3 object : rows) {
				if (object.getCause() != null) {
					switch (object.getCause()) {
					case "丢包率":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						patternSwitchBean4.getData().add(
								object.getPatternSwitchNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "视频码率":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						patternSwitchBean4.getData().add(
								object.getPatternSwitchNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "视频帧率":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						patternSwitchBean4.getData().add(
								object.getPatternSwitchNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "音频码率":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						patternSwitchBean4.getData().add(
								object.getPatternSwitchNum());
						downDispatchSmallBean4.getData().add(
								object.getDownDispatchSmallNum());
						otherBean4.getData().add(object.getOtherNum());
						break;
					case "其它原因":
						pingPongBean4.getData().add(object.getPingPongNum());
						adjacentBean4.getData().add(object.getAdjacentNum());
						weakCoverBean4.getData().add(object.getWeakCoverNum());
						disturbBean4.getData().add(object.getDisturbNum());
						overCoverBean4.getData().add(object.getOverCoverNum());
						patternSwitchBean4.getData().add(
								object.getPatternSwitchNum());
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
		pingPongBean4.setName("乒乓切换");
		adjacentBean4.setName("邻区缺失");
		weakCoverBean4.setName("弱覆盖");
		disturbBean4.setName("干扰");
		overCoverBean4.setName("重叠覆盖");
		patternSwitchBean4.setName("模式转换");
		downDispatchSmallBean4.setName("下行调度数小");
		otherBean4.setName("其他");
		List<VideoWholeResponseBean4> rows2 = new ArrayList<>();
		rows2.add(pingPongBean4);
		rows2.add(adjacentBean4);
		rows2.add(weakCoverBean4);
		rows2.add(disturbBean4);
		rows2.add(overCoverBean4);
		rows2.add(patternSwitchBean4);
		rows2.add(downDispatchSmallBean4);
		rows2.add(otherBean4);
		easyuiPageList.setRows(rows2);
		return easyuiPageList;
	}

	@Override
	public List<VideoQualityBadWeakCover> getVideoWeakCoversByLogIds(
			List<Long> testLogItemIds) {

		return videoWeakCoverDao.queryVideoWeakCoversByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadWeakCover getVideoWeakCoverById(Long id) {

		return videoWeakCoverDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoWeakCoverAnalysis(
			VideoQualityBadWeakCover videoQualityBadWeakCover) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("weakCoverAdjustAntennaFeeder",
				doWeakCoverAdjustAntennaFeederAnalysis(videoQualityBadWeakCover));

		return map;
	}

	@Override
	public EasyuiPageList doWeakCoverAdjustAntennaFeederAnalysis(
			VideoQualityBadWeakCover videoQualityBadWeakCover) {
		if (null == videoQualityBadWeakCover
				|| null == videoQualityBadWeakCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(weakCoverAdjustAntennaFeederDao
				.queryWeakCoverAdjustAntennaFeeder(videoQualityBadWeakCover));
		return easyuiPageList;
	}

	@Override
	public List<VideoQualtyBadDisturb> getVideoDisturbsByLogIds(
			List<Long> testLogItemIds) {

		return videoDisturbDao.queryVideoDisturbsByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualtyBadDisturb getVideoDisturbById(Long roadId) {

		return videoDisturbDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoDisturbAnalysis(
			VideoQualtyBadDisturb videoDisturbById) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("disturbPciOrSignalStrengthAdjust",
				doDisturbPciOrSignalStrengthAdjustAnalysis(videoDisturbById));

		return map;
	}

	@Override
	public EasyuiPageList doDisturbPciOrSignalStrengthAdjustAnalysis(
			VideoQualtyBadDisturb videoDisturbById) {
		if (null == videoDisturbById || null == videoDisturbById.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(disturbPciOrSignalStrengthAdjustDao
				.queryDisturbPciOrSignalStrengthAdjust(videoDisturbById));
		return easyuiPageList;
	}

	@Override
	public List<VideoQualityBadNeighbourPlot> getVideoNeighbourPlotsByLogIds(
			List<Long> testLogItemIds) {

		return videoNeighbourPlotDao
				.queryVideoNeighbourPlotsByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadNeighbourPlot getVideoNeighbourPlotById(Long roadId) {

		return videoNeighbourPlotDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoNeighbourPlotAnalysis(
			VideoQualityBadNeighbourPlot videoQualityBadNeighbourPlot) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("neighbourPlotAddNeighbourPlot",
				doNeighbourPlotAddNeighbourPlotAnalysis(videoQualityBadNeighbourPlot));

		return map;
	}

	@Override
	public EasyuiPageList doNeighbourPlotAddNeighbourPlotAnalysis(
			VideoQualityBadNeighbourPlot videoQualityBadNeighbourPlot) {
		if (null == videoQualityBadNeighbourPlot
				|| null == videoQualityBadNeighbourPlot.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList
				.setRows(neighbourPlotAddNeighbourPlotDao
						.queryNeighbourPlotAddNeighbourPlot(videoQualityBadNeighbourPlot));
		return easyuiPageList;
	}

	@Override
	public List<VideoQualityBadOverCover> getVideoOverCoversByLogIds(
			List<Long> testLogItemIds) {

		return videoOverCoverDao.queryVideoOverCoversByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadOverCover getVideoOverCoverById(Long roadId) {

		return videoOverCoverDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoOverCoverAnalysis(
			VideoQualityBadOverCover videoQualityBadOverCover) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("overCoverAzimuthOrDowndipAngleAdjust",
				doOverCoverAzimuthOrDowndipAngleAdjustAnalysis(videoQualityBadOverCover));

		return map;
	}

	@Override
	public EasyuiPageList doOverCoverAzimuthOrDowndipAngleAdjustAnalysis(
			VideoQualityBadOverCover videoQualityBadOverCover) {
		if (null == videoQualityBadOverCover
				|| null == videoQualityBadOverCover.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList
				.setRows(overCoverAzimuthOrDowndipAngleAdjustDao
						.queryOverCoverAzimuthOrDowndipAngleAdjust(videoQualityBadOverCover));
		return easyuiPageList;
	}

	@Override
	public List<VideoQualityBadPingPong> getVideoPingPongsByLogIds(
			List<Long> testLogItemIds) {

		return videoPingPongDao.queryVideoPingPongsByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadPingPong getVideoPingPongById(Long id) {

		return videoPingPongDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoPingPongAnalysis(
			VideoQualityBadPingPong videoQualityBadPingPong) {
		Map<String, EasyuiPageList> map = new HashMap<>();
		ArrayList<Long> arrayList = new ArrayList<>();
		EasyuiPageList doPingPongAdjustCutParameterAnalysis = doPingPongAdjustCutParameterAnalysis(videoQualityBadPingPong);
		map.put("pingPongAdjustCutParameter",
				doPingPongAdjustCutParameterAnalysis);
		if (doPingPongAdjustCutParameterAnalysis.getRows() != null
				&& doPingPongAdjustCutParameterAnalysis.getRows().size() != 0) {
			for (Object object : doPingPongAdjustCutParameterAnalysis.getRows()) {
				PingPongAdjustCutParameter pongAdjustCutParameter = (PingPongAdjustCutParameter) object;
				arrayList.add(pongAdjustCutParameter.getCellId());
			}
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(arrayList);
		map.put("cellIdList", easyuiPageList);
		map.put("pingPongCutEvent",
				doPingPongCutEventAnalysis(videoQualityBadPingPong));

		return map;
	}

	@Override
	public EasyuiPageList doPingPongAdjustCutParameterAnalysis(
			VideoQualityBadPingPong videoQualityBadPingPong) {
		if (null == videoQualityBadPingPong
				|| null == videoQualityBadPingPong.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList.setRows(pingPongAdjustCutParameterDao
				.queryPingPongAdjustCutParameter(videoQualityBadPingPong));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doPingPongCutEventAnalysis(
			VideoQualityBadPingPong videoQualityBadPingPong) {
		if (null == videoQualityBadPingPong
				|| null == videoQualityBadPingPong.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		// List<PingPongCutEvent> queryPingPongCutEvent = pingPongCutEventDao
		// .queryPingPongCutEvent(videoQualityBadPingPong);
		// // 去重
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
		// // 按时间排序
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
		easyuiPageList.setRows(pingPongCutEventDao
				.queryPingPongCutEvent(videoQualityBadPingPong));
		// }

		return easyuiPageList;
	}

	@Override
	public List<VideoQualityBadOther> getVideoOthersByLogIds(
			List<Long> testLogItemIds) {

		return videoOtherDao.queryVideoOthersByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadOther getVideoOtherById(Long roadId) {

		return videoOtherDao.find(roadId);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoOtherAnalysis(
			VideoQualityBadOther videoQualityBadOther) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoQualityBadPatternSwitch> getVideoPatternSwitchsByLogIds(
			List<Long> testLogItemIds) {

		return videoPatternSwitchDao
				.queryVideoPatternSwitchsByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadPatternSwitch getVideoPatternSwitchById(Long id) {

		return videoPatternSwitchDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoPatternSwitchAnalysis(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("patternSwitchAdjust",
				doPatternSwitchAdjustAnalysis(videoQualityBadPatternSwitch));
		map.put("patternSwitchKeyIndex",
				doPatternSwitchKeyIndexAnalysis(videoQualityBadPatternSwitch));

		return map;
	}

	@Override
	public EasyuiPageList doPatternSwitchAdjustAnalysis(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch) {
		if (null == videoQualityBadPatternSwitch
				|| null == videoQualityBadPatternSwitch.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList
				.setRows(patternSwitchAdjustPatternSwitchSetValueDao
						.queryDownDispatchSmallInspectCellUserAndExistDisturb(videoQualityBadPatternSwitch));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doPatternSwitchKeyIndexAnalysis(
			VideoQualityBadPatternSwitch videoQualityBadPatternSwitch) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<VideoWholeResponseBean0> rows = new ArrayList<>();
		VideoWholeResponseBean0 wholeIndexResponseBean0 = new VideoWholeResponseBean0();
		rows.add(wholeIndexResponseBean0);
		easyuiPageList.setRows(rows);

		/**
		 * 计算问题路段视频码率均值
		 */
		Float videoBitRate = videoQualityBadPatternSwitch.getVideoBitRate();
		if (null != videoBitRate && videoBitRate != 0) {
			wholeIndexResponseBean0.setVideoBitRate(NumberFormatUtils.format(
					videoBitRate, 2));
		}
		/**
		 * 计算问题路段视频帧率均值
		 */
		Float videoFrameRate = videoQualityBadPatternSwitch.getVideoFrameRate();
		if (null != videoFrameRate && videoFrameRate != 0) {
			wholeIndexResponseBean0.setVideoFrameRate(NumberFormatUtils.format(
					videoFrameRate, 2));
		}
		/**
		 * 计算问题路段语音丢包率均值
		 */
		Long audioPacketNum = videoQualityBadPatternSwitch.getAudioPacketNum();
		Long audioPacketlossNum = videoQualityBadPatternSwitch
				.getAudioPacketlossNum();
		Long videoPacketNum = videoQualityBadPatternSwitch.getVideoPacketNum();
		Long videoPacketlossNum = videoQualityBadPatternSwitch
				.getVideoPacketlossNum();
		Long packetNum = audioPacketNum + videoPacketNum;
		Long packetlossNum = audioPacketlossNum + videoPacketlossNum;
		if (null != packetNum && packetNum != 0 && null != packetlossNum
				&& packetlossNum != 0) {
			wholeIndexResponseBean0.setPackLossRate(NumberFormatUtils.format(
					packetlossNum * 100f / packetNum, 4));
		}
		/**
		 * 计算问题路段i_RTT均值
		 */
		Float irtt = videoQualityBadPatternSwitch.getIrtt();
		if (null != irtt && irtt != 0) {
			wholeIndexResponseBean0.setIrtt(NumberFormatUtils.format(irtt, 4));
		}
		/**
		 * 计算问题路段音频码率均值
		 */
		Float audioBitRate = videoQualityBadPatternSwitch.getAudioBitRate();
		if (null != audioBitRate && audioBitRate != 0) {
			wholeIndexResponseBean0.setAudioBitRate(NumberFormatUtils.format(
					audioBitRate, 2));
		}

		return easyuiPageList;
	}

	@Override
	public List<VideoQualityBadDownDispatchSmall> getVideoDownDispatchSmallsByLogIds(
			List<Long> testLogItemIds) {

		return videoDownDispatchSmallDao
				.queryVideoDownDispatchSmallsByLogIds(testLogItemIds);
	}

	@Override
	public VideoQualityBadDownDispatchSmall getVideoDownDispatchSmallById(
			Long id) {

		return videoDownDispatchSmallDao.find(id);
	}

	@Override
	public Map<String, EasyuiPageList> doVideoDownDispatchSmallAnalysis(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall) {
		Map<String, EasyuiPageList> map = new HashMap<>();

		map.put("downDispatchSmallAdjust",
				doDownDispatchSmallAdjustAnalysis(videoQualityBadDownDispatchSmall));
		map.put("downDispatchSmallKeyIndex",
				doDownDispatchSmallKeyIndexAnalysis(videoQualityBadDownDispatchSmall));

		return map;
	}

	@Override
	public EasyuiPageList doDownDispatchSmallAdjustAnalysis(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall) {
		if (null == videoQualityBadDownDispatchSmall
				|| null == videoQualityBadDownDispatchSmall.getId()) {
			return new EasyuiPageList();
		}
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		easyuiPageList
				.setRows(downDispatchSmallInspectCellUserAndExistDisturbDao
						.queryPingPongCutEvent(videoQualityBadDownDispatchSmall));
		return easyuiPageList;
	}

	@Override
	public EasyuiPageList doDownDispatchSmallKeyIndexAnalysis(
			VideoQualityBadDownDispatchSmall videoQualityBadDownDispatchSmall) {
		EasyuiPageList easyuiPageList = new EasyuiPageList();
		List<VideoWholeResponseBean0> rows = new ArrayList<>();
		VideoWholeResponseBean0 wholeIndexResponseBean0 = new VideoWholeResponseBean0();
		rows.add(wholeIndexResponseBean0);
		easyuiPageList.setRows(rows);

		/**
		 * 计算问题路段视频码率均值
		 */
		Float videoBitRate = videoQualityBadDownDispatchSmall.getVideoBitRate();
		if (null != videoBitRate && videoBitRate != 0) {
			wholeIndexResponseBean0.setVideoBitRate(NumberFormatUtils.format(
					videoBitRate, 2));
		}
		/**
		 * 计算问题路段视频帧率均值
		 */
		Float videoFrameRate = videoQualityBadDownDispatchSmall
				.getVideoFrameRate();
		if (null != videoFrameRate && videoFrameRate != 0) {
			wholeIndexResponseBean0.setVideoFrameRate(NumberFormatUtils.format(
					videoFrameRate, 2));
		}
		/**
		 * 计算问题路段语音丢包率均值
		 */
		Long audioPacketNum = videoQualityBadDownDispatchSmall
				.getAudioPacketNum();
		Long audioPacketlossNum = videoQualityBadDownDispatchSmall
				.getAudioPacketlossNum();
		Long videoPacketNum = videoQualityBadDownDispatchSmall
				.getVideoPacketNum();
		Long videoPacketlossNum = videoQualityBadDownDispatchSmall
				.getVideoPacketlossNum();
		Long packetNum = audioPacketNum + videoPacketNum;
		Long packetlossNum = audioPacketlossNum + videoPacketlossNum;
		if (null != packetNum && packetNum != 0 && null != packetlossNum
				&& packetlossNum != 0) {
			wholeIndexResponseBean0.setPackLossRate(NumberFormatUtils.format(
					packetlossNum * 100f / packetNum, 4));
		}
		/**
		 * 计算问题路段i_RTT均值
		 */
		Float irtt = videoQualityBadDownDispatchSmall.getIrtt();
		if (null != irtt && irtt != 0) {
			wholeIndexResponseBean0.setIrtt(NumberFormatUtils.format(irtt, 2));
		}
		/**
		 * 计算问题路段音频码率均值
		 */
		Float audioBitRate = videoQualityBadDownDispatchSmall.getAudioBitRate();
		if (null != audioBitRate && audioBitRate != 0) {
			wholeIndexResponseBean0.setAudioBitRate(NumberFormatUtils.format(
					audioBitRate, 2));
		}

		return easyuiPageList;
	}

	@Override
	public LteCell queryLteCellInfoByCellId(Long cellId) {

		return lteCellDao.queryLteCellByCellId(cellId);
	}

	@Override
	public List<VideoStatementeResponseBean> getDownloadInfo(
			List<VideoQualityBad> videoQualityBads) {
		List<VideoStatementeResponseBean> videoOthersByLogIds = new ArrayList<>();
		if (null != videoQualityBads && videoQualityBads.size() != 0) {
			for (VideoQualityBad videoQualityBad : videoQualityBads) {
				VideoStatementeResponseBean videoStatementeResponseBean = new VideoStatementeResponseBean();
				if (videoQualityBad.getCellId() != null) {
					LteCell queryLteCellInfoByCellId = queryLteCellInfoByCellId(videoQualityBad
							.getCellId());
					if (queryLteCellInfoByCellId != null) {
						videoStatementeResponseBean
								.setRegion(queryLteCellInfoByCellId.getRegion());
						videoStatementeResponseBean
								.setVender(queryLteCellInfoByCellId.getVender());
						videoStatementeResponseBean
								.setCoverScene(queryLteCellInfoByCellId
										.getCoverScene());
						videoStatementeResponseBean
								.setIsBelongtoNetwork(queryLteCellInfoByCellId
										.getIsBelongtoNetwork());
					}

				}
				videoStatementeResponseBean.setBoxId(videoQualityBad
						.getTestLogItem().getBoxId());
				videoStatementeResponseBean.setEvenName("VMOS质差");
				if (videoQualityBad.getKeyParameterCause() != null) {
					switch (videoQualityBad.getKeyParameterCause()) {
					case 0:
						videoStatementeResponseBean.setReason("丢包率");
						break;
					case 1:
						videoStatementeResponseBean.setReason("视频码率");
						break;
					case 2:
						videoStatementeResponseBean.setReason("视频帧率");
						break;
					case 3:
						videoStatementeResponseBean.setReason("音频码率");
						break;
					case -1:
						videoStatementeResponseBean.setReason("其它");
						break;

					default:
						break;
					}
				}
				if (videoQualityBad instanceof VideoQualityBadPingPong) {
					videoStatementeResponseBean.setReasonWireless("乒乓切换");
					List<PingPongAdjustCutParameter> queryPingPongAdjustCutParameter = pingPongAdjustCutParameterDao
							.queryPingPongAdjustCutParameter((VideoQualityBadPingPong) videoQualityBad);
					if (queryPingPongAdjustCutParameter != null
							&& queryPingPongAdjustCutParameter.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName()
								+ ">小区(<"
								+ videoQualityBad.getCellId()
								+ ">,<"
								+ videoQualityBad.getCellPci()
								+ ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryPingPongAdjustCutParameter.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryPingPongAdjustCutParameter.get(0)
										.getRsrp()
								+ ",和问题采样点距离(m):"
								+ queryPingPongAdjustCutParameter.get(0)
										.getToProblemDotDistance());
					} else {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName() + ">小区(<"
								+ videoQualityBad.getCellId() + ">,<"
								+ videoQualityBad.getCellPci() + ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:,RSRP:,和问题采样点距离(m):");
					}

				} else if (videoQualityBad instanceof VideoQualityBadNeighbourPlot) {
					videoStatementeResponseBean.setReasonWireless("邻区问题");
					List<NeighbourPlotAddNeighbourPlot> queryNeighbourPlotAddNeighbourPlot = neighbourPlotAddNeighbourPlotDao
							.queryNeighbourPlotAddNeighbourPlot((VideoQualityBadNeighbourPlot) videoQualityBad);
					if (queryNeighbourPlotAddNeighbourPlot != null
							&& queryNeighbourPlotAddNeighbourPlot.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName()
								+ ">小区(<"
								+ videoQualityBad.getCellId()
								+ ">,<"
								+ videoQualityBad.getCellPci()
								+ ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getRsrp()
								+ ",和问题采样点距离(m):"
								+ queryNeighbourPlotAddNeighbourPlot.get(0)
										.getToProblemDotDistance());
					} else {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName() + ">小区(<"
								+ videoQualityBad.getCellId() + ">,<"
								+ videoQualityBad.getCellPci() + ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:,RSRP:,和问题采样点距离(m):");
					}

				} else if (videoQualityBad instanceof VideoQualityBadWeakCover) {
					videoStatementeResponseBean.setReasonWireless("弱覆盖");
					List<WeakCoverAdjustAntennaFeeder> queryWeakCoverAdjustAntennaFeeder = weakCoverAdjustAntennaFeederDao
							.queryWeakCoverAdjustAntennaFeeder((VideoQualityBadWeakCover) videoQualityBad);
					if (queryWeakCoverAdjustAntennaFeeder != null
							&& queryWeakCoverAdjustAntennaFeeder.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName()
								+ ">小区(<"
								+ videoQualityBad.getCellId()
								+ ">,<"
								+ videoQualityBad.getCellPci()
								+ ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getRsrp()
								+ ",和问题采样点距离(m):"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getToProblemDotDistance()
								+ ",工参站高(m):"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getProjectHeight()
								+ ",工参下倾角:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getProjectDowndipAngle()
								+ ",工参方向角:"
								+ queryWeakCoverAdjustAntennaFeeder.get(0)
										.getProjectAzimuth());
					} else {
						videoStatementeResponseBean
								.setOptimizationInfo("调整<"
										+ videoQualityBad.getCellName()
										+ ">小区(<"
										+ videoQualityBad.getCellId()
										+ ">,<"
										+ videoQualityBad.getCellPci()
										+ ">),建议"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:,RSRP:,和问题采样点距离(m):,工参站高(m):,工参下倾角:,工参方向角:");
					}

				} else if (videoQualityBad instanceof VideoQualtyBadDisturb) {
					videoStatementeResponseBean.setReasonWireless("干扰");
					List<DisturbPciOrSignalStrengthAdjust> queryDisturbPciOrSignalStrengthAdjust = disturbPciOrSignalStrengthAdjustDao
							.queryDisturbPciOrSignalStrengthAdjust((VideoQualtyBadDisturb) videoQualityBad);
					if (queryDisturbPciOrSignalStrengthAdjust != null
							&& queryDisturbPciOrSignalStrengthAdjust.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName()
								+ ">小区(<"
								+ videoQualityBad.getCellId()
								+ ">,<"
								+ videoQualityBad.getCellPci()
								+ ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getEarfcn()
								+ ",RSRP:"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getRsrp()
								+ ",和问题采样点距离(m)"
								+ queryDisturbPciOrSignalStrengthAdjust.get(0)
										.getToProblemDotDistance());
					} else {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName() + ">小区(<"
								+ videoQualityBad.getCellId() + ">,<"
								+ videoQualityBad.getCellPci() + ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:,RSRP:,和问题采样点距离(m):");
					}

				} else if (videoQualityBad instanceof VideoQualityBadOverCover) {
					videoStatementeResponseBean.setReasonWireless("重叠覆盖");
					List<OverCoverAzimuthOrDowndipAngleAdjust> queryOverCoverAzimuthOrDowndipAngleAdjust = overCoverAzimuthOrDowndipAngleAdjustDao
							.queryOverCoverAzimuthOrDowndipAngleAdjust((VideoQualityBadOverCover) videoQualityBad);
					if (queryOverCoverAzimuthOrDowndipAngleAdjust != null
							&& queryOverCoverAzimuthOrDowndipAngleAdjust.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName()
								+ ">小区(<"
								+ videoQualityBad.getCellId()
								+ ">,<"
								+ videoQualityBad.getCellPci()
								+ ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getEarfcn()
								+ ",RSRP:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getRsrp()
								+ ",和问题采样点距离(m)"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getToProblemDotDistance()
								+ ",工参下倾角:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getProjectDowndipAngle()
								+ ",工参方位角:"
								+ queryOverCoverAzimuthOrDowndipAngleAdjust
										.get(0).getProjectAzimuth());
					} else {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName() + ">小区(<"
								+ videoQualityBad.getCellId() + ">,<"
								+ videoQualityBad.getCellPci() + ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:,RSRP:,和问题采样点距离(m):,工参下倾角:,工参方位角:");
					}

				} else if (videoQualityBad instanceof VideoQualityBadOther) {
					videoStatementeResponseBean.setReasonWireless("其他");
					videoStatementeResponseBean.setOptimizationInfo("调整<"
							+ videoQualityBad.getCellName() + ">小区(<"
							+ videoQualityBad.getCellId() + ">,<"
							+ videoQualityBad.getCellPci() + ">),建议"
							+ videoQualityBad.getOptimization());
				} else if (videoQualityBad instanceof VideoQualityBadPatternSwitch) {
					videoStatementeResponseBean.setReasonWireless("模式转换");
					List<PatternSwitchAdjustPatternSwitchSetValue> queryDownDispatchSmallInspectCellUserAndExistDisturb = patternSwitchAdjustPatternSwitchSetValueDao
							.queryDownDispatchSmallInspectCellUserAndExistDisturb((VideoQualityBadPatternSwitch) videoQualityBad);
					if (queryDownDispatchSmallInspectCellUserAndExistDisturb != null
							&& queryDownDispatchSmallInspectCellUserAndExistDisturb
									.size() != 0) {
						videoStatementeResponseBean
								.setOptimizationInfo("调整<"
										+ videoQualityBad.getCellName()
										+ ">小区(<"
										+ videoQualityBad.getCellId()
										+ ">,<"
										+ videoQualityBad.getCellPci()
										+ ">),建议"
										+ videoQualityBad.getOptimization()
										+ ",EARFCN:"
										+ queryDownDispatchSmallInspectCellUserAndExistDisturb
												.get(0).getEarfcn()
										+ ",RSRP:"
										+ queryDownDispatchSmallInspectCellUserAndExistDisturb
												.get(0).getRsrp()
										+ ",和问题采样点距离(m)"
										+ queryDownDispatchSmallInspectCellUserAndExistDisturb
												.get(0)
												.getToProblemDotDistance()
										+ ",SINR:"
										+ queryDownDispatchSmallInspectCellUserAndExistDisturb
												.get(0).getSinr());
					} else {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName() + ">小区(<"
								+ videoQualityBad.getCellId() + ">,<"
								+ videoQualityBad.getCellPci() + ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:,RSRP:,和问题采样点距离(m):,SINR:");
					}

				} else if (videoQualityBad instanceof VideoQualityBadDownDispatchSmall) {
					videoStatementeResponseBean.setReasonWireless("下行调度小");
					List<DownDispatchSmallInspectCellUserAndExistDisturb> queryPingPongCutEvent = downDispatchSmallInspectCellUserAndExistDisturbDao
							.queryPingPongCutEvent((VideoQualityBadDownDispatchSmall) videoQualityBad);
					if (queryPingPongCutEvent != null
							&& queryPingPongCutEvent.size() != 0) {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName()
								+ ">小区(<"
								+ videoQualityBad.getCellId()
								+ ">,<"
								+ videoQualityBad.getCellPci()
								+ ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:"
								+ queryPingPongCutEvent.get(0).getEarfcn()
								+ ",RSRP:"
								+ queryPingPongCutEvent.get(0).getRsrp()
								+ ",和问题采样点距离(m)"
								+ queryPingPongCutEvent.get(0)
										.getToProblemDotDistance() + ",SINR:"
								+ queryPingPongCutEvent.get(0).getSinr());
					} else {
						videoStatementeResponseBean.setOptimizationInfo("调整<"
								+ videoQualityBad.getCellName() + ">小区(<"
								+ videoQualityBad.getCellId() + ">,<"
								+ videoQualityBad.getCellPci() + ">),建议"
								+ videoQualityBad.getOptimization()
								+ ",EARFCN:,RSRP:,和问题采样点距离(m):,SINR:");
					}

				}
				videoStatementeResponseBean.setBeginTime(videoQualityBad
						.getTimeValue());
				if (videoQualityBad.getM_stRoadName() != null) {
					videoStatementeResponseBean.setRoadName(videoQualityBad
							.getM_stRoadName());
				} else {
					videoStatementeResponseBean.setRoadName("经度:"
							+ videoQualityBad.getLongitude() + "," + "纬度:"
							+ videoQualityBad.getLatitude());
				}
				videoStatementeResponseBean.setLongitude(videoQualityBad
						.getLongitude() != null ? String
						.valueOf(videoQualityBad.getLongitude()) : null);
				videoStatementeResponseBean.setLatitude(videoQualityBad
						.getLatitude() != null ? String.valueOf(videoQualityBad
						.getLatitude()) : null);
				videoStatementeResponseBean.setDistance("-");
				videoStatementeResponseBean.setQuestionTime("-");
				videoStatementeResponseBean.setRsrpAvg(videoQualityBad
						.getRsrpValueAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getRsrpValueAvg(), 2)) : null);

				videoStatementeResponseBean.setRsrpMin(videoQualityBad
						.getRsrpValueMin() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getRsrpValueMin(), 2)) : null);

				videoStatementeResponseBean.setSinrAvg(videoQualityBad
						.getSinrValueAvg() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getSinrValueAvg(), 2)) : null);

				videoStatementeResponseBean.setSinrMin(videoQualityBad
						.getSinrValueMin() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getSinrValueMin(), 2)) : null);

				videoStatementeResponseBean.setTestLogName(videoQualityBad
						.getTestLogItem().getFileName());
				videoStatementeResponseBean.setOptimization(videoQualityBad
						.getOptimization());
				/**
				 * 计算问题路段丢包率
				 */
				Long audioPacketNum = videoQualityBad.getAudioPacketNum();
				Long audioPacketlossNum = videoQualityBad
						.getAudioPacketlossNum();
				Long videoPacketNum = videoQualityBad.getVideoPacketNum();
				Long videoPacketlossNum = videoQualityBad
						.getVideoPacketlossNum();
				Long packetNum = audioPacketNum + videoPacketNum;
				Long packetlossNum = audioPacketlossNum + videoPacketlossNum;
				if (null != packetNum && packetNum != 0
						&& null != packetlossNum && packetlossNum != 0) {
					videoStatementeResponseBean.setLossPageRate(String
							.valueOf(NumberFormatUtils.format(packetlossNum
									* 100f / packetNum, 2)));
				}
				videoStatementeResponseBean.setCellInfo("在<"
						+ videoStatementeResponseBean.getRoadName() + ">路段占用<"
						+ videoQualityBad.getCellName() + ">小区(<"
						+ videoQualityBad.getCellId() + ">,<"
						+ videoQualityBad.getCellPci() + ">),场强为"
						+ videoQualityBad.getRsrpValue() + "dbm,SINR为<"
						+ videoQualityBad.getSinrValue() + ">db,问题详情描述:"
						+ "视频码率为" + videoQualityBad.getVideoBitRate()
						+ ",视频帧率为" + videoQualityBad.getVideoFrameRate()
						+ ",丢包率为"
						+ videoStatementeResponseBean.getLossPageRate()
						+ ",音频码率为" + videoQualityBad.getAudioBitRate()
						+ "VMOS均值为" + videoQualityBad.getVmos());
				videoStatementeResponseBean.setVideoBitRate(videoQualityBad
						.getVideoBitRate() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getVideoBitRate(), 2)) : null);
				videoStatementeResponseBean
						.setVideoFrameRate(videoQualityBad.getVideoFrameRate() != null ? String.valueOf(NumberFormatUtils
								.format(videoQualityBad.getVideoFrameRate(), 2))
								: null);
				videoStatementeResponseBean
						.setIrtt(videoQualityBad.getIrtt() != null ? String
								.valueOf(NumberFormatUtils.format(
										videoQualityBad.getIrtt(), 2)) : null);
				videoStatementeResponseBean.setAudioBitRate(videoQualityBad
						.getAudioBitRate() != null ? String
						.valueOf(NumberFormatUtils.format(
								videoQualityBad.getAudioBitRate(), 2)) : null);
				videoStatementeResponseBean
						.setVmos(videoQualityBad.getVmos() != null ? String
								.valueOf(NumberFormatUtils.format(
										videoQualityBad.getVmos(), 2)) : null);
				videoOthersByLogIds.add(videoStatementeResponseBean);
			}
		}
		return videoOthersByLogIds;
	}

	@Override
	public void addQBRRoadName(String roadName, Long qbrId) {
		VideoQualityBad find = videoQualityBadDao.find(qbrId);
		if (null != find && !StringUtils.hasText(find.getM_stRoadName())) {
			videoQualityBadDao.addQBRRoadName(roadName, qbrId);
		}
	}
}
