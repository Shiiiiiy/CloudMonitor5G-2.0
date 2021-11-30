package com.datang.web.action.knowfeeling;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageAction;
import com.datang.common.action.page.PageList;
import com.datang.common.util.POIExcelUtil;
import com.datang.constant.VolteAnalysisThresholdTypeConstant;
import com.datang.domain.platform.analysisThreshold.VolteAnalysisThreshold;
import com.datang.service.knowfeeling.KnowFeelingService;
import com.datang.service.platform.analysisThreshold.IVolteAnalysisThresholdService;
import com.datang.web.action.ReturnType;
import com.datang.web.beans.report.KnowFeelingRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 感知监控
 * @author shiyan
 * @date 2021/11/23 13:38
 */
@Controller
@Scope("prototype")
public class KnowFeelingMonitorAction extends PageAction implements ModelDriven<KnowFeelingRequest> {


    @Autowired
    private KnowFeelingService knowFeelingService;

    @Autowired
    private IVolteAnalysisThresholdService analysisThresholdService;

    private KnowFeelingRequest kfRequest = new KnowFeelingRequest();
    /**
     * 跳转到 监控界面
     */
    public String listUI() {
        return ReturnType.LISTUI;
    }


    public String queryData(){

        Map<String,Object> map = new HashMap(8);

        Map<String,Object>  diagnoseInfo = knowFeelingService.queryKnowFeelingStaticsByAreaAndTime(kfRequest.getProv(),kfRequest.getCity(),kfRequest.getBeginDate(),kfRequest.getEndDate());
        List<Map<String,Object>> detailInfo = knowFeelingService.queryKnowFeelingDetailInfo(kfRequest.getProv(),kfRequest.getCity(),kfRequest.getBeginDate(),kfRequest.getEndDate());


        List<Map<String,Object>> traffic = knowFeelingService.queryKnowFeelingByTraffic(kfRequest.getProv(),kfRequest.getCity(),kfRequest.getBeginDate(),kfRequest.getEndDate());
        List<Map<String,Object>> app = knowFeelingService.queryKnowFeelingByApp(kfRequest.getProv(),kfRequest.getCity(),kfRequest.getBeginDate(),kfRequest.getEndDate());

        List<VolteAnalysisThreshold> thresholds = analysisThresholdService
                .queryBySubjectType(VolteAnalysisThresholdTypeConstant.KNOW_FEELING_MONITOR);

        map.put("diagnose",diagnoseInfo);
        map.put("rows",detailInfo.toArray());
        map.put("traffic",traffic);
        map.put("app",app);
        map.put("thresholds",thresholds);
        ActionContext.getContext().getValueStack().push(map);

        return ReturnType.JSON;
    }




    public String exportExcelData(){

        return "exportExcelData";
    }


    /**
     * 导出
     * @author
     * @return
     */
    @SuppressWarnings("rawtypes")
    public InputStream getExportExcelData(){

        List<Map<String,Object>> data = knowFeelingService.queryKnowFeelingDetailInfo(kfRequest.getProv(),kfRequest.getCity(),kfRequest.getBeginDate(),kfRequest.getEndDate());

        Map<String, Collection> hashMap = new HashMap<>();
        hashMap.put("dataList", data);

        Workbook transformToExcel = null;
        try {

            // 导出excel2007
            transformToExcel = POIExcelUtil.transformToExcel(hashMap, "templates/感知分类统计信息.xlsx");
            ActionContext.getContext().put("fileName",new String(("感知分类统计信息"+System.currentTimeMillis()+".xlsx").getBytes(),"ISO8859-1"));

            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            if (null != transformToExcel) {
                transformToExcel.write(byteOutputStream);
                return new ByteArrayInputStream(byteOutputStream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    @Override
    public AbstractPageList doPageQuery(PageList pageList) {
        return null;
    }

    @Override
    public KnowFeelingRequest getModel() {
        return kfRequest;
    }

    public KnowFeelingRequest getKfRequest() {
        return kfRequest;
    }

    public void setKfRequest(KnowFeelingRequest kfRequest) {
        this.kfRequest = kfRequest;
    }
}
