/**
 * 
 */
package com.datang.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.ParamConstant;
import com.datang.common.util.StringUtils;
import com.datang.common.util.TestLogItemUtils;
import com.datang.domain.report.StatisticeTask;
import com.datang.domain.taskOrderManage.FixedPointTaskOrderPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.report.IReportService;
import com.datang.service.taskOrderManage.CQTTaskOrderService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.util.DateUtil;
import com.datang.web.beans.VoLTEDissertation.wholePreview.VoLTEWholePreviewParam;

/**
 * 自动建立新的统计任务，并自动导出的报表的xls文件以统计任务的名称自动保存在服务器中，APP可以进行下载
 * @author lucheng
 * @date 2020年8月19日 下午3:56:39
 */
@Controller
public class CreatCQTTaskReport implements Runnable{
	
	/**
	 * 日志服务
	 */
	@Autowired
	private ITestLogItemService testLogItemService;
	
	
	/**
	 *  定点测试任务服务
	 */
	@Autowired
	private CQTTaskOrderService fixedPointTaskOrderService;
	
	/**
	 * 统计任务服务
	 */
	@Autowired
	private IReportService reportService;
	
	private String cityId;// 城市id
	
	private List<String> logfileNames;// 文件名集合
	
	private FixedPointTaskOrderPojo newfixedPointTask;// 定点测试任务工单
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreatCQTTaskReport.class);
	
	@Value("${appTaskReportFileLink}")
	private String fileSaveUrl;
	
	@Override
	public void run() {
		Map<String, String> map = creatStatisticTask();
		if(map.get("path")!=null && StringUtils.hasText(map.get("path"))){
			newfixedPointTask.setFilePath((String)map.get("path"));
			fixedPointTaskOrderService.update(newfixedPointTask);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public  Map<String, String> creatStatisticTask() {
		long start = new Date().getTime();
		String rltStatus = null;
		loop:while(true){
			rltStatus= "2";
			List<TestLogItem> queryTestLogItems = testLogItemService.queryTestLogItemsByLogName(newfixedPointTask.getBoxId(), logfileNames);
			if(queryTestLogItems.size()==logfileNames.size()){
				for (TestLogItem testLogItem : queryTestLogItems) {
					//判断文件是否为已解析成功，不成功就状态设为“1”
					if(testLogItem.getTestFileStatus()==null || testLogItem.getTestFileStatus()!=2){
						rltStatus = "1";
					}
				}
				if(rltStatus.equals("2")){
					System.out.println("解析完成，准备输出报告");
					break loop;
				}else if ((new Date().getTime() - start) > (12*60*60*1000)) {
				    break loop;
			    }
			}
			try {
				//休眠5min
				System.out.println("CQT任务日志没有解析完成，5min后再次查询");
				Thread.sleep(5*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break loop;
			}
		}
		Map<String,String> returnParaMap = new HashMap<String, String>();
		if(rltStatus.equals("2")){
			String filePath ="";
			Map<String, Object> map;
			File file;
			try {
				//第一步：自动创建统计任务
				StatisticeTask statisticeTask = new StatisticeTask();
				
				//保存城市id
				if (cityId != null) {
					statisticeTask.setCityIds(cityId);

				}
				
				//保存创建人，默认为工单的创建人
				if (null != newfixedPointTask.getTaskInitiator()) {
					statisticeTask.setCreaterName(newfixedPointTask.getTaskInitiator());
				}
				
				//八平村boxid
				if (null != newfixedPointTask.getBoxId()) {
					statisticeTask.setBoxIds(newfixedPointTask.getBoxId());
				}
				
				//查询cqt日志
				List<TestLogItem> queryTestLogItems = testLogItemService.queryTestLogItemsByLogName(newfixedPointTask.getBoxId(), logfileNames);
				
				//保存所有的cqt日志id
				StringBuilder allLogIDBuilder = new StringBuilder();
				//保存所有的日志名称
				StringBuilder allLogNameBuilder = new StringBuilder(); 
				//保存移动所有的日志名称
				StringBuilder moveLogIDBuilder = new StringBuilder();
				//保存联通所有的日志名称
				StringBuilder linkLogIDBuilder = new StringBuilder();
				//保存电信所有的日志名称
				StringBuilder telecomLogIDBuilder = new StringBuilder();

				for (TestLogItem testLogItem : queryTestLogItems) {
					if (0 != allLogIDBuilder.toString().length()) {
						allLogIDBuilder.append(","
								+ testLogItem.getRecSeqNo());
						allLogNameBuilder.append(","
								+ testLogItem.getFileName());
					} else {
						allLogIDBuilder.append(testLogItem.getRecSeqNo());
						allLogNameBuilder.append(testLogItem.getFileName());
					}
					if (null != testLogItem.getOperatorName()) {
						if (testLogItem.getOperatorName().trim().equals("中国移动")||testLogItem.getOperatorName().trim().equals("China Mobile")) {
							if (0 != moveLogIDBuilder.toString().length()) {
								moveLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								moveLogIDBuilder.append(testLogItem.getRecSeqNo());
							}
						} else if (testLogItem.getOperatorName().trim()
								.equals("中国联通")) {
							if (0 != linkLogIDBuilder.toString().length()) {
								linkLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								linkLogIDBuilder.append(testLogItem.getRecSeqNo());
							}
						} else if (testLogItem.getOperatorName().trim()
								.equals("中国电信")) {
							if (0 != telecomLogIDBuilder.toString().length()) {
								telecomLogIDBuilder.append(","
										+ testLogItem.getRecSeqNo());
							} else {
								telecomLogIDBuilder.append(testLogItem
										.getRecSeqNo());
							}
						}
					}
				}
				
				statisticeTask.setLogIds(allLogIDBuilder.toString().trim());
				//统计任务名称
				StringBuilder taskName = new StringBuilder();
				taskName.append("定点测试-");
				taskName.append(newfixedPointTask.getWorkOrderId());
				taskName.append('-');
				taskName.append(DateUtil
						.getCurDateStr(new Date().getTime()));
				statisticeTask.setName(taskName.toString().trim());
				
				//保存当前日期
				statisticeTask.setBeginDate(new Date());
				
				//结束日期设置为1年以后
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(Calendar.YEAR, 1); //把日期往后增加一年，整数往后推，负数往前移
				Date oneYearLatedate = calendar.getTime();
				statisticeTask.setEndDate(oneYearLatedate);
				
				//汇总方式：log文件
				statisticeTask.setCollectType("6");
				
				//测试级别：日常优化
				statisticeTask.setTestRank("2");

				statisticeTask.setCreatDate(new Date());
				//保存统计任务
				reportService.save(statisticeTask);
				
				
				
				//第二步：自动统计nsa指标报表，并输出excel到指定位置
				//当前默认输出所有日志，不区分移动，联通，电信
				Object attribute = null;
//			Integer typeNo = 0;
//			if (null != typeNo) {
//				if (0 == typeNo) {
//					attribute = moveLogIDBuilder.toString();
//				} else if (1 == typeNo) {
//					attribute = linkLogIDBuilder.toString();
//				} else if (2 == typeNo) {
//					attribute = telecomLogIDBuilder.toString();
//				}
//			}
				attribute = allLogIDBuilder.toString();
				
				map = new HashMap<>();
				if (null != attribute) {
					if (attribute instanceof String) {
						String testLogItemIds = (String) attribute;
						List<TestLogItem> testLogItems = testLogItemService
								.queryTestLogItems(testLogItemIds);
						ArrayList<TestLogItem> listTestLogItem = new ArrayList<TestLogItem>();
						String idsString = null;
						for (TestLogItem testLogItem : testLogItems) {
							// if (testLogItem.getServiceType() != null) {
							// if (testLogItem.getServiceType().indexOf("0,") == -1
							// && testLogItem.getServiceType().indexOf("1,") == -1
							// && testLogItem.getServiceType().indexOf("2,") == -1) {
							listTestLogItem.add(testLogItem);
							if (idsString == null) {
								idsString = String.valueOf(testLogItem.getRecSeqNo());
							} else {
								idsString = idsString + ","
										+ String.valueOf(testLogItem.getRecSeqNo());
							}
							// }
							// }
						}

						// 汇总开始时间和结束时间
						map.putAll(TestLogItemUtils.amountBeginEndDate(listTestLogItem));
						// 汇总其他信息
						VoLTEWholePreviewParam lteWholePreviewParam = new VoLTEWholePreviewParam();
						lteWholePreviewParam
								.setQueryTable(ParamConstant.VOLTE_DATA_TABLE);
						lteWholePreviewParam
								.setReportType(ParamConstant.REPOR_TTYPE_NSA_INDEX);
						lteWholePreviewParam.setGroupByField(ParamConstant.RECSEQNO);
						lteWholePreviewParam.setTestLogItemIds(idsString);
						// '指标汇总'sheet
						lteWholePreviewParam
						.setStairClassify(ParamConstant.STAIR_CLASSIFY_INDEX_SUMMARY);
						AbstractPageList coverKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("summaryKpi", coverKpi);
						List coverKpirows = coverKpi.getRows();
						if (null != coverKpirows && 0 != coverKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, coverKpirows);
						}
						// '业务类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_BUSINESS);
						AbstractPageList disturbKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("businessKpi", disturbKpi);
						List disturbKpirows = disturbKpi.getRows();
						if (null != disturbKpirows && 0 != disturbKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, disturbKpirows);
						}
						// '覆盖类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_COVER);
						AbstractPageList subIndexKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("coverKpi", subIndexKpi);
						List subIndexKpirows = subIndexKpi.getRows();
						if (null != subIndexKpirows && 0 != subIndexKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, subIndexKpirows);
						}
						// '移动类（5G）统计指标'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_MOVE);
						AbstractPageList eeKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("mobileKpi", eeKpi);
						List eeKpirows = eeKpi.getRows();
						if (null != eeKpirows && 0 != eeKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, eeKpirows);
						}
						// '接入类'sheet
						lteWholePreviewParam
								.setStairClassify(ParamConstant.STAIR_CLASSIFY_INSERT);
						AbstractPageList beamKpi = reportService
								.queryKpi(lteWholePreviewParam);
						map.put("accessKpi", beamKpi);
						List beamKpirows = beamKpi.getRows();
						if (null != beamKpirows && 0 != beamKpirows.size()) {
							TestLogItemUtils.amountFileNameAndTerminalGroup(
									listTestLogItem, beamKpirows);
						}
					}
					ServletActionContext.getRequest().getSession()
							.setAttribute("KPItestLogItemIds", attribute);
				} else {
					map.put("summaryKpi", new EasyuiPageList());
					map.put("businessKpi", new EasyuiPageList());
					map.put("coverKpi", new EasyuiPageList());
					map.put("mobileKpi", new EasyuiPageList());
					map.put("accessKpi", new EasyuiPageList());
				}
				
				File file1 = new File(fileSaveUrl+ "/CQT任务统计报告/");
				if (!file1.exists()) {
					file1.mkdirs();
				}
				
				file = new File(fileSaveUrl + "/CQT任务统计报告/" + taskName+".xls");
				filePath = fileSaveUrl + "/CQT任务统计报告/" + taskName+".xls";

			} catch (Exception e1) {
				e1.printStackTrace();
				LOGGER.error(e1.getMessage(), e1);
				System.out.println("发生错误："+e1);
				returnParaMap.put("errorMsg", "发生错误："+e1);
				return returnParaMap;
			}
			
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				Workbook transformToExcel = POIExcelUtil.transformToExcel(map,
						"templates/NSA指标报表.xls");
				if (null != transformToExcel) {
					transformToExcel.write(fileOutputStream);
				}
				System.out.println("创建统计任务成功，并保存生成的报表");
				returnParaMap.put("path", filePath);
				return returnParaMap;
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
				System.out.println("发生错误："+e);
				returnParaMap.put("errorMsg", "发生错误："+e);
				return returnParaMap;
			}
		}else{
			System.out.println("日志解析失败");
			returnParaMap.put("errorMsg", "日志解析失败");
			return returnParaMap;
		}
	}


	public CreatCQTTaskReport() {
		super();
	}


	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}


	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the logfileNames
	 */
	public List<String> getLogfileNames() {
		return logfileNames;
	}

	/**
	 * @param logfileNames the logfileNames to set
	 */
	public void setLogfileNames(List<String> logfileNames) {
		this.logfileNames = logfileNames;
	}

	/**
	 * @return the newfixedPointTask
	 */
	public FixedPointTaskOrderPojo getNewfixedPointTask() {
		return newfixedPointTask;
	}



	/**
	 * @param newfixedPointTask the newfixedPointTask to set
	 */
	public void setNewfixedPointTask(FixedPointTaskOrderPojo newfixedPointTask) {
		this.newfixedPointTask = newfixedPointTask;
	}


}
