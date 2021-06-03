package com.datang.service.influx;

import java.util.List;
import java.util.Map;

/**
 * 与influx 相关的数据返回接口
 */
public interface InfluxService {
    /**
     * 根据文件名列表获取地图轨迹数据集合
     * @param fileNames
     * @return
     */
    List<Map<String, Object>> getMapTrailByLogFiles(List<String> fileNames);

    /**
     * 获取采样点的数据集
     * @param fileLogIds
     * @return
     */
    List<Map<String, Object>> getSampPointByLogFiles(List<String> fileLogIds);

    /**
     * 获取事件报表的数据集
     * @param fileLogIds
     * @return
     */
    List<Map<String, Object>> getEventByLogFiles(List<String> fileLogIds);

    /**
     * 获取栅格的数据集
     * @param fileLogIds
     * @return
     */
    List<Map.Entry<String, List<Map<String, Object>>>> getGridDatasByLogFiles(List<String> fileLogIds);

    /**
     * 异常事件分析报表获取
     * @param fileLogIds
     * @return
     */
    List<Map<String, Object>> getAbEvtAnaList(List<String> fileLogIds);

    /**
     * 异常事件分析表格查询概览
     * @param fileLogIds
     * @return
     */
    List<Map<String, Object>> getAbEvtAnaGerView(List<String> fileLogIds);

    /**
     * 获取异常事件kpi配置
     * @return
     */
    Map<String, String> getAbevtKpiConfig();


    /**
     * 获取网络配置报表
     * 包含6个报表数据
     * @return
     */
    Map<String,List<Map<String, Object>>> getNetConfigReports(List<String> fileLogIds);

    /**
     * 获取语音业务分析
     * 报表数据
     * @return
     */
    List<Map<String, Object>> getVoiceBusiReports(List<String> fileLogIds);

    /**
     * 获取小区频点pci信息
     * @param fileLogIds
     * @return
     */
    List<Map<String, Object>> getReportCellKpi(List<String> fileLogIds);
}
