package com.datang.service.oppositeOpen;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dPerformanceReceivePojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dResultPojo;
import com.datang.domain.oppositeOpen3d.OppositeOpen3dWirelessPojo;
import com.datang.domain.planParam.PlanParamPojo;
import com.datang.domain.platform.projectParam.Plan4GParam;

/**
 * 反开3d报告生成Service
 * @author maxuancheng
 *
 */
public interface OppositeOpen3dReportCreateService {

	/**
	 * 分页查询
	 * @author maxuancheng
	 * date:2020年3月10日 下午4:22:07
	 * @param pageList
	 * @return
	 */
	public AbstractPageList doPageQuery(PageList pageList);

	/**
	 * 根据id查询工参数据
	 * @author maxuancheng
	 * date:2020年3月12日 上午10:51:51
	 * @param id
	 * @return
	 */
	public Plan4GParam find(Long id);
	
	/**
	 * 根据id查询工参数据
	 * @author lucheng
	 * date:2020年8月26日 上午10:51:51
	 * @param id
	 * @return
	 */
	public Plan4GParam find2(Long id);

	/**
	 * 修改数据
	 * @author maxuancheng
	 * date:2020年3月12日 上午10:59:06
	 * @param plan
	 */
	public void update(Plan4GParam plan);

	/**
	 * 根据id删除报告
	 * @author maxuancheng
	 * date:2020年3月12日 上午10:59:53
	 * @param idLong
	 */
	public void deleteReportByids(List<Long> idLong);

	/**
	 * 新增小区无线参数数据
	 * @author maxuancheng
	 * date:2020年3月13日 下午4:29:49
	 * @param oow
	 */
	public void saveWireless(OppositeOpen3dWirelessPojo oow);
	
	/**
	 * 根据id查询数据
	 * @author maxuancheng
	 * date:2020年2月24日 下午2:10:54
	 * @param idList
	 * @return
	 */
	public List<Plan4GParam> findByIds(List<Long> idList);
	
	/**
	 * 根据id查询数据
	 * @author lucheng
	 * date:2020年8月24日 下午2:10:54
	 * @param idList
	 * @return
	 */
	public List<Plan4GParam> findByIds2(List<Long> idList);
	
	/**
	 * 根据siteName查询数据
	 * @author maxuancheng
	 * date:2020年3月16日 上午11:04:49
	 * @param siteName
	 * @return
	 */
	public OppositeOpen3dWirelessPojo findBySiteName(String siteName);

	/**
	 * 修改数据
	 * @author maxuancheng
	 * date:2020年3月16日 上午11:25:39
	 * @param oow
	 */
	public void update(OppositeOpen3dWirelessPojo oow);
	
	/**
	 * 保存性能验收报告
	 * @author lucheng
	 * date:2020年3月19日 上午11:25:39
	 * @param oow
	 */
	public void savePerformanceReceivePojo(OppositeOpen3dPerformanceReceivePojo oop);
	
	/**
	 * 更新性能验收报告
	 * @author lucheng
	 * date:2020年3月19日 上午11:25:39
	 * @param oow
	 */
	public void updatePerformanceReceivePojo(OppositeOpen3dPerformanceReceivePojo oop);

	/**
	 *创建小区无线参数数据
	 * @author maxuancheng
	 * date:2020年3月25日 上午11:37:30
	 * @param oow
	 */
	public void creteOOW(OppositeOpen3dWirelessPojo oow);

	/**
	 * 修改小区无线参数数据
	 * @author maxuancheng
	 * date:2020年3月25日 下午1:27:01
	 * @param oow
	 */
	public void updateOOW(OppositeOpen3dWirelessPojo oow);

	/**
	 * 创建小区结果表
	 * @author maxuancheng
	 * date:2020年3月25日 下午1:27:36
	 * @param oor
	 */
	public void creatOOR(OppositeOpen3dResultPojo oor);

	/**
	 * 修改小区结果表
	 * @author maxuancheng
	 * date:2020年3月25日 下午1:28:46
	 * @param oor
	 */
	public void updateOOR(OppositeOpen3dResultPojo oor);

	/**
	 * 查询所有数据
	 * @author maxuancheng
	 * date:2020年4月9日 下午4:41:01
	 * @return
	 */
	public List<Plan4GParam> findAll();
	
	/**
	 * 根据enbId查询对应的localCellId
	 * @param enbId
	 * @return
	 */
	public List<Plan4GParam> getAllLocalCellId(String enbId);
	

	/**
	 * 根据站名查询对应的4g工参
	 * @author lucheng
	 * @date 2020年8月20日 下午8:57:12
	 * @param siteName
	 * @return
	 */
	public List<Plan4GParam> getAllBySitename(String siteName);
	

}
