package com.datang.timeTask;

import com.datang.service.RailWayStation.TrainUpdateSchduleService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 10:01
 * @Version 1.0
 **/
@Slf4j
public class RailwaySchdule {

    @Autowired
    private TrainUpdateSchduleService trainUpdateSchduleService;

    /*定时任务会触发的方法*/
    public void syncEverydayTrainList() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        log.info("获取高铁信息quartz定时任务开始！");
        try {
            log.info(dateFormat.format(new Date())+" - 执行全路客运车站信息数据 - start");
            trainUpdateSchduleService.analysisStationInfo();
            log.info(dateFormat.format(new Date())+" - 执行车次列表数据更新 - start");
            //1、同步今日车次
            trainUpdateSchduleService.syncTrainListByStation();
            //2、更新完，同步车次时刻信息
            log.info(dateFormat.format(new Date())+" - 执行同步车次时刻信息更新 - start");
            trainUpdateSchduleService.autoSyncTrainFahrplan();
            log.info("执行所有车次列表数据更新结束 - end");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取高铁信息quartz定时任务失败！");
        }
        log.info(dateFormat.format(new Date())+" - 获取高铁信息quartz定时任务结束！");
    }

}
