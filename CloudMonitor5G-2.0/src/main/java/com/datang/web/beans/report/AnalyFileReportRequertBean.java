package com.datang.web.beans.report;

import lombok.Data;

import java.util.Date;

/**
 * 
 * 
 * @explain
 * @name AlarmRequestBean
 * @author shenyanwei
 * @date 2016年7月12日上午10:36:04
 */
@Data
public class AnalyFileReportRequertBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176078388182457911L;

	/**
	 * 任务id
	 * */
	private Long taskId;

	/**
	 * 报表id
	 * */
	private String reportId;


	private String description;
}
