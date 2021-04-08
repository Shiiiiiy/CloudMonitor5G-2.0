package com.datang.service.service5g.gis5g;

import java.util.List;

import com.datang.web.beans.gisSql.TestLogItemIndexGpsPoint;

public interface QualityBadGpsPointService {

	List<TestLogItemIndexGpsPoint> getPointsByTestLogItem(String testLogItemIds, Integer coverType, Integer indexType);

}
