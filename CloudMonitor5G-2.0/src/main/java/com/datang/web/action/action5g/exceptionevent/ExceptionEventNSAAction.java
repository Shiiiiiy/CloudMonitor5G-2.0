package com.datang.web.action.action5g.exceptionevent;

import com.datang.service.exceptionevent.ExceptionEventNSAService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventNSADto;
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
 * @create 2019-09-18 15:39
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Data
@Slf4j
public class ExceptionEventNSAAction extends ActionSupport implements ModelDriven<ExceptionEventNSADto> {

    private ExceptionEventNSADto eventNSADto = new ExceptionEventNSADto();

    @Autowired
    private ExceptionEventNSAService nsaService;


    public String exceptionparamsnsalistui(){
    	ExceptionEventNSADto eea = nsaService.queryData();
    	ActionContext.getContext().getValueStack().push(eea);
        return ReturnType.LISTUI;
    }

    @Override
    public ExceptionEventNSADto getModel() {
        return eventNSADto;
    }


    public String updateexceptionnsamethod(){
        nsaService.updatePrams(eventNSADto);
        ActionContext.getContext().getValueStack().set("success", "SUCCESS");
    	return ReturnType.JSON;
    }

}
