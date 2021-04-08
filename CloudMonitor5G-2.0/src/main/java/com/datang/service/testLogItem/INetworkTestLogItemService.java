/**
 * 
 */
package com.datang.service.testLogItem;

import java.util.List;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.domain.testLogItem.NetworkTestLogItem;

/**
 * 网络测日志Service----接口
 * 
 * @author yinzhipeng
 * @date:2017年2月6日 下午1:28:47
 * @version
 */
public interface INetworkTestLogItemService {

	/**
	 * 多条件分页查询
	 * 
	 * @param pageList
	 * @return
	 */
	public AbstractPageList pageList(PageList pageList);

	/**
	 * 添加一条网络侧日志记录
	 * 
	 * @param networkTestLogItem
	 */
	public void addNetworkTestLogItem(NetworkTestLogItem networkTestLogItem);

	/**
	 * 根据文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public NetworkTestLogItem findByFileName(String fileName);
	
	/**
	 * 根据参数
	 * 
	 * @param fileName
	 * @return
	 */
	public List<NetworkTestLogItem> findByParam(PageList pageList);
	
	/**
	 * 根据id查询数据
	 * @author lucheng
	 * @date 2020年12月5日 下午4:42:41
	 * @param id
	 * @return
	 */
	public NetworkTestLogItem find(Long id);
	
	/**
	 * 根据id删除数据
	 * @author lucheng
	 * @date 2020年12月5日 下午4:47:54
	 * @param idList
	 */
	public void delete(List<Long> idList);

}
