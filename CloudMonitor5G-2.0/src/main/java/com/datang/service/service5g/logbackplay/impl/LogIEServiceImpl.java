package com.datang.service.service5g.logbackplay.impl;

import com.datang.common.util.DateComputeUtils;
import com.datang.dao.dao5g.logbackplay.LogBackPlayDao;
import com.datang.domain.testLogItem.IEItem;
import com.datang.domain.testLogItem.PcapData;
import com.datang.service.influx.InfluxService;
import com.datang.service.service5g.logbackplay.LogIEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<Map<String, Object>> list = influxService.sigleDatas(logId,startTime,endTime);
        List<Map<String, Object>> sigleOrderList = list.stream().sorted(Comparator.comparing((Map<String, Object> item) -> item.get("time").toString(), new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DateComputeUtils.formatDate(o1).before(DateComputeUtils.formatDate(o2)) ? -1 : 1;
            }
        }).thenComparingInt(item->Integer.parseInt(item.get("MsgID").toString()))).collect(Collectors.toList());
        return sigleOrderList;
    }

    @Override
    public List<Map<String, Object>> evtWindowData(long logId, String startTime, String endTime) {
        List<Map<String, Object>> list = influxService.evtDatas(logId, startTime, endTime);
        List<Map<String, Object>> evtOrderList = list.stream().sorted(Comparator.comparing((Map<String, Object> item) -> item.get("time").toString(), new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return DateComputeUtils.formatDate(o1).before(DateComputeUtils.formatDate(o2)) ? -1 : 1;
            }
        }).thenComparingInt(item->Integer.parseInt(item.get("MsgID").toString()))).collect(Collectors.toList());
        return evtOrderList;
    }

    @Override
    public List<Map<String, Object>> synOper(long logId, String time) {
        return influxService.syncIEWindow(logId,time);
    }



    @Override
    public List<PcapData> pcapDatas(long logId) {

        return logBackPlayDao.pcapDatas(logId);

    }

    @Override
    public List<PcapData> syncPcapDatas(long logId, String time) {
        try {
            return logBackPlayDao.syncPcapDatas(logId,time);
        } catch (ParseException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<PcapData> morePcapDatas(long logId, long beginId, String direction) {
        return logBackPlayDao.morePcapDatas(logId,beginId,direction);
    }

    @Override
    public Long maxId(long logId) {
        return logBackPlayDao.maxId(logId);
    }

    @Override
    public Long minId(long logId) {
        return logBackPlayDao.minId(logId);
    }
}
