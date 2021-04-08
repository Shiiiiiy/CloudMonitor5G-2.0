/**
 * 
 */
package com.datang.web.action.platform.projectParam;

/**
 * 运营商类型域网络制式类型
 * 
 * @author yinzhipeng
 * @date:2015年10月16日 上午11:39:18
 * @version
 */
public interface ProjectParamInfoType {
	/**
	 * 中国移动
	 */
	public static final String CH_MO = "ChinaMobile";
	/**
	 * 中国联通
	 */
	public static final String CH_UN = "ChinaUnicom";
	/**
	 * 中国电信
	 */
	public static final String CH_TE = "ChinaTelecom";
	/**
	 * 5G
	 */
	public static final String FG = "5G";
	/**
	 * LTE
	 */
	public static final String LTE = "LTE";
	/**
	 * GSM
	 */
	public static final String GSM = "GSM";
	/**
	 * 5G-5G邻区
	 */
	public static final String FG_FG = "5G_5G_NB";
	/**
	 * 5G-LTE邻区
	 */
	public static final String FG_LTE = "5G_LTE_NB";
	/**
	 * TDL邻区
	 */
	public static final String TDL_NB = "TDL_NB";
	/**
	 * TDL-GSM邻区
	 */
	public static final String TDL_GSM_NB = "TDL_GSM_NB";
	/**
	 * LTE_5G配对小区
	 */
	public static final String LTE_5G = "LTE_5G";

}
