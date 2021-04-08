package com.datang.web.action.action5g.exceptionevent;

import com.datang.service.exceptionevent.ExceptionEvent45gService;
import com.datang.service.exceptionevent.ExceptionEventSAMoveService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
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
 * @create 2019-09-16 17:14
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Data
@Slf4j
public class ExceptionEvent45gAction extends ActionSupport implements ModelDriven<ExceptionEvent45gDto> {

    @Autowired
    private ExceptionEvent45gService exceptionEvent45gService;

    private ExceptionEvent45gDto exceptionEvent45gDto = new ExceptionEvent45gDto();


    /**
     *4/5G互操作的异常事件分析参数配置 页面的跳转
     * @return
     */
    public String exceptionparam45glistui(){
    	ExceptionEvent45gDto eea = exceptionEvent45gService.queryData();
    	ActionContext.getContext().getValueStack().push(eea);
        return ReturnType.LISTUI;
    }

    /**
     * 更新设置参数
     */
    public String updateexceptionevent45gmethod(){
    	exceptionEvent45gService.updateDataOfExceptionEventSAMoveparams(exceptionEvent45gDto);
    	ActionContext.getContext().getValueStack().set("success", "SUCCESS");
    	return ReturnType.JSON;
    }

    @Override
    public ExceptionEvent45gDto getModel() {
        return exceptionEvent45gDto;
    }
}
