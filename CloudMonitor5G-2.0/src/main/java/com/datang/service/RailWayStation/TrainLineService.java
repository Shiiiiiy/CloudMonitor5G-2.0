package com.datang.service.RailWayStation;

import com.datang.bean.railway.Line;
import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.railway.TrainXmlTablePojo;
import com.datang.domain.report.StatisticeTask;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface TrainLineService {

    public Long importRailwayTrail(File importFile);

    public AbstractPageList doPageQuery(PageList pageList);

    public List<TrainXmlTablePojo> getTrainXmlFile(String trainXmlIds);

    public void deleteTrainXml(Long id);

    public List<Map<String, String>> queryTrainList(String startStationName, String endStationName, String queryDateTime) throws Exception;

    List<TrainXmlTablePojo> findTrainXml(PageList pageList);

    void manualAddTrainXml(Line line);
}
