/**
 * 
 */
package com.datang.constant;

/**
 * volte分析门限类型常量
 * 
 * @author yinzhipeng
 * @date:2015年9月29日 上午10:00:14
 * @version
 */
public interface VolteAnalysisThresholdTypeConstant {
	/**
	 * 关键参数原因门限
	 */
	public static String KEY_PARAMETER_CAUSE = "KeyParameterCause";
	/**
	 * 流媒体专题关键参数原因门限
	 */
	public static String STREAM_KEY_PARAMETER_CAUSE = "StreamKeyParameterCause";
	/**
	 * 无线问题原因门限之乒乓切换
	 */
	public static String WIRELESS_PROBLEM_CAUSE_PINGPONG = "WirelessProblemCausePingPong";
	/**
	 * 无线问题原因门限之邻区问题
	 */
	public static String WIRELESS_PROBLEM_CAUSE_ADJACENTREGION = "WirelessProblemCauseAdjacentregion";
	/**
	 * 无线问题原因门限之弱覆盖
	 */
	public static String WIRELESS_PROBLEM_CAUSE_WEAKCOVER = "WirelessProblemCauseWeakcover";
	/**
	 * 无线问题原因门限之干扰
	 */
	public static String WIRELESS_PROBLEM_CAUSE_DISTURB = "WirelessProblemCauseDisturb";
	/**
	 * 无线问题原因门限之模式转换
	 */
	public static String WIRELESS_PROBLEM_CAUSE_PATTERNSWITCH = "WirelessProblemCausePatternSwitch";
	/**
	 * 无线问题原因门限之下行调度小
	 */
	public static String WIRELESS_PROBLEM_CAUSE_DOWNDISPATCHSMALL = "WirelessProblemCauseDownDispatchSmall";
	/**
	 * 无线问题原因门限之重叠覆盖
	 */
	public static String WIRELESS_PROBLEM_CAUSE_OVERCOVER = "WirelessProblemCauseOvercover";
	/**
	 * 流媒体专题无线问题原因门限之乒乓切换
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_PINGPONG = "StreamWirelessProblemCausePingPong";
	/**
	 * 流媒体专题无线问题原因门限之邻区问题
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_ADJACENTREGION = "StreamWirelessProblemCauseAdjacentregion";
	/**
	 * 流媒体专题无线问题原因门限之弱覆盖
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_WEAKCOVER = "StreamWirelessProblemCauseWeakcover";
	/**
	 * 流媒体专题无线问题原因门限之干扰
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_DISTURB = "StreamWirelessProblemCauseDisturb";
	/**
	 * 流媒体专题无线问题原因门限之公共
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_PATTERNSWITCH = "StreamWirelessProblemCausePublic";

	/**
	 * 流媒体专题无线问题原因门限之重叠覆盖
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_OVERCOVER = "StreamWirelessProblemCauseOvercover";
	/**
	 * 流媒体专题无线问题原因门限之下行调度小
	 */
	public static String STREAM_WIRELESS_PROBLEM_CAUSE_DOWN_DISPATCH_SMALL = "StreamWirelessProblemCauseDownDispatchSmall";
	/**
	 * 语音质差类型门限
	 */
	public static String VOICE_QUALITY_BAD = "VoiceQualityBad";
	/**
	 * 视频质差类型门限
	 */
	public static String VIDEO_QUALITY_BAD = "VideoQualityBad";
	/**
	 * 流媒体专题视频质差类型门限
	 */
	public static String STREAM_VIDEO_QUALITY_BAD = "StreamVideoQualityBad";
	/**
	 * 语音RTP连续丢包分析门限
	 */
	public static String VOICE_RTP_PACKETLOSS = "VoiceRTPPacketLoss";
	/**
	 * 资源隐患类型门限
	 */
	public static String ContinueWirelessBad = "ContinueWirelessBad";
	/**
	 * 时延抖动类型门限
	 */
	public static String CallEstablishDelay = "CallEstablishDelay";
	/**
	 * 质量问题类型门限
	 */
	public static String QUALITY_PROBLEM = "QualityProblem";
	/**
	 * 语音业务异常类型门限
	 */
	public static String VOICE_BUSINESS_UNUSUAL = "VoiceBusinessUnusual";
	/**
	 * 视频业务异常类型门限
	 */
	public static String VIDEO_BUSINESS_UNUSUAL = "VideoBusinessUnusual";
	/**
	 * 公共门限
	 */
	public static String PUBLIC_THRESHOLD = "PublicThreshold";
	/**
	 * 时延异常分析门限
	 */
	public static String DELAY_THRESHOLD = "DelayThreshold";
	/*
	 * MOS均值恶化门限
	 */
	public static String MOS_VORSEN = "MosVorsen";
	/*
	 * MOS均值稍降门限
	 */
	public static String MOS_DECLINE = "MosDecline";
	/*
	 * RSRP均值恶化门限(dBm)
	 */
	public static String RSRP_AVAREGE_VORSEN = "RsrpAvaregeVorsen";
	/*
	 * RSRP均值稍降门限
	 */
	public static String RSRP_AVAREGE_DECLINE = "RsrpAvaregeDecline";
	/*
	 * RSRP差值恶化门限(dBm)
	 */
	public static String RSRP_DIFFERENCE_VORSEN = "RsrpDifferenceVorsen";
	/*
	 * RSRP差值稍降门限
	 */
	public static String RSRP_DIFFERENCE_DECLINE = "RsrpDifferenceDecline";
	/*
	 * SINR均值恶化门限
	 */
	public static String SINR_AVAREGE_VORSEN = "SinrAvaregeVorsen";
	/*
	 * SINR差值恶化门限
	 */
	public static String SINR_DIFFERENCE_VORSEN = "SinrDifferenceVorsen";
	/*
	 * SINR均值稍降门限
	 */
	public static String SINR_AVAREGE_DECLINE = "SinrAvaregeDecline";
	/*
	 * SINR差值稍降门限
	 */
	public static String SINR_DIFFERENCE_DECLINE = "SinrDifferenceDecline";
	/*
	 * RTP丢包率均值恶化门限(%)
	 */
	public static String RTP_LOSEPAGE_AVAREGE_VORSEN = "RtpLosePageAvaregeVorsen";
	/*
	 * RTP丢包率差值恶化门限(%)
	 */
	public static String RTP_LOSEPAGE_DIFFERENCE_VORSEN = "RtpLosePageDifferenceVorsen";
	/*
	 * RTP丢包率均值稍降门限(%)
	 */
	public static String RTP_LOSEPAGE_AVAREGE_DECLINE = "RtpLosePageAvaregeDecline";
	/*
	 * RTP丢包率差值稍降门限(%)
	 */
	public static String RTP_LOSEPAGE_DIFFERENCE_DECLINE = "RtpLosePageDifferenceDecline";
	/*
	 * 栅格大小(米)
	 */
	public static String GRIDSIZE = "GridSize";

	/**
	 * 5G-干扰质差分析门限类型
	 */
	public static String DISTURB_ANALYSIS = "DisturbAnalysis";
	/**
	 * 5G-embb覆盖分析门限类型
	 */
	public static String EMBB_COVER_ANALYSIS = "EmbbCoverAnalysis";
	/**
	 * 5G-地图参数配置
	 */
	public static String MAP_PARAM = "MapParam";
	
	/**
	 * 弱覆盖参数设置
	 */
	public static String EMBB_WEAK_COVER_ANALYSIS = "EmbbWeakCoverAnasis";
	
	/**
	 * 过覆盖参数设置
	 */
	public static String EMBB_OVER_COVER_ANALYSIS = "EmbbOverCoverAnasis";
	
	/**
	 * 重叠覆盖参数设置
	 */
	public static String EMBB_OVERLAY_COVER_ANALYSIS = "EmbbOverlayCoverAnasis";
	
	/**
	 * 超远覆盖参数设置
	 */
	public static String EMBB_SPFAR_COVER_ANALYSIS = "EmbbSpfarCoverAnasis";
	
	/**
	 * 反向覆盖参数设置
	 */
	public static String EMBB_REVERSE_COVER_ANALYSIS = "EmbbReverseCoverAnasis";
	
	/**
	 * 质差参数设置
	 */
	public static String EMBB_QUALITY_BAD_ANALYSIS = "EmbbQualityBadAnasis";
	
	/**
	 * 地图图例设置名称
	 */
	public static String COLOR_MAP_ETG_TRAL = "ColorMapEtgTral";
	
	/**
	 * 地图图例样式类型
	 */
	public static String TARGET_CHOOSE_DATA = "TargetChooseData";
	
	/**
	 * 地图图例pci样式
	 */
	public static String PCI_MAP_COLOR = "pciMapColor";

	/**
	 * 感知监控门限
	 */
	public static String KNOW_FEELING_MONITOR = "knowFeelingMonitor";
	
}
