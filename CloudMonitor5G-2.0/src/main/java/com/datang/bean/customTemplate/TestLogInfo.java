package com.datang.bean.customTemplate;

import lombok.Data;

/**
 * 自定义报表的测试日志信息
 * @author lucheng
 * @date 2021年3月19日 上午11:13:36
 */
@Data
public class TestLogInfo {
	
	//省
	private String province;
	
	//市
	private String city;
	
	//测试用例编号以及名称
	private String test_case_s;
	
	//CQT的场景类型（二级场景）
	private String scene_type1;
	
	//场点名称
	private String scene_name;
	
	//测试卡（联通/电信/移动）
	private String test_card;
	
	//承建方（联通/电信）
	private String contractor;
	
	//主设备厂商(华为/中兴)
	private String equipement_producer;
	
	//仪表厂商
	private String instrument_producer;
	
	//测试手机型号
	private String mobile_phone_type;
	
	//日志时间
	private String log_time;
	
	//日志索引
	private String log_index;
	
	//完整的日志名称
	private String full_log_name;
}
