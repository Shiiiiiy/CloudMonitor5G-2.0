package com.datang.service.testLogItem;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.stationTest.EceptionCellLogPojo;
import com.datang.domain.testLogItem.StationVerificationLogPojo;

/**
 * 单站验证日志service
 * @author maxuancheng
 *
 */
public interface StationVerificationTestService {

	public AbstractPageList doPageQuery(PageList pageList);

	/**
	 * 通过id查询数据
	 * @author maxuancheng
	 * date:2020年2月18日 下午1:38:07
	 * @param id
	 * @return
	 */
	public StationVerificationLogPojo find(Long id);

	/**
	 * 修改数据
	 * @author maxuancheng
	 * date:2020年2月18日 下午1:44:50
	 * @param svl
	 */
	public void update(StationVerificationLogPojo svl);

	/**
	 * 根据id删除数据
	 * @author maxuancheng
	 * date:2020年2月18日 下午2:08:22
	 * @param id
	 */
	public void delete(String idStr);

	/**
	 * 根据相关小区查找测试业务为绕点的数据
	 * @author maxuancheng
	 * date:2020年2月26日 上午9:12:16
	 * @param cellName
	 * @return
	 */
	public List<StationVerificationLogPojo> findByCorrelativeCell(String cellName,String event,String wireStatus);

	/**
	 * 通过文件名获取指标祥表数据
	 * @author maxuancheng
	 * date:2020年3月27日 上午10:29:27
	 * @param fileName
	 * @return
	 */
	public List<EceptionCellLogPojo> findEceptionCellLogByFileName(String fileName);

	/**
	 * 修改指标性表
	 * @author maxuancheng
	 * date:2020年3月27日 上午11:21:52
	 * @param ecp
	 */
	public void updateEcp(EceptionCellLogPojo ecp);
	
	/**
	 * 根据boxId查询数据
	 * @param boxId
	 * @return
	 */
	public List<StationVerificationLogPojo> findByBoxid(String boxId);
	

	/**
	 * 根据日志名称和boxid查询数据
	 * @author lucheng
	 * @date 2020年8月25日 下午8:15:05
	 * @param boxId
	 * @param logNames
	 * @return
	 */
	public List<StationVerificationLogPojo> findOfBoxidLogName(String boxId,List<String> logNames);

}
