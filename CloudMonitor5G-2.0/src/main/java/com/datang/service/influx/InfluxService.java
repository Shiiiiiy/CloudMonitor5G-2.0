package com.datang.service.influx;

import com.datang.common.influxdb.InfluxDbConnection;

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


    /**
     * 日志回放linechart 窗口数据
     * @param logId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> lineChartDatas(long logId, String startTime, String endTime);
    /**
     * 日志回放 信令窗口数据
     * @param logId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> sigleDatas(long logId, String startTime, String endTime);
    /**
     * 日志回放 事件窗口数据
     * @param logId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> evtDatas(long logId, String startTime, String endTime);

    /**
     * 同步IE
     * @param logId
     * @param time
     * @return
     */
    List<Map<String,Object>> syncIEWindow(Long logId,String time);

    /**
     * 查询事件列表点发生的时刻列表
     * @param evts
     * @return
     */
    List<Map<String,Object>> evtPointTimes(Long logId,String[] evts);

    /**
     * 查询问题路段相关采样点
     *
     * @param influxDbConnection
     * @param sql
     * @param timeLists
     * @return
     */
    List<Map<String, Object>> queryRoadSampDatas(InfluxDbConnection influxDbConnection, String sql, List<Map<String, String>> timeLists, String[] wheres);




}
