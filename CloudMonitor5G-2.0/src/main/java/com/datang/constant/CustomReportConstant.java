package com.datang.constant;

import com.datang.common.util.DataInfo;

import java.util.List;
import java.util.Map;

/**
 * 自定义报告模板输出常量
 * @author lucheng
 * @date 2021年3月19日 上午10:58:57
 */
public interface CustomReportConstant {
	/**
	 * 测试卡名称
	 */
	public final static String G_CARD_UNICOM = "联通卡";
	public final static String G_CARD_TELCOM = "电信卡";
	public final static String G_CARD_MOBILE = "移动卡";
	/**
	 * DT报告
	 */
	public final static String G_CONTRATOR_ALL = "总体";
	public final static String G_CONTRATOR_ALL_UNICOM = "联通承建总体";
	public final static String G_CONTRATOR_ALL_TELCOM = "电信承建总体";
	public final static String G_CONTRATOR_UNICOM = "联通承建";
	public final static String G_CONTRATOR_TELCOM = "电信承建";

	/**
	 * CQT报告
	 */
	public final static String G_SCENE_ALL = "场景汇总";
	public final static String G_SCENE_NAME_ALL = "场点汇总";
	public final static String G_CONTRATOR_ALL_CQT = "电联承建汇总";

	/**
	 * DT表一测试用例
	 */
	public final static String[] DT_ALL_USE_CASE_1 = {
			"1.1联通卡5G网络DT室外下行业务测试",
			"1.2电信卡5G网络DT室外下行业务测试",
			"1.3移动卡5G网络DT室外下行业务测试"
	};

	/**
	 * DT表一测试用例
	 */
	public final static String[] DT_ALL_USE_CASE_1_2 = {"8.1联通卡5G网络DT室外百度云盘下载业务测试", "8.2电信卡5G网络DT室外百度云盘下载业务测试",
			"8.3移动卡5G网络DT室外百度云盘下载业务测试"};
	/**
	 * DT表二测试用例
	 **/
	public final static String[] DT_ALL_USE_CASE_2 = {"2.1联通卡5G网络DT室外上行业务测试",
			"2.2电信卡5G网络DT室外上行业务测试",
			"2.3移动卡5G网络DT室外上行业务测试"
	};



	/**
     * DT表三测试用例
	 **/
	public final static String[] DT_ALL_USE_CASE_3 = {
			"3.1联通卡5G网络DT室外EPSFallBack语音业务测试主叫",
			"3.2电信卡5G网络DT室外EPSFallBack语音业务测试主叫",
			"3.3移动卡5G网络DT室外EPSFallBack语音业务测试主叫",
	//		"3.4联通卡5G网络DT室外EPSFallBack语音业务测试被叫",
	//		"3.5电信卡5G网络DT室外EPSFallBack语音业务测试被叫",
	//		"3.6移动卡5G网络DT室外EPSFallBack语音业务测试被叫"
	};

    /**
     * DT表四测试用例
     */									    
    public final static String[] DT_ALL_USE_CASE_4 = {"7.1联通卡4G网络DT室外4G上行下行串行测试","7.2电信卡4G网络DT室外4G上行下行串行测试","7.3移动卡4G网络DT室外4G上行下行串行测试"};

    /**
	 * DT表5测试用例
	 */
	public final static String[] DT_ALL_USE_CASE_5 = {
			"9.1联通卡4G网络DT室外VoLTE语音业务测试主叫",
	//		"9.4联通卡4G网络DT室外VoLTE语音业务测试被叫"
	};

	/**
	 * DT表6测试用例
	 */
	public final static String[] DT_ALL_USE_CASE_6 = {
			"10.1联通卡4G网络DT室外CSFB语音业务测试主叫",
	//		"10.4联通卡4G网络DT室外CSFB语音业务测试被叫"
	};
	/**
	 * DT表7测试用例
	 */
	public final static String[] DT_ALL_USE_CASE_7 = {"8.1联通卡5G网络DT室外百度云盘下载业务测试","8.2电信卡5G网络DT室外百度云盘下载业务测试","8.3移动卡5G网络DT室外百度云盘下载业务测试"};




	/**
     * CQT表一测试用例
     */	
    public final static String[] CQT_ALL_USE_CASE_1 = {"11.1联通卡5G网络CQT室内下行业务测试",
														"11.2电信卡5G网络CQT室内下行业务测试",
														"11.3移动卡5G网络CQT室内下行业务测试"};
	/**
     * CQT表二测试用例
     */	
	public final static String[] CQT_ALL_USE_CASE_2 = {"12.1联通卡5G网络CQT室内上行业务测试",
															"12.2电信卡5G网络CQT室内上行业务测试",
															"12.3移动卡5G网络CQT室内上行业务测试"};
	/**
     * CQT表三测试用例
     */
	public final static String[] CQT_ALL_USE_CASE_3 = {"13.1联通卡5G网络CQT室内EPSFallBack语音业务测试主叫",
														"13.2电信卡5G网络CQT室内EPSFallBack语音业务测试主叫",
														"13.3移动卡5G网络CQT室内EPSFallBack语音业务测试主叫",
	//													"13.4联通卡5G网络CQT室内EPSFallBack语音业务测试被叫",
	//													"13.5电信卡5G网络CQT室内EPSFallBack语音业务测试被叫",
	//													"13.6移动卡5G网络CQT室内EPSFallBack语音业务测试被叫"
	};
    /**
     * CQT表四测试用例
     */	 
	public final static String[] CQT_ALL_USE_CASE_4 = {"14.1联通卡5G网络CQT室内高清视频业务测试",
															"14.2电信卡5G网络CQT室内高清视频业务测试",
															"14.3移动卡5G网络CQT室内高清视频业务测试"};
	/**
	 * CQT表5测试用例
	 */
	public final static String[] CQT_ALL_USE_CASE_5 = {"19.1联通卡4G网络CQT室内VoLTE语音业务测试主叫",
	//		"19.4联通卡4G网络CQT室内VoLTE语音业务测试被叫"
	};
	/**
	 * CQT表6测试用例
	 */
	public final static String[] CQT_ALL_USE_CASE_6 = {"20.1联通卡4G网络DT室外CSFB语音业务测试主叫",
	//		"20.4联通卡4G网络DT室外CSFB语音业务测试被叫"
	};

	/**
	 * CQT表7测试用例
	 */
	public final static String[] CQT_ALL_USE_CASE_7 = {"17.1联通卡4G网络CQT室内上行下行串行业务测试"};







	/**
     * DT移动承建方名称汇总
     */	
	public final static String[] DT_CONTRATOR_ALL_MOBILE = {"总体"};
	
    /**
     * DT电信联通承建方名称汇总
     */	
	public final static String[] DT_CONTRATOR_ALL_UNICOM_TELCOM = {"总体","联通承建总体","电信承建总体"};
	
    /**
     * CQT移动承建方名称汇总
     */	
	public final static String[] CQT_CONTRATOR_ALL_MOBILE = {"汇总"};
	
    /**
     * CQT电信联通承建方名称汇总
     */	
	public final static String[] CQT_CONTRATOR_ALL_UNICOM_TELCOM = {G_CONTRATOR_ALL_CQT,"联通承建汇总","电信承建汇总"};
	
	/**
	 * 联通汇总开始的列索引
	 */
	public final static Integer START_COL_UNICOM = 6;
	
	/**
	 * 电信汇总开始的列索引
	 */
	public final static Integer START_COL_TELCOM = 9;
	
	/**
	 * 移动汇总开始的列索引
	 */
	public final static Integer START_COL__MOBILE = 12;
	
	/**
	 * 联通SA汇总开始的列索引
	 */
	public final static Integer START_COL_UNICOM_SA = 13;
	
	/**
	 * 电信SA汇总开始的列索引
	 */
	public final static Integer START_COL_TELCOM_SA = 16;
	
	/**
	 * 移动SA汇总开始的列索引
	 */
	public final static Integer START_COL__MOBILE_SA = 19;
	
	/**
	 * DT的汇总表的行（和DT_COL1一一对应）
	 */
	public final static Integer[] DT_ROW1 = {4,5,6,7,8,9,10,11,12,13,14,15,16,27,28,29,30,31,32,33,34,35,36,37,38};
	
	/**
	 * DT的汇总表的行和表一的列映射
	 */
	public final static String[] DT_COL1 = {"AG","M","N","O","BE","BI","BL","BO","BR","EU","AP","AR","AT","BS","BV","BX","BY","BZ","CL","BS","BV","BX","BY","BZ","CL"};
	
	/**
	 * DT的汇总表的行SA（和DT_COL1_SA一一对应）
	 */
	public final static Integer[] DT_ROW1_SA = {4,5,6,7,8,9,10,11,12,13,14,15,16,27,28,29,30,31,32,33,34,35,36,37,38};
	
	/**
	 * DT的汇总表的行SA和表一的列映射
	 */
	public final static String[] DT_COL1_SA = {"DH","M","N","O","EF","EJ","EM","EP","ES","EU","DQ","DS","DU","EV","EY","FA","FB","FC","FO","EV","EY","FA","FB","FC","FO"};
	
	/**
	 * DT的汇总表的行（和DT_COL2一一对应）
	 */
	public final static Integer[] DT_ROW2 = {17,18,19};
	
	/**
	 * DT的汇总表的行和表二的列映射
	 */
	public final static String[] DT_COL2 = {"AQ","AU","AW"};
	
	/**
	 * DT的汇总表的行SA（和DT_COL2_SA一一对应）
	 */
	public final static Integer[] DT_ROW2_SA = {17,18,19};
	
	/**
	 * DT的汇总表的行SA和表二的列映射
	 */
	public final static String[] DT_COL2_SA = {"DR","DV","DX"};
	
	/**
	 * DT的汇总表的行（和DT_COL3一一对应）
	 */
	public final static Integer[] DT_ROW3 = {24,25,26};
	
	/**
	 * DT的汇总表的行和表三的列映射
	 */
	public final static String[] DT_COL3 = {"AJ","AK","Z"};
	
	/**
	 * DT的汇总表的行SA（和DT_COL3_SA一一对应）
	 */
	public final static Integer[] DT_ROW3_SA = {24,25,26};
	
	/**
	 * DT的汇总表的行SA和表三的列映射
	 */
	public final static String[] DT_COL3_SA = {"AJ","AK","Z"};
	
	/**
	 * DT的汇总表的行（和DT_COL4一一对应）
	 */
	public final static Integer[] DT_ROW4 = {39,40,41,42,43};
	
	/**
	 * DT的汇总表的行和表四的列映射
	 */
	public final static String[] DT_COL4 = {"S","Y","Z","AF","ZG"};
	
	/**
	 * DT的汇总表的行SA（和DT_COL4_SA一一对应）
	 */
	public final static Integer[] DT_ROW4_SA = {};
	
	/**
	 * DT的汇总表的行SA和表四的列映射
	 */
	public final static String[] DT_COL4_SA = {};
	
	//cqt
	/**
	 * CQT的汇总表的行（和CQT_COL1一一对应）
	 */
	public final static Integer[] CQT_ROW1 = {4,5,6,7,8,9,10,11,12,13,14,15,16,27,28,29,30,31,32};
	
	/**
	 * CQT的汇总表的行和表一的列映射
	 */
	public final static String[] CQT_COL1 = {"AH","N","O","P","BF","BJ","BM","BP","BS","EV","AQ","AS","AU","BT","BW","BY","BZ","CA","CM"};
	
	/**
	 * CQT的汇总表的行SA（和CQT_COL1_SA一一对应）
	 */
	public final static Integer[] CQT_ROW1_SA = {4,5,6,7,8,9,10,11,12,13,14,15,16,27,28,29,30,31,32};
	
	/**
	 * CQT的汇总表的行SA和表一的列映射
	 */
	public final static String[] CQT_COL1_SA = {"DI","N","O","P","EG","EK","EN","EQ","ET","EV","DR","DT","DV","EW","EZ","FB","FC","FD","FP"};
	
	/**
	 * CQT的汇总表的行（和CQT_COL2一一对应）
	 */
	public final static Integer[] CQT_ROW2 = {17,18,19};
	
	/**
	 * CQT的汇总表的行和表二的列映射
	 */
	public final static String[] CQT_COL2 = {"AR","AV","AX"};
	
	/**
	 * CQT的汇总表的行SA（和CQT_COL2_SA一一对应）
	 */
	public final static Integer[] CQT_ROW2_SA = {17,18,19};
	
	/**
	 * CQT的汇总表的行SA和表二的列映射
	 */
	public final static String[] CQT_COL2_SA = {"DS","DW","DY"};
	
	/**
	 * CQT的汇总表的行（和CQT_COL3一一对应）
	 */
	public final static Integer[] CQT_ROW3 = {24,25,26};
	
	/**
	 * CQT的汇总表的行和表三的列映射
	 */
	public final static String[] CQT_COL3 = {"W","AN","AA"};
	
	/**
	 * CQT的汇总表的行SA（和CQT_COL3_SA一一对应）
	 */
	public final static Integer[] CQT_ROW3_SA = {24,25,26};
	
	/**
	 * CQT的汇总表的行SA和表三的列映射
	 */
	public final static String[] CQT_COL3_SA = {"W","AN","AA"};
	
	/**
	 * CQT的汇总表的行（和CQT_COL4一一对应）
	 */
	public final static Integer[] CQT_ROW4 = {40,41,42};
	
	/**
	 * CQT的汇总表的行和表四的列映射
	 */
	public final static String[] CQT_COL4 = {"AH","AP","AX"};
	
	/**
	 * CQT的汇总表的行SA（和CQT_COL4_SA一一对应）
	 */
	public final static Integer[] CQT_ROW4_SA = {40,41,42};
	
	/**
	 * CQT的汇总表的行SA和表四的列映射
	 */
	public final static String[] CQT_COL4_SA = {"AH","AP","AX"};
	


	DataInfo[] CQT_SUMMARY_TABLE1 = {
			new DataInfo(4,"M", DataInfo.Type.Sum,"#.##"),
			new DataInfo(5,"AH"),
			new DataInfo(6,new String[]{"N","O"}),
			new DataInfo(8,"AQ","#.##")
	};

	DataInfo[] CQT_SUMMARY_TABLE2 = {
			new DataInfo(9,"AR","#.##")
	};

	DataInfo[] CQT_SUMMARY_TABLE3 = {
			new DataInfo(11,"W"),
			new DataInfo(12,"AN"),
			new DataInfo(13,"AA")
	};

	DataInfo[] CQT_SUMMARY_TABLE5 = {
			new DataInfo(25,"AN"),
			new DataInfo(26,"AO"),
			new DataInfo(27,"AW"),
	};
	DataInfo[] CQT_SUMMARY_TABLE6 = {
			new DataInfo(28,"U"),
			new DataInfo(29,"Z"),
			new DataInfo(30,"AG"),
	};
	DataInfo[] CQT_SUMMARY_TABLE7 = {
			new DataInfo(20,"M","#.##"),
			new DataInfo(21,"AC"),
			new DataInfo(22,"Q"),
			new DataInfo(23,"AI","#.##"),
			new DataInfo(24,"AP","#.##"),
	};


	DataInfo[] DT_SUMMARY_TABLE1 = {
			new DataInfo(4,"K", DataInfo.Type.Sum,"#.##"),
			new DataInfo(5,"J",DataInfo.Type.Sum,"#.##"),
			new DataInfo(6,"AG"),
			new DataInfo(7,new String[]{"M","N"}),
			new DataInfo(9,"AP","#.##")
	};

	DataInfo[] DT_SUMMARY_TABLE2 = {
			new DataInfo(10,"AQ","#.##")
	};
	DataInfo[] DT_SUMMARY_TABLE7 = {
			new DataInfo(11,"U","#.##")
	};

	DataInfo[] DT_SUMMARY_TABLE3 = {
			new DataInfo(12,"V"),
			new DataInfo(13,"AM"),
			new DataInfo(14,"Z"),
			new DataInfo(15,"AR")
	};



	DataInfo[] DT_SUMMARY_TABLE4 = {
			new DataInfo(20,"K", DataInfo.Type.Sum,"#.##"),
			new DataInfo(21,"J", DataInfo.Type.Sum,"#.##"),
			new DataInfo(22,"AB"),
			new DataInfo(23,"P"),
			new DataInfo(24,"AH","#.##"),
			new DataInfo(25,"AO","#.##")
	};


	DataInfo[] DT_SUMMARY_TABLE5 = {

			new DataInfo(26,"AM"),
			new DataInfo(27,"AN"),
			new DataInfo(28,"AV")
	};
	DataInfo[] DT_SUMMARY_TABLE6 = {

			new DataInfo(29,"T"),
			new DataInfo(30,"Y"),
			new DataInfo(31,"AF")
	};




}
