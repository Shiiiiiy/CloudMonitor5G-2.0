package com.datang.web.action.action5g.exceptionevent;

import com.datang.service.exceptionevent.ExceptionEventASAAccessService;
import com.datang.service.exceptionevent.ExceptionEventBussinessService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventBussinessDto;
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
public class ExceptionEventBussineessAction extends ActionSupport implements ModelDriven<ExceptionEventBussinessDto> {
    private ExceptionEventBussinessDto eventDto = new ExceptionEventBussinessDto();

    @Autowired
    private ExceptionEventBussinessService bussinessService;

    @Override
    public ExceptionEventBussinessDto getModel() {
        return eventDto;
    }

    public String exceptionparambussinesslistui(){
    	ExceptionEventBussinessDto eea = bussinessService.queryData();
    	ActionContext.getContext().getValueStack().push(eea);
        return ReturnType.LISTUI;
    }


    public String updateexceptioneventbussinessmethod(){
    	bussinessService.updateParams(eventDto);
        ActionContext.getContext().getValueStack().set("success", "SUCCESS");
    	return ReturnType.JSON;
    }


}
