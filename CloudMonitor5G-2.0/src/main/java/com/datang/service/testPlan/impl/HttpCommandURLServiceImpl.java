package com.datang.service.testPlan.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datang.dao.testPlan.HttpCommandURLDao;
import com.datang.domain.testPlan.HttpCommandURL;
import com.datang.service.testPlan.HttpCommandURLService;

/**
 * 
 * @author yinzhipeng
 * 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class HttpCommandURLServiceImpl implements HttpCommandURLService {
	@Autowired
	private HttpCommandURLDao httpCommandURLDao;

	@Override
	public List<HttpCommandURL> queryCanAddMustURL(List<Integer> hasAddIds) {
		List<HttpCommandURL> findAllMustURL = httpCommandURLDao
				.findAllMustURL();
		if (hasAddIds != null && hasAddIds.size() != 0) {
			List<HttpCommandURL> hasAddList = new ArrayList<HttpCommandURL>();
			// 删除已经添加过了的查询所有
			for (HttpCommandURL httpCommandURL : findAllMustURL) {
				if (hasAddIds.contains(httpCommandURL.getId())) {
					hasAddList.add(httpCommandURL);
				}
			}
			findAllMustURL.removeAll(hasAddList);
		}
		return findAllMustURL;
	}

	@Override
	public List<HttpCommandURL> queryCanAddRandomURL(List<Integer> hasAddIds) {
		List<HttpCommandURL> findAllRandomURL = httpCommandURLDao
				.findAllRandomURL();
		if (hasAddIds != null && hasAddIds.size() != 0) {
			List<HttpCommandURL> hasAddList = new ArrayList<HttpCommandURL>();
			// 删除已经添加过了的查询所有
			for (HttpCommandURL httpCommandURL : findAllRandomURL) {
				if (hasAddIds.contains(httpCommandURL.getId())) {
					hasAddList.add(httpCommandURL);
				}
			}
			findAllRandomURL.removeAll(hasAddList);
		}
		return findAllRandomURL;
	}

	@Override
	public HttpCommandURL queryHttpCommandURL(String url, boolean isMustUrl) {
		if (isMustUrl) {
			return httpCommandURLDao.findMustURLByUrl(url);
		} else {
			return httpCommandURLDao.findRandomURLByUrl(url);
		}

	}

	@Override
	public HttpCommandURL queryHttpCommandURL(Integer id) {
		return this.httpCommandURLDao.find(id);
	}

}
