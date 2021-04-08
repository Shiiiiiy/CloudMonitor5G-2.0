package com.datang.service.service5g.embbCover;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.stationTest.StationEtgTralPojo;

/**
 * embb覆盖专题分析service
 * @author maxuancheng
 *
 */
public interface EmbbCoverAnalyseService {

	public AbstractPageList doPageQuery(PageList pageList);
	
	/**
	 * 查询工参参数
	 * @param cellName
	 * @param Integer
	 * @return
	 */
	public Map<String,Object> getParam(String cellName,Integer netType);
	
	/**
	 *  根据日志名和pci查询轨迹点
	 * @param logName
	 * @param pci
	 * @param netType
	 * @return
	 */
	public List<StationEtgTralPojo> getGpsPointData(List<String> logName,Long pci,Long frequency1,Integer netType);

}
