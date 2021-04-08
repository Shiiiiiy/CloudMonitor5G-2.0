/**
 * 
 */
package com.datang.constant;

/**
 * 小区SQL常量
 * 
 * @author yinzhipeng
 * @date:2015年11月11日 下午3:51:18
 * @version
 */
public interface CellSQLConstant {

	public final static String LTE_CELL_TABLE = "IADS_LTE_CELL";// LTE小区表名
	public final static String CELL_INFO_TABLE = "IADS_CELL_INFO";// 小区信息表名
	public final static String TERMINAL_GROUP_TABLE = "IADS_TERMINAL_GROUP";// 设备组表名
	public final static String TESTLOG_ITEM_TABLE = "IADS_TESTLOG_ITEM";// 测试日志表名

	// 小区表
	public final static String CELL_ANT_HIGH = "ANT_HIGH";
	public final static String CELL_AZIMUTHE = "AZIMUTH";
	public final static String CELL_BAND_WIDTH1 = "BAND_WIDTH1";
	public final static String CELL_BAND_WIDTH2 = "BAND_WIDTH2";
	public final static String CELL_BEAM_WIDTH = "BEAM_WIDTH";
	public final static String CELL_CELL_ID = "CELL_ID";
	public final static String CELL_CELL_NAME = "CELL_NAME";
	public final static String CELL_DOOR_TYPE = "DOOR_TYPE";
	public final static String CELL_ENB_ID = "ENB_ID";
	public final static String CELL_ELEC_TILT = "ELEC_TILT";
	public final static String CELL_FREQ_COUNT = "FREQ_COUNT";
	public final static String CELL_FREQUENCY1 = "FREQUENCY1";
	public final static String CELL_FREQUENCY10 = "FREQUENCY10";
	public final static String CELL_FREQUENCY11 = "FREQUENCY11";
	public final static String CELL_FREQUENCY2 = "FREQUENCY2";
	public final static String CELL_FREQUENCY3 = "FREQUENCY3";
	public final static String CELL_FREQUENCY4 = "FREQUENCY4";
	public final static String CELL_FREQUENCY5 = "FREQUENCY5";
	public final static String CELL_FREQUENCY6 = "FREQUENCY6";
	public final static String CELL_FREQUENCY7 = "FREQUENCY7";
	public final static String CELL_FREQUENCY8 = "FREQUENCY8";
	public final static String CELL_FREQUENCY9 = "FREQUENCY9";
	public final static String CELL_LATITUDE = "LATITUDE";
	public final static String CELL_LOCAL_CELL_ID = "LOCAL_CELL_ID";
	public final static String CELL_LONGITUDE = "LONGITUDE";
	public final static String CELL_MCC = "MCC";
	public final static String CELL_MECH_TILT = "MECH_TILT";
	public final static String CELL_MME_GROUP_ID = "MME_GROUP_ID";
	public final static String CELL_MME_ID = "MME_ID";
	public final static String CELL_MNC = "MNC";
	public final static String CELL_PCI = "PCI";
	public final static String CELL_REGION = "REGION";
	public final static String CELL_SECTOR_TYPE = "SECTOR_TYPE";
	public final static String CELL_SITE_NAME = "SITE_NAME";
	public final static String CELL_TAC = "TAC";
	public final static String CELL_TOTAL_TILT = "TOTAL_TILT";
	public final static String CELL_V_BEAM_WIDTH = "V_BEAM_WIDTH";
	public final static String CELL_CI_ID = "CI_ID";

	public final static String MCC = "MCC"; // MCC
	public final static String MNC = "MNC"; // MNC
	public final static String MMEGROUPID = "MMEGROUPID"; // MMEGROUPID
	public final static String MMEID = "MMEID"; // MMEID
	public final static String ENB_ID = "ENODEBID"; // ENODEBID
	public final static String SITENAME = "SITENAME"; // SITENAME
	public final static String CELL_NAME = "CELLNAME"; // CELLNAME
	public final static String LOCALCELLI = "LOCALCELLI"; // LOCALCELLI
	public final static String CELL_ID = "CELLID";// CELLID
	public final static String TAC = "TAC"; // TAC
	public final static String PCI = "PCI"; // PCI
	public final static String FREQ1 = "FREQ1"; // FREQ1
	public final static String FREQ2 = "FREQ2"; // FREQ2
	public final static String BANDWIDTH1 = "BANDWIDTH1";// BANDWIDTH1
	public final static String BANDWIDTH2 = "BANDWIDTH2"; // BANDWIDTH2
	public final static String FREQCOUNT = "FREQCOUNT"; // FREQCOUNT
	public final static String LONGITUDE = "LONGITUDE";// LONGITUDE
	public final static String LATITUDE = "LATITUDE";// LATITUDE
	public final static String SECTOR_TYPE = "SECTORTYPE"; // SECTORTYPE
	public final static String DOORTYPE = "DOORTYPE"; // DOORTYPE
	public final static String TILTTOTAL = "TILTTOTAL"; // TILTTOTAL
	public final static String TILTM = "TILTM"; // TILTM
	public final static String TILTE = "TILTE"; // TILTE
	public final static String AZIMUTHE = "AZIMUTH"; // AZIMUTH
	public final static String BEAM_WIDTH = "BEAMWIDTH";// BEAMWIDTH
	public final static String MODEL3 = "MODEL3";
	public final static String MODEL_TAC = "MOD_TAC";
	public final static String HEIGHT = "HEIGHT";
	public final static String REGION = "REGION";

	// 小区信息表
	public final static String CELL_INFO_ID = "ID";
	public final static String CELL_INFO_OPERATOR_TYPE = "OPERATOR_TYPE";
	public final static String CELL_INFO_TID = "TID";

	// 设备组表
	public final static String TERMINAL_GROUP_ID = "ID";
	public final static String TERMINAL_GROUP_NAME = "NAME";

	// 测试日志表
	public final static String TESTLOG_ITEM_OPERATOR_NAME = "OPERATOR_NAME";
	public final static String TESTLOG_ITEM_TERMINAL_GROUP = "TERMINAL_GROUP";
	public final static String TESTLOG_ITEM_RECSEQNO = "RECSEQNO";

}
