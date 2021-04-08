package com.datang.service.platform.stationParam;

import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationProspectParamPojo;

/**
 * 单站参数Service
 * @author maxuancheng
 *
 */
public interface StationProspectParamService {

	/**
	 * 保存参数
	 * @author lucheng
	 * date:2020年7月8日 下午3:16:45
	 * @param stationParamPojo
	 */
	public void save(StationProspectParamPojo stationProspectParamPojo);

	/**
	 * 修改数据
	* @author lucheng
	 * date:2020年7月8日 下午3:16:45
	 * @param stationParamPojo
	 */
	public void update(StationProspectParamPojo stationProspectParamPojo);

	/**
	 * 根据id查询数据
	* @author lucheng
	 * date:2020年7月8日 下午3:16:45
	 * @param id
	 * @return
	 */
	public StationProspectParamPojo find(Long id);

	
	/**
	 * 通过基站名称获取基站勘察表数据
	 * @author lucheng
	 * date:2020年6月29日
	 * @param siteName
	 * @return
	 */
	public StationProspectParamPojo findBySiteName(String siteName);

}
