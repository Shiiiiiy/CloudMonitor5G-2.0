package com.datang.domain.customTemplate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定义日志统计任务实体
 * 
 * @explain
 * @name CustomLogReportTask
 */
@Data
public class CustomLogValidatePojo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 274653069108830834L;
	
	/**
	 * 日志名称
	 */
	private String filenameUpload;
	
	//MD5信息上报
	/**
	 * 上报IMEI
	 */
	private String imeiUpload;
	
	/**
	 * md5
	 */
	private String md5Upload;

	private String startTimeUpload;

	private String endTimeUpload;
	
	private String minLatUpload;
	
	private String maxLatUpload;
	
	private String minLongUpload;
	
	private String maxLongUpload;
	
	//MD5信息解码
	/**
	 * 解码IMEI
	 */
	private String imeiDecode;
	/**
	 * 解码日志名称
	 */
	private String filenameDecode;
	
	/**
	 * 解码md5
	 */
	private String md5Decode;
	
	//MD5校验结果
	private String filenameCheck;
	
	private String md5Check;

	private String exceptionMd5;
	
	
	
	//业务事件信息上报
	private String eventTypeUpload;

	private String timestampUpload;

	private String longitudeUpload;
	
	private String latitudeUpload;
	
	//业务事件信息解码
	private String eventTypeDecoded;

	private String timestampDecode;

	private String longitudeDecode;
	
	private String latitudeDecode;
	
	//业务事件校验结果
	private String eventTypeCheck;
	
	private String timestampCheck;

	private String longitudeCheck;
	
	private String latitudeCheck;
	
	private String exceptionBusinessEvent;

}
