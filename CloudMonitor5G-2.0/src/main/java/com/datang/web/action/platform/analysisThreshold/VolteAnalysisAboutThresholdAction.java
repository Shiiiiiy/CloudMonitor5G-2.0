/**
 * 
 */
package com.datang.web.action.platform.analysisThreshold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.domain.security.User;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisAboutThresholdService;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.service.platform.security.IUserService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Volte分析门限值
 * 
 * @author shenyanwei
 * @date 2016年4月28日下午3:06:43
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class VolteAnalysisAboutThresholdAction extends ActionSupport {
	@Autowired
	private IVolteAnalysisThresholdService analysisThresholdService;
	@Autowired
	private IVolteAnalysisAboutThresholdService aboutThresholdService;
	@Autowired
	private IUserService userService;

	private Map<String, VolteAnalysisThreshold> thresholdMap;
	private Map<String, VolteAnalysisAboutThreshold> aboutThresholdMap;

	/**
	 * 进入volte专题分析门限页面
	 * 
	 * @return
	 */
	public String volteDissThresholdListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		User user = getUser();
		List<VolteAnalysisAboutThreshold> volteAnalysisAboutTList = null;
		if (user != null) {
			volteAnalysisAboutTList = aboutThresholdService.selectByUser(user);
		}
		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType("volte");
		List<VolteAnalysisThreshold> voiceQualityBadList = new ArrayList<>();
		List<VolteAnalysisThreshold> VoiceRTPPacketLossList = new ArrayList<>();
		List<VolteAnalysisThreshold> continueWirelessBadList = new ArrayList<>();
		List<VolteAnalysisThreshold> qualityProblemList = new ArrayList<>();
		List<VolteAnalysisThreshold> publicThresholdList = new ArrayList<>();
		List<VolteAnalysisThreshold> delayThresholdList = new ArrayList<>();
		List<VolteAnalysisThreshold> videoQualityBadList = new ArrayList<>();
		List<VolteAnalysisThreshold> keyParameterCauseList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseOvercoverList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCausePingPongList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseAdjacentregioneList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseWeakcoverList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseDisturbList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCausePatternSwitchList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseDownDispatchSmallList = new ArrayList<>();
		String VoiceQualityBadStr = "1";
		String ContinueWirelessBadStr = "1";
		String QualityProblemStr = "1";
		String PublicThresholdStr = "1";
		String DelayThresholdStr = "1";
		String VoiceRTPPacketLossStr = "1";
		String VideoQualityBadStr = "1";
		String KeyParameterCauseStr = "1";
		String WirelessProblemCauseOvercoverStr = "1";
		String WirelessProblemCausePingPongStr = "1";
		String WirelessProblemCauseAdjacentregionStr = "1";
		String WirelessProblemCauseWeakcoverStr = "1";
		String WirelessProblemCauseDisturbStr = "1";
		String WirelessProblemCausePatternSwitchStr = "1";
		String WirelessProblemCauseDownDispatchSmallStr = "1";
		if (volteAnalysisAboutTList != null) {
			for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : volteAnalysisAboutTList) {
				if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("VoiceQualityBad")) {
					VoiceQualityBadStr = volteAnalysisAboutThreshold.getValue();

				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("ContinueWirelessBad")) {
					ContinueWirelessBadStr = volteAnalysisAboutThreshold
							.getValue();

				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("QualityProblem")) {
					QualityProblemStr = volteAnalysisAboutThreshold.getValue();

				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("PublicThreshold")) {
					PublicThresholdStr = volteAnalysisAboutThreshold.getValue();

				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("DelayThreshold")) {
					DelayThresholdStr = volteAnalysisAboutThreshold.getValue();

				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("VoiceRTPPacketLoss")) {
					VoiceRTPPacketLossStr = volteAnalysisAboutThreshold
							.getValue();

				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("VideoQualityBad")) {
					VideoQualityBadStr = volteAnalysisAboutThreshold.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("KeyParameterCause")) {
					KeyParameterCauseStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("WirelessProblemCausePingPong")) {
					WirelessProblemCausePingPongStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("WirelessProblemCauseAdjacentregion")) {
					WirelessProblemCauseAdjacentregionStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("WirelessProblemCauseWeakcover")) {
					WirelessProblemCauseWeakcoverStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("WirelessProblemCauseDisturb")) {
					WirelessProblemCauseDisturbStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("WirelessProblemCauseOvercover")) {
					WirelessProblemCauseOvercoverStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("WirelessProblemCausePatternSwitch")) {
					WirelessProblemCausePatternSwitchStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold
								.getThresholdType()
								.equals("WirelessProblemCauseDownDispatchSmall")) {
					WirelessProblemCauseDownDispatchSmallStr = volteAnalysisAboutThreshold
							.getValue();
				}
			}
		}
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {

			String thresholdType = volteAnalysisThreshold.getThresholdType();
			if (null == thresholdType || 0 == thresholdType.trim().length()) {
				continue;
			}

			switch (thresholdType.trim()) {
			case VolteAnalysisThresholdTypeConstant.VOICE_QUALITY_BAD:

				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, VoiceQualityBadStr));
				voiceQualityBadList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.VOICE_RTP_PACKETLOSS:

				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, VoiceRTPPacketLossStr));
				VoiceRTPPacketLossList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.ContinueWirelessBad:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, ContinueWirelessBadStr));
				continueWirelessBadList.add(volteAnalysisThreshold);
				break;

			case VolteAnalysisThresholdTypeConstant.QUALITY_PROBLEM:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, QualityProblemStr));
				qualityProblemList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.PUBLIC_THRESHOLD:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, PublicThresholdStr));
				publicThresholdList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.DELAY_THRESHOLD:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, DelayThresholdStr));
				delayThresholdList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.VIDEO_QUALITY_BAD:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, VideoQualityBadStr));
				videoQualityBadList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.KEY_PARAMETER_CAUSE:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, KeyParameterCauseStr));
				keyParameterCauseList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_ADJACENTREGION:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseAdjacentregionStr));
				wirelessProblemCauseAdjacentregioneList
						.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_DISTURB:
				volteAnalysisThreshold
						.setCurrentThreshold(getCurrentThreshold(
								volteAnalysisThreshold,
								WirelessProblemCauseDisturbStr));
				wirelessProblemCauseDisturbList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_OVERCOVER:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseOvercoverStr));
				wirelessProblemCauseOvercoverList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_PINGPONG:
				volteAnalysisThreshold
						.setCurrentThreshold(getCurrentThreshold(
								volteAnalysisThreshold,
								WirelessProblemCausePingPongStr));
				wirelessProblemCausePingPongList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_WEAKCOVER:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseWeakcoverStr));
				wirelessProblemCauseWeakcoverList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_PATTERNSWITCH:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCausePatternSwitchStr));
				wirelessProblemCausePatternSwitchList
						.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_DOWNDISPATCHSMALL:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseDownDispatchSmallStr));
				wirelessProblemCauseDownDispatchSmallList
						.add(volteAnalysisThreshold);
				break;
			default:
				break;
			}
		}
		valueStack.set(VolteAnalysisThresholdTypeConstant.VOICE_QUALITY_BAD,
				voiceQualityBadList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.ContinueWirelessBad,
				continueWirelessBadList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.QUALITY_PROBLEM,
				qualityProblemList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.PUBLIC_THRESHOLD,
				publicThresholdList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.DELAY_THRESHOLD,
				delayThresholdList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.VOICE_RTP_PACKETLOSS,
				VoiceRTPPacketLossList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.VIDEO_QUALITY_BAD,
				videoQualityBadList);
		valueStack.set(VolteAnalysisThresholdTypeConstant.KEY_PARAMETER_CAUSE,
				keyParameterCauseList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_ADJACENTREGION,
						wirelessProblemCauseAdjacentregioneList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_DISTURB,
						wirelessProblemCauseDisturbList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_OVERCOVER,
						wirelessProblemCauseOvercoverList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_PINGPONG,
						wirelessProblemCausePingPongList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_WEAKCOVER,
						wirelessProblemCauseWeakcoverList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_PATTERNSWITCH,
						wirelessProblemCausePatternSwitchList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.WIRELESS_PROBLEM_CAUSE_DOWNDISPATCHSMALL,
						wirelessProblemCauseDownDispatchSmallList);

		return ReturnType.LISTUI;
	}

	/**
	 * 保存Volte分析门限修改
	 * 
	 * @return
	 */
	public String saveVolteDissThreshold() {

		if (null != thresholdMap) {
			List<VolteAnalysisThreshold> list = new ArrayList<>();
			Map<String, VolteAnalysisAboutThreshold> map = new HashMap<String, VolteAnalysisAboutThreshold>();
			User user = getUser();
			for (Entry<String, VolteAnalysisThreshold> entry : thresholdMap
					.entrySet()) {
				VolteAnalysisAboutThreshold aboutThreshold = new VolteAnalysisAboutThreshold();
				VolteAnalysisThreshold value = entry.getValue();

				if (value.getCurrentThreshold() != null) {
					aboutThreshold.setThresholdType(value.getThresholdType());
					aboutThreshold.setValue(value.getCurrentThreshold());
					aboutThreshold.setUser(user);
					map.put(aboutThreshold.getThresholdType(), aboutThreshold);
				}

				list.add(value);
			}
			for (VolteAnalysisThreshold value : list) {
				VolteAnalysisAboutThreshold threshold = map.get(value
						.getThresholdType());
				if (threshold != null && threshold.getValue() != null) {
					value.setCurrentThreshold(getCurrentThreshold(value,
							threshold.getValue()));
					value.setSubjectType("volte");
					analysisThresholdService
							.updateVolteAnalysisThreshold(value);
				}
			}
			List<VolteAnalysisAboutThreshold> list2 = aboutThresholdService
					.selectByUser(user);
			List<VolteAnalysisAboutThreshold> list3 = new ArrayList<VolteAnalysisAboutThreshold>();
			for (Entry<String, VolteAnalysisAboutThreshold> entry : map
					.entrySet()) {
				VolteAnalysisAboutThreshold volteAnalysisAboutThreshold = entry
						.getValue();

				list3.add(volteAnalysisAboutThreshold);

				if (list2.size() != 0) {
					Map<String, VolteAnalysisAboutThreshold> map2 = new HashMap<String, VolteAnalysisAboutThreshold>();
					for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold2 : list2) {

						map2.put(
								volteAnalysisAboutThreshold2.getThresholdType(),
								volteAnalysisAboutThreshold2);
					}
					if (map2.get(volteAnalysisAboutThreshold.getThresholdType()) != null) {
						map2.get(volteAnalysisAboutThreshold.getThresholdType())
								.setValue(
										volteAnalysisAboutThreshold.getValue());
						aboutThresholdService.update(map2
								.get(volteAnalysisAboutThreshold
										.getThresholdType()));
					} else {
						aboutThresholdService.save(volteAnalysisAboutThreshold);
					}
				} else {
					aboutThresholdService.save(volteAnalysisAboutThreshold);
				}
			}
			user.setAboutThresholdList(list3);
			userService.modifyUser(user);
		}
		ActionContext.getContext().getValueStack().set("result", "SUCCESS");
		return ReturnType.JSON;
	}

	/**
	 * 重置Volte分析门限
	 * 
	 * @return
	 */
	public String resetVolteDissThreshold() {

		return ReturnType.JSON;
	}

	/**
	 * 进入流媒体专题分析门限页面
	 * 
	 * @return
	 */
	public String streamDissThresholdListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		User user = getUser();
		List<VolteAnalysisAboutThreshold> volteAnalysisAboutTList = null;
		if (user != null) {
			volteAnalysisAboutTList = aboutThresholdService.selectByUser(user);
		}
		List<VolteAnalysisThreshold> all = analysisThresholdService
				.queryBySubjectType("stream");
		List<VolteAnalysisThreshold> videoQualityBadList = new ArrayList<>();
		List<VolteAnalysisThreshold> keyParameterCauseList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseOvercoverList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCausePingPongList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseAdjacentregioneList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseWeakcoverList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseDisturbList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCausePublicList = new ArrayList<>();
		List<VolteAnalysisThreshold> wirelessProblemCauseDownDispatchSmallList = new ArrayList<>();

		String VideoQualityBadStr = "1";
		String KeyParameterCauseStr = "1";
		String WirelessProblemCauseOvercoverStr = "1";
		String WirelessProblemCausePingPongStr = "1";
		String WirelessProblemCauseAdjacentregionStr = "1";
		String WirelessProblemCauseWeakcoverStr = "1";
		String WirelessProblemCauseDisturbStr = "1";
		String WirelessProblemCausePublicStr = "1";
		String WirelessProblemCauseDownDispatchSmallStr = "1";
		if (volteAnalysisAboutTList != null) {
			for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : volteAnalysisAboutTList) {
				if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamVideoQualityBad")) {
					VideoQualityBadStr = volteAnalysisAboutThreshold.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamKeyParameterCause")) {
					KeyParameterCauseStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamWirelessProblemCausePingPong")) {
					WirelessProblemCausePingPongStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold
								.getThresholdType()
								.equals("StreamWirelessProblemCauseAdjacentregion")) {
					WirelessProblemCauseAdjacentregionStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamWirelessProblemCauseWeakcover")) {
					WirelessProblemCauseWeakcoverStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamWirelessProblemCauseDisturb")) {
					WirelessProblemCauseDisturbStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamWirelessProblemCauseOvercover")) {
					WirelessProblemCauseOvercoverStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold.getThresholdType()
								.equals("StreamWirelessProblemCausePublic")) {
					WirelessProblemCausePublicStr = volteAnalysisAboutThreshold
							.getValue();
				} else if (volteAnalysisAboutThreshold.getThresholdType() != null
						&& volteAnalysisAboutThreshold
								.getThresholdType()
								.equals("StreamWirelessProblemCauseDownDispatchSmall")) {
					WirelessProblemCauseDownDispatchSmallStr = volteAnalysisAboutThreshold
							.getValue();
				}
			}
		}
		for (VolteAnalysisThreshold volteAnalysisThreshold : all) {

			String thresholdType = volteAnalysisThreshold.getThresholdType();
			if (null == thresholdType || 0 == thresholdType.trim().length()) {
				continue;
			}

			switch (thresholdType.trim()) {

			case VolteAnalysisThresholdTypeConstant.STREAM_VIDEO_QUALITY_BAD:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, VideoQualityBadStr));
				videoQualityBadList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_KEY_PARAMETER_CAUSE:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, KeyParameterCauseStr));
				keyParameterCauseList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_ADJACENTREGION:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseAdjacentregionStr));
				wirelessProblemCauseAdjacentregioneList
						.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_DISTURB:
				volteAnalysisThreshold
						.setCurrentThreshold(getCurrentThreshold(
								volteAnalysisThreshold,
								WirelessProblemCauseDisturbStr));
				wirelessProblemCauseDisturbList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_OVERCOVER:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseOvercoverStr));
				wirelessProblemCauseOvercoverList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_PINGPONG:
				volteAnalysisThreshold
						.setCurrentThreshold(getCurrentThreshold(
								volteAnalysisThreshold,
								WirelessProblemCausePingPongStr));
				wirelessProblemCausePingPongList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_WEAKCOVER:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseWeakcoverStr));
				wirelessProblemCauseWeakcoverList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_PATTERNSWITCH:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold, WirelessProblemCausePublicStr));
				wirelessProblemCausePublicList.add(volteAnalysisThreshold);
				break;
			case VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_DOWN_DISPATCH_SMALL:
				volteAnalysisThreshold.setCurrentThreshold(getCurrentThreshold(
						volteAnalysisThreshold,
						WirelessProblemCauseDownDispatchSmallStr));
				wirelessProblemCauseDownDispatchSmallList
						.add(volteAnalysisThreshold);
				break;
			default:
				break;
			}
		}

		valueStack.set(
				VolteAnalysisThresholdTypeConstant.STREAM_VIDEO_QUALITY_BAD,
				videoQualityBadList);
		valueStack.set(
				VolteAnalysisThresholdTypeConstant.STREAM_KEY_PARAMETER_CAUSE,
				keyParameterCauseList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_ADJACENTREGION,
						wirelessProblemCauseAdjacentregioneList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_DISTURB,
						wirelessProblemCauseDisturbList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_OVERCOVER,
						wirelessProblemCauseOvercoverList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_PINGPONG,
						wirelessProblemCausePingPongList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_WEAKCOVER,
						wirelessProblemCauseWeakcoverList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_PATTERNSWITCH,
						wirelessProblemCausePublicList);
		valueStack
				.set(VolteAnalysisThresholdTypeConstant.STREAM_WIRELESS_PROBLEM_CAUSE_DOWN_DISPATCH_SMALL,
						wirelessProblemCauseDownDispatchSmallList);

		return "streamListUI";
	}

	/**
	 * 保存流媒体分析门限修改
	 * 
	 * @return
	 */
	public String saveStreamDissThreshold() {

		if (null != thresholdMap) {
			List<VolteAnalysisThreshold> list = new ArrayList<>();
			Map<String, VolteAnalysisAboutThreshold> map = new HashMap<String, VolteAnalysisAboutThreshold>();
			User user = getUser();
			for (Entry<String, VolteAnalysisThreshold> entry : thresholdMap
					.entrySet()) {
				VolteAnalysisAboutThreshold aboutThreshold = new VolteAnalysisAboutThreshold();
				VolteAnalysisThreshold value = entry.getValue();

				if (value.getCurrentThreshold() != null) {
					aboutThreshold.setThresholdType(value.getThresholdType());
					aboutThreshold.setValue(value.getCurrentThreshold());
					aboutThreshold.setUser(user);
					map.put(aboutThreshold.getThresholdType(), aboutThreshold);
				}

				list.add(value);
			}
			for (VolteAnalysisThreshold value : list) {
				VolteAnalysisAboutThreshold threshold = map.get(value
						.getThresholdType());
				if (threshold != null && threshold.getValue() != null) {
					value.setCurrentThreshold(getCurrentThreshold(value,
							threshold.getValue()));
					value.setSubjectType("stream");
					analysisThresholdService
							.updateVolteAnalysisThreshold(value);
				}
			}
			List<VolteAnalysisAboutThreshold> list2 = aboutThresholdService
					.selectByUser(user);
			List<VolteAnalysisAboutThreshold> list3 = new ArrayList<VolteAnalysisAboutThreshold>();
			for (Entry<String, VolteAnalysisAboutThreshold> entry : map
					.entrySet()) {
				VolteAnalysisAboutThreshold volteAnalysisAboutThreshold = entry
						.getValue();

				list3.add(volteAnalysisAboutThreshold);

				if (list2.size() != 0) {
					Map<String, VolteAnalysisAboutThreshold> map2 = new HashMap<String, VolteAnalysisAboutThreshold>();
					for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold2 : list2) {

						map2.put(
								volteAnalysisAboutThreshold2.getThresholdType(),
								volteAnalysisAboutThreshold2);
					}
					if (map2.get(volteAnalysisAboutThreshold.getThresholdType()) != null) {
						map2.get(volteAnalysisAboutThreshold.getThresholdType())
								.setValue(
										volteAnalysisAboutThreshold.getValue());
						aboutThresholdService.update(map2
								.get(volteAnalysisAboutThreshold
										.getThresholdType()));
					} else {
						aboutThresholdService.save(volteAnalysisAboutThreshold);
					}
				} else {
					aboutThresholdService.save(volteAnalysisAboutThreshold);
				}
			}
			user.setAboutThresholdList(list3);
			userService.modifyUser(user);
		}
		ActionContext.getContext().getValueStack().set("result", "SUCCESS");
		return ReturnType.JSON;
	}

	/**
	 * 重置流媒体分析门限
	 * 
	 * @return
	 */
	public String resetStreamDissThreshold() {

		return ReturnType.JSON;
	}

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	private User getUser() {
		String userName = (String) SecurityUtils.getSubject().getPrincipal();
		if (userName != null) {
			User user = userService.findByUsername(userName);
			return user;
		} else {
			return null;
		}
	}

	private String getCurrentThreshold(
			VolteAnalysisThreshold volteAnalysisThreshold, String str) {
		if (str.equals("1")) {
			return volteAnalysisThreshold.getThreshold1();
		} else if (str.equals("2")) {
			return volteAnalysisThreshold.getThreshold2();
		} else {
			return volteAnalysisThreshold.getThreshold3();
		}
	}

	/**
	 * @return the thresholdMapthresholdMap
	 */
	public Map<String, VolteAnalysisThreshold> getThresholdMap() {
		return thresholdMap;
	}

	/**
	 * @param thresholdMap
	 *            the thresholdMap to set
	 */
	public void setThresholdMap(Map<String, VolteAnalysisThreshold> thresholdMap) {
		this.thresholdMap = thresholdMap;
	}

	/**
	 * 
	 * @return the aboutThresholdMap
	 */
	public Map<String, VolteAnalysisAboutThreshold> getAboutThresholdMap() {
		return aboutThresholdMap;
	}

	/**
	 * 
	 * @param aboutThresholdMap
	 *            the aboutThresholdMap to set
	 */
	public void setAboutThresholdMap(
			Map<String, VolteAnalysisAboutThreshold> aboutThresholdMap) {
		this.aboutThresholdMap = aboutThresholdMap;
	}

}
