package com.datang.service.RailWayStation;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.railway.SubwayXmlTablePojo;
import com.datang.domain.railway.TrainXmlTablePojo;

import java.io.File;
import java.util.List;

public interface SubwayLineService {

    public Long importSubwayTrail(File importFile,String filename);

    public AbstractPageList doPageQuery(PageList pageList);

    List<SubwayXmlTablePojo> getSubwayXmlFile(String trainXmlIds);

    void deleteSubwayXml(Long id);

    List<SubwayXmlTablePojo> findSubwayXml(PageList pageList);
}
