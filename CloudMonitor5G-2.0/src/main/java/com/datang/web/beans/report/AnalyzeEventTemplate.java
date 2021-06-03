package com.datang.web.beans.report;

import com.datang.common.util.ClassUtil;
import com.datang.common.util.StringUtils;
import com.datang.service.influx.InfluxService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AnalyzeEventTemplate implements AnalyzeTemplate {

    private static Logger LOGGER = LoggerFactory
            .getLogger(AnalyzeEventTemplate.class);


    @Override
    public String getId() {
        return "2";
    }

    @Override
    public String getTemplateFileName() {
        return "2.异常事件分析";
    }

    @Override
    public InputStream getTemplateInputStream(InfluxService influxService) {

            Map<String, String> abevtKpiConfig = influxService.getAbevtKpiConfig();
            List<AnalyzeEventTemplate.EasyCell> startCellTitleList = new ArrayList<>();
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("事件名称（参见sheet异常事件清单）","evtName"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("发生时间","time"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("经度","lon"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("纬度","lat"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("事件判断依据","causeBy"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("事件发生前用户接入小区归属网络","network01"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("事件发生前接入小区ID","cellId"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("事件发生前接入基站ID","gnbId"));
            startCellTitleList.add(new AnalyzeEventTemplate.EasyCell("事件发生前接入小区PCI","pci"));

            List<AnalyzeEventTemplate.EasyCell> middleCellTitleList = new ArrayList<>();
            for(Map.Entry<String,String> entry:abevtKpiConfig.entrySet()){
                middleCellTitleList.add(new AnalyzeEventTemplate.EasyCell(entry.getValue(),entry.getKey()));
            }

            List<AnalyzeEventTemplate.EasyCell> endCellTitleList = new ArrayList<>();
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("是否存在TAU失败","exsistTauFail"));
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("源TAC","srcTac"));
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("目标TAC","destTac"));
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("是否存在切换失败","exsistSwFail"));
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("切换目标小区网络","swdestnetwork"));
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("切换目标小区ID","swdestcellId"));
            endCellTitleList.add(new AnalyzeEventTemplate.EasyCell("切换目标基站ID","swdestgnbId"));


            XSSFWorkbook workbook = null;

            try {
                InputStream resourceAsStream = ClassUtil.getResourceAsStream("templates/2.异常事件分析.xlsx");
                workbook = new XSSFWorkbook(resourceAsStream);
                resourceAsStream.close();
                XSSFSheet sheetAt = workbook.getSheetAt(0);
                XSSFRow rowTitle = sheetAt.getRow(1);
                XSSFRow rowValue = sheetAt.getRow(3);
                XSSFCell cellTitleTemplate = rowTitle.getCell(4);
                XSSFCell cellValueTemplate = rowValue.getCell(4);


                AnalyzeEventTemplate.EasyCellStyleConfig config = new AnalyzeEventTemplate.EasyCellStyleConfig();
                config.setTemplateTitleStyle((XSSFCellStyle)cellTitleTemplate.getCellStyle().clone());
                config.setTemplateTitleType(cellTitleTemplate.getCellType());
                config.setTemplateValueStyle((XSSFCellStyle)cellValueTemplate.getCellStyle().clone());
                config.setTemplateValueType(cellValueTemplate.getCellType());

                // 前段固定标题
                for(int i=0;i<startCellTitleList.size();i++){
                    Integer index = 4 + 1 + i;

                    XSSFCell cellTitle = rowTitle.createCell(index);
                    XSSFCell cellValue = rowValue.createCell(index);
                    AnalyzeEventTemplate.EasyCell easyCell = startCellTitleList.get(i);
                    AnalyzeEventTemplate.EasyCellStyleConfig.setCell(cellTitle,cellValue,easyCell,config);
                }

                // 中段动态标题
                for(int i=0;i<middleCellTitleList.size();i++){
                    Integer index = 4 + 1 + startCellTitleList.size() +  i;

                    XSSFCell cellTitle = rowTitle.createCell(index);
                    XSSFCell cellValue = rowValue.createCell(index);
                    AnalyzeEventTemplate.EasyCell easyCell = middleCellTitleList.get(i);
                    AnalyzeEventTemplate.EasyCellStyleConfig.setCell(cellTitle,cellValue,easyCell,config);
                }

                // 后段固定标题
                for(int i=0;i<endCellTitleList.size();i++){
                    Integer index = 4 + 1 +  middleCellTitleList.size() + startCellTitleList.size() +  i;
                    XSSFCell cellTitle = rowTitle.createCell(index);
                    XSSFCell cellValue = rowValue.createCell(index);
                    AnalyzeEventTemplate.EasyCell easyCell = endCellTitleList.get(i);
                    AnalyzeEventTemplate.EasyCellStyleConfig.setCell(cellTitle,cellValue,easyCell,config);
                }

                // 合并第一行单元格，以及设置样式
                CellRangeAddress region = new CellRangeAddress(0,0,4,4  +  middleCellTitleList.size() + startCellTitleList.size() + endCellTitleList.size());
                sheetAt.addMergedRegion(region);
                XSSFRow rowBigTitle = sheetAt.getRow(0);
                XSSFCell firstCell = rowBigTitle.getCell(5);
                firstCell.setCellStyle(config.getTemplateTitleStyle());
                firstCell.setCellType(config.getTemplateTitleType());

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream();
                workbook.write(bos);
                byte[] barray = bos.toByteArray();
                InputStream is = new ByteArrayInputStream(barray);
                return is;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

    }

    @Override
    public Map<String, Collection> getData(InfluxService influxService, List<String> logFileIdList) {
        List<Map<String,Object>> list = new ArrayList<>();
        for(String log:logFileIdList){
            List<String> each = new ArrayList<>();
            each.add(log);
            try{
                List<Map<String, Object>> abEvtAnaList = influxService.getAbEvtAnaList(each);
                list.addAll(abEvtAnaList);
                LOGGER.info(" influxDb " + abEvtAnaList.size() );
            }catch (Exception e){
                LOGGER.error(" influxDb is error");
            }

        }
        Map<String, Collection> hashMap1 = new HashMap<>();
        hashMap1.put("sqlObj1", list);
        return hashMap1;
    }


    @Data
    private static class EasyCellStyleConfig{
        private XSSFCellStyle templateTitleStyle;
        private Integer templateTitleType;
        private XSSFCellStyle templateValueStyle;
        private Integer templateValueType;

        public static void setCell(XSSFCell cellTitle,XSSFCell cellValue,EasyCell easyCell,EasyCellStyleConfig config){
            cellTitle.setCellType(config.getTemplateTitleType());
            cellTitle.setCellStyle(config.getTemplateTitleStyle());
            cellTitle.setCellValue(easyCell.getCellTitle());

            cellValue.setCellType(config.getTemplateValueType());
            cellValue.setCellStyle(config.getTemplateValueStyle());
            String cellValueStr = easyCell.getCellValue();
            if(cellValueStr!=null && StringUtils.hasText(cellValueStr)){
                cellValue.setCellValue("${item." + cellValueStr + "}");
            }

        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class EasyCell{
        private String cellTitle;
        private String cellValue;
    }

}
