/**
 * 
 */
package com.datang.web.action.customTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.customTemplate.CustomLogValidatePojo;
import com.datang.domain.customTemplate.CustomUploadLogItemPojo;
import com.datang.domain.security.User;
import com.datang.domain.testLogItem.StationVerificationLogPojo;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testPlan.TestPlan;
import com.datang.service.customTemplate.CustomLogReportService;
import com.datang.service.customTemplate.CustomReportUploadLogService;
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
public class CustomTestLogAction extends PageAction implements ModelDriven<TestLogItemPageQueryRequestBean> {

	/**
	 * 自定义统计任务服务
	 */
	@Autowired
	private CustomLogReportService customLogReportService;

	/**
	 * 测试日志服务
	 */
	@Autowired
	private CustomReportUploadLogService customReportUploadLogService;

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
	public String goToCustomReportLogJsp() {
		return ReturnType.LISTUI;
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
		pageList.putParam("pageQueryBean", testLogItemPageQueryRequestBean);
		AbstractPageList pageRlt = customReportUploadLogService.pageList(pageList);
		return pageRlt;
	}
	
	
	/**
	 * 校验详单下载
	 * @author lucheng
	 * @date 2021年3月31日 下午1:48:36
	 * @return
	 */
	public String downloadVerifacateDeatail() {
		return "downloadVerifacateDeatail";
	}
	
	public InputStream getVerifacateDeatailExcel() {
		if (null != testLogItemIds) {
			String[] logIds = testLogItemIds.trim().split(",");
			// 存储任务的id集合
			List<Long> idlist = new ArrayList<>();
			for (int i = 0; i < logIds.length; i++) {
				idlist.add(Long.parseLong(logIds[i]));
			}
			List<CustomUploadLogItemPojo> queryTestLogItems = customReportUploadLogService.queryTestLogItems(idlist);
			List<CustomLogValidatePojo> checkList = customReportUploadLogService.queryCheckLog(queryTestLogItems);
		}
		return null;
	}


	public static void deleteFile(File file) {
		// 判断文件不为null或文件目录存在
		if (file == null || !file.exists()) {
			System.out.println("文件删除失败,请检查文件路径是否正确");
			return;
		}
		// 取得这个目录下的所有子文件对象
		File[] files = file.listFiles();
		// 遍历该目录下的文件对象
		for (File f : files) {
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
	public void setTestLogItemPageQueryRequestBean(TestLogItemPageQueryRequestBean testLogItemPageQueryRequestBean) {
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
