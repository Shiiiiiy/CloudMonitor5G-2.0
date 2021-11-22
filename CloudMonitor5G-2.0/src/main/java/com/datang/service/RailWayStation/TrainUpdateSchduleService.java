package com.datang.service.RailWayStation;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 10:14
 * @Version 1.0
 **/
public interface TrainUpdateSchduleService {


    void analysisStationInfo() throws Exception;

    void syncTrainListByStation();

    void autoSyncTrainFahrplan();
}
