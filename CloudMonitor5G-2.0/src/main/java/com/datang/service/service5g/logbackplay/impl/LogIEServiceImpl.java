package com.datang.service.service5g.logbackplay.impl;

import com.datang.dao.dao5g.logbackplay.LogBackPlayDao;
import com.datang.domain.testLogItem.IEItem;
import com.datang.service.influx.InfluxService;
import com.datang.service.service5g.logbackplay.LogIEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("rawtypes")
public class LogIEServiceImpl implements LogIEService {
    @Autowired
    private InfluxService influxService;
    @Autowired
    private LogBackPlayDao logBackPlayDao;
    @Override
    public List<IEItem> getRecrodsByLogId(Long logId) {
        return logBackPlayDao.getRecrodsByLogId(logId);
    }

    @Override
    public List<Map<String, Object>> lineChartDatas(long logId, String startTime, String endTime) {
        return influxService.lineChartDatas(logId,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> sigleWindowData(long logId, String startTime, String endTime) {
        return influxService.sigleDatas(logId,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> evtWindowData(long logId, String startTime, String endTime) {
        return influxService.evtDatas(logId,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> synOper(long logId, String time) {
        return influxService.syncIEWindow(logId,time);
    }
}
