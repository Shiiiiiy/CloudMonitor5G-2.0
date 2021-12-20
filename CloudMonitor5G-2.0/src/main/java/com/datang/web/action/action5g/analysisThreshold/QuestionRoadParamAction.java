package com.datang.web.action.action5g.analysisThreshold;

import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.service.influx.bean.QuesRoadThreshold;
import com.datang.service.service5g.questionRoad.QuestionRoadParamService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问题路段分析参数设置
 * @author shiyan
 * @date 2021/6/23 16:22
 */
@Controller
@Scope("prototype")
public class QuestionRoadParamAction extends ActionSupport {


    @Autowired
    private QuestionRoadParamService questionRoadParamService;


    private String fieldNameEn;

    private String fieldValue;

    /**
     * 进入重叠覆盖参数设置页面
     *
     * @return
     */
    public String questionRoadListUI() {
        ValueStack valueStack = ActionContext.getContext().getValueStack();

        List<QuesRoadThreshold> all = questionRoadParamService.queryAll();

        Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
        for (QuesRoadThreshold threshold : all) {
            weakCoveragParamMap.put(threshold.getName(),String.valueOf(threshold.getValue().intValue()));
        }

        valueStack.set("questionRoadParam",
                weakCoveragParamMap);

        return "questionRoadListUI";
    }


    /**
     * 保存覆盖参数设置修改
     *
     * @return
     */
    public String saveThreshold() {
        String[] nameEnArry = null;
        String[] currentThresholdArry = null;
        if(fieldNameEn!=null && !fieldNameEn.equals("")){
            nameEnArry = fieldNameEn.split(",");
        }
        if(fieldValue!=null && !fieldValue.equals("")){
            currentThresholdArry = fieldValue.split(",");
        }
        if(nameEnArry.length == currentThresholdArry.length){
            questionRoadParamService.updateThreshold(nameEnArry,currentThresholdArry);
            ActionContext.getContext().getValueStack().set("result", "SUCCESS");
        }else{
            ActionContext.getContext().getValueStack().set("errorMsg", "英文名称和修改值不对应");
        }
        return ReturnType.JSON;
    }




    public String getFieldNameEn() {
        return fieldNameEn;
    }

    public void setFieldNameEn(String fieldNameEn) {
        this.fieldNameEn = fieldNameEn;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
