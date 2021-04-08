package com.datang.web.action.action5g.activation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.StringUtils;
import com.datang.domain.domain5g.activationShow.ActivationShowColorPojo;
import com.datang.domain.security.User;
import com.datang.domain.security.menu.Menu;
import com.datang.domain.security.menu.TerminalMenu;
import com.datang.domain.testManage.terminal.Terminal;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.errorLogManage.impl.ErrorLogManageService;
import com.datang.service.platform.security.IMenuManageService;
import com.datang.service.platform.security.IUserService;
import com.datang.service.service5g.activationShowService.ActivationShowService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.testManage.terminal.TerminalGroupAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 活跃度查看
 * @author maxuancheng
 * @date 2019年7月18日
 */
@Controller
@Scope("prototype")
public class ActivationShowAction extends PageAction implements ModelDriven<Terminal> {
	
	/**
	 * @author maxuancheng
	 */
	private static final long serialVersionUID = 6567251723931763001L;
	
	/**
	 * 区域菜单服务(查询)
	 */
	@Autowired
	private IMenuManageService menuManageService;
	
	@Autowired
	private TerminalGroupAction terminalGroupAction;
	
	@Autowired
	private ErrorLogManageService errorLogManageService;
	
	@Autowired
	private ActivationShowService activationShowService;
	
	/**
	 * 区域管理服务
	 */
	@Autowired
	private TerminalGroupService terminalGroupService;
	
	private Terminal terminal;
	
	private String cityId;
	
	private String cityName;
	
	@Autowired
	private ActivationShowColorPojo activationShowColorPojo;

	/**
	 * 用户服务
	 */
	@Autowired
	private IUserService userService;
	
	public String goToActivationShowList(){
		Map<String, Long> userNumber = getUserNumber(cityId);
		ActionContext.getContext().put("userNumber", userNumber);
		
		Map<String, Object> versionAndUserNumber = getVersionAndUserNumber(cityId,cityName);
		ActionContext.getContext().put("versionAndUserNumber", versionAndUserNumber);
		
		Map<String, Object> terminalUsedStatistics = getTerminalUsedStatistics(cityId);
		ActionContext.getContext().put("terminalUsedStatistics", terminalUsedStatistics);
		
		List<Object> terminalNumber = getTerminalNumber(cityName);
		ActionContext.getContext().put("terminalNumber", JSONArray.fromObject(terminalNumber));
		
		/*HashMap<String, Object> userPlaceShow = getUserPlaceShow();
		ActionContext.getContext().put("userPlaceShow", userPlaceShow);*/
		return ReturnType.LISTUI;
	}
	
	/**
	 * 根据城市名获取相关数据(用户数,软禁版本统计,终端使用情况统计,终端类型统计)
	 * @author maxuancheng
	 * @return
	 */
	public String getCityData(){
		Map<String, Object> map = new HashMap<>();
		Map<String, Long> userNumber = getUserNumber(cityId);
		map.put("userNumber", userNumber);
		
		Map<String, Object> versionAndUserNumber = getVersionAndUserNumber(cityId,cityName);
		map.put("versionAndUserNumber", versionAndUserNumber);
		
		Map<String, Object> terminalUsedStatistics = getTerminalUsedStatistics(cityId);
		map.put("terminalUsedStatistics", terminalUsedStatistics);
		
		List<Object> terminalNumber = getTerminalNumber(cityName);
		map.put("terminalNumber", terminalNumber);
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}
	
	public String getUserPlaceShow(){
		List<String> colorList = new ArrayList<>();
		colorList.add(activationShowColorPojo.getMostActiveColor());
		colorList.add(activationShowColorPojo.getMediumActiveColor());
		colorList.add(activationShowColorPojo.getLeastActiveColor());
		List<List<Object>> LonAndLat = errorLogManageService.getUserPlaceShow(colorList);
		HashMap<String, Object> map = new HashMap<>();
		map.put("LonAndLat", LonAndLat);
		//map.put("colorList", colorList);
		ActionContext.getContext().getValueStack().push(map);
		return ReturnType.JSON;
	}
	
	/**
	 * 根据城市名获取终端类型统计
	 * @author maxuancheng
	 * @param cityName 为null时获取所有城市
	 * @return
	 */
	private List<Object> getTerminalNumber(String cityName) {
		return errorLogManageService.getTerminalNumber(cityName);
	}

	/**
	 * 获取用户数量
	 * @author maxuancheng
	 * @return 
	 */
	public Map<String, Long> getUserNumber(String cityId){
		Map<String, Long> userNumber = new HashMap<>();
		HashSet<String> boxIdSet = new HashSet<>();
		if(StringUtils.hasText(cityId)){
			List<Terminal> cityIdTerminal = terminalGroupService.getAllTerminalByGroupId(Long.valueOf(cityId));
			if(cityIdTerminal != null && cityIdTerminal.size() > 0){
				for (Terminal terminal : cityIdTerminal) {
					boxIdSet.add(terminal.getBoxId());
				}
			}
		}else{
			List<TerminalGroup> allCityGroup = terminalGroupService.getAllCityGroup();
			if(allCityGroup != null && allCityGroup.size() > 0){
				for (TerminalGroup terminalGroup : allCityGroup) {
					Set<Terminal> terminals = terminalGroup.getTerminals();
					for (Terminal terminal : terminals) {
						boxIdSet.add(terminal.getBoxId());
					}
				}
			}
		}
		userNumber = activationShowService.getUserNumber(cityId);
		userNumber.put("userSum", Long.valueOf(boxIdSet.size()));
		return userNumber;
	}
	
	/**
	 * 获取APP或Outum最高版本号和用户数
	 * @author maxuancheng
	 * @param cityId
	 * @return
	 */
	public Map<String, Object> getVersionAndUserNumber(String cityId,String cityName){
		Map<String, Object> userNumber = new HashMap<>();
		List<Object> list = new ArrayList<>();
		Map<String, Object> all = errorLogManageService.getVersionAndUserNumber(cityName);
		list = activationShowService.getOnLineUserBoxid(cityId);
		Map<String, Object> onLine = errorLogManageService.getOnLineVersionAndUserNumber(list);
		userNumber.putAll(all);
		userNumber.putAll(onLine);
		return userNumber;
	}
	
	/**
	 * 终端统使用情况计
	 * @author maxuancheng
	 * @param cityId 城市id
	 * @return
	 */
	public Map<String,Object> getTerminalUsedStatistics(String cityId){
		Map<String, Object> userNumber = new HashMap<>();
		userNumber = activationShowService.getTerminalUsedStatistics(cityId);
		return userNumber;
	}
	
	/**
	 * 获取用户的权限范围内的区域菜单树
	 * 
	 * @return
	 */
	public String terminalGroupTree() {
		// 得到当前用户
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		User user = userService.findByUsername(username);
		List<Menu> menus = menuManageService.getMenus(user);
		// 获得用户权限范围类的区域权限和根权限
		List<TerminalMenu> userTerminalMenus = new ArrayList<>();
		List<TerminalMenu> topTerminalMenus = new ArrayList<>();
		for (Menu menu : menus) {
			if (menu instanceof TerminalMenu) {
				userTerminalMenus.add((TerminalMenu) menu);
				if (null == menu.getPid()) {
					topTerminalMenus.add((TerminalMenu) menu);
				}
			}
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[{\"id\":\"-1\",\"text\":\"区域\",\"children\":");
		Collections.sort(topTerminalMenus);
		TerminalMenu tm = new TerminalMenu();
		tm.setId(-2L);
		tm.setName("全部");
		topTerminalMenus.add(0, tm);
		buildTreeJson(topTerminalMenus, stringBuffer, userTerminalMenus);
		stringBuffer.append("}]");
		// 直接返回原始字符串
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(stringBuffer.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成菜单树JSON
	 * 
	 * @param menus
	 * @param stringBuffer
	 */
	private void buildTreeJson(List<TerminalMenu> menus, StringBuffer json,
			List<TerminalMenu> userTerminalMenus) {
		json.append("[");
		if (menus != null && menus.size() > 0) {
			for (int i = 0; i < menus.size(); i++) {
				TerminalMenu terminalMenu = menus.get(i);
				json.append("{");
				json.append("\"id\"");
				json.append(":");
				json.append(terminalMenu.getId());
				json.append(",");
				json.append("\"text\"");
				json.append(":");
				json.append("\"");
				json.append(terminalMenu.getName());
				json.append("\"");
				json.append(",");
				json.append("\"attributes\":");
				json.append("{\"refId\":");
				json.append(terminalMenu.getRefId());
				json.append(",\"type\":\"");
				json.append(terminalMenu.getType());
				json.append("\"}");
				List<TerminalMenu> children = new ArrayList<TerminalMenu>();
				children.addAll(terminalGroupAction.findTerminalMenusFromListByPid(terminalMenu.getId(), userTerminalMenus));
				Collections.sort(children);
				if (children != null && children.size() > 0) {
					json.append(",");
					json.append("\"children\"");
					json.append(":");
					buildTreeJson(children, json, userTerminalMenus);
				}
				json.append("}");
				if (i != menus.size() - 1) {
					json.append(",");
				}
			}
		}
		json.append("]");
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	@Override
	public Terminal getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractPageList doPageQuery(PageList pageList) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
