package com.datang.bean.customTemplate;


import java.util.List;

import lombok.Data;

/**
 * 自定义报表模板信息
 * @author lucheng
 * @date 2021年3月19日 上午11:14:02
 */
@Data
public class TemplateInfo implements Cloneable{
	//省
	private String province;
	
	//市
	private String city;
	
	//测试用例编号以及名称
	private String test_case_s;
	
	//场景类型1
	private String scene_type1;
	
	//场景类型2
	private String scene_type2;
	
	//场点名称
	private String scene_name;
	
	//测试卡（联通/电信/移动）
	private String test_card;
	
	//测试区域
	private String test_area;
	
	//承建商
	private String contractor;
	
	//主设备厂家
	private String equipement_producer;
	
	private List<String> sceneType1List;
	
	private List<String> sceneNameList;
	
	private List<String> contractorList;
	
	private List<String> equipementProducerList;
	
	private Integer muti_sample;
	
	private Integer row_index;
	
	private Integer table_index;
	
	private Integer sheet_index;
	
	private Integer log_num;
	
	private List<TestLogInfo> testLogList;
	
	@Override  
    public TemplateInfo clone() {  
		TemplateInfo templateInfo = null;  
        try{  
        	templateInfo = (TemplateInfo)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return templateInfo;  
    }
	
}
