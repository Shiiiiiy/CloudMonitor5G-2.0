/**
 * 
 */
package com.datang.service.testLogItem.impl;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.common.action.page.AbstractPageList;
import com.datang.common.action.page.PageList;
import com.datang.dao.testLogItem.NetworkTestLogItemDao;
import com.datang.domain.testLogItem.NetworkTestLogItem;
import com.datang.service.testLogItem.INetworkTestLogItemService;

/**
 * 网络测日志Service----实现
 * 
 * @author yinzhipeng
 * @date:2017年2月6日 下午1:36:52
 * @version
 */
@Service
@Transactional
@SuppressWarnings("all")
public class NetworkTestLogItemServiceImpl implements
		INetworkTestLogItemService {

	@Autowired
	private NetworkTestLogItemDao testLogItemDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.INetworkTestLogItemService#pageList(com
	 * .datang.common.action.page.PageList)
	 */
	@Override
	public AbstractPageList pageList(PageList pageList) {
		return testLogItemDao.getPageTestLogItem(pageList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.datang.service.testLogItem.INetworkTestLogItemService#
	 * addNetworkTestLogItem(com.datang.domain.testLogItem.NetworkTestLogItem)
	 */
	@Override
	public void addNetworkTestLogItem(NetworkTestLogItem networkTestLogItem) {
		testLogItemDao.create(networkTestLogItem);
		// System.out.println("这是ID" + networkTestLogItem.getRecSeqNo());
		ServletContext context = ServletActionContext.getRequest().getSession()
				.getServletContext();
		Object attribute = context.getAttribute("importpcapIds");
		if (attribute != null) {
			String string = String.valueOf(attribute);
			context.setAttribute("importpcapIds", string + ","
					+ networkTestLogItem.getRecSeqNo());
		} else {
			context.setAttribute("importpcapIds",
					networkTestLogItem.getRecSeqNo());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.datang.service.testLogItem.INetworkTestLogItemService#findByFileName
	 * (java.lang.String)
	 */
	@Override
	public NetworkTestLogItem findByFileName(String fileName) {
		return testLogItemDao.findByName(fileName);
	}
	
	@Override
	public List<NetworkTestLogItem> findByParam(PageList pageList){
		return testLogItemDao.findByParam(pageList);
	}
	
	@Override
	public NetworkTestLogItem find(Long id){
		return testLogItemDao.find(id);
	}
	
	@Override
	public void delete(List<Long> idList){
		for (Long id : idList) {
			testLogItemDao.delete(id);
		}
	}

}
