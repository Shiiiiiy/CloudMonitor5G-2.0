package com.datang.web.action.testLogItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.domain.testPlan.TestPlan;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.testLogItem.StationVerificationTestService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.service.testPlan.TestPlanService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 单站日志验证Controller
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class StationVerificationTestAction  extends PageAction implements
ModelDriven<StationVerificationLogPojo>{
	
	private static final long serialVersionUID = -3257693802764311416L;
	
	@Value("${stationReportFileLink}")
	private String reportUrl;
	
	@Value("${dzTestLogTtemUrl}")
	private String dzTestLogTtemUrl;
	
	/**
	 * 区域id
	 */
	private Long cityId;
	
	private Long testTimeStartQuery;
	
	private Long testTimeEndQuery;
	
	private String idStr;
	
	private Long[] idArry;
	
	private StationVerificationLogPojo stationVerificationLogPojo = new StationVerificationLogPojo();
	
	@Autowired
	private StationVerificationTestService stationVerificationTestService;
	
	@Autowired
	private IMenuManageService menuManageService;
	
	@Autowired
	private TerminalMenuService terminalMenuService;
	
	/**
	 * 测试计划服务
	 */
	@Autowired
	private TestPlanService testPlanService;
	
	/**
	 * Description:终端服务
	 * @author lucheng
	 * @date 下午6:52:39 
	 */
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 设备组服务
	 */
	@Autowired
	private TerminalGroupService groupService;

	public String goToStationVerificationJsp(){
		return ReturnType.LISTUI;
	}
	
	public String delete(){
		stationVerificationTestService.delete(idStr);
		ActionContext.getContext().getValueStack().set("status",200);
		return ReturnType.JSON;
	}
	
	public String updateParam(){
		if(StringUtils.hasText(idStr)){
			String[] idArry = idStr.replace(" ", "").split(",");
			for (String id : idArry) {
				StationVerificationLogPojo svl = stationVerificationTestService.find(Long.valueOf(id));
				if(StringUtils.hasText(stationVerificationLogPojo.getCorrelativeCell())){
					svl.setCorrelativeCell(stationVerificationLogPojo.getCorrelativeCell());
				}
				if(StringUtils.hasText(stationVerificationLogPojo.getTestService())){
					String value = stationVerificationLogPojo.getTestService();
					String ts = "";
					if(value.equals("DT")){
						ts = "绕点";
					}else if(value.equals("UL")){
						ts = "FTP上传";
					}else if(value.equals("DL")){
						ts = "FTP下载";
					}else if(value.equals("PING#32")){
						ts = "PING（32）测试";
					}else if(value.equals("PING#1500")){
						ts = "PING（1500）测试";
					}else if(value.equals("ATT")){
						ts = "ENDC成功率测试";
					}else if(value.equals("VO")){
						ts = "Volte测试";
					}else if(value.equals("CS")){
						ts = "CSFB测试";
					}else if(value.equals("volteTest")){
						ts = "volte测试";
					}else if(value.equals("raoDL")){
						ts = "绕点_下载";
					}else if(value.equals("raoUL")){
						ts = "绕点_上传";
					}
					svl.setTestService(ts);
				}
				if(StringUtils.hasText(stationVerificationLogPojo.getWirelessStatus())){
					svl.setWirelessStatus(stationVerificationLogPojo.getWirelessStatus());
				}
				if(stationVerificationLogPojo.getTestTime() != null){
					svl.setTestTime(stationVerificationLogPojo.getTestTime());			
				}
				stationVerificationTestService.update(svl);
				
				List<EceptionCellLogPojo> el = stationVerificationTestService.findEceptionCellLogByFileName(svl.getFileName());
				for (EceptionCellLogPojo ecp : el) {
					ecp.setNrCellname(svl.getCorrelativeCell());
					if(StringUtils.hasText(stationVerificationLogPojo.getTestService())){
						String value = stationVerificationLogPojo.getTestService();
						String ts = "";
						if(value.equals("DT")){
							ts = "绕点";
						}else if(value.equals("UL")){
							ts = "FTP上传";
						}else if(value.equals("DL")){
							ts = "FTP下载";
						}else if(value.equals("PING#32")){
							ts = "PING（32）测试";
						}else if(value.equals("PING#1500")){
							ts = "PING（1500）测试";
						}else if(value.equals("ATT")){
							ts = "ENDC成功率测试";
						}else if(value.equals("VO")){
							ts = "volte测试";
						}else if(value.equals("CS")){
							ts = "CSFB测试";
						}else if(value.equals("volteTest")){
							ts = "volte测试";
						}else if(value.equals("raoDL")){
							ts = "绕点_下载";
						}else if(value.equals("raoUL")){
							ts = "绕点_上传";
						}
						ecp.setNrTestevent(ts);
					}
					if(StringUtils.hasText(stationVerificationLogPojo.getWirelessStatus())){
						String value = stationVerificationLogPojo.getWirelessStatus();
						String ws = "";
						if(value.equals("J")){
							ws = "极好点";
						}else if(value.equals("Z")){
							ws = "中点";
						}else if(value.equals("H")){
							ws = "好点";
						}else if(value.equals("C")){
							ws = "差点";
						}
						ecp.setNrWirelessstation(ws);
					}
					ecp.setNrTestdate(svl.getTestTime().toString());
					stationVerificationTestService.updateEcp(ecp);
				}
			}
		}
		
		
		return ReturnType.JSON;
	}
	
	/**
	 * 下载测试日志
	 * 
	 * @return
	 */
	public String downloadLog() {
		ServletOutputStream outputStream = null;
		List<String> unUsedStrList = new ArrayList<String>() ;
		String respString = "<!DOCTYPE html><html><head><title></title></head><body><h2>";
		if (null != idStr) {
			String[] idsArry =  idStr.split(",");
			for (String id : idsArry) {
				StationVerificationLogPojo find = stationVerificationTestService.find(Long.valueOf(id));
				if (null != find && StringUtils.hasText(find.getFilelink())) {
					if(find.getFilelink().contains("./")){
						String filePath = dzTestLogTtemUrl + find.getFilelink().replace("./", "/").replace(" ", "");
						File log = new File(filePath);
						System.out.println(filePath);
						if (!log.exists() || !log.isFile()) {
							unUsedStrList.add(id);
							respString = respString+find.getFileName()+" 日志不存在!路径:"+filePath+"</br>";
						}
					}else{
						unUsedStrList.add(id);
						respString = respString+find.getFileName()+":日志名称的路径应该为相对路径!</br>";
					}
				}
			}
		}
		if(unUsedStrList==null || unUsedStrList.size()==0){
			return "downloadLog";
		}else{
			try {
			respString = respString + "</h2><input type='button' value='返回' onclick='self.window.history.go(-1);' /></body></html>";
			outputStream = ServletActionContext.getResponse().getOutputStream();
				outputStream.write(respString.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (null != outputStream) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		// 文件不存在
		return null;
	}

	/**
	 * 下载测试日志
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public InputStream getDownloadTestLog() {
		try {
			if (null != idStr) {
				String[] idsArry =  idStr.split(",");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String newDate = sdf.format(new Date());
				File file1 = new File(reportUrl+ "/testLog/");
				if (!file1.exists()) {
					file1.mkdirs();
				}
				
				deleteFile(file1);
				File zipFile = new File(reportUrl + "/testLog/" + newDate + "测试日志报告.zip");
			    
				List<File> fileList = new ArrayList<File>();
				for (String id : idsArry) {
					StationVerificationLogPojo find = stationVerificationTestService.find(Long.valueOf(id));
					if (null != find && StringUtils.hasText(find.getFilelink())) {
						File log = new File(dzTestLogTtemUrl + find.getFilelink().replace("./", "/").replace(" ", ""));
						if (log.exists() && log.isFile()) {
							fileList.add(log);
						}
					}
				}
				ActionContext.getContext().put("fileName",new String(zipFile.getName().getBytes(),"ISO8859-1"));
				ZipMultiFile.zipFiles(fileList, zipFile);
				FileInputStream zipIn = new FileInputStream(zipFile);
				zipFile.delete();
				return zipIn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            f.delete();
        }
    }

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// 获取用户权限范围内的二级域menu
		List<TerminalMenu> cities = menuManageService.getCities();
		// 将二级域menu转化成terminalGroup
		List<TerminalGroup> groupsByMenus = groupService.getGroupsByMenus(cities);
		String includeCityIds = "";
		for(int i=0;i<groupsByMenus.size();i++){
			includeCityIds = includeCityIds+groupsByMenus.get(i).getId();
			if(i!=groupsByMenus.size()-1){
				includeCityIds = includeCityIds+",";
			}
		}
		
		pageList.putParam("cityidStrInit", includeCityIds);
		pageList.putParam("cityId", cityId);
		pageList.putParam("testTimeStartQuery", testTimeStartQuery);
		pageList.putParam("testTimeEndQuery", testTimeEndQuery);
		pageList.putParam("stationVerificationLogPojo",stationVerificationLogPojo);
		AbstractPageList doPageQuery = stationVerificationTestService.doPageQuery(pageList);
		List<StationVerificationLogPojo>  testLogList= doPageQuery.getRows();
		for (StationVerificationLogPojo pojo : testLogList) {
			if(pojo.getLogVersion()!=null && !pojo.getLogVersion().equals("") && StringUtils.hasText(pojo.getBoxId())){
				Long versionId = Long.valueOf(pojo.getLogVersion());
				PageList queryParam = new PageList();
				Terminal terminal = terminalService.getTerminal(pojo.getBoxId());
				queryParam.putParam("terminalIds", terminal.getId().toString());
				queryParam.putParam("version", versionId);
				List<TestPlan> testPlans = testPlanService.queryTestPlanByBoxid(queryParam);
				if(testPlans.size() == 1){
					TestPlan testPlan = testPlans.get(0);
					pojo.setTestLevel(testPlan.getLevel());
					pojo.setTestTarget(testPlan.getAutoTestUnit().getGeneralItem().getTestTarget());
				}
			}
			
			
			Menu menu = terminalMenuService.get(pojo.getRegion());	
			// 查询设定的参数，如果区域对应的参数没有设置，则是都达标默认为空
			if (menu == null || menu.getStationParamPojo() == null) {
				pojo.setSatifyTarget(0);
			} else if (menu.getStationParamPojo() != null) {
				StationParamPojo spp = menu.getStationParamPojo();
				if (StringUtils.hasText(pojo.getTestService())) {
					List<EceptionCellLogPojo> logList = stationVerificationTestService
							.findEceptionCellLogByFileName(pojo.getFileName());
					if (logList != null && logList.size() > 0) {
						EceptionCellLogPojo eceptionCellLogPojo = logList.get(0);
						if (pojo.getTestService().equals("FTP下载")) {
							if ((eceptionCellLogPojo.getNrPhyThrDLAvg() != null
											&& eceptionCellLogPojo.getNrPhyThrDLAvg() < spp.getUpgradeGood())
									|| (eceptionCellLogPojo.getNrThrputdl() != null 
											&& Float.valueOf(eceptionCellLogPojo.getNrThrputdl()) < spp.getUpgradeGood())
									|| (eceptionCellLogPojo.getNrRlcThrDLAvg() != null
											&& eceptionCellLogPojo.getNrRlcThrDLAvg() < spp.getUpgradeGood())
									|| (eceptionCellLogPojo.getNrPdcpThrputDLAvg() != null
											&& eceptionCellLogPojo.getNrPdcpThrputDLAvg() < spp.getUpgradeGood())
									|| (eceptionCellLogPojo.getNrSdapThrDLAvg() != null
											&& eceptionCellLogPojo.getNrSdapThrDLAvg() < spp.getUpgradeGood())
									|| (eceptionCellLogPojo.getNrEarfcnsuccess() != null
											&& eceptionCellLogPojo.getNrEarfcndrop() != null
											&& Float.valueOf(eceptionCellLogPojo.getNrEarfcnsuccess())
													/ (Float.valueOf(eceptionCellLogPojo.getNrEarfcnsuccess()) +
															Float.valueOf(eceptionCellLogPojo.getNrEarfcnsuccess())) < (1 - (spp.getDisconectRatio5G()/100))
											&& Float.valueOf(eceptionCellLogPojo.getNrEarfcndrop()) != 0)) {
								pojo.setSatifyTarget(0);

							} else {
								pojo.setSatifyTarget(1);
							}
						} else if (pojo.getTestService().equals("FTP上传")) {
							if ((eceptionCellLogPojo.getNrPhyThrULAvg() != null
									&& eceptionCellLogPojo.getNrPhyThrULAvg() < spp.getUploadGood())
							|| (eceptionCellLogPojo.getNrThrputul() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrThrputul()) < spp.getUploadGood())
							|| (eceptionCellLogPojo.getNrRlcThrULAvg() != null
									&& eceptionCellLogPojo.getNrRlcThrULAvg() < spp.getUploadGood())
							|| (eceptionCellLogPojo.getNrPdcpThrputULAvg() != null
									&& eceptionCellLogPojo.getNrPdcpThrputULAvg() < spp.getUploadGood())
							|| (eceptionCellLogPojo.getNrSdapThrULAvg() != null
									&& eceptionCellLogPojo.getNrSdapThrULAvg() < spp.getUploadGood())
							|| (eceptionCellLogPojo.getNrEarfcnsuccess() != null
									&& eceptionCellLogPojo.getNrEarfcndrop() != null
									&& Float.valueOf(eceptionCellLogPojo.getNrEarfcnsuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getNrEarfcnsuccess()) +
													Float.valueOf(eceptionCellLogPojo.getNrEarfcnsuccess())) < (1 - (spp.getDisconectRatio5G()/100))
									&& Float.valueOf(eceptionCellLogPojo.getNrEarfcndrop()) != 0)) {
								pojo.setSatifyTarget(0);
							} else {
								pojo.setSatifyTarget(1);
							}
							
						} else if (pojo.getTestService().contains("PING") && pojo.getTestService().contains("（") && pojo.getTestService().contains("）")) {
							String pingName = pojo.getTestService().substring(pojo.getTestService().indexOf("（")+1, pojo.getTestService().indexOf("）")) ;
							if(StringUtils.hasText(pingName)){
								if (Float.valueOf(pingName)>=200) { //Ping（2000）测试
									if ((eceptionCellLogPojo.getNrPingresqtime() != null
											&& Float.valueOf(eceptionCellLogPojo.getNrPingresqtime()) > spp.getPing1500DelayTime())
									|| (eceptionCellLogPojo.getNrConnectTimeDelay() != null 
											&& Float.valueOf(eceptionCellLogPojo.getNrConnectTimeDelay()) > spp.getNrAccessDelayTime())
									|| (eceptionCellLogPojo.getNrPingrespose() != null
											&& eceptionCellLogPojo.getNrPingfailure() != null
											&& Float.valueOf(eceptionCellLogPojo.getNrPingrespose())
													/ (Float.valueOf(eceptionCellLogPojo.getNrPingrespose()) +
															Float.valueOf(eceptionCellLogPojo.getNrPingfailure())) < spp.getPing1500SuccessRatio()/100
											&& Float.valueOf(eceptionCellLogPojo.getNrPingfailure()) != 0)) {
										pojo.setSatifyTarget(0);
									} else {
										pojo.setSatifyTarget(1);
									}
								}else if (Float.valueOf(pingName)<200) { //Ping（32）测试
									if ((eceptionCellLogPojo.getNrPingresqtime() != null
											&& Float.valueOf(eceptionCellLogPojo.getNrPingresqtime()) > spp.getPing32DelayTime())
									|| (eceptionCellLogPojo.getNrConnectTimeDelay() != null 
											&& Float.valueOf(eceptionCellLogPojo.getNrConnectTimeDelay()) > spp.getNrAccessDelayTime())
									|| (eceptionCellLogPojo.getNrPingrespose() != null
											&& eceptionCellLogPojo.getNrPingfailure() != null
											&& Float.valueOf(eceptionCellLogPojo.getNrPingrespose())
													/ (Float.valueOf(eceptionCellLogPojo.getNrPingrespose()) +
															Float.valueOf(eceptionCellLogPojo.getNrPingfailure())) < spp.getPing32SuccessRatio()/100
											&& Float.valueOf(eceptionCellLogPojo.getNrPingfailure()) != 0)) {
										pojo.setSatifyTarget(0);
									} else {
										pojo.setSatifyTarget(1);
									}
								}
							}
						} else if (pojo.getTestService().equals("ENDC成功率测试")) {
							if (eceptionCellLogPojo.getLteEpsattachsuccess() != null
									&& eceptionCellLogPojo.getLteEpsattachfailure() != null
									&& Float.valueOf(eceptionCellLogPojo.getLteEpsattachsuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getLteEpsattachsuccess()) +
													Float.valueOf(eceptionCellLogPojo.getLteEpsattachfailure())) < spp.getConnectSuccessRatio4G()/100
									&& Float.valueOf(eceptionCellLogPojo.getLteEpsattachfailure()) != 0) {
								pojo.setSatifyTarget(0);
							} else {
								pojo.setSatifyTarget(1);
							}
						} else if (pojo.getTestService().equals("绕点_上传")) {
							if ((eceptionCellLogPojo.getNrRsrpavg() != null
									&& Float.valueOf(eceptionCellLogPojo.getNrRsrpavg()) < spp.getDtTestRsrp())
							|| (eceptionCellLogPojo.getNrSinr() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrSinr()) < spp.getDtTestSinr())
							|| (eceptionCellLogPojo.getNrPhyThrULAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrPhyThrULAvg()) < spp.getDtUploadRate())
							|| (eceptionCellLogPojo.getNrThrputul() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrThrputul()) < spp.getDtUploadRate())
							|| (eceptionCellLogPojo.getNrRlcThrULAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrRlcThrULAvg()) < spp.getDtUploadRate())
							| (eceptionCellLogPojo.getNrPdcpThrputULAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrPdcpThrputULAvg()) < spp.getDtUploadRate())
							|| (eceptionCellLogPojo.getNrSdapThrULAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrSdapThrULAvg()) < spp.getDtUploadRate())
							|| (eceptionCellLogPojo.getLteInterfreqhandoversuccess() != null
									&& eceptionCellLogPojo.getLteInterfreqhandoverfailure() != null
									&& Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoversuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoversuccess()) +
													Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoverfailure())) < spp.getChangeSuccessRatio4G()/100
									&& Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoverfailure()) != 0)
							|| (eceptionCellLogPojo.getNrEdschangesuccess() != null
									&& eceptionCellLogPojo.getNrEdschangefailure() != null
									&& Float.valueOf(eceptionCellLogPojo.getNrEdschangesuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getNrEdschangesuccess()) +
													Float.valueOf(eceptionCellLogPojo.getNrEdschangefailure())) < spp.getChangeSuccessRatio5G()/100
									&& Float.valueOf(eceptionCellLogPojo.getNrEdschangefailure()) != 0)) {
								pojo.setSatifyTarget(0);
							} else {
								pojo.setSatifyTarget(1);
							}
							
						} else if (pojo.getTestService().equals("绕点_下载")) {
							if ((eceptionCellLogPojo.getNrRsrpavg() != null
									&& Float.valueOf(eceptionCellLogPojo.getNrRsrpavg()) < spp.getDtTestRsrp())
							|| (eceptionCellLogPojo.getNrSinr() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrSinr()) < spp.getDtTestSinr())
							|| (eceptionCellLogPojo.getNrPhyThrDLAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrPhyThrDLAvg()) < spp.getDtDownloadRate())
							|| (eceptionCellLogPojo.getNrThrputdl() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrThrputdl()) < spp.getDtDownloadRate())
							|| (eceptionCellLogPojo.getNrRlcThrDLAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrRlcThrDLAvg()) < spp.getDtDownloadRate())
							| (eceptionCellLogPojo.getNrPdcpThrputDLAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrPdcpThrputDLAvg()) < spp.getDtDownloadRate())
							|| (eceptionCellLogPojo.getNrSdapThrDLAvg() != null 
									&& Float.valueOf(eceptionCellLogPojo.getNrSdapThrDLAvg()) < spp.getDtDownloadRate())
							|| (eceptionCellLogPojo.getLteInterfreqhandoversuccess() != null
									&& eceptionCellLogPojo.getLteInterfreqhandoverfailure() != null
									&& Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoversuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoversuccess()) +
													Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoverfailure())) < spp.getChangeSuccessRatio4G()/100
									&& Float.valueOf(eceptionCellLogPojo.getLteInterfreqhandoverfailure()) != 0)
							|| (eceptionCellLogPojo.getNrEdschangesuccess() != null
									&& eceptionCellLogPojo.getNrEdschangefailure() != null
									&& Float.valueOf(eceptionCellLogPojo.getNrEdschangesuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getNrEdschangesuccess()) +
													Float.valueOf(eceptionCellLogPojo.getNrEdschangefailure())) < spp.getChangeSuccessRatio5G()/100
									&& Float.valueOf(eceptionCellLogPojo.getNrEdschangefailure()) != 0)) {
								pojo.setSatifyTarget(0);
							} else {
								pojo.setSatifyTarget(1);
							}
							
						} else if (pojo.getTestService().equals("语音")) {
							if ((eceptionCellLogPojo.getVonrDelayTimeAvg() != null
									&& Float.valueOf(eceptionCellLogPojo.getVonrDelayTimeAvg() ) > spp.getVoiceAccesDelayTime())
							|| (eceptionCellLogPojo.getDialVonrSuccess() != null
									&& eceptionCellLogPojo.getVonrCancelNum() != null
									&& Float.valueOf(eceptionCellLogPojo.getDialVonrSuccess())
											/ (Float.valueOf(eceptionCellLogPojo.getDialVonrSuccess()) +
													Float.valueOf(eceptionCellLogPojo.getVonrCancelNum())) < spp.getVoiceAccessRatio()/100
									&& Float.valueOf(eceptionCellLogPojo.getVonrCancelNum()) != 0)
							|| (eceptionCellLogPojo.getDialVonrDrop() != null
									&& eceptionCellLogPojo.getDialVonrSuccess() != null
									&& Float.valueOf(eceptionCellLogPojo.getDialVonrDrop())
											/ (Float.valueOf(eceptionCellLogPojo.getDialVonrSuccess()) +
													Float.valueOf(eceptionCellLogPojo.getDialVonrDrop())) > spp.getVoiceDropRatio()/100)) {
								pojo.setSatifyTarget(0);
							} else {
								pojo.setSatifyTarget(1);
							}
							
						}
					}
				}

			}
 		}
		return doPageQuery;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	public Long getTestTimeStartQuery() {
		return testTimeStartQuery;
	}

	public void setTestTimeStartQuery(Long testTimeStartQuery) {
		this.testTimeStartQuery = testTimeStartQuery;
	}

	public Long getTestTimeEndQuery() {
		return testTimeEndQuery;
	}

	public void setTestTimeEndQuery(Long testTimeEndQuery) {
		this.testTimeEndQuery = testTimeEndQuery;
	}

	public StationVerificationLogPojo getStationVerificationLogPojo() {
		return stationVerificationLogPojo;
	}

	public void setStationVerificationLogPojo(StationVerificationLogPojo stationVerificationLogPojo) {
		this.stationVerificationLogPojo = stationVerificationLogPojo;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	@Override
	public StationVerificationLogPojo getModel() {
		return stationVerificationLogPojo;
	}
	
}
