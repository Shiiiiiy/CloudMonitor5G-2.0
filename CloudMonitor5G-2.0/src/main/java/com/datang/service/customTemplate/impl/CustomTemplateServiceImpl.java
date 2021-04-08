package com.datang.service.customTemplate.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.datang.bean.customTemplate.TemplateInfo;
import com.datang.bean.customTemplate.TestLogInfo;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.common.dao.jdbc.JdbcTemplate;
import com.datang.common.service.sql.SqlService;
import com.datang.common.util.CustomTemplateUtils;
import com.datang.constant.CustomReportConstant;
import com.datang.dao.customTemplate.CustomReportTemplateDao;
import com.datang.dao.customTemplate.MappingIeToKpiDao;
import com.datang.dao.testLogItem.TestLogItemDao;
import com.datang.dao.testManage.terminal.TerminalGroupDao;
import com.datang.domain.customTemplate.CustomReportTemplatePojo;
import com.datang.domain.customTemplate.MappingIeToKpiPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.CellInfo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.exception.ApplicationException;
import com.datang.service.customTemplate.CustomTemplateService;
import com.datang.tools.DrawGisLteCell;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.exception.ExpressionRuntimeException;

/**
 * @author lucheng
 * @date 2020年12月22日 下午2:50:30
 */
@Service
public class CustomTemplateServiceImpl implements CustomTemplateService {
	
	@Resource
	JdbcTemplate jdbc;
	
    @Autowired
    private CustomReportTemplateDao customReportTemplateDao;
    
    @Autowired
    private MappingIeToKpiDao mappingIeToKpiDao;
    
	@Autowired
	private TerminalGroupDao terminalGroupDao;
	
	@Autowired
	private TestLogItemDao testLogItemDao;
	
	@Value("${stationReportFileLink}")
	private String stationReportFileLink;
	
    
	@Override
	public AbstractPageList doPageQuery(PageList pageList){
		return customReportTemplateDao.doPageQuery(pageList);
	}
	
	@Override
	public CustomReportTemplatePojo find(Long id){
		return customReportTemplateDao.find(id);
	}
	
	@Override
	public void importReportTemplate(Long cityId, File importFile, String importFileFileName) {
		try {
			TerminalGroup terminalGroup = terminalGroupDao.find(cityId);
			if (null == terminalGroup) {
				throw new ApplicationException("分组已经不存在");
			}
			
			
			//解析excel得到所有IE指标
			Workbook workbook = null;
			try {
				workbook = new HSSFWorkbook(new FileInputStream(importFile));
			} catch (IOException e) {
				throw new ApplicationException("导入自定义报表失败");
			} catch (IllegalArgumentException e) {
				try {
					workbook = new XSSFWorkbook(new FileInputStream(importFile));
				} catch (IOException e1) {
					throw new ApplicationException("导入自定义报表失败");
				} catch (IllegalArgumentException e1) {
					throw new ApplicationException("不是EXCEL文件");
				} catch (Exception e1) {
					throw new ApplicationException("导入自定义报表失败");
				}
			} finally {
				if (null != workbook) {
					try {
						workbook.close();
					} catch (IOException e) {
						// do nothing
					}
				}
			}
			
			Set<String> commonIeSetSum = new HashSet<String>();
			Set<String> timeIeSetSum = new HashSet<String>();
			Set<String> mileageIeSetSum = new HashSet<String>();
			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheetAt = workbook.getSheetAt(i);
				if(sheetAt.getSheetName().contains("特别说明") || sheetAt.getSheetName().contains("汇总表")){
					continue;
				}
				// 总的excel中记录数
				int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
				if (0 == totalRowNum) {
					throw new ApplicationException("第"+i+"个sheet导入EXCEL中没有数据");
				}
				Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
				if (null == row) {
					throw new ApplicationException("第"+i+"个sheet导入EXCEL中没有数据");
				}
				
		        Map<Integer, String> map[] = getRowSpanColSpanMap(sheetAt); 
		        //时间栅格列索引
		        Map<Integer, String> timeMap = map[0];
		        //地理化栅格列索引
		        Map<Integer, String> mileageMap = map[1];
				
				for (int rowIndex = sheetAt.getFirstRowNum() + 1; rowIndex <= sheetAt.getLastRowNum(); rowIndex++) {
					Row rowContext = null;
					try {
						rowContext = sheetAt.getRow(rowIndex);
						// 防止出现中间空白行和防止代码getLastRowNum()的不准确
						short firstCellNum = rowContext.getFirstCellNum();
						short lastCellNum = rowContext.getLastCellNum();
						if (0 == lastCellNum - firstCellNum) {
							totalRowNum = totalRowNum - 1;
							continue;
						}
					} catch (NullPointerException e) {
						continue;
					} catch (Exception e) {
						throw new ApplicationException(
								"导入数据异常：请删除数据下面的空行，并确认数据是否有问题");
					}
					
					try {
						String cell0 = getCellValue(rowContext.getCell( rowContext.getFirstCellNum()));
						if (!StringUtils.isBlank(cell0) && cell0.equals("公式")) {
							for (int colIndex = 1; colIndex < rowContext.getPhysicalNumberOfCells(); colIndex++) {//获取每个单元格
									String cellValue = getCellValue(rowContext.getCell(colIndex));
									System.out.println(cellValue);
									if(!StringUtils.isBlank(cellValue)){
										//取出每个cell里<>的指标值
										Set<String> set = extractMessage(cellValue);
										if(timeMap.containsKey(colIndex)){
											timeIeSetSum.addAll(set);
										}else if(mileageMap.containsKey(colIndex)){
											mileageIeSetSum.addAll(set);
										}else{
											commonIeSetSum.addAll(set);
										}
									}
				            }
							break;
						}
					}catch (Exception e) {
						throw new ApplicationException(
								e.getMessage());
					}
				}
			}
			
			Map<Integer,Map<String,String>> map = new HashMap<Integer, Map<String,String>>(); 
			//根据IE名称得到所有对应的KPI名称集合，保存到数据库
			//普通栅格ie
			List<String> commonIeList = new ArrayList<>(commonIeSetSum);
			StringBuffer commonkpibuffer = new StringBuffer();
			if(commonIeList.size()>0){
				List<MappingIeToKpiPojo> commomPojoList = mappingIeToKpiDao.findByParam(commonIeList,null);
				if(commomPojoList.size() != commonIeList.size()){
					List<String> list = new ArrayList<String>();
					for (MappingIeToKpiPojo kpiPojo : commomPojoList) {
						list.add(kpiPojo.getIeName());
					}
					commonIeList.removeAll(list);
					StringBuffer iePro = new StringBuffer();
					for (String ie : commonIeList) {
						iePro.append(ie+",");
					}
					throw new ApplicationException(
							"普通栅格IE指标名称在映射表里无对应KPI,问题IE为"+iePro);
				}
				Integer rowIndex = 0;
				for (int i = 1; i < commomPojoList.size()+1; i++) {
					commonkpibuffer.append(commomPojoList.get(i-1).getKpiName());
					if(i%50==0 || i==commomPojoList.size()){
						rowIndex++;
						Map<String,String> conmmonMap = new HashMap<String, String>();
						conmmonMap.put("commonKpi", commonkpibuffer.toString());
						map.put(rowIndex, conmmonMap);
						commonkpibuffer = new StringBuffer();
					}else{
						commonkpibuffer.append(",");
					}
					
				}
			}
			//时间栅格ie
			List<String> timeIeList = new ArrayList<>(timeIeSetSum);
			StringBuffer timekpibuffer = new StringBuffer();
			if(timeIeList.size()>0){
				List<MappingIeToKpiPojo> timePojoList = mappingIeToKpiDao.findByParam(timeIeList,null);
				if(timePojoList.size() != timeIeList.size()){
					List<String> list = new ArrayList<String>();
					for (MappingIeToKpiPojo kpiPojo : timePojoList) {
						list.add(kpiPojo.getIeName());
					}
					timeIeList.removeAll(list);
					StringBuffer iePro = new StringBuffer();
					for (String ie : timeIeList) {
						iePro.append(ie+",");
					}
					throw new ApplicationException(
							"时间栅格IE指标名称在映射表里无对应KPI,问题IE为"+iePro);
				}		
				Integer rowIndex = 0;
				for (int i = 1; i < timePojoList.size()+1; i++) {
					timekpibuffer.append(timePojoList.get(i-1).getKpiName());
					if(i%50==0 || i==timePojoList.size()){
						rowIndex++;
						Map<String,String> timeMap =null;
						if(map.get(rowIndex)!=null){
							timeMap = map.get(rowIndex);
						}else{
							timeMap = new HashMap<String, String>();
						}
						timeMap.put("timeKpi", timekpibuffer.toString());
						map.put(rowIndex, timeMap);
						timekpibuffer = new StringBuffer();
					}else{
						timekpibuffer.append(",");
					}
					
				}
			}
			//地理化栅格ie
			List<String> mileIeList = new ArrayList<>(mileageIeSetSum);
			StringBuffer milekpibuffer = new StringBuffer();
			if(mileIeList.size()>0){
				List<MappingIeToKpiPojo> milePojoList = mappingIeToKpiDao.findByParam(mileIeList,null);
				if(milePojoList.size() != mileIeList.size()){
					List<String> list = new ArrayList<String>();
					for (MappingIeToKpiPojo kpiPojo : milePojoList) {
						list.add(kpiPojo.getIeName());
					}
					mileIeList.removeAll(list);
					StringBuffer iePro = new StringBuffer();
					for (String ie : mileIeList) {
						iePro.append(ie+",");
					}
					throw new ApplicationException(
							"地理化栅格IE指标名称在映射表里无对应KPI,问题IE为"+iePro);
				}	
				Integer rowIndex = 0;
				for (int i = 1; i < milePojoList.size()+1; i++) {
					milekpibuffer.append(milePojoList.get(i-1).getKpiName());
					if(i%50==0 || i==milePojoList.size()){
						rowIndex++;
						Map<String,String> mileMap =null;
						if(map.get(rowIndex)!=null){
							mileMap = map.get(rowIndex);
						}else{
							mileMap = new HashMap<String, String>();
						}
						mileMap.put("mileKpi", milekpibuffer.toString());
						map.put(rowIndex, mileMap);
						milekpibuffer = new StringBuffer();
					}else{
						milekpibuffer.append(",");
					}
					
				}
			}
			
			//保存excel到本地
			InputStream is = new FileInputStream(importFile);
			String fileType = importFileFileName.substring(importFileFileName.lastIndexOf("."));
			String fileName = importFileFileName.substring(0,importFileFileName.lastIndexOf("."))
					+ "_"+ terminalGroup.getName() + "_" + new Date().getTime() + fileType;
			//保存的本地路径
			String dataUrl = stationReportFileLink + "/uploadReportTemplate/" +fileName;
			//获取文件夹路径
			//File file1 =new File(path+imgpath); 
			File file1 =new File(dataUrl);
			//如果文件夹不存在则创建    
			if(!file1.getParentFile().exists()  && !file1 .isDirectory()){       
			    file1 .getParentFile().mkdir();  
			}
			File destFile = new File(dataUrl);
			OutputStream os = new FileOutputStream(destFile);
			byte[] buffer = new byte[400];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
			
			//保存模板到数据库
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			for (Entry<Integer, Map<String, String>> kpimap : map.entrySet()) {
				CustomReportTemplatePojo customReportTemplatePojo = new CustomReportTemplatePojo();
				customReportTemplatePojo.setTemplateName(fileName);
				customReportTemplatePojo.setUserName(usename);
				customReportTemplatePojo.setImportDate(new Date().getTime());
				customReportTemplatePojo.setRegion(terminalGroup.getName());
				customReportTemplatePojo.setGroup(terminalGroup);
				customReportTemplatePojo.setConmmonKpiNameSum(kpimap.getValue().get("commonKpi"));
				customReportTemplatePojo.setTimeKpiNameSum(kpimap.getValue().get("timeKpi"));
				customReportTemplatePojo.setMileageKpiNameSum(kpimap.getValue().get("mileKpi"));
				customReportTemplatePojo.setSaveFilePath(dataUrl);
				//保存导入记录s
				customReportTemplateDao.create(customReportTemplatePojo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(
					e.getMessage());
		}
		
	}
	
	/**
	 * 计算出excel公式结果
	 */
	@Override
	public String modifyExcelValue(PageList page) {
		OutputStream out = null;
		Workbook workbook = null;
		try {
			String taskName = (String) page.getParam("taskName");
			String logids = (String) page.getParam("logids");
			String templateName = (String) page.getParam("templateName");
			String excelPath = (String) page.getParam("excelPath");
			String exportFilePath = (String) page.getParam("exportFilePath");

			/**
			 * poi操作excel
			 */
			try {
				workbook = new HSSFWorkbook(new FileInputStream(new File(excelPath)));
			} catch (IOException e) {
				throw new ApplicationException("解析自定义报表失败");
			} catch (IllegalArgumentException e) {
				try {
					workbook = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
				} catch (IOException e1) {
					e1.printStackTrace();
					throw new ApplicationException("解析自定义报表失败");
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
					throw new ApplicationException("不是EXCEL文件");
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new ApplicationException("解析自定义报表失败");
				}
			}
			
			//获取KPI映射字段和结果表名
			PageList selectConditions = new PageList();
			selectConditions.putParam("templateName", templateName);
			List<CustomReportTemplatePojo> queryTemplateByParam = customReportTemplateDao.queryTemplateByParam(selectConditions);
			List<String> commonKpi = new ArrayList<String>();
			List<String> timeKpi = new ArrayList<String>();
			List<String> mileKpi = new ArrayList<String>();
			for (CustomReportTemplatePojo customReportTemplatePojo : queryTemplateByParam) {
				if(!StringUtils.isBlank(customReportTemplatePojo.getConmmonKpiNameSum())){
					commonKpi.addAll(Arrays.asList(customReportTemplatePojo.getConmmonKpiNameSum().split(",")));
				}
				if(!StringUtils.isBlank(customReportTemplatePojo.getTimeKpiNameSum())){
					timeKpi.addAll(Arrays.asList(customReportTemplatePojo.getTimeKpiNameSum().split(",")));
				}
				if(!StringUtils.isBlank(customReportTemplatePojo.getMileageKpiNameSum())){
					mileKpi.addAll(Arrays.asList(customReportTemplatePojo.getMileageKpiNameSum().split(",")));
				}
			}
			
			// 根据IE名称得到所有对应的KPI名称集合
			Map<String, String> ieToKpiMapping = new HashMap<String, String>();
			Map<String, String> kpiToieMapping = new HashMap<String, String>();
			Map<String, List<String>> commonTableMap = new HashMap<String, List<String>>();
			Map<String, List<String>> timeTableMap = new HashMap<String, List<String>>();
			Map<String, List<String>> mileTableMap = new HashMap<String, List<String>>();
			//普通类型栅格IE
			List<MappingIeToKpiPojo> commonPojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(commonKpi));
			if (commonPojoList.size() == commonKpi.size()) {
				for (MappingIeToKpiPojo mappingIeToKpiPojo : commonPojoList) {
					ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
					kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
					List<String> list = null;
					//普通类型ie结果表名称不需要变
					if (commonTableMap.get(mappingIeToKpiPojo.getTableName()) == null) {
						list = new ArrayList<String>();
						commonTableMap.put(mappingIeToKpiPojo.getTableName(), list);
					} else {
						list = commonTableMap.get(mappingIeToKpiPojo.getTableName());
					}
					list.add(mappingIeToKpiPojo.getKpiName());
				}
			}
			//时间类型栅格IE
			List<MappingIeToKpiPojo> timePojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(timeKpi));
			if (timePojoList.size() == timeKpi.size()) {
				for (MappingIeToKpiPojo mappingIeToKpiPojo : timePojoList) {
					ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
					kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
					List<String> list = null;
					//时间类型ie结果表名称需要变换
					String tablename = mappingIeToKpiPojo.getTableName();
					String timeTablename = tablename.substring(0,tablename.lastIndexOf("_"))+"_TIME"
												+tablename.substring(tablename.lastIndexOf("_"),tablename.length());
					if (timeTableMap.get(timeTablename) == null) {
						list = new ArrayList<String>();
						timeTableMap.put(timeTablename, list);
					} else {
						list = timeTableMap.get(timeTablename);
					}
					list.add(mappingIeToKpiPojo.getKpiName());
				}
			}
			//地理类型栅格IE
			List<MappingIeToKpiPojo> milePojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(mileKpi));
			if (milePojoList.size() == mileKpi.size()) {
				for (MappingIeToKpiPojo mappingIeToKpiPojo : milePojoList) {
					ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
					kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
					List<String> list = null;
					//时间类型ie结果表名称需要变换
					String tablename = mappingIeToKpiPojo.getTableName();
					String timeTablename = tablename.substring(0,tablename.lastIndexOf("_"))+"_MILEAGE"
													+tablename.substring(tablename.lastIndexOf("_"),tablename.length());
					if (mileTableMap.get(timeTablename) == null) {
						list = new ArrayList<String>();
						mileTableMap.put(timeTablename, list);
					} else {
						list = mileTableMap.get(timeTablename);
					}
					list.add(mappingIeToKpiPojo.getKpiName());
				}
			}
			
			// 获取所有日志名
			List<TestLogItem> testLogItems = new ArrayList<>();
			String[] logIds = logids.trim().split(",");
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				if (logIds[i] != null) {
					try {
						ids.add(Long.parseLong(logIds[i].trim()));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
			testLogItems = testLogItemDao.getTestLogItems(ids);
			List<String> logNameList = new ArrayList<String>();
			for (TestLogItem log : testLogItems) {
		    	if(templateName.contains("DT")){
		    		if(log.getFileName().contains("DT")){
		    			logNameList.add(log.getFileName());
		    		}
		    	}else if(templateName.contains("CQT")){
		    		if(log.getFileName().contains("CQT")){
		    			logNameList.add(log.getFileName());
		    		}
		    	}
			}
			
			CustomTemplateUtils templateUtil = new CustomTemplateUtils(logNameList,workbook,templateName);
			templateUtil.init();
			
			int numberOfSheets = workbook.getNumberOfSheets();
			//在表一到表4中插入结果
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheetAt = workbook.getSheetAt(i);
				String sheetName = sheetAt.getSheetName();
				
				List<TemplateInfo> tableTag;
			    int start_col = 9;
				if(sheetAt.getSheetName().contains("特别说明") || sheetAt.getSheetName().contains("汇总表")){
					continue;
				}
				
				// 总的excel中记录数
				int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
				if (0 == totalRowNum) {
					throw new ApplicationException("第" + i + "个sheet报表EXCEL中没有数据");
				}
				Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
				if (null == row) {
					throw new ApplicationException("第" + i + "个sheet报表EXCEL中没有数据");
				}
				
				if (sheetName.contains("表一")){
		            tableTag = templateUtil.getTable1();
		            //保存表一的sheet索引到TemplateInfo中
		            templateUtil.setTable_index(1);
		        }else if (sheetName.contains("表二")){
		            tableTag = templateUtil.getTable2();
		            templateUtil.setTable_index(2);
		        }else if (sheetName.contains("表三")){
		            tableTag = templateUtil.getTable3();
		            templateUtil.setTable_index(3);
		        }else if (sheetName.contains("表四")){
		            tableTag = templateUtil.getTable4();
		            templateUtil.setTable_index(4);
		        }else{
		        	continue;
		        }
	            templateUtil.saveSheetIndex(i);
				
				
		        Map<Integer, String> mapTypeIndex[] = getRowSpanColSpanMap(sheetAt); 
		        //时间栅格列索引
		        Map<Integer, String> timeMap = mapTypeIndex[0];
		        //地理化栅格列索引
		        Map<Integer, String> mileageMap = mapTypeIndex[1];
				
//				// 设置单位格的风格
//				CellStyle style = workbook.createCellStyle();
//				// 创建字体
//				Font font = workbook.createFont();
//				// 设置字体的颜色
//				font.setColor((short)0x07234B);
//				font.setFontHeightInPoints((short) 8);
//		        font.setFontName("微软雅黑");
//				style.setFont(font);
//				style.setAlignment(CellStyle.ALIGN_CENTER); 
//				style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 居中  
//				// 设置单元格上下左右边框
//				style.setBorderBottom((short)1);
//				style.setBorderTop((short)1);
//				style.setBorderLeft((short)1);
//				style.setBorderRight((short)1);

				Map<Integer, Integer> previsionMap = new HashMap<Integer, Integer>();
				// 公式的行数和row
				Integer formulaIndex = null;
				Row formulaRow = null;
				for (int j = sheetAt.getFirstRowNum(); j <= sheetAt.getLastRowNum(); j++) {
					Row rowContext = sheetAt.getRow(j);
					try {
						for (int s = 0; s <= rowContext.getLastCellNum(); s++) {
							//设置每个cell的格式
							Cell rowcell = rowContext.getCell(s); 
							if(rowcell==null){
								//不允许首行为空
								if(s==0){
									Cell creatcell = rowContext.createCell(s);
//									creatcell.setCellStyle(style);
								}
								continue;
							}
//							rowcell.setCellStyle(style);
							//取出精度和公式里的指标
							if(s==0){
								String cell0 = getCellValue(rowcell);
								if (!StringUtils.isBlank(cell0) && cell0.equals("精度")) {
									for (int k = 1; k < rowContext.getPhysicalNumberOfCells(); k++) {// 获取每个单元格
										String cellValue = getCellValue(rowContext.getCell(k));
										if (!StringUtils.isBlank(cellValue)) {
											// 取出每个cell里的设置精度
											Integer prevision = getPrevision(cellValue);
											// key指向位置index，value保存有效数字
											previsionMap.put(k, prevision);
										}
									}
								}
								if (!StringUtils.isBlank(cell0) && cell0.equals("公式")) {
									formulaIndex = j;
									formulaRow = rowContext;
								}
							}
						}
					} catch (Exception e) {
						throw new ApplicationException(e.getMessage());
					}
				}
				
				/**
				 * 插入行
				 */
				if(!tableTag.isEmpty()){
					// 第1个参数是指要开始插入的行，第2个参数是结尾行数,第三个参数表示动态添加的行数
					Integer beginIndex = formulaIndex + 1;
					sheetAt.shiftRows(beginIndex, sheetAt.getLastRowNum(), tableTag.size()+8, true, false);
					// 插入
					for (int j = 0; j < tableTag.size(); j++) {
						TemplateInfo templateInfo = tableTag.get(j);
						//获取省、市、测试用例等参数这一行对应的所有日志
						List<TestLogInfo> logList = templateInfo.getTestLogList();
						// 根据表名、指标集合、任务名、日志名获取对应的指标值
						Map<String, BigDecimal> commonKpiValue = new HashMap<String, BigDecimal>();
						Map<String, BigDecimal> timeKpiValue = new HashMap<String, BigDecimal>();
						Map<String, BigDecimal> mileKpiValue = new HashMap<String, BigDecimal>();
						for (TestLogInfo testLogInfo : logList) {
							String fileCondition = testLogInfo.getFull_log_name();
							Map<String, Object> logKpiValue = new HashMap<String, Object>();
							for (Map.Entry<String, List<String>> map : commonTableMap.entrySet()) {
								Map<String, Object> rltTemp = new HashMap<String, Object>();
								String sql = getSqlByParam(map.getKey(), map.getValue(), taskName, fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("任务解析失败，请联系管理员");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(commonKpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										commonKpiValue.put(log.getKey(), (BigDecimal)log.getValue());
									}
								}else{
									BigDecimal oldValue = commonKpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = (BigDecimal)log.getValue();
										if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("max")){
											if(newValue.compareTo(oldValue)==1){
												commonKpiValue.put(log.getKey(), newValue);
											}
										}else if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("min")){
											if(newValue.compareTo(oldValue)==-1){
												commonKpiValue.put(log.getKey(), newValue);
											}
										}else{
											commonKpiValue.put(log.getKey(), oldValue.add(newValue));
										}
									}
								}
							}
							logKpiValue.clear();
							for (Map.Entry<String, List<String>> map : timeTableMap.entrySet()) {
								Map<String, Object> rltTemp = new HashMap<String, Object>();
								String sql = getSqlByParam(map.getKey(), map.getValue(), taskName, fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("任务解析失败，请联系管理员");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(timeKpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										timeKpiValue.put(log.getKey(), (BigDecimal)log.getValue());
									}
								}else{
									BigDecimal oldValue = timeKpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = (BigDecimal)log.getValue();
										if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("max")){
											if(newValue.compareTo(oldValue)==1){
												timeKpiValue.put(log.getKey(), newValue);
											}
										}else if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("min")){
											if(newValue.compareTo(oldValue)==-1){
												timeKpiValue.put(log.getKey(), newValue);
											}
										}else{
											timeKpiValue.put(log.getKey(), oldValue.add(newValue));
										}
									}
								}
							}
							logKpiValue.clear();
							for (Map.Entry<String, List<String>> map : mileTableMap.entrySet()) {
								Map<String, Object> rltTemp = new HashMap<String, Object>();
								String sql = getSqlByParam(map.getKey(), map.getValue(), taskName, fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("任务解析失败，请联系管理员");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(mileKpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										mileKpiValue.put(log.getKey(), (BigDecimal)log.getValue());
									}
								}else{
									BigDecimal oldValue = mileKpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = (BigDecimal)log.getValue();
										if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("max")){
											if(newValue.compareTo(oldValue)==1){
												mileKpiValue.put(log.getKey(), newValue);
											}
										}else if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("min")){
											if(newValue.compareTo(oldValue)==-1){
												mileKpiValue.put(log.getKey(), newValue);
											}
										}else{
											mileKpiValue.put(log.getKey(), oldValue.add(newValue));
										}
									}
								}
							}
						}

						// 增加一行
						
						Row creRow = sheetAt.createRow(templateInfo.getRow_index());

						if(templateUtil.getIs_dt()){
							start_col = 9;
							for(int cellIndex=0;cellIndex<start_col;cellIndex++){
								Cell createCell = creRow.createCell(cellIndex);
//								createCell.setCellStyle(style);
								if(cellIndex == 0){
									createCell.setCellValue(templateInfo.getProvince());
								}else if(cellIndex == 1){
									createCell.setCellValue(templateInfo.getCity());
								}else if(cellIndex == 2){
									createCell.setCellValue(templateInfo.getTest_case_s());
								}else if(cellIndex == 3){
									createCell.setCellValue(templateInfo.getTest_card());
								}else if(cellIndex == 4){
									createCell.setCellValue(templateInfo.getTest_area());
								}else if(cellIndex == 5){
									createCell.setCellValue(templateInfo.getContractor());
								}else if(cellIndex == 6){
									createCell.setCellValue(templateInfo.getEquipement_producer());
								}else{
									createCell.setCellValue("");
								}
							}
						}else{
							start_col = 12;
							for(int cellIndex=0;cellIndex<start_col;cellIndex++){
								Cell createCell = creRow.createCell(cellIndex);
//								createCell.setCellStyle(style);
								if(cellIndex == 0){
									createCell.setCellValue(templateInfo.getProvince());
								}else if(cellIndex == 1){
									createCell.setCellValue(templateInfo.getCity());
								}else if(cellIndex == 2){
									createCell.setCellValue(templateInfo.getTest_case_s());
								}else if(cellIndex == 3){
									createCell.setCellValue(templateInfo.getScene_type1());
								}else if(cellIndex == 4){
									createCell.setCellValue(templateInfo.getScene_type2());
								}else if(cellIndex == 5){
									createCell.setCellValue(templateInfo.getScene_name());
								}else if(cellIndex == 6){
									createCell.setCellValue(templateInfo.getTest_card());
								}else if(cellIndex == 7){
									createCell.setCellValue(templateInfo.getContractor());
								}else if(cellIndex == 8){
									createCell.setCellValue(templateInfo.getEquipement_producer());
								}else if(cellIndex == 10){
									createCell.setCellValue("合计");
								}else{
									createCell.setCellValue("");
								}
							}
						}
						//从第二个单元格开始
						if (formulaRow != null) {
							for (int k = start_col; k < formulaRow.getPhysicalNumberOfCells(); k++) {// 获取每个单元格
								Cell createCell = creRow.createCell(k);
//								createCell.setCellStyle(style);
								String cellValue = getCellValue(formulaRow.getCell(k));
								if (!StringUtils.isBlank(cellValue)) {
									//获取参数
									Set<String> set = extractMessage(cellValue);
									//替换公式里的{}<>
									String expression = cellValue.replaceAll("\\{", "").replaceAll("\\}", "")
											.replaceAll("<", "").replaceAll(">", "");
									Map<String, Object> kpiValueMap = new HashMap<String, Object>();
									for (String kpiname : set) {
										if(timeMap.containsKey(k)){
											kpiValueMap.put(kpiname, timeKpiValue.get(ieToKpiMapping.get(kpiname)));
										}else if(mileageMap.containsKey(k)){
											kpiValueMap.put(kpiname, mileKpiValue.get(ieToKpiMapping.get(kpiname)));
										}else{
											kpiValueMap.put(kpiname, commonKpiValue.get(ieToKpiMapping.get(kpiname)));
										}
									}
									//获取公式结果
									Double excpressionVlaue = getExcpressionVlaue(kpiValueMap, expression);
									if (excpressionVlaue != null) {
										if(previsionMap.get(k)!=null){
											//保留有效数字
											NumberFormat nf = NumberFormat.getNumberInstance();
											nf.setMaximumFractionDigits(previsionMap.get(k));
											String value = nf.format(excpressionVlaue);
											createCell.setCellValue(value);
										}
									}
								}
							}
						} else {
							throw new ApplicationException("IE指标公式异常");
						}
						
						if(templateUtil.getIs_dt()){
							if (sheetName.contains("表一")){
								templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_1, tableTag, 
										 CustomReportConstant.DT_ROW1, CustomReportConstant.DT_COL1,
										 CustomReportConstant.DT_ROW1_SA, CustomReportConstant.DT_COL1_SA);
					        }else if (sheetName.contains("表二")){
					        	templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_2, tableTag, 
										 CustomReportConstant.DT_ROW2, CustomReportConstant.DT_COL2,
										 CustomReportConstant.DT_ROW2_SA, CustomReportConstant.DT_COL2_SA);
					        }else if (sheetName.contains("表三")){
					        	templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_3, tableTag, 
										 CustomReportConstant.DT_ROW3, CustomReportConstant.DT_COL3,
										 CustomReportConstant.DT_ROW3_SA, CustomReportConstant.DT_COL3_SA);
					        }else if (sheetName.contains("表四")){
					        	templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_4, tableTag, 
										 CustomReportConstant.DT_ROW4, CustomReportConstant.DT_COL4,
										 CustomReportConstant.DT_ROW4_SA, CustomReportConstant.DT_COL4_SA);
					        }
						}else{
							if (sheetName.contains("表一")){
								templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_1, tableTag, 
										 CustomReportConstant.CQT_ROW1, CustomReportConstant.CQT_COL1,
										 CustomReportConstant.CQT_ROW1_SA, CustomReportConstant.CQT_COL1_SA);
					        }else if (sheetName.contains("表二")){
					        	templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_2, tableTag, 
										 CustomReportConstant.CQT_ROW2, CustomReportConstant.CQT_COL2,
										 CustomReportConstant.CQT_ROW2_SA, CustomReportConstant.CQT_COL2_SA);
					        }else if (sheetName.contains("表三")){
					        	templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_3, tableTag, 
										 CustomReportConstant.CQT_ROW3, CustomReportConstant.CQT_COL3,
										 CustomReportConstant.CQT_ROW3_SA, CustomReportConstant.CQT_COL3_SA);
					        }else if (sheetName.contains("表四")){
					        	templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_4, tableTag, 
										 CustomReportConstant.CQT_ROW4, CustomReportConstant.CQT_COL4,
										 CustomReportConstant.CQT_ROW4_SA, CustomReportConstant.CQT_COL4_SA);
					        }
						}
					}
					// 将行号为i+1一直到行号为lastRowNum的单元格全部上移一行，以便删除i行
					sheetAt.shiftRows(formulaIndex, sheetAt.getLastRowNum(), -1);// 删除精度行
					sheetAt.shiftRows(formulaIndex, sheetAt.getLastRowNum(), -1);// 删除公式行
					// 删除sheet后面的空行
					//  -2：减去前面删除的两行，  +tableTag.size()+6：增加的行数，  +1：表示需要行号为i+1上移一行
					int index = formulaIndex-2+tableTag.size() + 6 + 1;
					int lastRowNum = sheetAt.getLastRowNum();
					for (int s = index; s <=lastRowNum; s++) {
						if (index >= 0 && index < sheetAt.getLastRowNum()) {
							sheetAt.shiftRows(index+1, sheetAt.getLastRowNum(), -1);
						}
						if (index == sheetAt.getLastRowNum()) {
							sheetAt.removeRow(sheetAt.getRow(index));
						}
					}
	            }else{
	            	// 删除sheet后面的空行
					int index = formulaIndex + 1;
					int lastRowNum = sheetAt.getLastRowNum();
					for (int s = index; s <=lastRowNum; s++) {
						if (index >= 0 && index < sheetAt.getLastRowNum()) {
							sheetAt.shiftRows(index+1, sheetAt.getLastRowNum(), -1);
						}
						if (index == sheetAt.getLastRowNum()) {
							sheetAt.removeRow(sheetAt.getRow(index));
						}
					}
	            }
				
			}
			
			// 输出为一个新的Excel，也就是动态修改完之后的excel
			try {
				out = new FileOutputStream(exportFilePath);
				workbook.write(out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return exportFilePath;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
	}

	@Override
	public void deleteCustomTemplate(List<Long> idsList){
		for (Long id : idsList) {
			customReportTemplateDao.delete(id);
		}
	}
	
	@Override
	public List<CustomReportTemplatePojo> queryTemplateByParam(PageList pageList){
		return customReportTemplateDao.queryTemplateByParam(pageList);
	}
	
	
	/**
	 * 通过Aviator实现表达式求值
	 * @author lucheng
	 * @date 2020年12月30日 下午7:04:55
	 * @param map 传入的参数，key：表达式中的参数名，value：对应设置的参数值
	 * @param expression 传入的表达式，如"a-(b-c)+100"
	 * @return
	 */
	public Double getExcpressionVlaue(Map<String,Object> map,String expression){
		try {
			// 编译表达式
			Expression compiledExp = AviatorEvaluator.compile(expression);
			// 执行表达式
			BigDecimal result = (BigDecimal) compiledExp.execute(map);
			if(result!=null){
				return result.doubleValue();
			}else{
				return null;
			}
		} catch (ArithmeticException e) {
			//考虑到除法会有除0,默认回传null
			return null;
		} catch (ExpressionRuntimeException e) {
			//考虑到会有空值,默认回传null
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
	}
	
	/**
	 * 获取指定合并单元格的列数
	 * @author lucheng
	 * @date 2021年3月17日 上午11:18:35
	 * @param sheet
	 * @return
	 */
	private Map<Integer, String>[] getRowSpanColSpanMap(Sheet sheet) throws Exception{
		//时间栅格列key
        Map<Integer, String> mapTime = new HashMap<Integer, String>();
        //地理栅格列key
        Map<Integer, String> mapMileage = new HashMap<Integer, String>();
        
        int mergedNum = sheet.getNumMergedRegions();
        CellRangeAddress range = null;
        for (int i = 0; i < mergedNum; i++) {
            range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int firstColumn = range.getFirstColumn();
            int lastRow  = range.getLastRow();
            int lastColumn  = range.getLastColumn();

            Row fRow = sheet.getRow(firstRow);
            String cell0 = getCellValue(fRow.getCell(firstColumn));
            if (!StringUtils.isBlank(cell0) && cell0.contains("基于1s时间栅格统计")) {
            	int tempCol = firstColumn;
            	while (tempCol <= lastColumn) {
            		mapTime.put(tempCol, "");
            		tempCol++;
                }
			}else if (!StringUtils.isBlank(cell0) && cell0.contains("基于10m×10m地理化栅格统计")) {
				int tempCol = firstColumn;
            	while (tempCol <= lastColumn) {
            		mapMileage.put(tempCol, "");
            		tempCol++;
                }
			}
        }
        Map[] map = { mapTime, mapMileage };
        return map;
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
	
	/**
	 * 提取<>中内容，忽略<>中的<>
	 * @param msg
	 * @return
	 */
	public Set<String> extractMessage(String msg) {
		Set<String> set = new HashSet<String>();
		int start = 0;
		int startFlag = 0;
		int endFlag = 0;
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '<') {
				startFlag++;
				if (startFlag == endFlag + 1) {
					start = i;
				}
			} else if (msg.charAt(i) == '>') {
				endFlag++;
				if (endFlag == startFlag) {
					set.add(msg.substring(start + 1, i));
				}
			}
		}
		return set;
	}
	
	/**
	 * 提取精度
	 * @param msg
	 * @return
	 */
	public Integer getPrevision(String msg) {
		if(msg.equals("取整")){
			return 0;
		}else{
			String value = msg.substring(msg.lastIndexOf(".")+1,msg.length()-1);
			try {
				Integer parseLong = Integer.parseInt(value);
				return parseLong;
			} catch (NumberFormatException e) {
				return 0;
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage());
			}
		}
	}
	
	/**
	 * 构造sql
	 * @author lucheng
	 * @date 2020年12月30日 下午6:07:48
	 * @param key
	 * @param value
	 * @param taskName
	 * @param fileName
	 * @return
	 */
	private String getSqlByParam(String tablename, List<String> kpiList, String taskName, String fileName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select ");
		for (int i = 0; i < kpiList.size(); i++) {
			if(i==0){
				buffer.append(kpiList.get(i));
			}else{
				buffer.append("," + kpiList.get(i));
			}
		}
		
		buffer.append(" from "+tablename+" where TASK_NAME='"+taskName+"'");
		buffer.append(" and LOG_NAME='"+fileName+"'");
		
		return buffer.toString();
	}
	
}
