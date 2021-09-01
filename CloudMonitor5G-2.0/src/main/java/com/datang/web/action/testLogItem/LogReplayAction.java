package com.datang.web.action.testLogItem;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.domain.testLogItem.LayoutConfig;
import com.datang.service.service5g.logbackplay.LogReplayLayoutService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@Scope("prototype")
public class LogReplayAction extends PageAction {


    @Autowired
    private LogReplayLayoutService logReplayLayoutService;

    private String logIds;
    private String logNames;

    private Long configId;
    private String configValue;
    private String configName;


    public String toReviewPage() {

        String param = ServletActionContext.getRequest().getParameter("logsId");

        return ReturnType.LISTUI;
    }


    public String getLayout(){

        LayoutConfig layoutConfig = logReplayLayoutService.getDefaultLayoutConfig();

        String value;


        if(layoutConfig == null){
            value =


                    " {\n" +
                    "\t\t\t\n" +
                    "\t\t\t'appL' : {\n" +
                    "\t\t\t\t'view5':'信令窗口',\t\n" +
                    "\t\t\t\t'view3':'LTE主小区信息窗口',\n" +
                    "\t\t\t\t'view2':'NR主小区信息窗口',\n" +
                    "\t\t\t\t'view7':'linechart窗口'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t'appM' :{\n" +
                    "\t\t\t\t'view6':'事件窗口',\n" +
                    "\t\t\t\t'view4':'LTE主邻区信息窗口',\n" +
                    "\t\t\t\t'view1':'NR主邻区信息窗口',\n" +
                    "\t\t'view8':'pcap窗口'\n"+
                    "\t\t\t},\n" +
                    "\n" +
                    "\t\t\t'appR' : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t'layout':\"3\"\n" +
                    "\t\t}";




        }else{
            value = layoutConfig.getValue();
        }
        ActionContext.getContext().getValueStack().push(value);
        return ReturnType.JSON;
    }



    public String  getAllLayout(){

        List<LayoutConfig> layoutConfig = logReplayLayoutService.getLayoutConfig();
        ActionContext.getContext().getValueStack().push(layoutConfig);

        return ReturnType.JSON;
    }



    public String saveLayout(){

        LayoutConfig layoutConfig = logReplayLayoutService.saveConfig(configValue, configId, configName);

        ActionContext.getContext().getValueStack().push(layoutConfig);
        return ReturnType.JSON;
    }








    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
        return null;
    }


    public String getLogIds() {
        return logIds;
    }

    public void setLogIds(String logIds) {
        this.logIds = logIds;
    }

    public String getLogNames() {
        return logNames;
    }

    public void setLogNames(String logNames) {
        this.logNames = logNames;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
