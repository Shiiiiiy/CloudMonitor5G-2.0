package com.datang.service.stationTest;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.stationTest.StationReportExcelPojo;


/**
 * 单眼报告进度查看Service
 * @author lucheng
 *
 */
public interface StationReportExcelService {
	
	/**
	 * 保存参数
	 * @author lucheng
	 * date:2020年11月20日 下午3:16:45
	 * @param stationParamPojo
	 */
	public void save(StationReportExcelPojo stationReportExcelPojo);

	/**
	 * 修改数据
	* @author lucheng
	 * date:2020年11月20日 下午3:16:45
	 * @param stationParamPojo
	 */
	public void update(StationReportExcelPojo stationReportExcelPojo);

	/**
	 * 根据id查询数据
	* @author lucheng
	 * date:2020年11月20日 下午3:16:45
	 * @param id
	 * @return
	 */
	public StationReportExcelPojo find(Long id);
	
	/**
	 * 根据id删除报告
	 * @author lucheng
	 * @date 2020年11月20日 下午6:47:54
	 * @param idList
	 * @return
	 */
	public void delete(List<Long> idList);
	
	
	/**
	 * 分页查询
	 * @author lucheng
	 * date:2020年11月20日 上午9:50:21
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList);
	
	/**
	 * 根据条件查询报告
	 * @author lucheng
	 * @date 2020年11月25日 上午10:49:57
	 * @param pageList
	 * @return
	 */
	public List<StationReportExcelPojo> findByParam(PageList pageList);

}
