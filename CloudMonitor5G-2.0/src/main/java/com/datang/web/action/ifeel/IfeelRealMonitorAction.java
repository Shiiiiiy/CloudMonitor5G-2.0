package com.datang.web.action.ifeel;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.datang.common.util.ReflectUtil;
import com.datang.common.util.StringUtils;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;
import com.datang.service.platform.stationParam.StationProspectParamService;
import com.datang.util.ZipUtils;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


/**
 * APP端单验文件上传Action
 * @author maxuancheng
 *
 */
@Controller
@Scope("prototype")
public class IfeelRealMonitorAction{
	/**
	 * 跳转到 list界面
	 *
	 * @return
	 */
	public String listUI() {
		return ReturnType.LISTUI;
	}


}
