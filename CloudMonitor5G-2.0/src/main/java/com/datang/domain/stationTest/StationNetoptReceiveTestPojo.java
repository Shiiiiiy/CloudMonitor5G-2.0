package com.datang.domain.stationTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;

/**
 * 单站网优验收测试实体类
 * @author maxuancheng
 *
 */
@Entity
@Table(name = "IADS_STATION_NETOPT_RECEIVE")
public class StationNetoptReceiveTestPojo {

	private Long id;
	
	/**
	 * 规划工参id
	 */
	private Long cellParamId;
	
	private String cellName;
	
	private String siteName;
	
	private String localCellId;
	
	/**
	 * 4G锚点连接建立成功率/尝试次数
	 */
	private String lteEpsattachSum;
	/**
	 * 4G锚点连接建立成功率/成功次数
	 */
	private String lteEpsattachSuccess;
	/**
	 * 4G锚点连接建立成功率/失败次数
	 */
	private String lteEpsattachFailure;
	/**
	 * 4G锚点连接建立成功率/成功率
	 */
	private String lteEpsattachSuccessRation;
	
	/**
	 * 5G连接建立成功次数  / 尝试次数
	 */
	private String nrEdsaddSum;
	/**
	 * 5G连接建立成功次数  / 成功次数
	 */
	private String nrEdsaddSuccess;
	/**
	 *  5G连接建立成功次数   /失败次数
	 */
	private String nrEdsaddFailure;
	/**
	 * 5G连接建立成功次数  /成功率
	 */
	private String nrEdsaddSuccessRation;
	
	/**
	 * 4G锚点切换成功次数  / 尝试次数
	 */
	private String lteInterfreqHandoverSum;
	/**
	 * 4G锚点切换成功次数  / 成功次数
	 */
	private String lteInterfreqHandoverSuccess;
	/**
	 *  4G锚点切换成功次数   /失败次数
	 */
	private String lteInterfreqHandoverFailure;
	/**
	 * 4G锚点切换成功次数  /成功率
	 */
	private String lteInterfreqHandoverSuccessRation;
	
	/**
	 *  5G连接切换成功次数  / 尝试次数
	 */
	private String nrEdschangeSum;
	/**
	 *  5G连接切换成功次数  / 成功次数
	 */
	private String nrEdschangeSuccess;
	/**
	 *  5G连接切换成功次数   /失败次数
	 */
	private String nrEdschangeFailure;
	/**
	 *  5G连接切换成功次数  /成功率
	 */
	private String nrEdschangeSuccessRation;
	
	/**
	 *  4G锚点RRC重建成功次数  / 尝试次数
	 */
	private String rrcConnectionSum;
	/**
	 *  4G锚点RRC重建成功次数  / 成功次数
	 */
	private String rrcConnectionSuccess;
	/**
	 *  4G锚点RRC重建成功次数   /失败次数
	 */
	private String rrcConnectionFailure;
	/**
	 *  4G锚点RRC重建成功次数  /成功率
	 */
	private String rrcConnectionSuccessRation;

	/**
	 *  4G锚点掉线次数  / 尝试次数
	 */
	private String lteEarfcnSum;
	/**
	 *  4G锚点掉线次数  / 成功次数
	 */
	private String lteEarfcnSuccess;
	/**
	 * 4G锚点掉线次数   /失败次数
	 */
	private String lteEarfcnFailure;
	/**
	 *  4G锚点掉线次数  /成功率
	 */
	private String lteEarfcnSuccessRation;

	/**
	 *  5G掉线次数  / 尝试次数
	 */
	private String nrEarfcnSum;
	/**
	 *  5G掉线次数  / 成功次数
	 */
	private String nrEarfcnSuccess;
	/**
	 * 5G掉线次数   /失败次数
	 */
	private String nrEarfcnFailure;
	/**
	 *  5G掉线次数  /成功率
	 */
	private String nrEarfcnSuccessRation;
	
	/**
	 *  单用户好点Ping包平均时延（32Bytes）  / 尝试次数
	 */
	private String ping32Sum;
	/**
	 *  单用户好点Ping包平均时延（32Bytes）  / 成功次数
	 */
	private String ping32Success;
	/**
	 * 单用户好点Ping包平均时延（32Bytes）   /失败次数
	 */
	private String ping32Failure;
	/**
	 *  单用户好点Ping包平均时延（32Bytes） /成功率
	 */
	private String ping32SuccessRation;
	
	/**
	 *  单用户好点Ping包平均时延（1500Bytes）  / 尝试次数
	 */
	private String ping1500Sum;
	/**
	 *  单用户好点Ping包平均时延（1500Bytes）  / 成功次数
	 */
	private String ping1500Success;
	/**
	 * 单用户好点Ping包平均时延（1500Bytes）   /失败次数
	 */
	private String ping1500Failure;
	/**
	 *  单用户好点Ping包平均时延（1500Bytes） /成功率
	 */
	private String ping1500SuccessRation;
	
	/**
	 *  FTP下行吞吐率  / SS-RSRP 好点
	 */
	private String ftpDownRsrpGoog;
	/**
	 * FTP下行吞吐率  / SS-RSRP 中点
	 */
	private String ftpDownRsrpMid;
	/**
	 *  FTP下行吞吐率  / SS-RSRP 差点
	 */
	private String ftpDownRsrpBad;
	
	/**
	 *  FTP下行吞吐率  / SS-SINR 好点
	 */
	private String ftpDownSinrGoog;
	/**
	 * FTP下行吞吐率  / SS-SINR 中点
	 */
	private String ftpDownSinrMid;
	/**
	 *  FTP下行吞吐率  / SS-SINR 差点
	 */
	private String ftpDownSinrBad;

	/**
	 *  FTP下行吞吐率  / 下行吞吐率 好点
	 */
	private String ftpDownThrputGoog;
	/**
	 * FTP下行吞吐率  / 下行吞吐率 中点
	 */
	private String ftpDownThrputMid;
	/**
	 *  FTP下行吞吐率  / 下行吞吐率 差点
	 */
	private String ftpDownThrputBad;

	/**
	 *  FTP上行吞吐率  / SS-RSRP 好点
	 */
	private String ftpUpRsrpGoog;
	/**
	 * FTP上行吞吐率  / SS-RSRP 中点
	 */
	private String ftpUpRsrpMid;
	/**
	 *  FTP上行吞吐率  / SS-RSRP 差点
	 */
	private String ftpUpRsrpBad;
	
	/**
	 *  FTP上行吞吐率  / SS-SINR 好点
	 */
	private String ftpUpSinrGoog;
	/**
	 * FTP上行吞吐率  / SS-SINR 中点
	 */
	private String ftpUpSinrMid;
	/**
	 *  FTP上行吞吐率  / SS-SINR 差点
	 */
	private String ftpUpSinrBad;

	/**
	 *  FTP上行吞吐率  / 下行吞吐率 好点
	 */
	private String ftpUpThrputGoog;
	/**
	 * FTP上行吞吐率  / 下行吞吐率 中点
	 */
	private String ftpUpThrputMid;
	/**
	 *  FTP上行吞吐率  / 下行吞吐率 差点
	 */
	private String ftpUpThrputBad;
	
	
	
	/**
	 * 1-“30”(4G锚点切换成功次数  /4G锚点总次数) /SgNB 异常释放率
	 * 序号：56
	 */
	private String lteInterfreqHandoverfailRation;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“4G锚点成功次数”，若有多个记录符合条件，那么取总.
	 * 序号：57
	 */
	private String dtLteEarfcnSuccessSum;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G成功次数”，若有多个记录符合条件，那么取总和。
	 * 序号：58
	 */
	private String dtNRSuccessSum;
	
	/**
	 * “57”(单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“DT”的记录的“4G锚点成功次数”，若有多个记录符合条件，那么取总)
	 * +“71”(单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“DT”的记录的“4G掉线次数”，若有多个记录符合条件，那么取总)
	 *  序号：61
	 */
	private String dtLteSumDegree;
	
	/**
	 * “58”(单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“DT”的记录的“5G成功次数”，若有多个记录符合条件，那么取总和)
	 * +“72”(单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“DT”的记录的“5G掉线次数”，若有多个记录符合条件，那么取总和) 
	 * 序号：62
	 */
	private String dtNRSumDegree;
	
	/**
	 * 4g掉线率
	 * “71”(单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“DT”的记录的“4G掉线次数”，若有多个记录符合条件，那么取总)
	 * 除以“61”(“57”+“71”)
	 * 序号：63
	 */
	private String dtLteEarfcnFailRation;
	
	/**
	 * 5g掉线率
	 * “72”(单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“DT”的记录的“5G掉线次数”，若有多个记录符合条件，那么取总和)
	 * 除以“62“(“58”+“72”)
	 * 序号：64
	 */
	private String dtNREarfcnFailRation;
	
	/**
	 * 单用户5G SSB覆盖/平均RSRP（dBm）
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况属性”为好点，“测试业务属性为”DT“的记录的“NR rsrp”，若有多个记录符合条件，则取最高值
	 * 序号：65
	 */
	private String dtNrSsbRsrp;
	
	/**
	 * 单用户5G SSB覆盖/平均SINR（dB）
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，“测试业务“属性为”DT“的记录的“NR sinr”，，若有多个记录符合条件，则取最低值
	 * 序号：66
	 */
	private String dtNrSsbSinr;
	
	/**
	 * 单用户5G SSB覆盖/Throughput DL
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，“测试业务“属性为”DT“的记录的“NR Throughput DL”，若有多个记录符合条件，则取最高值
	 * 序号：67
	 */
	private String dtNrSsbThrouDL;
	
	/**
	 * 单用户5G SSB覆盖/Throughput UL
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，“测试业务“属性为”DT“的记录的“NR Throughput UL”，若有多个记录符合条件，则取最高值
	 * 序号：68
	 */
	private String dtNrSsbThrouUL;
	
	/**
	 * 单用户4G SSB覆盖/RSRP（dBm）
	 * 单用户4G锚点覆盖/平均RSRP（dBm）
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况属性”为好点，“测试业务属性为”DT“的记录的“LTE rsrp”，若有多个记录符合条件，则取最高值
	 * 序号：69
	 */
	private String dtLteEarfcnRsrp;
	
	/**
	 * 单用户4G锚点覆盖/平均SINR（dB）
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，“测试业务“属性为”DT“的记录的“LTE sinr”，，若有多个记录符合条件，则取最低值
	 * 序号：70
	 */
	private String dtLteEarfcnSinr;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“4G掉线次数”，若有多个记录符合条件，那么取总
	 * 序号：71
	 */
	private String dtLteFailSumDegree;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G掉线次数”，若有多个记录符合条件，那么取总和。
	 * 序号：72
	 */
	private String dtNrFailSumDegree;
	
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点或者极好点，“测试业务“属性为”FTP下载“的记录的“NR下载吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：73
	 */
	private String ftpNrThrouDLRateMax;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点或者极好点，“测试业务“属性为”FTP上传“的记录的“NR上传吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：74
	 */
	private String ftpNrThrouULRateMax;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务“属性为”DT“的记录的“弱覆盖率”，若有多个记录符合条件，则取最低值
	 * 序号：75
	 */
	private String dtWeakCoverRateMin;
	
	/**
	 * 1-"75"  无线覆盖率
	 */
	private String dtWireCoverRate;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“主叫volte成功次数”，若有多个记录，则取所有“主叫Volte掉话次数”为0的记录的总和
	 * 序号：76
	 */
	private String dialVolteSuccessSum;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“主叫VONR功次数”，若有多个记录，则取所有“主叫VONR掉话次数”为0的记录的总和
	 * 序号：77
	 */
	private String dialVonrSuccessSum;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“NR切换成功次数”除以（“NR切换成功次数”+“NR切换失败次数”），若有多个记录符合条件，那么分子分母分别求和以后汇总
	 * 序号：78
	 */
	private String nrSwitchSuccessRate;
	
	/**
	 * “78”、“79”、“80”（localcellid=1/2/3）的平均值  切换功能(≥98%)
	 */
	private String nrSwitchSuccessAvg;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“ping（32）测试”或者“ping（1500）测试”
	 * （注意，这里的数字是根据门限判定以后的结果）。“无线情况”属性为好点或者极好点，的记录的“NR连接建立成功次数2”，若有多个记录符合条件，那么取总和，
	 *  但是如果某条记录的“NR连接建立失败次数2”不等于0，则该条记录的“NR连接建立成功次数2”不计入统计，
	 * 序号：81
	 */
	private String nrConnectSuccessSum;
	
	/**
	 * 匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“ping（32）测试”或者“ping（1500）测试”
	 * （注意，这里的数字是根据门限判定以后的结果）。“无线情况”属性为好点或者极好点，的记录的“NR连接建立时延”，若有多个记录符合条件，那么取最小值
	 * 序号：82
	 */
	private String nrConnectTimeDelay;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近的，“无线情况”属性为好点或极好点，
	 * “测试业务“属性为”FTP下载“记录的“NR PDSCH DMRS RSRP”，若有多个记录符合条件，则取最高值
	 * 序号：83
	 */
	private String nrPdschRsrpFtpDL;
	
	/**
	 * “测试日期”距离生成单验报告日期最近，“无线情况”属性为好点或极好点，
	 * “测试业务“属性为”FTP下载“的记录的“NR PDCCH DMRS SINR”，若有多个记录符合条件，则取最低值
	 * 序号：84
	 */
	private String nrPdschSinrFtpDL;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近的，“无线情况”属性为好点或极好点，
	 * “测试业务“属性为”FTP上传“记录的“NR PDSCH DMRS RSRP”，若有多个记录符合条件，则取最高值
	 * 序号：85
	 */
	private String nrPdschRsrpFtpUL;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点或极好点，
	 * “测试业务“属性为”FTP上传“的记录的“NR PDCCH DMRS SINR”，若有多个记录符合条件，则取最低值
	 * 序号：86
	 */
	private String nrPdschSinrFtpUL;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，
	 * “测试业务“属性为”绕点_下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：87
	 */
	private String pdcpnrThrputDL;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，
	 * “测试业务“属性为”绕点_上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：88
	 */
	private String pdcpnrThrputUL;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，
	 *“测试业务“属性为“绕点_下载“的记录的“下行低速率占比”，若有多个记录符合条件，则取最低值
	 * 序号：89
	 */
	private String nrMacThrputDLMIN;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，
	 * “测试业务“属性为“绕点_上传“的记录的“上行低速率占比”，若有多个记录符合条件，则取最低值
	 * 序号：90
	 */
	private String nrMacThrputULMin;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，
	 * “测试业务“属性为“绕点_下载“的记录的“CQI”，若有多个记录符合条件，则取平均值值
	 * 序号：91
	 */
	private String nrCwoWbCqiAvg;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，“无线情况”属性为好点，
	 * “测试业务“属性为“绕点_下载“的记录的“RANK”，若有多个记录符合条件，则取最低值
	 * 序号：92
	 */
	private String nrRankIndicatMIn;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 * “测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“NR切换成功次数”
	 * 序号：93
	 */
	private String nrDlUlSwitchSuccess;
	
	/**
	 * 单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“NR切换失败次数”
	 * 序号：94
	 */
	private String nrDlUlSwitchDrop;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G连接建立成功次数”，若有多个记录符合条件，那么取总和，
	 *   但是如果某条记录的“5G连接建立失败次数”不等于0，则该条记录的“5G连接建立成功次数”不计入统计，
	 * 序号：95
	 */
	private String nrConnectBuildSucess;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
	 *  序号：96
	 */
	private String ftpGoodPdcpnrThrputDL;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：97
	 */
	private String ftpGoodPdcpnrThrputUL;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“CSFB成功次数”，若有多个记录，则取所有“CSFB未接通次数”为0的记录的总和
	 * 序号：98
	 */
	private String volteCsfbSuccess;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“CSFB成功回落次数”，若有多个记录，则取所有“CSFB回落失败次数”为0的记录的总和
	 * 序号：99
	 */
	private String volteCsfbCallbackSuccess;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “测试业务”属性为“volte测试”。“无线情况”属性为极好点或者好点，的记录的“CSFB回落时延”，若有多个记录，则取所有“CSFB回落失败次数”为0的记录的平均值
	 * 序号：100
	 */
	private String volteCsfbCallbackTime;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “测试日期”距离生成单验报告日期最近，“无线情况”属性为差点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：101
	 */
	private String ftpBadPdcpnrThrputDL;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  “测试日期”距离生成单验报告日期最近，“无线情况”属性为差点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：102
	 */
	private String ftpBadPdcpnrThrputUL;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近，
	 *  匹配用户选择的sitename对应的所有cellname，“测试业务”属性为“绕点_下载”或者“绕点_上传”的记录的“5G连接切换成功次数-往本站1小区切换成功次数-往本站2小区切换成功次数-往本站3小区切换成功次数”，
	 *  若有多个记录符合，那么取总和。但是如果某条记录的“5G连接切换失败次数”不等于0，则该条记录的““5G连接切换成功次数-往本站1小区切换成功次数-往本站2小区切换成功次数-往本站3小区切换成功次数”不计入统计
	 * 序号：103
	 */
	private String nrStationSwitchSum;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 匹配用户选择的sitename对应的所有cellname，“测试日期”距离生成单验报告日期最近，“测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“5G掉线次数”，若有多个记录符合条件，那么取总和
	 * 序号：104
	 */
	private String stationDtNrFailSumDegree;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 测试业务”属性为“ENDC成功率测试”。“无线情况”属性为好点或者极好点，的记录的“NR连接建立成功次数”，若有多个记录符合条件，那么取总和，但是如果某条记录的“NR连接建立失败次数”不等于0，
	 * 则该条记录的“NR连接建立成功次数”不计入统计，
	 * 序号：105
	 */
	private String endcGoodNrConnectSum;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“5G成功次数”，若有多个记录符合条件，那么取总和。
	 * 序号：106
	 */
	private String raoNREarfcnSuccessSum;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 测试业务”属性为“绕点_下载“或者”绕点_上传“的记录的“4G成功次数”，若有多个记录符合条件，那么取总和。
	 * 序号：107
	 */
	private String raoLteEarfcnSuccessSum;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 无线情况”属性为中点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：108
	 */
	private String midFtpDlPdcpnrDlThr;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 无线情况”属性为中点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率”，若有多个记录符合条件，则取最高值
	 * 序号：109
	 */
	private String midFtpUlPdcpnrUlThr;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * “无线情况”属性为中点，“测试业务“属性为”FTP下载“的记录的“NR下载吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：113
	 */
	private String midFtpDlNrDlThrMax;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * “无线情况”属性为中点，“测试业务“属性为”FTP上传“的记录的“NR上传吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：114
	 */
	private String midFtpUlNrUlThrMax;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * “无线情况”属性为差点，“测试业务“属性为”FTP下载“的记录的“NR下载吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：115
	 */
	private String badFtpDlNrDlThrMax;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * “无线情况”属性为差点，“测试业务“属性为”FTP上传“的记录的“NR上传吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：116
	 */
	private String badFtpUlNrUlThrMax;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP下载“的记录的“PDCPNR下载吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：117
	 */
	private String goodFtpDlPdcpnrDLThrmax;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * “无线情况”属性为好点或者极好点，“测试业务“属性为”FTP上传“的记录的“PDCPNR上传吞吐率max”，若有多个记录符合条件，则取最高值
	 * 序号：118
	 */
	private String goodFtpUlPdcpnrULThrmax;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 	 “测试业务“属性为“绕点_下载“的记录的“弱覆盖率2”，若有多个记录符合条件，则取最低值
	 * 序号：119
	 */
	private String raoDLSSRsrpSinr100min;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 * 	“测试业务“属性为“绕点_下载“的记录的“弱覆盖率3”，若有多个记录符合条件，则取最低值
	 * 序号：120
	 */
	private String raoDLSSRsrpSinr105min;
	
	/**
	 *   单验日志指标详表里面，匹配“localcellid=1/2/3”的cellname，“测试日期”距离生成单验报告日期最近， 
	 *  “测试业务“属性为“绕点_下载“的记录的“弱覆盖率4”，若有多个记录符合条件，则取最低值
	 * 序号：121
	 */
	private String raoDLSSRsrpSinr110min;
	
	
	
	
	
	/**
	 * 单验日志规划工参
	 */
	private PlanParamPojo planParamPojo;
	

	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CELL_PARAM_ID")
	public Long getCellParamId() {
		return cellParamId;
	}

	public void setCellParamId(Long cellParamId) {
		this.cellParamId = cellParamId;
	}

	@Column(name = "CELL_NAME")
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	@Column(name = "SITE_NAME")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Column(name = "LOCAL_CELL_ID")
	public String getLocalCellId() {
		return localCellId;
	}

	public void setLocalCellId(String localCellId) {
		this.localCellId = localCellId;
	}

	@Column(name = "LTE_EPSATTACH_SUM")
	public String getLteEpsattachSum() {
		return lteEpsattachSum;
	}

	public void setLteEpsattachSum(String lteEpsattachSum) {
		this.lteEpsattachSum = lteEpsattachSum;
	}

	@Column(name = "LTE_EPSATTACH_SUCCESS")
	public String getLteEpsattachSuccess() {
		return lteEpsattachSuccess;
	}

	public void setLteEpsattachSuccess(String lteEpsattachSuccess) {
		this.lteEpsattachSuccess = lteEpsattachSuccess;
	}

	@Column(name = "LTE_EPSATTACH_FAILURE")
	public String getLteEpsattachFailure() {
		return lteEpsattachFailure;
	}

	public void setLteEpsattachFailure(String lteEpsattachFailure) {
		this.lteEpsattachFailure = lteEpsattachFailure;
	}

	@Column(name = "LTE_EPSATTACH_SUCCESSRATION")
	public String getLteEpsattachSuccessRation() {
		return lteEpsattachSuccessRation;
	}

	public void setLteEpsattachSuccessRation(String lteEpsattachSuccessRation) {
		this.lteEpsattachSuccessRation = lteEpsattachSuccessRation;
	}

	@Column(name = "NR_EDSADD_SUM")
	public String getNrEdsaddSum() {
		return nrEdsaddSum;
	}

	public void setNrEdsaddSum(String nrEdsaddSum) {
		this.nrEdsaddSum = nrEdsaddSum;
	}

	@Column(name = "NR_EDSADD_SUCCESS")
	public String getNrEdsaddSuccess() {
		return nrEdsaddSuccess;
	}

	public void setNrEdsaddSuccess(String nrEdsaddSuccess) {
		this.nrEdsaddSuccess = nrEdsaddSuccess;
	}

	@Column(name = "NR_EDSADD_FAILURE")
	public String getNrEdsaddFailure() {
		return nrEdsaddFailure;
	}

	public void setNrEdsaddFailure(String nrEdsaddFailure) {
		this.nrEdsaddFailure = nrEdsaddFailure;
	}

	@Column(name = "NR_EDSADD_SUCCESSRATION")
	public String getNrEdsaddSuccessRation() {
		return nrEdsaddSuccessRation;
	}

	public void setNrEdsaddSuccessRation(String nrEdsaddSuccessRation) {
		this.nrEdsaddSuccessRation = nrEdsaddSuccessRation;
	}

	@Column(name = "LTE_INTERFREQ_SUM")
	public String getLteInterfreqHandoverSum() {
		return lteInterfreqHandoverSum;
	}

	public void setLteInterfreqHandoverSum(String lteInterfreqHandoverSum) {
		this.lteInterfreqHandoverSum = lteInterfreqHandoverSum;
	}

	@Column(name = "LTE_INTERFREQ_SUCCESS")
	public String getLteInterfreqHandoverSuccess() {
		return lteInterfreqHandoverSuccess;
	}

	public void setLteInterfreqHandoverSuccess(String lteInterfreqHandoverSuccess) {
		this.lteInterfreqHandoverSuccess = lteInterfreqHandoverSuccess;
	}

	@Column(name = "LTE_INTERFREQ_FAILURE")
	public String getLteInterfreqHandoverFailure() {
		return lteInterfreqHandoverFailure;
	}

	public void setLteInterfreqHandoverFailure(String lteInterfreqHandoverFailure) {
		this.lteInterfreqHandoverFailure = lteInterfreqHandoverFailure;
	}

	@Column(name = "LT_EINTERFREQ_SUCCESSRATION")
	public String getLteInterfreqHandoverSuccessRation() {
		return lteInterfreqHandoverSuccessRation;
	}

	public void setLteInterfreqHandoverSuccessRation(String lteInterfreqHandoverSuccessRation) {
		this.lteInterfreqHandoverSuccessRation = lteInterfreqHandoverSuccessRation;
	}

	@Column(name = "NR_EDSCHANGE_SUM")
	public String getNrEdschangeSum() {
		return nrEdschangeSum;
	}

	public void setNrEdschangeSum(String nrEdschangeSum) {
		this.nrEdschangeSum = nrEdschangeSum;
	}

	@Column(name = "NR_EDSCHANGE_SUCCESS")
	public String getNrEdschangeSuccess() {
		return nrEdschangeSuccess;
	}

	public void setNrEdschangeSuccess(String nrEdschangeSuccess) {
		this.nrEdschangeSuccess = nrEdschangeSuccess;
	}

	@Column(name = "NR_EDSCHANGE_FAILURE")
	public String getNrEdschangeFailure() {
		return nrEdschangeFailure;
	}

	public void setNrEdschangeFailure(String nrEdschangeFailure) {
		this.nrEdschangeFailure = nrEdschangeFailure;
	}

	@Column(name = "NR_EDSCHANGE_SUCCESSRATION")
	public String getNrEdschangeSuccessRation() {
		return nrEdschangeSuccessRation;
	}

	public void setNrEdschangeSuccessRation(String nrEdschangeSuccessRation) {
		this.nrEdschangeSuccessRation = nrEdschangeSuccessRation;
	}

	@Column(name = "RRC_CONNECTION_SUM")
	public String getRrcConnectionSum() {
		return rrcConnectionSum;
	}

	public void setRrcConnectionSum(String rrcConnectionSum) {
		this.rrcConnectionSum = rrcConnectionSum;
	}

	@Column(name = "RRC_CONNECTION_SUCCESS")
	public String getRrcConnectionSuccess() {
		return rrcConnectionSuccess;
	}

	public void setRrcConnectionSuccess(String rrcConnectionSuccess) {
		this.rrcConnectionSuccess = rrcConnectionSuccess;
	}

	@Column(name = "RRC_CONNECTION_FAILURE")
	public String getRrcConnectionFailure() {
		return rrcConnectionFailure;
	}

	public void setRrcConnectionFailure(String rrcConnectionFailure) {
		this.rrcConnectionFailure = rrcConnectionFailure;
	}

	@Column(name = "RRC_CONNECTION_SUCCESSRATION")
	public String getRrcConnectionSuccessRation() {
		return rrcConnectionSuccessRation;
	}

	public void setRrcConnectionSuccessRation(String rrcConnectionSuccessRation) {
		this.rrcConnectionSuccessRation = rrcConnectionSuccessRation;
	}

	@Column(name = "LTE_EARFCN_SUM")
	public String getLteEarfcnSum() {
		return lteEarfcnSum;
	}

	public void setLteEarfcnSum(String lteEarfcnSum) {
		this.lteEarfcnSum = lteEarfcnSum;
	}

	@Column(name = "LTE_EARFCN_SUCCESS")
	public String getLteEarfcnSuccess() {
		return lteEarfcnSuccess;
	}

	public void setLteEarfcnSuccess(String lteEarfcnSuccess) {
		this.lteEarfcnSuccess = lteEarfcnSuccess;
	}

	@Column(name = "LTE_EARFCN_FAILURE")
	public String getLteEarfcnFailure() {
		return lteEarfcnFailure;
	}

	public void setLteEarfcnFailure(String lteEarfcnFailure) {
		this.lteEarfcnFailure = lteEarfcnFailure;
	}

	@Column(name = "LTE_EARFCN_SUCCESSRATION")
	public String getLteEarfcnSuccessRation() {
		return lteEarfcnSuccessRation;
	}

	public void setLteEarfcnSuccessRation(String lteEarfcnSuccessRation) {
		this.lteEarfcnSuccessRation = lteEarfcnSuccessRation;
	}

	@Column(name = "NR_EARFCN_SUM")
	public String getNrEarfcnSum() {
		return nrEarfcnSum;
	}

	public void setNrEarfcnSum(String nrEarfcnSum) {
		this.nrEarfcnSum = nrEarfcnSum;
	}

	@Column(name = "NR_EARFCN_SUCCESS")
	public String getNrEarfcnSuccess() {
		return nrEarfcnSuccess;
	}

	public void setNrEarfcnSuccess(String nrEarfcnSuccess) {
		this.nrEarfcnSuccess = nrEarfcnSuccess;
	}

	@Column(name = "NREARFCN_FAILURE")
	public String getNrEarfcnFailure() {
		return nrEarfcnFailure;
	}

	public void setNrEarfcnFailure(String nrEarfcnFailure) {
		this.nrEarfcnFailure = nrEarfcnFailure;
	}

	@Column(name = "NREARFCN_SUCCESS_RATION")
	public String getNrEarfcnSuccessRation() {
		return nrEarfcnSuccessRation;
	}

	public void setNrEarfcnSuccessRation(String nrEarfcnSuccessRation) {
		this.nrEarfcnSuccessRation = nrEarfcnSuccessRation;
	}

	@Column(name = "PING32_SUM")
	public String getPing32Sum() {
		return ping32Sum;
	}

	public void setPing32Sum(String ping32Sum) {
		this.ping32Sum = ping32Sum;
	}

	@Column(name = "PING32_SUCCESS")
	public String getPing32Success() {
		return ping32Success;
	}

	public void setPing32Success(String ping32Success) {
		this.ping32Success = ping32Success;
	}

	@Column(name = "PING32_FAILURE")
	public String getPing32Failure() {
		return ping32Failure;
	}

	public void setPing32Failure(String ping32Failure) {
		this.ping32Failure = ping32Failure;
	}

	@Column(name = "PING32_SUCCESS_RATION")
	public String getPing32SuccessRation() {
		return ping32SuccessRation;
	}

	public void setPing32SuccessRation(String ping32SuccessRation) {
		this.ping32SuccessRation = ping32SuccessRation;
	}

	@Column(name = "PING1500_SUM")
	public String getPing1500Sum() {
		return ping1500Sum;
	}

	public void setPing1500Sum(String ping1500Sum) {
		this.ping1500Sum = ping1500Sum;
	}

	@Column(name = "PING1500_SUCCESS")
	public String getPing1500Success() {
		return ping1500Success;
	}

	public void setPing1500Success(String ping1500Success) {
		this.ping1500Success = ping1500Success;
	}

	@Column(name = "PING1500_FAILURE")
	public String getPing1500Failure() {
		return ping1500Failure;
	}

	public void setPing1500Failure(String ping1500Failure) {
		this.ping1500Failure = ping1500Failure;
	}

	@Column(name = "PING1500_SUCCESS_RATION")
	public String getPing1500SuccessRation() {
		return ping1500SuccessRation;
	}

	public void setPing1500SuccessRation(String ping1500SuccessRation) {
		this.ping1500SuccessRation = ping1500SuccessRation;
	}

	@Column(name = "FTPDOWN_RSRP_GOOG")
	public String getFtpDownRsrpGoog() {
		return ftpDownRsrpGoog;
	}

	public void setFtpDownRsrpGoog(String ftpDownRsrpGoog) {
		this.ftpDownRsrpGoog = ftpDownRsrpGoog;
	}

	@Column(name = "FTPDOWN_RSRP_MID")
	public String getFtpDownRsrpMid() {
		return ftpDownRsrpMid;
	}

	public void setFtpDownRsrpMid(String ftpDownRsrpMid) {
		this.ftpDownRsrpMid = ftpDownRsrpMid;
	}

	@Column(name = "FTPDOWN_RSRP_BAD")
	public String getFtpDownRsrpBad() {
		return ftpDownRsrpBad;
	}

	public void setFtpDownRsrpBad(String ftpDownRsrpBad) {
		this.ftpDownRsrpBad = ftpDownRsrpBad;
	}

	@Column(name = "FTPDOWN_SINR_GOOG")
	public String getFtpDownSinrGoog() {
		return ftpDownSinrGoog;
	}

	public void setFtpDownSinrGoog(String ftpDownSinrGoog) {
		this.ftpDownSinrGoog = ftpDownSinrGoog;
	}

	@Column(name = "FTPDOWN_SINR_MID")
	public String getFtpDownSinrMid() {
		return ftpDownSinrMid;
	}

	public void setFtpDownSinrMid(String ftpDownSinrMid) {
		this.ftpDownSinrMid = ftpDownSinrMid;
	}

	@Column(name = "FTPDOWN_SINR_BAD")
	public String getFtpDownSinrBad() {
		return ftpDownSinrBad;
	}

	public void setFtpDownSinrBad(String ftpDownSinrBad) {
		this.ftpDownSinrBad = ftpDownSinrBad;
	}

	@Column(name = "FTPDOWN_THRPUT_GOOG")
	public String getFtpDownThrputGoog() {
		return ftpDownThrputGoog;
	}

	public void setFtpDownThrputGoog(String ftpDownThrputGoog) {
		this.ftpDownThrputGoog = ftpDownThrputGoog;
	}

	@Column(name = "FTPDOWN_THRPUT_MID")
	public String getFtpDownThrputMid() {
		return ftpDownThrputMid;
	}

	public void setFtpDownThrputMid(String ftpDownThrputMid) {
		this.ftpDownThrputMid = ftpDownThrputMid;
	}

	@Column(name = "FTPDOWN_THRPUT_BAD")
	public String getFtpDownThrputBad() {
		return ftpDownThrputBad;
	}

	public void setFtpDownThrputBad(String ftpDownThrputBad) {
		this.ftpDownThrputBad = ftpDownThrputBad;
	}

	@Column(name = "FTPUP_RSRP_GOOG")
	public String getFtpUpRsrpGoog() {
		return ftpUpRsrpGoog;
	}

	public void setFtpUpRsrpGoog(String ftpUpRsrpGoog) {
		this.ftpUpRsrpGoog = ftpUpRsrpGoog;
	}

	@Column(name = "FTPUP_RSRP_MID")
	public String getFtpUpRsrpMid() {
		return ftpUpRsrpMid;
	}

	public void setFtpUpRsrpMid(String ftpUpRsrpMid) {
		this.ftpUpRsrpMid = ftpUpRsrpMid;
	}

	@Column(name = "FTPUP_RSRP_BAD")
	public String getFtpUpRsrpBad() {
		return ftpUpRsrpBad;
	}

	public void setFtpUpRsrpBad(String ftpUpRsrpBad) {
		this.ftpUpRsrpBad = ftpUpRsrpBad;
	}

	@Column(name = "FTPUP_SINR_GOOG")
	public String getFtpUpSinrGoog() {
		return ftpUpSinrGoog;
	}

	public void setFtpUpSinrGoog(String ftpUpSinrGoog) {
		this.ftpUpSinrGoog = ftpUpSinrGoog;
	}

	@Column(name = "FTPUP_SINR_MID")
	public String getFtpUpSinrMid() {
		return ftpUpSinrMid;
	}

	public void setFtpUpSinrMid(String ftpUpSinrMid) {
		this.ftpUpSinrMid = ftpUpSinrMid;
	}

	@Column(name = "FTPUP_SINR_BAD")
	public String getFtpUpSinrBad() {
		return ftpUpSinrBad;
	}

	public void setFtpUpSinrBad(String ftpUpSinrBad) {
		this.ftpUpSinrBad = ftpUpSinrBad;
	}

	@Column(name = "FTPUP_THRPUT_GOOG")
	public String getFtpUpThrputGoog() {
		return ftpUpThrputGoog;
	}

	public void setFtpUpThrputGoog(String ftpUpThrputGoog) {
		this.ftpUpThrputGoog = ftpUpThrputGoog;
	}

	@Column(name = "FTPUP_THRPUT_MID")
	public String getFtpUpThrputMid() {
		return ftpUpThrputMid;
	}

	public void setFtpUpThrputMid(String ftpUpThrputMid) {
		this.ftpUpThrputMid = ftpUpThrputMid;
	}

	@Column(name = "FTPUP_THRPUT_BAD")
	public String getFtpUpThrputBad() {
		return ftpUpThrputBad;
	}

	public void setFtpUpThrputBad(String ftpUpThrputBad) {
		this.ftpUpThrputBad = ftpUpThrputBad;
	}
	
	@Column(name = "LTE_INTERFREQ_FAILRATION")
	public String getLteInterfreqHandoverfailRation() {
		return lteInterfreqHandoverfailRation;
	}

	public void setLteInterfreqHandoverfailRation(String lteInterfreqHandoverfailRation) {
		this.lteInterfreqHandoverfailRation = lteInterfreqHandoverfailRation;
	}

	@Column(name = "DT_LTE_EARFCN_SUCCESSSUM")
	public String getDtLteEarfcnSuccessSum() {
		return dtLteEarfcnSuccessSum;
	}

	public void setDtLteEarfcnSuccessSum(String dtLteEarfcnSuccessSum) {
		this.dtLteEarfcnSuccessSum = dtLteEarfcnSuccessSum;
	}

	@Column(name = "DT_NR_SUCCESSSUM")
	public String getDtNRSuccessSum() {
		return dtNRSuccessSum;
	}

	public void setDtNRSuccessSum(String dtNRSuccessSum) {
		this.dtNRSuccessSum = dtNRSuccessSum;
	}

	@Column(name = "DT_LTE_SUMDEGREE")
	public String getDtLteSumDegree() {
		return dtLteSumDegree;
	}

	public void setDtLteSumDegree(String dtLteSumDegree) {
		this.dtLteSumDegree = dtLteSumDegree;
	}

	@Column(name = "DT_NR_SUMDEGREE")
	public String getDtNRSumDegree() {
		return dtNRSumDegree;
	}

	public void setDtNRSumDegree(String dtNRSumDegree) {
		this.dtNRSumDegree = dtNRSumDegree;
	}

	@Column(name = "DT_LTE_EARFCN_FAILRATION")
	public String getDtLteEarfcnFailRation() {
		return dtLteEarfcnFailRation;
	}

	public void setDtLteEarfcnFailRation(String dtLteEarfcnFailRation) {
		this.dtLteEarfcnFailRation = dtLteEarfcnFailRation;
	}

	@Column(name = "DT_NR_EARFCN_FAILRATION")
	public String getDtNREarfcnFailRation() {
		return dtNREarfcnFailRation;
	}

	public void setDtNREarfcnFailRation(String dtNREarfcnFailRation) {
		this.dtNREarfcnFailRation = dtNREarfcnFailRation;
	}

	@Column(name = "DT_NR_SSB_RSRP")
	public String getDtNrSsbRsrp() {
		return dtNrSsbRsrp;
	}

	public void setDtNrSsbRsrp(String dtNrSsbRsrp) {
		this.dtNrSsbRsrp = dtNrSsbRsrp;
	}

	@Column(name = "DT_NR_SSB_SINR")
	public String getDtNrSsbSinr() {
		return dtNrSsbSinr;
	}

	public void setDtNrSsbSinr(String dtNrSsbSinr) {
		this.dtNrSsbSinr = dtNrSsbSinr;
	}
	
	@Column(name = "DT_NR_SSB_THDL")
	public String getDtNrSsbThrouDL() {
		return dtNrSsbThrouDL;
	}

	public void setDtNrSsbThrouDL(String dtNrSsbThrouDL) {
		this.dtNrSsbThrouDL = dtNrSsbThrouDL;
	}

	@Column(name = "DT_NR_SSB_THUL")
	public String getDtNrSsbThrouUL() {
		return dtNrSsbThrouUL;
	}

	public void setDtNrSsbThrouUL(String dtNrSsbThrouUL) {
		this.dtNrSsbThrouUL = dtNrSsbThrouUL;
	}

	@Column(name = "DT_LTE_EARF_RSRP")
	public String getDtLteEarfcnRsrp() {
		return dtLteEarfcnRsrp;
	}

	public void setDtLteEarfcnRsrp(String dtLteEarfcnRsrp) {
		this.dtLteEarfcnRsrp = dtLteEarfcnRsrp;
	}

	@Column(name = "DT_LTE_EARF_SINR")
	public String getDtLteEarfcnSinr() {
		return dtLteEarfcnSinr;
	}

	public void setDtLteEarfcnSinr(String dtLteEarfcnSinr) {
		this.dtLteEarfcnSinr = dtLteEarfcnSinr;
	}

	@Column(name = "DT_LTE_FAIL_DEGREE")
	public String getDtLteFailSumDegree() {
		return dtLteFailSumDegree;
	}

	public void setDtLteFailSumDegree(String dtLteFailSumDegree) {
		this.dtLteFailSumDegree = dtLteFailSumDegree;
	}

	@Column(name = "DT_NR_FAIL_DEGREE")
	public String getDtNrFailSumDegree() {
		return dtNrFailSumDegree;
	}

	public void setDtNrFailSumDegree(String dtNrFailSumDegree) {
		this.dtNrFailSumDegree = dtNrFailSumDegree;
	}
	
	

	/**
	 * @return the ftpNrThrouDLRateMax
	 */
	@Column(name = "FTP_NR_THRDL_MAX")
	public String getFtpNrThrouDLRateMax() {
		return ftpNrThrouDLRateMax;
	}

	/**
	 * @param ftpNrThrouDLRateMax the ftpNrThrouDLRateMax to set
	 */
	public void setFtpNrThrouDLRateMax(String ftpNrThrouDLRateMax) {
		this.ftpNrThrouDLRateMax = ftpNrThrouDLRateMax;
	}

	/**
	 * @return the ftpNrThrouULRateMax
	 */
	@Column(name = "FTP_NR_THRUL_MAX")
	public String getFtpNrThrouULRateMax() {
		return ftpNrThrouULRateMax;
	}

	/**
	 * @param ftpNrThrouULRateMax the ftpNrThrouULRateMax to set
	 */
	public void setFtpNrThrouULRateMax(String ftpNrThrouULRateMax) {
		this.ftpNrThrouULRateMax = ftpNrThrouULRateMax;
	}

	/**
	 * @return the dtWeakCoverRateMin
	 */
	@Column(name = "DT_WEAK_COVER_MIN")
	public String getDtWeakCoverRateMin() {
		return dtWeakCoverRateMin;
	}

	/**
	 * @param dtWeakCoverRateMin the dtWeakCoverRateMin to set
	 */
	public void setDtWeakCoverRateMin(String dtWeakCoverRateMin) {
		this.dtWeakCoverRateMin = dtWeakCoverRateMin;
	}
	

	/**
	 * @return the dtWireCoverRate
	 */
	@Column(name = "DT_WIRE_COVER_RATE")
	public String getDtWireCoverRate() {
		return dtWireCoverRate;
	}

	/**
	 * @param dtWireCoverRate the dtWireCoverRate to set
	 */
	public void setDtWireCoverRate(String dtWireCoverRate) {
		this.dtWireCoverRate = dtWireCoverRate;
	}

	/**
	 * @return the dialVolteSuccessSum
	 */
	@Column(name = "DIAL_VOLTE_SUCCESS")
	public String getDialVolteSuccessSum() {
		return dialVolteSuccessSum;
	}

	/**
	 * @param dialVolteSuccessSum the dialVolteSuccessSum to set
	 */
	public void setDialVolteSuccessSum(String dialVolteSuccessSum) {
		this.dialVolteSuccessSum = dialVolteSuccessSum;
	}

	/**
	 * @return the dialVonrSuccessSum
	 */
	@Column(name = "DIAL_VONR_SUCCESS")
	public String getDialVonrSuccessSum() {
		return dialVonrSuccessSum;
	}

	/**
	 * @param dialVonrSuccessSum the dialVonrSuccessSum to set
	 */
	public void setDialVonrSuccessSum(String dialVonrSuccessSum) {
		this.dialVonrSuccessSum = dialVonrSuccessSum;
	}

	/**
	 * @return the nrSwitchSuccessRate
	 */
	@Column(name = "NR_SWITCH_SUCCESS_RATE")
	public String getNrSwitchSuccessRate() {
		return nrSwitchSuccessRate;
	}

	/**
	 * @param nrSwitchSuccessRate the nrSwitchSuccessRate to set
	 */
	public void setNrSwitchSuccessRate(String nrSwitchSuccessRate) {
		this.nrSwitchSuccessRate = nrSwitchSuccessRate;
	}
	
	/**
	 * @return the nrSwitchSuccessAvg
	 */
	@Column(name = "NR_SWITCH_SUCCESS_AVG")
	public String getNrSwitchSuccessAvg() {
		return nrSwitchSuccessAvg;
	}

	/**
	 * @param nrSwitchSuccessAvg the nrSwitchSuccessAvg to set
	 */
	public void setNrSwitchSuccessAvg(String nrSwitchSuccessAvg) {
		this.nrSwitchSuccessAvg = nrSwitchSuccessAvg;
	}
	
	/**
	 * @return the nrConnectSuccessSum
	 */
	@Column(name = "NR_CONNECT_SUCCESS_SUM")
	public String getNrConnectSuccessSum() {
		return nrConnectSuccessSum;
	}

	/**
	 * @param nrConnectSuccessSum the nrConnectSuccessSum to set
	 */
	public void setNrConnectSuccessSum(String nrConnectSuccessSum) {
		this.nrConnectSuccessSum = nrConnectSuccessSum;
	}

	/**
	 * @return the nrConnectTimeDelay
	 */
	@Column(name = "NR_CONNECT_TIMEDELAY")
	public String getNrConnectTimeDelay() {
		return nrConnectTimeDelay;
	}

	/**
	 * @param nrConnectTimeDelay the nrConnectTimeDelay to set
	 */
	public void setNrConnectTimeDelay(String nrConnectTimeDelay) {
		this.nrConnectTimeDelay = nrConnectTimeDelay;
	}
	

	/**
	 * @return the nrPdschRsrpFtpDL
	 */
	@Column(name = "NR_PDSCH_RSRP_FTPDL")
	public String getNrPdschRsrpFtpDL() {
		return nrPdschRsrpFtpDL;
	}

	/**
	 * @param nrPdschRsrpFtpDL the nrPdschRsrpFtpDL to set
	 */
	public void setNrPdschRsrpFtpDL(String nrPdschRsrpFtpDL) {
		this.nrPdschRsrpFtpDL = nrPdschRsrpFtpDL;
	}

	/**
	 * @return the nrPdschSinrFtpDL
	 */
	@Column(name = "NR_PDSCH_SINR_FTPDL")
	public String getNrPdschSinrFtpDL() {
		return nrPdschSinrFtpDL;
	}

	/**
	 * @param nrPdschSinrFtpDL the nrPdschSinrFtpDL to set
	 */
	public void setNrPdschSinrFtpDL(String nrPdschSinrFtpDL) {
		this.nrPdschSinrFtpDL = nrPdschSinrFtpDL;
	}

	/**
	 * @return the nrPdschRsrpFtpUL
	 */
	@Column(name = "NR_PDSCH_RSRP_FTPUL")
	public String getNrPdschRsrpFtpUL() {
		return nrPdschRsrpFtpUL;
	}

	/**
	 * @param nrPdschRsrpFtpUL the nrPdschRsrpFtpUL to set
	 */
	public void setNrPdschRsrpFtpUL(String nrPdschRsrpFtpUL) {
		this.nrPdschRsrpFtpUL = nrPdschRsrpFtpUL;
	}

	/**
	 * @return the nrPdschSinrFtpUL
	 */
	@Column(name = "NR_PDSCH_SINR_FTPUL")
	public String getNrPdschSinrFtpUL() {
		return nrPdschSinrFtpUL;
	}

	/**
	 * @param nrPdschSinrFtpUL the nrPdschSinrFtpUL to set
	 */
	public void setNrPdschSinrFtpUL(String nrPdschSinrFtpUL) {
		this.nrPdschSinrFtpUL = nrPdschSinrFtpUL;
	}

	/**
	 * @return the pdcpnrThrputDL
	 */
	@Column(name = "PDCPNR_THRPUT_DL")
	public String getPdcpnrThrputDL() {
		return pdcpnrThrputDL;
	}

	/**
	 * @param pdcpnrThrputDL the pdcpnrThrputDL to set
	 */
	public void setPdcpnrThrputDL(String pdcpnrThrputDL) {
		this.pdcpnrThrputDL = pdcpnrThrputDL;
	}

	/**
	 * @return the pdcpnrThrputUL
	 */
	@Column(name = "PDCPNR_THRPUT_UL")
	public String getPdcpnrThrputUL() {
		return pdcpnrThrputUL;
	}

	/**
	 * @param pdcpnrThrputUL the pdcpnrThrputUL to set
	 */
	public void setPdcpnrThrputUL(String pdcpnrThrputUL) {
		this.pdcpnrThrputUL = pdcpnrThrputUL;
	}

	/**
	 * @return the nrMacThrputDLMIN
	 */
	@Column(name = "NR_MACTHRPUT_DL_MIN")
	public String getNrMacThrputDLMIN() {
		return nrMacThrputDLMIN;
	}

	/**
	 * @param nrMacThrputDLMIN the nrMacThrputDLMIN to set
	 */
	public void setNrMacThrputDLMIN(String nrMacThrputDLMIN) {
		this.nrMacThrputDLMIN = nrMacThrputDLMIN;
	}

	/**
	 * @return the nrMacThrputULMin
	 */
	@Column(name = "NR_MACTHRPUT_UL_MIN")
	public String getNrMacThrputULMin() {
		return nrMacThrputULMin;
	}

	/**
	 * @param nrMacThrputULMin the nrMacThrputULMin to set
	 */
	public void setNrMacThrputULMin(String nrMacThrputULMin) {
		this.nrMacThrputULMin = nrMacThrputULMin;
	}

	/**
	 * @return the nrCwoWbCqiAvg
	 */
	@Column(name = "NR_CWOWB_CQI_AVG")
	public String getNrCwoWbCqiAvg() {
		return nrCwoWbCqiAvg;
	}

	/**
	 * @param nrCwoWbCqiAvg the nrCwoWbCqiAvg to set
	 */
	public void setNrCwoWbCqiAvg(String nrCwoWbCqiAvg) {
		this.nrCwoWbCqiAvg = nrCwoWbCqiAvg;
	}

	/**
	 * @return the nrRankIndicatMIn
	 */
	@Column(name = "NR_RANK_INDECT_MIN")
	public String getNrRankIndicatMIn() {
		return nrRankIndicatMIn;
	}

	/**
	 * @param nrRankIndicatMIn the nrRankIndicatMIn to set
	 */
	public void setNrRankIndicatMIn(String nrRankIndicatMIn) {
		this.nrRankIndicatMIn = nrRankIndicatMIn;
	}

	/**
	 * @return the nrDlUlSwitchSuccess
	 */
	@Column(name = "NR_DLUL_SWITCH_SUCCESS")
	public String getNrDlUlSwitchSuccess() {
		return nrDlUlSwitchSuccess;
	}

	/**
	 * @param nrDlUlSwitchSuccess the nrDlUlSwitchSuccess to set
	 */
	public void setNrDlUlSwitchSuccess(String nrDlUlSwitchSuccess) {
		this.nrDlUlSwitchSuccess = nrDlUlSwitchSuccess;
	}

	/**
	 * @return the nrDlUlSwitchDrop
	 */
	@Column(name = "NR_DLUL_SWITCHS_DROP")
	public String getNrDlUlSwitchDrop() {
		return nrDlUlSwitchDrop;
	}

	/**
	 * @param nrDlUlSwitchDrop the nrDlUlSwitchDrop to set
	 */
	public void setNrDlUlSwitchDrop(String nrDlUlSwitchDrop) {
		this.nrDlUlSwitchDrop = nrDlUlSwitchDrop;
	}
	
	

	/**
	 * @return the nrConnectBuildSucess
	 */
	@Column(name = "NR_CONNECT_BUILD_SUCESS")
	public String getNrConnectBuildSucess() {
		return nrConnectBuildSucess;
	}

	/**
	 * @param nrConnectBuildSucess the nrConnectBuildSucess to set
	 */
	public void setNrConnectBuildSucess(String nrConnectBuildSucess) {
		this.nrConnectBuildSucess = nrConnectBuildSucess;
	}

	/**
	 * @return the ftpGoodPdcpnrThrputDL
	 */
	@Column(name = "FTP_GOOD_PDCPNR_THR_DL")
	public String getFtpGoodPdcpnrThrputDL() {
		return ftpGoodPdcpnrThrputDL;
	}

	/**
	 * @param ftpGoodPdcpnrThrputDL the ftpGoodPdcpnrThrputDL to set
	 */
	public void setFtpGoodPdcpnrThrputDL(String ftpGoodPdcpnrThrputDL) {
		this.ftpGoodPdcpnrThrputDL = ftpGoodPdcpnrThrputDL;
	}

	/**
	 * @return the ftpGoodPdcpnrThrputUL
	 */
	@Column(name = "FTP_GOOD_PDCPNR_THR_UL")
	public String getFtpGoodPdcpnrThrputUL() {
		return ftpGoodPdcpnrThrputUL;
	}

	/**
	 * @param ftpGoodPdcpnrThrputUL the ftpGoodPdcpnrThrputUL to set
	 */
	public void setFtpGoodPdcpnrThrputUL(String ftpGoodPdcpnrThrputUL) {
		this.ftpGoodPdcpnrThrputUL = ftpGoodPdcpnrThrputUL;
	}

	@Column(name = "VOCSFB_SUCCESS")
	public String getVolteCsfbSuccess() {
		return volteCsfbSuccess;
	}

	public void setVolteCsfbSuccess(String volteCsfbSuccess) {
		this.volteCsfbSuccess = volteCsfbSuccess;
	}

	@Column(name = "VOCSFB_CALLBACK_SUCCESS")
	public String getVolteCsfbCallbackSuccess() {
		return volteCsfbCallbackSuccess;
	}

	public void setVolteCsfbCallbackSuccess(String volteCsfbCallbackSuccess) {
		this.volteCsfbCallbackSuccess = volteCsfbCallbackSuccess;
	}

	@Column(name = "VOCSFB_CALLBACK_TIME")
	public String getVolteCsfbCallbackTime() {
		return volteCsfbCallbackTime;
	}

	public void setVolteCsfbCallbackTime(String volteCsfbCallbackTime) {
		this.volteCsfbCallbackTime = volteCsfbCallbackTime;
	}

	@Column(name = "FTP_BAD_PDCPNR_THR_DL")
	public String getFtpBadPdcpnrThrputDL() {
		return ftpBadPdcpnrThrputDL;
	}

	public void setFtpBadPdcpnrThrputDL(String ftpBadPdcpnrThrputDL) {
		this.ftpBadPdcpnrThrputDL = ftpBadPdcpnrThrputDL;
	}

	@Column(name = "FTP_BAD_PDCPNR_THR_UL")
	public String getFtpBadPdcpnrThrputUL() {
		return ftpBadPdcpnrThrputUL;
	}

	public void setFtpBadPdcpnrThrputUL(String ftpBadPdcpnrThrputUL) {
		this.ftpBadPdcpnrThrputUL = ftpBadPdcpnrThrputUL;
	}

	@Column(name = "NR_STATION_SWITCH_SUM")
	public String getNrStationSwitchSum() {
		return nrStationSwitchSum;
	}

	public void setNrStationSwitchSum(String nrStationSwitchSum) {
		this.nrStationSwitchSum = nrStationSwitchSum;
	}

	@Column(name = "STATION_DT_NR_FAIL_DEGREE")
	public String getStationDtNrFailSumDegree() {
		return stationDtNrFailSumDegree;
	}

	public void setStationDtNrFailSumDegree(String stationDtNrFailSumDegree) {
		this.stationDtNrFailSumDegree = stationDtNrFailSumDegree;
	}
	
	@Column(name = "ENDC_GOOD_NRCONNECT_SUM")
	public String getEndcGoodNrConnectSum() {
		return endcGoodNrConnectSum;
	}

	public void setEndcGoodNrConnectSum(String endcGoodNrConnectSum) {
		this.endcGoodNrConnectSum = endcGoodNrConnectSum;
	}

	@Column(name = "RAO_NREARFCN_SUC_SUM")
	public String getRaoNREarfcnSuccessSum() {
		return raoNREarfcnSuccessSum;
	}

	public void setRaoNREarfcnSuccessSum(String raoNREarfcnSuccessSum) {
		this.raoNREarfcnSuccessSum = raoNREarfcnSuccessSum;
	}

	@Column(name = "RAO_LTEEARFCN_SUC_SUM")
	public String getRaoLteEarfcnSuccessSum() {
		return raoLteEarfcnSuccessSum;
	}

	public void setRaoLteEarfcnSuccessSum(String raoLteEarfcnSuccessSum) {
		this.raoLteEarfcnSuccessSum = raoLteEarfcnSuccessSum;
	}

	@Column(name = "MID_FTPDL_PDCPNRDL_THR")
	public String getMidFtpDlPdcpnrDlThr() {
		return midFtpDlPdcpnrDlThr;
	}

	public void setMidFtpDlPdcpnrDlThr(String midFtpDlPdcpnrDlThr) {
		this.midFtpDlPdcpnrDlThr = midFtpDlPdcpnrDlThr;
	}

	@Column(name = "MID_FTPUL_PDCPNRUL_THR")
	public String getMidFtpUlPdcpnrUlThr() {
		return midFtpUlPdcpnrUlThr;
	}

	public void setMidFtpUlPdcpnrUlThr(String midFtpUlPdcpnrUlThr) {
		this.midFtpUlPdcpnrUlThr = midFtpUlPdcpnrUlThr;
	}

	@Column(name = "MID_FTPDL_NRDL_THR_MAX")
	public String getMidFtpDlNrDlThrMax() {
		return midFtpDlNrDlThrMax;
	}

	public void setMidFtpDlNrDlThrMax(String midFtpDlNrDlThrMax) {
		this.midFtpDlNrDlThrMax = midFtpDlNrDlThrMax;
	}

	@Column(name = "MID_FTPUL_NRUL_THR_MAX")
	public String getMidFtpUlNrUlThrMax() {
		return midFtpUlNrUlThrMax;
	}

	public void setMidFtpUlNrUlThrMax(String midFtpUlNrUlThrMax) {
		this.midFtpUlNrUlThrMax = midFtpUlNrUlThrMax;
	}

	@Column(name = "BAD_FTPDL_NRDL_THR_MAX")
	public String getBadFtpDlNrDlThrMax() {
		return badFtpDlNrDlThrMax;
	}

	public void setBadFtpDlNrDlThrMax(String badFtpDlNrDlThrMax) {
		this.badFtpDlNrDlThrMax = badFtpDlNrDlThrMax;
	}

	@Column(name = "BAD_FTPUL_NRUL_THR_MAX")
	public String getBadFtpUlNrUlThrMax() {
		return badFtpUlNrUlThrMax;
	}

	public void setBadFtpUlNrUlThrMax(String badFtpUlNrUlThrMax) {
		this.badFtpUlNrUlThrMax = badFtpUlNrUlThrMax;
	}
	
	@Column(name = "GOOD_FTPDL_PDCPNRDL_THRMAX")
	public String getGoodFtpDlPdcpnrDLThrmax() {
		return goodFtpDlPdcpnrDLThrmax;
	}

	public void setGoodFtpDlPdcpnrDLThrmax(String goodFtpDlPdcpnrDLThrmax) {
		this.goodFtpDlPdcpnrDLThrmax = goodFtpDlPdcpnrDLThrmax;
	}

	@Column(name = "GOOD_FTPUL_PDCPNRUL_THRMAX")
	public String getGoodFtpUlPdcpnrULThrmax() {
		return goodFtpUlPdcpnrULThrmax;
	}

	public void setGoodFtpUlPdcpnrULThrmax(String goodFtpUlPdcpnrULThrmax) {
		this.goodFtpUlPdcpnrULThrmax = goodFtpUlPdcpnrULThrmax;
	}

	@Column(name = "RAODL_SSRSRP_SINR_100MIN")
	public String getRaoDLSSRsrpSinr100min() {
		return raoDLSSRsrpSinr100min;
	}

	public void setRaoDLSSRsrpSinr100min(String raoDLSSRsrpSinr100min) {
		this.raoDLSSRsrpSinr100min = raoDLSSRsrpSinr100min;
	}

	@Column(name = "RAODL_SSRSRP_SINR_105IN")
	public String getRaoDLSSRsrpSinr105min() {
		return raoDLSSRsrpSinr105min;
	}

	public void setRaoDLSSRsrpSinr105min(String raoDLSSRsrpSinr105min) {
		this.raoDLSSRsrpSinr105min = raoDLSSRsrpSinr105min;
	}

	@Column(name = "RAODL_SSRSRP_SINR_110MIN")
	public String getRaoDLSSRsrpSinr110min() {
		return raoDLSSRsrpSinr110min;
	}

	public void setRaoDLSSRsrpSinr110min(String raoDLSSRsrpSinr110min) {
		this.raoDLSSRsrpSinr110min = raoDLSSRsrpSinr110min;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAN_PARAM_ID")
	public PlanParamPojo getPlanParamPojo() {
		return planParamPojo;
	}

	public void setPlanParamPojo(PlanParamPojo planParamPojo) {
		this.planParamPojo = planParamPojo;
	}
	
	
}
