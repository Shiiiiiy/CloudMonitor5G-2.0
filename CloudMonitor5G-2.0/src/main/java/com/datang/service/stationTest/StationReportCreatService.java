package com.datang.service.stationTest;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;
import com.datang.domain.platform.stationParam.StationBasicParamPojo;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.stationTest.StationCellParamCensusPojo;
import com.datang.domain.stationTest.StationEtgTralPojo;
import com.datang.domain.stationTest.StationNetoptReceiveTestPojo;
import com.datang.domain.stationTest.StationPerformanceReceivePojo;
import com.datang.domain.stationTest.StationSAMTralPojo;
import com.datang.domain.testLogItem.TestLogEtgTrailKpiPojo;

/**
 * 单验报告生成Service
 * @author maxuancheng
 *
 */
public interface StationReportCreatService {

	/**
	 * 分页查询
	 * @author maxuancheng
	 * date:2020年2月19日 上午9:50:21
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList);

	/**
	 * 查询数据
	 * @author maxuancheng
	 * date:2020年2月19日 下午6:46:22
	 * @param valueOf
	 * @return
	 */
	public PlanParamPojo find(Long valueOf);
	
	/**
	 * 查询数据 去除多线程下懒加载无法加载数据问题
	 * @author lucheng
	 * @date 2020年8月26日 下午2:44:51
	 * @param valueOf
	 * @return
	 */
	public PlanParamPojo find1(Long valueOf);

	/**
	 * 修改数据
	 * @author maxuancheng
	 * date:2020年2月19日 下午6:54:31
	 * @param plan
	 */
	public void update(PlanParamPojo plan);

	/**
	 * 根据cellName获取单验日志详表数据
	 * @author maxuancheng
	 * date:2020年2月21日 下午2:37:53
	 * @param cellName
	 * @return
	 */
	public List<EceptionCellLogPojo> getExceptionCellLogOfCellName(String cellName);

	/**
	 * 插入单站小区参数统计数据
	 * @author maxuancheng
	 * date:2020年2月21日 下午4:49:58
	 * @param cellParamCensus
	 */
	public void createExceptionCellParam(StationCellParamCensusPojo cellParamCensus);

	/**
	 * 根据id删除三个报表数据
	 * @author maxuancheng
	 * date:2020年2月21日 下午6:58:18
	 * @param idLong
	 */
	public void deleteByids(List<Long> idLong);

	/**
	 * 插入单站网优验收测试表
	 * @author maxuancheng
	 * date:2020年2月22日 下午4:05:34
	 * @param netoptReceiveTest
	 */
	public void createNetoptReceiveTest(StationNetoptReceiveTestPojo netoptReceiveTest);

	/**
	 * 插入单站性能验收测试数据
	 * @author maxuancheng
	 * date:2020年2月23日 下午12:43:11
	 * @param performanceReceive
	 */
	public void createPerformanceReceive(StationPerformanceReceivePojo performanceReceive);
	
	/**
	 * 插入站点规划参数和部分基站工程参数
	 * @author lucheng
	 * @param stationBasicParamPojo
	 */
	public void createStationBasicParamTest(StationBasicParamPojo stationBasicParamPojo);

	/**
	 * 根据id查询数据
	 * @author maxuancheng
	 * date:2020年2月24日 下午2:10:54
	 * @param idList
	 * @return
	 */
	public List<PlanParamPojo> findByIds(List<Long> idList);
	
	/**
	 * 根据id查询数据
	 * @author luchneg
	 * date:2020年8月24日 下午2:10:54
	 * @param idList
	 * @return
	 */
	public List<PlanParamPojo> findByIds2(List<Long> idList);

	/**
	 * 查询所有数据
	 * @author maxuancheng
	 * date:2020年2月25日 下午4:41:34
	 * @return
	 */
	public List<PlanParamPojo> findAll();

	/**
	 * 通过日志名称和pci值获取轨迹指标数据
	 * @author maxuancheng
	 * date:2020年2月26日 上午9:37:41
	 * @param fileName
	 * @return
	 */
	public List<StationEtgTralPojo> findTralById(String fileName,String pci);
	
	/**
	 * 报告生成通过日志名称和pci值和事件的名称获取详细轨迹指标数据（详表）
	 * @author lucheng
	 * @date 2020年10月15日 上午11:04:03
	 * @param fileName
	 * @param pci
	 * @param event 事件名称
	 * @return
	 */
	public List<StationEtgTralPojo> findEtgTralByReportId(String fileName,String pci,String event);
	
	/**
	 * 报告生成通过日志名称和pci值和事件的名称获取抽样轨迹指标数据（抽样表）
	 * @author lucheng
	 * @date 2020年10月15日 上午11:04:03
	 * @param fileName
	 * @param pci
	 * @param event 事件名称
	 * @return
	 */
	public List<StationSAMTralPojo> findSamTralByReportId(String fileName,String pci,String event);
	
	/**
	 * 报告生成通过日志名称和pci值和事件的名称获取最大的事件轨迹指标数据
	 * @author lucheng
	 * @date 2020年12月17日 下午2:46:01
	 * @param fileName
	 * @param pci
	 * @param event
	 * @return
	 */
	public StationEtgTralPojo findMaxTralByReportId(String fileName,String event);
	
	/**
	 * 报告生成通过日志名称获取最大的时间或最小的时间
	 * @author lucheng
	 * @date 2020年12月17日 下午2:46:01
	 * @param fileName
	 * @param type 0：求最大时间 1：求最小时间
	 * @return
	 */
	public StationEtgTralPojo findMaxTralTimeByReportName(String fileName,String type);
	
	/**
	 * 通过日志名称、pci、事件名称以及最大值去轨迹表中查询最大值前后指定时间范围长度的数据
	 * @author lucheng
	 * @date 2020年12月17日 下午3:04:45
	 * @param fileName
	 * @param pci
	 * @param event
	 * @param findTralByMaxTime 最大事件的时间
	 * @param timeLength 查询的时间范围长度
	 * @return
	 */
	public List<StationEtgTralPojo> findTralByMaxTime(String fileName,String event,String findTralByMaxTime,Long timeLength);
	
	/**
	 * 根据enbId查询对应的localCellId
	 * @param enbId
	 * @return
	 */
	public List<PlanParamPojo> getAllLocalCellId(String siteName);
	
	/**
	 * 获取同站下其余localcellid的网优验收测试表
	 * @param planParamPojo
	 * @return
	 */
	public List<StationNetoptReceiveTestPojo> getNetoptReceiveTest(String siteName);
	
	/**
	 * 获取同站下其余localcellid的性能验收测试表
	 * @param planParamPojo
	 * @return
	 */
	public List<StationPerformanceReceivePojo> getPerformanceReceiveList(String siteName);
	
	/**
	 * 获取同站下其余localcellid的基站工程参数表
	 * @param planParamPojo
	 * @return
	 */
	public List<StationBasicParamPojo> getStationBasicParamList(String siteName);
	
	/**
	 * 修改网优验收测试表数据
	 * @author lucheng
	 * date:2020年6月29日 下午6:54:31
	 * @param pojo
	 */
	public void updateNetoptReceiveTest(StationNetoptReceiveTestPojo pojo);
	
	/**
	 * 修改性能测试表数据
	 * @author lucheng
	 * date:2020年6月29日 下午6:54:31
	 * @param pojo
	 */
	public void updatePerformanceReceive(StationPerformanceReceivePojo pojo);
	
	/**
	 * 修改基站工程表数据
	 * @author lucheng
	 * date:2020年6月29日 下午6:54:31
	 * @param pojo
	 */
	public void updateStationBasicParamTest(StationBasicParamPojo pojo);
	
	/**
	 * 根据日志名称集合和小区名称得到单验日志详表数据
	 * @author lucheng
	 * @date 2020年8月25日 上午11:28:34
	 * @param cellName
	 * @return
	 */
	public List<EceptionCellLogPojo> getExceptionCellLogOfCellLog(String cellName,List<String> logNameList,String testService,String wireStatus);
	
	/**
	 * 根据参数查询轨迹点对应所有指标
	 * @author lucheng
	 * @date 2020年9月9日 下午8:04:30
	 * @param pageList
	 * @return
	 */
	public List<TestLogEtgTrailKpiPojo> findTrailKpiByParam(PageList pageList);

}
