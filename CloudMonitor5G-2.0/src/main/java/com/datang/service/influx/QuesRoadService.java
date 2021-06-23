package com.datang.service.influx;

import java.util.List;
import java.util.Map;

public interface QuesRoadService extends InfluxService{
    Map<String, List<Map<String, Object>>> analysize(List<String> fileLogIds);
}
