package com.datang.common.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.datang.bean.customTemplate.TemplateInfo;
import com.datang.bean.customTemplate.TestLogInfo;
import com.datang.constant.CustomReportConstant;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.exception.ApplicationException;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 自定义模板工具类
 * 注意：只能传入固定格式的DT和CQT的excel模板
 * @author lucheng
 * @date 2021年3月19日 上午10:42:15
 */
public class CustomTemplateUtils {
	private Log log = LogFactory.getLog(this.getClass());
	
	private List<String> logNameList; 
	private Workbook workbook;
    private String excel_template_name;
    
	private boolean is_dt;
	private List<TestLogInfo> log_infos = new ArrayList<TestLogInfo>();
	private List<TemplateInfo> table1 = new ArrayList<TemplateInfo>();
	private List<TemplateInfo> table2 = new ArrayList<TemplateInfo>();
	private List<TemplateInfo> table3 = new ArrayList<TemplateInfo>();
	private List<TemplateInfo> table4 = new ArrayList<TemplateInfo>();
    private int table1_row_start;
    private int table2_row_start;
    private int table3_row_start;
    private int table4_row_start;
    private TemplateInfo cur_template_info = new TemplateInfo();
    private int table_index = 1;//第几个表
    private int table_index2 = 0;//表中的位置
    private Map<Integer,List<String>> all_use_case = new HashMap<Integer, List<String>>();//测试用例集合
    
    /**
     * 方法入口
     * @author lucheng
     * @date 2021年3月19日 上午11:50:03
     */
    public void init() throws Exception{
    	if(excel_template_name.contains("DT")){
    		is_dt = true;
    	}else if(excel_template_name.contains("CQT")){
    		is_dt = false;
    	} else{
    		return;
    	}
    	boolean setLogNameRlt = setLogNames(logNameList);
    	if(setLogNameRlt){
    		WriteTemplateInfo();
    	}else{
    		throw new ApplicationException("日志为空或日志名格式不正确");
    	}
    	
    }
    
    /**
     * 获取所有日志名的参数汇总
     * @author lucheng
     * @date 2021年3月19日 下午2:01:52
     * @param logNameList
     */
    public boolean setLogNames(List<String> logNameList) throws Exception{
    	 table_index = 1;
    	 table_index2 = 0;
    	 log_infos.clear();
    	 if(is_dt){
    		 /**
    		 5G DT：log命名为“省（省名称一定要加省）_地级市（地市名一定要加市）
    	     _测试用例编号（编号中使用半角实心英文句号）及名称（编号和名称之间不空格，
    	        完全按照测试用例名称编写）_承建方（联通/电信）
    	        _主设备厂商_仪表厂商_测试手机型号_2021XXXX (测试日期以及log编号,可按照文件生成顺序编制)”
    	        实例：
    	        湖南省_长沙市_ 1.1 联通卡5G网络DT室外下行业务测试_联通_华为_鼎力_华为Mate20X_20210118_1
    		  */
    		 for (String pt : logNameList){
    			String name = pt.replaceAll(" ", "");
 	            //去掉后缀名
    			String[] res = name.substring(0, name.lastIndexOf(".")).split("_");
 	            if (res.length < 9){
 	            	continue;
 	            }
 	            TestLogInfo li = new TestLogInfo();
 	            li.setProvince(res[0]); //省
 	            li.setCity(res[1]);//城市
 	            li.setTest_case_s(res[2]);//测试用例编号及名称

 	            // 测试卡（联通/电信/移动）
 	            if (li.getTest_case_s().contains(CustomReportConstant.G_CARD_UNICOM)){
 	            	li.setTest_card(CustomReportConstant.G_CARD_UNICOM);
 	            }
 	            else if (li.getTest_case_s().contains(CustomReportConstant.G_CARD_TELCOM)){
 	            	li.setTest_card(CustomReportConstant.G_CARD_TELCOM);
 	            }else if (li.getTest_case_s().contains(CustomReportConstant.G_CARD_MOBILE)){
 	            	li.setTest_card(CustomReportConstant.G_CARD_MOBILE);
 	            }else{
 	            	continue;
 	            }

 	            //承建方（联通/电信）
 	            li.setContractor(res[3]);
 	            ////主设备厂商(华为/中兴)
 	            li.setEquipement_producer(res[4]);
 	            //仪表厂商
 	            li.setInstrument_producer(res[5]);
 	            //测试手机型号
 	            li.setMobile_phone_type(res[6]);
 	            li.setLog_time(res[7]);
 	            li.setLog_index(res[8]);
 	            li.setFull_log_name(pt);
 	            log_infos.add(li);
    		 }
    	 }else{
    		 /**
    		  * 5G CQT：log命名为“省（省名称一定要加省）_地级市
    	        （地市名一定要加市）_场景类型（如：高校、高铁车站等）
    	        _场景名称（如：清华大学）_测试用例编号（编号中使用半
    	        角实心英文句号）及名称（编号和名称之间不空格，完全
    	        按照测试用例名称编写）_承建方（联通/电信）_主设备厂商_仪表
    	        厂商_测试手机型号_2021XXXX (测试日期以及log编号,可按照文件生成顺序编制)”。
    	        实例：湖南省_长沙市_高校_清华大学_ 11.1 联通卡5G网络CQT室内下行业务测试_华为_鼎力_华为Mate20X_20210118_1
    	        湖南省_长沙市_高校_清华大学_11.1联通卡5G网络CQT室内下行业务测试_华为_鼎力_华为Mate20X_20210118_1
    		  */
    		 for (String pt : logNameList){
    			String name = pt.replaceAll(" ", "");
 	            //去掉后缀名
    			String[] res = name.substring(0, name.lastIndexOf(".")).split("_");
 	            if (res.length < 11){
 	            	continue; 
 	            }
 	            TestLogInfo li = new TestLogInfo();
 	            li.setProvince(res[0]); //省
	            li.setCity(res[1]);//城市
 	            li.setScene_type1(res[2]);//场景类型（二级场景）
 	            li.setScene_name(res[3]);//场点名称
 	            li.setTest_case_s(res[4]);//测试用例编号及名称

	            // 测试卡（联通/电信/移动）
	            if (li.getTest_case_s().contains(CustomReportConstant.G_CARD_UNICOM)){
	            	li.setTest_card(CustomReportConstant.G_CARD_UNICOM);
	            }
	            else if (li.getTest_case_s().contains(CustomReportConstant.G_CARD_TELCOM)){
	            	li.setTest_card(CustomReportConstant.G_CARD_TELCOM);
	            }else if (li.getTest_case_s().contains(CustomReportConstant.G_CARD_MOBILE)){
	            	li.setTest_card(CustomReportConstant.G_CARD_MOBILE);
	            }else{
	            	continue;
	            }

	            //承建方（联通/电信）
	            li.setContractor(res[5]);
	            ////主设备厂商(华为/中兴)
	            li.setEquipement_producer(res[6]);
	            //仪表厂商
	            li.setInstrument_producer(res[7]);
	            //测试手机型号
	            li.setMobile_phone_type(res[8]);
	            li.setLog_time(res[9]);
	            li.setLog_index(res[10]);
	            li.setFull_log_name(pt);

 	            log_infos.add(li);
    		 }
    	 }
    	 log.debug("__FUNCTION << Start");
    	 log.debug("__FUNCTION << log_infos size="+log_infos.size());
    	 return !log_infos.isEmpty();
    }
    
    /**
     * 保存excel需要插入的每一行日志参数，写入到table1-table4中
     * @author lucheng
     */
    public void WriteTemplateInfo() throws Exception{
    	table1.clear();
	    table2.clear();
	    table3.clear();
	    table4.clear();
	    table1_row_start = 5;
	    table2_row_start = 5;
	    table3_row_start = 4;
	    table4_row_start = 4;
	    
	    boolean ret = false;
	    if (is_dt)
	    {
//	    	List<String> list = new ArrayList<String>();
//	    	list.addAll(Arrays.asList(CustomReportConstant.DT_ALL_USE_CASE_1));
//	    	list.addAll(Arrays.asList(CustomReportConstant.DT_ALL_USE_CASE_1_2));
	    	all_use_case.put(1, Arrays.asList(CustomReportConstant.DT_ALL_USE_CASE_1));
	    	all_use_case.put(2, Arrays.asList(CustomReportConstant.DT_ALL_USE_CASE_2));
	    	all_use_case.put(3, Arrays.asList(CustomReportConstant.DT_ALL_USE_CASE_3));
	    	all_use_case.put(4, Arrays.asList(CustomReportConstant.DT_ALL_USE_CASE_4));
	        WriteDtTemplate();
	    }
	    else
	    {
	    	all_use_case.put(1, Arrays.asList(CustomReportConstant.CQT_ALL_USE_CASE_1));
	    	all_use_case.put(2, Arrays.asList(CustomReportConstant.CQT_ALL_USE_CASE_2));
	    	all_use_case.put(3, Arrays.asList(CustomReportConstant.CQT_ALL_USE_CASE_3));
	    	all_use_case.put(4, Arrays.asList(CustomReportConstant.CQT_ALL_USE_CASE_4));
	        WriteCqtTemplate();
	    }
	    table_index = 1;//第几个表
	    table_index2 = 0;//表中的位置
	    log.debug("__FUNCTION__ << end...");
    }
    
    /**
     * DT模板插入
     * @author lucheng
     */
    public void WriteDtTemplate() throws Exception{
    	log.debug("__FUNCTION__DT << begin get latiude according to log infos");
        Map<String, List<TestLogInfo>> province_city = Province();
        for (Entry<String, List<TestLogInfo>> province : province_city.entrySet()) {
        	Map<String, List<TestLogInfo>> cityMap = City(province.getValue());
        	for (Entry<String, List<TestLogInfo>> city : cityMap.entrySet()) {
        		TemplateInfo tmp = new TemplateInfo();
                tmp.setProvince(province.getKey());
                tmp.setCity(city.getKey());
    			table_index = 1;
    			WriteTableDt(city.getValue(), tmp);
                table_index = 2;
                WriteTableDt(city.getValue(), tmp);
                table_index = 3;
                WriteTableDt(city.getValue(), tmp);
                table_index = 4;
                WriteTableDt(city.getValue(), tmp);
        	}
		}
        log.debug("__FUNCTION__ << begin WriteDtTemplateImp");
//        return WriteDtTemplateImp();
    }
    
    /**
     * 把DT模板的日志信息写入table1-table4中
     * @author lucheng
     * @date 2021年3月19日 下午2:35:27
     * @param testlogList
     * @param tmp
     */
	public void WriteTableDt(List<TestLogInfo> testlogList, TemplateInfo tmp1) {
		TemplateInfo tmp = tmp1.clone();
		if (table_index < 3) {
			tmp.setMuti_sample(1);
		} else {
			tmp.setMuti_sample(0);
		}
		
		List<String> use_case = all_use_case.get(table_index);
		for (String test_case : use_case){
			List<TestLogInfo> logs_test_case = new ArrayList<TestLogInfo>();
			for (TestLogInfo s1 : testlogList){
				if (test_case.equals(s1.getTest_case_s()))
					logs_test_case.add(s1);
			}
			if (logs_test_case.isEmpty()){
				continue;
			}
			tmp.setTest_case_s(test_case);;//测试用例
	        tmp.setTest_card(logs_test_case.get(0).getTest_card());;// 测试卡（联通/电信/移动）
	        if (tmp.getTest_card().equals(CustomReportConstant.G_CARD_MOBILE)){
	        	//移动卡
	        	TemplateInfo tmp2 = tmp.clone();
	            tmp2.setRow_index(GetRowIndex());
	            tmp2.setTest_area("部分区域");
	            tmp2.setContractor(CustomReportConstant.G_CONTRATOR_ALL);
	            tmp2.setEquipement_producer("汇总");
	            PushBack(tmp2);
	        }else{
	        	//电信联通卡
	        	 tmp.setTest_area("主城区");
	             //3 承建方（联通/电信）=======================
	             Set<String> contrator_s = new HashSet<String>();
	             for (TestLogInfo contrator : logs_test_case){
	            	 contrator_s.add(contrator.getContractor());
	             }
	             List<String> contrator_v = new ArrayList<String>();
	             contrator_v.add(CustomReportConstant.G_CONTRATOR_ALL);
	             List<String> contrator_v2 = new ArrayList<String>();
	             if (contrator_s.contains("联通")){
	                 contrator_v.add(CustomReportConstant.G_CONTRATOR_ALL_UNICOM);
	                 contrator_v2.add(CustomReportConstant.G_CONTRATOR_UNICOM);
	             }
	             if (contrator_s.contains("电信")){
	                 contrator_v.add(CustomReportConstant.G_CONTRATOR_ALL_TELCOM);
	                 contrator_v2.add(CustomReportConstant.G_CONTRATOR_TELCOM);
	             }
	             //总体 联通承建总体 电信承建总体
	             for (String contrator : contrator_v){ //承建方集合
	                 List<TestLogInfo> logs_contrator = new ArrayList<TestLogInfo>();
	                 if (CustomReportConstant.G_CONTRATOR_ALL.equals(contrator)){
	                     logs_contrator = logs_test_case;
	                 }else
	                 {
	                     for (TestLogInfo contrator_test : logs_test_case)
	                     {
	                         if (contrator.contains(contrator_test.getContractor())){
	                        	 logs_contrator.add(contrator_test);
	                         }
	                     }
	                 }
	                 if (logs_contrator.isEmpty()){
	                	 continue;
	                 }
	                 TemplateInfo tmp4 = tmp.clone();
	                 tmp4.setContractor(contrator);

	                 //4 //主设备厂商(华为/中兴)
	                 Set<String> equipement_s = new HashSet<String>();
	                 for (TestLogInfo equipement : logs_contrator){
	                	 equipement_s.add(equipement.getEquipement_producer());
	                 }
	                 List<String> equipement_v = new ArrayList<String>();
	                 String equipement_all="";
	                 for (String eq1 : equipement_s)
	                 {
	                     equipement_v.add(eq1);
	                     equipement_all += eq1;
	                     equipement_all += "+";
	                 }
	                 if (!equipement_all.isEmpty())
	                 {
	                	 equipement_all = equipement_all.substring(0,equipement_all.length()-1);
	                 }
	                 
	                 if (equipement_v.isEmpty()){
	                	 continue;
	                 }
	                 if(contrator.contains("总体")){
	                     TemplateInfo tmp5 = tmp4;
	                     tmp5.setEquipement_producer(equipement_all);
	                     if (contrator .equals("总体")){
	                    	 tmp5.setEquipement_producer("汇总");
	                     }
	                     tmp5.setRow_index(GetRowIndex());;
	                     tmp5.setTable_index(table_index);
	                     tmp5.setLog_num(logs_contrator.size());
	                     
	                     List<String> contractorList = new ArrayList<String>();
	                     List<String> equipementProducerList = new ArrayList<String>();
	                     for (TestLogInfo log : logs_contrator) {
	                    	 contractorList.add(log.getContractor());
	                    	 equipementProducerList.add(log.getEquipement_producer());
						 }
	                     tmp5.setContractorList(contractorList);
	                     tmp5.setEquipementProducerList(equipementProducerList);
	                     PushBack(tmp5);
	                     continue;
	                 }
	             }
	             //联通承建 电信承建
	             for (String contrator : contrator_v2){ //承建方集合
	            	 List<TestLogInfo> logs_contrator = new ArrayList<TestLogInfo>();
	                 if ("总体" .equals(contrator)){
	                     logs_contrator = logs_test_case;
	                 }else{
	                     for (TestLogInfo contrator_test : logs_test_case)
	                     {
	                         if (contrator.contains(contrator_test.getContractor()))
	                             logs_contrator.add(contrator_test);
	                     }
	                 }
	                 if (logs_contrator.isEmpty()){
	                	 continue;
	                 }
	                 TemplateInfo tmp4 = tmp.clone();
	                 tmp4.setContractor(contrator);

	                 //4 //主设备厂商(华为/中兴)
	                 Set<String> equipement_s = new HashSet<String>();
	                 for (TestLogInfo equipement : logs_contrator){
	                	 equipement_s.add(equipement.getEquipement_producer());
	                 }
	                 List<String> equipement_v = new ArrayList<String>();
	                 String equipement_all = "";
	                 for (String eq1 : equipement_s){
	                     equipement_v.add(eq1);
	                     equipement_all += eq1;
	                     equipement_all += "+";
	                 }
	                 if (!equipement_all.isEmpty()){
	                	 equipement_all = equipement_all.substring(0,equipement_all.length()-1);
	                 }
	                 if (equipement_v.isEmpty()){
	                	 continue;
	                 }
	                 if (contrator.contains("总体")){
	                     TemplateInfo tmp5 = tmp4.clone();
	                     tmp5.setEquipement_producer(equipement_all);
	                     if (contrator.equals("总体")){
	                    	 tmp5.setEquipement_producer("汇总");
	                     }
	                     tmp5.setRow_index(GetRowIndex());
	                     tmp5.setTable_index(table_index);
	                     PushBack(tmp5);
	                     continue;
	                 }
	                 for (String eq2 : equipement_v){
	                     TemplateInfo tmp5 = tmp4.clone();
	                     tmp5.setEquipement_producer(eq2);
	                     tmp5.setRow_index(GetRowIndex());
	                     tmp5.setTable_index(table_index);
	                     PushBack(tmp5);
	                 }
	                 
	           }
	        }
		}
	}
    
    /**
     * CQT模板插入
     * @author lucheng
     */
    public void WriteCqtTemplate() throws Exception{
    	log.debug("__FUNCTION__CQT << begin get latiude according to log infos");
        Map<String, List<TestLogInfo>> province_city = Province();
        for (Entry<String, List<TestLogInfo>> province : province_city.entrySet()) {
        	Map<String, List<TestLogInfo>> cityMap = City(province.getValue());
        	for (Entry<String, List<TestLogInfo>> city : cityMap.entrySet()) {
        		TemplateInfo tmp = new TemplateInfo();
                tmp.setProvince(province.getKey());
                tmp.setCity(city.getKey());
    			table_index = 1;
    			WriteTableCqt(city.getValue(), tmp);
                table_index = 2;
                WriteTableCqt(city.getValue(), tmp);
                table_index = 3;
                WriteTableCqt(city.getValue(), tmp);
                table_index = 4;
                WriteTableCqt(city.getValue(), tmp);
        	}
		}
        log.debug("__FUNCTION__ << begin WriteCQTTemplateImp");
//    	return WriteCqtTemplateImp();
    }
    
	public void WriteTableCqt(List<TestLogInfo> testlogList, TemplateInfo tmp1) {
		TemplateInfo tmp = tmp1.clone();
		if (table_index < 3) {
			tmp.setMuti_sample(1);
		} else {
			tmp.setMuti_sample(0);
		}
		
		List<String> use_case = all_use_case.get(table_index);
		for (String test_case : use_case){
			List<TestLogInfo> logs_test_case = new ArrayList<TestLogInfo>();
			for (TestLogInfo s1 : testlogList){
				if (test_case.equals(s1.getTest_case_s()))
					logs_test_case.add(s1);
			}
			if (logs_test_case.isEmpty()){
				continue;
			}
			tmp.setTest_case_s(test_case);;//测试用例
	        tmp.setTest_card(logs_test_case.get(0).getTest_card());;// 测试卡（联通/电信/移动）
	        if (tmp.getTest_card().equals(CustomReportConstant.G_CARD_MOBILE)){
	        	//1) 场景类型=======================
	            Set<String> sence_s = new HashSet<String>();
	            for (TestLogInfo sence_type1 : logs_test_case){
	            	sence_s.add(sence_type1.getScene_type1());
	            }
	            List<String> sence_v = new ArrayList<String>();
	            sence_v.add(CustomReportConstant.G_SCENE_ALL);
	            for (String scene : sence_s) {
	            	sence_v.add(scene); //场景类型集合
	    		}
	            for (String sence_type1 : sence_v)
	            {
	                List<TestLogInfo> logs_sence = new ArrayList<TestLogInfo>();
	                if (CustomReportConstant.G_SCENE_ALL.equals(sence_type1)){
	                	logs_sence = logs_test_case;
	                }else{
	                    for (TestLogInfo logs : logs_test_case)
	                    {
	                        if (sence_type1.equals(logs.getScene_type1()))
	                            logs_sence.add(logs);
	                    }
	                }
	                if (logs_sence.isEmpty()){
	                	continue;
	                }
	                TemplateInfo tmp2 = tmp.clone();
	                tmp2.setScene_type1(sence_type1);
	                tmp2.setScene_type2(sence_type1);
	                //2)  场点名称==============================
	                Set<String> sence_names_s = new HashSet<String>();
	                for (TestLogInfo sence_name : logs_sence){
	                	sence_names_s.add(sence_name.getScene_name());
	                }
	                List<String> sence_names_v = new ArrayList<String>();//场点名称集合
	                sence_names_v.add(CustomReportConstant.G_SCENE_NAME_ALL);
		            for (String scene_name : sence_names_s) {
		            	sence_names_v.add(scene_name);
		    		}
	                for (String sence_name : sence_names_v){
	                    List<TestLogInfo> logs_sence_name = new ArrayList<TestLogInfo>();
	                    if (CustomReportConstant.G_SCENE_NAME_ALL == sence_name)
	                        logs_sence_name = logs_sence;
	                    else{
	                        for (TestLogInfo log_info : logs_sence)
	                        {
	                            if (sence_name.equals(log_info.getScene_name()))
	                                logs_sence_name.add(log_info);
	                        }
	                    }
	                    if (logs_sence_name.isEmpty()){
	                    	continue;
	                    }
	                    TemplateInfo tmp3 = tmp2.clone();
	                    tmp3.setScene_name(sence_name);
	                    if (tmp3.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL) 
	                    		&& !tmp3.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                    	continue;
	                    }else if (!tmp3.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL) 
	                    		&& tmp3.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                    	continue;
	                    }else if (tmp3.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL) 
	                    		&& tmp3.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                    	tmp3.setContractor("汇总");
	                    	tmp3.setEquipement_producer("汇总");
	                    }else{
	                    	tmp3.setContractor("-");
	                    	tmp3.setEquipement_producer("-");
	                    }
	                    tmp3.setRow_index(GetRowIndex());
	                    tmp3.setTable_index(table_index);
	                    tmp3.setLog_num(logs_sence_name.size());
	                    
	                    List<String> scene_type1s = new ArrayList<String>();
	                    List<String> scene_names = new ArrayList<String>();
	                    List<String> contractorList = new ArrayList<String>();
	                    List<String> equipementProducerList = new ArrayList<String>();
	                    for (TestLogInfo testLogInfo : logs_sence_name) {
	                    	scene_type1s.add(testLogInfo.getScene_type1());
	                    	scene_names.add(testLogInfo.getScene_name());
	                    	contractorList.add(testLogInfo.getContractor());
	                    	equipementProducerList.add(testLogInfo.getEquipement_producer());
						}
	                    tmp3.setSceneType1List(scene_type1s);
	                    tmp3.setSceneNameList(scene_names);
	                    tmp3.setContractorList(contractorList);
	                    tmp3.setEquipementProducerList(equipementProducerList);
	                    PushBack(tmp3);
	                }
	            }
	        }else{
	        	//1) 场景类型=======================
	            Set<String> sence_s = new HashSet<String>();
	            for (TestLogInfo sence_type1 : logs_test_case){
	            	sence_s.add(sence_type1.getScene_type1());
	            }
	            List<String> sence_v = new ArrayList<String>();//场景类型集合
	            sence_v.add(CustomReportConstant.G_SCENE_ALL);
	            for (String scene : sence_s) {
	            	sence_v.add(scene);
				}
	            for (String sence_type1 : sence_v){
	            	List<TestLogInfo> logs_sence = new ArrayList<TestLogInfo>();
	                if (CustomReportConstant.G_SCENE_ALL == sence_type1)
	                    logs_sence = logs_test_case;
	                else
	                {
	                    for (TestLogInfo logs : logs_test_case)
	                    {
	                        if (sence_type1 .equals(logs.getScene_type1()))
	                            logs_sence.add(logs);
	                    }
	                }
	                if (logs_sence.isEmpty()){
	                	continue;
	                }
	                TemplateInfo tmp2 = tmp.clone();
	                tmp2.setScene_type1(sence_type1);
	                tmp2.setScene_type2(sence_type1);
	                //2)  场点名称==============================
	                Set<String> sence_names_s = new HashSet<String>();
	                for (TestLogInfo sence_name : logs_sence){
	                	sence_names_s.add(sence_name.getScene_name());
	                }
	                List<String> sence_names_v = new ArrayList<String>();//场点名称集合
	                sence_names_v.add(CustomReportConstant.G_SCENE_NAME_ALL);
		            for (String scene_name : sence_names_s) {
		            	sence_names_v.add(scene_name);
		    		}
	                for (String sence_name : sence_names_v){
	                    List<TestLogInfo> logs_sence_name = new ArrayList<TestLogInfo>();
	                    if (CustomReportConstant.G_SCENE_NAME_ALL == sence_name)
	                        logs_sence_name = logs_sence;
	                    else{
	                        for (TestLogInfo log_info : logs_sence)
	                        {
	                            if (sence_name.equals(log_info.getScene_name()))
	                                logs_sence_name.add(log_info);
	                        }
	                    }
	                    if (logs_sence_name.isEmpty()){
	                    	continue;
	                    }
	                    
	                    TemplateInfo tmp3 = tmp2.clone();
	                    tmp3.setScene_name(sence_name);
	                    //3 承建方（联通/电信）=======================
	                    /* 场景,场点 都为汇总，或都为粒度
		                    */
	                    if (tmp3.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL) 
	                    		&& !tmp3.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                    	continue;
	                    }
	                    if (!tmp3.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL) 
	                    		&& tmp3.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                    	continue;
	                    }
	                    
	                    if (tmp3.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL)
	                    		&&tmp3.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                    	//场景,场点 都为汇总时和dt一样
	                    	Set<String> contrator_s = new HashSet<String>();
	                        for (TestLogInfo contrator : logs_sence_name){
	                        	contrator_s.add(contrator.getContractor());
	                        }
		       	            List<String> contrator_v = new ArrayList<String>();
		    	            contrator_v.add(CustomReportConstant.G_CONTRATOR_ALL_CQT);
		    	            List<String> contrator_v2 = new ArrayList<String>();
		    	            if (contrator_s.contains("联通")){
		    	                 contrator_v.add("联通承建汇总");
		    	                 contrator_v2.add("联通承建");
		    	            }
		    	            if (contrator_s.contains("电信")){
		    	                 contrator_v.add("电信承建汇总");
		    	                 contrator_v2.add("电信承建");
		    	            }
		    	            //电联承建汇总 联通承建总体 电信承建总体
	                        for (String contrator : contrator_v){ //承建方集合
	                        	List<TestLogInfo> logs_contrator = new ArrayList<TestLogInfo>();
	                            if (CustomReportConstant.G_CONTRATOR_ALL_CQT.equals(contrator))
	                                logs_contrator = logs_sence_name;
	                            else
	                            {
	                                for (TestLogInfo contrator_test : logs_sence_name)
	                                {
	                                    if (contrator.contains(contrator_test.getContractor()))
	                                        logs_contrator.add(contrator_test);
	                                }
	                            }
	                            if (logs_contrator.isEmpty()){
	                            	continue;
	                            }
	                            TemplateInfo tmp4 = tmp3.clone();
	                            tmp4.setContractor(contrator);

	                            //4 //主设备厂商(华为/中兴)
	                            Set<String> equipement_s = new HashSet<String>();
	                            for (TestLogInfo equipement : logs_contrator){
	                            	equipement_s.add(equipement.getEquipement_producer());
	                            }
	                            List<String> equipement_v = new ArrayList<String>();
	                            String equipement_all = "";
	                            for (String eq1 : equipement_s)
	                            {
	                                equipement_v.add(eq1);
	                                equipement_all += eq1;
	                                equipement_all += "+";
	                            }
	                            if (!equipement_all.isEmpty())
	                            {
	                            	equipement_all = equipement_all.substring(0,equipement_all.length()-1);
	                            }
	                            if (equipement_v.isEmpty()){
	                            	continue;
	                            }
	                            if (contrator.contains("汇总")){
	                                TemplateInfo tmp5 = tmp4.clone();
	                                tmp5.setEquipement_producer(equipement_all);
	                                if (CustomReportConstant.G_CONTRATOR_ALL_CQT.equals(contrator)){
	                                	tmp5.setEquipement_producer("主设备厂商汇总");
	                                }
	                                tmp5.setRow_index(GetRowIndex());
	                                tmp5.setTable_index(table_index);
	                                tmp5.setLog_num(logs_contrator.size());
	                                
	                                List<String> scene_type1s = new ArrayList<String>();
	        	                    List<String> scene_names = new ArrayList<String>();
	        	                    List<String> contractorList = new ArrayList<String>();
	        	                    List<String> equipementProducerList = new ArrayList<String>();
	        	                    for (TestLogInfo testLogInfo : logs_contrator) {
	        	                    	scene_type1s.add(testLogInfo.getScene_type1());
	        	                    	scene_names.add(testLogInfo.getScene_name());
	        	                    	contractorList.add(testLogInfo.getContractor());
	        	                    	equipementProducerList.add(testLogInfo.getEquipement_producer());
	        						}
	        	                    tmp5.setSceneType1List(scene_type1s);
	        	                    tmp5.setSceneNameList(scene_names);
	        	                    tmp5.setContractorList(contractorList);
	        	                    tmp5.setEquipementProducerList(equipementProducerList);
	                                PushBack(tmp5);
	                                continue;
	                            }
	                        }
	                        //联通承建 电信承建
	                        for (String contrator : contrator_v2){  //承建方集合
	                        	List<TestLogInfo> logs_contrator = new ArrayList<TestLogInfo>();
	                        	if (CustomReportConstant.G_CONTRATOR_ALL_CQT.equals(contrator))
	                                logs_contrator = logs_sence_name;
	                            else{
	                                for (TestLogInfo contrator_test : logs_sence_name)
	                                {
	                                    if (contrator.contains(contrator_test.getContractor()))
	                                        logs_contrator.add(contrator_test);
	                                }
	                            }
	                            if (logs_contrator.isEmpty()){
	                            	continue;
	                            }
	                            TemplateInfo tmp4 = tmp3.clone();
	                            tmp4.setContractor(contrator);

	                            //4 //主设备厂商(华为/中兴)
	                            Set<String> equipement_s = new HashSet<String>();
	                            for (TestLogInfo equipement : logs_contrator){
	                            	equipement_s.add(equipement.getEquipement_producer());
	                            }
	                            List<String> equipement_v = new ArrayList<String>();
	                            String equipement_all = "";
	                            for (String eq1 : equipement_s)
	                            {
	                                equipement_v.add(eq1);
	                                equipement_all += eq1;
	                                equipement_all += "+";
	                            }
	                            if (!equipement_all.isEmpty())
	                            {
	                            	equipement_all = equipement_all.substring(0,equipement_all.length()-1);
	                            }
	                            if (equipement_v.isEmpty()){
	                            	continue;
	                            }
	                            if (contrator.contains("汇总")){
	                                TemplateInfo tmp5 = tmp4.clone();
	                                tmp5.setEquipement_producer(equipement_all);
	                                if (CustomReportConstant.G_CONTRATOR_ALL_CQT.equals(contrator)){
	                                	tmp5.setEquipement_producer("主设备厂商汇总");
	                                }
	                                tmp5.setRow_index(GetRowIndex());
	                                PushBack(tmp5);
	                                continue;
	                            }
	                            for (String eq2 : equipement_v)
	                            {
	                                TemplateInfo tmp5 = tmp4.clone();
	                                tmp5.setEquipement_producer(eq2);
	                                tmp5.setRow_index(GetRowIndex());
	                                tmp5.setTable_index(table_index);
	                                tmp5.setLog_num(logs_contrator.size());
	                                
	                                List<String> scene_type1s = new ArrayList<String>();
	        	                    List<String> scene_names = new ArrayList<String>();
	        	                    List<String> contractorList = new ArrayList<String>();
	        	                    List<String> equipementProducerList = new ArrayList<String>();
	        	                    for (TestLogInfo testLogInfo : logs_contrator) {
	        	                    	scene_type1s.add(testLogInfo.getScene_type1());
	        	                    	scene_names.add(testLogInfo.getScene_name());
	        	                    	contractorList.add(testLogInfo.getContractor());
	        	                    	equipementProducerList.add(testLogInfo.getEquipement_producer());
	        						}
	        	                    tmp5.setSceneType1List(scene_type1s);
	        	                    tmp5.setSceneNameList(scene_names);
	        	                    tmp5.setContractorList(contractorList);
	        	                    tmp5.setEquipementProducerList(equipementProducerList);
	                                PushBack(tmp5);
	                            }
	                        }
	                        
	                    }else{
	                    	//场景,场点 都为粒度
	                        //承建方和设备都为粒度
	                        Set<String> contrator_s = new HashSet<String>();
	                        for (TestLogInfo contrator : logs_sence_name){
	                        	contrator_s.add(contrator.getContractor());
	                        }
	                        List<String> contrator_v = new ArrayList<String>();
	                        if (contrator_s.contains("联通"))
	                            contrator_v.add("联通承建");
	                        if (contrator_s.contains("电信"))
	                            contrator_v.add("电信承建");
	                        
	                        for (String contrator : contrator_v){
	                            List<TestLogInfo> logs_contrator = new ArrayList<TestLogInfo>();
	                            for (TestLogInfo contrator_test : logs_sence_name)
	                            {
	                                if (contrator.contains(contrator_test.getContractor()))
	                                    logs_contrator.add(contrator_test);
	                            }
	                            if (logs_contrator.isEmpty()){
	                            	continue;
	                            }
	                            TemplateInfo tmp4 = tmp3.clone();
	                            tmp4.setContractor(contrator);

	                            //4  主设备厂商(华为/中兴)
	                            Set<String> equipement_s = new HashSet<String>();
	                            for (TestLogInfo equipement : logs_contrator){
	                            	equipement_s.add(equipement.getEquipement_producer());
	                            }
	                            for (String eq2 : equipement_s){
	                                TemplateInfo tmp5 = tmp4.clone();
	                                tmp5.setEquipement_producer(eq2);
	                                tmp5.setRow_index(GetRowIndex());
	                                tmp5.setTable_index(table_index);
	                                tmp5.setLog_num(logs_contrator.size());
	                                List<String> scene_type1s = new ArrayList<String>();
	        	                    List<String> scene_names = new ArrayList<String>();
	        	                    List<String> contractorList = new ArrayList<String>();
	        	                    List<String> equipementProducerList = new ArrayList<String>();
	        	                    for (TestLogInfo testLogInfo : logs_contrator) {
	        	                    	scene_type1s.add(testLogInfo.getScene_type1());
	        	                    	scene_names.add(testLogInfo.getScene_name());
	        	                    	contractorList.add(testLogInfo.getContractor());
	        	                    	equipementProducerList.add(testLogInfo.getEquipement_producer());
	        						}
	        	                    tmp5.setSceneType1List(scene_type1s);
	        	                    tmp5.setSceneNameList(scene_names);
	        	                    tmp5.setContractorList(contractorList);
	        	                    tmp5.setEquipementProducerList(equipementProducerList);
	                                PushBack(tmp5);
	                            }
	                        }
	                    }
	                }
	            }
	        }
		}
	}
	
	/**
	 * 根据行信息获取对应的所有日志
	 * @author lucheng
	 * @date 2021年3月20日 上午11:09:04
	 * @param tli
	 * @return
	 */
	public List<TestLogInfo> FindLogs(TemplateInfo  tli){
	    //0
	    List<TestLogInfo> v0 = new ArrayList<TestLogInfo>();//province,city,test_case,test_card
	    for (TestLogInfo testLogInfo : log_infos) {
			if(testLogInfo.getProvince().equals(tli.getProvince()) && testLogInfo.getCity().equals(tli.getCity())
					&& testLogInfo.getTest_case_s().equals(tli.getTest_case_s()) && testLogInfo.getTest_card().equals(tli.getTest_card())){
				v0.add(testLogInfo);
			}
		}
	    
	    if (v0.isEmpty()){
	    	return v0;
	    }

	    if (is_dt){
	        if (tli.getTest_card().equals(CustomReportConstant.G_CARD_MOBILE))
	            return v0;
	        //3)
	        List<TestLogInfo> v3 = new ArrayList<TestLogInfo>();
	        if (tli.getContractor().equals("总体")){
	        	 v3 = v0;
	        }else{
	        	 v3 = FindLogsByContractor(v0, tli.getContractor());
	        }
	        if (v3.isEmpty()) return v3;

	        //4)
	        List<TestLogInfo> v4 = new ArrayList<TestLogInfo>();
	        if (tli.getEquipement_producer().contains("汇总")){
	        	v4 = v3;
	        }else{
	        	v4 = FindLogsByEquipement_producer(v3, tli.getEquipement_producer());

	        }
	        return v4;
	    }else{
	    	if (tli.getTest_card().equals(CustomReportConstant.G_CARD_MOBILE)){
	            if (tli.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL)
	            		&&tli.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	            	return v0;
	            }else{
	                List<TestLogInfo> v3 = new ArrayList<TestLogInfo>();
	                v3 = FindLogsBySenceType1(v0, tli.getScene_type1());
	                if (v3.isEmpty()){
	                	return v3;
	                }
	                List<TestLogInfo> v4 = new ArrayList<TestLogInfo>();
	                v4 = FindLogsBySenceName(v3, tli.getScene_name());
	                return v4;
	            }
	        }else{
	        	 if (tli.getScene_type1().equals(CustomReportConstant.G_SCENE_ALL)
		            		&&tli.getScene_name().equals(CustomReportConstant.G_SCENE_NAME_ALL)){
	                if (tli.getContractor().equals(CustomReportConstant.G_CONTRATOR_ALL_CQT)){
	                	return v0;
	                }
	                List<TestLogInfo> v1 = new ArrayList<TestLogInfo>();
	                v1 = FindLogsByContractor(v0, tli.getContractor());
	                //联通承建总体  电信承建总体
	                if (tli.getContractor().contains("总体") || tli.getContractor().contains("汇总")){
	                	return v1;
	                }
	                List<TestLogInfo> v2 = new ArrayList<TestLogInfo>();
	                v2 = FindLogsByEquipement_producer(v1, tli.getEquipement_producer());
	                return v2;
	            }else {
	                List<TestLogInfo> v1 = new ArrayList<TestLogInfo>();
	                for (TestLogInfo s : v0)
	                {
	                    if (tli.getScene_type1().equals(s.getScene_type1()) && tli.getScene_name().equals(s.getScene_name())
	                    	&&tli.getContractor().contains(s.getContractor()) && tli.getEquipement_producer().contains(s.getEquipement_producer()))
	                        v1.add(s);
	                }
	                return v1;
	            }
	        }       
	    }
	}
	
	
	/**
	 * DT报告汇总sheet插入
	 * @author lucheng
	 * @date 2021年3月21日 上午11:38:21
	 */
	public void dt_func_insert(Workbook workbook, String[] use_case1,List<TemplateInfo> table, Integer[] row, String[] col ,Integer[] row_sa, String[] col_sa){
		for (String use_case : use_case1){
            List<TemplateInfo> ti_use_case = func_use_case(table, use_case);
            if (ti_use_case.isEmpty()){
            	continue;
            }
            //F6 联通  I9电信 L11移动
            int start_col = 6;
            int start_col_sa = 13;
            String[] contractor_all;
            if (ti_use_case.get(0).getTest_card().equals(CustomReportConstant.G_CARD_UNICOM)){
                start_col = CustomReportConstant.START_COL_UNICOM;
                start_col_sa = CustomReportConstant.START_COL_UNICOM_SA;
                contractor_all = CustomReportConstant.DT_CONTRATOR_ALL_UNICOM_TELCOM;
            }
            else if (ti_use_case.get(0).getTest_card().equals(CustomReportConstant.G_CARD_TELCOM)){
                start_col = CustomReportConstant.START_COL_TELCOM;
                start_col_sa = CustomReportConstant.START_COL_TELCOM_SA;
                contractor_all = CustomReportConstant.DT_CONTRATOR_ALL_UNICOM_TELCOM;
            }else{
                start_col = CustomReportConstant.START_COL__MOBILE;
                start_col_sa = CustomReportConstant.START_COL__MOBILE_SA;
                contractor_all = CustomReportConstant.DT_CONTRATOR_ALL_MOBILE;
            }

            //"总体","联通承建总体","电信承建总体"
            //"总体"
            for (String con : contractor_all)
            {
                List<TemplateInfo> contractor_v1 = func_contractor(ti_use_case, con);
                if (contractor_v1.isEmpty())
                {
                    start_col += 1;
                    continue;
                }
                TemplateInfo cur = contractor_v1.get(0);
                List<String> msgs = new ArrayList<String>();
                Sheet sheetAt = workbook.getSheetAt(cur.getSheet_index());
            	Sheet sheetAll = workbook.getSheetAt(1);
                //nsa + sa==============================
                for (int k=0;k<col.length;k++)
                {
                	Row createRow = sheetAt.getRow(cur.getRow_index());
                	if(createRow!=null){
                		Cell createCell = createRow.getCell(numberTransform2(col[k])-1);
                		if(createCell!=null){
                			String cellValue = getCellValue(createCell);
                			Row createRowAll = sheetAll.getRow(row[k]-1);
                			if(createRowAll!= null && StringUtils.hasText(cellValue)){
                				Cell createCellAll = createRowAll.createCell(start_col-1);
                            	createCellAll.setCellValue(cellValue);
                			}
                		}
                	}
                	
                }
                start_col += 1;
                //sa====================================
                msgs.clear();
                for (int k=0;k<col_sa.length;k++)
                {
                	Row createRow = sheetAt.getRow(cur.getRow_index());
                	if(createRow!=null){
                		Cell createCell = createRow.getCell(numberTransform2(col_sa[k])-1);
                		if(createCell!=null){
                			String cellValue = getCellValue(createCell);
                        	
                        	Row createRowAll = sheetAll.getRow(row_sa[k]-1);
                        	if(createRowAll!= null && StringUtils.hasText(cellValue)){
                        		Cell createCellAll = createRowAll.createCell(start_col_sa-1);
                            	createCellAll.setCellValue(cellValue);
                			}
                		}
                	}
                }
                start_col_sa += 1;
            }
        }
	}
	
	/**
	 * CQT报告汇总sheet插入
	 * @author lucheng
	 * @date 2021年3月21日 上午11:38:21
	 */
	public void cqt_func_insert(Workbook workbook, String[] use_case1,List<TemplateInfo> table, Integer[] row, String[] col ,Integer[] row_sa, String[] col_sa){
		for (String use_case : use_case1){
            List<TemplateInfo> ti_use_case = func_use_case(table, use_case);
            if (ti_use_case.isEmpty()){
            	continue;
            }
            //F6 联通  I9电信 L11移动
            int start_col = 6;
            int start_col_sa = 13;
            String[] contractor_all;
            if (ti_use_case.get(0).getTest_card().equals(CustomReportConstant.G_CARD_UNICOM)){
                start_col = CustomReportConstant.START_COL_UNICOM;
                start_col_sa = CustomReportConstant.START_COL_UNICOM_SA;
                contractor_all = CustomReportConstant.CQT_CONTRATOR_ALL_UNICOM_TELCOM;
            }
            else if (ti_use_case.get(0).getTest_card().equals(CustomReportConstant.G_CARD_TELCOM)){
                start_col = CustomReportConstant.START_COL_TELCOM;
                start_col_sa = CustomReportConstant.START_COL_TELCOM_SA;
                contractor_all = CustomReportConstant.CQT_CONTRATOR_ALL_UNICOM_TELCOM;
            }else{
                start_col = CustomReportConstant.START_COL__MOBILE;
                start_col_sa = CustomReportConstant.START_COL__MOBILE_SA;
                contractor_all = CustomReportConstant.CQT_CONTRATOR_ALL_MOBILE;
            }

            //"总体","联通承建总体","电信承建总体"
            //"总体"
            for (String con : contractor_all)
            {
                List<TemplateInfo> contractor_v1 = func_contractor(ti_use_case, con);
                if (contractor_v1.isEmpty())
                {
                    start_col += 1;
                    continue;
                }
                TemplateInfo cur = contractor_v1.get(0);
                List<String> msgs = new ArrayList<String>();
                Sheet sheetAt = workbook.getSheetAt(cur.getSheet_index());
            	Sheet sheetAll = workbook.getSheetAt(1);
                //nsa + sa==============================
                for (int k=0;k<col.length;k++)
                {
                	Row createRow = sheetAt.getRow(cur.getRow_index());
                	if(createRow!=null){
                		Cell createCell = createRow.getCell(numberTransform2(col[k])-1);
                		if(createCell!=null){
                			String cellValue = getCellValue(createCell);
                			Row createRowAll = sheetAll.getRow(row[k]-1);
                			if(createRowAll!= null && StringUtils.hasText(cellValue)){
                				Cell createCellAll = createRowAll.createCell(start_col-1);
                            	createCellAll.setCellValue(cellValue);
                			}
                		}
                	}
                	
                }
                start_col += 1;
                //sa====================================
                msgs.clear();
                for (int k=0;k<col_sa.length;k++)
                {
                	Row createRow = sheetAt.getRow(cur.getRow_index());
                	if(createRow!=null){
                		Cell createCell = createRow.getCell(numberTransform2(col_sa[k])-1);
                		if(createCell!=null){
                			String cellValue = getCellValue(createCell);
                        	
                        	Row createRowAll = sheetAll.getRow(row_sa[k]-1);
                        	if(createRowAll!= null && StringUtils.hasText(cellValue)){
                        		Cell createCellAll = createRowAll.createCell(start_col_sa-1);
                            	createCellAll.setCellValue(cellValue);
                			}
                		}
                	}
                	
                }
                start_col_sa += 1;
            }
        }
	}
	
	public List<TemplateInfo> func_use_case(List<TemplateInfo> v,String use_case){
		List<TemplateInfo> v1 = new ArrayList<TemplateInfo>();
        for (TemplateInfo s : v)
        {
            if (s.getTest_case_s().equals(use_case))
                v1.add(s);
        }
        return v1;
	}
	
	public List<TemplateInfo> func_contractor(List<TemplateInfo> v,String contrator){
		List<TemplateInfo> v1 = new ArrayList<TemplateInfo>();
        for (TemplateInfo s : v)
        {
            if (s.getContractor()!=null && s.getContractor().equals(contrator))
                v1.add(s);
        }
        return v1;
	}
	
    /**
     * excel的列索引英语转化为数字
     * @author lucheng
     * @date 2021年3月20日 下午10:12:24
     * @param str
     * @return
     */
    public int numberTransform2(String str){
        char[] strArray = str.toCharArray();
        int num = 0;
        for(int i =0; i < strArray.length; i++)
        {
            num = num * 26 + ((int)(strArray[i] -'A') +1);
        }
        return num;
    }

    
    
    
    /**
     * 对不同省下的日志归类
     * @author lucheng
     * @date 2021年3月19日 下午2:19:48
     */
    public Map<String, List<TestLogInfo>> Province(){
    	Map<String, List<TestLogInfo>> m = new HashMap<String, List<TestLogInfo>>();
        for (TestLogInfo s : log_infos){
        	List<TestLogInfo> logList = null;
        	if(m.get(s.getProvince())==null){
        		logList = new ArrayList<TestLogInfo>();
        	}else{
        		logList = m.get(s.getProvince());
        	}
        	logList.add(s);
        	m.put(s.getProvince(), logList);
        }
        return m;
    }
    
    /**
     * 对省下的不同市的日志归类
     * @author lucheng
     * @date 2021年3月19日 下午2:20:27
     */
    public Map<String, List<TestLogInfo>> City(List<TestLogInfo> list){
    	Map<String, List<TestLogInfo>> m = new HashMap<String, List<TestLogInfo>>();
        for (TestLogInfo s : list){
        	List<TestLogInfo> logList = null;
        	if(m.get(s.getCity())==null){
        		logList = new ArrayList<TestLogInfo>();
        	}else{
        		logList = m.get(s.getCity());
        	}
        	logList.add(s);
        	m.put(s.getCity(), logList);
        }
        return m;
    }
    
    /**
     * 根据承建商找到对应的日志信息
     * @author lucheng
     * @date 2021年3月20日 上午11:02:36
     * @param src
     * @param contractor
     */
    public List<TestLogInfo> FindLogsByContractor(List<TestLogInfo> src,String contractor){
        List<TestLogInfo> v = new ArrayList<TestLogInfo>();
        for (TestLogInfo s : src)
        {
            if (contractor.contains(s.getContractor()))
                v.add(s);
        }
        return v;
    }
    
    /**
     * 根据设备厂家找到对应的日志信息
     * @author lucheng 
     * @date 2021年3月20日 上午11:04:33
     * @param src
     * @param equipement_producer
     */
    public List<TestLogInfo> FindLogsByEquipement_producer(List<TestLogInfo> src,String equipement_producer){
    	List<TestLogInfo> v = new ArrayList<TestLogInfo>();
        for (TestLogInfo s : src){
            if (equipement_producer.contains(s.getEquipement_producer()))
                v.add(s);
        }
        return v;
    }
    
    /**
     * 根据场景类型获取对应的日志信息
     * @author lucheng
     * @date 2021年3月20日 上午11:07:20
     */
    public List<TestLogInfo> FindLogsBySenceType1(List<TestLogInfo> src,String sence_type1){
    	List<TestLogInfo> v = new ArrayList<TestLogInfo>();
        for (TestLogInfo s : src){
            if (sence_type1.contains(s.getScene_type1()))
                v.add(s);
        }
        return v;
    }
    
    /**
     * 根据场点名称获取对应的日志信息
     * @author lucheng
     * @date 2021年3月20日 上午11:07:40
     */
    public List<TestLogInfo> FindLogsBySenceName(List<TestLogInfo> src,String sence_name){
    	List<TestLogInfo> v = new ArrayList<TestLogInfo>();
        for (TestLogInfo s : src){
            if (sence_name.contains(s.getScene_name()))
                v.add(s);
        }
        return v;
    }
    
    /**
     * excel每行结果中保存sheet页的索引
     * @author lucheng
     * @date 2021年3月20日 下午3:11:59
     * @param sheetIndex
     */
    public void saveSheetIndex(Integer sheetIndex){
    	if (1 == table_index){
    		for (TemplateInfo info: table1) {
    			info.setSheet_index(sheetIndex);
			}
    	}else if (2 == table_index){
    		for (TemplateInfo info: table2) {
				info.setSheet_index(sheetIndex);
			}
    	}else if (3 == table_index){
    		for (TemplateInfo info: table3) {
				info.setSheet_index(sheetIndex);
			}
    	}else if (4 == table_index){
    		for (TemplateInfo info: table4) {
				info.setSheet_index(sheetIndex);
			}
    	}
    	table_index = 1;
    }
    
    /**
     * 放sheet模板信息到table
     * @author lucheng
     * @date 2021年3月19日 下午3:44:56
     * @param tmp
     */
    public void PushBack(TemplateInfo tmp)
    {
        List<TestLogInfo> findLogs = FindLogs(tmp);
        tmp.setTestLogList(findLogs);
    	if (1 == table_index)
    		table1.add(tmp);
    	else if (2 == table_index)
    		table2.add(tmp);
    	else if (3 == table_index)
    		table3.add(tmp);
    	else if (4 == table_index)
    		table4.add(tmp);
    	else
    		return;
    }
    
    public int GetRowIndex(){
        if (1 == table_index)
            return table1_row_start++;
        else if (2 == table_index)
            return table2_row_start++;
        else if (3 == table_index)
            return table3_row_start++;
        else if (4 == table_index)
            return table4_row_start++;
        else
            return 100;
    }
    
	/**
	 * 获取Cell中的值
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		String value = new String();

		if (null == cell)
			return value;

		// 简单的查检列类型
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:// 字符串
			value = cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字
			// 读取实数
			double dd = cell.getNumericCellValue();
			long l = (long) dd;

			if (dd - l > 0) {
				// 说明是Double
				value = new Double(dd).toString().trim();
			} else {
				// 说明是Long
				value = new Long(l).toString().trim();
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = new String();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula()).trim();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:// boolean型值
			value = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue()).trim();
			break;
		default:
			break;
		}
		return value;
	}
    
	public CustomTemplateUtils(List<String> logNameList, Workbook workbook, String excel_template_name) {
		super();
		this.logNameList = logNameList;
		this.workbook = workbook;
		this.excel_template_name = excel_template_name;
	}

	public List<String> getLogNameList() {
		return logNameList;
	}

	public void setLogNameList(List<String> logNameList) {
		this.logNameList = logNameList;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public String getExcel_template_name() {
		return excel_template_name;
	}

	public void setExcel_template_name(String excel_template_name) {
		this.excel_template_name = excel_template_name;
	}

	public boolean getIs_dt() {
		return is_dt;
	}

	public void setIs_dt(boolean is_dt) {
		this.is_dt = is_dt;
	}

	public List<TestLogInfo> getLog_infos() {
		return log_infos;
	}

	public void setLog_infos(List<TestLogInfo> log_infos) {
		this.log_infos = log_infos;
	}

	public List<TemplateInfo> getTable1() {
		return table1;
	}

	public void setTable1(List<TemplateInfo> table1) {
		this.table1 = table1;
	}

	public List<TemplateInfo> getTable2() {
		return table2;
	}

	public void setTable2(List<TemplateInfo> table2) {
		this.table2 = table2;
	}

	public List<TemplateInfo> getTable3() {
		return table3;
	}

	public void setTable3(List<TemplateInfo> table3) {
		this.table3 = table3;
	}

	public List<TemplateInfo> getTable4() {
		return table4;
	}

	public void setTable4(List<TemplateInfo> table4) {
		this.table4 = table4;
	}

	public int getTable1_row_start() {
		return table1_row_start;
	}

	public void setTable1_row_start(int table1_row_start) {
		this.table1_row_start = table1_row_start;
	}

	public int getTable2_row_start() {
		return table2_row_start;
	}

	public void setTable2_row_start(int table2_row_start) {
		this.table2_row_start = table2_row_start;
	}

	public int getTable3_row_start() {
		return table3_row_start;
	}

	public void setTable3_row_start(int table3_row_start) {
		this.table3_row_start = table3_row_start;
	}

	public int getTable4_row_start() {
		return table4_row_start;
	}

	public void setTable4_row_start(int table4_row_start) {
		this.table4_row_start = table4_row_start;
	}

	public TemplateInfo getCur_template_info() {
		return cur_template_info;
	}

	public void setCur_template_info(TemplateInfo cur_template_info) {
		this.cur_template_info = cur_template_info;
	}

	public int getTable_index() {
		return table_index;
	}

	public void setTable_index(int table_index) {
		this.table_index = table_index;
	}

	public int getTable_index2() {
		return table_index2;
	}

	public void setTable_index2(int table_index2) {
		this.table_index2 = table_index2;
	} 

}
