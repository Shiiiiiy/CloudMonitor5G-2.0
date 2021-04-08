package com.datang.web.action.action5g.exceptionevent;

import com.datang.service.exceptionevent.ExceptionEventASAAccessService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 11:15
 * 模式接入性异常事件分析参数界面
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Data
@Slf4j
public class ExceptionEventSAAccessAction extends ActionSupport implements ModelDriven<ExceptionEventASAAccessDto> {
    private ExceptionEventASAAccessDto eventDto = new ExceptionEventASAAccessDto();

    @Autowired
    private ExceptionEventASAAccessService accessService;

    @Override
    public ExceptionEventASAAccessDto getModel() {
        return eventDto;
    }

    public String exceptionparamsaaceesslistui(){
    	ExceptionEventASAAccessDto eea = accessService.queryData();
    	ActionContext.getContext().getValueStack().push(eea);
        return ReturnType.LISTUI;
    }


    public String updateexceptioneventaccessmethod(){
        accessService.updateParams(eventDto);
        ActionContext.getContext().getValueStack().set("success", "SUCCESS");
    	return ReturnType.JSON;
    }


}
