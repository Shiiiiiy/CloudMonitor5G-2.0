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
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.datang.domain.testLogItem.UnicomLogItem;
import com.datang.service.influx.InfluxService;
import com.datang.service.testLogItem.UnicomLogItemService;
import com.datang.web.action.customTemplate.CustomReportLogAction;
import com.datang.web.beans.testLogItem.UnicomLogItemPageQueryRequestBean;
import org.apache.commons.lang.math.NumberUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date 2020???12???22??? ??????2:50:30
 */
@Service
public class CustomTemplateServiceImpl implements CustomTemplateService {

	private static Logger LOGGER = LoggerFactory.getLogger(CustomReportLogAction.class);

	// -->
	private final String influxDbKpiStr = "nr_xiaoqu_count,nr_xiaoqu_count_liantong,nr_xiaoqu_count_dianxin,sa_nr_xiaoqu_count,sa_nr_xiaoqu_count_liantong,sa_nr_xiaoqu_count_dianxin,nsa_nr_xiaoqu_count,nsa_nr_xiaoqu_count_liantong,nsa_nr_xiaoqu_count_dianxin";
	// --<
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

	@Autowired
	private InfluxService influxService;

	@Autowired
	private UnicomLogItemService unicomLogItemService;

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
				throw new ApplicationException("?????????????????????");
			}


			//??????excel????????????IE??????
			Workbook workbook = null;
			try {
				workbook = new HSSFWorkbook(new FileInputStream(importFile));
			} catch (IOException e) {
				throw new ApplicationException("???????????????????????????");
			} catch (IllegalArgumentException e) {
				try {
					workbook = new XSSFWorkbook(new FileInputStream(importFile));
				} catch (IOException e1) {
					throw new ApplicationException("???????????????????????????");
				} catch (IllegalArgumentException e1) {
					throw new ApplicationException("??????EXCEL??????");
				} catch (Exception e1) {
					throw new ApplicationException("???????????????????????????");
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
			Set<String> mileage50IeSetSum = new HashSet<String>();

			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheetAt = workbook.getSheetAt(i);
				if(sheetAt.getSheetName().contains("????????????") || sheetAt.getSheetName().contains("?????????")){
					continue;
				}
				// ??????excel????????????
				int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
				if (0 == totalRowNum) {
					throw new ApplicationException("???"+i+"???sheet??????EXCEL???????????????");
				}
				Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
				if (null == row) {
					throw new ApplicationException("???"+i+"???sheet??????EXCEL???????????????");
				}

				Map<Integer, String> map[] = getRowSpanColSpanMap(sheetAt);
				//?????????????????????
				Map<Integer, String> timeMap = map[0];
				//????????????????????????
				Map<Integer, String> mileageMap = map[1];
				//????????????????????????  50 * 50
				Map<Integer,String> mileage50Map = map[2];


				for (int rowIndex = sheetAt.getFirstRowNum() + 1; rowIndex <= sheetAt.getLastRowNum(); rowIndex++) {
					Row rowContext = null;
					try {
						rowContext = sheetAt.getRow(rowIndex);
						// ??????????????????????????????????????????getLastRowNum()????????????
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
								"????????????????????????????????????????????????????????????????????????????????????");
					}

					try {
						String cell0 = getCellValue(rowContext.getCell( rowContext.getFirstCellNum()));
						if (!StringUtils.isBlank(cell0) && cell0.equals("??????")) {
							for (int colIndex = 1; colIndex < rowContext.getPhysicalNumberOfCells(); colIndex++) {//?????????????????????
								String cellValue = getCellValue(rowContext.getCell(colIndex));
								System.out.println(cellValue);
								if(!StringUtils.isBlank(cellValue)){
									//????????????cell???<>????????????
									Set<String> set = extractMessage(cellValue);
									if(timeMap.containsKey(colIndex)){
										timeIeSetSum.addAll(set);
									}else if(mileageMap.containsKey(colIndex)){
										mileageIeSetSum.addAll(set);
									}else if(mileage50Map.containsKey(colIndex)){
										mileage50IeSetSum.addAll(set);
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

			List<Map<String,String>> kpiList = new ArrayList<>();
			//		Map<Integer,Map<String,String>> map = new HashMap<Integer, Map<String,String>>();
			//??????IE???????????????????????????KPI?????????????????????????????????
			//????????????ie
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
							"????????????IE????????????????????????????????????KPI,??????IE???"+iePro);
				}
				Integer rowIndex = 0;
				for (int i = 1; i < commomPojoList.size()+1; i++) {
					commonkpibuffer.append(commomPojoList.get(i-1).getKpiName());
					if(i%50==0 || i==commomPojoList.size()){
						rowIndex++;
						Map<String,String> conmmonMap = new HashMap<String, String>();
						conmmonMap.put("commonKpi", commonkpibuffer.toString());
						//map.put(rowIndex, conmmonMap);
						kpiList.add(conmmonMap);
						commonkpibuffer = new StringBuffer();
					}else{
						commonkpibuffer.append(",");
					}

				}
			}
			//????????????ie
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
							"????????????IE????????????????????????????????????KPI,??????IE???"+iePro);
				}
				Integer rowIndex = 0;
				for (int i = 1; i < timePojoList.size()+1; i++) {
					timekpibuffer.append(timePojoList.get(i-1).getKpiName());
					if(i%50==0 || i==timePojoList.size()){
						rowIndex++;
						Map<String,String> timeMap =null;
						if(kpiList.size()>=rowIndex){
							timeMap = kpiList.get(rowIndex-1);
							timeMap.put("timeKpi", timekpibuffer.toString());
						}else{
							timeMap = new HashMap<String, String>();
							timeMap.put("timeKpi", timekpibuffer.toString());
							kpiList.add(timeMap);
						}
						timekpibuffer = new StringBuffer();
					}else{
						timekpibuffer.append(",");
					}

				}
			}
			//???????????????ie
			Integer mileRowIndex = 0;
			List<String> mileIeList = new ArrayList<>(mileageIeSetSum);
			StringBuffer milekpibuffer = new StringBuffer();
			if(mileIeList.size()>0){
				String mileageKpiType = "10";
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
							"???????????????IE????????????????????????????????????KPI,??????IE???"+iePro);
				}
				for (int i = 1; i < milePojoList.size()+1; i++) {
					milekpibuffer.append(milePojoList.get(i-1).getKpiName());
					if(i%50==0 || i==milePojoList.size()){
						mileRowIndex++;
						Map<String,String> mileMap =null;
						if(kpiList.size()>=mileRowIndex){
							mileMap = kpiList.get(mileRowIndex-1);
							mileMap.put("mileKpi", milekpibuffer.toString());
							mileMap.put("mileKpiType",mileageKpiType);
						}else{
							mileMap = new HashMap<String, String>();
							mileMap.put("mileKpi", milekpibuffer.toString());
							mileMap.put("mileKpiType",mileageKpiType);
							kpiList.add(mileMap);
						}
						milekpibuffer = new StringBuffer();
					}else{
						milekpibuffer.append(",");
					}
				}
			}


			List<String> mile50IeList = new ArrayList<>(mileage50IeSetSum);
			StringBuffer mile50kpibuffer = new StringBuffer();
			if(mile50IeList.size()>0){
				String mileageKpiType = "50";
				List<MappingIeToKpiPojo> mile50PojoList = mappingIeToKpiDao.findByParam(mile50IeList,null);
				if(mile50PojoList.size() != mile50IeList.size()){
					List<String> list = new ArrayList<String>();
					for (MappingIeToKpiPojo kpiPojo : mile50PojoList) {
						list.add(kpiPojo.getIeName());
					}
					mile50IeList.removeAll(list);
					StringBuffer iePro = new StringBuffer();
					for (String ie : mile50IeList) {
						iePro.append(ie+",");
					}
					throw new ApplicationException(
							"???????????????IE????????????????????????????????????KPI,??????IE???"+iePro);
				}
				for (int i = 1; i < mile50PojoList.size()+1; i++) {
					mile50kpibuffer.append(mile50PojoList.get(i-1).getKpiName());
					if(i%50==0 || i==mile50PojoList.size()){
						mileRowIndex++;
						Map<String,String> mileMap =null;
						if(kpiList.size()>=mileRowIndex){
							mileMap = kpiList.get(mileRowIndex-1);
							mileMap.put("mileKpi", mile50kpibuffer.toString());
							mileMap.put("mileKpiType",mileageKpiType);
						}else{
							mileMap = new HashMap<String, String>();
							mileMap.put("mileKpi", mile50kpibuffer.toString());
							mileMap.put("mileKpiType",mileageKpiType);
							kpiList.add(mileMap);
						}
						mile50kpibuffer = new StringBuffer();
					}else{
						mile50kpibuffer.append(",");
					}
				}
			}

			//??????excel?????????
			InputStream is = new FileInputStream(importFile);
			String fileType = importFileFileName.substring(importFileFileName.lastIndexOf("."));
			String fileName = importFileFileName.substring(0,importFileFileName.lastIndexOf("."))
					+ "_"+ terminalGroup.getName() + "_" + new Date().getTime() + fileType;
			//?????????????????????
			String dataUrl = stationReportFileLink + "/uploadReportTemplate/" +fileName;
			//?????????????????????
			//File file1 =new File(path+imgpath);
			File file1 =new File(dataUrl);
			//?????????????????????????????????
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

			//????????????????????????
			String usename = (String) SecurityUtils.getSubject().getPrincipal();
			for (Map<String, String> map : kpiList) {
				CustomReportTemplatePojo customReportTemplatePojo = new CustomReportTemplatePojo();
				customReportTemplatePojo.setTemplateName(fileName);
				customReportTemplatePojo.setUserName("admin");
				customReportTemplatePojo.setImportDate(System.currentTimeMillis());
//				customReportTemplatePojo.setRegion(terminalGroup.getName());
//				customReportTemplatePojo.setGroup(terminalGroup);
				customReportTemplatePojo.setConmmonKpiNameSum(map.get("commonKpi"));
				customReportTemplatePojo.setTimeKpiNameSum(map.get("timeKpi"));
				customReportTemplatePojo.setMileageKpiNameSum(map.get("mileKpi"));
				customReportTemplatePojo.setMileageKpiType(map.get("mileKpiType"));
				customReportTemplatePojo.setSaveFilePath(dataUrl);
				//??????????????????s
				customReportTemplateDao.create(customReportTemplatePojo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(
					e.getMessage());
		}

	}

	/**
	 * ?????????excel????????????
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
			 * poi??????excel
			 */
			try {
				workbook = new HSSFWorkbook(new FileInputStream(new File(excelPath)));
			} catch (IOException e) {
				throw new ApplicationException("???????????????????????????");
			} catch (IllegalArgumentException e) {
				try {
					workbook = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
				} catch (IOException e1) {
					e1.printStackTrace();
					throw new ApplicationException("???????????????????????????");
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
					throw new ApplicationException("??????EXCEL??????");
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new ApplicationException("???????????????????????????");
				}
			}

			//??????KPI???????????????????????????
			PageList selectConditions = new PageList();
			selectConditions.putParam("templateName", templateName);
			List<CustomReportTemplatePojo> queryTemplateByParam = customReportTemplateDao.queryTemplateByParam(selectConditions);
			List<String> commonKpi = new ArrayList<String>();
			List<String> timeKpi = new ArrayList<String>();
			List<String> mileKpi = new ArrayList<String>();
			List<String> mile50Kpi = new ArrayList<>();
			// -->
			List<String> influxDbKpi = new ArrayList<String>();
			// --<
			for (CustomReportTemplatePojo customReportTemplatePojo : queryTemplateByParam) {
				if(!StringUtils.isBlank(customReportTemplatePojo.getConmmonKpiNameSum())){
					commonKpi.addAll(Arrays.asList(customReportTemplatePojo.getConmmonKpiNameSum().split(",")));
				}
				if(!StringUtils.isBlank(customReportTemplatePojo.getTimeKpiNameSum())){
					timeKpi.addAll(Arrays.asList(customReportTemplatePojo.getTimeKpiNameSum().split(",")));
				}
				if(!StringUtils.isBlank(customReportTemplatePojo.getMileageKpiNameSum()) &&  ( StringUtils.isBlank(customReportTemplatePojo.getMileageKpiType() ) || customReportTemplatePojo.getMileageKpiType().equals("10")  ) ){
					mileKpi.addAll(Arrays.asList(customReportTemplatePojo.getMileageKpiNameSum().split(",")));
				}
				//50*50???????????????
				if(!StringUtils.isBlank(customReportTemplatePojo.getMileageKpiNameSum()) &&    "50".equals(customReportTemplatePojo.getMileageKpiType())   ){
					mile50Kpi.addAll(Arrays.asList(customReportTemplatePojo.getMileageKpiNameSum().split(",")));
				}


			}
			// -->
			String[] influxDbKpiSplit = influxDbKpiStr.split(",");
			for(String s:influxDbKpiSplit){
				influxDbKpi.add(s);
			}
			// --<
			// ??????IE???????????????????????????KPI????????????
			Map<String, String> ieToKpiMapping = new HashMap<String, String>();
			Map<String, String> kpiToieMapping = new HashMap<String, String>();
			Map<String, List<String>> commonTableMap = new HashMap<String, List<String>>();
			Map<String, List<String>> timeTableMap = new HashMap<String, List<String>>();
			Map<String, List<String>> mileTableMap = new HashMap<String, List<String>>();
			Map<String, List<String>> mile50TableMap = new HashMap<String, List<String>>();
			Map<String, String> influxDbTableMap = new HashMap<String,String>();
			//??????????????????IE
			List<MappingIeToKpiPojo> commonPojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(commonKpi));
			if (commonPojoList.size() == commonKpi.size()) {
				for (MappingIeToKpiPojo mappingIeToKpiPojo : commonPojoList) {
					ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
					kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
					List<String> list = null;
					//????????????ie???????????????????????????
					if (commonTableMap.get(mappingIeToKpiPojo.getTableName()) == null) {
						list = new ArrayList<String>();
						commonTableMap.put(mappingIeToKpiPojo.getTableName(), list);
					} else {
						list = commonTableMap.get(mappingIeToKpiPojo.getTableName());
					}
					list.add(mappingIeToKpiPojo.getKpiName());
				}
			}
			//??????????????????IE
			List<MappingIeToKpiPojo> timePojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(timeKpi));
			//
			if (timePojoList.size() == timeKpi.size()) {
				for (MappingIeToKpiPojo mappingIeToKpiPojo : timePojoList) {
					ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
					kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
					List<String> list = null;
					//????????????ie???????????????????????????
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
			//??????????????????IE
			List<MappingIeToKpiPojo> milePojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(mileKpi));
			if (milePojoList.size() == mileKpi.size()) {
				for (MappingIeToKpiPojo mappingIeToKpiPojo : milePojoList) {
					ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
					kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
					List<String> list = null;
					//????????????ie???????????????????????????
					String tablename = mappingIeToKpiPojo.getTableName();
					String timeTablename = tablename.substring(0,tablename.lastIndexOf("_"))+"_GRID"
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


			if(mile50Kpi.size()>0){
				//??????????????????IE 50*50
				List<MappingIeToKpiPojo> mile50PojoList = mappingIeToKpiDao.findByParam(null,new ArrayList<>(mile50Kpi));
				if (mile50PojoList.size() == mile50Kpi.size()) {
					for (MappingIeToKpiPojo mappingIeToKpiPojo : mile50PojoList) {
						ieToKpiMapping.put(mappingIeToKpiPojo.getIeName(), mappingIeToKpiPojo.getKpiName());
						kpiToieMapping.put(mappingIeToKpiPojo.getKpiName(), mappingIeToKpiPojo.getIeName());
						List<String> list = null;
						String tablename = mappingIeToKpiPojo.getTableName();
						String timeTablename = tablename.substring(0,tablename.lastIndexOf("_"))+"_GRID"
								+tablename.substring(tablename.lastIndexOf("_"),tablename.length());
						if (mile50TableMap.get(timeTablename) == null) {
							list = new ArrayList<String>();
							mile50TableMap.put(timeTablename, list);
						} else {
							list = mile50TableMap.get(timeTablename);
						}
						list.add(mappingIeToKpiPojo.getKpiName());
					}
				}
			}
			//InfluxDb IE
			for (String kpi : influxDbKpi) {
				ieToKpiMapping.put(kpi,kpi);
				kpiToieMapping.put(kpi, kpi);
				List<String> list = null;
				//????????????ie???????????????????????????
				influxDbTableMap.put(kpi,kpi);
			}



			// ?????????????????????
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
				}else{
					logNameList.add(log.getFileName());
				}
			}

			CustomTemplateUtils templateUtil = new CustomTemplateUtils(logNameList,workbook,templateName);
			templateUtil.init();

			clearSummary(workbook,templateUtil);

			int numberOfSheets = workbook.getNumberOfSheets();
			//???????????????4???????????????
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheetAt = workbook.getSheetAt(i);
				String sheetName = sheetAt.getSheetName();
				List<TemplateInfo> tableTag;
				int start_col = 9;
				if(sheetAt.getSheetName().contains("????????????") || sheetAt.getSheetName().contains("?????????") || sheetAt.getSheetName().contains("??????????????????")){
					continue;
				}
				// ??????excel????????????
				int totalRowNum = sheetAt.getLastRowNum() - sheetAt.getFirstRowNum();
				if (0 == totalRowNum) {
					throw new ApplicationException("???" + i + "???sheet??????EXCEL???????????????");
				}
				Row row = sheetAt.getRow(sheetAt.getFirstRowNum());
				if (null == row) {
					throw new ApplicationException("???" + i + "???sheet??????EXCEL???????????????");
				}
				if (sheetName.contains("??????") || sheetName.contains("????????????") || sheetName.contains("????????????")  ){
					tableTag = templateUtil.getTable1();
					//???????????????sheet?????????TemplateInfo???
					templateUtil.setTable_index(1);
				}else if (sheetName.contains("??????") || sheetName.contains("????????????") || sheetName.contains("????????????") ){
					tableTag = templateUtil.getTable2();
					templateUtil.setTable_index(2);
				}else if (sheetName.contains("??????")){
					tableTag = templateUtil.getTable3();
					templateUtil.setTable_index(3);
				}else if (sheetName.contains("??????")){
					tableTag = templateUtil.getTable4();
					templateUtil.setTable_index(4);
				}else if (sheetName.contains("??????")){
					tableTag = templateUtil.getTable5();
					templateUtil.setTable_index(5);
				}else if (sheetName.contains("??????")){
					tableTag = templateUtil.getTable6();
					templateUtil.setTable_index(6);
				}else if (sheetName.contains("??????")){
					tableTag = templateUtil.getTable7();
					templateUtil.setTable_index(7);
				}else{
					continue;
				}
				templateUtil.saveSheetIndex(i);

				Map<Integer, String> mapTypeIndex[] = getRowSpanColSpanMap(sheetAt);
				//?????????????????????
				Map<Integer, String> timeMap = mapTypeIndex[0];
				//????????????????????????
				Map<Integer, String> mileageMap = mapTypeIndex[1];
				//???????????????50*50?????????
				Map<Integer, String> mileage50Map = mapTypeIndex[2];
//				// ????????????????????????
//				CellStyle style = workbook.createCellStyle();
//				// ????????????
//				Font font = workbook.createFont();
//				// ?????????????????????
//				font.setColor((short)0x07234B);
//				font.setFontHeightInPoints((short) 8);
//		        font.setFontName("????????????");
//				style.setFont(font);
//				style.setAlignment(CellStyle.ALIGN_CENTER);
//				style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // ??????
//				// ?????????????????????????????????
//				style.setBorderBottom((short)1);
//				style.setBorderTop((short)1);
//				style.setBorderLeft((short)1);
//				style.setBorderRight((short)1);

				Map<Integer, Integer> previsionMap = new HashMap<Integer, Integer>();
				// ??????????????????row
				Integer formulaIndex = null;
				Row formulaRow = null;
				for (int j = sheetAt.getFirstRowNum(); j <= sheetAt.getLastRowNum(); j++) {
					Row rowContext = sheetAt.getRow(j);
					try {
						for (int s = 0; s <= rowContext.getLastCellNum(); s++) {
							//????????????cell?????????
							Cell rowcell = rowContext.getCell(s);
							if(rowcell==null){
								//?????????????????????
								if(s==0){
									Cell creatcell = rowContext.createCell(s);
//									creatcell.setCellStyle(style);
								}
								continue;
							}
//							rowcell.setCellStyle(style);
							//?????????????????????????????????
							if(s==0){
								String cell0 = getCellValue(rowcell);
								if (!StringUtils.isBlank(cell0) && cell0.equals("??????")) {
									for (int k = 1; k < rowContext.getPhysicalNumberOfCells(); k++) {// ?????????????????????
										String cellValue = getCellValue(rowContext.getCell(k));
										if (!StringUtils.isBlank(cellValue)) {
											// ????????????cell??????????????????
											Integer prevision = getPrevision(cellValue);
											// key????????????index???value??????????????????
											previsionMap.put(k, prevision);
										}
									}
								}
								if (!StringUtils.isBlank(cell0) && cell0.equals("??????")) {
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
				 * ?????????
				 */
				if(!tableTag.isEmpty()){
					// ???1??????????????????????????????????????????2????????????????????????,??????????????????????????????????????????
					Integer beginIndex = formulaIndex + 1;
					sheetAt.shiftRows(beginIndex, sheetAt.getLastRowNum(), tableTag.size()+8, true, false);
					// ??????
					for (int j = 0; j < tableTag.size(); j++) {
						TemplateInfo templateInfo = tableTag.get(j);
						//?????????????????????????????????????????????????????????????????????
						List<TestLogInfo> logList = templateInfo.getTestLogList();
						// ???????????????????????????????????????????????????????????????????????????
						Map<String, BigDecimal> commonKpiValue = new HashMap<String, BigDecimal>();
						Map<String, BigDecimal> timeKpiValue = new HashMap<String, BigDecimal>();
						Map<String, BigDecimal> mileKpiValue = new HashMap<String, BigDecimal>();
						Map<String, BigDecimal> mile50KpiValue = new HashMap<String, BigDecimal>();
						Map<String, BigDecimal> influxKpiValue = new HashMap<String, BigDecimal>();
						for (TestLogInfo testLogInfo : logList) {
							String fileCondition = testLogInfo.getFull_log_name();
							String singleTaskName = unicomLogItemService.queryTaskNameByLogName(fileCondition);
							Map<String, Object> logKpiValue = new HashMap<String, Object>();
							for (Map.Entry<String, List<String>> map : commonTableMap.entrySet()) {
								Map<String, Object> rltTemp = new HashMap<String, Object>();

								String sql = getSqlByParam(map.getKey(), map.getValue(), singleTaskName, fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("???????????????????????????????????????");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(commonKpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										commonKpiValue.put(log.getKey(), (new BigDecimal(((log.getValue()).toString()))));
									}
								}else{
									BigDecimal oldValue = commonKpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = new BigDecimal(((log.getValue()).toString()));
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
								String sql = getSqlByParam(map.getKey(), map.getValue(), singleTaskName, fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("???????????????????????????????????????");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(timeKpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										timeKpiValue.put(log.getKey(), (new BigDecimal(((log.getValue()).toString()))));
									}
								}else{
									BigDecimal oldValue = timeKpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = new BigDecimal((log.getValue()).toString());
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
								String sql = getSqlByParam(map.getKey(), map.getValue(), singleTaskName+"_10", fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("???????????????????????????????????????");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(mileKpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										mileKpiValue.put(log.getKey(), (new BigDecimal(((log.getValue()).toString()))));
									}
								}else{
									BigDecimal oldValue = mileKpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = new BigDecimal(((log.getValue()).toString()));
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
							logKpiValue.clear();
							for (Map.Entry<String, List<String>> map : mile50TableMap.entrySet()) {
								Map<String, Object> rltTemp = new HashMap<String, Object>();
								String sql = getSqlByParam(map.getKey(), map.getValue(), singleTaskName+"_50", fileCondition);
								rltTemp = jdbc.objectQueryNoPage(sql);
//								if(rltTemp == null || rltTemp.isEmpty()){
//									throw new ApplicationException("???????????????????????????????????????");
//								}
								logKpiValue.putAll(rltTemp);
							}
							for (Entry<String, Object> log : logKpiValue.entrySet()) {
								if(mile50KpiValue.get(log.getKey())==null){
									if(log.getValue()!=null){
										mile50KpiValue.put(log.getKey(), (new BigDecimal(((log.getValue()).toString()))));
									}
								}else{
									BigDecimal oldValue = mile50KpiValue.get(log.getKey());
									if(log.getValue()!=null){
										BigDecimal newValue = new BigDecimal(((log.getValue()).toString()));
										if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("max")){
											if(newValue.compareTo(oldValue)==1){
												mile50KpiValue.put(log.getKey(), newValue);
											}
										}else if(kpiToieMapping.get(log.getKey()).toLowerCase().contains("min")){
											if(newValue.compareTo(oldValue)==-1){
												mile50KpiValue.put(log.getKey(), newValue);
											}
										}else{
											mile50KpiValue.put(log.getKey(), oldValue.add(newValue));
										}
									}
								}
							}
						}

						if(!templateUtil.isNormal()){
							influxKpiValue =  getInfluxDbKpi(logList.stream().map(it->it.getFull_log_name()).collect(Collectors.toList()));
							// ????????????
						}

						Row creRow = sheetAt.createRow(templateInfo.getRow_index());

						if(templateUtil.isNormal()){

							if(templateUtil.isYd()){
								if(numberOfSheets==0){
									start_col=6;
								}else{
									start_col=5;
								}
								for(int cellIndex=0;cellIndex<start_col;cellIndex++){
									Cell createCell = creRow.createCell(cellIndex);
									if(cellIndex == 0){
										createCell.setCellValue(templateInfo.getTestLogList().get(0).getFull_log_name());
									}else{
										createCell.setCellValue("");
									}
								}
							}else{
								start_col = 6;
								for(int cellIndex=0;cellIndex<start_col;cellIndex++){
									Cell createCell = creRow.createCell(cellIndex);
									if(cellIndex == 0){
										createCell.setCellValue(templateInfo.getTestLogList().get(0).getFull_log_name());
									}else{
										createCell.setCellValue("");
									}
								}
							}
						}
						else if(templateUtil.getIs_dt()){
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
									createCell.setCellValue("??????");
								}else{
									createCell.setCellValue("");
								}
							}
						}
						//???????????????????????????
						if (formulaRow != null) {
							for (int k = start_col; k < formulaRow.getPhysicalNumberOfCells(); k++) {// ?????????????????????
								Cell createCell = creRow.createCell(k);
								Cell cell = formulaRow.getCell(k);
								if(cell==null){
									continue;
								}
								CellStyle cellStyle = cell.getCellStyle();
								int cellType = formulaRow.getCell(k).getCellType();
								createCell.setCellStyle(cellStyle);
								createCell.setCellType(cellType);
								String cellValue = getCellValue(formulaRow.getCell(k));
								if (!StringUtils.isBlank(cellValue)) {
									//????????????
									Set<String> set = extractMessage(cellValue);
									//??????????????????{}<>
									String expression = cellValue.replaceAll("\\{", "").replaceAll("\\}", "")
											.replaceAll("<", "").replaceAll(">", "");
									Map<String, Object> kpiValueMap = new HashMap<String, Object>();
									for (String kpiname : set) {
										if(timeMap.containsKey(k)){
											kpiValueMap.put(kpiname, timeKpiValue.get(ieToKpiMapping.get(kpiname)));
										}else if(mileageMap.containsKey(k)){
											kpiValueMap.put(kpiname, mileKpiValue.get(ieToKpiMapping.get(kpiname)));
										}else if(mileage50Map.containsKey(k)){
											kpiValueMap.put(kpiname, mile50KpiValue.get(ieToKpiMapping.get(kpiname)));
										}else{
											kpiValueMap.put(kpiname, commonKpiValue.get(ieToKpiMapping.get(kpiname)));
										}
									}
									kpiValueMap.putAll(influxKpiValue);

									//??????????????????
									Double excpressionVlaue = getExcpressionVlaue(kpiValueMap, expression);
									if (excpressionVlaue != null) {
										if(previsionMap.get(k)!=null){
											//??????????????????
											NumberFormat nf = NumberFormat.getNumberInstance();
											nf.setMaximumFractionDigits(previsionMap.get(k));
											String value = nf.format(excpressionVlaue);
											createCell.setCellValue(Float.valueOf(value.replaceAll(",","")));
										}
									}
								}
							}
						} else {
							throw new ApplicationException("IE??????????????????");
						}


						/**
						 if(templateUtil.getIs_dt()){
						 if (sheetName.contains("??????")){
						 templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_1, tableTag,
						 CustomReportConstant.DT_ROW1, CustomReportConstant.DT_COL1,
						 CustomReportConstant.DT_ROW1_SA, CustomReportConstant.DT_COL1_SA);
						 }else if (sheetName.contains("??????")){
						 templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_2, tableTag,
						 CustomReportConstant.DT_ROW2, CustomReportConstant.DT_COL2,
						 CustomReportConstant.DT_ROW2_SA, CustomReportConstant.DT_COL2_SA);
						 }else if (sheetName.contains("??????")){
						 templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_3, tableTag,
						 CustomReportConstant.DT_ROW3, CustomReportConstant.DT_COL3,
						 CustomReportConstant.DT_ROW3_SA, CustomReportConstant.DT_COL3_SA);
						 }else if (sheetName.contains("??????")){
						 templateUtil.dt_func_insert(workbook,CustomReportConstant.DT_ALL_USE_CASE_4, tableTag,
						 CustomReportConstant.DT_ROW4, CustomReportConstant.DT_COL4,
						 CustomReportConstant.DT_ROW4_SA, CustomReportConstant.DT_COL4_SA);
						 }
						 }else{
						 if (sheetName.contains("??????")){
						 templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_1, tableTag,
						 CustomReportConstant.CQT_ROW1, CustomReportConstant.CQT_COL1,
						 CustomReportConstant.CQT_ROW1_SA, CustomReportConstant.CQT_COL1_SA);
						 }else if (sheetName.contains("??????")){
						 templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_2, tableTag,
						 CustomReportConstant.CQT_ROW2, CustomReportConstant.CQT_COL2,
						 CustomReportConstant.CQT_ROW2_SA, CustomReportConstant.CQT_COL2_SA);
						 }else if (sheetName.contains("??????")){
						 templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_3, tableTag,
						 CustomReportConstant.CQT_ROW3, CustomReportConstant.CQT_COL3,
						 CustomReportConstant.CQT_ROW3_SA, CustomReportConstant.CQT_COL3_SA);
						 }else if (sheetName.contains("??????")){
						 templateUtil.cqt_func_insert(workbook,CustomReportConstant.CQT_ALL_USE_CASE_4, tableTag,
						 CustomReportConstant.CQT_ROW4, CustomReportConstant.CQT_COL4,
						 CustomReportConstant.CQT_ROW4_SA, CustomReportConstant.CQT_COL4_SA);
						 }
						 }

						 **/
					}

					//?????????
					if(templateUtil.getIs_dt()){
						if (sheetName.contains("??????")){
							templateUtil.dtSummary(workbook,CustomReportConstant.DT_ALL_USE_CASE_1,tableTag,CustomReportConstant.DT_SUMMARY_TABLE1);
						}else if(sheetName.contains("??????")){
							templateUtil.dtSummary(workbook,CustomReportConstant.DT_ALL_USE_CASE_2,tableTag,CustomReportConstant.DT_SUMMARY_TABLE2);
						}else if(sheetName.contains("??????")){
							templateUtil.dtSummary(workbook,CustomReportConstant.DT_ALL_USE_CASE_3,tableTag,CustomReportConstant.DT_SUMMARY_TABLE3);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary2(workbook,CustomReportConstant.DT_ALL_USE_CASE_4,tableTag,CustomReportConstant.DT_SUMMARY_TABLE4);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary2(workbook,CustomReportConstant.DT_ALL_USE_CASE_5,tableTag,CustomReportConstant.DT_SUMMARY_TABLE5);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary2(workbook,CustomReportConstant.DT_ALL_USE_CASE_6,tableTag,CustomReportConstant.DT_SUMMARY_TABLE6);
						}else if(sheetName.contains("??????")){
							templateUtil.dtSummary(workbook,CustomReportConstant.DT_ALL_USE_CASE_7,tableTag,CustomReportConstant.DT_SUMMARY_TABLE7);
						}
					}else{
						if (sheetName.contains("??????")){
							templateUtil.cqtSummary(workbook,CustomReportConstant.CQT_ALL_USE_CASE_1,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE1);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary(workbook,CustomReportConstant.CQT_ALL_USE_CASE_2,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE2);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary(workbook,CustomReportConstant.CQT_ALL_USE_CASE_3,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE3);
						}else if(sheetName.contains("??????")){
							//	templateUtil.cqtSummary2(workbook,CustomReportConstant.CQT_ALL_USE_CASE_4,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE4);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary2(workbook,CustomReportConstant.CQT_ALL_USE_CASE_5,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE5);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary2(workbook,CustomReportConstant.CQT_ALL_USE_CASE_6,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE6);
						}else if(sheetName.contains("??????")){
							templateUtil.cqtSummary2(workbook,CustomReportConstant.CQT_ALL_USE_CASE_7,tableTag,CustomReportConstant.CQT_SUMMARY_TABLE7);
						}

					}

					// ????????????i+1??????????????????lastRowNum?????????????????????????????????????????????i???
					sheetAt.shiftRows(formulaIndex, sheetAt.getLastRowNum(), -1);// ???????????????
					sheetAt.shiftRows(formulaIndex, sheetAt.getLastRowNum(), -1);// ???????????????
					// ??????sheet???????????????
					//  -2?????????????????????????????????  +tableTag.size()+6?????????????????????  +1????????????????????????i+1????????????
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
					// ??????sheet???????????????
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

			// ?????????????????????Excel????????????????????????????????????excel
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

	private void clearSummary(Workbook workbook,CustomTemplateUtils templateUtils){
		Sheet sheet = workbook.getSheetAt(1);
		if(sheet==null){return;}
		if(sheet.getSheetName() !=null &&  ( sheet.getSheetName().contains("?????????")  || sheet.getSheetName().contains("??????????????????") ) ){

			int rowStart = 4;
			int rowEnd =13;
			int colStart = 6;
			int colEnd = 8;

			int rowStart2 = 20;
			int rowEnd2 = 30;

			if(templateUtils.getIs_dt()){
				rowEnd = 15;
				rowEnd2 = 31;
			}

			for (int i = rowStart; i <=rowEnd ; i++) {
				Row row = sheet.getRow(i - 1);
				for (int j = colStart; j <=colEnd ; j++) {
					templateUtils.clearCellValue(row,j-1);
				}
			}

			for (int i = rowStart2; i <=rowEnd2 ; i++) {
				Row row = sheet.getRow(i - 1);
				templateUtils.clearCellValue(row,6-1);
			}
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
	 * ??????Aviator?????????????????????
	 * @author lucheng
	 * @date 2020???12???30??? ??????7:04:55
	 * @param map ??????????????????key??????????????????????????????value???????????????????????????
	 * @param expression ????????????????????????"a-(b-c)+100"
	 * @return
	 */
	public Double getExcpressionVlaue(Map<String,Object> map,String expression){
		try {
			// ???????????????
			Expression compiledExp = AviatorEvaluator.compile(expression);
			// ???????????????
			BigDecimal result = (BigDecimal) compiledExp.execute(map);
			if(result!=null){
				return result.doubleValue();
			}else{
				return null;
			}
		} catch (ArithmeticException e) {
			//????????????????????????0,????????????null
			return null;
		} catch (ExpressionRuntimeException e) {
			//?????????????????????,????????????null
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * ????????????????????????????????????
	 * @author lucheng
	 * @date 2021???3???17??? ??????11:18:35
	 * @param sheet
	 * @return
	 */
	private Map<Integer, String>[] getRowSpanColSpanMap(Sheet sheet) throws Exception{
		//???????????????key
		Map<Integer, String> mapTime = new HashMap<Integer, String>();
		//???????????????key
		Map<Integer, String> mapMileage = new HashMap<Integer, String>();
		//???????????? 50*50
		Map<Integer, String> mapMileage50 = new HashMap<Integer, String>();

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
			if (!StringUtils.isBlank(cell0) && cell0.contains("??????1s??????????????????")) {
				int tempCol = firstColumn;
				while (tempCol <= lastColumn) {
					mapTime.put(tempCol, "");
					tempCol++;
				}
			}else if (!StringUtils.isBlank(cell0) && cell0.contains("??????10m??10m?????????????????????")) {
				int tempCol = firstColumn;
				while (tempCol <= lastColumn) {
					mapMileage.put(tempCol, "");
					tempCol++;
				}
			}else if(!StringUtils.isBlank(cell0) && cell0.contains("??????50m??50m?????????????????????")){
				int tempCol = firstColumn;
				while (tempCol <= lastColumn) {
					mapMileage50.put(tempCol, "");
					tempCol++;
				}
			}
		}
		Map[] map = { mapTime, mapMileage ,mapMileage50 };
		return map;
	}


	/**
	 * ??????Cell?????????
	 *
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		String value = new String();

		if (null == cell) {
			return value;
		}

		// ????????????????????????
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:// ?????????
				value = cell.getRichStringCellValue().getString().trim();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:// ??????
				// ????????????
				double dd = cell.getNumericCellValue();
				long l = (long) dd;

				if (dd - l > 0) {
					// ?????????Double
					value = new Double(dd).toString().trim();
				} else {
					// ?????????Long
					value = new Long(l).toString().trim();
				}
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				value = new String();
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				value = String.valueOf(cell.getCellFormula()).trim();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:// boolean??????
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
	 * ??????<>??????????????????<>??????<>
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
	 * ????????????
	 * @param msg
	 * @return
	 */
	public Integer getPrevision(String msg) {
		if(msg.equals("??????")){
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
	 * ??????sql
	 * @author lucheng
	 * @date 2020???12???30??? ??????6:07:48
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
		buffer.append(" and log_name='"+fileName+"'");

		return buffer.toString();
	}

	private Map<String,BigDecimal> getInfluxDbKpi(List<String> fileConditionList){

		UnicomLogItemPageQueryRequestBean pageParams = new UnicomLogItemPageQueryRequestBean();
		PageList unicomLogPageList = new PageList();
		unicomLogPageList.putParam("pageQueryBean", pageParams);
		pageParams.setFileNameList(fileConditionList);
		pageParams.setIsFinished(true);
		pageParams.setTestFileStatus(2);

		AbstractPageList abstractPageList = unicomLogItemService.pageList(unicomLogPageList);
		List<UnicomLogItem> unicomLogItemList = abstractPageList.getRows();
		List<String> logIds = unicomLogItemList.stream().map(it -> String.valueOf(it.getRecSeqNo())).collect(Collectors.toList());
		List<Map<String, Object>> reportCellKpi = influxService.getReportCellKpi(logIds);
		// SA NSA
		// ?????? ?????? ??????
		Map<String,BigDecimal> bigDecimalMap = new HashMap<>();
		bigDecimalMap.put("nr_xiaoqu_count",checkInfluxDbKpi(reportCellKpi,"",""));
		bigDecimalMap.put("nr_xiaoqu_count_liantong",checkInfluxDbKpi(reportCellKpi,"","??????"));
		bigDecimalMap.put("nr_xiaoqu_count_dianxin",checkInfluxDbKpi(reportCellKpi,"","??????"));
		bigDecimalMap.put("sa_nr_xiaoqu_count",checkInfluxDbKpi(reportCellKpi,"SA",""));
		bigDecimalMap.put("sa_nr_xiaoqu_count_liantong",checkInfluxDbKpi(reportCellKpi,"SA","??????"));
		bigDecimalMap.put("sa_nr_xiaoqu_count_dianxin",checkInfluxDbKpi(reportCellKpi,"SA","??????"));
		bigDecimalMap.put("nsa_nr_xiaoqu_count",checkInfluxDbKpi(reportCellKpi,"NSA",""));
		bigDecimalMap.put("nsa_nr_xiaoqu_count_liantong",checkInfluxDbKpi(reportCellKpi,"NSA","??????"));
		bigDecimalMap.put("nsa_nr_xiaoqu_count_dianxin",checkInfluxDbKpi(reportCellKpi,"NSA","??????"));
		return bigDecimalMap;
	}

	private BigDecimal checkInfluxDbKpi(List<Map<String, Object>> reportCellKpi,String nettype,String operator){
		long count =  reportCellKpi.stream().filter(it->{
			// ???????????? ?????????
			if(StringUtils.isBlank(nettype)) {
				return true;
			}
			String value = (String)it.get("nettype");
			// ???????????? ?????????
			if(StringUtils.isBlank(value)) {
				return false;
			}
			return value.equals(nettype);
		}).filter(it->{
			// ???????????? ?????????
			if(StringUtils.isBlank(operator)) {
				return true;
			}
			String value = (String)it.get("operator");
			// ???????????? ?????????
			if(StringUtils.isBlank(value)) {
				return false;
			}
			return value.equals(operator);
		}).map(it->{
			String fcn = StringUtils.defaultIfBlank((String)it.get("fcn"),"0");
			String pci =  StringUtils.defaultIfBlank((String)it.get("pci"),"0");
			return NumberUtils.toDouble(fcn) + "__" + NumberUtils.toDouble(pci);
		}).distinct().count();
		return new BigDecimal(count);
	}

}
