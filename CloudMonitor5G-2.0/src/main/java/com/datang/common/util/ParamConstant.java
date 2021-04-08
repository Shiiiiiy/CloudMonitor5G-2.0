package com.datang.common.util;

public interface ParamConstant {

	/*------------------------------------参数名----------------------------------------*/
	/**
	 * 报表类型
	 */
	public static final String REPOR_TTYPE = "reportType";

	/**
	 * 一级分类
	 */
	public static final String STAIR_CLASSIFY = "stairClassify";

	/**
	 * 测试日志ids
	 */
	public static final String TESTLOGITEM_IDS = "testLogItemIds";

	/**
	 * 动态KPI表ID,即TestLogItem的id
	 */
	public static final String RECSEQNO = "recseqno";
	/**
	 * 动态KPI表场景
	 */
	public static final String KEYPOINTNO = "keypointno";
	/**
	 * 动态KPI表楼层
	 */
	public static final String FLOORNO = "floorno";

	/**
	 * 字段分组
	 */
	public static final String GROUPBY_FIELD = "groupByField";
	/**
	 * 查询表名
	 */
	public static final String QUERY_TABLE = "queryTable";

	/**
	 * volte整体分析,报表导出查询的数据表
	 */
	public static final String VOLTE_DATA_TABLE = "IADS_ADT_LTE_DATA";
	/**
	 * 按楼层分类的数据表
	 */
	public static final String VOLTE_DATA_FLOORS_TABLE = "IADS_ADT_LTE_DATA_FLOOR";
	/**
	 * 按场景分类的数据表
	 */
	public static final String VOLTE_DATA_KEYPOINT_TABLE = "IADS_ADT_LTE_DATA_KEYPOINT";

	/**
	 * 报表类型:volte报表
	 */
	public static final String REPOR_TTYPE_VOLTE = "VoLTE报表";
	/**
	 * 报表类型:5G报表
	 */
	public static final String REPOR_TTYPE_5G = "5G报表";
	/**
	 * 报表类型:5G数据业务报表
	 */
	public static final String REPOR_TTYPE_5G_DATA = "5G数据业务报表";
	/**
	 * 报表类型:5G异常事件报表
	 */
	public static final String REPOR_TTYPE_5G_EE = "5G异常事件报表";
	/**
	 * 报表类型:NSA指标报表
	 */
	public static final String REPOR_TTYPE_NSA_INDEX = "NSA指标报表";
	/**
	 * 报表类型:测试轨迹报表
	 */
	public static final String REPOR_TTYPE_TEST_TRAIL= "测试轨迹报表";
	/**
	 * 报表类型:NBIoT报表
	 */
	public static final String REPOR_TTYPE_NBIOT = "NBIoT报表";
	/**
	 * 报表分类:业务统计指标
	 */
	public static final String BUSINESS_STATISTICS_INDEX = "业务统计指标";
	/**
	 * 报表分类:覆盖类指标占比统计
	 */
	public static final String COVER_INDEX_PROPORTION = "覆盖类指标占比统计";
	/**
	 * 报表分类:KPI汇总
	 */
	public static final String STAIR_CLASSIFY_TOTAL_KPI = "KPI汇总";
	/**
	 * 报表分类:VOLTE统计指标
	 */
	public static final String STAIR_CLASSIFY_VOLTE = "VOLTE统计指标";
	/**
	 * 报表分类:CS域语音统计指标
	 */
	public static final String STAIR_CLASSIFY_CS = "CS域语音统计指标";
	/**
	 * 报表分类:NSA指标报表-指标汇总
	 */
	public static final String STAIR_CLASSIFY_INDEX_SUMMARY = "指标汇总";
	/**
	 * 报表分类:NSA指标报表-覆盖类
	 */
	public static final String STAIR_CLASSIFY_COVER = "覆盖类";
	/**
	 * 报表分类:NSA指标报表-业务类
	 */
	public static final String STAIR_CLASSIFY_BUSINESS = "业务类";
	/**
	 * 报表分类:5G报表-覆盖类指标统计
	 */
	public static final String STAIR_CLASSIFY_COVER_5G = "覆盖类指标统计";
	/**
	 * 报表分类:干扰类
	 */
	public static final String STAIR_CLASSIFY_DISTURB = "干扰类";
	/**
	 * 报表分类:5G报表-干扰类指标统计
	 */
	public static final String STAIR_CLASSIFY_DISTURB_5G = "干扰类指标统计";
	/**
	 * 报表分类:分段占比统计
	 */
	public static final String SUB_INDEX_PROPORTION = "分段占比统计";
	/**
	 * 报表分类:调度类
	 */
	public static final String STAIR_CLASSIFY_DISPATCHER = "调度类";
	/**
	 * 报表分类:MOS统计
	 */
	public static final String STAIR_CLASSIFY_MOS = "MOS统计";
	/**
	 * 报表分类:VoTLE整体概览
	 */
	public static final String STAIR_CLASSIFY_VOLTE_TOTAL = "VoTLE整体概览";
	/**
	 * 报表分类:5G报表-对比分析
	 */
	public static final String STAIR_CLASSIFY_5G_COMP = "对比分析";
	/**
	 * 报表分类:VoLTE异常事件
	 */
	public static final String STAIR_CLASSIFY_VOLTE_EE = "VoLTE异常事件";
	/**
	 * 报表分类:VoLTE切换失败
	 */
	public static final String STAIR_CLASSIFY_VOLTE_HOF = "VoLTE切换失败";
	/**
	 * 报表类型:LTE数据业务报表
	 */
	public static final String REPOR_TTYPE_LTE = "LTE数据业务报表";
	/**
	 * 报表类型:流媒体报表
	 */
	public static final String REPOR_TTYPE_STREAM = "流媒体报表";
	/**
	 * 报表分类：数据业务统计指标
	 */
	public static final String STAIR_CLASSIFY_INDEX = "数据业务统计指标";

	/**
	 * 报表分类：5G报表-数据业务（5G）异常事件统计指标
	 */
	public static final String STAIR_CLASSIFY_5G_EE = "数据业务（5G）异常事件统计指标";
	/**
	 * 报表分类：5G报表-波束类统计指标
	 */
	public static final String STAIR_CLASSIFY_5G_BEAM = "波束类指标统计";
	/**
	 * 报表分类：5G报表-接入类异常事件
	 */
	public static final String STAIR_CLASSIFY_5G_INSERT = "接入类异常事件";

	/**
	 * 报表分类：5G报表-移动类异常事件
	 */
	public static final String STAIR_CLASSIFY_5G_MOVE = "移动类异常事件";
	/**
	 * 报表分类:5G报表-语音业务（4G）异常事件统计指标
	 */
	public static final String STAIR_CLASSIFY_5G_VOICE_EE = "语音业务（4G）异常事件统计指标";
	/**
	 * 报表分类:5G报表-会话类异常事件
	 */
	public static final String STAIR_CLASSIFY_5G_VOICE_PDU = "会话类异常事件";

	/**
	 * 报表分类：感知类指标
	 */
	public static final String STAIR_CLASSIFY_PERCEPTION_INDEX = "感知类指标";
	/**
	 * 报表分类：NSA指标报表-移动类
	 */
	public static final String STAIR_CLASSIFY_MOVE = "移动类";
	/**
	 * 报表分类：NSA指标报表-接入类
	 */
	public static final String STAIR_CLASSIFY_INSERT = "接入类";
	/**
	 * 报表分类：测试轨迹报表-RSRP指标统计
	 */
	public static final String  STAIR_CLASSIFY_RSRP_TRAIL= "RSRP指标统计";
	/**
	 * 报表分类：测试轨迹报表-SINR指标统计
	 */
	public static final String  STAIR_CLASSIFY_SINR_TRAIL= "SINR指标统计";
	/**
	 * 报表分类：测试轨迹报表-FTP下行速率统计
	 */
	public static final String  STAIR_CLASSIFY_FTPDL_TRAIL= "FTP下行速率统计";
	/**
	 * 报表分类：测试轨迹报表-FTP上行速率指标
	 */
	public static final String  STAIR_CLASSIFY_FTPUL_TRAIL= "FTP上行速率指标";
	/**
	 * 报表分类：应用层FTP速率分段占比
	 */
	public static final String STAIR_CLASSIFY_FTP_SECTION = "应用层FTP速率分段占比统计";
	/**
	 * 报表分类：网络覆盖类分段占比统计
	 */
	public static final String STAIR_CLASSIFY_NETWORK_WERKCOVER = "网络覆盖类分段占比统计";
	/**
	 * 报表分类：停车测试
	 */
	public static final String STAIR_CLASSIFY_STOP = "停车测试";
	/**
	 * 报表类型:VoLTE视频报表
	 */
	public static final String REPOR_TTYPE_VOLTE_VIDEO = "VoLTE视频报表";
	/**
	 * 报表分类：KPI统计
	 */
	public static final String STAIR_CLASSIFY_KPISTATISYICE = "KPI统计";
	/**
	 * 报表分类：质量类
	 */
	public static final String STAIR_CLASSIFY_QUALITY = "质量类";
	/**
	 * 报表分类：资源类
	 */
	public static final String STAIR_CLASSIFY_RESOURCE = "资源类";
	/**
	 * 报表分类：感知类
	 */
	public static final String STAIR_CLASSIFY_PERCEPTION = "感知类";
	/**
	 * startDate field name
	 */
	public static final String START_TIME = "startTime";

	/**
	 * endDate field name
	 */
	public static final String END_TIME = "endTime";

	/**
	 * endDate field name
	 */
	public static final String MEA_TIME = "meaTime";

	/**
	 * anaHours field name
	 */
	public static final String ANAHOURS = "anaHours";

	/**
	 * neType field name
	 */
	public static final String NE_TYPE = "neType";

	/**
	 * nes field name
	 */
	public static final String NES = "nes";

	/**
	 * ueType field name
	 */
	public static final String UE_TYPE = "ueType";

	/**
	 * ues field name
	 */
	public static final String UES = "ues";

	/**
	 * kpis field name
	 */
	public static final String KPIS = "kpis";

	/**
	 * kpis2 field name
	 */
	public static final String KPIS2 = "kpis2";

	/**
	 * groupByNeObject field name
	 */
	public static final String GROUP_BY_NE_OBJECT = "groupByNeObject";

	/**
	 * groupByUeObject field name
	 */
	public static final String GROUP_BY_UE_OBJECT = "groupByUeObject";

	/**
	 * NE_TABLE field name
	 */
	public static final String NE_TABLE = "neTable";

	/**
	 * UE_TABLE field name
	 */
	public static final String UE_TABLE = "ueTable";

	/**
	 * groupByTime field name
	 */
	public static final String GROUP_BY_TIME = "groupByTime";

	/**
	 * indicationKpi field name
	 */
	public static final String INDICATION_KPI = "indicationKpi";

	/**
	 * indicationCondition field name
	 */
	public static final String INDICATION_CONDITION = "indicationCondition";

	/**
	 * orderByStatement field name
	 */
	public static final String ORDERBY_STATEMENT = "orderByStatement";

	/**
	 * mergeKpis field name
	 */
	public static final String MERGE_KPIS = "mergeKpis";

	/**
	 * 三维指标类型定义
	 */
	public static final String COMBO_KPI_TYPE = "comKpiType";

	/**
	 * mergeKpis field name
	 */
	public static final String THRESHOLDINDICATIONKPIS = "thresholdIndicationKpis";

	/**
	 * mergeKpis field name
	 */
	public static final String CONDITIONSVALUE = "conditionsValue";

	/**
	 * orderByName field name
	 */
	public static final String ORDERBY_NAME = "orderByName";

	/**
	 * orderByType field name
	 */
	public static final String ORDERBY_TYPE = "orderByType";

	/**
	 * pageQuery field name
	 */
	public static final String PAGE_QUERY = "pageQuery";

	/**
	 * curPage field name
	 */
	public static final String CUR_PAGE = "curPage";

	/**
	 * pageSize field name
	 */
	public static final String PAGE_SIZE = "pageSize";

	/**
	 * pageSize field name
	 */
	public static final String TOP_N = "topN";

	/**
	 * qci field name
	 */
	public static final String QCI = "qci";

	/**
	 * failCause field name
	 */
	public static final String FAIL_CAUSE = "failCause";

	/**
	 * 主叫号码
	 */
	public static final String CALLING_NUMBER = "callingPartyNumber";

	/**
	 * 被叫号码
	 */
	public static final String CALLED_NUMBER = "calledPartyNumber";

	/**
	 * 协议类型
	 */
	public static final String INTERFACE_TYPE = "interfaceType";

	/**
	 * 协议类型
	 */
	public static final String SERVICE_TYPE = "serviceTypes";

	/**
	 * 订单号
	 */
	public static final String ORDERID = "orderId";

	/**
	 * CDR号
	 */
	public static final String CDRID = "cdrId";
	/**
	 * 地图显示类型
	 */
	public static final String MAPTYPE = "mapType";
	/**
	 * maxLongitude
	 */
	public static final String MAXLONGITUDE = "maxLongitude";
	/**
	 * maxLatitude
	 */
	public static final String MAXLATITUDE = "maxLatitude";
	/**
	 * minLongitude
	 */
	public static final String MINLONGITUDE = "minLongitude";
	/**
	 * minLatitude
	 */
	public static final String MINLATITUDE = "minLatitude";

	/**
	 * selectKpis
	 */
	public static final String SELECTTHRESHOLDKPIS = "selectThresholdKpis";

	/**
	 * 小区元指标
	 */
	public static final String CELLKPIS = "cellKpis";
	/**
	 * trendType
	 */
	public static final String TRENDTYPE = "trendType";

	/**
	 * anaTypestring
	 */
	public static final String ANA_TYPE_STRING = "anaTypestring";

	/*-------------------------------------结果信息--------------------------------------------*/

	/**
	 * anadate
	 */
	public static final String ANATIME = "anatime";

	/**
	 * anahour
	 */
	public static final String ANAHOUR = "anahour";

	/**
	 * mci
	 */
	public static final String MCI = "mci";

	/**
	 * bscid
	 */
	public static final String BSCID = "bscid";

	/**
	 * bi
	 */
	public static final String BI = "bi";

	/**
	 * bscname
	 */
	public static final String BSCNAME = "bscname";

	/**
	 * rncid
	 */
	public static final String RNCID = "rncid";

	/**
	 * ri =lacid*65536+rncid
	 */
	public static final String RI = "ri";

	/**
	 * rncname
	 */
	public static final String RNCNAME = "rncname";

	/**
	 * MSCID
	 */
	public static final String MSCID = "mscid";

	/**
	 * CICID
	 */
	public static final String CICID = "cicid";

	/**
	 * lacid
	 */
	public static final String LACID = "lacid";

	/**
	 * cellid
	 */
	public static final String CELLID = "cellid";

	/**
	 * ci
	 */
	public static final String CI = "ci";

	/**
	 * cellname
	 */
	public static final String CELLNAME = "cellname";

	/**
	 * pci
	 */
	public static final String PCI = "pci";

	/**
	 * freq1
	 */
	public static final String FREQ1 = "freq1";

	/**
	 * freq2
	 */
	public static final String FREQ2 = "freq2";

	/**
	 * mmes1apueid
	 */
	public static final String MMES1APUEID = "mmes1apueid";

	/**
	 * bscid
	 */
	public static final String BSCID_S = "bscid";

	/**
	 * bi
	 */
	public static final String BI_S = "bi";

	/**
	 * bscname
	 */
	public static final String BSCNAME_S = "bscname";

	/**
	 * rncid
	 */
	public static final String RNCID_S = "rncid";

	/**
	 * ri
	 */
	public static final String RI_S = "ri";

	/**
	 * rncname
	 */
	public static final String RNCNAME_S = "rncname";

	/**
	 * lacid
	 */
	public static final String LACID_S = "lacid";

	/**
	 * cellid
	 */
	public static final String CELLID_S = "cellid";

	/**
	 * ci
	 */
	public static final String CI_S = "ci";

	/**
	 * cellname
	 */
	public static final String CELLNAME_S = "cellname";

	/**
	 * bscid
	 */
	public static final String BSCID_T = "tbscid";

	/**
	 * bi
	 */
	public static final String BI_T = "tbi";

	/**
	 * bscname
	 */
	public static final String BSCNAME_T = "tbscname";

	/**
	 * rncid
	 */
	public static final String RNCID_T = "trncid";

	/**
	 * ri
	 */
	public static final String RI_T = "tri";

	/**
	 * rncname
	 */
	public static final String RNCNAME_T = "trncname";

	/**
	 * lacid
	 */
	public static final String LACID_T = "tlacid";

	/**
	 * cellid
	 */
	public static final String CELLID_T = "tcellid";

	/**
	 * tenodebid
	 */
	public static final String ENODEBID_T = "tenodebid";

	/**
	 * ci
	 */
	public static final String CI_T = "tci";

	/**
	 * cellname
	 */
	public static final String CELLNAME_T = "tcellname";

	/**
	 * 频点
	 */
	public static final String BCCH = "bcch";

	/**
	 * chi
	 */
	public static final String CHI = "chi";

	/**
	 * cell group id
	 */
	public static final String CELL_GROUP_ID = "groupid";

	/**
	 * cell group name
	 */
	public static final String CELL_GROUP_NAME = "groupname";

	/**
	 * creator name
	 */
	public static final String CELL_GROUP_CREATORNAME = "creatorname";

	/**
	 * imsi
	 */
	public static final String IMSI = "imsi";

	/**
	 * phonenumber
	 */
	public static final String PHONE_NUMBER = "phonenumber";

	/**
	 * msisdn
	 */
	public static final String MSISDN = "msisdn";

	/**
	 * spgroup
	 */
	public static final String SPGROUPID = "spgroup";

	/**
	 * username
	 */
	public static final String USERNAME = "username";

	/**
	 * spgroupname
	 */
	public static final String SPGROUPNAME = "groupname";

	/**
	 * spgroupname
	 */
	public static final String PRIORITY = "priority";

	/**
	 * imei
	 */
	public static final String IMEI = "imei";

	/**
	 * temanuf
	 */
	public static final String TEMANUF = "temanuf";

	/**
	 * temodel
	 */
	public static final String TEMODEL = "temodel";

	/**
	 * tac
	 */
	public static final String TAC = "tac";

	/**
	 * chipmanuf
	 */
	public static final String CHIPMANUF = "chipmanuf";

	/**
	 * chipmodel
	 */
	public static final String CHIPMODEL = "chipmodel";

	/**
	 * tetype
	 */
	public static final String TETYPE = "tetype";

	/**
	 * 终端制式
	 */
	public static final String STANDARD = "standard";

	/**
	 * WIFI能力
	 */
	public static final String WIFI = "wifi";

	/**
	 * 明星终端
	 */
	public static final String ISSTAR = "isstar";

	/**
	 * longitude
	 */
	public static final String LONGITUDE = "longitude";
	/**
	 * latitude
	 */
	public static final String LATITUDE = "latitude";
	/**
	 * gridlatunit
	 */
	public static final String GRIDLATUNIT = "gridLatunit";
	/**
	 * gridlongunit
	 */
	public static final String GRIDLONGUNIT = "gridLongunit";

	/**
	 * channelId
	 */
	public static final String CHANNELID = "channelId";
	/**
	 * boxId
	 */
	public static final String BOXID = "boxId";

	/**
	 * mlacid
	 */
	public static final String MLACID = "mlacid";

	/**
	 * mcellid
	 */
	public static final String MCELLID = "mcellid";

	/**
	 * adjlacid
	 */
	public static final String ADJLACID = "adjlacid";

	/**
	 * adjcellid
	 */
	public static final String ADJCELLID = "adjcellid";
	/**
	 * not in
	 */
	public static final String NOT_IN = "not in";
	/**
	 * beginTime
	 */
	public static final String BEGIN_TIME = "beginTime";
	/**
	 * nlacid
	 */
	public static final String NLACID = "nlacid";
	/**
	 * ncellid
	 */
	public static final String NCELLID = "ncellid";

	// 4G
	/**
	 * city
	 */
	public static final String CITY = "city";

	/**
	 * region
	 */
	public static final String REGION = "region";

	/**
	 * mmeid
	 */
	public static final String MMEID = "mmeid";

	/**
	 * company
	 */
	public static final String COMPANY = "company";
	/**
	 * enbtype
	 */
	public static final String ENBTYPE = "enbtype";
	/**
	 * enbversion
	 */
	public static final String ENBVERSION = "enbversion";

	/**
	 * enb
	 */
	public static final String ENODEBID = "enodebid";

	/**
	 * sitename
	 */
	public static final String SITENAME = "sitename";
	/**
	 * CAUSE
	 */
	public static final String CAUSE = "CAUSE";

	/**
	 * planName
	 */
	public static final String PLANNAME = "planName";
	/*------------------------------------------指标--------------------------------------------*/

	/**
	 * 坏小区数量
	 */
	public static final String BADCELLNUM = "badcellnumm";
	/**
	 * 坏小区标识
	 */
	public static final String BADCELLFLAG = "badcellflag";

	/**
	 * 指标:下行电平及质量同时有效时的MR数量
	 */
	public static final String MR_COUNT = "dlrxlevmrcnt";

	/**
	 * 指标:组合指标KPI1
	 */
	public static final String COM_KPI1 = "comkpi1";

	/**
	 * 指标:组合指标KPI2
	 */
	public static final String COM_KPI2 = "comkpi2";

	/**
	 * 指标:样本数
	 */
	public static final String SAMPLE_NUM = "comvalcount";
	/**
	 * 小区DSC类型
	 */
	public static final String DSCTYPE = "DCS";
	/**
	 * 小区室内类型
	 */
	public static final String DOORTYPE = "DoorType";

	/**
	 * 全网
	 */
	public static final String ALLNET = "全网";

	/**
	 * 网元列
	 */
	public static final String NE = "NE";

	/*------------------------------------------指标--------------------------------------------*/

	// 干扰分析
	/**
	 * 指标:干扰小区数量
	 */
	public static final String DISTURB_CELL_COUNT = "disturbcellcount";

	/**
	 * 指标:干扰程度
	 */
	public static final String DISTURB_RATIO = "dissetcntratio";

	/**
	 * 干扰标记
	 */
	public static final String DISTURB_FLAG = "disturbflag";

	// 短呼分析

	/**
	 * 指标:干扰小区数量
	 */
	public static final String SHORTCALL_CELL_COUNT = "shortcallcellcount";

	/**
	 * 指标:干扰程度
	 */
	public static final String SHORTCALL_RATIO = "shortcallratio";

	/**
	 * 干扰标记
	 */
	public static final String SHORTCALL_FLAG = "shortcallflag";

	// 链路均衡
	/**
	 * 指标:链路小区数量
	 */
	public static final String LinkLayer_CELL_COUNT = "linklayercellcount";
	/**
	 * 指标:链路程度
	 */
	public static final String LinkLayer_RATIO = "pldiffnoratio";

	/**
	 * 链路不平衡标记
	 */
	public static final String LinkLayer_FLAG = "linklayerflag";

	// 覆盖分析

	/**
	 * 指标:上行覆盖小区数量
	 */
	public static final String WEAK_COVER_UL_CELL_COUNT = "weakcoverulcellcount";

	/**
	 * 指标:下行覆盖小区数量
	 */
	public static final String WEAK_COVER_DL_CELL_COUNT = "weakcoverdlcellcount";

	/**
	 * 指标:上行覆蓋程度
	 */
	public static final String WEAK_COVER_UL_RATIO = "weakrxlevulratio";

	/**
	 * 指标:下行覆蓋程度
	 */
	public static final String WEAK_COVER_DL_RATIO = "weakrxlevdlratio";

	/**
	 * 上行覆盖标记
	 */
	public static final String WEAK_COVER_UL_FLAG = "weakcoverulflag";

	/**
	 * 下行覆盖标记
	 */
	public static final String WEAK_COVER_DL_FLAG = "weakcoverdlflag";

	/**
	 * 指标:过覆盖小区数量
	 */
	public static final String EXCESSIVE_COVER_CELL_COUNT = "excoverulcellcount";

	/**
	 * 指标:过覆蓋程度
	 */
	public static final String EXCESSIVE_COVER_RATIO = "exrxlevulratio";

	/**
	 * 过覆盖标记
	 */
	public static final String EXCESSIVE_COVER_FLAG = "excessivecoverulflag";

	/**
	 * 指标:重复覆盖小区数量
	 */
	public static final String REPEAT_COVER_CELL_COUNT = "recoverulcellcount";

	/**
	 * 指标:重复覆蓋程度
	 */
	public static final String REPEAT_COVER_RATIO = "rerxlevulratio";

	/**
	 * 重复覆盖标记
	 */
	public static final String REPEAT_COVER_FLAG = "recoverulflag";

	/**
	 * 覆盖空洞程度
	 */
	public static final String HOLLOW_COVER_RATIO = "hollowrxlevdlratio";

	/**
	 * 覆盖空洞小区总数
	 */
	public static final String HOLLOW_COVER_CELL_COUNT = "hollowrxlevcellcount";

	// 隐性故障
	public static final String CELL_RECESSIVEBAD_COUNT = "cellrecessivebadcount";
	public static final String BCCH_RECESSIVEBAD_COUNT = "bcchrecessivebadcount";

	public static final String ALL_CELL_COUNT = "allcellcount";
	public static final String HOP = "hop";

	// TEI异常次数
	public static final String TEIBAD_COUNT = "teibadcount";
	// TEI异常次数
	public static final String CELLBAD_COUNT = "cellbadcount";

	// 用户感知
	// 下行信号质量MR占比
	public static final String RXQUALDLMRNUM_RATIO = "rxqualdlmrnumratio";

	// 上行质差小区
	public static final String RXQUALULCELL_RATIO = "rxqualulcellratio";

	// 下行质差小区
	public static final String RXQUALDLCELL_RATIO = "rxqualdlcellratio";

	// public static final String RXQUALULMRNUM_AVG = "rxqualulmrnumavg";
	// public static final String RXQUALDLMRNUM_AVG = "rxqualdlmrnumavg";

	//

	// 质量分析

	/**
	 * 指标:质差小区数量
	 */
	public static final String QUALITY_BAD_CELL_COUNT = "qualitybadcellcount";

	/**
	 * 指标:质差MR数量
	 */
	public static final String QUALITY_BAD_MR_COUNT = "rxqualbadmrcount";

	/**
	 * 指标:质差程度
	 */
	public static final String QUALITY_BAD_RATIO = "rxqualbadratio";

	/**
	 * 质差标记
	 */
	public static final String QUALITY_BAD_FLAG = "qualitybadflag";

	/**
	 * 指标:质优小区数量
	 */
	public static final String QUALITY_GOOD_CELL_COUNT = "qualitygoodcellcount";

	/**
	 * 指标:质优MR数量
	 */
	public static final String QUALITY_GOOD_MR_COUNT = "rxqualgoodmrcount";

	/**
	 * 指标:质优程度
	 */
	public static final String QUALITY_GOOD_RATIO = "rxqualgoodratio";

	/**
	 * 质差标记
	 */
	public static final String QUALITY_GOOD_FLAG = "qualitygoodflag";
	/**
	 * 差小区类型
	 */
	public static final String BADCELLTYPE = "badcelltype";

	/*------------------------------------------3g指标--------------------------------------------*/

	// 干扰分析
	/**
	 * 指标:上行干扰小区数量
	 */
	public static final String TD_DISTURB_UL_CELL_COUNT = "tddisturbulcellcount";
	/**
	 * 指标:下行干扰小区数量
	 */
	public static final String TD_DISTURB_DL_CELL_COUNT = "tddisturbdlcellcount";

	/**
	 * 指标:上行干扰程度
	 */
	public static final String TD_DISTURB_UL_RATIO = "tddisturbulratio";
	/**
	 * 指标:下行干扰程度
	 */
	public static final String TD_DISTURB_DL_RATIO = "tddisturbdlratio";

	/**
	 * 上下干扰标记
	 */
	public static final String TD_DISTURB_UL_FLAG = "tddisturbulflag";
	/**
	 * 下行干扰标记
	 */
	public static final String TD_DISTURB_DL_FLAG = "tddisturbdlflag";

	// 覆盖分析

	/**
	 * 指标:上行覆盖小区数量
	 */
	public static final String TD_WEAK_COVER_UL_CELL_COUNT = "tdweakcoverulcellcount";

	/**
	 * 指标:下行覆盖小区数量
	 */
	public static final String TD_WEAK_COVER_DL_CELL_COUNT = "tdweakcoverdlcellcount";

	/**
	 * 指标:上行覆蓋程度
	 */
	public static final String TD_WEAK_COVER_UL_RATIO = "tdweakrxlevulratio";

	/**
	 * 指标:下行覆蓋程度
	 */
	public static final String TD_WEAK_COVER_DL_RATIO = "tdweakrxlevdlratio";

	/**
	 * 上行覆盖标记
	 */
	public static final String TD_WEAK_COVER_UL_FLAG = "tdweakcoverulflag";

	/**
	 * 下行覆盖标记
	 */
	public static final String TD_WEAK_COVER_DL_FLAG = "tdweakcoverdlflag";

	/**
	 * 指标:过覆盖小区数量
	 */
	public static final String TD_EXCESSIVE_COVER_CELL_COUNT = "tdexcoverulcellcount";

	/**
	 * 指标:过覆蓋程度
	 */
	public static final String TD_EXCESSIVE_COVER_RATIO = "tdexcessivecoverratio";

	/**
	 * 过覆盖标记
	 */
	public static final String TD_EXCESSIVE_COVER_FLAG = "tdexcessivecoverulflag";

	/**
	 * 指标:重复覆盖小区数量
	 */
	public static final String TD_REPEAT_COVER_CELL_COUNT = "tdrecoverulcellcount";

	/**
	 * 指标:重复覆蓋程度
	 */
	public static final String TD_REPEAT_COVER_RATIO = "tdrerxlevdlratio";

	/**
	 * 重复覆盖标记
	 */
	public static final String TD_REPEAT_COVER_FLAG = "tdrecoverdlflag";

	/**
	 * 覆盖空洞程度
	 */
	public static final String TD_HOLLOW_COVER_RATIO = "tdhollowrxlevdlratio";

	/**
	 * 覆盖空洞小区数量
	 */
	public static final String TD_HOLLOW_COVER_CELL_COUNT = "tdhollowrxlevcellcount";

	/**
	 * 覆盖空洞标记
	 */
	public static final String TD_HOLLOW_COVER_FLAG = "tdhollowrxlevflag";

	/**
	 * 高分组域掉话且低分组域无线接通率小区
	 */
	public static final String TDHIGHDROPCALLSETUPPSCELLFLAG = "tdhighdropcallsetuppscellflag";
	/**
	 * 低分组域无线接通率小区
	 */
	public static final String LOWCALLSETUPPSCELLFLAG = "lowcallsetuppscellflag";

	/**
	 * 高分组域掉线率小区
	 */
	public static final String TDHIGHDROPPSCELLFLAG = "tdhighdroppscellflag";

	/**
	 * 电高电路域掉话且低电路域无线接通率小区
	 */
	public static final String TDHIGHDROPCALLSETUPCSCELLFLAG = "tdhighdropcallsetupcscellflag";

	/**
	 * 低电路域无线接通率小区
	 */
	public static final String LOWCALLSETUPCSCELLFLAG = "lowcallsetupcscellflag";

	/**
	 * 高电路域掉话小区
	 */
	public static final String TDHIGHDROPCSCELLFLAG = "tdhighdropcscellflag";

	// 质量分析

	/**
	 * 指标:质差MR数量
	 */
	public static final String TD_QUALITY_BAD_MR_COUNT = "qualbadmrcount";

	/**
	 * 指标:质差程度
	 */
	public static final String TD_QUALITY_BAD_RATIO = "qualbadratio";

	/**
	 * 指标:质优MR数量
	 */
	public static final String TD_QUALITY_GOOD_MR_COUNT = "qualgoodmrcount";

	/**
	 * 指标:质优程度
	 */
	public static final String TD_QUALITY_GOOD_RATIO = "qualgoodratio";

	/*------------------------------------------LTE参数--------------------------------------------*/

	/**
	 * 指标:干扰小区比率
	 */
	public static final String LTE_DISTURB_CELL_RADIO = "celldistrubratio";

	/**
	 * 指标:重叠覆盖比率
	 */
	public static final String LTE_OVERLAP_COVER_CELL_RADIO = "overcelloverlapbestrowratio";

	/**
	 * 指标:弱覆盖比率
	 */
	public static final String LTE_WEAK_COVER_CELL_RADIO = "weakbestrowratio";

	/**
	 * 栅格ID
	 */
	public static final String LTE_GRIDID = "gridId";

	/*------------------------------------------共用参数--------------------------------------------*/

	/**
	 * 导出最大记录行
	 */
	public Integer EXPORT_MAX_SIZE = 5000;

	/**
	 * field name with ratio suffix
	 */
	public static final String RATIO = "ratio";

	/**
	 * field name with ratio suffix
	 */
	public static final String RATIO_TITLE = "占比";

	/**
	 * field name with ratio suffix
	 */
	public static final String TOTAL_CELL_NUM = "totalCellNum";

	/**
	 * field name with ratio suffix
	 */
	public static final String TOTAL_CELL_NUM_TITLE = "评估小区数";

	/**
	 * field name with ratio suffix
	 */
	public static final String RATIO_STRING = "ratiostring";

	/**
	 * 是
	 */
	public static final String YES = "是";

	/**
	 * 否
	 */
	public static final String NO = "否";

	/**
	 * field name with ratio suffix
	 */
	public static final String KPITYPE = "kpiType";

	/**
	 * field name with ratio suffix
	 */
	public static final String KPI_TYPE = "kpitype";

	/**
	 * field name with seq
	 */
	public static final String SEQ_PREFIX = "seq";

	/**
	 * field name with merge seq
	 */
	public static final String MERGESEQ_PREFIX = "mergeseq";

	/**
	 * decimal format
	 */
	public static final String RATIO_STRING_FORMAT = "###.##%";

	/**
	 * decimal format
	 */
	public static final String RATIO_FORMAT = "###.####";

	/**
	 * concat
	 */
	public static final String CONCAT = "concat";

	/**
	 * date_format
	 */
	public static final String DATE_FORMAT = "date_format";

	/**
	 * 日期表达式
	 */
	public static final String DATE_EXPRESSION = "%Y-%m-%d";

	/**
	 * 时间到小时时表达式
	 */
	public static final String HOUR_EXPRESSION = "%Y-%m-%d %H";

	/**
	 * 时间到分表达式
	 */
	public static final String TIME_EXPRESSION = "%Y-%m-%d %H:%i";

	/**
	 * 时间到秒表达式
	 */
	public static final String TIMESECOND_EXPRESSION = "%Y-%m-%d %H:%i:%s";

	/**
	 * "("
	 */
	public static final String LEFT_BRACKET = "(";

	/**
	 * ")"
	 */
	public static final String RIGHT_BRACKET = ")";

	/**
	 * and
	 */
	public static final String AND = "and";

	/**
	 * or
	 */
	public static final String OR = "or";

	/**
	 * in
	 */
	public static final String IN = "in";

	/**
	 * ON
	 */
	public static final String ON = "ON";

	/**
	 * +
	 */
	public static final String ADD = "+";

	/**
	 * *
	 */
	public static final String MULTIPLY = "*";

	/**
	 * =
	 */
	public static final String IDENTICAL = "=";

	/**
	 * SELECT
	 */
	public static final String SELECT = "SELECT";

	/**
	 * SELECT *
	 */
	public static final String SELECT_ALL = "SELECT *";

	/**
	 * FROM
	 */
	public static final String FROM = "FROM";

	/**
	 * WHERE
	 */
	public static final String WHERE = "WHERE";

	/**
	 * GROUP BY
	 */
	public static final String GROUP_BY = "GROUP BY";

	/**
	 * HAVING
	 */
	public static final String HAVING = "HAVING";

	/**
	 * ORDER BY
	 */
	public static final String ORDER_BY = "ORDER BY";

	/**
	 * asc
	 */
	public static final String ASC = "asc";
	/**
	 * desc
	 */
	public static final String DESC = "desc";

	/**
	 * LEFT JOIN
	 */
	public static final String LEFT_JOIN = "LEFT JOIN";

	/**
	 * RIGHT JOIN
	 */
	public static final String RIGHT_JOIN = "RIGHT JOIN";

	/**
	 * INNER JOIN
	 */
	public static final String INNER_JOIN = "INNER JOIN";

	/**
	 * split sign: 逻辑或 ||
	 */
	public static final String LOGIC_OR = "||";

	/**
	 * >
	 */
	public static final String LOGIC_GREAT = ">";

	/**
	 * >=
	 */
	public static final String LOGIC_GREAT_OR_EQUAL = ">=";

	/**
	 * <
	 */
	public static final String LOGIC_LESS = "<";

	/**
	 * <=
	 */
	public static final String LOGIC_LESS_OR_EQUAL = "<=";

	/**
	 * BETWEEN
	 */
	public static final String BETWEEN = "BETWEEN";

	/**
	 * LIMIT
	 */
	public static final String LIMIT = "LIMIT";

	/**
	 * 1=1
	 */
	public static final String IDENTICAL_EXP = "1=1";

	/**
	 * a=1
	 */
	public static final String EQUAL_ONE = "=1";

	/**
	 * a=2
	 */
	public static final String EQUAL_TWO = "=2";

	/**
	 * 1=1
	 */
	public static final String ISNOTNULL = "IS NOT NULL";

	/**
	 * 1=1
	 */
	public static final String ISNULL = "IS NULL";

	/**
	 * split sign: COMMA
	 */
	public static final String COMMA = ",";

	/**
	 * split sign: COLON
	 */
	public static final String COLON = ":";

	/**
	 * split sign: SIMICOLON
	 */
	public static final String SIMICOLON = ";";

	/**
	 * split sign: COMMA|SIMICOLON
	 */
	public static final String COMMA_OR_SIMICOLON = ",|;";

	/**
	 * split sign: 单引号
	 */
	public static final String SINGLE_QUOTES = "'";

	/**
	 * split sign: 波浪号
	 */
	public static final String TILDE = "~";

	/**
	 * .
	 */
	public static final String DOT = ".";

	/**
	 * split sign: SPACE
	 */
	public static final String SPACE = " ";

	/**
	 * split sign: EMPTY
	 */
	public static final String EMPTY = "";

	/**
	 * split sign: UNDER_LINE
	 */
	public static final String UNDER_LINE = "_";

	/**
	 * split sign: \dollar
	 */
	public static final String BACKSLASH_DOLLAR = "\\$";

	/**
	 * split sign: dollar
	 */
	public static final String DOLLAR = "$";

	/**
	 * enter
	 */
	public static final String ENTER = "\r\n";

	/**
	 * <br>
	 */
	public static final String BR = "<br>";

	/**
	 * if
	 */
	public static final String IF = "if (";

	/**
	 * IF(*,1,0)
	 */
	public static final String IF_VALUE = ",1,0 )";

	/**
	 * COUNT
	 */
	public static final String COUNT = "count";

	/**
	 * SUM
	 */
	public static final String SUM = "sum";

	/**
	 * ALLNUM
	 */
	public static final String ALLNUM = "allnum";

	/**
	 * BASETYPE
	 */
	public static final String BASETYPE = "3g";

	/**
	 * BASETLTEYPE
	 */
	public static final String BASETLTEYPE = "4g";

	/**
	 * THRESHOLDNUM
	 */
	public static final String THRESHOLDNUM = "thresholdnum";

	/**
	 * thresholdratio
	 */
	public static final String THRESHOLDRATIO = "thresholdratio";

	/**
	 * /
	 */
	public static final String DIVIDE = "/";

	/**
	 * CI=LACID*65536+CELLID BI=LACID*65536+BSCID
	 */
	public static final int STEP65536 = 65536;

	/**
	 * CHI=(LACID*65536+CELLID)*100+BCCH
	 */
	public static final int STEP100 = 100;

	/**
	 * 2\3G\4G切换参数名: 0 - 2G; 1 - 3G; 2 - 4G
	 */
	public static final String NET_SWITCH = "netSystem";

	/**
	 * 网络制式:2G
	 */
	public static final String NET_SYSTEM_2G = "0";

	/**
	 * 网络制式:3G
	 */
	public static final String NET_SYSTEM_3G = "1";

	/**
	 * 网络制式:3G
	 */
	public static final String ACTION_3G_SUFFIX = "3g";

	/**
	 * 网络制式:4G
	 */
	public static final String NET_SYSTEM_4G = "2";

	/**
	 * 网络制式:4G
	 */
	public static final String ACTION_4G_SUFFIX = "4g";

	/**
	 * Action
	 */
	public static final String ACTION = "Action";

	public static final String ANAHOUR_ALL = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23";

	/**
	 * 失败原因码
	 */
	public static final String FAILCAUSE = "FAILCAUSE";

	/**
	 * 失败原因中文名
	 */
	public static final String CAUSE_CN = "CAUSE_CN";

}
