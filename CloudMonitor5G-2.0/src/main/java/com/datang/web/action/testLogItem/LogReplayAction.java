package com.datang.web.action.testLogItem;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller
@Scope("prototype")
public class LogReplayAction extends PageAction {


    private String logsId;



    public String toReviewPage() {
        return ReturnType.LISTUI;
    }


    public String getLayout(){
        String s =    "{\n" +
                "\t\t\t\n" +
                "\t\t\n" +
                "\t\t\t'appL' : {\n" +
                "\t\t\t\t'view5':'信令窗口',\t\n" +
                "\t\t\t\t'view3':'LTE主小区信息窗口',\n" +
                "\t\t\t\t'view2':'NR主小区信息窗口',\n" +
                "\t\t\t\t'view7':'linechart窗口'\n" +
                "\t\t\t},\n" +
                "\t\t\t'appM' :{\n" +
                "\t\t\t\t'view6':'事件窗口',\n" +
                "\t\t\t\t'view4':'LTE主邻区信息窗口',\n" +
                "\t\t\t\t'view1':'NR主邻区信息窗口'\n" +
                "\t\t\t},\n" +
                "\n" +
                "\t\t\t'appR' : {\n" +
                "\t\t\t},\n" +
                "\t\t\t'layout':\"3\"\n" +
                "\t\t}\n" +
                "\t\t";

        ActionContext.getContext().getValueStack().push(s);
        return ReturnType.JSON;
    }


    public String saveLayout(){

        //ActionContext.getContext().getValueStack().push(s);
        return ReturnType.JSON;
    }

    public String updateLayout(){

        //ActionContext.getContext().getValueStack().push(s);
        return ReturnType.JSON;
    }







    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
        return null;
    }












    public String getLogsId() {
        return logsId;
    }
    public void setLogsId(String logsId) {
        this.logsId = logsId;
    }


}
