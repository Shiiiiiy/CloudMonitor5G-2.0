package com.datang.web.action.testLogItem;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.EasyuiPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.ClassUtil;
import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.dao.customTemplate.AnalyFileReportDao;
import com.datang.domain.customTemplate.AnalyFileReport;
import com.datang.domain.customTemplate.CustomLogReportTask;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.domain.testLogItem.LayoutConfig;
import com.datang.domain.testLogItem.TestLogItem;
import com.datang.service.customTemplate.CustomLogReportService;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.service.service5g.logbackplay.LogReplayLayoutService;
import com.datang.service.testLogItem.ITestLogItemService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.questionRoad.QuestionRoadBean;
import com.datang.web.beans.report.AnalyFileReportRequertBean;
import com.datang.web.beans.report.AnalyzeEventTemplate;
import com.datang.web.beans.report.QuesRoadTemplate;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import net.sf.json.JSONArray;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Scope("prototype")
public class LogReplayAction extends PageAction {


    @Autowired
    private LogReplayLayoutService logReplayLayoutService;

    @Autowired
    private CustomLogReportService customLogReportService;

    @Autowired
    private ITestLogItemService testLogItemService;

    @Autowired
    private IVolteAnalysisThresholdService analysisThresholdService;

    @Autowired
    private AnalyFileReportDao analyFileReportDao;

    private String logIds;
    private String logNames;

    private Long configId;
    private String configValue;
    private String configName;

    private Long reportId;


    public String toReviewPage() {

        //??????????????????
        ValueStack valueStack = ActionContext.getContext().getValueStack();
        List<VolteAnalysisThreshold> all = analysisThresholdService.queryBySubjectType(VolteAnalysisThresholdTypeConstant.QUESTION_ROAD_DISPLAY_SETTING);
        Map<String,String> weakCoveragParamMap = new HashMap<String,String>();
        for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
            weakCoveragParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
        }
        valueStack.set("cfgEntity", weakCoveragParamMap);

        return ReturnType.LISTUI;
    }

    /**
     * ????????????????????????
     */
    public String toQuestionRoadPage() throws IOException, SAXException, InvalidFormatException {

        ValueStack valueStack = ActionContext.getContext().getValueStack();

        CustomLogReportTask report = customLogReportService.queryOneByID(reportId);
        List<TestLogItem> logItems = testLogItemService.queryTestLogItems(report.getLogIds());

        StringBuilder ids = new StringBuilder();
        StringBuilder names = new StringBuilder();

        for(int i = 0; i < logItems.size();i++){
            ids.append(logItems.get(i).getRecSeqNo());
            names.append(logItems.get(i).getFileName());
            if(i != logItems.size()-1){
                ids.append(',');
                names.append(',');
            }
        }


        //??????????????????excel
        List<QuestionRoadBean> list = new ArrayList<>();
        PageList pageList = new PageList();
        AnalyFileReportRequertBean param = new AnalyFileReportRequertBean();
        param.setTaskId(report.getId());
        param.setReportId(new QuesRoadTemplate().getId());
        pageList.putParam("pageQueryBean",param);
        EasyuiPageList pageItem = analyFileReportDao.getPageItem(pageList);
        List<AnalyFileReport> rows = pageItem.getRows();
        if(rows==null || rows.size()==0){
        }else{
            String filePath = rows.get(0).getFilePath();
            InputStream excelInputStream = null;
            try{
                excelInputStream = new BufferedInputStream(new FileInputStream(filePath));
            }catch (Exception e){
                //
            }
            if(excelInputStream!=null){
                InputStream inputXml = ClassUtil.getResourceAsStream("replay/questionRoad.xml");

                XLSReader mainReader = ReaderBuilder.buildFromXML(inputXml);
                Map<String,Object> beans = new HashMap<>();
                //?????????sheet
                List<QuestionRoadBean> weakCover = new ArrayList<>();
                //????????????sheet
                List<QuestionRoadBean> overlapCover = new ArrayList<>();
                //????????????
                List<QuestionRoadBean> downQd = new ArrayList<>();
                //????????????
                List<QuestionRoadBean> upQd = new ArrayList<>();
                //???????????????
                List<QuestionRoadBean> downLowSpeed = new ArrayList<>();
                //???????????????
                List<QuestionRoadBean> upLowSpeed = new ArrayList<>();

                beans.put("weakCover",weakCover);
                beans.put("overlapCover",overlapCover);
                beans.put("downQd",downQd);
                beans.put("upQd",upQd);
                beans.put("downLowSpeed",downLowSpeed);
                beans.put("upLowSpeed",upLowSpeed);

                XLSReadStatus readStatus = mainReader.read(excelInputStream,beans);
                boolean status = readStatus.isStatusOK();
                if(status){
                    weakCover.forEach(a->a.setRoadType("???????????????"));
                    overlapCover.forEach(a->a.setRoadType("??????????????????"));
                    downQd.forEach(a->a.setRoadType("??????????????????"));
                    upQd.forEach(a->a.setRoadType("??????????????????"));
                    downLowSpeed.forEach(a->a.setRoadType("?????????????????????"));
                    upLowSpeed.forEach(a->a.setRoadType("?????????????????????"));

                    list.addAll(weakCover);
                    list.addAll(overlapCover);
                    list.addAll(downQd);
                    list.addAll(upQd);
                    list.addAll(downLowSpeed);
                    list.addAll(upLowSpeed);
                }
            }
        }
        //??????????????????
        valueStack.set("roadData", JSONArray.fromObject(list));

        //??????????????????
        List<VolteAnalysisThreshold> all = analysisThresholdService.queryBySubjectType(VolteAnalysisThresholdTypeConstant.QUESTION_ROAD_DISPLAY_SETTING);
        Map<String,String> weakCoverageParamMap = new HashMap<String,String>();
        for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
            weakCoverageParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
        }
        valueStack.set("cfgEntity", weakCoverageParamMap);


        valueStack.set("logIds", ids.toString());
        valueStack.set("logNames", names.toString());

        return ReturnType.LISTUI;
    }

    /**
     * ????????????????????????
     */
    public String toExceptionEventPage() throws IOException, SAXException, InvalidFormatException {

        ValueStack valueStack = ActionContext.getContext().getValueStack();

        CustomLogReportTask report = customLogReportService.queryOneByID(reportId);
        List<TestLogItem> logItems = testLogItemService.queryTestLogItems(report.getLogIds());

        StringBuilder ids = new StringBuilder();
        StringBuilder names = new StringBuilder();

        for(int i = 0; i < logItems.size();i++){
            ids.append(logItems.get(i).getRecSeqNo());
            names.append(logItems.get(i).getFileName());
            if(i != logItems.size()-1){
                ids.append(',');
                names.append(',');
            }
        }

        //??????????????????excel
        List<QuestionRoadBean> list = new ArrayList<>();
        PageList pageList = new PageList();
        AnalyFileReportRequertBean param = new AnalyFileReportRequertBean();
        param.setTaskId(report.getId());
        param.setReportId(new AnalyzeEventTemplate().getId());
        pageList.putParam("pageQueryBean",param);
        EasyuiPageList pageItem = analyFileReportDao.getPageItem(pageList);
        List<AnalyFileReport> rows = pageItem.getRows();
        if(rows==null || rows.size()==0){
        }else{
            String filePath = rows.get(0).getFilePath();
            InputStream excelInputStream = null;
            try{
                excelInputStream = new BufferedInputStream(new FileInputStream(filePath));
            }catch (Exception e){
                //
            }
            if(excelInputStream!=null){
                InputStream inputXml = ClassUtil.getResourceAsStream("replay/exceptionEvent.xml");

                XLSReader mainReader = ReaderBuilder.buildFromXML(inputXml);
                Map<String,Object> beans = new HashMap<>();
                //?????????sheet
                List<QuestionRoadBean> items = new ArrayList<>();
                beans.put("items",items);

                XLSReadStatus readStatus = mainReader.read(excelInputStream,beans);
                boolean status = readStatus.isStatusOK();
                if(status){
                    list.addAll(items);
                }
            }
        }
        //??????????????????
        valueStack.set("exceptEventData", JSONArray.fromObject(list));

        //??????????????????
        List<VolteAnalysisThreshold> all = analysisThresholdService.queryBySubjectType(VolteAnalysisThresholdTypeConstant.QUESTION_ROAD_DISPLAY_SETTING);
        Map<String,String> weakCoverageParamMap = new HashMap<String,String>();
        for (VolteAnalysisThreshold volteAnalysisThreshold : all) {
            weakCoverageParamMap.put(volteAnalysisThreshold.getNameEn(), volteAnalysisThreshold.getCurrentThreshold());
        }
        valueStack.set("cfgEntity", weakCoverageParamMap);


        valueStack.set("logIds", ids.toString());
        valueStack.set("logNames", names.toString());

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
                    "\t\t\t\t'view5':'????????????',\t\n" +
                    "\t\t\t\t'view3':'LTE?????????????????????',\n" +
                    "\t\t\t\t'view2':'NR?????????????????????',\n" +
                    "\t\t\t\t'view7':'linechart??????'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t'appM' :{\n" +
                    "\t\t\t\t'view6':'????????????',\n" +
                    "\t\t\t\t'view4':'LTE?????????????????????',\n" +
                    "\t\t\t\t'view1':'NR?????????????????????',\n" +
                    "\t\t'view8':'pcap??????'\n"+
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

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
