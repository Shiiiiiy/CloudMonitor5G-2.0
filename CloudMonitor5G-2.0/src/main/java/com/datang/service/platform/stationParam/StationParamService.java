package com.datang.service.platform.stationParam;

import java.util.List;
import java.util.Map;

import com.datang.common.action.page.PageList;
import com.datang.domain.platform.stationParam.StationParamPojo;
import com.datang.domain.platform.stationParam.StationReportTemplatePojo;

/**
 * 单站参数Service
 * @author maxuancheng
 *
 */
public interface StationParamService {

	/**
	 * 保存参数
	 * @author maxuancheng
	 * date:2020年2月14日 下午3:16:45
	 * @param stationParamPojo
	 */
	public void save(StationParamPojo stationParamPojo);

	/**
	 * 修改数据
	 * @author maxuancheng
	 * date:2020年2月14日 下午3:27:23
	 * @param stationParamPojo
	 */
	public void update(StationParamPojo stationParamPojo);

	/**
	 * 根据id查询数据
	 * @author maxuancheng
	 * date:2020年2月14日 下午3:27:38
	 * @param id
	 * @return
	 */
	public StationParamPojo find(Long id);

	/**
	 * 通过meauId获取数据
	 * @author maxuancheng
	 * date:2020年2月14日 下午5:18:53
	 * @param cityId
	 * @return
	 */
	public StationParamPojo findByMenuId(Long cityId);
	
	/**
	 * 获取区域模板对应的数据
	 * @author lucheng
	 * @date 2021年1月18日 上午10:39:08
	 * @param templteType 模板类型 0：单站 1：反开3d
	 * @param templteValue 值 区域模板的值
	 * @return
	 */
	public StationReportTemplatePojo findTemplateParam(Integer templteType,Long templteValue);

	/**
	 * 根据指定的参数获取区域模板参数
	 * @author lucheng
	 * @date 2021年1月18日 下午1:51:29
	 * @param param
	 * @return
	 */
	public List<StationReportTemplatePojo> findTemplateByParam(Map<String,Object> param);

}
