package com.datang.web.action.knowfeeling;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.common.util.StringUtils;
import com.datang.dao.knowfeeling.KnowFeelingDao;
import com.datang.domain.knowfeeling.KnowFeelingReportTask;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.domain.testManage.terminal.TerminalGroup;
import com.datang.service.knowfeeling.KnowFeelingService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.service.testManage.terminal.TerminalGroupService;
import com.datang.web.action.ReturnType;
import com.datang.web.action.testLogItem.UnicomLogItemAction;
import com.datang.web.beans.report.KnowFeelingRequest;
import com.datang.web.beans.report.ReportRequertBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务感知详单
 * @author shiyan
 * @date 2021/9/2 9:12
 */
@Controller
@Scope("prototype")
public class KnowFeelingAction extends PageAction implements ModelDriven<KnowFeelingRequest> {

    private static Logger logger = LoggerFactory.getLogger(KnowFeelingAction.class);

    @Autowired
    private KnowFeelingService knowFeelingService;

    @Autowired
    private ITestLogItemService testLogItemService;

    @Autowired
    private TerminalGroupService terminalGroupService;

   private KnowFeelingRequest knowFeelingRequest = new KnowFeelingRequest();

    /**
     * 跳转到 list界面
     */
    public String listUI() {
        return ReturnType.LISTUI;
    }

    /**
     * 任务id
     */
    private Long taskId;

    private String ids;

    private String qBeginDate;
    private String qEndDate;
    private String createrName;
    private String qCityIds;


    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
        ReportRequertBean reportRequertBean = new ReportRequertBean();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(StringUtils.hasText(qBeginDate)){
            try {
                reportRequertBean.setBeginDate(sdf.parse(qBeginDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(StringUtils.hasText(qEndDate)){
            try {
                reportRequertBean.setEndDate(sdf.parse(qEndDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        reportRequertBean.setCreaterName(createrName);
        reportRequertBean.setCityIds(qCityIds);
        pageList.putParam("pageQueryBean", reportRequertBean);
        return knowFeelingService.pageList(pageList);
    }

    /**
     * 跳转到添加界面
     *
     * @return
     */
    public String goAdd() {
        return "add";
    }


    public String addReportTask() {

        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        KnowFeelingReportTask task = new KnowFeelingReportTask();

        task.setCreatDate(new Date());
        task.setTaskStatus("3");
        task.setBeginDate(knowFeelingRequest.getBeginDate());
        task.setEndDate(knowFeelingRequest.getEndDate());
        task.setCreaterName(userName);
        task.setLogIds(knowFeelingRequest.getLogIds());

        StringBuilder terminalGroupNames = new StringBuilder();
        if (knowFeelingRequest.getCityIds() != null) {
            String[] cityIds = knowFeelingRequest.getCityIds().trim()
                    .split(",");
            // 存储TestLogItem的id集合
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < cityIds.length; i++) {
                if (StringUtils.hasText(cityIds[i])) {
                    try {
                        ids.add(cityIds[i].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            if (ids.size() > 1) {
                HashSet<String> nameSet = new HashSet<String>();
                for (String cityId : ids) {
                    if (cityId == null) {
                        continue;
                    } else {
                        Long id = Long.valueOf(cityId);
                        String name = terminalGroupService
                                .getProvinceNameByCityGroup(terminalGroupService
                                        .findGroupById(id));
                        nameSet.add(name);
                    }

                }
                if (nameSet.size() > 1) {
                    terminalGroupNames.append("全国");
                } else {
                    terminalGroupNames
                            .append((String) nameSet.toArray()[0]);
                }
            } else {
                Long id = Long.valueOf(ids.get(0));
                TerminalGroup terminalGroup = terminalGroupService
                        .findGroupById(id);
                if (terminalGroup != null) {
                    terminalGroupNames.append(terminalGroup.getName());
                }
            }
        }

        task.setCityIds(knowFeelingRequest.getCityIds());
        task.setTerminalGroup(terminalGroupNames.toString());
        knowFeelingService.save(task);
        return ReturnType.JSON;

    }

    /**
     * 跳转到详情页面
     */
    public String goSee(){
        ActionContext.getContext().getValueStack().set("taskId", taskId);

        System.out.println(taskId);

        return "report";
    }



    public String seeInfo() {

        ActionContext.getContext().getValueStack().set("rows", getReportData());
        ActionContext.getContext().getValueStack().set("footer", new ArrayList());
        return ReturnType.JSON;
    }

    public List<Map<String,Object>> getReportData(){

        KnowFeelingReportTask task = knowFeelingService.find(taskId);

        String logIds = task.getLogIds();

        List<TestLogItem> logList =  testLogItemService.queryTestLogItems(logIds);

        List<String> nameList = logList.stream().map(TestLogItem::getFileName).collect(Collectors.toList());

        if(nameList.size()<1){
            return new ArrayList<>();
        }


        return knowFeelingService.queryKnowFeelingStatics(nameList.toArray(new String[]{}));
    }

    public String downloadDataOverview() {
        return "downloadDataOverview";
    }



    public InputStream getDownloadDataOverview() {


        Map<String, List<Map<String,Object>>> hashMap = new HashMap<>(1);

        hashMap.put("sqlObj", getReportData());

        try {
            Workbook transformToExcel = POIExcelUtil.transformToExcel(hashMap, "templates/业务感知详单.xlsx");
            ActionContext.getContext().put("fileName",new String(("业务感知详单-" + System.currentTimeMillis() + ".xlsx").getBytes(),"ISO8859-1"));
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            transformToExcel.write(byteOutputStream);
            return new ByteArrayInputStream(byteOutputStream.toByteArray());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 删除多个统计任务
     *
     * @return
     * @throws Exception
     */
    public String delReport() {
        if (null != ids) {
            String[] logIds = ids.trim().split(",");
            // 存储TestLogItem的id集合
            List<Long> idss = new ArrayList<>();
            for (int i = 0; i < logIds.length; i++) {
                if (StringUtils.hasText(logIds[i])) {
                    try {
                        idss.add(Long.parseLong(logIds[i].trim()));
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            knowFeelingService.delete(idss);
        }

        return ReturnType.JSON;
    }


    @Override
    public KnowFeelingRequest getModel() {
        return knowFeelingRequest;
    }

    public KnowFeelingRequest getKnowFeelingRequest() {
        return knowFeelingRequest;
    }

    public void setKnowFeelingRequest(KnowFeelingRequest knowFeelingRequest) {
        this.knowFeelingRequest = knowFeelingRequest;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }


    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getqBeginDate() {
        return qBeginDate;
    }

    public void setqBeginDate(String qBeginDate) {
        this.qBeginDate = qBeginDate;
    }

    public String getqEndDate() {
        return qEndDate;
    }

    public void setqEndDate(String qEndDate) {
        this.qEndDate = qEndDate;
    }

    public String getqCityIds() {
        return qCityIds;
    }

    public void setqCityIds(String qCityIds) {
        this.qCityIds = qCityIds;
    }
}
