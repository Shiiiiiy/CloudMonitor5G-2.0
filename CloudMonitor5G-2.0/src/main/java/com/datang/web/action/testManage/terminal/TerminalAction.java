package com.datang.web.action.testManage.terminal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.datang.domain.testLogItem.UnicomLogItem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.ClassUtil;
import com.datang.common.util.CollectionUtils;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.security.menu.MenuType;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.ChannelType;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.domain.testManage.terminal.TestModule;
import com.datang.exception.ApplicationException;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.service.testManage.terminal.TerminalMenuService;
import com.datang.service.testManage.terminal.TerminalService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.testManage.terminal.TermianlTreeResponseBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 终端操作Action
 * 
 * @author yinzhipeng
 * @date:2015年10月8日 下午1:04:30
 * @version
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class TerminalAction extends PageAction implements ModelDriven<Terminal> {

	/**
	 * 区域组服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	/**
	 * 设备服务
	 */
	@Autowired
	private TerminalService terminalService;
	/**
	 * 终端菜单服务
	 */
	@Autowired
	private TerminalMenuService terminalMenuService;
	@Autowired
	private IMenuManageService menuManageService;
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 表单对象
	 */
	private Terminal terminal = new Terminal();
	/**
	 * 市级区域ID
	 */
	private Long cityId;
	/**
	 * 设备状态
	 */
	private Boolean terOnline;
	/**
	 * 上传文件
	 */
	private File importFile;

	/**
	 * 日志
	 */
	private static Logger LOGGER = LoggerFactory
			.getLogger(TerminalAction.class);

	/**
	 * 进入导入界面
	 */
	public String goImport() {
		return "import";
	}

	/**
	 * 批量导入设备
	 * 
	 * @return
	 */
	public String importTerminal() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		try {
			Map<Integer, Object> map = terminalService
					.importTerminal(importFile);
			int[] importCell = (int[]) map.get(1);
			valueStack.set("totalRowNum", importCell[0]);
			valueStack.set("failRowNum", importCell[1]);
			valueStack.set("why", map.get(0));
		} catch (ApplicationException appEx) {
			valueStack.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 下载模板
	 * 
	 * @return
	 */
	public String downloadTemp() {
		return "downloadTemp";
	}

	public InputStream getDownloadTemp() throws IOException {
		ActionContext.getContext().put("fileName",
				new String("终端表模板.xls".getBytes(), "ISO8859-1"));
		InputStream is = ClassUtil.getResourceAsStream("templates/设备表模板.xls");

		return is;
	}

	/**
	 * 跳转到终端list页面
	 * 
	 * @return
	 */
	public String terminalListUI() {
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			ActionContext.getContext().getValueStack().push(group);
		}
		return ReturnType.LISTUI;
	}

	/**
	 * 跳转到添加终端界面
	 * 
	 * @return
	 */
	public String newTerminalUI() {
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			ActionContext.getContext().getValueStack().push(group);
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		// 初始化最新终端信息
		Terminal terminal = new Terminal();
		session.setAttribute("terminal", terminal);

		return ReturnType.ADD;
	}

	/**
	 * 跳转到编辑终端界面
	 * 
	 * @return
	 */
	public String editTerminalUI() {
		if (null != cityId) {
			TerminalGroup group = terminalGroupService.findGroupById(cityId);
			ActionContext.getContext().getValueStack().push(group);
		}
		if (null != terminal.getId()) {
			Terminal terminalDB = terminalService.getTerminal(terminal.getId());
			if (null != terminalDB) {
				ActionContext.getContext().getValueStack()
						.set("terminal", terminalDB);
			}
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			session.setAttribute("terminal", terminalDB);
		}
		return ReturnType.UPDATE;
	}

	/**
	 * 添加车载终端基本信息
	 * 
	 * @return
	 */
	public String addTeBaseInfo() {
		HttpSession session = ServletActionContext.getRequest().getSession();

		try {
			// 将terminal的终端模块信息保存到现session中的terminal
			Terminal sessionTerminal = (Terminal) session
					.getAttribute("terminal");// session
			if(terminal.getTestTarget() == 3 && sessionTerminal.getTestModuls().size() < 1){
				List<TestModule> list = new ArrayList<TestModule>();
				TestModule tm = new TestModule();
				tm.setModuleType(ChannelType.APP_FG);
				tm.setCmoschipType("高通");
				tm.setChannelsNo(0);
				tm.setOperator("中国移动");
				tm.setIndependency(true);
				tm.setEnable(true);
				tm.setTestTerminalType("其他");
				list.add(tm);
				sessionTerminal.setTestModuls(list);
			}
			// 更新基本信息
			setProperties(sessionTerminal, terminal);
			terminalService.addTerminal(sessionTerminal, cityId);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 更新车载终端信息
	 * 
	 * @return
	 */
	public String updateTeInfo() {
		try {
			// 获得基本信息
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			Terminal sessionTerminal = (Terminal) session
					.getAttribute("terminal");// session
			// 更新基本信息
			setProperties(sessionTerminal, terminal);
			terminalService.updateTerminal(sessionTerminal);
		} catch (ApplicationException appEx) {
			ActionContext.getContext().getValueStack()
					.set("errorMsg", appEx.getMessage());
		}
		return ReturnType.JSON;
	}

	/**
	 * 删除多个车载终端
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delTerminal() {

		if (null != terminal.getId()) {
			try {
				terminalService.deleteTerminal(terminal.getId());
			} catch (ApplicationException e) {
				ActionContext.getContext().getValueStack()
						.set("errorMsg", e.getMessage());
			}
		}
		return ReturnType.JSON;
	}
	
	/**
	 * 改变终端状态
	 * @return
	 */
	public String enableTerminalOfFalse(){
		if(null != terminal.getId()){
			terminalService.updateTerminalEnable(terminal.getId(),false);			
		}else{
			ActionContext.getContext().getValueStack()
			.set("errorMsg", "终端ID为空!");
		}
		return ReturnType.JSON;
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
		pageList.putParam("cityId", cityId);
		pageList.putParam("name", terminal.getName());
		pageList.putParam("online", terOnline);
		pageList.putParam("testTarget", terminal.getTestTarget());
		pageList.putParam("installDate", terminal.getInstallDate());
		pageList.putParam("boxId", terminal.getBoxId());
		AbstractPageList abstractPageList = terminalService.queryPageTerminal(pageList);
		List<Terminal>  terminalList = abstractPageList.getRows();
		addNewField(terminalList);
		return abstractPageList;
	}


	private List<Terminal> addNewField(List<Terminal> terminalList){
		/**
		 *
		 * BOX_ID
		 *
		 * EVENTTYPE
		 * LONGITUDE
		 * LATITUDE
		 * FILE_NAME
		 * */
		List<Map<String, Object>> fullCuccTraffic = terminalService.getFullCuccTraffic();
		Map<String,Map<String, Object>> fullCuccTrafficMap = new HashMap<>();

		for(Map<String, Object> m:fullCuccTraffic){
			Object boxId = m.get("box_id");
			if(boxId==null) continue;
			fullCuccTrafficMap.put(boxId.toString(),m);
		}


		for(Terminal t:terminalList){
			String boxId = t.getBoxId();
			if(boxId==null) continue;
			Map<String, Object> stringObjectMap = fullCuccTrafficMap.get(boxId);
			t.setException(getMap(stringObjectMap,"eventtype"));
			t.setLongitude(getMap(stringObjectMap,"longitude"));
			t.setLatitude(getMap(stringObjectMap,"latitude"));
			t.setFileName(getMap(stringObjectMap,"file_name"));
		}
		return terminalList;
	}



	public String getMap(Map<String,Object> m,String key){
		if(m==null){
			return null;
		}
		if(m.get(key)==null){
			return null;
		}
		return m.get(key).toString();

	}


	/**
	 * 下载数据
	 * 
	 * @return
	 */
	public String downloadData() {
		return "downloadData";
	}
	
	public InputStream getDownloadData() throws IOException {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("cityId", cityId);
		param.put("name", terminal.getName());
		param.put("online", terOnline);
		param.put("testTarget", terminal.getTestTarget());
		param.put("installDate", terminal.getInstallDate());
		param.put("boxId", terminal.getBoxId());
		
		List<Terminal> tl = terminalService.findByParam(param);
		for (Terminal terminal : tl) {
			if(terminal.getInstallDate()!=null){
				Date installTime = new Date(terminal.getInstallDate());
				String time = simpleDateFormat.format(installTime);
				terminal.setInstallDateFormt(time);
			}
		}
		addNewField(tl);
		HashMap<String, List<Terminal>> hashMap = new HashMap<String, List<Terminal>>();
		hashMap.put("tl", tl);
		
		ByteArrayOutputStream byteOutputStream = null;
		try {
			byteOutputStream = new ByteArrayOutputStream();
			Workbook transformToExcel = POIExcelUtil.transformToExcel(hashMap,"templates/终端设备数据报表.xls");
			transformToExcel.write(byteOutputStream);
			ActionContext.getContext().put("fileName",new String("终端设备数据报表.xls".getBytes(), "ISO8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ByteArrayInputStream(byteOutputStream.toByteArray());
	}

	/**
	 * 获取该市级组所属省级menu中的所有市级组(只有自己权限的)
	 * 
	 * @param groupId
	 * @return
	 */
	private List<TerminalGroup> getAllCityGroup(Long groupId) {
		List<TerminalGroup> cityGroup = new ArrayList<TerminalGroup>();
		// 获取该市级组所属menu
		TerminalMenu groupMenu = terminalMenuService.getByRefId(groupId);
		// 获取该市级组所属的省级menu
		TerminalMenu pGroupMenu = terminalMenuService.get(groupMenu.getPid());
		// 获取该市级组所属省级menu中的所有市级menu(只有自己权限的)
		List<TerminalMenu> menus = menuManageService.getCities(pGroupMenu
				.getRefId());
		if (menus != null) {
			for (TerminalMenu menu : menus) {
				if (menu.getType().equals(MenuType.City.name())) {
					// 键menu全部转化为组
					TerminalGroup group = terminalGroupService
							.findGroupById(menu.getRefId());
					if (group != null) {
						cityGroup.add(group);
					}
				}
			}
		}
		return cityGroup;
	}

	/**
	 * 设置terminal的部分属性为newTerminal的属性
	 * 
	 * @param terminal
	 * @param newTerminal
	 */
	private void setProperties(Terminal terminal, Terminal newTerminal) {
		terminal.setBoxId(newTerminal.getBoxId());
		terminal.setConfigVersion(newTerminal.getConfigVersion());
		terminal.setEnable(newTerminal.isEnable());
		terminal.setFactoryDate(newTerminal.getFactoryDate());
		terminal.setHardwareVersion(newTerminal.getHardwareVersion());
		terminal.setManufacturer(newTerminal.getManufacturer());
		terminal.setName(newTerminal.getName());
		terminal.setProduceDate(newTerminal.getProduceDate());
		terminal.setRemark(newTerminal.getRemark());
		terminal.setSoftwareVersion(newTerminal.getSoftwareVersion());
		terminal.setTestTarget(newTerminal.getTestTarget());
		List<TestModule> moduleSet = newTerminal.getTestModuls();
		if (!CollectionUtils.isEmpty(moduleSet)) {
			for (TestModule testModule : moduleSet) {
				terminal.addTestModule(testModule);
			}
		}
	}

	/**
	 * 新建车载终端模块信息
	 * 
	 * @return
	 */
	public String newTeModuleInfo() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		TestModule testModule = new TestModule();
		session.setAttribute("testModule", testModule);
		return ReturnType.JSON;
	}

	/**
	 * 获取设备的模块并且构造成tree
	 * 
	 * @return
	 */
	public String terminalModuleNoTree() {
		if (null != terminal && StringUtils.hasText(terminal.getBoxId())) {
			Terminal queryTerminal = terminalService.getTerminal(terminal
					.getBoxId());
			if (null != queryTerminal) {
				List<TermianlTreeResponseBean> responseBeans = new ArrayList<>();
				List<TestModule> testModuls = queryTerminal.getTestModuls();
				if (null != testModuls && 0 != testModuls.size()) {
					for (TestModule testModule : testModuls) {
						TermianlTreeResponseBean responseBean = new TermianlTreeResponseBean();
						responseBean.setGroup(queryTerminal.getName());
						responseBean.setText("通道" + testModule.getChannelsNo());
						responseBean.setValue(testModule.getChannelsNo() + "");
						responseBeans.add(responseBean);
					}
				}
				ActionContext.getContext().getValueStack().push(responseBeans);
			}
		}
		return ReturnType.JSON;
	}

	/**
	 * @return the cityIdcityId
	 */
	public Long getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	@Override
	public Terminal getModel() {
		return terminal;
	}

	/**
	 * @return the terminalterminal
	 */
	public Terminal getTerminal() {
		return terminal;
	}

	/**
	 * @param terminal
	 *            the terminal to set
	 */
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	/**
	 * @return the terOnlineterOnline
	 */
	public Boolean getTerOnline() {
		return terOnline;
	}

	/**
	 * @param terOnline
	 *            the terOnline to set
	 */
	public void setTerOnline(Boolean terOnline) {
		this.terOnline = terOnline;
	}

	/**
	 * @return the importFile
	 */
	public File getImportFile() {
		return importFile;
	}

	/**
	 * @param the
	 *            importFile to set
	 */

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
}