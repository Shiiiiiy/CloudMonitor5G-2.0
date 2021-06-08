package com.datang.service.service5g.logbackplay;

import com.datang.domain.testLogItem.IEItem;

import java.util.List;
import java.util.Map;

/**
 * 日志回放服务
 */
public interface LogIEService {
    List<IEItem> getRecrodsByLogId(Long logId);

    List<Map<String, Object>> lineChartDatas(long logId, String startTime, String endTime);

    List<Map<String,Object>> sigleWindowData(long logId, String startTime, String endTime);

    List<Map<String,Object>> evtWindowData(long logId, String startTime, String endTime);

    List<Map<String,Object>> synOper(long logId, String time);
}
