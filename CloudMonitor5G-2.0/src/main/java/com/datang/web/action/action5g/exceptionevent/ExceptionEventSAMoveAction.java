package com.datang.web.action.action5g.exceptionevent;

import com.datang.service.exceptionevent.ExceptionEventSAMoveService;
import com.datang.web.action.ReturnType;
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
public class ExceptionEventSAMoveAction extends ActionSupport implements ModelDriven<ExceptionEventASAMoveDto> {

    @Autowired
    private ExceptionEventSAMoveService exceptionEventSAMoveService;

    private ExceptionEventASAMoveDto eventSAMoveDto = new ExceptionEventASAMoveDto();


    /**
     *SA模式移动性异常事件分析参数配置 页面的跳转
     * @return
     */
    public String exceptionparamsamovelistui(){
    	ExceptionEventASAMoveDto eea = exceptionEventSAMoveService.queryData();
    	ActionContext.getContext().getValueStack().push(eea);
        return ReturnType.LISTUI;
    }

    /**
     * 更新设置参数
     */
    public String updateexceptioneventmovemethod(){
       exceptionEventSAMoveService.updateDataOfExceptionEventSAMoveparams(eventSAMoveDto);
       ActionContext.getContext().getValueStack().set("success", "SUCCESS");
       return ReturnType.JSON;
       
    }

    @Override
    public ExceptionEventASAMoveDto getModel() {
        return eventSAMoveDto;
    }
}
