/**
 * 
 */
package com.datang.web.action.testLogItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.security.User;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testPlan.TestPlan;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.service.testPlan.TestPlanService;
import com.datang.util.ZipMultiFile;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.testLogItem.TestLogItemPageQueryRequestBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 测试日志Action
 * 
 * @author yinzhipeng
 * @date:2015年10月30日 上午9:51:02
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TestLogItemAction extends PageAction implements
		ModelDriven<TestLogItemPageQueryRequestBean> {
	
	@Value("${stationReportFileLink}")
	private String reportUrl;
	
	@Value("${dzTestLogTtemUrl}")
	private String dzTestLogTtemUrl;

	/**
	 * 测试日志服务
	 */
	@Autowired
	private ITestLogItemService testlogItemService;
	/**
	 * 菜单管理服务
	 */
	@Autowired
	private IMenuManageService menuManageService;
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * 用户管理服务
	 */
	@Autowired
	private IUserService userService;
	
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
	 * 测试日志筛选参数
	 */
	private TestLogItemPageQueryRequestBean testLogItemPageQueryRequestBean = new TestLogItemPageQueryRequestBean();
	/**
	 * 测试日志id数组按','分隔
	 */
	private String testLogItemIds;
	/**
	 * 测试日志id
	 */
	private Long recSeqNo;

	/**
	 * 跳转到testlogitem list界面
	 * 
	 * @return
	 */
	public String testLogItemListUI() {
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到未完成testlogitem list界面
	 * 
	 * @return
	 */
	public String unfinishedTestLogItemListUI() {
		return "unfinished" + ReturnType.LISTUI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.common.action.page.PageAction#doPageQuery(com.datang.common
	 * .action.page.PageList)
	 */
	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		Set<String> boxIdsList = testLogItemPageQueryRequestBean
				.getBoxIdsList();
		List<Long> cityIdsList = testLogItemPageQueryRequestBean
				.getCityIdsList();
		Set<String> boxIdsSet = testLogItemPageQueryRequestBean.getBoxIdsSet();
		if (0 == boxIdsList.size() && 0 == cityIdsList.size()) {// 默认没选中区域也没选中设备,则获取权限范围内的数据
			String username = (String) SecurityUtils.getSubject()
					.getPrincipal();
			User user = userService.findByUsername(username);
			if (!user.isPowerUser()) {
				List<String> terminalBoxIDs = menuManageService
						.getTerminalBoxIDs();
				if (null == terminalBoxIDs || 0 == terminalBoxIDs.size()) {// 用户权限范围内的设备为null
					return new EasyuiPageList();
				} else {
					boxIdsSet.addAll(terminalBoxIDs);
				}
			}
		} else if (0 == boxIdsList.size() && 0 != cityIdsList.size()) {// 选中区域没选中设备,则获取区域范围内的数据
			for (Long cityId : cityIdsList) {
				List<Terminal> allTerminalByGroupId = terminalGroupService
						.getAllTerminalByGroupId(cityId);
				for (Terminal terminal : allTerminalByGroupId) {
					boxIdsSet.add(terminal.getBoxId());
				}
			}
		} else {// 选中设备或者选中区域和设备,则获取设备范围内的数据
			boxIdsSet.addAll(boxIdsList);
		}

		// 查询对比分析日志
		Boolean isCompare = testLogItemPageQueryRequestBean.getIsCompare();
		if (null != isCompare && isCompare) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			Object attribute = ServletActionContext.getRequest().getSession()
					.getAttribute("testLogItemIds");
			if (null != attribute) {
				if (attribute instanceof String) {
					String testLogItemIds = (String) attribute;
					testLogItemPageQueryRequestBean
							.setSelectTestLogItemIds(testLogItemIds);
				}
			}
		}
		pageList.putParam("pageQueryBean", testLogItemPageQueryRequestBean);
		AbstractPageList pageRlt = testlogItemService.pageList(pageList);
		List<TestLogItem>  testLogList= pageRlt.getRows();
		for (TestLogItem testLogItem : testLogList) {
			PageList queryParam = new PageList();
			if(testLogItem.getLogVersion()!=null && !testLogItem.getLogVersion().equals("") && StringUtils.hasText(testLogItem.getBoxId())){
				Long versionId = Long.valueOf(testLogItem.getLogVersion());
				Terminal terminal = terminalService.getTerminal(testLogItem.getBoxId());
				queryParam.putParam("terminalIds", terminal.getId().toString());
				queryParam.putParam("version", versionId);
				List<TestPlan> testPlans = testPlanService.queryTestPlanByBoxid(queryParam);
				if(testPlans.size()==1){
					TestPlan testPlan = testPlans.get(0);
					testLogItem.setTestLevel(testPlan.getLevel());
					testLogItem.setTestTarget(testPlan.getAutoTestUnit().getGeneralItem().getTestTarget());
				}
			}
 		}
		return pageRlt;
	}

	/**
	 * 保存测试日志id数组
	 * 
	 * @return
	 */
	public String saveAnalysisTestLogItem() {
		if (StringUtils.hasText(testLogItemIds)) {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("testLogItemIds", testLogItemIds);
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 删除指定的测试日志
	 * 
	 * @return
	 */
	public String deleteTestLogItem() {
		if (StringUtils.hasText(testLogItemIds)) {
			testlogItemService.deleteTestLogItem(testLogItemIds);
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
		if (null != testLogItemIds) {
			String[] idsArry =  testLogItemIds.split(",");
			for (String id : idsArry) {
				TestLogItem queryTestLogById = testlogItemService.queryTestLogById(Long.valueOf(id));
				if (null != queryTestLogById && StringUtils.hasText(queryTestLogById.getFilelink())) {
					if(queryTestLogById.getFilelink().contains("./")){
						String filePath = dzTestLogTtemUrl + queryTestLogById.getFilelink().replace("./", "/").replace(" ", "");
						if(filePath.indexOf(queryTestLogById.getFileName()) == -1){
							filePath = filePath + queryTestLogById.getFileName();
						}
						File log = new File(filePath);
						System.out.println(filePath);
						if (!log.exists() || !log.isFile()) {
							unUsedStrList.add(id);
							respString = respString+queryTestLogById.getFileName()+" 日志不存在!路径:"+filePath+"</br>";
						}
					}else{
						unUsedStrList.add(id);
						respString = respString+queryTestLogById.getFileName()+":日志名称的路径应该为相对路径!</br>";
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
	public InputStream getDownloadTestLog() {
		try {
			if (null != testLogItemIds) {
				String[] idsArry =  testLogItemIds.split(",");
				
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
					TestLogItem queryTestLogById = testlogItemService.queryTestLogById(Long.valueOf(id));
					if (null != queryTestLogById && StringUtils.hasText(queryTestLogById.getFilelink())) {
						String filePath = dzTestLogTtemUrl + queryTestLogById.getFilelink().replace("./", "/");
						if(filePath.indexOf(queryTestLogById.getFileName()) == -1){
							filePath = filePath + queryTestLogById.getFileName();
						}
						
						File log = new File(filePath);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public TestLogItemPageQueryRequestBean getModel() {
		return testLogItemPageQueryRequestBean;
	}

	/**
	 * @return the
	 *         testLogItemPageQueryRequestBeantestLogItemPageQueryRequestBean
	 */
	public TestLogItemPageQueryRequestBean getTestLogItemPageQueryRequestBean() {
		return testLogItemPageQueryRequestBean;
	}

	/**
	 * @param testLogItemPageQueryRequestBean
	 *            the testLogItemPageQueryRequestBean to set
	 */
	public void setTestLogItemPageQueryRequestBean(
			TestLogItemPageQueryRequestBean testLogItemPageQueryRequestBean) {
		this.testLogItemPageQueryRequestBean = testLogItemPageQueryRequestBean;
	}

	/**
	 * @return the testLogItemIdstestLogItemIds
	 */
	public String getTestLogItemIds() {
		return testLogItemIds;
	}

	/**
	 * @param testLogItemIds
	 *            the testLogItemIds to set
	 */
	public void setTestLogItemIds(String testLogItemIds) {
		this.testLogItemIds = testLogItemIds;
	}

	/**
	 * @return the recSeqNorecSeqNo
	 */
	public Long getRecSeqNo() {
		return recSeqNo;
	}

	/**
	 * @param recSeqNo
	 *            the recSeqNo to set
	 */
	public void setRecSeqNo(Long recSeqNo) {
		this.recSeqNo = recSeqNo;
	}

}
