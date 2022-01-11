package com.datang.web.action.testLogItem;

import com.datang.domain.testLogItem.IEItem;
import com.datang.domain.testLogItem.PcapData;
import com.datang.service.service5g.logbackplay.LogIEService;
import com.datang.web.action.ReturnType;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
public class LogBackPlayAction {

    @Autowired
    private LogIEService logIEService;
    private long logId;
    private String startTime;
    private String endTime;
    /**
     * 同步请求下发的时间点
     */
    private String time;

    /**
     * 加载更多数据时的起始id
     */
    private long beginId;

    /**
     * 加载更多数据的方向
     */
    private String direction;

    /**
     * 日志回放
     *
     * @return
     */
    public String doLogBackPlay() {
        List<IEItem> ieItems = logIEService.getRecrodsByLogId(logId);
        ActionContext
                .getContext()
                .getValueStack()
                .push(ieItems);
        return ReturnType.JSON;
    }

    /**
     * linechart窗口 数据
     * @return
     */
    public String lineChart() {
        List<Map<String, Object>> ieItems = logIEService.lineChartDatas(logId,startTime,endTime);
        ActionContext
                .getContext()
                .getValueStack()
                .push(ieItems);
        return ReturnType.JSON;
    }

    /**
     * 信令窗口 数据
     * @return
     */
    public String sigleWindowData() {
        List<Map<String, Object>> ieItems = logIEService.sigleWindowData(logId,startTime,endTime);
        ActionContext
                .getContext()
                .getValueStack()
                .push(ieItems);
        return ReturnType.JSON;
    }

    /**
     * 事件窗口 数据
     * @return
     */
    public String evtWindowData() {
        List<Map<String, Object>> ieItems = logIEService.evtWindowData(logId,startTime,endTime);
        ActionContext
                .getContext()
                .getValueStack()
                .push(ieItems);
        return ReturnType.JSON;
    }

    /**
     * 同步操作
     * @return
     */
    public String synOper() {
        List<Map<String, Object>> ieItems = logIEService.synOper(logId,time);
        ActionContext
                .getContext()
                .getValueStack()
                .push(ieItems);
        return ReturnType.JSON;
    }

    /**
     * pcap窗口数据
     * @return
     */
    public String pcapData() {
        List<PcapData> items = logIEService.pcapDatas(logId);
        ValueStack valueStack = ActionContext.getContext().getValueStack();
        valueStack.set("items",items);

        valueStack.set("maxId",logIEService.maxId(logId));
        valueStack.set("minId",logIEService.minId(logId));

        return ReturnType.JSON;
    }

    /**
     * 加载pcap数据
     * @return
     */
    public String morePcapData() {
        List<PcapData> ieItems = logIEService.morePcapDatas(logId,beginId,direction);
        ActionContext.getContext().getValueStack().push(ieItems);
        return ReturnType.JSON;
    }



    /**
     * pcap窗口数据同步
     * @return
     */
    public String syncPcapData() {
        List<PcapData> data = logIEService.syncPcapDatas(logId,time);
        ActionContext.getContext().getValueStack().push(data);
        return ReturnType.JSON;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public long getBeginId() {
        return beginId;
    }

    public void setBeginId(long beginId) {
        this.beginId = beginId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
