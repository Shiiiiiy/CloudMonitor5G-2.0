/**
 * 
 */
package com.datang.web.action.platform.analysisThreshold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisAboutThreshold;
import com.datang.domain.security.User;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisAboutThresholdService;
import com.datang.service.platform.security.IUserService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 对比分析门限Action
 * 
 * @explain
 * @name ContrastThresholdAction
 * @author shenyanwei
 * @date 2016年5月19日下午4:53:59
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
public class ContrastThresholdAction extends ActionSupport {
	@Autowired
	private IVolteAnalysisAboutThresholdService aboutThresholdService;
	@Autowired
	private IUserService userService;
	private Map<String, VolteAnalysisAboutThreshold> aboutThresholdMap;

	/**
	 * 进入对比分析门限页面
	 * 
	 * @return
	 */
	public String contrastDissThresholdListUI() {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		User user = getUser();
		List<VolteAnalysisAboutThreshold> volteAnalysisAboutTList = null;
		if (user != null) {
			volteAnalysisAboutTList = aboutThresholdService.selectByUser(user);
		}
		Map<String, VolteAnalysisAboutThreshold> map = new HashMap<String, VolteAnalysisAboutThreshold>();
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : volteAnalysisAboutTList) {
			map.put(volteAnalysisAboutThreshold.getThresholdType(),
					volteAnalysisAboutThreshold);
		}
		List<VolteAnalysisAboutThreshold> list = new ArrayList<VolteAnalysisAboutThreshold>();
		list.add(new VolteAnalysisAboutThreshold(null, "MOS均值恶化门限",
				VolteAnalysisThresholdTypeConstant.MOS_VORSEN, "1", user));
		list.add(new VolteAnalysisAboutThreshold(null, "MOS均值稍降门限",
				VolteAnalysisThresholdTypeConstant.MOS_DECLINE, "0.4", user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP均值恶化门限(dBm)",
				VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_VORSEN, "-100",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP均值稍降门限(dBm)",
				VolteAnalysisThresholdTypeConstant.RSRP_AVAREGE_DECLINE, "-95",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP差值恶化门限",
				VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_VORSEN,
				"10", user));
		list.add(new VolteAnalysisAboutThreshold(null, "RSRP差值稍降门限",
				VolteAnalysisThresholdTypeConstant.RSRP_DIFFERENCE_DECLINE,
				"5", user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR均值恶化门限",
				VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_VORSEN, "6",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR差值恶化门限",
				VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_VORSEN, "4",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR均值稍降门限",
				VolteAnalysisThresholdTypeConstant.SINR_AVAREGE_DECLINE, "9",
				user));
		list.add(new VolteAnalysisAboutThreshold(null, "SINR差值稍降门限",
				VolteAnalysisThresholdTypeConstant.SINR_DIFFERENCE_DECLINE,
				"2", user));
		list.add(new VolteAnalysisAboutThreshold(null, "RTP丢包率均值恶化门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_VORSEN,
				"30", user));
		list.add(new VolteAnalysisAboutThreshold(
				null,
				"RTP丢包率差值恶化门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_VORSEN,
				"30", user));
		list.add(new VolteAnalysisAboutThreshold(
				null,
				"RTP丢包率均值稍降门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_AVAREGE_DECLINE,
				"20", user));
		list.add(new VolteAnalysisAboutThreshold(
				null,
				"RTP丢包率差值稍降门限(%)",
				VolteAnalysisThresholdTypeConstant.RTP_LOSEPAGE_DIFFERENCE_DECLINE,
				"20", user));
		list.add(new VolteAnalysisAboutThreshold(null, "栅格大小(米)",
				VolteAnalysisThresholdTypeConstant.GRIDSIZE, "500", user));
		for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : list) {
			VolteAnalysisAboutThreshold aboutThreshold = map
					.get(volteAnalysisAboutThreshold.getThresholdType());
			if (aboutThreshold != null) {
				volteAnalysisAboutThreshold.setId(aboutThreshold.getId());
				volteAnalysisAboutThreshold.setValue(aboutThreshold.getValue());
			}
		}
		valueStack.set("ContrastList", list);

		return ReturnType.LISTUI;
	}

	/**
	 * 保存对比分析门限修改
	 * 
	 * @return
	 */
	public String saveContrastDissThreshold() {

		if (null != aboutThresholdMap) {
			List<VolteAnalysisAboutThreshold> list = new ArrayList<VolteAnalysisAboutThreshold>();
			User user = getUser();
			List<VolteAnalysisAboutThreshold> selectByUser = aboutThresholdService
					.selectByUser(user);
			Map<String, VolteAnalysisAboutThreshold> map = new HashMap<String, VolteAnalysisAboutThreshold>();
			if (selectByUser != null) {
				for (VolteAnalysisAboutThreshold volteAnalysisAboutThreshold : selectByUser) {
					map.put(volteAnalysisAboutThreshold.getNameEn(),
							volteAnalysisAboutThreshold);
				}
			}
			for (Entry<String, VolteAnalysisAboutThreshold> entry : aboutThresholdMap
					.entrySet()) {
				VolteAnalysisAboutThreshold value = entry.getValue();
				value.setUser(user);
				list.add(value);
				VolteAnalysisAboutThreshold aboutThreshold = map.get(value
						.getNameEn());
				if (aboutThreshold != null) {
					aboutThreshold.setValue(value.getValue());
					aboutThresholdService.update(aboutThreshold);
				} else {
					aboutThresholdService.save(value);
				}

			}
			user.setAboutThresholdList(list);
			userService.modifyUser(user);
		}
		ActionContext.getContext().getValueStack().set("result", "SUCCESS");
		return ReturnType.JSON;
	}

	/**
	 * 重置对比分析门限
	 * 
	 * @return
	 */
	public String resetContrastDissThreshold() {

		return ReturnType.JSON;
	}

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	private User getUser() {
		String userName = (String) SecurityUtils.getSubject().getPrincipal();
		if (userName != null) {
			User user = userService.findByUsername(userName);
			return user;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return the aboutThresholdMap
	 */
	public Map<String, VolteAnalysisAboutThreshold> getAboutThresholdMap() {
		return aboutThresholdMap;
	}

	/**
	 * 
	 * @param aboutThresholdMap
	 *            the aboutThresholdMap to set
	 */
	public void setAboutThresholdMap(
			Map<String, VolteAnalysisAboutThreshold> aboutThresholdMap) {
		this.aboutThresholdMap = aboutThresholdMap;
	}

}
